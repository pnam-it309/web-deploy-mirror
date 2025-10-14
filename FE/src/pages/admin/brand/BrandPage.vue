<template>
  <div class="p-6">
    <div class="flex justify-between items-center mb-4">
      <h1 class="text-2xl font-semibold">Brands</h1>
      <button
        @click="openCreateModal"
        class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg"
      >
        + Thêm thương hiệu
      </button>
    </div>

    <!-- Table -->
    <div class="overflow-x-auto bg-white shadow rounded-lg">
      <table class="w-full text-left border-collapse">
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
        <tr
          v-for="(brand, index) in brands"
          :key="brand.id"
          class="hover:bg-gray-50"
        >
          <td class="p-3 border-b">{{ index + 1 }}</td>
          <td class="p-3 border-b font-medium">{{ brand.name }}</td>
          <td class="p-3 border-b">{{ brand.code }}</td>
          <td class="p-3 border-b">{{ brand.slug }}</td>
          <td class="p-3 border-b truncate max-w-xs">{{ brand.description }}</td>
          <td class="p-3 border-b">
              <span
                :class="[
                  'px-2 py-1 rounded text-sm font-semibold',
                  brand.status === 'ACTIVE'
                    ? 'bg-green-100 text-green-700'
                    : 'bg-red-100 text-red-700',
                ]"
              >
                {{ brand.status }}
              </span>
          </td>
          <td class="p-3 border-b">
            <img
              v-if="brand.logoUrl"
              :src="brand.logoUrl"
              alt="Logo"
              class="w-10 h-10 object-contain"
            />
          </td>
          <td class="p-3 border-b text-center space-x-2">
            <button
              @click="viewDetail(brand)"
              class="text-blue-600 hover:underline"
            >
              Xem
            </button>
            <button
              @click="editBrand(brand)"
              class="text-yellow-600 hover:underline"
            >
              Sửa
            </button>
            <button
              @click="deleteBrand(brand)"
              class="text-red-600 hover:underline"
            >
              Xoá
            </button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>

    <BrandCreateModal
      v-if="showModal"
      :editItem="selectedBrand"
      @close="closeModal"
      @save="saveBrand"
    />
  </div>
</template>

<script setup>
import { ref } from "vue";
import BrandCreateModal from "./BrandCreateModal.vue";

const showModal = ref(false);
const selectedBrand = ref(null);

const brands = ref([
  {
    id: 1,
    name: "Nike",
    code: "NIKE",
    slug: "nike",
    description: "Thương hiệu thể thao nổi tiếng thế giới.",
    status: "ACTIVE",
    logoUrl: "https://upload.wikimedia.org/wikipedia/commons/a/a6/Logo_NIKE.svg",
  },
  {
    id: 2,
    name: "Adidas",
    code: "ADIDAS",
    slug: "adidas",
    description: "Thương hiệu giày thể thao hàng đầu từ Đức.",
    status: "ACTIVE",
    logoUrl: "https://upload.wikimedia.org/wikipedia/commons/2/20/Adidas_Logo.svg",
  },
  {
    id: 3,
    name: "Puma",
    code: "PUMA",
    slug: "puma",
    description: "Hãng thời trang thể thao với phong cách năng động.",
    status: "INACTIVE",
    logoUrl: "https://upload.wikimedia.org/wikipedia/commons/f/fd/Puma_Logo.svg",
  },
]);

const openCreateModal = () => {
  selectedBrand.value = null;
  showModal.value = true;
};
const closeModal = () => (showModal.value = false);

const saveBrand = (newBrand) => {
  if (newBrand.id) {
    const index = brands.value.findIndex((b) => b.id === newBrand.id);
    if (index !== -1) brands.value[index] = newBrand;
  } else {
    newBrand.id = brands.value.length + 1;
    brands.value.push(newBrand);
  }
  showModal.value = false;
};

const viewDetail = (brand) => {
  alert(`Chi tiết thương hiệu:\nTên: ${brand.name}\nMô tả: ${brand.description}`);
};

const editBrand = (brand) => {
  selectedBrand.value = { ...brand };
  showModal.value = true;
};

const deleteBrand = (brand) => {
  if (confirm(`Xoá thương hiệu "${brand.name}"?`)) {
    brands.value = brands.value.filter((b) => b.id !== brand.id);
  }
};
</script>
