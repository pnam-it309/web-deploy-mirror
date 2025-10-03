<template>
  <div class="order-history">
    <div class="container">
      <div class="page-header">
        <h1>Order History</h1>
        <p class="subtitle">View and track your recent orders</p>
      </div>

      <div class="order-history-container">
        <!-- Order Filters -->
        <div class="order-filters">
          <div class="filter-group">
            <label for="time-range">Time Range:</label>
            <select id="time-range" v-model="filters.timeRange" @change="applyFilters">
              <option value="30">Last 30 days</option>
              <option value="90">Last 90 days</option>
              <option value="365">Last 12 months</option>
              <option value="all">All time</option>
            </select>
          </div>
          
          <div class="filter-group">
            <label for="status">Status:</label>
            <select id="status" v-model="filters.status" @change="applyFilters">
              <option value="all">All Orders</option>
              <option value="pending">Pending</option>
              <option value="processing">Processing</option>
              <option value="shipped">Shipped</option>
              <option value="delivered">Delivered</option>
              <option value="cancelled">Cancelled</option>
              <option value="refunded">Refunded</option>
            </select>
          </div>
          
          <div class="search-box">
            <input 
              type="text" 
              v-model="filters.search" 
              placeholder="Search by order #"
              @keyup.enter="applyFilters"
            >
            <button @click="applyFilters">
              <span class="search-icon">🔍</span>
            </button>
          </div>
        </div>

        <!-- Loading State -->
        <div v-if="isLoading" class="loading-state">
          <div class="spinner"></div>
          <p>Loading your orders...</p>
        </div>

        <!-- Empty State -->
        <div v-else-if="orders.length === 0" class="empty-state">
          <div class="empty-icon">📦</div>
          <h3>No orders found</h3>
          <p>You haven't placed any orders yet.</p>
          <router-link to="/products" class="btn btn-primary">Start Shopping</router-link>
        </div>

        <!-- Orders List -->
        <div v-else class="orders-list">
          <div v-for="order in orders" :key="order.id" class="order-card">
            <div class="order-header">
              <div class="order-meta">
                <h3>Order #{{ order.orderNumber }}</h3>
                <div class="order-date">Placed on {{ formatDate(order.orderDate) }}</div>
              </div>
              <div class="order-status" :class="getStatusClass(order.status)">
                {{ formatStatus(order.status) }}
              </div>
            </div>
            
            <div class="order-details">
              <div class="order-items">
                <div v-for="(item, index) in order.items" :key="index" class="order-item">
                  <div class="item-image">
                    <img :src="item.image" :alt="item.name">
                    <span class="quantity-badge">{{ item.quantity }}</span>
                  </div>
                  <div class="item-details">
                    <h4>{{ item.name }}</h4>
                    <div v-if="item.variant" class="variant-details">
                      <span v-for="(value, key) in item.variant" :key="key" class="variant">
                        {{ key }}: {{ value }}
                      </span>
                    </div>
                    <div class="item-price">{{ formatPrice(item.price) }}</div>
                  </div>
                </div>
              </div>
              
              <div class="order-summary">
                <div class="summary-row">
                  <span>Subtotal ({{ order.itemCount }} items)</span>
                  <span>{{ formatPrice(order.subtotal) }}</span>
                </div>
                <div v-if="order.discount > 0" class="summary-row">
                  <span>Discount</span>
                  <span class="discount">-{{ formatPrice(order.discount) }}</span>
                </div>
                <div class="summary-row">
                  <span>Shipping</span>
                  <span>{{ order.shippingCost === 0 ? 'Free' : formatPrice(order.shippingCost) }}</span>
                </div>
                <div class="summary-row total">
                  <span>Total</span>
                  <span>{{ formatPrice(order.total) }}</span>
                </div>
              </div>
            </div>
            
            <div class="order-actions">
              <router-link 
                :to="`/account/orders/${order.id}`" 
                class="btn btn-outline"
              >
                View Details
              </router-link>
              <button 
                v-if="canReorder(order.status)"
                @click="reorder(order)" 
                class="btn btn-outline"
              >
                Reorder
              </button>
              <button 
                v-if="canTrack(order.status)"
                @click="trackOrder(order)" 
                class="btn btn-outline"
              >
                Track Order
              </button>
              <button 
                v-if="canCancel(order.status)"
                @click="cancelOrder(order)" 
                class="btn btn-outline"
              >
                Cancel Order
              </button>
              <button 
                v-if="canReturn(order.status)"
                @click="initiateReturn(order)" 
                class="btn btn-outline"
              >
                Return Items
              </button>
            </div>
          </div>
          
          <!-- Pagination -->
          <div v-if="totalPages > 1" class="pagination">
            <button 
              :disabled="currentPage === 1"
              @click="changePage(currentPage - 1)"
              class="pagination-button"
            >
              &larr; Previous
            </button>
            
            <div class="page-numbers">
              <button 
                v-for="page in visiblePageNumbers" 
                :key="page"
                @click="changePage(page)"
                :class="{ active: currentPage === page }"
                class="page-number"
              >
                {{ page }}
              </button>
              
              <span v-if="showEllipsisAfter" class="ellipsis">...</span>
            </div>
            
            <button 
              :disabled="currentPage === totalPages"
              @click="changePage(currentPage + 1)"
              class="pagination-button"
            >
              Next &rarr;
            </button>
          </div>
        </div>
      </div>
    </div>
    
    <!-- Cancel Order Modal -->
    <div v-if="showCancelModal" class="modal-overlay" @click.self="closeCancelModal">
      <div class="modal">
        <div class="modal-header">
          <h3>Cancel Order #{{ selectedOrder?.orderNumber }}</h3>
          <button class="close-button" @click="closeCancelModal">&times;</button>
        </div>
        <div class="modal-body">
          <p>Are you sure you want to cancel this order?</p>
          <div v-if="selectedOrder" class="cancellation-reason">
            <label>Reason for cancellation:</label>
            <select v-model="cancellationReason">
              <option value="">Select a reason</option>
              <option value="found_cheaper">Found a better price elsewhere</option>
              <option value="shipping_too_long">Shipping is taking too long</option>
              <option value="changed_mind">Changed my mind</option>
              <option value="ordered_by_mistake">Ordered by mistake</option>
              <option value="other">Other (please specify)</option>
            </select>
            <textarea 
              v-if="cancellationReason === 'other'" 
              v-model="customReason" 
              placeholder="Please specify the reason..."
              rows="3"
            ></textarea>
          </div>
        </div>
        <div class="modal-footer">
          <button @click="closeCancelModal" class="btn btn-outline">Go Back</button>
          <button 
            @click="confirmCancelOrder" 
            :disabled="!cancellationReason || (cancellationReason === 'other' && !customReason)"
            class="btn btn-danger"
          >
            {{ isCancelling ? 'Cancelling...' : 'Confirm Cancellation' }}
          </button>
        </div>
      </div>
    </div>
    
    <!-- Return Modal -->
    <div v-if="showReturnModal" class="modal-overlay" @click.self="closeReturnModal">
      <div class="modal">
        <div class="modal-header">
          <h3>Return Items (Order #{{ selectedOrder?.orderNumber }})</h3>
          <button class="close-button" @click="closeReturnModal">&times;</button>
        </div>
        <div class="modal-body">
          <p>Select the items you would like to return:</p>
          
          <div v-if="selectedOrder" class="return-items">
            <div v-for="item in selectedOrder.items" :key="item.id" class="return-item">
              <div class="return-item-select">
                <input 
                  type="checkbox" 
                  :id="`return-${item.id}`" 
                  v-model="selectedReturnItems[item.id]"
                  :value="item"
                >
              </div>
              <div class="return-item-image">
                <img :src="item.image" :alt="item.name">
              </div>
              <div class="return-item-details">
                <h4>{{ item.name }}</h4>
                <div class="item-price">{{ formatPrice(item.price) }}</div>
                <div class="quantity-selector">
                  <label>Quantity:</label>
                  <select v-model="returnQuantities[item.id]" :disabled="!selectedReturnItems[item.id]">
                    <option 
                      v-for="n in item.quantity" 
                      :key="n" 
                      :value="n"
                    >
                      {{ n }}
                    </option>
                  </select>
                </div>
              </div>
            </div>
            
            <div class="return-reason">
              <label>Reason for return:</label>
              <select v-model="returnReason">
                <option value="">Select a reason</option>
                <option value="wrong_item">Wrong item was sent</option>
                <option value="defective">Item is defective or doesn't work</option>
                <option value="not_as_described">Item doesn't match description</option>
                <option value="no_longer_needed">No longer needed</option>
                <option value="better_price">Found better price elsewhere</option>
                <option value="other">Other (please specify)</option>
              </select>
              <textarea 
                v-if="returnReason === 'other'" 
                v-model="customReturnReason" 
                placeholder="Please specify the reason..."
                rows="3"
              ></textarea>
            </div>
            
            <div class="refund-method">
              <label>Refund method:</label>
              <div class="refund-options">
                <label class="radio-option">
                  <input 
                    type="radio" 
                    v-model="refundMethod" 
                    value="original" 
                    name="refund-method"
                  >
                  <span>Original payment method</span>
                </label>
                <label class="radio-option">
                  <input 
                    type="radio" 
                    v-model="refundMethod" 
                    value="store_credit" 
                    name="refund-method"
                  >
                  <span>Store credit (5% bonus)</span>
                </label>
              </div>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button @click="closeReturnModal" class="btn btn-outline">Cancel</button>
          <button 
            @click="submitReturn" 
            :disabled="!hasSelectedItems || !returnReason || (returnReason === 'other' && !customReturnReason)"
            class="btn btn-primary"
          >
            {{ isSubmittingReturn ? 'Processing...' : 'Submit Return Request' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';

type OrderStatus = 'pending' | 'processing' | 'shipped' | 'delivered' | 'cancelled' | 'refunded';

interface OrderItem {
  id: string | number;
  name: string;
  price: number;
  quantity: number;
  image: string;
  variant?: Record<string, string>;
  inStock: boolean;
  availableQuantity: number;
}

interface Order {
  id: string | number;
  orderNumber: string;
  orderDate: string | Date;
  status: OrderStatus;
  items: OrderItem[];
  itemCount: number;
  subtotal: number;
  discount: number;
  shippingCost: number;
  total: number;
  shippingAddress: {
    name: string;
    street: string;
    city: string;
    state: string;
    zip: string;
    country: string;
  };
  paymentMethod: string;
  trackingNumber?: string;
  estimatedDelivery?: string;
}

export default defineComponent({
  name: 'OrderHistory',
  setup() {
    const router = useRouter();
    
    // State
    const isLoading = ref(true);
    const orders = ref<Order[]>([]);
    const currentPage = ref(1);
    const itemsPerPage = 5;
    const totalOrders = ref(0);
    
    // Filters
    const filters = ref({
      timeRange: '30',
      status: 'all',
      search: ''
    });
    
    // Modal state
    const showCancelModal = ref(false);
    const showReturnModal = ref(false);
    const selectedOrder = ref<Order | null>(null);
    const isCancelling = ref(false);
    const cancellationReason = ref('');
    const customReason = ref('');
    const selectedReturnItems = ref<Record<string | number, boolean>>({});
    const returnQuantities = ref<Record<string | number, number>>({});
    const returnReason = ref('');
    const customReturnReason = ref('');
    const refundMethod = ref('original');
    const isSubmittingReturn = ref(false);
    
    // Computed
    const totalPages = computed(() => Math.ceil(totalOrders.value / itemsPerPage));
    
    const visiblePageNumbers = computed(() => {
      const maxVisiblePages = 5;
      const half = Math.floor(maxVisiblePages / 2);
      let start = Math.max(1, currentPage.value - half);
      const end = Math.min(totalPages.value, start + maxVisiblePages - 1);
      
      if (end - start + 1 < maxVisiblePages) {
        start = Math.max(1, end - maxVisiblePages + 1);
      }
      
      return Array.from({ length: end - start + 1 }, (_, i) => start + i);
    });
    
    const showEllipsisAfter = computed(() => {
      return visiblePageNumbers.value.length > 0 && 
             visiblePageNumbers.value[visiblePageNumbers.value.length - 1] < totalPages.value;
    });
    
    const hasSelectedItems = computed(() => {
      return Object.values(selectedReturnItems.value).some(selected => selected);
    });
    
    // Methods
    const fetchOrders = async () => {
      try {
        isLoading.value = true;
        // Simulate API call
        await new Promise(resolve => setTimeout(resolve, 1000));
        
        // Mock data - in a real app, this would be an API call
        const mockOrders: Order[] = [
          {
            id: 'ORD-1001',
            orderNumber: 'ORD-1001',
            orderDate: '2025-09-20T10:30:00',
            status: 'delivered',
            items: [
              {
                id: 'P001',
                name: 'Wireless Noise-Cancelling Headphones',
                price: 249.99,
                quantity: 1,
                image: 'https://via.placeholder.com/100x100?text=Headphones',
                variant: { Color: 'Black' },
                inStock: true,
                availableQuantity: 10
              },
              {
                id: 'P002',
                name: 'USB-C Charging Cable',
                price: 19.99,
                quantity: 2,
                image: 'https://via.placeholder.com/100x100?text=Cable',
                inStock: true,
                availableQuantity: 50
              }
            ],
            itemCount: 3,
            subtotal: 289.97,
            discount: 29.00,
            shippingCost: 0,
            total: 260.97,
            shippingAddress: {
              name: 'John Doe',
              street: '123 Main St',
              city: 'New York',
              state: 'NY',
              zip: '10001',
              country: 'United States'
            },
            paymentMethod: 'Visa ending in 4242',
            trackingNumber: '1Z999AA1234567890',
            estimatedDelivery: '2025-09-25'
          },
          {
            id: 'ORD-1000',
            orderNumber: 'ORD-1000',
            orderDate: '2025-09-15T14:20:00',
            status: 'shipped',
            items: [
              {
                id: 'P003',
                name: 'Smartphone 13 Pro',
                price: 999.99,
                quantity: 1,
                image: 'https://via.placeholder.com/100x100?text=Smartphone',
                variant: { Color: 'Midnight', Storage: '256GB' },
                inStock: true,
                availableQuantity: 5
              }
            ],
            itemCount: 1,
            subtotal: 999.99,
            discount: 0,
            shippingCost: 9.99,
            total: 1009.98,
            shippingAddress: {
              name: 'John Doe',
              street: '123 Main St',
              city: 'New York',
              state: 'NY',
              zip: '10001',
              country: 'United States'
            },
            paymentMethod: 'Mastercard ending in 5555',
            trackingNumber: '1Z999AA1234567891',
            estimatedDelivery: '2025-09-28'
          },
          {
            id: 'ORD-999',
            orderNumber: 'ORD-999',
            orderDate: '2025-09-10T09:15:00',
            status: 'cancelled',
            items: [
              {
                id: 'P004',
                name: 'Wireless Earbuds',
                price: 129.99,
                quantity: 1,
                image: 'https://via.placeholder.com/100x100?text=Earbuds',
                variant: { Color: 'White' },
                inStock: true,
                availableQuantity: 20
              }
            ],
            itemCount: 1,
            subtotal: 129.99,
            discount: 0,
            shippingCost: 0,
            total: 129.99,
            shippingAddress: {
              name: 'John Doe',
              street: '123 Main St',
              city: 'New York',
              state: 'NY',
              zip: '10001',
              country: 'United States'
            },
            paymentMethod: 'PayPal',
            trackingNumber: '1Z999AA1234567892',
            estimatedDelivery: '2025-09-20'
          }
        ];
        
        // Apply filters (simulated)
        let filteredOrders = [...mockOrders];
        
        if (filters.value.status !== 'all') {
          filteredOrders = filteredOrders.filter(order => order.status === filters.value.status);
        }
        
        if (filters.value.search) {
          const searchTerm = filters.value.search.toLowerCase();
          filteredOrders = filteredOrders.filter(order => 
            order.orderNumber.toLowerCase().includes(searchTerm)
          );
        }
        
        // Apply time range filter
        const now = new Date();
        const daysAgo = parseInt(filters.value.timeRange);
        
        if (filters.value.timeRange !== 'all') {
          const cutoffDate = new Date(now);
          cutoffDate.setDate(now.getDate() - daysAgo);
          
          filteredOrders = filteredOrders.filter(order => {
            const orderDate = new Date(order.orderDate);
            return orderDate >= cutoffDate;
          });
        }
        
        // Sort by date (newest first)
        filteredOrders.sort((a, b) => 
          new Date(b.orderDate).getTime() - new Date(a.orderDate).getTime()
        );
        
        totalOrders.value = filteredOrders.length;
        orders.value = filteredOrders;
      } catch (error) {
        console.error('Failed to fetch orders:', error);
        // In a real app, show an error message to the user
      } finally {
        isLoading.value = false;
      }
    };
    
    const applyFilters = () => {
      currentPage.value = 1; // Reset to first page when filters change
      fetchOrders();
    };
    
    const changePage = (page: number) => {
      if (page >= 1 && page <= totalPages.value) {
        currentPage.value = page;
        // In a real app, you would fetch the orders for the new page
        // For this demo, we're using client-side pagination
        window.scrollTo({ top: 0, behavior: 'smooth' });
      }
    };
    
    const formatDate = (dateString: string | Date) => {
      const options: Intl.DateTimeFormatOptions = { 
        year: 'numeric', 
        month: 'long', 
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      };
      return new Date(dateString).toLocaleDateString('en-US', options);
    };
    
    const formatPrice = (price: number) => {
      return new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD',
        minimumFractionDigits: 2
      }).format(price);
    };
    
    const formatStatus = (status: OrderStatus) => {
      const statusMap: Record<OrderStatus, string> = {
        pending: 'Pending',
        processing: 'Processing',
        shipped: 'Shipped',
        delivered: 'Delivered',
        cancelled: 'Cancelled',
        refunded: 'Refunded'
      };
      return statusMap[status] || status;
    };
    
    const getStatusClass = (status: OrderStatus) => {
      return `status-${status}`;
    };
    
    const canReorder = (status: OrderStatus) => {
      return ['delivered', 'cancelled', 'refunded'].includes(status);
    };
    
    const canTrack = (status: OrderStatus) => {
      return ['shipped', 'delivered'].includes(status);
    };
    
    const canCancel = (status: OrderStatus) => {
      return ['pending', 'processing'].includes(status);
    };
    
    const canReturn = (status: OrderStatus) => {
      // Typically, you can return items within 30 days of delivery
      return status === 'delivered';
    };
    
    const reorder = (order: Order) => {
      // In a real app, this would add all items to the cart
      console.log('Reordering items:', order.items);
      // For demo purposes, we'll just navigate to the cart
      router.push('/cart');
    };
    
    const trackOrder = (order: Order) => {
      // In a real app, this would open a tracking page or modal
      console.log('Tracking order:', order.trackingNumber);
      router.push(`/tracking/${order.id}`);
    };
    
    const cancelOrder = (order: Order) => {
      selectedOrder.value = order;
      cancellationReason.value = '';
      customReason.value = '';
      showCancelModal.value = true;
    };
    
    const confirmCancelOrder = async () => {
      if (!selectedOrder.value || !cancellationReason.value) return;
      
      try {
        isCancelling.value = true;
        // Simulate API call
        await new Promise(resolve => setTimeout(resolve, 1000));
        
        // Update the order status in the local state
        const orderIndex = orders.value.findIndex(o => o.id === selectedOrder.value?.id);
        if (orderIndex !== -1) {
          orders.value[orderIndex].status = 'cancelled';
        }
        
        closeCancelModal();
        // In a real app, show a success message
      } catch (error) {
        console.error('Failed to cancel order:', error);
        // In a real app, show an error message
      } finally {
        isCancelling.value = false;
      }
    };
    
    const initiateReturn = (order: Order) => {
      selectedOrder.value = order;
      selectedReturnItems.value = {};
      returnQuantities.value = {};
      returnReason.value = '';
      customReturnReason.value = '';
      refundMethod.value = 'original';
      
      // Initialize return quantities
      order.items.forEach(item => {
        selectedReturnItems.value[item.id] = false;
        returnQuantities.value[item.id] = 1;
      });
      
      showReturnModal.value = true;
    };
    
    const submitReturn = async () => {
      if (!selectedOrder.value || !hasSelectedItems.value || !returnReason.value) return;
      
      try {
        isSubmittingReturn.value = true;
        
        // Get selected items and quantities
        const returnItems = Object.entries(selectedReturnItems.value)
          .filter(([_, selected]) => selected)
          .map(([itemId]) => {
            const item = selectedOrder.value?.items.find(i => i.id == itemId);
            return {
              id: itemId,
              quantity: returnQuantities.value[itemId] || 1,
              name: item?.name || 'Item',
              price: item?.price || 0
            };
          });
        
        // Simulate API call
        await new Promise(resolve => setTimeout(resolve, 1500));
        
        // In a real app, you would submit the return request to your backend
        console.log('Submitting return request:', {
          orderId: selectedOrder.value.id,
          items: returnItems,
          reason: returnReason.value === 'other' ? customReturnReason.value : returnReason.value,
          refundMethod: refundMethod.value
        });
        
        // Close the modal and show success message
        closeReturnModal();
        // In a real app, show a success message and update the UI
      } catch (error) {
        console.error('Failed to submit return:', error);
        // In a real app, show an error message
      } finally {
        isSubmittingReturn.value = false;
      }
    };
    
    const closeCancelModal = () => {
      showCancelModal.value = false;
      selectedOrder.value = null;
    };
    
    const closeReturnModal = () => {
      showReturnModal.value = false;
      selectedOrder.value = null;
    };
    
    // Lifecycle hooks
    onMounted(() => {
      fetchOrders();
    });
    
    return {
      // State
      isLoading,
      orders,
      filters,
      currentPage,
      totalPages,
      visiblePageNumbers,
      showEllipsisAfter,
      showCancelModal,
      showReturnModal,
      selectedOrder,
      isCancelling,
      cancellationReason,
      customReason,
      selectedReturnItems,
      returnQuantities,
      returnReason,
      customReturnReason,
      refundMethod,
      isSubmittingReturn,
      hasSelectedItems,
      
      // Methods
      applyFilters,
      changePage,
      formatDate,
      formatPrice,
      formatStatus,
      getStatusClass,
      canReorder,
      canTrack,
      canCancel,
      canReturn,
      reorder,
      trackOrder,
      cancelOrder,
      confirmCancelOrder,
      initiateReturn,
      submitReturn,
      closeCancelModal,
      closeReturnModal
    };
  }
});
</script>

<style scoped>
/* Base styles */
.order-history {
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

/* Order Filters */
.order-filters {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  margin-bottom: 25px;
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.filter-group {
  display: flex;
  align-items: center;
  gap: 10px;
}

.filter-group label {
  font-weight: 500;
  color: #2c3e50;
  white-space: nowrap;
}

.filter-group select,
.search-box input {
  padding: 10px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 0.95rem;
  min-width: 150px;
  background-color: #fff;
  transition: border-color 0.2s, box-shadow 0.2s;
}

.filter-group select:focus,
.search-box input:focus {
  outline: none;
  border-color: #3498db;
  box-shadow: 0 0 0 2px rgba(52, 152, 219, 0.2);
}

.search-box {
  display: flex;
  margin-left: auto;
  max-width: 300px;
  flex: 1;
}

.search-box input {
  flex: 1;
  min-width: 100px;
  border-top-right-radius: 0;
  border-bottom-right-radius: 0;
  border-right: none;
}

.search-box button {
  background-color: #f8f9fa;
  border: 1px solid #ddd;
  border-left: none;
  border-top-right-radius: 4px;
  border-bottom-right-radius: 4px;
  padding: 0 15px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background-color 0.2s;
}

.search-box button:hover {
  background-color: #e9ecef;
}

.search-icon {
  font-size: 1.1rem;
  color: #6c757d;
}

/* Order Card */
.order-card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  margin-bottom: 20px;
  overflow: hidden;
  transition: transform 0.2s, box-shadow 0.2s;
}

.order-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #f0f0f0;
  background-color: #fafbfc;
}

.order-meta h3 {
  margin: 0 0 5px;
  font-size: 1.1rem;
  color: #2c3e50;
}

.order-date {
  font-size: 0.9rem;
  color: #6c757d;
}

.order-status {
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 0.8rem;
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

.status-refunded {
  background-color: #e2e3e5;
  color: #383d41;
  text-decoration: line-through;
}

.order-details {
  padding: 20px;
  display: flex;
  flex-wrap: wrap;
  gap: 30px;
}

.order-items {
  flex: 1;
  min-width: 300px;
}

.order-item {
  display: flex;
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #f0f0f0;
}

.order-item:last-child {
  margin-bottom: 0;
  padding-bottom: 0;
  border-bottom: none;
}

.item-image {
  position: relative;
  width: 80px;
  height: 80px;
  margin-right: 15px;
  flex-shrink: 0;
  border: 1px solid #eee;
  border-radius: 4px;
  overflow: hidden;
}

.item-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.quantity-badge {
  position: absolute;
  top: -8px;
  right: -8px;
  background-color: #3498db;
  color: white;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.7rem;
  font-weight: 600;
  border: 2px solid white;
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

.item-price {
  font-weight: 600;
  color: #2c3e50;
}

.order-summary {
  width: 250px;
  padding: 15px;
  background-color: #f8f9fa;
  border-radius: 6px;
  align-self: flex-start;
}

.summary-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
  font-size: 0.95rem;
  color: #2c3e50;
}

.summary-row.total {
  margin-top: 10px;
  padding-top: 10px;
  border-top: 1px solid #e1e4e8;
  font-weight: 600;
  font-size: 1.05rem;
}

.discount {
  color: #27ae60;
}

.order-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  padding: 15px 20px;
  border-top: 1px solid #f0f0f0;
  background-color: #fafbfc;
}

.btn {
  padding: 8px 16px;
  border-radius: 4px;
  font-size: 0.9rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
  text-decoration: none;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  border: 1px solid transparent;
}

.btn-outline {
  background: white;
  border-color: #3498db;
  color: #3498db;
}

.btn-outline:hover {
  background-color: #f0f8ff;
}

.btn-primary {
  background-color: #3498db;
  color: white;
  border: none;
}

.btn-primary:hover {
  background-color: #2980b9;
}

.btn-danger {
  background-color: #e74c3c;
  color: white;
  border: none;
}

.btn-danger:hover {
  background-color: #c0392b;
}

.btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* Pagination */
.pagination {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 30px;
  padding: 15px 0;
  border-top: 1px solid #e1e4e8;
}

.pagination-button {
  padding: 8px 16px;
  background: white;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 0.9rem;
  cursor: pointer;
  transition: all 0.2s;
}

.pagination-button:hover:not(:disabled) {
  background-color: #f8f9fa;
  border-color: #3498db;
  color: #3498db;
}

.pagination-button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.page-numbers {
  display: flex;
  align-items: center;
  gap: 5px;
}

.page-number {
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid #ddd;
  border-radius: 4px;
  background: white;
  font-size: 0.9rem;
  cursor: pointer;
  transition: all 0.2s;
}

.page-number:hover:not(.active) {
  background-color: #f8f9fa;
  border-color: #3498db;
  color: #3498db;
}

.page-number.active {
  background-color: #3498db;
  border-color: #3498db;
  color: white;
  font-weight: 500;
  cursor: default;
}

.ellipsis {
  padding: 0 10px;
  color: #6c757d;
  user-select: none;
}

/* Loading State */
.loading-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #3498db;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 20px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* Empty State */
.empty-state {
  text-align: center;
  padding: 60px 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.empty-icon {
  font-size: 3rem;
  margin-bottom: 20px;
  opacity: 0.7;
}

.empty-state h3 {
  margin: 0 0 10px;
  color: #2c3e50;
  font-size: 1.5rem;
}

.empty-state p {
  color: #6c757d;
  margin-bottom: 25px;
  font-size: 1.05rem;
}

/* Modal */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  padding: 20px;
}

.modal {
  background: white;
  border-radius: 8px;
  width: 100%;
  max-width: 500px;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: 0 5px 25px rgba(0, 0, 0, 0.2);
  animation: modalFadeIn 0.3s ease-out;
}

@keyframes modalFadeIn {
  from { opacity: 0; transform: translateY(-20px); }
  to { opacity: 1; transform: translateY(0); }
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #e1e4e8;
}

.modal-header h3 {
  margin: 0;
  font-size: 1.3rem;
  color: #2c3e50;
}

.close-button {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  color: #6c757d;
  padding: 5px;
  line-height: 1;
  transition: color 0.2s;
}

.close-button:hover {
  color: #2c3e50;
}

.modal-body {
  padding: 20px;
}

.modal-body p {
  margin: 0 0 20px;
  color: #2c3e50;
  line-height: 1.6;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  padding: 15px 20px;
  border-top: 1px solid #e1e4e8;
  background-color: #f8f9fa;
  border-bottom-left-radius: 8px;
  border-bottom-right-radius: 8px;
}

/* Cancellation Reason */
.cancellation-reason {
  margin-top: 20px;
}

.cancellation-reason label,
.return-reason label,
.refund-method label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #2c3e50;
}

.cancellation-reason select,
.return-reason select,
.refund-method select,
.return-quantity select {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 0.95rem;
  margin-bottom: 15px;
  background-color: white;
}

.cancellation-reason textarea,
.return-reason textarea {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 0.95rem;
  margin-top: 10px;
  min-height: 80px;
  resize: vertical;
}

/* Return Items */
.return-items {
  margin-top: 20px;
}

.return-item {
  display: flex;
  padding: 15px 0;
  border-bottom: 1px solid #f0f0f0;
}

.return-item:last-child {
  border-bottom: none;
}

.return-item-select {
  margin-right: 15px;
  display: flex;
  align-items: center;
}

.return-item-image {
  width: 80px;
  height: 80px;
  margin-right: 15px;
  border: 1px solid #eee;
  border-radius: 4px;
  overflow: hidden;
  flex-shrink: 0;
}

.return-item-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.return-item-details {
  flex: 1;
}

.return-item-details h4 {
  margin: 0 0 5px;
  font-size: 1rem;
  color: #2c3e50;
}

.quantity-selector {
  display: flex;
  align-items: center;
  margin-top: 8px;
  font-size: 0.9rem;
}

.quantity-selector label {
  margin-right: 10px;
  color: #6c757d;
}

.quantity-selector select {
  padding: 5px 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  background-color: white;
  font-size: 0.9rem;
  width: 80px;
}

.return-reason,
.refund-method {
  margin-top: 20px;
  padding-top: 15px;
  border-top: 1px solid #f0f0f0;
}

.refund-options {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-top: 10px;
}

.radio-option {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 8px 0;
}

.radio-option input[type="radio"] {
  margin: 0;
}

/* Responsive Styles */
@media (max-width: 992px) {
  .order-filters {
    flex-direction: column;
    gap: 15px;
  }
  
  .filter-group {
    width: 100%;
  }
  
  .filter-group select {
    flex: 1;
  }
  
  .search-box {
    width: 100%;
    max-width: 100%;
  }
  
  .order-details {
    flex-direction: column;
    gap: 20px;
  }
  
  .order-summary {
    width: 100%;
  }
  
  .order-actions {
    justify-content: flex-start;
  }
}

@media (max-width: 768px) {
  .page-header h1 {
    font-size: 1.8rem;
  }
  
  .order-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
  
  .order-status {
    align-self: flex-start;
  }
  
  .pagination {
    flex-direction: column;
    gap: 15px;
  }
  
  .page-numbers {
    flex-wrap: wrap;
    justify-content: center;
  }
  
  .modal {
    margin: 20px;
  }
}

@media (max-width: 576px) {
  .order-item {
    flex-direction: column;
  }
  
  .item-image {
    margin-bottom: 10px;
  }
  
  .order-actions {
    flex-direction: column;
  }
  
  .order-actions .btn {
    width: 100%;
  }
  
  .modal-footer {
    flex-direction: column;
  }
  
  .modal-footer .btn {
    width: 100%;
  }
}
</style>
