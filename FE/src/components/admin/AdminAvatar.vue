<template>
  <div :class="['relative inline-block', sizeClasses]">
    <img
      v-if="src"
      :src="src"
      :alt="alt"
      class="h-full w-full rounded-full object-cover ring-2 ring-white"
    />
    <div
      v-else
      class="h-full w-full rounded-full bg-gray-200 flex items-center justify-center text-gray-500 font-semibold ring-2 ring-white"
    >
      {{ initials }}
    </div>
    <span
      v-if="status"
      :class="[
        'absolute bottom-0 right-0 block rounded-full ring-2 ring-white transform translate-x-1/4 translate-y-1/4',
        statusColorClasses,
        statusSizeClasses
      ]"
    />
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';

const props = defineProps({
  src: {
    type: String,
    default: '',
  },
  alt: {
    type: String,
    default: 'Avatar',
  },
  name: {
    type: String,
    default: '',
  },
  size: {
    type: String,
    default: 'md',
    validator: (value: string) => ['sm', 'md', 'lg', 'xl'].includes(value),
  },
  status: {
    type: String,
    default: '', // 'online', 'offline', 'busy', etc.
  },
});

const sizeClasses = computed(() => {
  const sizes = {
    sm: 'h-8 w-8',
    md: 'h-10 w-10',
    lg: 'h-12 w-12',
    xl: 'h-14 w-14',
  };
  return sizes[props.size as keyof typeof sizes];
});

const statusSizeClasses = computed(() => {
  const sizes = {
    sm: 'h-2 w-2',
    md: 'h-2.5 w-2.5',
    lg: 'h-3 w-3',
    xl: 'h-3.5 w-3.5',
  };
  return sizes[props.size as keyof typeof sizes];
});

const statusColorClasses = computed(() => {
  const colors = {
    online: 'bg-green-400',
    offline: 'bg-gray-400',
    busy: 'bg-red-400',
    away: 'bg-yellow-400',
  };
  return colors[props.status as keyof typeof colors] || 'bg-gray-400';
});

const initials = computed(() => {
  if (!props.name) return props.alt.charAt(0).toUpperCase();
  return props.name
    .split(' ')
    .map((n) => n[0])
    .slice(0, 2)
    .join('')
    .toUpperCase();
});
</script>
