import { ref, onMounted, watch } from 'vue'

// Biến toàn cục để trạng thái được đồng bộ giữa các component
const isDark = ref(false)

export function useTheme() {
  
  // Hàm cập nhật class vào thẻ HTML
  const updateHTMLClass = () => {
    if (isDark.value) {
      document.documentElement.classList.add('dark')
      localStorage.setItem('theme', 'dark')
    } else {
      document.documentElement.classList.remove('dark')
      localStorage.setItem('theme', 'light')
    }
  }

  const toggleTheme = () => {
    isDark.value = !isDark.value
    updateHTMLClass()
  }

  onMounted(() => {
    // Kiểm tra localStorage hoặc setting của hệ thống
    const savedTheme = localStorage.getItem('theme')
    
    if (savedTheme === 'dark' || (!savedTheme && window.matchMedia('(prefers-color-scheme: dark)').matches)) {
      isDark.value = true
    } else {
      isDark.value = false
    }
    
    // Apply class ngay khi load
    updateHTMLClass()
  })

  return {
    isDark,
    toggleTheme
  }
}