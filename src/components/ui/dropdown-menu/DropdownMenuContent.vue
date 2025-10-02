<script setup lang="ts">
import { computed, onMounted, onUnmounted } from 'vue';

const props = withDefaults(defineProps<{
  class?: string;
}>(), {});

const { align, side, sideOffset } = inject('dropdownMenu', {
  align: 'start',
  side: 'bottom',
  sideOffset: 0
});

const classes = computed(() => ({
  'z-50 min-w-[8rem] overflow-hidden rounded-md border bg-popover p-1 text-popover-foreground shadow-md animate-in data-[side=bottom]:slide-in-from-top-2 data-[side=left]:slide-in-from-right-2 data-[side=right]:slide-in-from-left-2 data-[side=top]:slide-in-from-bottom-2': true,
  [props.class || '']: true
}));

const handleKeydown = (e: KeyboardEvent) => {
  if (e.key === 'Escape') {
    e.preventDefault();
    e.stopPropagation();
    close?.();
  }
};

onMounted(() => {
  document.addEventListener('keydown', handleKeydown);
});

onUnmounted(() => {
  document.removeEventListener('keydown', handleKeydown);
});
</script>

<template>
  <div
    :data-side="side"
    :data-align="align"
    :style="{
      '--radix-dropdown-menu-content-transform-origin': 'var(--radix-popper-transform-origin)',
      '--radix-dropdown-menu-content-available-width': 'var(--radix-popper-available-width)',
      '--radix-dropdown-menu-content-available-height': 'var(--radix-popper-available-height)',
      '--radix-dropdown-menu-trigger-width': 'var(--radix-popper-anchor-width)',
      '--radix-dropdown-menu-trigger-height': 'var(--radix-popper-anchor-height)',
    }"
    :class="classes"
  >
    <slot />
  </div>
</template>
