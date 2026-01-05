<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue';

interface Option {
  value: string | number;
  label: string;
}

interface Props {
  modelValue: (string | number)[]; 
  options: Option[];
  label?: string;
  placeholder?: string;
}

const props = defineProps<Props>();
const emit = defineEmits(['update:modelValue']);

const isOpen = ref(false);
const containerRef = ref<HTMLElement | null>(null);

// Toggle chọn/bỏ chọn
const toggleOption = (value: string | number) => {
  const newValue = [...props.modelValue];
  const index = newValue.indexOf(value);
  if (index === -1) {
    newValue.push(value);
  } else {
    newValue.splice(index, 1);
  }
  emit('update:modelValue', newValue);
};

// Hiển thị text các item đã chọn
const selectedLabels = computed(() => {
  if (props.modelValue.length === 0) return '';
  return props.options
    .filter(opt => props.modelValue.includes(opt.value))
    .map(opt => opt.label)
    .join(', ');
});

// Click outside để đóng dropdown
const handleClickOutside = (event: MouseEvent) => {
  if (containerRef.value && !containerRef.value.contains(event.target as Node)) {
    isOpen.value = false;
  }
};

onMounted(() => document.addEventListener('click', handleClickOutside));
onUnmounted(() => document.removeEventListener('click', handleClickOutside));
</script>

<template>
  <div class="flex flex-col gap-1.5 relative" ref="containerRef">
    <label v-if="label" class="text-[11px] font-bold text-dark uppercase tracking-wider">
      {{ label }}
    </label>
    
    <div 
      @click="isOpen = !isOpen"
      class="w-full px-4 py-2.5 bg-white border border-gray-200 rounded-sm cursor-pointer flex justify-between items-center min-h-[42px]"
      :class="{'border-primary ring-1 ring-primary/50': isOpen}"
    >
      <div v-if="modelValue.length > 0" class="flex flex-wrap gap-1">
        <span 
          v-for="val in modelValue" 
          :key="val"
          class="bg-gray-100 text-dark text-xs font-medium px-2 py-0.5 rounded border border-gray-200"
        >
          {{ options.find(o => o.value === val)?.label }}
          <span class="ml-1 text-gray-400 hover:text-red-500" @click.stop="toggleOption(val)">&times;</span>
        </span>
      </div>
      <span v-else class="text-gray-400 text-sm">{{ placeholder || 'Chọn nhiều...' }}</span>
      
      <svg class="w-4 h-4 text-gray-400 transition-transform" :class="{'rotate-180': isOpen}" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path></svg>
    </div>

    <div v-if="isOpen" class="absolute top-full left-0 w-full mt-1 bg-white border border-gray-200 shadow-lg rounded-sm z-50 max-h-60 overflow-y-auto custom-scrollbar">
      <div 
        v-for="opt in options" 
        :key="opt.value"
        @click="toggleOption(opt.value)"
        class="px-4 py-2 hover:bg-yellow-50 cursor-pointer flex items-center gap-3 text-sm text-dark transition-colors"
        :class="{'bg-yellow-50/50 font-medium': modelValue.includes(opt.value)}"
      >
        <div class="w-4 h-4 border border-gray-300 rounded flex items-center justify-center bg-white" :class="{'!bg-primary !border-primary': modelValue.includes(opt.value)}">
           <svg v-if="modelValue.includes(opt.value)" class="w-3 h-3 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" /></svg>
        </div>
        {{ opt.label }}
      </div>
    </div>
  </div>
</template>