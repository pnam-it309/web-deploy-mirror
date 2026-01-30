import { defineStore } from 'pinia';
import { ref } from 'vue';

export interface NotificationItem {
  id: string;
  type: 'success' | 'error' | 'info' | 'warning';
  message: string;
  timestamp: number;
  read: boolean;
}

export const useNotificationStore = defineStore('notification', () => {
  const notifications = ref<NotificationItem[]>([]);
  const unreadCount = ref(0);

  const addNotification = (type: NotificationItem['type'], message: string) => {
    const newItem: NotificationItem = {
      id: Date.now().toString() + Math.random().toString(36).substr(2, 9),
      type,
      message,
      timestamp: Date.now(),
      read: false
    };
    notifications.value.unshift(newItem); // Add to top
    unreadCount.value++;
    
    // Limit to 50 notifications
    if (notifications.value.length > 50) {
      notifications.value.pop();
    }
  };

  const markAsRead = (id: string) => {
    const item = notifications.value.find(n => n.id === id);
    if (item && !item.read) {
      item.read = true;
      unreadCount.value = Math.max(0, unreadCount.value - 1);
    }
  };

  const markAllAsRead = () => {
    notifications.value.forEach(n => n.read = true);
    unreadCount.value = 0;
  };
  
  const clearAll = () => {
    notifications.value = [];
    unreadCount.value = 0;
  };

  return {
    notifications,
    unreadCount,
    addNotification,
    markAsRead,
    markAllAsRead,
    clearAll
  };
}, {
  persist: true // Persist to localStorage
});
