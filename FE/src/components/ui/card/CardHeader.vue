<script setup lang="ts">
import { computed } from 'vue';

const props = withDefaults(defineProps<{
  class?: string;
}>(), {});

const classes = computed(() => ({
  'flex flex-col space-y-1.5 p-6': true,
  [props.class || '']: true
}));
</script>

<template>
  <div :class="classes">
    <slot />
  </div>
</template>
