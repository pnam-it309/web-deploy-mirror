<script setup lang="ts">
import { computed } from 'vue';

const props = withDefaults(defineProps<{
  orientation?: 'horizontal' | 'vertical';
  class?: string;
}>(), {
  orientation: 'horizontal'
});

const classes = computed(() => ({
  'shrink-0 bg-border': true,
  'h-[1px] w-full': props.orientation === 'horizontal',
  'h-full w-[1px]': props.orientation === 'vertical',
  [props.class || '']: true
}));
</script>

<template>
  <div 
    role="separator"
    :aria-orientation="orientation"
    :class="classes"
    v-bind="$attrs"
  />
</template>
