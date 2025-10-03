<template>
  <!-- Overlay -->
  <div class="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50">
    <!-- Modal -->
    <div class="bg-white rounded-lg shadow-lg w-full max-w-lg">
      <!-- Header -->
      <div class="flex justify-between items-center px-6 py-4 border-b border-gray-200">
        <h3 class="text-lg font-semibold text-gray-800">Thêm sản phẩm</h3>
        <button @click="$emit('close')" class="text-gray-400 hover:text-gray-600">
          ✕
        </button>
      </div>

      <!-- Body -->
      <div class="px-6 py-4 space-y-4">
        <div>
          <label class="block text-sm font-medium text-gray-700">Tên sản phẩm</label>
          <input v-model="form.name" type="text"
                 class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:ring-pink-500 focus:border-pink-500 sm:text-sm" />
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700">Mã SP</label>
          <input v-model="form.sku" type="text"
                 class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:ring-pink-500 focus:border-pink-500 sm:text-sm" />
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700">Giá</label>
          <input v-model="form.price" type="number"
                 class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:ring-pink-500 focus:border-pink-500 sm:text-sm" />
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700">Tồn kho</label>
          <input v-model="form.stock" type="number"
                 class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:ring-pink-500 focus:border-pink-500 sm:text-sm" />
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700">Trạng thái</label>
          <select v-model="form.status"
                  class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:ring-pink-500 focus:border-pink-500 sm:text-sm">
            <option value="active">Đang bán</option>
            <option value="inactive">Ngừng bán</option>
          </select>
        </div>
      </div>

      <!-- Footer -->
      <div class="flex justify-end space-x-3 px-6 py-4 border-t border-gray-200">
        <button @click="$emit('close')"
                class="px-4 py-2 rounded-md text-sm font-medium text-gray-700 bg-gray-100 hover:bg-gray-200">
          Hủy
        </button>
        <button @click="submitForm"
                class="px-4 py-2 rounded-md text-sm font-medium text-white bg-pink-600 hover:bg-pink-700">
          Lưu
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive } from "vue"

const emit = defineEmits(["close", "created"])

const form = reactive({
  name: "",
  sku: "",
  price: 0,
  stock: 0,
  status: "active",
})

const submitForm = () => {
  emit("created", { ...form })
  emit("close")
}
</script>
