import apiClient from '@/services/api/api';

const BASE_URL = '/admin/features';

export const FeatureService = {
  getAll: async () => {
    const response = await apiClient.get(BASE_URL); 
    return response.data;
  },
  getByAppId: async (appId: string) => {
    const response = await apiClient.get(`${BASE_URL}/by-app/${appId}`);
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

  deleteFeature: async (id: string) => {
    await apiClient.delete(`${BASE_URL}/${id}`);
  }
};