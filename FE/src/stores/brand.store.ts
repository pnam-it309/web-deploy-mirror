import { defineStore } from 'pinia';
import {
  BrandService,
  type Brand,
  type BrandCreatePayload,
  type BrandUpdatePayload,
} from '@/services/axios/brand.services';

export interface SpringPage<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  number: number; // trang hiện tại
  size: number;
}

// 1. ĐỊNH NGHĨA STATE
export interface BrandState {
  brands: Brand[];
  pageData: SpringPage<Brand> | null; // Dùng để lưu thông tin phân trang
  isLoading: boolean;
  error: string | null;
}

// 2. TẠO STORE
export const useBrandStore = defineStore('brand', {
  state: (): BrandState => ({
    brands: [],
    pageData: null,
    isLoading: false,
    error: null,
  }),

  // 3. TẠO ACTIONS (Nơi gọi API)
  actions: {
    async fetchBrands(filterParams: any = {}) { // <-- Nhận tham số
      this.isLoading = true;
      this.error = null;
      try {
        const params = {
          page: 0,
          size: 20,
          ...filterParams // Merge filter
        };
        const response = await BrandService.getAll(params);
        this.pageData = response.data as SpringPage<Brand>;
        this.brands = this.pageData.content; // Dữ liệu nằm trong 'content'
      } catch (err: any) {
        this.error = 'Lỗi không thể tải danh sách thương hiệu.';
        console.error(err);
      } finally {
        this.isLoading = false;
      }
    },
    async createBrand(payload: BrandCreatePayload) {
      this.isLoading = true;
      this.error = null;
      try {
        const response = await BrandService.create(payload);
        this.brands.unshift(response.data); // Thêm vào đầu danh sách
      } catch (err: any) {
        this.error = 'Lỗi không thể tạo thương hiệu.';
        console.error(err);
        throw err; // Ném lỗi ra để component biết
      } finally {
        this.isLoading = false;
      }
    },

    async updateBrand(id: string, payload: BrandUpdatePayload) {
      this.isLoading = true;
      this.error = null;
      try {
        const response = await BrandService.update(id, payload);
        // Cập nhật lại brand trong danh sách state
        const index = this.brands.findIndex((b) => b.id === id);
        if (index !== -1) {
          this.brands[index] = response.data;
        }
      } catch (err: any) {
        this.error = 'Lỗi không thể cập nhật thương hiệu.';
        console.error(err);
        throw err; // Ném lỗi ra để component biết
      } finally {
        this.isLoading = false;
      }
    },

    /**
     * Xoá một brand (soft delete)
     */
    async deleteBrand(id: string) {
      this.isLoading = true;
      this.error = null;
      try {
        await BrandService.deleteById(id);
        // Xoá brand ra khỏi danh sách state
        this.brands = this.brands.filter((b) => b.id !== id);
      } catch (err: any){
        this.error = 'Lỗi không thể xoá thương hiệu.';
        console.error(err);
      }finally {
        this.isLoading = false;
      }
    },
  },
});