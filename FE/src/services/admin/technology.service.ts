import apiClient from '@/services/api/api'
import type { Technology } from '@/types/admin.types'
import type {
  TechnologyQueryParams,
  TechnologyCreateRequest,
  TechnologyUpdateRequest,
} from '@/types/api.types'

const BASE_URL = '/admin/technologies'

export const TechnologyService = {
  getAll: async () => {
    const response = await apiClient.get<Technology[]>(BASE_URL, { params: { unpaged: true } })
    return response.data
  },

  getPage: async (params: TechnologyQueryParams) => {
    const response = await apiClient.get(BASE_URL, { params })
    return response.data
  },

  create: async (data: TechnologyCreateRequest) => {
    const response = await apiClient.post(BASE_URL, data)
    return response.data
  },

  update: async (id: string, data: TechnologyUpdateRequest) => {
    const response = await apiClient.put(`${BASE_URL}/${id}`, data)
    return response.data
  },
  deleteTechnology: async (id: string) => {
    await apiClient.delete(`${BASE_URL}/${id}`)
  },
  downloadTemplate: async () => {
    const response = await apiClient.get('/admin/export/technologies/template', { responseType: 'blob' })
    return response.data
  },
  exportExcel: async () => {
    const response = await apiClient.get('/admin/export/technologies/excel', { responseType: 'blob' })
    return response.data
  },
  importExcel: async (file: File) => {
    const formData = new FormData()
    formData.append('file', file)
    const response = await apiClient.post('/admin/export/technologies/excel', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    })
    return response.data
  },
}
