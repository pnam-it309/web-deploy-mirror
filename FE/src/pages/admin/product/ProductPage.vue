<template>
  <div class="p-6 relative">
    <div class="flex justify-between items-center mb-4">
      <h1 class="text-2xl font-semibold">Quản lý Sản phẩm</h1>
      <div class="flex items-center gap-1">
        <ButtonCustom color="soft-blue" @click="openCreateModal">
          + Thêm sản phẩm
        </ButtonCustom>
        <ButtonCustom color="soft-gray" @click="showImportPanel = !showImportPanel">
          📥 Import Sản phẩm
        </ButtonCustom>
      </div>
    </div>
      <ProductFilter 
      :brands="brandStore.brands"
      :categories="categoryStore.categories"
      @filter="handleFilterChange"
    />
    <div v-if="error" class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-4">
      <strong class="font-bold">Lỗi!</strong> <span class="block sm:inline"> {{ error }}</span>
    </div>

    <CardCustom>
      <div v-if="isLoading && !products.length" class="p-6 text-center">
        <div class="animate-spin inline-block w-6 h-6 border-2 border-current border-t-transparent text-blue-600 rounded-full mr-2"></div>
        <span>Đang tải dữ liệu...</span>
      </div>

      <table v-else class="w-full text-left border-collapse">
        <thead class="bg-gray-100">
          <tr>
            <th class="p-3 border-b font-semibold text-sm text-gray-600">SKU</th>
            <th class="p-3 border-b font-semibold text-sm text-gray-600">Tên sản phẩm</th>
            <th class="p-3 border-b font-semibold text-sm text-gray-600">Thương hiệu</th>
            <th class="p-3 border-b font-semibold text-sm text-gray-600">Danh mục</th>
            <th class="p-3 border-b font-semibold text-sm text-gray-600 text-right">Giá</th>
            <th class="p-3 border-b font-semibold text-sm text-gray-600 text-center">Trạng thái</th>
            <th class="p-3 border-b font-semibold text-sm text-gray-600 text-center">Hành động</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-gray-100">
          <tr v-for="product in products" :key="product.id" class="hover:bg-gray-50 transition-colors">
            <td class="p-3 font-mono text-sm text-blue-600 cursor-pointer" @click="openDetailModal(product)">
              {{ product.sku }}
            </td>
            <td class="p-3 font-medium text-gray-900 cursor-pointer hover:text-blue-600" @click="openDetailModal(product)">
              {{ product.name }}
            </td>
            <td class="p-3 text-gray-600">{{ product.brandName || '—' }}</td>
            <td class="p-3 text-gray-600">{{ product.categoryName || '—' }}</td>
            <td class="p-3 text-right font-medium text-gray-900">
              {{ product.price ? product.price.toLocaleString() : '0' }} ₫
            </td>
            <td class="p-3 text-center">
              <span :class="['px-2.5 py-0.5 rounded-full text-xs font-medium', product.status === 'ACTIVE' ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800']">
                {{ product.status === 'ACTIVE' ? 'Đang bán' : 'Ngừng bán' }}
              </span>
            </td>
            <td class="p-3 text-center space-x-2">
              <!-- SỬA: Nút Chi tiết giờ sẽ mở modal Detail -->
              <ButtonCustom color="info" size="small" @click.stop="openDetailModal(product)">
                Chi tiết
              </ButtonCustom>
              <ButtonCustom color="danger" size="small" @click.stop="askDeleteProduct(product)">
                Xoá
              </ButtonCustom>
            </td>
          </tr>
          <tr v-if="!products.length" class="bg-white">
            <td colspan="8" class="p-8 text-center text-gray-500 italic">
              Chưa có sản phẩm nào.
            </td>
          </tr>
        </tbody>
      </table>
    </CardCustom>

    <!-- Modals -->
    <ProductCreateModal
      v-if="showCreateModal"
      :loading="isLoading"
      @close="closeModal"
      @saved="handleCreate"
    />
    
    <ProductDetailModal
      v-if="showDetailModal && selectedProductId"
      :productId="selectedProductId"
      :loading="isLoading"
      @close="closeDetailModal"
      @saved="handleUpdate"
    />

    <!-- Confirm Delete Modal -->
    <ModalCustom :show="showDeleteConfirm" @close="cancelDelete" size="sm">
      <template #title>Xác nhận xoá</template>
      <template #default>
        <p>Bạn có chắc chắn muốn xoá sản phẩm <strong>{{ productToDelete?.name }}</strong>?</p>
        <p class="text-sm text-red-500 mt-2">Hành động này sẽ chuyển trạng thái sang Ngừng bán.</p>
      </template>
      <template #footer>
        <ButtonCustom color="secondary" @click="cancelDelete">Huỷ</ButtonCustom>
        <ButtonCustom color="danger" :loading="isLoading" @click="confirmDelete">Xoá</ButtonCustom>
      </template>
    </ModalCustom>
  <ProductImportPanel 
      :show="showImportPanel" 
      @close="showImportPanel = false"
    />
    <!-- SỬA: Thêm Toast Notification -->
    <ToastCustom :show="toast.show" :message="toast.message" :type="toast.type" />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useProductStore } from '@/stores/product.store';
import { storeToRefs } from 'pinia';
import { useCategoryStore } from '@/stores/category.store';
import { useBrandStore } from '@/stores/brand.store'; 
import ProductCreateModal from './ProductCreateModal.vue';
import ProductDetailModal from './ProductDetailModal.vue';
import ButtonCustom from '@/components/custom/Button/ButtonDefault.vue';
import CardCustom from '@/components/custom/Card/CardCustom.vue';
import ModalCustom from '@/components/custom/Modal/ModalCustom.vue';
import ToastCustom from '@/components/custom/Toast/ToastCustom.vue';
import ProductFilter from './ProductFilter.vue';
import ProductImportPanel from './ProductImportPanel.vue';

const productStore = useProductStore();
const brandStore = useBrandStore(); 
const categoryStore = useCategoryStore();
const { products, isLoading, error } = storeToRefs(productStore);

const toast = ref({ show: false, message: '', type: 'success' as 'success' | 'error' });
const showToast = (message: string, type: 'success' | 'error' = 'success') => {
  toast.value = { show: true, message, type };
  setTimeout(() => (toast.value.show = false), 3000); // Tự tắt sau 3s
};

onMounted(async () => {
  await Promise.all([
    brandStore.fetchBrands(),
    categoryStore.fetchCategories()
  ]);
  productStore.fetchProducts();
});

const showCreateModal = ref(false);
const showDetailModal = ref(false);
const showImportPanel = ref(false); 
const selectedProductId = ref<string | null>(null);
const selectedProduct = ref<any>(null);

const openCreateModal = () => {
  productStore.error = null;
  showCreateModal.value = true;
};

const openDetailModal = (product: any) => {
  productStore.error = null;
  selectedProductId.value = product.id;
  showDetailModal.value = true;
}

const closeModal = () => { showCreateModal.value = false; }
const closeDetailModal = () => { 
  showDetailModal.value = false; 
  selectedProductId.value = null; 
}

const handleCreate = async (payload: any) => {
  try {
    await productStore.createProduct(payload);
    showCreateModal.value = false;
    showToast('✅ Thêm sản phẩm thành công!');
    await productStore.fetchProducts();
  } catch (err) {
    console.error(err);
    showToast('❌ Lỗi khi thêm sản phẩm.', 'error');
  }
};

// 2. Update Product (Chi tiết)
const handleUpdate = async (payload: any) => {
  const { basic, detail } = payload;
  try {
    await productStore.updateProduct(basic.id, basic);
    await productStore.saveProductDetail(basic.id, detail);
    showToast('✅ Cập nhật sản phẩm thành công!');
    closeDetailModal(); 
    await productStore.fetchProducts(); 
  } catch (err) {
    console.error(err);
    showToast('❌ Lỗi khi cập nhật sản phẩm.', 'error');
  }
};

// const editProduct = (product: any) => {
//   // (Hàm này có thể không cần nữa vì dùng openDetailModal, nhưng giữ lại nếu muốn nút "Sửa" riêng)
//   openDetailModal(product);
// };

// Delete
const showDeleteConfirm = ref(false);
const productToDelete = ref<any>(null);
const askDeleteProduct = (product: any) => {
  productStore.error = null;
  productToDelete.value = product;
  showDeleteConfirm.value = true;
};
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
const handleFilterChange = (filterParams: any) => {
  productStore.fetchProducts(filterParams);
};
</script>