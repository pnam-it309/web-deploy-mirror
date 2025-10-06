<template>
  <div>
    <!-- Header -->
    <div class="flex justify-between items-center mb-6">
      <h2 class="text-2xl font-semibold text-black">Quản lý Khách hàng</h2>
      <div class="flex space-x-3">
        <button
          @click="showImportDialog = true"
          class="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
        >
          <ArrowUpTrayIcon class="-ml-1 mr-2 h-5 w-5" />
          Nhập từ Excel
        </button>
        <router-link
          to="/admin/customers/new"
          class="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-green-600 hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500"
        >
          <PlusIcon class="-ml-1 mr-2 h-5 w-5" />
          Thêm khách hàng
        </router-link>
      </div>
    </div>

    <!-- Filters -->
    <div class="bg-white shadow rounded-lg p-4 mb-6">
      <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
        <div>
          <label for="search" class="block text-sm font-medium text-black">Tìm kiếm</label>
          <input
            type="text"
            v-model="filters.search"
            class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 sm:text-sm text-black"
            placeholder="Username, email, họ tên..."
          />
        </div>

        <div>
          <label for="status" class="block text-sm font-medium text-black">Trạng thái</label>
          <select
            id="status"
            v-model="filters.status"
            class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 sm:text-sm text-black"
          >
            <option value="">Tất cả</option>
            <option value="active">Hoạt động</option>
            <option value="inactive">Khoá</option>
          </select>
        </div>

        <div class="flex items-end">
          <button
            @click="resetFilters"
            class="inline-flex items-center px-4 py-2 border border-gray-300 shadow-sm text-sm font-medium rounded-md text-black bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
          >
            Đặt lại
          </button>
        </div>
      </div>
    </div>

    <!-- Customers Table -->
    <div class="bg-white shadow rounded-lg overflow-hidden">
      <div class="overflow-x-auto">
        <table class="min-w-full divide-y divide-gray-200">
          <thead class="bg-gray-50">
            <tr>
              <th class="px-6 py-3 text-left text-xs font-medium text-black uppercase tracking-wider">
                <input
                  type="checkbox"
                  :checked="selectedCustomers.length === paginatedCustomers.length && paginatedCustomers.length > 0"
                  @change="toggleSelectAll"
                  class="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                />
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-black uppercase tracking-wider">ID</th>
              <th class="px-6 py-3 text-left text-xs font-medium text-black uppercase tracking-wider">Username</th>
              <th class="px-6 py-3 text-left text-xs font-medium text-black uppercase tracking-wider">Email</th>
              <th class="px-6 py-3 text-left text-xs font-medium text-black uppercase tracking-wider">Họ tên</th>
              <th class="px-6 py-3 text-left text-xs font-medium text-black uppercase tracking-wider">SĐT</th>
              <th class="px-6 py-3 text-left text-xs font-medium text-black uppercase tracking-wider">Trạng thái</th>
              <th class="relative px-6 py-3"><span class="sr-only">Hành động</span></th>
            </tr>
          </thead>

          <tbody class="bg-white divide-y divide-gray-200">
            <tr
              v-for="customer in paginatedCustomers"
              :key="customer.id"
              :class="{ 'bg-blue-50': selectedCustomers.includes(customer.id) }"
            >
              <td class="px-6 py-4 whitespace-nowrap">
                <input
                  type="checkbox"
                  :value="customer.id"
                  v-model="selectedCustomers"
                  class="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                />
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-black">{{ customer.id }}</td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-black">{{ customer.username }}</td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-black">{{ customer.email }}</td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-black">{{ customer.full_name }}</td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-black">{{ customer.phone }}</td>
              <td class="px-6 py-4 whitespace-nowrap">
                <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full" :class="customer.is_active">
                  {{ customer.is_active ? 'Hoạt động' : 'Khoá' }}
                </span>
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                <div class="flex space-x-2 justify-end">
                  <button @click="editCustomer(customer.id)" class="text-blue-600 hover:text-blue-900" title="Chỉnh sửa">
                    <PencilSquareIcon class="h-5 w-5" />
                  </button>
                  <button @click="confirmDelete(customer.id)" class="text-red-600 hover:text-red-900" title="Xóa">
                    <TrashIcon class="h-5 w-5" />
                  </button>
                </div>
              </td>
            </tr>

            <tr v-if="filteredCustomers.length === 0">
              <td colspan="8" class="px-6 py-4 text-center text-sm text-black">
                Không tìm thấy khách hàng nào phù hợp
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- Pagination -->
      <div class="bg-white px-4 py-3 flex items-center justify-between border-t border-gray-200 sm:px-6">
        <div class="flex-1 flex justify-between sm:hidden">
          <button @click="prevPage" :disabled="currentPage === 1" class="relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-black bg-white hover:bg-gray-50">Trước</button>
          <button @click="nextPage" :disabled="currentPage >= totalPages" class="ml-3 relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-black bg-white hover:bg-gray-50">Tiếp</button>
        </div>

        <div class="hidden sm:flex-1 sm:flex sm:items-center sm:justify-between">
          <div>
            <p class="text-sm text-black">
              Hiển thị
              <span class="font-medium">{{ (currentPage - 1) * itemsPerPage + 1 }}</span>
              đến
              <span class="font-medium">{{ Math.min(currentPage * itemsPerPage, filteredCustomers.length) }}</span>
              trong tổng số
              <span class="font-medium">{{ filteredCustomers.length }}</span>
              kết quả
            </p>
          </div>

          <div>
            <nav class="relative z-0 inline-flex rounded-md shadow-sm -space-x-px" aria-label="Pagination">
              <button @click="prevPage" :disabled="currentPage === 1" :class="{ 'opacity-50 cursor-not-allowed': currentPage === 1 }" class="relative inline-flex items-center px-2 py-2 rounded-l-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50">
                <ChevronLeftIcon class="h-5 w-5" />
              </button>

              <button
                v-for="page in visiblePages"
                :key="page"
                @click="currentPage = page"
                :class="{
                  'z-10 bg-blue-50 border-blue-500 text-blue-600': currentPage === page,
                  'bg-white border-gray-300 text-black hover:bg-gray-50': currentPage !== page
                }"
                class="relative inline-flex items-center px-4 py-2 border text-sm font-medium"
              >
                {{ page }}
              </button>

              <button @click="nextPage" :disabled="currentPage >= totalPages" :class="{ 'opacity-50 cursor-not-allowed': currentPage >= totalPages }" class="relative inline-flex items-center px-2 py-2 rounded-r-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50">
                <ChevronRightIcon class="h-5 w-5" />
              </button>
            </nav>
          </div>
        </div>
      </div>
    </div>

    <!-- Bulk Actions -->
    <div v-if="selectedCustomers.length > 0" class="fixed bottom-4 right-4 bg-white shadow-lg rounded-lg p-4 border border-gray-200">
      <div class="flex items-center space-x-4">
        <span class="text-sm text-black">Đã chọn <span class="font-semibold">{{ selectedCustomers.length }}</span> khách hàng</span>
        <div class="flex space-x-2">
          <button @click="bulkUpdateStatus(true)" class="inline-flex items-center px-3 py-1.5 border border-transparent text-xs font-medium rounded shadow-sm text-white bg-green-600 hover:bg-green-700">Kích hoạt</button>
          <button @click="bulkUpdateStatus(false)" class="inline-flex items-center px-3 py-1.5 border border-transparent text-xs font-medium rounded shadow-sm text-white bg-yellow-600 hover:bg-yellow-700">Khoá</button>
          <button @click="confirmBulkDelete" class="inline-flex items-center px-3 py-1.5 border border-transparent text-xs font-medium rounded shadow-sm text-white bg-red-600 hover:bg-red-700">Xóa</button>
        </div>
      </div>
    </div>

    <!-- Customer create modal (route-based) -->
    <CustomerCreateModal
      v-if="showCreateModal"
      @close="closeCreateModal"
      @created="onCustomerCreated"
    />

    <!-- Confirmation dialog for delete -->
    <ConfirmationDialog
      :open="showDeleteDialog"
      title="Xác nhận xóa"
      message="Bạn có chắc chắn muốn xóa khách hàng đã chọn?"
      confirm-text="Xóa"
      cancel-text="Hủy"
      @confirm="deleteSelectedCustomers"
      @cancel="showDeleteDialog = false"
    />

    <!-- Simple toast notification (custom, no lib) -->
    <div v-if="toast.show" :class="['fixed top-6 right-6 px-4 py-2 rounded shadow-lg', toast.type === 'success' ? 'bg-green-600 text-white' : toast.type === 'error' ? 'bg-red-600 text-white' : 'bg-gray-800 text-white']">
      {{ toast.message }}
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import {
  PlusIcon,
  PencilSquareIcon,
  TrashIcon,
  ChevronLeftIcon,
  ChevronRightIcon,
  ArrowUpTrayIcon
} from '@heroicons/vue/24/outline';
import ConfirmationDialog from '@/components/common/ConfirmationDialog.vue';
import CustomerCreateModal from '@/pages/admin/manage_customer/CustomerCreateModal.vue';

// Router & route
const router = useRouter();
const route = useRoute();

// Mock customers (replace with API)
const customers = ref([
  {
    id: 1,
    username: 'khangng',
    password_hash: '', // BE will hash, front-end may send plain password in create
    email: 'khang@example.com',
    full_name: 'Phạm Thị Hằng',
    role_id: null,
    phone: '0987654321',
    address: 'Hà Nội',
    is_active: true,
    is_customer: true,
    last_login: null,
    created_at: new Date().toISOString(),
    updated_at: new Date().toISOString()
  },
  {
    id: 2,
    username: 'tranb',
    password_hash: '',
    email: 'tranb@example.com',
    full_name: 'Trần B',
    role_id: null,
    phone: '0912345678',
    address: 'Hồ Chí Minh',
    is_active: false,
    is_customer: true,
    last_login: null,
    created_at: new Date().toISOString(),
    updated_at: new Date().toISOString()
  }
]);

// Filters
const filters = ref({
  search: '',
  status: ''
});

// Selected for bulk actions
const selectedCustomers = ref<number[]>([]);
const showDeleteDialog = ref(false);
const showImportDialog = ref(false);

// sync modal open with route name
const showCreateModal = ref(false);
watch(
  () => route.name,
  (name) => {
    showCreateModal.value = name === 'admin-customers-new';
  },
  { immediate: true }
);

// Pagination
const itemsPerPage = 10;
const currentPage = ref(1);

const filteredCustomers = computed(() => {
  return customers.value.filter(c => {
    const matchesSearch = !filters.value.search ||
      c.username.toLowerCase().includes(filters.value.search.toLowerCase()) ||
      (c.email && c.email.toLowerCase().includes(filters.value.search.toLowerCase())) ||
      (c.full_name && c.full_name.toLowerCase().includes(filters.value.search.toLowerCase()));

    const matchesStatus = !filters.value.status || (filters.value.status === 'active' ? c.is_active : !c.is_active);

    return matchesSearch && matchesStatus;
  });
});

const totalPages = computed(() => Math.max(1, Math.ceil(filteredCustomers.value.length / itemsPerPage)));

const paginatedCustomers = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  return filteredCustomers.value.slice(start, start + itemsPerPage);
});

// visible pages logic (same style as product)
const visiblePages = computed(() => {
  const pages: (number | string)[] = [];
  const maxVisible = 5;
  const total = totalPages.value;

  if (total <= maxVisible) {
    for (let i = 1; i <= total; i++) pages.push(i);
    return pages;
  }

  let start = Math.max(1, currentPage.value - 2);
  let end = Math.min(total, start + maxVisible - 1);
  if (end - start + 1 < maxVisible) start = Math.max(1, end - maxVisible + 1);

  if (start > 1) {
    pages.push(1);
    if (start > 2) pages.push('...');
  }
  for (let i = start; i <= end; i++) pages.push(i);
  if (end < total) {
    if (end < total - 1) pages.push('...');
    pages.push(total);
  }
  return pages;
});

// toast
const toast = ref({ show: false, message: '', type: 'success' as 'success' | 'error' | 'info' });
let toastTimer: any = null;
const showToast = (message: string, type: 'success' | 'error' | 'info' = 'success', duration = 2500) => {
  toast.value = { show: true, message, type };
  clearTimeout(toastTimer);
  toastTimer = setTimeout(() => {
    toast.value.show = false;
  }, duration);
};

// helpers
const formatDate = (iso?: string) => {
  if (!iso) return '';
  return new Date(iso).toLocaleString();
};

const toggleSelectAll = (e: Event) => {
  const target = e.target as HTMLInputElement;
  if (target.checked) {
    selectedCustomers.value = paginatedCustomers.value.map(c => c.id);
  } else {
    selectedCustomers.value = [];
  }
};

const resetFilters = () => {
  filters.value = { search: '', status: '' };
  currentPage.value = 1;
};

const editCustomer = (id: number) => {
  // navigate to edit route (if exists)
  router.push(`/admin/customers/edit/${id}`);
};

const confirmDelete = (id: number) => {
  selectedCustomers.value = [id];
  showDeleteDialog.value = true;
};

const deleteSelectedCustomers = () => {
  customers.value = customers.value.filter(c => !selectedCustomers.value.includes(c.id));
  selectedCustomers.value = [];
  showDeleteDialog.value = false;
  showToast('Xóa khách hàng thành công', 'success');
};

const confirmBulkDelete = () => {
  if (selectedCustomers.value.length > 0) showDeleteDialog.value = true;
};

const toggleStatus = (id: number, isActive: boolean) => {
  const c = customers.value.find(x => x.id === id);
  if (c) {
    c.is_active = isActive;
    c.updated_at = new Date().toISOString();
  }
};

const bulkUpdateStatus = (active: boolean) => {
  customers.value = customers.value.map(c => selectedCustomers.value.includes(c.id) ? { ...c, is_active: active, updated_at: new Date().toISOString() } : c);
  selectedCustomers.value = [];
  showToast(active ? 'Kích hoạt thành công' : 'Khoá thành công', 'success');
};

// handle import placeholder
const handleCustomersImported = (imported: any[]) => {
  // merge (simple)
  imported.forEach(ic => {
    if (!ic.id) ic.id = (Math.max(0, ...customers.value.map(c => c.id)) || 0) + 1;
    ic.created_at = ic.created_at || new Date().toISOString();
    ic.updated_at = ic.updated_at || new Date().toISOString();
    customers.value.unshift(ic);
  });
  showImportDialog.value = false;
  showToast('Import thành công', 'success');
};

// modal route sync
watch(
  () => route.name,
  (name) => {
    showCreateModal.value = name === 'admin-customers-new';
  },
  { immediate: true }
);

// closing modal -> navigate back to list
const closeCreateModal = () => {
  router.push({ name: 'admin-customers' }).catch(() => router.back());
};

// when modal emits created -> add to list and close modal
const onCustomerCreated = (payload: any) => {
  if (!payload) {
    showToast('Dữ liệu không hợp lệ', 'error');
    return;
  }

  // ensure id auto-increment
  const maxId = customers.value.length ? Math.max(...customers.value.map(c => c.id)) : 0;
  if (!payload.id) payload.id = maxId + 1;

  // set created/updated timestamps (override if backend provides)
  const now = new Date().toISOString();
  payload.created_at = payload.created_at || now;
  payload.updated_at = payload.updated_at || now;

  // ensure booleans/flags
  if (payload.is_customer === undefined) payload.is_customer = true;
  if (payload.is_active === undefined) payload.is_active = true;

  // Add to top of list to show immediately
  customers.value.unshift(payload);

  // feedback + close modal
  showToast('Thêm khách hàng thành công', 'success');
  router.push({ name: 'admin-customers' }).catch(() => router.back());
};

// pagination helpers
const prevPage = () => { if (currentPage.value > 1) currentPage.value--; };
const nextPage = () => { if (currentPage.value < totalPages.value) currentPage.value++; };

// lifecycle
onMounted(() => {
  // fetch customers from API here if needed
});
</script>

<style scoped>
/* Ensure text is black for readability (main change) */
.text-black { color: #000 !important; }

/* small tweaks to make table text consistently black */
table td, table th {
  color: #000;
}

/* toast style already inline via classes - keep compact */
</style>
