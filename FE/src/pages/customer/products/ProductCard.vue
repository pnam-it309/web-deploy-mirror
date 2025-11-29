<template>
  <div
    class="group cursor-pointer overflow-hidden rounded-lg border border-gray-200 bg-white hover:shadow-lg transition-shadow">

    <div class="aspect-square relative overflow-hidden bg-gray-100"
      @click="$router.push({ name: 'customer-product-detail', params: { id: product.id } })">

      <img :src="product.image" :alt="product.name"
        class="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300" />

      <Badge v-if="product.badge" :variant="getBadgeVariant(product.badge)" class="absolute top-3 left-3">
        {{ product.badge }}
      </Badge>

      <div class="absolute inset-0 bg-black bg-opacity-0 group-hover:bg-opacity-10 transition-all duration-300" />

      <button
        class="absolute top-3 right-3 p-2 rounded-full bg-white shadow-md opacity-0 group-hover:opacity-100 transition-opacity duration-200 hover:bg-red-50"
        @click.stop="$emit('toggle-wishlist', product)">
        <HeartIcon class="h-5 w-5" :class="isInWishlist ? 'text-red-500 fill-red-500' : 'text-gray-400'" />
      </button>

    </div>

    <div class="p-4">
      <div class="flex justify-between items-start mb-2"
        @click="$router.push({ name: 'customer-product-detail', params: { id: product.id } })">
        <h3 class="font-medium text-gray-900 line-clamp-1">{{ product.name }}</h3>
        <p class="text-lg font-semibold text-indigo-600">${{ product.price.toFixed(2) }}</p>
      </div>

      <div class="flex items-center mb-2"
        @click="$router.push({ name: 'customer-product-detail', params: { id: product.id } })">
        <div class="flex">
          <StarIcon v-for="i in 5" :key="i" class="h-4 w-4" :class="{
            'text-yellow-400 fill-yellow-400': i <= Math.floor(product.rating),
            'text-gray-300': i > Math.floor(product.rating)
          }" />
        </div>
        <span class="text-xs text-gray-500 ml-1">({{ product.reviewCount || 0 }})</span>
      </div>

      <p class="text-sm text-gray-500 mb-3 line-clamp-2"
        @click="$router.push({ name: 'customer-product-detail', params: { id: product.id } })">
        {{ product.description }}
      </p>

      <div class="flex justify-between items-center pt-2 border-t border-gray-100">

        <Button variant="secondary" size="xs" class="flex-1 max-w-[45%] mr-1 text-xs py-1"
          :class="product.inStock ? 'bg-indigo-600 text-white hover:bg-indigo-700' : 'bg-gray-300 text-gray-500 cursor-not-allowed hover:bg-gray-300'"
          :disabled="!product.inStock" @click.stop="$emit('add-to-cart', product)">
          Yêu cầu
        </Button>

        <Button variant="outline" size="xs"
          class="flex-1 max-w-[45%] text-indigo-600 border-indigo-600 hover:bg-indigo-50 hover:border-indigo-700 text-xs py-1"
          @click.stop="$router.push({ name: 'customer-product-detail', params: { id: product.id } })">
          View Details
        </Button>

        <span class="inline-flex items-center text-xs ml-2 pl-2 whitespace-nowrap"
          :class="product.inStock ? 'text-green-600' : 'text-red-600'">
          <span class="w-2 h-2 rounded-full mr-1" :class="product.inStock ? 'bg-green-500' : 'bg-red-500'"></span>
          {{ product.inStock ? 'In Stock' : 'Out of Stock' }}
        </span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { StarIcon, ShoppingCartIcon, HeartIcon } from '@heroicons/vue/24/solid';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import type { PropType } from 'vue';
import type { Product } from '@/types/product.ts';

defineProps({
  product: {
    type: Object as PropType<Product>,
    required: true
  },
  isInWishlist: {
    type: Boolean,
    default: false
  }
});

defineEmits(['add-to-cart', 'toggle-wishlist']);

const getBadgeVariant = (badge: string) => {
  switch (badge) {
    case 'Sale': return 'destructive';
    case 'New': return 'default';
    case 'Limited': return 'secondary';
    default: return 'default';
  }
};
</script>
