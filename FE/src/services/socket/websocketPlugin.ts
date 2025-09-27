import { App } from 'vue'
import { websocketService } from '@/services/socket/websocket'

export default {
  install(app: App) {
    app.config.globalProperties.$websocket = websocketService
  },
}
