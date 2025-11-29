<template>
  <div class="flex flex-col text-left">
    <label v-if="label" :class="['block text-sm font-medium mb-1.5 ml-1', labelColorClass]">
      {{ label }}
    </label>
    
    <div class="relative group">
      <select
        :value="modelValue"
        @change="$emit('update:modelValue', ($event.target as HTMLSelectElement).value)"
        :disabled="disabled"
        :class="[
          'appearance-none block w-full rounded-lg border text-sm font-medium py-2.5 pl-3 pr-10 shadow-sm transition-all duration-200 ease-in-out focus:outline-none focus:ring-2',
          inputColorClasses,
          disabled ? 'opacity-60 cursor-not-allowed' : ''
        ]"
      >
        <slot />
      </select>

      <div :class="['pointer-events-none absolute inset-y-0 right-0 flex items-center px-3 transition-colors', iconColorClass]">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
        </svg>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps({
  modelValue: [String, Number],
  label: String,
  disabled: { type: Boolean, default: false },
  color: { type: String, default: 'default' } // cream, sage, olive, coffee, mocha
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

const iconColorClass = computed(() => {
   switch (props.color) {
    case 'cream': case 'sage': return 'text-[#6c584c]/60'
    case 'olive': return 'text-[#adc178]'
    case 'coffee': return 'text-[#a98467]'
    case 'mocha': return 'text-[#6c584c]'
    default: return 'text-gray-400 group-hover:text-gray-600'
  }
})
</script>

<style scoped>
</style>