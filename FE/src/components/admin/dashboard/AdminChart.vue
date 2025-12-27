<template>
  <div class="w-full h-full min-h-[300px]">
    <Line v-if="type === 'line'" :data="chartData" :options="chartOptions" />
    <Bar v-else-if="type === 'bar'" :data="chartData" :options="chartOptions" />
    <Doughnut v-else-if="type === 'doughnut'" :data="chartData" :options="chartOptions" />
    <Pie v-else-if="type === 'pie'" :data="chartData" :options="chartOptions" />
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  BarElement,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  ArcElement,
  ChartData,
  ChartOptions
} from 'chart.js';
import { Bar, Line, Doughnut, Pie } from 'vue-chartjs';

ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
  PointElement,
  LineElement,
  ArcElement
);

const props = defineProps({
  type: {
    type: String,
    default: 'line',
    validator: (t: string) => ['line', 'bar', 'doughnut', 'pie'].includes(t),
  },
  data: {
    type: Object as () => ChartData,
    required: true,
  },
  options: {
    type: Object as () => ChartOptions,
    default: () => ({}),
  },
});

const defaultOptions: ChartOptions = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: {
      position: 'bottom',
      labels: {
        usePointStyle: true,
        boxWidth: 8,
      }
    }
  },
  scales: props.type === 'doughnut' || props.type === 'pie' ? {} : {
    y: {
      beginAtZero: true,
      grid: {
        color: '#f3f4f6',
      },
      border: {
        display: false
      }
    },
    x: {
      grid: {
        display: false,
      },
      border: {
        display: false
      }
    }
  }
};

const chartOptions = computed(() => {
  return { ...defaultOptions, ...props.options };
});

const chartData = computed(() => props.data);
</script>
