import apiClient from '@/services/api/api'
import { PREFIX_API_ADMIN } from '@/constants/url'

export interface Feature {
  id: string
  name: string
  description: string
  imagePreview?: string
  sortOrder: number
  status?: string
  appId?: string
  appName?: string
}

export const getFeaturesByApp = async (appId: string): Promise<Feature[]> => {
  const response = await apiClient.get(`${PREFIX_API_ADMIN}/features/app/${appId}`)
  return response.data.data
}

export const getFeatures = async (): Promise<Feature[]> => {
  const response = await apiClient.get(`${PREFIX_API_ADMIN}/features`)
  return response.data.data
}

export const createFeature = async (payload: any): Promise<boolean> => {
  const response = await apiClient.post(`${PREFIX_API_ADMIN}/features`, payload)
  return response.data.data
}

export const updateFeature = async (id: string, payload: any): Promise<boolean> => {
  const response = await apiClient.put(`${PREFIX_API_ADMIN}/features/${id}`, payload)
  return response.data.data
}

export const deleteFeature = async (id: string): Promise<boolean> => {
  const response = await apiClient.delete(`${PREFIX_API_ADMIN}/features/${id}`)
  return response.data.data
}

export const toggleFeatureStatus = async (id: string): Promise<boolean> => {
  const response = await apiClient.put(`${PREFIX_API_ADMIN}/features/${id}/status`)
  return response.data.data
}

export const uploadFeatureImage = async (id: string, file: File): Promise<string> => {
  const formData = new FormData()
  formData.append('file', file)
  const response = await apiClient.post(`${PREFIX_API_ADMIN}/features/${id}/images`, formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  })
  return response.data.data
}
// Refresh HMR

// Force HMR update
