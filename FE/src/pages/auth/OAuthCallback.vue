<template>
  <div style="display: flex; align-items: center; justify-content: center; height: 100vh; text-align: center;">
    <div>
      <div style="margin-bottom: 20px;">
        <div style="width: 40px; height: 40px; border: 4px solid #f3f3f3; border-top: 4px solid #1890ff; border-radius: 50%; animation: spin 1s linear infinite; margin: 0 auto;"></div>
      </div>
      <h2>Đang xử lý đăng nhập...</h2>
      <p>Vui lòng đợi trong khi chúng tôi xác thực thông tin của bạn.</p>
      <div style="margin-top: 20px;">
        <a-button type="primary" @click="goToAdmin" size="large">
          Vào trang quản trị
        </a-button>
      </div>
    </div>
  </div>

  <style>
    @keyframes spin {
      0% { transform: rotate(0deg); }
      100% { transform: rotate(360deg); }
    }
  </style>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { localStorageAction } from '@/utils/storage'
import { USER_INFO_STORAGE_KEY, ACCESS_TOKEN_STORAGE_KEY, REFRESH_TOKEN_STORAGE_KEY } from '@/constants/storagekey'

const router = useRouter()
const authStore = useAuthStore()

onMounted(async () => {
  try {
    // Get state and error from URL parameters
    const urlParams = new URLSearchParams(window.location.search)
    const state = urlParams.get('state')
    const error = urlParams.get('error')

    if (error) {
      console.error('OAuth error:', error)
      alert(`OAuth error: ${error}`)
      router.push('/selection')
      return
    }

    console.log('OAuth callback - State:', state)

    // If we have a state parameter, it contains the tokens from backend
    if (state) {
      try {
        // State is base64 encoded JSON string from backend
        const decodedState = atob(state)
        const tokenData = JSON.parse(decodedState)

        if (tokenData.accessToken && tokenData.refreshToken) {
          console.log('Got tokens from backend OAuth2 response')

          // Get user info using the access token
          const userResponse = await fetch(`${import.meta.env.VITE_BASE_URL_SERVER}/api/admin/profile`, {
            headers: {
              'Authorization': `Bearer ${tokenData.accessToken}`,
            }
          })

          if (userResponse.ok) {
            const userData = await userResponse.json()
            console.log('Got user data from backend:', userData)

            // Create user object from response
            const sessionUserData: any = {
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

            // Save tokens and user data
            authStore.user = sessionUserData
            authStore.accessToken = tokenData.accessToken
            authStore.refreshToken = tokenData.refreshToken
            authStore.userRole = sessionUserData.roleScreen

            localStorageAction.set(USER_INFO_STORAGE_KEY, sessionUserData)
            localStorageAction.set(ACCESS_TOKEN_STORAGE_KEY, tokenData.accessToken)
            localStorageAction.set(REFRESH_TOKEN_STORAGE_KEY, tokenData.refreshToken)
            localStorage.setItem('userRole', sessionUserData.roleScreen)

            console.log('Authentication successful, redirecting to dashboard')
            setTimeout(() => {
              goToAdmin()
            }, 2000)
            return
          }
        }
      } catch (decodeError) {
        console.error('Error decoding state parameter:', decodeError)
      }
    }

    // If no state or state decoding failed, try to get current session info
    console.log('No valid state found, trying to get current session')

    try {
      // Try to get user info from current session
      const userResponse = await fetch(`${import.meta.env.VITE_BASE_URL_SERVER}/api/admin/profile`, {
        credentials: 'include' // Include cookies
      })

      if (userResponse.ok) {
        const userData = await userResponse.json()
        console.log('Got user data from session:', userData)

        // Create user object from session data
        const sessionUserData: any = {
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

        // Save user data to store
        authStore.user = sessionUserData
        authStore.userRole = sessionUserData.roleScreen
        localStorageAction.set(USER_INFO_STORAGE_KEY, sessionUserData)
        localStorage.setItem('userRole', sessionUserData.roleScreen)

        console.log('Session authentication successful, redirecting to dashboard')
        setTimeout(() => {
          goToAdmin()
        }, 2000)
        return
      }
    } catch (error) {
      console.log('Could not get user data from session:', error)
    }

    // If all attempts failed, show manual navigation option
    console.log('Authentication may have succeeded on backend but frontend callback failed')
  } catch (error) {
    console.error('OAuth callback error:', error)
  }
})

const goToAdmin = () => {
  router.push('/admin')
}
</script>
