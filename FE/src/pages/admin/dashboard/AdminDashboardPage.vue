<template>
  <div class="space-y-6">
    <!-- Page Header -->
    <div class="flex flex-col md:flex-row md:items-center md:justify-between">
      <div>
        <h1 class="text-2xl font-bold text-gray-900">Dashboard</h1>
        <p class="mt-1 text-sm text-gray-500">Overview of your store's performance</p>
      </div>
      <div class="mt-4 flex space-x-3 md:mt-0">
        <ButtonDefault
          @click="openImportModal"
          :label="'Import Products'"
          :customClasses="'bg-blue-600 hover:bg-blue-700 text-white'"
        >
          <svg class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M15 13l-3-3m0 0l-3 3m3-3v12" />
          </svg>
        </ButtonDefault>
        <ButtonDefault
          @click="downloadProductTemplate"
          :label="'Download Template'"
          :customClasses="'bg-indigo-600 hover:bg-indigo-700 text-white'"
        >
          <svg class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 10v6m0 0l4-4m-4 4l-4-4m8 2a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
        </ButtonDefault>
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
  <!-- Import Products Modal -->
  <div v-if="showImportModal" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50" @click="closeImportModal">
    <div class="relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white" @click.stop>
      <div class="mt-3">
        <h3 class="text-lg font-medium text-gray-900 mb-4">Import Products</h3>

        <div class="mb-4">
          <label class="block text-sm font-medium text-gray-700 mb-2">
            Upload CSV/Excel File
          </label>
          <input
            ref="fileInput"
            type="file"
            accept=".csv,.xlsx,.xls"
            @change="handleFileSelect"
            class="block w-full text-sm text-gray-500 file:mr-4 file:py-2 file:px-4 file:rounded-full file:border-0 file:text-sm file:font-semibold file:bg-indigo-50 file:text-indigo-700 hover:file:bg-indigo-100"
          />
        </div>

        <div v-if="importFile" class="mb-4">
          <div class="flex items-center justify-between bg-gray-50 p-3 rounded-md">
            <span class="text-sm text-gray-700">{{ importFile.name }}</span>
            <button @click="removeFile" class="text-red-500 hover:text-red-700">
              <svg class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>
        </div>

        <div class="mb-4">
          <label class="flex items-center">
            <input
              v-model="importOptions.hasHeader"
              type="checkbox"
              class="rounded border-gray-300 text-indigo-600 shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
            />
            <span class="ml-2 text-sm text-gray-700">File has header row</span>
          </label>
        </div>

        <div class="flex justify-end space-x-3">
          <ButtonDefault
            @click="closeImportModal"
            :label="'Cancel'"
            :customClasses="'bg-gray-300 hover:bg-gray-400 text-gray-800'"
          />
          <ButtonDefault
            @click="importProducts"
            :label="'Import'"
            :customClasses="'bg-blue-600 hover:bg-blue-700 text-white'"
          />
        </div>
      </div>
    </div>
  </div>

  <!-- Import Results Modal -->
  <div v-if="showImportResults" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
    <div class="relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white">
      <div class="mt-3">
        <h3 class="text-lg font-medium text-gray-900 mb-4">Import Results</h3>

        <div v-if="importResults" class="space-y-3">
          <div class="flex items-center justify-between">
            <span class="text-sm text-gray-700">Successful:</span>
            <span class="text-sm font-medium text-green-600">{{ importResults.successCount }}</span>
          </div>
          <div class="flex items-center justify-between">
            <span class="text-sm text-gray-700">Failed:</span>
            <span class="text-sm font-medium text-red-600">{{ importResults.errorCount }}</span>
          </div>
          <div class="flex items-center justify-between">
            <span class="text-sm text-gray-700">Total:</span>
            <span class="text-sm font-medium text-gray-900">{{ importResults.totalCount }}</span>
          </div>

          <div v-if="importResults.errors && importResults.errors.length > 0" class="mt-4">
            <h4 class="text-sm font-medium text-red-600 mb-2">Errors:</h4>
            <div class="max-h-32 overflow-y-auto bg-red-50 p-3 rounded-md">
              <div v-for="(error, index) in importResults.errors.slice(0, 5)" :key="index" class="text-xs text-red-700 mb-1">
                Row {{ error.row }}: {{ error.message }}
              </div>
              <div v-if="importResults.errors.length > 5" class="text-xs text-red-600">
                ... and {{ importResults.errors.length - 5 }} more errors
              </div>
            </div>
          </div>
        </div>

        <div class="flex justify-end mt-6">
          <ButtonDefault
            @click="closeImportResults"
            :label="'Close'"
            :customClasses="'bg-indigo-600 hover:bg-indigo-700 text-white'"
          />
        </div>
      </div>
    </div>
  </div>
</div>
</template>
<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { Button } from '@/components/ui/button'
import { 
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

// Import modal state
const showImportModal = ref(false)
const importFile = ref<File | null>(null)
const importOptions = ref({
  hasHeader: true
})

// Import results state
const showImportResults = ref(false)
const importResults = ref<{
  successCount: number
  errorCount: number
  totalCount: number
  errors: any[] | null
} | null>(null)

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

// Import modal functions
const openImportModal = () => {
  showImportModal.value = true
}

const closeImportModal = () => {
  showImportModal.value = false
  importFile.value = null
  importOptions.value.hasHeader = true
}

const closeImportResults = () => {
  showImportResults.value = false
  importResults.value = null
}

const handleFileSelect = (event: Event) => {
  const target = event.target as HTMLInputElement
  const file = target.files?.[0]
  if (file) {
    importFile.value = file
  }
}

const removeFile = () => {
  importFile.value = null
  const fileInput = document.querySelector('input[type="file"]') as HTMLInputElement
  if (fileInput) {
    fileInput.value = ''
  }
}

const importProducts = async () => {
  if (!importFile.value) {
    alert('Please select a file to import')
    return
  }

  try {
    const formData = new FormData()
    formData.append('file', importFile.value)
    formData.append('hasHeader', importOptions.value.hasHeader.toString())
    formData.append('headerRow', '0')
    formData.append('dataStartRow', '1')

    const response = await fetch('/api/v1/admin/import_data/products/excel', {
      method: 'POST',
      body: formData
    })

    if (response.ok) {
      const result = await response.json()
      importResults.value = {
        successCount: result.data?.successCount || 0,
        errorCount: result.data?.errorCount || 0,
        totalCount: result.data?.totalCount || 0,
        errors: result.data?.errors || null
      }
      showImportModal.value = false
      showImportResults.value = true

      // Refresh dashboard data if import was successful
      if (importResults.value.successCount > 0) {
        console.log('Import successful, refreshing dashboard...')
      }
    } else {
      throw new Error(`Import failed: ${response.statusText}`)
    }
  } catch (error) {
    console.error('Import error:', error)
    alert('Import failed. Please check your file and try again.')
  }
}

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

// Download product template function
const downloadProductTemplate = () => {
  try {
    // Create sample data for template
    const sampleData = [
      {
        sku: 'SP001',
        name: 'Sản phẩm mẫu 1',
        slug: 'san-pham-mau-1',
        shortDescription: 'Mô tả ngắn sản phẩm mẫu 1',
        price: 100000,
        stockQuantity: 50,
        brand: 'Thương hiệu A',
        category: 'Danh mục A',
        status: 'ACTIVE',
        unit: 'CAI',
        longDescription: 'Mô tả dài chi tiết về sản phẩm mẫu 1 với đầy đủ thông tin về tính năng, công dụng và hướng dẫn sử dụng.',
        specification: '{"color": "Đỏ", "size": "Lớn", "material": "Nhựa"}',
        packaging: 'Hộp carton 10x15x20cm',
        weight: 1.5,
        dimensions: '10x15x20',
        images: 'image1.jpg,image2.jpg,image3.jpg'
      },
      {
        sku: 'SP002',
        name: 'Sản phẩm mẫu 2',
        slug: 'san-pham-mau-2',
        shortDescription: 'Mô tả ngắn sản phẩm mẫu 2',
        price: 200000,
        stockQuantity: 30,
        brand: 'Thương hiệu B',
        category: 'Danh mục B',
        status: 'ACTIVE',
        unit: 'HOP',
        longDescription: 'Mô tả dài chi tiết về sản phẩm mẫu 2 với đầy đủ thông tin về tính năng, công dụng và hướng dẫn sử dụng.',
        specification: '{"color": "Xanh", "size": "Trung bình", "material": "Kim loại"}',
        packaging: 'Thùng giấy 20x25x30cm',
        weight: 2.8,
        dimensions: '20x25x30',
        images: 'image4.jpg,image5.jpg'
      }
    ];

    // Create CSV content
    const headers = [
      'sku', 'name', 'slug', 'shortDescription', 'price', 'stockQuantity',
      'brand', 'category', 'status', 'unit', 'longDescription', 'specification',
      'packaging', 'weight', 'dimensions', 'images'
    ];

    const csvContent = [
      headers.join(','),
      ...sampleData.map(row =>
        headers.map(header => {
          const value = row[header as keyof typeof row];
          // Handle special cases
          if (header === 'specification' && typeof value === 'object') {
            return `"${JSON.stringify(value).replace(/"/g, '""')}"`;
          }
          if (typeof value === 'string' && value.includes(',')) {
            return `"${value.replace(/"/g, '""')}"`;
          }
          return value || '';
        }).join(',')
      )
    ].join('\n');

    // Create and download file
    const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
    const link = document.createElement('a');
    const url = URL.createObjectURL(blob);

    link.setAttribute('href', url);
    link.setAttribute('download', 'product_template.csv');
    link.style.visibility = 'hidden';

    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);

    console.log('Product template downloaded successfully');
  } catch (error) {
    console.error('Error downloading template:', error);
  }
};
</script>
