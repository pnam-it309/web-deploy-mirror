<script setup lang="ts">
import { computed } from 'vue';

const props = withDefaults(defineProps<{
  class?: string;
}>(), {});

const classes = computed(() => ({
  'ml-auto text-xs tracking-widest opacity-60': true,
  [props.class || '']: true
}));
</script>

<template>
  <span :class="classes">
    <slot />
  </span>
</template>
