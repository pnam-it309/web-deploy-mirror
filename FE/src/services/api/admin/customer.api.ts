import { API_ADMIN_CUSTOMER } from '@/constants/url'
import request from '@/services/request'

export interface CustomerRequest {
  code: string
  name: string
  email: string
  picture: string
}

export interface CustomerResponse {
  id: string
  code: string
  name: string
  email: string
  picture: string
  status: 'ACTIVE' | 'INACTIVE'
}

export interface PageResponse<T> {
  content: T[]
  totalElements: number
  totalPages: number
  number: number
  size: number
}

const base = API_ADMIN_CUSTOMER

export const adminCustomerApi = {
  async list(params: { search?: string; status?: string; page?: number; size?: number }) {
    const res = await request.get<PageResponse<CustomerResponse>>(base, { params })
    return res.data
  },

  async create(payload: CustomerRequest) {
    const res = await request.post<CustomerResponse>(base, payload)
    return res.data
  },

  async update(id: string, payload: CustomerRequest) {
    const res = await request.put<CustomerResponse>(`${base}/${id}`, payload)
    return res.data
  },

  async toggleStatus(id: string) {
    const res = await request.patch<CustomerResponse>(`${base}/${id}/toggle-status`)
    return res.data
  },
}
