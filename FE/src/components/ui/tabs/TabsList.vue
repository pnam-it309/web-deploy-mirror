<script setup lang="ts">
import { computed } from 'vue';

const props = withDefaults(defineProps<{
  class?: string;
}>(), {});

const classes = computed(() => ({
  'inline-flex h-10 items-center justify-center rounded-md bg-muted p-1 text-muted-foreground': true,
  [props.class || '']: true
}));
</script>

<template>
  <div role="tablist" :class="classes">
    <slot />
  </div>
</template>
