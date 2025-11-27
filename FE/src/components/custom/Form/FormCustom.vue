<script setup lang="ts">
const props = defineProps({
  customClasses: String,
  customClassesLabel: String,
  label: String
})
</script>

<template>
  <div
    class="
      col-span-12 xl:col-span-8
      rounded-xl shadow-sm
      bg-white/90 backdrop-blur-sm
      border border-[#e6dfc0] /* Viền màu Cream đậm */
      px-6 pt-6 pb-8 sm:px-8
      transition-all duration-300
      hover:shadow-md
    "
    :class="props.customClasses"
  >
    <!-- Header Form -->
    <div class="flex items-center justify-between mb-6 border-b border-[#e6dfc0] pb-4" v-if="props.label || $slots.extra">
      <h1
        v-if="props.label"
        class="text-xl font-bold text-[#6c584c] tracking-wide"
        :class="props.customClassesLabel"
      >
        {{ props.label }}
      </h1>
      <div v-if="$slots.extra">
        <slot name="extra" class="px-3"></slot>
      </div>
    </div>

    <!-- Form Content -->
    <slot></slot>
  </div>
</template>