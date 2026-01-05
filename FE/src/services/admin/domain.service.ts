import apiClient from '@/services/api/api'
import { PREFIX_API_ADMIN } from '@/constants/url'

export interface Domain {
    id: string
    name: string
    description: string
    icon: string
    slug: string
    parentId?: string
    sortOrder: number
}

export const getDomains = async (): Promise<Domain[]> => {
    const response = await apiClient.get(`${PREFIX_API_ADMIN}/domains`)
    return response.data.data
}

export const createDomain = async (payload: any): Promise<boolean> => {
    const response = await apiClient.post(`${PREFIX_API_ADMIN}/domains`, payload)
    return response.data.data
}

export const updateDomain = async (id: string, payload: any): Promise<boolean> => {
    const response = await apiClient.put(`${PREFIX_API_ADMIN}/domains/${id}`, payload)
    return response.data.data
}

export const deleteDomain = async (id: string): Promise<boolean> => {
    const response = await apiClient.delete(`${PREFIX_API_ADMIN}/domains/${id}`)
    return response.data.data
}
