import apiClient from '@/services/api/api'

export interface Review {
  id: string
  rating: number
  comment: string
  userEmail: string
  userName: string
  app: {
    id: string
    name: string
  }
  moderationStatus: 'PENDING' | 'APPROVED' | 'REJECTED'
  createdAt: number
  moderatedAt?: number
  moderatedBy?: string
}

export const ReviewService = {
  async getReviews(status: 'PENDING' | 'APPROVED' | 'REJECTED' | '' = '', page = 0, size = 20) {
    const response = await apiClient.get('/admin/moderation/reviews', {
      params: { status, page, size },
    })
    return response.data.data // Assuming ResponseObject structure
  },

  async approveReview(id: string) {
    return await apiClient.post(`/admin/moderation/reviews/${id}/approve`)
  },

  async rejectReview(id: string) {
    return await apiClient.post(`/admin/moderation/reviews/${id}/reject`)
  },

  async batchApprove(ids: string[]) {
    return await apiClient.post('/admin/moderation/reviews/batch-approve', ids)
  },

  async batchReject(ids: string[]) {
    return await apiClient.post('/admin/moderation/reviews/batch-reject', ids)
  },
}
