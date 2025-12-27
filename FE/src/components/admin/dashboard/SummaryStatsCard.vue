<template>
  <div
    class="bg-white rounded-xl shadow-sm p-6 flex items-center justify-between border border-gray-100 hover:shadow-md transition-shadow duration-200">
    <div>
      <p class="text-sm font-medium text-gray-500 mb-1">{{ title }}</p>
      <h4 class="text-2xl font-bold text-gray-900">{{ value }}</h4>
    </div>
    <div :class="['w-12 h-12 rounded-full flex items-center justify-center', colorClass]">
      <slot name="icon">
        <component :is="icon" class="w-6 h-6" />
      </slot>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';

const props = defineProps({
  title: {
    type: String,
    required: true,
  },
  value: {
    type: [String, Number],
    required: true,
  },
  icon: {
    type: Object, // Component
    default: null,
  },
  color: {
    type: String,
    default: 'blue',
    // Relaxed validator or expanded list
    validator: (val: string) => ['blue', 'purple', 'green', 'orange', 'red', 'pink', 'indigo', 'emerald', 'amber', 'rose'].includes(val),
  }
});

const colorClass = computed(() => {
  const colors: Record<string, string> = {
    blue: 'bg-blue-50 text-blue-600',
    purple: 'bg-purple-50 text-purple-600',
    green: 'bg-green-50 text-green-600',
    orange: 'bg-orange-50 text-orange-600',
    red: 'bg-red-50 text-red-600',
    pink: 'bg-pink-50 text-pink-600',
    indigo: 'bg-indigo-50 text-indigo-600',
    emerald: 'bg-emerald-50 text-emerald-600',
    amber: 'bg-amber-50 text-amber-600',
    rose: 'bg-rose-50 text-rose-600',
  };
  return colors[props.color] || colors.blue;
});

</script>
