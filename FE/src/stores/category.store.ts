import { defineStore } from 'pinia';
import {
  CategoryCreatePayload,
  CategoryService,
  type Category,
  
  type CategoryUpdatePayload,
} from '@/services/axios/category.services';
import type { AxiosError } from 'axios';
import type { SpringPage } from './brand.store';

export interface CategoryState {
  categories: Category[];
  pageData: SpringPage<Category> | null; 
  isLoading: boolean;
  error: string | null;
}

export const useCategoryStore = defineStore('category', {
  state: (): CategoryState => ({
    categories: [],
    pageData: null,
    isLoading: false,
    error: null,
  }),

  actions: {
    /**
     * Tải danh sách categories (có lọc)
     */
    async fetchCategories(filterParams: any = {}) { // <-- Nhận tham số
      this.isLoading = true;
      this.error = null;
      try {
        // Mặc định page 0, size 20
        const params = {
          page: 0,
          size: 20,
          ...filterParams // Merge filter vào params
        };
        
        const response = await CategoryService.getAll(params);
        this.pageData = response.data;
        this.categories = this.pageData.content;
      } catch (err) {
        this.error = 'Lỗi không thể tải danh sách danh mục.';
        console.error(err);
      } finally {
        this.isLoading = false;
      }
    },

    async createCategory(payload: CategoryCreatePayload) {
      this.isLoading = true;
      this.error = null;
      try {
        const response = await CategoryService.create(payload);
        await this.fetchCategories(); 
      } catch (err) {
        const axiosError = err as AxiosError<any>;
        if (axiosError.response && axiosError.response.data) {
          this.error = axiosError.response.data.message || 'Lỗi không thể tạo danh mục.';
        } else {
          this.error = 'Lỗi không thể tạo danh mục.';
        }
        console.error(err);
        throw new Error(this.error);
      } finally {
        this.isLoading = false;
      }
    },

    async updateCategory(id: string, payload: CategoryUpdatePayload) {
      this.isLoading = true;
      this.error = null;
      try {
        const response = await CategoryService.update(id, payload);
        await this.fetchCategories();
      } catch (err) {
        const axiosError = err as AxiosError<any>;
        if (axiosError.response && axiosError.response.data) {
          this.error = axiosError.response.data.message || 'Lỗi không thể cập nhật danh mục.';
        } else {
          this.error = 'Lỗi không thể cập nhật danh mục.';
        }
        console.error(err);
        throw new Error(this.error);
      } finally {
        this.isLoading = false;
      }
    },

    async deleteCategory(id: string) {
      this.isLoading = true;
      this.error = null;
      try {
        await CategoryService.deleteById(id);
        await this.fetchCategories();
      } catch (err) {
        this.error = 'Lỗi không thể xoá danh mục.';
        console.error(err);
      } finally {
        this.isLoading = false;
      }
    },
  },
});