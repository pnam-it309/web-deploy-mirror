<template>
  <div class="p-6 relative min-h-screen bg-[#f8f9fa] dark:bg-brand-dark-200 transition-colors duration-300">
    <!-- FILTER -->
    <div class="mb-5">
       <ProductFilter 
         :brands="brandStore.brands"
         :categories="categoryStore.categories"
         @filter="handleFilterChange"
       />
    </div>

    <!-- ERROR -->
    <div v-if="error" class="bg-red-50 dark:bg-red-900/20 border border-red-200 dark:border-red-800 text-red-700 dark:text-red-300 px-4 py-3 rounded-lg relative mb-5 flex items-center shadow-sm">
      <span class="font-bold mr-1">Lỗi!</span> {{ error }}
    </div>
    <ButtonCustom 
          color="cream-soft" 
          size="default"
          @click="showImportPanel = !showImportPanel"
          class="!py-2 mr-2"
        >
        📥Import Excel
        </ButtonCustom>
        
        <ButtonCustom 
          color="coffee" 
          size="default"
          @click="openCreateModal"
          class="!py-2"
        >
        + Thêm sản phẩm
        </ButtonCustom>
    <!-- TABLE CUSTOM -->
    <TableCustom
      :data="products"
      :columns="columns"
      :loading="isLoading"
      :total="products.length" 
      :pageSize="10"
    >
      <!-- 1. SKU -->
      <template #sku="{ record }">
        <span 
          class="font-mono text-sm text-blue-600 dark:text-blue-400 font-medium cursor-pointer hover:underline"
          @click="openDetailModal(record)"
        >
          {{ record.sku }}
        </span>
      </template>

      <!-- 2. Tên Sản Phẩm -->
      <template #name="{ record }">
        <div 
          class="font-semibold text-[#6c584c] dark:text-brand-cream hover:text-[#a98467] dark:hover:text-brand-sage transition-colors cursor-pointer"
          @click="openDetailModal(record)"
        >
          {{ record.name }}
        </div>
      </template>

      <!-- 3. Thương hiệu -->
      <template #brandName="{ record }">
        <span class="text-gray-600 dark:text-gray-400">{{ record.brandName || '—' }}</span>
      </template>

      <!-- 4. Danh mục -->
      <template #categoryName="{ record }">
        <span class="text-gray-600 dark:text-gray-400">{{ record.categoryName || '—' }}</span>
      </template>

      <!-- 5. Giá -->
      <template #price="{ record }">
        <span class="font-medium text-[#5a483e] dark:text-brand-sage">
          {{ record.price ? record.price.toLocaleString() : '0' }} ₫
        </span>
      </template>

      <!-- 6. Trạng thái -->
      <template #status="{ record }">
        <span :class="[' py-1 rounded-full text-xs font-medium border', record.status === 'ACTIVE' ? 'bg-[#dde5b6]/40 text-[#386641] border-[#adc178]/30 dark:bg-brand-sage/20 dark:text-brand-sage dark:border-brand-sage/30' : 'bg-red-50 text-red-700 border-red-100 dark:bg-red-900/30 dark:text-red-300 dark:border-red-800']">
          {{ record.status === 'ACTIVE' ? 'Đang bán' : 'Ngừng bán' }}
        </span>
      </template>

      <!-- 7. Hành động -->
      <template #actions="{ record }">
        <div class="flex justify-center space-x-2">
          <ButtonCustom color="sage-soft" size="small" @click.stop="openDetailModal(record)">
            Chi tiết
          </ButtonCustom>
          <ButtonCustom color="mocha-soft" size="small" @click.stop="askDeleteProduct(record)">
            Xoá
          </ButtonCustom>
        </div>
      </template>
    </TableCustom>

    <!-- MODALS (Giữ nguyên logic) -->
    <ProductCreateModal v-if="showCreateModal" :loading="isLoading" @close="closeModal" @saved="handleCreate" />
    <ProductDetailModal v-if="showDetailModal && selectedProductId" :productId="selectedProductId" :loading="isLoading" @close="closeDetailModal" @saved="handleUpdate" />
    
    <ModalCustom :show="showDeleteConfirm" @close="cancelDelete" size="sm">
      <template #title>Xác nhận xoá</template>
      <template #default>
        <p class="text-gray-700 dark:text-gray-300">Bạn có chắc chắn muốn xoá sản phẩm <strong class="text-[#6c584c] dark:text-brand-cream">{{ productToDelete?.name }}</strong>?</p>
        <p class="text-sm text-red-500 dark:text-red-400 mt-3 bg-red-50 dark:bg-red-900/20 p-3 rounded-md border border-red-100 dark:border-red-800/50">⚠️ Hành động này sẽ chuyển trạng thái sang <strong>Ngừng bán</strong>.</p>
      </template>
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
import TableCustom from '@/components/custom/Table/TableCustom.vue'; // Import TableCustom

const productStore = useProductStore();
const brandStore = useBrandStore(); 
const categoryStore = useCategoryStore();
const { products, isLoading, error } = storeToRefs(productStore);

// ĐỊNH NGHĨA CỘT CHO TABLE
const columns = [
  { title: 'SKU', key: 'sku', width: '120px' },
  { title: 'Tên sản phẩm', key: 'name' },
  { title: 'Thương hiệu', key: 'brandName' },
  { title: 'Danh mục', key: 'categoryName' },
  { title: 'Giá', key: 'price', align: 'right' },
  { title: 'Trạng thái', key: 'status', align: 'center', width: '120px' },
  { title: 'Hành động', key: 'actions', align: 'center', width: '180px' },
];

// Toast Logic
const toast = ref({ show: false, message: '', type: 'success' as 'success' | 'error' });
const showToast = (message: string, type: 'success' | 'error' = 'success') => {
  toast.value = { show: true, message, type };
  setTimeout(() => (toast.value.show = false), 3000);
};

onMounted(async () => {
  try {
    await Promise.all([
      brandStore.fetchBrands(),
      categoryStore.fetchCategories()
    ]);
    await productStore.fetchProducts();
  } catch (e) {
    console.error("Lỗi tải dữ liệu ban đầu:", e);
  }
});

// --- Modal & Actions Logic (Giữ nguyên như cũ) ---
const showCreateModal = ref(false);
const showDetailModal = ref(false);
const showImportPanel = ref(false); 
const selectedProductId = ref<string | null>(null);
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
  } catch (err) {
    showToast('❌ Lỗi khi thêm sản phẩm.', 'error');
  }
};

const handleUpdate = async (payload: any) => {
  const { basic, detail } = payload;
  try {
    await productStore.updateProduct(basic.id, basic);
    if (detail) await productStore.saveProductDetail(basic.id, detail);
    showToast('✅ Cập nhật sản phẩm thành công!');
    closeDetailModal();
    await productStore.fetchProducts();
  } catch (err) {
    showToast('❌ Lỗi khi cập nhật sản phẩm.', 'error');
  }
};

const askDeleteProduct = (product: any) => { productToDelete.value = product; showDeleteConfirm.value = true; };
const cancelDelete = () => { showDeleteConfirm.value = false; productToDelete.value = null; };
const confirmDelete = async () => {
  if (productToDelete.value) {
    try {
      await productStore.deleteProduct(productToDelete.value.id);
      showToast('🗑️ Xoá sản phẩm thành công!');
      await productStore.fetchProducts();
    } catch (e) {
      showToast('❌ Lỗi khi xoá sản phẩm.', 'error');
    }
    cancelDelete();
  }
};
</script>