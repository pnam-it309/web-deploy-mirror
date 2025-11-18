<template>
  <a-result status="error" title="404" sub-title="Xin lỗi, trang bạn truy cập không tồn tại.">
    <template #extra>
      <a-button type="primary" @click="goBack">Quay lại</a-button>
    </template>
  </a-result>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { Button as AButton, Result as AResult } from 'ant-design-vue'

const router = useRouter()

const goBack = () => {
  const token = localStorage.getItem('accessToken')
  if (token) {
    try {
      const payload = JSON.parse(atob(token.split('.')[1]))
      const userRole = payload.role || payload.authorities || 'guest'

      if (userRole === 'admin') {
        router.push('/admin')
      } else if (userRole === 'customer' || userRole === 'user') {
        router.push('/customer')
      } else {
        router.push('/selection')
      }
    } catch (error) {
      router.push('/selection')
    }
  } else {
    router.push('/selection')
  }
}
</script>

