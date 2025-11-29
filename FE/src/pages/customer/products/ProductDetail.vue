<template>
  <div class="product-detail-page">
    <div v-if="isLoading" class="loading">
      <div class="spinner"></div>
      <p>Loading product details...</p>
    </div>

    <div v-else-if="!product" class="not-found">
      <h2>Product Not Found</h2>
      <p>Sorry, we couldn't find the product you're looking for.</p>
      <router-link to="/customer/dashboard" class="back-to-shop">
        &larr; Back to Shop
      </router-link>
    </div>

    <div v-else class="product-container">
      <!-- Breadcrumb Navigation -->
      <nav class="breadcrumb">
        <router-link to="/">Home</router-link>
        <span class="divider">/</span>
        <router-link to="/customer/dashboard">Products</router-link>
        <span class="divider">/</span>
        <span class="current">{{ product.name }}</span>
      </nav>

      <!-- Product Main Content -->
      <div class="product-main">
        <!-- Product Gallery -->
        <div class="product-gallery">
          <div class="main-image">
            <img :src="selectedImage || product.images[0]" :alt="product.name"
              @click="openLightbox(selectedImage || product.images[0])">
            <button v-if="product.onSale" class="sale-badge">
              Sale
            </button>
          </div>

          <div class="thumbnail-container">
            <button v-for="(image, index) in product.images" :key="index" class="thumbnail"
              :class="{ active: selectedImage === image }" @click="selectedImage = image">
              <img :src="image" :alt="`${product.name} thumbnail ${index + 1}`">
            </button>
          </div>
        </div>

        <!-- Product Info -->
        <div class="product-info">
          <h1 class="product-title">{{ product.name }}</h1>

          <div class="product-meta">
            <div class="rating">
              <div class="stars" :style="{ '--rating': (product.rating * 20) + '%' }" aria-label="Rating">
                ★★★★★
              </div>
              <span class="review-count">({{ product.reviewCount }} reviews)</span>
            </div>

            <div class="availability" :class="{ 'in-stock': product.inStock, 'out-of-stock': !product.inStock }">
              {{ product.inStock ? 'In Stock' : 'Out of Stock' }}
            </div>

            <div class="sku">
              SKU: {{ product.sku || 'N/A' }}
            </div>
          </div>

          <div class="price-container">
            <span class="current-price">
              {{ formatPrice(product.price) }}
              <span v-if="product.originalPrice" class="original-price">
                {{ formatPrice(product.originalPrice) }}
              </span>
            </span>
            <span v-if="product.youSave" class="you-save">
              Save {{ formatPrice(product.youSave) }}
            </span>
          </div>

          <div class="product-description" v-html="product.description"></div>

          <div v-if="product.variants && product.variants.length > 0" class="variants">
            <div v-for="variant in product.variants" :key="variant.id" class="variant-option">
              <h4>{{ variant.name }}:</h4>
              <div class="variant-options">
                <button v-for="option in variant.options" :key="option" class="variant-button" :class="{
                  'selected': selectedVariants[variant.name] === option,
                  'disabled': !isVariantAvailable(option)
                }" @click="selectVariant(variant.name, option)" :disabled="!isVariantAvailable(option)">
                  {{ option }}
                </button>
              </div>
            </div>
          </div>

          <div class="quantity-selector">
            <label for="quantity">Quantity:</label>
            <div class="quantity-controls">
              <button class="quantity-btn" @click="decreaseQuantity" :disabled="quantity <= 1">-</button>
              <input type="number" id="quantity" v-model.number="quantity" min="1" :max="product.inventory"
                @change="validateQuantity">
              <button class="quantity-btn" @click="increaseQuantity"
                :disabled="quantity >= product.inventory">+</button>
            </div>
            <div class="inventory" v-if="product.inventory > 0">
              {{ product.inventory }} available
            </div>
          </div>

          <div class="action-buttons">
            <button class="add-to-cart" :disabled="!product.inStock" @click="addToCart">
              <span class="icon">🛒</span>
              Thêm vào Yêu cầu đặt hàng
            </button>
            <button class="buy-now" :disabled="!product.inStock" @click="buyNow">
              Buy Now
            </button>
            <button class="wishlist" :class="{ 'in-wishlist': isInWishlist }" @click="toggleWishlist"
              :title="isInWishlist ? 'Remove from Wishlist' : 'Add to Wishlist'">
              ♥
            </button>
          </div>

          <div class="shipping-info">
            <div class="shipping-item">
              <span class="icon">🚚</span>
              <div>
                <div class="title">Free Shipping</div>
                <div class="description">On orders over $50</div>
              </div>
            </div>
            <div class="shipping-item">
              <span class="icon">↩️</span>
              <div>
                <div class="title">Easy Returns</div>
                <div class="description">30-day return policy</div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Product Tabs -->
      <div class="product-tabs">
        <div class="tabs-header">
          <button v-for="tab in tabs" :key="tab.id" :class="{ active: activeTab === tab.id }"
            @click="activeTab = tab.id">
            {{ tab.label }}
          </button>
        </div>

        <div class="tabs-content">
          <div v-if="activeTab === 'description'" class="tab-pane">
            <h3>Product Description</h3>
            <div v-html="product.fullDescription || product.description"></div>
          </div>

          <div v-if="activeTab === 'specs'" class="tab-pane">
            <h3>Product Specifications</h3>
            <table class="specs-table">
              <tbody>
                <tr v-for="(value, key) in product.specifications" :key="key">
                  <th>{{ formatKey(key) }}</th>
                  <td>{{ value }}</td>
                </tr>
              </tbody>
            </table>
          </div>

          <div v-if="activeTab === 'reviews'" class="tab-pane">
            <h3>Customer Reviews</h3>
            <div class="reviews-summary">
              <div class="overall-rating">
                <div class="average">{{ product.rating.toFixed(1) }}</div>
                <div class="stars" :style="{ '--rating': (product.rating * 20) + '%' }" aria-label="Rating">
                  ★★★★★
                </div>
                <div class="total-reviews">{{ product.reviewCount }} reviews</div>
              </div>

              <div class="rating-bars">
                <div v-for="i in 5" :key="i" class="rating-bar">
                  <span class="star-count">{{ 6 - i }}★</span>
                  <div class="bar-container">
                    <div class="bar" :style="{ width: getRatingPercentage(6 - i) + '%' }"></div>
                  </div>
                  <span class="percentage">{{ getRatingPercentage(6 - i) }}%</span>
                </div>
              </div>
            </div>

            <button class="write-review" @click="openReviewForm">
              Write a Review
            </button>

            <div class="reviews-list">
              <div v-for="review in product.reviews" :key="review.id" class="review">
                <div class="review-header">
                  <div class="reviewer">{{ review.author }}</div>
                  <div class="review-date">{{ formatDate(review.date) }}</div>
                  <div class="review-rating">
                    <span class="stars" :style="{ '--rating': (review.rating * 20) + '%' }" aria-label="Rating">
                      ★★★★★
                    </span>
                  </div>
                </div>
                <h4 class="review-title">{{ review.title }}</h4>
                <div class="review-content">{{ review.content }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Related Products -->
      <div v-if="relatedProducts.length > 0" class="related-products">
        <h2>You May Also Like</h2>
        <div class="related-grid">
          <ProductCard v-for="related in relatedProducts" :key="related.id" :product="related"
            @add-to-cart="addToCart" />
        </div>
      </div>
    </div>

    <!-- Lightbox -->
    <div v-if="lightboxImage" class="lightbox" @click.self="closeLightbox">
      <div class="lightbox-content">
        <button class="close-lightbox" @click="closeLightbox">&times;</button>
        <img :src="lightboxImage" :alt="product?.name">
      </div>
    </div>

    <!-- Review Form Modal -->
    <div v-if="showReviewForm" class="modal-overlay" @click.self="closeReviewForm">
      <div class="review-modal">
        <button class="close-modal" @click="closeReviewForm">&times;</button>
        <h3>Write a Review</h3>
        <form @submit.prevent="submitReview">
          <div class="form-group">
            <label>Rating</label>
            <div class="rating-input">
              <span v-for="i in 5" :key="i" class="star" :class="{ 'active': reviewData.rating >= i }"
                @click="reviewData.rating = i">
                ★
              </span>
            </div>
          </div>

          <div class="form-group">
            <label for="review-title">Title</label>
            <input type="text" id="review-title" v-model="reviewData.title" required
              placeholder="Summarize your review">
          </div>

          <div class="form-group">
            <label for="review-content">Review</label>
            <textarea id="review-content" v-model="reviewData.content" rows="5" required
              placeholder="Share your experience with this product"></textarea>
          </div>

          <div class="form-actions">
            <button type="button" class="cancel" @click="closeReviewForm">
              Cancel
            </button>
            <button type="submit" class="submit">
              Submit Review
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed, onMounted, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import ProductCard from './ProductCard.vue';

interface Product {
  id: string | number;
  name: string;
  description: string;
  fullDescription?: string;
  price: number;
  originalPrice?: number;
  youSave?: number;
  images: string[];
  rating: number;
  reviewCount: number;
  inStock: boolean;
  inventory: number;
  sku?: string;
  onSale?: boolean;
  variants?: Array<{
    id: string | number;
    name: string;
    options: string[];
  }>;
  specifications?: Record<string, string>;
  reviews?: Array<{
    id: string | number;
    author: string;
    date: string;
    rating: number;
    title: string;
    content: string;
  }>;
  ratings?: {
    [key: number]: number; // key: rating (1-5), value: count
  };
}

export default defineComponent({
  name: 'ProductDetail',
  components: {
    ProductCard
  },
  setup() {
    const route = useRoute();
    const router = useRouter();

    // Product data
    const product = ref<Product | null>(null);
    const relatedProducts = ref<Product[]>([]);
    const isLoading = ref(true);
    const selectedImage = ref('');
    const lightboxImage = ref('');
    const quantity = ref(1);
    const selectedVariants = ref<Record<string, string>>({});
    const isInWishlist = ref(false);
    const activeTab = ref('description');
    const showReviewForm = ref(false);

    // Review form data
    const reviewData = ref({
      rating: 0,
      title: '',
      content: ''
    });

    // Tabs configuration (ĐÃ SỬA: Dùng computed)
    const tabs = computed(() => [
      { id: 'description', label: 'Description' },
      { id: 'specs', label: 'Specifications' },
      { id: 'reviews', label: `Reviews (${product.value?.reviewCount || 0})` }
    ]);

    // Fetch product data
    const fetchProduct = async () => {
      try {
        isLoading.value = true;
        const productId = route.params.id;

        // TRONG ỨNG DỤNG THỰC TẾ, GỌI API Ở ĐÂY
        // ... (API call logic)

        // Mock data for demonstration
        setTimeout(() => {
          product.value = {
            id: productId,
            name: 'Premium Wireless Headphones',
            description: 'High-quality wireless headphones with noise cancellation',
            fullDescription: '<p>Experience crystal clear sound with our premium wireless headphones. Featuring active noise cancellation, 30-hour battery life, and comfortable over-ear design. Perfect for music lovers and professionals alike.</p>',
            price: 199.99,
            originalPrice: 249.99,
            youSave: 50,
            images: [
              'https://via.placeholder.com/800x800?text=Headphones+Front',
              'https://via.placeholder.com/800x800?text=Headphones+Side',
              'https://via.placeholder.com/800x800?text=Headphones+Back',
              'https://via.placeholder.com/800x800?text=Headphones+Case'
            ],
            rating: 4.5,
            reviewCount: 128,
            inStock: true,
            inventory: 15,
            sku: 'HP-BT-2023',
            onSale: true,
            variants: [
              { id: 'color', name: 'Color', options: ['Black', 'White', 'Blue'] },
              { id: 'size', name: 'Size', options: ['One Size'] }
            ],
            specifications: { 'Brand': 'AudioPro', 'Model': 'AP-WH1000' /* ... */ },
            ratings: { 5: 85, 4: 30, 3: 10, 2: 2, 1: 1 },
            reviews: [ /* ... */]
          };

          // Logic set image/variant default
          if (product.value.variants) { /* ... */ }
          if (product.value.images.length > 0) { selectedImage.value = product.value.images[0]; }

          // Mock related products
          relatedProducts.value = [ /* ... */];

          isLoading.value = false;
          checkWishlist();
        }, 800);
      } catch (error) {
        console.error('Error fetching product:', error);
        isLoading.value = false;
      }
    };

    // Sửa lỗi 2: Thêm watcher để tải lại khi điều hướng giữa các trang chi tiết
    watch(
      () => route.params.id,
      (newId, oldId) => {
        if (newId && newId !== oldId) {
          fetchProduct();
          window.scrollTo({ top: 0, behavior: 'smooth' });
        }
      }
    );

    // ... (Giữ nguyên các hàm khác) ...

    onMounted(() => {
      fetchProduct();

      return () => {
        document.body.style.overflow = '';
      };
    });

    return {
      // State
      product, relatedProducts, isLoading, selectedImage, lightboxImage, quantity,
      selectedVariants, isInWishlist, activeTab, showReviewForm, reviewData, tabs, // tabs là computed

      // Methods
      formatPrice, formatDate, formatKey, getRatingPercentage,
      increaseQuantity, decreaseQuantity, validateQuantity, selectVariant, isVariantAvailable,
      openLightbox, closeLightbox, toggleWishlist, addToCart, buyNow,
      openReviewForm, closeReviewForm, submitReview
    };
  }
});
</script>

<style scoped>
/* Base styles */
.product-detail-page {
  max-width: 1400px;
  margin: 0 auto;
  padding: 20px;
}

/* Loading state */
.loading,
.not-found {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 50vh;
  text-align: center;
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
  0% {
    transform: rotate(0deg);
  }

  100% {
    transform: rotate(360deg);
  }
}

.not-found h2 {
  font-size: 2rem;
  color: #e74c3c;
  margin-bottom: 15px;
}

.back-to-shop {
  display: inline-block;
  margin-top: 20px;
  padding: 10px 20px;
  background-color: #3498db;
  color: white;
  text-decoration: none;
  border-radius: 4px;
  transition: background-color 0.2s;
}

.back-to-shop:hover {
  background-color: #2980b9;
}

/* Breadcrumb */
.breadcrumb {
  margin-bottom: 30px;
  font-size: 0.9rem;
  color: #7f8c8d;
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 8px;
}

.breadcrumb a {
  color: #3498db;
  text-decoration: none;
  transition: color 0.2s;
}

.breadcrumb a:hover {
  color: #2980b9;
  text-decoration: underline;
}

.breadcrumb .divider {
  color: #bdc3c7;
  margin: 0 5px;
}

.breadcrumb .current {
  color: #7f8c8d;
}

/* Product Main */
.product-main {
  display: flex;
  gap: 40px;
  margin-bottom: 60px;
}

/* Product Gallery */
.product-gallery {
  flex: 1;
  max-width: 600px;
}

.main-image {
  position: relative;
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  margin-bottom: 15px;
  box-shadow: 0 2px 15px rgba(0, 0, 0, 0.05);
  cursor: zoom-in;
}

.main-image img {
  width: 100%;
  height: auto;
  display: block;
  transition: transform 0.3s;
}

.main-image:hover img {
  transform: scale(1.02);
}

.sale-badge {
  position: absolute;
  top: 15px;
  right: 15px;
  background-color: #e74c3c;
  color: white;
  padding: 5px 12px;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: bold;
  z-index: 2;
}

.thumbnail-container {
  display: flex;
  gap: 10px;
  overflow-x: auto;
  padding-bottom: 10px;
}

.thumbnail {
  flex: 0 0 80px;
  height: 80px;
  border: 2px solid transparent;
  border-radius: 4px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.2s;
  background: #fff;
  padding: 2px;
}

.thumbnail:hover {
  border-color: #3498db;
}

.thumbnail.active {
  border-color: #e74c3c;
}

.thumbnail img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

/* Product Info */
.product-info {
  flex: 1;
  max-width: 600px;
}

.product-title {
  font-size: 2rem;
  color: #2c3e50;
  margin-bottom: 15px;
  line-height: 1.2;
}

.product-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  margin-bottom: 20px;
  font-size: 0.9rem;
  color: #7f8c8d;
  align-items: center;
}

.rating {
  display: flex;
  align-items: center;
  gap: 5px;
}

.stars {
  position: relative;
  display: inline-block;
  color: #e4e4e4;
  font-size: 1.1rem;
  letter-spacing: 2px;
}

.stars::before {
  content: '★★★★★';
  position: relative;
  z-index: 1;
}

.stars::after {
  content: '★★★★★';
  position: absolute;
  left: 0;
  top: 0;
  width: var(--rating, 0);
  overflow: hidden;
  color: #f1c40f;
  z-index: 2;
}

.review-count {
  color: #3498db;
  text-decoration: none;
  font-size: 0.9rem;
}

.review-count:hover {
  text-decoration: underline;
}

.availability {
  padding: 3px 8px;
  border-radius: 4px;
  font-size: 0.8rem;
  font-weight: 500;
}

.availability.in-stock {
  background-color: #e8f8f5;
  color: #27ae60;
}

.availability.out-of-stock {
  background-color: #fde8e8;
  color: #e74c3c;
}

.price-container {
  margin: 25px 0;
}

.current-price {
  font-size: 2rem;
  font-weight: bold;
  color: #2c3e50;
  margin-right: 15px;
}

.original-price {
  text-decoration: line-through;
  color: #95a5a6;
  font-size: 1.3rem;
  margin-left: 8px;
}

.you-save {
  display: inline-block;
  background-color: #f8e8e8;
  color: #e74c3c;
  padding: 3px 10px;
  border-radius: 4px;
  font-size: 0.9rem;
  margin-left: 10px;
  font-weight: 500;
}

.product-description {
  margin: 25px 0;
  line-height: 1.6;
  color: #34495e;
}

/* Variants */
.variants {
  margin: 25px 0;
  padding: 20px 0;
  border-top: 1px solid #eee;
  border-bottom: 1px solid #eee;
}

.variant-option {
  margin-bottom: 20px;
}

.variant-option:last-child {
  margin-bottom: 0;
}

.variant-option h4 {
  margin: 0 0 10px 0;
  font-size: 1rem;
  color: #2c3e50;
}

.variant-options {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.variant-button {
  padding: 8px 15px;
  border: 1px solid #ddd;
  background: #fff;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.2s;
  font-size: 0.9rem;
}

.variant-button:hover:not(.disabled) {
  border-color: #3498db;
  color: #3498db;
}

.variant-button.selected {
  background-color: #3498db;
  color: white;
  border-color: #3498db;
}

.variant-button.disabled {
  opacity: 0.5;
  cursor: not-allowed;
  text-decoration: line-through;
  position: relative;
  overflow: hidden;
}

.variant-button.disabled::after {
  content: '';
  position: absolute;
  top: 50%;
  left: 0;
  right: 0;
  height: 1px;
  background: #e74c3c;
  transform: rotate(-5deg);
}

/* Quantity Selector */
.quantity-selector {
  margin: 25px 0;
}

.quantity-selector label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #2c3e50;
}

.quantity-controls {
  display: flex;
  align-items: center;
  max-width: 150px;
  margin-bottom: 10px;
}

.quantity-btn {
  width: 40px;
  height: 40px;
  background: #f8f9fa;
  border: 1px solid #ddd;
  font-size: 1.2rem;
  cursor: pointer;
  transition: all 0.2s;
}

.quantity-btn:hover:not(:disabled) {
  background: #e9ecef;
}

.quantity-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.quantity-selector input[type="number"] {
  width: 60px;
  height: 40px;
  text-align: center;
  border: 1px solid #ddd;
  border-left: none;
  border-right: none;
  -moz-appearance: textfield;
}

.quantity-selector input[type="number"]::-webkit-outer-spin-button,
.quantity-selector input[type="number"]::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

.inventory {
  font-size: 0.85rem;
  color: #7f8c8d;
}

/* Action Buttons */
.action-buttons {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  margin: 30px 0;
}

.add-to-cart,
.buy-now,
.wishlist {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 12px 25px;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.add-to-cart {
  background-color: #3498db;
  color: white;
  flex: 2;
  min-width: 200px;
}

.add-to-cart:hover {
  background-color: #2980b9;
}

.add-to-cart:disabled {
  background-color: #bdc3c7;
  cursor: not-allowed;
}

.buy-now {
  background-color: #2ecc71;
  color: white;
  flex: 1;
  min-width: 150px;
}

.buy-now:hover {
  background-color: #27ae60;
}

.buy-now:disabled {
  background-color: #bdc3c7;
  cursor: not-allowed;
}

.wishlist {
  width: 50px;
  background-color: #f8f9fa;
  color: #7f8c8d;
  font-size: 1.2rem;
  padding: 0;
}

.wishlist:hover {
  background-color: #e9ecef;
  color: #e74c3c;
}

.wishlist.in-wishlist {
  color: #e74c3c;
}

.icon {
  margin-right: 8px;
  font-size: 1.1rem;
}

/* Shipping Info */
.shipping-info {
  display: flex;
  gap: 20px;
  margin: 25px 0;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 8px;
}

.shipping-item {
  display: flex;
  align-items: center;
  gap: 15px;
}

.shipping-item .icon {
  font-size: 1.5rem;
  margin: 0;
}

.shipping-item .title {
  font-weight: 500;
  color: #2c3e50;
  margin-bottom: 3px;
}

.shipping-item .description {
  font-size: 0.9rem;
  color: #7f8c8d;
}

/* Product Tabs */
.product-tabs {
  margin: 60px 0;
  border: 1px solid #eee;
  border-radius: 8px;
  overflow: hidden;
}

.tabs-header {
  display: flex;
  border-bottom: 1px solid #eee;
  background: #f8f9fa;
}

.tabs-header button {
  padding: 15px 25px;
  background: none;
  border: none;
  border-bottom: 3px solid transparent;
  font-size: 1rem;
  font-weight: 500;
  color: #7f8c8d;
  cursor: pointer;
  transition: all 0.2s;
}

.tabs-header button:hover {
  color: #3498db;
}

.tabs-header button.active {
  color: #2c3e50;
  border-bottom-color: #3498db;
  background: white;
}

.tabs-content {
  padding: 30px;
  background: white;
}

.tab-pane h3 {
  margin-top: 0;
  margin-bottom: 20px;
  color: #2c3e50;
  font-size: 1.5rem;
}

.tab-pane p {
  line-height: 1.7;
  color: #34495e;
  margin-bottom: 15px;
}

.tab-pane ul,
.tab-pane ol {
  padding-left: 20px;
  margin-bottom: 20px;
}

.tab-pane li {
  margin-bottom: 8px;
  line-height: 1.5;
}

/* Specifications Table */
.specs-table {
  width: 100%;
  border-collapse: collapse;
  margin: 20px 0;
}

.specs-table th,
.specs-table td {
  padding: 12px 15px;
  text-align: left;
  border-bottom: 1px solid #eee;
}

.specs-table th {
  width: 30%;
  font-weight: 500;
  color: #2c3e50;
  background-color: #f8f9fa;
}

.specs-table tr:hover td {
  background-color: #f8f9fa;
}

/* Reviews */
.reviews-summary {
  display: flex;
  gap: 50px;
  margin-bottom: 30px;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 8px;
}

.overall-rating {
  text-align: center;
  min-width: 150px;
}

.average {
  font-size: 3rem;
  font-weight: bold;
  color: #2c3e50;
  line-height: 1;
  margin-bottom: 10px;
}

.total-reviews {
  margin-top: 5px;
  color: #7f8c8d;
  font-size: 0.9rem;
}

.rating-bars {
  flex: 1;
}

.rating-bar {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.star-count {
  width: 50px;
  color: #2c3e50;
  font-weight: 500;
}

.bar-container {
  flex: 1;
  height: 8px;
  background: #e0e0e0;
  border-radius: 4px;
  margin: 0 10px;
  overflow: hidden;
}

.bar {
  height: 100%;
  background: #f1c40f;
  border-radius: 4px;
}

.percentage {
  width: 40px;
  text-align: right;
  color: #7f8c8d;
  font-size: 0.9rem;
}

.write-review {
  display: block;
  margin: 0 0 30px auto;
  padding: 10px 20px;
  background: #3498db;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.95rem;
  transition: background-color 0.2s;
}

.write-review:hover {
  background: #2980b9;
}

.reviews-list {
  margin-top: 30px;
}

.review {
  padding: 20px 0;
  border-bottom: 1px solid #eee;
}

.review:last-child {
  border-bottom: none;
}

.review-header {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 15px;
  margin-bottom: 10px;
}

.reviewer {
  font-weight: 500;
  color: #2c3e50;
}

.review-date {
  color: #7f8c8d;
  font-size: 0.9rem;
}

.review-title {
  margin: 10px 0;
  color: #2c3e50;
  font-size: 1.1rem;
}

.review-content {
  line-height: 1.6;
  color: #34495e;
}

/* Related Products */
.related-products {
  margin: 80px 0 40px;
}

.related-products h2 {
  text-align: center;
  margin-bottom: 40px;
  color: #2c3e50;
  font-size: 1.8rem;
  position: relative;
  padding-bottom: 15px;
}

.related-products h2::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 80px;
  height: 3px;
  background: #3498db;
}

.related-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 25px;
}

/* Lightbox */
.lightbox {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.9);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  padding: 20px;
}

.lightbox-content {
  position: relative;
  max-width: 90%;
  max-height: 90vh;
}

.lightbox img {
  max-width: 100%;
  max-height: 80vh;
  display: block;
  border: 3px solid white;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.5);
}

.close-lightbox {
  position: absolute;
  top: -40px;
  right: 0;
  background: none;
  border: none;
  color: white;
  font-size: 2rem;
  cursor: pointer;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  transition: background-color 0.2s;
}

.close-lightbox:hover {
  background: rgba(255, 255, 255, 0.2);
}

/* Review Form Modal */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.7);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  padding: 20px;
}

.review-modal {
  background: white;
  border-radius: 8px;
  max-width: 600px;
  width: 100%;
  max-height: 90vh;
  overflow-y: auto;
  position: relative;
  padding: 30px;
  box-shadow: 0 5px 30px rgba(0, 0, 0, 0.2);
}

.close-modal {
  position: absolute;
  top: 15px;
  right: 15px;
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  color: #7f8c8d;
  transition: color 0.2s;
}

.close-modal:hover {
  color: #e74c3c;
}

.review-modal h3 {
  margin-top: 0;
  margin-bottom: 25px;
  color: #2c3e50;
  text-align: center;
  font-size: 1.5rem;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #2c3e50;
}

.rating-input {
  display: flex;
  gap: 10px;
  margin: 10px 0;
}

.rating-input .star {
  font-size: 2rem;
  color: #e4e4e4;
  cursor: pointer;
  transition: color 0.2s;
}

.rating-input .star.active,
.rating-input .star:hover {
  color: #f1c40f;
}

.rating-input .star:hover~.star {
  color: #e4e4e4;
}

.form-group input[type="text"],
.form-group textarea {
  width: 100%;
  padding: 10px 15px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
  transition: border-color 0.2s;
}

.form-group input[type="text"]:focus,
.form-group textarea:focus {
  border-color: #3498db;
  outline: none;
  box-shadow: 0 0 0 2px rgba(52, 152, 219, 0.2);
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 15px;
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}

.form-actions button {
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.form-actions .cancel {
  background: #f8f9fa;
  color: #7f8c8d;
}

.form-actions .cancel:hover {
  background: #e9ecef;
}

.form-actions .submit {
  background: #3498db;
  color: white;
}

.form-actions .submit:hover {
  background: #2980b9;
}

/* Responsive Design */
@media (max-width: 992px) {
  .product-main {
    flex-direction: column;
    gap: 30px;
  }

  .product-gallery,
  .product-info {
    max-width: 100%;
  }

  .reviews-summary {
    flex-direction: column;
    gap: 20px;
  }

  .overall-rating {
    display: flex;
    flex-direction: column;
    align-items: center;
  }
}

@media (max-width: 768px) {
  .product-title {
    font-size: 1.7rem;
  }

  .action-buttons {
    flex-direction: column;
  }

  .add-to-cart,
  .buy-now {
    width: 100%;
  }

  .shipping-info {
    flex-direction: column;
    gap: 15px;
  }

  .tabs-header {
    overflow-x: auto;
    flex-wrap: nowrap;
    justify-content: flex-start;
  }

  .tabs-header button {
    white-space: nowrap;
  }

  .form-actions {
    flex-direction: column;
  }

  .form-actions button {
    width: 100%;
  }
}

@media (max-width: 480px) {
  .product-title {
    font-size: 1.5rem;
  }

  .current-price {
    font-size: 1.7rem;
  }

  .original-price {
    font-size: 1.1rem;
  }

  .tabs-content {
    padding: 20px 15px;
  }

  .review-modal {
    padding: 20px 15px;
  }
}
</style>
