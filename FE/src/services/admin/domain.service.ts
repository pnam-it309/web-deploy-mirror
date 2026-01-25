import apiClient from '@/services/api/api'
import type { Domain } from '@/types/admin.types'
import type {
  DomainQueryParams,
  DomainCreateRequest,
  DomainUpdateRequest,
  DomainOrderRequest,
} from '@/types/api.types'

const BASE_URL = '/admin/domains'

export const DomainService = {
  getAll: async () => {
    const response = await apiClient.get<Domain[]>(BASE_URL, { params: { unpaged: true } })
    return response.data
  },
  getPage: async (params: DomainQueryParams) => {
    const response = await apiClient.get(BASE_URL, { params })
    return response.data
  },

  create: async (data: DomainCreateRequest) => {
    const response = await apiClient.post(BASE_URL, data)
    return response.data
  },

  update: async (id: string, data: DomainUpdateRequest) => {
    const response = await apiClient.put(`${BASE_URL}/${id}`, data)
    return response.data
  },
  deleteDomain: async (id: string) => {
    await apiClient.delete(`${BASE_URL}/${id}`)
  },
  updateOrder: async (orders: DomainOrderRequest[] | { id: string; sortOrder: number }[]) => {
    await apiClient.put(`${BASE_URL}/bulk-update-order`, orders)
  },
}
