<template>
  <div class="product-detail">
    <div class="container mx-auto px-4 py-8">
      <!-- Breadcrumb -->
      <nav class="flex mb-6" aria-label="Breadcrumb">
        <ol class="inline-flex items-center space-x-1 md:space-x-2 rtl:space-x-reverse">
          <li class="inline-flex items-center">
            <router-link to="/" class="inline-flex items-center text-sm font-medium text-gray-700 hover:text-indigo-600">
              <svg class="w-3 h-3 me-2.5" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 20 20">
                <path d="m19.707 9.293-2-2-7-7a1 1 0 0 0-1.414 0l-7 7-2 2a1 1 0 0 0 1.414 1.414L3 11.414V18a2 2 0 0 0 2 2h10a2 2 0 0 0 2-2v-6.586l.293.293a1 1 0 0 0 1.414-1.414Z"/>
              </svg>
              Home
            </router-link>
          </li>
          <li>
            <div class="flex items-center">
              <svg class="rtl:rotate-180 w-3 h-3 text-gray-400 mx-1" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 6 10">
                <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="m1 9 4-4-4-4"/>
              </svg>
              <router-link to="/catalog" class="ms-1 text-sm font-medium text-gray-700 hover:text-indigo-600 md:ms-2">Catalog</router-link>
            </div>
          </li>
          <li aria-current="page">
            <div class="flex items-center">
              <svg class="rtl:rotate-180 w-3 h-3 text-gray-400 mx-1" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 6 10">
                <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="m1 9 4-4-4-4"/>
              </svg>
              <span class="ms-1 text-sm font-medium text-gray-500 md:ms-2">{{ product.name }}</span>
            </div>
          </li>
        </ol>
      </nav>

      <!-- Product Section -->
      <div class="lg:grid lg:grid-cols-2 lg:gap-8">
        <!-- Product Images -->
        <div class="mb-8 lg:mb-0">
          <div class="mb-4">
            <img 
              :src="currentImage || product.images[0]" 
              :alt="product.name"
              class="w-full h-96 object-contain rounded-lg border border-gray-200"
            >
          </div>
          <div class="grid grid-cols-4 gap-2">
            <button 
              v-for="(image, index) in product.images" 
              :key="index"
              @click="currentImage = image"
              :class="[
                'p-1 border rounded-md hover:border-indigo-500 transition-colors',
                currentImage === image ? 'border-indigo-500' : 'border-gray-200'
              ]"
            >
              <img 
                :src="image" 
                :alt="`${product.name} thumbnail ${index + 1}`"
                class="w-full h-20 object-cover rounded"
              >
            </button>
          </div>
        </div>

        <!-- Product Info -->
        <div>
          <h1 class="text-3xl font-bold text-gray-900 mb-2">{{ product.name }}</h1>
          
          <div class="flex items-center mb-4">
            <div class="flex items-center">
              <div class="flex items-center">
                <svg 
                  v-for="i in 5" 
                  :key="i"
                  :class="[
                    'h-5 w-5',
                    i <= Math.round(product.rating) ? 'text-yellow-400' : 'text-gray-300'
                  ]"
                  fill="currentColor"
                  viewBox="0 0 20 20"
                >
                  <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
                </svg>
              </div>
              <span class="text-gray-600 ml-2 text-sm">({{ product.reviewCount }} reviews)</span>
            </div>
            <span class="mx-2 text-gray-300">|</span>
            <span 
              v-if="product.inStock" 
              class="text-green-600 text-sm font-medium"
            >
              In Stock
            </span>
            <span 
              v-else 
              class="text-red-600 text-sm font-medium"
            >
              Out of Stock
            </span>
          </div>

          <div class="mb-6">
            <p class="text-3xl font-bold text-gray-900">${{ product.price.toFixed(2) }}</p>
            <p class="text-sm text-gray-500">+ ${{ product.shipping }} shipping</p>
          </div>

          <div class="mb-6">
            <h3 class="text-sm font-medium text-gray-900">Description</h3>
            <p class="mt-2 text-gray-600">{{ product.description }}</p>
          </div>

          <!-- Color Picker -->
          <div class="mb-6">
            <h3 class="text-sm font-medium text-gray-900">Color</h3>
            <div class="mt-2 flex space-x-2">
              <button 
                v-for="color in product.colors" 
                :key="color.id"
                @click="selectedColor = color"
                :class="[
                  'w-8 h-8 rounded-full border-2',
                  selectedColor.id === color.id ? 'border-indigo-600' : 'border-gray-200',
                ]"
                :style="{ backgroundColor: color.hex }"
                :title="color.name"
              >
                <span class="sr-only">{{ color.name }}</span>
              </button>
            </div>
          </div>

          <!-- Size Picker -->
          <div class="mb-6">
            <div class="flex items-center justify-between">
              <h3 class="text-sm font-medium text-gray-900">Size</h3>
              <button 
                @click="isSizeGuideOpen = true"
                class="text-sm font-medium text-indigo-600 hover:text-indigo-500"
              >
                Size guide
              </button>
            </div>
            <div class="mt-2 grid grid-cols-5 gap-2">
              <button 
                v-for="size in product.sizes" 
                :key="size"
                @click="selectedSize = size"
                :class="[
                  'py-2 px-3 border rounded-md text-sm font-medium',
                  selectedSize === size 
                    ? 'bg-indigo-600 text-white border-transparent' 
                    : 'bg-white text-gray-900 border-gray-300 hover:bg-gray-50',
                  !product.availableSizes.includes(size) ? 'opacity-50 cursor-not-allowed' : ''
                ]"
                :disabled="!product.availableSizes.includes(size)"
              >
                {{ size }}
              </button>
            </div>
          </div>

          <!-- Quantity Selector -->
          <div class="mb-6">
            <h3 class="text-sm font-medium text-gray-900 mb-2">Quantity</h3>
            <div class="flex items-center">
              <button 
                @click="quantity > 1 ? quantity-- : null"
                :class="[
                  'p-2 border border-gray-300 rounded-l-md',
                  quantity === 1 ? 'text-gray-400' : 'text-gray-700 hover:bg-gray-50'
                ]"
                :disabled="quantity === 1"
              >
                <span class="sr-only">Decrease quantity</span>
                <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 12H4" />
                </svg>
              </button>
              <input 
                v-model.number="quantity" 
                type="number" 
                min="1" 
                :max="product.stock"
                class="w-16 text-center border-t border-b border-gray-300 py-2 px-2 focus:outline-none focus:ring-1 focus:ring-indigo-500"
              >
              <button 
                @click="quantity < product.stock ? quantity++ : null"
                :class="[
                  'p-2 border border-gray-300 rounded-r-md',
                  quantity === product.stock ? 'text-gray-400' : 'text-gray-700 hover:bg-gray-50'
                ]"
                :disabled="quantity === product.stock"
              >
                <span class="sr-only">Increase quantity</span>
                <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
                </svg>
              </button>
              <span class="ml-2 text-sm text-gray-500">{{ product.stock }} available</span>
            </div>
          </div>

          <!-- Action Buttons -->
          <div class="flex space-x-4">
            <button
              @click="addToCart"
              :disabled="!product.inStock || !selectedSize"
              class="flex-1 bg-indigo-600 border border-transparent rounded-md py-3 px-8 flex items-center justify-center text-base font-medium text-white hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              <svg class="h-5 w-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z" />
              </svg>
              Add to cart
            </button>
            <button
              @click="addToWishlist"
              class="p-3 border border-gray-300 rounded-md text-gray-400 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
              :class="{ 'text-red-500': isInWishlist }"
              :title="isInWishlist ? 'Remove from wishlist' : 'Add to wishlist'"
            >
              <span class="sr-only">Add to wishlist</span>
              <svg 
                class="h-6 w-6" 
                fill="currentColor" 
                viewBox="0 0 20 20"
                :class="{ 'fill-current': isInWishlist }"
              >
                <path fill-rule="evenodd" d="M3.172 5.172a4 4 0 015.656 0L10 6.343l1.172-1.171a4 4 0 115.656 5.656L10 17.657l-6.828-6.829a4 4 0 010-5.656z" clip-rule="evenodd" />
              </svg>
            </button>
          </div>

          <!-- Shipping Info -->
          <div class="mt-8 border-t border-gray-200 pt-6">
            <h3 class="text-sm font-medium text-gray-900">Shipping & Returns</h3>
            <div class="mt-4">
              <div class="flex items-start">
                <div class="flex-shrink-0">
                  <svg class="h-5 w-5 text-green-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
                  </svg>
                </div>
                <p class="ml-3 text-sm text-gray-500">
                  Free shipping on orders over $50
                </p>
              </div>
              <div class="mt-2 flex items-start">
                <div class="flex-shrink-0">
                  <svg class="h-5 w-5 text-green-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
                  </svg>
                </div>
                <p class="ml-3 text-sm text-gray-500">
                  Easy 30-day return policy
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Product Tabs -->
      <div class="mt-16">
        <div class="border-b border-gray-200">
          <nav class="-mb-px flex space-x-8">
            <button
              v-for="tab in tabs"
              :key="tab.name"
              @click="currentTab = tab.name"
              :class="[
                'whitespace-nowrap py-4 px-1 border-b-2 font-medium text-sm',
                currentTab === tab.name
                  ? 'border-indigo-500 text-indigo-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
              ]"
            >
              {{ tab.name }}
            </button>
          </nav>
        </div>

        <!-- Tab Panels -->
        <div class="py-6">
          <!-- Description Panel -->
          <div v-if="currentTab === 'Description'">
            <div class="prose max-w-none">
              <h3>Product Details</h3>
              <p>{{ product.fullDescription }}</p>
              
              <h3 class="mt-6">Features</h3>
              <ul>
                <li v-for="(feature, index) in product.features" :key="index">
                  {{ feature }}
                </li>
              </ul>
            </div>
          </div>

          <!-- Specifications Panel -->
          <div v-else-if="currentTab === 'Specifications'" class="space-y-6">
            <div>
              <h3 class="text-lg font-medium text-gray-900">Technical Specifications</h3>
              <div class="mt-4 border-t border-gray-200">
                <dl class="divide-y divide-gray-200">
                  <div 
                    v-for="(value, key) in product.specifications" 
                    :key="key"
                    class="py-4 sm:grid sm:grid-cols-3 sm:gap-4"
                  >
                    <dt class="text-sm font-medium text-gray-500">{{ key }}</dt>
                    <dd class="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                      {{ value }}
                    </dd>
                  </div>
                </dl>
              </div>
            </div>
          </div>

          <!-- Reviews Panel -->
          <div v-else-if="currentTab === 'Reviews'" class="space-y-8">
            <div>
              <h3 class="text-lg font-medium text-gray-900">Customer Reviews</h3>
              <div class="mt-4 flex items-center">
                <div class="flex items-center">
                  <div class="flex items-center">
                    <svg 
                      v-for="i in 5" 
                      :key="i"
                      :class="[
                        'h-5 w-5',
                        i <= Math.round(product.rating) ? 'text-yellow-400' : 'text-gray-300'
                      ]"
                      fill="currentColor"
                      viewBox="0 0 20 20"
                    >
                      <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
                    </svg>
                  </div>
                  <p class="ml-2 text-sm text-gray-600">
                    {{ product.rating }} out of 5
                  </p>
                </div>
                <p class="ml-4 text-sm text-gray-500">
                  {{ product.reviewCount }} reviews
                </p>
              </div>

              <div class="mt-6">
                <button
                  @click="isReviewModalOpen = true"
                  class="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                >
                  Write a review
                </button>
              </div>
            </div>

            <div class="mt-8 space-y-8">
              <div 
                v-for="review in product.reviews" 
                :key="review.id"
                class="border-b border-gray-200 pb-8"
              >
                <div class="flex items-center">
                  <div class="flex items-center">
                    <div class="flex items-center">
                      <svg 
                        v-for="i in 5" 
                        :key="i"
                        :class="[
                          'h-5 w-5',
                          i <= review.rating ? 'text-yellow-400' : 'text-gray-300'
                        ]"
                        fill="currentColor"
                        viewBox="0 0 20 20"
                      >
                        <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
                      </svg>
                    </div>
                  </div>
                  <div class="ml-4">
                    <h4 class="text-sm font-bold text-gray-900">{{ review.author }}</h4>
                    <div class="flex items-center text-sm text-gray-500">
                      <time :datetime="review.date">{{ formatDate(review.date) }}</time>
                      <span class="mx-1">•</span>
                      <span>{{ review.verified ? 'Verified Purchase' : 'Anonymous' }}</span>
                    </div>
                  </div>
                </div>
                <div class="mt-4 space-y-3">
                  <h4 class="text-sm font-medium text-gray-900">{{ review.title }}</h4>
                  <p class="text-sm text-gray-600">{{ review.content }}</p>
                </div>
                <div class="mt-4 flex items-center">
                  <button 
                    v-for="(helpful, index) in ['Helpful', 'Not helpful']" 
                    :key="index"
                    @click="toggleHelpful(review, index)"
                    class="mr-4 text-xs text-gray-500 hover:text-gray-700 flex items-center"
                  >
                    <svg 
                      class="h-4 w-4 mr-1" 
                      fill="none" 
                      stroke="currentColor" 
                      viewBox="0 0 24 24"
                      :class="{
                        'text-indigo-600': (index === 0 && review.userVote === 'helpful') || (index === 1 && review.userVote === 'not-helpful')
                      }"
                    >
                      <path 
                        v-if="index === 0"
                        stroke-linecap="round" 
                        stroke-linejoin="round" 
                        stroke-width="2" 
                        d="M14 10h4.764a2 2 0 011.789 2.894l-3.5 7A2 2 0 0115.263 21h-4.017c-.163 0-.326-.02-.485-.06L7 20m7-10V5a2 2 0 00-2-2h-.095c-.5 0-.905.405-.905.905 0 .714-.211 1.412-.608 2.006L7 11v9m7-10h-2M7 20H5a2 2 0 01-2-2v-6a2 2 0 012-2h2.5" 
                      />
                      <path 
                        v-else
                        stroke-linecap="round" 
                        stroke-linejoin="round" 
                        stroke-width="2" 
                        d="M10 14H5.236a2 2 0 01-1.789-2.894l3.5-7A2 2 0 018.736 3h4.018a2 2 0 01.485.06l3.76.94m-7 10v5a2 2 0 002 2h.096c.5 0 .905-.405.905-.904 0-.715.211-1.413.608-2.008L17 13V4m-7 10h2m5-10h2a2 2 0 012 2v6a2 2 0 01-2 2h-2.5"
                      />
                    </svg>
                    {{ helpful }}
                    <span 
                      v-if="index === 0 && review.helpfulCount > 0"
                      class="ml-1 font-medium"
                    >
                      ({{ review.helpfulCount }})
                    </span>
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Related Products -->
      <div class="mt-16">
        <h2 class="text-2xl font-bold text-gray-900 mb-6">You may also like</h2>
        <div class="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
          <div 
            v-for="relatedProduct in relatedProducts" 
            :key="relatedProduct.id"
            class="group relative bg-white border border-gray-200 rounded-lg overflow-hidden shadow-sm hover:shadow-md transition-shadow duration-200"
          >
            <router-link 
              :to="{ name: 'product-detail', params: { id: relatedProduct.id } }" 
              class="block"
            >
              <div class="aspect-w-1 aspect-h-1 w-full overflow-hidden bg-gray-200">
                <img
                  :src="relatedProduct.image"
                  :alt="relatedProduct.name"
                  class="w-full h-48 object-cover object-center group-hover:opacity-75"
                >
              </div>
              <div class="p-4">
                <h3 class="text-sm font-medium text-gray-900 line-clamp-2 h-12">
                  {{ relatedProduct.name }}
                </h3>
                <div class="mt-1 flex items-center">
                  <div class="flex items-center">
                    <div class="flex items-center">
                      <svg 
                        v-for="i in 5" 
                        :key="i"
                        :class="[
                          'h-4 w-4',
                          i <= Math.round(relatedProduct.rating) ? 'text-yellow-400' : 'text-gray-300'
                        ]"
                        fill="currentColor"
                        viewBox="0 0 20 20"
                      >
                        <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
                      </svg>
                    </div>
                  </div>
                  <span class="ml-2 text-xs text-gray-500">({{ relatedProduct.reviewCount }})</span>
                </div>
                <div class="mt-2 flex items-center justify-between">
                  <p class="text-base font-semibold text-gray-900">${{ relatedProduct.price.toFixed(2) }}</p>
                  <span 
                    v-if="relatedProduct.inStock" 
                    class="inline-flex items-center px-2 py-0.5 rounded text-xs font-medium bg-green-100 text-green-800"
                  >
                    In Stock
                  </span>
                  <span 
                    v-else 
                    class="inline-flex items-center px-2 py-0.5 rounded text-xs font-medium bg-red-100 text-red-800"
                  >
                    Out of Stock
                  </span>
                </div>
              </div>
            </router-link>
          </div>
        </div>
      </div>
    </div>

    <!-- Size Guide Modal -->
    <div 
      v-if="isSizeGuideOpen" 
      class="fixed z-50 inset-0 overflow-y-auto"
      aria-labelledby="modal-title"
      role="dialog"
      aria-modal="true"
    >
      <div class="flex items-end justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
        <div 
          class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity" 
          aria-hidden="true"
          @click="isSizeGuideOpen = false"
        ></div>

        <span class="hidden sm:inline-block sm:align-middle sm:h-screen" aria-hidden="true">&#8203;</span>

        <div class="inline-block align-bottom bg-white rounded-lg px-4 pt-5 pb-4 text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-2xl sm:w-full sm:p-6">
          <div>
            <div class="mt-3 text-center sm:mt-0 sm:text-left">
              <h3 class="text-lg leading-6 font-medium text-gray-900" id="modal-title">
                Size Guide
              </h3>
              <div class="mt-4">
                <div class="overflow-x-auto">
                  <table class="min-w-full divide-y divide-gray-200">
                    <thead class="bg-gray-50">
                      <tr>
                        <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                          Size
                        </th>
                        <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                          Chest (in)
                        </th>
                        <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                          Waist (in)
                        </th>
                        <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                          Hips (in)
                        </th>
                      </tr>
                    </thead>
                    <tbody class="bg-white divide-y divide-gray-200">
                      <tr v-for="(size, index) in sizeGuide" :key="index">
                        <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                          {{ size.size }}
                        </td>
                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                          {{ size.chest }}"
                        </td>
                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                          {{ size.waist }}"
                        </td>
                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                          {{ size.hips }}"
                        </td>
                      </tr>
                    </tbody>
                  </table>
                </div>
                <div class="mt-4 text-sm text-gray-500">
                  <p>For best results, use a tape measure to find your size. If you're between sizes, we recommend sizing up.</p>
                </div>
              </div>
            </div>
          </div>
          <div class="mt-5 sm:mt-6">
            <button
              type="button"
              class="inline-flex justify-center w-full rounded-md border border-transparent shadow-sm px-4 py-2 bg-indigo-600 text-base font-medium text-white hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:text-sm"
              @click="isSizeGuideOpen = false"
            >
              Close
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Review Modal -->
    <div 
      v-if="isReviewModalOpen" 
      class="fixed z-50 inset-0 overflow-y-auto"
      aria-labelledby="modal-title"
      role="dialog"
      aria-modal="true"
    >
      <div class="flex items-center justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
        <div 
          class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity" 
          aria-hidden="true"
          @click="isReviewModalOpen = false"
        ></div>

        <span class="hidden sm:inline-block sm:align-middle sm:h-screen" aria-hidden="true">&#8203;</span>

        <div class="inline-block align-bottom bg-white rounded-lg px-4 pt-5 pb-4 text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-lg sm:w-full sm:p-6">
          <div>
            <div class="mt-3 text-center sm:mt-0 sm:text-left">
              <h3 class="text-lg leading-6 font-medium text-gray-900" id="modal-title">
                Write a review
              </h3>
              <div class="mt-4 space-y-4">
                <div>
                  <label for="rating" class="block text-sm font-medium text-gray-700">Rating</label>
                  <div class="mt-1 flex items-center">
                    <div class="flex items-center">
                      <button 
                        v-for="i in 5" 
                        :key="i"
                        @click="newReview.rating = i"
                        class="p-1"
                      >
                        <svg 
                          :class="[
                            'h-8 w-8',
                            i <= newReview.rating ? 'text-yellow-400' : 'text-gray-300'
                          ]"
                          fill="currentColor"
                          viewBox="0 0 20 20"
                        >
                          <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
                        </svg>
                      </button>
                    </div>
                    <span class="ml-2 text-sm text-gray-500">{{ newReview.rating }} out of 5</span>
                  </div>
                </div>
                
                <div>
                  <label for="title" class="block text-sm font-medium text-gray-700">Title</label>
                  <input 
                    type="text" 
                    id="title" 
                    v-model="newReview.title"
                    class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                    placeholder="What's most important to know?"
                  >
                </div>
                
                <div>
                  <label for="review" class="block text-sm font-medium text-gray-700">Review</label>
                  <div class="mt-1">
                    <textarea 
                      id="review" 
                      v-model="newReview.content"
                      rows="4" 
                      class="shadow-sm focus:ring-indigo-500 focus:border-indigo-500 block w-full sm:text-sm border border-gray-300 rounded-md"
                      placeholder="What did you like or dislike? What did you use this product for?"
                    ></textarea>
                  </div>
                </div>
                
                <div class="flex items-center">
                  <input 
                    id="recommend" 
                    v-model="newReview.wouldRecommend"
                    type="checkbox" 
                    class="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded"
                  >
                  <label for="recommend" class="ml-2 block text-sm text-gray-700">
                    I recommend this product
                  </label>
                </div>
                
                <div class="text-xs text-gray-500">
                  <p>By submitting a review, you agree to our <a href="#" class="text-indigo-600 hover:text-indigo-500">Terms of Use</a> and <a href="#" class="text-indigo-600 hover:text-indigo-500">Privacy Policy</a>.</p>
                </div>
              </div>
            </div>
          </div>
          <div class="mt-5 sm:mt-6 sm:grid sm:grid-cols-2 sm:gap-3 sm:grid-flow-row-dense">
            <button
              type="button"
              class="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-indigo-600 text-base font-medium text-white hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:col-start-2 sm:text-sm"
              @click="submitReview"
            >
              Submit review
            </button>
            <button
              type="button"
              class="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:mt-0 sm:col-start-1 sm:text-sm"
              @click="isReviewModalOpen = false"
            >
              Cancel
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { useCartStore } from '@/stores/cart';
import { useWishlistStore } from '@/stores/wishlist';

const route = useRoute();
const cartStore = useCartStore();
const wishlistStore = useWishlistStore();

// Mock product data - replace with actual API call
const product = ref({
  id: 1,
  name: 'Premium Cotton T-Shirt',
  price: 29.99,
  shipping: 4.99,
  description: 'A comfortable and stylish cotton t-shirt perfect for everyday wear.',
  fullDescription: 'Our premium cotton t-shirt is made from 100% organic cotton for maximum comfort. The relaxed fit and breathable fabric make it perfect for any occasion. Machine washable and pre-shrunk for long-lasting wear.',
  rating: 4.5,
  reviewCount: 42,
  inStock: true,
  stock: 15,
  images: [
    'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=800&q=80',
    'https://images.unsplash.com/photo-1523381210434-271e8be1f52b?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=800&q=80',
    'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=800&q=80',
    'https://images.unsplash.com/photo-1523381210434-271e8be1f52b?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=800&q=80'
  ],
  colors: [
    { id: 1, name: 'Black', hex: '#1F2937' },
    { id: 2, name: 'White', hex: '#F3F4F6' },
    { id: 3, name: 'Navy', hex: '#1E3A8A' },
    { id: 4, name: 'Gray', hex: '#6B7280' }
  ],
  sizes: ['XS', 'S', 'M', 'L', 'XL', 'XXL', '3XL'],
  availableSizes: ['S', 'M', 'L', 'XL'],
  features: [
    '100% organic cotton',
    'Pre-shrunk fabric',
    'Reinforced stitching',
    'Breathable material',
    'Machine washable',
    'Relaxed fit'
  ],
  specifications: {
    'Material': '100% Organic Cotton',
    'Fit': 'Regular',
    'Neckline': 'Crew Neck',
    'Sleeve Type': 'Short Sleeve',
    'Pattern': 'Solid',
    'Care Instructions': 'Machine wash cold, tumble dry low',
    'Origin': 'Made in USA',
    'SKU': 'TSHIRT-001'
  },
  reviews: [
    {
      id: 1,
      author: 'John D.',
      rating: 5,
      title: 'Perfect fit and very comfortable',
      content: 'I love this t-shirt! The material is soft and comfortable, and the fit is perfect. I will definitely be buying more colors.',
      date: '2023-04-15',
      verified: true,
      helpfulCount: 8,
      userVote: null
    },
    {
      id: 2,
      author: 'Sarah M.',
      rating: 4,
      title: 'Great quality, runs slightly large',
      content: 'The shirt is well-made and the fabric feels great. I usually wear a medium, but I should have gone with a small as it runs a bit large. Still very happy with the purchase!',
      date: '2023-04-10',
      verified: true,
      helpfulCount: 3,
      userVote: null
    },
    {
      id: 3,
      author: 'Michael T.',
      rating: 5,
      title: 'Worth every penny',
      content: 'I was hesitant about the price at first, but after receiving it, I can say it was worth every penny. The quality is outstanding and it fits perfectly.',
      date: '2023-04-05',
      verified: true,
      helpfulCount: 12,
      userVote: 'helpful'
    }
  ]
});

// Related products
const relatedProducts = ref([
  {
    id: 2,
    name: 'Classic White T-Shirt',
    price: 24.99,
    image: 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=800&q=80',
    rating: 4.2,
    reviewCount: 18,
    inStock: true
  },
  {
    id: 3,
    name: 'Premium V-Neck T-Shirt',
    price: 27.99,
    image: 'https://images.unsplash.com/photo-1527719327859-c6ce80353573?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=800&q=80',
    rating: 4.5,
    reviewCount: 24,
    inStock: true
  },
  {
    id: 4,
    name: 'Slim Fit T-Shirt',
    price: 26.99,
    image: 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=800&q=80',
    rating: 4.0,
    reviewCount: 15,
    inStock: false
  },
  {
    id: 5,
    name: 'Long Sleeve T-Shirt',
    price: 34.99,
    image: 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=800&q=80',
    rating: 4.7,
    reviewCount: 32,
    inStock: true
  }
]);

// UI State
const currentImage = ref(product.value.images[0]);
const selectedColor = ref(product.value.colors[0]);
const selectedSize = ref('');
const quantity = ref(1);
const isSizeGuideOpen = ref(false);
const isReviewModalOpen = ref(false);

// Tabs
const tabs = [
  { name: 'Description', current: true },
  { name: 'Specifications', current: false },
  { name: 'Reviews', current: false }
];
const currentTab = ref('Description');

// New review
const newReview = ref({
  rating: 5,
  title: '',
  content: '',
  wouldRecommend: true
});

// Size guide
const sizeGuide = [
  { size: 'XS', chest: '32-34', waist: '26-28', hips: '34-36' },
  { size: 'S', chest: '35-37', waist: '28-30', hips: '37-39' },
  { size: 'M', chest: '38-40', waist: '31-33', hips: '40-42' },
  { size: 'L', chest: '41-43', waist: '34-36', hips: '43-45' },
  { size: 'XL', chest: '44-46', waist: '37-39', hips: '46-48' },
  { size: 'XXL', chest: '47-49', waist: '40-42', hips: '49-51' },
  { size: '3XL', chest: '50-52', waist: '43-45', hips: '52-54' }
];

// Computed
const isInWishlist = computed(() => {
  return wishlistStore.isInWishlist(product.value.id);
});

// Methods
const addToCart = () => {
  if (!selectedSize.value) {
    // Show error message or highlight size selection
    return;
  }
  
  cartStore.addToCart({
    id: product.value.id,
    name: product.value.name,
    price: product.value.price,
    quantity: quantity.value,
    image: product.value.images[0],
    color: selectedColor.value.name,
    size: selectedSize.value
  });
  
  // Show success message or notification
  console.log('Added to cart:', {
    product: product.value.name,
    quantity: quantity.value,
    size: selectedSize.value,
    color: selectedColor.value.name
  });
};

const addToWishlist = () => {
  if (isInWishlist.value) {
    wishlistStore.removeFromWishlist(product.value.id);
  } else {
    wishlistStore.addToWishlist({
      id: product.value.id,
      name: product.value.name,
      price: product.value.price,
      image: product.value.images[0]
    });
  }
};

const submitReview = () => {
  // In a real app, this would make an API call to submit the review
  const newReviewObj = {
    id: Date.now(),
    author: 'You',
    rating: newReview.value.rating,
    title: newReview.value.title,
    content: newReview.value.content,
    date: new Date().toISOString().split('T')[0],
    verified: true,
    helpfulCount: 0,
    userVote: null
  };
  
  product.value.reviews.unshift(newReviewObj);
  
  // Update average rating
  const totalRating = product.value.reviews.reduce((sum, review) => sum + review.rating, 0);
  product.value.rating = totalRating / product.value.reviews.length;
  product.value.reviewCount = product.value.reviews.length;
  
  // Reset form and close modal
  newReview.value = {
    rating: 5,
    title: '',
    content: '',
    wouldRecommend: true
  };
  
  isReviewModalOpen.value = false;
  
  // Switch to reviews tab
  currentTab.value = 'Reviews';
};

const toggleHelpful = (review: any, voteType: number) => {
  if (review.userVote === (voteType === 0 ? 'helpful' : 'not-helpful')) {
    // If clicking the same button again, remove the vote
    if (voteType === 0) {
      review.helpfulCount--;
    }
    review.userVote = null;
  } else {
    // If changing vote
    if (voteType === 0) {
      if (review.userVote === 'not-helpful') {
        review.helpfulCount++;
      } else if (!review.userVote) {
        review.helpfulCount++;
      }
      review.userVote = 'helpful';
    } else {
      if (review.userVote === 'helpful') {
        review.helpfulCount--;
      }
      review.userVote = 'not-helpful';
    }
  }
};

const formatDate = (dateString: string) => {
  const options: Intl.DateTimeFormatOptions = { year: 'numeric', month: 'long', day: 'numeric' };
  return new Date(dateString).toLocaleDateString(undefined, options);
};

// Fetch product data based on ID from route
onMounted(() => {
  const productId = Number(route.params.id);
  // In a real app, you would fetch the product data here
  // fetchProduct(productId);
  
  // For now, we'll just log the product ID
  console.log('Loading product:', productId);
});
</script>

<style scoped>
.product-detail {
  min-height: calc(100vh - 4rem);
  padding-top: 1.5rem;
  padding-bottom: 3rem;
}

.prose {
  color: #374151;
  max-width: 65ch;
  font-size: 1rem;
  line-height: 1.75;
}

.prose h3 {
  color: #111827;
  font-weight: 600;
  font-size: 1.125rem;
  margin-top: 1.5em;
  margin-bottom: 0.5em;
  line-height: 1.6;
}

.prose ul {
  list-style-type: disc;
  padding-left: 1.5em;
  margin-top: 1em;
  margin-bottom: 1em;
}

.prose li {
  margin-bottom: 0.5em;
}

.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
