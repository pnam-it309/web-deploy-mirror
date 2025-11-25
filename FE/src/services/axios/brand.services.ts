// SỬA LẠI ĐƯỜNG DẪN IMPORT:
import apiClient from '../api/api'; // <-- Đi lên (../) rồi đi xuống (api/api)

import type { SpringPage } from '@/stores/brand.store'; // Import từ store

// 1. ĐỊNH NGHĨA CÁC TYPES/INTERFACES (Giống DTOs)
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

// 2. TẠO CÁC HÀM GỌI API (Dùng apiClient MỚI)
export const BrandService = {
  /**
   * API: GET /admin/brands/get-all-brands
   * (Đã có Interceptor đính token)
   */
  getAll: (page = 0, size = 20) => {
    return apiClient.get<SpringPage<Brand>>('/admin/brands/get-all-brands', {
      params: { page, size },
    });
  },

  /**
   * API: POST /admin/brands
   * (Đã có Interceptor đính token)
   */
  create: (payload: BrandCreatePayload) => {
    return apiClient.post<Brand>('/admin/brands', payload);
  },

  /**
   * API: PUT /admin/brands/{id}
   * (Đã có Interceptor đính token)
   */
  update: (id: string, payload: BrandUpdatePayload) => {
    return apiClient.put<Brand>(`/admin/brands/${id}`, payload);
  },

  /**
   * API: DELETE /admin/brands/{id}
   * (Đã có Interceptor đính token)
   */
  deleteById: (id: string) => {
    return apiClient.delete(`/admin/brands/${id}`);
  },
};