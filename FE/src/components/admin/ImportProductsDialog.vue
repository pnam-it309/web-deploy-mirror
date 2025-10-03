<template>
  <Dialog :open="open" @close="$emit('close')">
    <div class="fixed inset-0 bg-black/30" aria-hidden="true" />
    <div class="fixed inset-0 flex items-center justify-center p-4">
      <DialogPanel class="w-full max-w-2xl rounded-lg bg-white p-6 shadow-xl">
        <DialogTitle class="text-lg font-medium text-gray-900">Nhập sản phẩm từ Excel</DialogTitle>
        
        <div class="mt-4">
          <div class="border-2 border-dashed border-gray-300 rounded-lg p-6 text-center">
            <div class="flex justify-center">
              <ArrowUpTrayIcon class="h-12 w-12 text-gray-400" />
            </div>
            <div class="mt-4 flex text-sm text-gray-600">
              <label 
                for="file-upload" 
                class="relative cursor-pointer rounded-md bg-white font-medium text-blue-600 hover:text-blue-500 focus-within:outline-none focus-within:ring-2 focus-within:ring-blue-500 focus-within:ring-offset-2"
              >
                <span>Tải lên file Excel</span>
                <input 
                  id="file-upload" 
                  name="file-upload" 
                  type="file" 
                  class="sr-only" 
                  accept=".xlsx, .xls, .csv"
                  @change="handleFileUpload"
                />
              </label>
              <p class="pl-1">hoặc kéo thả vào đây</p>
            </div>
            <p class="text-xs text-gray-500 mt-1">
              Hỗ trợ định dạng XLSX, XLS, CSV (tối đa 10MB)
            </p>
            <p v-if="file" class="mt-4 text-sm text-gray-900">
              Đã chọn: {{ file.name }}
            </p>
          </div>

          <div class="mt-6">
            <h3 class="text-sm font-medium text-gray-900">Cấu hình nhập liệu</h3>
            <div class="mt-4 grid grid-cols-1 gap-4 sm:grid-cols-2">
              <div>
                <label for="sheet-name" class="block text-sm font-medium text-gray-700">Tên sheet</label>
                <input 
                  type="text" 
                  id="sheet-name" 
                  v-model="sheetName" 
                  class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 sm:text-sm"
                  placeholder="Sheet1"
                />
              </div>
              <div>
                <label for="header-row" class="block text-sm font-medium text-gray-700">Dòng tiêu đề</label>
                <input 
                  type="number" 
                  id="header-row" 
                  v-model.number="headerRow" 
                  min="1"
                  class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 sm:text-sm"
                />
              </div>
            </div>

            <div class="mt-4">
              <div class="flex items-start">
                <div class="flex h-5 items-center">
                  <input 
                    id="update-existing" 
                    type="checkbox" 
                    v-model="updateExisting" 
                    class="h-4 w-4 rounded border-gray-300 text-blue-600 focus:ring-blue-500"
                  />
                </div>
                <div class="ml-3 text-sm">
                  <label for="update-existing" class="font-medium text-gray-700">Cập nhật sản phẩm đã tồn tại</label>
                  <p class="text-gray-500">Nếu bỏ chọn, các sản phẩm trùng sẽ bị bỏ qua</p>
                </div>
              </div>
            </div>
          </div>

          <div v-if="previewData.length > 0" class="mt-6">
            <h3 class="text-sm font-medium text-gray-900 mb-2">Xem trước dữ liệu</h3>
            <div class="overflow-x-auto">
              <table class="min-w-full divide-y divide-gray-200 border">
                <thead class="bg-gray-50">
                  <tr>
                    <th 
                      v-for="(header, index) in previewHeaders" 
                      :key="index"
                      class="px-3 py-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider border-b"
                    >
                      {{ header }}
                    </th>
                  </tr>
                </thead>
                <tbody class="bg-white divide-y divide-gray-200">
                  <tr v-for="(row, rowIndex) in previewData" :key="rowIndex">
                    <td 
                      v-for="(cell, cellIndex) in row" 
                      :key="cellIndex"
                      class="px-3 py-2 whitespace-nowrap text-sm text-gray-500 border-b"
                    >
                      {{ cell }}
                    </td>
                  </tr>
                </tbody>
              </table>
              <p class="mt-1 text-xs text-gray-500">
                Hiển thị {{ Math.min(previewData.length, 5) }} trong tổng số {{ previewData.length }} dòng
              </p>
            </div>

            <div class="mt-4">
              <h4 class="text-sm font-medium text-gray-900 mb-2">Ánh xạ cột dữ liệu</h4>
              <div class="space-y-2">
                <div 
                  v-for="(field, index) in fieldMappings" 
                  :key="index"
                  class="grid grid-cols-3 gap-4 items-center"
                >
                  <label class="block text-sm font-medium text-gray-700">{{ field.label }}</label>
                  <div class="col-span-2">
                    <select 
                      v-model="fieldMappings[index].column"
                      class="mt-1 block w-full rounded-md border-gray-300 py-2 pl-3 pr-10 text-base focus:border-blue-500 focus:outline-none focus:ring-blue-500 sm:text-sm"
                    >
                      <option value="">-- Chọn cột --</option>
                      <option 
                        v-for="(header, headerIndex) in previewHeaders" 
                        :key="headerIndex"
                        :value="header"
                      >
                        {{ header }}
                      </option>
                    </select>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div class="mt-6 flex justify-end space-x-3">
            <button 
              type="button" 
              @click="$emit('close')"
              class="inline-flex items-center rounded-md border border-gray-300 bg-white px-4 py-2 text-sm font-medium text-gray-700 shadow-sm hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
            >
              Hủy
            </button>
            <button 
              type="button" 
              @click="importProducts"
              :disabled="!canImport"
              :class="{
                'bg-blue-600 hover:bg-blue-700': canImport,
                'bg-blue-300 cursor-not-allowed': !canImport
              }"
              class="inline-flex items-center rounded-md border border-transparent px-4 py-2 text-sm font-medium text-white shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
            >
              <ArrowPathIcon v-if="isImporting" class="-ml-1 mr-2 h-4 w-4 animate-spin" />
              Nhập sản phẩm
            </button>
          </div>
        </div>
      </DialogPanel>
    </div>
  </Dialog>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue';
import { Dialog, DialogPanel, DialogTitle } from '@headlessui/vue';
import { ArrowUpTrayIcon, ArrowPathIcon } from '@heroicons/vue/24/outline';
import * as XLSX from 'xlsx';

const emit = defineEmits(['close', 'imported']);

const props = defineProps({
  open: {
    type: Boolean,
    default: false
  }
});

const file = ref<File | null>(null);
const sheetName = ref('Sheet1');
const headerRow = ref(1);
const updateExisting = ref(true);
const previewData = ref<any[]>([]);
const previewHeaders = ref<string[]>([]);
const isImporting = ref(false);

const fieldMappings = ref([
  { label: 'Mã sản phẩm (SKU)', column: '', required: true },
  { label: 'Tên sản phẩm', column: '', required: true },
  { label: 'Mô tả', column: '', required: false },
  { label: 'Danh mục', column: '', required: true },
  { label: 'Giá bán', column: '', required: true },
  { label: 'Số lượng tồn', column: '', required: true },
  { label: 'Đơn vị tính', column: '', required: true },
  { label: 'Hình ảnh (URL)', column: '', required: false },
  { label: 'Trạng thái', column: '', required: false },
]);

const canImport = computed(() => {
  if (!file.value) return false;
  if (previewData.value.length === 0) return false;
  
  // Check if all required fields are mapped
  const requiredMappings = fieldMappings.value.filter(f => f.required);
  return requiredMappings.every(f => f.column);
});

const handleFileUpload = (event: Event) => {
  const target = event.target as HTMLInputElement;
  if (target.files && target.files.length > 0) {
    file.value = target.files[0];
    parseExcelFile(target.files[0]);
  }
};

const parseExcelFile = (file: File) => {
  const reader = new FileReader();
  
  reader.onload = (e) => {
    try {
      const data = new Uint8Array(e.target?.result as ArrayBuffer);
      const workbook = XLSX.read(data, { type: 'array' });
      
      // Get the first sheet
      const firstSheetName = workbook.SheetNames[0];
      const worksheet = workbook.Sheets[firstSheetName];
      
      // Convert to JSON
      const jsonData = XLSX.utils.sheet_to_json(worksheet, { header: 1 });
      
      if (jsonData.length > 0) {
        // Get headers (assuming first row is header)
        previewHeaders.value = jsonData[0] as string[];
        
        // Get data (skip header row)
        previewData.value = jsonData.slice(1, 6); // Show first 5 rows for preview
        
        // Try to auto-detect column mappings
        autoDetectMappings();
      }
    } catch (error) {
      console.error('Error parsing Excel file:', error);
      // Show error to user
    }
  };
  
  reader.onerror = (error) => {
    console.error('Error reading file:', error);
    // Show error to user
  };
  
  reader.readAsArrayBuffer(file);
};

const autoDetectMappings = () => {
  if (previewHeaders.value.length === 0) return;
  
  const headerTexts = previewHeaders.value.map((h: string) => h.toString().toLowerCase());
  
  fieldMappings.value = fieldMappings.value.map(field => {
    const fieldLabel = field.label.toLowerCase();
    
    // Try to find a header that matches the field label
    const matchingHeaderIndex = headerTexts.findIndex(header => 
      header.includes(fieldLabel) || fieldLabel.includes(header)
    );
    
    return {
      ...field,
      column: matchingHeaderIndex >= 0 ? previewHeaders.value[matchingHeaderIndex] : ''
    };
  });
};

const importProducts = async () => {
  if (!file.value || !canImport.value) return;
  
  isImporting.value = true;
  
  try {
    // Simulate API call
    await new Promise(resolve => setTimeout(resolve, 2000));
    
    // Process the file and create products
    const products = processFileData();
    
    // Emit the imported products
    emit('imported', products);
    emit('close');
    
    // Show success message
    // You can use a toast/notification system here
    console.log('Products imported successfully:', products);
  } catch (error) {
    console.error('Error importing products:', error);
    // Show error message
  } finally {
    isImporting.value = false;
  }
};

const processFileData = () => {
  // This is a simplified example. In a real app, you would:
  // 1. Read the entire file
  // 2. Map the data according to fieldMappings
  // 3. Validate the data
  // 4. Format it for your API
  
  return previewData.value.map((row: any) => {
    const product: Record<string, any> = {};
    
    fieldMappings.value.forEach(field => {
      if (field.column) {
        const columnIndex = previewHeaders.value.indexOf(field.column);
        if (columnIndex >= 0) {
          const key = field.label
            .toLowerCase()
            .replace(/[^a-z0-9]+/g, '_')
            .replace(/^_+|_+$/g, '');
          
          product[key] = row[columnIndex];
        }
      }
    });
    
    return product;
  });
};

// Reset form when dialog is opened
watch(() => props.open, (isOpen) => {
  if (isOpen) {
    file.value = null;
    sheetName.value = 'Sheet1';
    headerRow.value = 1;
    updateExisting.value = true;
    previewData.value = [];
    previewHeaders.value = [];
    fieldMappings.value = fieldMappings.value.map(f => ({ ...f, column: '' }));
  }
});
</script>
