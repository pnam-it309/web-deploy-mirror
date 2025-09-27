import { defineStore } from 'pinia'

export interface ToastItem {
  id: number
  message: string
  type?: 'success' | 'error' | 'info'
  timeout?: number
}

export const useNotifyStore = defineStore('notify', {
  state: () => ({
    items: [] as ToastItem[],
    _id: 1
  }),
  actions: {
    push(message: string, type: ToastItem['type'] = 'info', timeout = 2500) {
      const id = this._id++
      this.items.push({ id, message, type, timeout })
      setTimeout(() => this.remove(id), timeout)
    },
    success(message: string, timeout = 2500) { this.push(message, 'success', timeout) },
    error(message: string, timeout = 3000) { this.push(message, 'error', timeout) },
    remove(id: number) {
      this.items = this.items.filter(t => t.id !== id)
    }
  }
})
