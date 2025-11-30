<template>
  <div class="p-6 min-h-screen bg-[#f8f9fa] dark:bg-brand-dark-200 transition-colors duration-300">
    <!-- FILTER -->
    <div class="mb-5">
      <BrandFilter @filter="handleFilterChange" />
    </div>

    <!-- ERROR -->
    <div
      v-if="error"
      class="bg-red-50 dark:bg-red-900/20 border border-red-200 dark:border-red-800 text-red-700 dark:text-red-300 px-4 py-3 rounded-lg relative mb-5 shadow-sm flex items-center"
    >
      <span class="font-bold mr-1">Lỗi!</span> {{ error }}
    </div>
    <ButtonCustom color="coffee" @click="openCreateModal"> + Thêm thương hiệu </ButtonCustom>
    <!-- TABLE CUSTOM -->
    <TableCustom
      :data="brands"
      :columns="columns"
      :loading="isLoading"
      :total="brands.length"
      :pageSize="10"
    >
      <!-- 1. Index -->
      <template #index="{ index }">
        <span class="text-gray-500 dark:text-gray-400 font-mono">{{ index + 1 }}</span>
      </template>

      <!-- 2. Tên -->
      <template #name="{ record }">
        <div class="font-semibold text-[#6c584c] dark:text-brand-cream">{{ record.name }}</div>
      </template>

      <!-- 3. Code -->
      <template #code="{ record }">
        <span
          class="text-xs font-mono bg-gray-50 dark:bg-white/10 px-2 py-1 rounded text-gray-600 dark:text-gray-300 border border-gray-200 dark:border-white/10"
        >
          {{ record.code }}
        </span>
      </template>

      <!-- 4. Slug -->
      <template #slug="{ record }">
        <span class="text-sm text-gray-500 dark:text-gray-400 italic">{{ record.slug }}</span>
      </template>

      <!-- 5. Mô tả -->
      <template #description="{ record }">
        <div
          class="text-gray-600 dark:text-gray-400 max-w-xs truncate"
          :title="record.description || ''"
        >
          {{ record.description || '—' }}
        </div>
      </template>

      <!-- 6. Trạng thái -->
      <template #status="{ record }">
        <span
          :class="[
            'px-2.5 py-1 rounded-full text-xs font-medium border',
            record.status === 'ACTIVE'
              ? 'bg-[#dde5b6]/40 text-[#386641] border-[#adc178]/30 dark:bg-brand-sage/20 dark:text-brand-sage dark:border-brand-sage/30'
              : 'bg-red-50 text-red-700 border-red-100 dark:bg-red-900/30 dark:text-red-300 dark:border-red-800',
          ]"
        >
          {{ record.status === 'ACTIVE' ? 'Đang hoạt động' : 'Ngừng hoạt động' }}
        </span>
      </template>

      <!-- 7. Logo -->
      <template #logoUrl="{ record }">
        <div class="flex justify-center">
          <img
            v-if="record.logoUrl"
            :src="record.logoUrl"
            class="w-8 h-8 object-contain rounded border border-gray-100 bg-white p-0.5"
          />
          <span
            v-else
            class="w-8 h-8 flex items-center justify-center bg-gray-50 dark:bg-white/5 rounded text-xs text-gray-400 border border-dashed border-gray-200 dark:border-gray-700"
            >No img</span
          >
        </div>
      </template>

      <!-- 8. Actions -->
      <template #actions="{ record }">
        <div class="flex justify-center space-x-2">
          <ButtonCustom color="sage-soft" size="small" @click="editBrand(record)">Sửa</ButtonCustom>
          <ButtonCustom color="mocha-soft" size="small" @click="askDeleteBrand(record)"
            >Xoá</ButtonCustom
          >
        </div>
      </template>
    </TableCustom>

    <!-- MODALS -->
    <BrandCreateModal
      v-if="showModal"
      :editItem="selectedBrand"
      :loading="isLoading"
      @close="closeModal"
      @save="saveBrand"
    />

    <ModalCustom :show="showDeleteConfirm" @close="cancelDelete" size="sm">
      <template #title>Xác nhận xoá</template>
      <template #default>
        <p class="text-gray-700 dark:text-gray-300">
          Bạn có chắc chắn muốn xoá thương hiệu
          <strong class="text-[#6c584c] dark:text-brand-cream">{{ brandToDelete?.name }}</strong
          >?
        </p>
        <p
          class="text-sm text-red-500 dark:text-red-400 mt-3 bg-red-50 dark:bg-red-900/20 p-3 rounded-md border border-red-100 dark:border-red-800/50"
        >
          ⚠️ Hành động này sẽ chuyển trạng thái sang <strong>Ngừng hoạt động</strong>.
        </p>
      </template>
      <template #footer>
        <ButtonCustom color="cream" @click="cancelDelete">Huỷ bỏ</ButtonCustom>
        <ButtonCustom color="mocha" :loading="isLoading" @click="confirmDelete"
          >Xác nhận xoá</ButtonCustom
        >
      </template>
    </ModalCustom>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useBrandStore } from '@/stores/brand.store'
import { storeToRefs } from 'pinia'
import BrandCreateModal from './BrandCreateModal.vue'
import BrandFilter from './BrandFilter.vue'
import ButtonCustom from '@/components/custom/Button/ButtonDefault.vue'
import ModalCustom from '@/components/custom/Modal/ModalCustom.vue'
import TableCustom from '@/components/custom/Table/TableCustom.vue'

const brandStore = useBrandStore()
const { brands, isLoading, error } = storeToRefs(brandStore)

// ĐỊNH NGHĨA CỘT
const columns = [
  { title: '#', key: 'index', width: '50px' },
  { title: 'Tên thương hiệu', key: 'name' },
  { title: 'Mã code', key: 'code' },
  { title: 'Slug', key: 'slug' },
  { title: 'Mô tả', key: 'description' },
  { title: 'Trạng thái', key: 'status', align: 'center' },
  { title: 'Logo', key: 'logoUrl', align: 'center' },
  { title: 'Hành động', key: 'actions', align: 'center', width: '150px' },
]

onMounted(() => {
  brandStore.fetchBrands()
})

const showModal = ref(false)
const selectedBrand = ref(null)
const openCreateModal = () => {
  selectedBrand.value = null
  showModal.value = true
}
const closeModal = () => (showModal.value = false)

const saveBrand = async (brandData: any) => {
  try {
    if (brandData.id) await brandStore.updateBrand(brandData.id, brandData)
    else await brandStore.createBrand(brandData)
    closeModal()
  } catch (err) {
    console.error('Lỗi:', err)
  }
}

const editBrand = (brand: any) => {
  selectedBrand.value = { ...brand }
  showModal.value = true
}

const showDeleteConfirm = ref(false)
const brandToDelete = ref<any>(null)
const askDeleteBrand = (brand: any) => {
  brandToDelete.value = brand
  showDeleteConfirm.value = true
}
const cancelDelete = () => {
  showDeleteConfirm.value = false
  brandToDelete.value = null
}
const confirmDelete = async () => {
  if (brandToDelete.value) {
    await brandStore.deleteBrand(brandToDelete.value.id)
    cancelDelete()
  }
}

const handleFilterChange = (filterParams: any) => {
  brandStore.fetchBrands(filterParams)
}
</script>
