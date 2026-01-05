<script setup lang="ts">
import { useTheme } from '@/composable/useTheme'
import ToastContainer from '@/components/common/ToastContainer.vue' // Import notification container

// Initialize theme
useTheme()
</script>

<template>
  <div>
    <router-view />
    <ToastContainer />
  </div>
</template>

<style scoped></style>
