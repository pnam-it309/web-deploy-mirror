<template>
  <div class="home-page">
    <header class="hero">
      <h1>Welcome to Our Store</h1>
      <p>Discover amazing products at great prices</p>
    </header>
    
    <section class="featured-products">
      <h2>Featured Products</h2>
      <div class="product-grid">
        <!-- Featured products will be listed here -->
        <div class="product-placeholder">
          <p>Featured products will be displayed here</p>
        </div>
      </div>
    </section>
    
    <section class="categories">
      <h2>Shop by Category</h2>
      <div class="category-grid">
        <!-- Categories will be listed here -->
        <div class="category-placeholder">
          <p>Categories will be displayed here</p>
        </div>
      </div>
    </section>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';

export default defineComponent({
  name: 'HomePage',
  setup() {
    return {
      // Component logic will go here
    };
  },
});
</script>

<style scoped>
.home-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.hero {
  text-align: center;
  padding: 60px 20px;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  border-radius: 10px;
  margin-bottom: 40px;
}

.hero h1 {
  font-size: 2.5rem;
  color: #2c3e50;
  margin-bottom: 15px;
}

.hero p {
  font-size: 1.2rem;
  color: #7f8c8d;
}

section {
  margin-bottom: 50px;
}

h2 {
  color: #2c3e50;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 2px solid #eee;
}

.product-grid,
.category-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
  margin-top: 20px;
}

.product-placeholder,
.category-placeholder {
  background: #fff;
  border-radius: 8px;
  padding: 30px;
  text-align: center;
  box-shadow: 0 2px 15px rgba(0, 0, 0, 0.05);
  min-height: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #7f8c8d;
}

@media (max-width: 768px) {
  .product-grid,
  .category-grid {
    grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  }
  
  .hero h1 {
    font-size: 2rem;
  }
  
  .hero p {
    font-size: 1rem;
  }
}
</style>
