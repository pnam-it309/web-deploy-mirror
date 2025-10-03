<script setup lang="ts">
import { computed } from 'vue';

const props = withDefaults(defineProps<{
  class?: string;
}>(), {});

const classes = computed(() => ({
  'text-sm text-muted-foreground': true,
  [props.class || '']: true
}));
</script>

<template>
  <p :class="classes">
    <slot />
  </p>
</template>
