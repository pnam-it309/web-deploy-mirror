<template>
  <ModalCustom :show="true" @close="onClose">
    <template #title>Thêm sản phẩm mới</template>

    <!-- Hiển thị loading dependencies -->
    <div v-if="isLoadingDeps" class="p-6 text-center">
      <div class="animate-spin inline-block w-8 h-8 border-4 border-current border-t-transparent text-blue-600 rounded-full" role="status"></div>
      <p class="mt-2">Đang tải dữ liệu...</p>
    </div>

    <div v-else class="grid grid-cols-2 gap-4">
      <!-- Tên sản phẩm -->
      <InputCustom
        v-model.trim="form.name"
        label="Tên sản phẩm"
        required
      />
      
      <!-- SKU -->
      <InputCustom
        v-model.trim="form.sku"
        label="SKU (Mã sản phẩm)"
        required
      />
      
      <!-- Slug (Tự động) -->
      <div class="col-span-2">
        <InputCustom
          v-model="form.slug"
          label="Slug (Tự động tạo)"
          disabled
          class="bg-gray-100"
        />
      </div>

      <!-- Giá -->
      <InputCustom
        v-model.number="form.price"
        label="Giá (VND)"
        type="number"
        min="0"
        required
      />
      
      <!-- Tồn kho -->
      <InputCustom
        v-model.number="form.stockQuantity"
        label="Số lượng tồn kho"
        type="number"
        min="0"
        required
      />

      <!-- Dropdowns -->
      <SelectCustom v-model="form.brandId" label="Thương hiệu" required>
         <option value="">-- Chọn thương hiệu --</option>
         <option v-for="brand in brandStore.brands" :key="brand.id" :value="brand.id">{{ brand.name }}</option>
      </SelectCustom>

      <SelectCustom v-model="form.categoryId" label="Danh mục" required>
         <option value="">-- Chọn danh mục --</option>
         <option v-for="cat in categoryStore.categories" :key="cat.id" :value="cat.id">{{ cat.name }}</option>
      </SelectCustom>
      
      <SelectCustom v-model="form.unit" label="Đơn vị" required>
        <option value="">-- Chọn đơn vị --</option>
        <option v-for="unit in productUnits" :key="unit" :value="unit">{{ unit }}</option>
      </SelectCustom>
      
      <SelectCustom v-model="form.status" label="Trạng thái" required>
        <option value="ACTIVE">Đang bán</option>
        <option value="INACTIVE">Ngừng bán</option>
      </SelectCustom>
      
      <div class="col-span-2">
        <TextareaCustom v-model.trim="form.shortDescription" label="Mô tả ngắn" />
      </div>
    </div>

    <div v-if="validationError" class="mt-4 bg-red-50 text-red-700 p-3 rounded text-sm">
      {{ validationError }}
    </div>

    <template #footer>
      <ButtonCustom color="secondary" @click="onClose" :disabled="loading"> Hủy </ButtonCustom>
      <ButtonCustom color="primary" @click="onSave" :loading="loading" :disabled="loading || isLoadingDeps">
        Tạo sản phẩm
      </ButtonCustom>
    </template>
  </ModalCustom>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted, watch } from 'vue';
import { toSlug } from '@/utils/slug';
import ModalCustom from '@/components/custom/Modal/ModalCustom.vue';
import ButtonCustom from '@/components/custom/Button/ButtonDefault.vue';
import InputCustom from '@/components/custom/Input/InputCustom.vue';
import SelectCustom from '@/components/custom/Select/SelectCustom.vue';
import TextareaCustom from '@/components/custom/TextArea/TextAreaCustom.vue';

import { useProductStore } from '@/stores/product.store';
import { useBrandStore } from '@/stores/brand.store';
import { useCategoryStore } from '@/stores/category.store';

const props = defineProps({
  loading: Boolean,
});
const emit = defineEmits(['close', 'saved']);

const productStore = useProductStore();
const brandStore = useBrandStore();
const categoryStore = useCategoryStore();

const isLoadingDeps = ref(false);
const validationError = ref<string | null>(null);

const productUnits = [
  'Gram', 'Kilogram', 'Milliliter', 'Liter', 'Cai', 'Chiec', 'Bo', 'Goi',
  'Hop', 'Thung', 'Loc', 'Vien', 'Chai', 'Lo', 'Tuy', 'Tam', 'Cuon', 'Thang', 'Combo'
];

const form = reactive({
  sku: '',
  name: '',
  slug: '',
  shortDescription: '',
  price: 0,
  stockQuantity: 0,
  brandId: '',
  categoryId: '',
  unit: '',
  status: 'ACTIVE',
});

onMounted(async () => {
  isLoadingDeps.value = true;
  try {
    await productStore.fetchDependencies();
  } catch (e) {
    validationError.value = "Lỗi không thể tải danh sách Brand/Category.";
  } finally {
    isLoadingDeps.value = false;
  }
});

watch(() => form.name, (newName) => {
  form.slug = toSlug(newName);
});

const onSave = () => {
  validationError.value = null;
  if (!form.name || !form.sku || !form.brandId || !form.categoryId || !form.unit || !form.price) {
    validationError.value = 'Vui lòng điền đầy đủ các trường bắt buộc.';
    return;
  }
  
  const payload = { ...form };
  delete (payload as any).slug;

  emit('saved', payload);
};

const onClose = () => emit('close');
</script>