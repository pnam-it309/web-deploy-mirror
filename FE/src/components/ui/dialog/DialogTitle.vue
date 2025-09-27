<script setup lang="ts">
import { computed } from 'vue';

const props = withDefaults(defineProps<{
  class?: string;
}>(), {});

const classes = computed(() => ({
  'text-lg font-semibold leading-none tracking-tight': true,
  [props.class || '']: true
}));
</script>

<template>
  <h2 :class="classes">
    <slot />
  </h2>
</template>
