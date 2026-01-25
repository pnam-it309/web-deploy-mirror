/**
 * User & Auth Types
 * Type definitions for users, roles, and authentication
 */

// ============================================
// User Types
// ============================================

export interface User {
  id: string
  email: string
  username?: string
  name: string
  firstName?: string
  lastName?: string
  avatar?: string
  phone?: string
  address?: string
  bio?: string
  roles: Role[]
  permissions?: Permission[]
  status?: 'ACTIVE' | 'INACTIVE' | 'SUSPENDED'
  emailVerified?: boolean
  createdAt?: number
  updatedAt?: number
  lastLoginAt?: number
}

export interface UserProfile extends User {
  preferences?: UserPreferences
  statistics?: UserStatistics
}

export interface UserPreferences {
  language?: string
  theme?: 'light' | 'dark' | 'auto'
  notifications?: NotificationPreferences
  privacy?: PrivacySettings
}

export interface UserStatistics {
  appsCreated?: number
  appsViewed?: number
  bookmarks?: number
  comparisons?: number
}

// ============================================
// Role Types
// ============================================

export interface Role {
  id: string
  name: string
  code: string
  description?: string
  permissions?: Permission[]
  isDefault?: boolean
  createdAt?: number
  updatedAt?: number
}

export interface RoleSummary {
  id: string
  name: string
  code: string
  userCount?: number
}

// ============================================
// Permission Types
// ============================================

export interface Permission {
  id: string
  code: string
  name: string
  module: string
  description?: string
  actions?: PermissionAction[]
}

export type PermissionAction = 'CREATE' | 'READ' | 'UPDATE' | 'DELETE' | 'EXECUTE'

export interface PermissionGroup {
  module: string
  permissions: Permission[]
}

// ============================================
// Auth Types
// ============================================

export interface LoginRequest {
  username: string
  password: string
  rememberMe?: boolean
}

export interface LoginResponse {
  accessToken: string
  refreshToken: string
  tokenType?: string
  expiresIn?: number
  user: User
}

export interface RegisterRequest {
  email: string
  password: string
  confirmPassword: string
  name: string
  agreeToTerms: boolean
}

export interface OAuth2Request {
  provider: 'GOOGLE' | 'FACEBOOK' | 'GITHUB'
  code: string
  state?: string
  redirectUri?: string
}

export interface ResetPasswordRequest {
  email: string
}

export interface ChangePasswordRequest {
  currentPassword: string
  newPassword: string
  confirmPassword: string
}

// ============================================
// Session Types
// ============================================

export interface SessionData {
  user: User
  accessToken: string
  refreshToken: string
  expiresAt: number
  rolesNames: string[]
  rolesCodes: string[]
  permissions?: string[]
}

export interface TokenPayload {
  sub: string // User ID
  email: string
  name: string
  roles: string[]
  iat: number // Issued at
  exp: number // Expires at
}

// ============================================
// Notification Preferences
// ============================================

export interface NotificationPreferences {
  email?: boolean
  push?: boolean
  inApp?: boolean
  frequency?: 'realtime' | 'daily' | 'weekly'
  types?: {
    newProduct?: boolean
    productUpdate?: boolean
    comment?: boolean
    mention?: boolean
    newsletter?: boolean
  }
}

// ============================================
// Privacy Settings
// ============================================

export interface PrivacySettings {
  profileVisibility?: 'PUBLIC' | 'PRIVATE' | 'FRIENDS'
  showEmail?: boolean
  showPhone?: boolean
  allowMessages?: boolean
  allowFriendRequests?: boolean
}

// ============================================
// User Status Enums
// ============================================

export enum UserStatus {
  ACTIVE = 'ACTIVE',
  INACTIVE = 'INACTIVE',
  SUSPENDED = 'SUSPENDED',
  PENDING = 'PENDING',
}

export enum UserType {
  ADMIN = 'ADMIN',
  CUSTOMER = 'CUSTOMER',
  GUEST = 'GUEST',
}

// ============================================
// Admin User Management Types
// ============================================

export interface UserQueryParams {
  search?: string
  role?: string
  status?: UserStatus
  page?: number
  size?: number
  sortBy?: 'name' | 'email' | 'createdAt' | 'lastLoginAt'
  sortDirection?: 'asc' | 'desc'
}

export interface UserCreateRequest {
  email: string
  name: string
  password: string
  roles: string[]
  status?: UserStatus
}

export interface UserUpdateRequest {
  id: string
  name?: string
  email?: string
  roles?: string[]
  status?: UserStatus
  avatar?: string
}

// ============================================
// Role Assignment Types
// ============================================

export interface RoleAssignment {
  userId: string
  roleIds: string[]
}

export interface PermissionCheck {
  userId: string
  permission: string
  resource?: string
}
