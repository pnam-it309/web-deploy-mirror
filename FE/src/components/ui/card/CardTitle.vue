<script setup lang="ts">
import { computed } from 'vue';

const props = withDefaults(defineProps<{
  class?: string;
  as?: string;
}>(), {
  as: 'h3'
});

const classes = computed(() => ({
  'text-lg font-semibold leading-none tracking-tight': true,
  [props.class || '']: true
}));
</script>

<template>
  <component :is="as" :class="classes">
    <slot />
  </component>
</template>
