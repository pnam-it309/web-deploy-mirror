<template>
  <div class="order-list">
    <div class="bg-white shadow">
      <div class="max-w-7xl mx-auto py-6 px-4 sm:px-6 lg:px-8">
        <h1 class="text-3xl font-bold text-gray-900">Quản lý đơn hàng</h1>
      </div>
    </div>

    <div class="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
      <!-- Filters -->
      <div class="bg-white shadow rounded-lg p-4 mb-6">
        <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
          <!-- Search -->
          <div>
            <label for="search" class="block text-sm font-medium text-gray-700">Tìm kiếm</label>
            <div class="mt-1 relative rounded-md shadow-sm">
              <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                <svg class="h-5 w-5 text-gray-400" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor">
                  <path fill-rule="evenodd" d="M8 4a4 4 0 100 8 4 4 0 000-8zM2 8a6 6 0 1110.89 3.476l4.817 4.817a1 1 0 01-1.414 1.414l-4.816-4.816A6 6 0 012 8z" clip-rule="evenodd" />
                </svg>
              </div>
              <input
                type="text"
                id="search"
                v-model="filters.search"
                class="focus:ring-indigo-500 focus:border-indigo-500 block w-full pl-10 pr-3 py-2 sm:text-sm border-gray-300 rounded-md"
                placeholder="Mã đơn, tên khách..."
              />
            </div>
          </div>

          <!-- Status -->
          <div>
            <label for="status" class="block text-sm font-medium text-gray-700">Trạng thái</label>
            <select
              id="status"
              v-model="filters.status"
              class="mt-1 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm rounded-md"
            >
              <option value="">Tất cả trạng thái</option>
              <option value="pending">Chờ xác nhận</option>
              <option value="processing">Đang xử lý</option>
              <option value="shipped">Đang giao hàng</option>
              <option value="delivered">Đã giao hàng</option>
              <option value="cancelled">Đã hủy</option>
              <option value="refunded">Hoàn tiền</option>
            </select>
          </div>

          <!-- Date Range -->
          <div>
            <label for="date_range" class="block text-sm font-medium text-gray-700">Ngày tạo</label>
            <select
              id="date_range"
              v-model="filters.date_range"
              class="mt-1 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm rounded-md"
            >
              <option value="">Tất cả thời gian</option>
              <option value="today">Hôm nay</option>
              <option value="yesterday">Hôm qua</option>
              <option value="this_week">Tuần này</option>
              <option value="last_week">Tuần trước</option>
              <option value="this_month">Tháng này</option>
              <option value="last_month">Tháng trước</option>
              <option value="custom">Tùy chọn...</option>
            </select>
          </div>

          <!-- Actions -->
          <div class="flex items-end space-x-2">
            <button
              @click="resetFilters"
              class="inline-flex items-center px-4 py-2 border border-gray-300 shadow-sm text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
            >
              Đặt lại
            </button>
            <button
              @click="applyFilters"
              class="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
            >
              Áp dụng
            </button>
          </div>
        </div>

        <!-- Custom Date Range Picker (shown when 'custom' is selected) -->
        <div v-if="filters.date_range === 'custom'" class="mt-4 grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label for="start_date" class="block text-sm font-medium text-gray-700">Từ ngày</label>
            <input
              type="date"
              id="start_date"
              v-model="filters.start_date"
              class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
            />
          </div>
          <div>
            <label for="end_date" class="block text-sm font-medium text-gray-700">Đến ngày</label>
            <input
              type="date"
              id="end_date"
              v-model="filters.end_date"
              class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
            />
          </div>
        </div>
      </div>

      <!-- Orders Table -->
      <div class="bg-white shadow overflow-hidden sm:rounded-lg">
        <div class="overflow-x-auto">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Mã đơn
                </th>
                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Khách hàng
                </th>
                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Ngày đặt
                </th>
                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Tổng tiền
                </th>
                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Thanh toán
                </th>
                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Trạng thái
                </th>
                <th scope="col" class="relative px-6 py-3">
                  <span class="sr-only">Hành động</span>
                </th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="order in paginatedOrders" :key="order.id" class="hover:bg-gray-50">
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="text-sm font-medium text-gray-900">#{{ order.order_number }}</div>
                  <div class="text-sm text-gray-500">{{ formatDate(order.created_at) }}</div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="text-sm font-medium text-gray-900">{{ order.customer.name }}</div>
                  <div class="text-sm text-gray-500">{{ order.customer.email }}</div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                  {{ formatDateTime(order.created_at) }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                  {{ formatCurrency(order.total) }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span :class="getPaymentStatusClass(order.payment_status)" class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full">
                    {{ getPaymentStatusText(order.payment_status) }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span :class="getOrderStatusClass(order.status)" class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full">
                    {{ getOrderStatusText(order.status) }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                  <router-link
                    :to="{ name: 'admin-order-detail', params: { id: order.id } }"
                    class="text-indigo-600 hover:text-indigo-900 mr-4"
                  >
                    Xem
                  </router-link>
                  <button
                    v-if="canCancelOrder(order)"
                    @click="confirmCancelOrder(order)"
                    class="text-red-600 hover:text-red-900"
                  >
                    Hủy
                  </button>
                </td>
              </tr>
              <tr v-if="orders.length === 0">
                <td colspan="7" class="px-6 py-4 text-center text-sm text-gray-500">
                  Không tìm thấy đơn hàng nào
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- Pagination -->
        <div class="bg-white px-4 py-3 flex items-center justify-between border-t border-gray-200 sm:px-6">
          <div class="flex-1 flex justify-between sm:hidden">
            <button
              @click="previousPage"
              :disabled="pagination.currentPage === 1"
              class="relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50"
            >
              Trước
            </button>
            <button
              @click="nextPage"
              :disabled="pagination.currentPage === pagination.totalPages"
              class="ml-3 relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50"
            >
              Tiếp
            </button>
          </div>
          <div class="hidden sm:flex-1 sm:flex sm:items-center sm:justify-between">
            <div>
              <p class="text-sm text-gray-700">
                Hiển thị <span class="font-medium">{{ pagination.from }}</span> đến <span class="font-medium">{{ pagination.to }}</span> trong tổng số <span class="font-medium">{{ pagination.total }}</span> kết quả
              </p>
            </div>
            <div>
              <nav class="relative z-0 inline-flex rounded-md shadow-sm -space-x-px" aria-label="Pagination">
                <button
                  @click="previousPage"
                  :disabled="pagination.currentPage === 1"
                  :class="pagination.currentPage === 1 ? 'opacity-50 cursor-not-allowed' : 'hover:bg-gray-50'"
                  class="relative inline-flex items-center px-2 py-2 rounded-l-md border border-gray-300 bg-white text-sm font-medium text-gray-500"
                >
                  <span class="sr-only">Trước</span>
                  <svg class="h-5 w-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
                    <path fill-rule="evenodd" d="M12.707 5.293a1 1 0 010 1.414L9.414 10l3.293 3.293a1 1 0 01-1.414 1.414l-4-4a1 1 0 010-1.414l4-4a1 1 0 011.414 0z" clip-rule="evenodd" />
                  </svg>
                </button>
                <template v-for="page in pagination.totalPages" :key="page">
                  <button
                    @click="goToPage(page)"
                    :class="pagination.currentPage === page ? 'z-10 bg-indigo-50 border-indigo-500 text-indigo-600' : 'bg-white border-gray-300 text-gray-500 hover:bg-gray-50'"
                    class="relative inline-flex items-center px-4 py-2 border text-sm font-medium"
                  >
                    {{ page }}
                  </button>
                </template>
                <button
                  @click="nextPage"
                  :disabled="pagination.currentPage === pagination.totalPages"
                  :class="pagination.currentPage === pagination.totalPages ? 'opacity-50 cursor-not-allowed' : 'hover:bg-gray-50'"
                  class="relative inline-flex items-center px-2 py-2 rounded-r-md border border-gray-300 bg-white text-sm font-medium text-gray-500"
                >
                  <span class="sr-only">Tiếp</span>
                  <svg class="h-5 w-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
                    <path fill-rule="evenodd" d="M7.293 14.707a1 1 0 010-1.414L10.586 10 7.293 6.707a1 1 0 011.414-1.414l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0z" clip-rule="evenodd" />
                  </svg>
                </button>
              </nav>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Cancel Order Confirmation Modal -->
    <div v-if="showCancelModal" class="fixed z-10 inset-0 overflow-y-auto" aria-labelledby="modal-title" role="dialog" aria-modal="true">
      <div class="flex items-end justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
        <div class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity" aria-hidden="true" @click="showCancelModal = false"></div>

        <span class="hidden sm:inline-block sm:align-middle sm:h-screen" aria-hidden="true">&#8203;</span>

        <div class="inline-block align-bottom bg-white rounded-lg px-4 pt-5 pb-4 text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-lg sm:w-full sm:p-6">
          <div class="sm:flex sm:items-start">
            <div class="mx-auto flex-shrink-0 flex items-center justify-center h-12 w-12 rounded-full bg-red-100 sm:mx-0 sm:h-10 sm:w-10">
              <svg class="h-6 w-6 text-red-600" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" aria-hidden="true">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
              </svg>
            </div>
            <div class="mt-3 text-center sm:mt-0 sm:ml-4 sm:text-left">
              <h3 class="text-lg leading-6 font-medium text-gray-900" id="modal-title">
                Xác nhận hủy đơn hàng
              </h3>
              <div class="mt-2">
                <p class="text-sm text-gray-500">
                  Bạn có chắc chắn muốn hủy đơn hàng #{{ orderToCancel?.order_number }}? Hành động này không thể hoàn tác.
                </p>
              </div>
            </div>
          </div>
          <div class="mt-5 sm:mt-4 sm:flex sm:flex-row-reverse">
            <button
              type="button"
              @click="cancelOrder"
              class="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-red-600 text-base font-medium text-white hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500 sm:ml-3 sm:w-auto sm:text-sm"
              :disabled="cancelling"
            >
              <svg v-if="cancelling" class="animate-spin -ml-1 mr-3 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
              </svg>
              {{ cancelling ? 'Đang xử lý...' : 'Xác nhận hủy' }}
            </button>
            <button
              type="button"
              @click="showCancelModal = false"
              class="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:mt-0 sm:w-auto sm:text-sm"
              :disabled="cancelling"
            >
              Hủy
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, reactive, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';

export default defineComponent({
  name: 'OrderList',
  setup() {
    const router = useRouter();
    
    // Sample data - replace with actual API calls
    const orders = ref([
      {
        id: 1,
        order_number: 'ORD-2023-1001',
        customer: {
          id: 1,
          name: 'Nguyễn Văn A',
          email: 'nguyenvana@example.com',
          phone: '0912345678'
        },
        created_at: '2023-05-15T10:30:00Z',
        total: 1250000,
        status: 'processing',
        payment_status: 'paid',
        items: [
          { id: 1, name: 'Áo thun nam', quantity: 2, price: 250000, total: 500000 },
          { id: 2, name: 'Quần jean', quantity: 1, price: 750000, total: 750000 }
        ]
      },
      {
        id: 2,
        order_number: 'ORD-2023-1002',
        customer: {
          id: 2,
          name: 'Trần Thị B',
          email: 'tranthib@example.com',
          phone: '0987654321'
        },
        created_at: '2023-05-16T14:45:00Z',
        total: 1850000,
        status: 'shipped',
        payment_status: 'paid',
        items: [
          { id: 3, name: 'Áo khoác', quantity: 1, price: 1200000, total: 1200000 },
          { id: 4, name: 'Áo thun nữ', quantity: 2, price: 325000, total: 650000 }
        ]
      },
      {
        id: 3,
        order_number: 'ORD-2023-1003',
        customer: {
          id: 3,
          name: 'Lê Văn C',
          email: 'levanc@example.com',
          phone: '0905123456'
        },
        created_at: '2023-05-17T09:15:00Z',
        total: 950000,
        status: 'pending',
        payment_status: 'pending',
        items: [
          { id: 5, name: 'Quần tây', quantity: 1, price: 650000, total: 650000 },
          { id: 6, name: 'Áo sơ mi', quantity: 1, price: 300000, total: 300000 }
        ]
      },
      {
        id: 4,
        order_number: 'ORD-2023-1004',
        customer: {
          id: 4,
          name: 'Phạm Thị D',
          email: 'phamthid@example.com',
          phone: '0978123456'
        },
        created_at: '2023-05-18T16:20:00Z',
        total: 2200000,
        status: 'delivered',
        payment_status: 'paid',
        items: [
          { id: 7, name: 'Đầm dự tiệc', quantity: 1, price: 1500000, total: 1500000 },
          { id: 8, name: 'Giày cao gót', quantity: 1, price: 700000, total: 700000 }
        ]
      },
      {
        id: 5,
        order_number: 'ORD-2023-1005',
        customer: {
          id: 5,
          name: 'Hoàng Văn E',
          email: 'hoangvane@example.com',
          phone: '0918765432'
        },
        created_at: '2023-05-19T11:10:00Z',
        total: 3200000,
        status: 'cancelled',
        payment_status: 'refunded',
        items: [
          { id: 9, name: 'Bộ đồ thể thao', quantity: 2, price: 1600000, total: 3200000 }
        ]
      }
    ]);

    // Filters
    const filters = reactive({
      search: '',
      status: '',
      date_range: '',
      start_date: '',
      end_date: ''
    });

    // Pagination
    const pagination = reactive({
      currentPage: 1,
      perPage: 10,
      get total() {
        return filteredOrders.value.length;
      },
      get totalPages() {
        return Math.ceil(this.total / this.perPage);
      },
      get from() {
        return (this.currentPage - 1) * this.perPage + 1;
      },
      get to() {
        const to = this.currentPage * this.perPage;
        return to > this.total ? this.total : to;
      }
    });

    // Filtered and paginated orders
    const filteredOrders = computed(() => {
      return orders.value.filter(order => {
        // Search filter
        const searchTerm = filters.search.toLowerCase();
        const matchesSearch = !searchTerm || 
          order.order_number.toLowerCase().includes(searchTerm) ||
          order.customer.name.toLowerCase().includes(searchTerm) ||
          order.customer.email.toLowerCase().includes(searchTerm) ||
          order.customer.phone.includes(searchTerm);
        
        // Status filter
        const matchesStatus = !filters.status || order.status === filters.status;
        
        // Date range filter
        let matchesDateRange = true;
        if (filters.date_range) {
          const orderDate = new Date(order.created_at);
          const today = new Date();
          today.setHours(0, 0, 0, 0);
          
          switch (filters.date_range) {
            case 'today':
              const orderDateStr = orderDate.toISOString().split('T')[0];
              const todayStr = today.toISOString().split('T')[0];
              matchesDateRange = orderDateStr === todayStr;
              break;
            case 'yesterday':
              const yesterday = new Date(today);
              yesterday.setDate(yesterday.getDate() - 1);
              const yesterdayStr = yesterday.toISOString().split('T')[0];
              const orderDateYestStr = orderDate.toISOString().split('T')[0];
              matchesDateRange = orderDateYestStr === yesterdayStr;
              break;
            case 'this_week':
              const day = orderDate.getDay();
              const diff = orderDate.getDate() - day + (day === 0 ? -6 : 1);
              const weekStart = new Date(orderDate.setDate(diff));
              weekStart.setHours(0, 0, 0, 0);
              
              const thisWeekStart = new Date();
              thisWeekStart.setDate(thisWeekStart.getDate() - thisWeekStart.getDay() + (thisWeekStart.getDay() === 0 ? -6 : 1));
              thisWeekStart.setHours(0, 0, 0, 0);
              
              matchesDateRange = orderDate >= thisWeekStart && orderDate <= new Date();
              break;
            case 'last_week':
              const lastWeekStart = new Date();
              lastWeekStart.setDate(lastWeekStart.getDate() - lastWeekStart.getDay() - 6);
              lastWeekStart.setHours(0, 0, 0, 0);
              
              const lastWeekEnd = new Date(lastWeekStart);
              lastWeekEnd.setDate(lastWeekStart.getDate() + 6);
              lastWeekEnd.setHours(23, 59, 59, 999);
              
              matchesDateRange = orderDate >= lastWeekStart && orderDate <= lastWeekEnd;
              break;
            case 'this_month':
              const thisMonthStart = new Date(today.getFullYear(), today.getMonth(), 1);
              matchesDateRange = orderDate >= thisMonthStart && orderDate <= new Date();
              break;
            case 'last_month':
              const lastMonthEnd = new Date(today.getFullYear(), today.getMonth(), 0);
              const lastMonthStart = new Date(today.getFullYear(), today.getMonth() - 1, 1);
              matchesDateRange = orderDate >= lastMonthStart && orderDate <= lastMonthEnd;
              break;
            case 'custom':
              if (filters.start_date && filters.end_date) {
                const startDate = new Date(filters.start_date);
                startDate.setHours(0, 0, 0, 0);
                
                const endDate = new Date(filters.end_date);
                endDate.setHours(23, 59, 59, 999);
                
                matchesDateRange = orderDate >= startDate && orderDate <= endDate;
              }
              break;
          }
        }
        
        return matchesSearch && matchesStatus && matchesDateRange;
      });
    });

    const paginatedOrders = computed(() => {
      const start = (pagination.currentPage - 1) * pagination.perPage;
      const end = start + pagination.perPage;
      return filteredOrders.value.slice(start, end);
    });

    // Cancel order modal
    const showCancelModal = ref(false);
    const orderToCancel = ref<any>(null);
    const cancelling = ref(false);

    // Format currency
    const formatCurrency = (value: number) => {
      return new Intl.NumberFormat('vi-VN', { 
        style: 'currency', 
        currency: 'VND' 
      }).format(value);
    };

    // Format date
    const formatDate = (dateString: string) => {
      const options: Intl.DateTimeFormatOptions = { 
        year: 'numeric', 
        month: 'short', 
        day: 'numeric' 
      };
      return new Date(dateString).toLocaleDateString('vi-VN', options);
    };

    // Format date and time
    const formatDateTime = (dateTimeString: string) => {
      const options: Intl.DateTimeFormatOptions = { 
        year: 'numeric', 
        month: 'short', 
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      };
      return new Date(dateTimeString).toLocaleString('vi-VN', options);
    };

    // Get status class
    const getOrderStatusClass = (status: string) => {
      const statusMap: Record<string, string> = {
        'pending': 'bg-yellow-100 text-yellow-800',
        'processing': 'bg-blue-100 text-blue-800',
        'shipped': 'bg-indigo-100 text-indigo-800',
        'delivered': 'bg-green-100 text-green-800',
        'cancelled': 'bg-red-100 text-red-800',
        'refunded': 'bg-purple-100 text-purple-800'
      };
      return statusMap[status] || 'bg-gray-100 text-gray-800';
    };

    // Get status text
    const getOrderStatusText = (status: string) => {
      const statusMap: Record<string, string> = {
        'pending': 'Chờ xác nhận',
        'processing': 'Đang xử lý',
        'shipped': 'Đang giao hàng',
        'delivered': 'Đã giao hàng',
        'cancelled': 'Đã hủy',
        'refunded': 'Đã hoàn tiền'
      };
      return statusMap[status] || status;
    };

    // Get payment status class
    const getPaymentStatusClass = (status: string) => {
      const statusMap: Record<string, string> = {
        'pending': 'bg-yellow-100 text-yellow-800',
        'paid': 'bg-green-100 text-green-800',
        'failed': 'bg-red-100 text-red-800',
        'refunded': 'bg-purple-100 text-purple-800',
        'cancelled': 'bg-gray-100 text-gray-800'
      };
      return statusMap[status] || 'bg-gray-100 text-gray-800';
    };

    // Get payment status text
    const getPaymentStatusText = (status: string) => {
      const statusMap: Record<string, string> = {
        'pending': 'Chờ thanh toán',
        'paid': 'Đã thanh toán',
        'failed': 'Thanh toán thất bại',
        'refunded': 'Đã hoàn tiền',
        'cancelled': 'Đã hủy'
      };
      return statusMap[status] || status;
    };

    // Check if order can be cancelled
    const canCancelOrder = (order: any) => {
      return ['pending', 'processing'].includes(order.status);
    };

    // Confirm cancel order
    const confirmCancelOrder = (order: any) => {
      orderToCancel.value = order;
      showCancelModal.value = true;
    };

    // Cancel order
    const cancelOrder = async () => {
      if (!orderToCancel.value) return;
      
      cancelling.value = true;
      
      try {
        // Simulate API call
        await new Promise(resolve => setTimeout(resolve, 1000));
        
        // Update order status in local data
        const order = orders.value.find(o => o.id === orderToCancel.value.id);
        if (order) {
          order.status = 'cancelled';
          order.payment_status = 'refunded';
        }
        
        showCancelModal.value = false;
      } catch (error) {
        console.error('Error cancelling order:', error);
      } finally {
        cancelling.value = false;
      }
    };

    // Apply filters
    const applyFilters = () => {
      pagination.currentPage = 1;
    };

    // Reset filters
    const resetFilters = () => {
      filters.search = '';
      filters.status = '';
      filters.date_range = '';
      filters.start_date = '';
      filters.end_date = '';
      pagination.currentPage = 1;
    };

    // Pagination methods
    const goToPage = (page: number) => {
      if (page >= 1 && page <= pagination.totalPages) {
        pagination.currentPage = page;
      }
    };

    const previousPage = () => {
      if (pagination.currentPage > 1) {
        pagination.currentPage--;
      }
    };

    const nextPage = () => {
      if (pagination.currentPage < pagination.totalPages) {
        pagination.currentPage++;
      }
    };

    return {
      // Data
      orders: paginatedOrders,
      filters,
      pagination,
      showCancelModal,
      orderToCancel,
      cancelling,
      
      // Methods
      formatCurrency,
      formatDate,
      formatDateTime,
      getOrderStatusClass,
      getOrderStatusText,
      getPaymentStatusClass,
      getPaymentStatusText,
      canCancelOrder,
      confirmCancelOrder,
      cancelOrder,
      applyFilters,
      resetFilters,
      goToPage,
      previousPage,
      nextPage
    };
  }
});
</script>

<style scoped>
.order-list {
  min-height: calc(100vh - 64px);
}
</style>
