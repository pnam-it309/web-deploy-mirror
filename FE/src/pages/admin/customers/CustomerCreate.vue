<template>
  <div class="min-h-[calc(100vh-64px)] bg-[#f8f9fa] p-6">
    <!-- Breadcrumb -->
    <BreadcrumbDefault :items="breadcrumbItems" />
    
    <!-- Card Container -->
    <div class="bg-white rounded-lg shadow p-6">
      <h1 class="text-2xl font-semibold text-gray-800 mb-6">Thêm khách hàng mới</h1>
      
      <form @submit.prevent="handleSubmit" class="space-y-6">
        <!-- Basic Information -->
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div>
            <label for="fullName" class="block text-sm font-medium text-gray-700 mb-1">Họ và tên <span class="text-red-500">*</span></label>
            <input
              type="text"
              id="fullName"
              v-model="formData.fullName"
              class="form-input w-full rounded-md border-gray-300 focus:border-indigo-500 focus:ring-indigo-500"
              required
            >
          </div>
          
          <div>
            <label for="email" class="block text-sm font-medium text-gray-700 mb-1">Email <span class="text-red-500">*</span></label>
            <input
              type="email"
              id="email"
              v-model="formData.email"
              class="form-input w-full rounded-md border-gray-300 focus:border-indigo-500 focus:ring-indigo-500"
              required
            >
          </div>
          
          <div>
            <label for="phone" class="block text-sm font-medium text-gray-700 mb-1">Số điện thoại</label>
            <input
              type="tel"
              id="phone"
              v-model="formData.phone"
              class="form-input w-full rounded-md border-gray-300 focus:border-indigo-500 focus:ring-indigo-500"
            >
          </div>
          
          <div>
            <label for="status" class="block text-sm font-medium text-gray-700 mb-1">Trạng thái</label>
            <select
              id="status"
              v-model="formData.status"
              class="form-select w-full rounded-md border-gray-300 focus:border-indigo-500 focus:ring-indigo-500"
            >
              <option value="active">Hoạt động</option>
              <option value="inactive">Ngừng hoạt động</option>
              <option value="pending">Chờ xử lý</option>
            </select>
          </div>
        </div>
        
        <!-- Address Information -->
        <div class="border-t border-gray-200 pt-6">
          <h2 class="text-lg font-medium text-gray-900 mb-4">Thông tin địa chỉ</h2>
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div class="col-span-2">
              <label for="address" class="block text-sm font-medium text-gray-700 mb-1">Địa chỉ</label>
              <input
                type="text"
                id="address"
                v-model="formData.address.street"
                class="form-input w-full rounded-md border-gray-300 focus:border-indigo-500 focus:ring-indigo-500"
              >
            </div>
            
            <div>
              <label for="city" class="block text-sm font-medium text-gray-700 mb-1">Thành phố</label>
              <input
                type="text"
                id="city"
                v-model="formData.address.city"
                class="form-input w-full rounded-md border-gray-300 focus:border-indigo-500 focus:ring-indigo-500"
              >
            </div>
            
            <div>
              <label for="state" class="block text-sm font-medium text-gray-700 mb-1">Tỉnh/Thành phố</label>
              <input
                type="text"
                id="state"
                v-model="formData.address.state"
                class="form-input w-full rounded-md border-gray-300 focus:border-indigo-500 focus:ring-indigo-500"
              >
            </div>
            
            <div>
              <label for="postalCode" class="block text-sm font-medium text-gray-700 mb-1">Mã bưu điện</label>
              <input
                type="text"
                id="postalCode"
                v-model="formData.address.postalCode"
                class="form-input w-full rounded-md border-gray-300 focus:border-indigo-500 focus:ring-indigo-500"
              >
            </div>
            
            <div class="col-span-2">
              <label for="country" class="block text-sm font-medium text-gray-700 mb-1">Quốc gia</label>
              <input
                type="text"
                id="country"
                v-model="formData.address.country"
                class="form-input w-full rounded-md border-gray-300 focus:border-indigo-500 focus:ring-indigo-500"
              >
            </div>
          </div>
        </div>
        
        <!-- Form Actions -->
        <div class="flex justify-end space-x-3 pt-4">
          <button
            type="button"
            @click="$router.go(-1)"
            class="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none"
          >
            Hủy
          </button>
          <button
            type="submit"
            class="inline-flex items-center justify-center px-4 py-2 border border-transparent text-sm font-medium rounded-md text-white bg-[#6c584c] hover:bg-[#5a483e] focus:outline-none min-w-[120px] h-10"
            :disabled="isSubmitting"
            :class="{'opacity-50 cursor-not-allowed': isSubmitting}"
          >
            <span v-if="isSubmitting" class="flex items-center">
              <svg class="animate-spin -ml-1 mr-2 h-4 w-4 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
              </svg>
              Đang lưu...
            </span>
            <span v-else>Lưu thông tin</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import BreadcrumbDefault from '@/components/custom/Div/BreadcrumbDefault.vue';

export default defineComponent({
  name: 'CustomerCreate',
  components: {
    BreadcrumbDefault
  },
  setup() {
    const router = useRouter();
    const isSubmitting = ref(false);
    
    const breadcrumbItems = [
      { title: 'Trang chủ', to: { name: 'admin-dashboard' } },
      { title: 'Quản lý khách hàng', to: { name: 'admin-customers' } },
      { title: 'Thêm mới' }
    ];
    
    const formData = reactive({
      fullName: '',
      email: '',
      phone: '',
      status: 'active',
      address: {
        street: '',
        city: '',
        state: '',
        postalCode: '',
        country: 'Việt Nam'
      }
    });
    
    const handleSubmit = async () => {
      try {
        isSubmitting.value = true;
        // TODO: Implement API call to create customer
        console.log('Creating customer:', formData);
        
        // Simulate API call
        await new Promise(resolve => setTimeout(resolve, 1000));
        
        // Show success message
        // You might want to use a toast/notification component here
        
        // Redirect to customer list after successful creation
        await router.push({ name: 'admin-customers' });
        
      } catch (error) {
        console.error('Failed to create customer:', error);
        // You might want to show an error message to the user
      } finally {
        isSubmitting.value = false;
      }
    };
    
    return {
      breadcrumbItems,
      formData,
      isSubmitting,
      handleSubmit
    };
  }
});
</script>
