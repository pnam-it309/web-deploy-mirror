<template>
    <footer class="bg-gray-900 text-white pt-16 pb-8 mt-auto">
        <div class="container mx-auto px-4">
            <div class="grid grid-cols-1 md:grid-cols-4 gap-12 mb-12">
                <!-- Brand Column -->
                <div class="col-span-1 md:col-span-1">
                    <div class="flex items-center gap-2 mb-6">
                        <div class="w-8 h-8 bg-white p-1 rounded-lg flex items-center justify-center">
                            <img src="@/assets/images/logo-udpm-dark.png" alt="Logo" class="w-full h-full object-contain" />
                        </div>
                        <span class="text-xl font-bold">UDPM Catalog</span>
                    </div>
                    <p class="text-gray-400 text-sm leading-relaxed mb-6">
                        Kho tàng sản phẩm số chất lượng cao từ sinh viên FPT Polytechnic. Khám phá, học hỏi và chia sẻ.
                    </p>
                    <div class="flex gap-4">
                        <a href="#"
                            class="w-8 h-8 rounded-full bg-gray-800 flex items-center justify-center text-gray-400 hover:bg-indigo-600 hover:text-white transition-all">
                            <i class="fab fa-facebook-f text-sm"></i>
                        </a>
                        <a href="#"
                            class="w-8 h-8 rounded-full bg-gray-800 flex items-center justify-center text-gray-400 hover:bg-indigo-600 hover:text-white transition-all">
                            <i class="fab fa-github text-sm"></i>
                        </a>
                        <a href="#"
                            class="w-8 h-8 rounded-full bg-gray-800 flex items-center justify-center text-gray-400 hover:bg-indigo-600 hover:text-white transition-all">
                            <i class="fab fa-youtube text-sm"></i>
                        </a>
                    </div>
                </div>

                <!-- Links Column 1 -->
                <div>
                    <h4 class="text-lg font-bold mb-6">Khám phá</h4>
                    <ul class="space-y-4 text-sm text-gray-400">
                        <li><a href="#" class="hover:text-white transition-colors">Sản phẩm nổi bật</a></li>
                        <li><a href="#" class="hover:text-white transition-colors">Lĩnh vực HOT</a></li>
                        <li><a href="#" class="hover:text-white transition-colors">Tác giả xuất sắc</a></li>
                        <li><a href="#" class="hover:text-white transition-colors">Công nghệ mới</a></li>
                    </ul>
                </div>

                <!-- Links Column 2 -->
                <div>
                    <h4 class="text-lg font-bold mb-6">Thông tin</h4>
                    <ul class="space-y-4 text-sm text-gray-400">
                        <li><a href="#" class="hover:text-white transition-colors">Về chúng tôi</a></li>
                        <li><a href="#" class="hover:text-white transition-colors">Điều khoản sử dụng</a></li>
                        <li><a href="#" class="hover:text-white transition-colors">Chính sách bảo mật</a></li>
                        <li><a href="#" class="hover:text-white transition-colors">Liên hệ hỗ trợ</a></li>
                    </ul>
                </div>

                <!-- Contact Column -->
                <div>
                    <h4 class="text-lg font-bold mb-6">Liên hệ</h4>
                    <ul class="space-y-4 text-sm text-gray-400">
                        <li class="flex items-start gap-3">
                            <span class="mt-1 text-indigo-500">📍</span>
                            <span>Tòa nhà FPT Polytechnic, Phố Trịnh Văn Bô, Nam Từ Liêm, Hà Nội</span>
                        </li>
                        <li class="flex items-center gap-3">
                            <span class="text-indigo-500">📧</span>
                            <span>udpm@fpt.edu.vn</span>
                        </li>
                        <li class="flex items-center gap-3">
                            <span class="text-indigo-500">📞</span>
                            <span>(0XX) XXX XXXX</span>
                        </li>
                    </ul>
                </div>
            </div>

            <!-- Bottom Bar -->
            <div class="pt-8 border-t border-gray-800 flex flex-col md:flex-row justify-between items-center gap-4">
                <p class="text-gray-500 text-sm">
                    &copy; {{ new Date().getFullYear() }} FPL UDPM Catalog. Developed with ❤️ by FPL Students.
                </p>
                <div class="flex gap-6 text-sm text-gray-500">
                    <a href="#" class="hover:text-white transition-colors">Quyền riêng tư</a>
                    <a href="#" class="hover:text-white transition-colors">Cookie</a>
                </div>
            </div>
        </div>
    </footer>
</template>