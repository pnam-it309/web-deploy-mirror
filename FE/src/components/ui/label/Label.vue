<script setup lang="ts">
import { computed } from 'vue';

const props = withDefaults(defineProps<{
  for?: string;
  class?: string;
  required?: boolean;
}>(), {
  required: false
});

const classes = computed(() => ({
  'text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70': true,
  [props.class || '']: true
}));
</script>

<template>
  <label :for="for" :class="classes">
    <slot />
    <span v-if="required" class="text-destructive ml-1">*</span>
  </label>
</template>
