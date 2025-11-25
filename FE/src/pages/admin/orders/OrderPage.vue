<template>
  <div class="order-list p-6">
    <div class="flex justify-between items-center mb-6">
      <h2 class="text-2xl font-semibold text-gray-900">Quản lý đơn hàng</h2>
      <div class="flex items-center space-x-3">
        <router-link
          to="/admin/orders/new"
          class="inline-flex items-center px-4 py-2 rounded shadow text-white transition-colors bg-pink-600 hover:bg-pink-700 text-sm font-medium"
        >
          + Tạo đơn hàng mới
        </router-link>
      </div>
    </div>

    <CardCustom class="p-4 mb-6">
      <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
        <div>
          <label for="search" class="block text-sm font-medium text-gray-700">Tìm kiếm</label>
          <div class="mt-1 relative rounded-md shadow-sm">
            <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
              <svg
                class="h-5 w-5 text-gray-400"
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 20 20"
                fill="currentColor"
              >
                <path
                  fill-rule="evenodd"
                  d="M8 4a4 4 0 100 8 4 4 0 000-8zM2 8a6 6 0 1110.89 3.476l4.817 4.817a1 1 0 01-1.414 1.414l-4.816-4.816A6 6 0 012 8z"
                  clip-rule="evenodd"
                />
              </svg>
            </div>
            <input
              type="text"
              id="search"
              v-model="filters.search"
              class="focus:ring-pink-500 focus:border-pink-500 block w-full pl-10 pr-3 py-2 sm:text-sm border-gray-300 rounded-md"
              placeholder="Mã đơn, tên khách..."
            />
          </div>
        </div>

        <div>
          <label for="status" class="block text-sm font-medium text-gray-700">Trạng thái</label>
          <select
            id="status"
            v-model="filters.status"
            class="mt-1 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-pink-500 focus:border-pink-500 sm:text-sm rounded-md"
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

        <div>
          <label for="date_range" class="block text-sm font-medium text-gray-700">Ngày tạo</label>
          <select
            id="date_range"
            v-model="filters.date_range"
            class="mt-1 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-pink-500 focus:border-pink-500 sm:text-sm rounded-md"
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

        <div class="flex items-end space-x-2">
          <ButtonCustom color="secondary" @click="resetFilters"> Đặt lại </ButtonCustom>
          <ButtonCustom color="primary" @click="applyFilters"> Áp dụng </ButtonCustom>
        </div>
      </div>

      <div v-if="filters.date_range === 'custom'" class="mt-4 grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <label for="start_date" class="block text-sm font-medium text-gray-700">Từ ngày</label>
          <input
            type="date"
            id="start_date"
            v-model="filters.start_date"
            class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-pink-500 focus:border-pink-500 sm:text-sm"
          />
        </div>
        <div>
          <label for="end_date" class="block text-sm font-medium text-gray-700">Đến ngày</label>
          <input
            type="date"
            id="end_date"
            v-model="filters.end_date"
            class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-pink-500 focus:border-pink-500 sm:text-sm"
          />
        </div>
      </div>
    </CardCustom>

    <CardCustom>
      <div class="overflow-x-auto">
        <table class="min-w-full divide-y divide-gray-200">
          <thead class="bg-gray-50 w-full">
            <tr>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Mã đơn</th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Khách hàng</th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Ngày đặt</th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Tổng tiền</th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Thanh toán</th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Trạng thái</th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Hành động</th>
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
                <span
                  :class="getPaymentStatusClass(order.payment_status)"
                  class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full"
                >
                  {{ getPaymentStatusText(order.payment_status) }}
                </span>
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <span
                  :class="getOrderStatusClass(order.status)"
                  class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full"
                >
                  {{ getOrderStatusText(order.status) }}
                </span>
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium space-x-2">
                <!-- <router-link
                  :to="{ name: 'admin-order-detail', params: { id: order.id } }"
                  class="inline-flex items-center justify-center py-1 w-20 rounded shadow text-white transition-colors bg-blue-500 hover:bg-blue-600 text-sm font-medium"
                >
                  Xem
                </router-link> -->
                <ButtonCustom
                  v-if="canCancelOrder(order)"
                  color="danger"
                  size="small"
                  @click="confirmCancelOrder(order)"
                >
                  Hủy
                </ButtonCustom>
              </td>
            </tr>
            <tr v-if="!paginatedOrders.length">
              <td colspan="7" class="px-6 py-4 text-center text-sm text-gray-500">
                Không tìm thấy đơn hàng nào
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <div
        class="bg-white px-4 py-3 flex items-center justify-between border-t border-gray-200 sm:px-6"
      >
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
              Hiển thị <span class="font-medium">{{ pagination.from }}</span> đến
              <span class="font-medium">{{ pagination.to }}</span> trong tổng số
              <span class="font-medium">{{ pagination.total }}</span> kết quả
            </p>
          </div>
          <div>
            <nav
              class="relative z-0 inline-flex rounded-md shadow-sm -space-x-px"
              aria-label="Pagination"
            >
              <button
                @click="previousPage"
                :disabled="pagination.currentPage === 1"
                :class="
                  pagination.currentPage === 1
                    ? 'opacity-50 cursor-not-allowed'
                    : 'hover:bg-gray-50'
                "
                class="relative inline-flex items-center px-2 py-2 rounded-l-md border border-gray-300 bg-white text-sm font-medium text-gray-500"
              >
                <span class="sr-only">Trước</span>
                <svg
                  class="h-5 w-5"
                  xmlns="http://www.w3.org/2000/svg"
                  viewBox="0 0 20 20"
                  fill="currentColor"
                  aria-hidden="true"
                >
                  <path
                    fill-rule="evenodd"
                    d="M12.707 5.293a1 1 0 010 1.414L9.414 10l3.293 3.293a1 1 0 01-1.414 1.414l-4-4a1 1 0 010-1.414l4-4a1 1 0 011.414 0z"
                    clip-rule="evenodd"
                  />
                </svg>
              </button>
              <template v-for="page in pagination.totalPages" :key="page">
                <button
                  @click="goToPage(page)"
                  :class="
                    pagination.currentPage === page
                      ? 'z-10 bg-pink-50 border-pink-500 text-pink-600'
                      : 'bg-white border-gray-300 text-gray-500 hover:bg-gray-50'
                  "
                  class="relative inline-flex items-center px-4 py-2 border text-sm font-medium"
                >
                  {{ page }}
                </button>
              </template>
              <button
                @click="nextPage"
                :disabled="pagination.currentPage === pagination.totalPages"
                :class="
                  pagination.currentPage === pagination.totalPages
                    ? 'opacity-50 cursor-not-allowed'
                    : 'hover:bg-gray-50'
                "
                class="relative inline-flex items-center px-2 py-2 rounded-r-md border border-gray-300 bg-white text-sm font-medium text-gray-500"
              >
                <span class="sr-only">Tiếp</span>
                <svg
                  class="h-5 w-5"
                  xmlns="http://www.w3.org/2000/svg"
                  viewBox="0 0 20 20"
                  fill="currentColor"
                  aria-hidden="true"
                >
                  <path
                    fill-rule="evenodd"
                    d="M7.293 14.707a1 1 0 010-1.414L10.586 10 7.293 6.707a1 1 0 011.414-1.414l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0z"
                    clip-rule="evenodd"
                  />
                </svg>
              </button>
            </nav>
          </div>
        </div>
      </div>
    </CardCustom>
    <OrderCreateModal
      v-if="showCreateModal"
      @close="showCreateModal = false"
      @created="handleOrderCreated"
    />
    <ModalCustom :show="showCancelModal" @close="showCancelModal = false">
      <template #title> Xác nhận hủy đơn hàng </template>

      <template #default>
        <div class="sm:flex sm:items-start">
          <div
            class="mx-auto flex-shrink-0 flex items-center justify-center h-12 w-12 rounded-full bg-red-100 sm:mx-0 sm:h-10 sm:w-10"
          >
            <svg
              class="h-6 w-6 text-red-600"
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
              aria-hidden="true"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"
              />
            </svg>
          </div>
          <div class="mt-3 text-center sm:mt-0 sm:ml-4 sm:text-left">
            <div class="mt-2">
              <p class="text-sm text-gray-500">
                Bạn có chắc chắn muốn hủy đơn hàng #{{ orderToCancel?.order_number }}? Hành động
                này không thể hoàn tác.
              </p>
            </div>
          </div>
        </div>
      </template>

      <template #footer>
        <ButtonCustom color="danger" @click="cancelOrder" :disabled="cancelling">
          <svg
            v-if="cancelling"
            class="animate-spin -ml-1 mr-3 h-5 w-5 text-white"
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
          >
            <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
            <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
          </svg>
          {{ cancelling ? 'Đang xử lý...' : 'Xác nhận hủy' }}
        </ButtonCustom>
        <ButtonCustom color="secondary" @click="showCancelModal = false" :disabled="cancelling">
          Hủy
        </ButtonCustom>
      </template>
    </ModalCustom>

    <router-view />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed } from 'vue'
import ButtonCustom from '@/components/custom/Button/ButtonDefault.vue'
import CardCustom from '@/components/custom/Card/CardCustom.vue'
import ModalCustom from '@/components/custom/Modal/ModalCustom.vue'
import OrderCreateModal from './OrderCreateModal.vue'
const showCreateModal = ref(false)
const handleOrderCreated = (payload: any) => {
  // payload là object từ form: { code, customer, date, total, payment, status }
  
  // 1. Tạo một ID mới (cho FE-only)
  const newId = (orders.value.length ? Math.max(...orders.value.map(o => o.id)) : 0) + 1
  
  // 2. Chuyển đổi payload thành object Order hoàn chỉnh
  const newOrder = {
    id: newId,
    order_number: payload.code || `ORD-${newId}`,
    customer: {
      id: newId, // Dùng tạm
      name: payload.customer || 'Khách lẻ',
      email: `${payload.customer.toLowerCase().replace(' ', '.')}@example.com`,
      phone: '09xxxxxx',
    },
    created_at: payload.date ? new Date(payload.date).toISOString() : new Date().toISOString(),
    total: payload.total,
    status: payload.status,
    payment_status: payload.payment === 'cod' ? 'pending' : 'paid', // Map trạng thái
    items: [], // Khởi tạo mảng rỗng
  }
  
  // 3. Thêm vào đầu danh sách
  orders.value.unshift(newOrder)
  
  // 4. Đóng modal (Modal đã tự emit 'close', nhưng ta có thể đóng ở đây nếu muốn)
  showCreateModal.value = false 
}
// Sample data
const orders = ref([
  {
    id: 1,
    order_number: 'ORD-2023-1001',
    customer: { id: 1, name: 'Nguyễn Văn A', email: 'nguyenvana@example.com', phone: '0912345678' },
    created_at: '2023-05-15T10:30:00Z',
    total: 1250000,
    status: 'processing',
    payment_status: 'paid',
    items: [],
  },
  {
    id: 2,
    order_number: 'ORD-2023-1002',
    customer: { id: 2, name: 'Trần Thị B', email: 'tranthib@example.com', phone: '0987654321' },
    created_at: '2023-05-16T14:45:00Z',
    total: 1850000,
    status: 'shipped',
    payment_status: 'paid',
    items: [],
  },
  {
    id: 3,
    order_number: 'ORD-2023-1003',
    customer: { id: 3, name: 'Lê Văn C', email: 'levanc@example.com', phone: '0905123456' },
    created_at: '2023-05-17T09:15:00Z',
    total: 950000,
    status: 'pending',
    payment_status: 'pending',
    items: [],
  },
])

// Filters
const filters = reactive({
  search: '',
  status: '',
  date_range: '',
  start_date: '',
  end_date: '',
})

// Pagination
const pagination = reactive({
  currentPage: 1,
  perPage: 10,
  get total() {
    return filteredOrders.value.length
  },
  get totalPages() {
    return Math.ceil(this.total / this.perPage)
  },
  get from() {
    if (this.total === 0) return 0
    return (this.currentPage - 1) * this.perPage + 1
  },
  get to() {
    const to = this.currentPage * this.perPage
    return to > this.total ? this.total : to
  },
})

// ĐÃ SỬA: Bổ sung đầy đủ logic filter
const filteredOrders = computed(() => {
  return orders.value.filter((order) => {
    // Search filter
    const searchTerm = filters.search.toLowerCase()
    const matchesSearch =
      !searchTerm ||
      order.order_number.toLowerCase().includes(searchTerm) ||
      order.customer.name.toLowerCase().includes(searchTerm) ||
      order.customer.email.toLowerCase().includes(searchTerm) ||
      order.customer.phone.includes(searchTerm)

    // Status filter
    const matchesStatus = !filters.status || order.status === filters.status

    // Date range filter (ĐÃ THÊM LẠI)
    let matchesDateRange = true
    if (filters.date_range) {
      const orderDate = new Date(order.created_at)
      const today = new Date()
      today.setHours(0, 0, 0, 0)

      switch (filters.date_range) {
        case 'today':
          const orderDateStr = orderDate.toISOString().split('T')[0]
          const todayStr = today.toISOString().split('T')[0]
          matchesDateRange = orderDateStr === todayStr
          break
        case 'yesterday':
          const yesterday = new Date(today)
          yesterday.setDate(yesterday.getDate() - 1)
          const yesterdayStr = yesterday.toISOString().split('T')[0]
          const orderDateYestStr = orderDate.toISOString().split('T')[0]
          matchesDateRange = orderDateYestStr === yesterdayStr
          break
        case 'this_week':
          const day = orderDate.getDay()
          const diff = orderDate.getDate() - day + (day === 0 ? -6 : 1)
          const weekStart = new Date(orderDate.setDate(diff))
          weekStart.setHours(0, 0, 0, 0)

          const thisWeekStart = new Date()
          thisWeekStart.setDate(
            thisWeekStart.getDate() - thisWeekStart.getDay() + (thisWeekStart.getDay() === 0 ? -6 : 1)
          )
          thisWeekStart.setHours(0, 0, 0, 0)

          matchesDateRange = orderDate >= thisWeekStart && orderDate <= new Date()
          break
        case 'last_week':
          const lastWeekStart = new Date()
          lastWeekStart.setDate(lastWeekStart.getDate() - lastWeekStart.getDay() - 6)
          lastWeekStart.setHours(0, 0, 0, 0)

          const lastWeekEnd = new Date(lastWeekStart)
          lastWeekEnd.setDate(lastWeekStart.getDate() + 6)
          lastWeekEnd.setHours(23, 59, 59, 999)

          matchesDateRange = orderDate >= lastWeekStart && orderDate <= lastWeekEnd
          break
        case 'this_month':
          const thisMonthStart = new Date(today.getFullYear(), today.getMonth(), 1)
          matchesDateRange = orderDate >= thisMonthStart && orderDate <= new Date()
          break
        case 'last_month':
          const lastMonthEnd = new Date(today.getFullYear(), today.getMonth(), 0)
          const lastMonthStart = new Date(today.getFullYear(), today.getMonth() - 1, 1)
          matchesDateRange = orderDate >= lastMonthStart && orderDate <= lastMonthEnd
          break
        case 'custom':
          if (filters.start_date && filters.end_date) {
            const startDate = new Date(filters.start_date)
            startDate.setHours(0, 0, 0, 0)

            const endDate = new Date(filters.end_date)
            endDate.setHours(23, 59, 59, 999)

            matchesDateRange = orderDate >= startDate && orderDate <= endDate
          }
          break
      }
    }

    return matchesSearch && matchesStatus && matchesDateRange
  })
})

const paginatedOrders = computed(() => {
  // Reset về trang 1 nếu filter thay đổi và trang hiện tại vượt quá tổng số trang mới
  if (pagination.currentPage > pagination.totalPages && pagination.totalPages > 0) {
    pagination.currentPage = 1
  }
  
  const start = (pagination.currentPage - 1) * pagination.perPage
  const end = start + pagination.perPage
  return filteredOrders.value.slice(start, end)
})

// Cancel order modal
const showCancelModal = ref(false)
const orderToCancel = ref<any>(null)
const cancelling = ref(false)

// Format currency
const formatCurrency = (value: number) => {
  return new Intl.NumberFormat('vi-VN', {
    style: 'currency',
    currency: 'VND',
  }).format(value)
}

// Format date
const formatDate = (dateString: string) => {
  const options: Intl.DateTimeFormatOptions = {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
  }
  return new Date(dateString).toLocaleDateString('vi-VN', options)
}

// Format date and time
const formatDateTime = (dateTimeString: string) => {
  const options: Intl.DateTimeFormatOptions = {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  }
  return new Date(dateTimeString).toLocaleString('vi-VN', options)
}

// Get status class
const getOrderStatusClass = (status: string) => {
  const statusMap: Record<string, string> = {
    pending: 'bg-yellow-100 text-yellow-800',
    processing: 'bg-blue-100 text-blue-800',
    shipped: 'bg-indigo-100 text-indigo-800',
    delivered: 'bg-green-100 text-green-800',
    cancelled: 'bg-red-100 text-red-800',
    refunded: 'bg-purple-100 text-purple-800',
  }
  return statusMap[status] || 'bg-gray-100 text-gray-800'
}

// Get status text
const getOrderStatusText = (status: string) => {
  const statusMap: Record<string, string> = {
    pending: 'Chờ xác nhận',
    processing: 'Đang xử lý',
    shipped: 'Đang giao hàng',
    delivered: 'Đã giao hàng',
    cancelled: 'Đã hủy',
    refunded: 'Đã hoàn tiền',
  }
  return statusMap[status] || status
}

// Get payment status class
const getPaymentStatusClass = (status: string) => {
  const statusMap: Record<string, string> = {
    pending: 'bg-yellow-100 text-yellow-800',
    paid: 'bg-green-100 text-green-800',
    failed: 'bg-red-100 text-red-800',
    refunded: 'bg-purple-100 text-purple-800',
    cancelled: 'bg-gray-100 text-gray-800',
  }
  return statusMap[status] || 'bg-gray-100 text-gray-800'
}

// Get payment status text
const getPaymentStatusText = (status: string) => {
  const statusMap: Record<string, string> = {
    pending: 'Chờ thanh toán',
    paid: 'Đã thanh toán',
    failed: 'Thanh toán thất bại',
    refunded: 'Đã hoàn tiền',
    cancelled: 'Đã hủy',
  }
  return statusMap[status] || status
}

// Check if order can be cancelled
const canCancelOrder = (order: any) => {
  return ['pending', 'processing'].includes(order.status)
}

// Confirm cancel order
const confirmCancelOrder = (order: any) => {
  orderToCancel.value = order
  showCancelModal.value = true
}

// Cancel order
const cancelOrder = async () => {
  if (!orderToCancel.value) return

  cancelling.value = true

  try {
    await new Promise((resolve) => setTimeout(resolve, 1000))
    const order = orders.value.find((o) => o.id === orderToCancel.value.id)
    if (order) {
      order.status = 'cancelled'
      order.payment_status = 'refunded'
    }
    showCancelModal.value = false
  } catch (error) {
    console.error('Error cancelling order:', error)
  } finally {
    cancelling.value = false
  }
}

// Apply filters
const applyFilters = () => {
  pagination.currentPage = 1
}

// Reset filters
const resetFilters = () => {
  filters.search = ''
  filters.status = ''
  filters.date_range = ''
  filters.start_date = ''
  filters.end_date = ''
  pagination.currentPage = 1
}

// Pagination methods
const goToPage = (page: number) => {
  if (page >= 1 && page <= pagination.totalPages) {
    pagination.currentPage = page
  }
}

const previousPage = () => {
  if (pagination.currentPage > 1) {
    pagination.currentPage--
  }
}

const nextPage = () => {
  if (pagination.currentPage < pagination.totalPages) {
    pagination.currentPage++
  }
}
</script>

<style scoped>
.order-list {
  min-height: calc(100vh - 64px);
}
</style>