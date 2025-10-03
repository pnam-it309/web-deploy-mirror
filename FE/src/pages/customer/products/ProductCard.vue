<template>
  <div class="group cursor-pointer overflow-hidden rounded-lg border border-gray-200 hover:shadow-lg transition-shadow">
    <div class="aspect-square relative overflow-hidden bg-gray-100">
      <img
        :src="product.image"
        :alt="product.name"
        class="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
      />
      <Badge
        v-if="product.badge"
        :variant="getBadgeVariant(product.badge)"
        class="absolute top-3 left-3"
      >
        {{ product.badge }}
      </Badge>
      <div class="absolute inset-0 bg-black bg-opacity-0 group-hover:bg-opacity-10 transition-all duration-300" />

      <Button
        variant="secondary"
        size="sm"
        class="absolute bottom-3 left-1/2 -translate-x-1/2 opacity-0 group-hover:opacity-100 transition-opacity duration-200"
        @click.stop="$emit('add-to-cart', product)"
      >
        <ShoppingCartIcon class="h-4 w-4 mr-2" />
        Add to Cart
      </Button>
    </div>

    <div class="p-4">
      <div class="flex justify-between items-start mb-2">
        <h3 class="font-medium text-gray-900">{{ product.name }}</h3>
        <p class="text-lg font-semibold text-gray-900">${{ product.price.toFixed(2) }}</p>
      </div>

      <div class="flex items-center mb-2">
        <div class="flex">
          <StarIcon
            v-for="i in 5"
            :key="i"
            class="h-4 w-4"
            :class="{
              'text-yellow-400 fill-yellow-400': i <= Math.floor(product.rating),
              'text-gray-300': i > Math.floor(product.rating)
            }"
          />
        </div>
        <span class="text-xs text-gray-500 ml-1">({{ product.reviewCount || 0 }})</span>
      </div>

      <p class="text-sm text-gray-500 mb-3 line-clamp-2">{{ product.description }}</p>

      <div class="flex justify-between items-center">
        <span
          class="inline-flex items-center text-sm"
          :class="product.inStock ? 'text-green-600' : 'text-red-600'"
        >
          <span class="w-2 h-2 rounded-full mr-1" :class="product.inStock ? 'bg-green-500' : 'bg-red-500'"></span>
          {{ product.inStock ? 'In Stock' : 'Out of Stock' }}
        </span>

        <Button
          variant="ghost"
          size="sm"
          class="text-primary hover:bg-primary/10"
          @click.stop="$router.push(`/products/${product.id}`)"
        >
          View Details
        </Button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { StarIcon, ShoppingCartIcon } from '@heroicons/vue/24/solid';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import type { PropType } from 'vue';
import type { Product } from '@/types/product.ts';

defineProps({
  product: {
    type: Object as PropType<Product>,
    required: true
  }
});

defineEmits(['add-to-cart']);

const getBadgeVariant = (badge: string) => {
  switch (badge) {
    case 'Sale': return 'destructive';
    case 'New': return 'default';
    case 'Limited': return 'secondary';
    default: return 'default';
  }
};
</script>
