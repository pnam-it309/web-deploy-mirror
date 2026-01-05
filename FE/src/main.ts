import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router/index'
import { useAuthStore } from './stores/auth'
import websocketPlugin from '@/services/socket/configsocket/websocketPlugin'
import './theme/index.css'
import Toast from 'vue3-toastify'
import 'vue3-toastify/dist/index.css'
import 'boxicons/css/boxicons.min.css'
const initApp = async () => {
  const app = createApp(App)
  const pinia = createPinia()

  app.use(Toast, {
    position: 'top-right',
    autoClose: 3000,
    transition: 'slide',
  })

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
