<template>
  <div class="profile-container">
    <div class="max-w-4xl mx-auto p-6">
      <h1 class="text-2xl font-bold text-gray-800 mb-6">Thông tin cá nhân</h1>
      
      <!-- Profile Information -->
      <div class="bg-white rounded-lg shadow-md p-6 mb-6">
        <div class="flex flex-col md:flex-row items-center md:items-start gap-6">
          <!-- Avatar Section -->
          <div class="flex-shrink-0 text-center">
            <div class="relative w-32 h-32 rounded-full bg-gray-200 flex items-center justify-center overflow-hidden">
              <span class="text-4xl text-gray-500">
                {{ userInitials }}
              </span>
            </div>
            <button class="mt-3 text-sm text-blue-600 hover:text-blue-800">
              Thay đổi ảnh đại diện
            </button>
          </div>
          
          <!-- User Details -->
          <div class="flex-1 w-full">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div class="form-group">
                <label class="block text-sm font-medium text-gray-700 mb-1">Họ và tên</label>
                <input 
                  type="text" 
                  v-model="userInfo.fullName" 
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                >
              </div>
              
              <div class="form-group">
                <label class="block text-sm font-medium text-gray-700 mb-1">Email</label>
                <input 
                  type="email" 
                  v-model="userInfo.email" 
                  disabled
                  class="w-full px-3 py-2 border border-gray-300 rounded-md bg-gray-100 text-gray-500"
                >
              </div>
              
              <div class="form-group">
                <label class="block text-sm font-medium text-gray-700 mb-1">Số điện thoại</label>
                <input 
                  type="tel" 
                  v-model="userInfo.phone" 
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                >
              </div>
              
              <div class="form-group">
                <label class="block text-sm font-medium text-gray-700 mb-1">Ngày sinh</label>
                <input 
                  type="date" 
                  v-model="userInfo.dob" 
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                >
              </div>
              
              <div class="form-group md:col-span-2">
                <label class="block text-sm font-medium text-gray-700 mb-1">Địa chỉ</label>
                <textarea 
                  v-model="userInfo.address" 
                  rows="2"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                ></textarea>
              </div>
            </div>
            
            <div class="mt-6 flex justify-end">
              <button 
                @click="saveProfile"
                class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
              >
                Lưu thay đổi
              </button>
            </div>
          </div>
        </div>
      </div>
      
      <!-- Change Password Section -->
      <div class="bg-white rounded-lg shadow-md p-6">
        <h2 class="text-xl font-semibold text-gray-800 mb-4">Đổi mật khẩu</h2>
        
        <div class="space-y-4 max-w-lg">
          <div class="form-group">
            <label class="block text-sm font-medium text-gray-700 mb-1">Mật khẩu hiện tại</label>
            <input 
              type="password" 
              v-model="password.current" 
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
          </div>
          
          <div class="form-group">
            <label class="block text-sm font-medium text-gray-700 mb-1">Mật khẩu mới</label>
            <input 
              type="password" 
              v-model="password.new" 
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
          </div>
          
          <div class="form-group">
            <label class="block text-sm font-medium text-gray-700 mb-1">Xác nhận mật khẩu mới</label>
            <input 
              type="password" 
              v-model="password.confirm" 
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
          </div>
          
          <div class="pt-2">
            <button 
              @click="changePassword"
              class="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500 focus:ring-offset-2"
            >
              Đổi mật khẩu
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed } from 'vue'
import { useAuthStore } from '@/stores/auth'

export default defineComponent({
  name: 'CustomerProfile',
  setup() {
    const authStore = useAuthStore()
    
    // Mock user data - in a real app, this would come from your auth store or API
    const userInfo = ref({
      fullName: authStore.user?.name || 'Nguyễn Văn A',
      email: authStore.user?.email || 'user@example.com',
      phone: '0987654321',
      dob: '1990-01-01',
      address: 'Số 1, Đường ABC, Quận 1, TP. Hồ Chí Minh'
    })
    
    const password = ref({
      current: '',
      new: '',
      confirm: ''
    })
    
    // Compute user initials for avatar
    const userInitials = computed(() => {
      return userInfo.value.fullName
        .split(' ')
        .map(n => n[0])
        .join('')
        .toUpperCase()
        .substring(0, 2)
    })
    
    const saveProfile = () => {
      // TODO: Implement profile update logic
      console.log('Saving profile:', userInfo.value)
      // Example API call:
      // await updateProfile(userInfo.value)
    }
    
    const changePassword = () => {
      if (password.value.new !== password.value.confirm) {
        alert('Mật khẩu mới không khớp')
        return
      }
      
      // TODO: Implement password change logic
      console.log('Changing password:', password.value)
      // Example API call:
      // await changePassword({
      //   currentPassword: password.value.current,
      //   newPassword: password.value.new
      // })
    }
    
    return {
      userInfo,
      password,
      userInitials,
      saveProfile,
      changePassword
    }
  }
})
</script>

<style scoped>
.profile-container {
  min-height: calc(100vh - 160px);
  padding: 2rem 0;
}

.form-group {
  margin-bottom: 1rem;
}
</style>
