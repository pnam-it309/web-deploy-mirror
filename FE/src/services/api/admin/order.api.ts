import { API_ADMIN_ORDER } from '@/constants/url'
import request from '@/services/request'

// ==================== Request Interfaces ====================
export interface OrderItemRequest {
  productId?: string
  productNameSnapshot: string
  unitPrice: number
  quantity: number
  unitSnapshot?: string
  notes?: string
}

export interface OrderCreateRequest {
  customerName: string
  customerEmail?: string
  customerPhone?: string
  customerCompany?: string
  notes?: string
  items: OrderItemRequest[]
}

export interface OrderUpdateRequest {
  customerName: string
  customerEmail?: string
  customerPhone?: string
  customerCompany?: string
  notes?: string
  items: OrderItemRequest[]
}

export interface OrderStatusUpdateRequest {
  status: 'ACTIVE' | 'INACTIVE'
}

// ==================== Response Interfaces ====================
export interface OrderItemResponse {
  id: string
  productId?: string
  productNameSnapshot: string
  unitPrice: number
  quantity: number
  unitSnapshot?: string
  notes?: string
  total: number
  createdAt?: number
  updatedAt?: number
}

export interface OrderResponse {
  id: string
  orderNumber: string
  customerName: string
  customerEmail?: string
  customerPhone?: string
  customerCompany?: string
  notes?: string
  status: 'ACTIVE' | 'INACTIVE'
  statusText: string
  totalEstimated: number
  items: OrderItemResponse[]
  createdAt?: number
  updatedAt?: number
}

export interface OrderSummaryResponse {
  id: string
  orderNumber: string
  customerName: string
  customerEmail?: string
  customerPhone?: string
  status: 'ACTIVE' | 'INACTIVE'
  statusText: string
  totalEstimated: number
  itemCount: number
  createdAt?: number
  updatedAt?: number
}

export interface PageResponse<T> {
  content: T[]
  totalElements: number
  totalPages: number
  number: number
  size: number
}

// ==================== API Methods ====================
const base = API_ADMIN_ORDER

export const adminOrderApi = {
  /**
   * Get all orders with pagination and filters
   */
  async list(params: { 
    search?: string
    status?: 'ACTIVE' | 'INACTIVE'
    page?: number
    size?: number
    sort?: string
  }) {
    const res = await request.get<PageResponse<OrderSummaryResponse>>(base, { params })
    return res.data
  },

  /**
   * Get order by ID
   */
  async getById(id: string) {
    const res = await request.get<OrderResponse>(`${base}/${id}`)
    return res.data
  },

  /**
   * Create new order
   */
  async create(payload: OrderCreateRequest) {
    const res = await request.post<OrderResponse>(base, payload)
    return res.data
  },

  /**
   * Update existing order
   */
  async update(id: string, payload: OrderUpdateRequest) {
    const res = await request.put<OrderResponse>(`${base}/${id}`, payload)
    return res.data
  },

  /**
   * Update order status
   */
  async updateStatus(id: string, payload: OrderStatusUpdateRequest) {
    const res = await request.patch<OrderResponse>(`${base}/${id}/status`, payload)
    return res.data
  },

  /**
   * Delete order
   */
  async delete(id: string) {
    await request.delete(`${base}/${id}`)
  },
}
