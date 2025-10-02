<script setup lang="ts">
import { computed } from 'vue';

const props = withDefaults(defineProps<{
  src?: string;
  alt?: string;
  class?: string;
}>(), {
  alt: 'User avatar'
});

const classes = computed(() => ({
  'h-full w-full object-cover': true,
  [props.class || '']: true
}));
</script>

<template>
  <img
    v-if="src"
    :src="src"
    :alt="alt"
    :class="classes"
  >
</template>
