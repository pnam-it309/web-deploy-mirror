import apiClient from '@/services/api/api'
import { PREFIX_API_CUSTOMER } from '@/constants/url'
import type { PublicProduct } from './client.service'

export const incrementViewCount = async (id: string): Promise<boolean> => {
  const response = await apiClient.post(`${PREFIX_API_CUSTOMER}/apps/${id}/view`)
  return response.data.data
}

export const getRelatedProducts = async (id: string): Promise<PublicProduct[]> => {
  const response = await apiClient.get(`${PREFIX_API_CUSTOMER}/apps/${id}/related`)
  return response.data.data
}
