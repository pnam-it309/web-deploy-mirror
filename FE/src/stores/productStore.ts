import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import type { Product } from '@/types/product';

type ProductState = {
  currentProduct: Product | null;
  products: Product[];
  loading: boolean;
  error: string | null;
};

export const useProductStore = defineStore('product', () => {
  const state = ref<ProductState>({
    currentProduct: null,
    products: [],
    loading: false,
    error: null,
  });
  
  const currentProduct = computed(() => state.value.currentProduct);
  const products = computed(() => state.value.products);
  const loading = computed(() => state.value.loading);
  const error = computed(() => state.value.error);

  async function fetchProductById(productId: string) {
    state.value.loading = true;
    state.value.error = null;
    
    try {
      // TODO: Replace with your actual API call
      // const response = await fetch(`/api/products/${productId}`);
      // state.value.currentProduct = await response.json();
      
      // Mock data for now
      state.value.currentProduct = {
        id: productId,
        name: 'Sample Product',
        description: 'This is a sample product description.',
        price: 99.99,
        image: '/placeholder-product.jpg',
        category: 'Electronics',
        rating: 4.5,
        details: {
          'Brand': 'Sample Brand',
          'Color': 'Black',
          'Material': 'Plastic',
          'Weight': '1.2 kg'
        }
      };
    } catch (err) {
      console.error('Error fetching product:', err);
      state.value.error = 'Failed to load product details';
    } finally {
      state.value.loading = false;
    }
  }
  
  async function fetchProducts() {
    state.value.loading = true;
    state.value.error = null;
    
    try {
      // TODO: Replace with your actual API call
      // const response = await fetch('/api/products');
      // state.value.products = await response.json();
      
      // Mock data for now
      state.value.products = [
        {
          id: '1',
          name: 'Sample Product 1',
          description: 'Sample description 1',
          price: 99.99,
          image: '/placeholder-product.jpg',
          category: 'Electronics',
          rating: 4.5
        },
        {
          id: '2',
          name: 'Sample Product 2',
          description: 'Sample description 2',
          price: 149.99,
          image: '/placeholder-product.jpg',
          category: 'Electronics',
          rating: 4.8
        },
      ];
    } catch (err) {
      console.error('Error fetching products:', err);
      state.value.error = 'Failed to load products';
    } finally {
      state.value.loading = false;
    }
  }

  return {
    // State
    currentProduct,
    products,
    loading,
    error,
    
    // Actions
    fetchProductById,
    fetchProducts
  };
});
