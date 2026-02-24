<script setup lang="ts">
interface Props {
  modelValue?: string | null;
  label?: string;
  placeholder?: string;
  rows?: number;
  error?: string;
}
withDefaults(defineProps<Props>(), { rows: 4 });
defineEmits(['update:modelValue']);
</script>

<template>
  <div class="flex flex-col gap-1.5">
    <label v-if="label" class="text-[11px] font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider">
      {{ label }}
    </label>
    <textarea
      :value="modelValue"
      @input="$emit('update:modelValue', ($event.target as HTMLTextAreaElement).value)"
      :rows="rows"
      :placeholder="placeholder"
      class="w-full px-4 py-3 bg-white dark:bg-gray-800 border border-gray-200 dark:border-gray-700
             text-gray-900 dark:text-white rounded-lg
             focus:outline-none focus:border-primary focus:ring-2 focus:ring-primary/20
             placeholder-gray-400 dark:placeholder-gray-500 transition-all duration-300 text-sm resize-y shadow-sm"
      :class="{ '!border-red-500 focus:!border-red-500 focus:!ring-red-200': error }"
    ></textarea>
    <span v-if="error" class="text-xs text-red-500 mt-1">{{ error }}</span>
  </div>
</template>