<template>
    <div class="w-full bg-[#F5F2ED] py-20 px-4">
        <div class="max-w-full overflow-x-auto">
            <div class="text-center mb-12">
                <h2 class="text-4xl font-serif text-gray-900">Find Your Fit</h2>

                <!-- Navigation Arrows (Visual only for now) -->
                <div class="flex justify-end gap-3 px-8 mt-4">
                    <button class="p-2 rounded-full border border-gray-400 hover:bg-gray-200"><span
                            class="sr-only">Prev</span>&lt;</button>
                    <button class="p-2 rounded-full border border-gray-400 hover:bg-gray-200"><span
                            class="sr-only">Next</span>&gt;</button>
                </div>
            </div>

            <!-- Comparison Grid -->
            <div class="min-w-[1200px] mx-auto px-8 pb-12">
                <!-- Header Row: Product Images & Titles -->
                <div class="grid grid-cols-5 gap-8 mb-12">
                    <div v-for="(product, idx) in products" :key="idx" class="flex flex-col">
                        <div class="h-48 bg-transparent mb-6 flex items-end justify-center">
                            <img :src="product.image" :alt="product.name" class="max-h-full max-w-full object-contain">
                        </div>
                        <h3 class="text-2xl font-serif text-gray-900 mb-2">{{ product.name }}</h3>

                        <div v-if="product.badge"
                            class="self-start text-xs font-bold text-white px-2 py-1 uppercase tracking-wider mb-2"
                            :class="product.badgeColor || 'bg-gray-500'">
                            {{ product.badge }}
                        </div>

                        <p class="text-gray-600 text-sm leading-relaxed mb-4">{{ product.description }}</p>
                    </div>
                </div>

                <!-- Data Rows -->
                <div class="space-y-8 divide-y divide-gray-300">
                    <!-- Desk Type -->
                    <div class="pt-8">
                        <h4 class="text-xs font-bold uppercase tracking-widest text-gray-500 mb-4">Desk Type</h4>
                        <div class="grid grid-cols-5 gap-8">
                            <div v-for="(product, idx) in products" :key="idx" class="text-gray-900">{{
                                product.specs.type }}</div>
                        </div>
                    </div>

                    <!-- Starting At -->
                    <div class="pt-8">
                        <h4 class="text-xs font-bold uppercase tracking-widest text-gray-500 mb-4">Starting At</h4>
                        <div class="grid grid-cols-5 gap-8">
                            <div v-for="(product, idx) in products" :key="idx" class="text-gray-900 font-medium">{{
                                product.specs.price }}</div>
                        </div>
                    </div>

                    <!-- Ideal For -->
                    <div class="pt-8">
                        <h4 class="text-xs font-bold uppercase tracking-widest text-gray-500 mb-4">Ideal For</h4>
                        <div class="grid grid-cols-5 gap-8">
                            <div v-for="(product, idx) in products" :key="idx" class="text-gray-600 text-sm">{{
                                product.specs.idealFor }}</div>
                        </div>
                    </div>

                    <!-- Integrated Add-ons -->
                    <div class="pt-8">
                        <h4 class="text-xs font-bold uppercase tracking-widest text-gray-500 mb-4">Integrated Add-ons
                        </h4>
                        <div class="grid grid-cols-5 gap-8">
                            <div v-for="(product, idx) in products" :key="idx" class="text-gray-600 text-sm">{{
                                product.specs.addOns }}</div>
                        </div>
                    </div>

                    <!-- Height Range -->
                    <div class="pt-8">
                        <h4 class="text-xs font-bold uppercase tracking-widest text-gray-500 mb-4">Height Range</h4>
                        <div class="grid grid-cols-5 gap-8">
                            <div v-for="(product, idx) in products" :key="idx" class="text-gray-600 text-sm">{{
                                product.specs.heightRange }}</div>
                        </div>
                    </div>

                    <!-- Warranty -->
                    <div class="pt-8">
                        <h4 class="text-xs font-bold uppercase tracking-widest text-gray-500 mb-4">Warranty</h4>
                        <div class="grid grid-cols-5 gap-8">
                            <div v-for="(product, idx) in products" :key="idx" class="text-gray-600 text-sm">{{
                                product.specs.warranty }}</div>
                        </div>
                    </div>

                    <!-- Reviews / Quotes -->
                    <div class="pt-8">
                        <h4 class="text-xs font-bold uppercase tracking-widest text-gray-500 mb-4">Hear What Our
                            Customers Have To Say</h4>
                        <div class="grid grid-cols-5 gap-8">
                            <div v-for="(product, idx) in products" :key="idx" class="text-gray-500 text-sm italic">"{{
                                product.specs.quote }}"</div>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
const products = [
    {
        name: 'Duo Standing Desk',
        image: 'https://images.unsplash.com/photo-1595515106969-1ce29566ff1c?ixlib=rb-4.0.3&w=300&q=80',
        badge: 'Bestseller',
        badgeColor: 'bg-[#8FA893]',
        description: 'Our most popular adjustable desk for any space.',
        specs: {
            type: 'Sit-Stand',
            price: '$549',
            idealFor: 'Brings adjustable movement, intuitive organization and style to spaces of all sizes.',
            addOns: 'Cable Tray (with optional power), Desk Drawer, Desk Tray, Desk Hook',
            heightRange: '28"-47.3", Leveling Feet (1/4")',
            warranty: '10 Years',
            quote: 'It\'s clear that Branch designed every detail meticulously, with durability and functionality in mind.'
        }
    },
    {
        name: 'Standing Desk',
        image: 'https://images.unsplash.com/photo-1497215728101-856f4ea42174?ixlib=rb-4.0.3&w=300&q=80',
        badge: 'Max Range',
        badgeColor: 'bg-[#98A88F]',
        description: 'For more adjustment range, stability and space.',
        specs: {
            type: 'Sit-Stand',
            price: '$749',
            idealFor: 'More range, presets and desk space for power users and shared offices.',
            addOns: 'In-Desk Power',
            heightRange: '26"-52", Leveling Feet (1/4")',
            warranty: '10 Years',
            quote: 'Excellent experience. The desk is much sturdier than I anticipated. The assembly was a breeze.'
        }
    },
    {
        name: 'Four Leg Standing Desk',
        image: 'https://images.unsplash.com/photo-1518455027359-f3f8164ba6bd?ixlib=rb-4.0.3&w=300&q=80',
        badge: 'Top Of The Line',
        badgeColor: 'bg-[#8F9CA8]',
        description: 'Pioneering design in a familiar form.',
        specs: {
            type: 'Sit-Stand',
            price: '$949',
            idealFor: 'Beautiful and minimal form married with discreet sit-stand functionality.',
            addOns: 'Cable Tray (with optional power)',
            heightRange: '27.3"-44.6", Leveling Feet (1/4")',
            warranty: '10 Years',
            quote: 'The design is absolutely stunning.'
        }
    },
    {
        name: 'Daily Desk',
        image: 'https://images.unsplash.com/photo-1527864550417-7fd91fc51a46?ixlib=rb-4.0.3&w=300&q=80',
        badge: 'Attainable Style',
        badgeColor: 'bg-[#9CA88F]',
        description: 'Outsized functionality and minimal design.',
        specs: {
            type: 'Fixed Height',
            price: '$299',
            idealFor: 'Modern style and endless customizations for most spaces and budgets.',
            addOns: 'Cable Tray (with optional power), Desk Drawer, Casters',
            heightRange: '28.5", Leveling Feet (1/4")',
            warranty: '10 Years',
            quote: 'Perfect for my small apartment.'
        }
    },
    {
        name: 'Artisan Desk',
        image: 'https://plus.unsplash.com/premium_photo-1683133276662-799ff296fb08?ixlib=rb-4.0.3&w=300&q=80',
        badge: 'Modern Classic',
        badgeColor: 'bg-[#8FA2A8]',
        description: 'Natural wood warmth and handcrafted details.',
        specs: {
            type: 'Fixed Height',
            price: '$799',
            idealFor: 'Handcrafted warmth and high performance for design-ready spaces.',
            addOns: 'Artisan Drawer, Shelf, Cable Tray',
            heightRange: '28.5"',
            warranty: '5 Years',
            quote: 'A piece of art that functions as a desk.'
        }
    }
];
</script>
