<template>
  <transition name="slide-fade">
    <div v-if="show" class="fixed top-[64px] right-0 bottom-0 w-80 bg-white shadow-2xl border-l border-gray-200 z-40 flex flex-col">
      
      <!-- HEADER -->
      <div class="flex justify-between items-center p-4 border-b bg-gray-50">
        <h3 class="font-semibold text-gray-800">Hướng dẫn Import</h3>
        <button @click="$emit('close')" class="text-gray-500 hover:text-red-500 transition-colors">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor"><path fill-rule="evenodd" d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" clip-rule="evenodd" /></svg>
        </button>
      </div>

      <!-- CONTENT SCROLLABLE -->
      <div class="flex-1 overflow-y-auto p-4 custom-scrollbar">
        
        <!-- Bước 1: Chuẩn bị -->
        <div class="mb-6">
          <h4 class="text-sm font-bold text-blue-600 mb-2 uppercase">Bước 1: Chuẩn bị File</h4>
          <p class="text-sm text-gray-600 mb-2">Tải file mẫu Excel chuẩn để điền dữ liệu.</p>
          <button class="text-xs bg-green-100 text-green-700 px-3 py-1.5 rounded border border-green-200 hover:bg-green-200 flex items-center w-full justify-center gap-1 transition-colors">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-4l-4 4m0 0l-4-4m4 4V4" /></svg>
            Tải file mẫu .xlsx
          </button>
        </div>

        <!-- Bước 2: Lưu ý quan trọng -->
        <div class="mb-6">
          <h4 class="text-sm font-bold text-red-600 mb-2 uppercase">Bước 2: Lưu ý Nhập liệu</h4>
          <ul class="text-xs text-gray-600 space-y-2 list-disc pl-4">
            <li>
              <strong>Mã Thương hiệu & Danh mục:</strong> Phải nhập chính xác 
              <span class="bg-gray-100 px-1 font-mono text-red-500">CODE</span> (Mã), không phải Tên.
              <br><span class="text-gray-400 italic">(VD: Nhập 'APPLE', không phải 'Apple Inc.')</span>
            </li>
            <li>
              <strong>SKU (Mã SP):</strong> Không được trùng với sản phẩm đã có.
            </li>
            <li>
              <strong>Giá & Số lượng:</strong> Phải là số dương.
            </li>
            <li>
              <strong>Đơn vị tính:</strong> Nhập đúng chuẩn (VD: 'Cai', 'Hop', 'Kg'...).
            </li>
          </ul>
        </div>

        <!-- Bước 3: Upload -->
        <div class="mb-6">
          <h4 class="text-sm font-bold text-blue-600 mb-2 uppercase">Bước 3: Tải lên</h4>
          <div class="border-2 border-dashed border-gray-300 rounded-lg p-4 text-center hover:border-blue-400 transition-colors bg-gray-50"
               @dragover.prevent
               @drop.prevent="handleDrop"
          >
            <input type="file" ref="fileInput" class="hidden" accept=".xlsx, .xls" @change="handleFileSelect" />
            
            <div v-if="!selectedFile">
              <p class="text-xs text-gray-500 mb-2">Kéo thả file vào đây hoặc</p>
              <button @click="triggerFileInput" class="text-xs text-white bg-blue-500 px-3 py-1.5 rounded hover:bg-blue-600">Chọn file</button>
            </div>

            <div v-else class="text-left">
               <p class="text-xs font-bold text-gray-700 truncate mb-1">📄 {{ selectedFile.name }}</p>
               <p class="text-xs text-gray-500 mb-2">{{ (selectedFile.size / 1024).toFixed(2) }} KB</p>
               <div class="flex gap-2">
                  <button @click="doImport" :disabled="loading" class="flex-1 text-xs bg-blue-600 text-white py-1.5 rounded hover:bg-blue-700 disabled:opacity-50">
                    {{ loading ? 'Đang xử lý...' : 'Import Ngay' }}
                  </button>
                  <button @click="selectedFile = null" class="px-2 text-xs bg-gray-200 text-gray-700 rounded hover:bg-gray-300">Xoá</button>
               </div>
            </div>
          </div>
        </div>

        <!-- Kết quả Import -->
        <div v-if="result" class="mt-4 border-t pt-4">
          <h4 class="text-sm font-bold text-gray-800 mb-2">Kết quả:</h4>
          <div class="text-xs space-y-1">
             <p class="text-green-600">✅ Thành công: {{ result.successCount }} dòng</p>
             <p class="text-red-600">❌ Lỗi: {{ result.errorCount }} dòng</p>
          </div>

          <!-- List lỗi (Nếu có) -->
          <div v-if="result.errorRows && result.errorRows.length > 0" class="mt-3 bg-red-50 p-2 rounded border border-red-100">
            <p class="text-xs font-bold text-red-700 mb-1">Chi tiết lỗi:</p>
            <div class="max-h-40 overflow-y-auto custom-scrollbar">
               <div v-for="(err, idx) in result.errorRows" :key="idx" class="text-[10px] text-red-600 border-b border-red-100 last:border-0 py-1">
                  <span class="font-bold">Dòng {{ err.rowIndex }}:</span> {{ err.errorMessage }}
               </div>
            </div>
            <button class="mt-2 w-full text-xs text-center text-blue-600 hover:underline">Tải file lỗi</button>
          </div>
        </div>

      </div>
    </div>
  </transition>
</template>

<script setup lang="ts">
import { ref, defineProps, defineEmits } from 'vue';
import { useProductStore } from '@/stores/product.store';
import { toast } from 'vue3-toastify';

const props = defineProps({
  show: Boolean
});
const emit = defineEmits(['close']);

const productStore = useProductStore();
const fileInput = ref<HTMLInputElement | null>(null);
const selectedFile = ref<File | null>(null);
const loading = ref(false);
const result = ref<any>(null);

const triggerFileInput = () => fileInput.value?.click();

const handleFileSelect = (event: Event) => {
  const target = event.target as HTMLInputElement;
  if (target.files && target.files[0]) {
    selectedFile.value = target.files[0];
    result.value = null; // Reset kết quả cũ
  }
};

const handleDrop = (event: DragEvent) => {
  if (event.dataTransfer?.files && event.dataTransfer.files[0]) {
    selectedFile.value = event.dataTransfer.files[0];
    result.value = null;
  }
};

const doImport = async () => {
  if (!selectedFile.value) return;
  
  loading.value = true;
  try {
    const res = await productStore.importProducts(selectedFile.value);
    result.value = res;
    
    if (res.errorCount === 0) {
      toast.success(`Import thành công ${res.successCount} sản phẩm!`);
      // Nếu thành công hết thì có thể reset file hoặc đóng panel tuỳ ý
      // selectedFile.value = null;
    } else {
      toast.warning(`Đã import ${res.successCount} dòng. Có ${res.errorCount} dòng lỗi.`);
    }
  } catch (e: any) {
    toast.error(e.message || 'Lỗi Import');
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.slide-fade-enter-active {
  transition: all 0.3s ease-out;
}
.slide-fade-leave-active {
  transition: all 0.3s cubic-bezier(1, 0.5, 0.8, 1);
}
.slide-fade-enter-from,
.slide-fade-leave-to {
  transform: translateX(20px);
  opacity: 0;
}
.custom-scrollbar::-webkit-scrollbar { width: 4px; }
.custom-scrollbar::-webkit-scrollbar-track { background: #f1f1f1; }
.custom-scrollbar::-webkit-scrollbar-thumb { background: #d1d5db; border-radius: 2px; }
</style>