<script setup lang="ts">
import { computed } from 'vue';

const props = withDefaults(defineProps<{
  delayMs?: number;
  class?: string;
}>(), {
  delayMs: 0
});

const classes = computed(() => ({
  'flex h-full w-full items-center justify-center rounded-full bg-muted': true,
  [props.class || '']: true
}));
</script>

<template>
  <div :class="classes">
    <slot />
  </div>
</template>
