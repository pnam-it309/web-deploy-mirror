import { defineStore } from 'pinia';
import { reactive, computed } from 'vue';
import type { Product } from '@/types/product';

type CartItem = {
  product: Product;
  quantity: number;
};

type CartState = {
  items: CartItem[];
  isOpen: boolean;
};

export const useCartStore = defineStore('cart', () => {
  const state = reactive<CartState>({
    items: [],
    isOpen: false,
  });

  const totalItems = computed<number>(() => {
    return state.items.reduce((total: number, item: CartItem) => total + item.quantity, 0);
  });
  
  const totalPrice = computed<number>(() => {
    return state.items.reduce((total: number, item: CartItem) => {
      return total + (item.product.price * item.quantity);
    }, 0);
  });

  function addToCart(product: Product, quantity: number = 1) {
    const existingItem = state.items.find(item => item.product.id === product.id);
    
    if (existingItem) {
      existingItem.quantity += quantity;
    } else {
      state.items.push({ product, quantity });
    }
  }
  
  function removeFromCart(productId: string) {
    const index = state.items.findIndex(item => item.product.id === productId);
    if (index !== -1) {
      state.items.splice(index, 1);
    }
  }
  
  function updateQuantity(productId: string, quantity: number) {
    const item = state.items.find(item => item.product.id === productId);
    if (item) {
      item.quantity = quantity;
    }
  }
  
  function clearCart() {
    state.items = [];
  }
  
  function toggleCart() {
    state.isOpen = !state.isOpen;
  }
  
  function openCart() {
    state.isOpen = true;
  }
  
  function closeCart() {
    state.isOpen = false;
  }

  return {
    // State
    items: computed<CartItem[]>(() => state.items),
    isOpen: computed<boolean>(() => state.isOpen),
    
    // Getters
    totalItems,
    totalPrice,
    
    // Actions
    addToCart,
    removeFromCart,
    updateQuantity,
    clearCart,
    toggleCart,
    openCart,
    closeCart,
  };
});
