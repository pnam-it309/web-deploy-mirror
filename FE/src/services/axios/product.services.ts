import apiClient from '../api/api'; // (Đường dẫn đến api.ts)
import type { SpringPage } from '@/stores/brand.store'; // (Dùng chung)

// 1. Định nghĩa Types (Khớp với DTOs Backend)
export interface Product {
  id: string;
  name: string;
  slug: string;
  sku: string;
  shortDescription: string;
  price: number;
  stockQuantity: number;
  unit: string; // (Backend gửi Enum, FE nhận String)
  status: 'ACTIVE' | 'INACTIVE';
  brandId: string;
  brandName: string;
  categoryId: string;
  categoryName: string;
}

export interface ProductCreatePayload {
  name: string;
  sku: string;
  shortDescription: string;
  price: number;
  stockQuantity: number;
  unit: string;
  status: 'ACTIVE' | 'INACTIVE';
  brandId: string;
  categoryId: string;
}

export interface ProductUpdatePayload {
  name: string;
  shortDescription: string;
  price: number;
  stockQuantity: number;
  unit: string;
  status: 'ACTIVE' | 'INACTIVE';
  brandId: string;
  categoryId: string;
}
export interface ProductDetail {
  id?: string;
  productId: string;
  longDescription: string;
  specification: Record<string, any>; // JSON Object
  packaging: string;
  weight: number;
  dimensions: string;
}

export interface ProductDetailPayload {
  longDescription: string;
  specification: Record<string, any>;
  packaging: string;
  weight: number;
  dimensions: string;
}

// 2. Định nghĩa Service (gọi API Controller)
export const ProductService = {
  /*
   * API: GET /admin/products/get-all-products
   * Nhận thêm params (filter)
   */
  getAll: (params: any) => { // Thay đổi tham số
    return apiClient.get<SpringPage<Product>>('/admin/products/get-all-products', {
      params: params, // Truyền thẳng params xuống Axios
    });
  },

  /**
   * API: POST /admin/products
   */
  create: (payload: ProductCreatePayload) => {
    return apiClient.post<Product>('/admin/products', payload);
  },

  /**
   * API: PUT /admin/products/{id}
   */
  update: (id: string, payload: ProductUpdatePayload) => {
    return apiClient.put<Product>(`/admin/products/${id}`, payload);
  },

  /**
   * API: DELETE /admin/products/{id}
   */
  deleteById: (id: string) => {
    return apiClient.delete(`/admin/products/${id}`);
  },
  getDetail: (productId: string) => {
    return apiClient.get<ProductDetail>(`/admin/products/${productId}/details`);
  },

  updateDetail: (productId: string, payload: ProductDetailPayload) => {
    return apiClient.put<ProductDetail>(`/admin/products/${productId}/details`, payload);
  },
};