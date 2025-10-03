<script setup lang="ts">
import { computed } from 'vue';

const props = withDefaults(defineProps<{
  class?: string;
}>(), {});

const classes = computed(() => ({
  'flex items-center p-6 pt-0': true,
  [props.class || '']: true
}));
</script>

<template>
  <div :class="classes">
    <slot />
  </div>
</template>
