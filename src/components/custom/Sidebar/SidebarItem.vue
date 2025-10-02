<script setup lang="ts">
import { useSidebarStore } from '@/stores/sidebar'
import { useRoute } from 'vue-router'
import SidebarDropdown from './SidebarDropDown.vue'

const sidebarStore = useSidebarStore()
const route = useRoute()

interface SidebarItem {
  label: string
  icon: string
  routeName?: string
  children?: SidebarItem[]
}

const props = defineProps<{
  item: SidebarItem
  index: number
}>()

const currentPage = route.name

const handleItemClick = () => {
  if (props.item.children) {
    sidebarStore.page = sidebarStore.page === props.item.label ? '' : props.item.label
  } else {
    sidebarStore.page = props.item.label
  }
}
</script>

<template>
  <li>
    <router-link
      :to="item.routeName ? { name: item.routeName } : ''"
      class="group relative flex items-center gap-2.5 rounded-md py-2 px-4 font-medium text-gray-700 hover:text-blue-600 hover:bg-gray-100 transition-colors duration-300"
      :class="{
        'bg-gray-200 text-blue-600': sidebarStore.page === item.label
      }"
      @click.prevent="item.children ? handleItemClick() : null"
      @click="!item.children ? handleItemClick() : null"
    >
      <span
        v-html="item.icon"
        class="w-5 h-5"
        :class="{ 'text-blue-600': sidebarStore.page === item.label }"
      ></span>

      <span>{{ item.label }}</span>

      <svg
        v-if="item.children"
        class="absolute right-4 top-1/2 -translate-y-1/2 transform transition-transform duration-300"
        :class="{ 'rotate-180': sidebarStore.page === item.label }"
        width="20"
        height="20"
        viewBox="0 0 20 20"
      >
        <path
          fill="currentColor"
          d="M4.41 6.91c.33-.33.86-.33 1.18 0L10 11.32l4.41-4.41c.33-.33.86-.33 1.18 0 .33.33.33.86 0 1.18l-5 5c-.33.33-.86.33-1.18 0l-5-5c-.33-.33-.33-.86 0-1.18z"
        />
      </svg>
    </router-link>

    <!-- Dropdown Menu -->
    <div
      v-show="sidebarStore.page === item.label"
      class="overflow-hidden transition-all duration-300"
    >
      <SidebarDropdown
        v-if="item.children"
        :items="item.children"
        :currentPage="currentPage"
        :page="item.label"
      />
    </div>
  </li>
</template>
