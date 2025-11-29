<template>
  <div class="product-list-page bg-gray-50 min-h-screen py-8">
    <div class="container mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">

      <div class="page-header bg-white shadow-sm p-6 rounded-lg mb-8 text-center">
        <h1>Sản phẩm của chúng tôi</h1>
      </div>

      <div class="product-container flex flex-col lg:flex-row gap-6">
        <div class="product-grid-container lg:flex-1">
          <div
            class="toolbar bg-white shadow-sm p-4 rounded-lg flex flex-col sm:flex-row justify-between items-center mb-6">

            <div class="flex-1 w-full sm:w-auto mb-4 sm:mb-0 sm:mr-4 max-w-md">
              <div class="relative">
                <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                  <svg class="h-5 w-5 text-gray-400" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20"
                    fill="currentColor" aria-hidden="true">
                    <path fill-rule="evenodd"
                      d="M8 4a4 4 0 100 8 4 4 0 000-8zM2 8a6 6 0 1110.89 3.476l4.817 4.817a1 1 0 01-1.414 1.414l-4.816-4.816A6 6 0 012 8z"
                      clip-rule="evenodd" />
                  </svg>
                </div>
                <input v-model="searchQuery" @input="applyFilters" type="text"
                  class="block w-full pl-10 pr-3 py-2 border border-gray-300 rounded-md leading-5 bg-white placeholder-gray-500 focus:outline-none focus:placeholder-gray-400 focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                  placeholder="Search products by name...">
              </div>
            </div>

            <div class="flex items-center justify-between w-full sm:w-auto">
              <div v-if="!isLoading" class="results-count text-sm text-gray-600 mr-4 whitespace-nowrap">
                Showing **{{ paginatedProducts.length }}** of **{{ totalProductsCount }}** results
              </div>
              <div class="sort-options flex items-center gap-2">
                <label class="text-sm text-gray-600">Sort by:</label>
                <select v-model="sortBy" @change="applyFilters"
                  class="p-2 border border-gray-300 rounded-md text-sm focus:ring-indigo-500 focus:border-indigo-500">
                  <option value="featured">Featured</option>
                  <option value="price-asc">Price: Low to High</option>
                  <option value="price-desc">Price: High to Low</option>
                  <option value="name-asc">Name: A to Z</option>
                  <option value="name-desc">Name: Z to A</option>
                </select>
              </div>
            </div>
          </div>
          <div v-if="isLoading" class="loading p-12 bg-white rounded-lg shadow-sm text-center">
            <svg class="animate-spin h-8 w-8 text-indigo-600 mx-auto mb-3" xmlns="http://www.w3.org/2000/svg"
              fill="none" viewBox="0 0 24 24">
              <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
              <path class="opacity-75" fill="currentColor"
                d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z">
              </path>
            </svg>
            <p>Loading products...</p>
          </div>
          <div v-else-if="products.length === 0" class="no-results">
            <p class="p-6 bg-white rounded-lg shadow-sm">No products match your filters. Try adjusting your search
              criteria.</p>
            <button class="clear-filters" @click="resetFilters">Clear All Filters</button>
          </div>
          <div v-else class="product-grid">
            <ProductCard v-for="product in paginatedProducts" :key="product.id" :product="product"
              :is-in-wishlist="isInWishlist(product.id)" @add-to-cart="addToCart" @toggle-wishlist="toggleWishlist" />
          </div>
          <div v-if="totalPages > 1" class="pagination bg-white p-4 rounded-lg shadow-md mt-6">
            <button :disabled="currentPage === 1" @click="changePage(currentPage - 1)" class="page-nav">
              &larr; Previous
            </button>

            <div class="page-numbers">
              <button v-for="page in totalPages" :key="page" @click="changePage(page)"
                :class="{ active: currentPage === page }" class="page-number">
                {{ page }}
              </button>
            </div>

            <button :disabled="currentPage === totalPages" @click="changePage(currentPage + 1)" class="page-nav">
              Next &rarr;
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed, onMounted, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
// Giả định component ProductCard.vue đã tồn tại
import ProductCard from './ProductCard.vue';
// Giả định thư viện Axios đã được cài đặt
import axios from 'axios';

// --- Interface Sản phẩm (Tùy chỉnh theo API Backend của bạn) ---
interface Product {
  id: string;
  name: string;
  price: number; // Giả định giá trị USD
  category: string; // Tên category
  image: string; // URL hình ảnh
  rating: number;
  reviewCount: number;
  description: string;
  inStock: boolean;
  badge?: 'New' | 'Sale' | 'Limited';
}

// --- CONFIGURATION ---
const API_BASE_URL = 'http://your-backend-api.com/public/view_products';
const ITEMS_PER_PAGE = 8; // Số sản phẩm trên mỗi trang

export default defineComponent({
  name: 'ProductList',
  components: {
    ProductCard
  },
  setup() {
    const route = useRoute();
    const router = useRouter();

    // --- State ---
    // products giờ là nơi lưu trữ dữ liệu từ API
    const products = ref<Product[]>([]);
    const totalProductsCount = ref(0);
    const categories = ref<{ id: string, name: string, count: number }[]>([]); // Sẽ fetch từ API nếu cần

    const selectedCategories = ref<string[]>([]);
    const priceRange = ref({ min: 0, max: 500 });
    const sortBy = ref('featured');
    const searchQuery = ref('');

    // --- Phân trang State ---
    const currentPage = ref(1);
    const isLoading = ref(false);
    const wishlistIds = ref<string[]>([]);

    // -------------------------------------------------------------
    // LOGIC GỌI API
    // -------------------------------------------------------------
    const fetchProducts = async () => {
      isLoading.value = true;

      const params = {
        page: currentPage.value - 1, // API thường bắt đầu từ trang 0
        size: ITEMS_PER_PAGE,
        search: searchQuery.value,
        sort: sortBy.value,
        minPrice: priceRange.value.min,
        maxPrice: priceRange.value.max >= 500 ? undefined : priceRange.value.max,
        categories: selectedCategories.value.length > 0 ? selectedCategories.value.join(',') : undefined,
      };

      try {
        // !!! LƯU Ý: Đảm bảo cấu hình CORS ở Backend hoặc Proxy Frontend để API hoạt động !!!
        const response = await axios.get(API_BASE_URL, { params });

        // Giả định API trả về { content: Product[], totalElements: number }
        const data = response.data;

        // Cập nhật sản phẩm
        products.value = data.content.map((item: any) => ({
          ...item,
          // Giả định item.id tồn tại và item.price đã là number
          id: String(item.id),
          price: Number(item.price),
          inStock: item.stockQuantity > 0, // Tính toán từ stockQuantity
          category: item.category?.name || item.category, // Lấy tên category
          image: item.image || `https://picsum.photos/seed/${item.id}/300/300`,
        })) as Product[];

        totalProductsCount.value = data.totalElements || products.value.length;

        // **********************************************
        // NẾU CẦN TẢI DANH MỤC TỪ API:
        // **********************************************
        // const categoryResponse = await axios.get('API_CATEGORIES_URL');
        // categories.value = categoryResponse.data;

      } catch (error) {
        console.error("Error fetching products:", error);
        products.value = [];
        totalProductsCount.value = 0;
      } finally {
        isLoading.value = false;
      }
    };

    // -------------------------------------------------------------
    // COMPUTED
    // -------------------------------------------------------------
    // Giữ tên filteredProducts và paginatedProducts cho template cũ, nhưng chúng chỉ là products.value
    const filteredProducts = computed(() => products.value);

    const totalPages = computed(() =>
      Math.ceil(totalProductsCount.value / ITEMS_PER_PAGE)
    );

    const paginatedProducts = computed(() => products.value);

    // -------------------------------------------------------------
    // METHODS
    // -------------------------------------------------------------

    const applyFilters = () => {
      currentPage.value = 1;
      updateUrl();
      fetchProducts(); // Gọi API khi bộ lọc/tìm kiếm thay đổi
    };

    const changePage = (page: number) => {
      if (page >= 1 && page <= totalPages.value) {
        currentPage.value = page;
        updateUrl();
        window.scrollTo({ top: 0, behavior: 'smooth' });
        fetchProducts(); // Gọi API khi chuyển trang
      }
    };

    const resetFilters = () => {
      selectedCategories.value = [];
      priceRange.value = { min: 0, max: 500 };
      sortBy.value = 'featured';
      searchQuery.value = '';
      currentPage.value = 1;
      updateUrl();
      fetchProducts(); // Gọi API khi reset
    };

    const updateUrl = () => {
      const query: Record<string, any> = {};

      if (selectedCategories.value.length > 0) {
        query.categories = selectedCategories.value.join(',');
      }

      if (priceRange.value.min) query.minPrice = priceRange.value.min;
      if (priceRange.value.max < 500) query.maxPrice = priceRange.value.max;
      if (sortBy.value !== 'featured') query.sort = sortBy.value;
      if (searchQuery.value) query.search = searchQuery.value;
      if (currentPage.value > 1) query.page = currentPage.value;

      router.push({ query });
    };

    // --- Wishlist/Cart Mock Functions ---
    // Giữ nguyên các hàm này để template không báo lỗi
    const addToCart = (product: any) => {
      alert(`Added "${product.name}" to Cart!`);
    };

    const loadWishlist = () => {
      const wishlist = JSON.parse(localStorage.getItem('wishlist') || '[]');
      wishlistIds.value = wishlist.map((item: any) => item.id);
    };

    const toggleWishlist = (product: any) => {
      // Mock toggle logic
      const index = wishlistIds.value.indexOf(product.id);
      if (index === -1) {
        wishlistIds.value.push(product.id);
      } else {
        wishlistIds.value.splice(index, 1);
      }
    };

    const isInWishlist = (id: string | number) => {
      return wishlistIds.value.includes(String(id));
    };

    const parseUrlParams = () => {
      // Logic parse URL query params
      if (route.query.categories) {
        selectedCategories.value = route.query.categories
          .toString()
          .split(',');
      } else {
        selectedCategories.value = [];
      }

      if (route.query.minPrice) priceRange.value.min = Number(route.query.minPrice);
      if (route.query.maxPrice) priceRange.value.max = Number(route.query.maxPrice);
      if (route.query.sort) sortBy.value = route.query.sort.toString();
      if (route.query.search) searchQuery.value = route.query.search.toString();
      if (route.query.page) currentPage.value = Number(route.query.page);

      // Tải dữ liệu ban đầu
      fetchProducts();
    };

    // --- Lifecycle hooks ---
    onMounted(() => {
      parseUrlParams();
      loadWishlist();
      // Nếu bạn muốn tải Categories từ API, hãy gọi ở đây
      // fetchCategories();
    });

    // --- Watchers ---
    watch([sortBy, currentPage, selectedCategories, priceRange], () => {
      // Khi filter/sort thay đổi, gọi API (đã được xử lý trong applyFilters/changePage)
    });

    watch(() => route.query, () => {
      // Xử lý nút back/forward của trình duyệt
      parseUrlParams();
    });

    return {
      // State
      products, // Danh sách sản phẩm (từ API)
      categories, // Danh sách categories (fix cứng hoặc từ API)
      selectedCategories,
      priceRange,
      sortBy,
      searchQuery,
      currentPage,
      isLoading,
      totalProductsCount, // Tổng số lượng sản phẩm

      // Computed
      filteredProducts,
      totalPages,
      paginatedProducts,

      // Methods
      applyFilters,
      changePage,
      resetFilters,
      addToCart,
      toggleWishlist,
      isInWishlist
    };
  }
});
</script>

<style scoped>
/* Base/Container Styles */
.product-list-page {
  /* Giữ nguyên hoặc điều chỉnh theo Tailwind */
}

.page-header h1 {
  font-size: 2.5rem;
  color: #2c3e50;
  font-weight: 700;
}

/* Filters Sidebar (Tailwind-driven) */
.filters h3 {
  border-bottom: 1px solid #eee;
}

.filter-option label {
  transition: color 0.1s;
}

.filter-option label:hover {
  color: #4f46e5;
  /* indigo-600 hover */
}

/* Product Grid (Tailwind-driven) */
.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
}

/* Toolbar (Tailwind-driven) */
.toolbar {
  /* Đã được điều khiển bằng Tailwind */
}

/* Pagination (Tailwind-driven) */
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
}

.page-nav,
.page-number {
  padding: 8px 15px;
  border: 1px solid #ddd;
  background: #fff;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.2s;
  font-size: 0.95rem;
}

.page-nav:hover:not(:disabled),
.page-number:hover:not(.active) {
  background: #f0f4ff;
  /* Lighter indigo */
  border-color: #6366f1;
  /* indigo-500 */
  color: #4f46e5;
  /* indigo-600 */
}

.page-number.active {
  background: #4f46e5;
  color: white;
  border-color: #4f46e5;
}

.page-nav:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

/* Responsive Overrides */
@media (max-width: 1024px) {

  /* lg breakpoint */
  .product-container {
    flex-direction: column;
  }

  .filters {
    width: 100%;
  }
}

@media (max-width: 640px) {

  /* sm breakpoint */
  .toolbar {
    flex-direction: column;
    align-items: stretch;
  }

  .toolbar>* {
    width: 100%;
  }

  .sort-options {
    justify-content: space-between;
  }

  .product-grid {
    grid-template-columns: 1fr 1fr;
  }
}
</style>
