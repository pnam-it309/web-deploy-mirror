<template>
  <div class="wishlist-page">
    <div class="container">
      <h1 class="page-title">Sản Phẩm Yêu Thích</h1>
      
      <div v-if="wishlistItems.length === 0" class="empty-wishlist">
        <p>Danh sách yêu thích của bạn đang trống.</p>
        <router-link to="/customer/dashboard" class="continue-shopping">Tiếp tục mua sắm</router-link>
      </div>
      
      <div v-else class="wishlist-content">
        <div class="actions-bar">
          <button @click="addAllToRequest" class="btn-add-all">Thêm tất cả vào Yêu cầu đặt hàng</button>
        </div>
        
        <div class="wishlist-grid">
          <div v-for="item in wishlistItems" :key="item.id" class="wishlist-item">
            <div class="item-image">
              <img :src="item.image" :alt="item.name">
            </div>
            <div class="item-details">
              <h3 class="item-name">{{ item.name }}</h3>
              <p class="item-price">{{ formatPrice(item.price) }}</p>
              
              <div class="item-actions">
                <button @click="addToRequest(item)" class="btn-add">Thêm vào Yêu cầu</button>
                <button @click="removeFromWishlist(item.id)" class="btn-remove">Xóa</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted } from 'vue';

interface WishlistItem {
  id: string | number;
  name: string;
  price: number;
  image: string;
}

export default defineComponent({
  name: 'WishlistPage',
  setup() {
    const wishlistItems = ref<WishlistItem[]>([]);

    const loadWishlist = () => {
      const stored = localStorage.getItem('wishlist');
      if (stored) {
        try {
          wishlistItems.value = JSON.parse(stored);
        } catch (e) {
          console.error('Error parsing wishlist', e);
          wishlistItems.value = [];
        }
      }
    };

    const saveWishlist = () => {
      localStorage.setItem('wishlist', JSON.stringify(wishlistItems.value));
    };

    const removeFromWishlist = (id: string | number) => {
      wishlistItems.value = wishlistItems.value.filter(item => item.id !== id);
      saveWishlist();
    };

    const addToRequest = (item: WishlistItem) => {
      // Mock adding to request/cart
      alert(`Đã thêm "${item.name}" vào Yêu cầu đặt hàng!`);
      // In a real app, you would dispatch to a store or update another localStorage key
    };

    const addAllToRequest = () => {
      if (wishlistItems.value.length === 0) return;
      alert(`Đã thêm ${wishlistItems.value.length} sản phẩm vào Yêu cầu đặt hàng!`);
    };

    const formatPrice = (price: number) => {
      return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(price);
    };

    onMounted(() => {
      loadWishlist();
    });

    return {
      wishlistItems,
      removeFromWishlist,
      addToRequest,
      addAllToRequest,
      formatPrice
    };
  }
});
</script>

<style scoped>
.wishlist-page {
  padding: 2rem;
  background-color: #f9f9f9;
  min-height: 100vh;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
}

.page-title {
  font-size: 2rem;
  margin-bottom: 2rem;
  color: #333;
}

.empty-wishlist {
  text-align: center;
  padding: 3rem;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.continue-shopping {
  display: inline-block;
  margin-top: 1rem;
  color: #3498db;
  text-decoration: none;
}

.actions-bar {
  margin-bottom: 1.5rem;
  display: flex;
  justify-content: flex-end;
}

.btn-add-all {
  background-color: #2ecc71;
  color: white;
  border: none;
  padding: 0.75rem 1.5rem;
  border-radius: 4px;
  cursor: pointer;
  font-weight: bold;
  transition: background-color 0.2s;
}

.btn-add-all:hover {
  background-color: #27ae60;
}

.wishlist-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 2rem;
}

.wishlist-item {
  background: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  display: flex;
  flex-direction: column;
}

.item-image {
  height: 200px;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f0f0f0;
}

.item-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.item-details {
  padding: 1rem;
  display: flex;
  flex-direction: column;
  flex-grow: 1;
}

.item-name {
  font-size: 1.1rem;
  margin: 0 0 0.5rem;
  color: #333;
}

.item-price {
  font-weight: bold;
  color: #e74c3c;
  margin-bottom: 1rem;
}

.item-actions {
  margin-top: auto;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.btn-add {
  background-color: #3498db;
  color: white;
  border: none;
  padding: 0.5rem;
  border-radius: 4px;
  cursor: pointer;
}

.btn-add:hover {
  background-color: #2980b9;
}

.btn-remove {
  background-color: transparent;
  color: #e74c3c;
  border: 1px solid #e74c3c;
  padding: 0.5rem;
  border-radius: 4px;
  cursor: pointer;
}

.btn-remove:hover {
  background-color: #e74c3c;
  color: white;
}
</style>
