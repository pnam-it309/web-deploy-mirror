<template>
    <div class="bg-white p-6 rounded-2xl shadow-sm border border-gray-100 mb-8">
        <div class="flex flex-col md:flex-row md:items-center md:justify-between mb-4">
            <h2 class="text-lg font-semibold text-gray-900">Bộ lọc báo cáo</h2>
            <button @click="exportReport"
                class="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500">
                <svg class="-ml-1 mr-2 h-5 w-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20"
                    fill="currentColor">
                    <path fill-rule="evenodd"
                        d="M3 17a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zm3.293-7.707a1 1 0 011.414 0L9 10.586V3a1 1 0 112 0v7.586l1.293-1.293a1 1 0 111.414 1.414l-3 3a1 1 0 01-1.414 0l-3-3a1 1 0 010-1.414z"
                        clip-rule="evenodd" />
                </svg>
                Xuất báo cáo
            </button>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
            <!-- Date Range Picker -->
            <div class="space-y-2">
                <label class="block text-sm font-medium text-gray-700">Khoảng thời gian</label>
                <DatePicker v-model.range="dateRange" :max-date="new Date()"
                    :attributes="[{ highlight: { color: 'blue', fillMode: 'light' } }]" is-range class="w-full">
                    <template v-slot="{ inputValue, inputEvents }">
                        <div class="flex space-x-2">
                            <input :value="inputValue.start" v-on="inputEvents.start"
                                class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                                placeholder="Từ ngày" />
                            <span class="flex items-center">-</span>
                            <input :value="inputValue.end" v-on="inputEvents.end"
                                class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                                placeholder="Đến ngày" />
                        </div>
                    </template>
                </DatePicker>
            </div>

            <!-- Status Filter -->
            <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">Trạng thái đơn hàng</label>
                <div class="space-y-2">
                    <label v-for="status in orderStatuses" :key="status.id" class="flex items-center">
                        <input type="checkbox" :value="status.id" v-model="selectedStatuses"
                            class="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded">
                        <span class="ml-2 text-sm text-gray-700">{{ status.name }}</span>
                    </label>
                </div>
            </div>

            <!-- Action Buttons -->
            <div class="flex items-end space-x-3">
                <button @click="applyFilters"
                    class="flex-1 bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors text-sm font-medium">
                    Áp dụng
                </button>
                <button @click="resetFilters"
                    class="flex-1 bg-white border border-gray-300 text-gray-700 px-4 py-2 rounded-lg hover:bg-gray-50 transition-colors text-sm font-medium">
                    Đặt lại
                </button>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { DatePicker } from 'v-calendar';
import 'v-calendar/dist/style.css';

interface DateRange {
    start: Date | null;
    end: Date | null;
}

interface OrderStatus {
    id: string;
    name: string;
}

interface FilterEvent {
    startDate: Date | null;
    endDate: Date | null;
    statuses: string[];
}

const emit = defineEmits<{
    (e: 'filter', filters: FilterEvent): void;
    (e: 'export', filters: FilterEvent): void;
}>();

// Date range for filtering
const dateRange = ref<DateRange>({
    start: null,
    end: null
});

// Order status options
const orderStatuses: OrderStatus[] = [
    { id: 'pending', name: 'Chờ xử lý' },
    { id: 'processing', name: 'Đang xử lý' },
    { id: 'shipped', name: 'Đã giao hàng' },
    { id: 'delivered', name: 'Đã nhận hàng' },
    { id: 'cancelled', name: 'Đã hủy' },
];

const selectedStatuses = ref<string[]>([]);

const applyFilters = (): void => {
    const filters: FilterEvent = {
        startDate: dateRange.value.start,
        endDate: dateRange.value.end,
        statuses: selectedStatuses.value
    };
    emit('filter', filters);
};

const resetFilters = (): void => {
    dateRange.value = { start: null, end: null };
    selectedStatuses.value = [];

    // Emit empty filters to reset the parent component
    emit('filter', {
        startDate: null,
        endDate: null,
        statuses: []
    });
};

const exportReport = (): void => {
    const filters: FilterEvent = {
        startDate: dateRange.value.start,
        endDate: dateRange.value.end,
        statuses: selectedStatuses.value
    };
    emit('export', filters);
};

// Apply filters when component is mounted
onMounted(() => {
    applyFilters();
});
</script>

<style scoped>
:deep(.vc-pane-layout) {
    @apply bg-white shadow-lg rounded-lg border border-gray-200;
}

:deep(.vc-weekday) {
    @apply text-gray-800 font-medium text-sm;
}

:deep(.vc-day) {
    @apply text-gray-700 text-sm;
}

:deep(.vc-highlight) {
    @apply bg-blue-600 text-white;
}
</style>