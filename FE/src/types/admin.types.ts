// src/types/admin.types.ts
export interface PageableResponse<T> {
  content: T[]
  totalPages: number
  totalElements: number
  size: number
  number: number
  last: boolean
  first: boolean
  empty: boolean
}

export type ApprovalStatus = 'PENDING' | 'APPROVED' | 'REJECTED' | 'DRAFT'
export interface Domain {
  id: string
  name: string
  slug: string
  description?: string
  icon?: string
  sortOrder?: number
  color?: string
  status?: string
}

export interface Technology {
  id: string
  name: string
  icon?: string
}
export interface AppMemberRequest {
  customerId: string
  role: 'LEADER' | 'MEMBER'
}

export interface AppMemberResponse {
  id: string
  customerId: string | null
  fullName: string
  email: string
  avatar: string | null

  role: 'LEADER' | 'MEMBER'
  isGuest: boolean
}

export interface AppImageRequest {
  url: string
  isMain: boolean
}

export interface AppImageResponse {
  id: string
  url: string
  isMain: boolean
}
export interface AppCreateRequest {
  name: string
  sku?: string
  shortDescription?: string
  thumbnail?: string
  domainId: string
  technologyIds: string[]
}
export interface AppUpdateRequest {
  name: string
  sku?: string
  shortDescription?: string
  thumbnail?: string
  domainId: string
  technologyIds: string[]
  approvalStatus?: ApprovalStatus
  members: AppMemberRequest[]
  images: AppImageRequest[]
}
export interface AppResponse {
  id: string
  name: string
  sku: string
  shortDescription: string
  thumbnail: string
  viewCount: number
  approvalStatus: ApprovalStatus
  domainId: string
  domainName: string
  technologies: Technology[]
  members: AppMemberResponse[]
  images: AppImageResponse[]
  isFeatured?: boolean
  homepageSortOrder?: number
}
export interface AppDetailResponse {
  id: string
  appId: string
  longDescription: string
  demoUrl: string
  sourceUrl: string
  specifications: any
}

export interface AppDetailUpdateRequest {
  longDescription: string
  demoUrl: string
  sourceUrl: string
  specifications: any
}
export interface Feature {
  id: string
  name: string
  description?: string
  imagePreview?: string
  appId: string
  sortOrder: number
}

export interface FeatureRequest {
  name: string
  description?: string
  imagePreview?: string
  appId: string
  sortOrder?: number
}
export interface CustomerSearchResponse {
  id: string
  fullName: string
  email: string
  avatar: string | null
  code: string
}
export interface DashboardResponse {
  totalApps: number
  totalViews: number
  totalDomains: number
  totalTechnologies: number
  totalStudents: number
}
