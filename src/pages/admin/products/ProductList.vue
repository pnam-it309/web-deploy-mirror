<template>
  <div class="product-list">
    <div class="bg-white shadow">
      <div class="max-w-7xl mx-auto py-6 px-4 sm:px-6 lg:px-8 flex justify-between items-center">
        <h1 class="text-3xl font-bold text-gray-900">Quản lý sản phẩm</h1>
        <router-link
          :to="{ name: 'admin-product-create' }"
          class="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
        >
          <svg class="-ml-1 mr-2 h-5 w-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
            <path fill-rule="evenodd" d="M10 5a1 1 0 011 1v3h3a1 1 0 110 2h-3v3a1 1 0 11-2 0v-3H6a1 1 0 110-2h3V6a1 1 0 011-1z" clip-rule="evenodd" />
          </svg>
          Thêm sản phẩm
        </router-link>
      </div>
    </div>

    <div class="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
      <!-- Filters -->
      <div class="bg-white shadow rounded-lg p-4 mb-6">
        <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
          <!-- Search -->
          <div>
            <label for="search" class="block text-sm font-medium text-gray-700">Tìm kiếm</label>
            <input
              type="text"
              id="search"
              v-model="filters.search"
              placeholder="Tên sản phẩm, mã sản phẩm..."
              class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
            />
          </div>

          <!-- Category -->
          <div>
            <label for="category" class="block text-sm font-medium text-gray-700">Danh mục</label>
            <select
              id="category"
              v-model="filters.category"
              class="mt-1 block w-full border border-gray-300 bg-white rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
            >
              <option value="">Tất cả danh mục</option>
              <option v-for="category in categories" :key="category.id" :value="category.id">
                {{ category.name }}
              </option>
            </select>
          </div>

          <!-- Status -->
          <div>
            <label for="status" class="block text-sm font-medium text-gray-700">Trạng thái</label>
            <select
              id="status"
              v-model="filters.status"
              class="mt-1 block w-full border border-gray-300 bg-white rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
            >
              <option value="">Tất cả trạng thái</option>
              <option value="active">Đang bán</option>
              <option value="inactive">Ngừng bán</option>
              <option value="out_of_stock">Hết hàng</option>
            </select>
          </div>

          <!-- Actions -->
          <div class="flex items-end space-x-2">
            <button
              @click="resetFilters"
              class="inline-flex items-center px-4 py-2 border border-gray-300 shadow-sm text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
            >
              Đặt lại
            </button>
            <button
              @click="applyFilters"
              class="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
            >
              Áp dụng
            </button>
          </div>
        </div>
      </div>

      <!-- Products Table -->
      <div class="bg-white shadow overflow-hidden sm:rounded-lg">
        <div class="overflow-x-auto">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Sản phẩm
                </th>
                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Danh mục
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
              <tr v-for="product in products" :key="product.id">
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="flex items-center">
                    <div class="flex-shrink-0 h-10 w-10">
                      <img class="h-10 w-10 rounded-md" :src="product.image" :alt="product.name" />
                    </div>
                    <div class="ml-4">
                      <div class="text-sm font-medium text-gray-900">{{ product.name }}</div>
                      <div class="text-sm text-gray-500">Mã: {{ product.sku }}</div>
                    </div>
                  </div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="text-sm text-gray-900">{{ product.category }}</div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="text-sm text-gray-900">{{ formatCurrency(product.price) }}</div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="text-sm text-gray-900">{{ product.stock }}</div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span :class="getStatusClass(product.status)" class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full">
                    {{ getStatusText(product.status) }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                  <button @click="editProduct(product.id)" class="text-indigo-600 hover:text-indigo-900 mr-4">Sửa</button>
                  <button @click="confirmDelete(product.id)" class="text-red-600 hover:text-red-900">Xóa</button>
                </td>
              </tr>
              <tr v-if="products.length === 0">
                <td colspan="6" class="px-6 py-4 text-center text-sm text-gray-500">
                  Không có sản phẩm nào
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- Pagination -->
        <div class="bg-white px-4 py-3 flex items-center justify-between border-t border-gray-200 sm:px-6">
          <div class="flex-1 flex justify-between sm:hidden">
            <button
              @click="previousPage"
              :disabled="pagination.currentPage === 1"
              class="relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50"
            >
              Trước
            </button>
            <button
              @click="nextPage"
              :disabled="pagination.currentPage === pagination.totalPages"
              class="ml-3 relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50"
            >
              Tiếp
            </button>
          </div>
          <div class="hidden sm:flex-1 sm:flex sm:items-center sm:justify-between">
            <div>
              <p class="text-sm text-gray-700">
                Hiển thị <span class="font-medium">{{ pagination.from }}</span> đến <span class="font-medium">{{ pagination.to }}</span> trong tổng số <span class="font-medium">{{ pagination.total }}</span> kết quả
              </p>
            </div>
            <div>
              <nav class="relative z-0 inline-flex rounded-md shadow-sm -space-x-px" aria-label="Pagination">
                <button
                  @click="previousPage"
                  :disabled="pagination.currentPage === 1"
                  :class="pagination.currentPage === 1 ? 'opacity-50 cursor-not-allowed' : 'hover:bg-gray-50'"
                  class="relative inline-flex items-center px-2 py-2 rounded-l-md border border-gray-300 bg-white text-sm font-medium text-gray-500"
                >
                  <span class="sr-only">Trước</span>
                  <svg class="h-5 w-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
                    <path fill-rule="evenodd" d="M12.707 5.293a1 1 0 010 1.414L9.414 10l3.293 3.293a1 1 0 01-1.414 1.414l-4-4a1 1 0 010-1.414l4-4a1 1 0 011.414 0z" clip-rule="evenodd" />
                  </svg>
                </button>
                <!-- Page numbers -->
                <template v-for="page in pagination.totalPages" :key="page">
                  <button
                    @click="goToPage(page)"
                    :class="pagination.currentPage === page ? 'z-10 bg-indigo-50 border-indigo-500 text-indigo-600' : 'bg-white border-gray-300 text-gray-500 hover:bg-gray-50'"
                    class="relative inline-flex items-center px-4 py-2 border text-sm font-medium"
                  >
                    {{ page }}
                  </button>
                </template>
                <button
                  @click="nextPage"
                  :disabled="pagination.currentPage === pagination.totalPages"
                  :class="pagination.currentPage === pagination.totalPages ? 'opacity-50 cursor-not-allowed' : 'hover:bg-gray-50'"
                  class="relative inline-flex items-center px-2 py-2 rounded-r-md border border-gray-300 bg-white text-sm font-medium text-gray-500"
                >
                  <span class="sr-only">Tiếp</span>
                  <svg class="h-5 w-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
                    <path fill-rule="evenodd" d="M7.293 14.707a1 1 0 010-1.414L10.586 10 7.293 6.707a1 1 0 011.414-1.414l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0z" clip-rule="evenodd" />
                  </svg>
                </button>
              </nav>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, reactive, onMounted } from 'vue';
import { useRouter } from 'vue-router';

export default defineComponent({
  name: 'ProductList',
  setup() {
    const router = useRouter();
    
    // Sample data - replace with actual API calls
    const products = ref([
      // Sample product data
      // {
      //   id: 1,
      //   name: 'Áo thun nam',
      //   sku: 'ATN001',
      //   image: 'https://via.placeholder.com/40',
      //   category: 'Áo thun',
      //   price: 250000,
      //   stock: 100,
      //   status: 'active'
      // }
    ]);

    const categories = ref([
      { id: 1, name: 'Áo thun' },
      { id: 2, name: 'Quần jean' },
      { id: 3, name: 'Áo khoác' },
      { id: 4, name: 'Giày dép' },
    ]);

    const filters = reactive({
      search: '',
      category: '',
      status: ''
    });

    const pagination = reactive({
      currentPage: 1,
      perPage: 10,
      total: 0,
      get totalPages() {
        return Math.ceil(this.total / this.perPage);
      },
      get from() {
        return (this.currentPage - 1) * this.perPage + 1;
      },
      get to() {
        const to = this.currentPage * this.perPage;
        return to > this.total ? this.total : to;
      }
    });

    const formatCurrency = (value: number) => {
      return new Intl.NumberFormat('vi-VN', { 
        style: 'currency', 
        currency: 'VND' 
      }).format(value);
    };

    const getStatusClass = (status: string) => {
      const statusMap: Record<string, string> = {
        'active': 'bg-green-100 text-green-800',
        'inactive': 'bg-yellow-100 text-yellow-800',
        'out_of_stock': 'bg-red-100 text-red-800'
      };
      return statusMap[status] || 'bg-gray-100 text-gray-800';
    };

    const getStatusText = (status: string) => {
      const statusMap: Record<string, string> = {
        'active': 'Đang bán',
        'inactive': 'Ngừng bán',
        'out_of_stock': 'Hết hàng'
      };
      return statusMap[status] || status;
    };

    const fetchProducts = async () => {
      try {
        // Simulate API call
        await new Promise(resolve => setTimeout(resolve, 500));
        
        // Replace with actual API call
        // const response = await productApi.getProducts({
        //   page: pagination.currentPage,
        //   perPage: pagination.perPage,
        //   ...filters
        // });
        
        // products.value = response.data;
        // pagination.total = response.meta.total;
        
      } catch (error) {
        console.error('Error fetching products:', error);
      }
    };

    const applyFilters = () => {
      pagination.currentPage = 1;
      fetchProducts();
    };

    const resetFilters = () => {
      filters.search = '';
      filters.category = '';
      filters.status = '';
      applyFilters();
    };

    const goToPage = (page: number) => {
      if (page >= 1 && page <= pagination.totalPages) {
        pagination.currentPage = page;
        fetchProducts();
      }
    };

    const previousPage = () => {
      if (pagination.currentPage > 1) {
        pagination.currentPage--;
        fetchProducts();
      }
    };

    const nextPage = () => {
      if (pagination.currentPage < pagination.totalPages) {
        pagination.currentPage++;
        fetchProducts();
      }
    };

    const editProduct = (id: number) => {
      router.push({ 
        name: 'admin-product-edit',
        params: { id }
      });
    };

    const confirmDelete = (id: number) => {
      if (confirm('Bạn có chắc chắn muốn xóa sản phẩm này?')) {
        deleteProduct(id);
      }
    };

    const deleteProduct = async (id: number) => {
      try {
        // Replace with actual API call
        // await productApi.deleteProduct(id);
        // await fetchProducts();
        console.log(`Product ${id} deleted`);
      } catch (error) {
        console.error('Error deleting product:', error);
      }
    };

    onMounted(() => {
      fetchProducts();
    });

    return {
      products,
      categories,
      filters,
      pagination,
      formatCurrency,
      getStatusClass,
      getStatusText,
      applyFilters,
      resetFilters,
      goToPage,
      previousPage,
      nextPage,
      editProduct,
      confirmDelete
    };
  }
});
</script>

<style scoped>
.product-list {
  min-height: calc(100vh - 64px);
}
</style>
