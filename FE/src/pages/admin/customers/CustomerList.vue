<template>
  <div class="customer-list">
    <!-- Debug element - will be hidden but can be made visible for testing -->
    <div v-if="false" style="background: red; padding: 20px; color: white; position: fixed; top: 0; left: 0; z-index: 9999;">
      <h3>DEBUG: CustomerList Component</h3>
      <p>Loading: {{ loading }}</p>
      <p>Customers count: {{ customers.length }}</p>
      <p>Filtered count: {{ filteredCustomers.length }}</p>
    </div>

    <!-- Main content will go here -->
    <div v-if="loading">Đang tải dữ liệu...</div>
    <div v-else>
      <BreadcrumbDefault label="Quản lý khách hàng">
        <div class="flex flex-wrap gap-3 items-center mb-4">
          <div class="relative flex-1 max-w-md">
            <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
              <MagnifyingGlassIcon class="h-5 w-5 text-gray-400" />
            </div>
            <input
              v-model="filters.search"
              type="text"
              placeholder="Tìm theo tên, email, số điện thoại"
              class="block w-full pl-10 pr-3 py-2 border border-stroke rounded-md leading-5 bg-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
              @keyup.enter="applyFilters"
            />
          </div>
          <select
            v-model="filters.status"
            class="border border-stroke rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-500 text-sm"
          >
            <option value="">Tất cả trạng thái</option>
            <option value="active">Đang hoạt động</option>
            <option value="inactive">Ngừng hoạt động</option>
            <option value="banned">Đã khóa</option>
          </select>
          <button
            @click="applyFilters"
            class="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
          >
            <MagnifyingGlassIcon class="h-4 w-4 mr-2" />
            Tìm kiếm
          </button>
          <button
            @click="resetFilters"
            class="inline-flex items-center px-4 py-2 border border-gray-300 shadow-sm text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
          >
            <ArrowPathIcon class="h-4 w-4 mr-2" />
            Làm mới
          </button>
        </div>
        <div class="mt-2">
          <ButtonDefault 
            customClasses="bg-[#6c584c] hover:bg-[#5a483e] text-white px-4 py-2 rounded-md whitespace-nowrap"
            @click="goToCreate"
          >
            + Thêm khách hàng
          </ButtonDefault>
        </div>
        <DivCustom label="Danh sách khách hàng">

          <div v-if="customers.length === 0">Không có khách hàng nào</div>
          <div v-else>
            <TableCustom
              :data="paginatedCustomers"
              :columns="columns"
              :pageSize="pagination.pageSize"
              :total="pagination.total"
              @change="handleTableChange"
            >
              <template #default="{ item }: { item: Customer }">
                <div :class="columns[0].class">
                  <span class="font-mono text-sm">{{ formatAccountCode(item.id) }}</span>
                </div>
                <div :class="columns[1].class">{{ item.name }}</div>
                <div :class="columns[2].class">
                  <span class="truncate block max-w-[280px]" :title="item.email">{{ item.email }}</span>
                </div>
                <div :class="columns[3].class">
                  <span :class="`px-2 py-1 rounded-full text-xs font-medium ${getStatusClass(item.status)}`">{{ getStatusText(item.status) }}</span>
                </div>
                <div :class="columns[4].class" class="gap-2 flex justify-center">
                  <button
                    class="w-9 h-9 inline-flex items-center justify-center rounded-md bg-indigo-600 text-white hover:bg-indigo-500"
                    title="Xem chi tiết"
                    @click="editCustomer(item)"
                  >
                    <EyeIcon class="w-5 h-5" />
                  </button>
                  <button
                    class="w-9 h-9 inline-flex items-center justify-center rounded-md bg-gray-200 text-gray-700 hover:bg-gray-300"
                    :title="item.status === 'active' ? 'Chuyển sang ngừng hoạt động' : 'Chuyển sang hoạt động'"
                    @click="toggleStatus(item)"
                  >
                    <ArrowsRightLeftIcon class="w-5 h-5" />
                  </button>
                </div>
              </template>
            </TableCustom>
          </div>
        </DivCustom>
      </BreadcrumbDefault>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, reactive, computed, onMounted, watchEffect } from 'vue';
import { useRouter } from 'vue-router';
import { message } from 'ant-design-vue';
import BreadcrumbDefault from '@/components/custom/Div/BreadcrumbDefault.vue'
import DivCustom from '@/components/custom/Div/DivCustom.vue'
import TableCustom from '@/components/custom/Table/TableCustom.vue'
import ButtonDefault from '@/components/custom/Button/ButtonDefault.vue'
import { EyeIcon, ArrowsRightLeftIcon, MagnifyingGlassIcon, ArrowPathIcon } from '@heroicons/vue/24/outline'
import request from '@/services/request'

interface Customer {
  id: string;
  name: string;
  email: string;
  phone?: string;
  status: string;
  groups: Array<{ id: number; name: string }>;
  orders_count: number;
  orders_success_count: number;
  total_spent: number;
  avg_order_value: number;
  created_at: string;
  is_vip?: boolean;
  avatar?: string;
}

interface Filters {
  search: string;
  status: string;
  group: string;
  sort: string;
  page: number;
  perPage: number;
  date_range: string;
  min_orders: string;
  min_spent: string;
  start_date: string;
  end_date: string;
  order_count: string;
  total_spent: string;
  total_spent_filter: string;
}

interface PaginationState {
  current: number;
  pageSize: number;
  total: number;
  showSizeChanger: boolean;
  showQuickJumper: boolean;
  showTotal: (total: number, range: [number, number]) => string;
  currentPage: number;
  totalPages: number;
  from: number;
  to: number;
}

export default defineComponent({
  name: 'CustomerList',
  components: { 
    BreadcrumbDefault, 
    DivCustom, 
    TableCustom, 
    ButtonDefault, 
    EyeIcon, 
    ArrowsRightLeftIcon,
    MagnifyingGlassIcon,
    ArrowPathIcon
  },
  setup() {
    const router = useRouter();

    // State
    const loading = ref(false);
    const showDeleteModal = ref(false);
    const customerToDelete = ref<Customer | null>(null);
    const deleting = ref(false);
    const deleteOption = ref('soft');
    const showAdvancedFilters = ref(false);
    const selectedCustomers = ref<string[]>([]);
    const bulkAction = ref('');

    // Filters state
    const filters = reactive<Filters>({
      search: '',
      status: '',
      group: '',
      sort: 'newest',
      page: 1,
      perPage: 10,
      date_range: '',
      min_orders: '',
      min_spent: '',
      start_date: '',
      end_date: '',
      order_count: '',
      total_spent: '',
      total_spent_filter: 'at_least'
    });

    // Pagination
    const pagination = reactive<PaginationState>({
      current: 1,
      pageSize: 10,
      total: 0,
      showSizeChanger: true,
      showQuickJumper: true,
      showTotal: (total: number, [start, end]: [number, number]) =>
        `Hiển thị ${start}-${end} trong tổng số ${total} khách hàng`,
      get currentPage() { return this.current; },
      set currentPage(page: number) { this.current = page; },
      get totalPages() { return Math.ceil(this.total / this.pageSize); },
      get from() { return (this.current - 1) * this.pageSize + 1; },
      get to() {
        const to = this.current * this.pageSize;
        return to > this.total ? this.total : to;
      }
    });

    // Table columns configuration for TableCustom
    const columns = ref([
      { key: 'id', title: 'ID', class: 'col-span-1 flex items-center justify-center' },
      { key: 'name', title: 'Tên', class: 'col-span-3 flex items-center justify-start pl-2' },
      { key: 'email', title: 'Email', class: 'col-span-4 flex items-center justify-start pl-2' },
      { key: 'status', title: 'Trạng thái', class: 'col-span-2 flex items-center justify-center' },
      { key: 'actions', title: 'Hành động', class: 'col-span-2 flex items-center justify-center' },
    ])

    // Data
    const customers = ref<Customer[]>([]);
    const customerGroups = ref([
      { id: 1, name: 'Khách hàng thân thiết' },
      { id: 2, name: 'Khách hàng mới' },
      { id: 3, name: 'Khách hàng VIP' }
    ]);

    // Computed
    const allSelected = computed(() => {
      return selectedCustomers.value.length === filteredCustomers.value.length &&
             filteredCustomers.value.length > 0;
    });

    const filteredCustomers = computed(() => {
      return customers.value.filter(customer => {
        const searchTerm = filters.search.toLowerCase();
        const matchesSearch = !searchTerm ||
          customer.name.toLowerCase().includes(searchTerm) ||
          customer.email.toLowerCase().includes(searchTerm) ||
          (customer.phone && customer.phone.includes(searchTerm));

        const matchesStatus = !filters.status || customer.status === filters.status;

        // Add more filter conditions as needed

        return matchesSearch && matchesStatus;
      });
    });

    const paginatedCustomers = computed(() => {
      // Server returns the current page already
      return customers.value
    });

    // Format account code (first 6 characters of ID)
    const formatAccountCode = (id: string): string => {
      return id ? `KH${id.substring(0, 6).toUpperCase()}` : '';
    };

    // Methods
    const formatCurrency = (value: number): string => {
      return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
      }).format(value);
    };

    const formatDate = (dateString: string): string => {
      return new Date(dateString).toLocaleDateString('vi-VN');
    };

    const getStatusClass = (status: string): string => {
      switch (status) {
        case 'active': return 'bg-green-100 text-green-800';
        case 'inactive': return 'bg-yellow-100 text-yellow-800';
        case 'banned': return 'bg-red-100 text-red-800';
        default: return 'bg-gray-100 text-gray-800';
      }
    };

    const getStatusText = (status: string): string => {
      switch (status) {
        case 'active': return 'Đang hoạt động';
        case 'inactive': return 'Ngừng hoạt động';
        case 'banned': return 'Đã khóa';
        default: return 'Không xác định';
      }
    };

    const getGroupColor = (groupId: number): string => {
      const colors = [
        'bg-blue-100 text-blue-800',
        'bg-green-100 text-green-800',
        'bg-purple-100 text-purple-800',
        'bg-yellow-100 text-yellow-800',
        'bg-pink-100 text-pink-800'
      ];
      return colors[groupId % colors.length];
    };

    const toggleSelectAll = () => {
      if (allSelected.value) {
        selectedCustomers.value = [];
      } else {
        selectedCustomers.value = filteredCustomers.value.map(c => c.id);
      }
    };

    const applyBulkAction = () => {
      if (!bulkAction.value) return;

      switch (bulkAction.value) {
        case 'delete':
          showDeleteModal.value = true;
          break;
        // Add other bulk actions here
      }
    };

    const exportCustomers = () => {
      console.log('Exporting customers...');
    };

    const applyFilters = () => {
      pagination.current = 1; // Reset to first page
      fetchCustomers()
    };

    const resetFilters = () => {
      Object.assign(filters, {
        search: '',
        status: '',
        group: '',
        sort: 'newest',
        date_range: '',
        order_count: '',
        total_spent: '',
        start_date: '',
        end_date: ''
      });
      applyFilters();
    };

    const goToPage = (page: number) => {
      if (page >= 1 && page <= pagination.totalPages) {
        pagination.current = page;
      }
    };

    const previousPage = () => {
      if (pagination.current > 1) {
        pagination.current--;
      }
    };

    const nextPage = () => {
      if (pagination.current < pagination.totalPages) {
        pagination.current++;
      }
    };

    const handleTableChange = (page: number, pageSize?: number) => {
      pagination.current = page
      if (pageSize) pagination.pageSize = pageSize
      fetchCustomers()
    }

    const editCustomer = (customer: Customer) => {
      router.push({ name: 'admin-customers-detail', params: { id: customer.id } });
    };

    const goToCreate = () => {
      router.push({ name: 'admin-customers-new' })
    }

    const toggleStatus = async (customer: Customer) => {
      try {
        const res = await request.patch(`/admin/customers/${customer.id}/toggle-status`)
        const updated = res.data
        customer.status = (updated.status || '').toString().toLowerCase()
        message.success('Cập nhật trạng thái thành công')
      } catch (e) {
        message.error('Cập nhật trạng thái thất bại')
      }
    }

    const confirmDeleteCustomer = (customer: Customer) => {
      customerToDelete.value = customer;
      showDeleteModal.value = true;
    };

    const deleteCustomer = async () => {
      if (!customerToDelete.value) return;

      deleting.value = true;
      try {
        // Replace with actual API call
        console.log(`Deleting customer ${customerToDelete.value.id} with option: ${deleteOption.value}`);

        // Remove from local state
        const index = customers.value.findIndex(c => c.id === customerToDelete.value?.id);
        if (index !== -1) {
          customers.value.splice(index, 1);
        }

        message.success('Xóa khách hàng thành công');
        showDeleteModal.value = false;
      } catch (error) {
        console.error('Error deleting customer:', error);
        message.error('Có lỗi xảy ra khi xóa khách hàng');
      } finally {
        deleting.value = false;
      }
    };

    const fetchCustomers = async () => {
      loading.value = true
      try {
        const params: any = {
          search: filters.search || undefined,
          status: filters.status || undefined,
          page: pagination.current - 1, // Spring page index starts at 0
          size: pagination.pageSize,
        }
        const res = await request.get(`/admin/customers`, { params })
        const page = res.data
        customers.value = page.content || []
        pagination.total = page.totalElements || 0
      } catch (error) {
        console.error('fetchCustomers - Error:', error)
        message.error('Có lỗi xảy ra khi tải danh sách khách hàng')
      } finally {
        loading.value = false
      }
    }

    // Lifecycle hooks
    onMounted(() => {
      fetchCustomers()
    })

    // Watch for filter changes
    watchEffect(() => {
      // Whenever filters change, reload from server
      // Keep client-side filtered values but rely on server paging
      // Debounce could be added if needed
    })

    // Initial debug log
    console.log('Component setup completed', {
      loading: loading.value,
      customersCount: customers.value.length,
      showAdvancedFilters: showAdvancedFilters.value
    });

    return {
      loading,
      customers,
      filteredCustomers,
      paginatedCustomers,
      pagination,
      filters,
      columns,
      customerGroups,
      selectedCustomers,
      bulkAction,
      showAdvancedFilters,
      allSelected,
      showDeleteModal,
      customerToDelete,
      deleting,
      deleteOption,
      toggleSelectAll,
      handleTableChange,
      editCustomer,
      deleteCustomer: confirmDeleteCustomer,
      confirmDelete: deleteCustomer,
      toggleStatus,
      goToCreate,
      applyFilters,
      resetFilters,
      formatCurrency,
      formatDate,
      formatAccountCode,
      getStatusClass,
      getStatusText,
      getGroupColor
    };
  },
});
</script>
