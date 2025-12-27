import apiClient from '@/services/api/api'
import { PREFIX_API_ADMIN } from '@/constants/url'

export interface Technology {
    id: string
    name: string
    icon: string
}

export const getTechnologies = async (): Promise<Technology[]> => {
    const response = await apiClient.get(`${PREFIX_API_ADMIN}/technologies`)
    return response.data.data
}

export const createTechnology = async (payload: any): Promise<boolean> => {
    const response = await apiClient.post(`${PREFIX_API_ADMIN}/technologies`, payload)
    return response.data.data
}

export const updateTechnology = async (id: string, payload: any): Promise<boolean> => {
    const response = await apiClient.put(`${PREFIX_API_ADMIN}/technologies/${id}`, payload)
    return response.data.data
}

export const deleteTechnology = async (id: string): Promise<boolean> => {
    const response = await apiClient.delete(`${PREFIX_API_ADMIN}/technologies/${id}`)
    return response.data.data
}
