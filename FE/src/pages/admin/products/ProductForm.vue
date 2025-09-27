<template>
  <div class="product-form">
    <div class="bg-white shadow">
      <div class="max-w-7xl mx-auto py-6 px-4 sm:px-6 lg:px-8 flex justify-between items-center">
        <h1 class="text-3xl font-bold text-gray-900">
          {{ isEditMode ? 'Chỉnh sửa sản phẩm' : 'Thêm sản phẩm mới' }}
        </h1>
        <router-link
          :to="{ name: 'admin-products' }"
          class="inline-flex items-center px-4 py-2 border border-gray-300 shadow-sm text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
        >
          <svg class="-ml-1 mr-2 h-5 w-5 text-gray-500" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
            <path fill-rule="evenodd" d="M12.707 5.293a1 1 0 010 1.414L9.414 10l3.293 3.293a1 1 0 01-1.414 1.414l-4-4a1 1 0 010-1.414l4-4a1 1 0 011.414 0z" clip-rule="evenodd" />
          </svg>
          Quay lại
        </router-link>
      </div>
    </div>

    <div class="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
      <div class="bg-white shadow overflow-hidden sm:rounded-lg">
        <form @submit.prevent="submitForm">
          <div class="px-4 py-5 sm:p-6">
            <!-- Form Errors -->
            <div v-if="Object.keys(errors).length > 0" class="mb-6 bg-red-50 border-l-4 border-red-400 p-4">
              <div class="flex">
                <div class="flex-shrink-0">
                  <svg class="h-5 w-5 text-red-400" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor">
                    <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clip-rule="evenodd" />
                  </svg>
                </div>
                <div class="ml-3">
                  <h3 class="text-sm font-medium text-red-800">
                    Có {{ Object.keys(errors).length }} lỗi xảy ra khi gửi biểu mẫu của bạn
                  </h3>
                  <div class="mt-2 text-sm text-red-700">
                    <ul class="list-disc pl-5 space-y-1">
                      <li v-for="(error, field) in errors" :key="field">
                        {{ error[0] }}
                      </li>
                    </ul>
                  </div>
                </div>
              </div>
            </div>

            <!-- Basic Information -->
            <div class="border-b border-gray-200 pb-6">
              <h3 class="text-lg font-medium leading-6 text-gray-900 mb-4">Thông tin cơ bản</h3>
              
              <div class="grid grid-cols-1 gap-y-6 gap-x-4 sm:grid-cols-6">
                <!-- Product Name -->
                <div class="sm:col-span-4">
                  <label for="name" class="block text-sm font-medium text-gray-700">
                    Tên sản phẩm <span class="text-red-500">*</span>
                  </label>
                  <div class="mt-1">
                    <input
                      type="text"
                      id="name"
                      v-model="form.name"
                      class="shadow-sm focus:ring-indigo-500 focus:border-indigo-500 block w-full sm:text-sm border-gray-300 rounded-md"
                      :class="{ 'border-red-300': errors.name }"
                    />
                  </div>
                </div>

                <!-- SKU -->
                <div class="sm:col-span-2">
                  <label for="sku" class="block text-sm font-medium text-gray-700">
                    Mã SKU
                  </label>
                  <div class="mt-1">
                    <input
                      type="text"
                      id="sku"
                      v-model="form.sku"
                      class="shadow-sm focus:ring-indigo-500 focus:border-indigo-500 block w-full sm:text-sm border-gray-300 rounded-md"
                      :class="{ 'border-red-300': errors.sku }"
                    />
                  </div>
                </div>

                <!-- Description -->
                <div class="sm:col-span-6">
                  <label for="description" class="block text-sm font-medium text-gray-700">
                    Mô tả
                  </label>
                  <div class="mt-1">
                    <textarea
                      id="description"
                      v-model="form.description"
                      rows="3"
                      class="shadow-sm focus:ring-indigo-500 focus:border-indigo-500 block w-full sm:text-sm border border-gray-300 rounded-md"
                      :class="{ 'border-red-300': errors.description }"
                    />
                  </div>
                </div>

                <!-- Category -->
                <div class="sm:col-span-3">
                  <label for="category" class="block text-sm font-medium text-gray-700">
                    Danh mục <span class="text-red-500">*</span>
                  </label>
                  <div class="mt-1">
                    <select
                      id="category"
                      v-model="form.category_id"
                      class="shadow-sm focus:ring-indigo-500 focus:border-indigo-500 block w-full sm:text-sm border-gray-300 rounded-md"
                      :class="{ 'border-red-300': errors.category_id }"
                    >
                      <option value="">Chọn danh mục</option>
                      <option v-for="category in categories" :key="category.id" :value="category.id">
                        {{ category.name }}
                      </option>
                    </select>
                  </div>
                </div>

                <!-- Status -->
                <div class="sm:col-span-3">
                  <label for="status" class="block text-sm font-medium text-gray-700">
                    Trạng thái
                  </label>
                  <div class="mt-1">
                    <select
                      id="status"
                      v-model="form.status"
                      class="shadow-sm focus:ring-indigo-500 focus:border-indigo-500 block w-full sm:text-sm border-gray-300 rounded-md"
                    >
                      <option value="active">Đang bán</option>
                      <option value="inactive">Ngừng bán</option>
                      <option value="out_of_stock">Hết hàng</option>
                    </select>
                  </div>
                </div>
              </div>
            </div>

            <!-- Pricing -->
            <div class="border-b border-gray-200 py-6">
              <h3 class="text-lg font-medium leading-6 text-gray-900 mb-4">Giá cả</h3>
              
              <div class="grid grid-cols-1 gap-y-6 gap-x-4 sm:grid-cols-6">
                <!-- Price -->
                <div class="sm:col-span-2">
                  <label for="price" class="block text-sm font-medium text-gray-700">
                    Giá bán <span class="text-red-500">*</span>
                  </label>
                  <div class="mt-1 relative rounded-md shadow-sm">
                    <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                      <span class="text-gray-500 sm:text-sm">₫</span>
                    </div>
                    <input
                      type="number"
                      id="price"
                      v-model.number="form.price"
                      min="0"
                      step="1000"
                      class="focus:ring-indigo-500 focus:border-indigo-500 block w-full pl-7 pr-12 sm:text-sm border-gray-300 rounded-md"
                      :class="{ 'border-red-300': errors.price }"
                      placeholder="0.00"
                    />
                  </div>
                </div>

                <!-- Cost Price -->
                <div class="sm:col-span-2">
                  <label for="cost_price" class="block text-sm font-medium text-gray-700">
                    Giá nhập
                  </label>
                  <div class="mt-1 relative rounded-md shadow-sm">
                    <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                      <span class="text-gray-500 sm:text-sm">₫</span>
                    </div>
                    <input
                      type="number"
                      id="cost_price"
                      v-model.number="form.cost_price"
                      min="0"
                      step="1000"
                      class="focus:ring-indigo-500 focus:border-indigo-500 block w-full pl-7 pr-12 sm:text-sm border-gray-300 rounded-md"
                      :class="{ 'border-red-300': errors.cost_price }"
                      placeholder="0.00"
                    />
                  </div>
                </div>

                <!-- Special Price -->
                <div class="sm:col-span-2">
                  <label for="special_price" class="block text-sm font-medium text-gray-700">
                    Giá khuyến mãi
                  </label>
                  <div class="mt-1 relative rounded-md shadow-sm">
                    <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                      <span class="text-gray-500 sm:text-sm">₫</span>
                    </div>
                    <input
                      type="number"
                      id="special_price"
                      v-model.number="form.special_price"
                      min="0"
                      step="1000"
                      class="focus:ring-indigo-500 focus:border-indigo-500 block w-full pl-7 pr-12 sm:text-sm border-gray-300 rounded-md"
                      :class="{ 'border-red-300': errors.special_price }"
                      placeholder="0.00"
                    />
                  </div>
                </div>
              </div>
            </div>

            <!-- Inventory -->
            <div class="border-b border-gray-200 py-6">
              <h3 class="text-lg font-medium leading-6 text-gray-900 mb-4">Tồn kho</h3>
              
              <div class="grid grid-cols-1 gap-y-6 gap-x-4 sm:grid-cols-6">
                <!-- Stock Quantity -->
                <div class="sm:col-span-2">
                  <label for="stock_quantity" class="block text-sm font-medium text-gray-700">
                    Số lượng tồn kho
                  </label>
                  <div class="mt-1">
                    <input
                      type="number"
                      id="stock_quantity"
                      v-model.number="form.stock_quantity"
                      min="0"
                      class="shadow-sm focus:ring-indigo-500 focus:border-indigo-500 block w-full sm:text-sm border-gray-300 rounded-md"
                      :class="{ 'border-red-300': errors.stock_quantity }"
                    />
                  </div>
                </div>

                <!-- Stock Status -->
                <div class="sm:col-span-2">
                  <label for="stock_status" class="block text-sm font-medium text-gray-700">
                    Tình trạng tồn kho
                  </label>
                  <div class="mt-1">
                    <select
                      id="stock_status"
                      v-model="form.stock_status"
                      class="shadow-sm focus:ring-indigo-500 focus:border-indigo-500 block w-full sm:text-sm border-gray-300 rounded-md"
                    >
                      <option value="in_stock">Còn hàng</option>
                      <option value="out_of_stock">Hết hàng</option>
                      <option value="preorder">Đặt trước</option>
                    </select>
                  </div>
                </div>
              </div>
            </div>

            <!-- Images -->
            <div class="py-6">
              <h3 class="text-lg font-medium leading-6 text-gray-900 mb-4">Hình ảnh sản phẩm</h3>
              
              <div class="mt-1 flex justify-center px-6 pt-5 pb-6 border-2 border-gray-300 border-dashed rounded-md">
                <div class="space-y-1 text-center">
                  <svg class="mx-auto h-12 w-12 text-gray-400" stroke="currentColor" fill="none" viewBox="0 0 48 48" aria-hidden="true">
                    <path d="M28 8H12a4 4 0 00-4 4v20m32-12v8m0 0v8a4 4 0 01-4 4H12a4 4 0 01-4-4v-4m32-4l-3.172-3.172a4 4 0 00-5.656 0L28 28M8 32l9.172-9.172a4 4 0 015.656 0L28 28m0 0l4 4m4-24h8m-4-4v8m-12 4h.02" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" />
                  </svg>
                  <div class="flex text-sm text-gray-600">
                    <label for="file-upload" class="relative cursor-pointer bg-white rounded-md font-medium text-indigo-600 hover:text-indigo-500 focus-within:outline-none focus-within:ring-2 focus-within:ring-offset-2 focus-within:ring-indigo-500">
                      <span>Tải ảnh lên</span>
                      <input id="file-upload" name="file-upload" type="file" class="sr-only" multiple @change="handleFileUpload" />
                    </label>
                    <p class="pl-1">hoặc kéo thả ảnh vào đây</p>
                  </div>
                  <p class="text-xs text-gray-500">
                    PNG, JPG, GIF lên đến 10MB
                  </p>
                </div>
              </div>

              <!-- Image Preview -->
              <div v-if="images.length > 0" class="mt-4 grid grid-cols-2 gap-4 sm:grid-cols-3 lg:grid-cols-4">
                <div v-for="(image, index) in images" :key="index" class="relative group">
                  <img :src="image.preview || image.url" class="h-40 w-full object-cover rounded-md" />
                  <button
                    type="button"
                    @click="removeImage(index)"
                    class="absolute -top-2 -right-2 bg-red-500 text-white rounded-full p-1 opacity-0 group-hover:opacity-100 focus:outline-none"
                  >
                    <svg class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                    </svg>
                  </button>
                  <div v-if="index === 0" class="absolute top-2 left-2 bg-blue-500 text-white text-xs px-2 py-1 rounded">
                    Ảnh đại diện
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- Form Actions -->
          <div class="px-4 py-3 bg-gray-50 text-right sm:px-6">
            <button
              type="button"
              @click="$router.push({ name: 'admin-products' })"
              class="bg-white py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
            >
              Hủy
            </button>
            <button
              type="submit"
              :disabled="saving"
              class="ml-3 inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              <svg v-if="saving" class="animate-spin -ml-1 mr-3 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
              </svg>
              {{ saving ? 'Đang lưu...' : 'Lưu sản phẩm' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, reactive, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';

export default defineComponent({
  name: 'ProductForm',
  props: {
    id: {
      type: [String, Number],
      default: null
    }
  },
  setup(props) {
    const route = useRoute();
    const router = useRouter();
    
    const isEditMode = computed(() => !!props.id);
    const saving = ref(false);
    const errors = ref({});
    
    // Sample categories - replace with actual API call
    const categories = ref([
      { id: 1, name: 'Áo thun' },
      { id: 2, name: 'Quần jean' },
      { id: 3, name: 'Áo khoác' },
      { id: 4, name: 'Giày dép' },
    ]);

    // Form data
    const form = reactive({
      name: '',
      sku: '',
      description: '',
      category_id: '',
      status: 'active',
      price: 0,
      cost_price: 0,
      special_price: 0,
      stock_quantity: 0,
      stock_status: 'in_stock',
    });

    // Image handling
    const images = ref<Array<{file?: File, preview?: string, url?: string}>>([]);

    // Load product data if in edit mode
    const loadProduct = async () => {
      if (!isEditMode.value) return;
      
      try {
        // Replace with actual API call
        // const response = await productApi.getProduct(props.id);
        // Object.assign(form, response.data);
        
        // Sample data for testing
        Object.assign(form, {
          name: 'Áo thun nam',
          sku: 'ATN001',
          description: 'Áo thun nam chất liệu cotton mềm mại',
          category_id: 1,
          status: 'active',
          price: 250000,
          cost_price: 150000,
          special_price: 0,
          stock_quantity: 100,
          stock_status: 'in_stock',
        });
        
        // Sample image for testing
        images.value = [
          { url: 'https://via.placeholder.com/300' },
          { url: 'https://via.placeholder.com/300' },
        ];
        
      } catch (error) {
        console.error('Error loading product:', error);
      }
    };

    // Handle file upload
    const handleFileUpload = (event: Event) => {
      const target = event.target as HTMLInputElement;
      if (!target.files || target.files.length === 0) return;
      
      Array.from(target.files).forEach(file => {
        const reader = new FileReader();
        reader.onload = (e) => {
          images.value.push({
            file,
            preview: e.target?.result as string
          });
        };
        reader.readAsDataURL(file);
      });
      
      // Reset file input
      target.value = '';
    };

    // Remove image
    const removeImage = (index: number) => {
      images.value.splice(index, 1);
    };

    // Form submission
    const submitForm = async () => {
      saving.value = true;
      errors.value = {};
      
      try {
        const formData = new FormData();
        
        // Append form data
        Object.keys(form).forEach(key => {
          formData.append(key, (form as any)[key]);
        });
        
        // Append images
        images.value.forEach((image, index) => {
          if (image.file) {
            formData.append(`images[${index}]`, image.file);
          }
        });
        
        if (isEditMode.value) {
          // Update existing product
          // await productApi.updateProduct(props.id, formData);
          console.log('Updating product:', formData);
        } else {
          // Create new product
          // await productApi.createProduct(formData);
          console.log('Creating product:', formData);
        }
        
        // Redirect to product list on success
        router.push({ name: 'admin-products' });
        
      } catch (error: any) {
        if (error.response?.status === 422) {
          // Handle validation errors
          errors.value = error.response.data.errors;
        } else {
          console.error('Error saving product:', error);
          alert('Đã xảy ra lỗi khi lưu sản phẩm. Vui lòng thử lại sau.');
        }
      } finally {
        saving.value = false;
      }
    };

    // Load product data when component mounts
    onMounted(() => {
      if (isEditMode.value) {
        loadProduct();
      }
    });

    return {
      isEditMode,
      form,
      errors,
      saving,
      categories,
      images,
      handleFileUpload,
      removeImage,
      submitForm,
    };
  }
});
</script>

<style scoped>
.product-form {
  min-height: calc(100vh - 64px);
}
</style>
