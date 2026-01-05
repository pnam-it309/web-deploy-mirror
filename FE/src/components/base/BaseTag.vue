<script setup lang="ts">
interface Props {
  variant?: 'gold' | 'dark' | 'success' | 'gray';
}
withDefaults(defineProps<Props>(), { variant: 'gray' });

const variants = {
  gold: 'bg-primary-light text-yellow-800 border border-primary',
  dark: 'bg-dark text-white',
  success: 'bg-green-100 text-green-800 border border-green-200',
  gray: 'bg-gray-100 text-gray-600',
};
</script>

<template>
  <span :class="`px-2.5 py-0.5 rounded text-[10px] font-bold uppercase tracking-wide border-0 ${variants[variant]}`">
    <slot />
  </span>
</template>