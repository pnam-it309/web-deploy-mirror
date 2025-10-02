import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import type { Product } from '@/types/product';

export const useCartStore = defineStore('cart', () => {
  const items = ref<Array<{ product: Product; quantity: number }>>([]);
  
  const itemCount = computed(() => 
    items.value.reduce((total, item) => total + item.quantity, 0)
  );
  
  const total = computed(() =>
    items.value.reduce(
      (sum, item) => sum + item.product.price * item.quantity,
      0
    )
  );

  function addToCart(product: Product) {
    const existingItem = items.value.find(item => item.product.id === product.id);
    
    if (existingItem) {
      existingItem.quantity += 1;
    } else {
      items.value.push({ product, quantity: 1 });
    }
  }

  function updateQuantity(productId: string, newQuantity: number) {
    if (newQuantity < 1) return;
    
    const item = items.value.find(item => item.product.id === productId);
    if (item) {
      item.quantity = newQuantity;
    }
  }

  function removeFromCart(productId: string) {
    const index = items.value.findIndex(item => item.product.id === productId);
    if (index !== -1) {
      items.value.splice(index, 1);
    }
  }

  function clearCart() {
    items.value = [];
  }

  return {
    items,
    itemCount,
    total,
    addToCart,
    updateQuantity,
    removeFromCart,
    clearCart,
  };
});
