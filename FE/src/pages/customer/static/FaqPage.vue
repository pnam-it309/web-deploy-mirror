<template>
  <div class="faq-page">
    <!-- Hero Section -->
    <div class="bg-gradient-to-r from-blue-600 to-blue-800 text-white py-16">
      <div class="container mx-auto px-4 text-center">
        <h1 class="text-4xl md:text-5xl font-bold mb-4">Câu Hỏi Thường Gặp</h1>
        <p class="text-xl md:text-2xl max-w-3xl mx-auto">Tìm câu trả lời cho những thắc mắc phổ biến của bạn</p>
      </div>
    </div>

    <!-- FAQ Section -->
    <section class="py-16 bg-white">
      <div class="container mx-auto px-4 max-w-4xl">
        <div class="space-y-6">
          <div v-for="(category, index) in faqCategories" :key="index" class="mb-12">
            <h2 class="text-2xl font-bold text-gray-800 mb-6 pb-2 border-b border-gray-200">{{ category.name }}</h2>
            <div class="space-y-4">
              <div v-for="(item, itemIndex) in category.items" :key="itemIndex" class="border border-gray-200 rounded-lg overflow-hidden">
                <button 
                  class="w-full px-6 py-4 text-left flex justify-between items-center bg-gray-50 hover:bg-gray-100 transition-colors duration-200"
                  @click="toggleFaq(index, itemIndex)"
                >
                  <span class="font-medium text-gray-800">{{ item.question }}</span>
                  <svg 
                    class="w-5 h-5 text-gray-500 transition-transform duration-200" 
                    :class="{ 'transform rotate-180': item.isOpen }"
                    fill="none" 
                    viewBox="0 0 24 24" 
                    stroke="currentColor"
                  >
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
                  </svg>
                </button>
                <div 
                  v-show="item.isOpen"
                  class="px-6 py-4 bg-white text-gray-600"
                  v-html="item.answer"
                ></div>
              </div>
            </div>
          </div>
        </div>

        <!-- Contact CTA -->
        <div class="mt-16 bg-blue-50 rounded-lg p-8 text-center">
          <h3 class="text-2xl font-bold text-gray-800 mb-4">Không tìm thấy câu trả lời bạn cần?</h3>
          <p class="text-gray-600 mb-6">Đội ngũ hỗ trợ của chúng tôi luôn sẵn lòng giúp đỡ bạn</p>
          <router-link 
            :to="{ name: 'customer-contact' }"
            class="inline-flex items-center px-6 py-3 border border-transparent text-base font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 transition-colors"
          >
            Liên Hệ Hỗ Trợ
          </router-link>
        </div>
      </div>
    </section>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue';

export default defineComponent({
  name: 'FaqPage',
  setup() {
    const faqCategories = ref([
      {
        name: 'Đặt Hàng & Thanh Toán',
        items: [
          {
            question: 'Làm thế nào để đặt hàng?',
            answer: 'Để đặt hàng, bạn chỉ cần thêm sản phẩm vào giỏ hàng, chọn phương thức thanh toán và điền thông tin giao hàng. Sau khi đơn hàng được xác nhận, chúng tôi sẽ liên hệ với bạn trong thời gian sớm nhất.',
            isOpen: false
          },
          {
            question: 'Có những phương thức thanh toán nào?',
            answer: 'Chúng tôi chấp nhận nhiều hình thức thanh toán khác nhau bao gồm: <ul class="list-disc pl-5 mt-2 space-y-1"><li>Thanh toán khi nhận hàng (COD)</li><li>Chuyển khoản ngân hàng</li><li>Ví điện tử (Momo, ZaloPay, VNPay)</li><li>Thẻ tín dụng/ghi nợ (Visa, Mastercard, JCB)</li></ul>',
            isOpen: false
          },
          {
            question: 'Làm sao để kiểm tra tình trạng đơn hàng?',
            answer: 'Bạn có thể kiểm tra tình trạng đơn hàng bằng cách đăng nhập vào tài khoản và vào mục "Đơn hàng của tôi". Nếu cần hỗ trợ thêm, vui lòng liên hệ tổng đài chăm sóc khách hàng của chúng tôi.',
            isOpen: false
          }
        ]
      },
      {
        name: 'Vận Chuyển & Giao Hàng',
        items: [
          {
            question: 'Thời gian giao hàng dự kiến là bao lâu?',
            answer: 'Thời gian giao hàng phụ thuộc vào địa chỉ nhận hàng của quý khách. Thông thường, đơn hàng sẽ được giao trong vòng 1-3 ngày làm việc đối với khu vực nội thành và 3-7 ngày đối với các tỉnh thành khác.',
            isOpen: false
          },
          {
            question: 'Phí vận chuyển được tính như thế nào?',
            answer: 'Phí vận chuyển được tính dựa trên địa chỉ giao hàng và trọng lượng đơn hàng. Chúng tôi có chính sách miễn phí vận chuyển cho đơn hàng có giá trị từ 500,000 VND trở lên. Bạn có thể kiểm tra chính xác phí vận chuyển trong quá trình đặt hàng.',
            isOpen: false
          },
          {
            question: 'Có giao hàng quốc tế không?',
            answer: 'Hiện tại, chúng tôi mới chỉ cung cấp dịch vụ giao hàng trong phạm vi lãnh thổ Việt Nam. Chúng tôi sẽ thông báo ngay khi có dịch vụ giao hàng quốc tế.',
            isOpen: false
          }
        ]
      },
      {
        name: 'Đổi Trả & Bảo Hành',
        items: [
          {
            question: 'Chính sách đổi trả hàng như thế nào?',
            answer: 'Chúng tôi chấp nhận đổi trả hàng trong vòng 07 ngày kể từ ngày nhận hàng nếu sản phẩm gặp lỗi từ nhà sản xuất hoặc không đúng với mô tả. Sản phẩm phải còn nguyên vẹn, chưa qua sử dụng và còn đầy đủ hộp, phụ kiện đi kèm.',
            isOpen: false
          },
          {
            question: 'Làm thế nào để yêu cầu bảo hành?',
            answer: 'Để yêu cầu bảo hành, vui lòng liên hệ bộ phận chăm sóc khách hàng qua hotline hoặc email với thông tin đơn hàng và tình trạng sản phẩm. Chúng tôi sẽ hướng dẫn bạn các bước tiếp theo để xử lý yêu cầu bảo hành.',
            isOpen: false
          },
          {
            question: 'Thời gian bảo hành là bao lâu?',
            answer: 'Thời gian bảo hành tùy thuộc vào từng sản phẩm và chính sách của nhà sản xuất. Thông thường, sản phẩm điện tử có thời gian bảo hành từ 12-24 tháng. Chi tiết về thời gian bảo hành được ghi rõ trên phiếu bảo hành và hóa đơn mua hàng.',
            isOpen: false
          }
        ]
      },
      {
        name: 'Tài Khoản & Bảo Mật',
        items: [
          {
            question: 'Làm thế nào để đặt lại mật khẩu?',
            answer: 'Tại trang đăng nhập, nhấp vào liên kết "Quên mật khẩu" và làm theo hướng dẫn. Bạn sẽ nhận được email hướng dẫn đặt lại mật khẩu mới. Vui lòng kiểm tra cả hộp thư spam nếu không thấy email trong hộp thư chính.',
            isOpen: false
          },
          {
            question: 'Làm sao để cập nhật thông tin tài khoản?',
            answer: 'Sau khi đăng nhập, bạn có thể cập nhật thông tin cá nhân trong mục "Tài khoản của tôi". Tại đây, bạn có thể thay đổi thông tin liên hệ, địa chỉ nhận hàng và mật khẩu.',
            isOpen: false
          },
          {
            question: 'Thông tin thanh toán của tôi có an toàn không?',
            answer: 'Chúng tôi sử dụng công nghệ mã hóa SSL để bảo vệ thông tin thanh toán của bạn. Mọi giao dịch đều được bảo mật và không lưu trữ thông tin thẻ tín dụng trên hệ thống của chúng tôi.',
            isOpen: false
          }
        ]
      }
    ]);

    const toggleFaq = (categoryIndex: number, itemIndex: number) => {
      faqCategories.value[categoryIndex].items[itemIndex].isOpen = 
        !faqCategories.value[categoryIndex].items[itemIndex].isOpen;
    };

    return {
      faqCategories,
      toggleFaq
    };
  }
});
</script>

<style scoped>
.faq-page {
  min-height: calc(100vh - 80px);
}

/* Smooth transition for FAQ items */
.fade-enter-active, .fade-leave-active {
  transition: opacity 0.3s ease;
}
.fade-enter-from, .fade-leave-to {
  opacity: 0;
}
</style>
