<script setup lang="ts">
import { provide, ref, watch } from 'vue';
import { onClickOutside } from '@vueuse/core';

defineOptions({
  inheritAttrs: false,
});

const props = withDefaults(defineProps<{
  modelValue: boolean;
  align?: 'start' | 'center' | 'end';
  side?: 'top' | 'right' | 'bottom' | 'left';
  sideOffset?: number;
  class?: string;
}>(), {
  modelValue: false,
  align: 'start',
  side: 'bottom',
  sideOffset: 0,
});

const emit = defineEmits(['update:modelValue']);

const isOpen = ref(props.modelValue);
const triggerRef = ref<HTMLElement | null>(null);
const contentRef = ref<HTMLElement | null>(null);

watch(() => props.modelValue, (newValue) => {
  isOpen.value = newValue;
});

watch(isOpen, (newValue) => {
  emit('update:modelValue', newValue);
});

const positionClasses = computed(() => ({
  'absolute z-50 min-w-[8rem] overflow-hidden rounded-md border bg-popover p-1 text-popover-foreground shadow-md': true,
  'data-[side=bottom]:translate-y-1 data-[side=left]:-translate-x-1 data-[side=right]:translate-x-1 data-[side=top]:-translate-y-1': true,
  [props.class || '']: true
}));

const alignClasses = {
  start: 'origin-top-left left-0',
  center: 'origin-top',
  end: 'origin-top-right right-0'
};

const sideClasses = {
  top: 'bottom-full mb-2',
  right: 'left-full ml-2',
  bottom: 'top-full mt-2',
  left: 'right-full mr-2'
};

const close = () => {
  isOpen.value = false;
};

const toggle = () => {
  isOpen.value = !isOpen.value;
};

onClickOutside(contentRef, (event) => {
  if (triggerRef.value && !triggerRef.value.contains(event.target as Node)) {
    close();
  }
}, { ignore: [triggerRef] });

provide('dropdownMenu', {
  isOpen,
  close,
  toggle,
  align: toRef(props, 'align'),
  side: toRef(props, 'side'),
  sideOffset: toRef(props, 'sideOffset')
});
</script>

<template>
  <div class="relative inline-block text-left">
    <div ref="triggerRef" @click="toggle">
      <slot name="trigger" :is-open="isOpen" :close="close" :toggle="toggle" />
    </div>
    
    <Teleport to="body">
      <Transition
        enter-active-class="transition duration-100 ease-out"
        enter-from-class="transform scale-95 opacity-0"
        enter-to-class="transform scale-100 opacity-100"
        leave-active-class="transition duration-75 ease-in"
        leave-from-class="transform scale-100 opacity-100"
        leave-to-class="transform scale-95 opacity-0"
      >
        <div
          v-if="isOpen"
          ref="contentRef"
          :class="[
            positionClasses,
            alignClasses[align],
            sideClasses[side],
          ]"
          :style="{
            '--radix-dropdown-menu-content-transform-origin': 'var(--radix-popper-transform-origin)',
            '--radix-dropdown-menu-content-available-width': 'var(--radix-popper-available-width)',
            '--radix-dropdown-menu-content-available-height': 'var(--radix-popper-available-height)',
            '--radix-dropdown-menu-trigger-width': 'var(--radix-popper-anchor-width)',
            '--radix-dropdown-menu-trigger-height': 'var(--radix-popper-anchor-height)',
          }"
          @click.stop
        >
          <slot />
        </div>
      </Transition>
    </Teleport>
  </div>
</template>
