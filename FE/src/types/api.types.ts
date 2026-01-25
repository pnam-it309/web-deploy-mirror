/**
 * API Types
 * Type definitions for API requests and responses
 */

// ============================================
// Pagination Types
// ============================================

export interface PaginatedResponse<T> {
  content: T[]
  totalPages: number
  totalElements: number
  number: number
  size: number
}

export interface PageParams {
  page?: number
  size?: number
}

export interface SearchParams extends PageParams {
  search?: string
}

// ============================================
// App/Product Types
// ============================================

export interface AppQueryParams extends SearchParams {
  domainId?: string
  technologyIds?: string[]
  status?: string
  sortBy?: string
  sortDirection?: 'asc' | 'desc'
}

export interface AppCreateRequest {
  name: string
  description: string
  shortDescription?: string
  thumbnailUrl?: string
  domainId: string
  technologies: string[]
  features?: string[]
  specifications?: Record<string, unknown>
  videoUrl?: string
  demoUrl?: string
  sourceCodeUrl?: string
  teamSize?: number
  developmentDuration?: number
  status?: 'DRAFT' | 'PUBLISHED' | 'ARCHIVED'
}

export interface AppUpdateRequest extends Partial<AppCreateRequest> {
  id: string
}

export interface AppDetailExtensionRequest {
  appId: string
  detailDescription?: string
  installation?: string
  usage?: string
  features?: string
  screenshots?: string[]
  relatedApps?: string[]
}

// ============================================
// Domain Types
// ============================================

export interface DomainQueryParams extends SearchParams {
  status?: string
}

export interface DomainCreateRequest {
  name: string
  code?: string
  description?: string
  icon?: string
  orderIndex?: number
}

export interface DomainUpdateRequest extends Partial<DomainCreateRequest> {
  id: string
}

export interface DomainOrderRequest {
  id: string
  orderIndex: number
}

// ============================================
// Feature Types
// ============================================

export interface FeatureQueryParams extends SearchParams {
  appId?: string
}

export interface FeatureCreateRequest {
  name: string
  description?: string
  icon?: string
  orderIndex?: number
  appId?: string
}

export interface FeatureUpdateRequest extends Partial<FeatureCreateRequest> {
  id: string
}

export interface FeatureOrderRequest {
  id: string
  orderIndex: number
}

// ============================================
// Technology Types
// ============================================

export interface TechnologyQueryParams extends SearchParams {
  category?: string
}

export interface TechnologyCreateRequest {
  name: string
  code?: string
  icon?: string
  category?: string
  version?: string
  description?: string
}

export interface TechnologyUpdateRequest extends Partial<TechnologyCreateRequest> {
  id: string
}

// ============================================
// Media Types
// ============================================

export interface MediaUploadRequest {
  file: File
  folder?: string
  tags?: string[]
}

export interface MediaResponse {
  id: string
  url: string
  thumbnailUrl?: string
  type: 'IMAGE' | 'VIDEO' | 'DOCUMENT'
  size: number
  filename: string
  uploadedAt: number
}

// ============================================
// Analytics Types
// ============================================

export interface SearchAnalyticsParams {
  startDate?: string
  endDate?: string
  period?: 'day' | 'week' | 'month'
  limit?: number
}

export interface SearchAnalyticsResponse {
  keyword: string
  count: number
  lastSearched: number
}

export interface ViewAnalyticsResponse {
  appId: string
  appName: string
  viewCount: number
  period: string
}

// ============================================
// Error Response Types
// ============================================

export interface ApiError {
  status: number
  message: string
  timestamp: number
  path?: string
  errors?: Record<string, string[]>
}

export interface ValidationError {
  field: string
  message: string
  rejectedValue?: any
}

// ============================================
// Webhook Types
// ============================================

export interface WebhookCreateRequest {
  url: string
  events: string[]
  secret?: string
  active?: boolean
}

export interface WebhookResponse {
  id: string
  url: string
  events: string[]
  secret?: string
  active: boolean
  createdAt: number
}

// ============================================
// Role & Permission Types
// ============================================

export interface RoleCreateRequest {
  name: string
  code: string
  description?: string
  permissions?: string[]
}

export interface PermissionResponse {
  id: string
  code: string
  name: string
  module: string
  description?: string
}
