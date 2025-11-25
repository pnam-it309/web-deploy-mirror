<template>
  <div
    v-if="show"
    class="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50"
  >
    <div class="bg-white rounded-lg shadow-lg w-full max-w-2xl text-gray-900">
      <div class="flex justify-between items-center px-6 py-4 border-b">
        <h3 class="text-lg font-semibold">
          <slot name="title">Tiêu đề Modal</slot>
        </h3>
        <button @click="$emit('close')" class="text-gray-500 hover:text-gray-700 text-xl">
          ×
        </button>
      </div>

      <div class="px-6 py-4">
        <slot />
      </div>

      <div class="flex justify-end space-x-3 px-6 py-4 border-t">
        <slot name="footer" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps({
  show: {
    type: Boolean,
    default: false
  }
})
defineEmits(['close'])
</script>