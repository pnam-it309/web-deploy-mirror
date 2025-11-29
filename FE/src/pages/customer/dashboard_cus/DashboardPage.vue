<template>
  <div class="min-h-screen bg-gray-50 p-6 sm:p-8 lg:pl-0 font-sans">
    <div class="max-w-7xl mx-auto space-y-8">
      <!-- Hero Banner -->
      <div class="relative overflow-hidden rounded-2xl bg-gradient-to-r from-blue-700 via-purple-600 to-pink-600 shadow-xl text-white">
        <div class="absolute top-0 left-0 w-full h-full bg-[url('https://www.transparenttextures.com/patterns/cubes.png')] opacity-10"></div>
        <div class="relative z-10 p-8 md:p-12 lg:p-16">
          <div class="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-white/20 backdrop-blur-sm text-sm font-medium mb-6">
            <SparklesIcon class="w-4 h-4 text-yellow-300" />
            <span>Khuyến mãi đặc biệt</span>
          </div>
          <h1 class="text-4xl md:text-5xl lg:text-6xl font-bold mb-4 leading-tight">
            Mua sắm thông minh <br />
            Tiết kiệm tối đa
          </h1>
          <p class="text-lg md:text-xl text-blue-100 mb-8 max-w-2xl">
            Giảm giá lên đến 40% cho các sản phẩm công nghệ hàng đầu. Đừng bỏ lỡ cơ hội sở hữu những thiết bị mơ ước.
          </p>
          <button @click="$router.push('/customer/products')" class="group bg-white text-blue-600 hover:bg-blue-50 px-8 py-3 rounded-full font-semibold transition-all shadow-lg hover:shadow-xl flex items-center gap-2">
            Khám phá ngay
            <ArrowRightIcon class="w-5 h-5 group-hover:translate-x-1 transition-transform" />
          </button>
        </div>
      </div>

      <!-- Quick Stats / Summary Cards -->
      <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
        <!-- Orders Card -->
        <div class="group relative overflow-hidden rounded-2xl bg-blue-500 text-white p-6 shadow-lg transition-transform hover:-translate-y-1 cursor-pointer" @click="$router.push('/customer/orders')">
          <div class="absolute -right-6 -top-6 w-32 h-32 bg-white/10 rounded-full blur-2xl group-hover:bg-white/20 transition-colors"></div>
          <div class="flex justify-between items-start mb-8">
            <div class="p-3 bg-white/20 rounded-xl backdrop-blur-sm">
              <CubeIcon class="w-8 h-8 text-white" />
            </div>
            <ArrowRightIcon class="w-5 h-5 text-blue-200 group-hover:text-white transition-colors" />
          </div>
          <div>
            <div class="text-4xl font-bold mb-1">0</div>
            <div class="text-blue-100 text-sm font-medium">Đơn hàng của bạn</div>
          </div>
        </div>

        <!-- Wishlist Card -->
        <div class="group relative overflow-hidden rounded-2xl bg-purple-500 text-white p-6 shadow-lg transition-transform hover:-translate-y-1 cursor-pointer" @click="$router.push('/customer/wishlist')">
          <div class="absolute -right-6 -top-6 w-32 h-32 bg-white/10 rounded-full blur-2xl group-hover:bg-white/20 transition-colors"></div>
          <div class="flex justify-between items-start mb-8">
            <div class="p-3 bg-white/20 rounded-xl backdrop-blur-sm">
              <HeartIcon class="w-8 h-8 text-white" />
            </div>
            <ArrowRightIcon class="w-5 h-5 text-purple-200 group-hover:text-white transition-colors" />
          </div>
          <div>
            <div class="text-4xl font-bold mb-1">{{ wishlistCount }}</div>
            <div class="text-purple-100 text-sm font-medium">Sản phẩm yêu thích</div>
          </div>
        </div>

        <!-- Offers Card -->
        <div class="group relative overflow-hidden rounded-2xl bg-pink-500 text-white p-6 shadow-lg transition-transform hover:-translate-y-1 cursor-pointer">
          <div class="absolute -right-6 -top-6 w-32 h-32 bg-white/10 rounded-full blur-2xl group-hover:bg-white/20 transition-colors"></div>
          <div class="flex justify-between items-start mb-8">
            <div class="p-3 bg-white/20 rounded-xl backdrop-blur-sm">
              <GiftIcon class="w-8 h-8 text-white" />
            </div>
            <ArrowRightIcon class="w-5 h-5 text-pink-200 group-hover:text-white transition-colors" />
          </div>
          <div>
            <div class="text-4xl font-bold mb-1">5</div>
            <div class="text-pink-100 text-sm font-medium">Ưu đãi có sẵn</div>
          </div>
        </div>
      </div>

      <!-- Categories -->
      <div>
        <div class="mb-6">
          <h2 class="text-xl font-bold text-gray-900">Danh mục sản phẩm</h2>
          <p class="text-gray-500 text-sm">Khám phá theo danh mục yêu thích</p>
        </div>
        <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
          <div v-for="cat in categories" :key="cat.name"
               class="bg-white p-6 rounded-2xl shadow-sm border border-gray-100 hover:shadow-md transition-all cursor-pointer group"
               @click="filterByCategory(cat.id)">
            <div :class="`w-14 h-14 rounded-2xl ${cat.color} flex items-center justify-center mb-4 group-hover:scale-110 transition-transform`">
              <component :is="cat.icon" class="w-8 h-8 text-white" />
            </div>
            <h3 class="font-semibold text-gray-900 mb-1">{{ cat.name }}</h3>
            <p class="text-sm text-gray-500">{{ cat.count }} sản phẩm</p>
          </div>
        </div>
      </div>

      <!-- Flash Sale -->
      <div class="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden">
        <div class="p-6 border-b border-gray-100 flex flex-col sm:flex-row justify-between items-center gap-4">
          <div class="flex items-center gap-3">
            <div class="p-2 bg-red-100 rounded-lg">
              <BoltIcon class="w-6 h-6 text-red-600" />
            </div>
            <div>
              <h2 class="text-lg font-bold text-gray-900">Flash Sale - Giảm giá sốc</h2>
              <div class="flex items-center gap-2 text-sm text-gray-500">
                <ClockIcon class="w-4 h-4" />
                <span>Kết thúc trong <span class="font-mono font-bold text-red-600">{{ timeLeft }}</span></span>
              </div>
            </div>
          </div>
          <button class="text-blue-600 text-sm font-medium hover:underline flex items-center gap-1">
            Xem tất cả <ArrowRightIcon class="w-4 h-4" />
          </button>
        </div>

        <div class="p-6 grid grid-cols-1 md:grid-cols-2 gap-6">
          <!-- Flash Sale Item 1 -->
          <div class="flex gap-4 p-4 rounded-xl hover:bg-gray-50 transition-colors border border-gray-100">
            <div class="w-32 h-32 bg-gray-100 rounded-lg flex-shrink-0 overflow-hidden relative">
              <img src="https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=500&q=80" alt="Headphone" class="w-full h-full object-cover" />
              <div class="absolute top-2 right-2 bg-red-500 text-white text-xs font-bold px-2 py-1 rounded">-25%</div>
            </div>
            <div class="flex-1 flex flex-col justify-center">
              <div class="text-xs text-red-500 font-medium mb-1">Phụ kiện</div>
              <h3 class="font-bold text-gray-900 mb-2 line-clamp-1">Sony WH-1000XM5</h3>
              <p class="text-sm text-gray-500 mb-3 line-clamp-2">Tai nghe over-ear với công nghệ chống ồn hàng đầu</p>
              <div class="flex items-center gap-3 mt-auto">
                <span class="text-lg font-bold text-red-600">8.990.000đ</span>
                <span class="text-sm text-gray-400 line-through">11.956.700đ</span>
              </div>
            </div>
          </div>

          <!-- Flash Sale Item 2 -->
          <div class="flex gap-4 p-4 rounded-xl hover:bg-gray-50 transition-colors border border-gray-100">
            <div class="w-32 h-32 bg-gray-100 rounded-lg flex-shrink-0 overflow-hidden relative">
              <img src="https://images.unsplash.com/photo-1610945265078-38584e12a878?w=500&q=80" alt="Phone" class="w-full h-full object-cover" />
              <div class="absolute top-2 right-2 bg-red-500 text-white text-xs font-bold px-2 py-1 rounded">-25%</div>
            </div>
            <div class="flex-1 flex flex-col justify-center">
              <div class="text-xs text-red-500 font-medium mb-1">Điện thoại</div>
              <h3 class="font-bold text-gray-900 mb-2 line-clamp-1">Samsung Galaxy S24 Ultra</h3>
              <p class="text-sm text-gray-500 mb-3 line-clamp-2">Flagship Android với S Pen, camera zoom 100x</p>
              <div class="flex items-center gap-3 mt-auto">
                <span class="text-lg font-bold text-red-600">31.990.000đ</span>
                <span class="text-sm text-gray-400 line-through">42.546.700đ</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Featured Products -->
      <div>
        <div class="flex justify-between items-end mb-6">
          <div>
            <div class="flex items-center gap-2 mb-1">
              <div class="p-1.5 bg-blue-100 rounded-lg">
                <ChartBarIcon class="w-5 h-5 text-blue-600" />
              </div>
              <h2 class="text-xl font-bold text-gray-900">Sản phẩm nổi bật</h2>
            </div>
            <p class="text-gray-500 text-sm">Được nhiều người lựa chọn</p>
          </div>
          <button @click="$router.push('/customer/products')" class="text-blue-600 text-sm font-medium hover:underline flex items-center gap-1">
            Xem tất cả <ArrowRightIcon class="w-4 h-4" />
          </button>
        </div>

        <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
          <ProductCard
            v-for="product in featuredProducts"
            :key="product.id"
            :product="product"
            :is-in-wishlist="isInWishlist(product.id)"
            @add-to-cart="addToCart"
            @toggle-wishlist="toggleWishlist"
          />
        </div>
      </div>

      <!-- Bottom Promo Banners -->
      <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div class="relative overflow-hidden rounded-2xl bg-green-500 text-white p-8 shadow-lg">
          <div class="absolute -right-10 -bottom-10 w-48 h-48 bg-white/10 rounded-full blur-3xl"></div>
          <GiftIcon class="w-10 h-10 mb-4 text-green-100" />
          <h3 class="text-2xl font-bold mb-2">Ưu đãi độc quyền</h3>
          <p class="text-green-100 mb-6">Nhận voucher 500.000đ cho đơn hàng đầu tiên</p>
          <button class="bg-white text-green-600 px-6 py-2 rounded-lg font-semibold hover:bg-green-50 transition-colors">
            Nhận ngay
          </button>
        </div>

        <div class="relative overflow-hidden rounded-2xl bg-orange-500 text-white p-8 shadow-lg">
          <div class="absolute -right-10 -bottom-10 w-48 h-48 bg-white/10 rounded-full blur-3xl"></div>
          <TruckIcon class="w-10 h-10 mb-4 text-orange-100" />
          <h3 class="text-2xl font-bold mb-2">Miễn phí vận chuyển</h3>
          <p class="text-orange-100 mb-6">Cho tất cả đơn hàng trên 500.000đ</p>
          <button class="bg-white text-orange-600 px-6 py-2 rounded-lg font-semibold hover:bg-orange-50 transition-colors">
            Mua sắm ngay
          </button>
        </div>
      </div>

    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue';
import { useRouter } from 'vue-router';
import {
  SparklesIcon,
  ArrowRightIcon,
  CubeIcon,
  HeartIcon,
  GiftIcon,
  ComputerDesktopIcon,
  DevicePhoneMobileIcon,
  DeviceTabletIcon,
  SpeakerWaveIcon,
  BoltIcon,
  ClockIcon,
  ChartBarIcon,
  TruckIcon
} from '@heroicons/vue/24/outline';
import ProductCard from '@/pages/customer/products/ProductCard.vue';
import type { Product } from '@/types/product';

const router = useRouter();

// --- State ---
const wishlistCount = ref(0);
const timeLeft = ref('12:45:30');
const wishlistIds = ref<string[]>([]);

// Categories Data
const categories = [
  { id: 'Laptop', name: 'Laptop', count: '125', icon: ComputerDesktopIcon, color: 'bg-blue-500' },
  { id: 'Phone', name: 'Điện thoại', count: '89', icon: DevicePhoneMobileIcon, color: 'bg-purple-500' },
  { id: 'Tablet', name: 'Tablet', count: '56', icon: DeviceTabletIcon, color: 'bg-green-500' },
  { id: 'Accessories', name: 'Phụ kiện', count: '234', icon: SpeakerWaveIcon, color: 'bg-orange-500' },
];

// Featured Products Mock Data
// Store original products to reset filters
const allProducts = ref<Product[]>([]);
const featuredProducts = ref<Product[]>([]);

// Initialize products
const initializeProducts = () => {
  const products = [
  {
    id: 'p1',
    name: 'iPhone 15 Pro Max',
    price: 29990000,
    category: 'Điện thoại',
    image: 'https://images.unsplash.com/photo-1696446701796-da61225697cc?w=500&q=80',
    rating: 5,
    reviewCount: 128,
    description: 'Titan tự nhiên, chip A17 Pro mạnh mẽ nhất.',
    inStock: true,
    badge: 'New'
  },
  {
    id: 'p2',
    name: 'MacBook Pro 14"',
    price: 52990000,
    category: 'Laptop',
    image: 'https://images.unsplash.com/photo-1517336714731-489689fd1ca4?w=500&q=80',
    rating: 5,
    reviewCount: 85,
    description: 'Chip M3 Pro, màn hình Liquid Retina XDR.',
    inStock: true,
    badge: 'Best Seller'
  },
  {
    id: 'p3',
    name: 'AirPods Pro (Gen 2)',
    price: 5990000,
    category: 'Phụ kiện',
    image: 'https://images.unsplash.com/photo-1603351154351-5cf99bc32f2d?w=500&q=80',
    rating: 4.8,
    reviewCount: 342,
    description: 'Chống ồn chủ động gấp 2 lần, USB-C.',
    inStock: true
  },
  {
    id: 'p4',
    name: 'Samsung Galaxy Tab S9',
    price: 18990000,
    category: 'Tablet',
    image: 'https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?w=500&q=80',
    rating: 4.7,
    reviewCount: 56,
    description: 'Màn hình Dynamic AMOLED 2X, kháng nước IP68.',
    inStock: true
  },
];
  allProducts.value = [...products];
  featuredProducts.value = [...products];
};

// --- Methods ---

const updateWishlistCount = () => {
  const wishlist = JSON.parse(localStorage.getItem('wishlist') || '[]');
  wishlistCount.value = wishlist.length;
  wishlistIds.value = wishlist.map((item: any) => item.id);
};

const filterByCategory = (categoryId: string) => {
  // Map category IDs to what ProductList expects or just pass as query
  // For demo purposes, we'll just pass the name as search or category
  // Assuming ProductList handles 'categories' query param
  router.push({ path: '/customer/products', query: { categories: categoryId } });
};

const addToCart = (product: Product) => {
  alert(`Đã thêm "${product.name}" vào Yêu cầu đặt hàng!`);
};

const toggleWishlist = (product: Product) => {
  const wishlist = JSON.parse(localStorage.getItem('wishlist') || '[]');
  const index = wishlist.findIndex((item: any) => item.id === product.id);

  if (index === -1) {
    wishlist.push({
      id: product.id,
      name: product.name,
      price: product.price,
      image: product.image
    });
    wishlistIds.value.push(product.id);
  } else {
    wishlist.splice(index, 1);
    wishlistIds.value = wishlistIds.value.filter(id => id !== product.id);
  }

  localStorage.setItem('wishlist', JSON.stringify(wishlist));
  window.dispatchEvent(new Event('wishlist-updated'));
  updateWishlistCount();
};

const isInWishlist = (id: string | number) => {
  return wishlistIds.value.includes(String(id));
};

// Countdown Timer Logic
let timerInterval: any;
const startTimer = () => {
  // Set a deadline 12 hours from now for demo
  const deadline = new Date();
  deadline.setHours(deadline.getHours() + 12);

  timerInterval = setInterval(() => {
    const now = new Date();
    const diff = deadline.getTime() - now.getTime();

    if (diff <= 0) {
      clearInterval(timerInterval);
      timeLeft.value = "00:00:00";
      return;
    }

    const hours = Math.floor((diff / (1000 * 60 * 60)) % 24);
    const minutes = Math.floor((diff / (1000 * 60)) % 60);
    const seconds = Math.floor((diff / 1000) % 60);

    timeLeft.value = `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
  }, 1000);
};

// --- Lifecycle ---
onMounted(() => {
  updateWishlistCount();
  startTimer();
  initializeProducts();
  window.addEventListener('wishlist-updated', updateWishlistCount);
});

onUnmounted(() => {
  clearInterval(timerInterval);
  window.removeEventListener('wishlist-updated', updateWishlistCount);
});
</script>

<style scoped>
/* Custom Scrollbar for hidden overflow if needed */
.no-scrollbar::-webkit-scrollbar {
  display: none;
}
.no-scrollbar {
  -ms-overflow-style: none;
  scrollbar-width: none;
}
</style>
