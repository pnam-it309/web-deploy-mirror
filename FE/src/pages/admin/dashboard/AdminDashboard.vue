<template>
  <div class="admin-dashboard">
    <div class="bg-white shadow">
      <div class="max-w-7xl mx-auto py-6 px-4 sm:px-6 lg:px-8">
        <h1 class="text-3xl font-bold text-gray-900">Tổng quan</h1>
      </div>
    </div>

    <div class="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
      <!-- Stats Cards -->
      <div class="grid grid-cols-1 gap-5 mt-6 sm:grid-cols-2 lg:grid-cols-4">
        <!-- Total Sales -->
        <div class="bg-white overflow-hidden shadow rounded-lg">
          <div class="px-4 py-5 sm:p-6">
            <div class="flex items-center">
              <div class="flex-shrink-0 bg-indigo-500 rounded-md p-3">
                <svg class="h-6 w-6 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
              </div>
              <div class="ml-5 w-0 flex-1">
                <dl>
                  <dt class="text-sm font-medium text-gray-500 truncate">
                    Tổng doanh thu
                  </dt>
                  <dd class="flex items-baseline">
                    <div class="text-2xl font-semibold text-gray-900">
                      {{ formatCurrency(stats.totalSales) }}
                    </div>
                  </dd>
                </dl>
              </div>
            </div>
          </div>
        </div>

        <!-- Total Orders -->
        <div class="bg-white overflow-hidden shadow rounded-lg">
          <div class="px-4 py-5 sm:p-6">
            <div class="flex items-center">
              <div class="flex-shrink-0 bg-green-500 rounded-md p-3">
                <svg class="h-6 w-6 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
                </svg>
              </div>
              <div class="ml-5 w-0 flex-1">
                <dl>
                  <dt class="text-sm font-medium text-gray-500 truncate">
                    Tổng đơn hàng
                  </dt>
                  <dd class="flex items-baseline">
                    <div class="text-2xl font-semibold text-gray-900">
                      {{ stats.totalOrders }}
                    </div>
                  </dd>
                </dl>
              </div>
            </div>
          </div>
        </div>

        <!-- Total Products -->
        <div class="bg-white overflow-hidden shadow rounded-lg">
          <div class="px-4 py-5 sm:p-6">
            <div class="flex items-center">
              <div class="flex-shrink-0 bg-yellow-500 rounded-md p-3">
                <svg class="h-6 w-6 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4" />
                </svg>
              </div>
              <div class="ml-5 w-0 flex-1">
                <dl>
                  <dt class="text-sm font-medium text-gray-500 truncate">
                    Tổng sản phẩm
                  </dt>
                  <dd class="flex items-baseline">
                    <div class="text-2xl font-semibold text-gray-900">
                      {{ stats.totalProducts }}
                    </div>
                  </dd>
                </dl>
              </div>
            </div>
          </div>
        </div>

        <!-- Total Customers -->
        <div class="bg-white overflow-hidden shadow rounded-lg">
          <div class="px-4 py-5 sm:p-6">
            <div class="flex items-center">
              <div class="flex-shrink-0 bg-red-500 rounded-md p-3">
                <svg class="h-6 w-6 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z" />
                </svg>
              </div>
              <div class="ml-5 w-0 flex-1">
                <dl>
                  <dt class="text-sm font-medium text-gray-500 truncate">
                    Tổng khách hàng
                  </dt>
                  <dd class="flex items-baseline">
                    <div class="text-2xl font-semibold text-gray-900">
                      {{ stats.totalCustomers }}
                    </div>
                  </dd>
                </dl>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Recent Orders & Stats -->
      <div class="mt-8">
        <div class="grid grid-cols-1 gap-5 lg:grid-cols-2">
          <!-- Recent Orders -->
          <div class="bg-white shadow overflow-hidden sm:rounded-lg">
            <div class="px-4 py-5 sm:px-6 border-b border-gray-200">
              <h3 class="text-lg leading-6 font-medium text-gray-900">
                Đơn hàng gần đây
              </h3>
            </div>
            <div class="bg-white overflow-hidden">
              <table class="min-w-full divide-y divide-gray-200">
                <thead class="bg-gray-50">
                  <tr>
                    <th scope="col"
                      class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Mã đơn
                    </th>
                    <th scope="col"
                      class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Khách hàng
                    </th>
                    <th scope="col"
                      class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Trạng thái
                    </th>
                    <th scope="col"
                      class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Tổng tiền
                    </th>
                  </tr>
                </thead>
                <tbody class="bg-white divide-y divide-gray-200">
                  <tr v-for="order in recentOrders" :key="order.id" class="hover:bg-gray-50 cursor-pointer"
                    @click="viewOrder(order.id)">
                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                      #{{ order.id }}
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                      {{ order.customer }}
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap">
                      <span :class="getStatusBadgeClass(order.status)"
                        class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full">
                        {{ order.status }}
                      </span>
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                      {{ formatCurrency(order.total) }}
                    </td>
                  </tr>
                  <tr v-if="recentOrders.length === 0">
                    <td colspan="4" class="px-6 py-4 text-center text-sm text-gray-500">
                      Không có đơn hàng nào gần đây
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>

          <!-- Sales Chart -->
          <div class="bg-white shadow overflow-hidden sm:rounded-lg">
            <div class="px-4 py-5 sm:px-6 border-b border-gray-200">
              <h3 class="text-lg leading-6 font-medium text-gray-900">
                Thống kê doanh thu
              </h3>
            </div>
            <div class="p-6">
              <div class="h-64 flex items-center justify-center">
                <p class="text-gray-500">Biểu đồ doanh thu sẽ được hiển thị tại đây</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';

export default defineComponent({
  name: 'AdminDashboard',
  setup() {
    const router = useRouter();

    // Sample data - replace with actual API calls
    const stats = ref({
      totalSales: 0,
      totalOrders: 0,
      totalProducts: 0,
      totalCustomers: 0
    });

    const recentOrders = ref([
      // Sample data - replace with actual data
      // { id: '1001', customer: 'Nguyễn Văn A', status: 'Đã giao hàng', total: 1250000 },
      // { id: '1002', customer: 'Trần Thị B', status: 'Đang giao hàng', total: 750000 },
      // { id: '1003', customer: 'Lê Văn C', status: 'Chờ xác nhận', total: 2300000 },
    ]);

    const formatCurrency = (value: number) => {
      return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
      }).format(value);
    };

    const getStatusBadgeClass = (status: string) => {
      const statusMap: Record<string, string> = {
        'Đã giao hàng': 'bg-green-100 text-green-800',
        'Đang giao hàng': 'bg-blue-100 text-blue-800',
        'Chờ xác nhận': 'bg-yellow-100 text-yellow-800',
        'Đã hủy': 'bg-red-100 text-red-800'
      };
      return statusMap[status] || 'bg-gray-100 text-gray-800';
    };

    const viewOrder = (orderId: string) => {
      router.push({
        name: 'admin-order-detail',
        params: { id: orderId }
      });
    };

    // Simulate loading data
    onMounted(async () => {
      // Replace with actual API calls
      await new Promise(resolve => setTimeout(resolve, 500));

      stats.value = {
        totalSales: 12500000,
        totalOrders: 42,
        totalProducts: 156,
        totalCustomers: 89
      };
    });

    return {
      stats,
      recentOrders,
      formatCurrency,
      getStatusBadgeClass,
      viewOrder
    };
  }
});
</script>

<style scoped>
.admin-dashboard {
  min-height: calc(100vh - 64px);
}
</style>
