/**
 * Global Injection Keys
 * Used for Vue's Provide/Inject mechanism to ensure Type Safety.
 * 
 * Example:
 * provide(SOCKET_SERVICE_KEY, websocketService)
 * const socket = inject(SOCKET_SERVICE_KEY)
 */

import type { InjectionKey } from 'vue';

// Define explicit types for injected services/values if needed
// import { WebSocketService } from '@/services/socket/configsocket/websocket';

// Generic Key Example
// export const SOCKET_SERVICE_KEY: InjectionKey<WebSocketService> = Symbol('SOCKET_SERVICE');

/**
 * Key Constants
 * General purpose keys for events, emitters, or specific logic identification.
 */
export const KEYS = {
  // UI Keys
  SIDEBAR_COLLAPSED: 'sidebar-collapsed',
  THEME_MODE: 'theme-mode',
  
  // Event Bus Keys
  EVENT_REFRESH_DATA: 'event:refresh-data',
  EVENT_SHOW_NOTIFICATION: 'event:show-notification',
  
  // Feature Specific
  CART_UPDATE: 'cart:update-count',
} as const;
