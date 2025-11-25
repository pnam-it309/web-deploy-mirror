<template>
  <div class="p-6">
    <!-- TIÊU ĐỀ VÀ NÚT TẠO MỚI -->
    <div class="flex justify-between items-center mb-4">
      <h1 class="text-2xl font-semibold">Quản lý Thương hiệu</h1>
      <ButtonCustom color="primary" @click="openCreateModal">
        + Thêm thương hiệu
      </ButtonCustom>
    </div>
  <div class="mb-4">
       <BrandFilter @filter="handleFilterChange" />
    </div>
    <!-- KHUNG BÁO LỖI -->
    <div
      v-if="error"
      class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-4"
      role="alert"
    >
      <strong class="font-bold">Lỗi!</strong>
      <span class="block sm:inline"> {{ error }}</span>
    </div>

    <!-- BẢNG DỮ LIỆU -->
    <CardCustom>
      <!-- TRẠNG THÁI LOADING -->
      <div v-if="isLoading && !brands.length" class="p-6 text-center">
        <p>Đang tải dữ liệu...</p>
      </div>

      <table v-else class="w-full text-left border-collapse">
        <thead class="bg-gray-100">
          <tr>
            <th class="p-3 border-b">#</th>
            <th class="p-3 border-b">Tên thương hiệu</th>
            <th class="p-3 border-b">Mã code</th>
            <th class="p-3 border-b">Slug</th>
            <th class="p-3 border-b">Mô tả</th>
            <th class="p-3 border-b">Trạng thái</th>
            <th class="p-3 border-b">Logo</th>
            <th class="p-3 border-b text-center">Hành động</th>
          </tr>
        </thead>
        <tbody>
          <!-- DÙNG DỮ LIỆU TỪ STORE -->
          <tr
            v-for="(brand, index) in brands"
            :key="brand.id"
            class="hover:bg-gray-50"
          >
            <td class="p-3 border-b">{{ index + 1 }}</td>
            <td class="p-3 border-b font-medium">{{ brand.name }}</td>
            <td class="p-3 border-b">{{ brand.code }}</td>
            <td class="p-3 border-b">{{ brand.slug }}</td>
            <td class="p-3 border-b truncate max-w-xs">
              {{ brand.description }}
            </td>
            <td class="p-3 border-b">
              <span
                :class="[
                  'px-2 py-1 rounded-full text-xs leading-5 font-semibold',
                  brand.status === 'ACTIVE'
                    ? 'bg-green-100 text-green-800'
                    : 'bg-red-100 text-red-800',
                ]"
              >
                {{ brand.status }}
              </span>
            </td>
            <td class="p-3 border-b">
              <img
                vif="brand.logoUrl"
                :src="brand.logoUrl"
                alt="Logo"
                class="w-10 h-10 object-contain"
              />
            </td>
            <td
              class="p-3 border-b text-center grid gap-2 grid-rows-2 justify-center"
            >
              <ButtonCustom color="info" size="small" @click="editBrand(brand)">
                Sửa
              </ButtonCustom>
              <ButtonCustom
                color="danger"
                size="small"
                @click="askDeleteBrand(brand)"
              >
                Xoá
              </ButtonCustom>
            </td>
          </tr>
          <!-- TRẠNG THÁI RỖNG -->
          <tr v-if="!brands.length && !isLoading">
            <td colspan="8" class="p-6 text-center text-gray-500 italic">
              Chưa có thương hiệu nào.
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

    <!-- MODAL XÁC NHẬN XOÁ (Thay thế confirm()) -->
    <ModalCustom
      :show="showDeleteConfirm"
      @close="cancelDelete"
      size="sm"
    >
      <template #title>Xác nhận xoá</template>
      <template #default>
        <p>
          Bạn có chắc chắn muốn xoá thương hiệu "<strong>{{
            brandToDelete?.name
          }}</strong
          >"?
        </p>
        <p class="text-sm text-red-600 mt-2">
          Hành động này sẽ chỉ đổi trạng thái sang INACTIVE (Soft Delete).
        </p>
      </template>
      <template #footer>
        <ButtonCustom color="secondary" @click="cancelDelete">
          Huỷ
        </ButtonCustom>
        <ButtonCustom
          color="danger"
          :loading="isLoading"
          @click="confirmDelete"
        >
          Xoá
        </ButtonCustom>
      </template>
    </ModalCustom>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useBrandStore } from '@/stores/brand.store'; // Import store
import { storeToRefs } from 'pinia'; // Dùng để lấy state
import BrandCreateModal from './BrandCreateModal.vue';
import ButtonCustom from '@/components/custom/Button/ButtonDefault.vue';
import CardCustom from '@/components/custom/Card/CardCustom.vue';
import ModalCustom from '@/components/custom/Modal/ModalCustom.vue'; // Import Modal
import BrandFilter from './BrandFilter.vue';

// 1. KHỞI TẠO STORE
const brandStore = useBrandStore();

// 2. LẤY STATE (dùng storeToRefs để giữ reactivity)
// Xoá bỏ hoàn toàn "brands = ref([...])" (dữ liệu giả)
const { brands, isLoading, error } = storeToRefs(brandStore);

// 3. TẢI DỮ LIỆU KHI COMPONENT ĐƯỢC TẠO
onMounted(() => {
  brandStore.fetchBrands(); // Gọi action từ store
});

// --- Logic cho Modal Tạo/Sửa ---
const showModal = ref(false);
const selectedBrand = ref(null);

const openCreateModal = () => {
  selectedBrand.value = null;
  showModal.value = true;
};
const closeModal = () => (showModal.value = false);

// 4. HÀM SAVE (GỌI STORE ACTION)
const saveBrand = async (brandData) => {
  try {
    if (brandData.id) {
      // Logic Cập nhật
      await brandStore.updateBrand(brandData.id, brandData);
    } else {
      // Logic Tạo mới
      await brandStore.createBrand(brandData);
    }
    closeModal(); // Chỉ đóng modal khi API thành công
  } catch (err) {
    // Nếu API thất bại, store đã gán lỗi
    // Modal sẽ không đóng, user có thể sửa lại
    console.error('Lỗi không thể lưu:', err);
  }
};

const editBrand = (brand) => {
  selectedBrand.value = { ...brand }; // Copy để tránh sửa trực tiếp state
  showModal.value = true;
};

// --- Logic cho Modal Xoá (Thay thế confirm()) ---
const showDeleteConfirm = ref(false);
const brandToDelete = ref(null);

const askDeleteBrand = (brand) => {
  brandToDelete.value = brand;
  showDeleteConfirm.value = true;
};

const cancelDelete = () => {
  showDeleteConfirm.value = false;
  brandToDelete.value = null;
};

// 5. HÀM DELETE (GỌI STORE ACTION)
const confirmDelete = async () => {
  if (brandToDelete.value) {
    await brandStore.deleteBrand(brandToDelete.value.id);
    cancelDelete(); // Đóng modal xác nhận
  }
};
const handleFilterChange = (filterParams) => {
  brandStore.fetchBrands(filterParams);
};

</script>