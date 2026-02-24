<template>
  <!-- Desktop spacer -->
  <div class="hidden lg:block h-full flex-shrink-0 transition-[width] duration-300 ease-[cubic-bezier(0.4,0,0.2,1)]"
    :class="[sidebarStore.isDesktopExpanded ? 'w-72' : 'w-20']"></div>

  <!-- Mobile Overlay -->
  <div v-if="sidebarStore.isMobileOpen"
    @click="sidebarStore.closeMobileSidebar"
    class="fixed inset-0 bg-black/60 z-40 lg:hidden backdrop-blur-sm transition-opacity duration-300">
  </div>

  <aside
    class="sidebar-shell fixed inset-y-0 left-0 z-50 transform transition-all duration-300 ease-[cubic-bezier(0.4,0,0.2,1)] flex flex-col h-full overflow-hidden lg:translate-x-0"
    :class="[
      isExpanded ? 'w-72' : 'w-20',
      sidebarStore.isMobileOpen ? 'translate-x-0' : '-translate-x-full',
      themeStore.theme === 'dark' ? 'sidebar-dark' : 'sidebar-light'
    ]"
    @mouseenter="isHovered = true"
    @mouseleave="isHovered = false">

    <!-- ── Animated Background Blobs ── -->
    <div class="sidebar-blob sidebar-blob-1"></div>
    <div class="sidebar-blob sidebar-blob-2"></div>

    <!-- ── Logo ── -->
    <div class="sidebar-logo relative z-10 h-16 flex items-center justify-between px-5 flex-shrink-0">
      <div class="flex items-center gap-3 transition-all duration-300 whitespace-nowrap overflow-hidden"
        :class="[isExpanded ? 'w-full opacity-100' : 'w-0 opacity-0']">
        <div class="logo-icon w-9 h-9 rounded-xl flex items-center justify-center text-white shadow-lg flex-shrink-0">
          <span class="font-serif italic text-lg font-bold">U</span>
        </div>
        <span class="logo-text font-serif tracking-tight text-xl font-bold">
          UDPM <span class="highlight">Admin</span>
        </span>
      </div>

      <div v-if="!isExpanded" class="w-full flex justify-center">
        <div class="logo-icon w-9 h-9 rounded-xl flex items-center justify-center text-white shadow-lg">
          <span class="font-serif italic text-lg font-bold">U</span>
        </div>
      </div>

      <button v-if="isExpanded" @click="sidebarStore.toggleDesktopSidebar"
        class="collapse-btn p-1.5 rounded-lg transition-all absolute right-4 z-20">
        <ChevronDoubleLeftIcon class="w-4 h-4" />
      </button>
    </div>

    <!-- ── Navigation ── -->
    <div class="flex-1 overflow-y-auto py-4 px-3 custom-scrollbar relative z-10 flex flex-col gap-0.5">
      <template v-for="group in menuGroups" :key="group.label">
        <div v-if="isExpanded" class="px-3 pt-4 pb-1.5 first:pt-1">
          <span class="group-label text-[9px] font-black uppercase tracking-[0.15em] select-none">
            {{ group.label }}
          </span>
        </div>
        <div v-else class="divider my-2 mx-3 h-px"></div>

        <router-link
          v-for="item in group.items"
          :key="item.path"
          :to="item.path"
          class="sidebar-item group relative flex items-center h-11 w-full rounded-xl transition-all duration-200 overflow-hidden"
          :class="isActive(item.path) ? 'active' : ''">

          <div class="active-bar absolute left-0 inset-y-1 w-[3.5px] rounded-full transition-all duration-300"
            :class="isActive(item.path) ? 'opacity-100' : 'opacity-0'"></div>

          <div class="flex-shrink-0 w-11 h-11 flex items-center justify-center">
            <component :is="item.icon"
              class="nav-icon w-5 h-5 transition-all duration-300 group-hover:scale-110" />
          </div>

          <span class="nav-label whitespace-nowrap transition-all duration-300 font-semibold text-sm"
            :class="[
              isExpanded ? 'opacity-100 translate-x-0' : 'opacity-0 -translate-x-3 pointer-events-none'
            ]">
            {{ item.name }}
          </span>

          <div v-if="!isExpanded"
            class="sidebar-tooltip absolute left-full ml-3 px-2.5 py-1.5 bg-gray-900 dark:bg-indigo-950 text-white text-xs font-semibold rounded-lg whitespace-nowrap opacity-0 group-hover:opacity-100 pointer-events-none transition-all duration-200 shadow-xl z-50 border dark:border-indigo-500/30">
            {{ item.name }}
            <div class="absolute right-full top-1/2 -translate-y-1/2 border-4 border-transparent border-r-gray-900 dark:border-r-indigo-950"></div>
          </div>
        </router-link>
      </template>
    </div>

    <!-- ── Footer ── -->
    <div class="sidebar-footer relative z-10 p-3 flex-shrink-0">
      <div class="user-pill flex items-center gap-3 p-3 rounded-xl border overflow-hidden"
        :class="isExpanded ? '' : 'justify-center'">
        <div class="w-7 h-7 rounded-full bg-gradient-to-tr from-violet-500 to-indigo-500 flex-shrink-0 flex items-center justify-center text-white text-xs font-bold shadow-md">
          A
        </div>
        <div v-if="isExpanded" class="flex-1 min-w-0">
          <p class="user-name text-xs font-semibold truncate">Administrator</p>
          <p class="user-role text-[10px] truncate">UDPM System</p>
        </div>
      </div>
    </div>
  </aside>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue';
import { useRoute } from 'vue-router';
import { useSidebarStore } from '@/stores/sidebar.store';
import { useThemeStore } from '@/stores/theme.store';
import {
  Squares2X2Icon,
  FolderIcon,
  CubeIcon,
  CpuChipIcon,
  PuzzlePieceIcon,
  ChevronDoubleLeftIcon,
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
const themeStore = useThemeStore();
const isHovered = ref(false);

const isExpanded = computed(() => sidebarStore.isDesktopExpanded || isHovered.value);

const menuGroups = [
  {
    label: 'Tổng quan',
    items: [
      { name: 'Dashboard', path: '/admin/dashboard', icon: Squares2X2Icon },
    ]
  },
  {
    label: 'Nội dung',
    items: [
      { name: 'Sản phẩm', path: '/admin/apps', icon: CubeIcon },
      //{ name: 'Kiểm duyệt', path: '/admin/moderation', icon: HandThumbUpIcon },
      { name: 'Lĩnh vực', path: '/admin/domains', icon: FolderIcon },
      { name: 'Công nghệ', path: '/admin/technologies', icon: CpuChipIcon },
      { name: 'Chức năng', path: '/admin/features', icon: PuzzlePieceIcon },
      //{ name: 'Thư viện Media', path: '/admin/media-library', icon: PhotoIcon },
    ]
  },
  // {
  //   label: 'Bảo mật & Hệ thống',
  //   items: [
  //     { name: 'Nhật ký hoạt động', path: '/admin/audit-logs', icon: ClipboardDocumentListIcon },
  //     { name: 'Phân tích tìm kiếm', path: '/admin/analytics/search', icon: MagnifyingGlassIcon },
  //     { name: 'Đăng ký nhận tin', path: '/admin/subscriptions', icon: EnvelopeIcon },
  //     { name: 'Webhooks', path: '/admin/webhooks', icon: BoltIcon },
  //     { name: 'Danh sách IP', path: '/admin/ip-whitelist', icon: ShieldCheckIcon },
  //     { name: 'Thùng rác', path: '/admin/trash', icon: TrashIcon },
  //   ]
  // }
];

const isActive = (path: string) => route.path.startsWith(path);
</script>

<style scoped>
/* ── Sidebar Shell ── */
.sidebar-shell {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  backdrop-filter: blur(20px);
}

.sidebar-light {
  background: white;
  border-right: 1px solid rgba(0, 0, 0, 0.08);
  box-shadow: 10px 0 30px rgba(0, 0, 0, 0.02);
}

.sidebar-dark {
  background: linear-gradient(
    170deg,
    rgba(10, 10, 25, 1) 0%,
    rgba(15, 12, 35, 1) 50%,
    rgba(8, 8, 20, 1) 100%
  );
  border-right: 1px solid rgba(124, 58, 237, 0.2);
  box-shadow: 4px 0 40px rgba(0, 0, 0, 0.5);
}

/* ── Logo ── */
.sidebar-light .sidebar-logo { border-bottom: 1px solid rgba(0, 0, 0, 0.05); }
.sidebar-dark .sidebar-logo { border-bottom: 1px solid rgba(124, 58, 237, 0.15); background: rgba(124, 58, 237, 0.03); }

.logo-icon { background: linear-gradient(135deg, #7c3aed, #4f46e5); }
.sidebar-light .logo-text { color: #111827; }
.sidebar-dark .logo-text { color: white; text-shadow: 0 0 15px rgba(255, 255, 255, 0.1); }

.sidebar-light .highlight { color: #7c3aed; }
.sidebar-dark .highlight { color: #c084fc; }

.sidebar-light .collapse-btn { color: #6b7280; }
.sidebar-dark .collapse-btn { color: #a78bfa; }
.sidebar-light .collapse-btn:hover { background: #f3f4f6; }
.sidebar-dark .collapse-btn:hover { background: rgba(124, 58, 237, 0.1); color: white; }

/* ── Group & Labels ── */
.sidebar-light .group-label { color: #9ca3af; }
.sidebar-dark .group-label { color: #6b6b8b; }

.sidebar-light .divider { background: #f3f4f6; }
.sidebar-dark .divider { background: rgba(124, 58, 237, 0.1); }

/* ── Menu Items ── */
.sidebar-light .sidebar-item { color: #4b5563; }
.sidebar-dark .sidebar-item { color: #94a3b8; }

.sidebar-light .sidebar-item:not(.active):hover { background: #f9fafb; color: #111827; }
.sidebar-dark .sidebar-item:not(.active):hover { background: rgba(124, 58, 237, 0.08); color: #e2e8f0; }

/* ── Active State ── */
.sidebar-light .sidebar-item.active { background: #f5f3ff; }
.sidebar-dark .sidebar-item.active { 
  background: linear-gradient(125deg, rgba(124, 58, 237, 0.5) 0%, rgba(79, 70, 229, 0.4) 50%, rgba(37, 99, 235, 0.3) 100%);
  box-shadow: inset 0 0 15px rgba(124, 58, 237, 0.1), 0 4px 15px rgba(0,0,0,0.2);
}

.sidebar-light .sidebar-item.active .nav-label { color: #7c3aed; font-weight: 700; }
.sidebar-dark .sidebar-item.active .nav-label { color: white; text-shadow: 0 0 8px rgba(124, 58, 237, 0.5); }

.sidebar-light .nav-icon { color: #9ca3af; }
.sidebar-dark .nav-icon { color: #64748b; }

.sidebar-light .sidebar-item:hover .nav-icon { color: #7c3aed; }
.sidebar-dark .sidebar-item:hover .nav-icon { color: #c084fc; }

.sidebar-light .sidebar-item.active .nav-icon { color: #7c3aed; }
.sidebar-dark .sidebar-item.active .nav-icon { color: #c084fc; filter: drop-shadow(0 0 5px rgba(124, 58, 237, 0.8)); }

/* ── Active Indicator ── */
.sidebar-light .active-bar { background: #7c3aed; }
.sidebar-dark .active-bar { background: linear-gradient(to bottom, #c084fc, #6366f1); box-shadow: 0 0 15px rgba(167, 139, 250, 0.8); }

/* ── Footer ── */
.sidebar-light .sidebar-footer { border-top: 1px solid rgba(0, 0, 0, 0.05); }
.sidebar-dark .sidebar-footer { border-top: 1px solid rgba(124, 58, 237, 0.15); }

.sidebar-light .user-pill { background: #f9fafb; border-color: #e5e7eb; }
.sidebar-dark .user-pill { background: rgba(124, 58, 237, 0.05); border-color: rgba(124, 58, 237, 0.2); }

.sidebar-light .user-name { color: #374151; }
.sidebar-dark .user-name { color: #e2e8f0; }

.sidebar-light .user-role { color: #9ca3af; }
.sidebar-dark .user-role { color: #64748b; }

/* ── Blobs ── */
.sidebar-blob { position: absolute; border-radius: 50%; filter: blur(80px); pointer-events: none; z-index: 0; }
.sidebar-light .sidebar-blob { opacity: 0.05; }
.sidebar-dark .sidebar-blob { opacity: 0.6; }

.sidebar-blob-1 { width: 250px; height: 250px; background: radial-gradient(circle, rgba(124, 58, 237, 0.3) 0%, transparent 70%); top: -80px; left: -100px; }
.sidebar-blob-2 { width: 200px; height: 200px; background: radial-gradient(circle, rgba(79, 70, 229, 0.2) 0%, transparent 70%); bottom: 100px; right: -80px; }

/* ── Scrollbar ── */
.custom-scrollbar::-webkit-scrollbar { width: 3px; }
.custom-scrollbar::-webkit-scrollbar-thumb { background: rgba(0, 0, 0, 0.1); border-radius: 999px; }
.sidebar-dark .custom-scrollbar::-webkit-scrollbar-thumb { background: rgba(124, 58, 237, 0.3); }
</style>