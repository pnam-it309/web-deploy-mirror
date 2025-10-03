<template>
  <div class="p-6">
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-2xl font-semibold text-gray-900">Danh sách sản phẩm</h1>
      <router-link 
        to="/admin/products/create" 
        class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500"
      >
        Thêm sản phẩm
      </router-link>
    </div>

    <div class="bg-white shadow overflow-hidden sm:rounded-lg">
      <div class="px-4 py-5 sm:px-6">
        <p class="text-gray-500">Danh sách các sản phẩm trong cửa hàng</p>
      </div>
      <div class="border-t border-gray-200">
        <!-- Product list will be displayed here -->
        <div class="p-4 text-center text-gray-500">
          Danh sách sản phẩm sẽ được hiển thị tại đây
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
// Component logic will go here
</script>
