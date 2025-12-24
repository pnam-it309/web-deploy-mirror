<template>
    <div class="w-full bg-white py-16 px-4 sm:px-6 lg:px-8">
        <div class="max-w-7xl mx-auto">
            <h2 class="text-4xl font-serif text-center text-gray-900 mb-16">Before You Buy</h2>

            <div class="flex flex-col lg:flex-row gap-12 items-start">
                <!-- Left Image Area -->
                <div class="w-full lg:w-1/2 relative">
                    <img src="https://images.unsplash.com/photo-1595515106969-1ce29566ff1c?ixlib=rb-4.0.3&auto=format&fit=crop&w=1000&q=80"
                        alt="Desk Detail" class="w-full h-auto rounded-lg shadow-sm object-cover" />
                </div>

                <!-- Right Content Area (Accordion/Tabs) -->
                <div class="w-full lg:w-1/2 space-y-0">
                    <!-- Item 1 -->
                    <div
                        class="border-l-2 border-transparent hover:border-indigo-600 pl-6 py-4 cursor-pointer group transition-all">
                        <h3 class="text-xl font-medium text-gray-400 group-hover:text-gray-900">Find Your Fit</h3>
                    </div>

                    <!-- Item 2 -->
                    <div
                        class="border-l-2 border-transparent hover:border-indigo-600 pl-6 py-4 cursor-pointer group transition-all">
                        <h3 class="text-xl font-medium text-gray-400 group-hover:text-gray-900">Size & Stature</h3>
                    </div>

                    <!-- Item 3 -->
                    <div
                        class="border-l-2 border-transparent hover:border-indigo-600 pl-6 py-4 cursor-pointer group transition-all">
                        <h3 class="text-xl font-medium text-gray-400 group-hover:text-gray-900">Mind Your Materials</h3>
                    </div>

                    <!-- Item 4 (Active) -->
                    <div class="border-l-2 border-indigo-600 pl-6 py-4 cursor-pointer">
                        <h3 class="text-xl font-bold text-gray-900 mb-2">Checkout And Relax</h3>
                        <p class="text-gray-600 leading-relaxed">
                            Once you purchase your new office furniture online, we'll ship it for free. Worried about
                            the fit? We've got your back—schedule a free ergonomic consultation.
                        </p>
                    </div>

                    <!-- Item 5 -->
                    <div
                        class="border-l-2 border-transparent hover:border-indigo-600 pl-6 py-4 cursor-pointer group transition-all">
                        <h3 class="text-xl font-medium text-gray-400 group-hover:text-gray-900">For Corporate Teams</h3>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
// Static content for now, could be made interactive state-based later
</script>
