<template>
  <div class="checkout-page">
    <div class="container">
      <!-- Checkout Header with Progress Steps -->
      <div class="checkout-header">
        <div class="logo">
          <router-link to="/">YourStore</router-link>
        </div>
        <div class="steps">
          <div 
            v-for="(step, index) in steps" 
            :key="index"
            class="step"
            :class="{ 
              'active': currentStep === index + 1,
              'completed': currentStep > index + 1,
              'first': index === 0,
              'last': index === steps.length - 1
            }"
          >
            <div class="step-number">
              <span v-if="currentStep <= index + 1">{{ index + 1 }}</span>
              <span v-else>✓</span>
            </div>
            <div class="step-label">{{ step }}</div>
          </div>
        </div>
        <div class="secure-checkout">
          <span class="lock-icon">🔒</span>
          <span>Secure Checkout</span>
        </div>
      </div>

      <div class="checkout-container">
        <!-- Left Column - Checkout Form -->
        <div class="checkout-form-container">
          <!-- Customer Information -->
          <form @submit.prevent="handleSubmit" class="checkout-form">
            <!-- Step 1: Customer Information -->
            <div v-if="currentStep === 1" class="form-step">
              <h2>Contact Information</h2>
              
              <div class="form-group">
                <label for="email">Email address *</label>
                <input 
                  type="email" 
                  id="email" 
                  v-model="formData.email" 
                  required
                  :class="{ 'error': errors.email }"
                  @input="clearError('email')"
                >
                <div v-if="errors.email" class="error-message">{{ errors.email }}</div>
                <div class="form-note">We'll send your order confirmation here</div>
              </div>
              
              <div class="form-group checkbox-group">
                <input 
                  type="checkbox" 
                  id="emailMe" 
                  v-model="formData.emailUpdates"
                >
                <label for="emailMe">
                  Email me with news and offers
                </label>
              </div>
              
              <h3>Shipping Address</h3>
              
              <div class="form-row">
                <div class="form-group">
                  <label for="firstName">First name *</label>
                  <input 
                    type="text" 
                    id="firstName" 
                    v-model="formData.shipping.firstName" 
                    required
                    :class="{ 'error': errors['shipping.firstName'] }"
                    @input="clearError('shipping.firstName')"
                  >
                  <div v-if="errors['shipping.firstName']" class="error-message">
                    {{ errors['shipping.firstName'] }}
                  </div>
                </div>
                <div class="form-group">
                  <label for="lastName">Last name *</label>
                  <input 
                    type="text" 
                    id="lastName" 
                    v-model="formData.shipping.lastName" 
                    required
                    :class="{ 'error': errors['shipping.lastName'] }"
                    @input="clearError('shipping.lastName')"
                  >
                  <div v-if="errors['shipping.lastName']" class="error-message">
                    {{ errors['shipping.lastName'] }}
                  </div>
                </div>
              </div>
              
              <div class="form-group">
                <label for="company">Company (optional)</label>
                <input 
                  type="text" 
                  id="company" 
                  v-model="formData.shipping.company"
                >
              </div>
              
              <div class="form-group">
                <label for="address">Address *</label>
                <input 
                  type="text" 
                  id="address" 
                  v-model="formData.shipping.address1" 
                  required
                  placeholder="Street address"
                  :class="{ 'error': errors['shipping.address1'] }"
                  @input="clearError('shipping.address1')"
                >
                <div v-if="errors['shipping.address1']" class="error-message">
                  {{ errors['shipping.address1'] }}
                </div>
                <input 
                  type="text" 
                  v-model="formData.shipping.address2" 
                  placeholder="Apartment, suite, etc. (optional)"
                  class="mt-2"
                >
              </div>
              
              <div class="form-row">
                <div class="form-group">
                  <label for="city">City *</label>
                  <input 
                    type="text" 
                    id="city" 
                    v-model="formData.shipping.city" 
                    required
                    :class="{ 'error': errors['shipping.city'] }"
                    @input="clearError('shipping.city')"
                  >
                  <div v-if="errors['shipping.city']" class="error-message">
                    {{ errors['shipping.city'] }}
                  </div>
                </div>
                <div class="form-group">
                  <label for="country">Country/Region *</label>
                  <select 
                    id="country" 
                    v-model="formData.shipping.country" 
                    required
                    :class="{ 'error': errors['shipping.country'] }"
                    @change="clearError('shipping.country')"
                  >
                    <option value="">Select a country</option>
                    <option 
                      v-for="country in countries" 
                      :key="country.code" 
                      :value="country.code"
                    >
                      {{ country.name }}
                    </option>
                  </select>
                  <div v-if="errors['shipping.country']" class="error-message">
                    {{ errors['shipping.country'] }}
                  </div>
                </div>
              </div>
              
              <div class="form-row">
                <div class="form-group">
                  <label for="state">State/Province *</label>
                  <select 
                    v-if="formData.shipping.country === 'US'" 
                    id="state" 
                    v-model="formData.shipping.state" 
                    required
                    :class="{ 'error': errors['shipping.state'] }"
                    @change="clearError('shipping.state')"
                  >
                    <option value="">Select a state</option>
                    <option 
                      v-for="(state, code) in usStates" 
                      :key="code" 
                      :value="code"
                    >
                      {{ state }}
                    </option>
                  </select>
                  <input 
                    v-else
                    type="text" 
                    id="state" 
                    v-model="formData.shipping.state" 
                    required
                    :class="{ 'error': errors['shipping.state'] }"
                    @input="clearError('shipping.state')"
                  >
                  <div v-if="errors['shipping.state']" class="error-message">
                    {{ errors['shipping.state'] }}
                  </div>
                </div>
                <div class="form-group">
                  <label for="zip">ZIP/Postal code *</label>
                  <input 
                    type="text" 
                    id="zip" 
                    v-model="formData.shipping.zip" 
                    required
                    :class="{ 'error': errors['shipping.zip'] }"
                    @input="clearError('shipping.zip')"
                  >
                  <div v-if="errors['shipping.zip']" class="error-message">
                    {{ errors['shipping.zip'] }}
                  </div>
                </div>
              </div>
              
              <div class="form-group">
                <label for="phone">Phone *</label>
                <input 
                  type="tel" 
                  id="phone" 
                  v-model="formData.shipping.phone" 
                  required
                  :class="{ 'error': errors['shipping.phone'] }"
                  @input="clearError('shipping.phone')"
                >
                <div v-if="errors['shipping.phone']" class="error-message">
                  {{ errors['shipping.phone'] }}
                </div>
                <div class="form-note">For delivery questions only</div>
              </div>
              
              <div class="form-group checkbox-group">
                <input 
                  type="checkbox" 
                  id="saveInfo" 
                  v-model="formData.saveInfo"
                >
                <label for="saveInfo">
                  Save this information for next time
                </label>
              </div>
              
              <div class="form-actions">
                <router-link to="/cart" class="back-link">
                  &larr; Return to cart
                </router-link>
                <button 
                  type="button" 
                  class="btn btn-primary"
                  @click="goToNextStep"
                  :disabled="isSubmitting"
                >
                  <span v-if="isSubmitting" class="spinner"></span>
                  <span v-else>Continue to shipping</span>
                </button>
              </div>
            </div>
            
            <!-- Step 2: Shipping Method -->
            <div v-else-if="currentStep === 2" class="form-step">
              <h2>Shipping Method</h2>
              
              <div class="shipping-options">
                <div 
                  v-for="option in shippingOptions" 
                  :key="option.id"
                  class="shipping-option"
                  :class="{ 'selected': formData.shippingMethod === option.id }"
                  @click="formData.shippingMethod = option.id"
                >
                  <div class="shipping-option-radio">
                    <input 
                      type="radio" 
                      :id="'shipping-' + option.id" 
                      v-model="formData.shippingMethod" 
                      :value="option.id"
                      required
                    >
                    <span class="radio-custom"></span>
                  </div>
                  <div class="shipping-option-details">
                    <div class="shipping-option-name">
                      {{ option.name }}
                      <span v-if="option.estimatedDelivery">
                        • {{ option.estimatedDelivery }}
                      </span>
                    </div>
                    <div class="shipping-option-description">
                      {{ option.description }}
                    </div>
                  </div>
                  <div class="shipping-option-price">
                    {{ option.price === 0 ? 'Free' : `$${option.price.toFixed(2)}` }}
                  </div>
                </div>
              </div>
              
              <div class="form-actions">
                <button 
                  type="button" 
                  class="btn btn-outline"
                  @click="currentStep--"
                  :disabled="isSubmitting"
                >
                  &larr; Return to information
                </button>
                <button 
                  type="button" 
                  class="btn btn-primary"
                  @click="goToNextStep"
                  :disabled="isSubmitting || !formData.shippingMethod"
                >
                  <span v-if="isSubmitting" class="spinner"></span>
                  <span v-else>Continue to payment</span>
                </button>
              </div>
            </div>
            
            <!-- Step 3: Payment -->
            <div v-else-if="currentStep === 3" class="form-step">
              <h2>Payment</h2>
              
              <div class="payment-methods">
                <div 
                  class="payment-method-tabs"
                  role="tablist"
                >
                  <button
                    type="button"
                    role="tab"
                    :class="{ 'active': formData.paymentMethod === 'credit-card' }"
                    @click="formData.paymentMethod = 'credit-card'"
                    aria-selected="formData.paymentMethod === 'credit-card'"
                    aria-controls="credit-card-panel"
                  >
                    <span class="payment-icon">💳</span>
                    <span>Credit Card</span>
                  </button>
                  <button
                    type="button"
                    role="tab"
                    :class="{ 'active': formData.paymentMethod === 'paypal' }"
                    @click="formData.paymentMethod = 'paypal'"
                    aria-selected="formData.paymentMethod === 'paypal'"
                    aria-controls="paypal-panel"
                  >
                    <span class="payment-icon">🔵</span>
                    <span>PayPal</span>
                  </button>
                  <button
                    type="button"
                    role="tab"
                    :class="{ 'active': formData.paymentMethod === 'apple-pay' }"
                    @click="formData.paymentMethod = 'apple-pay'"
                    aria-selected="formData.paymentMethod === 'apple-pay'"
                    aria-controls="apple-pay-panel"
                  >
                    <span class="payment-icon"></span>
                    <span>Apple Pay</span>
                  </button>
                </div>
                
                <div class="payment-panels">
                  <!-- Credit Card Panel -->
                  <div 
                    v-if="formData.paymentMethod === 'credit-card'"
                    id="credit-card-panel"
                    role="tabpanel"
                    aria-labelledby="credit-card-tab"
                  >
                    <div class="form-group">
                      <label for="cardNumber">Card number *</label>
                      <div class="input-with-icon">
                        <input 
                          type="text" 
                          id="cardNumber" 
                          v-model="formData.payment.cardNumber" 
                          placeholder="1234 5678 9012 3456"
                          required
                          :class="{ 'error': errors['payment.cardNumber'] }"
                          @input="formatCardNumber"
                        >
                        <span class="card-type">
                          <span v-if="cardType === 'visa'" class="visa">Visa</span>
                          <span v-else-if="cardType === 'mastercard'" class="mastercard">Mastercard</span>
                          <span v-else-if="cardType === 'amex'" class="amex">Amex</span>
                          <span v-else-if="cardType === 'discover'" class="discover">Discover</span>
                        </span>
                      </div>
                      <div v-if="errors['payment.cardNumber']" class="error-message">
                        {{ errors['payment.cardNumber'] }}
                      </div>
                    </div>
                    
                    <div class="form-group">
                      <label for="cardName">Name on card *</label>
                      <input 
                        type="text" 
                        id="cardName" 
                        v-model="formData.payment.cardName" 
                        required
                        :class="{ 'error': errors['payment.cardName'] }"
                        @input="clearError('payment.cardName')"
                      >
                      <div v-if="errors['payment.cardName']" class="error-message">
                        {{ errors['payment.cardName'] }}
                      </div>
                    </div>
                    
                    <div class="form-row">
                      <div class="form-group">
                        <label for="expiryDate">Expiration date (MM/YY) *</label>
                        <input 
                          type="text" 
                          id="expiryDate" 
                          v-model="formData.payment.expiryDate" 
                          placeholder="MM/YY"
                          required
                          :class="{ 'error': errors['payment.expiryDate'] }"
                          @input="formatExpiryDate"
                          maxlength="5"
                        >
                        <div v-if="errors['payment.expiryDate']" class="error-message">
                          {{ errors['payment.expiryDate'] }}
                        </div>
                      </div>
                      <div class="form-group">
                        <label for="cvv">Security code (CVV) *</label>
                        <div class="input-with-icon">
                          <input 
                            type="text" 
                            id="cvv" 
                            v-model="formData.payment.cvv" 
                            placeholder="123"
                            required
                            :class="{ 'error': errors['payment.cvv'] }"
                            @input="formatCVV"
                            maxlength="4"
                          >
                          <span class="info-icon" @mouseover="showCvvInfo = true" @mouseleave="showCvvInfo = false">
                            ?
                            <div v-if="showCvvInfo" class="tooltip">
                              <div class="tooltip-arrow"></div>
                              <div class="tooltip-inner">
                                3 or 4 digits on the back of your card
                              </div>
                            </div>
                          </span>
                        </div>
                        <div v-if="errors['payment.cvv']" class="error-message">
                          {{ errors['payment.cvv'] }}
                        </div>
                      </div>
                    </div>
                    
                    <div class="form-group checkbox-group">
                      <input 
                        type="checkbox" 
                        id="saveCard" 
                        v-model="formData.saveCard"
                      >
                      <label for="saveCard">
                        Save this card for future purchases
                      </label>
                    </div>
                  </div>
                  
                  <!-- PayPal Panel -->
                  <div 
                    v-else-if="formData.paymentMethod === 'paypal'"
                    id="paypal-panel"
                    role="tabpanel"
                    aria-labelledby="paypal-tab"
                    class="payment-method-content"
                  >
                    <div class="payment-method-description">
                      <p>You will be redirected to PayPal to complete your purchase securely.</p>
                      <button type="button" class="btn btn-paypal">
                        <img src="https://www.paypalobjects.com/webstatic/en_US/i/buttons/checkout-logo-medium.png" alt="Check out with PayPal">
                      </button>
                    </div>
                  </div>
                  
                  <!-- Apple Pay Panel -->
                  <div 
                    v-else-if="formData.paymentMethod === 'apple-pay'"
                    id="apple-pay-panel"
                    role="tabpanel"
                    aria-labelledby="apple-pay-tab"
                    class="payment-method-content"
                  >
                    <div class="payment-method-description">
                      <p>Complete your purchase with Face ID, Touch ID, or your device passcode.</p>
                      <button type="button" class="btn-apple-pay">
                        <span class="apple-pay-logo"></span>
                        <span class="apple-pay-text">Pay</span>
                      </button>
                      <p class="apple-pay-note">
                        Apple Pay is available on iPhone, iPad, and Mac with Safari.
                      </p>
                    </div>
                  </div>
                </div>
              </div>
              
              <div class="billing-address">
                <div class="billing-address-header">
                  <h3>Billing address</h3>
                  <div class="form-group checkbox-group">
                    <input 
                      type="checkbox" 
                      id="sameAsShipping" 
                      v-model="formData.sameBillingAddress"
                      @change="updateBillingAddress"
                    >
                    <label for="sameAsShipping">
                      Same as shipping address
                    </label>
                  </div>
                </div>
                
                <div v-if="!formData.sameBillingAddress" class="billing-address-fields">
                  <div class="form-row">
                    <div class="form-group">
                      <label for="billingFirstName">First name *</label>
                      <input 
                        type="text" 
                        id="billingFirstName" 
                        v-model="formData.billing.firstName" 
                        required
                        :class="{ 'error': errors['billing.firstName'] }"
                        @input="clearError('billing.firstName')"
                      >
                      <div v-if="errors['billing.firstName']" class="error-message">
                        {{ errors['billing.firstName'] }}
                      </div>
                    </div>
                    <div class="form-group">
                      <label for="billingLastName">Last name *</label>
                      <input 
                        type="text" 
                        id="billingLastName" 
                        v-model="formData.billing.lastName" 
                        required
                        :class="{ 'error': errors['billing.lastName'] }"
                        @input="clearError('billing.lastName')"
                      >
                      <div v-if="errors['billing.lastName']" class="error-message">
                        {{ errors['billing.lastName'] }}
                      </div>
                    </div>
                  </div>
                  
                  <div class="form-group">
                    <label for="billingAddress">Address *</label>
                    <input 
                      type="text" 
                      id="billingAddress" 
                      v-model="formData.billing.address1" 
                      required
                      placeholder="Street address"
                      :class="{ 'error': errors['billing.address1'] }"
                      @input="clearError('billing.address1')"
                    >
                    <div v-if="errors['billing.address1']" class="error-message">
                      {{ errors['billing.address1'] }}
                    </div>
                    <input 
                      type="text" 
                      v-model="formData.billing.address2" 
                      placeholder="Apartment, suite, etc. (optional)"
                      class="mt-2"
                    >
                  </div>
                  
                  <div class="form-row">
                    <div class="form-group">
                      <label for="billingCity">City *</label>
                      <input 
                        type="text" 
                        id="billingCity" 
                        v-model="formData.billing.city" 
                        required
                        :class="{ 'error': errors['billing.city'] }"
                        @input="clearError('billing.city')"
                      >
                      <div v-if="errors['billing.city']" class="error-message">
                        {{ errors['billing.city'] }}
                      </div>
                    </div>
                    <div class="form-group">
                      <label for="billingCountry">Country/Region *</label>
                      <select 
                        id="billingCountry" 
                        v-model="formData.billing.country" 
                        required
                        :class="{ 'error': errors['billing.country'] }"
                        @change="clearError('billing.country')"
                      >
                        <option value="">Select a country</option>
                        <option 
                          v-for="country in countries" 
                          :key="country.code" 
                          :value="country.code"
                        >
                          {{ country.name }}
                        </option>
                      </select>
                      <div v-if="errors['billing.country']" class="error-message">
                        {{ errors['billing.country'] }}
                      </div>
                    </div>
                  </div>
                  
                  <div class="form-row">
                    <div class="form-group">
                      <label for="billingState">State/Province *</label>
                      <select 
                        v-if="formData.billing.country === 'US'" 
                        id="billingState" 
                        v-model="formData.billing.state" 
                        required
                        :class="{ 'error': errors['billing.state'] }"
                        @change="clearError('billing.state')"
                      >
                        <option value="">Select a state</option>
                        <option 
                          v-for="(state, code) in usStates" 
                          :key="code" 
                          :value="code"
                        >
                          {{ state }}
                        </option>
                      </select>
                      <input 
                        v-else
                        type="text" 
                        id="billingState" 
                        v-model="formData.billing.state" 
                        required
                        :class="{ 'error': errors['billing.state'] }"
                        @input="clearError('billing.state')"
                      >
                      <div v-if="errors['billing.state']" class="error-message">
                        {{ errors['billing.state'] }}
                      </div>
                    </div>
                    <div class="form-group">
                      <label for="billingZip">ZIP/Postal code *</label>
                      <input 
                        type="text" 
                        id="billingZip" 
                        v-model="formData.billing.zip" 
                        required
                        :class="{ 'error': errors['billing.zip'] }"
                        @input="clearError('billing.zip')"
                      >
                      <div v-if="errors['billing.zip']" class="error-message">
                        {{ errors['billing.zip'] }}
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              
              <div class="form-actions">
                <button 
                  type="button" 
                  class="btn btn-outline"
                  @click="currentStep--"
                  :disabled="isSubmitting"
                >
                  &larr; Return to shipping
                </button>
                <button 
                  type="submit" 
                  class="btn btn-primary"
                  :disabled="isSubmitting"
                >
                  <span v-if="isSubmitting" class="spinner"></span>
                  <span v-else>Pay {{ formatPrice(orderTotal) }}</span>
                </button>
              </div>
            </div>
            
            <!-- Step 4: Order Review -->
            <div v-else-if="currentStep === 4" class="form-step">
              <div class="order-confirmation">
                <div class="confirmation-header">
                  <div class="confirmation-icon">
                    <svg viewBox="0 0 24 24" width="48" height="48">
                      <path fill="#27ae60" d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z"></path>
                    </svg>
                  </div>
                  <h2>Thank you for your order!</h2>
                  <p class="order-number">Order #{{ orderNumber }}</p>
                  <p class="confirmation-message">
                    A confirmation email has been sent to <strong>{{ formData.email }}</strong>.
                  </p>
                </div>
                
                <div class="order-summary">
                  <h3>Order Summary</h3>
                  
                      <div class="order-items">
                        <div v-for="(item, index) in cartItems" :key="index" class="order-item">
                          <div class="order-item-image">
                            <img :src="item.image" :alt="item.name">
                            <span class="quantity-badge">{{ item.quantity }}</span>
                          </div>
                          <div class="order-item-details">
                            <h4>{{ item.name }}</h4>
                            <div v-if="item.variant" class="variant-details">
                              <span 
                                v-for="(value, key) in item.variant" 
                                :key="key"
                                class="variant"
                              >
                                {{ key }}: {{ value }}
                              </span>
                            </div>
                          </div>
                          <div class="order-item-price">
                            {{ formatPrice(item.price * item.quantity) }}
                          </div>
                        </div>
                      </div>
                      
                      <div class="order-totals">
                        <div class="order-total-row">
                          <span>Subtotal ({{ totalItems }} {{ totalItems === 1 ? 'item' : 'items' }})</span>
                          <span>{{ formatPrice(subtotal) }}</span>
                        </div>
                        <div v-if="appliedCoupon" class="order-total-row">
                          <span>Discount ({{ appliedCoupon.code }})</span>
                          <span class="discount">-{{ formatPrice(discount) }}</span>
                        </div>
                        <div class="order-total-row">
                          <span>Shipping</span>
                          <span>{{ shippingCost === 0 ? 'Free' : formatPrice(shippingCost) }}</span>
                        </div>
                        <div class="order-total-row total">
                          <span>Total</span>
                          <span>{{ formatPrice(orderTotal) }}</span>
                        </div>
                      </div>
                    </div>
                    
                    <div class="shipping-info">
                      <div class="shipping-address">
                        <h3>Shipping Address</h3>
                        <address>
                          {{ formData.shipping.firstName }} {{ formData.shipping.lastName }}<br>
                          <template v-if="formData.shipping.company">
                            {{ formData.shipping.company }}<br>
                          </template>
                          {{ formData.shipping.address1 }}<br>
                          <template v-if="formData.shipping.address2">
                            {{ formData.shipping.address2 }}<br>
                          </template>
                          {{ formData.shipping.city }}, {{ formData.shipping.state }} {{ formData.shipping.zip }}<br>
                          {{ getCountryName(formData.shipping.country) }}<br>
                          {{ formData.shipping.phone }}
                        </address>
                      </div>
                      
                      <div class="shipping-method">
                        <h3>Shipping Method</h3>
                        <p>{{ getShippingMethodName(formData.shippingMethod) }}</p>
                        <p>Estimated delivery: {{ getEstimatedDelivery() }}</p>
                      </div>
                      
                      <div class="billing-address" v-if="!formData.sameBillingAddress">
                        <h3>Billing Address</h3>
                        <address>
                          {{ formData.billing.firstName }} {{ formData.billing.lastName }}<br>
                          <template v-if="formData.billing.company">
                            {{ formData.billing.company }}<br>
                          </template>
                          {{ formData.billing.address1 }}<br>
                          <template v-if="formData.billing.address2">
                            {{ formData.billing.address2 }}<br>
                          </template>
                          {{ formData.billing.city }}, {{ formData.billing.state }} {{ formData.billing.zip }}<br>
                          {{ getCountryName(formData.billing.country) }}
                        </address>
                      </div>
                      
                      <div class="payment-method">
                        <h3>Payment Method</h3>
                        <div v-if="formData.paymentMethod === 'credit-card'">
                          <p>Credit Card ending in {{ formData.payment.cardNumber.slice(-4) }}</p>
                          <p>Expires {{ formData.payment.expiryDate }}</p>
                        </div>
                        <div v-else-if="formData.paymentMethod === 'paypal'">
                          <p>PayPal</p>
                        </div>
                        <div v-else-if="formData.paymentMethod === 'apple-pay'">
                          <p>Apple Pay</p>
                        </div>
                      </div>
                    </div>
                    
                    <div class="order-actions">
                      <router-link to="/products" class="btn btn-primary">
                        Continue Shopping
                      </router-link>
                      <button 
                        type="button" 
                        class="btn btn-outline"
                        @click="printOrder"
                      >
                        Print Order
                      </button>
                    </div>
                  </div>
                </div>
              </form>
            </div>
          </div>
          
          <!-- Right Column - Order Summary -->
          <div class="order-summary-container">
            <div class="order-summary-card">
              <h3>Order Summary</h3>
              
              <div class="order-items">
                <div v-for="(item, index) in cartItems" :key="index" class="order-item">
                  <div class="order-item-image">
                    <img :src="item.image" :alt="item.name">
                    <span class="quantity-badge">{{ item.quantity }}</span>
                  </div>
                  <div class="order-item-details">
                    <h4>{{ item.name }}</h4>
                    <div v-if="item.variant" class="variant-details">
                      <span 
                        v-for="(value, key) in item.variant" 
                        :key="key"
                        class="variant"
                      >
                        {{ key }}: {{ value }}
                      </span>
                    </div>
                  </div>
                  <div class="order-item-price">
                    {{ formatPrice(item.price * item.quantity) }}
                  </div>
                </div>
              </div>
              
              <div class="order-totals">
                <div class="order-total-row">
                  <span>Subtotal ({{ totalItems }} {{ totalItems === 1 ? 'item' : 'items' }})</span>
                  <span>{{ formatPrice(subtotal) }}</span>
                </div>
                <div v-if="appliedCoupon" class="order-total-row">
                  <span>Discount ({{ appliedCoupon.code }})</span>
                  <span class="discount">-{{ formatPrice(discount) }}</span>
                </div>
                <div class="order-total-row">
                  <span>Shipping</span>
                  <span>{{ shippingCost === 0 ? 'Free' : formatPrice(shippingCost) }}</span>
                </div>
                <div class="order-total-row total">
                  <span>Total</span>
                  <span>{{ formatPrice(orderTotal) }}</span>
                </div>
              </div>
              
              <div v-if="currentStep < 4" class="secure-checkout-badge">
                <span class="lock-icon">🔒</span>
                <span>Secure Checkout</span>
              </div>
              
              <div v-if="currentStep < 3" class="accepted-payments">
                <span>We accept:</span>
                <div class="payment-methods">
                  <span>💳</span>
                  <span>🏦</span>
                  <span>📱</span>
                  <span>💎</span>
                </div>
              </div>
            </div>
            
            <div v-if="currentStep < 4" class="need-help">
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
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';

interface Country {
  code: string;
  name: string;
}

interface ShippingOption {
  id: string;
  name: string;
  description: string;
  price: number;
  estimatedDelivery: string;
}

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
  name: 'Checkout',
  setup() {
    const router = useRouter();
    
    // Form data
    const formData = ref({
      email: '',
      emailUpdates: false,
      saveInfo: false,
      shipping: {
        firstName: '',
        lastName: '',
        company: '',
        address1: '',
        address2: '',
        city: '',
        country: '',
        state: '',
        zip: '',
        phone: ''
      },
      shippingMethod: '',
      sameBillingAddress: true,
      billing: {
        firstName: '',
        lastName: '',
        company: '',
        address1: '',
        address2: '',
        city: '',
        country: '',
        state: '',
        zip: ''
      },
      paymentMethod: 'credit-card',
      payment: {
        cardNumber: '',
        cardName: '',
        expiryDate: '',
        cvv: ''
      },
      saveCard: false
    });
    
    // UI State
    const currentStep = ref(1);
    const steps = ['Information', 'Shipping', 'Payment', 'Review'];
    const isSubmitting = ref(false);
    const showCvvInfo = ref(false);
    const cardType = ref<string | null>(null);
    const errors = ref<Record<string, string>>({});
    const orderNumber = ref('');
    
    // Cart data (in a real app, this would come from a store)
    const cartItems = ref<CartItem[]>([]);
    const appliedCoupon = ref<Coupon | null>(null);
    const subtotal = ref(0);
    const discount = ref(0);
    const shippingCost = ref(0);
    const orderTotal = ref(0);
    const totalItems = computed(() => {
      return cartItems.value.reduce((sum, item) => sum + item.quantity, 0);
    });
    
    // Shipping options
    const shippingOptions: ShippingOption[] = [
      {
        id: 'standard',
        name: 'Standard Shipping',
        description: 'Delivered in 3-5 business days',
        price: 4.99,
        estimatedDelivery: 'Estimated delivery: May 10 - 12'
      },
      {
        id: 'express',
        name: 'Express Shipping',
        description: 'Delivered in 1-2 business days',
        price: 9.99,
        estimatedDelivery: 'Estimated delivery: May 6 - 7'
      },
      {
        id: 'overnight',
        name: 'Overnight Shipping',
        description: 'Next business day delivery',
        price: 19.99,
        estimatedDelivery: 'Estimated delivery: Tomorrow by 5 PM'
      }
    ];
    
    // Countries list
    const countries: Country[] = [
      { code: 'US', name: 'United States' },
      { code: 'CA', name: 'Canada' },
      { code: 'GB', name: 'United Kingdom' },
      { code: 'AU', name: 'Australia' },
      { code: 'DE', name: 'Germany' },
      { code: 'FR', name: 'France' },
      { code: 'JP', name: 'Japan' },
      { code: 'SG', name: 'Singapore' }
    ];
    
    // US States
    const usStates: Record<string, string> = {
      AL: 'Alabama',
      AK: 'Alaska',
      AZ: 'Arizona',
      AR: 'Arkansas',
      CA: 'California',
      CO: 'Colorado',
      CT: 'Connecticut',
      DE: 'Delaware',
      FL: 'Florida',
      GA: 'Georgia',
      HI: 'Hawaii',
      ID: 'Idaho',
      IL: 'Illinois',
      IN: 'Indiana',
      IA: 'Iowa',
      KS: 'Kansas',
      KY: 'Kentucky',
      LA: 'Louisiana',
      ME: 'Maine',
      MD: 'Maryland',
      MA: 'Massachusetts',
      MI: 'Michigan',
      MN: 'Minnesota',
      MS: 'Mississippi',
      MO: 'Missouri',
      MT: 'Montana',
      NE: 'Nebraska',
      NV: 'Nevada',
      NH: 'New Hampshire',
      NJ: 'New Jersey',
      NM: 'New Mexico',
      NY: 'New York',
      NC: 'North Carolina',
      ND: 'North Dakota',
      OH: 'Ohio',
      OK: 'Oklahoma',
      OR: 'Oregon',
      PA: 'Pennsylvania',
      RI: 'Rhode Island',
      SC: 'South Carolina',
      SD: 'South Dakota',
      TN: 'Tennessee',
      TX: 'Texas',
      UT: 'Utah',
      VT: 'Vermont',
      VA: 'Virginia',
      WA: 'Washington',
      WV: 'West Virginia',
      WI: 'Wisconsin',
      WY: 'Wyoming'
    };
    
    // Mock cart data (in a real app, this would come from a store)
    const loadCartData = () => {
      // In a real app, you would fetch this from your API or store
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
      
      // Calculate order totals
      calculateTotals();
    };
    
    // Calculate order totals
    const calculateTotals = () => {
      // Calculate subtotal
      subtotal.value = cartItems.value.reduce((sum, item) => {
        return sum + (item.price * item.quantity);
      }, 0);
      
      // Calculate discount (if any)
      if (appliedCoupon.value) {
        const coupon = appliedCoupon.value;
        if (coupon.type === 'percentage') {
          discount.value = (subtotal.value * coupon.discount) / 100;
        } else {
          discount.value = Math.min(coupon.discount, subtotal.value);
        }
      } else {
        discount.value = 0;
      }
      
      // Set shipping cost based on selected method or default to standard
      if (formData.value.shippingMethod) {
        const method = shippingOptions.find(m => m.id === formData.value.shippingMethod);
        shippingCost.value = method ? method.price : 0;
      } else {
        // Default to standard shipping if not selected yet
        shippingCost.value = 4.99;
      }
      
      // Calculate total
      orderTotal.value = Math.max(0, (subtotal.value - discount.value) + shippingCost.value);
    };
    
    // Format price
    const formatPrice = (price: number) => {
      return new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD',
        minimumFractionDigits: 2
      }).format(price);
    };
    
    // Get country name by code
    const getCountryName = (code: string) => {
      const country = countries.find(c => c.code === code);
      return country ? country.name : code;
    };
    
    // Get shipping method name by ID
    const getShippingMethodName = (id: string) => {
      const method = shippingOptions.find(m => m.id === id);
      return method ? method.name : '';
    };
    
    // Get estimated delivery date
    const getEstimatedDelivery = () => {
      if (!formData.value.shippingMethod) return '';
      
      const method = shippingOptions.find(m => m.id === formData.value.shippingMethod);
      return method ? method.estimatedDelivery : '';
    };
    
    // Format card number with spaces
    const formatCardNumber = () => {
      // Remove all non-digits
      let value = formData.value.payment.cardNumber.replace(/\D/g, '');
      
      // Detect card type
      if (/^4/.test(value)) {
        cardType.value = 'visa';
      } else if (/^5[1-5]/.test(value)) {
        cardType.value = 'mastercard';
      } else if (/^3[47]/.test(value)) {
        cardType.value = 'amex';
      } else if (/^6(?:011|5)/.test(value)) {
        cardType.value = 'discover';
      } else {
        cardType.value = null;
      }
      
      // Add spaces for better readability
      if (cardType.value === 'amex') {
        // Amex format: 4-6-5
        value = value.replace(/^(\d{0,4})(\d{0,6})(\d{0,5}).*/, '$1 $2 $3').trim();
      } else {
        // Standard format: 4-4-4-4
        value = value.replace(/(\d{0,4})(\d{0,4})(\d{0,4})(\d{0,4}).*/, '$1 $2 $3 $4').trim();
      }
      
      formData.value.payment.cardNumber = value;
    };
    
    // Format expiry date as MM/YY
    const formatExpiryDate = () => {
      let value = formData.value.payment.expiryDate;
      
      // Remove all non-digits
      value = value.replace(/\D/g, '');
      
      // Add slash after 2 digits
      if (value.length > 2) {
        value = value.substring(0, 2) + '/' + value.substring(2, 4);
      }
      
      formData.value.payment.expiryDate = value;
    };
    
    // Format CVV (limit to 3-4 digits)
    const formatCVV = () => {
      formData.value.payment.cvv = formData.value.payment.cvv.replace(/\D/g, '').substring(0, 4);
    };
    
    // Update billing address when "same as shipping" is toggled
    const updateBillingAddress = () => {
      if (formData.value.sameBillingAddress) {
        const { firstName, lastName, company, address1, address2, city, country, state, zip } = formData.value.shipping;
        formData.value.billing = { firstName, lastName, company, address1, address2, city, country, state, zip };
      }
    };
    
    // Clear error when field is edited
    const clearError = (field: string) => {
      if (errors.value[field]) {
        const newErrors = { ...errors.value };
        delete newErrors[field];
        errors.value = newErrors;
      }
    };
    
    // Validate form
    const validateForm = (step: number) => {
      const newErrors: Record<string, string> = {};
      
      if (step === 1) {
        // Validate email
        if (!formData.value.email) {
          newErrors.email = 'Email is required';
        } else if (!/\S+@\S+\.\S+/.test(formData.value.email)) {
          newErrors.email = 'Please enter a valid email address';
        }
        
        // Validate shipping address
        if (!formData.value.shipping.firstName) {
          newErrors['shipping.firstName'] = 'First name is required';
        }
        
        if (!formData.value.shipping.lastName) {
          newErrors['shipping.lastName'] = 'Last name is required';
        }
        
        if (!formData.value.shipping.address1) {
          newErrors['shipping.address1'] = 'Address is required';
        }
        
        if (!formData.value.shipping.city) {
          newErrors['shipping.city'] = 'City is required';
        }
        
        if (!formData.value.shipping.country) {
          newErrors['shipping.country'] = 'Country is required';
        }
        
        if (!formData.value.shipping.state) {
          newErrors['shipping.state'] = 'State/Province is required';
        }
        
        if (!formData.value.shipping.zip) {
          newErrors['shipping.zip'] = 'ZIP/Postal code is required';
        } else if (!/^\d{5}(-\d{4})?$/.test(formData.value.shipping.zip)) {
          newErrors['shipping.zip'] = 'Please enter a valid ZIP code';
        }
        
        if (!formData.value.shipping.phone) {
          newErrors['shipping.phone'] = 'Phone number is required';
        } else if (!/^\+?[\d\s-]{10,}$/.test(formData.value.shipping.phone)) {
          newErrors['shipping.phone'] = 'Please enter a valid phone number';
        }
      } else if (step === 2) {
        // Validate shipping method
        if (!formData.value.shippingMethod) {
          newErrors.shippingMethod = 'Please select a shipping method';
        }
      } else if (step === 3) {
        // Validate payment method
        if (formData.value.paymentMethod === 'credit-card') {
          // Validate card number (basic validation)
          const cardNumber = formData.value.payment.cardNumber.replace(/\s/g, '');
          if (!cardNumber) {
            newErrors['payment.cardNumber'] = 'Card number is required';
          } else if (cardNumber.length < 13 || cardNumber.length > 19) {
            newErrors['payment.cardNumber'] = 'Please enter a valid card number';
          }
          
          // Validate card name
          if (!formData.value.payment.cardName) {
            newErrors['payment.cardName'] = 'Name on card is required';
          }
          
          // Validate expiry date
          if (!formData.value.payment.expiryDate) {
            newErrors['payment.expiryDate'] = 'Expiry date is required';
          } else {
            const [month, year] = formData.value.payment.expiryDate.split('/');
            if (!month || !year || month.length !== 2 || year.length !== 2) {
              newErrors['payment.expiryDate'] = 'Please use MM/YY format';
            } else {
              const now = new Date();
              const currentYear = now.getFullYear() % 100;
              const currentMonth = now.getMonth() + 1;
              
              const expMonth = parseInt(month, 10);
              const expYear = parseInt(year, 10);
              
              if (expMonth < 1 || expMonth > 12) {
                newErrors['payment.expiryDate'] = 'Invalid month';
              } else if (expYear < currentYear || (expYear === currentYear && expMonth < currentMonth)) {
                newErrors['payment.expiryDate'] = 'Card has expired';
              }
            }
          }
          
          // Validate CVV
          if (!formData.value.payment.cvv) {
            newErrors['payment.cvv'] = 'Security code is required';
          } else if (formData.value.payment.cvv.length < 3 || formData.value.payment.cvv.length > 4) {
            newErrors['payment.cvv'] = 'Invalid security code';
          }
        }
        
        // Validate billing address if different from shipping
        if (!formData.value.sameBillingAddress) {
          if (!formData.value.billing.firstName) {
            newErrors['billing.firstName'] = 'First name is required';
          }
          
          if (!formData.value.billing.lastName) {
            newErrors['billing.lastName'] = 'Last name is required';
          }
          
          if (!formData.value.billing.address1) {
            newErrors['billing.address1'] = 'Address is required';
          }
          
          if (!formData.value.billing.city) {
            newErrors['billing.city'] = 'City is required';
          }
          
          if (!formData.value.billing.country) {
            newErrors['billing.country'] = 'Country is required';
          }
          
          if (!formData.value.billing.state) {
            newErrors['billing.state'] = 'State/Province is required';
          }
          
          if (!formData.value.billing.zip) {
            newErrors['billing.zip'] = 'ZIP/Postal code is required';
          } else if (!/^\d{5}(-\d{4})?$/.test(formData.value.billing.zip)) {
            newErrors['billing.zip'] = 'Please enter a valid ZIP code';
          }
        }
      }
      
      errors.value = newErrors;
      return Object.keys(newErrors).length === 0;
    };
    
    // Handle form submission
    const handleSubmit = async () => {
      if (currentStep.value < 4) {
        if (validateForm(currentStep.value)) {
          if (currentStep.value === 3) {
            // Process payment on the final step
            await processPayment();
          } else {
            // Go to next step
            currentStep.value++;
            window.scrollTo(0, 0);
          }
        } else {
          // Scroll to first error
          const firstError = Object.keys(errors.value)[0];
          if (firstError) {
            const element = document.getElementById(firstError.split('.').pop() || '');
            if (element) {
              element.scrollIntoView({ behavior: 'smooth', block: 'center' });
              element.focus();
            }
          }
        }
      }
    };
    
    // Process payment (mock)
    const processPayment = async () => {
      isSubmitting.value = true;
      
      try {
        // In a real app, you would call your payment API here
        await new Promise(resolve => setTimeout(resolve, 2000));
        
        // Generate a random order number
        orderNumber.value = 'ORD-' + Math.floor(100000 + Math.random() * 900000);
        
        // Go to confirmation step
        currentStep.value++;
        window.scrollTo(0, 0);
      } catch (error) {
        console.error('Payment failed:', error);
        // Show error message to user
        alert('Payment failed. Please try again or use a different payment method.');
      } finally {
        isSubmitting.value = false;
      }
    };
    
    // Navigate to next step
    const goToNextStep = () => {
      if (validateForm(currentStep.value)) {
        currentStep.value++;
        window.scrollTo(0, 0);
      }
    };
    
    // Print order
    const printOrder = () => {
      window.print();
    };
    
    // Initialize component
    onMounted(() => {
      loadCartData();
      
      // Set default country to US
      formData.value.shipping.country = 'US';
      formData.value.billing.country = 'US';
      
      // Set default shipping method
      formData.value.shippingMethod = 'standard';
      
      // Set default payment method
      formData.value.paymentMethod = 'credit-card';
      
      // Generate a random order number for demo purposes
      orderNumber.value = 'ORD-' + Math.floor(100000 + Math.random() * 900000);
    });
    
    return {
      // Data
      formData,
      currentStep,
      steps,
      isSubmitting,
      showCvvInfo,
      cardType,
      errors,
      orderNumber,
      cartItems,
      appliedCoupon,
      subtotal,
      discount,
      shippingCost,
      orderTotal,
      totalItems,
      shippingOptions,
      countries,
      usStates,
      
      // Methods
      formatPrice,
      getCountryName,
      getShippingMethodName,
      getEstimatedDelivery,
      formatCardNumber,
      formatExpiryDate,
      formatCVV,
      updateBillingAddress,
      clearError,
      handleSubmit,
      goToNextStep,
      printOrder
    };
  }
});
</script>

<style scoped>
/* Base styles */
.checkout-page {
  background-color: #f8f9fa;
  min-height: 100vh;
  padding: 20px 0 60px;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;
  color: #2c3e50;
  line-height: 1.6;
}

.container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 20px;
}

/* Checkout Header */
.checkout-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 0;
  margin-bottom: 30px;
  border-bottom: 1px solid #e1e4e8;
}

.logo a {
  font-size: 1.8rem;
  font-weight: 700;
  color: #2c3e50;
  text-decoration: none;
  letter-spacing: -0.5px;
}

.steps {
  display: flex;
  flex: 1;
  justify-content: center;
  margin: 0 40px;
}

.step {
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
  flex: 1;
  max-width: 200px;
}

.step:not(.last)::after {
  content: '';
  position: absolute;
  top: 15px;
  left: 60%;
  right: -40%;
  height: 2px;
  background-color: #e1e4e8;
  z-index: 1;
}

.step.completed:not(.last)::after {
  background-color: #3498db;
}

.step-number {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background-color: #e1e4e8;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  color: #6c757d;
  margin-bottom: 8px;
  position: relative;
  z-index: 2;
}

.step.active .step-number {
  background-color: #3498db;
  color: white;
}

.step.completed .step-number {
  background-color: #27ae60;
  color: white;
}

.step-label {
  font-size: 0.9rem;
  color: #6c757d;
  text-align: center;
  font-weight: 500;
}

.step.active .step-label {
  color: #3498db;
  font-weight: 600;
}

.step.completed .step-label {
  color: #27ae60;
}

.secure-checkout {
  display: flex;
  align-items: center;
  font-size: 0.9rem;
  color: #6c757d;
}

.lock-icon {
  margin-right: 6px;
  font-size: 1rem;
}

/* Checkout Container */
.checkout-container {
  display: flex;
  gap: 30px;
  align-items: flex-start;
}

/* Checkout Form */
.checkout-form-container {
  flex: 2;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 15px rgba(0, 0, 0, 0.05);
  overflow: hidden;
  margin-bottom: 30px;
}

.checkout-form {
  padding: 30px;
}

.form-step {
  animation: fadeIn 0.3s ease-in-out;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

h2 {
  font-size: 1.5rem;
  margin-top: 0;
  margin-bottom: 20px;
  color: #2c3e50;
  font-weight: 600;
}

h3 {
  font-size: 1.2rem;
  margin: 25px 0 15px;
  color: #2c3e50;
  font-weight: 600;
}

.form-group {
  margin-bottom: 20px;
}

.form-row {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
}

.form-row .form-group {
  flex: 1;
  margin-bottom: 0;
}

label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #2c3e50;
  font-size: 0.95rem;
}

input[type="text"],
input[type="email"],
input[type="tel"],
input[type="number"],
select,
textarea {
  width: 100%;
  padding: 12px 15px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
  transition: border-color 0.2s, box-shadow 0.2s;
  background-color: #fff;
}

input[type="text"]:focus,
input[type="email"]:focus,
input[type="tel"]:focus,
input[type="number"]:focus,
select:focus,
textarea:focus {
  border-color: #3498db;
  outline: none;
  box-shadow: 0 0 0 2px rgba(52, 152, 219, 0.2);
}

input.error,
select.error {
  border-color: #e74c3c;
}

.error-message {
  color: #e74c3c;
  font-size: 0.85rem;
  margin-top: 5px;
}

.form-note {
  font-size: 0.85rem;
  color: #6c757d;
  margin-top: 5px;
}

.checkbox-group {
  display: flex;
  align-items: center;
}

.checkbox-group input[type="checkbox"] {
  width: auto;
  margin-right: 10px;
}

.checkbox-group label {
  margin-bottom: 0;
  font-weight: normal;
  cursor: pointer;
}

/* Shipping Options */
.shipping-options {
  border: 1px solid #e1e4e8;
  border-radius: 6px;
  overflow: hidden;
  margin-bottom: 20px;
}

.shipping-option {
  display: flex;
  align-items: center;
  padding: 15px 20px;
  border-bottom: 1px solid #e1e4e8;
  cursor: pointer;
  transition: background-color 0.2s;
}

.shipping-option:last-child {
  border-bottom: none;
}

.shipping-option:hover {
  background-color: #f8f9fa;
}

.shipping-option.selected {
  background-color: #f0f8ff;
  border-left: 3px solid #3498db;
}

.shipping-option-radio {
  margin-right: 15px;
  position: relative;
}

.shipping-option-radio input[type="radio"] {
  position: absolute;
  opacity: 0;
  width: 0;
  height: 0;
}

.radio-custom {
  display: inline-block;
  width: 20px;
  height: 20px;
  border: 2px solid #bdc3c7;
  border-radius: 50%;
  position: relative;
  transition: all 0.2s;
}

.shipping-option-radio input[type="radio"]:checked + .radio-custom {
  border-color: #3498db;
  background-color: #3498db;
  box-shadow: inset 0 0 0 3px white;
}

.shipping-option-details {
  flex: 1;
}

.shipping-option-name {
  font-weight: 600;
  margin-bottom: 3px;
  color: #2c3e50;
}

.shipping-option-description {
  font-size: 0.9rem;
  color: #6c757d;
}

.shipping-option-price {
  font-weight: 600;
  color: #2c3e50;
  margin-left: 15px;
  white-space: nowrap;
}

/* Payment Methods */
.payment-methods {
  margin-bottom: 25px;
}

.payment-method-tabs {
  display: flex;
  border-bottom: 1px solid #e1e4e8;
  margin-bottom: 20px;
}

.payment-method-tabs button {
  padding: 12px 20px;
  background: none;
  border: none;
  border-bottom: 2px solid transparent;
  font-size: 0.95rem;
  font-weight: 500;
  color: #6c757d;
  cursor: pointer;
  display: flex;
  align-items: center;
  transition: all 0.2s;
}

.payment-method-tabs button:hover {
  color: #3498db;
}

.payment-method-tabs button.active {
  color: #3498db;
  border-bottom-color: #3498db;
  font-weight: 600;
}

.payment-icon {
  margin-right: 8px;
  font-size: 1.2rem;
}

.payment-method-content {
  padding: 20px 0;
}

.input-with-icon {
  position: relative;
}

.card-type {
  position: absolute;
  right: 15px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 0.8rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.visa { color: #1a1f71; }
.mastercard { color: #eb001b; }
.amex { color: #006fcf; }
.discover { color: #ff6600; }

.info-icon {
  position: absolute;
  right: 15px;
  top: 50%;
  transform: translateY(-50%);
  width: 20px;
  height: 20px;
  border-radius: 50%;
  background-color: #e1e4e8;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.8rem;
  color: #6c757d;
  cursor: help;
  position: relative;
}

.tooltip {
  position: absolute;
  bottom: 100%;
  right: 0;
  margin-bottom: 10px;
  padding: 8px 12px;
  background-color: #2c3e50;
  color: white;
  border-radius: 4px;
  font-size: 0.85rem;
  width: 200px;
  z-index: 10;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  display: none;
}

.tooltip-arrow {
  position: absolute;
  top: 100%;
  right: 20px;
  width: 0;
  height: 0;
  border-left: 6px solid transparent;
  border-right: 6px solid transparent;
  border-top: 6px solid #2c3e50;
}

.info-icon:hover .tooltip {
  display: block;
}

/* Billing Address */
.billing-address {
  margin: 30px 0;
  padding: 20px;
  background-color: #f8f9fa;
  border-radius: 6px;
}

.billing-address-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.billing-address h3 {
  margin: 0;
  font-size: 1.1rem;
}

.billing-address-fields {
  margin-top: 15px;
  padding-top: 15px;
  border-top: 1px solid #e1e4e8;
}

/* Order Summary */
.order-summary-container {
  flex: 1;
  position: sticky;
  top: 20px;
}

.order-summary-card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 15px rgba(0, 0, 0, 0.05);
  padding: 25px;
  margin-bottom: 20px;
}

.order-summary h3 {
  margin-top: 0;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #e1e4e8;
  font-size: 1.3rem;
}

.order-items {
  margin-bottom: 20px;
}

.order-item {
  display: flex;
  margin-bottom: 15px;
  padding-bottom: 15px;
  border-bottom: 1px solid #f0f0f0;
}

.order-item:last-child {
  margin-bottom: 0;
  padding-bottom: 0;
  border-bottom: none;
}

.order-item-image {
  position: relative;
  width: 70px;
  height: 70px;
  margin-right: 15px;
  flex-shrink: 0;
  border: 1px solid #eee;
  border-radius: 4px;
  overflow: hidden;
}

.order-item-image img {
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
  width: 22px;
  height: 22px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.7rem;
  font-weight: 600;
}

.order-item-details {
  flex: 1;
  min-width: 0;
}

.order-item-details h4 {
  margin: 0 0 5px;
  font-size: 0.95rem;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.variant-details {
  font-size: 0.8rem;
  color: #6c757d;
}

.variant {
  display: block;
  margin-bottom: 2px;
}

.order-item-price {
  font-weight: 600;
  color: #2c3e50;
  margin-left: 10px;
  white-space: nowrap;
}

.order-totals {
  border-top: 1px solid #e1e4e8;
  padding-top: 15px;
  margin-top: 15px;
}

.order-total-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
  font-size: 0.95rem;
}

.order-total-row.total {
  font-size: 1.1rem;
  font-weight: 600;
  margin-top: 15px;
  padding-top: 10px;
  border-top: 1px solid #e1e4e8;
}

.discount {
  color: #27ae60;
}

.secure-checkout-badge {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 20px;
  padding: 10px;
  background-color: #f8f9fa;
  border-radius: 4px;
  font-size: 0.9rem;
  color: #6c757d;
}

.secure-checkout-badge .lock-icon {
  margin-right: 8px;
  font-size: 1rem;
}

.accepted-payments {
  text-align: center;
  margin-top: 20px;
  padding-top: 15px;
  border-top: 1px dashed #ddd;
  font-size: 0.85rem;
  color: #6c757d;
}

.payment-methods {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-top: 8px;
  font-size: 1.5rem;
}

/* Need Help */
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
  color: #6c757d;
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
  color: #3498db;
}

/* Form Actions */
.form-actions {
  display: flex;
  justify-content: space-between;
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px solid #e1e4e8;
}

.back-link {
  display: inline-flex;
  align-items: center;
  color: #3498db;
  text-decoration: none;
  font-weight: 500;
  transition: color 0.2s;
}

.back-link:hover {
  color: #2980b9;
  text-decoration: underline;
}

.btn {
  padding: 12px 25px;
  border-radius: 4px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
  border: none;
  font-size: 1rem;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

.btn-primary {
  background-color: #3498db;
  color: white;
}

.btn-primary:hover:not(:disabled) {
  background-color: #2980b9;
}

.btn-outline {
  background: none;
  border: 1px solid #3498db;
  color: #3498db;
}

.btn-outline:hover:not(:disabled) {
  background-color: #f0f8ff;
}

.btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

/* PayPal Button */
.btn-paypal {
  background-color: #ffc439;
  border: none;
  border-radius: 4px;
  padding: 12px 20px;
  margin-top: 15px;
  cursor: pointer;
  transition: background-color 0.2s;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.btn-paypal:hover {
  background-color: #f7bb23;
}

.btn-paypal img {
  height: 20px;
}

/* Apple Pay Button */
.btn-apple-pay {
  background-color: #000;
  border: none;
  border-radius: 4px;
  padding: 12px 20px;
  margin: 15px 0;
  cursor: pointer;
  color: white;
  font-family: -apple-system, BlinkMacSystemFont, sans-serif;
  font-size: 1rem;
  font-weight: 500;
  letter-spacing: 0.5px;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
}

.apple-pay-logo {
  display: inline-block;
  width: 18px;
  height: 18px;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='white'%3E%3Cpath d='M18.71 19.5c-.83 1.24-1.71 2.45-3.05 2.47-1.34.03-1.77-.79-3.29-.79-1.53 0-2 .77-3.27.82-1.31.05-2.3-1.32-3.14-2.53C4.25 17 2.94 12.45 4.7 9.39c.87-1.52 2.43-2.48 4.12-2.51 1.28-.02 2.5.87 3.29.87.78 0 2.26-1.07 3.81-.91.65.03 2.47.26 3.64 1.98-.09.06-2.17 1.28-2.15 3.81.03 3.02 2.65 4.03 2.68 4.04-.03.07-.42 1.44-1.38 2.83M13 3.5c.73-.83 1.94-1.46 2.94-1.5.13 1.17-.34 2.35-1.04 3.19-.69.85-1.83 1.51-2.95 1.42-.15-1.15.41-2.35 1.05-3.11z'/%3E%3C/svg%3E");
  background-size: contain;
  background-repeat: no-repeat;
  margin-right: 8px;
}

.apple-pay-note {
  font-size: 0.8rem;
  color: #6c757d;
  margin-top: 10px;
}

/* Order Confirmation */
.order-confirmation {
  text-align: center;
  padding: 30px 0;
}

.confirmation-header {
  margin-bottom: 40px;
}

.confirmation-icon {
  width: 80px;
  height: 80px;
  background-color: #e8f8f0;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 20px;
}

.confirmation-header h2 {
  font-size: 2rem;
  margin-bottom: 10px;
  color: #27ae60;
}

.order-number {
  font-size: 1.1rem;
  color: #6c757d;
  margin-bottom: 20px;
}

.confirmation-message {
  font-size: 1.1rem;
  color: #2c3e50;
  max-width: 600px;
  margin: 0 auto 30px;
  line-height: 1.6;
}

.order-summary {
  text-align: left;
  max-width: 800px;
  margin: 0 auto 40px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 15px rgba(0, 0, 0, 0.05);
  padding: 30px;
}

.order-summary h3 {
  text-align: center;
  margin-top: 0;
  padding-bottom: 15px;
  border-bottom: 1px solid #e1e4e8;
}

.order-actions {
  display: flex;
  justify-content: center;
  gap: 15px;
  margin-top: 30px;
}

/* Shipping Info */
.shipping-info {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 30px;
  margin-top: 30px;
  padding-top: 30px;
  border-top: 1px solid #e1e4e8;
}

.shipping-address h3,
.shipping-method h3,
.billing-address h3,
.payment-method h3 {
  margin-top: 0;
  font-size: 1.1rem;
  color: #2c3e50;
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 1px solid #f0f0f0;
}

address {
  font-style: normal;
  line-height: 1.6;
  color: #6c757d;
}

/* Spinner */
.spinner {
  display: inline-block;
  width: 20px;
  height: 20px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  border-top-color: white;
  animation: spin 1s ease-in-out infinite;
  margin-right: 8px;
}

.btn-outline .spinner {
  border-top-color: #3498db;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

/* Responsive Styles */
@media (max-width: 1200px) {
  .checkout-container {
    flex-direction: column;
  }
  
  .order-summary-container {
    width: 100%;
    position: static;
    margin-top: 30px;
  }
  
  .checkout-form-container,
  .order-summary-card {
    width: 100%;
  }
}

@media (max-width: 992px) {
  .checkout-header {
    flex-direction: column;
    text-align: center;
  }
  
  .logo {
    margin-bottom: 20px;
  }
  
  .steps {
    margin: 20px 0;
    width: 100%;
  }
  
  .secure-checkout {
    margin-top: 20px;
  }
  
  .form-row {
    flex-direction: column;
    gap: 0;
  }
  
  .form-row .form-group {
    margin-bottom: 20px;
  }
  
  .form-actions {
    flex-direction: column;
    gap: 15px;
  }
  
  .back-link {
    justify-content: center;
  }
  
  .btn {
    width: 100%;
  }
  
  .shipping-info {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .step:not(.last)::after {
    display: none;
  }
  
  .step {
    flex: none;
    width: 25%;
    max-width: none;
  }
  
  .step-label {
    font-size: 0.8rem;
  }
  
  .checkout-form {
    padding: 20px 15px;
  }
  
  .shipping-option {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .shipping-option-details {
    margin: 10px 0;
    width: 100%;
  }
  
  .shipping-option-price {
    margin-left: 0;
    width: 100%;
    text-align: right;
  }
  
  .payment-method-tabs {
    flex-direction: column;
  }
  
  .payment-method-tabs button {
    justify-content: center;
    border-bottom: 1px solid #e1e4e8;
  }
  
  .payment-method-tabs button.active {
    border-bottom: 2px solid #3498db;
  }
  
  .order-actions {
    flex-direction: column;
  }
  
  .order-actions .btn {
    width: 100%;
  }
}

@media (max-width: 480px) {
  .step {
    width: 25%;
  }
  
  .step-label {
    font-size: 0.7rem;
  }
  
  .step-number {
    width: 26px;
    height: 26px;
    font-size: 0.9rem;
  }
  
  .order-item {
    flex-wrap: wrap;
  }
  
  .order-item-price {
    width: 100%;
    text-align: right;
    margin-top: 10px;
    margin-left: 0;
  }
  
  .order-confirmation {
    padding: 20px 0;
  }
  
  .confirmation-header h2 {
    font-size: 1.5rem;
  }
  
  .confirmation-message {
    font-size: 1rem;
  }
  
  .order-summary {
    padding: 20px 15px;
  }
}

/* Print Styles */
@media print {
  .checkout-header,
  .order-summary-container,
  .form-actions,
  .back-link,
  .btn {
    display: none !important;
  }
  
  .checkout-container {
    display: block;
  }
  
  .checkout-form-container {
    box-shadow: none;
    border: none;
    padding: 0;
    margin: 0;
  }
  
  .order-confirmation {
    text-align: left;
    padding: 0;
  }
  
  .confirmation-header {
    text-align: center;
    margin-bottom: 20px;
  }
  
  .confirmation-icon {
    display: none;
  }
  
  .order-summary {
    box-shadow: none;
    padding: 0;
    margin: 0 0 30px;
  }
  
  .shipping-info {
    grid-template-columns: 1fr 1fr;
    gap: 20px;
    margin-top: 20px;
    padding-top: 20px;
    border-top: 1px solid #eee;
  }
  
  .billing-address,
  .payment-method {
    margin-top: 20px;
  }
  
  .order-actions {
    display: none;
  }
  
  @page {
    margin: 1cm;
  }
}
</style>
