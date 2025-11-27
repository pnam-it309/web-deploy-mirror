<template>
  <div class="p-6">
    <!-- FILTER -->
    <div class="mb-4">
       <BrandFilter @filter="handleFilterChange" />
    </div>

    <!-- ERROR ALERT -->
    <div
      v-if="error"
      class="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg relative mb-4 shadow-sm"
      role="alert"
    >
      <strong class="font-bold">Lỗi!</strong>
      <span class="block sm:inline"> {{ error }}</span>
    </div>
    
    <ButtonCustom color="coffee" @click="openCreateModal">
      + Thêm thương hiệu
    </ButtonCustom>
    <!-- BẢNG DỮ LIỆU -->
    <CardCustom>
      <!-- TRẠNG THÁI LOADING -->
      <div v-if="isLoading && !brands.length" class="p-12 text-center">
        <div class="animate-spin inline-block w-8 h-8 border-4 border-current border-t-transparent text-[#adc178] rounded-full mb-2"></div>
        <p class="text-gray-500 text-sm font-medium">Đang tải dữ liệu...</p>
      </div>

      <table v-else class="w-full text-left border-collapse">
        <!-- Header: Màu Sage nhạt, chữ Nâu -->
        <thead class="bg-[#f7f9ef]">
          <tr>
            <th class="p-4 border-b border-[#e6dfc0] font-bold text-sm text-[#5a483e]">#</th>
            <th class="p-4 border-b border-[#e6dfc0] font-bold text-sm text-[#5a483e]">Tên thương hiệu</th>
            <th class="p-4 border-b border-[#e6dfc0] font-bold text-sm text-[#5a483e]">Mã code</th>
            <th class="p-4 border-b border-[#e6dfc0] font-bold text-sm text-[#5a483e]">Slug</th>
            <th class="p-4 border-b border-[#e6dfc0] font-bold text-sm text-[#5a483e]">Mô tả</th>
            <th class="p-4 border-b border-[#e6dfc0] font-bold text-sm text-[#5a483e] text-center">Trạng thái</th>
            <th class="p-4 border-b border-[#e6dfc0] font-bold text-sm text-[#5a483e] text-center">Logo</th>
            <th class="p-4 border-b border-[#e6dfc0] font-bold text-sm text-[#5a483e] text-center">Hành động</th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="(brand, index) in brands"
            :key="brand.id"
            class="hover:bg-[#f0ead2]/30 transition-colors border-b border-[#f0ead2] last:border-0"
          >
            <td class="p-4 text-sm text-gray-500">{{ index + 1 }}</td>
            
            <td class="p-4 font-medium text-[#6c584c]">{{ brand.name }}</td>
            
            <td class="p-4 text-sm font-mono text-gray-600 bg-gray-50/50 px-2 py-1 rounded w-fit">{{ brand.code }}</td>
            
            <td class="p-4 text-sm text-gray-500 italic">{{ brand.slug }}</td>
            
            <td class="p-4 text-sm text-gray-600 truncate max-w-[200px]" :title="brand.description">
              {{ brand.description || '—' }}
            </td>
            
            <td class="p-4 text-center">
              <!-- Badge trạng thái custom theo Brand Palette -->
              <span
                :class="[
                  'px-2.5 py-1 rounded-full text-xs font-medium border',
                  brand.status === 'ACTIVE'
                    ? 'bg-[#dde5b6]/40 text-[#386641] border-[#adc178]/30' // Xanh Olive nhạt
                    : 'bg-red-50 text-red-700 border-red-100',
                ]"
              >
                {{ brand.status === 'ACTIVE' ? 'Đang hoạt động' : 'Ngừng hoạt động' }}
              </span>
            </td>
            
            <td class="p-4 text-center">
              <div class="flex justify-center">
                <img
                  v-if="brand.logoUrl"
                  :src="brand.logoUrl"
                  alt="Logo"
                  class="w-10 h-10 object-contain rounded-lg border border-gray-100 bg-white p-0.5 shadow-sm"
                />
                <span v-else class="w-10 h-10 flex items-center justify-center bg-gray-50 rounded-lg text-xs text-gray-400 border border-dashed border-gray-200">
                  No img
                </span>
              </div>
            </td>
            
            <td class="p-4 text-center">
              <div class="flex items-center justify-center gap-2">
                <!-- Nút Sửa: Màu Sage Soft (Xanh nhẹ) -->
                <ButtonCustom color="default" size="small" @click="editBrand(brand)">
                  Sửa
                </ButtonCustom>
                <!-- Nút Xoá: Màu Mocha Soft (Nâu đỏ nhẹ) -->
                <ButtonCustom color="cream" size="small" @click="askDeleteBrand(brand)">
                  Xoá
                </ButtonCustom>
              </div>
            </td>
          </tr>
          
          <!-- Empty State -->
          <tr v-if="!brands.length && !isLoading">
            <td colspan="8" class="p-12 text-center text-gray-500 italic bg-[#fffdf5]/50">
              <div class="flex flex-col items-center justify-center">
                <span class="text-4xl mb-2">📭</span>
                <span>Chưa có thương hiệu nào.</span>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </CardCustom>

    <!-- MODAL TẠO/SỬA -->
    <BrandCreateModal
      v-if="showModal"
      :editItem="selectedBrand"
      :loading="isLoading"
      @close="closeModal"
      @save="saveBrand"
    />

    <!-- MODAL XÁC NHẬN XOÁ -->
    <ModalCustom
      :show="showDeleteConfirm"
      @close="cancelDelete"
      size="sm"
    >
      <template #title>Xác nhận xoá</template>
      <template #default>
        <p class="text-gray-700">
          Bạn có chắc chắn muốn xoá thương hiệu <strong class="text-[#6c584c]">{{ brandToDelete?.name }}</strong>?
        </p>
        <p class="text-sm text-red-500 mt-2 bg-red-50 p-2 rounded border border-red-100">
          ⚠️ Hành động này sẽ chuyển trạng thái sang <strong>Ngừng hoạt động</strong>.
        </p>
      </template>
      <template #footer>
        <!-- Nút Huỷ: Màu Cream (Kem) -->
        <ButtonCustom color="cream" @click="cancelDelete">
          Huỷ bỏ
        </ButtonCustom>
        <!-- Nút Xoá: Màu Mocha (Nâu đậm) -->
        <ButtonCustom
          color="mocha"
          :loading="isLoading"
          @click="confirmDelete"
        >
          Xác nhận xoá
        </ButtonCustom>
      </template>
    </ModalCustom>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useBrandStore } from '@/stores/brand.store';
import { storeToRefs } from 'pinia';
import BrandCreateModal from './BrandCreateModal.vue';
import ButtonCustom from '@/components/custom/Button/ButtonDefault.vue';
import CardCustom from '@/components/custom/Card/CardCustom.vue';
import ModalCustom from '@/components/custom/Modal/ModalCustom.vue';
import BrandFilter from './BrandFilter.vue';

// 1. KHỞI TẠO STORE
const brandStore = useBrandStore();
const { brands, isLoading, error } = storeToRefs(brandStore);

onMounted(() => {
  brandStore.fetchBrands();
});

// --- Logic Modal Tạo/Sửa ---
const showModal = ref(false);
const selectedBrand = ref(null);

const openCreateModal = () => {
  selectedBrand.value = null;
  showModal.value = true;
};
const closeModal = () => (showModal.value = false);

const saveBrand = async (brandData: any) => {
  try {
    if (brandData.id) {
      await brandStore.updateBrand(brandData.id, brandData);
    } else {
      await brandStore.createBrand(brandData);
    }
    closeModal();
  } catch (err) {
    console.error('Lỗi không thể lưu:', err);
  }
};

const editBrand = (brand: any) => {
  selectedBrand.value = { ...brand };
  showModal.value = true;
};

// --- Logic Modal Xoá ---
const showDeleteConfirm = ref(false);
const brandToDelete = ref<any>(null);

const askDeleteBrand = (brand: any) => {
  brandToDelete.value = brand;
  showDeleteConfirm.value = true;
};

const cancelDelete = () => {
  showDeleteConfirm.value = false;
  brandToDelete.value = null;
};

const confirmDelete = async () => {
  if (brandToDelete.value) {
    await brandStore.deleteBrand(brandToDelete.value.id);
    cancelDelete();
  }
};

const handleFilterChange = (filterParams: any) => {
  brandStore.fetchBrands(filterParams);
};
</script>