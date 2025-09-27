<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-50 py-12 px-4 sm:px-6 lg:px-8">
    <div class="max-w-md w-full space-y-8">
      <div>
        <h2 class="mt-6 text-center text-3xl font-extrabold text-gray-900">
          Tạo tài khoản mới
        </h2>
        <p class="mt-2 text-center text-sm text-gray-600">
          Đã có tài khoản?
          <router-link 
            :to="{ name: 'login' }"
            class="font-medium text-indigo-600 hover:text-indigo-500"
          >
            Đăng nhập ngay
          </router-link>
        </p>
      </div>

      <div class="mt-8">
        <div class="bg-white py-8 px-4 shadow sm:rounded-lg sm:px-10">
          <form class="space-y-6" @submit.prevent="handleRegister">
            <div>
              <label for="email" class="block text-sm font-medium text-gray-700">
                Email
              </label>
              <div class="mt-1">
                <input
                  id="email"
                  v-model="form.email"
                  name="email"
                  type="email"
                  autocomplete="email"
                  required
                  class="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                />
              </div>
            </div>

            <div>
              <label for="password" class="block text-sm font-medium text-gray-700">
                Mật khẩu
              </label>
              <div class="mt-1">
                <input
                  id="password"
                  v-model="form.password"
                  name="password"
                  type="password"
                  autocomplete="new-password"
                  required
                  class="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                />
              </div>
            </div>

            <div>
              <label for="confirm-password" class="block text-sm font-medium text-gray-700">
                Xác nhận mật khẩu
              </label>
              <div class="mt-1">
                <input
                  id="confirm-password"
                  v-model="form.confirmPassword"
                  name="confirm-password"
                  type="password"
                  autocomplete="new-password"
                  required
                  class="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                />
              </div>
            </div>

            <div>
              <button
                type="submit"
                :disabled="loading"
                class="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                <span v-if="!loading">Đăng ký</span>
                <span v-else>Đang xử lý...</span>
              </button>
            </div>
          </form>

          <div class="mt-6">
            <div class="relative">
              <div class="absolute inset-0 flex items-center">
                <div class="w-full border-t border-gray-300"></div>
              </div>
              <div class="relative flex justify-center text-sm">
                <span class="px-2 bg-white text-gray-500">Hoặc đăng ký bằng</span>
              </div>
            </div>

            <div class="mt-6 grid grid-cols-1 gap-3">
              <button
                @click="loginWithGoogle"
                type="button"
                class="w-full inline-flex justify-center py-2 px-4 border border-gray-300 rounded-md shadow-sm bg-white text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
              >
                <svg class="w-5 h-5" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M12.545,10.239v3.821h5.445c-0.712,2.315-2.647,3.972-5.445,3.972c-3.332,0-6.033-2.701-6.033-6.032s2.701-6.032,6.033-6.032c1.498,0,2.866,0.549,3.921,1.453l2.814-2.814C17.503,2.988,15.139,2,12.545,2C7.021,2,2.543,6.477,2.543,12s4.478,10,10.002,10c8.396,0,10.249-7.85,9.426-11.748L12.545,10.239z"/>
                </svg>
                <span class="ml-2">Tiếp tục với Google</span>
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth';
import { GoogleAuthProvider, signInWithPopup } from 'firebase/auth';
import { auth } from '@/firebase';

export default defineComponent({
  name: 'RegisterPage',
  setup() {
    const router = useRouter();
    const authStore = useAuthStore();
    const loading = ref(false);
    const error = ref('');

    const form = reactive({
      email: '',
      password: '',
      confirmPassword: '',
    });

    const handleRegister = async () => {
      if (form.password !== form.confirmPassword) {
        error.value = 'Mật khẩu xác nhận không khớp';
        return;
      }

      try {
        loading.value = true;
        error.value = '';
        
        // Call your registration API here
        // await authStore.register({
        //   email: form.email,
        //   password: form.password,
        // });
        
        // For now, just redirect to login
        await router.push({ 
          name: 'login',
          query: { registered: 'true' } 
        });
      } catch (err: any) {
        console.error('Registration error:', err);
        error.value = err.message || 'Đăng ký thất bại. Vui lòng thử lại.';
      } finally {
        loading.value = false;
      }
    };

    const loginWithGoogle = async () => {
      try {
        loading.value = true;
        error.value = '';
        
        const provider = new GoogleAuthProvider();
        const result = await signInWithPopup(auth, provider);
        const user = result.user;
        
        // Register or login the user with your backend
        await authStore.login({
          user: {
            id: user.uid,
            email: user.email,
            name: user.displayName,
            avatar: user.photoURL
          },
          accessToken: await user.getIdToken(),
          refreshToken: user.refreshToken
        });
        
        // Redirect to role selection
        await router.push({ name: 'select-role' });
      } catch (err: any) {
        console.error('Google login error:', err);
        error.value = 'Đăng nhập bằng Google thất bại. Vui lòng thử lại.';
      } finally {
        loading.value = false;
      }
    };

    return {
      form,
      loading,
      error,
      handleRegister,
      loginWithGoogle,
    };
  },
});
</script>

<style scoped>
/* Add any custom styles here */
</style>
