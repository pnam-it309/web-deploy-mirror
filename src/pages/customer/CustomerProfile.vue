<template>
  <div class="container mx-auto max-w-6xl px-4 py-8">
    <div class="flex items-center gap-4 mb-8">
      <div class="relative">
        <Avatar class="h-20 w-20">
          <AvatarImage :src="user.avatar" />
          <AvatarFallback class="text-2xl">
            {{ user.name.split(' ').map(n => n[0]).join('') }}
          </AvatarFallback>
        </Avatar>
        <Button
          variant="outline"
          size="icon"
          class="absolute -bottom-1 -right-1 h-8 w-8 rounded-full"
          @click="openAvatarUpload"
        >
          <PencilIcon class="h-4 w-4" />
        </Button>
      </div>
      <div>
        <h1 class="text-2xl font-bold">Welcome back, {{ user.name }}!</h1>
        <p class="text-muted-foreground">Member since {{ formatDate(user.memberSince) }}</p>
      </div>
    </div>

    <Tabs v-model="activeTab" class="space-y-6">
      <TabsList class="grid w-full grid-cols-4">
        <TabsTrigger value="profile">
          <UserIcon class="h-4 w-4 mr-2" />
          Profile
        </TabsTrigger>
        <TabsTrigger value="orders">
          <ShoppingBagIcon class="h-4 w-4 mr-2" />
          Orders
        </TabsTrigger>
        <TabsTrigger value="wishlist">
          <HeartIcon class="h-4 w-4 mr-2" />
          Wishlist
        </TabsTrigger>
        <TabsTrigger value="settings">
          <Cog6ToothIcon class="h-4 w-4 mr-2" />
          Settings
        </TabsTrigger>
      </TabsList>

      <TabsContent value="profile" class="space-y-6">
        <Card>
          <CardHeader>
            <CardTitle>Personal Information</CardTitle>
            <CardDescription>
              Update your personal information and how we can reach you.
            </CardDescription>
          </CardHeader>
          <CardContent>
            <form @submit.prevent="saveProfile" class="space-y-6">
              <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div class="space-y-2">
                  <Label for="name">Full Name</Label>
                  <Input
                    id="name"
                    v-model="formData.name"
                    :disabled="!isEditing"
                  />
                </div>
                <div class="space-y-2">
                  <Label for="email">Email</Label>
                  <Input
                    id="email"
                    type="email"
                    v-model="formData.email"
                    :disabled="!isEditing"
                  />
                </div>
                <div class="space-y-2">
                  <Label for="phone">Phone</Label>
                  <Input
                    id="phone"
                    v-model="formData.phone"
                    :disabled="!isEditing"
                  />
                </div>
                <div class="space-y-2">
                  <Label for="birthday">Date of Birth</Label>
                  <Input
                    id="birthday"
                    type="date"
                    v-model="formData.birthday"
                    :disabled="!isEditing"
                  />
                </div>
              </div>

              <div class="space-y-2">
                <Label for="bio">Bio</Label>
                <Textarea
                  id="bio"
                  v-model="formData.bio"
                  :disabled="!isEditing"
                  placeholder="Tell us a little about yourself..."
                  class="min-h-[100px]"
                />
              </div>

              <div class="flex justify-end gap-3 pt-4">
                <Button
                  v-if="isEditing"
                  type="button"
                  variant="outline"
                  @click="cancelEdit"
                >
                  Cancel
                </Button>
                <Button
                  v-if="!isEditing"
                  type="button"
                  @click="isEditing = true"
                >
                  Edit Profile
                </Button>
                <Button
                  v-else
                  type="submit"
                >
                  Save Changes
                </Button>
              </div>
            </form>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>Address Book</CardTitle>
            <CardDescription>
              Manage your shipping addresses for faster checkout.
            </CardDescription>
          </CardHeader>
          <CardContent>
            <div class="space-y-4">
              <div
                v-for="(address, index) in user.addresses"
                :key="index"
                class="border rounded-lg p-4 relative"
                :class="{ 'border-primary': address.isDefault }"
              >
                <div class="flex justify-between items-start">
                  <div>
                    <h4 class="font-medium">{{ address.name }}</h4>
                    <p class="text-sm text-muted-foreground">
                      {{ address.street }}<br>
                      {{ address.city }}, {{ address.state }} {{ address.zip }}<br>
                      {{ address.country }}
                    </p>
                    <p class="text-sm mt-2">
                      <span class="font-medium">Phone:</span> {{ address.phone }}
                    </p>
                  </div>
                  <div class="flex gap-2">
                    <Button
                      variant="ghost"
                      size="sm"
                      @click="editAddress(address)"
                    >
                      <PencilIcon class="h-4 w-4" />
                    </Button>
                    <Button
                      variant="ghost"
                      size="sm"
                      class="text-destructive hover:text-destructive"
                      @click="removeAddress(index)"
                    >
                      <TrashIcon class="h-4 w-4" />
                    </Button>
                  </div>
                </div>

                <div v-if="address.isDefault" class="mt-2">
                  <Badge variant="secondary" class="text-xs">
                    <CheckIcon class="h-3 w-3 mr-1" /> Default
                  </Badge>
                </div>

                <div v-else class="mt-2">
                  <Button
                    variant="outline"
                    size="sm"
                    class="text-xs h-7"
                    @click="setDefaultAddress(index)"
                  >
                    Set as default
                  </Button>
                </div>
              </div>

              <Button
                variant="outline"
                class="w-full mt-4"
                @click="openAddressModal"
              >
                <PlusIcon class="h-4 w-4 mr-2" />
                Add New Address
              </Button>
            </div>
          </CardContent>
        </Card>
      </TabsContent>

      <TabsContent value="orders" class="space-y-6">
        <Card>
          <CardHeader>
            <CardTitle>Order History</CardTitle>
            <CardDescription>
              View your recent orders and track their status.
            </CardDescription>
          </CardHeader>
          <CardContent>
            <div v-if="user.orders.length > 0" class="space-y-6">
              <div
                v-for="order in user.orders"
                :key="order.id"
                class="border rounded-lg overflow-hidden"
              >
                <div class="bg-muted/50 p-4 flex justify-between items-center">
                  <div>
                    <p class="font-medium">Order #{{ order.id }}</p>
                    <p class="text-sm text-muted-foreground">
                      Placed on {{ formatDate(order.date) }}
                    </p>
                  </div>
                  <div class="text-right">
                    <p class="font-medium">${{ order.total.toFixed(2) }}</p>
                    <Badge :variant="getStatusVariant(order.status)">
                      {{ order.status }}
                    </Badge>
                  </div>
                </div>

                <div class="p-4">
                  <div class="flex gap-4 overflow-x-auto pb-2">
                    <div
                      v-for="(item, index) in order.items.slice(0, 4)"
                      :key="index"
                      class="flex-shrink-0 w-16 h-16 rounded-md overflow-hidden bg-muted"
                    >
                      <img
                        :src="item.image"
                        :alt="item.name"
                        class="w-full h-full object-cover"
                      />
                    </div>

                    <div
                      v-if="order.items.length > 4"
                      class="flex-shrink-0 w-16 h-16 rounded-md bg-muted flex items-center justify-center text-muted-foreground text-sm"
                    >
                      +{{ order.items.length - 4 }} more
                    </div>
                  </div>

                  <div class="mt-4 flex justify-end">
                    <Button variant="outline" size="sm" class="mr-2">
                      <TruckIcon class="h-4 w-4 mr-2" />
                      Track Order
                    </Button>
                    <Button variant="outline" size="sm">
                      <ArrowPathIcon class="h-4 w-4 mr-2" />
                      Order Again
                    </Button>
                  </div>
                </div>
              </div>
            </div>

            <div v-else class="text-center py-12">
              <ShoppingBagIcon class="h-12 w-12 text-muted-foreground mx-auto mb-4" />
              <h3 class="text-lg font-medium mb-2">No Orders Yet</h3>
              <p class="text-muted-foreground mb-6">
                You haven't placed any orders yet. Start shopping to see your orders here.
              </p>
              <router-link to="/products">
                <Button>
                  Start Shopping
                </Button>
              </router-link>
            </div>
          </CardContent>
        </Card>
      </TabsContent>

      <TabsContent value="wishlist" class="space-y-6">
        <Card>
          <CardHeader>
            <CardTitle>Your Wishlist</CardTitle>
            <CardDescription>
              Your saved items are waiting for you.
            </CardDescription>
          </CardHeader>
          <CardContent>
            <div v-if="user.wishlist.length > 0" class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
              <ProductCard
                v-for="product in user.wishlist"
                :key="product.id"
                :product="product"
                @add-to-cart="addToCart"
              />
            </div>

            <div v-else class="text-center py-12">
              <HeartIcon class="h-12 w-12 text-muted-foreground mx-auto mb-4" />
              <h3 class="text-lg font-medium mb-2">Your wishlist is empty</h3>
              <p class="text-muted-foreground mb-6">
                Save items you love to buy them later.
              </p>
              <router-link to="/products">
                <Button>
                  Browse Products
                </Button>
              </router-link>
            </div>
          </CardContent>
        </Card>
      </TabsContent>

      <TabsContent value="settings" class="space-y-6">
        <Card>
          <CardHeader>
            <CardTitle>Account Settings</CardTitle>
            <CardDescription>
              Manage your account preferences and security settings.
            </CardDescription>
          </CardHeader>
          <CardContent class="space-y-6">
            <div class="space-y-4">
              <h4 class="font-medium">Email Notifications</h4>
              <div class="space-y-4">
                <div class="flex items-center justify-between">
                  <div class="space-y-1">
                    <Label>Order Updates</Label>
                    <p class="text-sm text-muted-foreground">
                      Get notified about your order status
                    </p>
                  </div>
                  <Switch v-model:checked="settings.emailNotifications.orderUpdates" />
                </div>

                <div class="flex items-center justify-between">
                  <div class="space-y-1">
                    <Label>Promotions & Offers</Label>
                    <p class="text-sm text-muted-foreground">
                      Receive exclusive deals and discounts
                    </p>
                  </div>
                  <Switch v-model:checked="settings.emailNotifications.promotions" />
                </div>

                <div class="flex items-center justify-between">
                  <div class="space-y-1">
                    <Label>Newsletter</Label>
                    <p class="text-sm text-muted-foreground">
                      Get the latest news and updates
                    </p>
                  </div>
                  <Switch v-model:checked="settings.emailNotifications.newsletter" />
                </div>
              </div>
            </div>

            <Separator />

            <div class="space-y-4">
              <h4 class="font-medium">Security</h4>
              <div class="space-y-4">
                <div class="flex items-center justify-between">
                  <div class="space-y-1">
                    <Label>Two-Factor Authentication</Label>
                    <p class="text-sm text-muted-foreground">
                      Add an extra layer of security to your account
                    </p>
                  </div>
                  <Switch v-model:checked="settings.security.twoFactorAuth" />
                </div>

                <div class="flex items-center justify-between">
                  <div class="space-y-1">
                    <Label>Login Notifications</Label>
                    <p class="text-sm text-muted-foreground">
                      Get notified when someone logs into your account
                    </p>
                  </div>
                  <Switch v-model:checked="settings.security.loginNotifications" />
                </div>
              </div>
            </div>

            <Separator />

            <div class="space-y-4">
              <h4 class="font-medium">Danger Zone</h4>
              <div class="space-y-4">
                <div class="flex items-center justify-between p-4 bg-destructive/10 rounded-lg">
                  <div>
                    <h5 class="font-medium">Deactivate Account</h5>
                    <p class="text-sm text-muted-foreground">
                      Your account will be temporarily disabled
                    </p>
                  </div>
                  <Button variant="destructive" size="sm">
                    Deactivate
                  </Button>
                </div>

                <div class="flex items-center justify-between p-4 bg-destructive/10 rounded-lg">
                  <div>
                    <h5 class="font-medium">Delete Account</h5>
                    <p class="text-sm text-muted-foreground">
                      Permanently delete your account and all data
                    </p>
                  </div>
                  <Button variant="destructive" size="sm">
                    Delete Account
                  </Button>
                </div>
              </div>
            </div>

            <div class="flex justify-end pt-4">
              <Button @click="saveSettings">
                Save Changes
              </Button>
            </div>
          </CardContent>
        </Card>
      </TabsContent>
    </Tabs>

    <!-- Address Modal -->
    <Dialog :open="isAddressModalOpen" @update:open="val => isAddressModalOpen = val">
      <DialogContent class="sm:max-w-[500px]">
        <DialogHeader>
          <DialogTitle>{{ editingAddressIndex === null ? 'Add New' : 'Edit' }} Address</DialogTitle>
          <DialogDescription>
            {{ editingAddressIndex === null ? 'Add a new shipping address' : 'Update your address details' }}
          </DialogDescription>
        </DialogHeader>

        <div class="grid gap-4 py-4">
          <div class="space-y-2">
            <Label for="address-name">Address Name</Label>
            <Input
              id="address-name"
              v-model="addressForm.name"
              placeholder="e.g. Home, Work"
            />
          </div>

          <div class="space-y-2">
            <Label for="street">Street Address</Label>
            <Input
              id="street"
              v-model="addressForm.street"
              placeholder="123 Main St"
            />
          </div>

          <div class="grid grid-cols-2 gap-4">
            <div class="space-y-2">
              <Label for="city">City</Label>
              <Input id="city" v-model="addressForm.city" />
            </div>
            <div class="space-y-2">
              <Label for="state">State/Province</Label>
              <Input id="state" v-model="addressForm.state" />
            </div>
          </div>

          <div class="grid grid-cols-2 gap-4">
            <div class="space-y-2">
              <Label for="zip">ZIP/Postal Code</Label>
              <Input id="zip" v-model="addressForm.zip" />
            </div>
            <div class="space-y-2">
              <Label for="country">Country</Label>
              <Input id="country" v-model="addressForm.country" />
            </div>
          </div>

          <div class="space-y-2">
            <Label for="phone">Phone Number</Label>
            <Input
              id="phone"
              v-model="addressForm.phone"
              placeholder="+1 (555) 123-4567"
            />
          </div>

          <div class="flex items-center space-x-2 pt-2">
            <Checkbox id="default-address" v-model:checked="addressForm.isDefault" />
            <Label for="default-address" class="text-sm font-medium leading-none">
              Set as default shipping address
            </Label>
          </div>
        </div>

        <DialogFooter>
          <Button variant="outline" @click="isAddressModalOpen = false">
            Cancel
          </Button>
          <Button type="submit" @click="saveAddress">
            {{ editingAddressIndex === null ? 'Add Address' : 'Save Changes' }}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>

    <!-- Avatar Upload Dialog -->
    <Dialog :open="isAvatarUploadOpen" @update:open="val => isAvatarUploadOpen = val">
      <DialogContent class="sm:max-w-[425px]">
        <DialogHeader>
          <DialogTitle>Update Profile Picture</DialogTitle>
          <DialogDescription>
            Upload a new profile picture. Click save when you're done.
          </DialogDescription>
        </DialogHeader>

        <div class="grid gap-4 py-4">
          <div class="flex justify-center">
            <div class="relative">
              <Avatar class="h-32 w-32">
                <AvatarImage :src="newAvatar || user.avatar" />
                <AvatarFallback class="text-4xl">
                  {{ user.name.split(' ').map(n => n[0]).join('') }}
                </AvatarFallback>
              </Avatar>

              <label
                for="avatar-upload"
                class="absolute -bottom-2 -right-2 bg-primary text-primary-foreground p-2 rounded-full cursor-pointer hover:bg-primary/90"
              >
                <PencilIcon class="h-4 w-4" />
                <input
                  id="avatar-upload"
                  type="file"
                  class="hidden"
                  accept="image/*"
                  @change="handleAvatarUpload"
                />
              </label>
            </div>
          </div>

          <div class="text-center text-sm text-muted-foreground">
            <p>JPG, GIF or PNG. Max size 2MB</p>
          </div>
        </div>

        <DialogFooter>
          <Button variant="outline" @click="isAvatarUploadOpen = false">
            Cancel
          </Button>
          <Button @click="saveAvatar">
            Save Changes
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import {
  UserIcon,
  ShoppingBagIcon,
  HeartIcon,
  Cog6ToothIcon,
  PencilIcon,
  TrashIcon,
  CheckIcon,
  PlusIcon,
  TruckIcon,
  ArrowPathIcon
} from '@heroicons/vue/24/outline';
import {
  Tabs,
  TabsContent,
  TabsList,
  TabsTrigger
} from '@/components/ui/tabs';
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle
} from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Textarea } from '@/components/ui/textarea';
import { Separator } from '@/components/ui/separator';
import { Switch } from '@/components/ui/switch';
import { Badge } from '@/components/ui/badge';
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar';
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle
} from '@/components/ui/dialog';
import ProductCard from '@/pages/customer/products/ProductCard.vue';
import type { Product } from '@/types/product.ts';

const router = useRouter();
const activeTab = ref('profile');
const isEditing = ref(false);
const isAddressModalOpen = ref(false);
const isAvatarUploadOpen = ref(false);
const editingAddressIndex = ref<number | null>(null);
const newAvatar = ref<string | null>(null);

// User data - in a real app, this would come from an API
const user = reactive({
  name: 'John Doe',
  email: 'john.doe@example.com',
  phone: '+1 (555) 123-4567',
  bio: 'Tech enthusiast and avid online shopper',
  birthday: '1990-01-01',
  memberSince: '2023-01-15',
  avatar: 'https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=200&h=200&fit=crop&crop=face',
  addresses: [
    {
      name: 'Home',
      street: '123 Main St',
      city: 'Anytown',
      state: 'CA',
      zip: '12345',
      country: 'United States',
      phone: '+1 (555) 123-4567',
      isDefault: true
    },
    {
      name: 'Work',
      street: '456 Business Ave',
      city: 'Somewhere',
      state: 'NY',
      zip: '54321',
      country: 'United States',
      phone: '+1 (555) 987-6543',
      isDefault: false
    }
  ],
  orders: [
    {
      id: 'ORD-001',
      date: '2024-01-15',
      status: 'Delivered',
      total: 299.99,
      items: [
        {
          id: '1',
          name: 'Wireless Headphones',
          image: 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=100&h=100&fit=crop'
        },
        {
          id: '2',
          name: 'USB Cable',
          image: 'https://images.unsplash.com/photo-1587825140708-dfaf72ae4b04?w=100&h=100&fit=crop'
        }
      ]
    },
    {
      id: 'ORD-002',
      date: '2024-01-10',
      status: 'Shipped',
      total: 159.99,
      items: [
        {
          id: '3',
          name: 'Bluetooth Speaker',
          image: 'https://images.unsplash.com/photo-1572569511254-d8f925fe2cbb?w=100&h=100&fit=crop'
        }
      ]
    },
    {
      id: 'ORD-003',
      date: '2024-01-05',
      status: 'Processing',
      total: 89.98,
      items: [
        {
          id: '4',
          name: 'Phone Case',
          image: 'https://images.unsplash.com/photo-1603313018859-15c3ba333b75?w=100&h=100&fit=crop'
        },
        {
          id: '5',
          name: 'Screen Protector',
          image: 'https://images.unsplash.com/photo-1601784553446-aa02c9acbf37?w=100&h=100&fit=crop'
        }
      ]
    }
  ],
  wishlist: [
    {
      id: '6',
      name: 'Smart Watch Pro',
      price: 399.99,
      image: 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=300&h=300&fit=crop',
      category: 'Electronics',
      rating: 4.8,
      reviewCount: 215,
      inStock: true,
      badge: 'New'
    },
    {
      id: '7',
      name: 'Gaming Keyboard',
      price: 159.99,
      image: 'https://images.unsplash.com/photo-1541140532154-b024d705b90a?w=300&h=300&fit=crop',
      category: 'Electronics',
      rating: 4.5,
      reviewCount: 142,
      inStock: true
    },
    {
      id: '8',
      name: 'Wireless Earbuds',
      price: 129.99,
      image: 'https://images.unsplash.com/photo-1572569511254-d8f925fe2cbb?w=300&h=300&fit=crop',
      category: 'Electronics',
      rating: 4.2,
      reviewCount: 89,
      inStock: true,
      badge: 'Sale'
    }
  ]
});

// Form data
const formData = reactive({
  name: '',
  email: '',
  phone: '',
  bio: '',
  birthday: ''
});

// Address form
const addressForm = reactive({
  name: '',
  street: '',
  city: '',
  state: '',
  zip: '',
  country: '',
  phone: '',
  isDefault: false
});

// Settings
const settings = reactive({
  emailNotifications: {
    orderUpdates: true,
    promotions: true,
    newsletter: false
  },
  security: {
    twoFactorAuth: false,
    loginNotifications: true
  }
});

// Initialize form data
const initFormData = () => {
  formData.name = user.name;
  formData.email = user.email;
  formData.phone = user.phone;
  formData.bio = user.bio;
  formData.birthday = user.birthday;
};

// Save profile
const saveProfile = () => {
  user.name = formData.name;
  user.email = formData.email;
  user.phone = formData.phone;
  user.bio = formData.bio;
  user.birthday = formData.birthday;
  isEditing.value = false;
  // In a real app, you would save to an API here
  console.log('Profile saved:', formData);
};

// Cancel editing
const cancelEdit = () => {
  initFormData();
  isEditing.value = false;
};

// Open address modal
const openAddressModal = (index: number | null = null) => {
  editingAddressIndex.value = index;

  if (index !== null) {
    // Edit existing address
    const address = user.addresses[index];
    Object.assign(addressForm, address);
  } else {
    // Add new address
    resetAddressForm();
  }

  isAddressModalOpen.value = true;
};

// Reset address form
const resetAddressForm = () => {
  addressForm.name = '';
  addressForm.street = '';
  addressForm.city = '';
  addressForm.state = '';
  addressForm.zip = '';
  addressForm.country = '';
  addressForm.phone = '';
  addressForm.isDefault = false;
};

// Save address
const saveAddress = () => {
  const address = { ...addressForm };

  if (address.isDefault) {
    // Remove default flag from other addresses
    user.addresses.forEach(addr => {
      addr.isDefault = false;
    });
  }

  if (editingAddressIndex.value !== null) {
    // Update existing address
    user.addresses[editingAddressIndex.value] = address;
  } else {
    // Add new address
    user.addresses.push(address);
  }

  isAddressModalOpen.value = false;
  resetAddressForm();
};

// Edit address
const editAddress = (address: any) => {
  const index = user.addresses.findIndex(addr => addr.name === address.name);
  if (index !== -1) {
    openAddressModal(index);
  }
};

// Remove address
const removeAddress = (index: number) => {
  if (confirm('Are you sure you want to remove this address?')) {
    user.addresses.splice(index, 1);
  }
};

// Set default address
const setDefaultAddress = (index: number) => {
  user.addresses.forEach((addr, i) => {
    addr.isDefault = i === index;
  });
};

// Open avatar upload
const openAvatarUpload = () => {
  newAvatar.value = null;
  isAvatarUploadOpen.value = true;
};

// Handle avatar upload
const handleAvatarUpload = (event: Event) => {
  const input = event.target as HTMLInputElement;
  if (input.files && input.files[0]) {
    const file = input.files[0];
    const reader = new FileReader();

    reader.onload = (e) => {
      newAvatar.value = e.target?.result as string;
    };

    reader.readAsDataURL(file);
  }
};

// Save avatar
const saveAvatar = () => {
  if (newAvatar.value) {
    user.avatar = newAvatar.value;
  }
  isAvatarUploadOpen.value = false;
};

// Save settings
const saveSettings = () => {
  // In a real app, you would save to an API here
  console.log('Settings saved:', settings);
};

// Format date
const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  });
};

// Get status variant for order status badge
const getStatusVariant = (status: string) => {
  switch (status.toLowerCase()) {
    case 'delivered':
      return 'default';
    case 'shipped':
      return 'secondary';
    case 'processing':
      return 'outline';
    case 'cancelled':
      return 'destructive';
    default:
      return 'outline';
  }
};

// Add to cart function (for wishlist)
const addToCart = (product: Product) => {
  // In a real app, you would add to cart using a store
  console.log('Added to cart:', product);
};

// Initialize component
onMounted(() => {
  initFormData();
});
</script>
