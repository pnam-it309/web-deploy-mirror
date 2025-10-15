<template>
  <!-- Overlay -->
  <div class="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50">
    <!-- Modal -->
    <div class="bg-white rounded-lg shadow-lg w-full max-w-xl text-black">
      <!-- Header -->
      <div class="flex justify-between items-center px-6 py-4 border-b">
        <h3 class="text-lg font-semibold text-black">Thêm khách hàng</h3>
        <button @click="closeModal" class="text-black hover:text-gray-700">✕</button>
      </div>

      <!-- Body -->
      <div class="px-6 py-6">
        <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
          <div class="flex flex-col">
            <label class="block text-sm font-medium text-black mb-1">Mã khách hàng</label>
            <input v-model.trim="form.code" type="text" class="input" placeholder="Nhập mã..." />
          </div>
          <div class="flex flex-col">
            <label class="block text-sm font-medium text-black mb-1">Họ tên</label>
            <input v-model.trim="form.name" type="text" class="input" placeholder="Nhập họ tên..." />
          </div>
          <div class="flex flex-col">
            <label class="block text-sm font-medium text-black mb-1">Email</label>
            <input v-model.trim="form.email" type="email" class="input" placeholder="Nhập email..." />
          </div>
          <div class="flex flex-col">
            <label class="block text-sm font-medium text-black mb-1">Ảnh đại diện (URL)</label>
            <input v-model.trim="form.picture" type="text" class="input" placeholder="https://..." />
          </div>
        </div>
      </div>

      <!-- Footer -->
      <div class="flex justify-end gap-3 px-6 py-4 border-t">
        <button @click="closeModal"
                class="px-5 py-2 bg-gray-100 hover:bg-gray-200 rounded text-black font-medium">
          Hủy
        </button>
        <button @click="submitForm"
                class="px-5 py-2 bg-pink-600 hover:bg-pink-700 text-white rounded font-medium">
          Lưu
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive } from 'vue'
import { adminCustomerApi } from '@/services/api/admin/customer.api'

const emit = defineEmits(['close', 'created'])

type CustomerCreateRequest = {
  code: string
  name: string
  email: string
  picture: string
}

const form = reactive<CustomerCreateRequest>({
  code: '',
  name: '',
  email: '',
  picture: ''
})

const submitForm = async () => {
  if (!form.name || !form.email) {
    alert('⚠️ Vui lòng nhập đầy đủ Họ tên và Email!')
    return
  }

  try {
    const res = await adminCustomerApi.create(form)
    emit('created', res)
    alert('🎉 Thêm khách hàng thành công!')
    emit('close')
  } catch (e) {
    alert('❌ Lỗi khi tạo khách hàng')
  }
}

const closeModal = () => {
  emit('close')
}
</script>

<style scoped>
.input {
  width: 100%;
  padding: 0.5rem 0.75rem;
  border: 1px solid #d1d5db; /* gray-300 */
  border-radius: 0.375rem; /* rounded-md */
  font-size: 0.95rem;
  line-height: 1.25rem;
  color: #000;
  outline: none;
  transition: border 0.2s, box-shadow 0.2s;
}

.input:focus {
  border-color: #ec4899; /* pink-500 */
  box-shadow: 0 0 0 3px rgba(236,72,153,0.2);
}

textarea.input {
  padding-top: 0.5rem;
  padding-bottom: 0.5rem;
}

button {
  transition: all 0.2s;
}
</style>
