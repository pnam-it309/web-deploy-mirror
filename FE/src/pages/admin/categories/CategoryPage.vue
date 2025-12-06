<template>
  <div class="p-6 min-h-screen bg-[#f8f9fa] dark:bg-brand-dark-200 transition-colors duration-300 rounded-lg">
    <!-- FILTER -->
    <div class="mb-5">
      <Categoryfilter :categories="categories" @filter="handleFilterChange" ref="categoryFilterRef" />
    </div>

    <!-- ERROR -->
    <div v-if="error"
      class="bg-red-50 dark:bg-red-900/20 border border-red-200 dark:border-red-800 text-red-700 dark:text-red-300 px-4 py-3 rounded-lg relative mb-5 shadow-sm flex items-center rounded-lg">
      <span class="font-bold mr-1">Lỗi!</span> {{ error }}
    </div>
    <ButtonCustom color="coffee" @click="openCreateModal" class="shadow-md hover:shadow-lg transition-all">
      <template #default>+ Thêm danh mục</template>
    </ButtonCustom>
    <!-- TABLE CUSTOM -->
    <TableCustom :data="filteredCategories" :columns="columns" :loading="isLoading" :total="categories.length"
      :pageSize="10">
      <!-- 1. Index -->
      <template #index="{ index }">
        <span class="text-gray-500 dark:text-gray-400 font-mono">{{ index + 1 }}</span>
      </template>

      <!-- 2. Name -->
      <template #name="{ record }">
        <div class="font-semibold text-[#6c584c] dark:text-brand-cream">{{ record.name }}</div>
      </template>

      <!-- 3. Slug -->
      <template #slug="{ record }">
        <span
          class="text-xs font-mono text-gray-600 dark:text-gray-300 bg-gray-50 dark:bg-white/10 px-2 py-1 rounded border border-gray-100 dark:border-white/10">
          {{ record.slug }}
        </span>
      </template>

      <!-- 4. Description -->
      <template #description="{ record }">
        <div class="text-gray-600 dark:text-gray-400 max-w-xs truncate" :title="record.description || ''">
          {{ record.description || '—' }}
        </div>
      </template>

      <!-- 5. Parent -->
      <template #parentId="{ record }">
        <span v-if="record.parentId"
          class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-[#e6dfc0]/40 text-[#5a483e] border border-[#e6dfc0] dark:bg-brand-coffee/20 dark:text-brand-sage dark:border-brand-coffee/30">
          <span class="mr-1 opacity-60">↳</span> {{ getParentName(record.parentId) }}
        </span>
        <span v-else class="text-gray-400 dark:text-gray-600 text-xs italic">Gốc</span>
      </template>

      <!-- 6. Actions -->
      <template #actions="{ record }">
        <div class="flex justify-center space-x-2">
          <ButtonCustom color="sage-soft" size="small" @click="editCategory(record)">Sửa</ButtonCustom>
          <ButtonCustom color="mocha-soft" size="small" @click="askDeleteCategory(record)">Xoá</ButtonCustom>
        </div>
      </template>
    </TableCustom>

    <!-- MODALS -->
    <CategoryCreateModal v-if="showModal" :edit-item="editingCategory" :loading="isLoading" @close="closeModal"
      @saved="handleSaved" />

    <ModalCustom :show="showDeleteConfirm" @close="cancelDelete" size="sm">
      <template #title>Xác nhận xoá</template>
      <template #default>
        <p class="text-gray-700 dark:text-gray-300">Bạn có chắc chắn muốn xoá danh mục <strong
            class="text-[#6c584c] dark:text-brand-cream">{{ categoryToDelete?.name }}</strong>?</p>
        <p
          class="text-sm text-red-500 dark:text-red-400 mt-3 bg-red-50 dark:bg-red-900/20 p-3 rounded-md border border-red-100 dark:border-red-800/50">
          ⚠️ Hành động này sẽ chuyển trạng thái sang <strong>Ngừng hoạt động</strong>.</p>
      </template>
      <template #footer>
        <ButtonCustom color="cream" @click="cancelDelete">Huỷ bỏ</ButtonCustom>
        <ButtonCustom color="mocha" :loading="isLoading" @click="confirmDelete">Xác nhận xoá</ButtonCustom>
      </template>
    </ModalCustom>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { storeToRefs } from 'pinia';
import { useCategoryStore } from '@/stores/category.store';
import Categoryfilter from './Categoryfilter.vue'
import CategoryCreateModal from './CategoryCreateModal.vue'
import ButtonCustom from '@/components/custom/Button/ButtonDefault.vue'
import ModalCustom from '@/components/custom/Modal/ModalCustom.vue'
import TableCustom from '@/components/custom/Table/TableCustom.vue' // Import mới

import type { Category as CategoryResponse } from '@/services/axios/category.service';

const categoryStore = useCategoryStore();
const { categories, isLoading, error } = storeToRefs(categoryStore);

// --- ĐỊNH NGHĨA CỘT ---
const columns = [
  { title: '#', key: 'index', width: '50px' },
  { title: 'Tên danh mục', key: 'name' },
  { title: 'Slug', key: 'slug' },
  { title: 'Mô tả', key: 'description' },
  { title: 'Danh mục cha', key: 'parentId' },
  { title: 'Hành động', key: 'actions', align: 'center', width: '150px' },
];

const editingCategory = ref<CategoryResponse | null>(null)
const showModal = ref(false)
const categoryFilterRef = ref()

onMounted(() => { categoryStore.fetchCategories(); });

const filteredCategories = computed(() => categories.value)

const handleFilterChange = (filterParams: any) => { categoryStore.fetchCategories(filterParams); }

const openCreateModal = () => {
  categoryStore.error = null; editingCategory.value = null; showModal.value = true;
};

const editCategory = (cat: CategoryResponse) => {
  categoryStore.error = null; editingCategory.value = { ...cat }; showModal.value = true;
}

const closeModal = () => { showModal.value = false; editingCategory.value = null; }

const handleSaved = async (payload: any) => {
  try {
    if (payload.id) await categoryStore.updateCategory(payload.id, payload);
    else await categoryStore.createCategory(payload);
    closeModal();
  } catch (err) { console.error('Lỗi:', err); }
}

const showDeleteConfirm = ref(false);
const categoryToDelete = ref<CategoryResponse | null>(null);
const askDeleteCategory = (category: CategoryResponse) => {
  categoryStore.error = null; categoryToDelete.value = category; showDeleteConfirm.value = true;
}
const cancelDelete = () => { showDeleteConfirm.value = false; categoryToDelete.value = null; };
const confirmDelete = async () => {
  if (categoryToDelete.value) {
    await categoryStore.deleteCategory(categoryToDelete.value.id);
    cancelDelete();
  }
}

const getParentName = (parentId: string | null | undefined) => {
  if (!parentId) return '—'
  const p = categories.value.find((c) => c.id === parentId)
  return p ? p.name : '—'
}
</script>