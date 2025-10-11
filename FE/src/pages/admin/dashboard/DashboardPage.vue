<template>
  <div class="space-y-6">
    <!-- Page Header -->
    <div class="flex flex-col md:flex-row md:items-center md:justify-between">
      <div>
        <h1 class="text-2xl font-bold text-gray-900">Dashboard</h1>
        <p class="mt-1 text-sm text-gray-500">Overview of your store's performance</p>
      </div>
      <div class="mt-4 flex space-x-3 md:mt-0">
        <button type="button" class="inline-flex items-center px-4 py-2 border border-gray-300 shadow-sm text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
          <ArrowDownTrayIcon class="-ml-1 mr-2 h-5 w-5 text-gray-500" />
          Export
        </button>
        <button type="button" class="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
          <PlusIcon class="-ml-1 mr-2 h-5 w-5" />
          Add New
        </button>
      </div>
    </div>

    <!-- Stats Grid -->
    <div class="grid grid-cols-1 gap-5 sm:grid-cols-2 lg:grid-cols-4">
      <div v-for="stat in stats" :key="stat.name" class="bg-white overflow-hidden shadow rounded-lg">
        <div class="p-5">
          <div class="flex items-center">
            <div class="flex-shrink-0">
              <component :is="stat.icon" class="h-6 w-6 text-gray-400" aria-hidden="true" />
            </div>
            <div class="ml-5 w-0 flex-1">
              <dl>
                <dt class="text-sm font-medium text-gray-500 truncate">
                  {{ stat.name }}
                </dt>
                <dd class="flex items-baseline">
                  <div class="text-2xl font-semibold text-gray-900">
                    {{ stat.value }}
                  </div>
                  <div :class="[stat.changeType === 'increase' ? 'text-green-600' : 'text-red-600', 'ml-2 flex items-baseline text-sm font-semibold']">
                    {{ stat.change }}
                  </div>
                </dd>
              </dl>
            </div>
          </div>
        </div>
        <div class="bg-gray-50 px-5 py-3">
          <div class="text-sm">
            <a :href="stat.href" class="font-medium text-indigo-700 hover:text-indigo-900">
              View all
            </a>
          </div>
        </div>
      </div>
    </div>

    <!-- Quick Actions -->
    <div class="bg-white shadow rounded-lg overflow-hidden">
      <div class="p-6">
        <h2 class="text-lg font-medium text-gray-900">Quick Actions</h2>
        <div class="mt-6 grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-4">
          <button
            v-for="(action, actionIdx) in quickActions"
            :key="actionIdx"
            @click="action.action"
            class="relative flex items-center space-x-3 rounded-lg border border-gray-300 bg-white px-6 py-5 shadow-sm focus-within:ring-2 focus-within:ring-indigo-500 focus-within:ring-offset-2 hover:border-gray-400"
          >
            <div class="flex-shrink-0">
              <component :is="action.icon" class="h-6 w-6 text-gray-400" aria-hidden="true" />
            </div>
            <div class="min-w-0 flex-1">
              <div class="focus:outline-none">
                <span class="absolute inset-0" aria-hidden="true" />
                <p class="text-sm font-medium text-gray-900">{{ action.title }}</p>
                <p class="truncate text-sm text-gray-500">{{ action.description }}</p>
              </div>
            </div>
          </button>
        </div>
      </div>
    </div>

    <!-- Recent Orders -->
    <div class="bg-white shadow rounded-lg overflow-hidden">
      <div class="px-6 py-5 border-b border-gray-200 sm:flex sm:items-center sm:justify-between">
        <h3 class="text-lg font-medium leading-6 text-gray-900">Recent Orders</h3>
        <div class="mt-3 sm:mt-0 sm:ml-4">
          <button
            type="button"
            class="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
          >
            View all
          </button>
        </div>
      </div>
      <div class="overflow-x-auto">
        <table class="min-w-full divide-y divide-gray-200">
          <thead class="bg-gray-50">
            <tr>
              <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Order</th>
              <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Customer</th>
              <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Date</th>
              <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Items</th>
              <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Amount</th>
              <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
              <th scope="col" class="relative px-6 py-3">
                <span class="sr-only">Actions</span>
              </th>
            </tr>
          </thead>
          <tbody class="bg-white divide-y divide-gray-200">
            <tr v-for="order in recentOrders" :key="order.id" class="hover:bg-gray-50">
              <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                {{ order.id }}
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                {{ order.customer }}
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                {{ order.date }}
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                {{ order.items }} items
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                {{ order.amount }}
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full" :class="getStatusBadgeClass(order.status)">
                  {{ getStatusText(order.status) }}
                </span>
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                <a href="#" class="text-indigo-600 hover:text-indigo-900">View</a>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Low Stock Products -->
    <div class="bg-white shadow rounded-lg overflow-hidden">
      <div class="px-6 py-5 border-b border-gray-200 sm:flex sm:items-center sm:justify-between">
        <h3 class="text-lg font-medium leading-6 text-gray-900">Low Stock Products</h3>
        <div class="mt-3 sm:mt-0 sm:ml-4">
          <button
            type="button"
            class="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
          >
            View all
          </button>
        </div>
      </div>
      <div class="overflow-x-auto">
        <table class="min-w-full divide-y divide-gray-200">
          <thead class="bg-gray-50">
            <tr>
              <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Product</th>
              <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">SKU</th>
              <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Category</th>
              <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Price</th>
              <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Stock</th>
              <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
              <th scope="col" class="relative px-6 py-3">
                <span class="sr-only">Actions</span>
              </th>
            </tr>
          </thead>
          <tbody class="bg-white divide-y divide-gray-200">
            <tr v-for="product in lowStockProducts" :key="product.id" class="hover:bg-gray-50">
              <td class="px-6 py-4 whitespace-nowrap">
                <div class="flex items-center">
                  <div class="flex-shrink-0 h-10 w-10 bg-gray-200 rounded-md flex items-center justify-center">
                    <CubeIcon class="h-6 w-6 text-gray-500" />
                  </div>
                  <div class="ml-4">
                    <div class="text-sm font-medium text-gray-900">{{ product.name }}</div>
                  </div>
                </div>
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                {{ product.sku }}
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                {{ product.category }}
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                {{ product.price }}
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                {{ product.stock }} in stock
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full" :class="getStockStatusClass(product.stock)">
                  {{ getStockStatusText(product.stock) }}
                </span>
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                <button class="text-indigo-600 hover:text-indigo-900 mr-4">Edit</button>
                <button class="text-indigo-600 hover:text-indigo-900">Restock</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
    
      <StatCard 
        title="Đơn hàng mới" 
        :value="stats.newOrders" 
        icon="DocumentTextIcon"
        trend="up"
        :trend-value="8"
      />
      <StatCard 
        title="Doanh thu tháng" 
        :value="`${stats.monthlyRevenue} đ`" 
        icon="CurrencyDollarIcon"
        trend="up"
        :trend-value="15"
      />
      <StatCard 
        title="Khách hàng mới" 
        :value="stats.newCustomers" 
        icon="UserGroupIcon"
        trend="up"
        :trend-value="5"
      />
    </div>

    <!-- Recent Orders -->
    <div class="bg-white rounded-lg shadow p-6 mb-8">
      <div class="flex justify-between items-center mb-4">
        <h3 class="text-lg font-medium">Đơn hàng gần đây</h3>
        <router-link to="/admin/orders" class="text-blue-600 hover:text-blue-800 text-sm">
          Xem tất cả
        </router-link>
      </div>
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
                Sản phẩm
              </th>
              <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Tổng tiền
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
            <tr v-for="order in recentOrders" :key="order.id">
              <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                {{ order.id }}
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                {{ order.customer }}
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                {{ order.productCount }} sản phẩm
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                {{ order.total }}
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <span :class="getStatusBadgeClass(order.status)" class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full">
                  {{ getStatusText(order.status) }}
                </span>
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                <button class="text-blue-600 hover:text-blue-900 mr-3">Xem</button>
                <button class="text-green-600 hover:text-green-900">Duyệt</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Low Stock Products -->
    <div class="bg-white rounded-lg shadow p-6">
      <div class="flex justify-between items-center mb-4">
        <h3 class="text-lg font-medium">Sản phẩm sắp hết hàng</h3>
        <router-link to="/admin/inventory" class="text-blue-600 hover:text-blue-800 text-sm">
          Quản lý kho
        </router-link>
      </div>
      <div class="overflow-x-auto">
        <table class="min-w-full divide-y divide-gray-200">
          <thead class="bg-gray-50">
            <tr>
              <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Sản phẩm
              </th>
              <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Mã SP
              </th>
              <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Tồn kho
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
            <tr v-for="product in lowStockProducts" :key="product.id">
              <td class="px-6 py-4 whitespace-nowrap">
                <div class="flex items-center">
                  <div class="flex-shrink-0 h-10 w-10">
                    <img class="h-10 w-10 rounded-full" :src="product.image" alt="">
                  </div>
                  <div class="ml-4">
                    <div class="text-sm font-medium text-gray-900">{{ product.name }}</div>
                    <div class="text-sm text-gray-500">{{ product.category }}</div>
                  </div>
                </div>
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                {{ product.sku }}
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                {{ product.stock }} {{ product.unit }}
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full" :class="getStockStatusClass(product.stock)">
                  {{ getStockStatusText(product.stock) }}
                </span>
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                <button class="text-blue-600 hover:text-blue-900">Nhập hàng</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { 
  ArrowDownTrayIcon,
  PlusIcon,
  ArrowUpIcon,
  ArrowDownIcon,
  ClockIcon,
  CheckCircleIcon,
  XCircleIcon,
  ExclamationCircleIcon,
  ShoppingBagIcon,
  CurrencyDollarIcon,
  UsersIcon,
  CubeIcon,
  DocumentTextIcon
} from '@heroicons/vue/24/outline'

const router = useRouter()

// Quick actions
const quickActions = [
  { 
    title: 'Add Product', 
    description: 'Create a new product', 
    icon: CubeIcon,
    action: () => router.push('/admin/products/new')
  },
  { 
    title: 'Process Orders', 
    description: 'Manage pending orders', 
    icon: DocumentTextIcon,
    action: () => router.push('/admin/orders?status=pending')
  },
  { 
    title: 'View Inventory', 
    description: 'Check stock levels', 
    icon: ShoppingBagIcon,
    action: () => router.push('/admin/inventory')
  },
  { 
    title: 'Generate Report', 
    description: 'Download sales reports', 
    icon: DocumentTextIcon,
    action: () => console.log('Generate Report')
  }
]

// Mock data - replace with actual API calls
const stats = ref([
  { 
    name: 'Total Products',
    value: '1,234',
    change: '+12%',
    changeType: 'increase',
    icon: CubeIcon,
    href: '/admin/products'
  },
  { 
    name: 'Total Orders',
    value: '89',
    change: '+5%',
    changeType: 'increase',
    icon: ShoppingBagIcon,
    href: '/admin/orders'
  },
  { 
    name: 'Revenue',
    value: '$12,345',
    change: '+8.2%',
    changeType: 'increase',
    icon: CurrencyDollarIcon,
    href: '/admin/reports/sales'
  },
  { 
    name: 'New Customers',
    value: '34',
    change: '-2.1%',
    changeType: 'decrease',
    icon: UsersIcon,
    href: '/admin/customers'
  }
])

const recentOrders = ref([
  { 
    id: '#ORD-001', 
    customer: 'Nguyễn Văn A', 
    date: '2023-06-15', 
    amount: '1,200,000', 
    status: 'completed',
    items: 3
  },
  { 
    id: '#ORD-002', 
    customer: 'Trần Thị B', 
    date: '2023-06-14', 
    amount: '850,000', 
    status: 'processing',
    items: 2
  },
  { 
    id: '#ORD-003', 
    customer: 'Lê Văn C', 
    date: '2023-06-14', 
    amount: '2,300,000', 
    status: 'pending',
    items: 5
  },
  { 
    id: '#ORD-004', 
    customer: 'Phạm Thị D', 
    date: '2023-06-13', 
    amount: '450,000', 
    status: 'completed',
    items: 1
  },
  { 
    id: '#ORD-005', 
    customer: 'Hoàng Văn E', 
    date: '2023-06-13', 
    amount: '1,750,000', 
    status: 'cancelled',
    items: 4
  },
])

const lowStockProducts = ref([
  { 
    id: 1, 
    name: 'Áo thun nam cổ tròn', 
    sku: 'ATN-001', 
    stock: 2, 
    status: 'critical',
    price: '250,000',
    category: 'Áo thun'
  },
  { 
    id: 2, 
    name: 'Quần jean nữ ống loe', 
    sku: 'QJN-045', 
    stock: 5, 
    status: 'warning',
    price: '450,000',
    category: 'Quần'
  },
  { 
    id: 3, 
    name: 'Giày thể thao đế đôi', 
    sku: 'GTD-112', 
    stock: 1, 
    status: 'critical',
    price: '1,200,000',
    category: 'Giày dép'
  },
  { 
    id: 4, 
    name: 'Túi xách nữ thời trang', 
    sku: 'TXN-078', 
    stock: 3, 
    status: 'warning',
    price: '350,000',
    category: 'Phụ kiện'
  },
  { 
    id: 5, 
    name: 'Ví da nam cao cấp', 
    sku: 'VDN-056', 
    stock: 4, 
    status: 'warning',
    price: '280,000',
    category: 'Phụ kiện'
  },
])

const getStatusBadgeClass = (status) => {
  const statusClasses = {
    completed: 'bg-green-50 text-green-700 ring-green-600/20',
    processing: 'bg-blue-50 text-blue-700 ring-blue-600/20',
    pending: 'bg-yellow-50 text-yellow-700 ring-yellow-600/20',
    cancelled: 'bg-red-50 text-red-700 ring-red-600/10',
    critical: 'bg-red-50 text-red-700 ring-red-600/10',
    warning: 'bg-yellow-50 text-yellow-700 ring-yellow-600/20'
  }
  return statusClasses[status] || 'bg-gray-50 text-gray-700 ring-gray-600/20'
}

const getStockStatusClass = (stock) => {
  if (stock <= 2) return 'bg-red-50 text-red-700 ring-1 ring-red-600/10'
  if (stock <= 5) return 'bg-yellow-50 text-yellow-700 ring-1 ring-yellow-600/20'
  return 'bg-gray-50 text-gray-700 ring-1 ring-gray-600/20'
}

const getStockStatusText = (stock) => {
  if (stock <= 2) return 'Nguy cấp'
  if (stock <= 5) return 'Cảnh báo'
  return 'Bình thường'
}

const getStatusText = (status) => {
  const statusText = {
    completed: 'Hoàn thành',
    processing: 'Đang xử lý',
    pending: 'Chờ xử lý',
    cancelled: 'Đã hủy',
    in_stock: 'Còn hàng',
    low: 'Sắp hết',
    critical: 'Nguy cấp',
    warning: 'Cảnh báo',
  }
  return statusText[status] || status
}
</script>
