<template>
  <ModalCustom :show="true" @close="close">
    <template #title> Tạo đơn hàng mới </template>

    <template #default>
      <div class="space-y-4">
        <InputCustom v-model="form.code" label="Mã đơn" />
        <InputCustom v-model="form.customer" label="Khách hàng" />
        <InputCustom v-model="form.date" label="Ngày đặt" type="date" />
        <InputCustom v-model.number="form.total" label="Tổng tiền" type="number" min="0" />

        <SelectCustom v-model="form.payment" label="Thanh toán">
          <option value="cod">Thanh toán khi nhận hàng (COD)</option>
          <option value="bank">Chuyển khoản</option>
          <option value="card">Thẻ tín dụng</option>
        </SelectCustom>

        <SelectCustom v-model="form.status" label="Trạng thái">
          <option value="new">Mới</option>
          <option value="processing">Đang xử lý</option>
          <option value="completed">Hoàn thành</option>
          <option value="cancelled">Hủy</option>
        </SelectCustom>
      </div>
    </template>

    <template #footer>
      <ButtonCustom color="secondary" @click="close"> Hủy </ButtonCustom>
      <ButtonCustom color="primary" @click="submitForm"> Lưu </ButtonCustom>
    </template>
  </ModalCustom>
</template>

<script setup lang="ts">
import { reactive, onMounted, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'
// Import components
import ModalCustom from '@/components/custom/Modal/ModalCustom.vue'
import ButtonCustom from '@/components/custom/Button/ButtonDefault.vue'
import InputCustom from '@/components/custom/Input/InputCustom.vue'
import SelectCustom from '@/components/custom/Select/SelectCustom.vue'

const emit = defineEmits<{
  (e: 'close'): void
  (
    e: 'created',
    payload: {
      code: string
      customer: string
      date: string
      total: number
      payment: string
      status: string
    }
  ): void
}>()

const router = useRouter()

const form = reactive({
  code: '',
  customer: '',
  date: new Date().toISOString().substr(0, 10), // default hôm nay
  total: 0,
  payment: 'cod',
  status: 'new',
})

const close = () => {
  emit('close')
  router.push({ name: 'admin-orders' }).catch(() => {
    router.back()
  })
}

const submitForm = () => {
  emit('created', { ...form })
  close()
}

const onKeydown = (e: KeyboardEvent) => {
  if (e.key === 'Escape' || e.key === 'Esc') {
    close()
  }
}

onMounted(() => {
  window.addEventListener('keydown', onKeydown)
})

onBeforeUnmount(() => {
  window.removeEventListener('keydown', onKeydown)
})
</script>