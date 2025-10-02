<script setup lang="ts">
import { computed } from 'vue';

const props = withDefaults(defineProps<{
  modelValue?: string | number;
  type?: string;
  placeholder?: string;
  disabled?: boolean;
  class?: string;
}>(), {
  type: 'text',
  disabled: false,
});

const emit = defineEmits(['update:modelValue']);

const value = computed({
  get() {
    return props.modelValue;
  },
  set(value) {
    emit('update:modelValue', value);
  },
});

const classes = computed(() => ({
  'flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50': true,
  [props.class || '']: true
}));
</script>

<template>
  <input
    v-model="value"
    :type="type"
    :placeholder="placeholder"
    :disabled="disabled"
    :class="classes"
    v-bind="$attrs"
  >
</template>
