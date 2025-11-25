<template>
  <div>
    <label class="block text-sm font-medium text-gray-700">{{ label }}</label>
    <textarea
      :value="modelValue"
      @input="$emit('update:modelValue', ($event.target as HTMLTextAreaElement).value)"
      class="input h-24"
    ></textarea>
  </div>
</template>

<script setup lang="ts">
defineProps({
  modelValue: String,
  label: String
})
defineEmits(['update:modelValue'])
</script>

<style scoped>
.input {
  @apply mt-1 block w-full rounded-md border border-gray-300 shadow-sm
  focus:ring-pink-500 focus:border-pink-500 sm:text-sm text-gray-900;
}
</style>