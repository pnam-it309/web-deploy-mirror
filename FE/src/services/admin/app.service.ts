import apiClient from '@/services/api/api'
import { PREFIX_API_ADMIN } from '@/constants/url'

export interface AdminUpdateAppConfigRequest {
  isFeatured?: boolean
  homepageSortOrder?: number
}

export const updateHomepageConfig = async (id: string, request: AdminUpdateAppConfigRequest): Promise<boolean> => {
  const response = await apiClient.put(`${PREFIX_API_ADMIN}/apps/${id}/homepage-config`, request)
  return response.data.data
}

