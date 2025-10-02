<script setup lang="ts">
import { computed } from 'vue';

const props = withDefaults(defineProps<{
  modelValue?: string;
  placeholder?: string;
  disabled?: boolean;
  class?: string;
  rows?: number;
}>(), {
  disabled: false,
  rows: 3
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
  'flex min-h-[80px] w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50': true,
  [props.class || '']: true
}));
</script>

<template>
  <textarea
    v-model="value"
    :placeholder="placeholder"
    :disabled="disabled"
    :class="classes"
    :rows="rows"
    v-bind="$attrs"
  ></textarea>
</template>
