import { createApp } from 'vue'
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
import App from './App.vue'
import router from './router/index'
import { useAuthStore } from './stores/auth'
import { useNotificationStore } from './stores/notification.store'
import websocketPlugin from '@/services/socket/configsocket/websocketPlugin'
import './theme/index.css'
import Toast, { toast } from 'vue3-toastify'
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

  pinia.use(piniaPluginPersistedstate)
  app.use(pinia)
  app.use(router)
  app.use(websocketPlugin)

  // Setup Notification Interceptor
  const notificationStore = useNotificationStore(pinia)
  
  const originalSuccess = toast.success;
  const originalError = toast.error;
  const originalInfo = toast.info;
  const originalWarning = toast.warning;

  // Monkey patch toast methods to capture notifications
  // Using Object.assign or direct assignment if allowed
  toast.success = (content: any, options?: any) => {
    notificationStore.addNotification('success', typeof content === 'string' ? content : 'Thao tác thành công');
    return originalSuccess(content, options);
  };
  
  toast.error = (content: any, options?: any) => {
     notificationStore.addNotification('error', typeof content === 'string' ? content : 'Có lỗi xảy ra');
    return originalError(content, options);
  };

  toast.info = (content: any, options?: any) => {
     notificationStore.addNotification('info', typeof content === 'string' ? content : 'Thông báo');
    return originalInfo(content, options);
  };
  
  toast.warning = (content: any, options?: any) => {
     notificationStore.addNotification('warning', typeof content === 'string' ? content : 'Cảnh báo');
    return originalWarning(content, options);
  };

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
