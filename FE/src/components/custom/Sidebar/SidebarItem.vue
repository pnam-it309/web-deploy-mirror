<script setup lang="ts">
import { useSidebarStore } from '@/stores/sidebar.store' // Import store mới
import { useRoute } from 'vue-router'
import { computed } from 'vue'
import SidebarDropdown from './SidebarDropDown.vue'

const sidebarStore = useSidebarStore()
const route = useRoute()

interface SidebarItemProps {
  item: {
    label: string
    icon?: any // Cho phép icon là component
    routeName?: string
    children?: any[]
  }
  index: number
  isCollapsed: boolean // Nhận prop từ AdminSidebar để ẩn text khi thu nhỏ
}

const props = defineProps<SidebarItemProps>()
const emit = defineEmits(['item-click'])

// Kiểm tra xem mục này có đang active không
const isActive = computed(() => {
  if (props.item.routeName) {
    return route.name === props.item.routeName
  }
  // Nếu là group cha, check xem con có active không
  if (props.item.children) {
    return sidebarStore.page === props.item.label
  }
  return false
})

const handleItemClick = () => {
  if (props.item.children) {
    // Toggle dropdown
    sidebarStore.page = sidebarStore.page === props.item.label ? '' : props.item.label
  } else {
    // Set selected item
    sidebarStore.selected = props.item.label
    emit('item-click')
  }
}
</script>

<template>
  <li>
    <router-link
      :to="item.routeName ? { name: item.routeName } : ''"
      @click.prevent="item.children ? handleItemClick() : null"
      @click="!item.children ? handleItemClick() : null"
      class="
        group relative flex items-center gap-3 rounded-lg px-4 py-3 font-medium text-sm duration-300 ease-in-out
        hover:bg-[#fffdf5] hover:text-[#6c584c]
      "
      :class="{
        'bg-[#f7f9ef] text-[#386641]': isActive && !item.children, // Active state cho item đơn
        'bg-[#fffdf5] text-[#6c584c]': isActive && item.children,  // Active state cho group cha đang mở
        'text-gray-600': !isActive
      }"
    >
      <!-- Icon -->
      <component 
        :is="item.icon" 
        class="h-5 w-5 transition-colors"
        :class="{ 'text-[#adc178]': isActive, 'text-gray-400 group-hover:text-[#a98467]': !isActive }" 
      />

      <!-- Label (Ẩn khi collapsed) -->
      <span v-if="!isCollapsed" class="transition-opacity duration-300">{{ item.label }}</span>

      <!-- Arrow Icon cho Dropdown -->
      <svg
        v-if="item.children && !isCollapsed"
        class="absolute right-4 top-1/2 -translate-y-1/2 fill-current w-4 h-4 transition-transform duration-300"
        :class="{ 'rotate-180': sidebarStore.page === item.label }"
        viewBox="0 0 20 20"
      >
        <path
          fill-rule="evenodd"
          d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z"
          clip-rule="evenodd"
        />
      </svg>
    </router-link>

    <!-- Dropdown Menu -->
    <div
      v-if="item.children && !isCollapsed"
      v-show="sidebarStore.page === item.label"
      class="overflow-hidden transition-all duration-300"
    >
      <SidebarDropdown
        :items="item.children"
        @item-click="$emit('item-click')"
      />
    </div>
  </li>
</template>