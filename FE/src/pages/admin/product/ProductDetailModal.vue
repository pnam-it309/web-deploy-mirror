<template>
  <ModalCustom :show="true" @close="onClose" size="xl">
    <template #title>
      <div class="flex items-center justify-between w-full">
        <div class="flex items-center gap-2">
          <span class="font-bold text-lg text-[#5a483e]">Chi tiết sản phẩm</span>
          <span v-if="form.name" class="text-gray-500 text-sm font-normal truncate max-w-[300px]">
             - {{ form.name }}
          </span>
        </div>
        <!-- Badge trạng thái -->
        <span v-if="!isEditing" class="ml-3 text-xs font-medium px-2.5 py-0.5 bg-gray-100 text-gray-600 rounded border border-gray-200">
          Chế độ xem
        </span>
        <span v-else class="ml-3 text-xs font-medium px-2.5 py-0.5 bg-blue-50 text-blue-600 rounded border border-blue-200">
          Đang chỉnh sửa
        </span>
      </div>
    </template>

    <!-- Header Actions -->
    <div class="flex justify-end mb-4 space-x-2 px-1">
      <ButtonCustom 
        v-if="!isEditing"
        @click="startEditing" 
        color="info"
        size="small"
        class="flex items-center"
      >
        <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1.5" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15.232 5.232l3.536 3.536m-2.036-5.036a2.5 2.5 0 113.536 3.536L6.5 21.036H3v-3.572L16.732 3.732z" /></svg>
        Chỉnh sửa
      </ButtonCustom>
      
      <ButtonCustom 
        v-else
        @click="cancelEditing" 
        color="secondary"
        size="small"
        class="flex items-center"
      >
        <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1.5" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" /></svg>
        Huỷ bỏ
      </ButtonCustom>
    </div>

    <!-- Alert Lỗi -->
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

    <!-- TABS -->
    <div class="border-b border-gray-200 mb-0">
      <nav class="-mb-px flex space-x-6 px-1">
        <button @click="activeTab = 'general'" :class="getTabClass('general')">Thông tin chung</button>
        <button @click="activeTab = 'detail'" :class="getTabClass('detail')">Chi tiết & Thông số</button>
      </nav>
    </div>

    <!-- CONTAINER CHÍNH -->
    <div class="p-1 max-h-[65vh] overflow-y-auto custom-scrollbar">
      
      <div v-if="isLoadingData" class="p-12 text-center">
        <div class="animate-spin inline-block w-8 h-8 border-4 border-current border-t-transparent text-[#adc178] rounded-full"></div>
        <p class="mt-2 text-sm text-gray-500">Đang tải dữ liệu...</p>
      </div>

      <div v-else class="mt-4">
        <fieldset :disabled="!isEditing" class="contents">
          
          <!-- TAB 1: GENERAL -->
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

          <!-- TAB 2: DETAILS -->
          <div v-show="activeTab === 'detail'" class="space-y-5">
            
            <!-- Quill Editor -->
            <div>
              <label class="block text-sm font-medium text-[#5a483e] mb-2">Mô tả chi tiết</label>
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

            <!-- Thông số kỹ thuật -->
            <div class="mt-6 border-t border-[#e6dfc0] pt-4">
              <label class="block text-sm font-medium text-[#5a483e] mb-3">Thông số kỹ thuật</label>
              
              <!-- View Mode -->
              <div v-if="!isEditing" class="bg-gray-50 rounded border border-gray-200 overflow-hidden">
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

              <!-- Edit Mode -->
              <div v-else class="space-y-2 p-3 bg-gray-50 rounded border border-dashed border-gray-300">
                <div v-for="(item, index) in specList" :key="index" class="flex gap-2 items-center">
                  <div class="flex-1">
                    <InputCustom 
                      v-model="item.key" 
                      placeholder="Tên thông số (VD: RAM)" 
                      class="text-sm"
                    />
                  </div>
                  <div class="flex-1">
                    <InputCustom 
                      v-model="item.value" 
                      placeholder="Giá trị (VD: 8GB)" 
                      class="text-sm"
                    />
                  </div>
                  
                  <button @click="removeSpec(index)" class="text-red-500 hover:text-red-700 p-1 pointer-events-auto transition-colors" title="Xoá">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor"><path fill-rule="evenodd" d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" clip-rule="evenodd" /></svg>
                  </button>
                </div>

                <button @click="addSpec" class="mt-1 text-xs text-blue-600 hover:text-blue-800 font-medium flex items-center pointer-events-auto transition-colors px-2 py-1 rounded hover:bg-blue-50">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" viewBox="0 0 20 20" fill="currentColor"><path fill-rule="evenodd" d="M10 5a1 1 0 011 1v3h3a1 1 0 110 2h-3v3a1 1 0 11-2 0v-3H6a1 1 0 110-2h3V6a1 1 0 011-1z" clip-rule="evenodd" /></svg>
                  Thêm thông số
                </button>
              </div>
            </div>

            <!-- Packaging -->
            <div class="grid grid-cols-3 gap-3 border-t border-[#e6dfc0] pt-4">
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
        <ButtonCustom color="cream" @click="cancelEditing">Huỷ bỏ</ButtonCustom>
        <ButtonCustom color="coffee" @click="onSave" :loading="loading">Lưu thay đổi</ButtonCustom>
      </div>
      <div v-else>
        <ButtonCustom color="secondary" @click="onClose">Đóng</ButtonCustom>
      </div>
    </template>
  </ModalCustom>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted, watch } from 'vue';
import { toSlug } from '@/utils/slug';
import { ProductService } from '@/services/axios/product.services'; // Direct service call
import { useProductStore } from '@/stores/product.store';
import { useBrandStore } from '@/stores/brand.store';
import { useCategoryStore } from '@/stores/category.store';

// Components
import ModalCustom from '@/components/custom/Modal/ModalCustom.vue';
import ButtonCustom from '@/components/custom/Button/ButtonDefault.vue';
import InputCustom from '@/components/custom/Input/InputCustom.vue';
import SelectCustom from '@/components/custom/Select/SelectCustom.vue';
import TextareaCustom from '@/components/custom/TextArea/TextAreaCustom.vue';
import AlertCustom from '@/components/custom/Alert/AlertCustom.vue';
import { QuillEditor } from '@vueup/vue-quill';
import '@vueup/vue-quill/dist/vue-quill.snow.css';

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
const isLoadingData = ref(true); // Mặc định là true khi mới mở
const validationError = ref<string | null>(null);

const productUnits = ['Gram', 'Kilogram', 'Milliliter', 'Liter', 'Cai', 'Chiec', 'Bo', 'Goi', 'Hop', 'Thung', 'Loc', 'Vien', 'Chai', 'Lo', 'Tuy', 'Tam', 'Cuon', 'Thang', 'Combo'];

let originalBasicData: any = null;
let originalDetailData: any = null;

// Form Basic
const form = reactive({
  id: '', sku: '', name: '', slug: '', shortDescription: '', price: 0, stockQuantity: 0,
  brandId: '', categoryId: '', unit: '', status: 'ACTIVE'
});

// Form Detail
const detailForm = reactive({
  longDescription: '', packaging: '', weight: 0, dimensions: ''
});
const specList = ref<{key: string, value: string}[]>([]);

// --- LOGIC LOAD DỮ LIỆU CHẮC CHẮN ---
onMounted(async () => {
  isLoadingData.value = true;
  try {
    // 1. Load danh mục & thương hiệu
    await productStore.fetchDependencies();

    // 2. Load thông tin cơ bản (Basic)
    // Thử tìm trong store trước
    let productBasic = productStore.products.find(p => p.id === props.productId);
    
    // Nếu không có trong store (ví dụ reload trang), gọi API lấy trực tiếp
    if (!productBasic) {
       const res = await ProductService.getProductById(props.productId); // Cần đảm bảo service có hàm này
       productBasic = res.data;
    }

    if (productBasic) {
      // Map dữ liệu vào form
      Object.assign(form, {
        ...productBasic,
        brandId: productBasic.brandId || '',
        categoryId: productBasic.categoryId || '',
        unit: productBasic.unit || '',
      });
    }

    // 3. Load thông tin chi tiết (Detail)
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
    
    // Lưu bản gốc để restore khi huỷ
    saveSnapshot();

  } catch (e) {
    console.error(e);
    validationError.value = "Lỗi tải dữ liệu sản phẩm.";
  } finally {
    isLoadingData.value = false;
  }
});

const startEditing = () => { isEditing.value = true; };

const cancelEditing = () => {
  // Restore data cũ từ snapshot
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

// Tự động tạo slug khi sửa tên (chỉ khi đang edit)
watch(() => form.name, (n) => { if(isEditing.value) form.slug = toSlug(n); });

const addSpec = () => specList.value.push({ key: '', value: '' });
const removeSpec = (i: number) => specList.value.splice(i, 1);

const getTabClass = (tab: string) => {
  return activeTab.value === tab
    ? 'border-b-2 border-[#adc178] text-[#386641] font-bold px-1 pb-2 transition-colors'
    : 'border-b-2 border-transparent text-gray-500 hover:text-gray-700 px-1 pb-2 transition-colors';
};

const onSave = () => {
  validationError.value = null;
  
  if (!form.name || !form.price || !form.brandId || !form.categoryId) {
    validationError.value = "Vui lòng điền đầy đủ thông tin chung.";
    return;
  }

  const specJson: Record<string, any> = {};
  specList.value.forEach(i => { if(i.key.trim()) specJson[i.key.trim()] = i.value.trim(); });

  const payload = {
    basic: { ...form },
    detail: { 
      ...detailForm,
      specification: specJson 
    }
  };
  delete (payload.basic as any).slug; // Backend tự tạo slug
  
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