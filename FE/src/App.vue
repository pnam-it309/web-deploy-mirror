<script setup lang="ts">
import { useTheme } from '@/composable/useTheme'

// Initialize theme
useTheme()
</script>

<template>
  <div>
    <router-view />
  </div>
</template>

<style scoped></style>
