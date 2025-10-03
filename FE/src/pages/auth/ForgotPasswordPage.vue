<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-50 py-12 px-4 sm:px-6 lg:px-8">
    <div class="max-w-md w-full space-y-8">
      <div>
        <h2 class="mt-6 text-center text-3xl font-extrabold text-gray-900">
          Reset your password
        </h2>
        <p class="mt-2 text-center text-sm text-gray-600">
          Or
          {{ ' ' }}
          <router-link to="/auth/login" class="font-medium text-indigo-600 hover:text-indigo-500">
            sign in to your account
          </router-link>
        </p>
      </div>
      
      <form class="mt-8 space-y-6" @submit.prevent="handleSubmit">
        <div class="rounded-md shadow-sm -space-y-px">
          <div>
            <label for="email-address" class="sr-only">Email address</label>
            <input
              id="email-address"
              v-model="email"
              name="email"
              type="email"
              autocomplete="email"
              required
              class="appearance-none rounded-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-t-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 focus:z-10 sm:text-sm"
              placeholder="Email address"
            >
          </div>
        </div>

        <div>
          <button
            type="submit"
            :disabled="isLoading"
            class="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            <span v-if="!isLoading">Send reset link</span>
            <span v-else>Sending...</span>
          </button>
        </div>
        
        <div v-if="error" class="text-sm text-red-600 text-center">
          {{ error }}
        </div>
        
        <div v-if="message" class="text-sm text-green-600 text-center">
          {{ message }}
        </div>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const email = ref('');
const isLoading = ref(false);
const error = ref('');
const message = ref('');

const handleSubmit = async () => {
  if (!email.value) {
    error.value = 'Please enter your email address';
    return;
  }

  try {
    isLoading.value = true;
    error.value = '';
    message.value = '';
    
    // TODO: Implement password reset logic
    // await authStore.forgotPassword(email.value);
    
    message.value = 'If an account exists with this email, you will receive a password reset link.';
    email.value = '';
  } catch (err: any) {
    error.value = err.message || 'Failed to send reset link. Please try again.';
  } finally {
    isLoading.value = false;
  }
};
</script>
