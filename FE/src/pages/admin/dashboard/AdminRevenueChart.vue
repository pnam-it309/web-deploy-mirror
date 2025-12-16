<template>
  <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
    <!-- 1. Biểu đồ Doanh thu (Line) -->
    <CardCustom class="col-span-1 lg:col-span-2">
      <div class="p-6">
        <div class="flex justify-between items-center mb-6">
          <div>
             <h3 class="text-lg font-bold text-brand-mocha dark:text-brand-sage">Biểu đồ Doanh thu & Đơn hàng</h3>
             <p class="text-xs text-gray-500 dark:text-gray-400">Dữ liệu theo thời gian thực</p>
          </div>
        </div>
        <div class="h-80 w-full relative">
          <!-- Chỉ render khi có dữ liệu -->
          <LineChart v-if="chartData && chartData.labels.length > 0" :data="chartData" :options="lineOptions" />
          <div v-else class="flex h-full items-center justify-center text-gray-400 italic">
             Chưa có dữ liệu biểu đồ
          </div>
        </div>
      </div>
    </CardCustom>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { Line as LineChart } from 'vue-chartjs';
import { Chart as ChartJS, registerables } from 'chart.js';
import CardCustom from '@/components/custom/Card/CardCustom.vue';
import { useDashboardStore } from '@/stores/dashboard.store';

ChartJS.register(...registerables);

// Lấy store
const dashboardStore = useDashboardStore();

// Computed data cho ChartJS
const chartData = computed(() => {
  const rawData = dashboardStore.chartData;
  return {
    labels: rawData.labels || [],
    datasets: [
      {
        label: 'Doanh thu (Triệu VNĐ)',
        data: rawData.revenue || [],
        borderColor: '#adc178', // Olive
        backgroundColor: 'rgba(173, 193, 120, 0.2)',
        tension: 0.4,
        fill: true,
        yAxisID: 'y'
      },
      {
        label: 'Số đơn hàng',
        data: rawData.orders || [],
        borderColor: '#a98467', // Coffee
        backgroundColor: 'rgba(169, 132, 103, 0.2)',
        tension: 0.4,
        fill: true,
        yAxisID: 'y1' // Trục Y thứ 2
      }
    ]
  };
});

// Options
const lineOptions = {
  responsive: true,
  maintainAspectRatio: false,
  interaction: {
    mode: 'index',
    intersect: false,
  },
  plugins: {
    legend: { labels: { color: '#6c584c' } }
  },
  scales: {
    y: {
      type: 'linear',
      display: true,
      position: 'left',
      grid: { color: 'rgba(0,0,0,0.05)' },
      ticks: { color: '#adc178' },
      title: { display: true, text: 'Doanh thu' }
    },
    y1: {
      type: 'linear',
      display: true,
      position: 'right',
      grid: { drawOnChartArea: false }, // không vẽ lưới cho trục này
      ticks: { color: '#a98467' },
      title: { display: true, text: 'Đơn hàng' }
    },
    x: { grid: { display: false }, ticks: { color: '#6c584c' } }
  }
};
</script>