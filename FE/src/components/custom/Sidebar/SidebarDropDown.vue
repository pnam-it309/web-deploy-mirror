<script setup lang="ts">
import { useSidebarStore } from '@/stores/sidebar.store' // Import store mới
import { useRoute } from 'vue-router'
import { computed } from 'vue'

const sidebarStore = useSidebarStore()
const route = useRoute()

const props = defineProps(['items'])
const emit = defineEmits(['item-click'])

const handleItemClick = (label: string) => {
  sidebarStore.selected = label
  emit('item-click')
}

// Helper check active state
const isActive = (routeName: string) => {
  return route.name === routeName
}
</script>

<template>
  <ul class="mt-2 mb-4 flex flex-col gap-1 pl-9"> <!-- Tăng padding-left để thụt vào -->
    <template v-for="(childItem, index) in items" :key="index">
      <li>
        <router-link
          :to="{ name: childItem.routeName }"
          @click="handleItemClick(childItem.label)"
          class="
            group relative flex items-center gap-2.5 rounded-md px-4 py-2 font-medium text-sm duration-300 ease-in-out
            hover:text-[#6c584c]
          "
          :class="{
            'text-[#386641] bg-[#f7f9ef]': isActive(childItem.routeName), // Active con
            'text-gray-500': !isActive(childItem.routeName)
          }"
        >
          <!-- Dấu chấm tròn nhỏ trang trí -->
          <span 
             class="w-1.5 h-1.5 rounded-full transition-colors"
             :class="isActive(childItem.routeName) ? 'bg-[#adc178]' : 'bg-gray-300 group-hover:bg-[#a98467]'"
          ></span>
          
          {{ childItem.label }}
        </router-link>
      </li>
    </template>
  </ul>
</template>