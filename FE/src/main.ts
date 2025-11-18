import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './routes/router'
import { useAuthStore } from './stores/auth'
import websocketPlugin from '@/services/socket/configsocket/websocketPlugin'
import './theme/index.css'

const initApp = async () => {
  const app = createApp(App)
  const pinia = createPinia()

  app.use(pinia)
  app.use(websocketPlugin)

  // Initialize auth store BEFORE setting up router
  console.log('Initializing authentication state...')
  const authStore = useAuthStore()
  await authStore.initializeAuth()
  console.log('Authentication state initialized:', {
    isAuthenticated: authStore.isAuthenticated,
    hasUser: !!authStore.user,
    userRole: authStore.userRole
  })

  // Set up router after auth is initialized
  app.use(router)

  app.mount('#app')
}

// Initialize the app
initApp().catch((error) => {
  console.error('Failed to initialize app:', error)
})
