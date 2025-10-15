<template>
  <div class="p-6">
    <h1 class="text-2xl font-semibold text-gray-900">My Dashboard</h1>

    <!-- Welcome Section -->
    <div class="mt-6 bg-indigo-700 rounded-lg shadow-xl overflow-hidden lg:grid lg:grid-cols-2 lg:gap-4">
      <div class="pt-10 pb-12 px-6 sm:pt-16 sm:px-16 lg:py-16 lg:pr-0 xl:py-20 xl:px-20">
        <div class="lg:self-center">
          <h2 class="text-3xl font-extrabold text-white sm:text-4xl">
            <span class="block">Welcome back, {{ userName }}!</span>
          </h2>
          <p class="mt-4 text-lg leading-6 text-indigo-200">
            Check out what's new in your account or start shopping for your next favorite product.
          </p>
          <div class="mt-8 flex flex-col sm:flex-row space-y-3 sm:space-y-0 sm:space-x-3">
            <router-link to="/catalog"
              class="flex items-center justify-center px-5 py-3 border border-transparent text-base font-medium rounded-md text-indigo-600 bg-white hover:bg-indigo-50">
              Start Shopping
            </router-link>
            <router-link to="/customer/orders"
              class="flex items-center justify-center px-5 py-3 border border-transparent text-base font-medium rounded-md text-white bg-indigo-600 bg-opacity-60 hover:bg-opacity-70">
              View Orders
            </router-link>
          </div>
        </div>
      </div>
      <div class="-mt-6 aspect-w-5 aspect-h-3 md:aspect-w-2 md:aspect-h-1">
        <img
          class="transform translate-x-6 translate-y-6 rounded-md object-cover object-left-top sm:translate-x-16 lg:translate-y-20"
          src="https://tailwindui.com/img/component-images/full-width-with-sidebar.jpg" alt="App screenshot">
      </div>
    </div>

    <!-- Quick Stats -->
    <div class="mt-8">
      <h2 class="text-lg font-medium text-gray-900">Quick Stats</h2>
      <div class="mt-4 grid grid-cols-1 gap-5 sm:grid-cols-2 lg:grid-cols-4">
        <div v-for="stat in stats" :key="stat.name" class="bg-white overflow-hidden shadow rounded-lg">
          <div class="px-4 py-5 sm:p-6">
            <dt class="text-sm font-medium text-gray-500 truncate">
              {{ stat.name }}
            </dt>
            <dd class="mt-1 text-3xl font-semibold text-gray-900">
              {{ stat.value }}
            </dd>
            <div class="mt-2">
              <span :class="[
                stat.changeType === 'increase' ? 'text-green-600' : 'text-red-600',
                'text-sm font-medium'
              ]">
                {{ stat.change }}
                <span class="sr-only">
                  {{ stat.changeType === 'increase' ? 'Increased' : 'Decreased' }} by
                </span>
              </span>
              <span class="text-sm text-gray-500 ml-2">
                {{ stat.period }}
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Recent Orders - Using Animated Table -->
    <div class="mt-8">
      <div class="flex justify-between items-center">
        <h2 class="text-lg font-medium text-gray-900">Recent Orders</h2>
        <router-link to="/customer/orders" class="text-sm font-medium text-indigo-600 hover:text-indigo-500">
          View all
        </router-link>
      </div>

      <div class="mt-4">
        <!-- Simple table first to test display -->
        <div class="bg-white shadow overflow-hidden sm:rounded-md">
          <div class="px-4 py-5 sm:px-6">
            <h3 class="text-lg leading-6 font-medium text-gray-900">Recent Orders (Testing)</h3>
            <p class="mt-1 max-w-2xl text-sm text-gray-500">Dữ liệu đơn hàng gần đây</p>
          </div>
          <div class="border-t border-gray-200">
            <dl>
              <div v-for="order in tableData" :key="order.id" class="bg-gray-50 px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6 hover:bg-gray-100 transition-colors cursor-pointer" @click="handleOrderClick(order)">
                <dt class="text-sm font-medium text-gray-500">Order #{{ order.orderNumber }}</dt>
                <dd class="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">{{ order.items }} items - {{ order.total }} - {{ order.status }}</dd>
              </div>
            </dl>
          </div>
        </div>

        <!-- AnimatedTable with debug -->
        <div class="mt-8">
          <h3 class="text-lg font-medium text-gray-900 mb-4">Animated Table (Debug)</h3>
          <AnimatedTable
            :data="tableData"
            :columns="tableColumns"
            :loading="false"
            :items-per-page="5"
            :show-pagination="false"
            :animation-config="{
              hoverScale: 1.02,
              hoverTransition: 'all 0.2s cubic-bezier(0.4, 0, 0.2, 1)',
              rowHoverColor: 'rgba(99, 102, 241, 0.1)',
              selectedRowColor: 'rgba(99, 102, 241, 0.2)',
              staggerDelay: 50,
            }"
            @row-click="handleOrderClick"
          />
        </div>
      </div>
    </div>

    <!-- Recommended Products -->
    <div class="mt-8">
      <h2 class="text-lg font-medium text-gray-900">Recommended For You</h2>
      <div class="mt-4 grid grid-cols-1 gap-y-10 gap-x-6 sm:grid-cols-2 lg:grid-cols-4 xl:gap-x-8">
        <div v-for="product in recommendedProducts" :key="product.id" class="group relative">
          <div
            class="w-full min-h-80 bg-gray-200 aspect-w-1 aspect-h-1 rounded-md overflow-hidden group-hover:opacity-75 lg:h-80 lg:aspect-none">
            <img :src="product.imageSrc" :alt="product.name"
              class="w-full h-full object-center object-cover lg:w-full lg:h-full">
          </div>
          <div class="mt-4 flex justify-between">
            <div>
              <h3 class="text-sm text-gray-700">
                <router-link :to="`/product/${product.id}`">
                  <span aria-hidden="true" class="absolute inset-0" />
                  {{ product.name }}
                </router-link>
              </h3>
              <p class="mt-1 text-sm text-gray-500">{{ product.color }}</p>
            </div>
            <p class="text-sm font-medium text-gray-900">{{ product.price }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import { useAuthStore } from '@/stores/auth';
import AnimatedTable from '@/components/AnimatedTable.vue';

const authStore = useAuthStore();

// User name from auth store
const userName = computed(() => {
  return authStore.user?.fullName || 'Customer';
});

// Table configuration for orders
const tableColumns = [
  { key: 'orderNumber', label: 'Order #', sortable: true },
  { key: 'date', label: 'Date', sortable: true },
  { key: 'items', label: 'Items', sortable: false },
  { key: 'total', label: 'Total', sortable: true },
  { key: 'status', label: 'Status', sortable: false },
];

const tableData = ref([
  {
    id: 1,
    orderNumber: 'WU88191139',
    date: '2023-10-15',
    items: 3,
    total: '$299.00',
    status: 'Delivered'
  },
  {
    id: 2,
    orderNumber: 'WU88191138',
    date: '2023-10-10',
    items: 1,
    total: '$99.00',
    status: 'Shipped'
  },
  {
    id: 3,
    orderNumber: 'WU88191137',
    date: '2023-10-05',
    items: 2,
    total: '$199.00',
    status: 'Processing'
  },
  {
    id: 4,
    orderNumber: 'WU88191136',
    date: '2023-09-28',
    items: 1,
    total: '$49.00',
    status: 'Delivered'
  },
  {
    id: 5,
    orderNumber: 'WU88191135',
    date: '2023-09-20',
    items: 4,
    total: '$399.00',
    status: 'Delivered'
  }
]);

// Handle order click
const handleOrderClick = (order: any) => {
  console.log('Clicked order:', order);
  // Navigate to order detail page
  // router.push(`/customer/orders/${order.id}`);
};

// Debug: Check if component loads
console.log('Dashboard loaded with AnimatedTable component');
console.log('Table data:', tableData.value);
console.log('Table columns:', tableColumns);

const stats = ref([
  { name: 'Total Orders', value: '12', change: '+2.5%', changeType: 'increase', period: 'vs last month' },
  { name: 'Total Spent', value: '$1,234', change: '+5.4%', changeType: 'increase', period: 'vs last month' },
  { name: 'Wishlist', value: '8', change: '+1', changeType: 'increase', period: 'items' },
  { name: 'Reward Points', value: '1,250', change: '+150', changeType: 'increase', period: 'points' },
]);

// Recommended products
const recommendedProducts = ref([
  {
    id: 1,
    name: 'Wireless Earbuds',
    color: 'Black',
    price: '$99.00',
    imageSrc: 'https://tailwindui.com/img/ecommerce-images/home-page-02-product-01.jpg',
  },
  {
    id: 2,
    name: 'Leather Journal',
    color: 'Brown',
    price: '$45.00',
    imageSrc: 'https://tailwindui.com/img/ecommerce-images/home-page-02-product-02.jpg',
  },
  {
    id: 3,
    name: 'Desk Organizer',
    color: 'Walnut',
    price: '$79.00',
    imageSrc: 'https://tailwindui.com/img/ecommerce-images/home-page-02-product-03.jpg',
  },
  {
    id: 4,
    name: 'Bluetooth Speaker',
    color: 'Gray',
    price: '$129.00',
    imageSrc: 'https://tailwindui.com/img/ecommerce-images/home-page-02-product-04.jpg',
  },
]);
</script>
