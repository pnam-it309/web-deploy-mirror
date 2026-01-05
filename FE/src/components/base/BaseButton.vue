<script setup lang="ts">
import { computed } from 'vue';

interface Props {
  variant?: 'primary' | 'secondary' | 'outline' | 'danger';
  size?: 'sm' | 'md' | 'lg';
  type?: 'button' | 'submit';
  disabled?: boolean;
}

const props = withDefaults(defineProps<Props>(), {
  variant: 'primary',
  size: 'md',
  type: 'button',
  disabled: false,
});

const classes = computed(() => {
  const base = "font-medium transition-all duration-300 rounded-sm focus:outline-none flex items-center justify-center gap-2 tracking-wide";
  
  const sizes = {
    sm: "px-3 py-1.5 text-xs",
    md: "px-6 py-2.5 text-sm",
    lg: "px-8 py-3 text-base",
  };

  const variants = {
    primary: "bg-primary text-dark hover:bg-primary-hover shadow-md hover:shadow-lg border border-transparent",
    secondary: "bg-dark text-primary border border-primary hover:bg-primary hover:text-dark",
    outline: "bg-white text-gray-600 border border-gray-300 hover:border-primary hover:text-primary",
    danger: "bg-red-600 text-white hover:bg-red-700",
  };

  return `${base} ${sizes[props.size]} ${variants[props.variant]} ${props.disabled ? 'opacity-50 cursor-not-allowed' : ''}`;
});
</script>

<template>
  <button :type="type" :class="classes" :disabled="disabled">
    <slot />
  </button>
</template>