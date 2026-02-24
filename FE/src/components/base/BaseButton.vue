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

// Styled for "Premium Modern" - Deep Slate & Electric Blue
const classes = computed(() => {
  const base = "font-medium transition-all duration-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-offset-2 flex items-center justify-center gap-2 tracking-wide disabled:opacity-50 disabled:cursor-not-allowed transform active:scale-95";
  
  const sizes = {
    sm: "px-3 py-1.5 text-xs",
    md: "px-5 py-2.5 text-sm",
    lg: "px-8 py-3 text-base",
  };

  const variants = {
    primary: "bg-primary text-white hover:bg-primary-hover shadow-lg shadow-primary/30 hover:shadow-primary/40 focus:ring-primary",
    secondary: "bg-secondary text-white hover:bg-secondary-light shadow-md shadow-secondary/30 focus:ring-secondary",
    outline: "bg-white dark:bg-gray-800 text-secondary dark:text-gray-300 border border-gray-200 dark:border-gray-600 hover:border-primary hover:text-primary dark:hover:border-primary dark:hover:text-primary hover:bg-primary-50 dark:hover:bg-primary/10 focus:ring-gray-200 dark:focus:ring-gray-700",
    danger: "bg-red-600 text-white hover:bg-red-700 shadow-lg shadow-red-500/30 focus:ring-red-500 border border-transparent",
  };

  return `${base} ${sizes[props.size]} ${variants[props.variant]}`;
});
</script>

<template>
  <button :type="type" :class="classes" :disabled="disabled">
    <slot />
  </button>
</template>