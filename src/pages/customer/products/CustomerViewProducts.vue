<template>
  <div v-if="!loading && product" class="container mx-auto max-w-6xl px-4 py-8">
    <!-- Breadcrumb -->
    <div class="flex items-center gap-2 text-sm text-muted-foreground mb-8">
      <router-link to="/" class="hover:text-foreground">Home</router-link>
      <span>/</span>
      <router-link to="/products" class="hover:text-foreground">Products</router-link>
      <span>/</span>
      <span class="text-foreground">{{ product.name }}</span>
    </div>

    <div class="grid grid-cols-1 lg:grid-cols-2 gap-12 mb-12">
      <!-- Product Image Gallery -->
      <div class="space-y-4">
        <div class="aspect-square relative overflow-hidden rounded-lg bg-muted">
          <img
            :src="currentImage"
            :alt="product.name"
            class="w-full h-full object-cover"
          />
          <Badge
            v-if="product.badge"
            :variant="getBadgeVariant(product.badge)"
            class="absolute top-4 left-4"
          >
            {{ product.badge }}
          </Badge>
        </div>

        <div class="grid grid-cols-4 gap-2">
          <button
            v-for="(img, index) in product.images || [product.image]"
            :key="index"
            @click="currentImage = img"
            class="aspect-square rounded-md overflow-hidden border-2 transition-all"
            :class="currentImage === img ? 'border-primary' : 'border-transparent'"
          >
            <img :src="img" :alt="`${product.name} thumbnail ${index + 1}`" class="w-full h-full object-cover" />
          </button>
        </div>
      </div>

      <!-- Product Info -->
      <div class="space-y-6">
        <div>
          <div class="flex items-center gap-2 text-sm text-muted-foreground mb-2">
            <span>{{ product.category }}</span>
          </div>
          <h1 class="mb-4">{{ product.name }}</h1>

          <!-- Rating -->
          <div class="flex items-center gap-2 mb-4">
            <div class="flex items-center gap-1">
              <StarIcon
                v-for="i in 5"
                :key="i"
                class="h-5 w-5"
                :class="{
                  'fill-yellow-400 text-yellow-400': i <= Math.floor(product.rating),
                  'text-gray-300': i > Math.floor(product.rating)
                }"
              />
            </div>
            <span class="text-sm text-muted-foreground">
              {{ product.reviewCount || 0 }} reviews
            </span>
          </div>

          <!-- Price -->
          <div class="text-3xl font-bold mb-6">
            ${{ product.price.toFixed(2) }}
          </div>

          <!-- Description -->
          <p class="text-muted-foreground mb-6">
            {{ product.description }}
          </p>

          <!-- Add to Cart -->
          <div class="flex flex-wrap items-center gap-4 mb-8">
            <div class="flex items-center border rounded-md">
              <button
                @click="quantity > 1 ? quantity-- : null"
                class="px-4 py-2 text-lg text-muted-foreground hover:bg-muted"
                :class="{ 'opacity-50 cursor-not-allowed': quantity <= 1 }"
              >
                -
              </button>
              <span class="w-12 text-center">{{ quantity }}</span>
              <button
                @click="quantity++"
                class="px-4 py-2 text-lg text-muted-foreground hover:bg-muted"
              >
                +
              </button>
            </div>

            <Button
              size="lg"
              class="flex-1 min-w-[200px]"
              @click="addToCart"
              :disabled="!product.inStock"
            >
              <ShoppingCartIcon class="h-5 w-5 mr-2" />
              Add to Cart
            </Button>

            <Button
              variant="outline"
              size="icon"
              class="h-12 w-12"
              @click="toggleWishlist"
            >
              <HeartIcon
                class="h-6 w-6"
                :class="{ 'fill-red-500 text-red-500': isWishlisted }"
              />
            </Button>
          </div>

          <!-- Stock Status -->
          <div class="flex items-center gap-2 mb-6">
            <div
              class="w-3 h-3 rounded-full"
              :class="product.inStock ? 'bg-green-500' : 'bg-red-500'"
            ></div>
            <span class="text-sm">
              {{ product.inStock ? 'In Stock' : 'Out of Stock' }}
            </span>
          </div>

          <!-- Product Details -->
          <div class="space-y-4">
            <div v-if="product.details" class="space-y-2">
              <h3 class="font-medium">Product Details</h3>
              <ul class="text-sm text-muted-foreground space-y-1">
                <li v-for="(value, key) in product.details" :key="key">
                  <span class="font-medium text-foreground">{{ key }}:</span> {{ value }}
                </li>
              </ul>
            </div>

            <div class="pt-4 border-t">
              <h3 class="font-medium mb-3">Benefits</h3>
              <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
                <div v-for="(benefit, index) in benefits" :key="index" class="flex items-start gap-3">
                  <component :is="benefit.icon" class="h-6 w-6 text-primary mt-0.5 flex-shrink-0" />
                  <div>
                    <h4 class="font-medium">{{ benefit.title }}</h4>
                    <p class="text-sm text-muted-foreground">{{ benefit.description }}</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Product Tabs -->
    <Tabs default-value="description" class="mb-16">
      <TabsList class="grid w-full grid-cols-3 max-w-md mb-8">
        <TabsTrigger value="description">Description</TabsTrigger>
        <TabsTrigger value="specifications">Specifications</TabsTrigger>
        <TabsTrigger value="reviews">Reviews</TabsTrigger>
      </TabsList>

      <TabsContent value="description" class="prose max-w-none">
        <h3>Product Description</h3>
        <p>
          {{ product.description }}
        </p>
        <p>
          This high-quality product is designed to meet all your needs with its premium features and durable construction.
          It's perfect for both professional and personal use, offering exceptional performance and reliability.
        </p>
      </TabsContent>

      <TabsContent value="specifications">
        <div class="space-y-4">
          <h3 class="text-lg font-medium">Technical Specifications</h3>
          <div class="bg-muted/50 rounded-lg p-6">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div v-for="(value, key) in product.details || {}" :key="key" class="flex">
                <span class="w-1/3 text-muted-foreground">{{ key }}</span>
                <span class="flex-1 font-medium">{{ value }}</span>
              </div>
            </div>
          </div>
        </div>
      </TabsContent>

      <TabsContent value="reviews">
        <div class="space-y-8">
          <div class="flex items-start gap-12">
            <div class="text-center">
              <div class="text-5xl font-bold mb-2">{{ product.rating.toFixed(1) }}</div>
              <div class="flex justify-center mb-2">
                <StarIcon
                  v-for="i in 5"
                  :key="i"
                  class="h-5 w-5"
                  :class="{
                    'fill-yellow-400 text-yellow-400': i <= Math.floor(product.rating),
                    'text-gray-300': i > Math.floor(product.rating)
                  }"
                />
              </div>
              <p class="text-sm text-muted-foreground">Based on {{ product.reviewCount || 0 }} reviews</p>
            </div>

            <div class="flex-1">
              <div v-for="i in 5" :key="i" class="flex items-center gap-2 mb-2">
                <span class="w-8 text-sm text-muted-foreground">{{ 6 - i }} stars</span>
                <div class="h-2 bg-muted rounded-full flex-1 max-w-xs">
                  <div
                    class="h-full bg-yellow-400 rounded-full"
                    :style="{ width: `${getRatingPercentage(6 - i)}%` }"
                  ></div>
                </div>
                <span class="text-sm text-muted-foreground w-8 text-right">
                  {{ getRatingCount(6 - i) }}
                </span>
              </div>
            </div>
          </div>

          <Button class="mt-6">Write a Review</Button>

          <div class="space-y-6 mt-8">
            <div v-for="review in reviews" :key="review.id" class="border-b pb-6">
              <div class="flex items-center gap-4 mb-2">
                <div class="h-10 w-10 rounded-full bg-muted flex items-center justify-center">
                  <span class="font-medium">{{ review.author.charAt(0).toUpperCase() }}</span>
                </div>
                <div>
                  <h4 class="font-medium">{{ review.author }}</h4>
                  <div class="flex items-center gap-1">
                    <StarIcon
                      v-for="i in 5"
                      :key="i"
                      class="h-4 w-4"
                      :class="{
                        'fill-yellow-400 text-yellow-400': i <= review.rating,
                        'text-gray-300': i > review.rating
                      }"
                    />
                    <span class="text-xs text-muted-foreground ml-1">{{ formatDate(review.date) }}</span>
                  </div>
                </div>
              </div>
              <p class="text-muted-foreground">{{ review.comment }}</p>
            </div>
          </div>
        </div>
      </TabsContent>
    </Tabs>

    <!-- Related Products -->
    <section class="mb-16">
      <h2 class="text-2xl font-bold mb-8">You May Also Like</h2>
      <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
        <ProductCard
          v-for="relatedProduct in relatedProducts"
          :key="relatedProduct.id"
          :product="relatedProduct"
          class="h-full"
          @add-to-cart="addRelatedToCart"
        />
      </div>
    </section>
  </div>

  <div v-else-if="loading" class="container mx-auto max-w-6xl px-4 py-16 text-center">
    <div class="animate-pulse space-y-4">
      <div class="h-8 bg-muted rounded w-1/3 mx-auto"></div>
      <div class="h-4 bg-muted rounded w-1/2 mx-auto"></div>
    </div>
  </div>

  <div v-else class="container mx-auto max-w-6xl px-4 py-16 text-center">
    <h1 class="text-2xl font-bold mb-4">Product Not Found</h1>
    <p class="text-muted-foreground mb-8">The product you're looking for doesn't exist or has been removed.</p>
    <Button @click="$router.push('/products')">
      <ArrowLeftIcon class="h-4 w-4 mr-2" />
      Back to Products
    </Button>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import {
  StarIcon,
  ShoppingCartIcon,
  HeartIcon,
  ArrowLeftIcon,
  TruckIcon,
  ShieldCheckIcon,
  RefreshCwIcon
} from '@heroicons/vue/24/outline';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import ProductCard from '@/pages/customer/products/ProductCard.vue';
import { useCartStore } from '@/stores/cart.ts';
import type { Product } from '@/types/product.ts';

const route = useRoute();
const router = useRouter();
const cartStore = useCartStore();

const productId = route.params.id as string;
const loading = ref(true);
const product = ref<Product | null>(null);
const currentImage = ref('');
const quantity = ref(1);
const isWishlisted = ref(false);

// Mock related products - replace with API call in production
const relatedProducts = ref<Product[]>([]);

// Mock reviews - replace with API call in production
const reviews = ref([
  {
    id: 1,
    author: 'John Doe',
    rating: 5,
    date: '2024-01-15',
    comment: 'This product exceeded my expectations! The quality is outstanding and it works perfectly.'
  },
  {
    id: 2,
    author: 'Jane Smith',
    rating: 4,
    date: '2024-01-10',
    comment: 'Very good product, but the shipping took longer than expected.'
  }
]);

const benefits = [
  {
    icon: TruckIcon,
    title: 'Free Shipping',
    description: 'Free delivery on orders over $50'
  },
  {
    icon: RefreshCwIcon,
    title: '30-Day Returns',
    description: 'Easy returns within 30 days'
  },
  {
    icon: ShieldCheckIcon,
    title: '2-Year Warranty',
    description: 'Comprehensive product warranty'
  }
];

// Fetch product data
const fetchProduct = async () => {
  try {
    loading.value = true;
    // In a real app, you would fetch the product from an API
    // const response = await fetch(`/api/products/${productId}`);
    // product.value = await response.json();

    // Mock data for demo
    await new Promise(resolve => setTimeout(resolve, 500));

    // This would come from an API in a real app
    const mockProduct: Product = {
      id: productId,
      name: 'Premium Wireless Headphones',
      description: 'Experience crystal clear sound with our premium wireless headphones. Featuring active noise cancellation, 30-hour battery life, and plush ear cushions for all-day comfort.',
      price: 299.99,
      image: 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=800&h=800&fit=crop',
      images: [
        'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=800&h=800&fit=crop',
        'https://images.unsplash.com/photo-1572569511254-d8f925fe2cbb?w=800&h=800&fit=crop',
        'https://images.unsplash.com/photo-1578319439584-104c94d37305?w=800&h=800&fit=crop',
        'https://images.unsplash.com/photo-1618366719320-5a0a8d9b9a2d?w=800&h=800&fit=crop'
      ],
      category: 'Electronics',
      rating: 4.5,
      reviewCount: 128,
      badge: 'New',
      inStock: true,
      details: {
        'Brand': 'AudioPro',
        'Model': 'WH-1000X',
        'Color': 'Black',
        'Connectivity': 'Bluetooth 5.0',
        'Battery Life': '30 hours',
        'Noise Cancellation': 'Active',
        'Weight': '254g',
        'Warranty': '2 years'
      }
    };

    product.value = mockProduct;
    currentImage.value = mockProduct.image;

    // Generate some related products
    relatedProducts.value = Array(4).fill(0).map((_, i) => ({
      id: `related-${i}`,
      name: `Related Product ${i + 1}`,
      description: `High-quality related product that pairs well with ${mockProduct.name}`,
      price: Math.floor(Math.random() * 200) + 99.99,
      image: `https://picsum.photos/seed/product-${i}/500/500`,
      category: mockProduct.category,
      rating: Math.floor(Math.random() * 2) + 3.5, // 3.5 - 5.5
      reviewCount: Math.floor(Math.random() * 100),
      inStock: Math.random() > 0.2,
      badge: Math.random() > 0.7 ? 'Sale' : Math.random() > 0.7 ? 'New' : undefined
    }));

  } catch (error) {
    console.error('Error fetching product:', error);
    // Handle error
  } finally {
    loading.value = false;
  }
};

const getBadgeVariant = (badge: string) => {
  switch (badge) {
    case 'Sale': return 'destructive';
    case 'New': return 'default';
    case 'Limited': return 'secondary';
    default: return 'default';
  }
};

const addToCart = () => {
  if (!product.value) return;

  for (let i = 0; i < quantity.value; i++) {
    cartStore.addToCart(product.value);
  }

  // Show success message
  console.log(`${quantity.value} x ${product.value.name} added to cart!`);
  // In a real app, you might want to show a toast notification here
};

const addRelatedToCart = (relatedProduct: Product) => {
  cartStore.addToCart(relatedProduct);
  // Show success message
  console.log(`${relatedProduct.name} added to cart!`);
};

const toggleWishlist = () => {
  isWishlisted.value = !isWishlisted.value;
  console.log(isWishlisted.value ? 'Added to wishlist' : 'Removed from wishlist');
  // In a real app, you would update the wishlist in your backend here
};

const getRatingPercentage = (stars: number) => {
  // In a real app, this would be calculated from actual review data
  if (stars === 5) return 75;
  if (stars === 4) return 20;
  if (stars === 3) return 5;
  return 0;
};

const getRatingCount = (stars: number) => {
  // In a real app, this would come from your review data
  const totalReviews = product.value?.reviewCount || 0;
  const percentage = getRatingPercentage(stars) / 100;
  return Math.round(totalReviews * percentage);
};

const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  });
};

onMounted(() => {
  fetchProduct();
});
</script>
