<template>
  <div class="order-tracking">
    <div class="container">
      <!-- Page Header -->
      <div class="page-header">
        <h1>Track Your Order</h1>
        <p class="subtitle">Enter your order details to track your shipment</p>
      </div>

      <div class="tracking-container">
        <!-- Tracking Form -->
        <div class="tracking-form-container">
          <div class="tracking-card">
            <h2>Track an Order</h2>
            <p class="form-description">
              Enter your order number and email address to track your order status and view shipping updates.
            </p>
            
            <form @submit.prevent="trackOrder" class="tracking-form">
              <div class="form-group">
                <label for="orderNumber">Order Number *</label>
                <input
                  type="text"
                  id="orderNumber"
                  v-model="trackingForm.orderNumber"
                  placeholder="e.g., ORD-123456"
                  required
                  :class="{ 'error': errors.orderNumber }"
                  @input="clearError('orderNumber')"
                >
                <div v-if="errors.orderNumber" class="error-message">{{ errors.orderNumber }}</div>
              </div>
              
              <div class="form-group">
                <label for="email">Email Address *</label>
                <input
                  type="email"
                  id="email"
                  v-model="trackingForm.email"
                  placeholder="email@example.com"
                  required
                  :class="{ 'error': errors.email }"
                  @input="clearError('email')"
                >
                <div v-if="errors.email" class="error-message">{{ errors.email }}</div>
              </div>
              
              <div class="form-actions">
                <button type="submit" class="btn btn-primary" :disabled="isLoading">
                  <span v-if="isLoading" class="spinner"></span>
                  <span v-else>Track Order</span>
                </button>
              </div>
              
              <div class="help-link">
                <router-link to="/account/orders">View your order history</router-link> or
                <router-link to="/contact">contact support</router-link> if you need assistance.
              </div>
            </form>
          </div>
          
          <!-- Tracking Help -->
          <div class="tracking-help">
            <h3>Need Help Tracking Your Order?</h3>
            <ul class="help-list">
              <li>Your order number was included in your confirmation email.</li>
              <li>Please allow 24-48 hours for tracking information to update after your order ships.</li>
              <li>For international orders, tracking may take longer to update.</li>
              <li>If you're having trouble, contact our <router-link to="/contact">customer support</router-link>.</li>
            </ul>
          </div>
        </div>
        
        <!-- Tracking Results -->
        <div v-if="order" class="tracking-results">
          <div class="order-summary">
            <div class="order-header">
              <h2>Order #{{ order.orderNumber }}</h2>
              <div class="order-date">Placed on {{ formatDate(order.orderDate) }}</div>
            </div>
            
            <div class="delivery-status">
              <div class="status-header">
                <div class="status-badge" :class="getStatusClass(order.status)">
                  {{ formatStatus(order.status) }}
                </div>
                <div v-if="order.estimatedDelivery" class="estimated-delivery">
                  Estimated delivery: <strong>{{ formatDeliveryDate(order.estimatedDelivery) }}</strong>
                </div>
              </div>
              
              <!-- Tracking Timeline -->
              <div class="timeline">
                <div 
                  v-for="(event, index) in trackingEvents" 
                  :key="index"
                  class="timeline-event"
                  :class="{ 
                    'active': isEventActive(event.status),
                    'completed': isEventCompleted(event.status)
                  }"
                >
                  <div class="timeline-dot"></div>
                  <div class="timeline-content">
                    <div class="event-title">{{ event.title }}</div>
                    <div class="event-date" v-if="event.date">{{ formatEventDate(event.date) }}</div>
                    <div class="event-location" v-if="event.location">{{ event.location }}</div>
                    <div class="event-description" v-if="event.description">{{ event.description }}</div>
                  </div>
                </div>
              </div>
            </div>
            
            <!-- Tracking Number -->
            <div v-if="order.trackingNumber" class="tracking-number">
              <div class="tracking-number-label">Tracking Number:</div>
              <div class="tracking-number-value">{{ order.trackingNumber }}</div>
              <div class="tracking-carrier">
                Carrier: {{ order.shippingCarrier || 'Standard Shipping' }}
                <a 
                  v-if="order.trackingUrl" 
                  :href="order.trackingUrl" 
                  target="_blank" 
                  class="tracking-link"
                >
                  Track on carrier's website <span>↗</span>
                </a>
              </div>
            </div>
            
            <!-- Shipping Address -->
            <div class="shipping-address">
              <h3>Shipping Address</h3>
              <address>
                {{ order.shippingAddress.name }}<br>
                {{ order.shippingAddress.street }}<br>
                {{ order.shippingAddress.city }}, {{ order.shippingAddress.state }} {{ order.shippingAddress.zip }}<br>
                {{ order.shippingAddress.country }}
              </address>
            </div>
            
            <!-- Order Items -->
            <div class="order-items">
              <h3>Order Items</h3>
              <div v-for="(item, index) in order.items" :key="index" class="order-item">
                <div class="item-image">
                  <img :src="item.image" :alt="item.name">
                </div>
                <div class="item-details">
                  <h4>{{ item.name }}</h4>
                  <div v-if="item.variant" class="variant-details">
                    <span v-for="(value, key) in item.variant" :key="key" class="variant">
                      {{ key }}: {{ value }}
                    </span>
                  </div>
                  <div class="item-quantity">Quantity: {{ item.quantity }}</div>
                </div>
                <div class="item-status" :class="getItemStatusClass(item.status)">
                  {{ formatStatus(item.status) }}
                </div>
              </div>
            </div>
            
            <!-- Order Actions -->
            <div class="order-actions">
              <router-link 
                :to="`/account/orders/${order.id}`" 
                class="btn btn-outline"
              >
                View Order Details
              </router-link>
              <button 
                v-if="canReorderOrder(order)"
                @click="reorderItems(order)" 
                class="btn btn-primary"
              >
                Reorder Items
              </button>
              <button 
                v-if="canReturnOrder(order)"
                @click="initiateReturn(order)" 
                class="btn btn-outline"
              >
                Start a Return
              </button>
              <button 
                v-if="canCancelOrder(order)"
                @click="cancelOrder(order)" 
                class="btn btn-outline"
              >
                Cancel Order
              </button>
            </div>
          </div>
          
          <!-- Shipping Carrier Info -->
          <div v-if="shippingCarrierInfo" class="carrier-info">
            <h3>About {{ shippingCarrierInfo.name }}</h3>
            <div class="carrier-details">
              <div class="carrier-logo" v-if="shippingCarrierInfo.logo">
                <img :src="shippingCarrierInfo.logo" :alt="shippingCarrierInfo.name">
              </div>
              <div class="carrier-description">
                <p>{{ shippingCarrierInfo.description }}</p>
                <div class="carrier-contact" v-if="shippingCarrierInfo.phone || shippingCarrierInfo.website">
                  <div v-if="shippingCarrierInfo.phone" class="contact-method">
                    <span class="contact-icon">📞</span>
                    <a :href="'tel:' + shippingCarrierInfo.phone">{{ shippingCarrierInfo.phone }}</a>
                  </div>
                  <div v-if="shippingCarrierInfo.website" class="contact-method">
                    <span class="contact-icon">🌐</span>
                    <a :href="shippingCarrierInfo.website" target="_blank">{{ shippingCarrierInfo.website }}</a>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        
        <!-- No Results -->
        <div v-else-if="searchPerformed && !isLoading" class="no-results">
          <div class="no-results-icon">🔍</div>
          <h3>Order Not Found</h3>
          <p>We couldn't find an order matching the information provided. Please check your order number and email address and try again.</p>
          <div class="suggestions">
            <p>If you need assistance, please:</p>
            <ul>
              <li>Verify your order number and email address are correct</li>
              <li>Check your email for the order confirmation</li>
              <li>Contact our <router-link to="/contact">customer support</router-link> for help</li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';

type OrderStatus = 'pending' | 'processing' | 'shipped' | 'delivered' | 'cancelled' | 'returned';

interface OrderItem {
  id: string | number;
  name: string;
  price: number;
  quantity: number;
  image: string;
  variant?: Record<string, string>;
  status: OrderStatus;
}

interface ShippingAddress {
  name: string;
  street: string;
  city: string;
  state: string;
  zip: string;
  country: string;
}

interface Order {
  id: string | number;
  orderNumber: string;
  orderDate: string | Date;
  status: OrderStatus;
  items: OrderItem[];
  shippingAddress: ShippingAddress;
  trackingNumber?: string;
  trackingUrl?: string;
  shippingCarrier?: string;
  estimatedDelivery?: string | Date;
  carrierService?: string;
}

interface TrackingEvent {
  status: OrderStatus;
  title: string;
  date?: string | Date;
  location?: string;
  description?: string;
}

export default defineComponent({
  name: 'OrderTracking',
  setup() {
    const route = useRoute();
    const router = useRouter();
    
    // Form state
    const trackingForm = ref({
      orderNumber: '',
      email: ''
    });
    
    const errors = ref<Record<string, string>>({});
    const isLoading = ref(false);
    const order = ref<Order | null>(null);
    const searchPerformed = ref(false);
    
    // Mock data for demonstration
    const mockOrders: Order[] = [
      {
        id: 'ORD-1001',
        orderNumber: 'ORD-1001',
        orderDate: '2025-09-20T10:30:00',
        status: 'shipped',
        items: [
          {
            id: 'P001',
            name: 'Wireless Noise-Cancelling Headphones',
            price: 249.99,
            quantity: 1,
            image: 'https://via.placeholder.com/100x100?text=Headphones',
            variant: { Color: 'Black' },
            status: 'shipped'
          },
          {
            id: 'P002',
            name: 'USB-C Charging Cable',
            price: 19.99,
            quantity: 2,
            image: 'https://via.placeholder.com/100x100?text=Cable',
            status: 'shipped'
          }
        ],
        shippingAddress: {
          name: 'John Doe',
          street: '123 Main St',
          city: 'New York',
          state: 'NY',
          zip: '10001',
          country: 'United States'
        },
        trackingNumber: '1Z999AA1234567890',
        trackingUrl: 'https://www.ups.com/track?tracknum=1Z999AA1234567890',
        shippingCarrier: 'UPS',
        estimatedDelivery: '2025-09-27',
        carrierService: 'UPS Ground'
      },
      {
        id: 'ORD-1000',
        orderNumber: 'ORD-1000',
        orderDate: '2025-09-15T14:20:00',
        status: 'delivered',
        items: [
          {
            id: 'P003',
            name: 'Smartphone 13 Pro',
            price: 999.99,
            quantity: 1,
            image: 'https://via.placeholder.com/100x100?text=Smartphone',
            variant: { Color: 'Midnight', Storage: '256GB' },
            status: 'delivered'
          }
        ],
        shippingAddress: {
          name: 'John Doe',
          street: '123 Main St',
          city: 'New York',
          state: 'NY',
          zip: '10001',
          country: 'United States'
        },
        trackingNumber: '1Z999AA1234567891',
        trackingUrl: 'https://www.ups.com/track?tracknum=1Z999AA1234567891',
        shippingCarrier: 'UPS',
        estimatedDelivery: '2025-09-25',
        carrierService: 'UPS 2nd Day Air'
      }
    ];
    
    // Shipping carrier information
    const shippingCarriers = {
      'UPS': {
        name: 'UPS',
        logo: 'https://www.ups.com/assets/resources/images/UPS_logo.svg',
        description: 'United Parcel Service (UPS) is a global leader in logistics, offering a broad range of solutions for the transportation of packages and freight.',
        phone: '1-800-742-5877',
        website: 'https://www.ups.com'
      },
      'FedEx': {
        name: 'FedEx',
        logo: 'https://www.fedex.com/content/dam/fedex-com/logos/logo.png',
        description: 'FedEx Corporation is an American multinational delivery services company known for its overnight shipping service and pioneering a system that could track packages and provide real-time updates on package location.',
        phone: '1-800-463-3339',
        website: 'https://www.fedex.com'
      },
      'USPS': {
        name: 'USPS',
        logo: 'https://www.usps.com/assets/images/home/usps-logo-2x.png',
        description: 'The United States Postal Service (USPS) is an independent agency of the executive branch of the United States federal government responsible for providing postal service in the United States.',
        phone: '1-800-275-8777',
        website: 'https://www.usps.com'
      },
      'DHL': {
        name: 'DHL',
        logo: 'https://www.dhl.com/content/dam/dhl/global/core/images/logos/dhl-logo.svg',
        description: 'DHL is a German international courier, package delivery and express mail service which is a division of the German logistics firm Deutsche Post.',
        phone: '1-800-225-5345',
        website: 'https://www.dhl.com'
      }
    };
    
    // Computed
    const shippingCarrierInfo = computed(() => {
      if (!order.value?.shippingCarrier) return null;
      return shippingCarriers[order.value.shippingCarrier as keyof typeof shippingCarriers] || null;
    });
    
    const trackingEvents = computed<TrackingEvent[]>(() => {
      if (!order.value) return [];
      
      const events: TrackingEvent[] = [
        {
          status: 'pending',
          title: 'Order Placed',
          date: order.value.orderDate,
          description: 'We\'ve received your order.'
        },
        {
          status: 'processing',
          title: 'Order Processed',
          description: 'We\'re preparing your order for shipment.'
        },
        {
          status: 'shipped',
          title: 'Shipped',
          description: order.value.carrierService 
            ? `Shipped via ${order.value.shippingCarrier} ${order.value.carrierService}`
            : 'Your order is on its way.',
          location: 'New York, NY'
        },
        {
          status: 'delivered',
          title: 'Delivered',
          description: 'Your order has been delivered.'
        }
      ];
      
      // Add tracking events based on order status
      if (order.value.status === 'delivered') {
        events[1].date = new Date(order.value.orderDate);
        events[1].date.setHours(events[1].date.getHours() + 2);
        
        events[2].date = new Date(order.value.orderDate);
        events[2].date.setDate(events[2].date.getDate() + 1);
        
        events[3].date = order.value.estimatedDelivery || new Date();
      } else if (order.value.status === 'shipped') {
        events[1].date = new Date(order.value.orderDate);
        events[1].date.setHours(events[1].date.getHours() + 2);
        
        events[2].date = new Date();
        events[2].date.setDate(events[2].date.getDate() - 1);
      } else if (order.value.status === 'processing') {
        events[1].date = new Date();
      }
      
      // Filter out future events for pending/processing orders
      return events.filter((event, index, array) => {
        if (event.status === 'delivered' && order.value?.status !== 'delivered') {
          return false;
        }
        return true;
      });
    });
    
    // Methods
    const validateForm = (): boolean => {
      const newErrors: Record<string, string> = {};
      
      if (!trackingForm.value.orderNumber.trim()) {
        newErrors.orderNumber = 'Order number is required';
      } else if (!/^[A-Za-z0-9-]+$/.test(trackingForm.value.orderNumber)) {
        newErrors.orderNumber = 'Please enter a valid order number';
      }
      
      if (!trackingForm.value.email.trim()) {
        newErrors.email = 'Email address is required';
      } else if (!/\S+@\S+\.\S+/.test(trackingForm.value.email)) {
        newErrors.email = 'Please enter a valid email address';
      }
      
      errors.value = newErrors;
      return Object.keys(newErrors).length === 0;
    };
    
    const clearError = (field: string) => {
      if (errors.value[field]) {
        const newErrors = { ...errors.value };
        delete newErrors[field];
        errors.value = newErrors;
      }
    };
    
    const trackOrder = async () => {
      if (!validateForm()) return;
      
      try {
        isLoading.value = true;
        searchPerformed.value = true;
        
        // Simulate API call
        await new Promise(resolve => setTimeout(resolve, 1000));
        
        // In a real app, this would be an API call to fetch order by number and email
        const foundOrder = mockOrders.find(
          order => order.orderNumber === trackingForm.value.orderNumber.toUpperCase()
        );
        
        if (foundOrder) {
          order.value = foundOrder;
        } else {
          order.value = null;
        }
      } catch (error) {
        console.error('Failed to track order:', error);
        // In a real app, show an error message to the user
      } finally {
        isLoading.value = false;
      }
    };
    
    const formatDate = (date: string | Date): string => {
      const options: Intl.DateTimeFormatOptions = { 
        year: 'numeric', 
        month: 'long', 
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      };
      return new Date(date).toLocaleDateString('en-US', options);
    };
    
    const formatDeliveryDate = (date: string | Date): string => {
      const options: Intl.DateTimeFormatOptions = { 
        weekday: 'long',
        month: 'long', 
        day: 'numeric',
        year: 'numeric'
      };
      return new Date(date).toLocaleDateString('en-US', options);
    };
    
    const formatEventDate = (date: string | Date): string => {
      const options: Intl.DateTimeFormatOptions = { 
        month: 'short', 
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
        hour12: true
      };
      return new Date(date).toLocaleDateString('en-US', options);
    };
    
    const formatStatus = (status: OrderStatus): string => {
      const statusMap: Record<OrderStatus, string> = {
        pending: 'Pending',
        processing: 'Processing',
        shipped: 'Shipped',
        delivered: 'Delivered',
        cancelled: 'Cancelled',
        returned: 'Returned'
      };
      return statusMap[status] || status;
    };
    
    const getStatusClass = (status: OrderStatus): string => {
      return `status-${status}`;
    };
    
    const getItemStatusClass = (status: OrderStatus): string => {
      return getStatusClass(status);
    };
    
    const isEventActive = (status: OrderStatus): boolean => {
      if (!order.value) return false;
      
      const statusOrder: OrderStatus[] = ['pending', 'processing', 'shipped', 'delivered'];
      const currentStatusIndex = statusOrder.indexOf(order.value.status);
      const eventStatusIndex = statusOrder.indexOf(status);
      
      return eventStatusIndex === currentStatusIndex;
    };
    
    const isEventCompleted = (status: OrderStatus): boolean => {
      if (!order.value) return false;
      
      const statusOrder: OrderStatus[] = ['pending', 'processing', 'shipped', 'delivered'];
      const currentStatusIndex = statusOrder.indexOf(order.value.status);
      const eventStatusIndex = statusOrder.indexOf(status);
      
      return eventStatusIndex >= 0 && eventStatusIndex < currentStatusIndex;
    };
    
    const canReorderOrder = (order: Order): boolean => {
      return ['delivered', 'cancelled', 'returned'].includes(order.status);
    };
    
    const canReturnOrder = (order: Order): boolean => {
      // Typically, returns are allowed within 30 days of delivery
      if (order.status !== 'delivered') return false;
      
      const deliveryDate = order.estimatedDelivery ? new Date(order.estimatedDelivery) : new Date();
      const thirtyDaysAgo = new Date();
      thirtyDaysAgo.setDate(thirtyDaysAgo.getDate() - 30);
      
      return deliveryDate >= thirtyDaysAgo;
    };
    
    const canCancelOrder = (order: Order): boolean => {
      return ['pending', 'processing'].includes(order.status);
    };
    
    const reorderItems = (order: Order) => {
      // In a real app, this would add all items to the cart
      console.log('Reordering items:', order.items);
      // For demo purposes, we'll just navigate to the cart
      router.push('/cart');
    };
    
    const initiateReturn = (order: Order) => {
      // In a real app, this would navigate to a returns page or open a modal
      console.log('Initiating return for order:', order.id);
      router.push(`/account/orders/${order.id}/return`);
    };
    
    const cancelOrder = (order: Order) => {
      // In a real app, this would open a cancellation confirmation modal
      console.log('Cancelling order:', order.id);
      // For demo purposes, we'll just update the status
      order.status = 'cancelled';
    };
    
    // Lifecycle hooks
    onMounted(() => {
      // Check for order number in query params
      if (route.query.orderNumber) {
        trackingForm.value.orderNumber = route.query.orderNumber as string;
        
        // Auto-submit if email is also provided
        if (route.query.email) {
          trackingForm.value.email = route.query.email as string;
          trackOrder();
        }
      }
    });
    
    return {
      // State
      trackingForm,
      errors,
      isLoading,
      order,
      searchPerformed,
      shippingCarrierInfo,
      trackingEvents,
      
      // Methods
      trackOrder,
      clearError,
      formatDate,
      formatDeliveryDate,
      formatEventDate,
      formatStatus,
      getStatusClass,
      getItemStatusClass,
      isEventActive,
      isEventCompleted,
      canReorderOrder,
      canReturnOrder,
      canCancelOrder,
      reorderItems,
      initiateReturn,
      cancelOrder
    };
  }
});
</script>

<style scoped>
/* Base styles */
.order-tracking {
  padding: 30px 0 60px;
  background-color: #f8f9fa;
  min-height: calc(100vh - 200px);
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.page-header {
  margin-bottom: 30px;
  text-align: center;
}

.page-header h1 {
  font-size: 2.2rem;
  color: #2c3e50;
  margin-bottom: 10px;
}

.subtitle {
  font-size: 1.1rem;
  color: #6c757d;
  margin: 0;
}

/* Tracking Form */
.tracking-container {
  max-width: 1000px;
  margin: 0 auto;
}

.tracking-form-container {
  margin-bottom: 40px;
}

.tracking-card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 15px rgba(0, 0, 0, 0.05);
  padding: 30px;
  margin-bottom: 20px;
}

.tracking-card h2 {
  margin-top: 0;
  margin-bottom: 15px;
  font-size: 1.5rem;
  color: #2c3e50;
}

.form-description {
  color: #6c757d;
  margin-bottom: 25px;
  line-height: 1.6;
}

.tracking-form {
  max-width: 600px;
  margin: 0 auto;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #2c3e50;
}

.form-group input[type="text"],
.form-group input[type="email"] {
  width: 100%;
  padding: 12px 15px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
  transition: border-color 0.2s, box-shadow 0.2s;
}

.form-group input:focus {
  outline: none;
  border-color: #3498db;
  box-shadow: 0 0 0 2px rgba(52, 152, 219, 0.2);
}

.form-group input.error {
  border-color: #e74c3c;
}

.error-message {
  color: #e74c3c;
  font-size: 0.85rem;
  margin-top: 5px;
}

.form-actions {
  margin-top: 30px;
  text-align: center;
}

.btn {
  padding: 12px 25px;
  border-radius: 4px;
  font-size: 1rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
  text-decoration: none;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  border: 1px solid transparent;
}

.btn-primary {
  background-color: #3498db;
  color: white;
  border: none;
  min-width: 180px;
}

.btn-primary:hover:not(:disabled) {
  background-color: #2980b9;
}

.btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.help-link {
  margin-top: 20px;
  text-align: center;
  color: #6c757d;
  font-size: 0.95rem;
}

.help-link a {
  color: #3498db;
  text-decoration: none;
}

.help-link a:hover {
  text-decoration: underline;
}

/* Tracking Help */
.tracking-help {
  background: #f8f9fa;
  border-radius: 8px;
  padding: 20px;
  border: 1px dashed #ddd;
}

.tracking-help h3 {
  margin-top: 0;
  margin-bottom: 15px;
  font-size: 1.2rem;
  color: #2c3e50;
}

.help-list {
  margin: 0;
  padding-left: 20px;
}

.help-list li {
  margin-bottom: 8px;
  line-height: 1.5;
  color: #6c757d;
}

.help-list a {
  color: #3498db;
  text-decoration: none;
}

.help-list a:hover {
  text-decoration: underline;
}

/* Tracking Results */
.tracking-results {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 15px rgba(0, 0, 0, 0.05);
  overflow: hidden;
}

.order-summary {
  padding: 30px;
}

.order-header {
  margin-bottom: 25px;
  padding-bottom: 15px;
  border-bottom: 1px solid #f0f0f0;
}

.order-header h2 {
  margin: 0 0 5px;
  font-size: 1.5rem;
  color: #2c3e50;
}

.order-date {
  color: #6c757d;
  font-size: 0.95rem;
}

/* Delivery Status */
.delivery-status {
  margin-bottom: 30px;
}

.status-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.status-badge {
  display: inline-block;
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 0.85rem;
  font-weight: 600;
  text-transform: capitalize;
}

.status-pending {
  background-color: #fff3cd;
  color: #856404;
}

.status-processing {
  background-color: #cce5ff;
  color: #004085;
}

.status-shipped {
  background-color: #d4edda;
  color: #155724;
}

.status-delivered {
  background-color: #e2e3e5;
  color: #383d41;
}

.status-cancelled {
  background-color: #f8d7da;
  color: #721c24;
}

.status-returned {
  background-color: #e2e3e5;
  color: #383d41;
  text-decoration: line-through;
}

.estimated-delivery {
  color: #2c3e50;
  font-size: 0.95rem;
}

/* Timeline */
.timeline {
  position: relative;
  padding-left: 30px;
  margin: 30px 0;
}

.timeline::before {
  content: '';
  position: absolute;
  top: 0;
  bottom: 0;
  left: 9px;
  width: 2px;
  background-color: #e9ecef;
  z-index: 1;
}

.timeline-event {
  position: relative;
  padding-bottom: 30px;
  padding-left: 30px;
}

.timeline-event:last-child {
  padding-bottom: 0;
}

.timeline-dot {
  position: absolute;
  left: -30px;
  top: 5px;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  background-color: #e9ecef;
  border: 3px solid white;
  z-index: 2;
}

.timeline-event.completed .timeline-dot {
  background-color: #28a745;
  border-color: white;
}

.timeline-event.active .timeline-dot {
  background-color: #007bff;
  border-color: white;
  box-shadow: 0 0 0 3px rgba(0, 123, 255, 0.25);
}

.timeline-content {
  position: relative;
  background: #f8f9fa;
  border-radius: 6px;
  padding: 15px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}

.timeline-event.completed .timeline-content {
  background-color: #f8f9fa;
}

.timeline-event.active .timeline-content {
  background-color: #e7f1ff;
  border-left: 3px solid #007bff;
}

.event-title {
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 5px;
}

.event-date {
  font-size: 0.85rem;
  color: #6c757d;
  margin-bottom: 5px;
}

.event-location {
  font-size: 0.85rem;
  color: #2c3e50;
  font-weight: 500;
  margin-bottom: 5px;
}

.event-description {
  font-size: 0.9rem;
  color: #6c757d;
  line-height: 1.5;
  margin-top: 8px;
}

/* Tracking Number */
.tracking-number {
  background: #f8f9fa;
  border-radius: 6px;
  padding: 15px;
  margin-bottom: 25px;
  border: 1px solid #e9ecef;
}

.tracking-number-label {
  font-size: 0.9rem;
  color: #6c757d;
  margin-bottom: 5px;
}

.tracking-number-value {
  font-family: 'Courier New', monospace;
  font-size: 1.2rem;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 10px;
  letter-spacing: 0.5px;
}

.tracking-carrier {
  display: flex;
  align-items: center;
  font-size: 0.9rem;
  color: #6c757d;
}

.tracking-link {
  margin-left: auto;
  color: #3498db;
  text-decoration: none;
  display: flex;
  align-items: center;
  font-weight: 500;
}

.tracking-link:hover {
  text-decoration: underline;
}

.tracking-link span {
  margin-left: 3px;
  font-size: 0.9em;
}

/* Shipping Address */
.shipping-address {
  margin-bottom: 30px;
  padding-bottom: 25px;
  border-bottom: 1px solid #f0f0f0;
}

.shipping-address h3,
.order-items h3 {
  margin-top: 0;
  margin-bottom: 15px;
  font-size: 1.2rem;
  color: #2c3e50;
}

address {
  font-style: normal;
  line-height: 1.6;
  color: #6c757d;
}

/* Order Items */
.order-items {
  margin-bottom: 30px;
}

.order-item {
  display: flex;
  padding: 15px 0;
  border-bottom: 1px solid #f0f0f0;
  align-items: center;
}

.order-item:last-child {
  border-bottom: none;
}

.item-image {
  width: 70px;
  height: 70px;
  margin-right: 15px;
  border: 1px solid #eee;
  border-radius: 4px;
  overflow: hidden;
  flex-shrink: 0;
}

.item-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.item-details {
  flex: 1;
  min-width: 0;
}

.item-details h4 {
  margin: 0 0 5px;
  font-size: 1rem;
  color: #2c3e50;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.variant-details {
  font-size: 0.85rem;
  color: #6c757d;
  margin-bottom: 5px;
}

.variant {
  display: block;
  margin-bottom: 2px;
}

.item-quantity {
  font-size: 0.9rem;
  color: #6c757d;
}

.item-status {
  margin-left: 15px;
  font-size: 0.85rem;
  font-weight: 500;
  padding: 4px 10px;
  border-radius: 12px;
  white-space: nowrap;
}

/* Order Actions */
.order-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  padding-top: 20px;
  border-top: 1px solid #f0f0f0;
}

.btn-outline {
  background: white;
  border-color: #3498db;
  color: #3498db;
}

.btn-outline:hover {
  background-color: #f0f8ff;
}

/* Carrier Info */
.carrier-info {
  background: #f8f9fa;
  border-radius: 8px;
  padding: 25px;
  margin-top: 30px;
  border: 1px solid #e9ecef;
}

.carrier-info h3 {
  margin-top: 0;
  margin-bottom: 15px;
  font-size: 1.2rem;
  color: #2c3e50;
}

.carrier-details {
  display: flex;
  gap: 20px;
  align-items: flex-start;
}

.carrier-logo {
  width: 100px;
  flex-shrink: 0;
}

.carrier-logo img {
  max-width: 100%;
  height: auto;
}

.carrier-description {
  flex: 1;
}

.carrier-description p {
  margin: 0 0 15px;
  color: #6c757d;
  line-height: 1.6;
}

.carrier-contact {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.contact-method {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #2c3e50;
}

.contact-icon {
  font-size: 1.1rem;
  opacity: 0.8;
}

.contact-method a {
  color: #3498db;
  text-decoration: none;
}

.contact-method a:hover {
  text-decoration: underline;
}

/* No Results */
.no-results {
  text-align: center;
  padding: 60px 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.no-results-icon {
  font-size: 3rem;
  margin-bottom: 20px;
  opacity: 0.7;
}

.no-results h3 {
  margin: 0 0 15px;
  color: #2c3e50;
  font-size: 1.5rem;
}

.no-results p {
  color: #6c757d;
  margin-bottom: 25px;
  font-size: 1.05rem;
  max-width: 600px;
  margin-left: auto;
  margin-right: auto;
}

.suggestions {
  margin-top: 25px;
  padding-top: 20px;
  border-top: 1px solid #f0f0f0;
  text-align: left;
  max-width: 500px;
  margin-left: auto;
  margin-right: auto;
}

.suggestions p {
  font-weight: 500;
  margin-bottom: 10px;
}

.suggestions ul {
  margin: 0;
  padding-left: 20px;
  color: #6c757d;
}

.suggestions li {
  margin-bottom: 5px;
  line-height: 1.5;
}

.suggestions a {
  color: #3498db;
  text-decoration: none;
}

.suggestions a:hover {
  text-decoration: underline;
}

/* Spinner */
.spinner {
  display: inline-block;
  width: 20px;
  height: 20px;
  border: 3px solid rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  border-top-color: white;
  animation: spin 1s ease-in-out infinite;
  margin-right: 8px;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

/* Responsive Styles */
@media (max-width: 992px) {
  .carrier-details {
    flex-direction: column;
  }
  
  .carrier-logo {
    margin-bottom: 15px;
  }
}

@media (max-width: 768px) {
  .page-header h1 {
    font-size: 1.8rem;
  }
  
  .tracking-card,
  .order-summary {
    padding: 20px;
  }
  
  .status-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
  
  .timeline {
    padding-left: 20px;
  }
  
  .timeline::before {
    left: 7px;
  }
  
  .timeline-dot {
    left: -23px;
    width: 16px;
    height: 16px;
  }
  
  .order-item {
    flex-wrap: wrap;
  }
  
  .item-status {
    margin-left: 0;
    margin-top: 10px;
    width: 100%;
  }
  
  .order-actions {
    flex-direction: column;
  }
  
  .order-actions .btn {
    width: 100%;
  }
}

@media (max-width: 576px) {
  .page-header h1 {
    font-size: 1.6rem;
  }
  
  .subtitle {
    font-size: 1rem;
  }
  
  .tracking-number-value {
    font-size: 1rem;
    word-break: break-all;
  }
  
  .tracking-carrier {
    flex-direction: column;
    align-items: flex-start;
    gap: 5px;
  }
  
  .tracking-link {
    margin-left: 0;
  }
  
  .event-title {
    font-size: 1rem;
  }
  
  .event-date,
  .event-location,
  .event-description {
    font-size: 0.8rem;
  }
}
</style>
