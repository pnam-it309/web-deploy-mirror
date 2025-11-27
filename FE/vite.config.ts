import path from 'path'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vite.config.js.org/
export default defineConfig({
  define: {
    global: 'globalThis',
  },
  plugins: [vue()],
  optimizeDeps: {
    include: ['v-calendar']
  },
  server: {
    port: 6789, // Port của Frontend (Vue)
    strictPort: false,

    // Cấu hình Proxy Server (ĐÃ SỬA LỖI OAUTH2)
    proxy: {
      // 1. Làn đường cho API dữ liệu (giữ nguyên)
      '/api/v1': {
        target: 'http://localhost:9999', // Port Backend (Spring)
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api\/v1/, ''),
      },

      // 2. Làn đường MỚI cho Login Google
      // (Backend SecurityConfig có .requestMatchers("/oauth2/**"))
      '/oauth2': {
        target: 'http://localhost:9999',
        changeOrigin: true,
        // KHÔNG CÓ rewrite, vì chúng ta muốn gửi /oauth2/... đến Backend
      },

      // 3. Làn đường MỚI cho các API Auth khác (nếu có)
      // (Backend SecurityConfig có .requestMatchers("/auth/**"))
      '/auth': {
        target: 'http://localhost:9999',
        changeOrigin: true,
        // KHÔNG CÓ rewrite
      }
    }
  },
  css: {
    preprocessorOptions: {
      scss: {
        api: 'modern-compiler'
      }
    }
  },
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src')
    }
  }
})
