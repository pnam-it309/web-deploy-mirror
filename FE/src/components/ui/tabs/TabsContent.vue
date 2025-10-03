<script setup lang="ts">
import { inject, computed } from 'vue';

const props = withDefaults(defineProps<{
  value: string;
  class?: string;
}>(), {});

const { activeTab } = inject('tabs', {
  activeTab: ''
});

const isActive = computed(() => activeTab === props.value);

const classes = computed(() => ({
  'mt-2 ring-offset-background focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2': true,
  [props.class || '']: true
}));
</script>

<template>
  <div 
    v-if="isActive"
    role="tabpanel"
    :class="classes"
    tabindex="0"
  >
    <slot />
  </div>
</template>
