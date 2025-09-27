<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-50 py-12 px-4 sm:px-6 lg:px-8">
    <div class="max-w-md w-full space-y-8">
      <div>
        <h2 class="mt-6 text-center text-3xl font-extrabold text-gray-900">
          Đăng nhập vào tài khoản
        </h2>
        <p class="mt-2 text-center text-sm text-gray-600">
          Hoặc
          {{ ' ' }}
          <router-link to="/register" class="font-medium text-indigo-600 hover:text-indigo-500">
            đăng ký tài khoản mới
          </router-link>
        </p>
      </div>
      
      <div v-if="error" class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative" role="alert">
        <span class="block sm:inline">{{ error }}</span>
      </div>

      <!-- Social Login Buttons -->
      <div class="mt-6">
        <div class="relative">
          <div class="absolute inset-0 flex items-center">
            <div class="w-full border-t border-gray-300"></div>
          </div>
          <div class="relative flex justify-center text-sm">
            <span class="px-2 bg-gray-50 text-gray-500">Hoặc đăng nhập bằng</span>
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
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { GoogleAuthProvider, signInWithPopup } from 'firebase/auth'
import { auth } from '@/firebase';

export default defineComponent({
  name: 'LoginPage',
  setup() {
    const authStore = useAuthStore();
    const router = useRouter();
    const loading = ref(false);
    const error = ref('');

    const form = reactive({
      email: '',
      password: '',
      remember: false
    });

    const handleLogin = async () => {
      if (!form.email || !form.password) {
        error.value = 'Vui lòng nhập đầy đủ thông tin đăng nhập';
        return;
      }

      try {
        loading.value = true;
        error.value = '';
        
        // Call your authentication API here
        // For example:
        // await authStore.login({
        //   email: form.email,
        //   password: form.password
        // });
        
        // For now, we'll simulate a successful login
        await new Promise(resolve => setTimeout(resolve, 1000));
        
        // Redirect based on user role
        const userRole = 'admin'; // This should come from your auth response
        if (userRole === 'admin') {
          await router.push({ name: 'admin-dashboard' });
        } else {
          await router.push({ name: 'home' });
        }
      } catch (err) {
        console.error('Login error:', err);
        error.value = 'Đăng nhập thất bại. Vui lòng kiểm tra lại thông tin đăng nhập.';
      } finally {
        loading.value = false;
      }
    };

    const loginWithGoogle = async () => {
      try {
        error.value = ''
        const provider = new GoogleAuthProvider()
        const result = await signInWithPopup(auth, provider)
        const user = result.user
        
        // Here you would typically send the Google token to your backend
        // and get your application's JWT token in response
        // For now, we'll simulate a successful login
        await authStore.login({
          user: {
            id: user.uid,
            email: user.email,
            name: user.displayName,
            avatar: user.photoURL
          },
          accessToken: await user.getIdToken(),
          refreshToken: user.refreshToken
        })
        
        // Redirect to role selection
        await router.push({ name: 'select-role' })
      } catch (err: any) {
        console.error('Google login error:', err)
        error.value = 'Đăng nhập bằng Google thất bại. Vui lòng thử lại.'
      }
    }

    return {
      form,
      loading,
      error,
      handleLogin,
      loginWithGoogle
    };
  }
});
</script>

<style scoped>
/* Add your styles here */
</style>
