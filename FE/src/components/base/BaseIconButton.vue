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
      'text-gray-500 hover:bg-gray-500 hover:text-dark': variant === 'default'
    }"
  >
    <slot />
  </button>
</template>