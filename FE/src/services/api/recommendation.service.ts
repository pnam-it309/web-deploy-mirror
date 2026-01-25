import request from '../request'

/**
 * Product Recommendation Service
 * 
 * Provides content-based product recommendations
 */

export interface SimilarProductResponse {
  id: string
  name: string
  slug: string
  summary: string
  thumbnail: string
  viewCount: number
  isFeatured: boolean
  domainName: string
  technologyNames: string[]
}

/**
 * Get similar products based on content similarity
 * 
 * @param productId Source product ID
 * @param limit Number of recommendations (default: 5)
 */
export const getSimilarProducts = (productId: string, limit: number = 5) => {
  return request.get<SimilarProductResponse[]>(
    `/customer/recommendations/similar/${productId}`,
    { params: { limit } }
  )
}

/**
 * Get personalized recommendations for user
 * (Currently returns popular products, future: collaborative filtering)
 * 
 * @param limit Number of recommendations
 */
export const getPersonalizedRecommendations = (limit: number = 10) => {
  return request.get<SimilarProductResponse[]>(
    '/customer/recommendations/personalized',
    { params: { limit } }
  )
}
