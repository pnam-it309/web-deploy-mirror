<template>
  <div>
    <div class="flex justify-between items-center mb-6">
      <h2 class="text-2xl font-semibold">Quản lý Sản phẩm</h2>
      <div class="flex space-x-3">
        <button
          @click="showImportDialog = true"
          class="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
        >
          <ArrowUpTrayIcon class="-ml-1 mr-2 h-5 w-5" />
          Nhập từ Excel
        </button>
        <router-link
          to="/admin/products/new"
          class="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-green-600 hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500"
        >
          <PlusIcon class="-ml-1 mr-2 h-5 w-5" />
          Thêm sản phẩm
        </router-link>
      </div>
    </div>

    <!-- Filters -->
    <div class="bg-white shadow rounded-lg p-4 mb-6">
      <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
        <div>
          <label for="search" class="block text-sm font-medium text-gray-700">Tìm kiếm</label>
          <input
            type="text"
            v-model="filters.search"
            class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 sm:text-sm"
            placeholder="Tên hoặc mã sản phẩm..."
          />
        </div>
        <div>
          <label for="category" class="block text-sm font-medium text-gray-700">Danh mục</label>
          <select
            id="category"
            v-model="filters.category"
            class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 sm:text-sm"
          >
            <option value="">Tất cả danh mục</option>
            <option v-for="category in categories" :key="category.id" :value="category.id">
              {{ category.name }}
            </option>
          </select>
        </div>
        <div>
          <label for="status" class="block text-sm font-medium text-gray-700">Trạng thái</label>
          <select
            id="status"
            v-model="filters.status"
            class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 sm:text-sm"
          >
            <option value="">Tất cả trạng thái</option>
            <option v-for="status in statuses" :key="status.id" :value="status.id">
              {{ status.name }}
            </option>
          </select>
        </div>
        <div class="flex items-end">
          <button
            @click="resetFilters"
            class="inline-flex items-center px-4 py-2 border border-gray-300 shadow-sm text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
          >
            <XMarkIcon class="-ml-1 mr-2 h-5 w-5 text-gray-500" />
            Đặt lại
          </button>
        </div>
      </div>
    </div>

    <!-- Products Table -->
    <div class="bg-white shadow rounded-lg overflow-hidden">
      <div class="overflow-x-auto">
        <table class="min-w-full divide-y divide-gray-200">
          <thead class="bg-gray-50">
            <tr>
              <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                <input
                  type="checkbox"
                  :checked="selectedProducts.length === filteredProducts.length"
                  @change="toggleSelectAll"
                  class="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                />
              </th>
              <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Sản phẩm
              </th>
              <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Mã SP
              </th>
              <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Giá
              </th>
              <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Tồn kho
              </th>
              <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Trạng thái
              </th>
              <th scope="col" class="relative px-6 py-3">
                <span class="sr-only">Hành động</span>
              </th>
            </tr>
          </thead>
          <tbody class="bg-white divide-y divide-gray-200">
            <tr v-for="product in paginatedProducts" :key="product.id" :class="{ 'bg-blue-50': selectedProducts.includes(product.id) }">
              <td class="px-6 py-4 whitespace-nowrap">
                <input
                  type="checkbox"
                  :value="product.id"
                  v-model="selectedProducts"
                  class="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                />
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <div class="flex items-center">
                  <div class="flex-shrink-0 h-10 w-10">
                    <img class="h-10 w-10 rounded-md" :src="product.image" :alt="product.name">
                  </div>
                  <div class="ml-4">
                    <div class="text-sm font-medium text-gray-900">
                      {{ product.name }}
                    </div>
                    <div class="text-sm text-gray-500">
                      {{ product.category }}
                    </div>
                  </div>
                </div>
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                {{ product.sku }}
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                {{ formatPrice(product.price) }}
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                {{ product.stock }} {{ product.unit }}
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full" :class="getStatusClass(product.status)">
                  {{ getStatusText(product.status) }}
                </span>
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                <div class="flex space-x-2 justify-end">
                  <button
                    @click="editProduct(product.id)"
                    class="text-blue-600 hover:text-blue-900"
                    title="Chỉnh sửa"
                  >
                    <PencilSquareIcon class="h-5 w-5" />
                  </button>
                  <button
                    @click="confirmDelete(product.id)"
                    class="text-red-600 hover:text-red-900"
                    title="Xóa"
                  >
                    <TrashIcon class="h-5 w-5" />
                  </button>
                  <button
                    @click="toggleStatus(product.id, product.status === 'active' ? 'inactive' : 'active')"
                    class="text-gray-600 hover:text-gray-900"
                    :title="product.status === 'active' ? 'Ẩn sản phẩm' : 'Hiện sản phẩm'"
                  >
                    <EyeIcon v-if="product.status === 'active'" class="h-5 w-5" />
                    <EyeSlashIcon v-else class="h-5 w-5" />
                  </button>
                </div>
              </td>
            </tr>
            <tr v-if="filteredProducts.length === 0">
              <td colspan="7" class="px-6 py-4 text-center text-sm text-gray-500">
                Không tìm thấy sản phẩm nào phù hợp
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- Pagination -->
      <div class="bg-white px-4 py-3 flex items-center justify-between border-t border-gray-200 sm:px-6">
        <div class="flex-1 flex justify-between sm:hidden">
          <button
            @click="currentPage--"
            :disabled="currentPage === 1"
            class="relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50"
          >
            Trước
          </button>
          <button
            @click="currentPage++"
            :disabled="currentPage >= totalPages"
            class="ml-3 relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50"
          >
            Tiếp
          </button>
        </div>
        <div class="hidden sm:flex-1 sm:flex sm:items-center sm:justify-between">
          <div>
            <p class="text-sm text-gray-700">
              Hiển thị
              <span class="font-medium">{{ (currentPage - 1) * itemsPerPage + 1 }}</span>
              đến
              <span class="font-medium">{{ Math.min(currentPage * itemsPerPage, filteredProducts.length) }}</span>
              trong tổng số
              <span class="font-medium">{{ filteredProducts.length }}</span>
              kết quả
            </p>
          </div>
          <div>
            <nav class="relative z-0 inline-flex rounded-md shadow-sm -space-x-px" aria-label="Pagination">
              <button
                @click="currentPage--"
                :disabled="currentPage === 1"
                :class="{ 'opacity-50 cursor-not-allowed': currentPage === 1 }"
                class="relative inline-flex items-center px-2 py-2 rounded-l-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50"
              >
                <span class="sr-only">Trước</span>
                <ChevronLeftIcon class="h-5 w-5" aria-hidden="true" />
              </button>
              <button
                v-for="page in visiblePages"
                :key="page"
                @click="currentPage = page"
                :class="{
                  'z-10 bg-blue-50 border-blue-500 text-blue-600': currentPage === page,
                  'bg-white border-gray-300 text-gray-500 hover:bg-gray-50': currentPage !== page
                }"
                class="relative inline-flex items-center px-4 py-2 border text-sm font-medium"
              >
                {{ page }}
              </button>
              <button
                @click="currentPage++"
                :disabled="currentPage >= totalPages"
                :class="{ 'opacity-50 cursor-not-allowed': currentPage >= totalPages }"
                class="relative inline-flex items-center px-2 py-2 rounded-r-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50"
              >
                <span class="sr-only">Tiếp</span>
                <ChevronRightIcon class="h-5 w-5" aria-hidden="true" />
              </button>
            </nav>
          </div>
        </div>
      </div>
    </div>

    <!-- Bulk Actions -->
    <div v-if="selectedProducts.length > 0" class="fixed bottom-4 right-4 bg-white shadow-lg rounded-lg p-4 border border-gray-200">
      <div class="flex items-center space-x-4">
        <span class="text-sm text-gray-700">
          Đã chọn <span class="font-semibold">{{ selectedProducts.length }}</span> sản phẩm
        </span>
        <div class="flex space-x-2">
          <button
            @click="bulkUpdateStatus('active')"
            class="inline-flex items-center px-3 py-1.5 border border-transparent text-xs font-medium rounded shadow-sm text-white bg-green-600 hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500"
          >
            <CheckIcon class="-ml-1 mr-1 h-4 w-4" />
            Kích hoạt
          </button>
          <button
            @click="bulkUpdateStatus('inactive')"
            class="inline-flex items-center px-3 py-1.5 border border-transparent text-xs font-medium rounded shadow-sm text-white bg-yellow-600 hover:bg-yellow-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-yellow-500"
          >
            <EyeSlashIcon class="-ml-1 mr-1 h-4 w-4" />
            Ẩn
          </button>
          <button
            @click="confirmBulkDelete"
            class="inline-flex items-center px-3 py-1.5 border border-transparent text-xs font-medium rounded shadow-sm text-white bg-red-600 hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500"
          >
            <TrashIcon class="-ml-1 mr-1 h-4 w-4" />
            Xóa
          </button>
        </div>
      </div>
    </div>

    <!-- Import Dialog -->
    <ImportProductsDialog
      :open="showImportDialog"
      @close="showImportDialog = false"
      @imported="handleProductsImported"
    />

    <!-- Delete Confirmation Dialog -->
    <ConfirmationDialog
      :open="showDeleteDialog"
      title="Xác nhận xóa"
      message="Bạn có chắc chắn muốn xóa sản phẩm đã chọn?"
      confirm-text="Xóa"
      cancel-text="Hủy"
      @confirm="deleteSelectedProducts"
      @cancel="showDeleteDialog = false"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useProductStore } from '@/stores/productStore.ts';
import {
  PlusIcon,
  PencilSquareIcon,
  TrashIcon,
  EyeIcon,
  EyeSlashIcon,
  ChevronLeftIcon,
  ChevronRightIcon,
  CheckIcon,
  ArrowUpTrayIcon,
  XMarkIcon
} from '@heroicons/vue/24/outline';
import ConfirmationDialog from '@/components/common/ConfirmationDialog.vue';
import ImportProductsDialog from '@/components/admin/ImportProductsDialog.vue';

const router = useRouter();
const productStore = useProductStore();

// Mock data - replace with actual API calls
const products = ref([
  {
    id: 1,
    name: 'Bánh xe đẩy 4 inch',
    sku: 'BX001',
    category: 'Phụ kiện',
    price: 150000,
    stock: 5,
    unit: 'cái',
    status: 'active',
    image: '/placeholder-product.jpg',
  },
  // Add more mock products as needed
]);

const categories = ref([
  { id: 1, name: 'Phụ kiện' },
  { id: 2, name: 'Khóa' },
  { id: 3, name: 'Bản lề' },
  { id: 4, name: 'Tay nắm' },
]);

const statuses = ref([
  { id: 'active', name: 'Đang bán' },
  { id: 'inactive', name: 'Ngừng bán' },
  { id: 'out_of_stock', name: 'Hết hàng' },
]);

const filters = ref({
  search: '',
  category: '',
  status: '',
});

const selectedProducts = ref<number[]>([]);
const showDeleteDialog = ref(false);
const showImportDialog = ref(false);

// Pagination
const itemsPerPage = 10;
const currentPage = ref(1);

// Computed properties
const filteredProducts = computed(() => {
  return products.value.filter(product => {
    const matchesSearch = !filters.value.search ||
      product.name.toLowerCase().includes(filters.value.search.toLowerCase()) ||
      product.sku.toLowerCase().includes(filters.value.search.toLowerCase());

    const matchesCategory = !filters.value.category ||
      product.category.toLowerCase() === filters.value.category.toLowerCase();

    const matchesStatus = !filters.value.status ||
      product.status === filters.value.status;

    return matchesSearch && matchesCategory && matchesStatus;
  });
});

const totalPages = computed(() => {
  return Math.ceil(filteredProducts.value.length / itemsPerPage);
});

const paginatedProducts = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  const end = start + itemsPerPage;
  return filteredProducts.value.slice(start, end);
});

const visiblePages = computed(() => {
  const pages = [];
  const maxVisiblePages = 5;

  if (totalPages.value <= maxVisiblePages) {
    for (let i = 1; i <= totalPages.value; i++) {
      pages.push(i);
    }
  } else {
    let startPage = Math.max(1, currentPage.value - 2);
    let endPage = Math.min(totalPages.value, startPage + maxVisiblePages - 1);

    if (endPage - startPage + 1 < maxVisiblePages) {
      startPage = Math.max(1, endPage - maxVisiblePages + 1);
    }

    if (startPage > 1) {
      pages.push(1);
      if (startPage > 2) {
        pages.push('...');
      }
    }

    for (let i = startPage; i <= endPage; i++) {
      pages.push(i);
    }

    if (endPage < totalPages.value) {
      if (endPage < totalPages.value - 1) {
        pages.push('...');
      }
      pages.push(totalPages.value);
    }
  }

  return pages;
});

// Methods
const formatPrice = (price: number) => {
  return new Intl.NumberFormat('vi-VN', {
    style: 'currency',
    currency: 'VND',
  }).format(price);
};

const getStatusClass = (status: string) => {
  const classes: Record<string, string> = {
    active: 'bg-green-100 text-green-800',
    inactive: 'bg-yellow-100 text-yellow-800',
    out_of_stock: 'bg-red-100 text-red-800',
  };
  return classes[status] || 'bg-gray-100 text-gray-800';
};

const getStatusText = (status: string) => {
  const statusMap: Record<string, string> = {
    active: 'Đang bán',
    inactive: 'Ngừng bán',
    out_of_stock: 'Hết hàng',
  };
  return statusMap[status] || status;
};

const toggleSelectAll = (event: Event) => {
  const target = event.target as HTMLInputElement;
  if (target.checked) {
    selectedProducts.value = paginatedProducts.value.map(product => product.id);
  } else {
    selectedProducts.value = [];
  }
};

const resetFilters = () => {
  filters.value = {
    search: '',
    category: '',
    status: '',
  };
  currentPage.value = 1;
};

const editProduct = (id: number) => {
  router.push(`/admin/products/edit/${id}`);
};

const confirmDelete = (id: number) => {
  selectedProducts.value = [id];
  showDeleteDialog.value = true;
};

const deleteSelectedProducts = () => {
  // TODO: Implement actual delete logic
  console.log('Deleting products:', selectedProducts.value);
  products.value = products.value.filter(product => !selectedProducts.value.includes(product.id));
  selectedProducts.value = [];
  showDeleteDialog.value = false;
};

const confirmBulkDelete = () => {
  if (selectedProducts.value.length > 0) {
    showDeleteDialog.value = true;
  }
};

const toggleStatus = (id: number, status: string) => {
  const product = products.value.find(p => p.id === id);
  if (product) {
    product.status = status;
  }
};

const bulkUpdateStatus = (status: string) => {
  // TODO: Implement bulk status update
  console.log(`Updating ${selectedProducts.value.length} products to status: ${status}`);
  products.value = products.value.map(product => {
    if (selectedProducts.value.includes(product.id)) {
      return { ...product, status };
    }
    return product;
  });
  selectedProducts.value = [];
};

const handleProductsImported = (importedProducts: any[]) => {
  // TODO: Handle imported products
  console.log('Products imported:', importedProducts);
  showImportDialog.value = false;
  // Refresh products list
  // fetchProducts();
};

// Lifecycle hooks
onMounted(() => {
  // Fetch products from API
  // fetchProducts();
});
</script>
