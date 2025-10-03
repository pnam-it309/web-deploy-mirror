// Storage keys
const ACCESS_TOKEN_STORAGE_KEY = 'access_token'
const REFRESH_TOKEN_STORAGE_KEY = 'refresh_token'
const USER_INFO_STORAGE_KEY = 'user_info'

// Types
interface UserInformation {
  userId: string
  userCode: string
  fullName: string
  email: string
  pictureUrl?: string
  rolesNames: string[]
  rolesCodes: string[]
  roleScreen: string
  idFacility: string
  roleSwitch?: string
}

import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { 
  GoogleAuthProvider, 
  signInWithPopup, 
  signInWithEmailAndPassword,
  signOut as firebaseSignOut,
  onAuthStateChanged,
  type UserCredential
} from 'firebase/auth'
import { auth } from '@/firebase'
import { localStorageAction } from '@/utils/storage'
import { getExpireTime } from '@/utils/token.helper'

// Google Auth Provider instance
export const googleAuthProvider = new GoogleAuthProvider()
googleAuthProvider.addScope('email')
googleAuthProvider.addScope('profile')

export const useAuthStore = defineStore('auth', () => {
  // State
  const user = ref<UserInformation | null>(null)
  const accessToken = ref<string | null>(null)
  const refreshToken = ref<string | null>(null)
  const userRole = ref<string | null>(null)
  const loading = ref(false)
  const error = ref<string | null>(null)

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
  const initializeAuth = () => {
    return new Promise<void>((resolve) => {
      const unsubscribe = onAuthStateChanged(auth, async (firebaseUser) => {
        if (firebaseUser) {
          try {
            const token = await firebaseUser.getIdToken()
            const userData: UserInformation = {
              userId: firebaseUser.uid,
              userCode: firebaseUser.uid,
              fullName: firebaseUser.displayName || '',
              email: firebaseUser.email || '',
              pictureUrl: firebaseUser.photoURL || '',
              rolesNames: [],
              rolesCodes: [],
              roleScreen: '',
              idFacility: '',
              roleSwitch: ''
            }

            user.value = userData
            accessToken.value = token
            
            // Set user role based on email (in a real app, this would come from your backend)
            const isAdmin = firebaseUser.email?.endsWith('@admin.com') || false
            setUserRole(isAdmin ? 'admin' : 'customer')
            
            // Save to local storage
            localStorageAction.set(USER_INFO_STORAGE_KEY, userData)
            localStorageAction.set(ACCESS_TOKEN_STORAGE_KEY, token)
          } catch (err) {
            console.error('Error initializing auth:', err)
            resetAuthState()
          }
        } else {
          resetAuthState()
        }
        resolve()
      })
      
      return () => unsubscribe()
    })
  }

  const resetAuthState = () => {
    user.value = null
    accessToken.value = null
    refreshToken.value = null
    userRole.value = null
    
    localStorageAction.remove(USER_INFO_STORAGE_KEY)
    localStorageAction.remove(ACCESS_TOKEN_STORAGE_KEY)
    localStorageAction.remove(REFRESH_TOKEN_STORAGE_KEY)
    localStorage.removeItem('userRole')
  }

  const login = async (credentials: { email: string; password: string }): Promise<UserCredential> => {
    try {
      loading.value = true
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
    } finally {
      loading.value = false
    }
  }

  const loginWithGoogle = async (): Promise<UserCredential> => {
    try {
      loading.value = true
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
      
      // Set user role based on email (in a real app, this would come from your backend)
      const isAdmin = result.user.email?.endsWith('@admin.com') || false
      setUserRole(isAdmin ? 'admin' : 'customer')
      
      localStorageAction.set(USER_INFO_STORAGE_KEY, userData)
      localStorageAction.set(ACCESS_TOKEN_STORAGE_KEY, token)
      
      return result
    } catch (error) {
      console.error('Google login error:', error)
      throw error
    } finally {
      loading.value = false
    }
  }
  
  const handleOAuthCallback = async (params: { code: string; state?: string }) => {
    // This would be implemented based on your OAuth provider
    // For now, we'll just return the current user
    return { user: user.value }
  }
  
  const logout = async () => {
    try {
      loading.value = true
      await firebaseSignOut(auth)
      resetAuthState()
    } catch (error) {
      console.error('Logout error:', error)
      throw error
    } finally {
      loading.value = false
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

  // Initialize auth state when the store is created
  initializeAuth()

  return {
    // State
    user,
    accessToken,
    refreshToken,
    userRole,
    loading,
    error,
    
    // Getters
    isAuthenticated,
    
    // Actions
    initializeAuth,
    resetAuthState,
    login,
    loginWithGoogle,
    handleOAuthCallback,
    logout,
    setUserRole,
    clearUserRole
  }
})
