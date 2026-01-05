import apiClient from '@/services/api/api'
import { PREFIX_API_CUSTOMER } from '@/constants/url'

export const incrementViewCount = async (id: string): Promise<boolean> => {
  const response = await apiClient.post(`${PREFIX_API_CUSTOMER}/apps/${id}/view`)
  return response.data.data
}

