import apiClient from '@/services/api/api'
import { PREFIX_API_ADMIN } from '@/constants/url'

export interface DashboardStats {
  totalDomains: number
  totalApps: number
  totalFeatures: number
  totalActiveUsers: number
  domainDistribution?: Record<string, number>
  statusDistribution?: Record<string, number>
  topViewedApps?: {
    id: string
    name: string
    viewCount: number
    thumbnail: string
  }[]
}

export const getDashboardStats = async (): Promise<DashboardStats> => {
  const response = await apiClient.get(`${PREFIX_API_ADMIN}/dashboard/stats`)
  return response.data.data // Assuming standard ResponseObject wrapper returns data in .data
}
