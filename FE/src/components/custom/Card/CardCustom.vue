<template>
  <div 
    :class="[
      'backdrop-blur-sm mt-4 border rounded-xl shadow-sm overflow-hidden transition-all duration-300 hover:shadow-md',
      cardColorClasses
    ]"
  >
    <div class="overflow-x-auto">
      <slot />
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps({
  color: { type: String, default: 'default' }
})

const cardColorClasses = computed(() => {
  switch (props.color) {
    case 'cream': return 'bg-[#fffdf5]/80 border-[#e6dfc0]' // Cream siêu nhạt
    case 'sage': return 'bg-[#f7f9ef]/80 border-[#dde5b6]' // Sage siêu nhạt
    case 'olive': return 'bg-[#fcfdf9] border-[#adc178]/30'
    case 'coffee': return 'bg-[#fffaf5] border-[#a98467]/20'
    case 'mocha': return 'bg-[#faf9f8] border-[#6c584c]/20'
    default: return 'bg-white/90 border-gray-200/80'
  }
})
</script>