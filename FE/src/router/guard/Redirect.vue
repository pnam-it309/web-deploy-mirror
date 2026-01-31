<template>
  <div class="redirect-container">
    <p class="redirect-message">
    <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-indigo-600"></div>
    <span v-if="loading">Đang xử lý đăng nhập...</span>
    <span v-else-if="error" class="error-message">{{ error }}</span>
    </p>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ROUTES_CONSTANTS } from '@/constants/path'
import { ROLES } from '@/constants/roles'
import { useAuthStore } from '@/stores/auth'
import {
  ACCESS_TOKEN_STORAGE_KEY,
  REFRESH_TOKEN_STORAGE_KEY,
  USER_INFO_STORAGE_KEY
} from '@/constants/storagekey'
import { localStorageAction } from '@/utils/storage'
import { toast } from 'vue3-toastify'
import { jwtDecode } from 'jwt-decode'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

const loading = ref(true)
const error = ref<string | null>(null)

const processOAuthCallback = async () => {
  try {
    const { state } = route.query
    if (!state) throw new Error('Thiếu thông tin xác thực')

    // Decode state
    const decodedState = JSON.parse(atob(state as string))
    const { accessToken, refreshToken } = decodedState

    if (!accessToken) throw new Error('Không tìm thấy access token')

    // Decode JWT for user info
    const decodedToken: any = jwtDecode(accessToken)
    
    // Normalize user object to match what app expects
    const user = {
      id: decodedToken.userId || decodedToken.sub,
      email: decodedToken.email,
      name: decodedToken.fullName || decodedToken.name,
      roleScreen: decodedToken.roleScreen || ROLES.CUSTOMER,
      avatar: decodedToken.pictureUrl || decodedToken.picture
    }

    // Store in localStorage
    localStorageAction.set(ACCESS_TOKEN_STORAGE_KEY, accessToken)
    if (refreshToken) {
      localStorageAction.set(REFRESH_TOKEN_STORAGE_KEY, refreshToken)
    }
    localStorageAction.set(USER_INFO_STORAGE_KEY, user)

    // Update store
    authStore.user = user
    authStore.accessToken = accessToken
    authStore.refreshToken = refreshToken || null
    authStore.setUserRole(user.roleScreen)

    // Immediate redirect
    const redirectTo = user.roleScreen === ROLES.ADMIN 
      ? { name: ROUTES_CONSTANTS.ADMIN.children.DASHBOARD.name }
      : { name: ROUTES_CONSTANTS.CUSTOMER.children.HOME.name }
    
    router.push(redirectTo)
  } catch (err) {
    console.error('Login error:', err)
    error.value = 'Đăng nhập thất bại. Vui lòng thử lại.'
    setTimeout(() => router.push('/'), 2000)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  console.log('🚀 Redirect component mounted')
  console.log('📍 Current path:', route.path)
  console.log('🔍 Full route:', route)
  processOAuthCallback()
})
</script>

<style scoped>
.redirect-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f0f0f0;
}

.redirect-message {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1rem;
  font-size: 1.2rem;
  color: #333;
  text-align: center;
  padding: 2rem;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.error-message {
  color: #ff4d4f;
  margin-top: 1rem;
}
</style>