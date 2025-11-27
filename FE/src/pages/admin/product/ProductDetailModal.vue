<template>
  <!-- Dùng size="lg" hoặc "xl" để đủ rộng cho giao diện Tabs -->
  <ModalCustom :show="true" @close="onClose" size="lg">
    <template #title>Thêm sản phẩm mới</template>

    <!-- Loading -->
    <div v-if="isLoadingDeps" class="p-12 text-center">
      <div class="animate-spin inline-block w-8 h-8 border-4 border-current border-t-transparent text-[#adc178] rounded-full mb-2"></div>
      <p class="text-gray-500 text-sm">Đang tải dữ liệu...</p>
    </div>

    <div v-else>
      <!-- TABS -->
      <div class="border-b border-gray-200 mb-4">
        <nav class="-mb-px flex space-x-6 px-1">
          <button 
            @click="activeTab = 'general'" 
            :class="[
              'whitespace-nowrap py-3 px-1 border-b-2 font-medium text-sm transition-colors',
              activeTab === 'general' 
                ? 'border-blue-600 text-blue-600' 
                : 'border-transparent text-gray-500 hover:text-gray-700'
            ]"
          >
            Thông tin chung
          </button>
          <button 
            @click="activeTab = 'detail'" 
            :class="[
              'whitespace-nowrap py-3 px-1 border-b-2 font-medium text-sm transition-colors',
              activeTab === 'detail' 
                ? 'border-blue-600 text-blue-600' 
                : 'border-transparent text-gray-500 hover:text-gray-700'
            ]"
          >
            Chi tiết & Thông số
          </button>
        </nav>
      </div>

      <!-- CONTAINER SCROLLABLE -->
      <div class="p-1 max-h-[65vh] overflow-y-auto custom-scrollbar">
        
        <!-- TAB 1: THÔNG TIN CHUNG -->
        <div v-show="activeTab === 'general'" class="grid grid-cols-1 md:grid-cols-3 gap-4">
          <!-- Dòng 1 -->
          <InputCustom v-model.trim="form.name" label="Tên sản phẩm" required :disabled="loading" />
          <InputCustom v-model.trim="form.sku" label="SKU" required :disabled="loading" />
          <InputCustom v-model="form.slug" label="Slug (Tự động)" disabled class="bg-gray-50 text-gray-500" />

          <!-- Dòng 2 -->
          <InputCustom v-model.number="form.price" label="Giá (VND)" type="number" min="0" required :disabled="loading" />
          <InputCustom v-model.number="form.stockQuantity" label="Tồn kho" type="number" min="0" required :disabled="loading" />
          <SelectCustom v-model="form.unit" label="Đơn vị" required :disabled="loading">
            <option v-for="unit in productUnits" :key="unit" :value="unit">{{ unit }}</option>
          </SelectCustom>

          <!-- Dòng 3 -->
          <SelectCustom v-model="form.brandId" label="Thương hiệu" required :disabled="loading">
             <option value="">-- Chọn thương hiệu --</option>
             <option v-for="brand in brandStore.brands" :key="brand.id" :value="brand.id">{{ brand.name }}</option>
          </SelectCustom>

          <SelectCustom v-model="form.categoryId" label="Danh mục" required :disabled="loading">
             <option value="">-- Chọn danh mục --</option>
             <option v-for="cat in categoryStore.categories" :key="cat.id" :value="cat.id">{{ cat.name }}</option>
          </SelectCustom>
          
          <SelectCustom v-model="form.status" label="Trạng thái" required :disabled="loading">
            <option value="ACTIVE">Đang bán</option>
            <option value="INACTIVE">Ngừng bán</option>
          </SelectCustom>
          
          <!-- Dòng 4 -->
          <div class="col-span-1 md:col-span-3">
            <TextareaCustom v-model.trim="form.shortDescription" label="Mô tả ngắn" :disabled="loading" rows="2" />
          </div>
        </div>

        <!-- TAB 2: CHI TIẾT & THÔNG SỐ -->
        <div v-show="activeTab === 'detail'" class="space-y-5">
          <!-- Editor -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Mô tả chi tiết</label>
            <div class="h-64 mb-12">
               <QuillEditor 
                 v-model:content="detailForm.longDescription" 
                 contentType="html" 
                 theme="snow" 
                 toolbar="essential" 
               />
            </div>
          </div>

          <!-- Thông số kỹ thuật -->
          <div class="mt-6 border-t pt-4">
            <label class="block text-sm font-medium text-gray-700 mb-3">Thông số kỹ thuật</label>
            <div class="space-y-2 p-3 bg-gray-50 rounded border border-dashed border-gray-300">
              <div v-for="(item, index) in specList" :key="index" class="flex gap-2 items-center">
                <input v-model="item.key" placeholder="Tên thông số (VD: RAM)" class="flex-1 border rounded px-2 py-1.5 text-sm focus:ring-2 focus:ring-blue-500 outline-none" />
                <input v-model="item.value" placeholder="Giá trị (VD: 8GB)" class="flex-1 border rounded px-2 py-1.5 text-sm focus:ring-2 focus:ring-blue-500 outline-none" />
                <button @click="removeSpec(index)" class="text-red-500 hover:text-red-700 p-1" title="Xoá">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 20 20" fill="currentColor"><path fill-rule="evenodd" d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" clip-rule="evenodd" /></svg>
                </button>
              </div>
              <button @click="addSpec" class="mt-1 text-xs text-blue-600 hover:text-blue-800 font-medium flex items-center">
                + Thêm thông số
              </button>
            </div>
          </div>

          <!-- Packaging -->
          <div class="grid grid-cols-3 gap-3 border-t pt-4">
             <InputCustom v-model="detailForm.packaging" label="Quy cách" placeholder="VD: Hộp giấy" />
             <InputCustom v-model.number="detailForm.weight" label="KL (kg)" type="number" step="0.01" />
             <InputCustom v-model="detailForm.dimensions" label="Kích thước" placeholder="DxRxC" />
          </div>
        </div>
      </div>

      <!-- Alert Lỗi -->
      <div class="mt-4">
        <AlertCustom 
          v-if="validationError" 
          type="error" 
          :show="!!validationError" 
          @close="validationError = null"
        >
          {{ validationError }}
        </AlertCustom>
      </div>
    </div>

    <template #footer>
      <!-- Nút Huỷ -->
      <ButtonCustom color="cream" @click="onClose" :disabled="loading">
        Hủy bỏ
      </ButtonCustom>
      <!-- Nút Tạo -->
      <ButtonCustom 
        color="coffee" 
        @click="onSave" 
        :loading="loading" 
        :disabled="loading || isLoadingDeps"
      >
        Tạo sản phẩm
      </ButtonCustom>
    </template>
  </ModalCustom>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted, watch } from 'vue';
import { toSlug } from '@/utils/slug';
// Components UI
import ModalCustom from '@/components/custom/Modal/ModalCustom.vue';
import ButtonCustom from '@/components/custom/Button/ButtonDefault.vue';
import InputCustom from '@/components/custom/Input/InputCustom.vue';
import SelectCustom from '@/components/custom/Select/SelectCustom.vue';
import TextareaCustom from '@/components/custom/TextArea/TextAreaCustom.vue';
import AlertCustom from '@/components/custom/Alert/AlertCustom.vue';
// Editor
import { QuillEditor } from '@vueup/vue-quill';
import '@vueup/vue-quill/dist/vue-quill.snow.css';

// Stores
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
const activeTab = ref('general'); // Tab mặc định

const productUnits = [
  'Gram', 'Kilogram', 'Milliliter', 'Liter', 'Cai', 'Chiec', 'Bo', 'Goi',
  'Hop', 'Thung', 'Loc', 'Vien', 'Chai', 'Lo', 'Tuy', 'Tam', 'Cuon', 'Thang', 'Combo'
];

// Form Cơ bản
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

// Form Chi tiết (Mới thêm lại)
const detailForm = reactive({
  longDescription: '',
  packaging: '',
  weight: 0,
  dimensions: ''
});
const specList = ref<{key: string, value: string}[]>([]);

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

const addSpec = () => specList.value.push({ key: '', value: '' });
const removeSpec = (i: number) => specList.value.splice(i, 1);

const onSave = () => {
  validationError.value = null;
  
  // Validate
  if (!form.name || !form.sku || !form.brandId || !form.categoryId || !form.unit || !form.price) {
    validationError.value = 'Vui lòng điền đầy đủ các trường bắt buộc ở tab "Thông tin chung".';
    return;
  }
  
  // Chuẩn bị Specs JSON
  const specJson: Record<string, any> = {};
  specList.value.forEach(i => { if(i.key.trim()) specJson[i.key.trim()] = i.value.trim(); });

  const payload = {
    basic: { ...form },
    // Gửi kèm cả detailForm và specifications
    detail: { 
      ...detailForm,
      specification: specJson 
    }
  };
  delete (payload.basic as any).slug; // Backend tự tạo slug

  emit('saved', payload); // Gửi payload đầy đủ
};

const onClose = () => emit('close');
</script>

<style scoped>
.ql-editor { padding: 0; }
.custom-scrollbar::-webkit-scrollbar { width: 6px; }
.custom-scrollbar::-webkit-scrollbar-track { background: #f1f1f1; }
.custom-scrollbar::-webkit-scrollbar-thumb { background: #d1d5db; border-radius: 3px; }
.custom-scrollbar::-webkit-scrollbar-thumb:hover { background: #9ca3af; }
</style>