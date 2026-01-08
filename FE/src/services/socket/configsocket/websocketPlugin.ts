import { App } from 'vue'
import { websocketService } from './websocket'

export default {
  install(app: App) {
    app.config.globalProperties.$websocket = websocketService
  },
}
