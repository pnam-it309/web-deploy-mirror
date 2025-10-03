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
      <h1>Danh sách khách hàng</h1>
      <div v-if="customers.length === 0">Không có khách hàng nào</div>
      <div v-else>
        <!-- Simple table to display customers -->
        <table style="width: 100%; border-collapse: collapse;">
          <thead>
            <tr>
              <th style="border: 1px solid #ddd; padding: 8px;">ID</th>
              <th style="border: 1px solid #ddd; padding: 8px;">Tên</th>
              <th style="border: 1px solid #ddd; padding: 8px;">Email</th>
              <th style="border: 1px solid #ddd; padding: 8px;">Trạng thái</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="customer in paginatedCustomers" :key="customer.id">
              <td style="border: 1px solid #ddd; padding: 8px;">{{ customer.id }}</td>
              <td style="border: 1px solid #ddd; padding: 8px;">{{ customer.name }}</td>
              <td style="border: 1px solid #ddd; padding: 8px;">{{ customer.email }}</td>
              <td style="border: 1px solid #ddd; padding: 8px;">{{ getStatusText(customer.status) }}</td>
            </tr>
          </tbody>
        </table>
        
        <!-- Simple pagination -->
        <div style="margin-top: 20px;">
          <button @click="previousPage" :disabled="pagination.current <= 1">Trang trước</button>
          <span style="margin: 0 10px;">Trang {{ pagination.current }} / {{ pagination.totalPages }}</span>
          <button @click="nextPage" :disabled="pagination.current >= pagination.totalPages">Trang sau</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, reactive, computed, onMounted, watchEffect } from 'vue';
import { useRouter } from 'vue-router';
import { message } from 'ant-design-vue';

interface Customer {
  id: number;
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
  setup() {
    const router = useRouter();
    
    // State
    const loading = ref(false);
    const showDeleteModal = ref(false);
    const customerToDelete = ref<Customer | null>(null);
    const deleting = ref(false);
    const deleteOption = ref('soft');
    const showAdvancedFilters = ref(false);
    const selectedCustomers = ref<number[]>([]);
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

    // Sample data - replace with API call
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
      const start = (pagination.current - 1) * pagination.pageSize;
      const end = start + pagination.pageSize;
      return filteredCustomers.value.slice(start, end);
    });

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
      // Additional filter logic here
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

    const editCustomer = (customer: Customer) => {
      router.push({ name: 'admin-customer-edit', params: { id: customer.id } });
    };

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
      console.log('fetchCustomers - Starting to fetch customers...');
      loading.value = true;
      try {
        // Replace with actual API call
        // const response = await customerApi.getList(filters);
        // customers.value = response.data;
        // pagination.total = response.total;
        
        // Sample data
        customers.value = [
          {
            id: 1,
            name: 'Nguyễn Văn A',
            email: 'nguyenvana@example.com',
            phone: '0987654321',
            status: 'active',
            groups: [{ id: 1, name: 'Khách hàng thân thiết' }],
            orders_count: 12,
            orders_success_count: 10,
            total_spent: 15000000,
            avg_order_value: 1250000,
            created_at: '2023-01-15T00:00:00Z',
            is_vip: true
          }
          // Add more sample data as needed
        ];
        
        pagination.total = customers.value.length;
        console.log('fetchCustomers - Data loaded successfully', {
          customersCount: customers.value.length,
          firstCustomer: customers.value[0] || 'No customers'
        });
      } catch (error) {
        console.error('fetchCustomers - Error:', error);
        message.error('Có lỗi xảy ra khi tải danh sách khách hàng');
      } finally {
        loading.value = false;
        console.log('fetchCustomers - Loading completed');
      }
    };

    // Lifecycle hooks
    onMounted(() => {
      console.log('Component mounted, fetching customers...');
      fetchCustomers();
    });

    // Watch for filter changes
    watchEffect(() => {
      console.log('Filtered customers count changed:', filteredCustomers.value.length);
      pagination.total = filteredCustomers.value.length;
    });
    
    // Initial debug log
    console.log('Component setup completed', {
      loading: loading.value,
      customersCount: customers.value.length,
      showAdvancedFilters: showAdvancedFilters.value
    });

    return {
      // State
      loading,
      showDeleteModal,
      customerToDelete,
      deleting,
      deleteOption,
      showAdvancedFilters,
      selectedCustomers,
      bulkAction,
      filters,
      pagination,
      customers,
      customerGroups,
      
      // Computed
      allSelected,
      filteredCustomers,
      paginatedCustomers,
      
      // Methods
      applyFilters,
      resetFilters,
      exportCustomers,
      formatCurrency,
      formatDate,
      getStatusClass,
      getStatusText,
      getGroupColor,
      toggleSelectAll,
      applyBulkAction,
      confirmDeleteCustomer,
      deleteCustomer,
      goToPage,
      previousPage,
      nextPage,
      editCustomer,
      fetchCustomers
    };
  }
});
</script>