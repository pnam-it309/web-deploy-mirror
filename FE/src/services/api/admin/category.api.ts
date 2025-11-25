import request from '@/services/request';
import { Pageable } from '@/types/common';
import { Category, CategoryCreateRequest, CategoryUpdateRequest } from '@/types/category';

// types/category.ts
export interface Category {
  id: string;
  name: string;
  slug: string;
  description: string | null;
  parentId: string | null;
  children?: Category[];
}

export interface CategoryCreateRequest {
  name: string;
  slug: string;
  description?: string | null;
  parentId?: string | null;
}

export interface CategoryUpdateRequest extends CategoryCreateRequest {
  id: string;
}
export interface CategoryResponse {
  id: string;
  name: string;
  slug: string;
  description: string | null;
  parentId: string | null;
  children?: CategoryResponse[];
}

export interface CategoryCreateRequest {
  name: string;
  description?: string | null;
  parentId?: string | null;
}

export interface CategoryUpdateRequest extends CategoryCreateRequest {
  id: string;
}

const API_BASE_URL = '/api/admin/categories';

export const categoryApi = {
  async getAllCategories(pageable: Pageable) {
    const response = await request.get(API_BASE_URL + '/get-all-categories', {
      params: {
        page: pageable.page,
        size: pageable.size,
        sort: pageable.sort,
      },
    });
    return response.data;
  },

  async getCategoryById(id: string): Promise<CategoryResponse> {
    const response = await request.get(`${API_BASE_URL}/${id}`);
    return response.data;
  },

  async createCategory(requestData: CategoryCreateRequest): Promise<CategoryResponse> {
    const response = await request.post(API_BASE_URL, requestData);
    return response.data;
  },

  async updateCategory(requestData: CategoryUpdateRequest): Promise<CategoryResponse> {
    const { id, ...updateData } = requestData;
    const response = await request.put(`${API_BASE_URL}/${id}`, updateData);
    return response.data;
  },

  async deleteCategory(id: string): Promise<void> {
    await request.delete(`${API_BASE_URL}/${id}`);
  },
};
