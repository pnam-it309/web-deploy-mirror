<script setup lang="ts">
import { computed } from 'vue';

const props = withDefaults(defineProps<{
  variant?: 'default' | 'destructive' | 'outline' | 'secondary' | 'ghost' | 'link' | 'primary';
  size?: 'default' | 'sm' | 'lg' | 'icon';
  as?: string;
  disabled?: boolean;
  loading?: boolean;
}>(), {
  variant: 'default',
  size: 'default',
  as: 'button',
  disabled: false,
  loading: false
});

const classes = computed(() => ({
  'inline-flex items-center justify-center rounded-md font-medium transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:opacity-50 disabled:pointer-events-none ring-offset-background': true,
  'bg-primary text-primary-foreground hover:bg-primary/90': props.variant === 'default',
  'bg-destructive text-destructive-foreground hover:bg-destructive/90': props.variant === 'destructive',
  'border border-input hover:bg-accent hover:text-accent-foreground': props.variant === 'outline',
  'bg-secondary text-secondary-foreground hover:bg-secondary/80': props.variant === 'secondary',
  'hover:bg-accent hover:text-accent-foreground': props.variant === 'ghost',
  'underline-offset-4 hover:underline text-primary': props.variant === 'link',
  'h-10 py-2 px-4': props.size === 'default',
  'h-9 px-3 rounded-md': props.size === 'sm',
  'h-11 px-8 rounded-md': props.size === 'lg',
  'h-10 w-10 p-0': props.size === 'icon',
  'opacity-70 cursor-not-allowed': props.disabled || props.loading,
  'relative': props.loading
}));
</script>

<template>
  <component
    :is="as"
    :class="classes"
    :disabled="disabled || loading"
    v-bind="$attrs"
  >
    <span v-if="loading" class="absolute inset-0 flex items-center justify-center">
      <svg
        class="animate-spin h-4 w-4 text-current"
        xmlns="http://www.w3.org/2000/svg"
        fill="none"
        viewBox="0 0 24 24"
      >
        <circle
          class="opacity-25"
          cx="12"
          cy="12"
          r="10"
          stroke="currentColor"
          stroke-width="4"
        ></circle>
        <path
          class="opacity-75"
          fill="currentColor"
          d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
        ></path>
      </svg>
      <span class="sr-only">Loading...</span>
    </span>
    <span :class="{ 'opacity-0': loading }">
      <slot />
    </span>
  </component>
</template>
