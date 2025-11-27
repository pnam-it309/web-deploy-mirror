<template>
  <div class="bg-white p-4 rounded-lg shadow-sm border border-gray-200 mb-4">
    <div class="grid grid-cols-1 md:grid-cols-4 gap-4 items-end">
      
      <!-- 1. Tìm kiếm (Tên/SKU) -->
      <div class="col-span-1 md:col-span-4 lg:col-span-1">
        <InputCustom
          v-model="filter.keyword"
          label="Tìm kiếm"
          placeholder="Tên sản phẩm, SKU..."
          @input="onInput"
        />
      </div>

      <!-- 2. Thương hiệu -->
      <div>
        <SelectCustom 
          v-model="filter.brandId" 
          label="Thương hiệu" 
          @change="onChange"
        >
          <option value="">Tất cả thương hiệu</option>
          <option v-for="b in brands" :key="b.id" :value="b.id">{{ b.name }}</option>
        </SelectCustom>
      </div>

      <!-- 3. Danh mục -->
      <div>
        <SelectCustom 
          v-model="filter.categoryId" 
          label="Danh mục" 
          @change="onChange"
        >
          <option value="">Tất cả danh mục</option>
          <option v-for="c in categories" :key="c.id" :value="c.id">{{ c.name }}</option>
        </SelectCustom>
      </div>

      <!-- 4. Trạng thái -->
      <div>
        <SelectCustom 
          v-model="filter.status" 
          label="Trạng thái" 
          @change="onChange"
        >
          <option value="">Tất cả trạng thái</option>
          <option value="ACTIVE">Đang bán</option>
          <option value="INACTIVE">Ngừng bán</option>
        </SelectCustom>
      </div>

      <!-- 5. Khoảng giá (Nâng cao) -->
      <div class="col-span-1 md:col-span-3 lg:col-span-2 flex items-center space-x-2">
        <div class="flex-1">
          <InputCustom
            v-model.number="filter.minPrice"
            label="Giá từ"
            type="number"
            placeholder="0"
            @input="onInput"
          />
        </div>
        <span class="pt-6 text-gray-400 font-bold">-</span>
        <div class="flex-1">
          <InputCustom
            v-model.number="filter.maxPrice"
            label="Đến"
            type="number"
            placeholder="Tối đa"
            @input="onInput"
          />
        </div>
      </div>

      <!-- Nút Reset -->
      <div class="col-span-1 flex justify-end md:justify-start md:col-span-4 lg:col-span-4 mt-2">
        <ButtonCustom
          color="sage" 
          @click="resetFilter"
          class="h-[42px] flex items-center justify-center gap-2 px-6"
        >
          <ReloadOutlined /> Làm mới
        </ButtonCustom>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive } from 'vue'
import { ReloadOutlined } from '@ant-design/icons-vue'

// Import các component tùy chỉnh chuẩn
import InputCustom from '@/components/custom/Input/InputCustom.vue'
import SelectCustom from '@/components/custom/Select/SelectCustom.vue'
import ButtonCustom from '@/components/custom/Button/ButtonDefault.vue'

const props = defineProps({
  brands: { type: Array as () => any[], default: () => [] },
  categories: { type: Array as () => any[], default: () => [] },
})

const emit = defineEmits(['filter'])

const filter = reactive({
  keyword: '',
  brandId: '',
  categoryId: '',
  status: '',
  minPrice: null as number | null,
  maxPrice: null as number | null,
})

let debounceTimer: any = null

// Hàm debounce để tránh spam API khi gõ phím
const onInput = () => {
  if (debounceTimer) clearTimeout(debounceTimer)
  debounceTimer = setTimeout(() => {
    emitFilter()
  }, 500) // Chờ 500ms sau khi ngừng gõ mới gọi API
}

// Với Select box thì gọi ngay
const onChange = () => {
  emitFilter()
}

const emitFilter = () => {
  // Clone và loại bỏ các giá trị rỗng để URL sạch đẹp
  const payload = { ...filter }
  if (!payload.keyword) delete (payload as any).keyword
  if (!payload.brandId) delete (payload as any).brandId
  if (!payload.categoryId) delete (payload as any).categoryId
  if (!payload.status) delete (payload as any).status
  if (payload.minPrice === null || payload.minPrice === 0 || payload.minPrice === '')
    delete (payload as any).minPrice
  if (payload.maxPrice === null || payload.maxPrice === 0 || payload.maxPrice === '')
    delete (payload as any).maxPrice

  emit('filter', payload)
}

const resetFilter = () => {
  filter.keyword = ''
  filter.brandId = ''
  filter.categoryId = ''
  filter.status = ''
  filter.minPrice = null
  filter.maxPrice = null
  emitFilter()
}
</script>

<style scoped>
/* Không cần thêm style vì đã dùng Tailwind và Component Custom */
</style>