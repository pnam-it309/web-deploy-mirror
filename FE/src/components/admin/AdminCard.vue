<template>
  <div class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
    <div v-if="$slots.header || title" class="px-6 py-4 border-b border-gray-100 flex items-center justify-between">
      <slot name="header">
        <h3 class="text-lg font-semibold text-gray-900">{{ title }}</h3>
        <slot name="actions" />
      </slot>
    </div>
    <div class="p-6" :class="bodyClass">
      <slot />
    </div>
    <div v-if="$slots.footer" class="px-6 py-4 bg-gray-50 border-t border-gray-100">
      <slot name="footer" />
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps({
  title: {
    type: String,
    default: '',
  },
  bodyClass: {
    type: String,
    default: '',
  }
});
</script>
