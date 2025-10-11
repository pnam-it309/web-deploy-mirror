<template>
  <div class="p-6 bg-white rounded-lg shadow">
    <h1 class="text-2xl font-semibold mb-6">Create New Customer</h1>
    
    <form @submit.prevent="handleSubmit" class="space-y-6">
      <!-- Basic Information -->
      <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div>
          <label for="fullName" class="block text-sm font-medium text-gray-700">Full Name</label>
          <input
            type="text"
            id="fullName"
            v-model="formData.fullName"
            class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
            required
          >
        </div>
        
        <div>
          <label for="email" class="block text-sm font-medium text-gray-700">Email</label>
          <input
            type="email"
            id="email"
            v-model="formData.email"
            class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
            required
          >
        </div>
        
        <div>
          <label for="phone" class="block text-sm font-medium text-gray-700">Phone Number</label>
          <input
            type="tel"
            id="phone"
            v-model="formData.phone"
            class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
          >
        </div>
        
        <div>
          <label for="status" class="block text-sm font-medium text-gray-700">Status</label>
          <select
            id="status"
            v-model="formData.status"
            class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
          >
            <option value="active">Active</option>
            <option value="inactive">Inactive</option>
            <option value="pending">Pending</option>
          </select>
        </div>
      </div>
      
      <!-- Address Information -->
      <div class="border-t border-gray-200 pt-6">
        <h2 class="text-lg font-medium text-gray-900 mb-4">Address Information</h2>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div class="col-span-2">
            <label for="address" class="block text-sm font-medium text-gray-700">Street Address</label>
            <input
              type="text"
              id="address"
              v-model="formData.address.street"
              class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
            >
          </div>
          
          <div>
            <label for="city" class="block text-sm font-medium text-gray-700">City</label>
            <input
              type="text"
              id="city"
              v-model="formData.address.city"
              class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
            >
          </div>
          
          <div>
            <label for="state" class="block text-sm font-medium text-gray-700">State/Province</label>
            <input
              type="text"
              id="state"
              v-model="formData.address.state"
              class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
            >
          </div>
          
          <div>
            <label for="postalCode" class="block text-sm font-medium text-gray-700">Postal Code</label>
            <input
              type="text"
              id="postalCode"
              v-model="formData.address.postalCode"
              class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
            >
          </div>
          
          <div>
            <label for="country" class="block text-sm font-medium text-gray-700">Country</label>
            <input
              type="text"
              id="country"
              v-model="formData.address.country"
              class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
            >
          </div>
        </div>
      </div>
      
      <!-- Form Actions -->
      <div class="flex justify-end space-x-3 pt-6 border-t border-gray-200">
        <button
          type="button"
          @click="$router.go(-1)"
          class="px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
        >
          Cancel
        </button>
        <button
          type="submit"
          class="inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
          :disabled="isSubmitting"
        >
          <span v-if="isSubmitting">
            <svg class="animate-spin -ml-1 mr-2 h-4 w-4 text-white inline" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
              <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
              <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
            </svg>
            Saving...
          </span>
          <span v-else>Create Customer</span>
        </button>
      </div>
    </form>
  </div>
</template>

<script lang="ts">
import { defineComponent, reactive, ref } from 'vue';
import { useRouter } from 'vue-router';

export default defineComponent({
  name: 'CustomerCreate',
  setup() {
    const router = useRouter();
    const isSubmitting = ref(false);
    
    const formData = reactive({
      fullName: '',
      email: '',
      phone: '',
      status: 'active',
      address: {
        street: '',
        city: '',
        state: '',
        postalCode: '',
        country: ''
      }
    });
    
    const handleSubmit = async () => {
      try {
        isSubmitting.value = true;
        // TODO: Implement API call to create customer
        console.log('Creating customer:', formData);
        
        // Simulate API call
        await new Promise(resolve => setTimeout(resolve, 1000));
        
        // Redirect to customer list after successful creation
        await router.push({ name: 'admin-customers' });
        
      } catch (error) {
        console.error('Failed to create customer:', error);
      } finally {
        isSubmitting.value = false;
      }
    };
    
    return {
      formData,
      isSubmitting,
      handleSubmit
    };
  }
});
</script>
