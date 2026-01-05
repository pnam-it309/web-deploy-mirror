import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useSidebarStore = defineStore('sidebar', () => {
  const isDesktopExpanded = ref(true)
  const isSidebarOpen = ref(false)
  const selected = ref('') // Lưu tên mục đang được chọn (để highlight)
  const page = ref('')     // Lưu nhóm trang đang mở (để expand dropdown)

  function toggleSidebar() {
    isSidebarOpen.value = !isSidebarOpen.value
  }

  function toggleDesktopSidebar() {
    isDesktopExpanded.value = !isDesktopExpanded.value
  }

  return { isSidebarOpen, toggleSidebar, selected, page, isDesktopExpanded, toggleDesktopSidebar }
})