<template>
  <div class="min-h-screen bg-gray-50 flex items-center justify-center">
    <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600"></div>
  </div>
</template>

<script lang="ts" setup>
import { useAuthStore } from '@/stores/auth'
import { getExpireTime } from '@/utils/token.helper'
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import dayjs from 'dayjs'
import { ROUTES_CONSTANTS } from '@/constants/path'

const router = useRouter()

const { accessToken } = useAuthStore()

onMounted(() => {
  console.log('Landing mounted, accessToken:', accessToken)
  if (!accessToken) {
    console.log('No accessToken, redirecting to login')
    router.push({ name: ROUTES_CONSTANTS.CUSTOMER.children.HOME.name })
    return
  }

  const expiredTimeAccessToken = getExpireTime(accessToken)
  const now = dayjs().valueOf()
  const expire = expiredTimeAccessToken * 1000
  console.log('Now:', now, 'Expire:', expire, 'Diff (ms):', expire - now)

  if (dayjs().isAfter(expire)) {
    console.log('Token expired, redirecting to login')
    router.push({ name: ROUTES_CONSTANTS.CUSTOMER.children.HOME.name })
    return
  }

  // Always redirect to customer home page as requested
  setTimeout(() => {
    router.push({ name: ROUTES_CONSTANTS.CUSTOMER.children.HOME.name })
  }, 300)
})
</script>
