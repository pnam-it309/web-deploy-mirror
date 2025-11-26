<template>
  <div class="p-6">
    <h1 class="text-2xl font-semibold text-gray-900">My Orders</h1>

    <div class="mt-6 flex flex-col sm:flex-row sm:items-center sm:justify-between">
      <div class="flex-1 max-w-lg">
        <div class="relative">
          <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
            <svg class="h-5 w-5 text-gray-400" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20"
              fill="currentColor" aria-hidden="true">
              <path fill-rule="evenodd"
                d="M8 4a4 4 0 100 8 4 4 0 000-8zM2 8a6 6 0 1110.89 3.476l4.817 4.817a1 1 0 01-1.414 1.414l-4.816-4.816A6 6 0 012 8z"
                clip-rule="evenodd" />
            </svg>
          </div>
          <input v-model="searchQuery" @input="applyFilters" type="text"
            class="block w-full pl-10 pr-3 py-2 border border-gray-300 rounded-md leading-5 bg-white placeholder-gray-500 focus:outline-none focus:placeholder-gray-400 focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
            placeholder="Search orders...">
        </div>
      </div>
      <div class="mt-3 sm:mt-0 sm:ml-4">
        <select v-model="statusFilter" @change="applyFilters"
          class="block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm rounded-md">
          <option value="all">All Orders</option>
          <option value="processing">Processing</option>
          <option value="shipped">Shipped</option>
          <option value="delivered">Delivered</option>
          <option value="cancelled">Cancelled</option>
        </select>
      </div>
    </div>

    <div class="mt-6">
      <div class="bg-white shadow overflow-hidden sm:rounded-md">
        <ul class="divide-y divide-gray-200">
          <li v-for="order in paginatedOrders" :key="order.id">
            <div class="px-4 py-4 sm:px-6">
              <div class="flex items-center justify-between">
                <div>
                  <p class="text-sm font-medium text-indigo-600 truncate">
                    Order #{{ order.number }}
                  </p>
                  <p class="mt-1 text-sm text-gray-500">
                    Placed on {{ formatDate(order.date) }}
                  </p>
                </div>
                <div class="ml-2 flex-shrink-0 flex">
                  <p :class="[
                    getStatusClass(order.status),
                    'px-2 inline-flex text-xs leading-5 font-semibold rounded-full'
                  ]">
                    {{ order.status }}
                  </p>
                </div>
              </div>

              <div class="mt-2 sm:flex sm:justify-between">
                <div class="sm:flex">
                  <p class="flex items-center text-sm text-gray-500">
                    {{ order.items.length }} items
                  </p>
                  <p class="mt-2 flex items-center text-sm text-gray-500 sm:mt-0 sm:ml-6">
                    <svg class="flex-shrink-0 mr-1.5 h-5 w-5 text-gray-400" xmlns="http://www.w3.org/2000/svg"
                      viewBox="0 0 20 20" fill="currentColor">
                      <path fill-rule="evenodd"
                        d="M5.05 4.05a7 7 0 119.9 9.9L10 18.9l-4.95-4.95a7 7 0 010-9.9zM10 11a2 2 0 100-4 2 2 0 000 4z"
                        clip-rule="evenodd" />
                    </svg>
                    {{ order.shippingAddress.city }}, {{ order.shippingAddress.country }}
                  </p>
                </div>
                <div class="mt-2 flex items-center text-sm text-gray-500 sm:mt-0">
                  <svg class="flex-shrink-0 mr-1.5 h-5 w-5 text-gray-400" xmlns="http://www.w3.org/2000/svg"
                    viewBox="0 0 20 20" fill="currentColor">
                    <path fill-rule="evenodd"
                      d="M10 18a8 8 0 100-16 8 8 0 000 16zm1-12a1 1 0 10-2 0v4a1 1 0 00.293.707l2.828 2.829a1 1 0 101.415-1.415L11 9.586V6z"
                      clip-rule="evenodd" />
                  </svg>
                  <p>
                    {{ order.expectedDelivery }}
                  </p>
                </div>
              </div>

              <div class="mt-4">
                <div class="flex -space-x-2 overflow-hidden">
                  <img v-for="(item, index) in order.items.slice(0, 4)" :key="index"
                    class="inline-block h-10 w-10 rounded-full ring-2 ring-white" :src="item.image" :alt="item.name"
                    :title="item.name">
                  <span v-if="order.items.length > 4"
                    class="inline-flex items-center justify-center h-10 w-10 rounded-full bg-gray-100 text-gray-500 text-xs font-medium ring-2 ring-white">
                    +{{ order.items.length - 4 }}
                  </span>
                </div>
              </div>

              <div class="mt-4 flex justify-between items-center">
                <p class="text-lg font-medium text-gray-900">
                  ${{ order.total }}
                </p>
                <div class="flex space-x-3">
                  <button @click="viewDetails(order)" type="button"
                    class="inline-flex items-center px-3 py-2 border border-gray-300 shadow-sm text-sm leading-4 font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                    <svg class="-ml-0.5 mr-2 h-4 w-4" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                      stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
                    </svg>
                    View Details
                  </button>
                  <button v-if="order.status === 'Delivered'" type="button"
                    class="inline-flex items-center px-3 py-2 border border-transparent text-sm leading-4 font-medium rounded-md text-indigo-700 bg-indigo-100 hover:bg-indigo-200 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                    Buy Again
                  </button>
                </div>
              </div>
            </div>
          </li>
        </ul>

        <div v-if="paginatedOrders.length === 0" class="text-center py-12">
          <svg class="mx-auto h-12 w-12 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1"
              d="M16 11V7a4 4 0 00-8 0v4M5 9h14l1 12H4L5 9z" />
          </svg>
          <h3 class="mt-2 text-sm font-medium text-gray-900">No orders found</h3>
          <p class="mt-1 text-sm text-gray-500">
            {{ statusFilter === 'all' ? 'You haven\'t placed any orders yet.' : `No ${statusFilter} orders found.` }}
          </p>
          <div class="mt-6">
            <router-link to="/catalog"
              class="inline-flex items-center px-4 py-2 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
              <svg class="-ml-1 mr-2 h-5 w-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20"
                fill="currentColor">
                <path fill-rule="evenodd"
                  d="M10 3a1 1 0 011 1v5h5a1 1 0 110 2h-5v5a1 1 0 11-2 0v-5H4a1 1 0 110-2h5V4a1 1 0 011-1z"
                  clip-rule="evenodd" />
              </svg>
              Start Shopping
            </router-link>
          </div>
        </div>
      </div>

      <div v-if="totalPages > 1" class="mt-4 flex items-center justify-between">
        <div class="flex-1 flex justify-between sm:hidden">
          <button @click="changePage(currentPage - 1)" :disabled="currentPage === 1"
            class="relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50">
            Previous
          </button>
          <button @click="changePage(currentPage + 1)" :disabled="currentPage === totalPages"
            class="ml-3 relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50">
            Next
          </button>
        </div>
        <div class="hidden sm:flex-1 sm:flex sm:items-center sm:justify-between">
          <div>
            <p class="text-sm text-gray-700">
              Showing <span class="font-medium">{{ (currentPage - 1) * itemsPerPage + 1 }}</span> to <span
                class="font-medium">{{ Math.min(currentPage * itemsPerPage, filteredOrders.length) }}</span> of <span
                class="font-medium">{{ filteredOrders.length }}</span> results
            </p>
          </div>
          <div>
            <nav class="relative z-0 inline-flex rounded-md shadow-sm -space-x-px" aria-label="Pagination">
              <button @click="changePage(currentPage - 1)" :disabled="currentPage === 1"
                class="relative inline-flex items-center px-2 py-2 rounded-l-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50">
                <span class="sr-only">Previous</span>
                <svg class="h-5 w-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor"
                  aria-hidden="true">
                  <path fill-rule="evenodd"
                    d="M12.707 5.293a1 1 0 010 1.414L9.414 10l3.293 3.293a1 1 0 01-1.414 1.414l-4-4a1 1 0 010-1.414l4-4a1 1 0 011.414 0z"
                    clip-rule="evenodd" />
                </svg>
              </button>
              <button v-for="page in totalPages" :key="page" @click="changePage(page)"
                :aria-current="currentPage === page ? 'page' : undefined" :class="[
                  currentPage === page ? 'z-10 bg-indigo-50 border-indigo-500 text-indigo-600' : 'bg-white border-gray-300 text-gray-500 hover:bg-gray-50',
                  'relative inline-flex items-center px-4 py-2 border text-sm font-medium'
                ]">
                {{ page }}
              </button>
              <button @click="changePage(currentPage + 1)" :disabled="currentPage === totalPages"
                class="relative inline-flex items-center px-2 py-2 rounded-r-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50">
                <span class="sr-only">Next</span>
                <svg class="h-5 w-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor"
                  aria-hidden="true">
                  <path fill-rule="evenodd"
                    d="M7.293 14.707a1 1 0 010-1.414L10.586 10 7.293 6.707a1 1 0 011.414-1.414l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0z"
                    clip-rule="evenodd" />
                </svg>
              </button>
            </nav>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

// --- State Phân trang ---
const currentPage = ref(1);
const itemsPerPage = 5; // Số đơn hàng trên mỗi trang

// Search and filter
const searchQuery = ref('');
const statusFilter = ref('all');

// --- Dữ liệu đơn hàng mẫu (Mở rộng để có nhiều trang hơn) ---
const orders = ref([
  {
    id: 1, number: 'WU88191139', date: '2023-10-15', status: 'Delivered', total: '299.00', expectedDelivery: 'Delivered on Oct 18, 2023', shippingAddress: { city: 'New York', country: 'USA' },
    items: [{ id: 1, name: 'Wireless Headphones', image: 'https://tailwindui.com/img/ecommerce-images/shopping-cart-page-04-product-01.jpg' }, { id: 2, name: 'Leather Journal', image: 'https://tailwindui.com/img/ecommerce-images/shopping-cart-page-04-product-02.jpg' }, { id: 3, name: 'Desk Organizer', image: 'https://tailwindui.com/img/ecommerce-images/shopping-cart-page-04-product-03.jpg' }]
  },
  {
    id: 2, number: 'WU88191138', date: '2023-10-10', status: 'Shipped', total: '99.00', expectedDelivery: 'Expected on Oct 20, 2023', shippingAddress: { city: 'Los Angeles', country: 'USA' },
    items: [{ id: 4, name: 'Bluetooth Speaker', image: 'https://tailwindui.com/img/ecommerce-images/shopping-cart-page-04-product-04.jpg' }]
  },
  {
    id: 3, number: 'WU88191137', date: '2023-10-05', status: 'Processing', total: '199.00', expectedDelivery: 'Expected on Oct 25, 2023', shippingAddress: { city: 'Chicago', country: 'USA' },
    items: [{ id: 5, name: 'Leather Wallet', image: 'https://tailwindui.com/img/ecommerce-images/shopping-cart-page-04-product-01.jpg' }, { id: 6, name: 'Watch', image: 'https://tailwindui.com/img/ecommerce-images/shopping-cart-page-04-product-02.jpg' }]
  },
  {
    id: 4, number: 'WU88191136', date: '2023-09-28', status: 'Cancelled', total: '149.00', expectedDelivery: 'Cancelled on Sep 30, 2023', shippingAddress: { city: 'Houston', country: 'USA' },
    items: [{ id: 7, name: 'Sunglasses', image: 'https://tailwindui.com/img/ecommerce-images/shopping-cart-page-04-product-03.jpg' }]
  },
  // Thêm đơn hàng để kích hoạt phân trang
  {
    id: 5, number: 'WU88191135', date: '2023-09-25', status: 'Delivered', total: '450.00', expectedDelivery: 'Delivered on Sep 28, 2023', shippingAddress: { city: 'Miami', country: 'USA' },
    items: [{ id: 8, name: 'Gaming Mouse', image: 'https://tailwindui.com/img/ecommerce-images/shopping-cart-page-04-product-04.jpg' }, { id: 9, name: 'Mechanical Keyboard', image: 'https://tailwindui.com/img/ecommerce-images/shopping-cart-page-04-product-01.jpg' }]
  },
  {
    id: 6, number: 'WU88191134', date: '2023-09-20', status: 'Shipped', total: '75.00', expectedDelivery: 'Expected on Sep 30, 2023', shippingAddress: { city: 'Dallas', country: 'USA' },
    items: [{ id: 10, name: 'T-Shirt', image: 'https://tailwindui.com/img/ecommerce-images/shopping-cart-page-04-product-02.jpg' }]
  },
  {
    id: 7, number: 'WU88191133', date: '2023-09-15', status: 'Processing', total: '50.00', expectedDelivery: 'Expected on Oct 1, 2023', shippingAddress: { city: 'San Jose', country: 'USA' },
    items: [{ id: 11, name: 'Socks', image: 'https://tailwindui.com/img/ecommerce-images/shopping-cart-page-04-product-03.jpg' }]
  }
]);

// Filter orders based on search and status
const filteredOrders = computed(() => {
  const query = searchQuery.value.toLowerCase();
  return orders.value.filter(order => {
    const matchesSearch = order.number.toLowerCase().includes(query);
    const matchesStatus = statusFilter.value === 'all' || order.status.toLowerCase() === statusFilter.value.toLowerCase();
    return matchesSearch && matchesStatus;
  });
});

// --- Logic Phân trang ---
const totalPages = computed(() => {
  return Math.ceil(filteredOrders.value.length / itemsPerPage);
});

const paginatedOrders = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  const end = start + itemsPerPage;
  return filteredOrders.value.slice(start, end);
});

const changePage = (page: number) => {
  if (page >= 1 && page <= totalPages.value) {
    currentPage.value = page;
    // Cuộn lên đầu trang sau khi chuyển trang
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }
};

const applyFilters = () => {
  // Reset về trang 1 khi filter/search thay đổi
  currentPage.value = 1;
};

// --- Logic View Details (Sửa từ trackOrder) ---
const viewDetails = (order: any) => {
  // Chuyển hướng đến trang chi tiết đơn hàng
  // Sử dụng Order Number làm ID trong URL (hoặc Order ID thực nếu có)
  router.push(`/customer/orders/${order.number}`);
};


// Helper functions
const formatDate = (dateString: string) => {
  const options: Intl.DateTimeFormatOptions = { year: 'numeric', month: 'long', day: 'numeric' };
  return new Date(dateString).toLocaleDateString('en-US', options);
};

const getStatusClass = (status: string) => {
  switch (status.toLowerCase()) {
    case 'delivered':
      return 'bg-green-100 text-green-800';
    case 'shipped':
      return 'bg-blue-100 text-blue-800';
    case 'processing':
      return 'bg-yellow-100 text-yellow-800';
    case 'cancelled':
      return 'bg-red-100 text-red-800';
    default:
      return 'bg-gray-100 text-gray-800';
  }
};
</script>
