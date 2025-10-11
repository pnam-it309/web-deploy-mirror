<template>
  <div class="p-6">
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-2xl font-semibold text-gray-900">Admin Dashboard</h1>
      <div class="space-x-2">
        <button 
          @click="goToLogin"
          class="px-4 py-2 bg-gray-100 text-gray-700 rounded-md hover:bg-gray-200"
        >
          Đăng xuất
        </button>
        <button 
          @click="goToHome"
          class="px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600"
        >
          Về trang chủ
        </button>
      </div>
    </div>
    <div class="mt-6 grid grid-cols-1 gap-5 sm:grid-cols-2 lg:grid-cols-4">
      <!-- Stats Cards -->
      <div 
        v-for="stat in stats" 
        :key="stat.name"
        class="bg-white overflow-hidden shadow rounded-lg"
      >
        <div class="px-4 py-5 sm:p-6">
          <dt class="text-sm font-medium text-gray-500 truncate">
            {{ stat.name }}
          </dt>
          <dd class="mt-1 text-3xl font-semibold text-gray-900">
            {{ stat.value }}
          </dd>
          <div 
            v-if="stat.change"
            :class="[
              stat.changeType === 'increase' ? 'text-green-600' : 'text-red-600',
              'mt-2 text-sm font-medium'
            ]"
          >
            {{ stat.change }}
            <span class="sr-only">
              {{ stat.changeType === 'increase' ? 'Increased' : 'Decreased' }} by
            </span>
          </div>
        </div>
      </div>
    </div>

    <!-- Recent Activity -->
    <div class="mt-8">
      <h2 class="text-lg font-medium text-gray-900">Recent Activity</h2>
      <div class="mt-4 bg-white shadow overflow-hidden sm:rounded-md">
        <ul class="divide-y divide-gray-200">
          <li v-for="(activity, index) in recentActivity" :key="index">
            <div class="px-4 py-4 sm:px-6">
              <div class="flex items-center justify-between">
                <p class="text-sm font-medium text-indigo-600 truncate">
                  {{ activity.title }}
                </p>
                <div class="ml-2 flex-shrink-0 flex">
                  <p class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-green-100 text-green-800">
                    {{ activity.status }}
                  </p>
                </div>
              </div>
              <div class="mt-2 sm:flex sm:justify-between">
                <div class="sm:flex">
                  <p class="flex items-center text-sm text-gray-500">
                    {{ activity.description }}
                  </p>
                </div>
                <div class="mt-2 flex items-center text-sm text-gray-500 sm:mt-0">
                  <p>
                    {{ activity.time }}
                  </p>
                </div>
              </div>
            </div>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth';

const router = useRouter();
const authStore = useAuthStore();

const goToLogin = async () => {
  await authStore.logout();
  router.push('/auth/login?role=admin');
};

const goToHome = () => {
  window.location.href = '/';
};

// Mock data for dashboard
const stats = ref([
  { name: 'Tổng sản phẩm', value: '1,234', change: '12%', changeType: 'increase' },
  { name: 'Đơn hàng', value: '567', change: '5%', changeType: 'increase' },
  { name: 'Khách hàng', value: '890', change: '8%', changeType: 'decrease' },
  { name: 'Doanh thu', value: '$12,345', change: '15%', changeType: 'increase' },
  { name: 'Total Users', value: '2,345', change: '12%', changeType: 'increase' },
  { name: 'Total Orders', value: '1,234', change: '5.4%', changeType: 'increase' },
  { name: 'Total Revenue', value: '$24,567', change: '3.2%', changeType: 'decrease' },
  { name: 'Active Products', value: '156', change: '2.1%', changeType: 'increase' },
]);

const recentActivity = ref([
  {
    title: 'New order #1234',
    description: 'Customer: John Doe',
    status: 'Completed',
    time: '2h ago'
  },
  {
    title: 'New user registered',
    description: 'jane.doe@example.com',
    status: 'Active',
    time: '4h ago'
  },
  {
    title: 'Product out of stock',
    description: 'Product: Premium Headphones',
    status: 'Warning',
    time: '1d ago'
  },
]);
</script>
