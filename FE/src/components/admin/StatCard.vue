<template>
  <div class="bg-white overflow-hidden shadow rounded-lg">
    <div class="p-5">
      <div class="flex items-center">
        <div class="flex-shrink-0 rounded-md p-3" :class="iconBgColor">
          <component :is="icon" class="h-6 w-6 text-white" />
        </div>
        <div class="ml-5 w-0 flex-1">
          <dl>
            <dt class="text-sm font-medium text-gray-500 truncate">
              {{ title }}
            </dt>
            <dd class="flex items-baseline">
              <div class="text-2xl font-semibold text-gray-900">
                {{ value }}
              </div>
              <div 
                v-if="trend && trendValue" 
                :class="trend === 'up' ? 'text-green-600' : 'text-red-600'"
                class="ml-2 flex items-baseline text-sm font-semibold"
              >
                <ArrowUpIcon 
                  v-if="trend === 'up'" 
                  class="self-center flex-shrink-0 h-4 w-4 text-green-500"
                />
                <ArrowDownIcon 
                  v-else 
                  class="self-center flex-shrink-0 h-4 w-4 text-red-500"
                />
                <span class="sr-only">
                  {{ trend === 'up' ? 'Tăng' : 'Giảm' }} so với tháng trước
                </span>
                {{ trendValue }}%
              </div>
            </dd>
          </dl>
        </div>
      </div>
    </div>
    <div class="bg-gray-50 px-5 py-3">
      <div class="text-sm">
        <a href="#" class="font-medium text-blue-700 hover:text-blue-900">
          Xem chi tiết
        </a>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { ArrowUpIcon, ArrowDownIcon } from '@heroicons/vue/20/solid';

const props = defineProps({
  title: {
    type: String,
    required: true
  },
  value: {
    type: [String, Number],
    required: true
  },
  icon: {
    type: String,
    required: true
  },
  trend: {
    type: String,
    validator: (value: string) => ['up', 'down', ''].includes(value),
    default: ''
  },
  trendValue: {
    type: [String, Number],
    default: 0
  },
  iconColor: {
    type: String,
    default: 'bg-blue-500'
  }
});

const iconBgColor = computed(() => {
  const colors: Record<string, string> = {
    'CubeIcon': 'bg-blue-500',
    'DocumentTextIcon': 'bg-green-500',
    'CurrencyDollarIcon': 'bg-yellow-500',
    'UserGroupIcon': 'bg-purple-500',
    'ArrowUpTrayIcon': 'bg-pink-500',
    'ArchiveBoxIcon': 'bg-indigo-500',
  };
  return colors[props.icon] || props.iconColor;
});
</script>
