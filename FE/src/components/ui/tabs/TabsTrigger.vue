<script setup lang="ts">
import { inject, computed } from 'vue';

const props = withDefaults(defineProps<{
  value: string;
  disabled?: boolean;
  class?: string;
}>(), {
  disabled: false
});

const { activeTab, activateTab } = inject('tabs', {
  activeTab: '',
  activateTab: () => {}
});

const isActive = computed(() => activeTab === props.value);

const classes = computed(() => ({
  'inline-flex items-center justify-center whitespace-nowrap rounded-sm px-3 py-1.5 text-sm font-medium ring-offset-background transition-all focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50': true,
  'bg-background text-foreground shadow-sm': isActive.value,
  'text-muted-foreground hover:text-foreground': !isActive.value,
  [props.class || '']: true
}));

const handleClick = () => {
  if (!props.disabled) {
    activateTab(props.value);
  }
};
</script>

<template>
  <button
    type="button"
    role="tab"
    :aria-selected="isActive"
    :disabled="disabled"
    :class="classes"
    @click="handleClick"
  >
    <slot />
  </button>
</template>
