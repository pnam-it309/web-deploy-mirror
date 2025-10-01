<template>
  <div class="p-6">
    <div class="md:flex md:items-center md:justify-between">
      <div class="flex-1 min-w-0">
        <h1 class="text-2xl font-semibold text-gray-900">My Profile</h1>
        <p class="mt-1 text-sm text-gray-500">
          Manage your personal information and preferences.
        </p>
      </div>
      <div class="mt-4 flex md:mt-0 md:ml-4">
        <button
          type="button"
          class="inline-flex items-center px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
        >
          <svg class="-ml-1 mr-2 h-5 w-5 text-gray-500" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor">
            <path d="M10 12a2 2 0 100-4 2 2 0 000 4z" />
            <path fill-rule="evenodd" d="M.458 10C1.732 5.943 5.522 3 10 3s8.268 2.943 9.542 7c-1.274 4.057-5.064 7-9.542 7S1.732 14.057.458 10zM14 10a4 4 0 11-8 0 4 4 0 018 0z" clip-rule="evenodd" />
          </svg>
          Preview
        </button>
        <button
          type="button"
          @click="saveProfile"
          class="ml-3 inline-flex items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
        >
          <svg class="-ml-1 mr-2 h-5 w-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor">
            <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd" />
          </svg>
          Save Changes
        </button>
      </div>
    </div>

    <div class="mt-8">
      <div class="md:grid md:grid-cols-3 md:gap-6">
        <div class="md:col-span-1">
          <div class="px-4 sm:px-0">
            <h3 class="text-lg font-medium leading-6 text-gray-900">Personal Information</h3>
            <p class="mt-1 text-sm text-gray-600">
              This information will be displayed publicly so be careful what you share.
            </p>
          </div>
        </div>
        <div class="mt-5 md:mt-0 md:col-span-2">
          <form @submit.prevent="saveProfile">
            <div class="shadow sm:rounded-md sm:overflow-hidden">
              <div class="px-4 py-5 bg-white space-y-6 sm:p-6">
                <!-- Profile Picture -->
                <div>
                  <label class="block text-sm font-medium text-gray-700">
                    Photo
                  </label>
                  <div class="mt-2 flex items-center">
                    <span class="inline-block h-12 w-12 rounded-full overflow-hidden bg-gray-100">
                      <img class="h-full w-full text-gray-300" :src="user.avatar || 'https://ui-avatars.com/api/?name=' + encodeURIComponent(user.name) + '&background=4f46e5&color=fff'" alt="">
                    </span>
                    <button
                      type="button"
                      class="ml-5 bg-white py-2 px-3 border border-gray-300 rounded-md shadow-sm text-sm leading-4 font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                    >
                      Change
                    </button>
                  </div>
                </div>

                <!-- Name -->
                <div class="grid grid-cols-6 gap-6">
                  <div class="col-span-6 sm:col-span-3">
                    <label for="first-name" class="block text-sm font-medium text-gray-700">First name</label>
                    <input
                      type="text"
                      v-model="user.firstName"
                      id="first-name"
                      autocomplete="given-name"
                      class="mt-1 focus:ring-indigo-500 focus:border-indigo-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md"
                    >
                  </div>

                  <div class="col-span-6 sm:col-span-3">
                    <label for="last-name" class="block text-sm font-medium text-gray-700">Last name</label>
                    <input
                      type="text"
                      v-model="user.lastName"
                      id="last-name"
                      autocomplete="family-name"
                      class="mt-1 focus:ring-indigo-500 focus:border-indigo-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md"
                    >
                  </div>
                </div>

                <!-- Email -->
                <div class="col-span-6 sm:col-span-4">
                  <label for="email-address" class="block text-sm font-medium text-gray-700">Email address</label>
                  <input
                    type="text"
                    v-model="user.email"
                    id="email-address"
                    autocomplete="email"
                    class="mt-1 focus:ring-indigo-500 focus:border-indigo-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md"
                    disabled
                  >
                </div>

                <!-- Phone -->
                <div class="col-span-6 sm:col-span-4">
                  <label for="phone" class="block text-sm font-medium text-gray-700">Phone number</label>
                  <input
                    type="text"
                    v-model="user.phone"
                    id="phone"
                    autocomplete="tel"
                    class="mt-1 focus:ring-indigo-500 focus:border-indigo-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md"
                  >
                </div>

                <!-- Bio -->
                <div>
                  <label for="about" class="block text-sm font-medium text-gray-700">
                    About
                  </label>
                  <div class="mt-1">
                    <textarea
                      id="about"
                      v-model="user.bio"
                      rows="3"
                      class="shadow-sm focus:ring-indigo-500 focus:border-indigo-500 mt-1 block w-full sm:text-sm border border-gray-300 rounded-md"
                      placeholder="A short introduction about yourself..."
                    ></textarea>
                  </div>
                  <p class="mt-2 text-sm text-gray-500">
                    Brief description for your profile. URLs are hyperlinked.
                  </p>
                </div>

                <!-- Location -->
                <div class="col-span-6 sm:col-span-6 lg:col-span-2">
                  <label for="country" class="block text-sm font-medium text-gray-700">Country / Region</label>
                  <select
                    id="country"
                    v-model="user.country"
                    autocomplete="country"
                    class="mt-1 block w-full py-2 px-3 border border-gray-300 bg-white rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                  >
                    <option>United States</option>
                    <option>Canada</option>
                    <option>Mexico</option>
                    <option>United Kingdom</option>
                    <option>Australia</option>
                    <option>Other</option>
                  </select>
                </div>
              </div>
              
              <!-- Privacy Section -->
              <div class="px-4 py-3 bg-gray-50 text-right sm:px-6">
                <h3 class="text-lg font-medium leading-6 text-gray-900 text-left mb-4">Privacy</h3>
                <div class="space-y-4">
                  <div class="flex items-start">
                    <div class="flex items-center h-5">
                      <input
                        id="comments"
                        v-model="user.privacy.allowMarketing"
                        type="checkbox"
                        class="focus:ring-indigo-500 h-4 w-4 text-indigo-600 border-gray-300 rounded"
                      >
                    </div>
                    <div class="ml-3 text-sm">
                      <label for="comments" class="font-medium text-gray-700">Email me about new products and offers</label>
                      <p class="text-gray-500">Get notified when new products are added or there are special offers.</p>
                    </div>
                  </div>
                  <div class="flex items-start">
                    <div class="flex items-center h-5">
                      <input
                        id="candidates"
                        v-model="user.privacy.allowSMS"
                        type="checkbox"
                        class="focus:ring-indigo-500 h-4 w-4 text-indigo-600 border-gray-300 rounded"
                      >
                    </div>
                    <div class="ml-3 text-sm">
                      <label for="candidates" class="font-medium text-gray-700">Text me updates about my orders</label>
                      <p class="text-gray-500">Get order updates and delivery notifications via SMS.</p>
                    </div>
                  </div>
                  <div class="flex items-start">
                    <div class="flex items-center h-5">
                      <input
                        id="offers"
                        v-model="user.privacy.showProfile"
                        type="checkbox"
                        class="focus:ring-indigo-500 h-4 w-4 text-indigo-600 border-gray-300 rounded"
                      >
                    </div>
                    <div class="ml-3 text-sm">
                      <label for="offers" class="font-medium text-gray-700">Make my profile public</label>
                      <p class="text-gray-500">Allow others to find and view your profile.</p>
                    </div>
                  </div>
                </div>
              </div>
              
              <!-- Save Button -->
              <div class="px-4 py-3 bg-gray-50 text-right sm:px-6">
                <button
                  type="submit"
                  class="inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                >
                  Save
                </button>
              </div>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';

// User data
const user = ref({
  firstName: 'John',
  lastName: 'Doe',
  email: 'john.doe@example.com',
  phone: '+1 (555) 123-4567',
  bio: 'I love shopping for the latest tech gadgets and fashion trends!',
  country: 'United States',
  avatar: 'https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=facearea&facepad=2&w=256&h=256&q=80',
  privacy: {
    allowMarketing: true,
    allowSMS: false,
    showProfile: true
  }
});

// Computed full name
const fullName = computed(() => {
  return `${user.value.firstName} ${user.value.lastName}`.trim();
});

// Save profile
const saveProfile = () => {
  // TODO: Implement save profile logic
  console.log('Saving profile:', user.value);
  alert('Profile saved successfully!');
};
</script>
