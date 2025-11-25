import {
  ACCESS_TOKEN_STORAGE_KEY,
  REFRESH_TOKEN_STORAGE_KEY,
  USER_INFO_STORAGE_KEY
} from '@/constants/storagekey'
import type { UserInformation } from '@/types/auth.type'
import { localStorageAction } from '@/utils/storage'
import { defineStore } from 'pinia'
import { getExpireTime } from '@/utils/token.helper'
import { computed, ref } from 'vue'

export const useAuthStore = defineStore('auth', () => {
  // State
  const user = ref<UserInformation | null>(localStorageAction.get(USER_INFO_STORAGE_KEY) || null)
  const accessToken = ref<string | null>(localStorageAction.get(ACCESS_TOKEN_STORAGE_KEY) || null)
  const refreshToken = ref<string | null>(localStorageAction.get(REFRESH_TOKEN_STORAGE_KEY) || null)
  const userRole = ref<string | null>(localStorage.getItem('userRole') || null)

  // Getters
  const isAuthenticated = computed(() => {
    if (!accessToken.value) return false

    try {
      const expire = getExpireTime(accessToken.value)
      return Date.now() < expire * 1000
    } catch (e) {
      return false
    }
  })

  // Actions
  const setTokens = (access: string | null, refresh: string | null) => {
    accessToken.value = access
    refreshToken.value = refresh
    
    if (access) {
      localStorageAction.set(ACCESS_TOKEN_STORAGE_KEY, access)
    } else {
      localStorageAction.remove(ACCESS_TOKEN_STORAGE_KEY)
    }
    
    if (refresh) {
      localStorageAction.set(REFRESH_TOKEN_STORAGE_KEY, refresh)
    } else {
      localStorageAction.remove(REFRESH_TOKEN_STORAGE_KEY)
    }
  }

  const loginWithGoogle = async (): Promise<{ success: boolean; redirectUrl?: string }> => {
    try {
      // Get Google Client ID from environment
      const clientId = import.meta.env.VITE_GOOGLE_CLIENT_ID

      if (!clientId || clientId === 'your_google_client_id_here' || clientId === 'your_actual_google_client_id_here') {
        console.error('Google Client ID not configured. Please set VITE_GOOGLE_CLIENT_ID in .env file with your actual Google OAuth2 Client ID')
        alert('Google OAuth2 chưa được cấu hình. Vui lòng liên hệ admin để cấu hình Google Client ID.')
        return { success: false }
      }

      // Use backend OAuth2 endpoint - backend will handle the OAuth2 flow
      const backendUrl = `${import.meta.env.VITE_BASE_URL_SERVER}/oauth2/authorization/google?redirect_uri=${encodeURIComponent(window.location.origin)}`

      console.log('Redirecting to backend OAuth2 authorization:', backendUrl)
      window.location.href = backendUrl

      return { success: true }
    } catch (error) {
      console.error('Google login error:', error)
      return { success: false }
    }
  }

  // Handle OAuth callback - This will be called when backend redirects back to frontend
  const handleOAuthCallback = async (code?: string, state?: string): Promise<{ user: UserInformation | null }> => {
    try {
      console.log('OAuth callback received:', { code, state })

      // Try to get current user info from localStorage first
      const existingUser = localStorageAction.get(USER_INFO_STORAGE_KEY)
      const existingToken = localStorageAction.get(ACCESS_TOKEN_STORAGE_KEY)

      if (existingUser && existingToken) {
        console.log('Found existing auth data, checking token validity')

        // Check if token is still valid
        try {
          const expire = getExpireTime(existingToken)
          if (Date.now() < expire * 1000) {
            console.log('Token still valid, using existing auth data')
            return { user: existingUser }
          }
        } catch (e) {
          console.log('Token expired or invalid')
        }
      }

      // If no valid token, try to get user info from backend session/profile
      console.log('Attempting to get user info from backend session')

      // Try to get user info from a protected endpoint using session cookies
      try {
        const userResponse = await fetch(`${import.meta.env.VITE_BASE_URL_SERVER}/api/admin/profile`, {
          credentials: 'include' // Include cookies
        })

        if (userResponse.ok) {
          const userData = await userResponse.json()
          console.log('Got user data from session:', userData)

          // Create user object from session data
          const sessionUserData: UserInformation = {
            userId: userData.id || '',
            userCode: userData.code || userData.id || '',
            fullName: userData.name || '',
            email: userData.email || '',
            pictureUrl: userData.picture || '',
            rolesNames: userData.roles?.map((r: any) => r.name) || [],
            rolesCodes: userData.roles?.map((r: any) => r.code) || [],
            roleScreen: userData.roleScreen || 'ADMIN',
            idFacility: userData.idFacility,
            roleSwitch: userData.roleSwitch
          }

          // Save user data (but no tokens since using session)
          user.value = sessionUserData
          localStorageAction.set(USER_INFO_STORAGE_KEY, sessionUserData)

          // Set user role for routing
          if (sessionUserData.roleScreen) {
            setUserRole(sessionUserData.roleScreen)
          }

          return { user: sessionUserData }
        }
      } catch (error) {
        console.log('Could not get user data from session:', error)
      }

      // If that doesn't work, try with Authorization header if we have existing token
      if (existingToken) {
        try {
          const userResponse = await fetch(`${import.meta.env.VITE_BASE_URL_SERVER}/api/admin/profile`, {
            headers: {
              'Authorization': `Bearer ${existingToken}`,
            }
          })

          if (userResponse.ok) {
            const userData = await userResponse.json()
            console.log('Got user data from existing token:', userData)

            if (existingUser) {
              return { user: existingUser }
            }
          }
        } catch (error) {
          console.log('Could not get user data from existing token:', error)
        }
      }

      console.log('OAuth callback failed - no valid authentication found')
      return { user: null }
    } catch (error) {
      console.error('OAuth callback error:', error)
      return { user: null }
    }
  }

  const logout = async () => {
    try {
      // Call backend logout if needed
      if (accessToken.value) {
        await fetch(`${import.meta.env.VITE_BASE_URL_SERVER}/auth/logout`, {
          method: 'POST',
          headers: {
            'Authorization': `Bearer ${accessToken.value}`
          }
        })
      }
    } catch (error) {
      console.error('Logout error:', error)
    } finally {
      // Clear all auth data
      user.value = null
      setTokens(null, null)
      clearUserRole()
      localStorageAction.remove(USER_INFO_STORAGE_KEY)
    }
  }

  const setUserRole = (role: string) => {
    userRole.value = role
    localStorage.setItem('userRole', role)
  }

  const clearUserRole = () => {
    userRole.value = null
    localStorage.removeItem('userRole')
  }

  // Initialize auth state from localStorage
  const initializeAuth = async () => {
    try {
      console.log('Initializing auth state...')
      const storedUser = localStorageAction.get(USER_INFO_STORAGE_KEY)
      const storedToken = localStorageAction.get(ACCESS_TOKEN_STORAGE_KEY)
      const storedRefreshToken = localStorageAction.get(REFRESH_TOKEN_STORAGE_KEY)
      const storedRole = localStorage.getItem('userRole')

      console.log('Stored auth data:', {
        hasUser: !!storedUser,
        hasToken: !!storedToken,
        hasRefreshToken: !!storedRefreshToken,
        hasRole: !!storedRole
      })

      if (storedUser && storedToken) {
        console.log('Restoring authentication state from localStorage')
        user.value = storedUser
        accessToken.value = storedToken
        refreshToken.value = storedRefreshToken
        userRole.value = storedRole

        // Verify token is still valid
        try {
          const expire = getExpireTime(storedToken)
          const isValid = Date.now() < expire * 1000

          if (isValid) {
            console.log('Authentication state restored successfully')
          } else {
            console.log('Token expired, clearing auth state')
            logout()
          }
        } catch (tokenError) {
          console.error('Invalid token, clearing auth state:', tokenError)
          logout()
        }
      } else {
        console.log('No stored authentication data found')
      }
    } catch (error) {
      console.error('Error initializing auth:', error)
      // Clear corrupted data
      logout()
    }
  }

  return {
    // State
    user,
    accessToken,
    refreshToken,
    userRole,

    // Getters
    isAuthenticated,

    // Actions
    loginWithGoogle,
    handleOAuthCallback,
    logout,
    setUserRole,
    clearUserRole,
    initializeAuth
  }
})
