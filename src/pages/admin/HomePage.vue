<template>
  <div class="min-h-screen">
    <!-- Hero Section -->
    <section class="relative bg-gradient-to-r from-primary to-primary/80 text-primary-foreground py-20 px-4">
      <div class="container mx-auto max-w-6xl text-center">
        <h1 class="mb-6 leading-tight">
          Discover Amazing Products at Unbeatable Prices
        </h1>
        <p class="mb-8 max-w-2xl mx-auto opacity-90">
          Shop the latest trends in electronics, fashion, home goods, and more.
          Quality products, fast shipping, and exceptional customer service.
        </p>
        <div class="flex gap-4 justify-center flex-wrap">
          <router-link to="/products">
            <Button size="lg" variant="secondary">
              Shop Now
              <ArrowRightIcon class="ml-2 h-4 w-4" />
            </Button>
          </router-link>
          <router-link to="/products">
            <Button size="lg" variant="outline" class="border-primary-foreground text-primary-foreground hover:bg-primary-foreground hover:text-primary">
              Browse Categories
            </Button>
          </router-link>
        </div>
      </div>
    </section>

    <!-- Features Section -->
    <section class="py-16 px-4 bg-muted/30">
      <div class="container mx-auto max-w-6xl">
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          <Card v-for="(feature, index) in features" :key="index" class="text-center p-6 border-0 shadow-sm">
            <CardContent class="p-0">
              <component :is="feature.icon" class="h-12 w-12 text-primary mx-auto mb-4" />
              <h3 class="mb-2">{{ feature.title }}</h3>
              <p class="text-muted-foreground">{{ feature.description }}</p>
            </CardContent>
          </Card>
        </div>
      </div>
    </section>

    <!-- Featured Products Section -->
    <section class="py-16 px-4">
      <div class="container mx-auto max-w-6xl">
        <div class="text-center mb-12">
          <h2 class="mb-4">Featured Products</h2>
          <p class="text-muted-foreground max-w-2xl mx-auto">
            Check out our hand-picked selection of the most popular and trending products
          </p>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
          <ProductCard
            v-for="product in featuredProducts"
            :key="product.id"
            :product="product"
            @add-to-cart="addToCart"
          />
        </div>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { ShoppingBagIcon, ShieldCheckIcon, ArrowTrendingUpIcon, UsersIcon, ArrowRightIcon } from '@heroicons/vue/24/outline';
import { Button } from '@/components/ui/button';
import { Card, CardContent } from '@/components/ui/card';
import ProductCard from '@/pages/customer/products/ProductCard.vue';
import { useCartStore } from '@/stores/cart.ts';
import type { Product } from '@/types/product.ts';

const cartStore = useCartStore();
const router = useRouter();

const features = [
  {
    icon: ShoppingBagIcon,
    title: "Free Shipping",
    description: "Free shipping on orders over $50"
  },
  {
    icon: ShieldCheckIcon,
    title: "Secure Payment",
    description: "100% secure payment protection"
  },
  {
    icon: ArrowTrendingUpIcon,
    title: "Best Prices",
    description: "Competitive prices guaranteed"
  },
  {
    icon: UsersIcon,
    title: "24/7 Support",
    description: "Round-the-clock customer service"
  }
];

// Mock data - replace with API call in production
const featuredProducts = ref<Product[]>([
  {
    id: '1',
    name: 'Wireless Headphones',
    description: 'High-quality wireless headphones with noise cancellation',
    price: 199.99,
    image: 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=500&h=500&fit=crop',
    category: 'Electronics',
    rating: 4.5,
    reviewCount: 128,
    badge: 'New',
    inStock: true
  },
  // Add more products as needed
]);

const addToCart = (product: Product) => {
  cartStore.addToCart(product);
  // Show toast notification
  // You can implement a toast system or use a library like vue-toastification
  console.log(`${product.name} added to cart!`);
};

const viewProduct = (productId: string) => {
  router.push(`/products/${productId}`);
};
</script>
