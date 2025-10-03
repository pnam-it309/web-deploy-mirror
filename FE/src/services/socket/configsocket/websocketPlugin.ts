import { App } from "vue";
import { websocketService } from "@/services/socket/configsocket/websocket.ts";

export default {
  install(app: App) {
    app.config.globalProperties.$websocket = websocketService;
  },
};
