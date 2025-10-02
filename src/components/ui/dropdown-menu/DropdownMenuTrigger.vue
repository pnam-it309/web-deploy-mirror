<script setup lang="ts">
import { inject, computed } from 'vue';

const props = withDefaults(defineProps<{
  asChild?: boolean;
  class?: string;
}>(), {
  asChild: false
});

const { toggle } = inject('dropdownMenu', {
  isOpen: false,
  toggle: () => {}
});

const classes = computed(() => ({
  'flex cursor-pointer select-none items-center rounded-sm px-2 py-1.5 text-sm outline-none focus:bg-accent focus:text-accent-foreground data-[state=open]:bg-accent data-[state=open]:text-accent-foreground': true,
  [props.class || '']: true
}));

const handleClick = (e: Event) => {
  e.preventDefault();
  toggle();
};
</script>

<template>
  <component
    :is="asChild ? 'div' : 'button'"
    :class="classes"
    @click="handleClick"
  >
    <slot />
  </component>
</template>
