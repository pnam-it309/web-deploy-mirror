import apiClient from '@/services/api/api'

const BASE_URL = '/admin/apps'

export const AppService = {
  getAll: async (params: any) => {
    const response = await apiClient.get(BASE_URL, { params })
    return response.data
  },
  getById: async (id: string) => {
    const response = await apiClient.get(`${BASE_URL}/${id}`)
    return response.data
  },
  getDetailExtension: async (appId: string) => {
    const response = await apiClient.get(`${BASE_URL}/${appId}/details`)
    return response.data
  },
  create: async (data: any) => {
    const response = await apiClient.post(BASE_URL, data)
    return response.data
  },
  update: async (id: string, data: any) => {
    const response = await apiClient.put(`${BASE_URL}/${id}`, data)
    return response.data
  },
  updateDetailExtension: async (appId: string, data: any) => {
    const response = await apiClient.put(`${BASE_URL}/${appId}/details`, data)
    return response.data
  },
  deleteApp: async (id: string) => {
    await apiClient.delete(`${BASE_URL}/${id}`)
  },
  changeStatus: async (id: string, status: string) => {
    await apiClient.put(`${BASE_URL}/${id}/status?status=${status}`)
  },
  toggleFeatured: async (id: string) => {
    await apiClient.put(`${BASE_URL}/${id}/featured`)
  },
  updateHomepageOrder: async (orders: { id: string; homepageSortOrder: number }[]) => {
    await apiClient.put(`${BASE_URL}/bulk-update-homepage-order`, orders)
  },
}
