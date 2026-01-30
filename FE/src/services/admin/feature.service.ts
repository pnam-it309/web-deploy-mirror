import apiClient from '@/services/api/api'
import type {
  FeatureCreateRequest,
  FeatureUpdateRequest,
  FeatureOrderRequest,
} from '@/types/api.types'

const BASE_URL = '/admin/features'

export const FeatureService = {
  getAll: async () => {
    const response = await apiClient.get(BASE_URL)
    return response.data
  },
  getByAppId: async (appId: string) => {
    const response = await apiClient.get(`${BASE_URL}/by-app/${appId}`)
    return response.data
  },

  create: async (data: FeatureCreateRequest) => {
    const response = await apiClient.post(BASE_URL, data)
    return response.data
  },

  update: async (id: string, data: FeatureUpdateRequest) => {
    const response = await apiClient.put(`${BASE_URL}/${id}`, data)
    return response.data
  },

  deleteFeature: async (id: string) => {
    await apiClient.delete(`${BASE_URL}/${id}`)
  },
  updateOrder: async (orders: FeatureOrderRequest[] | { id: string; sortOrder: number }[]) => {
    await apiClient.put(`${BASE_URL}/bulk-update-order`, orders)
  },
  downloadTemplate: async () => {
    const response = await apiClient.get('/admin/export/features/template', { responseType: 'blob' })
    return response.data
  },
  exportExcel: async () => {
    const response = await apiClient.get('/admin/export/features/excel', { responseType: 'blob' })
    return response.data
  },
  importExcel: async (file: File) => {
    const formData = new FormData()
    formData.append('file', file)
    const response = await apiClient.post('/admin/export/features/excel', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    })
    return response.data
  },
}
