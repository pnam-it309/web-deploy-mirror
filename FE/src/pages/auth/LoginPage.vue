<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-50 py-12 px-4 sm:px-6 lg:px-8">
    <div class="max-w-md w-full space-y-8">
      <div>
        <div class="text-center">
          <h1 class="text-4xl font-bold text-indigo-600">UDPM</h1>
          <h2 class="mt-2 text-2xl font-semibold text-gray-900">
            Đăng nhập vào tài khoản
          </h2>
        </div>
      </div>
      
      <!-- Error Message -->
      <div v-if="error" class="bg-red-50 border-l-4 border-red-400 p-4">
        <div class="flex">
          <div class="flex-shrink-0">
            <XCircleIcon class="h-5 w-5 text-red-400" aria-hidden="true" />
          </div>
          <div class="ml-3">
            <p class="text-sm text-red-700">{{ error }}</p>
          </div>
        </div>
      </div>

      <!-- Google Sign In Button -->
      <div class="mt-6">
        <button
          @click="signInWithGoogle"
          type="button"
          class="w-full flex justify-center py-2 px-4 border border-gray-300 rounded-md shadow-sm bg-white text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
          :disabled="isLoading"
        >
          <img class="h-5 w-5 mr-2" src="https://www.google.com/favicon.ico" alt="Google" />
          <span>Đăng nhập với Google</span>
        </button>
      </div>

      <!-- Divider -->
      <div class="relative mt-6">
        <div class="absolute inset-0 flex items-center">
          <div class="w-full border-t border-gray-300"></div>
        </div>
        <div class="relative flex justify-center text-sm">
          <span class="px-2 bg-gray-50 text-gray-500">Hoặc đăng nhập bằng email</span>
        </div>
      </div>

      <div class="mt-6 grid grid-cols-1 gap-3">
        <button
          @click="loginWithGoogle"
          type="button"
          class="w-full flex justify-center py-2 px-4 border border-gray-300 rounded-md shadow-sm bg-white text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
        >
          <svg class="w-5 h-5 mr-2" viewBox="0 0 24 24" width="24" height="24" xmlns="http://www.w3.org/2000/svg">
            <g transform="matrix(1, 0, 0, 1, 27.009001, -39.238998)">
              <path d="M -3.264 51.891 C -3.254 51.597 -3.223 51.301 -3.223 51 C -3.223 50.699 -3.254 50.403 -3.264 50.11 L -3.264 50.11 Z" fill="#4285F4"/>
              <path d="M -14.469 39.239 L -14.469 39.239 L -14.469 39.239 C -18.089 39.239 -21.229 40.77 -23.595 43.333 L -23.595 43.333 L -16.085 50.11 C -14.814 47.32 -12.468 45.252 -9.63 44.362 L -9.63 44.362 Z" fill="#34A853"/>
              <path d="M -0.8 24 C -0.8 22.29 -1.1 20.67 -1.64 19.15 L -1.64 19.15 L -9.63 26.64 C -10.39 28.33 -10.8 30.15 -10.8 32 C -10.8 33.85 -10.39 35.67 -9.63 37.36 L -1.64 44.85 C -1.1 43.33 -0.8 41.71 -0.8 40 C -0.8 33.4 -5.4 27.89 -11.54 26.25 L -11.54 26.25 L -16.085 19.15 C -20.5 21.3 -23.2 25.45 -23.2 30 C -23.2 34.55 -20.5 38.7 -16.085 40.85 L -16.085 40.85 L -9.63 44.85 C -2.2 42.15 3.2 34.45 3.2 30 C 3.2 29.29 3.15 28.6 3.06 27.92 L 3.06 27.92 L -0.8 24 Z" fill="#FBBC05"/>
              <path d="M -11.54 13.75 L -11.54 13.75 L -11.54 13.75 C -9.85 13.29 -8.01 13.04 -6.08 13.04 C -2.71 13.04 0.24 14.29 2.8 16.29 L 2.8 16.29 L 7.38 11.71 C 4.69 9.23 1.19 7.5 -2.64 7.5 C -8.24 7.5 -13.17 10.58 -15.54 15.07 L -9.63 20.5 C -8.52 17.56 -6.12 15.25 -3.26 14.25 L -3.26 14.25 L -11.54 13.75 Z" fill="#EA4335"/>
            </g>
          </svg>
          Tiếp tục với Google
        </button>
      </div>

      <form class="mt-8 space-y-6" @submit.prevent="handleLogin">
        <div class="rounded-md shadow-sm -space-y-px">
          <div>
            <label for="email-address" class="sr-only">Địa chỉ email</label>
            <input
              id="email-address"
              v-model="form.email"
              name="email"
              type="email"
              autocomplete="email"
              required
              class="appearance-none rounded-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-t-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 focus:z-10 sm:text-sm"
              placeholder="Địa chỉ email"
            >
          </div>
          <div>
            <label for="password" class="sr-only">Mật khẩu</label>
            <input
              id="password"
              v-model="form.password"
              name="password"
              type="password"
              autocomplete="current-password"
              required
              class="appearance-none rounded-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-b-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 focus:z-10 sm:text-sm"
              placeholder="Mật khẩu"
            >
          </div>
        </div>

        <div class="flex items-center justify-between">
          <div class="flex items-center">
            <input
              id="remember-me"
              v-model="form.remember"
              name="remember-me"
              type="checkbox"
              class="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded"
            >
            <label for="remember-me" class="ml-2 block text-sm text-gray-900">
              Ghi nhớ đăng nhập
            </label>
          </div>

          <div class="text-sm">
            <a href="#" class="font-medium text-indigo-600 hover:text-indigo-500">
              Quên mật khẩu?
            </a>
          </div>
        </div>

        <div>
          <button
            type="submit"
            class="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
            :disabled="loading"
          >
            <span class="absolute left-0 inset-y-0 flex items-center pl-3">
              <svg class="h-5 w-5 text-indigo-500 group-hover:text-indigo-400" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
                <path fill-rule="evenodd" d="M5 9V7a5 5 0 0110 0v2a2 2 0 012 2v5a2 2 0 01-2 2H5a2 2 0 01-2-2v-5a2 2 0 012-2zm8-2v2H7V7a3 3 0 016 0z" clip-rule="evenodd" />
              </svg>
            </span>
            {{ loading ? 'Đang đăng nhập...' : 'Đăng nhập' }}
          </button>
        </div>
      </form>

      <!-- Quick Navigation Buttons -->
      <div class="mt-8 pt-6 border-t border-gray-200">
        <p class="text-center text-sm text-gray-500 mb-4">
          Hoặc truy cập nhanh:
        </p>
        <div class="grid grid-cols-1 gap-3 sm:grid-cols-2">
          <button
            @click="navigateToAdmin"
            type="button"
            class="w-full flex items-center justify-center px-4 py-2 border border-transparent text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z" />
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
            </svg>
            Admin Dashboard
          </button>
          <button
            @click="navigateToCustomer"
            type="button"
            class="w-full flex items-center justify-center px-4 py-2 border border-transparent text-sm font-medium rounded-md text-indigo-700 bg-indigo-100 hover:bg-indigo-200 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
            </svg>
            Customer Dashboard
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { XCircleIcon } from '@heroicons/vue/24/outline'
import { useAuthStore } from '@/stores/auth'
import { googleAuthProvider } from '@/stores/auth'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()

const error = ref('')
const isLoading = ref(false)

const form = {
  email: '',
  password: '',
  remember: false,
};

const signInWithGoogle = async () => {
  // Directly navigate to admin dashboard
  window.location.href = '/admin/dashboard'
}

const navigateToAdmin = () => {
  // Direct navigation to admin dashboard
  window.location.href = '/admin/dashboard'
}

const navigateToCustomer = () => {
  // Direct navigation to customer dashboard
  window.location.href = '/customer/dashboard'
}

const handleLogin = async () => {
  // Directly navigate to admin dashboard
  window.location.href = '/admin/dashboard'
}
</script>

<style scoped>
  /* Add your styles here */
</style>
