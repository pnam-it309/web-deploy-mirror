<script setup lang="ts">
interface Option {
  value: string | number;
  label: string;
}
interface Props {
  modelValue?: string | number | null | undefined;
  options: Option[];
  label?: string;
  placeholder?: string;
  error?: string;
}
defineProps<Props>();
defineEmits(['update:modelValue']);
</script>
<template>
  <div class="flex flex-col gap-1.5">
    <label v-if="label" class="text-[11px] font-bold text-dark uppercase tracking-wider">
      {{ label }}
    </label>
    <div class="relative">
      <select
        :value="modelValue"
        @change="$emit('update:modelValue', ($event.target as HTMLSelectElement).value)"
        class="w-full px-4 py-2.5 bg-white border border-gray-200 text-dark rounded-sm 
               focus:outline-none focus:border-primary focus:ring-1 focus:ring-primary/50
               appearance-none cursor-pointer text-sm transition-all duration-300"
        :class="{ 'text-gray-400': !modelValue, 'border-red-500': error }"
      >
        <option value="" disabled selected>{{ placeholder || 'Chọn một tùy chọn' }}</option>
        <option v-for="opt in options" :key="opt.value" :value="opt.value">
          {{ opt.label }}
        </option>
      </select>
      <div class="absolute inset-y-0 right-0 flex items-center px-3 pointer-events-none text-gray-500">
        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path></svg>
      </div>
    </div>
    <span v-if="error" class="text-xs text-red-500">{{ error }}</span>
  </div>
</template>