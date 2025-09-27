<script setup lang="ts">
import { computed } from 'vue';

const props = withDefaults(defineProps<{
  modelValue?: boolean;
  disabled?: boolean;
  class?: string;
}>(), {
  modelValue: false,
  disabled: false
});

const emit = defineEmits(['update:modelValue']);

const checked = computed({
  get() {
    return props.modelValue;
  },
  set(value) {
    emit('update:modelValue', value);
  }
});

const toggle = () => {
  if (!props.disabled) {
    checked.value = !checked.value;
  }
};

const switchClasses = computed(() => ({
  'inline-flex h-6 w-11 items-center rounded-full transition-colors focus:outline-none focus:ring-2 focus:ring-ring focus:ring-offset-2 focus:ring-offset-background': true,
  'bg-primary': checked.value && !props.disabled,
  'bg-muted-foreground/30': !checked.value && !props.disabled,
  'cursor-not-allowed opacity-50': props.disabled,
  'cursor-pointer': !props.disabled,
  [props.class || '']: true
}));

const thumbClasses = computed(() => ({
  'pointer-events-none block h-5 w-5 rounded-full bg-background shadow-lg ring-0 transition-transform': true,
  'translate-x-6': checked.value,
  'translate-x-1': !checked.value,
}));
</script>

<template>
  <button
    type="button"
    role="switch"
    :aria-checked="checked"
    :disabled="disabled"
    :class="switchClasses"
    @click="toggle"
  >
    <span :class="thumbClasses" />
  </button>
</template>
