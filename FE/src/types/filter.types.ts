/**
 * Filter Types
 * Type definitions for filter parameters and state
 */

// ============================================
// Product/App Filter Types
// ============================================

export interface ProductFilterParams {
  search?: string
  domain?: string | string[]
  domainId?: string
  technologies?: string[]
  technologyIds?: string[]
  status?: 'DRAFT' | 'PUBLISHED' | 'ARCHIVED' | string
  startDate?: string | Date
  endDate?: string | Date
  minTeamSize?: number
  maxTeamSize?: number
  sortBy?: 'name' | 'createdAt' | 'viewCount' | 'rating'
  sortDirection?: 'asc' | 'desc'
  page?: number
  size?: number
}

// ============================================
// Advanced Filter Types
// ============================================

export interface AdvancedFilterParams extends ProductFilterParams {
  features?: string[]
  hasDemo?: boolean
  hasSourceCode?: boolean
  hasVideo?: boolean
  minRating?: number
  developmentYear?: number
  developmentPeriodMin?: number // In months
  developmentPeriodMax?: number
}

// ============================================
// Domain Filter Types
// ============================================

export interface DomainFilterParams {
  search?: string
  status?: 'ACTIVE' | 'INACTIVE' | string
  hasApps?: boolean
}

// ============================================
// Technology Filter Types
// ============================================

export interface TechnologyFilterParams {
  search?: string
  category?: string
  version?: string
}

// ============================================
// Analytics Filter Types
// ============================================

export interface AnalyticsFilterParams {
  startDate: string | Date
  endDate: string | Date
  period?: 'hour' | 'day' | 'week' | 'month' | 'year'
  groupBy?: 'domain' | 'technology' | 'user'
  limit?: number
}

// ============================================
// Search Filter Types
// ============================================

export interface SearchFilterParams {
  query: string
  filters?: {
    domains?: string[]
    technologies?: string[]
    dateRange?: {
      start: string | Date
      end: string | Date
    }
  }
  facets?: string[] // For faceted search
  highlight?: boolean
}

// ============================================
// Table Filter Types (for Admin)
// ============================================

export interface TableFilterState {
  search?: string
  filters?: Record<string, any>
  sorter?: {
    field: string
    order: 'ascend' | 'descend' | null
  }
  pagination?: {
    current: number
    pageSize: number
    total?: number
  }
}

// ============================================
// Date Range Filter Types
// ============================================

export interface DateRangeFilter {
  start: string | Date | null
  end: string | Date | null
  preset?: 'today' | 'yesterday' | 'last7days' | 'last30days' | 'thisMonth' | 'lastMonth' | 'custom'
}

// ============================================
// Multi-Select Filter Types
// ============================================

export interface MultiSelectFilter {
  selected: string[]
  options: FilterOption[]
  searchable?: boolean
}

export interface FilterOption {
  value: string
  label: string
  count?: number
  disabled?: boolean
}

// ============================================
// Filter State Management
// ============================================

export interface FilterState {
  product?: ProductFilterParams
  domain?: DomainFilterParams
  technology?: TechnologyFilterParams
  analytics?: AnalyticsFilterParams
  dateRange?: DateRangeFilter
}

export interface FilterActions {
  setFilter: (key: string, value: any) => void
  clearFilter: (key?: string) => void
  resetFilters: () => void
  applyFilters: () => void
}

// ============================================
// URL Query Params (for routing)
// ============================================

export type QueryParams = Record<string, string | string[] | undefined>

// ============================================
// Helper Types
// ============================================

export type SortDirection = 'asc' | 'desc' | 'ascend' | 'descend'

export type FilterOperator = 'eq' | 'ne' | 'gt' | 'gte' | 'lt' | 'lte' | 'like' | 'in' | 'between'

export interface FilterCriteria {
  field: string
  operator: FilterOperator
  value: any
}
