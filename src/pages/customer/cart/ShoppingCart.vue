<template>
  <div class="shopping-cart">
    <div class="container">
      <h1 class="page-title">Shopping Cart</h1>
      
      <div v-if="isLoading" class="loading">
        <div class="spinner"></div>
        <p>Loading your cart...</p>
      </div>
      
      <div v-else-if="cartItems.length === 0" class="empty-cart">
        <div class="empty-cart-content">
          <div class="empty-cart-icon">🛒</div>
          <h2>Your cart is empty</h2>
          <p>Looks like you haven't added anything to your cart yet.</p>
          <router-link to="/products" class="continue-shopping">
            Continue Shopping
          </router-link>
        </div>
      </div>
      
      <div v-else class="cart-container">
        <div class="cart-items">
          <div class="cart-header">
            <div class="header-product">Product</div>
            <div class="header-price">Price</div>
            <div class="header-quantity">Quantity</div>
            <div class="header-total">Total</div>
            <div class="header-remove"></div>
          </div>
          
          <div 
            v-for="(item, index) in cartItems" 
            :key="item.id + (item.variant ? Object.values(item.variant).join('-') : '')" 
            class="cart-item"
            :class="{ 'updating': updatingItemId === item.id }"
          >
            <div class="product-cell">
              <div class="product-image">
                <img :src="item.image" :alt="item.name">
              </div>
              <div class="product-details">
                <h3 class="product-name">
                  <router-link :to="`/products/${item.id}`">{{ item.name }}</router-link>
                </h3>
                <div v-if="item.variant" class="product-variants">
                  <span 
                    v-for="(value, key) in item.variant" 
                    :key="key"
                    class="variant"
                  >
                    {{ key }}: {{ value }}
                  </span>
                </div>
                <div v-if="!item.inStock" class="stock-warning">
                  Out of stock
                </div>
                <div v-else-if="item.lowStock" class="stock-warning low">
                  Only {{ item.availableQuantity }} left in stock
                </div>
              </div>
            </div>
            
            <div class="price-cell">
              <div class="price">
                {{ formatPrice(item.price) }}
                <span v-if="item.originalPrice" class="original-price">
                  {{ formatPrice(item.originalPrice) }}
                </span>
              </div>
            </div>
            
            <div class="quantity-cell">
              <div class="quantity-selector">
                <button 
                  class="quantity-btn" 
                  @click="updateQuantity(item, item.quantity - 1)"
                  :disabled="item.quantity <= 1 || updatingItemId === item.id"
                >-</button>
                <input 
                  type="number" 
                  v-model.number="item.quantity" 
                  min="1" 
                  :max="item.availableQuantity"
                  @change="updateQuantity(item, item.quantity)"
                  :disabled="updatingItemId === item.id"
                >
                <button 
                  class="quantity-btn" 
                  @click="updateQuantity(item, item.quantity + 1)"
                  :disabled="item.quantity >= item.availableQuantity || updatingItemId === item.id"
                >+</button>
              </div>
            </div>
            
            <div class="total-cell">
              <div class="total-price">
                {{ formatPrice(item.price * item.quantity) }}
              </div>
            </div>
            
            <div class="remove-cell">
              <button 
                class="remove-btn" 
                @click="removeItem(item)"
                :disabled="updatingItemId === item.id"
                title="Remove item"
              >
                &times;
              </button>
            </div>
          </div>
          
          <div class="cart-actions">
            <router-link to="/products" class="continue-shopping">
              &larr; Continue Shopping
            </router-link>
            <button 
              class="clear-cart" 
              @click="clearCart"
              :disabled="isClearingCart"
            >
              <span v-if="isClearingCart" class="spinner small"></span>
              <span v-else>Clear Cart</span>
            </button>
          </div>
        </div>
        
        <div class="cart-summary">
          <div class="summary-card">
            <h3>Order Summary</h3>
            
            <div class="summary-row">
              <span>Subtotal ({{ totalItems }} {{ totalItems === 1 ? 'item' : 'items' }})</span>
              <span>{{ formatPrice(subtotal) }}</span>
            </div>
            
            <div class="summary-row" v-if="discount > 0">
              <span>Discount</span>
              <span class="discount">-{{ formatPrice(discount) }}</span>
            </div>
            
            <div class="summary-row shipping">
              <div class="shipping-header">
                <span>Shipping</span>
                <span v-if="shippingCost === 0" class="free-shipping">Free</span>
              </div>
              <div class="shipping-options">
                <div class="shipping-option">
                  <input 
                    type="radio" 
                    id="standard" 
                    value="standard" 
                    v-model="shippingMethod"
                  >
                  <label for="standard">
                    Standard Delivery
                    <span class="delivery-time">3-5 business days</span>
                    <span class="price">
                      {{ shippingCost === 0 ? 'Free' : formatPrice(shippingCost) }}
                    </span>
                  </label>
                </div>
                <div class="shipping-option">
                  <input 
                    type="radio" 
                    id="express" 
                    value="express" 
                    v-model="shippingMethod"
                  >
                  <label for="express">
                    Express Delivery
                    <span class="delivery-time">1-2 business days</span>
                    <span class="price">
                      {{ formatPrice(expressShippingCost) }}
                    </span>
                  </label>
                </div>
              </div>
            </div>
            
            <div class="summary-row total">
              <span>Total</span>
              <span class="total-amount">{{ formatPrice(total) }}</span>
            </div>
            
            <div class="coupon-code">
              <div class="coupon-input">
                <input 
                  type="text" 
                  v-model="couponCode" 
                  placeholder="Coupon code"
                  :disabled="isApplyingCoupon"
                >
                <button 
                  class="apply-coupon" 
                  @click="applyCoupon"
                  :disabled="!couponCode || isApplyingCoupon"
                >
                  <span v-if="isApplyingCoupon" class="spinner small"></span>
                  <span v-else>Apply</span>
                </button>
              </div>
              <div v-if="appliedCoupon" class="coupon-applied">
                <span class="coupon-name">{{ appliedCoupon.code }}</span> applied
                <button class="remove-coupon" @click="removeCoupon">
                  &times;
                </button>
              </div>
              <div v-if="couponError" class="coupon-error">
                {{ couponError }}
              </div>
            </div>
            
            <button 
              class="checkout-btn" 
              @click="proceedToCheckout"
              :disabled="isCheckingOut"
            >
              <span v-if="isCheckingOut" class="spinner small"></span>
              <span v-else>Proceed to Checkout</span>
            </button>
            
            <div class="secure-checkout">
              <span class="lock-icon">🔒</span>
              <span>Secure Checkout</span>
            </div>
            
            <div class="accepted-payments">
              <span>We accept:</span>
              <div class="payment-methods">
                <span>💳</span>
                <span>🏦</span>
                <span>📱</span>
                <span>💎</span>
              </div>
            </div>
          </div>
          
          <div class="need-help">
            <h4>Need help?</h4>
            <p>If you have any questions, please don't hesitate to contact our customer service.</p>
            <div class="contact-info">
              <div class="contact-method">
                <span class="icon">📞</span>
                <span>+1 (555) 123-4567</span>
              </div>
              <div class="contact-method">
                <span class="icon">✉️</span>
                <span>support@example.com</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- Recently Viewed -->
    <div v-if="recentlyViewed.length > 0" class="recently-viewed">
      <div class="container">
        <h3>Recently Viewed</h3>
        <div class="recently-viewed-grid">
          <ProductCard 
            v-for="product in recentlyViewed" 
            :key="product.id" 
            :product="product"
            @add-to-cart="addToCart"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import ProductCard from '../products/ProductCard.vue';

interface CartItem {
  id: string | number;
  name: string;
  price: number;
  originalPrice?: number;
  quantity: number;
  image: string;
  variant?: Record<string, string>;
  inStock: boolean;
  availableQuantity: number;
  lowStock?: boolean;
}

interface Coupon {
  code: string;
  discount: number;
  type: 'percentage' | 'fixed';
  minPurchase?: number;
}

export default defineComponent({
  name: 'ShoppingCart',
  components: {
    ProductCard
  },
  setup() {
    const router = useRouter();
    
    // Cart state
    const cartItems = ref<CartItem[]>([]);
    const isLoading = ref(true);
    const isUpdating = ref(false);
    const updatingItemId = ref<string | number | null>(null);
    const isClearingCart = ref(false);
    const isCheckingOut = ref(false);
    
    // Coupon state
    const couponCode = ref('');
    const appliedCoupon = ref<Coupon | null>(null);
    const isApplyingCoupon = ref(false);
    const couponError = ref('');
    
    // Shipping state
    const shippingMethod = ref('standard');
    const standardShippingCost = ref(0);
    const expressShippingCost = ref(9.99);
    
    // Recently viewed products
    const recentlyViewed = ref<Array<{
      id: string | number;
      name: string;
      price: number;
      originalPrice?: number;
      image: string;
      rating: number;
      reviewCount: number;
      inStock: boolean;
      onSale?: boolean;
    }>>([]);
    
    // Computed properties
    const subtotal = computed(() => {
      return cartItems.value.reduce((sum, item) => {
        return sum + (item.price * item.quantity);
      }, 0);
    });
    
    const discount = computed(() => {
      if (!appliedCoupon.value) return 0;
      
      const coupon = appliedCoupon.value;
      let discountAmount = 0;
      
      if (coupon.type === 'percentage') {
        discountAmount = (subtotal.value * coupon.discount) / 100;
      } else {
        discountAmount = Math.min(coupon.discount, subtotal.value);
      }
      
      return discountAmount;
    });
    
    const shippingCost = computed(() => {
      // Free shipping for orders over $50
      if (subtotal.value >= 50) {
        return 0;
      }
      return standardShippingCost.value;
    });
    
    const total = computed(() => {
      const shipping = shippingMethod.value === 'express' 
        ? expressShippingCost.value 
        : shippingCost.value;
      
      return Math.max(0, (subtotal.value - discount.value) + shipping);
    });
    
    const totalItems = computed(() => {
      return cartItems.value.reduce((sum, item) => sum + item.quantity, 0);
    });
    
    // Methods
    const fetchCart = async () => {
      try {
        isLoading.value = true;
        // In a real app, you would fetch the cart from your API
        // const response = await fetch('/api/cart');
        // cartItems.value = await response.json();
        
        // Mock data for demonstration
        setTimeout(() => {
          cartItems.value = [
            {
              id: 1,
              name: 'Premium Wireless Headphones',
              price: 199.99,
              originalPrice: 249.99,
              quantity: 1,
              image: 'https://via.placeholder.com/100x100?text=Headphones',
              variant: {
                Color: 'Black',
                Size: 'One Size'
              },
              inStock: true,
              availableQuantity: 10,
              lowStock: true
            },
            {
              id: 2,
              name: 'Wireless Earbuds',
              price: 129.99,
              quantity: 2,
              image: 'https://via.placeholder.com/100x100?text=Earbuds',
              inStock: true,
              availableQuantity: 5,
              lowStock: false
            }
          ];
          
          // Set standard shipping cost based on subtotal
          standardShippingCost.value = subtotal.value >= 50 ? 0 : 4.99;
          
          // Load recently viewed (in a real app, this would come from your API)
          recentlyViewed.value = [
            {
              id: 3,
              name: 'Bluetooth Speaker',
              price: 79.99,
              image: 'https://via.placeholder.com/300x300?text=Speaker',
              rating: 4.7,
              reviewCount: 156,
              inStock: true
            },
            {
              id: 4,
              name: 'Smart Watch',
              price: 199.99,
              originalPrice: 249.99,
              image: 'https://via.placeholder.com/300x300?text=Watch',
              rating: 4.3,
              reviewCount: 89,
              inStock: true,
              onSale: true
            },
            {
              id: 5,
              name: 'Laptop Stand',
              price: 29.99,
              image: 'https://via.placeholder.com/300x300?text=Laptop+Stand',
              rating: 4.5,
              reviewCount: 42,
              inStock: true
            }
          ];
          
          isLoading.value = false;
        }, 500);
      } catch (error) {
        console.error('Error fetching cart:', error);
        isLoading.value = false;
      }
    };
    
    const updateQuantity = async (item: CartItem, newQuantity: number) => {
      if (newQuantity < 1 || newQuantity > item.availableQuantity) {
        return;
      }
      
      try {
        updatingItemId.value = item.id;
        isUpdating.value = true;
        
        // In a real app, you would call your API to update the cart
        // await fetch(`/api/cart/${item.id}`, {
        //   method: 'PUT',
        //   body: JSON.stringify({ quantity: newQuantity })
        // });
        
        // Update local state
        item.quantity = newQuantity;
        
        // If quantity is 0, remove the item
        if (newQuantity === 0) {
          cartItems.value = cartItems.value.filter(i => i.id !== item.id);
        }
      } catch (error) {
        console.error('Error updating quantity:', error);
        // Show error to user
      } finally {
        updatingItemId.value = null;
        isUpdating.value = false;
      }
    };
    
    const removeItem = async (item: CartItem) => {
      if (!confirm('Are you sure you want to remove this item from your cart?')) {
        return;
      }
      
      try {
        updatingItemId.value = item.id;
        
        // In a real app, you would call your API to remove the item
        // await fetch(`/api/cart/${item.id}`, { method: 'DELETE' });
        
        // Update local state
        cartItems.value = cartItems.value.filter(i => i.id !== item.id);
      } catch (error) {
        console.error('Error removing item:', error);
        // Show error to user
      } finally {
        updatingItemId.value = null;
      }
    };
    
    const clearCart = async () => {
      if (!confirm('Are you sure you want to clear your cart?')) {
        return;
      }
      
      try {
        isClearingCart.value = true;
        
        // In a real app, you would call your API to clear the cart
        // await fetch('/api/cart', { method: 'DELETE' });
        
        // Update local state
        cartItems.value = [];
        appliedCoupon.value = null;
        couponCode.value = '';
      } catch (error) {
        console.error('Error clearing cart:', error);
        // Show error to user
      } finally {
        isClearingCart.value = false;
      }
    };
    
    const applyCoupon = async () => {
      if (!couponCode.value.trim()) return;
      
      try {
        isApplyingCoupon.value = true;
        couponError.value = '';
        
        // In a real app, you would validate the coupon with your API
        // const response = await fetch(`/api/coupons/validate?code=${encodeURIComponent(couponCode.value)}`);
        // const data = await response.json();
        
        // Mock API response
        const mockCoupons: Record<string, Coupon> = {
          'SAVE10': { code: 'SAVE10', discount: 10, type: 'percentage' },
          'FREESHIP': { code: 'FREESHIP', discount: 0, type: 'fixed' }, // Special case for free shipping
          '20OFF': { code: '20OFF', discount: 20, type: 'percentage', minPurchase: 100 }
        };
        
        setTimeout(() => {
          const coupon = mockCoupons[couponCode.value.toUpperCase()];
          
          if (!coupon) {
            couponError.value = 'Invalid coupon code';
            return;
          }
          
          if (coupon.minPurchase && subtotal.value < coupon.minPurchase) {
            couponError.value = `Minimum purchase of $${coupon.minPurchase} required`;
            return;
          }
          
          // Special handling for free shipping coupon
          if (coupon.code === 'FREESHIP') {
            standardShippingCost.value = 0;
            expressShippingCost.value = 0;
          }
          
          appliedCoupon.value = coupon;
          couponCode.value = '';
        }, 800);
      } catch (error) {
        console.error('Error applying coupon:', error);
        couponError.value = 'Failed to apply coupon. Please try again.';
      } finally {
        isApplyingCoupon.value = false;
      }
    };
    
    const removeCoupon = () => {
      appliedCoupon.value = null;
      
      // Reset shipping costs (in a real app, you'd fetch the original values)
      standardShippingCost.value = subtotal.value >= 50 ? 0 : 4.99;
      expressShippingCost.value = 9.99;
    };
    
    const proceedToCheckout = () => {
      isCheckingOut.value = true;
      
      // In a real app, you might want to save the cart state before navigating
      // and handle guest vs. logged-in user flows
      
      router.push('/checkout');
    };
    
    const addToCart = (product: any) => {
      // This would be handled by your cart store in a real app
      console.log('Add to cart:', product);
      // Redirect to product page or show "added to cart" notification
    };
    
    const formatPrice = (price: number) => {
      return new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD',
        minimumFractionDigits: 2
      }).format(price);
    };
    
    // Fetch cart data when component mounts
    onMounted(() => {
      fetchCart();
    });
    
    return {
      // State
      cartItems,
      isLoading,
      isUpdating,
      updatingItemId,
      isClearingCart,
      isCheckingOut,
      couponCode,
      appliedCoupon,
      isApplyingCoupon,
      couponError,
      shippingMethod,
      shippingCost,
      expressShippingCost,
      recentlyViewed,
      
      // Computed
      subtotal,
      discount,
      total,
      totalItems,
      
      // Methods
      updateQuantity,
      removeItem,
      clearCart,
      applyCoupon,
      removeCoupon,
      proceedToCheckout,
      addToCart,
      formatPrice
    };
  }
});
</script>

<style scoped>
/* Base styles */
.shopping-cart {
  background-color: #f8f9fa;
  min-height: calc(100vh - 80px);
  padding: 40px 0;
}

.container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 20px;
}

.page-title {
  font-size: 2.2rem;
  color: #2c3e50;
  margin-bottom: 30px;
  font-weight: 600;
}

/* Loading state */
.loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 300px;
  text-align: center;
}

.spinner {
  border: 4px solid rgba(0, 0, 0, 0.1);
  border-radius: 50%;
  border-top: 4px solid #3498db;
  width: 40px;
  height: 40px;
  animation: spin 1s linear infinite;
  margin-bottom: 15px;
}

.spinner.small {
  width: 20px;
  height: 20px;
  border-width: 2px;
  display: inline-block;
  vertical-align: middle;
  margin-right: 5px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* Empty cart */
.empty-cart {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 50vh;
  text-align: center;
}

.empty-cart-content {
  max-width: 500px;
  padding: 40px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 15px rgba(0, 0, 0, 0.05);
}

.empty-cart-icon {
  font-size: 4rem;
  margin-bottom: 20px;
  opacity: 0.8;
}

.empty-cart h2 {
  font-size: 1.8rem;
  color: #2c3e50;
  margin-bottom: 10px;
}

.empty-cart p {
  color: #7f8c8d;
  margin-bottom: 25px;
  font-size: 1.1rem;
}

/* Cart container */
.cart-container {
  display: flex;
  gap: 30px;
  align-items: flex-start;
}

/* Cart items */
.cart-items {
  flex: 2;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 15px rgba(0, 0, 0, 0.05);
  overflow: hidden;
}

.cart-header {
  display: none; /* Hidden on mobile, shown on desktop */
  padding: 15px 20px;
  background: #f8f9fa;
  font-weight: 500;
  color: #2c3e50;
  border-bottom: 1px solid #eee;
}

.cart-header > div {
  padding: 0 10px;
}

.header-product { flex: 3; }
.header-price { flex: 1; text-align: center; }
.header-quantity { flex: 1; text-align: center; }
.header-total { flex: 1; text-align: right; }
.header-remove { width: 40px; }

.cart-item {
  display: flex;
  flex-wrap: wrap;
  padding: 20px;
  border-bottom: 1px solid #eee;
  position: relative;
  transition: background-color 0.2s;
}

.cart-item:last-child {
  border-bottom: none;
}

.cart-item.updating {
  opacity: 0.7;
  pointer-events: none;
}

.cart-item.updating::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(255, 255, 255, 0.7);
  z-index: 1;
}

/* Product cell */
.product-cell {
  display: flex;
  flex: 1 1 100%;
  margin-bottom: 15px;
}

.product-image {
  width: 100px;
  height: 100px;
  flex-shrink: 0;
  margin-right: 15px;
  border: 1px solid #eee;
  border-radius: 4px;
  overflow: hidden;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.product-details {
  flex: 1;
}

.product-name {
  margin: 0 0 8px 0;
  font-size: 1rem;
}

.product-name a {
  color: #2c3e50;
  text-decoration: none;
  transition: color 0.2s;
}

.product-name a:hover {
  color: #3498db;
  text-decoration: underline;
}

.product-variants {
  margin-top: 5px;
  font-size: 0.85rem;
  color: #7f8c8d;
}

.variant {
  display: inline-block;
  margin-right: 10px;
  margin-bottom: 5px;
}

.stock-warning {
  display: inline-block;
  font-size: 0.8rem;
  padding: 2px 8px;
  border-radius: 4px;
  margin-top: 5px;
}

.stock-warning:not(.low) {
  background-color: #fde8e8;
  color: #e74c3c;
}

.stock-warning.low {
  background-color: #fff3e0;
  color: #f39c12;
}

/* Price cell */
.price-cell {
  flex: 1;
  text-align: left;
  margin-bottom: 15px;
}

.price {
  font-weight: 500;
  color: #2c3e50;
}

.original-price {
  text-decoration: line-through;
  color: #95a5a6;
  font-size: 0.9em;
  margin-left: 5px;
}

/* Quantity cell */
.quantity-cell {
  flex: 1;
  margin-bottom: 15px;
}

.quantity-selector {
  display: flex;
  max-width: 120px;
  border: 1px solid #ddd;
  border-radius: 4px;
  overflow: hidden;
}

.quantity-btn {
  width: 35px;
  height: 35px;
  background: #f8f9fa;
  border: none;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.2s;
}

.quantity-btn:hover:not(:disabled) {
  background: #e9ecef;
}

.quantity-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.quantity-selector input {
  width: 50px;
  height: 35px;
  border: none;
  border-left: 1px solid #eee;
  border-right: 1px solid #eee;
  text-align: center;
  -moz-appearance: textfield;
}

.quantity-selector input::-webkit-outer-spin-button,
.quantity-selector input::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

/* Total cell */
.total-cell {
  flex: 1;
  text-align: right;
  margin-bottom: 15px;
}

.total-price {
  font-weight: 600;
  color: #2c3e50;
}

/* Remove cell */
.remove-cell {
  width: 40px;
  text-align: right;
}

.remove-btn {
  background: none;
  border: none;
  font-size: 1.5rem;
  color: #95a5a6;
  cursor: pointer;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  transition: all 0.2s;
}

.remove-btn:hover:not(:disabled) {
  color: #e74c3c;
  background-color: #fde8e8;
}

.remove-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

/* Cart actions */
.cart-actions {
  display: flex;
  justify-content: space-between;
  padding: 20px;
  border-top: 1px solid #eee;
  background: #f8f9fa;
}

.continue-shopping {
  color: #3498db;
  text-decoration: none;
  font-weight: 500;
  display: inline-flex;
  align-items: center;
  transition: color 0.2s;
}

.continue-shopping:hover {
  color: #2980b9;
  text-decoration: underline;
}

.clear-cart {
  background: none;
  border: 1px solid #e74c3c;
  color: #e74c3c;
  padding: 8px 15px;
  border-radius: 4px;
  cursor: pointer;
  font-weight: 500;
  transition: all 0.2s;
  display: flex;
  align-items: center;
  justify-content: center;
  min-width: 120px;
}

.clear-cart:hover:not(:disabled) {
  background-color: #fde8e8;
}

.clear-cart:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

/* Cart summary */
.cart-summary {
  flex: 1;
  position: sticky;
  top: 20px;
}

.summary-card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 15px rgba(0, 0, 0, 0.05);
  padding: 25px;
  margin-bottom: 20px;
}

.summary-card h3 {
  margin-top: 0;
  margin-bottom: 20px;
  color: #2c3e50;
  font-size: 1.3rem;
  padding-bottom: 15px;
  border-bottom: 1px solid #eee;
}

.summary-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 12px;
  font-size: 0.95rem;
}

.summary-row:last-child {
  margin-bottom: 0;
}

.summary-row.total {
  margin-top: 20px;
  padding-top: 15px;
  border-top: 1px solid #eee;
  font-size: 1.1rem;
  font-weight: 600;
  color: #2c3e50;
}

.summary-row .discount {
  color: #27ae60;
}

.shipping {
  flex-direction: column;
  align-items: flex-start;
  margin: 20px 0;
  padding: 15px 0;
  border-top: 1px solid #eee;
  border-bottom: 1px solid #eee;
}

.shipping-header {
  width: 100%;
  display: flex;
  justify-content: space-between;
  margin-bottom: 15px;
}

.free-shipping {
  color: #27ae60;
  font-weight: 500;
}

.shipping-options {
  width: 100%;
}

.shipping-option {
  margin-bottom: 10px;
}

.shipping-option:last-child {
  margin-bottom: 0;
}

.shipping-option input[type="radio"] {
  display: none;
}

.shipping-option label {
  display: flex;
  flex-direction: column;
  padding: 12px 15px;
  border: 1px solid #ddd;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.2s;
  position: relative;
  padding-left: 35px;
}

.shipping-option input[type="radio"]:checked + label {
  border-color: #3498db;
  background-color: #f0f8ff;
}

.shipping-option label::before {
  content: '';
  position: absolute;
  left: 10px;
  top: 50%;
  transform: translateY(-50%);
  width: 18px;
  height: 18px;
  border: 2px solid #bdc3c7;
  border-radius: 50%;
  transition: all 0.2s;
}

.shipping-option input[type="radio"]:checked + label::before {
  border-color: #3498db;
  background-color: #3498db;
  box-shadow: inset 0 0 0 3px white;
}

.delivery-time {
  font-size: 0.85rem;
  color: #7f8c8d;
  margin-top: 3px;
}

.price {
  margin-top: 5px;
  font-weight: 500;
  color: #2c3e50;
}

/* Coupon code */
.coupon-code {
  margin: 20px 0;
  padding-top: 20px;
  border-top: 1px dashed #ddd;
}

.coupon-input {
  display: flex;
  margin-bottom: 10px;
}

.coupon-input input {
  flex: 1;
  padding: 10px 15px;
  border: 1px solid #ddd;
  border-radius: 4px 0 0 4px;
  font-size: 0.95rem;
}

.apply-coupon {
  background-color: #2ecc71;
  color: white;
  border: none;
  padding: 0 20px;
  border-radius: 0 4px 4px 0;
  cursor: pointer;
  font-weight: 500;
  transition: background-color 0.2s;
  white-space: nowrap;
}

.apply-coupon:hover:not(:disabled) {
  background-color: #27ae60;
}

.apply-coupon:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.coupon-applied {
  display: flex;
  align-items: center;
  background-color: #e8f8f5;
  color: #27ae60;
  padding: 8px 12px;
  border-radius: 4px;
  font-size: 0.9rem;
  margin-top: 10px;
}

.coupon-name {
  font-weight: 500;
  margin-right: 5px;
}

.remove-coupon {
  margin-left: auto;
  background: none;
  border: none;
  color: #27ae60;
  font-size: 1.2rem;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  transition: background-color 0.2s;
}

.remove-coupon:hover {
  background-color: rgba(39, 174, 96, 0.1);
}

.coupon-error {
  color: #e74c3c;
  font-size: 0.85rem;
  margin-top: 5px;
}

/* Checkout button */
.checkout-btn {
  display: block;
  width: 100%;
  padding: 15px;
  background-color: #3498db;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 1.1rem;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s;
  margin: 25px 0 15px;
}

.checkout-btn:hover:not(:disabled) {
  background-color: #2980b9;
}

.checkout-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.secure-checkout {
  text-align: center;
  color: #7f8c8d;
  font-size: 0.9rem;
  margin-bottom: 15px;
}

.lock-icon {
  margin-right: 5px;
}

.accepted-payments {
  display: flex;
  flex-direction: column;
  align-items: center;
  font-size: 0.85rem;
  color: #7f8c8d;
  margin-top: 20px;
  padding-top: 15px;
  border-top: 1px dashed #ddd;
}

.payment-methods {
  display: flex;
  gap: 10px;
  margin-top: 8px;
  font-size: 1.5rem;
}

/* Need help */
.need-help {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 15px rgba(0, 0, 0, 0.05);
  padding: 25px;
}

.need-help h4 {
  margin-top: 0;
  margin-bottom: 15px;
  color: #2c3e50;
  font-size: 1.1rem;
}

.need-help p {
  color: #7f8c8d;
  margin-bottom: 20px;
  line-height: 1.6;
}

.contact-info {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.contact-method {
  display: flex;
  align-items: center;
  gap: 10px;
  color: #2c3e50;
}

.contact-method .icon {
  font-size: 1.2rem;
}

/* Recently viewed */
.recently-viewed {
  margin-top: 60px;
  padding: 40px 0;
  background: white;
  border-top: 1px solid #eee;
}

.recently-viewed h3 {
  text-align: center;
  margin-bottom: 30px;
  color: #2c3e50;
  font-size: 1.5rem;
  position: relative;
  padding-bottom: 15px;
}

.recently-viewed h3::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 60px;
  height: 3px;
  background: #3498db;
}

.recently-viewed-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 20px;
}

/* Responsive styles */
@media (min-width: 768px) {
  .cart-header {
    display: flex;
  }
  
  .product-cell,
  .price-cell,
  .quantity-cell,
  .total-cell,
  .remove-cell {
    flex: 1;
    margin-bottom: 0;
    display: flex;
    align-items: center;
  }
  
  .product-cell {
    flex: 3;
  }
  
  .price-cell {
    justify-content: center;
  }
  
  .total-cell {
    justify-content: flex-end;
  }
  
  .remove-cell {
    justify-content: flex-end;
  }
  
  .cart-item {
    flex-wrap: nowrap;
    padding: 15px 20px;
  }
  
  .product-cell {
    margin-bottom: 0;
  }
  
  .recently-viewed-grid {
    grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  }
}

@media (max-width: 991px) {
  .cart-container {
    flex-direction: column;
  }
  
  .cart-summary {
    width: 100%;
    position: static;
  }
}

@media (max-width: 767px) {
  .page-title {
    font-size: 1.8rem;
    margin-bottom: 20px;
  }
  
  .cart-actions {
    flex-direction: column;
    gap: 15px;
  }
  
  .continue-shopping,
  .clear-cart {
    width: 100%;
    text-align: center;
    justify-content: center;
  }
  
  .recently-viewed {
    margin-top: 40px;
    padding: 30px 0;
  }
}

@media (max-width: 480px) {
  .product-cell {
    flex-direction: column;
  }
  
  .product-image {
    margin-right: 0;
    margin-bottom: 10px;
  }
  
  .quantity-selector {
    max-width: 100%;
  }
  
  .recently-viewed h3 {
    font-size: 1.3rem;
  }
  
  .recently-viewed-grid {
    grid-template-columns: 1fr 1fr;
    gap: 15px;
  }
}
</style>
