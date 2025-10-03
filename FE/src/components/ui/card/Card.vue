<script setup lang="ts">
import { computed } from 'vue';

const props = withDefaults(defineProps<{
  variant?: 'default' | 'outline';
  class?: string;
}>(), {
  variant: 'default'
});

const classes = computed(() => ({
  'rounded-lg border bg-card text-card-foreground shadow-sm': true,
  'border-border': props.variant === 'default',
  'border-border/50': props.variant === 'outline',
  [props.class || '']: true
}));
</script>

<template>
  <div :class="classes">
    <slot />
  </div>
</template>
