import { ref, watchEffect } from 'vue'

export interface BookmarkedProduct {
  id: string
  name: string
  thumbnail: string
  domainName?: string
  addedAt: number
}

const STORAGE_KEY = 'bookmarked-products'

// Load initial bookmarks from localStorage
const loadBookmarks = (): BookmarkedProduct[] => {
  try {
    const stored = localStorage.getItem(STORAGE_KEY)
    return stored ? JSON.parse(stored) : []
  } catch {
    return []
  }
}

const bookmarks = ref<BookmarkedProduct[]>(loadBookmarks())

// Persist to localStorage whenever bookmarks change
watchEffect(() => {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(bookmarks.value))
})

export function useBookmarks() {
  const isBookmarked = (productId: string): boolean => {
    return bookmarks.value.some((b) => b.id === productId)
  }

  const addBookmark = (product: Omit<BookmarkedProduct, 'addedAt'>) => {
    if (!isBookmarked(product.id)) {
      bookmarks.value.push({
        ...product,
        addedAt: Date.now(),
      })
    }
  }

  const removeBookmark = (productId: string) => {
    bookmarks.value = bookmarks.value.filter((b) => b.id !== productId)
  }

  const toggleBookmark = (product: Omit<BookmarkedProduct, 'addedAt'>) => {
    if (isBookmarked(product.id)) {
      removeBookmark(product.id)
    } else {
      addBookmark(product)
    }
  }

  const clearBookmarks = () => {
    bookmarks.value = []
  }

  const getBookmarks = () => bookmarks.value

  const getBookmarkCount = () => bookmarks.value.length

  return {
    bookmarks,
    isBookmarked,
    addBookmark,
    removeBookmark,
    toggleBookmark,
    clearBookmarks,
    getBookmarks,
    getBookmarkCount,
  }
}
