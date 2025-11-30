<template>
  <div
    :class="[
      'col-span-12 xl:col-span-8 rounded-xl shadow-sm backdrop-blur-sm border transition-all duration-300 hover:shadow-md',
      containerClasses
    ]"
  >
    <div
      v-if="props.label"
      :class="[
        'px-5 py-3.5 rounded-t-xl border-b',
        headerClasses
      ]"
    >
      <div class="flex items-center gap-2.5">
        <div :class="['text-lg flex items-center', iconClasses]">
          <slot name="icon" />
        </div>
        <span :class="['text-base font-bold tracking-wide', textClasses]">
          {{ props.label }}
        </span>
      </div>
    </div>

    <!-- Body -->
    <div class="px-6 py-6 sm:px-7.5">
      <div class="flex justify-end mb-4" v-if="$slots.extra">
        <slot name="extra" />
      </div>
      <slot />
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps({
  label: String,
  color: { type: String, default: 'default' } // cream, sage, coffee...
})

const containerClasses = computed(() => {
  switch(props.color) {
    case 'cream': return 'bg-[#fffdf5] border-[#e6dfc0]'
    case 'sage': return 'bg-[#f7f9ef] border-[#dde5b6]'
    case 'coffee': return 'bg-[#fffaf5] border-[#a98467]/30'
    default: return 'bg-white/90 border-[#e6dfc0]' // Mặc định hơi hướng Cream
  }
})

const headerClasses = computed(() => {
  switch(props.color) {
    case 'cream': return 'bg-[#f0ead2]/40 border-[#e6dfc0]'
    case 'sage': return 'bg-[#dde5b6]/30 border-[#dde5b6]'
    case 'coffee': return 'bg-[#a98467]/10 border-[#a98467]/30'
    default: return 'bg-[#f0ead2]/30 border-[#e6dfc0]'
  }
})

const iconClasses = computed(() => {
  switch(props.color) {
    case 'sage': return 'text-[#6a994e]'
    case 'coffee': return 'text-[#a98467]'
    default: return 'text-[#adc178]' // Olive
  }
})

const textClasses = computed(() => {
  switch(props.color) {
    case 'coffee': return 'text-[#8c6239]'
    default: return 'text-[#6c584c]' // Mocha
  }
})
</script>