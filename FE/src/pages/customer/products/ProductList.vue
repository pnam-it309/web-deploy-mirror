<template>
  <div class="product-list-page">
    <div class="page-header">
      <h1>Our Products</h1>
      <p>Browse our wide selection of products</p>
    </div>

    <div class="product-container">
      <!-- Filters Sidebar -->
      <aside class="filters">
        <div class="filter-section">
          <h3>Categories</h3>
          <div class="filter-options">
            <div v-for="category in categories" :key="category.id" class="filter-option">
              <input 
                type="checkbox" 
                :id="'cat-' + category.id" 
                v-model="selectedCategories" 
                :value="category.id"
              >
              <label :for="'cat-' + category.id">
                {{ category.name }} 
                <span class="count">({{ category.count }})</span>
              </label>
            </div>
          </div>
        </div>

        <div class="filter-section">
          <h3>Price Range</h3>
          <div class="price-range">
            <div class="price-inputs">
              <input 
                type="number" 
                v-model.number="priceRange.min" 
                placeholder="Min" 
                min="0"
              >
              <span>to</span>
              <input 
                type="number" 
                v-model.number="priceRange.max" 
                placeholder="Max" 
                :min="priceRange.min"
              >
            </div>
            <button class="apply-filters" @click="applyFilters">Apply</button>
          </div>
        </div>
      </aside>

      <!-- Product Grid -->
      <div class="product-grid-container">
        <div class="toolbar">
          <div class="results-count">
            Showing {{ filteredProducts.length }} products
          </div>
          <div class="sort-options">
            <label>Sort by:</label>
            <select v-model="sortBy">
              <option value="featured">Featured</option>
              <option value="price-asc">Price: Low to High</option>
              <option value="price-desc">Price: High to Low</option>
              <option value="name-asc">Name: A to Z</option>
              <option value="name-desc">Name: Z to A</option>
            </select>
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
            @add-to-cart="addToCart"
          />
        </div>

        <div v-if="totalPages > 1" class="pagination">
          <button 
            :disabled="currentPage === 1"
            @click="currentPage--"
            class="page-nav"
          >
            &larr; Previous
          </button>
          
          <div class="page-numbers">
            <button 
              v-for="page in totalPages" 
              :key="page"
              @click="currentPage = page"
              :class="{ active: currentPage === page }"
              class="page-number"
            >
              {{ page }}
            </button>
          </div>
          
          <button 
            :disabled="currentPage === totalPages"
            @click="currentPage++"
            class="page-nav"
          >
            Next &rarr;
          </button>
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
    
    // State
    const products = ref([
      // This would typically come from an API
      { id: 1, name: 'Product 1', price: 29.99, category: 1, image: 'https://via.placeholder.com/300', rating: 4.5 },
      { id: 2, name: 'Product 2', price: 49.99, category: 2, image: 'https://via.placeholder.com/300', rating: 4.0 },
      // Add more sample products as needed
    ]);
    
    const categories = ref([
      { id: 1, name: 'Electronics', count: 24 },
      { id: 2, name: 'Clothing', count: 18 },
      { id: 3, name: 'Home & Garden', count: 32 },
      { id: 4, name: 'Books', count: 12 },
      { id: 5, name: 'Sports', count: 15 },
    ]);
    
    const selectedCategories = ref<number[]>([]);
    const priceRange = ref({ min: 0, max: 1000 });
    const sortBy = ref('featured');
    const currentPage = ref(1);
    const itemsPerPage = 12;
    const isLoading = ref(false);

    // Computed properties
    const filteredProducts = computed(() => {
      return products.value
        .filter(product => {
          const matchesCategory = selectedCategories.value.length === 0 || 
                                selectedCategories.value.includes(product.category);
          const matchesPrice = (!priceRange.value.min || product.price >= priceRange.value.min) &&
                             (!priceRange.value.max || product.price <= priceRange.value.max);
          return matchesCategory && matchesPrice;
        })
        .sort((a, b) => {
          switch (sortBy.value) {
            case 'price-asc': return a.price - b.price;
            case 'price-desc': return b.price - a.price;
            case 'name-asc': return a.name.localeCompare(b.name);
            case 'name-desc': return b.name.localeCompare(a.name);
            default: return 0; // featured
          }
        });
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
      currentPage.value = 1; // Reset to first page when filters change
      updateUrl();
    };

    const resetFilters = () => {
      selectedCategories.value = [];
      priceRange.value = { min: 0, max: 1000 };
      sortBy.value = 'featured';
      currentPage.value = 1;
      updateUrl();
    };

    const updateUrl = () => {
      const query: Record<string, any> = {};
      
      if (selectedCategories.value.length > 0) {
        query.categories = selectedCategories.value.join(',');
      }
      
      if (priceRange.value.min) query.minPrice = priceRange.value.min;
      if (priceRange.value.max < 1000) query.maxPrice = priceRange.value.max;
      if (sortBy.value !== 'featured') query.sort = sortBy.value;
      if (currentPage.value > 1) query.page = currentPage.value;
      
      router.push({ query });
    };

    const addToCart = (product: any) => {
      // Implement add to cart functionality
      console.log('Added to cart:', product);
      // You would typically dispatch to a store here
    };

    const parseUrlParams = () => {
      if (route.query.categories) {
        selectedCategories.value = route.query.categories
          .toString()
          .split(',')
          .map(Number);
      }
      
      if (route.query.minPrice) priceRange.value.min = Number(route.query.minPrice);
      if (route.query.maxPrice) priceRange.value.max = Number(route.query.maxPrice);
      if (route.query.sort) sortBy.value = route.query.sort.toString();
      if (route.query.page) currentPage.value = Number(route.query.page);
    };

    // Lifecycle hooks
    onMounted(() => {
      parseUrlParams();
      // Here you would typically fetch products from an API
      // fetchProducts();
    });

    // Watchers
    watch([sortBy, currentPage], () => {
      updateUrl();
    });

    // Watch for route changes to handle browser navigation
    watch(() => route.query, () => {
      parseUrlParams();
    });

    return {
      products,
      categories,
      selectedCategories,
      priceRange,
      sortBy,
      currentPage,
      isLoading,
      filteredProducts,
      totalPages,
      paginatedProducts,
      applyFilters,
      resetFilters,
      addToCart
    };
  }
});
</script>

<style scoped>
.product-list-page {
  max-width: 1400px;
  margin: 0 auto;
  padding: 20px;
}

.page-header {
  text-align: center;
  margin-bottom: 40px;
  padding: 30px 0;
  background: #f8f9fa;
  border-radius: 8px;
}

.page-header h1 {
  font-size: 2.5rem;
  color: #2c3e50;
  margin-bottom: 10px;
}

.page-header p {
  font-size: 1.1rem;
  color: #7f8c8d;
}

.product-container {
  display: flex;
  gap: 30px;
}

/* Filters Sidebar */
.filters {
  flex: 0 0 250px;
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 15px rgba(0, 0, 0, 0.05);
  height: fit-content;
}

.filter-section {
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #eee;
}

.filter-section:last-child {
  border-bottom: none;
  margin-bottom: 0;
  padding-bottom: 0;
}

.filter-section h3 {
  font-size: 1.1rem;
  margin-bottom: 15px;
  color: #2c3e50;
}

.filter-option {
  margin-bottom: 12px;
  display: flex;
  align-items: center;
}

.filter-option input[type="checkbox"] {
  margin-right: 10px;
  width: 16px;
  height: 16px;
}

.filter-option label {
  display: flex;
  align-items: center;
  cursor: pointer;
  font-size: 0.95rem;
  color: #34495e;
  width: 100%;
}

.filter-option .count {
  margin-left: auto;
  color: #7f8c8d;
  font-size: 0.85rem;
}

.price-range {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.price-inputs {
  display: flex;
  align-items: center;
  gap: 10px;
}

.price-inputs input {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 0.9rem;
}

.apply-filters, .clear-filters {
  width: 100%;
  padding: 10px;
  background-color: #3498db;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: 500;
  transition: background-color 0.2s;
}

.apply-filters:hover, .clear-filters:hover {
  background-color: #2980b9;
}

.clear-filters {
  background-color: #95a5a6;
  margin-top: 10px;
}

.clear-filters:hover {
  background-color: #7f8c8d;
}

/* Product Grid */
.product-grid-container {
  flex: 1;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 15px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 15px rgba(0, 0, 0, 0.05);
}

.results-count {
  font-size: 0.95rem;
  color: #7f8c8d;
}

.sort-options {
  display: flex;
  align-items: center;
  gap: 10px;
}

.sort-options label {
  font-size: 0.95rem;
  color: #7f8c8d;
}

.sort-options select {
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  background-color: #fff;
  font-size: 0.95rem;
  cursor: pointer;
}

/* Product Grid */
.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

/* Loading State */
.loading, .no-results {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 300px;
  background: #fff;
  border-radius: 8px;
  padding: 40px;
  text-align: center;
  box-shadow: 0 2px 15px rgba(0, 0, 0, 0.05);
}

.spinner {
  border: 4px solid rgba(0, 0, 0, 0.1);
  border-radius: 50%;
  border-top: 4px solid #3498db;
  width: 40px;
  height: 40px;
  animation: spin 1s linear infinite;
  margin-bottom: 20px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* Pagination */
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
  margin-top: 40px;
  padding: 20px 0;
}

.page-nav, .page-number {
  padding: 8px 15px;
  border: 1px solid #ddd;
  background: #fff;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.2s;
  font-size: 0.95rem;
}

.page-number {
  min-width: 40px;
}

.page-nav:hover:not(:disabled), 
.page-number:hover:not(.active) {
  background: #f8f9fa;
  border-color: #3498db;
  color: #3498db;
}

.page-nav:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.page-number.active {
  background: #3498db;
  color: white;
  border-color: #3498db;
}

/* Responsive Design */
@media (max-width: 992px) {
  .product-container {
    flex-direction: column;
  }
  
  .filters {
    flex: 0 0 auto;
    margin-bottom: 20px;
  }
  
  .toolbar {
    flex-direction: column;
    gap: 15px;
    align-items: flex-start;
  }
  
  .sort-options {
    width: 100%;
    justify-content: space-between;
  }
}

@media (max-width: 768px) {
  .product-grid {
    grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  }
  
  .page-header h1 {
    font-size: 2rem;
  }
  
  .page-header p {
    font-size: 1rem;
  }
}

@media (max-width: 480px) {
  .product-grid {
    grid-template-columns: 1fr 1fr;
  }
  
  .page-numbers {
    display: none;
  }
  
  .pagination {
    gap: 5px;
  }
  
  .page-nav {
    padding: 8px 10px;
    font-size: 0.85rem;
  }
}
</style>
