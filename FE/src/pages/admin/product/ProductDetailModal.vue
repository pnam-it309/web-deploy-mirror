<template>
  <div class="modal-backdrop">
    <div class="modal">
      <h3>Chi tiết sản phẩm</h3>
      <form @submit.prevent="updateProduct">
        <label>SKU:</label>
        <input v-model="form.sku" disabled />

        <label>Tên sản phẩm:</label>
        <input v-model="form.name" />

        <label>Giá:</label>
        <input type="number" v-model="form.price" step="0.01" />

        <label>Tồn kho:</label>
        <input type="number" v-model="form.stockQuantity" />

        <label>Danh mục:</label>
        <select v-model="form.categoryId">
          <option v-for="c in categories" :key="c.id" :value="c.id">{{ c.name }}</option>
        </select>

        <label>Thương hiệu:</label>
        <select v-model="form.brandId">
          <option v-for="b in brands" :key="b.id" :value="b.id">{{ b.name }}</option>
        </select>

        <label>Mô tả chi tiết:</label>
        <textarea v-model="form.productDetail.longDescription"></textarea>

        <label>Thông số kỹ thuật:</label>
        <textarea v-model="form.productDetail.specification"></textarea>

        <div class="modal-actions">
          <button type="submit" class="btn btn-primary">Lưu</button>
          <button type="button" class="btn btn-secondary" @click="$emit('close')">Đóng</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "ProductDetailModal",
  props: ["product"],
  data() {
    return {
      form: JSON.parse(JSON.stringify(this.product)),
      categories: [],
      brands: [
        { id: 1, name: "Apple" },
        { id: 2, name: "Samsung" },
        { id: 3, name: "Sony" },
      ],
    };
  },
  methods: {
    async loadCategories() {
      const res = await axios.get("/api/admin/categories");
      this.categories = res.data || [];
    },
    async updateProduct() {
      try {
        await axios.put(`/api/admin/products/${this.form.id}`, this.form);
        alert("Cập nhật thành công!");
        this.$emit("updated");
        this.$emit("close");
      } catch (e) {
        console.error(e);
        alert("Lỗi khi cập nhật!");
      }
    },
  },
  mounted() {
    this.loadCategories();
  },
};
</script>
