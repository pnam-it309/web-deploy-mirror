<template>
  <div class="page-wrapper flex flex-col bg-gray-50/50">
    <div
      :class="[
        'content-wrapper flex-grow p-6 shadow-sm border rounded-xl m-4 backdrop-blur-sm',
        wrapperColorClasses
      ]"
    >
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
  // Logic tiêu đề cũ giữ nguyên
  if (route.name === 'StudentDetails') return 'Chi tiết sinh viên'
  if (route.name === 'StudentList') return 'Quản lý sinh viên'
  return props.label
})

const wrapperColorClasses = computed(() => {
  switch(props.color) {
    case 'cream': return 'bg-[#fffdf5] border-[#e6dfc0]'
    case 'sage': return 'bg-[#f7f9ef] border-[#dde5b6]'
    default: return 'bg-white/90 border-gray-200/80'
  }
})

const iconColorClass = computed(() => {
    switch(props.color) {
        case 'sage': return 'text-[#6a994e]'
        default: return 'text-[#a98467]' // Coffee
    }
})

const linkColorClass = computed(() => {
    switch(props.color) {
        case 'sage': return 'text-[#386641] hover:text-[#6a994e]'
        default: return 'text-[#6c584c] hover:text-[#a98467]' // Mocha -> Coffee
    }
})
</script>

<style scoped>
</style>