<script setup lang="ts">
import { useTheme } from '@/composable/ui/useTheme'

useTheme()
</script>

<template>
  <div>
    <router-view />
  </div>
</template>

<style scoped></style>
