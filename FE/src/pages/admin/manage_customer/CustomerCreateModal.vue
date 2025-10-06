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
      <div class="px-6 py-6 space-y-5">
        <div class="flex flex-col">
          <label class="block text-sm font-medium text-black mb-1">Username</label>
          <input v-model.trim="form.username" type="text" class="input" placeholder="Nhập username..." />
        </div>

        <div class="flex flex-col">
          <label class="block text-sm font-medium text-black mb-1">Email</label>
          <input v-model.trim="form.email" type="email" class="input" placeholder="Nhập email..." />
        </div>

        <div class="flex flex-col">
          <label class="block text-sm font-medium text-black mb-1">Họ tên</label>
          <input v-model.trim="form.full_name" type="text" class="input" placeholder="Nhập họ tên..." />
        </div>

        <div class="flex flex-col">
          <label class="block text-sm font-medium text-black mb-1">Số điện thoại</label>
          <input v-model.trim="form.phone" type="text" class="input" placeholder="Nhập số điện thoại..." />
        </div>

        <div class="flex flex-col">
          <label class="block text-sm font-medium text-black mb-1">Địa chỉ</label>
          <textarea v-model.trim="form.address" class="input h-24 resize-none" placeholder="Nhập địa chỉ..."></textarea>
        </div>

        <div class="flex flex-col">
          <label class="block text-sm font-medium text-black mb-1">Trạng thái</label>
          <select v-model="form.is_active" class="input">
            <option :value="true">Hoạt động</option>
            <option :value="false">Khoá</option>
          </select>
        </div>
      </div>

      <!-- Footer -->
      <div class="flex justify-end space-x-3 px-6 py-4 border-t">
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

const emit = defineEmits(['close', 'created'])

const form = reactive({
  id: 0,
  username: '',
  email: '',
  full_name: '',
  phone: '',
  address: '',
  is_active: true,
  is_customer: true,
  created_at: '',
  updated_at: ''
})

const submitForm = () => {
  if (!form.username || !form.email) {
    alert('⚠️ Vui lòng nhập đầy đủ Username và Email!')
    return
  }

  const now = new Date().toISOString()
  const newCustomer = {
    ...form,
    id: Date.now(),
    created_at: now,
    updated_at: now
  }

  emit('created', newCustomer)
  alert('🎉 Thêm khách hàng thành công!')
  emit('close')
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
