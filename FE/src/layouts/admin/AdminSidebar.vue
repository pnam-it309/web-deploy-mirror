<template>
  <div class="hidden lg:block h-full flex-shrink-0 transition-[width] duration-300 ease-[cubic-bezier(0.4,0,0.2,1)]"
    :class="[sidebarStore.isDesktopExpanded ? 'w-64' : 'w-20']"></div>

  <aside
    class="fixed inset-y-0 left-0 z-50 bg-white border-r border-gray-100 shadow-xl transform transition-all duration-300 ease-[cubic-bezier(0.4,0,0.2,1)] flex flex-col h-full overflow-hidden"
    :class="[isExpanded ? 'w-64' : 'w-20']" @mouseenter="isHovered = true" @mouseleave="isHovered = false">
    <div
      class="h-16 flex items-center justify-between px-4 bg-dark text-white relative transition-all duration-300 flex-shrink-0">
      <div
        class="flex items-center gap-2 font-bold text-xl transition-all duration-300 whitespace-nowrap overflow-hidden"
        :class="[isExpanded ? 'w-full opacity-100' : 'w-0 opacity-0']">
        <span class="text-primary">UDPM</span> Panel
      </div>

      <button @click="sidebarStore.toggleDesktopSidebar"
        class="p-1.5 rounded-md hover:bg-white/10 text-white focus:outline-none transition-colors absolute right-4 z-20">
        <ChevronDoubleLeftIcon v-if="sidebarStore.isDesktopExpanded" class="w-5 h-5" />
        <ChevronDoubleRightIcon v-else class="w-5 h-5" />
      </button>
    </div>

    <div class="flex-1 overflow-y-auto py-6 px-3 space-y-2 custom-scrollbar flex flex-col w-full">
      <router-link v-for="item in menuItems" :key="item.path" :to="item.path"
        class="group flex items-center h-12 w-full text-sm font-medium rounded-md transition-all duration-200 relative overflow-hidden"
        :class="[
          isActive(item.path)
            ? 'bg-primary/10 text-primary'
            : 'text-gray-500 hover:bg-gray-50 hover:text-dark'
        ]">
        <div class="absolute left-0 top-0 bottom-0 w-1 bg-primary transition-all duration-300"
          :class="[isActive(item.path) ? 'opacity-100' : 'opacity-0']"></div>

        <component :is="item.icon" class="flex-shrink-0 w-6 h-6 transition-colors duration-300 ml-2 relative z-10"
          :class="[isActive(item.path) ? 'text-primary' : 'text-gray-400 group-hover:text-dark']" />

        <span class="ml-3 whitespace-nowrap transition-all duration-300 z-10"
          :class="[isExpanded ? 'opacity-100 translate-x-0' : 'opacity-0 -translate-x-4']">
          {{ item.name }}
        </span>
      </router-link>
    </div>

    <div class="p-4 border-t border-gray-100 bg-gray-50">
      <div class="flex items-center gap-3 overflow-hidden">
        <div
          class="w-8 h-8 rounded-full bg-primary text-white flex items-center justify-center font-bold text-xs flex-shrink-0">
          AD
        </div>
        <div class="flex flex-col transition-opacity duration-300" :class="[isExpanded ? 'opacity-100' : 'opacity-0']">
          <span class="text-xs font-bold text-dark truncate">Admin User</span>
          <span class="text-[10px] text-gray-500 truncate">admin@udpm.vn</span>
        </div>
      </div>
    </div>
  </aside>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue';
import { useRoute } from 'vue-router';
import { useSidebarStore } from '@/stores/sidebar.store';
import {
  Squares2X2Icon,
  FolderIcon,
  CubeIcon,
  CpuChipIcon,
  PuzzlePieceIcon,
  ChevronDoubleLeftIcon,
  ChevronDoubleRightIcon,
  ComputerDesktopIcon
} from '@heroicons/vue/24/outline';

const route = useRoute();
const sidebarStore = useSidebarStore();
const isHovered = ref(false);

const isExpanded = computed(() => sidebarStore.isDesktopExpanded || isHovered.value);

const menuItems = [
  { name: 'Tổng quan', path: '/admin/dashboard', icon: Squares2X2Icon },
  { name: 'Cấu hình Trang chủ', path: '/admin/homepage-config', icon: ComputerDesktopIcon },
  { name: 'Sản phẩm', path: '/admin/apps', icon: CubeIcon },
  { name: 'Lĩnh vực', path: '/admin/domains', icon: FolderIcon },
  { name: 'Công nghệ', path: '/admin/technologies', icon: CpuChipIcon },
  { name: 'Chức năng', path: '/admin/features', icon: PuzzlePieceIcon },
];
const isActive = (path: string) => route.path.startsWith(path);
</script>

<style scoped>
.custom-scrollbar::-webkit-scrollbar {
  width: 0px;
  background: transparent;
}
</style>