import { PREFIX_API_AUTH } from '@/constants/url'
import request from '@/services/request'

export interface UserProfile {
  id: string
  name: string
  email: string
  avatar: string
  roles: string[]
}

export const authService = {
  getCurrentUser: async () => {
    return await request.get<UserProfile>(`${PREFIX_API_AUTH}/me`)
  },
  logout: async () => {
    return await request.post(`${PREFIX_API_AUTH}/logout`)
  },
}
