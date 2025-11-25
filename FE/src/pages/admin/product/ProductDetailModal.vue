<template>
  <ModalCustom :show="true" @close="onClose" size="lg">
    <template #title>
      <div class="flex items-center justify-between w-full">
        <div class="flex items-center gap-2">
          <span class="font-bold text-lg">Chi tiết sản phẩm</span>
          <span v-if="form.name" class="text-gray-500 text-sm font-normal truncate max-w-[300px]">
             - {{ form.name }}
          </span>
        </div>
        <span v-if="!isEditing" class="ml-3 text-xs font-medium px-2.5 py-0.5 bg-gray-100 text-gray-800 rounded border border-gray-200">
          Chế độ xem
        </span>
        <span v-else class="ml-3 text-xs font-medium px-2.5 py-0.5 bg-blue-100 text-blue-800 rounded border border-blue-200">
          Đang chỉnh sửa
        </span>
      </div>
    </template>

    <div class="flex justify-end mb-4 space-x-2 px-1">
      <button 
        v-if="!isEditing"
        @click="startEditing" 
        class="px-3 py-1.5 bg-blue-600 text-white rounded hover:bg-blue-700 flex items-center text-sm shadow-sm transition-all"
      >
        <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1.5" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15.232 5.232l3.536 3.536m-2.036-5.036a2.5 2.5 0 113.536 3.536L6.5 21.036H3v-3.572L16.732 3.732z" /></svg>
        Chỉnh sửa
      </button>
      <button 
        v-else
        @click="cancelEditing" 
        class="px-3 py-1.5 bg-white border border-gray-300 text-gray-700 rounded hover:bg-gray-50 text-sm flex items-center shadow-sm transition-all"
      >
        <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1.5" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" /></svg>
        Huỷ bỏ
      </button>
    </div>

    <!-- Sử dụng AlertCustom cho lỗi -->
    <div class="mb-4 px-1">
        <AlertCustom 
            v-if="validationError" 
            type="error" 
            :show="!!validationError" 
            @close="validationError = null"
        >
            {{ validationError }}
        </AlertCustom>
    </div>

    <div class="border-b border-gray-200 mb-0">
      <nav class="-mb-px flex space-x-6 px-1">
        <button @click="activeTab = 'general'" :class="getTabClass('general')">Thông tin chung</button>
        <button @click="activeTab = 'detail'" :class="getTabClass('detail')">Chi tiết & Thông số</button>
      </nav>
    </div>

    <div class="p-1 max-h-[65vh] overflow-y-auto custom-scrollbar">
      
      <div v-if="!isReady" class="p-12 text-center">
        <div class="animate-spin inline-block w-8 h-8 border-4 border-current border-t-transparent text-blue-600 rounded-full"></div>
        <p class="mt-2 text-sm text-gray-500">Đang tải dữ liệu...</p>
      </div>

      <div v-else class="mt-4">
        <fieldset :disabled="!isEditing" class="contents">
          
          <div v-show="activeTab === 'general'" class="grid grid-cols-1 md:grid-cols-3 gap-4">
            <InputCustom v-model.trim="form.name" label="Tên sản phẩm" :class="{ 'bg-gray-50': !isEditing }" />
            <InputCustom v-model.trim="form.sku" label="SKU" disabled class="bg-gray-100 text-gray-500 cursor-not-allowed" />
            <InputCustom v-model="form.slug" label="Slug" disabled class="bg-gray-100 text-gray-500 cursor-not-allowed" />

            <InputCustom v-model.number="form.price" label="Giá (VND)" type="number" :class="{ 'bg-gray-50': !isEditing }" />
            <InputCustom v-model.number="form.stockQuantity" label="Tồn kho" type="number" :class="{ 'bg-gray-50': !isEditing }" />
            
            <SelectCustom v-model="form.unit" label="Đơn vị" :class="{ 'bg-gray-50': !isEditing }">
              <option v-for="unit in productUnits" :key="unit" :value="unit">{{ unit }}</option>
            </SelectCustom>
            
            <SelectCustom v-model="form.brandId" label="Thương hiệu" :class="{ 'bg-gray-50': !isEditing }">
               <option v-for="brand in brandStore.brands" :key="brand.id" :value="brand.id">{{ brand.name }}</option>
            </SelectCustom>

            <SelectCustom v-model="form.categoryId" label="Danh mục" :class="{ 'bg-gray-50': !isEditing }">
               <option v-for="cat in categoryStore.categories" :key="cat.id" :value="cat.id">{{ cat.name }}</option>
            </SelectCustom>

            <SelectCustom v-model="form.status" label="Trạng thái" :class="{ 'bg-gray-50': !isEditing }">
              <option value="ACTIVE">Đang bán</option>
              <option value="INACTIVE">Ngừng bán</option>
            </SelectCustom>

            <div class="col-span-1 md:col-span-3">
              <TextareaCustom v-model.trim="form.shortDescription" label="Mô tả ngắn" :class="{ 'bg-gray-50': !isEditing }" rows="3" />
            </div>
          </div>

          <div v-show="activeTab === 'detail'" class="space-y-5">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Mô tả chi tiết</label>
              <div class="bg-white" :class="{'border rounded-md p-4 bg-gray-50 min-h-[100px]': !isEditing}">
                <div v-if="!isEditing">
                  <div v-if="!detailForm.longDescription" class="text-gray-500 italic text-sm">Chưa có mô tả chi tiết.</div>
                  <div v-else v-html="detailForm.longDescription" class="prose max-w-none ql-editor text-sm"></div>
                </div>
                <div v-else class="h-64 mb-12">
                   <QuillEditor v-model:content="detailForm.longDescription" contentType="html" theme="snow" toolbar="essential" />
                </div>
              </div>
            </div>

            <div class="mt-6 border-t pt-4">
              <label class="block text-sm font-medium text-gray-700 mb-3">Thông số kỹ thuật</label>
              <div v-if="!isEditing" class="bg-gray-50 rounded border overflow-hidden">
                 <table class="min-w-full divide-y divide-gray-200">
                   <tbody class="divide-y divide-gray-200">
                     <tr v-for="(item, index) in specList" :key="index">
                       <td class="px-3 py-2 font-medium text-xs text-gray-600 w-1/3 bg-gray-100 border-r">{{ item.key }}</td>
                       <td class="px-3 py-2 text-xs text-gray-800">{{ item.value }}</td>
                     </tr>
                     <tr v-if="specList.length === 0">
                       <td colspan="2" class="p-3 text-center italic text-gray-500 text-xs">Chưa cập nhật thông số</td>
                     </tr>
                   </tbody>
                 </table>
              </div>
              <div v-else class="space-y-2 p-3 bg-gray-50 rounded border border-dashed border-gray-300">
                <div v-for="(item, index) in specList" :key="index" class="flex gap-2 items-center">
                  <input v-model="item.key" placeholder="Tên thông số" class="flex-1 border rounded px-2 py-1.5 text-sm focus:ring-2 focus:ring-blue-500 outline-none" />
                  <input v-model="item.value" placeholder="Giá trị" class="flex-1 border rounded px-2 py-1.5 text-sm focus:ring-2 focus:ring-blue-500 outline-none" />
                  <button @click="removeSpec(index)" class="text-red-500 hover:text-red-700 p-1 pointer-events-auto">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 20 20" fill="currentColor"><path fill-rule="evenodd" d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" clip-rule="evenodd" /></svg>
                  </button>
                </div>
                <button @click="addSpec" class="mt-1 text-xs text-blue-600 hover:text-blue-800 font-medium flex items-center pointer-events-auto">
                  + Thêm thông số
                </button>
              </div>
            </div>

            <div class="grid grid-cols-3 gap-3 border-t pt-4">
               <InputCustom v-model="detailForm.packaging" label="Quy cách" :class="{ 'bg-gray-50': !isEditing }" placeholder="VD: Hộp giấy" />
               <InputCustom v-model.number="detailForm.weight" label="KL (kg)" type="number" step="0.01" :class="{ 'bg-gray-50': !isEditing }" />
               <InputCustom v-model="detailForm.dimensions" label="Kích thước" :class="{ 'bg-gray-50': !isEditing }" placeholder="DxRxC" />
            </div>
          </div>
        </fieldset>
      </div>
    </div>

    <template #footer>
      <div v-if="isEditing" class="flex justify-end space-x-3">
        <ButtonCustom color="secondary" @click="cancelEditing">Huỷ bỏ</ButtonCustom>
        <ButtonCustom color="primary" @click="onSave" :loading="loading">Lưu thay đổi</ButtonCustom>
      </div>
      <div v-else>
        <ButtonCustom color="secondary" @click="onClose">Đóng</ButtonCustom>
      </div>
    </template>
  </ModalCustom>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted, watch, nextTick } from 'vue';
import { toSlug } from '@/utils/slug';
import ModalCustom from '@/components/custom/Modal/ModalCustom.vue';
import ButtonCustom from '@/components/custom/Button/ButtonDefault.vue';
import InputCustom from '@/components/custom/Input/InputCustom.vue';
import SelectCustom from '@/components/custom/Select/SelectCustom.vue';
import TextareaCustom from '@/components/custom/TextArea/TextAreaCustom.vue';
import AlertCustom from '@/components/custom/Alert/AlertCustom.vue'; // Import mới
import { QuillEditor } from '@vueup/vue-quill';
import '@vueup/vue-quill/dist/vue-quill.snow.css';

import { useProductStore } from '@/stores/product.store';
import { useBrandStore } from '@/stores/brand.store';
import { useCategoryStore } from '@/stores/category.store';

const props = defineProps({
  productId: { type: String, required: true },
  loading: Boolean,
});
const emit = defineEmits(['close', 'saved']);

const productStore = useProductStore();
const brandStore = useBrandStore();
const categoryStore = useCategoryStore();

const activeTab = ref('general');
const isEditing = ref(false);
const isReady = ref(false);
const validationError = ref<string | null>(null); // Thêm validation

const productUnits = ['Gram', 'Kilogram', 'Milliliter', 'Liter', 'Cai', 'Chiec', 'Bo', 'Goi', 'Hop', 'Thung', 'Loc', 'Vien', 'Chai', 'Lo', 'Tuy', 'Tam', 'Cuon', 'Thang', 'Combo'];

let originalBasicData: any = null;
let originalDetailData: any = null;

const form = reactive({
  id: '', sku: '', name: '', slug: '', shortDescription: '', price: 0, stockQuantity: 0,
  brandId: '', categoryId: '', unit: '', status: 'ACTIVE'
});

const detailForm = reactive({
  longDescription: '', packaging: '', weight: 0, dimensions: ''
});
const specList = ref<{key: string, value: string}[]>([]);

onMounted(async () => {
  try {
    // Load Dependencies
    await productStore.fetchDependencies();
    
    // Load Basic
    const productBasic = productStore.products.find(p => p.id === props.productId);
    if (productBasic) {
      Object.assign(form, { ...productBasic });
    }
    
    // Load Detail
    await productStore.fetchProductDetail(props.productId);
    const detail = productStore.currentDetail;
    if (detail) {
      detailForm.longDescription = detail.longDescription || '';
      detailForm.packaging = detail.packaging || '';
      detailForm.weight = detail.weight || 0;
      detailForm.dimensions = detail.dimensions || '';
      if (detail.specification) {
        specList.value = Object.entries(detail.specification).map(([key, value]) => ({
          key, value: String(value)
        }));
      }
    }
    
    saveSnapshot();
    isReady.value = true; // Dữ liệu đã sẵn sàng, form sẽ hiển thị và select đúng

  } catch (e) {
    console.error(e);
    validationError.value = "Lỗi tải dữ liệu.";
    isReady.value = true;
  }
});

const startEditing = () => { isEditing.value = true; };

const cancelEditing = () => {
  if (originalBasicData) Object.assign(form, JSON.parse(originalBasicData));
  if (originalDetailData) {
    const d = JSON.parse(originalDetailData);
    detailForm.longDescription = d.longDescription;
    detailForm.packaging = d.packaging;
    detailForm.weight = d.weight;
    detailForm.dimensions = d.dimensions;
    specList.value = d.specList;
  }
  isEditing.value = false;
  validationError.value = null;
};

const saveSnapshot = () => {
  originalBasicData = JSON.stringify(form);
  originalDetailData = JSON.stringify({ ...detailForm, specList: specList.value });
};

watch(() => form.name, (n) => { if(isEditing.value) form.slug = toSlug(n); });

const addSpec = () => specList.value.push({ key: '', value: '' });
const removeSpec = (i: number) => specList.value.splice(i, 1);

const getTabClass = (tab: string) => {
  return activeTab.value === tab
    ? 'border-b-2 border-blue-600 text-blue-600 font-medium px-1 pb-2'
    : 'border-b-2 border-transparent text-gray-500 hover:text-gray-700 px-1 pb-2';
};

const onSave = () => {
  validationError.value = null;
  
  // Validation cơ bản
  if (!form.name || !form.price || !form.brandId || !form.categoryId) {
    validationError.value = "Vui lòng điền đầy đủ thông tin chung.";
    return;
  }

  const specJson: Record<string, any> = {};
  specList.value.forEach(i => { if(i.key.trim()) specJson[i.key.trim()] = i.value.trim(); });

  const payload = {
    basic: { ...form },
    detail: { ...detailForm, specification: specJson }
  };
  delete (payload.basic as any).slug;
  
  emit('saved', payload);
};

const onClose = () => emit('close');
</script>

<style scoped>
.ql-editor { padding: 0; }
fieldset[disabled] { pointer-events: none; }
.custom-scrollbar::-webkit-scrollbar { width: 6px; }
.custom-scrollbar::-webkit-scrollbar-track { background: #f1f1f1; }
.custom-scrollbar::-webkit-scrollbar-thumb { background: #d1d5db; border-radius: 3px; }
.custom-scrollbar::-webkit-scrollbar-thumb:hover { background: #9ca3af; }
</style> 