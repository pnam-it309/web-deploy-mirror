import { ref, computed } from 'vue'

export interface CompareProduct {
  id: string
  name: string
  thumbnail?: string
  domainName?: string
  description?: string
  technologies?: { name: string; icon?: string }[]
  features?: { name: string; description?: string }[]
  demoUrl?: string
  sourceUrl?: string
}

const MAX_COMPARE_ITEMS = 3
const compareList = ref<CompareProduct[]>([])

export function useCompare() {
  const isInCompare = (productId: string) => {
    return compareList.value.some((p) => p.id === productId)
  }

  const addToCompare = (product: CompareProduct) => {
    if (compareList.value.length >= MAX_COMPARE_ITEMS) {
      return { success: false, message: `Chỉ có thể so sánh tối đa ${MAX_COMPARE_ITEMS} sản phẩm` }
    }
    if (isInCompare(product.id)) {
      return { success: false, message: 'Sản phẩm đã có trong danh sách so sánh' }
    }
    compareList.value.push(product)
    return { success: true, message: 'Đã thêm vào danh sách so sánh' }
  }

  const removeFromCompare = (productId: string) => {
    compareList.value = compareList.value.filter((p) => p.id !== productId)
  }

  const clearCompare = () => {
    compareList.value = []
  }

  const compareCount = computed(() => compareList.value.length)

  const canAddMore = computed(() => compareList.value.length < MAX_COMPARE_ITEMS)

  return {
    compareList,
    compareCount,
    canAddMore,
    isInCompare,
    addToCompare,
    removeFromCompare,
    clearCompare,
    MAX_COMPARE_ITEMS,
  }
}
