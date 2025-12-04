<template>
  <div class="page-wrapper flex flex-col min-h-screen bg-gray-50/30">
    <div
      :class="[
        'content-wrapper flex-grow p-6 shadow-sm border rounded-xl m-4 backdrop-blur-sm',
        wrapperColorClasses
      ]"
    >
      <!-- ... (Nội dung tiêu đề giữ nguyên) ... -->
      <div v-if="label" class="ml-1 mb-6 flex items-center gap-2">
        <div :class="iconColorClass">
          <slot name="icon" />
        </div>
        
        <router-link 
          :to="link || '#'"
          :class="['text-lg font-bold transition-colors duration-200', linkColorClass]"
        >
          {{ pageTitle }}
        </router-link>
      </div>
      
      <slot />
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const props = defineProps({
  label: String,
  link: String,
  color: { type: String, default: 'default' }
})

const route = useRoute()
const pageTitle = computed(() => {
    // (Logic tiêu đề giữ nguyên)
    return props.label
})

const wrapperColorClasses = computed(() => {
  switch(props.color) {
    case 'cream': return 'bg-[#fffdf5]/90 border-[#e6dfc0]'
    case 'sage': return 'bg-[#f7f9ef]/90 border-[#dde5b6]'
    case 'coffee': return 'bg-[#fffaf5]/90 border-[#a98467]/20'
    default: return 'bg-white/80 border-gray-200/60'
  }
})

const iconColorClass = computed(() => {
    switch(props.color) {
        case 'sage': return 'text-[#6a994e]'
        default: return 'text-[#a98467]'
    }
})

const linkColorClass = computed(() => {
    switch(props.color) {
        case 'sage': return 'text-[#386641] hover:text-[#6a994e]'
        default: return 'text-[#6c584c] hover:text-[#a98467]'
    }
})
</script>