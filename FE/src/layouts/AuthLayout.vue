<template>
  <div class="auth-layout">
    <div class="auth-container">
      <div class="auth-card">
        <div class="auth-header">
          <h1 class="auth-title">
            <slot name="title">Authentication</slot>
          </h1>
          <p class="auth-subtitle">
            <slot name="subtitle"></slot>
          </p>
        </div>

        <div class="auth-content">
          <slot></slot>
        </div>

        <div class="auth-footer">
          <slot name="footer">
            <p class="text-center text-sm text-gray-600">
              &copy; 2025 Your Company. All rights reserved.
            </p>
          </slot>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
// No script logic needed for this layout
</script>

<style scoped>
.auth-layout {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f8fafc;
  padding: 2rem 1rem;
}

.auth-container {
  width: 100%;
  max-width: 28rem;
  margin: 0 auto;
}

.auth-card {
  background: white;
  border-radius: 0.5rem;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
  padding: 2.5rem;
  width: 100%;
}

.auth-header {
  margin-bottom: 2rem;
  text-align: center;
}

.auth-title {
  font-size: 1.5rem;
  font-weight: 600;
  color: #1a202c;
  margin-bottom: 0.5rem;
}

.auth-subtitle {
  color: #718096;
  font-size: 0.875rem;
  margin-top: 0.5rem;
}

.auth-content {
  margin-bottom: 1.5rem;
}

.auth-footer {
  margin-top: 1.5rem;
  padding-top: 1.5rem;
  border-top: 1px solid #e2e8f0;
  text-align: center;
}

/* Responsive adjustments */
@media (max-width: 640px) {
  .auth-card {
    padding: 1.5rem;
  }

  .auth-title {
    font-size: 1.25rem;
  }
}
</style>
