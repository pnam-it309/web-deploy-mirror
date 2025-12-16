<template>
  <div class="p-6 relative min-h-screen bg-[#f8f9fa] dark:bg-brand-dark-200 transition-colors duration-300">
    
    <div class="mb-5"><ProductFilter :brands="brandStore.brands" :categories="categoryStore.categories" @filter="handleFilterChange" /></div>
<ButtonCustom color="cream-soft" size="default" @click="showImportPanel = !showImportPanel" class="!py-2">
          <span class="mr-2">📥</span> Import Excel
        </ButtonCustom>
        <ButtonCustom color="coffee" size="default" @click="openCreateModal" class="!py-2 ml-2">
          + Thêm sản phẩm
        </ButtonCustom>
    <!-- TABLE -->
    <TableCustom :data="products" :columns="columns" :loading="isLoading" :total="products.length" :pageSize="10">
      <!-- ... (Các cột khác giữ nguyên) ... -->
      
      <!-- CỘT GIÁ -->
      <template #price="{ record }">
         <span class="font-medium text-[#5a483e] dark:text-brand-sage">
            {{ record.price ? record.price.toLocaleString() : '0' }} ₫
         </span>
      </template>

      <!-- SỬA CỘT TRẠNG THÁI -->
      <template #status="{ record }">
        <!-- Ưu tiên 1: Hết hàng (Stock = 0) -->
        <span v-if="record.stockQuantity === 0" 
              class="px-2.5 py-1 rounded-full text-xs font-medium border bg-gray-100 text-gray-600 border-gray-200 dark:bg-white/10 dark:text-gray-400 dark:border-white/20">
           Hết hàng
        </span>
        
        <!-- Ưu tiên 2: Sắp hết (0 < Stock <= 10) -->
        <span v-else-if="record.stockQuantity <= 10" 
              class="px-2.5 py-1 rounded-full text-xs font-medium border bg-red-50 text-red-700 border-red-100 dark:bg-red-900/30 dark:text-red-300 dark:border-red-800">
           Sắp hết ({{ record.stockQuantity }})
        </span>

        <!-- Ưu tiên 3: Theo trạng thái Active/Inactive -->
        <span v-else
          :class="['px-2.5 py-1 rounded-full text-xs font-medium border', 
            record.status === 'ACTIVE' 
              ? 'bg-[#dde5b6]/40 text-[#386641] border-[#adc178]/30 dark:bg-brand-sage/20 dark:text-brand-sage dark:border-brand-sage/30' 
              : 'bg-red-50 text-red-700 border-red-100 dark:bg-red-900/30 dark:text-red-300 dark:border-red-800']"
        >
          {{ record.status === 'ACTIVE' ? 'Đang bán' : 'Ngừng bán' }}
        </span>
      </template>

      <!-- ... (Actions giữ nguyên) ... -->
       <template #actions="{ record }">
        <div class="flex justify-center space-x-2">
          <ButtonCustom color="sage-soft" size="small" @click.stop="openDetailModal(record)">Chi tiết</ButtonCustom>
          <ButtonCustom color="mocha-soft" size="small" @click.stop="askDeleteProduct(record)">Xoá</ButtonCustom>
        </div>
      </template>
    </TableCustom>

    <!-- ... (Modals giữ nguyên) ... -->
    <ProductCreateModal v-if="showCreateModal" :loading="isLoading" @close="closeModal" @saved="handleCreate" />
    <ProductDetailModal v-if="showDetailModal && selectedProductId" :productId="selectedProductId" :loading="isLoading" @close="closeDetailModal" @saved="handleUpdate" />
    <ModalCustom :show="showDeleteConfirm" @close="cancelDelete" size="sm">
      <template #title>Xác nhận xoá</template>
      <template #default><p>Bạn có chắc muốn xoá sản phẩm <strong>{{ productToDelete?.name }}</strong>?</p></template>
      <template #footer>
        <ButtonCustom color="cream" @click="cancelDelete">Huỷ</ButtonCustom>
        <ButtonCustom color="mocha" :loading="isLoading" @click="confirmDelete">Xác nhận xoá</ButtonCustom>
      </template>
    </ModalCustom>
    <ProductImportPanel :show="showImportPanel" @close="showImportPanel = false" />
    <ToastCustom :show="toast.show" :message="toast.message" :type="toast.type" />
  </div>
</template>

<script setup lang="ts">
// ... (Script giữ nguyên) ...
import { ref, onMounted } from 'vue';
import { storeToRefs } from 'pinia';
import { useProductStore } from '@/stores/product.store';
import { useBrandStore } from '@/stores/brand.store'; 
import { useCategoryStore } from '@/stores/category.store'; 
import ProductCreateModal from './ProductCreateModal.vue';
import ProductDetailModal from './ProductDetailModal.vue';
import ProductFilter from './ProductFilter.vue';
import ProductImportPanel from './ProductImportPanel.vue';
import ButtonCustom from '@/components/custom/Button/ButtonDefault.vue';
import ModalCustom from '@/components/custom/Modal/ModalCustom.vue';
import ToastCustom from '@/components/custom/Toast/ToastCustom.vue';
import TableCustom from '@/components/custom/Table/TableCustom.vue';

const productStore = useProductStore();
const brandStore = useBrandStore(); 
const categoryStore = useCategoryStore();
const { products, isLoading, error } = storeToRefs(productStore);

const columns = [
  { title: 'SKU', key: 'sku', width: '120px' },
  { title: 'Tên sản phẩm', key: 'name' },
  { title: 'Thương hiệu', key: 'brandName' },
  { title: 'Danh mục', key: 'categoryName' },
  { title: 'Giá', key: 'price', align: 'right' },
  { title: 'Trạng thái', key: 'status', align: 'center', width: '150px' }, // Tăng width cho status
  { title: 'Hành động', key: 'actions', align: 'center', width: '180px' },
];

const toast = ref({ show: false, message: '', type: 'success' });
const showToast = (message: string, type: 'success' | 'error' = 'success') => {
  toast.value = { show: true, message, type };
  setTimeout(() => (toast.value.show = false), 3000);
};

onMounted(async () => {
  await Promise.all([brandStore.fetchBrands(), categoryStore.fetchCategories()]);
  await productStore.fetchProducts();
});

// ... (Các hàm handler giữ nguyên) ...
const showCreateModal = ref(false);
const showDetailModal = ref(false);
const showImportPanel = ref(false); 
const selectedProductId = ref<string | null>(null);
const selectedProduct = ref<any>(null);
const showDeleteConfirm = ref(false);
const productToDelete = ref<any>(null);

const openCreateModal = () => { productStore.error = null; showCreateModal.value = true; };
const openDetailModal = (product: any) => { productStore.error = null; selectedProductId.value = product.id; showDetailModal.value = true; }
const closeModal = () => { showCreateModal.value = false; }
const closeDetailModal = () => { showDetailModal.value = false; selectedProductId.value = null; }

const handleFilterChange = (filterParams: any) => { productStore.fetchProducts(filterParams); };

const handleCreate = async (payload: any) => {
  try {
    await (productStore.createProduct(payload) as any);
    showCreateModal.value = false;
    showToast('✅ Thêm sản phẩm thành công!');
    await productStore.fetchProducts();
  } catch (err) { showToast('❌ Lỗi khi thêm sản phẩm.', 'error'); }
};

const handleUpdate = async (payload: any) => {
  const { basic, detail } = payload;
  try {
    await productStore.updateProduct(basic.id, basic);
    if (detail) await productStore.saveProductDetail(basic.id, detail);
    showToast('✅ Cập nhật thành công!');
    closeDetailModal();
    await productStore.fetchProducts();
  } catch (err) { showToast('❌ Lỗi cập nhật.', 'error'); }
};

const askDeleteProduct = (product: any) => { productToDelete.value = product; showDeleteConfirm.value = true; };
const cancelDelete = () => { showDeleteConfirm.value = false; productToDelete.value = null; };
const confirmDelete = async () => {
  if (productToDelete.value) {
    try {
      await productStore.deleteProduct(productToDelete.value.id);
      showToast('🗑️ Xoá thành công!');
      await productStore.fetchProducts();
    } catch (e) { showToast('❌ Lỗi xoá.', 'error'); }
    cancelDelete();
  }
};
</script>