<template>
  <div class="search-container">
    <!-- Search Input -->
    <div class="search-box">
      <input
        v-model="searchQuery"
        @input="handleSearch"
        type="text"
        placeholder="Tìm kiếm sản phẩm... (hỗ trợ gõ sai chính tả)"
        class="search-input"
      />
      <svg v-if="isSearching" class="spinner" viewBox="0 0 50 50">
        <circle cx="25" cy="25" r="20" fill="none" stroke-width="5"></circle>
      </svg>
    </div>

    <!-- Results Count -->
    <div v-if="searchResults.length > 0" class="results-meta">
      <p class="text-sm text-gray-600 dark:text-gray-400">
        Tìm thấy <strong>{{ estimatedTotal }}</strong> kết quả 
        trong <strong>{{ processingTime }}ms</strong>
      </p>
    </div>

    <!-- Search Results -->
    <div v-if="searchQuery && searchResults.length > 0" class="results-grid">
      <div
        v-for="product in searchResults"
        :key="product.id"
        class="product-card"
        @click="navigateToProduct(product.slug)"
      >
        <img
          :src="product.thumbnail_url || 'https://placehold.co/400x300'"
          :alt="product.name"
          class="product-thumbnail"
        />
        <div class="product-info">
          <h3 class="product-name" v-html="highlightMatches(product.name)"></h3>
          <p class="product-summary" v-html="highlightMatches(product.summary)"></p>
          
          <!-- Technologies -->
          <div class="tech-tags">
            <span
              v-for="(tech, index) in product.technology_names.slice(0, 3)"
              :key="index"
              class="tech-tag"
            >
              {{ tech }}
            </span>
          </div>

          <!-- Metadata -->
          <div class="metadata">
            <span class="domain">📁 {{ product.domain_name }}</span>
            <span class="views">👁️ {{ product.view_count }}</span>
            <span v-if="product.is_featured" class="featured-badge">⭐ Nổi bật</span>
          </div>
        </div>
      </div>
    </div>

    <!-- Empty State -->
    <div v-else-if="searchQuery && !isSearching" class="empty-state">
      <svg class="empty-icon" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" 
          d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
      </svg>
      <p>Không tìm thấy kết quả cho "<strong>{{ searchQuery }}</strong>"</p>
      <p class="text-sm text-gray-500">Thử tìm kiếm với từ khoá khác</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { searchProducts } from '@/services/api/search.service'

const router = useRouter()

const searchQuery = ref('')
const searchResults = ref<any[]>([])
const isSearching = ref(false)
const estimatedTotal = ref(0)
const processingTime = ref(0)

let searchTimeout: NodeJS.Timeout

/**
 * Debounced search handler
 * Waits 300ms after user stops typing before searching
 */
const handleSearch = () => {
  clearTimeout(searchTimeout)
  
  if (!searchQuery.value.trim()) {
    searchResults.value = []
    return
  }

  searchTimeout = setTimeout(async () => {
    await performSearch()
  }, 300) // 300ms debounce
}

/**
 * Execute search via Meilisearch API
 */
const performSearch = async () => {
  isSearching.value = true
  
  try {
    const response = await searchProducts({
      q: searchQuery.value,
      limit: 20
    })
    
    if (response.data) {
      searchResults.value = response.data.hits
      estimatedTotal.value = response.data.estimatedTotalHits
      processingTime.value = response.data.processingTimeMs
    }
  } catch (error) {
    console.error('Search failed:', error)
    searchResults.value = []
  } finally {
    isSearching.value = false
  }
}

/**
 * Navigate to product detail page
 */
const navigateToProduct = (slug: string) => {
  router.push(`/apps/${slug}`)
}

/**
 * Highlight search matches (simple version)
 * Meilisearch returns highlighted matches, we can use them
 */
const highlightMatches = (text: string) => {
  if (!text) return ''
  // Basic highlighting - Meilisearch provides <em> tags
  return text.replace(/<em>/g, '<mark>').replace(/<\/em>/g, '</mark>')
}
</script>

<style scoped>
.search-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem 1rem;
}

.search-box {
  position: relative;
  margin-bottom: 1.5rem;
}

.search-input {
  width: 100%;
  padding: 1rem 3rem 1rem 1.5rem;
  font-size: 1.125rem;
  border: 2px solid #e5e7eb;
  border-radius: 0.75rem;
  transition: all 0.3s ease;
}

.search-input:focus {
  outline: none;
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

.spinner {
  position: absolute;
  right: 1rem;
  top: 50%;
  transform: translateY(-50%);
  width: 24px;
  height: 24px;
  animation: spin 1s linear infinite;
}

.spinner circle {
  stroke: #3b82f6;
  stroke-linecap: round;
  animation: dash 1.5s ease-in-out infinite;
}

@keyframes spin {
  0% { transform: translateY(-50%) rotate(0deg); }
  100% { transform: translateY(-50%) rotate(360deg); }
}

@keyframes dash {
  0% {
    stroke-dasharray: 1, 150;
    stroke-dashoffset: 0;
  }
  50% {
    stroke-dasharray: 90, 150;
    stroke-dashoffset: -35;
  }
  100% {
    stroke-dasharray: 90, 150;
    stroke-dashoffset: -124;
  }
}

.results-meta {
  margin-bottom: 1rem;
}

.results-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 1.5rem;
}

.product-card {
  background: white;
  border-radius: 0.75rem;
  overflow: hidden;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  cursor: pointer;
}

.product-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.15);
}

.product-thumbnail {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.product-info {
  padding: 1.25rem;
}

.product-name {
  font-size: 1.125rem;
  font-weight: 600;
  margin-bottom: 0.5rem;
  color: #1f2937;
}

.product-name :deep(mark) {
  background-color: #fef08a;
  padding: 0 0.25rem;
  border-radius: 0.25rem;
}

.product-summary {
  font-size: 0.875rem;
  color: #6b7280;
  margin-bottom: 1rem;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.product-summary :deep(mark) {
  background-color: #fef08a;
}

.tech-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  margin-bottom: 1rem;
}

.tech-tag {
  font-size: 0.75rem;
  padding: 0.25rem 0.75rem;
  background-color: #e0e7ff;
  color: #4f46e5;
  border-radius: 9999px;
  font-weight: 500;
}

.metadata {
  display: flex;
  gap: 1rem;
  font-size: 0.75rem;
  color: #9ca3af;
}

.featured-badge {
  color: #f59e0b;
  font-weight: 600;
}

.empty-state {
  text-align: center;
  padding: 4rem 2rem;
  color: #6b7280;
}

.empty-icon {
  width: 64px;
  height: 64px;
  margin: 0 auto 1rem;
  color: #d1d5db;
}

/* Dark mode support */
@media (prefers-color-scheme: dark) {
  .product-card {
    background: #1f2937;
  }
  
  .product-name {
    color: #f3f4f6;
  }
  
  .search-input {
    background: #1f2937;
    color: white;
    border-color: #374151;
  }
}
</style>
