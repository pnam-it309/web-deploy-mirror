import { UserInformation } from './auth.type'

declare module '@/stores/auth' {
  export interface AuthState {
    user: UserInformation | null
    accessToken: string | null
    refreshToken: string | null
    userRole: string | null
    isAuthenticated: boolean
  }
  
  export interface AuthActions {
    setTokens(access: string | null, refresh: string | null): void
    loginWithGoogle(): Promise<{ success: boolean; redirectUrl?: string }>
    handleOAuthCallback(code?: string, state?: string): Promise<{ user: UserInformation | null }>
    logout(): void
    setUserRole(role: string): void
    clearUserRole(): void
    initializeAuth(): void
  }
  
  export interface AuthGetters {
    isAuthenticated: boolean
  }
  
  export interface AuthStore extends AuthState, AuthActions, AuthGetters {}
}
