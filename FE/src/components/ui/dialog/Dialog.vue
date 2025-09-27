<script setup lang="ts">
import { computed } from 'vue';

defineOptions({
  inheritAttrs: false,
});

const props = withDefaults(defineProps<{
  open: boolean;
  class?: string;
}>(), {
  open: false
});

const emit = defineEmits(['update:open']);

const isOpen = computed({
  get() {
    return props.open;
  },
  set(value) {
    emit('update:open', value);
  },
});

const classes = computed(() => ({
  'fixed inset-0 z-50 flex items-start justify-center sm:items-center': true,
  [props.class || '']: true
}));
</script>

<template>
  <Teleport to="body">
    <div v-if="isOpen" class="fixed inset-0 z-50 bg-background/80 backdrop-blur-sm" />
    <Transition
      enter-active-class="duration-200 ease-out"
      enter-from-class="opacity-0"
      enter-to-class="opacity-100"
      leave-active-class="duration-200 ease-in"
      leave-from-class="opacity-100"
      leave-to-class="opacity-0"
    >
      <div v-if="isOpen" :class="classes" v-bind="$attrs">
        <Transition
          enter-active-class="duration-200 ease-out"
          enter-from-class="opacity-0 scale-95"
          enter-to-class="opacity-100 scale-100"
          leave-active-class="duration-200 ease-in"
          leave-from-class="opacity-100 scale-100"
          leave-to-class="opacity-0 scale-95"
        >
          <div class="fixed inset-0 z-50 flex items-center justify-center p-4 sm:p-0">
            <slot />
          </div>
        </Transition>
      </div>
    </Transition>
  </Teleport>
</template>
