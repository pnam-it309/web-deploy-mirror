import { defineStore } from 'pinia';
import {
  ProductService,
  type Product,
  type ProductCreatePayload,
  type ProductUpdatePayload,
  type ProductDetail, // <-- Import mới
  type ProductDetailPayload
} from '@/services/axios/product.services'; 
import type { AxiosError } from 'axios';
import type { SpringPage } from './brand.store';

import { useBrandStore } from './brand.store';
import { useCategoryStore } from './category.store'; 

export interface ProductState {
  products: Product[];
  pageData: SpringPage<Product> | null; 
  currentDetail: ProductDetail | null;
  isLoading: boolean;
  error: string | null;
}

export const useProductStore = defineStore('product', {
  state: (): ProductState => ({
    products: [],
    pageData: null,
    currentDetail: null,
    isLoading: false,
    error: null,
  }),

  actions: {
     async fetchProducts(filterParams: any = {}) { // Nhận tham số filter
        this.isLoading = true;
        this.error = null;
        try {
          // Mặc định page 0, size 20 nếu không truyền
          const params = {
            page: 0,
            size: 20,
            ...filterParams // Merge filter vào params
          };
          const response = await ProductService.getAll(params);
          this.pageData = response.data;
          this.products = this.pageData.content;
        } catch (err) {
        this.error = 'Lỗi không thể tải danh sách sản phẩm.';
      } finally {
        this.isLoading = false;
      }
    },

    async fetchDependencies() {
      const brandStore = useBrandStore();
      const categoryStore = useCategoryStore();
    
      // Chạy song song
      await Promise.all([
        brandStore.fetchBrands(), 
        categoryStore.fetchCategories()
      ]);
    },
    async createProduct(payload: ProductCreatePayload) {
      this.isLoading = true;
      this.error = null;
      try {
        const response = await ProductService.create(payload);
        this.products.unshift(response.data); 
        return response.data;
      } catch (err) {
        const axiosError = err as AxiosError<any>;
        if (axiosError.response && axiosError.response.data) {
          this.error = axiosError.response.data.message || 'Lỗi không thể tạo sản phẩm.';
        } else {
          this.error = 'Lỗi không thể tạo sản phẩm.';
          throw new Error(this.error); 
        }
      } finally {
        this.isLoading = false;
      }
    },

    /**
     * Cập nhật một product
     */
    async updateProduct(id: string, payload: ProductUpdatePayload) {
      this.isLoading = true;
      this.error = null;
      try {
        const response = await ProductService.update(id, payload);
        const index = this.products.findIndex((p) => p.id === id);
        if (index !== -1) {
          this.products[index] = response.data;
        }
      } catch (err) {
         const axiosError = err as AxiosError<any>;
        if (axiosError.response && axiosError.response.data) {
          this.error = axiosError.response.data.message || 'Lỗi không thể cập nhật sản phẩm.';
        } else {
          this.error = 'Lỗi không thể cập nhật sản phẩm.';
            throw new Error(this.error);
        }
       
      } finally {
        this.isLoading = false;
      }
    },
    async deleteProduct(id: string) {
      this.isLoading = true;
      this.error = null;
      try {
        await ProductService.deleteById(id);
        this.products = this.products.filter((p) => p.id !== id);
      } catch (err) {
        this.error = 'Lỗi không thể xoá sản phẩm.';
      } finally {
        this.isLoading = false;
      }
    },
    async fetchProductDetail(productId: string) {
      this.isLoading = true;
      this.currentDetail = null; // Reset cũ
      try {
        const response = await ProductService.getDetail(productId);
        this.currentDetail = response.data;
      } catch (err) {
        console.error("Lỗi tải chi tiết:", err);
        // Không throw lỗi chặn, để người dùng có thể nhập mới
      } finally {
        this.isLoading = false;
      }
    },

    async saveProductDetail(productId: string, payload: ProductDetailPayload) {
      this.isLoading = true;
      try {
        const response = await ProductService.updateDetail(productId, payload);
        this.currentDetail = response.data;
      } catch (err) {
        console.error("Lỗi lưu chi tiết:", err);
        throw err;
      } finally {
        this.isLoading = false;
      }
    }
  },
});