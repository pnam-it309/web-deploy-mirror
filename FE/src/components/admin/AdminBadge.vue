<template>
  <span
    class="inline-flex items-center rounded-md px-2 py-1 text-xs font-medium ring-1 ring-inset"
    :class="computedClasses"
  >
    <slot>{{ label }}</slot>
  </span>
</template>

<script setup lang="ts">
import { computed } from 'vue';

const props = defineProps({
  label: {
    type: String,
    default: '',
  },
  variant: {
    type: String,
    default: 'default',
    validator: (value: string) =>
      ['default', 'success', 'warning', 'danger', 'info', 'primary'].includes(value),
  },
});

const computedClasses = computed(() => {
  const variants = {
    default: 'bg-gray-50 text-gray-600 ring-gray-500/10',
    success: 'bg-green-50 text-green-700 ring-green-600/20',
    warning: 'bg-yellow-50 text-yellow-800 ring-yellow-600/20',
    danger: 'bg-red-50 text-red-700 ring-red-600/10',
    info: 'bg-blue-50 text-blue-700 ring-blue-700/10',
    primary: 'bg-indigo-50 text-indigo-700 ring-indigo-700/10',
  };

  return variants[props.variant as keyof typeof variants];
});
</script>
