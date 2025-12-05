<template>
  <div class="min-h-screen bg-gray-50 flex flex-col items-center justify-center p-4">
    <!-- Hiển thị loading khi đang xử lý OAuth callback -->
    <div v-if="processingOAuth" class="oauth-processing">
      <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600"></div>
      <p class="mt-4 text-lg text-gray-700">Đang xử lý đăng nhập...</p>
    </div>

    <!-- Nội dung selection page bình thường -->
    <div v-else class="selection-content">
      <!-- Logo -->
      <div class="mb-12 text-center">
        <img src="@/assets/images/logo-udpm-dark.png" alt="UDPM Logo" class="h-24 mx-auto mb-4">
        <h1 class="text-3xl font-bold text-gray-900">Chào mừng đến với UDPM</h1>
        <p class="mt-2 text-gray-600">Vui lòng chọn chế độ đăng nhập</p>
      </div>

      <!-- Selection Buttons -->
      <div class="flex flex-col md:flex-row justify-center items-center gap-8 w-full max-w-4xl mx-auto">
        <!-- Admin Button -->
        <div @click="handleRedirectLoginADMIN"
          class="group relative bg-white p-6 rounded-xl shadow-md hover:shadow-lg transition-shadow duration-300 cursor-pointer border-2 border-transparent hover:border-indigo-500">
          <div class="flex flex-col items-center text-center">
            <div class="bg-indigo-100 p-4 rounded-full mb-4 group-hover:bg-indigo-200 transition-colors">
              <img src="@/assets/images/Admin.png" alt="Admin" class="h-20 w-20 object-contain">
            </div>
            <h3 class="text-xl font-semibold text-gray-900 mb-2">Quản trị viên</h3>
            <p class="text-gray-600 text-sm">Truy cập trang quản trị hệ thống</p>
          </div>
        </div>

        <!-- Customer Button with Google Sign In -->
        <div @click="handleCustomerLogin"
          class="group relative bg-white p-6 rounded-xl shadow-md hover:shadow-lg transition-shadow duration-300 cursor-pointer border-2 border-transparent hover:border-green-500">
          <div class="flex flex-col items-center text-center">
            <div class="bg-green-100 p-4 rounded-full mb-4 group-hover:bg-green-200 transition-colors">
              <img src="@/assets/images/Member.png" alt="Customer" class="h-20 w-20 object-contain">
            </div>
            <h3 class="text-xl font-semibold text-gray-900 mb-2">Khách hàng</h3>
            <p class="text-gray-600 text-sm">Đăng nhập bằng Gmail để tiếp tục</p>
            <div class="mt-4 flex items-center justify-center">
              <img src="https://upload.wikimedia.org/wikipedia/commons/5/53/Google_%22G%22_Logo.svg" alt="Google"
                class="h-6 w-6 mr-2">
              <span class="text-sm text-gray-700">Đăng nhập với Google</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { URL_OAUTH2_GOOGLE_ADMIN, URL_OAUTH2_GOOGLE_CUSTOMER } from '@/constants/url'
import { ROLES } from '@/constants/roles'
import { toast } from 'vue3-toastify'
import { cookieStorageAction } from '@/utils/storage'
import { localStorageAction } from '@/utils/storage'
import {
  ACCESS_TOKEN_STORAGE_KEY,
  REFRESH_TOKEN_STORAGE_KEY,
  USER_INFO_STORAGE_KEY
} from '@/constants/storagekey'
import { useAuthStore } from '@/stores/auth'
import { jwtDecode } from 'jwt-decode'
import type { UserInformation } from '@/types/auth.type'
import {
  ACCOUNT_EXPIRED,
  ACCOUNT_NOT_EXIST,
  ACCOUNT_NOT_EXIST_MESSAGE,
  ACCOUNT_NOT_HAVE_PERMISSION,
  ACCOUNT_NOT_HAVE_PERMISSION_MESSAGE
} from '@/constants/cookie.constants'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

// Debug logs
console.log('[AUTH] LoginSelection component loaded')

// Reactive states
const loading = ref(false)
const error = ref<string | null>(null)
const processingOAuth = ref(false)

// Hàm xử lý OAuth callback
// Hàm xử lý khi có token (từ API hoặc từ state)
const handleAuthSuccess = async (accessToken: string, refreshToken: string | null, role?: string) => {
  console.log('✅ Xử lý đăng nhập thành công')
  console.log('✅ Token:', accessToken.substring(0, 50) + '...')

  // Decode JWT để lấy thông tin user
  const decodedToken: any = jwtDecode(accessToken)
  console.log('🔍 Decoded JWT:', decodedToken)

  // Tạo user object từ JWT
  const user: UserInformation = {
    userId: decodedToken.sub || decodedToken.id || decodedToken.userId,
    userCode: decodedToken.userCode || decodedToken.sub || '',
    fullName: decodedToken.name || decodedToken.fullName || 'User',
    email: decodedToken.email,
    pictureUrl: decodedToken.picture || decodedToken.pictureUrl,
    rolesNames: decodedToken.rolesNames || [],
    rolesCodes: decodedToken.rolesCodes || [],
    roleScreen: decodedToken.roleScreen || role || ROLES.ADMIN,
    roleSwitch: decodedToken.roleSwitch,
    idFacility: decodedToken.idFacility || null
  }

  console.log('👤 User info:', user)

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
  authStore.setUserRole(user.roleScreen)

  console.log('💾 Đã lưu thông tin auth, role:', user.roleScreen)

  // Redirect based on role immediately
  setTimeout(() => {
    if (user.roleScreen === ROLES.ADMIN) {
      console.log('🎯 Redirect to ADMIN dashboard')
      router.push({ name: 'admin-dashboard' })
    } else if (user.roleScreen === ROLES.CUSTOMER) {
      console.log('🎯 Redirect to CUSTOMER dashboard')
      router.push({ name: 'customer-dashboard' })
    }
  }, 500)
}

// Hàm xử lý OAuth callback
const processOAuthCallback = async () => {
  const { code, state } = route.query

  console.log('🔄 Phát hiện OAuth callback')
  processingOAuth.value = true

  try {
    // 1. Kiểm tra xem state có chứa token không (Backend redirect pattern)
    if (state && typeof state === 'string' && state.length > 20) {
      try {
        // Thử decode base64
        const decodedState = atob(state)
        if (decodedState.includes('accessToken')) {
          const authData = JSON.parse(decodedState)
          if (authData.accessToken) {
            console.log('📦 Tìm thấy access token trong state parameter')
            await handleAuthSuccess(authData.accessToken, authData.refreshToken || null)
            return true
          }
        }
      } catch (e) {
        console.log('ℹ️ State không phải là token container:', e)
      }
    }

    // 2. Nếu không có token trong state, kiểm tra code để trao đổi
    if (!code) {
      console.log('📭 Không có code parameter trong URL')
      return false
    }

    // Determine the role from the cookie or URL state
    let role: string = ROLES.CUSTOMER

    // Try to get role from cookie first
    const roleCookie = document.cookie
      .split('; ')
      .find(row => row.startsWith('screen='))
      ?.split('=')[1]

    if (roleCookie) {
      role = roleCookie
      console.log('🔑 Lấy role từ cookie:', role)
    }
    // If no cookie, try to get from state if it's a simple role string
    else if (state && typeof state === 'string' && !state.startsWith('{') && !state.includes('${SCREEN_R}')) {
      role = state
      console.log('🔑 Lấy role từ state:', role)
    }

    // Exchange the authorization code for tokens
    console.log('🔄 Đang trao đổi mã xác thực...')
    const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/api/auth/oauth2/callback/google?code=${code}&state=${state || ''}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include' // Important for cookies
    })

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}))
      throw new Error(errorData.message || 'Lỗi khi xác thực với máy chủ')
    }

    const { accessToken, refreshToken } = await response.json()

    if (!accessToken) {
      throw new Error('Không nhận được access token từ máy chủ')
    }

    console.log('✅ Đăng nhập thành công với role:', role)

    // Sử dụng hàm helper để xử lý thành công
    await handleAuthSuccess(accessToken, refreshToken, role)

    return true

  } catch (err) {
    console.error('❌ Lỗi xử lý OAuth callback:', err)
    toast.error('Đăng nhập thất bại. Vui lòng thử lại.')

    // Clear URL parameters
    router.replace({ name: 'selection' })
    return false
  } finally {
    processingOAuth.value = false
  }
}

// Handle customer login with Google
const handleCustomerLogin = () => {
  try {
    // Set role to CUSTOMER before redirecting to Google OAuth
    setRoleCookie(ROLES.CUSTOMER)

    // Call the function to get the OAuth URL
    const oauthUrl = URL_OAUTH2_GOOGLE_CUSTOMER()
    console.log('[AUTH] Redirecting to:', oauthUrl)

    // Redirect to Google OAuth for customer login
    window.location.href = oauthUrl
  } catch (error) {
    console.error('Error during customer login:', error)
    toast.error('Có lỗi xảy ra khi đăng nhập. Vui lòng thử lại.')
  }
}

// Set role cookie with expiration (1 hour) and debug logging
const setRoleCookie = (role: string) => {
  try {
    const expires = new Date()
    expires.setTime(expires.getTime() + 60 * 60 * 1000) // 1 hour

    // Changed cookie name from 'ROLE' to 'screen' to match backend
    const cookieValue = `screen=${role};expires=${expires.toUTCString()};path=/;SameSite=Lax`
    document.cookie = cookieValue

    console.log(`[AUTH] Set role cookie:`, {
      name: 'screen',
      value: role,
      expires: expires.toISOString(),
      path: '/',
      sameSite: 'Lax'
    })

    // Verify the cookie was set
    const cookies = document.cookie.split(';')
    const roleCookie = cookies.find(c => c.trim().startsWith('screen='))
    console.log('[AUTH] Current cookies after setting:', cookies)
    console.log('[AUTH] Found screen cookie:', roleCookie)

    if (!roleCookie) {
      console.error('[AUTH] Failed to set screen cookie!')
      throw new Error('Failed to set screen cookie')
    }
  } catch (err) {
    console.error('[AUTH] Error setting screen cookie:', err)
    throw err
  }
}

// Set redirect_uri cookie để backend redirect về đúng URL
const setRedirectUriCookie = (role: string) => {
  try {
    const frontendUrl = window.location.origin;
    // Backend đang redirect về /selection nên chúng ta sẽ xử lý tại đây
    const redirectUri = `${frontendUrl}/selection`;

    const expires = new Date();
    expires.setTime(expires.getTime() + 5 * 60 * 1000); // 5 phút

    const cookieValue = `redirect_uri=${encodeURIComponent(redirectUri)};expires=${expires.toUTCString()};path=/;SameSite=Lax`;
    document.cookie = cookieValue;

    console.log(`[AUTH] Set redirect_uri cookie for ${role}:`, redirectUri);

    // Verify cookie
    const cookies = document.cookie.split(';');
    const redirectCookie = cookies.find(c => c.trim().startsWith('redirect_uri='));
    console.log('[AUTH] Found redirect_uri cookie:', redirectCookie);

  } catch (err) {
    console.error('[AUTH] Error setting redirect_uri cookie:', err);
  }
}

// Redirect to Google login for Admin
const handleRedirectLoginADMIN = async () => {
  try {
    console.log('[AUTH] Initiating ADMIN login...')
    loading.value = true
    error.value = null

    // Set the correct cookie name 'screen' with value 'ADMIN'
    console.log('[AUTH] Setting ADMIN role cookie...')
    setRoleCookie('ADMIN')

    // Set redirect_uri cookie
    setRedirectUriCookie('ADMIN')

    // Small delay to ensure cookie is set
    await new Promise(resolve => setTimeout(resolve, 100))

    // Verify cookie is set before redirect
    const cookies = document.cookie.split(';')
    const roleCookie = cookies.find(c => c.trim().startsWith('screen='))
    console.log('[AUTH] Pre-redirect cookies:', cookies)
    console.log('[AUTH] Pre-redirect screen cookie:', roleCookie)

    if (!roleCookie) {
      const errorMsg = 'Screen cookie not set before redirect'
      console.error(`[AUTH] ${errorMsg}`)
      throw new Error(errorMsg)
    }

    console.log('[AUTH] Redirecting to Google OAuth2 for ADMIN...')
    window.location.href = URL_OAUTH2_GOOGLE_ADMIN()
  } catch (error) {
    console.error('[AUTH] Error in admin login redirect:', error)
    toast.error('Có lỗi xảy ra khi chuyển hướng đến Google. Vui lòng thử lại.')
    loading.value = false
  }
}



// Mounted lifecycle
onMounted(async () => {
  console.log('[AUTH] LoginSelection mounted - checking for cookie-based errors...')
  console.log('[AUTH] Current cookies:', document.cookie)
  console.log('[AUTH] Current route query:', route.query)

  // Thử xử lý OAuth callback nếu có state parameter
  const hasOAuthState = route.query.state;
  if (hasOAuthState) {
    console.log('[AUTH] Phát hiện OAuth state parameter, xử lý callback...');
    await processOAuthCallback();
  } else {
    console.log('[AUTH] Không có OAuth state, hiển thị selection page bình thường');
  }

  // Kiểm tra lỗi từ cookie
  const accountNotExistError = cookieStorageAction.get(ACCOUNT_NOT_EXIST)
  const accountNotHavePermission = cookieStorageAction.get(ACCOUNT_NOT_HAVE_PERMISSION)
  const accountExpired = cookieStorageAction.get(ACCOUNT_EXPIRED)

  if (accountNotExistError) {
    console.warn('[AUTH] ACCOUNT_NOT_EXIST detected:', accountNotExistError)
    toast.error(ACCOUNT_NOT_EXIST_MESSAGE)
    cookieStorageAction.remove(ACCOUNT_NOT_EXIST)
  }

  if (accountNotHavePermission) {
    console.warn('[AUTH] ACCOUNT_NOT_HAVE_PERMISSION detected:', accountNotHavePermission)
    toast.error(ACCOUNT_NOT_HAVE_PERMISSION_MESSAGE)
    cookieStorageAction.remove(ACCOUNT_NOT_HAVE_PERMISSION)
  }

  if (accountExpired) {
    console.warn('[AUTH] ACCOUNT_EXPIRED detected')
    toast.error('Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại.')
    cookieStorageAction.remove(ACCOUNT_EXPIRED)
  }
})
</script>

<style scoped>
.oauth-processing {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 60vh;
  gap: 1rem;
}

.selection-content {
  width: 100%;
}
</style>