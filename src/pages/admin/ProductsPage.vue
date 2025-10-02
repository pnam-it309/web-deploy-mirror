<template>
  <div class="p-6">
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-2xl font-semibold text-gray-900">Product Management</h1>
      <router-link
        to="/admin/products/new"
        class="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
      >
        Add Product
      </router-link>
    </div>
    
    <div class="flex flex-col">
      <div class="-my-2 overflow-x-auto sm:-mx-6 lg:-mx-8">
        <div class="py-2 align-middle inline-block min-w-full sm:px-6 lg:px-8">
          <div class="shadow overflow-hidden border-b border-gray-200 sm:rounded-lg">
            <table class="min-w-full divide-y divide-gray-200">
              <thead class="bg-gray-50">
                <tr>
                  <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Product
                  </th>
                  <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Category
                  </th>
                  <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Price
                  </th>
                  <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Stock
                  </th>
                  <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Status
                  </th>
                  <th scope="col" class="relative px-6 py-3">
                    <span class="sr-only">Actions</span>
                  </th>
                </tr>
              </thead>
              <tbody class="bg-white divide-y divide-gray-200">
                <tr v-for="product in products" :key="product.id" class="hover:bg-gray-50">
                  <td class="px-6 py-4 whitespace-nowrap">
                    <div class="flex items-center">
                      <div class="flex-shrink-0 h-10 w-10">
                        <img class="h-10 w-10 rounded-full" :src="product.image" alt="">
                      </div>
                      <div class="ml-4">
                        <div class="text-sm font-medium text-gray-900">{{ product.name }}</div>
                        <div class="text-sm text-gray-500">SKU: {{ product.sku }}</div>
                      </div>
                    </div>
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap">
                    <div class="text-sm text-gray-900">{{ product.category }}</div>
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap">
                    <div class="text-sm text-gray-900">${{ product.price.toFixed(2) }}</div>
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap">
                    <div class="text-sm text-gray-900">{{ product.stock }}</div>
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap">
                    <span :class="[
                      product.status === 'In Stock' ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800',
                      'px-2 inline-flex text-xs leading-5 font-semibold rounded-full'
                    ]">
                      {{ product.status }}
                    </span>
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                    <a href="#" class="text-indigo-600 hover:text-indigo-900 mr-4">Edit</a>
                    <a href="#" class="text-red-600 hover:text-red-900">Delete</a>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
const products = [
  {
    id: 1,
    name: 'Wireless Headphones',
    sku: 'WH-001',
    category: 'Electronics',
    price: 99.99,
    stock: 45,
    status: 'In Stock',
    image: 'https://via.placeholder.com/40',
  },
  {
    id: 2,
    name: 'Smart Watch',
    sku: 'SW-101',
    category: 'Wearables',
    price: 199.99,
    stock: 0,
    status: 'Out of Stock',
    image: 'https://via.placeholder.com/40',
  },
  {
    id: 3,
    name: 'Bluetooth Speaker',
    sku: 'BS-201',
    category: 'Audio',
    price: 79.99,
    stock: 12,
    status: 'In Stock',
    image: 'https://via.placeholder.com/40',
  },
];
</script>
