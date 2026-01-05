import apiClient from '@/services/api/api'
import { PREFIX_API_ADMIN } from '@/constants/url'

export interface AppConfig {
  isFeatured: boolean
  homepageSortOrder: number
}

// ... existing

export const getApps = async (): Promise<any[]> => {
  const response = await apiClient.get(`${PREFIX_API_ADMIN}/apps`)
  return response.data.data
}

export const getAppDetail = async (id: string): Promise<any> => {
  const response = await apiClient.get(`${PREFIX_API_ADMIN}/apps/${id}`)
  return response.data.data
}

export const createApp = async (payload: any): Promise<string> => {
  const response = await apiClient.post(`${PREFIX_API_ADMIN}/apps`, payload)
  return response.data.data
}

export const updateApp = async (id: string, payload: any): Promise<boolean> => {
  const response = await apiClient.put(`${PREFIX_API_ADMIN}/apps/${id}`, payload)
  return response.data.data
}

export const deleteApp = async (id: string): Promise<boolean> => {
  const response = await apiClient.delete(`${PREFIX_API_ADMIN}/apps/${id}`)
  return response.data.data
}

export const uploadAppImage = async (id: string, file: File): Promise<string> => {
  const formData = new FormData()
  formData.append('file', file)
  const response = await apiClient.post(`${PREFIX_API_ADMIN}/apps/${id}/images`, formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  })
  return response.data.data
}

// Members
export const searchUsers = async (query: string): Promise<any[]> => {
  const response = await apiClient.get(`${PREFIX_API_ADMIN}/apps/users/search`, {
    params: { query },
  })
  return response.data.data.data // data.data is Pageable, so .data is list
}

export const addMember = async (
  appId: string,
  payload: { customerId: string; role: string }
): Promise<boolean> => {
  const response = await apiClient.post(`${PREFIX_API_ADMIN}/apps/${appId}/members`, payload)
  return response.data.data
}

export const getMembers = async (appId: string): Promise<any[]> => {
  const response = await apiClient.get(`${PREFIX_API_ADMIN}/apps/${appId}/members`)
  return response.data.data
}

export const removeMember = async (appId: string, memberId: string): Promise<boolean> => {
  const response = await apiClient.delete(`${PREFIX_API_ADMIN}/apps/${appId}/members/${memberId}`)
  return response.data.data
}

// Gallery
export const uploadGallery = async (appId: string, files: File[]): Promise<string[]> => {
  const formData = new FormData()
  files.forEach((file) => formData.append('files', file))
  const response = await apiClient.post(`${PREFIX_API_ADMIN}/apps/${appId}/gallery`, formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  })
  return response.data.data
}

export const deleteGalleryImage = async (imageId: string, appId: string): Promise<boolean> => {
  // Delete endpoint uses generic delete or specific? Controller has deleteImage with AppID and ImageID
  // Controller: @DeleteMapping("/{id}/gallery/{imageId}")
  const response = await apiClient.delete(`${PREFIX_API_ADMIN}/apps/${appId}/gallery/${imageId}`)
  return response.data.data
}

export const updateHomepageConfig = async (id: string, payload: AppConfig): Promise<boolean> => {
  const response = await apiClient.put(`${PREFIX_API_ADMIN}/apps/${id}/homepage-config`, payload)
  return response.data.data
}

export const toggleAppStatus = async (id: string): Promise<boolean> => {
  const response = await apiClient.put(`${PREFIX_API_ADMIN}/apps/${id}/status`)
  return response.data.data
}
// Refresh HMR
