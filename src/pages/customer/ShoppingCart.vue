<template>
  <div class="container mx-auto max-w-6xl px-4 py-8">
    <div v-if="cartItems.length > 0">
      <div class="flex items-center gap-4 mb-8">
        <router-link to="/products">
          <Button variant="outline" size="sm">
            <ArrowLeftIcon class="h-4 w-4 mr-2" />
            Continue Shopping
          </Button>
        </router-link>
        <h1 class="text-2xl font-bold">Shopping Cart ({{ cartItemCount }} items)</h1>
      </div>

      <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
        <!-- Cart Items -->
        <div class="lg:col-span-2 space-y-4">
          <div class="flex justify-between items-center">
            <h2 class="text-lg font-medium">Cart Items</h2>
            <Button
              variant="outline"
              size="sm"
              @click="clearCart"
              class="text-destructive hover:text-destructive"
            >
              Clear Cart
            </Button>
          </div>

          <div class="space-y-4">
            <div
              v-for="item in cartItems"
              :key="item.product.id"
              class="border rounded-lg overflow-hidden"
            >
              <div class="p-4">
                <div class="flex gap-4">
                  <div class="w-24 h-24 rounded-md overflow-hidden bg-muted flex-shrink-0">
                    <img
                      :src="item.product.image"
                      :alt="item.product.name"
                      class="w-full h-full object-cover"
                    />
                  </div>

                  <div class="flex-1 min-w-0">
                    <div class="flex justify-between items-start">
                      <div>
                        <h3 class="font-medium line-clamp-2">{{ item.product.name }}</h3>
                        <p class="text-sm text-muted-foreground">{{ item.product.category }}</p>
                        <p class="text-lg font-semibold mt-1">${{ item.product.price.toFixed(2) }}</p>
                      </div>

                      <Button
                        variant="ghost"
                        size="sm"
                        class="text-destructive hover:text-destructive"
                        @click="removeItem(item.product.id)"
                      >
                        <TrashIcon class="h-4 w-4" />
                      </Button>
                    </div>

                    <div class="flex items-center justify-between mt-4">
                      <div class="flex items-center border rounded-md">
                        <Button
                          variant="ghost"
                          size="sm"
                          class="h-8 w-8 p-0"
                          :disabled="item.quantity <= 1"
                          @click="updateQuantity(item.product.id, item.quantity - 1)"
                        >
                          <MinusIcon class="h-4 w-4" />
                        </Button>
                        <span class="w-8 text-center">{{ item.quantity }}</span>
                        <Button
                          variant="ghost"
                          size="sm"
                          class="h-8 w-8 p-0"
                          @click="updateQuantity(item.product.id, item.quantity + 1)"
                        >
                          <PlusIcon class="h-4 w-4" />
                        </Button>
                      </div>

                      <p class="text-lg font-semibold">
                        ${{ (item.product.price * item.quantity).toFixed(2) }}
                      </p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Order Summary -->
        <div class="space-y-6">
          <Card>
            <CardHeader>
              <CardTitle>Order Summary</CardTitle>
            </CardHeader>
            <CardContent class="space-y-4">
              <div class="space-y-2">
                <div class="flex justify-between">
                  <span class="text-muted-foreground">Subtotal</span>
                  <span>${{ cartTotal.toFixed(2) }}</span>
                </div>
                <div class="flex justify-between">
                  <span class="text-muted-foreground">Shipping</span>
                  <span>{{ shippingCost === 0 ? 'Free' : `$${shippingCost.toFixed(2)}` }}</span>
                </div>
                <div class="flex justify-between">
                  <span class="text-muted-foreground">Tax</span>
                  <span>${{ tax.toFixed(2) }}</span>
                </div>
                <Separator class="my-2" />
                <div class="flex justify-between font-medium text-lg">
                  <span>Total</span>
                  <span>${{ (cartTotal + shippingCost + tax).toFixed(2) }}</span>
                </div>
              </div>

              <Button
                size="lg"
                class="w-full"
                @click="proceedToCheckout"
              >
                Proceed to Checkout
              </Button>

              <div class="text-center text-sm text-muted-foreground">
                or
                <router-link to="/products" class="text-primary hover:underline">
                  Continue Shopping
                </router-link>
              </div>

              <div class="pt-4 border-t">
                <h3 class="font-medium mb-3">We Accept</h3>
                <div class="flex justify-center gap-4">
                  <CreditCardIcon class="h-8 w-12 text-muted-foreground" />
                  <CreditCardIcon class="h-8 w-12 text-muted-foreground" />
                  <CreditCardIcon class="h-8 w-12 text-muted-foreground" />
                </div>
              </div>
            </CardContent>
          </Card>

          <Card>
            <CardHeader class="pb-3">
              <CardTitle class="text-sm font-medium">Apply Promo Code</CardTitle>
            </CardHeader>
            <CardContent>
              <div class="flex gap-2">
                <Input placeholder="Enter promo code" class="flex-1" />
                <Button variant="outline">Apply</Button>
              </div>
            </CardContent>
          </Card>
        </div>
      </div>

      <!-- Recommended Products -->
      <section class="mt-16">
        <h2 class="text-xl font-bold mb-6">You Might Also Like</h2>
        <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
          <ProductCard
            v-for="product in recommendedProducts"
            :key="product.id"
            :product="product"
            @add-to-cart="addToCart"
          />
        </div>
      </section>
    </div>

    <!-- Empty Cart State -->
    <div v-else class="text-center py-16">
      <ShoppingCartIcon class="h-24 w-24 text-muted-foreground mx-auto mb-6" />
      <h1 class="text-2xl font-bold mb-2">Your Cart is Empty</h1>
      <p class="text-muted-foreground mb-8 max-w-md mx-auto">
        Looks like you haven't added any items to your cart yet.
      </p>
      <router-link to="/products">
        <Button size="lg">
          Start Shopping
        </Button>
      </router-link>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import {
  ShoppingCartIcon,
  ArrowLeftIcon,
  TrashIcon,
  PlusIcon,
  MinusIcon,
  CreditCardIcon
} from '@heroicons/vue/24/outline';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Input } from '@/components/ui/input';
import { Separator } from '@/components/ui/separator';
import ProductCard from '@/pages/customer/products/ProductCard.vue';
import { useCartStore } from '@/stores/cart.ts';
import type { Product } from '@/types/product.ts';

const router = useRouter();
const cartStore = useCartStore();

// Cart state from store
const cartItems = computed(() => cartStore.items);
const cartTotal = computed(() => cartStore.total);
const cartItemCount = computed(() => cartStore.itemCount);

// Calculate shipping (free over $50)
const shippingCost = computed(() => cartTotal.value > 50 ? 0 : 9.99);

// Calculate tax (8% of subtotal)
const tax = computed(() => cartTotal.value * 0.08);

// Mock recommended products - in a real app, these would come from an API
const recommendedProducts = ref<Product[]>([]);

// Load recommended products
const loadRecommendedProducts = () => {
  // In a real app, this would be an API call
  recommendedProducts.value = Array(4).fill(0).map((_, i) => ({
    id: `rec-${i}`,
    name: `Recommended Product ${i + 1}`,
    description: 'High-quality product that pairs well with your cart items',
    price: Math.floor(Math.random() * 200) + 50,
    image: `https://picsum.photos/seed/rec-${i}/500/500`,
    category: ['Electronics', 'Home', 'Fashion', 'Accessories'][Math.floor(Math.random() * 4)],
    rating: Math.floor(Math.random() * 2) + 3.5, // 3.5 - 5.5
    reviewCount: Math.floor(Math.random() * 100),
    inStock: Math.random() > 0.2,
    badge: Math.random() > 0.7 ? 'Sale' : Math.random() > 0.7 ? 'New' : undefined
  }));
};

// Cart actions
const updateQuantity = (productId: string, newQuantity: number) => {
  if (newQuantity < 1) return;
  cartStore.updateQuantity(productId, newQuantity);
};

const removeItem = (productId: string) => {
  cartStore.removeFromCart(productId);
  // In a real app, you might want to show a toast notification here
  console.log('Item removed from cart');
};

const clearCart = () => {
  cartStore.clearCart();
  // In a real app, you might want to show a toast notification here
  console.log('Cart cleared');
};

const proceedToCheckout = () => {
  router.push('/checkout');
};

const addToCart = (product: Product) => {
  cartStore.addToCart(product);
  // In a real app, you might want to show a toast notification here
  console.log(`${product.name} added to cart`);
};

// Load recommended products when component mounts
onMounted(() => {
  loadRecommendedProducts();
});
</script>
