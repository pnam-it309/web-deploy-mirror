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
    <label v-if="label" class="text-[11px] font-bold text-dark uppercase tracking-wider">
      {{ label }}
    </label>
    <textarea
      :value="modelValue"
      @input="$emit('update:modelValue', ($event.target as HTMLTextAreaElement).value)"
      :rows="rows"
      :placeholder="placeholder"
      class="w-full px-4 py-3 bg-white border border-gray-200 text-dark rounded-sm 
             focus:outline-none focus:border-primary focus:ring-1 focus:ring-primary/50
             placeholder-gray-400 transition-all duration-300 text-sm resize-y"
      :class="{ 'border-red-500 focus:border-red-500': error }"
    ></textarea>
    <span v-if="error" class="text-xs text-red-500">{{ error }}</span>
  </div>
</template>