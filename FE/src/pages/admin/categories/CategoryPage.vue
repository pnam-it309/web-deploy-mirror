<template>
  <div class="p-6 text-gray-900">
    <!-- Header -->
    <div class="flex justify-between items-center mb-6">
      <h2 class="text-2xl font-semibold">Quản lý Danh mục sản phẩm</h2>
      <div class="flex items-center space-x-3">
        <button
          @click="openCreateModal"
          class="px-4 py-2 bg-pink-600 hover:bg-pink-700 text-white rounded-md shadow"
        >
          + Thêm danh mục
        </button>
      </div>
    </div>

    <!-- Table -->
    <div class="bg-white rounded-lg shadow border border-gray-200 overflow-hidden">
      <table class="min-w-full divide-y divide-gray-200">
        <thead class="bg-gray-100">
          <tr>
            <th class="px-6 py-3 text-left text-sm font-medium text-gray-700">#</th>
            <th class="px-6 py-3 text-left text-sm font-medium text-gray-700">Tên danh mục</th>
            <th class="px-6 py-3 text-left text-sm font-medium text-gray-700">Slug</th>
            <th class="px-6 py-3 text-left text-sm font-medium text-gray-700">Mô tả</th>
            <th class="px-6 py-3 text-left text-sm font-medium text-gray-700">Danh mục cha</th>
            <th class="px-6 py-3 text-center text-sm font-medium text-gray-700">Hành động</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-gray-100 bg-white">
          <tr
            v-for="(cat, index) in categories"
            :key="cat.id"
            class="hover:bg-gray-50 transition-colors"
          >
            <td class="px-6 py-3">{{ index + 1 }}</td>
            <td class="px-6 py-3 font-medium">{{ cat.name }}</td>
            <td class="px-6 py-3">{{ cat.slug }}</td>
            <td class="px-6 py-3">{{ cat.description || '—' }}</td>
            <td class="px-6 py-3">
              {{ getParentName(cat.parentId) }}
            </td>
            <td class="px-6 py-3 text-center space-x-2">
              <button
                @click="editCategory(cat)"
                class="px-3 py-1 bg-blue-500 hover:bg-blue-600 text-white rounded"
              >
                Sửa
              </button>
              <button
                @click="confirmDelete(cat.id)"
                class="px-3 py-1 bg-red-500 hover:bg-red-600 text-white rounded"
              >
                Xóa
              </button>
            </td>
          </tr>
          <tr v-if="!categories.length">
            <td colspan="6" class="text-center py-6 text-gray-500 italic">
              Chưa có danh mục nào
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Modal -->
    <CategoryCreateModal
      v-if="showModal"
      :categories="categories"
      :edit-item="editingCategory"
      @close="closeModal"
      @saved="handleSaved"
    />

    <!-- Toast (simple) -->
    <div v-if="toast.show" :class="['fixed top-6 right-6 px-4 py-2 rounded shadow-lg text-white', toast.type === 'success' ? 'bg-green-600' : 'bg-red-600']">
      {{ toast.message }}
    </div>

    <!-- Confirm delete dialog (browser confirm used) -->
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import CategoryCreateModal from './CategoryCreateModal.vue'

/**
 * NOTE:
 * - categories: mỗi item có cấu trúc { id, name, slug, description, parentId }
 * - parentId có thể null nếu là root (danh mục cha).
 *
 * Logic:
 * - Tạo mới: parentId lấy từ modal; id do parent gán (maxId + 1).
 * - Sửa: modal gửi object có id; cập nhật item tương ứng.
 * - Xóa: xóa cả subtree (con cháu) để phù hợp cascade.
 */

const categories = ref([
  // ví dụ mock dữ liệu
  { id: 1, name: 'Điện thoại', slug: 'dien-thoai', description: 'Điện thoại thông minh', parentId: null },
  { id: 2, name: 'Phụ kiện', slug: 'phu-kien', description: 'Phụ kiện điện thoại', parentId: 1 },
  { id: 3, name: 'Laptop', slug: 'laptop', description: 'Máy tính xách tay', parentId: null },
])

const showModal = ref(false)
const editingCategory = ref(null)

const toast = ref({ show: false, message: '', type: 'success' as 'success' | 'error' })
let toastTimer: any = null
const showToast = (message: string, type: 'success' | 'error' = 'success', duration = 2200) => {
  toast.value = { show: true, message, type }
  clearTimeout(toastTimer)
  toastTimer = setTimeout(() => (toast.value.show = false), duration)
}

const openCreateModal = () => {
  editingCategory.value = null
  showModal.value = true
}

const editCategory = (cat: any) => {
  // truyền object copy để modal không mutate trực tiếp
  editingCategory.value = { ...cat }
  showModal.value = true
}

const closeModal = () => {
  showModal.value = false
  editingCategory.value = null
}

const handleSaved = (payload: any) => {
  // payload: { id?, name, slug, description, parentId }
  if (!payload) return

  // update case: id exists and matches
  if (payload.id) {
    const idx = categories.value.findIndex((c) => c.id === payload.id)
    if (idx !== -1) {
      // update fields
      categories.value[idx] = {
        ...categories.value[idx],
        name: payload.name,
        slug: payload.slug,
        description: payload.description,
        parentId: payload.parentId ?? null,
      }
      showToast('✏️ Cập nhật danh mục thành công', 'success')
    } else {
      // if id provided but not found, treat as new
      const newId = (categories.value.length ? Math.max(...categories.value.map(c => c.id)) : 0) + 1
      categories.value.push({ id: newId, ...payload, parentId: payload.parentId ?? null })
      showToast('🎉 Thêm danh mục thành công', 'success')
    }
  } else {
    // create: assign auto-increment id
    const newId = (categories.value.length ? Math.max(...categories.value.map(c => c.id)) : 0) + 1
    categories.value.push({ id: newId, ...payload, parentId: payload.parentId ?? null })
    showToast('🎉 Thêm danh mục thành công', 'success')
  }

  // close modal
  showModal.value = false
  editingCategory.value = null
}

/**
 * Xóa 1 category và tất cả descendants (con cháu),
 * tương ứng với cascade = ALL + orphanRemoval = true trên entity.
 */
const confirmDelete = (id: number) => {
  // tìm descendants
  const toDelete = getSubtreeIds(id)
  const msg = toDelete.length > 1
    ? `Danh mục này có ${toDelete.length - 1} danh mục con. Xóa sẽ xoá toàn bộ cây. Bạn có chắc?`
    : 'Bạn có chắc muốn xóa danh mục này?'
  if (!confirm(msg)) return

  categories.value = categories.value.filter(c => !toDelete.includes(c.id))
  showToast('🗑️ Xóa danh mục thành công', 'success')
}

/**
 * Trả về mảng id của node + tất cả descendants
 */
const getSubtreeIds = (rootId: number) => {
  const result: number[] = []
  const map = new Map<number, any[]>()
  for (const c of categories.value) {
    const pid = c.parentId ?? null
    if (!map.has(pid)) map.set(pid, [])
    map.get(pid)!.push(c)
  }
  const stack = [rootId]
  while (stack.length) {
    const id = stack.pop()!
    result.push(id)
    const children = map.get(id) || []
    for (const ch of children) stack.push(ch.id)
  }
  return result
}

/**
 * Lấy tên parent hiển thị (hoặc "—")
 */
const getParentName = (parentId: number | null) => {
  if (!parentId && parentId !== 0) return '—'
  const p = categories.value.find(c => c.id === parentId)
  return p ? p.name : '—'
}
</script>

<style scoped>
table th,
table td {
  @apply text-sm;
}
</style>
