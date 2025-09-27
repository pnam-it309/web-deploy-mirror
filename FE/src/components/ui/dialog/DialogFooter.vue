<script setup lang="ts">
import { computed } from 'vue';

const props = withDefaults(defineProps<{
  class?: string;
}>(), {});

const classes = computed(() => ({
  'flex flex-col-reverse sm:flex-row sm:justify-end sm:space-x-2': true,
  [props.class || '']: true
}));
</script>

<template>
  <div :class="classes">
    <slot />
  </div>
</template>
