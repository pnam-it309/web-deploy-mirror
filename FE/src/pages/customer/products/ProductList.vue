<template>
  <div class="product-list-page bg-gray-50 min-h-screen py-8">
    <div class="container mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">

      <div class="page-header bg-white shadow-sm p-6 rounded-lg mb-8 text-center">
        <h1>Our Products</h1>
        <p class="text-lg text-gray-500">Browse our wide selection of products</p>
      </div>

      <div class="product-container flex flex-col lg:flex-row gap-6">

        <aside class="filters lg:flex-none lg:w-64 bg-white p-6 rounded-lg shadow-md h-fit">
          <div class="filter-section">
            <h3 class="text-lg font-semibold text-gray-900 mb-4">Categories</h3>
            <div class="filter-options space-y-2">
              <div v-for="category in categories" :key="category.id"
                class="filter-option flex items-center justify-between">
                <div class="flex items-center">
                  <input type="checkbox" :id="'cat-' + category.id" v-model="selectedCategories" :value="category.id"
                    @change="applyFilters"
                    class="h-4 w-4 text-indigo-600 border-gray-300 rounded focus:ring-indigo-500">
                  <label :for="'cat-' + category.id" class="ml-3 text-sm text-gray-700 cursor-pointer">
                    {{ category.name }}
                  </label>
                </div>
                <span class="count text-xs text-gray-500 bg-gray-100 px-2 py-0.5 rounded-full">{{ category.count
                  }}</span>
              </div>
            </div>
          </div>

          <div class="filter-section border-t border-gray-200 pt-6 mt-6">
            <h3 class="text-lg font-semibold text-gray-900 mb-4">Price Range</h3>
            <div class="price-range space-y-3">
              <div class="price-inputs flex items-center gap-2">
                <input type="number" v-model.number="priceRange.min" placeholder="Min" min="0"
                  class="w-full p-2 border border-gray-300 rounded-md text-sm focus:ring-indigo-500 focus:border-indigo-500">
                <span class="text-gray-500">to</span>
                <input type="number" v-model.number="priceRange.max" placeholder="Max" :min="priceRange.min"
                  class="w-full p-2 border border-gray-300 rounded-md text-sm focus:ring-indigo-500 focus:border-indigo-500">
              </div>
              <button
                class="apply-filters w-full py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700 transition"
                @click="applyFilters">Apply Price Filter</button>
            </div>
          </div>

          <button
            class="clear-filters w-full py-2 mt-6 bg-gray-200 text-gray-700 rounded-md hover:bg-gray-300 transition"
            @click="resetFilters">Clear All Filters</button>
        </aside>

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
              <div class="results-count text-sm text-gray-600 mr-4 whitespace-nowrap">
                Showing {{ paginatedProducts.length }} of {{ filteredProducts.length }} results
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

          <div v-if="isLoading" class="loading">
            <div class="spinner"></div>
            <p>Loading products...</p>
          </div>

          <div v-else-if="filteredProducts.length === 0" class="no-results">
            <p>No products match your filters. Try adjusting your search criteria.</p>
            <button class="clear-filters" @click="resetFilters">Clear All Filters</button>
          </div>

          <div v-else class="product-grid">
            <ProductCard 
              v-for="product in paginatedProducts" 
              :key="product.id" 
              :product="product"
              :is-in-wishlist="isInWishlist(product.id)"
              @add-to-cart="addToCart" 
              @toggle-wishlist="toggleWishlist"
            />
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
import ProductCard from './ProductCard.vue';

export default defineComponent({
  name: 'ProductList',
  components: {
    ProductCard
  },
  setup() {
    const route = useRoute();
    const router = useRouter();

    // --- State ---
    const products = ref([
      // Mock Data (Thêm nhiều sản phẩm hơn để kiểm tra phân trang)
      { id: 'p1', name: 'Wireless Headphones Pro', price: 299.99, category: 'Electronics', image: 'https://picsum.photos/seed/p1/300/300', rating: 4.5, reviewCount: 50, description: 'Premium sound quality with active noise cancellation.', inStock: true, badge: 'New' as const },
      { id: 'p2', name: 'Leather Messenger Bag', price: 149.50, category: 'Clothing', image: 'https://picsum.photos/seed/p2/300/300', rating: 4.0, reviewCount: 30, description: 'Handcrafted leather bag for modern professionals.', inStock: true, badge: 'Sale' as const },
      { id: 'p3', name: 'Smart Home Hub', price: 99.00, category: 'Electronics', image: 'https://picsum.photos/seed/p3/300/300', rating: 4.8, reviewCount: 80, description: 'Control all your smart devices easily.', inStock: true },
      { id: 'p4', name: 'Gaming Mouse RGB', price: 59.99, category: 'Electronics', image: 'https://picsum.photos/seed/p4/300/300', rating: 3.9, reviewCount: 15, description: 'Ergonomic design with customizable RGB lighting.', inStock: false },
      { id: 'p5', name: 'Organic Coffee Beans', price: 19.99, category: 'Home & Garden', image: 'https://picsum.photos/seed/p5/300/300', rating: 5.0, reviewCount: 100, description: '100% Arabica, ethically sourced.', inStock: true },
      { id: 'p6', name: 'Wool Scarf', price: 35.00, category: 'Clothing', image: 'https://picsum.photos/seed/p6/300/300', rating: 4.2, reviewCount: 20, description: 'Soft and warm pure wool scarf.', inStock: true },
      { id: 'p7', name: 'Water Bottle Stainless', price: 25.00, category: 'Home & Garden', image: 'https://picsum.photos/seed/p7/300/300', rating: 4.6, reviewCount: 40, description: 'Keeps drinks cold for 24 hours.', inStock: true },
      { id: 'p8', name: 'Fantasy Novel Set', price: 75.00, category: 'Books', image: 'https://picsum.photos/seed/p8/300/300', rating: 4.9, reviewCount: 60, description: 'An epic four-book fantasy series.', inStock: true },
      { id: 'p9', name: 'Running Shoes X', price: 110.00, category: 'Sports', image: 'https://picsum.photos/seed/p9/300/300', rating: 4.1, reviewCount: 35, description: 'Lightweight and durable for long distance running.', inStock: true },
      { id: 'p10', name: 'Yoga Mat Eco', price: 45.00, category: 'Sports', image: 'https://picsum.photos/seed/p10/300/300', rating: 4.4, reviewCount: 25, description: 'Non-slip and environmentally friendly.', inStock: true },
      { id: 'p11', name: 'Designer Dress', price: 250.00, category: 'Clothing', image: 'https://picsum.photos/seed/p11/300/300', rating: 4.7, reviewCount: 15, description: 'Elegant evening dress.', inStock: true, badge: 'Limited' as const },
      { id: 'p12', name: '4K Monitor 32"', price: 450.00, category: 'Electronics', image: 'https://picsum.photos/seed/p12/300/300', rating: 4.3, reviewCount: 55, description: 'Stunning 4K resolution and high refresh rate.', inStock: true },
      { id: 'p13', name: 'Gardening Gloves', price: 15.00, category: 'Home & Garden', image: 'https://picsum.photos/seed/p13/300/300', rating: 3.5, reviewCount: 10, description: 'Durable gloves for all gardening tasks.', inStock: true },
    ]);

    const categories = ref([
      { id: 'Electronics', name: 'Electronics', count: 4 },
      { id: 'Clothing', name: 'Clothing', count: 3 },
      { id: 'Home & Garden', name: 'Home & Garden', count: 2 },
      { id: 'Books', name: 'Books', count: 1 },
      { id: 'Sports', name: 'Sports', count: 2 },
    ]);

    const selectedCategories = ref<string[]>([]);
    const priceRange = ref({ min: 0, max: 500 }); // Đặt max thấp hơn để dễ test
    const sortBy = ref('featured');
    const searchQuery = ref(''); // State mới cho Tìm kiếm

    // --- Phân trang State ---
    const currentPage = ref(1);
    const itemsPerPage = 8; // Đặt 8 sản phẩm/trang
    const isLoading = ref(false);
    const wishlistIds = ref<string[]>([]);

    // Computed properties
    const filteredProducts = computed(() => {
      const query = searchQuery.value.toLowerCase();

      const filtered = products.value
        .filter(product => {
          // 1. Lọc theo Tìm kiếm
          const matchesSearch = product.name.toLowerCase().includes(query);

          // 2. Lọc theo Danh mục
          const matchesCategory = selectedCategories.value.length === 0 ||
            selectedCategories.value.includes(product.category);

          // 3. Lọc theo Giá
          const matchesPrice = (!priceRange.value.min || product.price >= priceRange.value.min) &&
            (!priceRange.value.max || product.price <= priceRange.value.max);

          return matchesSearch && matchesCategory && matchesPrice;
        })
        .sort((a, b) => {
          // 4. Sắp xếp
          switch (sortBy.value) {
            case 'price-asc': return a.price - b.price;
            case 'price-desc': return b.price - a.price;
            case 'name-asc': return a.name.localeCompare(b.name);
            case 'name-desc': return b.name.localeCompare(a.name);
            default: return 0; // featured
          }
        });

      return filtered;
    });

    const totalPages = computed(() =>
      Math.ceil(filteredProducts.value.length / itemsPerPage)
    );

    const paginatedProducts = computed(() => {
      const start = (currentPage.value - 1) * itemsPerPage;
      const end = start + itemsPerPage;
      return filteredProducts.value.slice(start, end);
    });

    // Methods
    const applyFilters = () => {
      // Reset về trang 1 khi lọc/tìm kiếm/sắp xếp thay đổi
      currentPage.value = 1;
      updateUrl();
    };

    const changePage = (page: number) => {
      if (page >= 1 && page <= totalPages.value) {
        currentPage.value = page;
        updateUrl();
        window.scrollTo({ top: 0, behavior: 'smooth' });
      }
    };

    const resetFilters = () => {
      selectedCategories.value = [];
      priceRange.value = { min: 0, max: 500 };
      sortBy.value = 'featured';
      searchQuery.value = '';
      currentPage.value = 1;
      updateUrl();
    };

    const updateUrl = () => {
      const query: Record<string, any> = {};

      if (selectedCategories.value.length > 0) {
        query.categories = selectedCategories.value.join(',');
      }

      if (priceRange.value.min) query.minPrice = priceRange.value.min;
      if (priceRange.value.max < 500) query.maxPrice = priceRange.value.max;
      if (sortBy.value !== 'featured') query.sort = sortBy.value;
      if (searchQuery.value) query.search = searchQuery.value; // Thêm tìm kiếm vào URL
      if (currentPage.value > 1) query.page = currentPage.value;

      router.push({ query });
    };

    const addToCart = (product: any) => {
      alert(`Đã thêm "${product.name}" vào Yêu cầu đặt hàng!`);
      // Giả định dispatch to a cart store
    };

    const loadWishlist = () => {
      const wishlist = JSON.parse(localStorage.getItem('wishlist') || '[]');
      wishlistIds.value = wishlist.map((item: any) => item.id);
    };

    const toggleWishlist = (product: any) => {
      const wishlist = JSON.parse(localStorage.getItem('wishlist') || '[]');
      const index = wishlist.findIndex((item: any) => item.id === product.id);
      
      if (index === -1) {
        wishlist.push({
          id: product.id,
          name: product.name,
          price: product.price,
          image: product.image
        });
        wishlistIds.value.push(product.id);
      } else {
        wishlist.splice(index, 1);
        wishlistIds.value = wishlistIds.value.filter(id => id !== product.id);
      }
      
      localStorage.setItem('wishlist', JSON.stringify(wishlist));
      // Dispatch custom event for layout to update
      window.dispatchEvent(new Event('wishlist-updated'));
    };

    const isInWishlist = (id: string | number) => {
      return wishlistIds.value.includes(String(id));
    };

    const parseUrlParams = () => {
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
      if (route.query.search) searchQuery.value = route.query.search.toString(); // Lấy từ URL
      if (route.query.page) currentPage.value = Number(route.query.page);
    };

    // Lifecycle hooks
    onMounted(() => {
      parseUrlParams();
      loadWishlist();
    });

    // Watchers
    watch([sortBy, currentPage], () => {
      updateUrl();
    });

    // Watch for route query changes to handle browser navigation
    watch(() => route.query, () => {
      parseUrlParams();
    });

    return {
      // State
      products,
      categories,
      selectedCategories,
      priceRange,
      sortBy,
      searchQuery,
      currentPage,
      isLoading,

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
