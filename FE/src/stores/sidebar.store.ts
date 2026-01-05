import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useSidebarStore = defineStore('sidebar', () => {
  const isDesktopExpanded = ref(true);

  const toggleDesktopSidebar = () => {
    isDesktopExpanded.value = !isDesktopExpanded.value;
  };

  const setExpanded = (value: boolean) => {
    isDesktopExpanded.value = value;
  };

  return {
    isDesktopExpanded,
    toggleDesktopSidebar,
    setExpanded
  };
});