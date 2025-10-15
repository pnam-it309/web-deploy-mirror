# Table Animation System

Hệ thống animation cho table với các hiệu ứng đẹp và có thể tái sử dụng.

## 📁 Files Created

### 1. `useTableAnimations.ts` (Composable)
- **Location:** `src/composable/useTableAnimations.ts`
- **Purpose:** Quản lý state và logic animation cho table
- **Features:**
  - Hover effects với scale và shadow
  - Row selection với màu nền đặc biệt
  - Loading animations với staggered effect
  - Skeleton loading states

### 2. `AnimatedTable.vue` (Component)
- **Location:** `src/components/AnimatedTable.vue`
- **Purpose:** Table component hoàn chỉnh với animations
- **Features:**
  - Sortable columns
  - Pagination
  - Loading states
  - Hover và selection effects

### 3. Global CSS (Updated)
- **Location:** `src/theme/index.css`
- **Added:** Table animation keyframes và utility classes

## 🚀 Cách sử dụng

### 1. Import Composable

```typescript
import { useTableAnimations } from '@/composable/useTableAnimations'

// Trong component
const animations = useTableAnimations({
  hoverScale: 1.02,
  hoverTransition: 'all 0.2s cubic-bezier(0.4, 0, 0.2, 1)',
  rowHoverColor: 'rgba(99, 102, 241, 0.1)',
  selectedRowColor: 'rgba(99, 102, 241, 0.2)',
})
```

### 2. Sử dụng trong Template

```vue
<template>
  <div class="table-container">
    <!-- Table với animations -->
    <table>
      <tbody>
        <tr
          v-for="(row, index) in data"
          :key="row.id"
          :style="animations.getRowStyle(index)"
          :class="animations.state.selectedRowIndex === index ? 'bg-indigo-50' : ''"
          @mouseenter="animations.handleRowHover(index)"
          @mouseleave="animations.handleRowHover(null)"
          @click="animations.handleRowClick(index)"
        >
          <!-- Table cells với animations -->
          <td
            v-for="(header, colIndex) in headers"
            :key="header.key"
            :style="animations.getCellStyle(index, colIndex)"
          >
            {{ row[header.key] }}
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>
```

### 3. Sử dụng AnimatedTable Component

```vue
<template>
  <AnimatedTable
    :data="tableData"
    :columns="tableColumns"
    :loading="isLoading"
    :items-per-page="10"
    :show-pagination="true"
    @row-click="handleRowClick"
  />
</template>

<script setup>
import AnimatedTable from '@/components/AnimatedTable.vue'

const tableColumns = [
  { key: 'name', label: 'Tên', sortable: true },
  { key: 'email', label: 'Email', sortable: true },
  { key: 'role', label: 'Vai trò', sortable: false },
]

const tableData = [
  { id: 1, name: 'Nguyễn Văn A', email: 'a@example.com', role: 'Admin' },
  { id: 2, name: 'Trần Thị B', email: 'b@example.com', role: 'User' },
  // ... more data
]

const handleRowClick = (row) => {
  console.log('Clicked row:', row)
}
</script>
```

## ⚙️ Configuration Options

```typescript
interface TableAnimationConfig {
  hoverScale?: number              // Scale khi hover (default: 1.02)
  hoverTransition?: string         // CSS transition (default: smooth cubic-bezier)
  rowHoverColor?: string          // Màu nền khi hover (default: indigo nhẹ)
  selectedRowColor?: string       // Màu nền khi select (default: indigo đậm hơn)
  loadingDuration?: number         // Thời gian loading (default: 600ms)
  staggerDelay?: number           // Delay giữa các rows (default: 50ms)
}
```

## 🎨 Available Animations

### Hover Effects
- **Scale animation:** Row phóng to nhẹ khi hover
- **Shadow effect:** Đổ bóng mờ khi hover
- **Color transition:** Màu nền thay đổi mượt mà

### Loading States
- **Skeleton loading:** Hiệu ứng loading với shimmer
- **Staggered appearance:** Các rows xuất hiện lần lượt
- **Pulse animation:** Hiệu ứng nhấp nháy cho trạng thái loading

### Selection
- **Row highlighting:** Màu nền đặc biệt khi select
- **Border accent:** Viền trái màu đặc trưng
- **Smooth transitions:** Chuyển đổi mượt mà giữa các trạng thái

## 🔧 Utility Classes

Các class CSS có thể sử dụng trực tiếp:

```css
.table-transition      /* Smooth transitions */
.table-row-hover       /* Hover effects cơ bản */
.table-row-hover:hover /* Scale và shadow khi hover */
.skeleton              /* Loading skeleton */
.pulse                 /* Pulse animation */
```

## 📋 Ví dụ hoàn chỉnh

Xem `AnimatedTable.vue` để có ví dụ đầy đủ về cách implement một table với toàn bộ tính năng animations.

## 🎯 Lợi ích

- ✨ **UX tốt hơn:** Hiệu ứng đẹp mắt và tương tác mượt mà
- 🔄 **Tái sử dụng:** Có thể dùng cho mọi table trong ứng dụng
- ⚡ **Performance:** Optimized với Vue 3 Composition API
- 🎨 **Customizable:** Dễ dàng tùy chỉnh màu sắc và hiệu ứng
- 📱 **Responsive:** Hoạt động tốt trên mọi thiết bị
