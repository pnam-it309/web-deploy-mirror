<template>
  <div class="min-h-screen bg-gray-100 p-6 sm:p-8 lg:pl-0">
    <div class="max-w-full mx-auto">

      <div class="bg-white p-6 rounded-t-lg shadow-sm border-b border-gray-200" style="text-align: center">
        <div>
          <h1 class="text-xl font-semibold text-gray-800">Chào mừng bạn đến với catalogweb
          </h1>
        </div>
      </div>

      <div class="mt-4 p-6 bg-white shadow-md border border-gray-200">
        <h2 class="text-lg font-medium text-gray-800">Portfolio Performance</h2>
        <div class="mt-4 grid grid-cols-1 gap-5 sm:grid-cols-3">

          <div class="bg-white overflow-hidden rounded-lg border border-gray-200">
            <div class="flex items-center p-6 space-x-4">
              <div class="p-3 rounded-full bg-red-500 bg-opacity-10 text-red-500">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24"
                  stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M3 10h18M7 15h1m4 0h1m-7 4h12a3 3 0 003-3V8a3 3 0 00-3-3H6a3 3 0 00-3 3v8a3 3 0 003 3z" />
                </svg>
              </div>
              <div>
                <dt class="text-sm font-medium text-gray-500 truncate">Cash Deposits</dt>
                <dd class="mt-1 text-2xl font-bold text-gray-900">1,7M</dd>
                <p class="text-xs text-red-500">-54.7% less earnings</p>
              </div>
            </div>
          </div>

          <div class="bg-white overflow-hidden rounded-lg border border-gray-200">
            <div class="flex items-center p-6 space-x-4">
              <div class="p-3 rounded-full bg-red-700 bg-opacity-10 text-red-700">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24"
                  stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M13 7h8m0 0v8m0-8l-8 8-4-4-6 6" />
                </svg>
              </div>
              <div>
                <dt class="text-sm font-medium text-gray-500 truncate">Invested Dividends</dt>
                <dd class="mt-1 text-2xl font-bold text-gray-900">9M</dd>
                <p class="text-xs text-blue-500">Show Rate - 14.1%</p>
              </div>
            </div>
          </div>

          <div class="bg-white overflow-hidden rounded-lg border border-gray-200">
            <div class="flex items-center p-6 space-x-4">
              <div class="p-3 rounded-full bg-green-500 bg-opacity-10 text-green-500">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24"
                  stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M12 8c1.657 0 3 .895 3 2s-1.343 2-3 2m0-4c1.657 0 3 .895 3 2s-1.343 2-3 2m0 0c-1.657 0-3 .895-3 2s1.343 2 3 2m0-4c-1.657 0-3 .895-3 2s1.343 2 3 2" />
                </svg>
              </div>
              <div>
                <dt class="text-sm font-medium text-gray-500 truncate">Capital Gains</dt>
                <dd class="mt-1 text-2xl font-bold text-gray-900">$563</dd>
                <p class="text-xs text-green-500">Increased by +7.35%</p>
              </div>
            </div>
          </div>
        </div>
        <div class="mt-4 flex justify-center">
          <button
            class="px-5 py-2 border border-gray-300 text-sm font-medium rounded-md text-blue-600 bg-white hover:bg-gray-50">
            View Complete Report
          </button>
        </div>
      </div>

      <div class="mt-4 p-6 bg-white shadow-md border border-gray-200">
        <h2 class="text-lg font-medium text-gray-800">Company Agents Status</h2>

        <div class="mt-4">
          <div class="flex flex-col">
            <div class="-my-2 overflow-x-auto sm:-mx-6 lg:-mx-8">
              <div class="py-2 align-middle inline-block min-w-full sm:px-6 lg:px-8">
                <div class="shadow overflow-hidden border-b border-gray-200 sm:rounded-lg">
                  <table class="min-w-full divide-y divide-gray-200">
                    <thead class="bg-gray-50">
                      <tr>
                        <th scope="col"
                          class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                          #
                        </th>
                        <th scope="col"
                          class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                          Avatar
                        </th>
                        <th scope="col"
                          class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                          Name
                        </th>
                        <th scope="col"
                          class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                          Company
                        </th>
                        <th scope="col"
                          class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                          Status
                        </th>
                        <th scope="col"
                          class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                          Due Date
                        </th>
                        <th scope="col"
                          class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                          Target Achievement
                        </th>
                        <th scope="col" class="relative px-6 py-3">
                          <span class="sr-only">Actions</span>
                          Actions
                        </th>
                      </tr>
                    </thead>
                    <tbody class="bg-white divide-y divide-gray-200">
                      <tr v-for="(agent, index) in agentsData" :key="agent.name">
                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{{ index + 54 }}</td>
                        <td class="px-6 py-4 whitespace-nowrap">
                          <img :src="agent.avatar" alt="Avatar" class="h-8 w-8 rounded-full">
                        </td>
                        <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-blue-600">
                          {{ agent.name }}
                        </td>
                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{{ agent.company }}</td>
                        <td class="px-6 py-4 whitespace-nowrap text-sm">
                          <span :class="getStatusClasses(agent.status)">
                            {{ agent.status }}
                          </span>
                        </td>
                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">12 Dec</td>
                        <td class="px-6 py-4 whitespace-nowrap text-sm">
                          <div class="flex items-center space-x-2">
                            <span :class="getProgressTextClasses(agent.progress)">
                              {{ agent.progress }}%
                            </span>
                            <div class="w-24 h-2 bg-gray-200 rounded-full">
                              <div :class="getProgressBgClasses(agent.progress)"
                                :style="{ width: agent.progress + '%' }" class="h-full rounded-full"></div>
                            </div>
                          </div>
                        </td>
                        <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium space-x-2">
                          <button
                            class="text-white px-3 py-1 text-xs rounded bg-blue-500 hover:bg-blue-600">Hire</button>
                          <button class="text-white px-3 py-1 text-xs rounded bg-red-500 hover:bg-red-600">Fire</button>
                        </td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="mt-4 flex justify-center">
          <button
            class="px-5 py-2 border border-gray-300 text-sm font-medium rounded-md text-blue-600 bg-white hover:bg-gray-50">
            View Complete Report
          </button>
        </div>
      </div>

    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';

// Dữ liệu cho Bảng Company Agents Status
const agentsData = ref([
  {
    name: 'Juan C. Cargill',
    company: 'Micro Electronics',
    status: 'Canceled',
    progress: 71,
    avatar: 'https://i.pravatar.cc/150?img=1'
  },
  {
    name: 'Johnathan Phelan',
    company: 'Hatchworks',
    status: 'On Hold',
    progress: 54,
    avatar: 'https://i.pravatar.cc/150?img=2'
  },
  {
    name: 'Darrell Love',
    company: 'Riddle Electronics',
    status: 'Active',
    progress: 97,
    avatar: 'https://i.pravatar.cc/150?img=3'
  },
  {
    name: 'George T. Cottrell',
    company: 'Prudicud',
    status: 'Completed',
    progress: 89,
    avatar: 'https://i.pravatar.cc/150?img=4'
  },
]);

// Hàm trả về classes cho Status badge
const getStatusClasses = (status: string) => {
  switch (status) {
    case 'Canceled':
      return 'px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-red-100 text-red-800';
    case 'On Hold':
      return 'px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-yellow-100 text-yellow-800';
    case 'Active':
      return 'px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-blue-100 text-blue-800';
    case 'Completed':
      return 'px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-green-100 text-green-800';
    default:
      return 'px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-gray-100 text-gray-800';
  }
};

// Hàm trả về classes cho thanh Progress bar (Background)
const getProgressBgClasses = (progress: number) => {
  if (progress < 60) {
    return 'bg-red-500';
  } else if (progress < 90) {
    return 'bg-yellow-500';
  } else {
    return 'bg-green-500';
  }
};

// Hàm trả về classes cho phần trăm Progress (Text)
const getProgressTextClasses = (progress: number) => {
  if (progress < 60) {
    return 'text-red-600 font-bold';
  } else if (progress < 90) {
    return 'text-yellow-600 font-bold';
  } else {
    return 'text-green-600 font-bold';
  }
};
</script>
