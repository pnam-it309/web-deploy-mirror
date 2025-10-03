<template>
  <div class="container mx-auto p-4">
    <h1 class="text-2xl font-bold mb-6">Quản lý nhập liệu</h1>
    
    <div class="bg-white rounded-lg shadow p-6 mb-6">
      <ImportDataFilter 
        @search="handleSearch"
        @reset="handleReset"
      />
    </div>

    <div class="bg-white rounded-lg shadow p-6">
      <div class="flex justify-between items-center mb-4">
        <h2 class="text-xl font-semibold">Danh sách dữ liệu</h2>
        <button 
          @click="showUploadModal = true"
          class="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 flex items-center"
        >
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" viewBox="0 0 20 20" fill="currentColor">
            <path fill-rule="evenodd" d="M3 17a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zm3.293-7.707a1 1 0 011.414 0L9 10.586V3a1 1 0 112 0v7.586l1.293-1.293a1 1 0 111.414 1.414l-3 3a1 1 0 01-1.414 0l-3-3a1 1 0 010-1.414z" clip-rule="evenodd" />
          </svg>
          Tải lên dữ liệu
        </button>
      </div>
      
      <ImportDataTable 
        :items="importData"
        :loading="isLoading"
        @edit="handleEdit"
        @delete="handleDelete"
      />
    </div>

    <!-- Upload Modal -->
    <div v-if="showUploadModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
      <div class="bg-white rounded-lg w-full max-w-md p-6">
        <div class="flex justify-between items-center mb-4">
          <h3 class="text-lg font-semibold">Tải lên dữ liệu</h3>
          <button @click="showUploadModal = false" class="text-gray-500 hover:text-gray-700">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>
        
        <div class="border-2 border-dashed border-gray-300 rounded-lg p-6 text-center">
          <input 
            type="file" 
            ref="fileInput"
            @change="handleFileChange"
            class="hidden" 
            accept=".xlsx, .xls, .csv"
          />
          <div class="flex flex-col items-center">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-12 w-12 text-gray-400 mb-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M15 13l-3-3m0 0l-3 3m3-3v12" />
            </svg>
            <p class="text-gray-600 mb-2">Kéo thả file vào đây hoặc</p>
            <button 
              @click="$refs.fileInput.click()"
              class="text-blue-600 font-medium hover:text-blue-800"
            >
              Chọn file
            </button>
            <p class="text-sm text-gray-500 mt-2">Hỗ trợ: .xlsx, .xls, .csv</p>
          </div>
        </div>

        <div v-if="selectedFile" class="mt-4 p-3 bg-gray-50 rounded-md">
          <div class="flex justify-between items-center">
            <div class="flex items-center">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-gray-500 mr-2" viewBox="0 0 20 20" fill="currentColor">
                <path fill-rule="evenodd" d="M4 4a2 2 0 012-2h4.586A2 2 0 0112 2.586L15.414 6A2 2 0 0116 7.414V16a2 2 0 01-2 2H6a2 2 0 01-2-2V4z" clip-rule="evenodd" />
              </svg>
              <span class="text-sm">{{ selectedFile.name }}</span>
            </div>
            <button @click="removeFile" class="text-red-500 hover:text-red-700">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                <path fill-rule="evenodd" d="M9 2a1 1 0 00-.894.553L7.382 4H4a1 1 0 000 2v10a2 2 0 002 2h8a2 2 0 002-2V6a1 1 0 100-2h-3.382l-.724-1.447A1 1 0 0011 2H9zM7 8a1 1 0 012 0v6a1 1 0 11-2 0V8zm5-1a1 1 0 00-1 1v6a1 1 0 102 0V8a1 1 0 00-1-1z" clip-rule="evenodd" />
              </svg>
            </button>
          </div>
        </div>

        <div class="mt-6 flex justify-end space-x-3">
          <button 
            @click="showUploadModal = false"
            class="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50"
          >
            Hủy
          </button>
          <button 
            @click="uploadFile"
            :disabled="!selectedFile || isUploading"
            :class="{
              'bg-blue-600 hover:bg-blue-700 text-white': selectedFile && !isUploading,
              'bg-blue-300 cursor-not-allowed': !selectedFile || isUploading
            }"
            class="px-4 py-2 rounded-md"
          >
            <span v-if="isUploading">
              <svg class="animate-spin -ml-1 mr-2 h-4 w-4 text-white inline" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
              </svg>
              Đang tải lên...
            </span>
            <span v-else>Tải lên</span>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import ImportDataFilter from './ImportDataFilter.vue';
import ImportDataTable from './ImportDataTable.vue';
import { useToast } from 'vue-toastification';

const toast = useToast();

// State
const importData = ref([]);
const isLoading = ref(false);
const showUploadModal = ref(false);
const selectedFile = ref<File | null>(null);
const isUploading = ref(false);

// Methods
const fetchImportData = async (params = {}) => {
  try {
    isLoading.value = true;
    // Replace with your actual API call
    const response = await fetch('http://localhost:9999/api/v1/import_data', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      },
    });
    
    if (!response.ok) throw new Error('Failed to fetch data');
    
    const data = await response.json();
    importData.value = data;
  } catch (error) {
    console.error('Error fetching import data:', error);
    toast.error('Lỗi khi tải dữ liệu');
  } finally {
    isLoading.value = false;
  }
};

const handleSearch = (filters: any) => {
  // Handle search with filters
  fetchImportData(filters);
};

const handleReset = () => {
  // Reset filters and fetch all data
  fetchImportData();
};

const handleEdit = (item: any) => {
  // Handle edit action
  console.log('Edit item:', item);
};

const handleDelete = async (id: string) => {
  if (!confirm('Bạn có chắc chắn muốn xóa dữ liệu này?')) return;
  
  try {
    // Replace with your actual API call
    const response = await fetch(`http://localhost:9999/api/v1/import_data/${id}`, {
      method: 'DELETE',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      },
    });
    
    if (!response.ok) throw new Error('Failed to delete data');
    
    toast.success('Xóa dữ liệu thành công');
    fetchImportData(); // Refresh the data
  } catch (error) {
    console.error('Error deleting data:', error);
    toast.error('Lỗi khi xóa dữ liệu');
  }
};

const handleFileChange = (event: Event) => {
  const target = event.target as HTMLInputElement;
  if (target.files && target.files.length > 0) {
    selectedFile.value = target.files[0];
  }
};

const removeFile = () => {
  selectedFile.value = null;
  if (this.$refs.fileInput) {
    (this.$refs.fileInput as HTMLInputElement).value = '';
  }
};

const uploadFile = async () => {
  if (!selectedFile.value) return;
  
  const formData = new FormData();
  formData.append('file', selectedFile.value);
  
  try {
    isUploading.value = true;
    
    // Replace with your actual API call
    const response = await fetch('http://localhost:9999/api/v1/import_data/upload', {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      },
      body: formData
    });
    
    if (!response.ok) throw new Error('Upload failed');
    
    const data = await response.json();
    toast.success('Tải lên thành công');
    showUploadModal.value = false;
    selectedFile.value = null;
    fetchImportData(); // Refresh the data
  } catch (error) {
    console.error('Error uploading file:', error);
    toast.error('Lỗi khi tải lên file');
  } finally {
    isUploading.value = false;
  }
};

// Lifecycle hooks
onMounted(() => {
  fetchImportData();
});
</script>