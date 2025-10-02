<script setup lang="ts">
import { provide, ref, watch, computed } from 'vue';

const props = withDefaults(defineProps<{
  modelValue?: string;
  defaultValue?: string;
  class?: string;
}>(), {
  defaultValue: ''
});

const emit = defineEmits(['update:modelValue']);

const activeTab = ref(props.defaultValue);

// Update active tab when modelValue changes
watch(() => props.modelValue, (newValue) => {
  if (newValue !== undefined) {
    activeTab.value = newValue;
  }
}, { immediate: true });

// Update modelValue when active tab changes
watch(activeTab, (newValue) => {
  emit('update:modelValue', newValue);
});

const activateTab = (value: string) => {
  activeTab.value = value;
};

provide('tabs', {
  activeTab,
  activateTab
});
</script>

<template>
  <div :class="['tabs', props.class]">
    <slot />
  </div>
</template>
