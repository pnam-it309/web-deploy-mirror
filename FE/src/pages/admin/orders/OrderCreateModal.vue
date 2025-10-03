<template>
  <!-- Overlay -->
  <!-- Sửa: thêm @click.self="close" để click ngoài modal (overlay) sẽ đóng modal -->
  <div
    class="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50"
    @click.self="close"
  >
    <!-- Modal -->
    <div class="bg-white rounded-lg shadow-lg w-full max-w-lg">
      <!-- Header -->
      <div class="flex justify-between items-center px-6 py-4 border-b border-gray-200">
        <h3 class="text-lg font-semibold text-gray-800">Tạo đơn hàng mới</h3>

        <!-- Sửa: đổi từ @click="$emit('close')" sang @click="close" để dùng hàm close() -->
        <!-- close() sẽ emit('close') và điều hướng về route cha (nếu component đang render qua route) -->
        <button @click="close" class="text-gray-400 hover:text-gray-600" aria-label="Đóng">
          ✕
        </button>
      </div>

      <!-- Body -->
      <div class="px-6 py-4 space-y-4">
        <!-- Mã đơn -->
        <div>
          <label class="block text-sm font-medium text-gray-700">Mã đơn</label>
          <input v-model="form.code" type="text"
                 class="mt-1 block w-full rounded-md border-gray-300 shadow-sm 
                        focus:ring-pink-500 focus:border-pink-500 sm:text-sm" />
        </div>

        <!-- Khách hàng -->
        <div>
          <label class="block text-sm font-medium text-gray-700">Khách hàng</label>
          <input v-model="form.customer" type="text"
                 class="mt-1 block w-full rounded-md border-gray-300 shadow-sm 
                        focus:ring-pink-500 focus:border-pink-500 sm:text-sm" />
        </div>

        <!-- Ngày đặt -->
        <div>
          <label class="block text-sm font-medium text-gray-700">Ngày đặt</label>
          <input v-model="form.date" type="date"
                 class="mt-1 block w-full rounded-md border-gray-300 shadow-sm 
                        focus:ring-pink-500 focus:border-pink-500 sm:text-sm" />
        </div>

        <!-- Tổng tiền -->
        <div>
          <label class="block text-sm font-medium text-gray-700">Tổng tiền</label>
          <input v-model="form.total" type="number" min="0"
                 class="mt-1 block w-full rounded-md border-gray-300 shadow-sm 
                        focus:ring-pink-500 focus:border-pink-500 sm:text-sm" />
        </div>

        <!-- Thanh toán -->
        <div>
          <label class="block text-sm font-medium text-gray-700">Thanh toán</label>
          <select v-model="form.payment"
                  class="mt-1 block w-full rounded-md border-gray-300 shadow-sm 
                         focus:ring-pink-500 focus:border-pink-500 sm:text-sm">
            <option value="cod">Thanh toán khi nhận hàng (COD)</option>
            <option value="bank">Chuyển khoản</option>
            <option value="card">Thẻ tín dụng</option>
          </select>
        </div>

        <!-- Trạng thái -->
        <div>
          <label class="block text-sm font-medium text-gray-700">Trạng thái</label>
          <select v-model="form.status"
                  class="mt-1 block w-full rounded-md border-gray-300 shadow-sm 
                         focus:ring-pink-500 focus:border-pink-500 sm:text-sm">
            <option value="new">Mới</option>
            <option value="processing">Đang xử lý</option>
            <option value="completed">Hoàn thành</option>
            <option value="cancelled">Hủy</option>
          </select>
        </div>
      </div>

      <!-- Footer -->
      <div class="flex justify-end space-x-3 px-6 py-4 border-t border-gray-200">
        <!-- Sửa: đổi @click="$emit('close')" sang @click="close" -->
        <button @click="close"
                class="px-4 py-2 rounded-md text-sm font-medium text-gray-700 bg-gray-100 hover:bg-gray-200">
          Hủy
        </button>
        <button @click="submitForm"
                class="px-4 py-2 rounded-md text-sm font-medium text-white bg-pink-600 hover:bg-pink-700">
          Lưu
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, onMounted, onBeforeUnmount } from "vue"
import { useRouter } from "vue-router"

/**
 * Thay đổi chính:
 * - Sử dụng defineEmits để emit sự kiện 'created' và 'close' như trước.
 * - Thêm hàm close() để:
 *    1) emit('close') để giữ hành vi tương thích nếu parent đang lắng nghe event
 *    2) điều hướng về route cha (name: 'admin-orders') — phù hợp trường hợp modal được render bằng route con
 * - Thêm @click.self trên overlay để click ngoài modal đóng.
 * - Thêm handler phím Escape để đóng modal khi người dùng nhấn Esc.
 *
 * Lưu ý: Nếu bạn render modal bằng v-if trong parent và parent xử lý event 'close' thì emit('close') vẫn có tác dụng.
 * Nếu modal được render bởi router (route con), router sẽ push về route cha để "đóng" modal.
 */

// kiểu emits (TypeScript)
const emit = defineEmits<{
  (e: 'close'): void
  (e: 'created', payload: {
    code: string,
    customer: string,
    date: string,
    total: number,
    payment: string,
    status: string
  }): void
}>()

const router = useRouter()

const form = reactive({
  code: "",
  customer: "",
  date: new Date().toISOString().substr(0, 10), // default hôm nay
  total: 0,
  payment: "cod",
  status: "new",
})

/**
 * close() - gọi khi cần đóng modal
 * - emit('close') để parent (nếu lắng nghe) nhận được sự kiện
 * - điều hướng về route cha 'admin-orders' (nếu đang dùng route-based modal)
 * - nếu push về route cha lỗi, fallback dùng router.back()
 */
const close = () => {
  emit('close')

  // Thử điều hướng về route cha có name 'admin-orders'.
  // Nếu bạn dùng tên route khác, đổi sang tên tương ứng hoặc dùng router.back()
  router.push({ name: 'admin-orders' }).catch(() => {
    // fallback: quay lại lịch sử nếu push thất bại
    router.back()
  })
}

// submit form: emit sự kiện 'created' rồi đóng modal (qua close())
const submitForm = () => {
  emit("created", { ...form })
  close()
}

// Sửa: thêm listener phím Escape để đóng modal bằng Esc
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
