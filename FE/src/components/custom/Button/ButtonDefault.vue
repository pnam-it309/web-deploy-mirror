<template>
  <button
    type="button"
    :class="[
      'inline-flex items-center justify-center rounded-md font-medium transition-all duration-200 focus:outline-none focus:ring-2 focus:ring-offset-2 disabled:opacity-50 disabled:cursor-not-allowed shadow-sm active:scale-95',
      sizeClasses,
      colorClasses
    ]"
    :disabled="disabled || loading"
    @click="handleClick"
  >
    <svg
      v-if="loading"
      class="animate-spin -ml-1 mr-2 h-4 w-4 text-current"
      xmlns="http://www.w3.org/2000/svg"
      fill="none"
      viewBox="0 0 24 24"
    >
      <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
      <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
    </svg>
    <slot />
  </button>
</template>

<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps({
  color: {
    type: String,
    default: 'coffee',
  },
  size: {
    type: String,
    default: 'default',
  },
  loading: {
    type: Boolean,
    default: false,
  },
  disabled: {
    type: Boolean,
    default: false,
  },
})

const emit = defineEmits(['click'])

const handleClick = (event: MouseEvent) => {
  if (props.loading || props.disabled) {
    event.preventDefault()
    event.stopPropagation()
    return
  }
  emit('click', event) 
}

const colorClasses = computed(() => {
  switch (props.color) {
    // --- SOLID BRAND COLORS (Màu bệt) ---
    // Cream (#f0ead2) - Nền sáng, chữ tối (Mocha)
    case 'cream':
      return 'bg-[#f0ead2] text-[#6c584c] hover:bg-[#e6dfc0] focus:ring-[#adc178] border border-transparent'
    
    // Sage (#dde5b6) - Nền xanh nhạt, chữ tối (Mocha)
    case 'sage':
      return 'bg-[#dde5b6] text-[#6c584c] hover:bg-[#d0dba0] focus:ring-[#adc178] border border-transparent'
    
    // Olive (#adc178) - Nền xanh oliu, chữ trắng
    case 'olive':
      return 'bg-[#adc178] text-white hover:bg-[#9cb365] focus:ring-[#dde5b6] border border-transparent'
    
    // Coffee (#a98467) - Nền nâu cà phê, chữ trắng
    case 'coffee':
      return 'bg-[#a98467] text-white hover:bg-[#967259] focus:ring-[#f0ead2] border border-transparent'
    
    // Mocha (#6c584c) - Nền nâu đậm, chữ trắng
    case 'mocha':
      return 'bg-[#6c584c] text-white hover:bg-[#5a483e] focus:ring-[#a98467] border border-transparent'

    // --- TRANSLUCENT / SOFT BRAND COLORS
    case 'cream-soft':
      return 'bg-[#f0ead2]/50 text-[#6c584c] hover:bg-[#f0ead2]/80 focus:ring-[#f0ead2] border border-transparent'

    case 'sage-soft':
      return 'bg-[#dde5b6]/40 text-[#5a483e] hover:bg-[#dde5b6]/70 focus:ring-[#dde5b6] border border-transparent'

    case 'olive-soft':
      return 'bg-[#adc178]/20 text-[#5a483e] hover:bg-[#adc178]/40 focus:ring-[#adc178] border border-transparent'

    case 'coffee-soft':
      return 'bg-[#a98467]/15 text-[#a98467] hover:bg-[#a98467]/30 focus:ring-[#a98467] border border-transparent'

    case 'mocha-soft':
      return 'bg-[#6c584c]/10 text-[#6c584c] hover:bg-[#6c584c]/25 focus:ring-[#6c584c] border border-transparent'

    // Fallback (Nếu lỡ nhập sai thì về Coffee)
    default: 
      return 'bg-[#a98467] text-white hover:bg-[#967259] focus:ring-[#f0ead2] border border-transparent'
  }
})

const sizeClasses = computed(() => {
  switch (props.size) {
    case 'small':
      return 'px-3 py-1.5 text-xs'
    case 'large':
      return 'px-6 py-3 text-base'
    default:
      return 'px-4 py-2 text-sm'
  }
})
</script>