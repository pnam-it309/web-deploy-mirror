import apiClient from '@/services/api/api'
import type {
  AppQueryParams,
  AppCreateRequest,
  AppUpdateRequest,
  AppDetailExtensionRequest,
  PaginatedResponse,
} from '@/types/api.types'

const BASE_URL = '/admin/apps'

export const AppService = {
  getAll: async (params: AppQueryParams) => {
    const response = await apiClient.get<PaginatedResponse<any>>(BASE_URL, { params })
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
  create: async (data: AppCreateRequest) => {
    const response = await apiClient.post(BASE_URL, data)
    return response.data
  },
  update: async (id: string, data: AppUpdateRequest) => {
    const response = await apiClient.put(`${BASE_URL}/${id}`, data)
    return response.data
  },
  updateDetailExtension: async (appId: string, data: AppDetailExtensionRequest) => {
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
  duplicate: async (id: string) => {
    const response = await apiClient.post(`${BASE_URL}/${id}/duplicate`)
    return response.data
  },
  exportApps: async () => {
    const response = await apiClient.get('/admin/export/apps', {
      responseType: 'blob',
    })
    return response.data
  },
  importApps: async (file: File) => {
    const formData = new FormData()
    formData.append('file', file)
    const response = await apiClient.post('/admin/import/apps', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })
    return response.data
  },
}
