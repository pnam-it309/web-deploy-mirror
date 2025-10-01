import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './app/router'
import { useAuthStore } from './stores/auth'
import websocketPlugin from '@/services/socket/websocketPlugin'
import './theme/index.css'

const initApp = async () => {
  const app = createApp(App)
  const pinia = createPinia()
  
  app.use(pinia)
  app.use(router)
  app.use(websocketPlugin)
  
  // Initialize auth store
  const authStore = useAuthStore()
  await authStore.initializeAuth()
  
  app.mount('#app')
}

// Initialize the app
initApp().catch((error) => {
  console.error('Failed to initialize app:', error)
})
