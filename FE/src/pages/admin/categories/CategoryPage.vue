<template>
  <div class="p-6 text-gray-900">
    <div class="flex justify-between items-center mb-6">
      <h2 class="text-2xl font-semibold">Quản lý Danh mục sản phẩm</h2>
      <div class="flex items-center space-x-3">
        <ButtonCustom
          color="primary"
          @click="openCreateModal"
        >
          + Thêm danh mục
        </ButtonCustom>
      </div>
    </div>
    <Categoryfilter
      :categories="categories"
      @filter="handleFilterChange"
      ref="categoryFilterRef"
    />
    <!-- KHUNG BÁO LỖI (Lấy lỗi từ Pinia Store) -->
    <div
      v-if="error"
      class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-4"
      role="alert"
    >
      <strong class="font-bold">Lỗi!</strong>
      <span class="block sm:inline"> {{ error }}</span>
    </div>

    <CardCustom>
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
          <tr v-if="isLoading">
            <td colspan="6" class="text-center py-6 text-gray-500">Đang tải...</td>
          </tr>
          <tr
            v-else
            v-for="(cat, index) in filteredCategories"
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
              <ButtonCustom color="info" size="small" @click="editCategory(cat)">
                Sửa
              </ButtonCustom>
              <ButtonCustom color="danger" size="small" @click="askDeleteCategory(cat)">
                Xoá
              </ButtonCustom>
            </td>
          </tr>
          <tr v-if="!isLoading && (!categories || categories.length === 0)">
            <td colspan="6" class="text-center py-6 text-gray-500 italic">
              Chưa có danh mục nào
            </td>
          </tr>
        </tbody>
      </table>
    </CardCustom>

    <!-- MODAL TẠO/SỬA (Dùng v-if) -->
    <CategoryCreateModal
      v-if="showModal"
      :edit-item="editingCategory"
      :loading="isLoading"
      @close="closeModal"
      @saved="handleSaved"
    />

    <!-- MODAL XÁC NHẬN XOÁ (Thêm mới) -->
    <ModalCustom :show="showDeleteConfirm" @close="cancelDelete" size="sm">
      <template #title>Xác nhận xoá</template>
      <template #default>
        <p>
          Bạn có chắc chắn muốn xoá danh mục "<strong>{{ categoryToDelete?.name }}</strong>"?
        </p>
        <p class="text-sm text-red-600 mt-2">
          Hành động này sẽ chỉ đổi trạng thái sang INACTIVE (Soft Delete).
        </p>
      </template>
      <template #footer>
        <ButtonCustom color="secondary" @click="cancelDelete"> Huỷ </ButtonCustom>
        <ButtonCustom color="danger" :loading="isLoading" @click="confirmDelete">
          Xoá
        </ButtonCustom>
      </template>
    </ModalCustom>

  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { storeToRefs } from 'pinia';
import { useCategoryStore } from '@/stores/category.store'; // <-- DÙNG STORE
import Categoryfilter from './Categoryfilter.vue'
import CategoryCreateModal from './CategoryCreateModal.vue'
import ButtonCustom from '@/components/custom/Button/ButtonDefault.vue'
import CardCustom from '@/components/custom/Card/CardCustom.vue'
import ModalCustom from '@/components/custom/Modal/ModalCustom.vue' // <-- Import Modal

// Interface (định nghĩa) cho Category (DÙNG TỪ STORE)
import type { Category as CategoryResponse } from '@/services/axios/category.services';

// --- (Xoá bỏ stubCategories) ---

// 1. KHỞI TẠO STORE
const categoryStore = useCategoryStore();
const router = useRouter();

// 2. LẤY STATE (từ Pinia)
const { categories, isLoading, error } = storeToRefs(categoryStore);

const editingCategory = ref<CategoryResponse | null>(null)
const showModal = ref(false)

// (Các ref cho filter - giữ nguyên)
const categoryFilterRef = ref()
const currentFilters = ref({
  keyword: '',
  parentCategory: undefined,
  categoryType: ''
})

const handleFilter = (filters: any) => {
  currentFilters.value = filters
}

// 3. TẢI DỮ LIỆU KHI MOUNTED (Gọi action)
onMounted(() => {
  categoryStore.fetchCategories();
});

// Logic lọc (Giữ nguyên)
const filteredCategories = computed(() => {
  // TODO: Thêm logic lọc dựa trên currentFilters.value
  return categories.value
})

// --- Logic Modal Tạo/Sửa ---
const openCreateModal = () => {
  categoryStore.error = null;
  editingCategory.value = null;
  showModal.value = true;
};

const editCategory = (cat: CategoryResponse) => {
  categoryStore.error = null;
  editingCategory.value = { ...cat };
  showModal.value = true;
}

const closeModal = () => {
  showModal.value = false
  editingCategory.value = null
  // (Xoá logic router, vì modal không còn điểu khiển bởi route)
}

// 4. HÀM SAVE (Gọi store action)
const handleSaved = async (payload: any) => {
  try {
    if (payload.id) {
      await categoryStore.updateCategory(payload.id, payload);
    } else {
      await categoryStore.createCategory(payload);
    }
    closeModal();
  } catch(err) {
    // Lỗi đã được store xử lý và gán vào 'error'
    console.error('Lỗi không thể lưu danh mục:', err);
  }
}

// --- Logic Modal Xoá (Nâng cấp) ---
const showDeleteConfirm = ref(false);
const categoryToDelete = ref<CategoryResponse | null>(null);

const askDeleteCategory = (category: CategoryResponse) => {
  // (Xoá logic getSubtreeIds vì Backend sẽ xử lý)
  categoryStore.error = null;
  categoryToDelete.value = category;
  showDeleteConfirm.value = true;
}

const cancelDelete = () => {
  showDeleteConfirm.value = false;
  categoryToDelete.value = null;
};

// 5. HÀM DELETE (Gọi store action)
const confirmDelete = async () => {
  if (categoryToDelete.value) {
    await categoryStore.deleteCategory(categoryToDelete.value.id);
    cancelDelete();
  }
}

// (Hàm getParentName - Sửa để dùng ID string)
const getParentName = (parentId: string | null | undefined) => {
  if (!parentId) return '—'
  const p = categories.value.find((c) => c.id === parentId)
  return p ? p.name : '—'
}
const handleFilterChange = (filterParams: any) => {
  // Gọi store để load lại dữ liệu từ Server với tham số lọc
  categoryStore.fetchCategories(filterParams);
}
</script>

<style scoped>
table th,
table td {
  @apply text-sm;
}
</style>
