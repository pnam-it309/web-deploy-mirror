<script setup lang="ts">
import { computed } from 'vue';

const props = withDefaults(defineProps<{
  class?: string;
}>(), {});

const classes = computed(() => ({
  '-mx-1 my-1 h-px bg-muted': true,
  [props.class || '']: true
}));
</script>

<template>
  <div :class="classes" role="separator" />
</template>
