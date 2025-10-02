<template>
  <a-spin size="large" />
</template>

<script lang="ts" setup>
import { useAuthStore } from '@/stores/auth'
import { getExpireTime } from '@/utils/token.helper'
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import dayjs from 'dayjs'

const router = useRouter()

const { accessToken } = useAuthStore()

onMounted(() => {
  console.log('Landing mounted, accessToken:', accessToken)
  if (!accessToken) {
    console.log('No accessToken, redirecting to login')
    router.push({ name: 'login' })
    return
  }

  const expiredTimeAccessToken = getExpireTime(accessToken)
  const now = dayjs().valueOf()
  const expire = expiredTimeAccessToken * 1000
  console.log('Now:', now, 'Expire:', expire, 'Diff (ms):', expire - now)

  if (dayjs().isAfter(expire)) {
    console.log('Token expired, redirecting to login')
    router.push({ name: 'login' })
    return
  }

  // Nếu đã xác thực, chuyển hướng vào trang chính (ví dụ: admin)
  setTimeout(() => {
    router.push({ path: '/admin' }) // hoặc route nào bạn muốn
  }, 300)
})
</script>
