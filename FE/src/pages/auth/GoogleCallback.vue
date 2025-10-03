<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-50">
    <div class="text-center">
      <div class="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500 mx-auto"></div>
      <p class="mt-4 text-gray-600">Đang xử lý đăng nhập...</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()

onMounted(async () => {
  try {
    // Handle the OAuth callback
    const { code, state } = route.query
    
    if (!code || typeof code !== 'string') {
      throw new Error('Invalid OAuth response')
    }

    // Exchange the authorization code for an access token
    const result = await authStore.handleOAuthCallback({
      code: code as string,
      state: state as string | undefined
    })

    // Determine user role (in a real app, this would come from your backend)
    const isAdmin = result.user?.email?.endsWith('@admin.com')
    authStore.setUserRole(isAdmin ? 'admin' : 'customer')

    // Redirect to the intended URL or dashboard
    const redirectPath = route.query.redirect?.toString() || 
                       (isAdmin ? '/admin/dashboard' : '/customer/dashboard')
    await router.push(redirectPath)
    
  } catch (error) {
    console.error('OAuth callback error:', error)
    await router.push({
      name: 'login',
      query: { 
        error: 'Đăng nhập thất bại. Vui lòng thử lại.' 
      }
    })
  }
})
</script>
