import request from '@/services/request'
import { API_CUSTOMER_ORDERS } from '@/constants/url'

export const getCustomerOrders = (params: any) => {
  return request.get(API_CUSTOMER_ORDERS, { params })
}

export const getCustomerOrderDetail = (id: string) => {
  return request.get(`${API_CUSTOMER_ORDERS}/${id}`)
}

export const cancelCustomerOrder = (id: string, reason: string) => {
  return request.put(`${API_CUSTOMER_ORDERS}/${id}/cancel`, reason, {
    headers: {
      'Content-Type': 'text/plain'
    }
  })
}
