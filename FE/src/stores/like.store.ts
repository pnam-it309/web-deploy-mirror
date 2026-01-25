import { defineStore } from 'pinia'
import apiClient from '@/services/api/api'

export const useLikeStore = defineStore('like', () => {
  const toggleLike = async (productId: string) => {
    try {
      const response = await apiClient.post(`/customer/likes/${productId}/toggle`)
      return response.data
    } catch (error) {
      throw error
    }
  }

  const getLikeStatus = async (productId: string) => {
    try {
      const response = await apiClient.get(`/customer/likes/${productId}/status`)
      return response.data
    } catch (error) {
      console.error('Get like status error:', error)
      return { isLiked: false, likeCount: 0 }
    }
  }

  return {
    toggleLike,
    getLikeStatus,
  }
})
