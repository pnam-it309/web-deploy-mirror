<template>
  <div class="order-detail">
    <!-- Order Header -->
    <div class="bg-white shadow">
      <div class="max-w-7xl mx-auto py-6 px-4 sm:px-6 lg:px-8">
        <div class="flex flex-col md:flex-row md:items-center md:justify-between">
          <div>
            <h1 class="text-3xl font-bold text-gray-900">Đơn hàng #{{ order.order_number }}</h1>
            <div class="mt-1 flex flex-col sm:flex-row sm:flex-wrap sm:mt-0 sm:space-x-6">
              <div class="mt-2 flex items-center text-sm text-gray-500">
                <svg class="flex-shrink-0 mr-1.5 h-5 w-5 text-gray-400" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor">
                  <path fill-rule="evenodd" d="M6 2a1 1 0 00-1 1v1H4a2 2 0 00-2 2v10a2 2 0 002 2h12a2 2 0 002-2V6a2 2 0 00-2-2h-1V3a1 1 0 10-2 0v1H7V3a1 1 0 00-1-1zm0 5a1 1 0 000 2h8a1 1 0 100-2H6z" clip-rule="evenodd" />
                </svg>
                Đặt lúc {{ formatDateTime(order.created_at) }}
              </div>
              <div class="mt-2 flex items-center text-sm text-gray-500">
                <svg class="flex-shrink-0 mr-1.5 h-5 w-5 text-gray-400" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor">
                  <path fill-rule="evenodd" d="M10 2a8 8 0 100 16 8 8 0 000-16zm0 14a6 6 0 110-12 6 6 0 010 12z" clip-rule="evenodd" />
                  <path fill-rule="evenodd" d="M10 5a1 1 0 011 1v4.586l2.707 2.707a1 1 0 01-1.414 1.414l-3-3A1 1 0 019 11V6a1 1 0 011-1z" clip-rule="evenodd" />
                </svg>
                Cập nhật lần cuối: {{ formatDateTime(order.updated_at) }}
              </div>
            </div>
          </div>
          <div class="mt-4 flex md:mt-0 md:ml-4 space-x-3">
            <button
              @click="printOrder"
              type="button"
              class="inline-flex items-center px-4 py-2 border border-gray-300 shadow-sm text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
            >
              <svg class="-ml-1 mr-2 h-5 w-5 text-gray-500" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor">
                <path fill-rule="evenodd" d="M5 4v3H4a2 2 0 00-2 2v3a2 2 0 002 2h1v2a2 2 0 002 2h6a2 2 0 002-2v-2h1a2 2 0 002-2V9a2 2 0 00-2-2h-1V4a2 2 0 00-2-2H7a2 2 0 00-2 2zm8 0H7v3h6V4zm0 8H7v4h6v-4z" clip-rule="evenodd" />
              </svg>
              In đơn hàng
            </button>
            <router-link
              :to="{ name: 'admin-orders' }"
              class="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
            >
              <svg class="-ml-1 mr-2 h-5 w-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor">
                <path fill-rule="evenodd" d="M12.707 5.293a1 1 0 010 1.414L9.414 10l3.293 3.293a1 1 0 01-1.414 1.414l-4-4a1 1 0 010-1.414l4-4a1 1 0 011.414 0z" clip-rule="evenodd" />
              </svg>
              Quay lại
            </router-link>
          </div>
        </div>
      </div>
    </div>

    <div class="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
      <!-- Order Status & Actions -->
      <div class="bg-white shadow overflow-hidden sm:rounded-lg mb-6">
        <div class="px-4 py-5 sm:px-6 flex flex-wrap items-center justify-between">
          <div>
            <h3 class="text-lg leading-6 font-medium text-gray-900">
              Trạng thái đơn hàng
            </h3>
            <p class="mt-1 max-w-2xl text-sm text-gray-500">
              Cập nhật trạng thái đơn hàng và thông báo cho khách hàng
            </p>
          </div>
          <div class="mt-4 sm:mt-0">
            <div class="flex items-center space-x-2">
              <span class="text-sm font-medium text-gray-700">Trạng thái hiện tại:</span>
              <span :class="getStatusClass(order.status)" class="px-2.5 py-0.5 rounded-full text-xs font-medium">
                {{ getStatusText(order.status) }}
              </span>
            </div>
          </div>
        </div>
        <div class="border-t border-gray-200 px-4 py-5 sm:px-6">
          <div class="flex flex-wrap items-center justify-between">
            <div class="flex space-x-2">
              <button
                v-for="status in availableStatuses"
                :key="status.value"
                @click="updateOrderStatus(status.value)"
                :disabled="isUpdating || !canUpdateToStatus(order.status, status.value)"
                :class="[
                  'inline-flex items-center px-4 py-2 border text-sm font-medium rounded-md shadow-sm',
                  order.status === status.value
                    ? 'bg-indigo-100 text-indigo-700 border-indigo-300'
                    : canUpdateToStatus(order.status, status.value)
                      ? 'bg-white text-gray-700 border-gray-300 hover:bg-gray-50'
                      : 'bg-gray-50 text-gray-400 border-gray-200 cursor-not-allowed'
                ]"
              >
                <span>{{ status.label }}</span>
                <span v-if="isUpdating && status.value === order.status" class="ml-2">
                  <svg class="animate-spin h-4 w-4 text-indigo-500" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                    <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                    <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                  </svg>
                </span>
              </button>
            </div>
            <div class="mt-4 sm:mt-0">
              <button
                v-if="canCancelOrder"
                @click="confirmCancelOrder"
                class="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-red-600 hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500"
              >
                <svg class="-ml-1 mr-2 h-5 w-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor">
                  <path fill-rule="evenodd" d="M9 2a1 1 0 00-.894.553L7.382 4H4a1 1 0 000 2v10a2 2 0 002 2h8a2 2 0 002-2V6a1 1 0 100-2h-3.382l-.724-1.447A1 1 0 0011 2H9zM7 8a1 1 0 012 0v6a1 1 0 11-2 0V8zm5-1a1 1 0 00-1 1v6a1 1 0 102 0V8a1 1 0 00-1-1z" clip-rule="evenodd" />
                </svg>
                Hủy đơn hàng
              </button>
            </div>
          </div>
        </div>
      </div>

      <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <!-- Order Summary -->
        <div class="lg:col-span-2 space-y-6">
          <!-- Order Items -->
          <div class="bg-white shadow overflow-hidden sm:rounded-lg">
            <div class="px-4 py-5 sm:px-6">
              <h3 class="text-lg leading-6 font-medium text-gray-900">
                Sản phẩm đã đặt
              </h3>
            </div>
            <div class="border-t border-gray-200">
              <table class="min-w-full divide-y divide-gray-200">
                <thead class="bg-gray-50">
                  <tr>
                    <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Sản phẩm
                    </th>
                    <th scope="col" class="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Đơn giá
                    </th>
                    <th scope="col" class="px-6 py-3 text-center text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Số lượng
                    </th>
                    <th scope="col" class="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Thành tiền
                    </th>
                  </tr>
                </thead>
                <tbody class="bg-white divide-y divide-gray-200">
                  <tr v-for="item in order.items" :key="item.id">
                    <td class="px-6 py-4 whitespace-nowrap">
                      <div class="flex items-center">
                        <div class="flex-shrink-0 h-10 w-10">
                          <img class="h-10 w-10 rounded-md" :src="item.image || 'https://via.placeholder.com/40'" :alt="item.name" />
                        </div>
                        <div class="ml-4">
                          <div class="text-sm font-medium text-gray-900">{{ item.name }}</div>
                          <div class="text-sm text-gray-500">SKU: {{ item.sku || 'N/A' }}</div>
                        </div>
                      </div>
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900 text-right">
                      {{ formatCurrency(item.price) }}
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500 text-center">
                      {{ item.quantity }}
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900 text-right">
                      {{ formatCurrency(item.total) }}
                    </td>
                  </tr>
                </tbody>
                <tfoot class="bg-gray-50">
                  <tr>
                    <td colspan="3" class="px-6 py-3 text-sm font-medium text-gray-900 text-right">
                      Tạm tính:
                    </td>
                    <td class="px-6 py-3 text-sm font-medium text-gray-900 text-right">
                      {{ formatCurrency(order.subtotal) }}
                    </td>
                  </tr>
                  <tr v-if="order.discount > 0">
                    <td colspan="3" class="px-6 py-1 text-sm text-gray-500 text-right">
                      Giảm giá:
                    </td>
                    <td class="px-6 py-1 text-sm text-gray-500 text-right">
                      -{{ formatCurrency(order.discount) }}
                    </td>
                  </tr>
                  <tr v-if="order.shipping_fee > 0">
                    <td colspan="3" class="px-6 py-1 text-sm text-gray-500 text-right">
                      Phí vận chuyển:
                    </td>
                    <td class="px-6 py-1 text-sm text-gray-500 text-right">
                      {{ formatCurrency(order.shipping_fee) }}
                    </td>
                  </tr>
                  <tr v-if="order.tax_amount > 0">
                    <td colspan="3" class="px-6 py-1 text-sm text-gray-500 text-right">
                      Thuế (VAT):
                    </td>
                    <td class="px-6 py-1 text-sm text-gray-500 text-right">
                      {{ formatCurrency(order.tax_amount) }}
                    </td>
                  </tr>
                  <tr>
                    <td colspan="3" class="px-6 py-3 text-base font-bold text-gray-900 text-right">
                      Tổng cộng:
                    </td>
                    <td class="px-6 py-3 text-base font-bold text-gray-900 text-right">
                      {{ formatCurrency(order.total) }}
                    </td>
                  </tr>
                </tfoot>
              </table>
            </div>
          </div>

          <!-- Order Notes -->
          <div class="bg-white shadow overflow-hidden sm:rounded-lg">
            <div class="px-4 py-5 sm:px-6">
              <h3 class="text-lg leading-6 font-medium text-gray-900">
                Ghi chú đơn hàng
              </h3>
            </div>
            <div class="border-t border-gray-200 px-4 py-5 sm:p-6">
              <div v-if="order.notes && order.notes.length > 0" class="space-y-4">
                <div v-for="(note, index) in order.notes" :key="index" class="border-l-4 border-gray-200 pl-4 py-2">
                  <div class="flex items-center justify-between">
                    <p class="text-sm text-gray-700">{{ note.content }}</p>
                    <span class="text-xs text-gray-500">{{ formatDateTime(note.created_at) }}</span>
                  </div>
                  <p class="text-xs text-gray-500 mt-1">
                    <span v-if="note.user">{{ note.user.name }}</span>
                    <span v-else>Hệ thống</span>
                  </p>
                </div>
              </div>
              <p v-else class="text-sm text-gray-500">Không có ghi chú nào cho đơn hàng này.</p>
              
              <div class="mt-4">
                <div class="flex items-start space-x-4">
                  <div class="min-w-0 flex-1">
                    <form @submit.prevent="addNote">
                      <div>
                        <label for="note" class="sr-only">Thêm ghi chú</label>
                        <textarea
                          id="note"
                          v-model="newNote"
                          rows="3"
                          class="shadow-sm block w-full focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm border border-gray-300 rounded-md"
                          placeholder="Thêm ghi chú về đơn hàng này..."
                        ></textarea>
                      </div>
                      <div class="mt-3 flex items-center justify-between">
                        <div class="flex items-center">
                          <input
                            id="notify_customer"
                            v-model="notifyCustomer"
                            type="checkbox"
                            class="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded"
                          />
                          <label for="notify_customer" class="ml-2 block text-sm text-gray-700">
                            Gửi thông báo cho khách hàng
                          </label>
                        </div>
                        <button
                          type="submit"
                          class="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                        >
                          Thêm ghi chú
                        </button>
                      </div>
                    </form>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Order Information -->
        <div class="space-y-6">
          <!-- Customer Information -->
          <div class="bg-white shadow overflow-hidden sm:rounded-lg">
            <div class="px-4 py-5 sm:px-6">
              <h3 class="text-lg leading-6 font-medium text-gray-900">
                Thông tin khách hàng
              </h3>
            </div>
            <div class="border-t border-gray-200 px-4 py-5 sm:p-0">
              <dl class="sm:divide-y sm:divide-gray-200">
                <div class="py-4 sm:py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
                  <dt class="text-sm font-medium text-gray-500">
                    Tên khách hàng
                  </dt>
                  <dd class="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                    {{ order.customer.name }}
                  </dd>
                </div>
                <div class="py-4 sm:py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
                  <dt class="text-sm font-medium text-gray-500">
                    Email
                  </dt>
                  <dd class="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                    {{ order.customer.email }}
                  </dd>
                </div>
                <div class="py-4 sm:py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
                  <dt class="text-sm font-medium text-gray-500">
                    Số điện thoại
                  </dt>
                  <dd class="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                    {{ order.customer.phone || 'N/A' }}
                  </dd>
                </div>
                <div class="py-4 sm:py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
                  <dt class="text-sm font-medium text-gray-500">
                    Địa chỉ
                  </dt>
                  <dd class="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                    {{ order.shipping_address.address_line1 }},<br>
                    <template v-if="order.shipping_address.address_line2">
                      {{ order.shipping_address.address_line2 }},<br>
                    </template>
                    {{ order.shipping_address.city }}, {{ order.shipping_address.state }}<br>
                    {{ order.shipping_address.country }}, {{ order.shipping_address.postal_code }}
                  </dd>
                </div>
              </dl>
            </div>
          </div>

          <!-- Shipping Information -->
          <div class="bg-white shadow overflow-hidden sm:rounded-lg">
            <div class="px-4 py-5 sm:px-6">
              <h3 class="text-lg leading-6 font-medium text-gray-900">
                Thông tin giao hàng
              </h3>
            </div>
            <div class="border-t border-gray-200 px-4 py-5 sm:p-0">
              <dl class="sm:divide-y sm:divide-gray-200">
                <div class="py-4 sm:py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
                  <dt class="text-sm font-medium text-gray-500">
                    Phương thức vận chuyển
                  </dt>
                  <dd class="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                    {{ order.shipping_method?.name || 'Chưa chọn' }}
                  </dd>
                </div>
                <div class="py-4 sm:py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
                  <dt class="text-sm font-medium text-gray-500">
                    Trạng thái giao hàng
                  </dt>
                  <dd class="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                    <span :class="getShippingStatusClass(order.shipping_status)" class="px-2.5 py-0.5 rounded-full text-xs font-medium">
                      {{ getShippingStatusText(order.shipping_status) }}
                    </span>
                  </dd>
                </div>
                <div v-if="order.tracking_number" class="py-4 sm:py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
                  <dt class="text-sm font-medium text-gray-500">
                    Mã vận đơn
                  </dt>
                  <dd class="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                    {{ order.tracking_number }}
                    <a v-if="order.tracking_url" :href="order.tracking_url" target="_blank" class="ml-2 text-indigo-600 hover:text-indigo-900">
                      (Theo dõi đơn hàng)
                    </a>
                  </dd>
                </div>
                <div v-if="order.shipped_at" class="py-4 sm:py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
                  <dt class="text-sm font-medium text-gray-500">
                    Ngày gửi hàng
                  </dt>
                  <dd class="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                    {{ formatDateTime(order.shipped_at) }}
                  </dd>
                </div>
                <div v-if="order.delivered_at" class="py-4 sm:py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
                  <dt class="text-sm font-medium text-gray-500">
                    Ngày giao hàng
                  </dt>
                  <dd class="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                    {{ formatDateTime(order.delivered_at) }}
                  </dd>
                </div>
              </dl>
            </div>
          </div>

          <!-- Payment Information -->
          <div class="bg-white shadow overflow-hidden sm:rounded-lg">
            <div class="px-4 py-5 sm:px-6">
              <h3 class="text-lg leading-6 font-medium text-gray-900">
                Thông tin thanh toán
              </h3>
            </div>
            <div class="border-t border-gray-200 px-4 py-5 sm:p-0">
              <dl class="sm:divide-y sm:divide-gray-200">
                <div class="py-4 sm:py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
                  <dt class="text-sm font-medium text-gray-500">
                    Phương thức thanh toán
                  </dt>
                  <dd class="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                    {{ order.payment_method?.name || 'Chưa chọn' }}
                  </dd>
                </div>
                <div class="py-4 sm:py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
                  <dt class="text-sm font-medium text-gray-500">
                    Trạng thái thanh toán
                  </dt>
                  <dd class="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                    <span :class="getPaymentStatusClass(order.payment_status)" class="px-2.5 py-0.5 rounded-full text-xs font-medium">
                      {{ getPaymentStatusText(order.payment_status) }}
                    </span>
                  </dd>
                </div>
                <div v-if="order.paid_at" class="py-4 sm:py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
                  <dt class="text-sm font-medium text-gray-500">
                    Ngày thanh toán
                  </dt>
                  <dd class="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                    {{ formatDateTime(order.paid_at) }}
                  </dd>
                </div>
                <div v-if="order.payment_reference" class="py-4 sm:py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
                  <dt class="text-sm font-medium text-gray-500">
                    Mã giao dịch
                  </dt>
                  <dd class="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                    {{ order.payment_reference }}
                  </dd>
                </div>
              </dl>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Cancel Order Modal -->
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
                  Bạn có chắc chắn muốn hủy đơn hàng #{{ order.order_number }}? Hành động này không thể hoàn tác.
                </p>
              </div>
              <div class="mt-4">
                <label for="cancellation_reason" class="block text-sm font-medium text-gray-700">Lý do hủy đơn</label>
                <select
                  id="cancellation_reason"
                  v-model="cancellationReason"
                  class="mt-1 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm rounded-md"
                  required
                >
                  <option value="">Chọn lý do hủy đơn</option>
                  <option value="out_of_stock">Hết hàng</option>
                  <option value="customer_request">Theo yêu cầu của khách hàng</option>
                  <option value="payment_issue">Vấn đề thanh toán</option>
                  <option value="shipping_issue">Không thể giao hàng</option>
                  <option value="other">Lý do khác</option>
                </select>
              </div>
              <div v-if="cancellationReason === 'other'" class="mt-2">
                <label for="custom_reason" class="block text-sm font-medium text-gray-700">Mô tả lý do</label>
                <textarea
                  id="custom_reason"
                  v-model="customCancellationReason"
                  rows="3"
                  class="shadow-sm focus:ring-indigo-500 focus:border-indigo-500 block w-full sm:text-sm border border-gray-300 rounded-md"
                  placeholder="Nhập lý do hủy đơn hàng..."
                  required
                ></textarea>
              </div>
            </div>
          </div>
          <div class="mt-5 sm:mt-4 sm:flex sm:flex-row-reverse">
            <button
              type="button"
              @click="cancelOrder"
              class="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-red-600 text-base font-medium text-white hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500 sm:ml-3 sm:w-auto sm:text-sm"
              :disabled="!cancellationReason || (cancellationReason === 'other' && !customCancellationReason)"
            >
              <svg v-if="isCancelling" class="animate-spin -ml-1 mr-3 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
              </svg>
              {{ isCancelling ? 'Đang xử lý...' : 'Xác nhận hủy' }}
            </button>
            <button
              type="button"
              @click="showCancelModal = false"
              class="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:mt-0 sm:w-auto sm:text-sm"
              :disabled="isCancelling"
            >
              Hủy bỏ
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';

export default defineComponent({
  name: 'OrderDetail',
  props: {
    id: {
      type: [String, Number],
      required: true
    }
  },
  setup(props) {
    const route = useRoute();
    const router = useRouter();
    
    // Order data - replace with actual API call
    const order = ref({
      id: 1,
      order_number: 'ORD-2023-1001',
      customer: {
        id: 1,
        name: 'Nguyễn Văn A',
        email: 'nguyenvana@example.com',
        phone: '0912345678',
        avatar: 'https://via.placeholder.com/40'
      },
      status: 'processing',
      payment_status: 'paid',
      payment_method: {
        id: 1,
        name: 'Chuyển khoản ngân hàng',
        code: 'bank_transfer'
      },
      payment_reference: 'PAY-123456',
      paid_at: '2023-05-15T10:45:00Z',
      shipping_status: 'processing',
      shipping_method: {
        id: 1,
        name: 'Giao hàng tiêu chuẩn',
        code: 'standard',
        cost: 30000
      },
      tracking_number: 'GHN123456789',
      tracking_url: 'https://tracking.ghn.vn/order/status?id=GHN123456789',
      shipping_address: {
        first_name: 'Nguyễn Văn',
        last_name: 'A',
        address_line1: '123 Đường ABC',
        address_line2: 'Phường 1, Quận 1',
        city: 'Hồ Chí Minh',
        state: 'Hồ Chí Minh',
        country: 'Việt Nam',
        postal_code: '70000',
        phone: '0912345678'
      },
      billing_address: {
        first_name: 'Nguyễn Văn',
        last_name: 'A',
        address_line1: '123 Đường ABC',
        address_line2: 'Phường 1, Quận 1',
        city: 'Hồ Chí Minh',
        state: 'Hồ Chí Minh',
        country: 'Việt Nam',
        postal_code: '70000',
        phone: '0912345678'
      },
      items: [
        {
          id: 1,
          product_id: 101,
          name: 'Áo thun nam cổ tròn',
          sku: 'ATN-001',
          price: 250000,
          quantity: 2,
          total: 500000,
          image: 'https://via.placeholder.com/100'
        },
        {
          id: 2,
          product_id: 102,
          name: 'Quần jean nam ống đứng',
          sku: 'QJN-001',
          price: 750000,
          quantity: 1,
          total: 750000,
          image: 'https://via.placeholder.com/100'
        }
      ],
      subtotal: 1250000,
      discount: 0,
      shipping_fee: 30000,
      tax_amount: 125000,
      total: 1405000,
      notes: [
        {
          id: 1,
          content: 'Khách hàng yêu cầu giao hàng trước 17h',
          created_at: '2023-05-15T10:35:00Z',
          user: {
            id: 1,
            name: 'Quản trị viên',
            email: 'admin@example.com'
          }
        },
        {
          id: 2,
          content: 'Đã xác nhận đơn hàng và chuẩn bị giao',
          created_at: '2023-05-15T11:20:00Z',
          user: {
            id: 1,
            name: 'Nhân viên kho',
            email: 'warehouse@example.com'
          }
        }
      ],
      created_at: '2023-05-15T10:30:00Z',
      updated_at: '2023-05-15T11:20:00Z',
      shipped_at: null,
      delivered_at: null
    });

    // Form data
    const newNote = ref('');
    const notifyCustomer = ref(false);
    const isUpdating = ref(false);
    const isCancelling = ref(false);
    const showCancelModal = ref(false);
    const cancellationReason = ref('');
    const customCancellationReason = ref('');

    // Available statuses for order
    const availableStatuses = [
      { value: 'pending', label: 'Chờ xác nhận' },
      { value: 'processing', label: 'Đang xử lý' },
      { value: 'shipped', label: 'Đang giao hàng' },
      { value: 'delivered', label: 'Đã giao hàng' },
      { value: 'cancelled', label: 'Đã hủy' }
    ];

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
    const getStatusClass = (status: string) => {
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
    const getStatusText = (status: string) => {
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

    // Get shipping status class
    const getShippingStatusClass = (status: string) => {
      const statusMap: Record<string, string> = {
        'pending': 'bg-yellow-100 text-yellow-800',
        'processing': 'bg-blue-100 text-blue-800',
        'shipped': 'bg-indigo-100 text-indigo-800',
        'in_transit': 'bg-purple-100 text-purple-800',
        'delivered': 'bg-green-100 text-green-800',
        'cancelled': 'bg-red-100 text-red-800',
        'returned': 'bg-gray-100 text-gray-800'
      };
      return statusMap[status] || 'bg-gray-100 text-gray-800';
    };

    // Get shipping status text
    const getShippingStatusText = (status: string) => {
      const statusMap: Record<string, string> = {
        'pending': 'Chờ xử lý',
        'processing': 'Đang đóng gói',
        'shipped': 'Đã giao cho đơn vị vận chuyển',
        'in_transit': 'Đang vận chuyển',
        'out_for_delivery': 'Đang giao hàng',
        'delivered': 'Đã giao hàng',
        'cancelled': 'Đã hủy',
        'returned': 'Đã trả hàng'
      };
      return statusMap[status] || status;
    };

    // Check if order can be cancelled
    const canCancelOrder = computed(() => {
      return ['pending', 'processing'].includes(order.value.status);
    });

    // Check if status can be updated to target status
    const canUpdateToStatus = (currentStatus: string, targetStatus: string) => {
      const statusFlow: Record<string, string[]> = {
        'pending': ['processing', 'cancelled'],
        'processing': ['shipped', 'cancelled'],
        'shipped': ['delivered'],
        'delivered': [],
        'cancelled': []
      };
      
      return statusFlow[currentStatus]?.includes(targetStatus) || false;
    };

    // Update order status
    const updateOrderStatus = async (status: string) => {
      if (status === order.value.status || !canUpdateToStatus(order.value.status, status)) {
        return;
      }
      
      try {
        isUpdating.value = true;
        
        // Simulate API call
        await new Promise(resolve => setTimeout(resolve, 1000));
        
        // Update order status in local data
        order.value.status = status;
        order.value.updated_at = new Date().toISOString();
        
        // Add system note
        const note = {
          id: Date.now(),
          content: `Cập nhật trạng thái đơn hàng thành "${getStatusText(status)}"`,
          created_at: new Date().toISOString(),
          user: {
            id: 1,
            name: 'Quản trị viên',
            email: 'admin@example.com'
          }
        };
        
        order.value.notes = [note, ...order.value.notes];
        
        // Update shipping status if needed
        if (status === 'shipped') {
          order.value.shipping_status = 'shipped';
          order.value.shipped_at = new Date().toISOString();
        } else if (status === 'delivered') {
          order.value.shipping_status = 'delivered';
          order.value.delivered_at = new Date().toISOString();
        }
        
      } catch (error) {
        console.error('Error updating order status:', error);
      } finally {
        isUpdating.value = false;
      }
    };

    // Add note to order
    const addNote = async () => {
      if (!newNote.value.trim()) return;
      
      try {
        // Simulate API call
        await new Promise(resolve => setTimeout(resolve, 500));
        
        const note = {
          id: Date.now(),
          content: newNote.value,
          created_at: new Date().toISOString(),
          user: {
            id: 1,
            name: 'Quản trị viên',
            email: 'admin@example.com'
          }
        };
        
        order.value.notes = [note, ...order.value.notes];
        newNote.value = '';
        
        // Notify customer if selected
        if (notifyCustomer.value) {
          // Send notification to customer
          console.log('Sending notification to customer:', order.value.customer.email);
        }
        
      } catch (error) {
        console.error('Error adding note:', error);
      }
    };

    // Confirm cancel order
    const confirmCancelOrder = () => {
      cancellationReason.value = '';
      customCancellationReason.value = '';
      showCancelModal.value = true;
    };

    // Cancel order
    const cancelOrder = async () => {
      if (!cancellationReason.value || (cancellationReason.value === 'other' && !customCancellationReason.value)) {
        return;
      }
      
      try {
        isCancelling.value = true;
        
        const reason = cancellationReason.value === 'other' 
          ? customCancellationReason.value 
          : cancellationReason.value;
        
        // Simulate API call
        await new Promise(resolve => setTimeout(resolve, 1000));
        
        // Update order status
        order.value.status = 'cancelled';
        order.value.updated_at = new Date().toISOString();
        
        // Add cancellation note
        const note = {
          id: Date.now(),
          content: `Đơn hàng đã bị hủy. Lý do: ${reason}`,
          created_at: new Date().toISOString(),
          user: {
            id: 1,
            name: 'Quản trị viên',
            email: 'admin@example.com'
          }
        };
        
        order.value.notes = [note, ...order.value.notes];
        
        // Close modal
        showCancelModal.value = false;
        
      } catch (error) {
        console.error('Error cancelling order:', error);
      } finally {
        isCancelling.value = false;
      }
    };

    // Print order
    const printOrder = () => {
      window.print();
    };

    // Fetch order data
    const fetchOrder = async () => {
      try {
        // Simulate API call
        await new Promise(resolve => setTimeout(resolve, 500));
        
        // In a real app, you would fetch the order data from your API
        // const response = await orderApi.getOrder(route.params.id);
        // order.value = response.data;
        
      } catch (error) {
        console.error('Error fetching order:', error);
      }
    };

    // Initialize component
    onMounted(() => {
      fetchOrder();
    });

    return {
      // Data
      order,
      newNote,
      notifyCustomer,
      isUpdating,
      isCancelling,
      showCancelModal,
      cancellationReason,
      customCancellationReason,
      availableStatuses,
      
      // Methods
      formatCurrency,
      formatDate,
      formatDateTime,
      getStatusClass,
      getStatusText,
      getPaymentStatusClass,
      getPaymentStatusText,
      getShippingStatusClass,
      getShippingStatusText,
      canUpdateToStatus,
      updateOrderStatus,
      addNote,
      confirmCancelOrder,
      cancelOrder,
      printOrder,
      canCancelOrder
    };
  }
});
</script>

<style scoped>
.order-detail {
  min-height: calc(100vh - 64px);
}

/* Print styles */
@media print {
  .no-print {
    display: none !important;
  }
  
  body, html, #app {
    margin: 0;
    padding: 0;
    background: #fff;
  }
  
  .order-detail {
    font-size: 12px;
  }
  
  .shadow {
    box-shadow: none !important;
  }
  
  .border, .border-t, .border-b, .border-l, .border-r {
    border-color: #e5e7eb !important;
  }
  
  .bg-gray-50 {
    background-color: #f9fafb !important;
  }
  
  .text-gray-900 {
    color: #111827 !important;
  }
  
  .text-gray-500 {
    color: #6b7280 !important;
  }
}
</style>
