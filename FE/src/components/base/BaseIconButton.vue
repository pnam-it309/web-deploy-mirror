<script setup lang="ts">
defineProps<{
  variant?: 'primary' | 'danger' | 'default' | 'secondary';
}>();
</script>

<template>
  <button 
    class="p-2 rounded-full transition-all duration-300 hover:bg-opacity-10 focus:outline-none"
    :class="{
      'text-primary hover:bg-primary': variant === 'primary' || !variant,
      'text-red-500 hover:bg-red-500': variant === 'danger',
      'text-blue-500 hover:bg-blue-500 hover:text-white border border-blue-100 hover:border-blue-500': variant === 'secondary',
      'text-gray-500 hover:bg-gray-500 hover:text-dark': variant === 'default'
    }"
  >
    <slot />
  </button>
</template>