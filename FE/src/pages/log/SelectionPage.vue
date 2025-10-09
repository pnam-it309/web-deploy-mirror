<template>
  <div class="min-h-screen bg-gray-50 flex flex-col items-center justify-center p-4">
    <!-- Logo -->
    <div class="mb-12 text-center">
      <img 
        src="@/assets/images/logo-udpm-dark.png" 
        alt="UDPM Logo" 
        class="h-24 mx-auto mb-4"
      >
      <h1 class="text-3xl font-bold text-gray-900">Chào mừng đến với UDPM</h1>
      <p class="mt-2 text-gray-600">Vui lòng chọn chế độ đăng nhập</p>
    </div>

    <!-- Selection Buttons -->
    <div class="grid grid-cols-1 md:grid-cols-2 gap-8 w-full max-w-4xl">
      <!-- Admin Button -->
      <div 
        @click="navigateToAdmin"
        class="group relative bg-white p-6 rounded-xl shadow-md hover:shadow-lg transition-shadow duration-300 cursor-pointer border-2 border-transparent hover:border-indigo-500"
      >
        <div class="flex flex-col items-center text-center">
          <div class="bg-indigo-100 p-4 rounded-full mb-4 group-hover:bg-indigo-200 transition-colors">
            <img 
              src="@/assets/images/Admin.png" 
              alt="Admin" 
              class="h-20 w-20 object-contain"
            >
          </div>
          <h3 class="text-xl font-semibold text-gray-900 mb-2">Quản trị viên</h3>
          <p class="text-gray-600 text-sm">Truy cập trang quản trị hệ thống</p>
        </div>
      </div>

      <!-- Customer Button -->
      <div 
        @click="navigateToCustomer"
        class="group relative bg-white p-6 rounded-xl shadow-md hover:shadow-lg transition-shadow duration-300 cursor-pointer border-2 border-transparent hover:border-green-500"
      >
        <div class="flex flex-col items-center text-center">
          <div class="bg-green-100 p-4 rounded-full mb-4 group-hover:bg-green-200 transition-colors">
            <img 
              src="@/assets/images/Member.png" 
              alt="Customer" 
              class="h-20 w-20 object-contain"
            >
          </div>
          <h3 class="text-xl font-semibold text-gray-900 mb-2">Khách hàng</h3>
          <p class="text-gray-600 text-sm">Truy cập trang dành cho khách hàng</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router';

const router = useRouter();

const navigateToAdmin = () => {
  // Navigate to admin dashboard using Vue Router
  router.push({ name: 'admin-dashboard' });
}

const navigateToCustomer = () => {
  // Navigate to customer dashboard using Vue Router
  router.push({ name: 'customer-dashboard' });
}
</script>

<style scoped>
/* Add any custom styles here */
</style>
