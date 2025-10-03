<template>
  <div class="p-6">
    <h1 class="text-2xl font-semibold text-gray-900">Account Settings</h1>
    
    <div class="mt-8">
      <div class="md:grid md:grid-cols-3 md:gap-6">
        <div class="md:col-span-1">
          <div class="px-4 sm:px-0">
            <h3 class="text-lg font-medium leading-6 text-gray-900">Security</h3>
            <p class="mt-1 text-sm text-gray-600">
              Update your password and secure your account.
            </p>
          </div>
        </div>
        <div class="mt-5 md:mt-0 md:col-span-2">
          <form @submit.prevent="updatePassword">
            <div class="shadow sm:rounded-md sm:overflow-hidden">
              <div class="px-4 py-5 bg-white space-y-6 sm:p-6">
                <!-- Current Password -->
                <div>
                  <label for="current-password" class="block text-sm font-medium text-gray-700">
                    Current password
                  </label>
                  <div class="mt-1 relative rounded-md shadow-sm">
                    <input
                      :type="showCurrentPassword ? 'text' : 'password'"
                      v-model="password.current"
                      id="current-password"
                      class="focus:ring-indigo-500 focus:border-indigo-500 block w-full pr-10 sm:text-sm border-gray-300 rounded-md"
                      placeholder="Enter your current password"
                    >
                    <div class="absolute inset-y-0 right-0 pr-3 flex items-center">
                      <button
                        type="button"
                        @click="showCurrentPassword = !showCurrentPassword"
                        class="text-gray-500 hover:text-gray-600 focus:outline-none"
                      >
                        <svg
                          v-if="showCurrentPassword"
                          class="h-5 w-5"
                          fill="none"
                          viewBox="0 0 24 24"
                          stroke="currentColor"
                        >
                          <path
                            stroke-linecap="round"
                            stroke-linejoin="round"
                            stroke-width="2"
                            d="M13.875 18.825A10.05 10.05 0 0112 19c-4.478 0-8.268-2.943-9.543-7a9.97 9.97 0 011.563-3.029m5.858.908a3 3 0 114.243 4.243M9.878 9.878l4.242 4.242M9.88 9.88l-3.29-3.29m7.532 7.532l3.29 3.29M3 3l3.59 3.59m0 0A9.953 9.953 0 0112 5c4.478 0 8.268 2.943 9.543 7a10.025 10.025 0 01-4.132 5.411m0 0L21 21"
                          />
                        </svg>
                        <svg
                          v-else
                          class="h-5 w-5"
                          fill="none"
                          viewBox="0 0 24 24"
                          stroke="currentColor"
                        >
                          <path
                            stroke-linecap="round"
                            stroke-linejoin="round"
                            stroke-width="2"
                            d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"
                          />
                          <path
                            stroke-linecap="round"
                            stroke-linejoin="round"
                            stroke-width="2"
                            d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"
                          />
                        </svg>
                      </button>
                    </div>
                  </div>
                </div>

                <!-- New Password -->
                <div>
                  <label for="new-password" class="block text-sm font-medium text-gray-700">
                    New password
                  </label>
                  <div class="mt-1 relative rounded-md shadow-sm">
                    <input
                      :type="showNewPassword ? 'text' : 'password'"
                      v-model="password.new"
                      id="new-password"
                      class="focus:ring-indigo-500 focus:border-indigo-500 block w-full pr-10 sm:text-sm border-gray-300 rounded-md"
                      placeholder="Enter your new password"
                    >
                    <div class="absolute inset-y-0 right-0 pr-3 flex items-center">
                      <button
                        type="button"
                        @click="showNewPassword = !showNewPassword"
                        class="text-gray-500 hover:text-gray-600 focus:outline-none"
                      >
                        <svg
                          v-if="showNewPassword"
                          class="h-5 w-5"
                          fill="none"
                          viewBox="0 0 24 24"
                          stroke="currentColor"
                        >
                          <path
                            stroke-linecap="round"
                            stroke-linejoin="round"
                            stroke-width="2"
                            d="M13.875 18.825A10.05 10.05 0 0112 19c-4.478 0-8.268-2.943-9.543-7a9.97 9.97 0 011.563-3.029m5.858.908a3 3 0 114.243 4.243M9.878 9.878l4.242 4.242M9.88 9.88l-3.29-3.29m7.532 7.532l3.29 3.29M3 3l3.59 3.59m0 0A9.953 9.953 0 0112 5c4.478 0 8.268 2.943 9.543 7a10.025 10.025 0 01-4.132 5.411m0 0L21 21"
                          />
                        </svg>
                        <svg
                          v-else
                          class="h-5 w-5"
                          fill="none"
                          viewBox="0 0 24 24"
                          stroke="currentColor"
                        >
                          <path
                            stroke-linecap="round"
                            stroke-linejoin="round"
                            stroke-width="2"
                            d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"
                          />
                          <path
                            stroke-linecap="round"
                            stroke-linejoin="round"
                            stroke-width="2"
                            d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"
                          />
                        </svg>
                      </button>
                    </div>
                  </div>
                </div>

                <!-- Confirm New Password -->
                <div>
                  <label for="confirm-password" class="block text-sm font-medium text-gray-700">
                    Confirm new password
                  </label>
                  <div class="mt-1 relative rounded-md shadow-sm">
                    <input
                      :type="showConfirmPassword ? 'text' : 'password'"
                      v-model="password.confirm"
                      id="confirm-password"
                      class="focus:ring-indigo-500 focus:border-indigo-500 block w-full pr-10 sm:text-sm border-gray-300 rounded-md"
                      placeholder="Confirm your new password"
                    >
                    <div class="absolute inset-y-0 right-0 pr-3 flex items-center">
                      <button
                        type="button"
                        @click="showConfirmPassword = !showConfirmPassword"
                        class="text-gray-500 hover:text-gray-600 focus:outline-none"
                      >
                        <svg
                          v-if="showConfirmPassword"
                          class="h-5 w-5"
                          fill="none"
                          viewBox="0 0 24 24"
                          stroke="currentColor"
                        >
                          <path
                            stroke-linecap="round"
                            stroke-linejoin="round"
                            stroke-width="2"
                            d="M13.875 18.825A10.05 10.05 0 0112 19c-4.478 0-8.268-2.943-9.543-7a9.97 9.97 0 011.563-3.029m5.858.908a3 3 0 114.243 4.243M9.878 9.878l4.242 4.242M9.88 9.88l-3.29-3.29m7.532 7.532l3.29 3.29M3 3l3.59 3.59m0 0A9.953 9.953 0 0112 5c4.478 0 8.268 2.943 9.543 7a10.025 10.025 0 01-4.132 5.411m0 0L21 21"
                          />
                        </svg>
                        <svg
                          v-else
                          class="h-5 w-5"
                          fill="none"
                          viewBox="0 0 24 24"
                          stroke="currentColor"
                        >
                          <path
                            stroke-linecap="round"
                            stroke-linejoin="round"
                            stroke-width="2"
                            d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"
                          />
                          <path
                            stroke-linecap="round"
                            stroke-linejoin="round"
                            stroke-width="2"
                            d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"
                          />
                        </svg>
                      </button>
                    </div>
                  </div>
                </div>

                <!-- Password Strength -->
                <div v-if="password.new" class="mt-2">
                  <div class="flex items-center justify-between mb-1">
                    <span class="text-sm font-medium text-gray-700">Password strength</span>
                    <span class="text-xs font-medium text-gray-500">{{ passwordStrength }}/4</span>
                  </div>
                  <div class="w-full bg-gray-200 rounded-full h-2">
                    <div
                      :class="[
                        'h-2 rounded-full',
                        passwordStrength === 0 ? 'bg-red-500' : '',
                        passwordStrength === 1 ? 'bg-yellow-500 w-1/4' : '',
                        passwordStrength === 2 ? 'bg-yellow-400 w-1/2' : '',
                        passwordStrength === 3 ? 'bg-blue-500 w-3/4' : '',
                        passwordStrength === 4 ? 'bg-green-500 w-full' : ''
                      ]"
                    ></div>
                  </div>
                  <p v-if="password.new && passwordStrength < 3" class="mt-1 text-xs text-yellow-600">
                    Your password should be at least 8 characters long and contain a mix of letters, numbers, and symbols.
                  </p>
                </div>
              </div>
              
              <div class="px-4 py-3 bg-gray-50 text-right sm:px-6">
                <button
                  type="submit"
                  :disabled="!isFormValid"
                  :class="[
                    'inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white',
                    isFormValid ? 'bg-indigo-600 hover:bg-indigo-700' : 'bg-indigo-300 cursor-not-allowed',
                    'focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500'
                  ]"
                >
                  Update Password
                </button>
              </div>
            </div>
          </form>
        </div>
      </div>
    </div>
    
    <!-- Two-Factor Authentication -->
    <div class="mt-10">
      <div class="md:grid md:grid-cols-3 md:gap-6">
        <div class="md:col-span-1">
          <div class="px-4 sm:px-0">
            <h3 class="text-lg font-medium leading-6 text-gray-900">Two-Factor Authentication</h3>
            <p class="mt-1 text-sm text-gray-600">
              Add an extra layer of security to your account.
            </p>
          </div>
        </div>
        <div class="mt-5 md:mt-0 md:col-span-2">
          <div class="shadow sm:rounded-md sm:overflow-hidden">
            <div class="px-4 py-5 bg-white space-y-6 sm:p-6">
              <div class="flex items-start">
                <div class="flex items-center h-5">
                  <input
                    id="two-factor"
                    v-model="twoFactorEnabled"
                    type="checkbox"
                    class="focus:ring-indigo-500 h-4 w-4 text-indigo-600 border-gray-300 rounded"
                  >
                </div>
                <div class="ml-3 text-sm">
                  <label for="two-factor" class="font-medium text-gray-700">Enable Two-Factor Authentication</label>
                  <p class="text-gray-500">Require a verification code when signing in.</p>
                </div>
              </div>
              
              <div v-if="twoFactorEnabled" class="mt-4 p-4 bg-gray-50 rounded-md">
                <div class="flex items-center">
                  <div class="flex-shrink-0">
                    <svg class="h-5 w-5 text-yellow-400" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor">
                      <path fill-rule="evenodd" d="M8.257 3.099c.765-1.36 2.722-1.36 3.486 0l5.58 9.92c.75 1.334-.213 2.98-1.742 2.98H4.42c-1.53 0-2.493-1.646-1.743-2.98l5.58-9.92zM11 13a1 1 0 11-2 0 1 1 0 012 0zm-1-8a1 1 0 00-1 1v3a1 1 0 002 0V6a1 1 0 00-1-1z" clip-rule="evenodd" />
                    </svg>
                  </div>
                  <div class="ml-3">
                    <h3 class="text-sm font-medium text-yellow-800">Setup required</h3>
                    <div class="mt-2 text-sm text-yellow-700">
                      <p>Scan the QR code with your authenticator app or enter the setup key manually.</p>
                    </div>
                    <div class="mt-4">
                      <div class="flex justify-center">
                        <div class="p-2 bg-white rounded">
                          <!-- QR Code Placeholder -->
                          <div class="w-40 h-40 flex items-center justify-center border-2 border-dashed border-gray-300 rounded">
                            <span class="text-xs text-gray-500">QR Code</span>
                          </div>
                        </div>
                      </div>
                      <div class="mt-4 text-center">
                        <p class="text-xs text-gray-500">Or enter this code manually:</p>
                        <p class="mt-1 font-mono text-sm font-medium text-gray-900">JBSWY3DPEHPK3PXP</p>
                      </div>
                      <div class="mt-4">
                        <label for="verification-code" class="block text-sm font-medium text-gray-700">Verification code</label>
                        <div class="mt-1">
                          <input
                            type="text"
                            id="verification-code"
                            v-model="verificationCode"
                            class="shadow-sm focus:ring-indigo-500 focus:border-indigo-500 block w-full sm:text-sm border-gray-300 rounded-md"
                            placeholder="Enter 6-digit code"
                          >
                        </div>
                        <button
                          type="button"
                          class="mt-2 inline-flex items-center px-3 py-1.5 border border-transparent text-xs font-medium rounded-md shadow-sm text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                        >
                          Verify & Activate
                        </button>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- Delete Account -->
    <div class="mt-10">
      <div class="md:grid md:grid-cols-3 md:gap-6">
        <div class="md:col-span-1">
          <div class="px-4 sm:px-0">
            <h3 class="text-lg font-medium leading-6 text-gray-900">Delete Account</h3>
            <p class="mt-1 text-sm text-gray-600">
              Permanently delete your account and all of your data.
            </p>
          </div>
        </div>
        <div class="mt-5 md:mt-0 md:col-span-2">
          <div class="bg-white shadow sm:rounded-lg overflow-hidden">
            <div class="px-4 py-5 sm:p-6">
              <h3 class="text-lg leading-6 font-medium text-gray-900">Delete your account</h3>
              <div class="mt-2 max-w-xl text-sm text-gray-500">
                <p>Once you delete your account, you will lose all your data including order history and saved preferences. This action cannot be undone.</p>
              </div>
              <div class="mt-5">
                <button
                  type="button"
                  @click="showDeleteModal = true"
                  class="inline-flex items-center justify-center px-4 py-2 border border-transparent font-medium rounded-md text-red-700 bg-red-100 hover:bg-red-200 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500 sm:text-sm"
                >
                  Delete account
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- Delete Account Modal -->
    <div v-if="showDeleteModal" class="fixed z-10 inset-0 overflow-y-auto" aria-labelledby="modal-title" role="dialog" aria-modal="true">
      <div class="flex items-end justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
        <div class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity" aria-hidden="true" @click="showDeleteModal = false"></div>
        <span class="hidden sm:inline-block sm:align-middle sm:h-screen" aria-hidden="true">&#8203;</span>
        <div class="inline-block align-bottom bg-white rounded-lg px-4 pt-5 pb-4 text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-lg sm:w-full sm:p-6">
          <div class="sm:flex sm:items-start">
            <div class="mx-auto flex-shrink-0 flex items-center justify-center h-12 w-12 rounded-full bg-red-100 sm:mx-0 sm:h-10 sm:w-10">
              <svg class="h-6 w-6 text-red-600" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" aria-hidden="true">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
              </svg>
            </div>
            <div class="mt-3 text-center sm:mt-0 sm:ml-4 sm:text-left">
              <h3 class="text-lg leading-6 font-medium text-gray-900" id="modal-title">Delete account</h3>
              <div class="mt-2">
                <p class="text-sm text-gray-500">Are you sure you want to delete your account? All of your data will be permanently removed. This action cannot be undone.</p>
              </div>
              <div class="mt-4">
                <label for="delete-confirm" class="block text-sm font-medium text-gray-700">
                  Type "DELETE" to confirm
                </label>
                <input
                  type="text"
                  id="delete-confirm"
                  v-model="deleteConfirmation"
                  class="mt-1 focus:ring-indigo-500 focus:border-indigo-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md"
                  placeholder="Type DELETE to confirm"
                >
              </div>
            </div>
          </div>
          <div class="mt-5 sm:mt-4 sm:flex sm:flex-row-reverse">
            <button
              type="button"
              @click="deleteAccount"
              :disabled="deleteConfirmation !== 'DELETE'"
              :class="[
                'w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 text-base font-medium text-white sm:ml-3 sm:w-auto sm:text-sm',
                deleteConfirmation === 'DELETE' ? 'bg-red-600 hover:bg-red-700' : 'bg-red-300 cursor-not-allowed',
                'focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500'
              ]"
            >
              Delete
            </button>
            <button
              type="button"
              @click="showDeleteModal = false"
              class="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:mt-0 sm:w-auto sm:text-sm"
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
import { ref, computed, watch } from 'vue';

// Password Management
const password = ref({
  current: '',
  new: '',
  confirm: ''
});

const showCurrentPassword = ref(false);
const showNewPassword = ref(false);
const showConfirmPassword = ref(false);

// Two-Factor Authentication
const twoFactorEnabled = ref(false);
const verificationCode = ref('');

// Delete Account
const showDeleteModal = ref(false);
const deleteConfirmation = ref('');

// Password Strength Calculation
const passwordStrength = computed(() => {
  let strength = 0;
  const pass = password.value.new;
  
  if (!pass) return 0;
  
  // Length check
  if (pass.length >= 8) strength++;
  
  // Contains number
  if (/\d/.test(pass)) strength++;
  
  // Contains special char
  if (/[!@#$%^&*(),.?":{}|<>]/.test(pass)) strength++;
  
  // Contains both lower and upper case
  if (/[a-z]/.test(pass) && /[A-Z]/.test(pass)) strength++;
  
  return Math.min(strength, 4);
});

// Form Validation
const isFormValid = computed(() => {
  return (
    password.value.current &&
    password.value.new &&
    password.value.confirm &&
    password.value.new === password.value.confirm &&
    passwordStrength.value >= 3
  );
});

// Watch for password changes to clear confirm if they don't match
watch(() => password.value.new, (newVal, oldVal) => {
  if (password.value.confirm && newVal !== password.value.confirm) {
    password.value.confirm = '';
  }
});

// Methods
const updatePassword = async () => {
  if (!isFormValid.value) return;
  
  try {
    // TODO: Implement password update logic
    console.log('Updating password...');
    
    // Simulate API call
    await new Promise(resolve => setTimeout(resolve, 1000));
    
    // Reset form
    password.value = { current: '', new: '', confirm: '' };
    
    // Show success message
    alert('Password updated successfully!');
  } catch (error) {
    console.error('Failed to update password:', error);
    alert('Failed to update password. Please try again.');
  }
};

const deleteAccount = async () => {
  if (deleteConfirmation.value !== 'DELETE') return;
  
  try {
    // TODO: Implement account deletion logic
    console.log('Deleting account...');
    
    // Simulate API call
    await new Promise(resolve => setTimeout(resolve, 1000));
    
    // Redirect to home or login page
    // router.push('/');
    alert('Your account has been scheduled for deletion.');
    showDeleteModal.value = false;
  } catch (error) {
    console.error('Failed to delete account:', error);
    alert('Failed to delete account. Please try again.');
  }
};
</script>
