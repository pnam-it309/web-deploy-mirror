<template>
  <div class="redirect-container">
    <p class="redirect-message">
      <a-spin size="large" />
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
    console.log('🔍 Bắt đầu xử lý OAuth callback')
    console.log('📋 Route query:', route.query)
    
    const { state } = route.query
    
    if (!state) {
      throw new Error('Thiếu thông tin xác thực. Vui lòng thử lại.')
    }

    console.log('📦 State parameter received:', state)

    try {
      // Decode the state parameter
      const decodedState = JSON.parse(decodeURIComponent(atob(state as string)))
      console.log('🔓 Decoded state:', decodedState)
      
      const { accessToken, refreshToken } = decodedState
      
      if (!accessToken) {
        throw new Error('Không tìm thấy access token')
      }

      console.log('✅ Token nhận được:', accessToken)

      // Decode JWT để lấy thông tin user
      const decodedToken: any = jwtDecode(accessToken)
      console.log('🔍 Decoded JWT:', decodedToken)

      // Tạo user object từ JWT
      const user = {
        id: decodedToken.sub || decodedToken.id,
        email: decodedToken.email,
        name: decodedToken.name,
        roleScreen: decodedToken.roleScreen || ROLES.ADMIN, // Fallback to ADMIN
        picture: decodedToken.picture
      }

      console.log('👤 User info từ JWT:', user)

      if (!user.roleScreen) {
        throw new Error('Không tìm thấy thông tin role trong token')
      }

      // Store tokens and user data
      localStorageAction.set(ACCESS_TOKEN_STORAGE_KEY, accessToken)
      if (refreshToken) {
        localStorageAction.set(REFRESH_TOKEN_STORAGE_KEY, refreshToken)
      }
      localStorageAction.set(USER_INFO_STORAGE_KEY, user)
      
      // Update auth store
      authStore.user = user
      authStore.accessToken = accessToken
      authStore.refreshToken = refreshToken || null

      // Set user role
      authStore.setUserRole(user.roleScreen)

      console.log('💾 Đã lưu thông tin auth, role:', user.roleScreen)

      // Redirect based on role
      await redirectBasedOnRole(user.roleScreen)
      
    } catch (parseError) {
      console.error('❌ Lỗi parse state:', parseError)
      throw new Error('Dữ liệu xác thực không hợp lệ')
    }
    
  } catch (err) {
    console.error('❌ Lỗi xử lý đăng nhập:', err)
    error.value = err instanceof Error ? err.message : 'Có lỗi xảy ra khi đăng nhập. Vui lòng thử lại.'
    toast.error('Đăng nhập thất bại. Vui lòng thử lại.')
    
    // Redirect to login after showing error
    setTimeout(() => {
      router.push({ name: ROUTES_CONSTANTS.LOGIN.name })
    }, 3000)
  } finally {
    loading.value = false
  }
}

const redirectBasedOnRole = (roleScreen: string) => {
  return new Promise<void>((resolve) => {
    let redirectTo = { name: 'selection' }
    
    if (roleScreen === ROLES.ADMIN) {
      redirectTo = { name: 'admin-dashboard' }
      console.log('🎯 Redirect to ADMIN dashboard')
    } else if (roleScreen === ROLES.CUSTOMER) {
      redirectTo = { name: 'customer-dashboard' }
      console.log('🎯 Redirect to CUSTOMER dashboard')
    }
    
    // Thêm delay để đảm bảo store được cập nhật
    setTimeout(() => {
      console.log('🔄 Thực hiện redirect đến:', redirectTo)
      router.push(redirectTo)
        .then(() => {
          console.log('✅ Redirect thành công')
          resolve()
        })
        .catch(err => {
          console.error('❌ Navigation error:', err)
          // Fallback to selection page
          router.push({ name: 'selection' })
          resolve()
        })
    }, 1000)
  })
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