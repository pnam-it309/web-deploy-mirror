import apiClient from '@/services/api/api'
import { PREFIX_API_ADMIN } from '@/constants/url'

export interface SearchKeywordResponse {
  keyword: string
  searchCount: number
}

export interface SearchTrendResponse {
  date: string
  searchCount: number
}

export interface SearchQueryData {
  id: string
  queryText: string
  resultCount: number
  hasResults: boolean
  category: string
  createdAt: number
}

const searchAnalyticsService = {
  getTopKeywords: async (
    period: string = 'week',
    limit: number = 20
  ): Promise<SearchKeywordResponse[]> => {
    const response = await apiClient.get(`${PREFIX_API_ADMIN}/analytics/search/top-keywords`, {
      params: { period, limit },
    })
    return response.data
  },

  getNoResultQueries: async (period: string = 'week'): Promise<SearchQueryData[]> => {
    const response = await apiClient.get(`${PREFIX_API_ADMIN}/analytics/search/no-results`, {
      params: { period },
    })
    return response.data
  },

  getSearchTrends: async (period: string = 'week'): Promise<SearchTrendResponse[]> => {
    const response = await apiClient.get(`${PREFIX_API_ADMIN}/analytics/search/trends`, {
      params: { period },
    })
    return response.data
  },
}

export default searchAnalyticsService
