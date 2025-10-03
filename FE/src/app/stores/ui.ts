import { defineStore } from 'pinia'

export const useUIStore = defineStore('ui', {
  state: () => ({
    isQuoteOpen: false as boolean,
    selectedSKU: '' as string,
  }),
  actions: {
    openQuote(sku?: string) {
      if (sku) this.selectedSKU = sku
      this.isQuoteOpen = true
    },
    closeQuote() {
      this.isQuoteOpen = false
    }
  }
})
