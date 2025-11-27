<template>
  <div class="p-6 min-h-screen bg-[#f8f9fa]">
    <!-- FILTER: Bộ lọc -->
    <div class="mb-5">
      <Categoryfilter
        :categories="categories"
        @filter="handleFilterChange"
        ref="categoryFilterRef"
      />
    </div>

    <!-- ERROR ALERT: Thông báo lỗi -->
    <div
      v-if="error"
      class="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg relative mb-5 shadow-sm flex items-center"
      role="alert"
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
      <span class="font-medium mr-1">Lỗi!</span> {{ error }}
    </div>
    <ButtonCustom
      color="coffee"
      @click="openCreateModal"
      class="shadow-md hover:shadow-lg transition-all"
    >+ Thêm danh mục 
    </ButtonCustom>
    <!-- TABLE: Bảng dữ liệu -->
    <CardCustom>
      <!-- Loading State -->
      <div v-if="isLoading && !categories.length" class="p-12 text-center">
        <div
          class="animate-spin inline-block w-8 h-8 border-4 border-current border-t-transparent text-[#adc178] rounded-full mb-3"
        ></div>
        <p class="text-gray-500 text-sm font-medium animate-pulse">Đang tải dữ liệu...</p>
      </div>

      <!-- Data Table -->
      <table v-else class="w-full text-left border-collapse">
        <thead class="bg-[#f7f9ef]">
          <!-- Header màu Sage nhạt -->
          <tr>
            <th class="px-6 py-4 text-sm font-bold text-[#5a483e] border-b border-[#e6dfc0]">#</th>
            <th class="px-6 py-4 text-sm font-bold text-[#5a483e] border-b border-[#e6dfc0]">
              Tên danh mục
            </th>
            <th class="px-6 py-4 text-sm font-bold text-[#5a483e] border-b border-[#e6dfc0]">
              Slug
            </th>
            <th class="px-6 py-4 text-sm font-bold text-[#5a483e] border-b border-[#e6dfc0]">
              Mô tả
            </th>
            <th class="px-6 py-4 text-sm font-bold text-[#5a483e] border-b border-[#e6dfc0]">
              Danh mục cha
            </th>
            <th
              class="px-6 py-4 text-sm font-bold text-[#5a483e] border-b border-[#e6dfc0] text-center"
            >
              Hành động
            </th>
          </tr>
        </thead>
        <tbody class="bg-white divide-y divide-[#f0ead2]">
          <!-- Đường kẻ màu Cream -->

          <tr
            v-for="(cat, index) in filteredCategories"
            :key="cat.id"
            class="hover:bg-[#f0ead2]/30 transition-colors duration-150"
          >
            <td class="px-6 py-4 text-sm text-gray-500">{{ index + 1 }}</td>

            <td class="px-6 py-4">
              <div class="font-semibold text-[#6c584c]">{{ cat.name }}</div>
            </td>

            <td class="px-6 py-4">
              <span
                class="text-sm font-mono text-gray-500 bg-gray-50 px-2 py-1 rounded border border-gray-100"
              >
                {{ cat.slug }}
              </span>
            </td>

            <td
              class="px-6 py-4 text-sm text-gray-600 max-w-xs truncate"
              :title="cat.description || ''"
            >
              {{ cat.description || '—' }}
            </td>

            <td class="px-6 py-4">
              <span
                v-if="cat.parentId"
                class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-[#e6dfc0]/40 text-[#5a483e]"
              >
                {{ getParentName(cat.parentId) }}
              </span>
              <span v-else class="text-gray-400 text-sm">—</span>
            </td>

            <td class="px-6 py-4 text-center">
              <div class="flex justify-center space-x-2">
                <!-- Nút Sửa: Sage Soft -->
                <ButtonCustom color="default" size="small" @click="editCategory(cat)">
                  Sửa
                </ButtonCustom>
                <!-- Nút Xoá: Mocha Soft -->
                <ButtonCustom color="cream" size="small" @click="askDeleteCategory(cat)">
                  Xoá
                </ButtonCustom>
              </div>
            </td>
          </tr>

          <!-- Empty State -->
          <tr v-if="!isLoading && (!categories || categories.length === 0)">
            <td colspan="6" class="p-12 text-center bg-[#fffdf5]/50">
              <div class="flex flex-col items-center justify-center text-gray-400">
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  class="h-12 w-12 mb-2 opacity-50"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke="currentColor"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="1.5"
                    d="M20 13V6a2 2 0 00-2-2H6a2 2 0 00-2 2v7m16 0v5a2 2 0 01-2 2H6a2 2 0 01-2-2v-5m16 0h-2.586a1 1 0 00-.707.293l-2.414 2.414a1 1 0 01-.707.293h-3.172a1 1 0 01-.707-.293l-2.414-2.414A1 1 0 006.586 13H4"
                  />
                </svg>
                <span class="text-sm font-medium">Chưa có danh mục nào</span>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </CardCustom>

    <!-- MODAL TẠO/SỬA -->
    <CategoryCreateModal
      v-if="showModal"
      :edit-item="editingCategory"
      :loading="isLoading"
      @close="closeModal"
      @saved="handleSaved"
    />

    <!-- MODAL XÁC NHẬN XOÁ -->
    <ModalCustom :show="showDeleteConfirm" @close="cancelDelete" size="sm">
      <template #title>Xác nhận xoá</template>
      <template #default>
        <p class="text-gray-700">
          Bạn có chắc chắn muốn xoá danh mục
          <strong class="text-[#6c584c]">{{ categoryToDelete?.name }}</strong
          >?
        </p>
        <p
          class="text-sm text-red-500 mt-2 bg-red-50 p-3 rounded-md border border-red-100 flex items-start gap-2"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="h-5 w-5 flex-shrink-0"
            viewBox="0 0 20 20"
            fill="currentColor"
          >
            <path
              fill-rule="evenodd"
              d="M8.257 3.099c.765-1.36 2.722-1.36 3.486 0l5.58 9.92c.75 1.334-.213 2.98-1.742 2.98H4.42c-1.53 0-2.493-1.646-1.743-2.98l5.58-9.92zM11 13a1 1 0 11-2 0 1 1 0 012 0zm-1-8a1 1 0 00-1 1v3a1 1 0 002 0V6a1 1 0 00-1-1z"
              clip-rule="evenodd"
            />
          </svg>
          Hành động này sẽ chuyển trạng thái sang <strong>Ngừng hoạt động</strong>.
        </p>
      </template>
      <template #footer>
        <!-- Nút Huỷ: Màu Cream -->
        <ButtonCustom color="cream" @click="cancelDelete"> Huỷ bỏ </ButtonCustom>
        <!-- Nút Xoá: Màu Mocha -->
        <ButtonCustom color="mocha" :loading="isLoading" @click="confirmDelete">
          Xác nhận xoá
        </ButtonCustom>
      </template>
    </ModalCustom>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { storeToRefs } from 'pinia'
import { useCategoryStore } from '@/stores/category.store'
import Categoryfilter from './CategoryFilter.vue'
import CategoryCreateModal from './CategoryCreateModal.vue'
import ButtonCustom from '@/components/custom/Button/ButtonDefault.vue'
import CardCustom from '@/components/custom/Card/CardCustom.vue'
import ModalCustom from '@/components/custom/Modal/ModalCustom.vue'

// Lưu ý: Đường dẫn import service nên kiểm tra lại là 'category.service' hay 'category.services' tuỳ file bạn tạo
import type { Category as CategoryResponse } from '@/services/axios/category.services'

const categoryStore = useCategoryStore()
const { categories, isLoading, error } = storeToRefs(categoryStore)

const editingCategory = ref<CategoryResponse | null>(null)
const showModal = ref(false)
const categoryFilterRef = ref()

const handleFilterChange = (filterParams: any) => {
  categoryStore.fetchCategories(filterParams)
}

onMounted(() => {
  categoryStore.fetchCategories()
})

const filteredCategories = computed(() => {
  return categories.value
})

const openCreateModal = () => {
  categoryStore.error = null
  editingCategory.value = null
  showModal.value = true
}

const editCategory = (cat: CategoryResponse) => {
  categoryStore.error = null
  editingCategory.value = { ...cat }
  showModal.value = true
}

const closeModal = () => {
  showModal.value = false
  editingCategory.value = null
}

const handleSaved = async (payload: any) => {
  try {
    if (payload.id) {
      await categoryStore.updateCategory(payload.id, payload)
    } else {
      await categoryStore.createCategory(payload)
    }
    closeModal()
  } catch (err) {
    console.error('Lỗi không thể lưu danh mục:', err)
  }
}

const showDeleteConfirm = ref(false)
const categoryToDelete = ref<CategoryResponse | null>(null)

const askDeleteCategory = (category: CategoryResponse) => {
  categoryStore.error = null
  categoryToDelete.value = category
  showDeleteConfirm.value = true
}

const cancelDelete = () => {
  showDeleteConfirm.value = false
  categoryToDelete.value = null
}

const confirmDelete = async () => {
  if (categoryToDelete.value) {
    await categoryStore.deleteCategory(categoryToDelete.value.id)
    cancelDelete()
  }
}

const getParentName = (parentId: string | null | undefined) => {
  if (!parentId) return '—'
  const p = categories.value.find((c) => c.id === parentId)
  return p ? p.name : '—'
}
</script>
