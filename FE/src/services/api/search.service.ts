import request from '../request'

/**
 * Meilisearch Search Service
 * 
 * Provides instant, typo-tolerant product search
 */

export interface SearchParams {
  q: string
  domainId?: string
  techIds?: string // Comma-separated tech IDs
  limit?: number
}

export interface SearchResponse {
  hits: any[]
  query: string
  processingTimeMs: number
  estimatedTotalHits: number
}

/**
 * Search products with Meilisearch
 * 
 * @param params Search parameters
 * @returns Search results với highlighted matches
 */
export const searchProducts = (params: SearchParams) => {
  return request.get<SearchResponse>('/customer/search', { params })
}

/**
 * Get autocomplete suggestions
 * 
 * @param query Search query
 * @returns Top 5 suggestions
 */
export const getSuggestions = (query: string) => {
  return request.get('/customer/search/suggest', { params: { q: query } })
}
