<script setup lang="ts">
import { computed } from 'vue';

const props = withDefaults(defineProps<{
  src?: string;
  alt?: string;
  size?: 'sm' | 'md' | 'lg' | 'xl' | '2xl';
  class?: string;
}>(), {
  size: 'md',
  alt: 'User avatar'
});

const sizeClasses = {
  'sm': 'h-8 w-8',
  'md': 'h-10 w-10',
  'lg': 'h-12 w-12',
  'xl': 'h-16 w-16',
  '2xl': 'h-20 w-20'
};

const classes = computed(() => ({
  'relative flex shrink-0 overflow-hidden rounded-full': true,
  [sizeClasses[props.size]]: true,
  [props.class || '']: true
}));
</script>

<template>
  <div :class="classes">
    <img
      v-if="src"
      :src="src"
      :alt="alt"
      class="h-full w-full object-cover"
    >
    <slot v-else />
  </div>
</template>
