<template>
  <div class="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50">
    <div class="bg-white rounded-lg shadow-lg w-full max-w-2xl text-gray-900">
      <!-- Header -->
      <div class="flex justify-between items-center px-6 py-4 border-b">
        <h3 class="text-lg font-semibold">
          {{ isEdit ? 'Chỉnh sửa sản phẩm' : 'Thêm sản phẩm mới' }}
        </h3>
        <button @click="onClose" class="text-gray-500 hover:text-gray-700 text-xl">×</button>
      </div>

      <!-- Body -->
      <div class="px-6 py-4 grid grid-cols-2 gap-4">
        <div>
          <label class="block text-sm font-medium text-gray-700">Tên sản phẩm</label>
          <input v-model.trim="form.name" type="text" class="input" />
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700">Slug</label>
          <input v-model.trim="form.slug" type="text" class="input" />
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700">SKU</label>
          <input v-model.trim="form.sku" type="text" class="input" />
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700">Giá (VND)</label>
          <input v-model.number="form.price" type="number" min="0" class="input" />
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700">Số lượng tồn kho</label>
          <input v-model.number="form.stockQuantity" type="number" min="0" class="input" />
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700">Trạng thái</label>
          <select v-model="form.status" class="input">
            <option value="ACTIVE">Đang bán</option>
            <option value="INACTIVE">Ngừng bán</option>
          </select>
        </div>

        <div class="col-span-2">
          <label class="block text-sm font-medium text-gray-700">Mô tả ngắn</label>
          <textarea v-model.trim="form.shortDescription" class="input h-24"></textarea>
        </div>
      </div>

      <!-- Footer -->
      <div class="flex justify-end space-x-3 px-6 py-4 border-t">
        <button @click="onClose" class="px-4 py-2 bg-gray-100 hover:bg-gray-200 rounded text-gray-700">
          Hủy
        </button>
        <button @click="onSave" class="px-4 py-2 bg-pink-600 hover:bg-pink-700 text-white rounded">
          {{ isEdit ? 'Lưu thay đổi' : 'Thêm mới' }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, watch, computed } from 'vue'

const props = defineProps({
  editItem: { type: Object, default: null },
})
const emit = defineEmits(['close', 'saved'])

const form = reactive({
  id: undefined,
  sku: '',
  name: '',
  slug: '',
  shortDescription: '',
  price: 0,
  stockQuantity: 0,
  brand: { id: 1, name: 'Apple' },
  category: { id: 1, name: 'Điện thoại' },
  status: 'ACTIVE',
})

const isEdit = computed(() => !!props.editItem)

watch(
  () => props.editItem,
  (val) => {
    if (val) Object.assign(form, val)
    else {
      form.id = undefined
      form.sku = ''
      form.name = ''
      form.slug = ''
      form.shortDescription = ''
      form.price = 0
      form.stockQuantity = 0
      form.status = 'ACTIVE'
    }
  },
  { immediate: true }
)

const onSave = () => {
  if (!form.name || !form.slug || !form.sku) {
    alert('Vui lòng nhập đủ thông tin bắt buộc: Tên, Slug, SKU.')
    return
  }
  emit('saved', { ...form })
}

const onClose = () => emit('close')
</script>

<style scoped>
.input {
  @apply mt-1 block w-full rounded-md border border-gray-300 shadow-sm
  focus:ring-pink-500 focus:border-pink-500 sm:text-sm text-gray-900;
}
</style>
