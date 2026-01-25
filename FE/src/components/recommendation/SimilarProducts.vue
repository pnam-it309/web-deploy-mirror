<template>
  <div v-if="similarProducts.length > 0" class="recommendations-section">
    <h3 class="section-title">
      <svg class="icon" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" 
          d="M13 10V3L4 14h7v7l9-11h-7z" />
      </svg>
      Sản phẩm tương tự
    </h3>

    <div class="products-grid">
      <router-link
        v-for="product in similarProducts"
        :key="product.id"
        :to="`/apps/${product.slug}`"
        class="product-card"
      >
        <div class="thumbnail-wrapper">
          <img
            :src="product.thumbnail || 'https://placehold.co/400x250 '"
            :alt="product.name"
            class="thumbnail"
          />
          <span v-if="product.isFeatured" class="featured-badge">⭐ Nổi bật</span>
        </div>

        <div class="product-content">
          <h4 class="product-name">{{ product.name }}</h4>
          <p class="product-summary">{{ product.summary }}</p>

          <div class="tech-tags">
            <span
              v-for="(tech, index) in product.technologyNames?.slice(0, 3)"
              :key="index"
              class="tech-tag"
            >
              {{ tech }}
            </span>
          </div>

          <div class="metadata">
            <span class="domain">📁 {{ product.domainName }}</span>
            <span class="views">👁️ {{ product.viewCount || 0 }}</span>
          </div>
        </div>
      </router-link>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getSimilarProducts } from '@/services/api/recommendation.service'

interface Props {
  productId: string
  limit?: number
}

const props = withDefaults(defineProps<Props>(), {
  limit: 5
})

const similarProducts = ref<any[]>([])
const isLoading = ref(false)

onMounted(async () => {
  await loadSimilarProducts()
})

const loadSimilarProducts = async () => {
  isLoading.value = true
  
  try {
    const response = await getSimilarProducts(props.productId, props.limit)
    if (response.data) {
      similarProducts.value = response.data
    }
  } catch (error) {
    console.error('Failed to load similar products:', error)
  } finally {
    isLoading.value = false
  }
}
</script>

<style scoped>
.recommendations-section {
  margin: 4rem 0;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  font-size: 1.5rem;
  font-weight: 700;
  margin-bottom: 1.5rem;
  color: #1f2937;
}

.section-title .icon {
  width: 28px;
  height: 28px;
  color: #3b82f6;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 1.5rem;
}

.product-card {
  background: white;
  border-radius: 1rem;
  overflow: hidden;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  text-decoration: none;
  color: inherit;
}

.product-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.15);
}

.thumbnail-wrapper {
  position: relative;
  height: 180px;
  overflow: hidden;
}

.thumbnail {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.product-card:hover .thumbnail {
  transform: scale(1.1);
}

.featured-badge {
  position: absolute;
  top: 0.75rem;
  right: 0.75rem;
  background: linear-gradient(135deg, #fbbf24 0%, #f59e0b 100%);
  color: white;
  font-size: 0.75rem;
  font-weight: 600;
  padding: 0.375rem 0.75rem;
  border-radius: 9999px;
  box-shadow: 0 2px 8px rgba(251, 191, 36, 0.4);
}

.product-content {
  padding: 1.25rem;
}

.product-name {
  font-size: 1.125rem;
  font-weight: 600;
  margin-bottom: 0.5rem;
  color: #1f2937;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.product-summary {
  font-size: 0.875rem;
  color: #6b7280;
  line-height: 1.5;
  margin-bottom: 1rem;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.tech-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  margin-bottom: 1rem;
}

.tech-tag {
  font-size: 0.7rem;
  padding: 0.25rem 0.625rem;
  background: linear-gradient(135deg, #e0e7ff 0%, #c7d2fe 100%);
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

/* Dark mode */
@media (prefers-color-scheme: dark) {
  .section-title {
    color: #f3f4f6;
  }
  
  .product-card {
    background: #1f2937;
  }
  
  .product-name {
    color: #f3f4f6;
  }
}
</style>
