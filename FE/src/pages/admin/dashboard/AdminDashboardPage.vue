<template>
  <div class="p-6 min-h-screen bg-[#f8f9fa] dark:bg-brand-dark-200 transition-colors duration-300">
    
    <!-- HEADER -->
    <div class="flex flex-col md:flex-row justify-between items-start md:items-center mb-6 gap-4 relative z-20">
      <div>
        <!-- Tiêu đề: Nâu đậm (Light) -> Kem sáng (Dark) để nổi bật trên nền tối -->
        <h1 class="text-2xl font-bold text-brand-mocha dark:text-brand-cream tracking-tight">Dashboard Tổng quan</h1>
        <p class="text-sm text-gray-500 dark:text-gray-300 font-medium">Chào mừng trở lại, Admin!</p>
      </div>
      
      <!-- Group Nút Hành Động -->
      <div class="flex gap-2">
        <ButtonCustom color="coffee" @click="$router.push('/admin/products/new')">
           <span class="mr-1">+</span> Sản phẩm
        </ButtonCustom>
        <ButtonCustom color="olive" @click="$router.push('/admin/orders')">
           Xem đơn hàng
        </ButtonCustom>
      </div>
    </div>

    <!-- FILTER: Bộ lọc thống kê -->
    <div class="mb-6 relative z-30">
      <AdminDashFil @filter="handleFilter" @export="handleExport" />
    </div>

    <!-- LOADING STATE -->
    <div v-if="isLoading" class="p-12 text-center relative z-0">
       <div class="animate-spin inline-block w-8 h-8 border-4 border-current border-t-transparent text-brand-olive rounded-full"></div>
       <p class="mt-2 text-brand-coffee dark:text-brand-sage">Đang tải dữ liệu thống kê...</p>
    </div>

    <!-- MAIN CONTENT -->
    <div v-else class="relative z-0">
      
      <!-- 1. STATS CARDS: Thẻ thống kê tổng quan -->
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 mb-6">
        
        <!-- Doanh thu (Viền trái Xanh lá - Olive) -->
        <CardCustom class="!p-0 border-l-4 border-l-[#adc178]">
          <div class="p-5 flex items-center justify-between">
            <div>
               <p class="text-xs font-bold text-gray-500 dark:text-gray-300 uppercase tracking-wide">Tổng doanh thu</p>
               <p class="text-2xl font-bold text-brand-mocha dark:text-brand-cream mt-1">{{ formatCurrency(stats.totalRevenue) }}</p>
               <div :class="['text-xs font-medium mt-1 flex items-center', stats.revenueChange >= 0 ? 'text-green-600 dark:text-green-400' : 'text-red-500 dark:text-red-400']">
                  <span>{{ stats.revenueChange >= 0 ? '↑' : '↓' }} {{ Math.abs(stats.revenueChange) }}%</span>
                  <span class="text-gray-400 dark:text-gray-400 font-normal ml-1">tháng này</span>
               </div>
            </div>
            <div class="p-3 rounded-full bg-green-100 text-green-600 dark:bg-green-900/30 dark:text-green-400">
               <CurrencyDollarIcon class="w-6 h-6" />
            </div>
          </div>
        </CardCustom>

        <!-- Đơn hàng (Viền trái Nâu - Coffee) -->
        <CardCustom class="!p-0 border-l-4 border-l-[#a98467]">
          <div class="p-5 flex items-center justify-between">
            <div>
               <p class="text-xs font-bold text-gray-500 dark:text-gray-300 uppercase tracking-wide">Đơn hàng</p>
               <p class="text-2xl font-bold text-brand-mocha dark:text-brand-cream mt-1">{{ stats.totalOrders }}</p>
               <div :class="['text-xs font-medium mt-1 flex items-center', stats.ordersChange >= 0 ? 'text-green-600 dark:text-green-400' : 'text-red-500 dark:text-red-400']">
                  <span>{{ stats.ordersChange >= 0 ? '↑' : '↓' }} {{ Math.abs(stats.ordersChange) }}%</span>
               </div>
            </div>
            <div class="p-3 rounded-full bg-orange-100 text-orange-600 dark:bg-orange-900/30 dark:text-orange-400">
               <ShoppingBagIcon class="w-6 h-6" />
            </div>
          </div>
        </CardCustom>

        <!-- Khách hàng (Viền trái Nâu đậm - Mocha) -->
        <CardCustom class="!p-0 border-l-4 border-l-[#6c584c]">
          <div class="p-5 flex items-center justify-between">
            <div>
               <p class="text-xs font-bold text-gray-500 dark:text-gray-300 uppercase tracking-wide">Khách hàng</p>
               <p class="text-2xl font-bold text-brand-mocha dark:text-brand-cream mt-1">{{ stats.totalCustomers }}</p>
               <div :class="['text-xs font-medium mt-1 flex items-center', stats.customersChange >= 0 ? 'text-green-600 dark:text-green-400' : 'text-red-500 dark:text-red-400']">
                  <span>{{ stats.customersChange >= 0 ? '↑' : '↓' }} {{ Math.abs(stats.customersChange) }}%</span>
               </div>
            </div>
            <div class="p-3 rounded-full bg-blue-100 text-blue-600 dark:bg-blue-900/30 dark:text-blue-400">
               <UsersIcon class="w-6 h-6" />
            </div>
          </div>
        </CardCustom>

        <!-- Sản phẩm (Viền trái Xanh nhạt - Sage) -->
        <CardCustom class="!p-0 border-l-4 border-l-[#dde5b6]">
          <div class="p-5 flex items-center justify-between">
            <div>
               <p class="text-xs font-bold text-gray-500 dark:text-gray-300 uppercase tracking-wide">Sản phẩm</p>
               <p class="text-2xl font-bold text-brand-mocha dark:text-brand-cream mt-1">{{ stats.totalProducts }}</p>
               <div :class="['text-xs font-medium mt-1 flex items-center', stats.productsChange >= 0 ? 'text-green-600 dark:text-green-400' : 'text-red-500 dark:text-red-400']">
                  <span>{{ stats.productsChange >= 0 ? '↑' : '↓' }} {{ Math.abs(stats.productsChange) }}%</span>
               </div>
            </div>
            <div class="p-3 rounded-full bg-purple-100 text-purple-600 dark:bg-purple-900/30 dark:text-purple-400">
               <CubeIcon class="w-6 h-6" />
            </div>
          </div>
        </CardCustom>
      </div>

      <!-- 2. CHARTS: Biểu đồ doanh thu -->
      <div class="mb-6 relative z-0">
         <AdminRevenueChart />
      </div>

      <!-- 3. TABLES: Đơn hàng mới & Sắp hết hàng -->
      <div class="grid grid-cols-1 lg:grid-cols-2 gap-6 relative z-0">
          
          <!-- BẢNG ĐƠN HÀNG GẦN ĐÂY -->
          <CardCustom>
              <div class="p-4 border-b border-[#e6dfc0] dark:border-brand-dark-50 flex justify-between items-center bg-gray-50/50 dark:bg-brand-dark-300/30">
                 <h3 class="font-bold text-lg text-brand-mocha dark:text-brand-cream">Đơn hàng mới</h3>
                 <ButtonCustom color="cream-soft" size="small" @click="$router.push('/admin/orders')">Xem tất cả</ButtonCustom>
              </div>
              
              <table class="w-full text-left">
                <thead class="bg-[#f7f9ef] dark:bg-brand-dark-300 text-xs uppercase text-gray-500 dark:text-gray-300 font-bold tracking-wider">
                  <tr>
                    <th class="px-4 py-3">Mã đơn</th>
                    <th class="px-4 py-3">Khách</th>
                    <th class="px-4 py-3 text-right">Tổng tiền</th>
                    <th class="px-4 py-3 text-center">TT</th>
                  </tr>
                </thead>
                <tbody class="divide-y divide-[#f0ead2] dark:divide-brand-dark-50">
                  <tr v-for="order in recentOrders" :key="order.id" class="hover:bg-[#f0ead2]/20 dark:hover:bg-white/5 transition-colors">
                    <td class="px-4 py-3 font-mono text-blue-600 dark:text-blue-400 text-sm font-medium">{{ order.orderCode }}</td>
                    <td class="px-4 py-3 text-sm text-brand-mocha dark:text-brand-cream font-medium">{{ order.customerName }}</td>
                    <td class="px-4 py-3 text-sm text-right font-bold text-brand-mocha dark:text-brand-cream">{{ formatCurrency(order.totalAmount) }}</td>
                    <td class="px-4 py-3 text-center">
                      <!-- Badge trạng thái nhỏ gọn -->
                      <span :class="['px-2 py-0.5 rounded text-[10px] font-bold uppercase border', getStatusClass(order.orderStatus)]">
                        {{ order.orderStatus }}
                      </span>
                    </td>
                  </tr>
                  <tr v-if="!recentOrders || recentOrders.length === 0">
                    <td colspan="4" class="p-8 text-center text-sm text-gray-500 dark:text-gray-400 italic">Không có đơn hàng mới</td>
                  </tr>
                </tbody>
              </table>
          </CardCustom>

          <!-- BẢNG SẮP HẾT HÀNG (< 100) -->
          <CardCustom>
              <div class="p-4 border-b border-[#e6dfc0] dark:border-brand-dark-50 flex justify-between items-center bg-red-50/30 dark:bg-red-900/10">
                 <div class="flex items-center gap-2">
                    <div class="w-2 h-2 rounded-full bg-red-500 animate-pulse"></div>
                    <h3 class="font-bold text-lg text-red-700 dark:text-red-400">Sắp hết hàng</h3>
                 </div>
                 <ButtonCustom color="cream-soft" size="small" @click="$router.push('/admin/products')">Nhập kho</ButtonCustom>
              </div>
              
              <table class="w-full text-left">
                <thead class="bg-red-50 dark:bg-red-900/20 text-xs uppercase text-red-600 dark:text-red-300 font-bold tracking-wider">
                  <tr>
                    <th class="px-4 py-3">Sản phẩm</th>
                    <th class="px-4 py-3 text-center">Tồn kho</th>
                    <th class="px-4 py-3 text-center">Giá</th>
                  </tr>
                </thead>
                <tbody class="divide-y divide-[#f0ead2] dark:divide-brand-dark-50">
                  <tr v-for="prod in lowStockProducts" :key="prod.id" class="hover:bg-red-50/30 dark:hover:bg-red-900/10 transition-colors">
                    <td class="px-4 py-3 text-sm font-medium text-brand-mocha dark:text-brand-cream max-w-[200px] truncate" :title="prod.name">
                      {{ prod.name }}
                    </td>
                    <td class="px-4 py-3 text-center">
                      <!-- Badge đỏ đậm cảnh báo -->
                      <span class="px-2.5 py-0.5 bg-red-100 dark:bg-red-900/40 text-red-700 dark:text-red-300 rounded-full text-xs font-bold border border-red-200 dark:border-red-800">
                        {{ prod.stockQuantity }}
                      </span>
                    </td>
                    <td class="px-4 py-3 text-sm text-center text-gray-500 dark:text-gray-400">
                      {{ formatCurrency(prod.price) }}
                    </td>
                  </tr>
                  <tr v-if="!lowStockProducts || lowStockProducts.length === 0">
                    <td colspan="3" class="p-8 text-center text-sm text-green-600 dark:text-green-400 italic font-medium">
                      Kho hàng ổn định 
                    </td>
                  </tr>
                </tbody>
              </table>
          </CardCustom>
      </div>

    </div>
    
    <!-- TOAST THÔNG BÁO -->
    <ToastCustom :show="toast.show" :message="toast.message" :type="toast.type" />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { storeToRefs } from 'pinia';
import { useDashboardStore } from '@/stores/dashboard.store';
import { CurrencyDollarIcon, ShoppingBagIcon, UsersIcon, CubeIcon } from '@heroicons/vue/24/outline';
import AdminDashFil from './AdminDashFil.vue';
import AdminRevenueChart from './AdminRevenueChart.vue';
import ButtonCustom from '@/components/custom/Button/ButtonDefault.vue';
import CardCustom from '@/components/custom/Card/CardCustom.vue';
import ToastCustom from '@/components/custom/Toast/ToastCustom.vue'; 

const dashboardStore = useDashboardStore();
// Đảm bảo lấy đúng state từ store
const { stats, recentOrders, lowStockProducts, isLoading } = storeToRefs(dashboardStore);

// Toast Logic
const toast = ref({ show: false, message: '', type: 'success' as 'success' | 'error' });
// (Có thể mở rộng để dùng toast global sau này)

onMounted(() => { 
  dashboardStore.fetchDashboardData(); 
});

const handleFilter = (filters: any) => {
  dashboardStore.fetchDashboardData(filters);
};

const handleExport = (filters: any) => {
  console.log('Export requested:', filters);
  // Logic export excel sẽ thêm sau
};

// Helper Format tiền
const formatCurrency = (value: number) => {
  if (value === undefined || value === null) return '0 ₫';
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value);
};

// Helper class trạng thái đơn hàng
const getStatusClass = (status: string) => {
  switch(status) {
    case 'PENDING': return 'bg-yellow-50 dark:bg-yellow-900/20 text-yellow-700 dark:text-yellow-400 border-yellow-200 dark:border-yellow-800';
    case 'CONFIRMED': return 'bg-blue-50 dark:bg-blue-900/20 text-blue-700 dark:text-blue-400 border-blue-200 dark:border-blue-800';
    case 'SHIPPED': return 'bg-purple-50 dark:bg-purple-900/20 text-purple-700 dark:text-purple-400 border-purple-200 dark:border-purple-800';
    case 'DELIVERED': return 'bg-green-50 dark:bg-green-900/20 text-green-700 dark:text-green-400 border-green-200 dark:border-green-800';
    case 'CANCELLED': return 'bg-red-50 dark:bg-red-900/20 text-red-700 dark:text-red-400 border-red-200 dark:border-red-800';
    default: return 'bg-gray-50 dark:bg-gray-700 text-gray-700 dark:text-gray-300';
  }
};
</script>