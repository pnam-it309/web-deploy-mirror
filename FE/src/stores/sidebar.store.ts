import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useSidebarStore = defineStore('sidebar', () => {
  const isDesktopExpanded = ref(true);

  const isMobileOpen = ref(false);

  const toggleDesktopSidebar = () => {
    isDesktopExpanded.value = !isDesktopExpanded.value;
  };

  const toggleMobileSidebar = () => {
    isMobileOpen.value = !isMobileOpen.value;
  };

  const closeMobileSidebar = () => {
    isMobileOpen.value = false;
  };

  const setExpanded = (value: boolean) => {
    isDesktopExpanded.value = value;
  };

  return {
    isDesktopExpanded,
    isMobileOpen,
    toggleDesktopSidebar,
    toggleMobileSidebar,
    closeMobileSidebar,
    setExpanded
  };
});