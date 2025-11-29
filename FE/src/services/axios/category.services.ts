import apiClient from '../api/api'; // (Đường dẫn đến api.ts)
import type { SpringPage } from '@/stores/brand.store'; // (Dùng chung)

export interface Category {
  id: string;
  name: string;
  slug: string;
  description?: string;
  parentId?: string;
  children?: Category[];
}

export interface CategoryCreatePayload {
  name: string;
  description?: string;
  parentId?: string;
}

export interface CategoryUpdatePayload {
  name: string;
  description?: string;
  parentId?: string;
}

// 2. Định nghĩa Service (gọi API Controller)
export const CategoryService = {
  /**
   * API: GET /admin/categories/get-all-categories
   */
    getAll: (params: any) => { // <-- Thay đổi ở đây
    return apiClient.get<SpringPage<Category>>('/admin/categories/get-all-categories', {
      params: params, // <-- Truyền params xuống Axios
    });
  },

  /**
   * API: POST /admin/categories
   */
  create: (payload: CategoryCreatePayload) => {
    return apiClient.post<Category>('/admin/categories', payload);
  },

  /**
   * API: PUT /admin/categories/{id}
   */
  update: (id: string, payload: CategoryUpdatePayload) => {
    return apiClient.put<Category>(`/admin/categories/${id}`, payload);
  },

  /**
   * API: DELETE /admin/categories/{id}
   */
  deleteById: (id: string) => {
    return apiClient.delete(`/admin/categories/${id}`);
  },
};