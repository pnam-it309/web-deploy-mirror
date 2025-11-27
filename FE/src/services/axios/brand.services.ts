// SỬA LẠI ĐƯỜNG DẪN IMPORT:
import apiClient from '../api/api';
import type { SpringPage } from '@/stores/brand.store'; // Import từ store
export interface Brand {
  id: string; 
  name: string;
  code: string;
  slug: string;
  description?: string;
  status: 'ACTIVE' | 'INACTIVE';
  logoUrl?: string;
}

export interface BrandCreatePayload {
  name: string;
  code: string;
  slug: string;
  description?: string;
  logoUrl?: string;
  status: 'ACTIVE' | 'INACTIVE'; 
}

export interface BrandUpdatePayload {
  name?: string;
  slug?: string;
  description?: string;
  logoUrl?: string;
  status?: 'ACTIVE' | 'INACTIVE';
}
// Dùng apiClient MỚI
export const BrandService = {
  /**
   * API: GET /admin/brands/get-all-brands
   */
  getAll: (params: any) => { 
    return apiClient.get<SpringPage<Brand>>('/admin/brands/get-all-brands', {
      params: params,
    });
  },
  /**
   * API: POST /admin/brands
   */
  create: (payload: BrandCreatePayload) => {
    return apiClient.post<Brand>('/admin/brands', payload);
  },

  /**
   * API: PUT /admin/brands/{id}
   */
  update: (id: string, payload: BrandUpdatePayload) => {
    return apiClient.put<Brand>(`/admin/brands/${id}`, payload);
  },

  /**
   * API: DELETE /admin/brands/{id}
   */
  deleteById: (id: string) => {
    return apiClient.delete(`/admin/brands/${id}`);
  },
};