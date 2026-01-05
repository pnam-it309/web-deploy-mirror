```html
<template>
  <!-- Spacer for Layout Flow (Push content only when locked) -->
  <div class="hidden lg:block h-full flex-shrink-0 transition-[width] duration-300 ease-[cubic-bezier(0.4,0,0.2,1)]"
    :class="[sidebarStore.isDesktopExpanded ? 'w-72' : 'w-24']"></div>

  <!-- Actual Sidebar (Overlay on Hover) -->
  <aside
    class="fixed inset-y-0 left-0 z-50 bg-white border-r-4 border-[#1e293b] rounded-r-[3rem] transform transition-all duration-300 ease-[cubic-bezier(0.4,0,0.2,1)] flex flex-col h-full overflow-hidden"
    :class="[isExpanded ? 'w-72 shadow-2xl' : 'w-24 pl-2']" @mouseenter="isHovered = true"
    @mouseleave="isHovered = false">
    <!-- Logo -->
    <div
      class="h-24 flex items-center justify-between px-6 bg-[#1e293b] text-white rounded-tr-[2.5rem] relative overflow-hidden transition-all duration-300 flex-shrink-0">
      <div v-show="isExpanded"
        class="flex items-center gap-3 font-bold text-2xl opacity-100 transition-opacity duration-300 whitespace-nowrap">
        <span>Admin Panel</span>
      </div>
      <!-- Toggle Button -->
      <!-- Kept as requested. Toggles the 'persistent' expand state. -->
      <button @click="sidebarStore.toggleDesktopSidebar"
        class="p-2 rounded-full hover:bg-white/10 text-white focus:outline-none transition-colors absolute right-6">
        <!-- Icon logic: if manually expanded, show close. If collapsed (but hover expanded), show... 
              Actually simpler: just toggle the store state.
         -->
        <ChevronDoubleLeftIcon v-if="sidebarStore.isDesktopExpanded" class="w-6 h-6" />
        <ChevronDoubleRightIcon v-else class="w-6 h-6" />
      </button>
    </div>

    <!-- Scrollable content -->
    <div class="flex-1 overflow-y-auto py-8 space-y-6 custom-scrollbar flex flex-col w-full relative">
      <!-- Menu Section -->
      <div class="w-full px-4">

        <nav class="relative w-full flex flex-col gap-4 z-0">
          <!-- Fluid Active Indicator (Sliding Pill) -->
          <div
            class="absolute left-0 w-full h-14 bg-[#1e293b] rounded-2xl shadow-lg shadow-blue-900/20 z-0 pointer-events-none transition-all duration-500 ease-[cubic-bezier(0.34,1.56,0.64,1)]"
            :class="[activeIndex === -1 ? 'opacity-0 scale-95' : 'opacity-100 scale-100']"
            :style="{ transform: `translateY(${Math.max(0, activeIndex) * 72}px)` }"></div>

          <router-link v-for="(item, index) in menuItems" :key="item.path" :to="item.path"
            class="group flex items-center h-14 w-full text-base font-medium rounded-2xl transition-colors duration-200 relative z-10 px-4 overflow-hidden outline-none"
            :class="[
              !isExpanded ? 'justify-center' : '',
              !isActive(item.path) ? 'hover:bg-gray-100' : ''
            ]">

            <!-- Icon -->
            <component :is="item.icon" :class="[
              isActive(item.path) ? 'text-white' : 'text-gray-400 group-hover:text-[#1e293b]',
              'flex-shrink-0 h-8 w-8 transition-colors duration-300 relative z-20',
              isExpanded ? 'mr-4' : ''
            ]" aria-hidden="true" />

            <!-- Text Label -->
            <div class="overflow-hidden transition-all duration-300 ease-in-out whitespace-nowrap relative z-20"
              :class="[isExpanded ? 'w-full opacity-100 delay-75' : 'w-0 opacity-0']">
              <span class="truncate block"
                :class="[isActive(item.path) ? 'text-white' : 'text-gray-500 group-hover:text-[#1e293b]']">
                {{ item.name }}
              </span>
            </div>
          </router-link>
        </nav>
      </div>

    </div>

    <!-- Bottom Decoration -->
    <div class="h-20 bg-white relative flex-shrink-0">
    </div>
  </aside>
</template>

<script setup lang="ts">
import { useRoute } from 'vue-router';
import { useSidebarStore } from '@/stores/sidebar.store'
import { computed, ref } from 'vue'
import {
  Squares2X2Icon,
  FolderIcon,
  CubeIcon,
  CpuChipIcon,
  PuzzlePieceIcon,
  ChevronDoubleLeftIcon,
  ChevronDoubleRightIcon
} from '@heroicons/vue/24/outline';

const route = useRoute();
const sidebarStore = useSidebarStore()
const isHovered = ref(false)

// Combined expanded state: either manually expanded (locked) OR hovered
const isExpanded = computed(() => sidebarStore.isDesktopExpanded || isHovered.value)

const menuItems = [
  { name: 'Trang chủ', path: '/admin/dashboard', icon: Squares2X2Icon },
  { name: 'Lĩnh vực', path: '/admin/domains', icon: FolderIcon },
  { name: 'Ứng dụng', path: '/admin/apps', icon: CubeIcon },
  { name: 'Công nghệ', path: '/admin/technologies', icon: CpuChipIcon },
  { name: 'Chức năng', path: '/admin/features', icon: PuzzlePieceIcon },
];

const isActive = (path: string) => {
  return route.path.startsWith(path);
}

const activeIndex = computed(() => {
  return menuItems.findIndex(item => isActive(item.path))
})
</script>

<style scoped>
/* Custom scrollbar for better aesthetics */
.custom-scrollbar::-webkit-scrollbar {
  width: 5px;
}

.custom-scrollbar::-webkit-scrollbar-track {
  background: transparent;
}

.custom-scrollbar::-webkit-scrollbar-thumb {
  background-color: #f1f5f9;
  border-radius: 20px;
}

.custom-scrollbar:hover::-webkit-scrollbar-thumb {
  background-color: #cbd5e1;
}
</style>
