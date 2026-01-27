<template>
  <div class="hidden lg:block h-full flex-shrink-0 transition-[width] duration-300 ease-[cubic-bezier(0.4,0,0.2,1)]"
    :class="[sidebarStore.isDesktopExpanded ? 'w-72' : 'w-20']"></div>

  <!-- Mobile Overlay -->
  <div v-if="sidebarStore.isMobileOpen" 
       @click="sidebarStore.closeMobileSidebar"
       class="fixed inset-0 bg-black/50 z-40 lg:hidden backdrop-blur-sm transition-opacity duration-300">
  </div>

  <aside
    class="fixed inset-y-0 left-0 z-50 bg-white dark:bg-gray-800 border-r border-gray-100 dark:border-gray-700 shadow-xl transform transition-all duration-300 ease-[cubic-bezier(0.4,0,0.2,1)] flex flex-col h-full overflow-hidden lg:translate-x-0"
    :class="[
      isExpanded ? 'w-72' : 'w-20',
      sidebarStore.isMobileOpen ? 'translate-x-0' : '-translate-x-full'
    ]" 
    @mouseenter="isHovered = true" 
    @mouseleave="isHovered = false">
    
    <!-- Logo Section -->
    <div
      class="h-16 flex items-center justify-between px-6 bg-white dark:bg-gray-800 text-gray-900 dark:text-white relative transition-all duration-300 flex-shrink-0 border-b border-gray-100 dark:border-gray-700">
      <div
        class="flex items-center gap-3 font-bold text-2xl transition-all duration-300 whitespace-nowrap overflow-hidden"
        :class="[isExpanded ? 'w-full opacity-100' : 'w-0 opacity-0']">
        <div class="w-10 h-10 rounded-xl bg-gradient-to-tr from-blue-600 to-indigo-600 flex items-center justify-center text-white shadow-lg shadow-blue-600/20">
             <span class="font-serif italic text-xl">U</span>
        </div>
        <span class="font-serif tracking-tight">UDPM <span class="text-blue-600 dark:text-blue-400">Admin</span></span>
      </div>

       <div v-if="!isExpanded" class="w-full flex justify-center">
            <div class="w-10 h-10 rounded-xl bg-gradient-to-tr from-blue-600 to-indigo-600 flex items-center justify-center text-white shadow-lg shadow-blue-600/20">
                 <span class="font-serif italic text-xl">U</span>
            </div>
       </div>

      <button v-if="isExpanded" @click="sidebarStore.toggleDesktopSidebar"
        class="p-2 rounded-lg hover:bg-gray-100 dark:hover:bg-gray-700 text-gray-500 dark:text-gray-400 focus:outline-none transition-all absolute right-4 z-20">
        <ChevronDoubleLeftIcon class="w-5 h-5" />
      </button>
    </div>

    <!-- Menu Items -->
    <div class="flex-1 overflow-y-auto py-6 px-4 space-y-1.5 custom-scrollbar flex flex-col w-full">
      <router-link v-for="item in menuItems" :key="item.path" :to="item.path"
        class="group flex items-center h-12 w-full text-sm font-bold rounded-xl transition-all duration-200 relative overflow-hidden"
        :class="[
          isActive(item.path)
            ? 'bg-blue-50 dark:bg-blue-900/20 text-blue-700 dark:text-blue-400 shadow-sm'
            : 'text-gray-600 dark:text-gray-400 hover:bg-gray-50 dark:hover:bg-gray-700/50 hover:text-gray-900 dark:hover:text-white'
        ]">
        
        <component :is="item.icon" class="flex-shrink-0 w-6 h-6 transition-colors duration-300 ml-3 relative z-10"
          :class="[isActive(item.path) ? 'text-blue-600 dark:text-blue-400' : 'text-gray-400 dark:text-gray-500 group-hover:text-gray-600 dark:group-hover:text-gray-300']" />

        <span class="ml-4 whitespace-nowrap transition-all duration-300 z-10 font-bold tracking-wide"
          :class="[isExpanded ? 'opacity-100 translate-x-0' : 'opacity-0 -translate-x-4']">
          {{ item.name }}
        </span>
        
         <div v-if="isActive(item.path)" class="absolute right-3 w-2 h-2 rounded-full bg-blue-600 dark:bg-blue-400 shadow-md shadow-blue-600/20" 
            :class="[isExpanded ? 'opacity-100' : 'opacity-0']"></div>
      </router-link>
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
  ComputerDesktopIcon,
  PhotoIcon,
  ClipboardDocumentListIcon,
  TrashIcon,
  BoltIcon,
  EnvelopeIcon,
  ShieldCheckIcon,
  HandThumbUpIcon,
  MagnifyingGlassIcon
} from '@heroicons/vue/24/outline';

const route = useRoute();
const sidebarStore = useSidebarStore();
const isHovered = ref(false);

const isExpanded = computed(() => sidebarStore.isDesktopExpanded || isHovered.value);

const menuItems = [
  { name: 'Tổng quan', path: '/admin/dashboard', icon: Squares2X2Icon },
  // { name: 'Cấu hình Trang chủ', path: '/admin/homepage-config', icon: ComputerDesktopIcon },
  { name: 'Sản phẩm', path: '/admin/apps', icon: CubeIcon },
  //{ name: 'Kiểm duyệt', path: '/admin/moderation', icon: HandThumbUpIcon },
  { name: 'Lĩnh vực', path: '/admin/domains', icon: FolderIcon },
  { name: 'Công nghệ', path: '/admin/technologies', icon: CpuChipIcon },
  { name: 'Chức năng', path: '/admin/features', icon: PuzzlePieceIcon },
  //{ name: 'Thư viện Media', path: '/admin/media-library', icon: PhotoIcon },
  //{ name: 'Nhật ký hoạt động', path: '/admin/audit-logs', icon: ClipboardDocumentListIcon },
  //{ name: 'Thùng rác', path: '/admin/trash', icon: TrashIcon },
  //{ name: 'Webhooks', path: '/admin/webhooks', icon: BoltIcon },
  //{ name: 'Đăng ký nhận tin', path: '/admin/subscriptions', icon: EnvelopeIcon },
  //{ name: 'Danh sách IP', path: '/admin/ip-whitelist', icon: ShieldCheckIcon },
  //{ name: 'Phân tích tìm kiếm', path: '/admin/analytics/search', icon: MagnifyingGlassIcon },
];
const isActive = (path: string) => route.path.startsWith(path);
</script>

<style scoped>
.custom-scrollbar::-webkit-scrollbar {
  width: 0px;
  background: transparent;
}
</style>