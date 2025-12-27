import { defineStore } from 'pinia';

export type NotificationType = 'success' | 'error' | 'warning' | 'info';

export interface NotificationItem {
  id: string;
  type: NotificationType;
  message: string;
  title?: string;
  duration?: number;
}

export const useNotifyStore = defineStore('notify', {
  state: () => ({
    notifications: [] as NotificationItem[],
  }),
  actions: {
    add(payload: Omit<NotificationItem, 'id'> & { id?: string }) {
      const id = payload.id || Date.now().toString();
      const item: NotificationItem = {
        id,
        duration: 3000, // default 3s
        ...payload,
      };

      this.notifications.push(item);

      if (item.duration && item.duration > 0) {
        setTimeout(() => {
          this.remove(id);
        }, item.duration);
      }
    },

    remove(id: string) {
      const index = this.notifications.findIndex((n) => n.id === id);
      if (index !== -1) {
        this.notifications.splice(index, 1);
      }
    },

    // Helpers
    success(message: string, title?: string, duration = 3000) {
      this.add({ type: 'success', message, title, duration });
    },
    error(message: string, title?: string, duration = 4000) {
      this.add({ type: 'error', message, title, duration });
    },
    warning(message: string, title?: string, duration = 3500) {
      this.add({ type: 'warning', message, title, duration });
    },
    info(message: string, title?: string, duration = 3000) {
      this.add({ type: 'info', message, title, duration });
    }
  },
});
