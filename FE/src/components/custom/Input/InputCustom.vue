<template>
  <div class="flex flex-col text-left">
    <label 
      v-if="label" 
      :class="['block text-sm font-medium mb-1.5 ml-1', labelColorClass]"
    >
      {{ label }} <span v-if="required" class="text-red-500 ml-0.5">*</span>
    </label>
    
    <div class="relative">
      <input
        :value="modelValue"
        @input="$emit('update:modelValue', ($event.target as HTMLInputElement).value)"
        :type="type"
        :min="min"
        :placeholder="placeholder"
        :disabled="disabled"
        :class="[
          'block w-full rounded-lg border text-sm font-medium py-2.5 px-3 shadow-sm transition-all duration-200 ease-in-out focus:outline-none focus:ring-2 placeholder:text-gray-400',
          inputColorClasses,
          disabled ? 'opacity-60 cursor-not-allowed' : ''
        ]"
      />
      <slot name="suffix" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps({
  modelValue: [String, Number],
  label: String,
  type: { type: String, default: 'text' },
  min: { type: [String, Number], default: undefined },
  placeholder: String,
  disabled: { type: Boolean, default: false },
  required: { type: Boolean, default: false },
  color: { type: String, default: 'default' } // Thêm prop color
})

defineEmits(['update:modelValue'])

const labelColorClass = computed(() => {
  switch (props.color) {
    case 'cream': case 'sage': return 'text-[#6c584c]'
    case 'olive': return 'text-[#386641]'
    case 'coffee': return 'text-[#a98467]'
    case 'mocha': return 'text-[#6c584c]'
    default: return 'text-[#5a483e]'
  }
})

const inputColorClasses = computed(() => {
  switch (props.color) {
    case 'cream': return 'bg-[#f0ead2]/50 border-[#f0ead2] text-[#6c584c] focus:border-[#a98467] focus:ring-[#a98467]/20 hover:bg-[#f0ead2]/80'
    case 'sage': return 'bg-[#dde5b6]/40 border-[#dde5b6] text-[#5a483e] focus:border-[#adc178] focus:ring-[#adc178]/20 hover:bg-[#dde5b6]/60'
    case 'olive': return 'bg-[#adc178]/10 border-[#adc178]/30 text-[#386641] focus:border-[#adc178] focus:ring-[#adc178]/20 hover:bg-[#adc178]/20'
    case 'coffee': return 'bg-[#a98467]/10 border-[#a98467]/30 text-[#6c584c] focus:border-[#a98467] focus:ring-[#a98467]/20 hover:bg-[#a98467]/20'
    case 'mocha': return 'bg-[#6c584c]/5 border-[#6c584c]/20 text-[#6c584c] focus:border-[#6c584c] focus:ring-[#6c584c]/20 hover:bg-[#6c584c]/10'
    default: return 'bg-white/60 border-gray-200 text-gray-700 hover:bg-white/80 hover:border-gray-300 focus:border-blue-500 focus:ring-blue-500/20 focus:bg-white'
  }
})
</script>

<style scoped>
</style>