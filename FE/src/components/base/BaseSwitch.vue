<script setup lang="ts">
interface Props {
  modelValue: boolean;
  label?: string;
}
defineProps<Props>();
defineEmits(['update:modelValue']);
</script>

<template>
  <label class="flex items-center cursor-pointer gap-3 group">
    <div class="relative">
      <input 
        type="checkbox" 
        class="sr-only" 
        :checked="modelValue"
        @change="$emit('update:modelValue', ($event.target as HTMLInputElement).checked)"
      >
      <div class="block bg-gray-200 w-10 h-6 rounded-full transition-colors duration-300 group-hover:bg-gray-300"
           :class="{ '!bg-primary': modelValue }"></div>
      <div class="absolute left-1 top-1 bg-white w-4 h-4 rounded-full transition-transform duration-300 shadow-sm"
           :class="{ 'translate-x-4': modelValue }"></div>
    </div>
    <span v-if="label" class="text-sm font-medium text-dark select-none">{{ label }}</span>
  </label>
</template>