// src/stores/auth.js
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

type authenticationData = {
  user: UserInformation
  accessToken: string
  refreshToken: string
}

export const useAuthStore = defineStore('auth', () => {
  const user = ref<UserInformation | null>(localStorageAction.get(USER_INFO_STORAGE_KEY) || null)
  const accessToken = ref<string | null>(localStorageAction.get(ACCESS_TOKEN_STORAGE_KEY) || null)
  const refreshToken = ref<string | null>(localStorageAction.get(REFRESH_TOKEN_STORAGE_KEY) || null)
  const userRole = ref<string | null>(localStorage.getItem('userRole') || null)

  const isAuthenticated = computed(() => {
    if (!accessToken.value) return false;
    // Kiểm tra hạn token
    try {
      const expire = getExpireTime(accessToken.value);
      return Date.now() < expire * 1000;
    } catch (e) {
      return false;
    }
  })

  const login = (tokenData: authenticationData) => {
    let userData = tokenData.user
    // Ưu tiên roleScreen nếu là SINH_VIEN
    if (userData && userData.roleScreen === 'SINH_VIEN') {
      (userData as any).rolesCodes = ['SINH_VIEN']
    } else if (userData && (userData as any).rolesCode && !(userData as any).rolesCodes) {
      (userData as any).rolesCodes = (userData as any).rolesCode
    }
    user.value = userData
    accessToken.value = tokenData.accessToken
    refreshToken.value = tokenData.refreshToken
    console.log('token: ' + accessToken.value)
    localStorageAction.set(USER_INFO_STORAGE_KEY, userData)
    localStorageAction.set(ACCESS_TOKEN_STORAGE_KEY, tokenData.accessToken)
    localStorageAction.set(REFRESH_TOKEN_STORAGE_KEY, tokenData.refreshToken)

    console.log('🤡Ra User Login :', userData)
  }

  const logout = () => {
    user.value = null
    accessToken.value = null
    refreshToken.value = null
    localStorageAction.remove(USER_INFO_STORAGE_KEY)
    localStorageAction.remove(ACCESS_TOKEN_STORAGE_KEY)
    localStorageAction.remove(REFRESH_TOKEN_STORAGE_KEY)
  }

  const setUserRole = (role: string) => {
    userRole.value = role;
    localStorage.setItem('userRole', role);
  };

  const clearUserRole = () => {
    userRole.value = null;
    localStorage.removeItem('userRole');
  };

  // Clear role on logout
  const enhancedLogout = () => {
    logout();
    clearUserRole();
  };

  return { 
    user, 
    isAuthenticated, 
    login, 
    logout: enhancedLogout, 
    accessToken, 
    refreshToken,
    userRole,
    setUserRole,
    clearUserRole
  }
})
