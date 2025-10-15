<template>
  <div class="p-6 text-gray-900">
    <!-- Header -->
    <div class="flex justify-between items-center mb-6">
      <h2 class="text-2xl font-semibold">Quản lý sản phẩm</h2>
      <div class="flex items-center space-x-3">
        <button
          @click="openCreateModal"
          class="px-4 py-2 bg-pink-600 hover:bg-pink-700 text-white rounded-md shadow"
        >
          + Thêm sản phẩm
        </button>
      </div>
    </div>

    <!-- Table -->
    <div class="bg-white rounded-lg shadow border border-gray-200 overflow-x-auto">
      <table class="min-w-[1200px] divide-y divide-gray-200">
        <thead class="bg-gray-100">
          <tr>
            <th class="px-6 py-3 text-left text-sm font-medium text-gray-700 sticky left-0 z-20 bg-gray-100">STT</th>
            <th class="px-6 py-3 text-left text-sm font-medium text-gray-700">Tên sản phẩm</th>
            <th class="px-6 py-3 text-left text-sm font-medium text-gray-700">SKU</th>
            <th class="px-6 py-3 text-left text-sm font-medium text-gray-700">Slug</th>
            <th class="px-6 py-3 text-left text-sm font-medium text-gray-700">Giá</th>
            <th class="px-6 py-3 text-left text-sm font-medium text-gray-700">Tồn kho</th>
            <th class="px-6 py-3 text-left text-sm font-medium text-gray-700">Thương hiệu</th>
            <th class="px-6 py-3 text-left text-sm font-medium text-gray-700">Danh mục</th>
            <th class="px-6 py-3 text-center text-sm font-medium text-gray-700 sticky right-0 z-20 bg-gray-100">Hành động</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-gray-100 bg-white">
          <tr
            v-for="(p, index) in products"
            :key="p.id"
            class="hover:bg-gray-50 transition-colors"
          >
            <td class="px-6 py-3 sticky left-0 z-10 bg-white">{{ index + 1 }}</td>
            <td class="px-6 py-3 font-medium">{{ p.name }}</td>
            <td class="px-6 py-3">{{ p.sku }}</td>
            <td class="px-6 py-3">{{ p.slug }}</td>
            <td class="px-6 py-3">{{ formatPrice(p.price) }}</td>
            <td class="px-6 py-3">{{ p.stockQuantity }}</td>
            <td class="px-6 py-3">{{ p.brand?.name || '—' }}</td>
            <td class="px-6 py-3">{{ p.category?.name || '—' }}</td>
            <td class="px-6 py-3 text-center space-x-2 sticky right-0 z-10 bg-white">
              <button
                @click="viewProduct(p)"
                class="px-3 py-1 bg-gray-500 hover:bg-gray-600 text-white rounded"
              >
                Xem
              </button>
              <button
                @click="editProduct(p)"
                class="px-3 py-1 bg-blue-500 hover:bg-blue-600 text-white rounded"
              >
                Sửa
              </button>
              <button
                @click="confirmDelete(p.id)"
                class="px-3 py-1 bg-red-500 hover:bg-red-600 text-white rounded"
              >
                Xóa
              </button>
            </td>
          </tr>
          <tr v-if="!products.length">
            <td colspan="9" class="text-center py-6 text-gray-500 italic">
              Chưa có sản phẩm nào
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Modal -->
    <ProductCreateModal
      v-if="showModal"
      :edit-item="editingProduct"
      @close="closeModal"
      @saved="handleSaved"
    />

    <!-- Toast -->
    <div
      v-if="toast.show"
      :class="[
        'fixed top-6 right-6 px-4 py-2 rounded shadow-lg text-white',
        toast.type === 'success' ? 'bg-green-600' : 'bg-red-600'
      ]"
    >
      {{ toast.message }}
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import ProductCreateModal from './ProductCreateModal.vue'

const products = ref([
  {
    id: 1,
    sku: 'IP15-001',
    name: 'iPhone 15 Pro Max',
    slug: 'iphone-15-pro-max',
    shortDescription: 'Siêu phẩm iPhone mới nhất',
    price: 34990000,
    stockQuantity: 50,
    brand: { id: 1, name: 'Apple' },
    category: { id: 1, name: 'Điện thoại' },
    status: 'ACTIVE',
  },
  {
    id: 2,
    sku: 'SS24-001',
    name: 'Samsung Galaxy S24 Ultra',
    slug: 'samsung-galaxy-s24-ultra',
    shortDescription: 'Flagship mạnh mẽ của Samsung',
    price: 31990000,
    stockQuantity: 30,
    brand: { id: 2, name: 'Samsung' },
    category: { id: 1, name: 'Điện thoại' },
    status: 'ACTIVE',
  },
])

const showModal = ref(false)
const editingProduct = ref<any | null>(null)
const toast = ref({ show: false, message: '', type: 'success' as 'success' | 'error' })
let toastTimer: any = null

const showToast = (message: string, type: 'success' | 'error' = 'success', duration = 2200) => {
  toast.value = { show: true, message, type }
  clearTimeout(toastTimer)
  toastTimer = setTimeout(() => (toast.value.show = false), duration)
}

const formatPrice = (price: number) =>
  price.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' })

const openCreateModal = () => {
  editingProduct.value = null
  showModal.value = true
}

const editProduct = (p: any) => {
  editingProduct.value = { ...p }
  showModal.value = true
}

const viewProduct = (p: any) => {
  alert(
    `📦 Chi tiết sản phẩm:\n\nTên: ${p.name}\nSKU: ${p.sku}\nGiá: ${formatPrice(p.price)}\nTồn kho: ${p.stockQuantity}\nMô tả: ${p.shortDescription}`
  )
}

const closeModal = () => {
  showModal.value = false
  editingProduct.value = null
}

const handleSaved = (payload: any) => {
  if (!payload) return

  if (payload.id) {
    const idx = products.value.findIndex((p) => p.id === payload.id)
    if (idx !== -1) {
      products.value[idx] = { ...products.value[idx], ...payload }
      showToast('✏️ Cập nhật sản phẩm thành công', 'success')
    } else {
      const newId = Math.max(...products.value.map((p) => p.id)) + 1
      products.value.push({ id: newId, ...payload })
      showToast('🎉 Thêm sản phẩm thành công', 'success')
    }
  } else {
    const newId = (products.value.length ? Math.max(...products.value.map((p) => p.id)) : 0) + 1
    products.value.push({ id: newId, ...payload })
    showToast('🎉 Thêm sản phẩm thành công', 'success')
  }

  showModal.value = false
  editingProduct.value = null
}

const confirmDelete = (id: number) => {
  if (!confirm('Bạn có chắc muốn xóa sản phẩm này?')) return
  products.value = products.value.filter((p) => p.id !== id)
  showToast('🗑️ Xóa sản phẩm thành công', 'success')
}
</script>

<style scoped>
table th,
table td {
  @apply text-sm;
}
</style>
