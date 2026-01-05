import apiClient from '@/services/api/api';
import type { Technology } from '@/types/admin.types';

const BASE_URL = '/admin/technologies';

export const TechnologyService = {
  getAll: async () => {
    const response = await apiClient.get<Technology[]>(BASE_URL, { params: { unpaged: true } });
    return response.data;
  },

  getPage: async (params: any) => {
    const response = await apiClient.get(BASE_URL, { params });
    return response.data;
  },

  create: async (data: any) => {
    const response = await apiClient.post(BASE_URL, data);
    return response.data;
  },

  update: async (id: string, data: any) => {
    const response = await apiClient.put(`${BASE_URL}/${id}`, data);
    return response.data;
  },
  deleteTechnology: async (id: string) => {
    await apiClient.delete(`${BASE_URL}/${id}`);
  }
};