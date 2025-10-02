<script setup lang="ts">
import { computed } from 'vue';

const props = withDefaults(defineProps<{
  disabled?: boolean;
  class?: string;
  asChild?: boolean;
}>(), {
  disabled: false,
  asChild: false
});

const { close } = inject('dropdownMenu', {
  close: () => {}
});

const classes = computed(() => ({
  'relative flex cursor-default select-none items-center rounded-sm px-2 py-1.5 text-sm outline-none transition-colors focus:bg-accent focus:text-accent-foreground data-[disabled]:pointer-events-none data-[disabled]:opacity-50': true,
  [props.class || '']: true
}));

const handleClick = (e: Event) => {
  if (props.disabled) {
    e.preventDefault();
    return;
  }
  close();
};
</script>

<template>
  <component
    :is="asChild ? 'div' : 'button'"
    :class="classes"
    :disabled="disabled"
    :aria-disabled="disabled"
    @click="handleClick"
  >
    <slot />
  </component>
</template>
