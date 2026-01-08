import request from '@/services/request'
import { API_CUSTOMER_DOMAIN, API_CUSTOMER_APP, API_CUSTOMER_TECHNOLOGY } from '@/constants/url'

export interface PublicDomain {
  id: string
  name: string
  slug: string
  icon: string
  productCount?: number
}

export interface PublicProduct {
  id: string
  name: string
  description: string
  thumbnail: string
  domainId: string
  domainName: string
  technologies: { id: string; name: string; icon: string }[]
}

export interface ProductDetail extends PublicProduct {
  sku?: string
  shortDescription?: string
  longDescription?: string
  demoUrl?: string
  sourceUrl?: string
  specifications?: any
  viewCount?: number
  features: {
    id: string
    name: string
    description: string
    imagePreview: string
    videoUrl?: string
  }[]
  teamMembers: { id: string; fullName: string; role: string }[]
  // images: ... (Add later if backend supports gallery)
}

export interface PublicTechnology {
  id: string
  name: string
  icon: string
}

export interface ProductFilterParams {
  page?: number
  size?: number
  query?: string
  domainId?: string
  technologyIds?: string[] // comma separated or array
  sort?: string
}

export const getPublicDomains = async (): Promise<PublicDomain[]> => {
  const res = await request.get(API_CUSTOMER_DOMAIN)
  return res.data.data
}

export const getPublicFeaturedProducts = async (params?: ProductFilterParams): Promise<any> => {
  // Return any to handle pagination response structure: { data: [], totalPages: 1, ... }
  const res = await request.get(API_CUSTOMER_APP, { params })
  return res.data.data
}

export const getProductDetail = async (id: string): Promise<ProductDetail | null> => {
  const res = await request.get(`${API_CUSTOMER_APP}/${id}`)
  return res.data.data
}

export const getTechnologies = async (): Promise<PublicTechnology[]> => {
  const res = await request.get(API_CUSTOMER_TECHNOLOGY)
  return res.data.data
}

export const incrementViewCount = async (id: string): Promise<void> => {
  await request.post(`${API_CUSTOMER_APP}/${id}/view`)
}
