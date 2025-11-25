<template>
  <div>
    <label class="block text-sm font-medium text-gray-700 m-2">{{ label }}</label>
    <input
      :value="modelValue"
      @input="$emit('update:modelValue', ($event.target as HTMLInputElement).value)"
      :type="type"
      :min="min"
      class="input"
    />
  </div>
</template>

<script setup lang="ts">
defineProps({
  modelValue: [String, Number],
  label: String,
  type: { type: String, default: 'text' },
  min: { type: [String, Number], default: undefined }
})
defineEmits(['update:modelValue'])
</script>

<style scoped>
.input {
  @apply mt-1 block w-full rounded-md border border-gray-300 shadow-sm
  focus:ring-pink-500 focus:border-pink-500 sm:text-sm text-gray-900 px-3 py-2;
}
</style>