<template>
  <DivCustom label="Bộ lọc thống kê" color="cream">
    <template #icon>
      <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 4a1 1 0 011-1h16a1 1 0 011 1v2.586a1 1 0 01-.293.707l-6.414 6.414a1 1 0 00-.293.707V17l-4 4v-6.586a1 1 0 00-.293-.707L3.293 7.293A1 1 0 013 6.586V4z" />
      </svg>
    </template>
    
    <div class="flex flex-col md:flex-row gap-4 items-end">
      <!-- Date Range Picker -->
      <div class="flex-1 w-full">
        <label class="block text-sm font-medium text-brand-mocha dark:text-brand-sage mb-1.5 ml-1">Khoảng thời gian</label>
        
        <!-- FIX QUAN TRỌNG: teleport="body" sẽ đưa popup lịch ra ngoài, không bao giờ bị che -->
        <DatePicker 
          v-model.range="dateRange" 
          mode="date"
          :masks="{ input: 'DD/MM/YYYY' }"
          :max-date="new Date()"
          :columns="2"
          teleport="body" 
          :popover="{ visibility: 'click', placement: 'bottom-start' }"
        >
          <template #default="{ inputValue, inputEvents }">
            <div class="flex items-center gap-2">
              <div class="relative flex-1 group">
                <input
                  :value="inputValue.start"
                  v-on="inputEvents.start"
                  readonly
                  class="block w-full rounded-lg border border-gray-200 bg-white/60 dark:bg-brand-dark-300/50 dark:border-brand-coffee/30 dark:text-brand-cream text-sm py-2.5 px-3 focus:ring-2 focus:ring-brand-olive/20 outline-none transition-colors cursor-pointer group-hover:border-brand-olive/50"
                  placeholder="Từ ngày"
                />
                <div class="absolute right-3 top-2.5 pointer-events-none text-gray-400">
                   <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" /></svg>
                </div>
              </div>

              <span class="text-brand-coffee font-bold">-</span>
              
              <div class="relative flex-1 group">
                <input
                  :value="inputValue.end"
                  v-on="inputEvents.end"
                  readonly
                  class="block w-full rounded-lg border border-gray-200 bg-white/60 dark:bg-brand-dark-300/50 dark:border-brand-coffee/30 dark:text-brand-cream text-sm py-2.5 px-3 focus:ring-2 focus:ring-brand-olive/20 outline-none transition-colors cursor-pointer group-hover:border-brand-olive/50"
                  placeholder="Đến ngày"
                />
                <div class="absolute right-3 top-2.5 pointer-events-none text-gray-400">
                   <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" /></svg>
                </div>
              </div>
            </div>
          </template>
        </DatePicker>
      </div>

      <!-- Actions -->
      <div class="flex gap-2 w-full md:w-auto">
        <ButtonCustom color="coffee" @click="applyFilters" class="flex-1 md:flex-none h-[42px] flex items-center justify-center">
          <span class="mr-1">🔍</span> Xem
        </ButtonCustom>
        <ButtonCustom color="cream" @click="resetFilters" class="flex-1 md:flex-none h-[42px] flex items-center justify-center">
          Làm mới
        </ButtonCustom>
        <ButtonCustom color="sage-soft" @click="exportReport" class="flex-1 md:flex-none h-[42px] flex items-center justify-center">
          Xuất báo cáo
        </ButtonCustom>
      </div>
    </div>
  </DivCustom>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { DatePicker } from 'v-calendar';
import 'v-calendar/dist/style.css';
import DivCustom from '@/components/custom/Div/DivCustom.vue';
import ButtonCustom from '@/components/custom/Button/ButtonDefault.vue';

const emit = defineEmits(['filter', 'export']);

const today = new Date();
const lastMonth = new Date(new Date().setDate(today.getDate() - 30));

const dateRange = ref<{ start: Date, end: Date }>({ 
  start: lastMonth, 
  end: today 
});

const applyFilters = () => {
  // Chỉ emit, KHÔNG gán lại dateRange.value = ... để tránh mất react
  emit('filter', { 
    startDate: dateRange.value.start, 
    endDate: dateRange.value.end 
  });
};

const resetFilters = () => {
  // Gán giá trị mới cho object value
  dateRange.value = { start: lastMonth, end: today };
  applyFilters();
};

const exportReport = () => {
  emit('export', { 
    startDate: dateRange.value.start, 
    endDate: dateRange.value.end 
  });
};

onMounted(() => {
  applyFilters();
});
</script>

<style>
/* Style Global cho Popup Lịch (Vì nó teleport ra body nên phải dùng style global) */
.vc-popover-content-wrapper {
  z-index: 99999 !important; 
}
.vc-pane-container {
    border-radius: 0.75rem;
    overflow: hidden;
    border: 1px solid #e6dfc0;
    box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.1);
    background-color: white;
}
.dark .vc-pane-container {
    background-color: #3e332e;
    border-color: #5e504a;
    color: #f0ead2;
}
.dark .vc-title, .dark .vc-arrow {
  color: #f0ead2; /* Màu chữ tiêu đề lịch */
}
.dark .vc-header, .dark .vc-weekday {
    color: #adc178;
}
.dark .vc-day-content:hover {
    background-color: #5e504a;
}
.vc-highlight {
    background-color: #adc178 !important;
}
.dark .vc-nav-item:hover {
    background-color: #5e504a;
    color: #f0ead2;
}
.dark .vc-nav-container {
    background-color: #3e332e; 
    border-color: #5e504a;
}
</style>