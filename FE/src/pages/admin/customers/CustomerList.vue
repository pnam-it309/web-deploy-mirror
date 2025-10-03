<template>
  <div class="customer-list">
    <div class="bg-white shadow">
      <div class="max-w-7xl mx-auto py-6 px-4 sm:px-6 lg:px-8 flex justify-between items-center">
        <h1 class="text-3xl font-bold text-gray-900">Quản lý khách hàng</h1>
        <router-link
          :to="{ name: 'admin-customer-create' }"
          class="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
        >
          <svg class="-ml-1 mr-2 h-5 w-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
            <path fill-rule="evenodd" d="M10 5a1 1 0 011 1v3h3a1 1 0 110 2h-3v3a1 1 0 11-2 0v-3H6a1 1 0 110-2h3V6a1 1 0 011-1z" clip-rule="evenodd" />
          </svg>
          Thêm khách hàng
        </router-link>
      </div>
    </div>

    <div class="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
      <!-- Filters -->
      <div class="bg-white shadow rounded-lg p-4 mb-6">
        <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
          <!-- Search -->
          <div class="md:col-span-2">
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
                placeholder="Tên, email hoặc số điện thoại..."
                @keyup.enter="applyFilters"
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
              <option value="">Tất cả</option>
              <option value="active">Đang hoạt động</option>
              <option value="inactive">Ngừng hoạt động</option>
              <option value="banned">Đã khóa</option>
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

        <!-- Advanced Filters (toggleable) -->
        <div class="mt-4">
          <button
            @click="showAdvancedFilters = !showAdvancedFilters"
            type="button"
            class="text-sm text-indigo-600 hover:text-indigo-500 focus:outline-none flex items-center"
          >
            <span>Bộ lọc nâng cao</span>
            <svg
              :class="['ml-1 h-5 w-5 transform transition-transform', { 'rotate-180': showAdvancedFilters }]"
              xmlns="http://www.w3.org/2000/svg"
              viewBox="0 0 20 20"
              fill="currentColor"
              aria-hidden="true"
            >
              <path fill-rule="evenodd" d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z" clip-rule="evenodd" />
            </svg>
          </button>

          <div v-if="showAdvancedFilters" class="mt-4 grid grid-cols-1 md:grid-cols-3 gap-4 pt-4 border-t border-gray-200">
            <!-- Date Range -->
            <div>
              <label for="date_range" class="block text-sm font-medium text-gray-700">Ngày đăng ký</label>
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
                <option value="custom">Tùy chỉnh...</option>
              </select>
            </div>

            <!-- Custom Date Range -->
            <div v-if="filters.date_range === 'custom'" class="md:col-span-2">
              <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
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

            <!-- Customer Group -->
            <div>
              <label for="group" class="block text-sm font-medium text-gray-700">Nhóm khách hàng</label>
              <select
                id="group"
                v-model="filters.group"
                class="mt-1 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm rounded-md"
              >
                <option value="">Tất cả nhóm</option>
                <option v-for="group in customerGroups" :key="group.id" :value="group.id">
                  {{ group.name }}
                </option>
              </select>
            </div>

            <!-- Order Count -->
            <div>
              <label for="order_count" class="block text-sm font-medium text-gray-700">Số đơn hàng</label>
              <select
                id="order_count"
                v-model="filters.order_count"
                class="mt-1 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm rounded-md"
              >
                <option value="">Bất kỳ</option>
                <option value="0">Chưa có đơn hàng</option>
                <option value="1-5">1-5 đơn hàng</option>
                <option value="6-20">6-20 đơn hàng</option>
                <option value="20-50">20-50 đơn hàng</option>
                <option value="50+">Trên 50 đơn hàng</option>
              </select>
            </div>

            <!-- Total Spent -->
            <div>
              <label for="total_spent" class="block text-sm font-medium text-gray-700">Tổng chi tiêu</label>
              <select
                id="total_spent"
                v-model="filters.total_spent"
                class="mt-1 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm rounded-md"
              >
                <option value="">Bất kỳ</option>
                <option value="0">Chưa mua hàng</option>
                <option value="0-1000000">Dưới 1.000.000₫</option>
                <option value="1000000-5000000">1.000.000₫ - 5.000.000₫</option>
                <option value="5000000-20000000">5.000.000₫ - 20.000.000₫</option>
                <option value="20000000-100000000">20.000.000₫ - 100.000.000₫</option>
                <option value="100000000">Trên 100.000.000₫</option>
              </select>
            </div>
          </div>
        </div>
      </div>

      <!-- Bulk Actions -->
      <div v-if="selectedCustomers.length > 0" class="bg-indigo-50 p-4 rounded-lg mb-6">
        <div class="flex items-center justify-between">
          <div class="flex items-center">
            <span class="text-sm text-indigo-700">
              Đã chọn <span class="font-semibold">{{ selectedCustomers.length }}</span> khách hàng
            </span>
          </div>
          <div class="flex space-x-3">
            <select
              v-model="bulkAction"
              class="block w-40 pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm rounded-md"
            >
              <option value="">Thao tác hàng loạt</option>
              <option value="add_to_group">Thêm vào nhóm</option>
              <option value="remove_from_group">Xóa khỏi nhóm</option>
              <option value="change_status">Đổi trạng thái</option>
              <option value="export">Xuất dữ liệu</option>
              <option value="delete" class="text-red-600">Xóa khách hàng</option>
            </select>
            <button
              @click="applyBulkAction"
              :disabled="!bulkAction"
              class="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              Áp dụng
            </button>
          </div>
        </div>
      </div>

      <!-- Customers Table -->
      <div class="bg-white shadow overflow-hidden sm:rounded-lg">
        <div class="overflow-x-auto">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  <input
                    type="checkbox"
                    :checked="allSelected"
                    @change="toggleSelectAll"
                    class="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded"
                  />
                </th>
                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Khách hàng
                </th>
                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Nhóm
                </th>
                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Đơn hàng
                </th>
                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Chi tiêu
                </th>
                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Đăng ký lúc
                </th>
                <th scope="col" class="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Trạng thái
                </th>
                <th scope="col" class="relative px-6 py-3">
                  <span class="sr-only">Hành động</span>
                </th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="customer in paginatedCustomers" :key="customer.id" class="hover:bg-gray-50">
                <td class="px-6 py-4 whitespace-nowrap">
                  <input
                    type="checkbox"
                    :value="customer.id"
                    v-model="selectedCustomers"
                    class="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded"
                  />
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="flex items-center">
                    <div class="flex-shrink-0 h-10 w-10">
                      <img class="h-10 w-10 rounded-full" :src="customer.avatar || 'https://via.placeholder.com/40'" :alt="customer.name" />
                    </div>
                    <div class="ml-4">
                      <div class="text-sm font-medium text-gray-900">
                        {{ customer.name }}
                        <span v-if="customer.is_vip" class="ml-1 text-yellow-500">
                          <svg class="h-4 w-4 inline-block" fill="currentColor" viewBox="0 0 20 20">
                            <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
                          </svg>
                        </span>
                      </div>
                      <div class="text-sm text-gray-500">{{ customer.email }}</div>
                      <div class="text-sm text-gray-500">{{ customer.phone || 'Chưa có số điện thoại' }}</div>
                    </div>
                  </div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="flex flex-wrap gap-1">
                    <span 
                      v-for="group in customer.groups" 
                      :key="group.id"
                      class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium"
                      :class="getGroupColor(group.id)"
                    >
                      {{ group.name }}
                    </span>
                  </div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                  <div class="text-gray-900 font-medium">{{ customer.orders_count || 0 }} đơn</div>
                  <div>Thành công: {{ customer.orders_success_count || 0 }}</div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                  <div class="text-gray-900 font-medium">{{ formatCurrency(customer.total_spent || 0) }}</div>
                  <div>TB/đơn: {{ formatCurrency(customer.avg_order_value || 0) }}</div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                  {{ formatDate(customer.created_at) }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                  <span :class="getStatusClass(customer.status)" class="px-2.5 py-0.5 rounded-full text-xs font-medium">
                    {{ getStatusText(customer.status) }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                  <div class="flex justify-end space-x-2">
                    <router-link
                      :to="{ name: 'admin-customer-detail', params: { id: customer.id } }"
                      class="text-indigo-600 hover:text-indigo-900"
                      title="Xem chi tiết"
                    >
                      <svg class="h-5 w-5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"></path>
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"></path>
                      </svg>
                    </router-link>
                    <button
                      @click="editCustomer(customer)"
                      class="text-yellow-600 hover:text-yellow-900"
                      title="Chỉnh sửa"
                    >
                      <svg class="h-5 w-5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"></path>
                      </svg>
                    </button>
                    <button
                      @click="confirmDeleteCustomer(customer)"
                      class="text-red-600 hover:text-red-900"
                      title="Xóa"
                    >
                      <svg class="h-5 w-5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"></path>
                      </svg>
                    </button>
                  </div>
                </td>
              </tr>
              <tr v-if="filteredCustomers.length === 0">
                <td colspan="8" class="px-6 py-4 text-center text-sm text-gray-500">
                  Không tìm thấy khách hàng nào phù hợp
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

    <!-- Delete Confirmation Modal -->
    <div v-if="showDeleteModal" class="fixed z-10 inset-0 overflow-y-auto" aria-labelledby="modal-title" role="dialog" aria-modal="true">
      <div class="flex items-end justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
        <div class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity" aria-hidden="true" @click="showDeleteModal = false"></div>

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
                Xác nhận xóa khách hàng
              </h3>
              <div class="mt-2">
                <p class="text-sm text-gray-500">
                  Bạn có chắc chắn muốn xóa khách hàng <span class="font-medium">{{ customerToDelete?.name }}</span>? Hành động này không thể hoàn tác.
                </p>
                <div class="mt-4 bg-yellow-50 border-l-4 border-yellow-400 p-4">
                  <div class="flex">
                    <div class="flex-shrink-0">
                      <svg class="h-5 w-5 text-yellow-400" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor">
                        <path fill-rule="evenodd" d="M8.257 3.099c.765-1.36 2.722-1.36 3.486 0l5.58 9.92c.75 1.334-.213 2.98-1.742 2.98H4.42c-1.53 0-2.493-1.646-1.743-2.98l5.58-9.92zM11 13a1 1 0 11-2 0 1 1 0 012 0zm-1-8a1 1 0 00-1 1v3a1 1 0 002 0V6a1 1 0 00-1-1z" clip-rule="evenodd" />
                      </svg>
                    </div>
                    <div class="ml-3">
                      <p class="text-sm text-yellow-700">
                        Khách hàng này có <span class="font-medium">{{ customerToDelete?.orders_count || 0 }} đơn hàng</span> trong hệ thống. Việc xóa khách hàng sẽ không xóa các đơn hàng liên quan.
                      </p>
                    </div>
                  </div>
                </div>
                <div class="mt-4">
                  <label for="delete_option" class="block text-sm font-medium text-gray-700">Tùy chọn xóa</label>
                  <select
                    id="delete_option"
                    v-model="deleteOption"
                    class="mt-1 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm rounded-md"
                  >
                    <option value="soft">Xóa mềm (có thể khôi phục)</option>
                    <option value="force">Xóa vĩnh viễn (không thể khôi phục)</option>
                  </select>
                </div>
              </div>
            </div>
          </div>
          <div class="mt-5 sm:mt-4 sm:flex sm:flex-row-reverse">
            <button
              type="button"
              @click="deleteCustomer"
              class="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-red-600 text-base font-medium text-white hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500 sm:ml-3 sm:w-auto sm:text-sm"
              :disabled="deleting"
            >
              <svg v-if="deleting" class="animate-spin -ml-1 mr-3 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
              </svg>
              {{ deleting ? 'Đang xóa...' : 'Xác nhận xóa' }}
            </button>
            <button
              type="button"
              @click="showDeleteModal = false"
              class="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:mt-0 sm:w-auto sm:text-sm"
              :disabled="deleting"
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
import { defineComponent, ref, reactive, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';

export default defineComponent({
  name: 'CustomerList',
  setup() {
    const router = useRouter();
    
    // Sample data - replace with actual API calls
    const customers = ref([
      {
        id: 1,
        name: 'Nguyễn Văn A',
        email: 'nguyenvana@example.com',
        phone: '0912345678',
        avatar: 'https://randomuser.me/api/portraits/men/1.jpg',
        status: 'active',
        is_vip: true,
        groups: [
          { id: 1, name: 'Khách hàng thân thiết' },
          { id: 2, name: 'Đã mua nhiều' }
        ],
        orders_count: 15,
        orders_success_count: 12,
        total_spent: 18500000,
        avg_order_value: 1233333,
        created_at: '2023-01-15T10:30:00Z',
        last_order_at: '2023-05-20T14:25:00Z'
      },
      {
        id: 2,
        name: 'Trần Thị B',
        email: 'tranthib@example.com',
        phone: '0987654321',
        avatar: 'https://randomuser.me/api/portraits/women/2.jpg',
        status: 'active',
        is_vip: false,
        groups: [
          { id: 3, name: 'Khách hàng mới' }
        ],
        orders_count: 2,
        orders_success_count: 1,
        total_spent: 1250000,
        avg_order_value: 625000,
        created_at: '2023-04-22T08:15:00Z',
        last_order_at: '2023-05-18T16:45:00Z'
      },
      {
        id: 3,
        name: 'Lê Văn C',
        email: 'levanc@example.com',
        phone: '0905123456',
        avatar: 'https://randomuser.me/api/portraits/men/3.jpg',
        status: 'inactive',
        is_vip: false,
        groups: [],
        orders_count: 0,
        orders_success_count: 0,
        total_spent: 0,
        avg_order_value: 0,
        created_at: '2023-03-10T14:20:00Z',
        last_order_at: null
      },
      {
        id: 4,
        name: 'Phạm Thị D',
        email: 'phamthid@example.com',
        phone: '0978123456',
        avatar: 'https://randomuser.me/api/portraits/women/4.jpg',
        status: 'banned',
        is_vip: false,
        groups: [],
        orders_count: 5,
        orders_success_count: 3,
        total_spent: 4500000,
        avg_order_value: 900000,
        created_at: '2022-11-05T09:10:00Z',
        last_order_at: '2023-02-15T11:30:00Z'
      },
      {
        id: 5,
        name: 'Hoàng Văn E',
        email: 'hoangvane@example.com',
        phone: '0918765432',
        avatar: 'https://randomuser.me/api/portraits/men/5.jpg',
        status: 'active',
        is_vip: true,
        groups: [
          { id: 1, name: 'Khách hàng thân thiết' },
          { id: 2, name: 'Đã mua nhiều' },
          { id: 4, name: 'Đánh giá tốt' }
        ],
        orders_count: 42,
        orders_success_count: 38,
        total_spent: 125000000,
        avg_order_value: 2976190,
        created_at: '2020-06-18T16:45:00Z',
        last_order_at: '2023-05-22T10:15:00Z'
      },
      {
        id: 6,
        name: 'Vũ Thị F',
        email: 'vuthif@example.com',
        phone: '0987123456',
        avatar: 'https://randomuser.me/api/portraits/women/6.jpg',
        status: 'active',
        is_vip: false,
        groups: [
          { id: 3, name: 'Khách hàng mới' }
        ],
        orders_count: 1,
        orders_success_count: 0,
        total_spent: 0,
        avg_order_value: 0,
        created_at: '2023-05-10T13:20:00Z',
        last_order_at: '2023-05-15T09:30:00Z'
      },
      {
        id: 7,
        name: 'Đặng Văn G',
        email: 'dangvang@example.com',
        phone: '0909876543',
        avatar: 'https://randomuser.me/api/portraits/men/7.jpg',
        status: 'active',
        is_vip: false,
        groups: [
          { id: 2, name: 'Đã mua nhiều' }
        ],
        orders_count: 8,
        orders_success_count: 7,
        total_spent: 18500000,
        avg_order_value: 2312500,
        created_at: '2022-09-22T11:45:00Z',
        last_order_at: '2023-05-18T17:20:00Z'
      },
      {
        id: 8,
        name: 'Bùi Thị H',
        email: 'buithih@example.com',
        phone: '0911122334',
        avatar: 'https://randomuser.me/api/portraits/women/8.jpg',
        status: 'inactive',
        is_vip: false,
        groups: [],
        orders_count: 3,
        orders_success_count: 2,
        total_spent: 4500000,
        avg_order_value: 1500000,
        created_at: '2023-02-15T10:20:00Z',
        last_order_at: '2023-04-10T14:15:00Z'
      },
      {
        id: 9,
        name: 'Ngô Văn I',
        email: 'ngovani@example.com',
        phone: '0988777666',
        avatar: 'https://randomuser.me/api/portraits/men/9.jpg',
        status: 'active',
        is_vip: true,
        groups: [
          { id: 1, name: 'Khách hàng thân thiết' },
          { id: 4, name: 'Đánh giá tốt' }
        ],
        orders_count: 22,
        orders_success_count: 20,
        total_spent: 85000000,
        avg_order_value: 3863636,
        created_at: '2021-07-30T09:15:00Z',
        last_order_at: '2023-05-21T16:30:00Z'
      },
      {
        id: 10,
        name: 'Đỗ Thị K',
        email: 'dothik@example.com',
        phone: '0911222333',
        avatar: 'https://randomuser.me/api/portraits/women/10.jpg',
        status: 'active',
        is_vip: false,
        groups: [],
        orders_count: 5,
        orders_success_count: 4,
        total_spent: 12500000,
        avg_order_value: 2500000,
        created_at: '2023-03-05T14:25:00Z',
        last_order_at: '2023-05-15T11:20:00Z'
      }
    ]);

    // Customer groups
    const customerGroups = ref([
      { id: 1, name: 'Khách hàng thân thiết', color: 'bg-purple-100 text-purple-800' },
      { id: 2, name: 'Đã mua nhiều', color: 'bg-blue-100 text-blue-800' },
      { id: 3, name: 'Khách hàng mới', color: 'bg-green-100 text-green-800' },
      { id: 4, name: 'Đánh giá tốt', color: 'bg-yellow-100 text-yellow-800' },
      { id: 5, name: 'Chưa mua lần nào', color: 'bg-gray-100 text-gray-800' }
    ]);

    // Filters
    const filters = reactive({
      search: '',
      status: '',
      date_range: '',
      start_date: '',
      end_date: '',
      group: '',
      order_count: '',
      total_spent: ''
    });

    // Show/hide advanced filters
    const showAdvancedFilters = ref(false);

    // Selected customers for bulk actions
    const selectedCustomers = ref<number[]>([]);
    const allSelected = computed(() => {
      return selectedCustomers.value.length === filteredCustomers.value.length && filteredCustomers.value.length > 0;
    });

    // Toggle select all customers
    const toggleSelectAll = () => {
      if (allSelected.value) {
        selectedCustomers.value = [];
      } else {
        selectedCustomers.value = filteredCustomers.value.map(customer => customer.id);
      }
    };

    // Bulk actions
    const bulkAction = ref('');
    const applyBulkAction = () => {
      if (!bulkAction.value) return;
      
      switch (bulkAction.value) {
        case 'delete':
          // Show confirmation for delete
          if (selectedCustomers.value.length > 0) {
            // In a real app, you would handle multiple deletions
            const customer = customers.value.find(c => c.id === selectedCustomers.value[0]);
            if (customer) {
              confirmDeleteCustomer(customer);
            }
          }
          break;
        case 'export':
          exportCustomers();
          break;
        // Add other bulk actions here
        default:
          console.log('Action not implemented:', bulkAction.value);
      }
      
      // Reset bulk action
      bulkAction.value = '';
    };

    // Export customers
    const exportCustomers = () => {
      // In a real app, this would generate and download a CSV/Excel file
      console.log('Exporting customers:', selectedCustomers.value);
      alert(`Đã xuất dữ liệu của ${selectedCustomers.value.length} khách hàng`);
    };

    // Pagination
    const pagination = reactive({
      currentPage: 1,
      perPage: 10,
      get total() {
        return filteredCustomers.value.length;
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

    // Filtered and paginated customers
    const filteredCustomers = computed(() => {
      return customers.value.filter(customer => {
        // Search filter
        const searchTerm = filters.search.toLowerCase();
        const matchesSearch = !searchTerm || 
          customer.name.toLowerCase().includes(searchTerm) ||
          customer.email.toLowerCase().includes(searchTerm) ||
          (customer.phone && customer.phone.includes(searchTerm));
        
        // Status filter
        const matchesStatus = !filters.status || customer.status === filters.status;
        
        // Group filter
        const matchesGroup = !filters.group || 
          customer.groups.some(group => group.id.toString() === filters.group);
        
        // Order count filter
        let matchesOrderCount = true;
        if (filters.order_count) {
          if (filters.order_count === '0') {
            matchesOrderCount = customer.orders_count === 0;
          } else if (filters.order_count.includes('-')) {
            const [min, max] = filters.order_count.split('-').map(Number);
            matchesOrderCount = customer.orders_count >= min && 
              (isNaN(max) ? true : customer.orders_count <= max);
          } else if (filters.order_count.endsWith('+')) {
            const min = parseInt(filters.order_count);
            matchesOrderCount = customer.orders_count >= min;
          }
        }
        
        // Total spent filter
        let matchesTotalSpent = true;
        if (filters.total_spent) {
          if (filters.total_spent === '0') {
            matchesTotalSpent = customer.total_spent === 0;
          } else if (filters.total_spent.includes('-')) {
            const [min, max] = filters.total_spent.split('-').map(Number);
            matchesTotalSpent = customer.total_spent >= min && 
              (isNaN(max) ? true : customer.total_spent <= max);
          } else if (filters.total_spent.endsWith('+')) {
            const min = parseInt(filters.total_spent);
            matchesTotalSpent = customer.total_spent >= min;
          }
        }
        
        // Date range filter
        let matchesDateRange = true;
        if (filters.date_range) {
          const customerDate = new Date(customer.created_at);
          const today = new Date();
          today.setHours(0, 0, 0, 0);
          
          switch (filters.date_range) {
            case 'today':
              const todayStr = today.toISOString().split('T')[0];
              const customerDateStr = customerDate.toISOString().split('T')[0];
              matchesDateRange = customerDateStr === todayStr;
              break;
            case 'yesterday':
              const yesterday = new Date(today);
              yesterday.setDate(yesterday.getDate() - 1);
              const yesterdayStr = yesterday.toISOString().split('T')[0];
              const customerDateYestStr = customerDate.toISOString().split('T')[0];
              matchesDateRange = customerDateYestStr === yesterdayStr;
              break;
            case 'this_week':
              const day = customerDate.getDay();
              const diff = customerDate.getDate() - day + (day === 0 ? -6 : 1);
              const weekStart = new Date(customerDate.setDate(diff));
              weekStart.setHours(0, 0, 0, 0);
              
              const thisWeekStart = new Date();
              thisWeekStart.setDate(thisWeekStart.getDate() - thisWeekStart.getDay() + (thisWeekStart.getDay() === 0 ? -6 : 1));
              thisWeekStart.setHours(0, 0, 0, 0);
              
              matchesDateRange = customerDate >= thisWeekStart && customerDate <= new Date();
              break;
            case 'last_week':
              const lastWeekStart = new Date();
              lastWeekStart.setDate(lastWeekStart.getDate() - lastWeekStart.getDay() - 6);
              lastWeekStart.setHours(0, 0, 0, 0);
              
              const lastWeekEnd = new Date(lastWeekStart);
              lastWeekEnd.setDate(lastWeekStart.getDate() + 6);
              lastWeekEnd.setHours(23, 59, 59, 999);
              
              matchesDateRange = customerDate >= lastWeekStart && customerDate <= lastWeekEnd;
              break;
            case 'this_month':
              const thisMonthStart = new Date(today.getFullYear(), today.getMonth(), 1);
              matchesDateRange = customerDate >= thisMonthStart && customerDate <= new Date();
              break;
            case 'last_month':
              const lastMonthEnd = new Date(today.getFullYear(), today.getMonth(), 0);
              const lastMonthStart = new Date(today.getFullYear(), today.getMonth() - 1, 1);
              matchesDateRange = customerDate >= lastMonthStart && customerDate <= lastMonthEnd;
              break;
            case 'custom':
              if (filters.start_date && filters.end_date) {
                const startDate = new Date(filters.start_date);
                startDate.setHours(0, 0, 0, 0);
                
                const endDate = new Date(filters.end_date);
                endDate.setHours(23, 59, 59, 999);
                
                matchesDateRange = customerDate >= startDate && customerDate <= endDate;
              }
              break;
          }
        }
        
        return matchesSearch && matchesStatus && matchesGroup && 
               matchesOrderCount && matchesTotalSpent && matchesDateRange;
      });
    });

    const paginatedCustomers = computed(() => {
      const start = (pagination.currentPage - 1) * pagination.perPage;
      const end = start + pagination.perPage;
      return filteredCustomers.value.slice(start, end);
    });

    // Delete customer modal
    const showDeleteModal = ref(false);
    const customerToDelete = ref<any>(null);
    const deleting = ref(false);
    const deleteOption = ref('soft');

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

    // Get status class
    const getStatusClass = (status: string) => {
      const statusMap: Record<string, string> = {
        'active': 'bg-green-100 text-green-800',
        'inactive': 'bg-yellow-100 text-yellow-800',
        'banned': 'bg-red-100 text-red-800',
        'pending': 'bg-blue-100 text-blue-800'
      };
      return statusMap[status] || 'bg-gray-100 text-gray-800';
    };

    // Get status text
    const getStatusText = (status: string) => {
      const statusMap: Record<string, string> = {
        'active': 'Đang hoạt động',
        'inactive': 'Ngừng hoạt động',
        'banned': 'Đã khóa',
        'pending': 'Chờ kích hoạt'
      };
      return statusMap[status] || status;
    };

    // Get group color
    const getGroupColor = (groupId: number) => {
      const group = customerGroups.value.find(g => g.id === groupId);
      return group ? group.color : 'bg-gray-100 text-gray-800';
    };

    // Apply filters
    const applyFilters = () => {
      pagination.currentPage = 1;
      selectedCustomers.value = [];
    };

    // Reset filters
    const resetFilters = () => {
      filters.search = '';
      filters.status = '';
      filters.date_range = '';
      filters.start_date = '';
      filters.end_date = '';
      filters.group = '';
      filters.order_count = '';
      filters.total_spent = '';
      showAdvancedFilters.value = false;
      pagination.currentPage = 1;
      selectedCustomers.value = [];
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

    // Edit customer
    const editCustomer = (customer: any) => {
      router.push({ name: 'admin-customer-edit', params: { id: customer.id } });
    };

    // Confirm delete customer
    const confirmDeleteCustomer = (customer: any) => {
      customerToDelete.value = customer;
      deleteOption.value = 'soft';
      showDeleteModal.value = true;
    };

    // Delete customer
    const deleteCustomer = async () => {
      if (!customerToDelete.value) return;
      
      try {
        deleting.value = true;
        
        // Simulate API call
        await new Promise(resolve => setTimeout(resolve, 1000));
        
        // Remove customer from the list
        const index = customers.value.findIndex(c => c.id === customerToDelete.value.id);
        if (index !== -1) {
          if (deleteOption.value === 'force') {
            customers.value.splice(index, 1);
          } else {
            // Soft delete
            customers.value[index].status = 'inactive';
          }
        }
        
        // Remove from selected customers
        selectedCustomers.value = selectedCustomers.value.filter(id => id !== customerToDelete.value.id);
        
        // Close modal
        showDeleteModal.value = false;
        
      } catch (error) {
        console.error('Error deleting customer:', error);
      } finally {
        deleting.value = false;
      }
    };

    // Initialize component
    onMounted(() => {
      // In a real app, you would fetch customers from an API here
      // fetchCustomers();
    });

    return {
      // Data
      customers: paginatedCustomers,
      customerGroups,
      filters,
      showAdvancedFilters,
      selectedCustomers,
      allSelected,
      bulkAction,
      pagination,
      showDeleteModal,
      customerToDelete,
      deleting,
      deleteOption,
      
      // Methods
      formatCurrency,
      formatDate,
      getStatusClass,
      getStatusText,
      getGroupColor,
      toggleSelectAll,
      applyBulkAction,
      exportCustomers,
      applyFilters,
      resetFilters,
      goToPage,
      previousPage,
      nextPage,
      editCustomer,
      confirmDeleteCustomer,
      deleteCustomer
    };
  }
});
</script>

<style scoped>
.customer-list {
  min-height: calc(100vh - 64px);
}
</style>
