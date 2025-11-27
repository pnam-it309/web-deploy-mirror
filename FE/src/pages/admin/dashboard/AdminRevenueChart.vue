<template>
  <div class="space-y-6">
    <!-- Existing Charts -->
    <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
      <!-- Monthly Revenue Chart -->
      <div class="bg-white shadow rounded-lg p-6">
        <h3 class="text-lg font-medium leading-6 text-gray-900">Doanh thu theo tháng</h3>
        <p class="text-sm text-gray-500 mb-4">Biểu đồ doanh thu trong năm 2024</p>
        <div class="h-64">
          <LineChart 
            v-if="chartData && chartData.labels && chartData.labels.length > 0"
            :data="chartData"
            :options="chartOptions"
            :key="'line-chart' + chartDataKey"
          />
        </div>
      </div>

      <!-- Category Distribution Chart -->
      <div class="bg-white shadow rounded-lg p-6">
        <h3 class="text-lg font-medium leading-6 text-gray-900">Phân bổ theo danh mục</h3>
        <p class="text-sm text-gray-500 mb-4">Tỷ lệ doanh thu theo từng danh mục sản phẩm</p>
        <div class="h-64">
          <PieChart 
            v-if="pieData && pieData.labels && pieData.labels.length > 0"
            :data="pieData"
            :options="pieOptions"
            :key="'pie-chart' + chartDataKey"
          />
        </div>
      </div>
    </div>

    <!-- New Revenue Comparison and Brand Performance Section -->
    <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
      <!-- Revenue Comparison by Year -->
      <div class="bg-white shadow rounded-lg p-6">
        <div class="flex justify-between items-center mb-4">
          <h3 class="text-lg font-medium text-gray-900">So sánh doanh thu theo năm</h3>
        </div>
        <div class="h-80">
          <BarChart 
            v-if="barChartData && barChartData.labels && barChartData.labels.length > 0"
            :data="barChartData"
            :options="barChartOptions"
            :key="'bar-chart' + chartDataKey"
          />
        </div>
      </div>

      <!-- Brand Performance -->
      <div class="bg-white shadow rounded-lg p-6">
        <div class="flex justify-between items-center mb-4">
          <h3 class="text-lg font-medium text-gray-900">Hiệu suất theo thương hiệu</h3>
        </div>
        <div class="space-y-4">
          <div v-for="brand in brandPerformance" :key="brand.name" class="flex items-center justify-between p-3 hover:bg-gray-50 rounded-lg">
            <div class="flex items-center space-x-3">
              <div class="w-10 h-10 rounded-full bg-gray-100 flex items-center justify-center">
                <span class="text-sm font-medium text-gray-700">{{ brand.name.charAt(0) }}</span>
              </div>
              <div>
                <p class="text-sm font-medium text-gray-900">{{ brand.name }}</p>
                <p class="text-sm text-gray-500">{{ formatCurrency(brand.revenue) }}</p>
              </div>
            </div>
            <div :class="['text-sm font-medium', brand.change >= 0 ? 'text-green-600' : 'text-red-600']">
              <div class="flex items-center">
                <span>{{ brand.change >= 0 ? '↑' : '↓' }} {{ Math.abs(brand.change) }}%</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, nextTick } from 'vue';
import { Line as LineChart, Pie as PieChart, Bar as BarChart } from 'vue-chartjs';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  ArcElement,
  BarElement,
  Title,
  Tooltip,
  Legend
} from 'chart.js';

// Register ChartJS components
ChartJS.register(
  Title,
  Tooltip,
  Legend,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  ArcElement,
  BarElement
);

// Chart data key for forcing re-render
const chartDataKey = ref(0);

// Line Chart Data
// Initialize chart data with empty structure
const chartData = ref({
  labels: [],
  datasets: []
});

// Initialize chart data
const initChartData = () => {
  // Ensure data is properly structured
  chartData.value = {
    labels: ['T1', 'T2', 'T3', 'T4', 'T5', 'T6', 'T7', 'T8', 'T9', 'T10', 'T11', 'T12'],
    datasets: [{
      label: 'Doanh thu (triệu VNĐ)',
      data: [65, 75, 90, 80, 100, 110, 125, 115, 140, 155, 170, 190],
      borderColor: '#6366F1',
      backgroundColor: 'rgba(99, 102, 241, 0.2)',
      tension: 0.3,
      fill: true,
      pointBackgroundColor: '#6366F1',
      pointBorderColor: '#fff',
      pointHoverRadius: 6,
      pointHoverBorderWidth: 2
    }]
  };
  
  // Force update the chart data
  chartData.value = JSON.parse(JSON.stringify(chartData.value));
};

// Force chart re-render when data changes
const updateCharts = () => {
  chartDataKey.value++;
};

// Bar Chart Data
const barChartData = ref({
  labels: ['2022', '2023', '2024'],
  datasets: [
    {
      label: 'Doanh thu (triệu VNĐ)',
      data: [1200, 1800, 2400],
      backgroundColor: 'rgba(99, 102, 241, 0.8)',
      borderColor: '#6366F1',
      borderWidth: 1
    }
  ]
});

// Bar Chart Options
const barChartOptions = ref({
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: {
      position: 'top' as const,
    }
  },
  scales: {
    y: {
      beginAtZero: true,
      ticks: {
        callback: function(value: any) {
          return value + ' tr';
        }
      }
    }
  }
});

// Brand Performance Data
const brandPerformance = ref([
  { name: 'Apple', revenue: 4500000, change: 12 },
  { name: 'Samsung', revenue: 3800000, change: 8 },
  { name: 'Xiaomi', revenue: 1200000, change: -5 },
  { name: 'Oppo', revenue: 950000, change: 3 },
  { name: 'Vivo', revenue: 850000, change: -2 }
]);

// Format currency
const formatCurrency = (value: number) => {
  return new Intl.NumberFormat('vi-VN', { 
    style: 'currency', 
    currency: 'VND',
    maximumFractionDigits: 0
  }).format(value);
};

onMounted(async () => {
  // Initialize chart data
  initChartData();
  
  // Small delay to ensure DOM is ready
  await nextTick();
  
  // Force update charts
  updateCharts();
  
  // Force another update after a short delay to ensure everything is rendered
  setTimeout(updateCharts, 100);
});

// Line Chart Options
const chartOptions = ref({
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: {
      position: 'top' as const,
    },
    tooltip: {
      callbacks: {
        label: function(context: any) {
          return `${context.parsed.y} triệu VNĐ`;
        }
      }
    }
  },
  scales: {
    y: {
      beginAtZero: true,
      ticks: {
        callback: function(value: any) {
          return value + ' tr';
        }
      }
    }
  }
});

// Pie Chart Data
const pieData = ref({
  labels: ['Điện thoại', 'Laptop', 'Phụ kiện', 'Tablet', 'Khác'],
  datasets: [
    {
      data: [45, 20, 15, 15, 5],
      backgroundColor: [
        '#6366F1',
        '#EC4899',
        '#F59E0B',
        '#EF4444',
        '#10B981'
      ],
      borderWidth: 1
    }
  ]
});

// Pie Chart Options
const pieOptions = ref({
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: {
      position: 'right' as const,
    },
    tooltip: {
      callbacks: {
        label: function(context: any) {
          const label = context.label || '';
          const value = context.raw || 0;
          const total = context.dataset.data.reduce((a: number, b: number) => a + b, 0);
          const percentage = Math.round((value / total) * 100);
          return `${label}: ${percentage}% (${value} triệu VNĐ)`;
        }
      }
    }
  }
});
</script>