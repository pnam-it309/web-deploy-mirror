<script setup lang="ts">
interface Props {
  // 1. Thêm dấu ? để biến này thành optional, hoặc thêm | undefined | null
  modelValue?: string | number | null; 
  label?: string;
  placeholder?: string;
  type?: string;
  error?: string;
}

// withDefaults giúp gán giá trị mặc định nếu cha không truyền
withDefaults(defineProps<Props>(), {
  modelValue: '', // Mặc định là chuỗi rỗng
  type: 'text'
});

defineEmits(['update:modelValue']);
</script>

<template>
  <div class="flex flex-col gap-1.5">
    <label v-if="label" class="text-sm font-semibold text-dark uppercase tracking-wider text-[11px]">
      {{ label }}
    </label>
    
    <input
      :type="type"
      
      :value="modelValue || ''"
      
      @input="$emit('update:modelValue', ($event.target as HTMLInputElement).value)"
      :placeholder="placeholder"
      class="w-full px-4 py-2.5 bg-white border border-gray-200 text-dark rounded-sm 
             focus:outline-none focus:border-primary focus:ring-1 focus:ring-primary/50
             placeholder-gray-400 transition-all duration-300 text-sm"
      :class="{ 'border-red-500 focus:border-red-500 focus:ring-red-200': error }"
    />
    
    <span v-if="error" class="text-xs text-red-500 mt-1">{{ error }}</span>
  </div>
</template>