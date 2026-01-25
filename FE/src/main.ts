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
import * as Sentry from '@sentry/vue'

const initApp = async () => {
  const app = createApp(App)
  const pinia = createPinia()

  // Initialize Sentry for error monitoring
  if (import.meta.env.VITE_SENTRY_DSN) {
    Sentry.init({
      app,
      dsn: import.meta.env.VITE_SENTRY_DSN,
      integrations: [
        new Sentry.BrowserTracing({
          routingInstrumentation: Sentry.vueRouterInstrumentation(router),
        }),
        new Sentry.Replay(),
      ],
      tracesSampleRate: 0.1,
      replaysSessionSampleRate: 0.1,
      replaysOnErrorSampleRate: 1.0,
      environment: import.meta.env.MODE || 'development',
    })
  }

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
  // Send to Sentry if configured
  if (import.meta.env.VITE_SENTRY_DSN) {
    Sentry.captureException(error)
  }
})
