<template>
  <button
    :class="computedClasses"
    :disabled="disabled"
    @click="$emit('click', $event)"
  >
    <slot name="prefix">
      <i v-if="icon" :class="icon" class="mr-2"></i>
    </slot>
    <slot />
    <slot name="suffix" />
  </button>
</template>

<script setup lang="ts">
import { computed } from 'vue';

const props = defineProps({
  variant: {
    type: String,
    default: 'primary',
    validator: (value: string) =>
      ['primary', 'secondary', 'outline', 'ghost', 'danger'].includes(value),
  },
  size: {
    type: String,
    default: 'md',
    validator: (value: string) => ['sm', 'md', 'lg', 'icon'].includes(value),
  },
  icon: {
    type: String,
    default: '',
  },
  disabled: {
    type: Boolean,
    default: false,
  },
  block: {
    type: Boolean,
    default: false,
  },
}); // End of props

defineEmits(['click']);

const computedClasses = computed(() => {
  const baseClasses =
    'inline-flex items-center justify-center rounded-lg font-medium transition-colors focus:outline-none focus:ring-2 focus:ring-offset-2 disabled:opacity-50 disabled:pointer-events-none';

  const variantClasses = {
    primary: 'bg-[#1e293b] text-white hover:bg-[#334155] focus:ring-[#1e293b]', // Dark blue based on screenshot
    secondary: 'bg-white text-gray-700 border border-gray-200 hover:bg-gray-50 focus:ring-gray-200',
    outline: 'border border-gray-300 text-gray-700 bg-transparent hover:bg-gray-50 focus:ring-gray-500',
    ghost: 'text-gray-600 hover:bg-gray-100 hover:text-gray-900 focus:ring-gray-500',
    danger: 'bg-red-500 text-white hover:bg-red-600 focus:ring-red-500',
  };

  const sizeClasses = {
    sm: 'h-8 px-3 text-xs',
    md: 'h-10 px-4 py-2 text-sm',
    lg: 'h-12 px-6 text-base',
    icon: 'h-10 w-10 p-2',
  };

  const widthClass = props.block ? 'w-full' : '';

  return [
    baseClasses,
    variantClasses[props.variant as keyof typeof variantClasses],
    sizeClasses[props.size as keyof typeof sizeClasses],
    widthClass,
  ].join(' ');
});
</script>
