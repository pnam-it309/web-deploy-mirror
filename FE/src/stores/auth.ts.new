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
import { 
  GoogleAuthProvider, 
  signInWithPopup, 
  signInWithEmailAndPassword,
  signOut as firebaseSignOut,
  type UserCredential
} from 'firebase/auth'
import { auth } from '@/firebase'

// Google Auth Provider instance
export const googleAuthProvider = new GoogleAuthProvider()

// Add OAuth scopes
googleAuthProvider.addScope('email')
googleAuthProvider.addScope('profile')

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
  const login = async (credentials: { email: string; password: string }): Promise<UserCredential> => {
    try {
      const userCredential = await signInWithEmailAndPassword(
        auth, 
        credentials.email, 
        credentials.password
      )
      
      const token = await userCredential.user.getIdToken()
      
      const userData: UserInformation = {
        userId: userCredential.user.uid,
        userCode: userCredential.user.uid,
        fullName: userCredential.user.displayName || '',
        email: userCredential.user.email || '',
        pictureUrl: userCredential.user.photoURL || '',
        rolesNames: [],
        rolesCodes: [],
        roleScreen: '',
        idFacility: '',
        roleSwitch: ''
      }
      
      // Save user data and token
      user.value = userData
      accessToken.value = token
      
      localStorageAction.set(USER_INFO_STORAGE_KEY, userData)
      localStorageAction.set(ACCESS_TOKEN_STORAGE_KEY, token)
      
      return userCredential
    } catch (error) {
      console.error('Login error:', error)
      throw error
    }
  }

  const loginWithGoogle = async (): Promise<UserCredential> => {
    try {
      const result = await signInWithPopup(auth, googleAuthProvider)
      const token = await result.user.getIdToken()
      
      const userData: UserInformation = {
        userId: result.user.uid,
        userCode: result.user.uid,
        fullName: result.user.displayName || '',
        email: result.user.email || '',
        pictureUrl: result.user.photoURL || '',
        rolesNames: [],
        rolesCodes: [],
        roleScreen: '',
        idFacility: '',
        roleSwitch: ''
      }
      
      // Save user data and token
      user.value = userData
      accessToken.value = token
      
      localStorageAction.set(USER_INFO_STORAGE_KEY, userData)
      localStorageAction.set(ACCESS_TOKEN_STORAGE_KEY, token)
      
      return result
    } catch (error) {
      console.error('Google login error:', error)
      throw error
    }
  }
  
  const handleOAuthCallback = async (params: { code: string; state?: string }) => {
    // This would be implemented based on your OAuth provider
    // For now, we'll just return the current user
    return { user: user.value }
  }
  
  const logout = async () => {
    try {
      await firebaseSignOut(auth)
    } catch (error) {
      console.error('Logout error:', error)
      throw error
    } finally {
      user.value = null
      accessToken.value = null
      refreshToken.value = null
      userRole.value = null
      
      localStorageAction.remove(USER_INFO_STORAGE_KEY)
      localStorageAction.remove(ACCESS_TOKEN_STORAGE_KEY)
      localStorageAction.remove(REFRESH_TOKEN_STORAGE_KEY)
      localStorage.removeItem('userRole')
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

  // Enhanced logout that also clears role
  const enhancedLogout = () => {
    logout()
    clearUserRole()
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
    login,
    loginWithGoogle,
    handleOAuthCallback,
    logout: enhancedLogout,
    setUserRole,
    clearUserRole
  }
})
