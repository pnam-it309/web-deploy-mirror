<template>
  <div class="product-list-page bg-gray-50 min-h-screen py-8">
    <div class="container mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">

      <CategorySlider :categories="categories" @select="handleCategorySelect" />

      <div class="product-container flex flex-col lg:flex-row gap-6">
        <div class="product-grid-container lg:flex-1">

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

  <!-- Part 3: Banner & Before You Buy -->
  <PromoBanner />
  <BeforeYouBuy />

  <!-- Part 4: Product Comparison -->
  <ProductComparison />

</template>

<script lang="ts">
import { defineComponent, ref, computed, onMounted, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
// Giả định component ProductCard.vue đã tồn tại
import ProductCard from './ProductCard.vue';
import CategorySlider from '@/components/custom/CategorySlider.vue';
// Giả định thư viện Axios đã được cài đặt
import request from '@/services/request';
import { API_CUSTOMER_PRODUCTS, API_CUSTOMER_WISHLIST } from '@/constants/url';
import { getDashboardCategories } from '@/services/api/customer/dashboard.api';
import { toast } from 'vue3-toastify';
import PromoBanner from '@/components/custom/PromoBanner.vue';
import BeforeYouBuy from '@/components/custom/BeforeYouBuy.vue';
import ProductComparison from '@/components/custom/ProductComparison.vue';

// --- CONFIGURATION ---
const ITEMS_PER_PAGE = 8; // Số sản phẩm trên mỗi trang

export default defineComponent({
  name: 'ProductList',
  components: {
    ProductCard,
    CategorySlider,
    PromoBanner,
    BeforeYouBuy,
    ProductComparison
  },
  setup() {
    const route = useRoute();
    const router = useRouter();

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
      isFavorite?: boolean;
    }

    // --- State ---
    // products giờ là nơi lưu trữ dữ liệu từ API
    const products = ref<Product[]>([]);
    const totalProductsCount = ref(0);
    const categories = ref<any[]>([]); // Sẽ fetch từ API nếu cần

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
        // Sử dụng request service thay vì axios trực tiếp để tận dụng baseURL và interceptors
        const response = await request.get(API_CUSTOMER_PRODUCTS, { params });

        // API trả về DefaultResponse<PaginationResponse<Product[]>>
        // response.data là DefaultResponse
        // response.data.data là PaginationResponse (chứa data và totalPages/totalElements)
        // response.data.data.data là mảng sản phẩm
        const apiResponse = response.data;

        if (apiResponse.success && apiResponse.data) {
          const pageData = apiResponse.data;
          // Kiểm tra xem pageData có phải là mảng không hay là object phân trang
          const content = Array.isArray(pageData) ? pageData : (pageData.data || []);

          products.value = content.map((item: any) => ({
            ...item,
            id: String(item.id),
            price: Number(item.price),
            inStock: item.stockQuantity > 0,
            category: item.categoryName || item.category?.name || item.category,
            image: item.image || `https://picsum.photos/seed/${item.id}/300/300`,
            isFavorite: item.isFavorite,
          })) as Product[];

          totalProductsCount.value = pageData.totalElements || pageData.total || products.value.length;
        } else {
          products.value = [];
          totalProductsCount.value = 0;
        }

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



    const toggleWishlist = async (product: any) => {
      try {
        if (product.isFavorite) {
          const response = await request.delete(`${API_CUSTOMER_WISHLIST}/${product.id}`);
          if (response.data.status === 'OK') {
            product.isFavorite = false;
            toast.success('Removed from wishlist');
          }
        } else {
          const response = await request.post(`${API_CUSTOMER_WISHLIST}/${product.id}`);
          if (response.data.status === 'OK') {
            product.isFavorite = true;
            toast.success('Added to wishlist');
          }
        }
      } catch (e) {
        console.error(e);
        toast.error('Failed to update wishlist');
      }
    };

    const isInWishlist = (id: string | number) => {
      // Since we are iterating over 'products' which now has 'isFavorite', 
      // we can just return that property. However, the template calls this method with ID.
      // It's better to pass the whole product object to 'is-in-wishlist' prop if possible, 
      // but ProductCard expects a boolean.
      // Let's find the product in our list.
      const product = products.value.find(p => p.id === String(id));
      return product ? !!product.isFavorite : false;
    };

    // Update fetchProducts to map isFavorite
    // ... inside fetchProducts mapping:
    // isFavorite: item.isFavorite,

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
      fetchCategories();
    };

    const fetchCategories = async () => {
      try {
        const res = await getDashboardCategories();
        if (res && res.data) {
          categories.value = res.data.map((c: any) => ({
            id: c.id,
            name: c.name,
            // Map other properties if needed by slider, currently slider just needs name and id
          }));
        }
      } catch (e) {
        console.error("Failed to fetch categories", e);
      }
    };

    const handleCategorySelect = (categoryName: string) => {
      selectedCategories.value = [categoryName];
      applyFilters();
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
      isInWishlist,
      handleCategorySelect
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
