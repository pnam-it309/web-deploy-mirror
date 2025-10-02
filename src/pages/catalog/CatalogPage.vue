<template>
  <div class="catalog-page">
    <div class="container mx-auto px-4 py-8">
      <!-- Header -->
      <div class="mb-8">
        <h1 class="text-3xl font-bold text-gray-900">Product Catalog</h1>
        <p class="mt-2 text-gray-600">Browse our collection of products</p>
      </div>

      <!-- Search and Filters -->
      <div class="mb-8 flex flex-col md:flex-row md:items-center md:justify-between gap-4">
        <div class="relative flex-1 max-w-xl">
          <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
            <svg class="h-5 w-5 text-gray-400" fill="currentColor" viewBox="0 0 20 20">
              <path fill-rule="evenodd" d="M8 4a4 4 0 100 8 4 4 0 000-8zM2 8a6 6 0 1110.89 3.476l4.817 4.817a1 1 0 01-1.414 1.414l-4.816-4.816A6 6 0 012 8z" clip-rule="evenodd" />
            </svg>
          </div>
          <input
            v-model="searchQuery"
            type="text"
            class="block w-full pl-10 pr-3 py-2 border border-gray-300 rounded-md leading-5 bg-white placeholder-gray-500 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
            placeholder="Search products..."
          >
        </div>
        
        <div class="flex items-center space-x-4">
          <div class="relative">
            <select
              v-model="sortBy"
              class="appearance-none bg-white border border-gray-300 rounded-md pl-3 pr-10 py-2 text-left text-sm focus:outline-none focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500"
            >
              <option value="name_asc">Name (A-Z)</option>
              <option value="name_desc">Name (Z-A)</option>
              <option value="price_asc">Price (Low to High)</option>
              <option value="price_desc">Price (High to Low)</option>
              <option value="newest">Newest</option>
            </select>
          </div>
          
          <button
            @click="isFilterOpen = !isFilterOpen"
            class="inline-flex items-center px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
          >
            <svg class="h-5 w-5 text-gray-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 4a1 1 0 011-1h16a1 1 0 011 1v2.586a1 1 0 01-.293.707l-6.414 6.414a1 1 0 00-.293.707V17l-4 4v-6.586a1 1 0 00-.293-.707L3.293 7.293A1 1 0 013 6.586V4z" />
            </svg>
            <span class="ml-2">Filters</span>
          </button>
        </div>
      </div>

      <!-- Filter Panel -->
      <div v-if="isFilterOpen" class="mb-8 p-4 bg-gray-50 rounded-lg">
        <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
          <div>
            <h3 class="text-sm font-medium text-gray-900 mb-3">Categories</h3>
            <div class="space-y-2">
              <div v-for="category in categories" :key="category.id" class="flex items-center">
                <input
                  :id="`category-${category.id}`"
                  v-model="selectedCategories"
                  type="checkbox"
                  :value="category.id"
                  class="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded"
                >
                <label :for="`category-${category.id}`" class="ml-3 text-sm text-gray-700">
                  {{ category.name }} ({{ category.count }})
                </label>
              </div>
            </div>
          </div>
          
          <div>
            <h3 class="text-sm font-medium text-gray-900 mb-3">Price Range</h3>
            <div class="space-y-4">
              <div class="flex items-center space-x-4">
                <div class="flex-1">
                  <label for="min-price" class="block text-xs text-gray-500">Min</label>
                  <input
                    id="min-price"
                    v-model.number="priceRange.min"
                    type="number"
                    min="0"
                    class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                  >
                </div>
                <div class="flex-1">
                  <label for="max-price" class="block text-xs text-gray-500">Max</label>
                  <input
                    id="max-price"
                    v-model.number="priceRange.max"
                    type="number"
                    :min="priceRange.min + 1"
                    class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                  >
                </div>
              </div>
              <button
                @click="applyPriceFilter"
                class="w-full bg-indigo-600 border border-transparent rounded-md shadow-sm py-2 px-4 text-sm font-medium text-white hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
              >
                Apply Price
              </button>
            </div>
          </div>
          
          <div>
            <h3 class="text-sm font-medium text-gray-900 mb-3">Rating</h3>
            <div class="space-y-2">
              <div v-for="rating in [4, 3, 2, 1]" :key="rating" class="flex items-center">
                <input
                  :id="`rating-${rating}`"
                  v-model="selectedRating"
                  type="radio"
                  :value="rating"
                  class="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300"
                >
                <label :for="`rating-${rating}`" class="ml-3 text-sm text-gray-700">
                  <div class="flex items-center">
                    <span v-for="i in 5" :key="i" class="text-yellow-400">
                      <svg
                        :class="{
                          'text-yellow-400': i <= rating,
                          'text-gray-300': i > rating
                        }"
                        class="h-5 w-5"
                        fill="currentColor"
                        viewBox="0 0 20 20"
                      >
                        <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
                      </svg>
                    </span>
                    <span class="ml-1 text-xs text-gray-500">& up</span>
                  </div>
                </label>
              </div>
              <div class="flex items-center">
                <input
                  id="rating-any"
                  v-model="selectedRating"
                  type="radio"
                  value="0"
                  class="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300"
                >
                <label for="rating-any" class="ml-3 text-sm text-gray-700">Any rating</label>
              </div>
            </div>
          </div>
        </div>
        
        <div class="mt-4 flex justify-end space-x-3">
          <button
            type="button"
            @click="resetFilters"
            class="bg-white py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
          >
            Reset all
          </button>
          <button
            type="button"
            @click="applyFilters"
            class="inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
          >
            Apply filters
          </button>
        </div>
      </div>

      <!-- Products Grid -->
      <div v-if="filteredProducts.length > 0" class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
        <div v-for="product in filteredProducts" :key="product.id" class="group relative bg-white border border-gray-200 rounded-lg overflow-hidden shadow-sm hover:shadow-md transition-shadow duration-200">
          <router-link :to="{ name: 'product-detail', params: { id: product.id } }" class="block">
            <div class="aspect-w-1 aspect-h-1 w-full overflow-hidden bg-gray-200">
              <img
                :src="product.image"
                :alt="product.name"
                class="w-full h-48 object-cover object-center group-hover:opacity-75"
              >
            </div>
            <div class="p-4">
              <h3 class="text-sm font-medium text-gray-900 line-clamp-2 h-12">
                {{ product.name }}
              </h3>
              <div class="mt-1 flex items-center">
                <div class="flex items-center">
                  <span v-for="i in 5" :key="i" class="text-yellow-400">
                    <svg
                      :class="{
                        'text-yellow-400': i <= Math.round(product.rating),
                        'text-gray-300': i > Math.round(product.rating)
                      }"
                      class="h-4 w-4"
                      fill="currentColor"
                      viewBox="0 0 20 20"
                    >
                      <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
                    </svg>
                  </span>
                </div>
                <span class="ml-2 text-xs text-gray-500">({{ product.reviewCount }})</span>
              </div>
              <div class="mt-2 flex items-center justify-between">
                <p class="text-lg font-semibold text-gray-900">${{ product.price.toFixed(2) }}</p>
                <span v-if="product.inStock" class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-green-100 text-green-800">
                  In Stock
                </span>
                <span v-else class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-red-100 text-red-800">
                  Out of Stock
                </span>
              </div>
            </div>
          </router-link>
          <div class="p-4 border-t border-gray-100">
            <button
              @click="addToCart(product)"
              :disabled="!product.inStock"
              class="w-full flex items-center justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              <svg class="h-5 w-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z" />
              </svg>
              Add to cart
            </button>
          </div>
        </div>
      </div>
      
      <!-- Empty state -->
      <div v-else class="text-center py-12">
        <svg class="mx-auto h-12 w-12 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1" d="M20 13V6a2 2 0 00-2-2H6a2 2 0 00-2 2v7m16 0v5a2 2 0 01-2 2H6a2 2 0 01-2-2v-5m16 0h-2.586a1 1 0 00-.707.293l-2.414 2.414a1 1 0 01-.707.293h-3.172a1 1 0 01-.707-.293l-2.414-2.414A1 1 0 006.586 13H4" />
        </svg>
        <h3 class="mt-2 text-sm font-medium text-gray-900">No products found</h3>
        <p class="mt-1 text-sm text-gray-500">Try adjusting your search or filter to find what you're looking for.</p>
        <div class="mt-6">
          <button
            type="button"
            @click="resetFilters"
            class="inline-flex items-center px-4 py-2 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
          >
            <svg class="-ml-1 mr-2 h-5 w-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
            </svg>
            Reset filters
          </button>
        </div>
      </div>

      <!-- Pagination -->
      <div v-if="filteredProducts.length > 0" class="mt-8 flex items-center justify-between border-t border-gray-200 px-4 py-3 sm:px-6">
        <div class="flex flex-1 justify-between sm:hidden">
          <button
            @click="currentPage--"
            :disabled="currentPage === 1"
            class="relative inline-flex items-center rounded-md border border-gray-300 bg-white px-4 py-2 text-sm font-medium text-gray-700 hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            Previous
          </button>
          <button
            @click="currentPage++"
            :disabled="currentPage === totalPages"
            class="relative ml-3 inline-flex items-center rounded-md border border-gray-300 bg-white px-4 py-2 text-sm font-medium text-gray-700 hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            Next
          </button>
        </div>
        <div class="hidden sm:flex sm:flex-1 sm:items-center sm:justify-between">
          <div>
            <p class="text-sm text-gray-700">
              Showing <span class="font-medium">{{ (currentPage - 1) * itemsPerPage + 1 }}</span>
              to <span class="font-medium">{{ Math.min(currentPage * itemsPerPage, filteredProducts.length) }}</span>
              of <span class="font-medium">{{ filteredProducts.length }}</span> results
            </p>
          </div>
          <div>
            <nav class="isolate inline-flex -space-x-px rounded-md shadow-sm" aria-label="Pagination">
              <button
                @click="currentPage--"
                :disabled="currentPage === 1"
                class="relative inline-flex items-center rounded-l-md px-2 py-2 text-gray-400 ring-1 ring-inset ring-gray-300 hover:bg-gray-50 focus:z-20 focus:outline-offset-0 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                <span class="sr-only">Previous</span>
                <svg class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
                  <path fill-rule="evenodd" d="M12.79 5.23a.75.75 0 01-.02 1.06L8.832 10l3.938 3.71a.75.75 0 11-1.04 1.08l-4.5-4.25a.75.75 0 010-1.08l4.5-4.25a.75.75 0 011.06.02z" clip-rule="evenodd" />
                </svg>
              </button>
              <button
                v-for="page in paginationRange"
                :key="page"
                @click="currentPage = page"
                :aria-current="currentPage === page ? 'page' : undefined"
                :class="[
                  'relative inline-flex items-center px-4 py-2 text-sm font-semibold',
                  currentPage === page
                    ? 'z-10 bg-indigo-600 text-white focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600'
                    : 'text-gray-900 ring-1 ring-inset ring-gray-300 hover:bg-gray-50 focus:outline-offset-0'
                ]"
              >
                {{ page }}
              </button>
              <button
                @click="currentPage++"
                :disabled="currentPage === totalPages"
                class="relative inline-flex items-center rounded-r-md px-2 py-2 text-gray-400 ring-1 ring-inset ring-gray-300 hover:bg-gray-50 focus:z-20 focus:outline-offset-0 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                <span class="sr-only">Next</span>
                <svg class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
                  <path fill-rule="evenodd" d="M7.21 14.77a.75.75 0 01.02-1.06L11.168 10 7.23 6.29a.75.75 0 111.04-1.08l4.5 4.25a.75.75 0 010 1.08l-4.5 4.25a.75.75 0 01-1.06-.02z" clip-rule="evenodd" />
                </svg>
              </button>
            </nav>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useCartStore } from '@/stores/cart';

const router = useRouter();
const cartStore = useCartStore();

// Mock data - replace with actual API calls
const products = ref([
  {
    id: 1,
    name: 'Premium T-Shirt',
    price: 29.99,
    image: 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=800&q=80',
    rating: 4.5,
    reviewCount: 24,
    inStock: true,
    categoryId: 1
  },
  {
    id: 2,
    name: 'Classic Jeans',
    price: 59.99,
    image: 'https://images.unsplash.com/photo-1542272604-787c3835535d?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=800&q=80',
    rating: 4.2,
    reviewCount: 18,
    inStock: true,
    categoryId: 2
  },
  {
    id: 3,
    name: 'Running Shoes',
    price: 89.99,
    image: 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=800&q=80',
    rating: 4.7,
    reviewCount: 36,
    inStock: true,
    categoryId: 3
  },
  {
    id: 4,
    name: 'Wireless Headphones',
    price: 129.99,
    image: 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=800&q=80',
    rating: 4.8,
    reviewCount: 42,
    inStock: false,
    categoryId: 4
  },
  {
    id: 5,
    name: 'Smart Watch',
    price: 199.99,
    image: 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=800&q=80',
    rating: 4.6,
    reviewCount: 31,
    inStock: true,
    categoryId: 4
  },
  {
    id: 6,
    name: 'Leather Wallet',
    price: 49.99,
    image: 'https://images.unsplash.com/photo-1591047139829-d91aecb1cbaa?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=800&q=80',
    rating: 4.4,
    reviewCount: 15,
    inStock: true,
    categoryId: 5
  },
  {
    id: 7,
    name: 'Sunglasses',
    price: 79.99,
    image: 'https://images.unsplash.com/photo-1572635196236-43738773bbb9?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=800&q=80',
    rating: 4.3,
    reviewCount: 22,
    inStock: true,
    categoryId: 6
  },
  {
    id: 8,
    name: 'Backpack',
    price: 69.99,
    image: 'https://images.unsplash.com/photo-1553062407-98eeb64c6a62?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=800&q=80',
    rating: 4.5,
    reviewCount: 28,
    inStock: true,
    categoryId: 7
  }
]);

const categories = ref([
  { id: 1, name: 'T-Shirts', count: 12 },
  { id: 2, name: 'Jeans', count: 8 },
  { id: 3, name: 'Shoes', count: 15 },
  { id: 4, name: 'Electronics', count: 6 },
  { id: 5, name: 'Accessories', count: 10 },
  { id: 6, name: 'Sunglasses', count: 7 },
  { id: 7, name: 'Bags', count: 5 }
]);

// Search and filter state
const searchQuery = ref('');
const sortBy = ref('name_asc');
const selectedCategories = ref<number[]>([]);
const priceRange = ref({ min: 0, max: 1000 });
const selectedRating = ref(0);
const isFilterOpen = ref(false);

// Pagination state
const currentPage = ref(1);
const itemsPerPage = 12;

// Computed properties
const filteredProducts = computed(() => {
  let result = [...products.value];
  
  // Apply search
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase();
    result = result.filter(product => 
      product.name.toLowerCase().includes(query)
    );
  }
  
  // Apply category filter
  if (selectedCategories.value.length > 0) {
    result = result.filter(product => 
      selectedCategories.value.includes(product.categoryId)
    );
  }
  
  // Apply price filter
  result = result.filter(product => 
    product.price >= priceRange.value.min && 
    product.price <= priceRange.value.max
  );
  
  // Apply rating filter
  if (selectedRating.value > 0) {
    result = result.filter(product => 
      Math.round(product.rating) >= selectedRating.value
    );
  }
  
  // Apply sorting
  result.sort((a, b) => {
    switch (sortBy.value) {
      case 'name_asc':
        return a.name.localeCompare(b.name);
      case 'name_desc':
        return b.name.localeCompare(a.name);
      case 'price_asc':
        return a.price - b.price;
      case 'price_desc':
        return b.price - a.price;
      case 'newest':
        return b.id - a.id; // Assuming higher IDs are newer
      default:
        return 0;
    }
  });
  
  return result;
});

const totalPages = computed(() => 
  Math.ceil(filteredProducts.value.length / itemsPerPage)
);

const paginatedProducts = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  const end = start + itemsPerPage;
  return filteredProducts.value.slice(start, end);
});

const paginationRange = computed(() => {
  const range: number[] = [];
  const maxVisiblePages = 5;
  let startPage = Math.max(1, currentPage.value - Math.floor(maxVisiblePages / 2));
  let endPage = startPage + maxVisiblePages - 1;
  
  if (endPage > totalPages.value) {
    endPage = totalPages.value;
    startPage = Math.max(1, endPage - maxVisiblePages + 1);
  }
  
  for (let i = startPage; i <= endPage; i++) {
    range.push(i);
  }
  
  return range;
});

// Methods
const addToCart = (product: any) => {
  cartStore.addToCart({
    id: product.id,
    name: product.name,
    price: product.price,
    quantity: 1,
    image: product.image
  });
};

const applyPriceFilter = () => {
  // Reset to first page when applying new filters
  currentPage.value = 1;
};

const applyFilters = () => {
  // Reset to first page when applying new filters
  currentPage.value = 1;
  isFilterOpen.value = false;
};

const resetFilters = () => {
  searchQuery.value = '';
  sortBy.value = 'name_asc';
  selectedCategories.value = [];
  priceRange.value = { min: 0, max: 1000 };
  selectedRating.value = 0;
  currentPage.value = 1;
};

// Watch for route changes to update filters from query params
onMounted(() => {
  const query = router.currentRoute.value.query;
  
  if (query.search) {
    searchQuery.value = query.search as string;
  }
  if (query.category) {
    const categoryId = parseInt(query.category as string);
    if (!isNaN(categoryId)) {
      selectedCategories.value = [categoryId];
    }
  }
});
</script>

<style scoped>
.catalog-page {
  min-height: calc(100vh - 4rem);
}

.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
