<template>
  <div class="p-6 relative min-h-screen bg-[#f8f9fa]">
    <!-- FILTER -->
    <div class="mb-5">
      <ProductFilter
        :brands="brandStore.brands"
        :categories="categoryStore.categories"
        @filter="handleFilterChange"
      />
    </div>

    <!-- ERROR ALERT -->
    <div
      v-if="error"
      class="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg relative mb-5 flex items-center shadow-sm"
    >
      <svg
        xmlns="http://www.w3.org/2000/svg"
        class="h-5 w-5 mr-2 text-red-500"
        viewBox="0 0 20 20"
        fill="currentColor"
      >
        <path
          fill-rule="evenodd"
          d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7 4a1 1 0 11-2 0 1 1 0 012 0zm-1-9a1 1 0 00-1 1v4a1 1 0 102 0V6a1 1 0 00-1-1z"
          clip-rule="evenodd"
        />
      </svg>
      <span class="font-bold mr-1">Lỗi!</span> <span class="block sm:inline"> {{ error }}</span>
    </div>
    <ButtonCustom
      color="gray"
      size="default"
      @click="showImportPanel = !showImportPanel"
      class="!py-2"
    >
      <span class="mr-2">📥</span> Import Excel
    </ButtonCustom>
    <ButtonCustom color="primary" size="default" @click="openCreateModal" class="ml-2 !py-2">
      + Thêm sản phẩm
    </ButtonCustom>
    <CardCustom>
      <div v-if="isLoading && !products.length" class="p-12 text-center">
        <div
          class="animate-spin inline-block w-8 h-8 border-4 border-current border-t-transparent text-[#adc178] rounded-full mb-3"
        ></div>
        <p class="text-gray-500 text-sm font-medium">Đang tải dữ liệu...</p>
      </div>
      <table v-else class="w-full text-left border-collapse">
        <thead class="bg-[#f7f9ef]">
          <tr>
            <th class="p-4 border-b border-[#e6dfc0] font-bold text-sm text-[#5a483e]">SKU</th>
            <th class="p-4 border-b border-[#e6dfc0] font-bold text-sm text-[#5a483e]">
              Tên sản phẩm
            </th>
            <th class="p-4 border-b border-[#e6dfc0] font-bold text-sm text-[#5a483e]">
              Thương hiệu
            </th>
            <th class="p-4 border-b border-[#e6dfc0] font-bold text-sm text-[#5a483e]">Danh mục</th>
            <th class="p-4 border-b border-[#e6dfc0] font-bold text-sm text-[#5a483e] text-right">
              Giá
            </th>
            <th class="p-4 border-b border-[#e6dfc0] font-bold text-sm text-[#5a483e] text-center">
              Trạng thái
            </th>
            <th class="p-4 border-b border-[#e6dfc0] font-bold text-sm text-[#5a483e] text-center">
              Hành động
            </th>
          </tr>
        </thead>
        <tbody class="bg-white divide-y divide-[#f0ead2]">
          <tr
            v-for="product in products"
            :key="product.id"
            class="hover:bg-[#f0ead2]/30 transition-colors duration-150 cursor-pointer"
            @click="openDetailModal(product)"
          >
            <td class="p-4 text-sm font-mono text-blue-600 font-medium">
              {{ product.sku }}
            </td>
            <td class="p-4">
              <div class="font-semibold text-[#6c584c] hover:text-[#a98467] transition-colors">
                {{ product.name }}
              </div>
            </td>
            <td class="p-4 text-sm text-gray-600">{{ product.brandName || '—' }}</td>
            <td class="p-4 text-sm text-gray-600">{{ product.categoryName || '—' }}</td>
            <td class="p-4 text-right font-medium text-[#5a483e]">
              {{ product.price ? product.price.toLocaleString() : '0' }} ₫
            </td>
            <td class="p-4 text-center">
              <span
                :class="[
                  'px-2.5 py-1 rounded-full text-xs font-medium border',
                  product.status === 'ACTIVE'
                    ? 'bg-[#dde5b6]/40 text-[#386641] border-[#adc178]/30'
                    : 'bg-red-50 text-red-700 border-red-100',
                ]"
              >
                {{ product.status === 'ACTIVE' ? 'Đang bán' : 'Ngừng bán' }}
              </span>
            </td>
            <td class="p-4 text-center" @click.stop>
              <div class="flex items-center justify-center gap-2">
                <!-- Nút Chi tiết: Màu Info (hoặc Sage Soft) -->
                <ButtonCustom color="default" size="small" @click="openDetailModal(product)">
                  Chi tiết
                </ButtonCustom>
                <!-- Nút Xoá: Màu Đỏ (hoặc Mocha Soft) -->
                <ButtonCustom color="cream" size="small" @click="askDeleteProduct(product)">
                  Xoá
                </ButtonCustom>
              </div>
            </td>
          </tr>

          <!-- Empty State -->
          <tr v-if="!products.length">
            <td colspan="8" class="p-12 text-center bg-[#fffdf5]/50 text-gray-500 italic">
              Chưa có sản phẩm nào.
            </td>
          </tr>
        </tbody>
      </table>
    </CardCustom>

    <!-- Modals -->
    <ProductCreateModal
      v-if="showCreateModal"
      :loading="isLoading"
      @close="closeModal"
      @saved="handleCreate"
    />

    <ProductDetailModal
      v-if="showDetailModal && selectedProductId"
      :productId="selectedProductId"
      :loading="isLoading"
      @close="closeDetailModal"
      @saved="handleUpdate"
    />

    <!-- Confirm Delete Modal -->
    <ModalCustom :show="showDeleteConfirm" @close="cancelDelete" size="sm">
      <template #title>Xác nhận xoá</template>
      <template #default>
        <p class="text-gray-700">
          Bạn có chắc chắn muốn xoá sản phẩm
          <strong class="text-[#6c584c]">{{ productToDelete?.name }}</strong
          >?
        </p>
        <p class="text-sm text-red-500 mt-2 bg-red-50 p-2 rounded border border-red-100">
          ⚠️ Hành động này sẽ chuyển trạng thái sang <strong>Ngừng bán</strong>.
        </p>
      </template>
      <template #footer>
        <ButtonCustom color="secondary" @click="cancelDelete">Huỷ</ButtonCustom>
        <ButtonCustom color="danger" :loading="isLoading" @click="confirmDelete">Xoá</ButtonCustom>
      </template>
    </ModalCustom>

    <!-- Panel Import -->
    <ProductImportPanel :show="showImportPanel" @close="showImportPanel = false" />

    <!-- TOAST THÔNG BÁO -->
    <ToastCustom :show="toast.show" :message="toast.message" :type="toast.type" />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { storeToRefs } from 'pinia'
import { useProductStore } from '@/stores/product.store'
import { useBrandStore } from '@/stores/brand.store'
import { useCategoryStore } from '@/stores/category.store'

// Import Components
import ProductCreateModal from './ProductCreateModal.vue'
import ProductDetailModal from './ProductDetailModal.vue'
import ProductFilter from './ProductFilter.vue'
import ProductImportPanel from './ProductImportPanel.vue'
import ButtonCustom from '@/components/custom/Button/ButtonDefault.vue'
import CardCustom from '@/components/custom/Card/CardCustom.vue'
import ModalCustom from '@/components/custom/Modal/ModalCustom.vue'
import ToastCustom from '@/components/custom/Toast/ToastCustom.vue'

const productStore = useProductStore()
const brandStore = useBrandStore()
const categoryStore = useCategoryStore()
const { products, isLoading, error } = storeToRefs(productStore)

// --- Toast State ---
const toast = ref({ show: false, message: '', type: 'success' as 'success' | 'error' })
const showToast = (message: string, type: 'success' | 'error' = 'success') => {
  toast.value = { show: true, message, type }
  setTimeout(() => (toast.value.show = false), 3000) // Tự tắt sau 3s
}

onMounted(async () => {
  try {
    await Promise.all([
      brandStore.fetchBrands(), // Load danh sách thương hiệu
      categoryStore.fetchCategories(), // Load danh sách danh mục
    ])
    await productStore.fetchProducts()
  } catch (e) {
    console.error('Lỗi tải dữ liệu ban đầu:', e)
  }
})

// --- Modal State ---
const showCreateModal = ref(false)
const showDetailModal = ref(false)
const showImportPanel = ref(false)
const selectedProductId = ref<string | null>(null)
const selectedProduct = ref<any>(null)
const showDeleteConfirm = ref(false)
const productToDelete = ref<any>(null)

const openCreateModal = () => {
  productStore.error = null
  selectedProduct.value = null
  showCreateModal.value = true
}

const openDetailModal = (product: any) => {
  productStore.error = null
  selectedProductId.value = product.id
  showDetailModal.value = true
}

const closeModal = () => {
  showCreateModal.value = false
}
const closeDetailModal = () => {
  showDetailModal.value = false
  selectedProductId.value = null
}

// --- Handlers ---

const handleFilterChange = (filterParams: any) => {
  productStore.fetchProducts(filterParams)
}

const handleCreate = async (payload: any) => {
  try {
    await productStore.createProduct(payload)
    showCreateModal.value = false // Đóng Modal
    showToast('✅ Thêm sản phẩm thành công!', 'success')
    await productStore.fetchProducts() // Reload
  } catch (err) {
    console.error(err)
    showToast('❌ Lỗi khi thêm sản phẩm.', 'error')
  }
}

const handleUpdate = async (payload: any) => {
  const { basic, detail } = payload
  try {
    // 1. Update Basic Info
    await productStore.updateProduct(basic.id, basic)
    // 2. Update Detail Info (nếu có)
    if (detail) {
      await productStore.saveProductDetail(basic.id, detail)
    }

    showToast('✅ Cập nhật sản phẩm thành công!', 'success')
    closeDetailModal() // Đóng Modal sau khi thành công
    await productStore.fetchProducts() // Reload
  } catch (err) {
    console.error(err)
    showToast('❌ Lỗi khi cập nhật sản phẩm.', 'error')
  }
}

const askDeleteProduct = (product: any) => {
  productStore.error = null
  productToDelete.value = product
  showDeleteConfirm.value = true
}

const cancelDelete = () => {
  showDeleteConfirm.value = false
  productToDelete.value = null
}

const confirmDelete = async () => {
  if (productToDelete.value) {
    try {
      await productStore.deleteProduct(productToDelete.value.id)
      showToast('🗑️ Xoá sản phẩm thành công!', 'success')
      await productStore.fetchProducts()
    } catch (e) {
      showToast('❌ Lỗi khi xoá sản phẩm.', 'error')
    }
    cancelDelete()
  }
}

// Giữ hàm này nếu cần gọi từ @click
const editProduct = (p: any) => openDetailModal(p)
</script>
