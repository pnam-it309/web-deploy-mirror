<script setup lang="ts">
import { ref, watchEffect } from 'vue'

const dark = ref(false)
watchEffect(() => {
  const root = document.documentElement
  if (dark.value) root.classList.add('dark')
  else root.classList.remove('dark')
})
</script>

<template>
  <div>
    <router-view />
    <button
      class="fixed bottom-3 right-3 rounded-full px-3 py-2 text-xs bg-slate-900 text-white dark:bg-white dark:text-slate-900 shadow"
      @click="dark = !dark"
    >
      {{ dark ? 'Light' : 'Dark' }}
    </button>
  </div>
</template>

<style scoped></style>
