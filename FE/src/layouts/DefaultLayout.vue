<template>
  <div class="default-layout">
    <header class="header">
      <slot name="header">
        <!-- Default header content -->
        <nav class="navbar">
          <div class="container">
            <router-link to="/" class="logo">Your Logo</router-link>
            <div class="nav-links">
              <router-link to="/login" v-if="!isAuthenticated">Login</router-link>
              <a href="#" @click.prevent="handleLogout" v-else>Logout</a>
            </div>
          </div>
        </nav>
      </slot>
    </header>
    
    <main class="main-content">
      <slot></slot>
    </main>
    
    <footer class="footer">
      <slot name="footer">
        <div class="container">
          <p>&copy; 2025 Your Company. All rights reserved.</p>
        </div>
      </slot>
    </footer>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useAuthStore } from '@/stores/auth';
import { useRouter } from 'vue-router';

const authStore = useAuthStore();
const router = useRouter();

const isAuthenticated = computed(() => authStore.isAuthenticated);

const handleLogout = async () => {
  try {
    await authStore.logout();
    router.push('/login');
  } catch (error) {
    console.error('Logout failed:', error);
  }
};
</script>

<style scoped>
.default-layout {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.header {
  background-color: #ffffff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 100;
}

.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem 0;
}

.container {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 1rem;
}

.logo {
  font-size: 1.5rem;
  font-weight: bold;
  text-decoration: none;
  color: #333;
}

.nav-links a {
  margin-left: 1rem;
  text-decoration: none;
  color: #333;
}

.main-content {
  flex: 1;
  padding: 2rem 0;
}

.footer {
  background-color: #f8f9fa;
  padding: 1.5rem 0;
  margin-top: auto;
}

.footer p {
  text-align: center;
  color: #6c757d;
  margin: 0;
}
</style>
