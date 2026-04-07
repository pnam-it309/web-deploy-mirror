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
    port: 6789,
    host: '0.0.0.0',
    strictPort: true,
    watch: {
      usePolling: true,
    },
    proxy: {
      '/api/v1': {
        target: 'http://backend:9999',
        changeOrigin: true,
      },
      '/oauth2': {
        target: 'http://backend:9999',
        changeOrigin: true,
      },
      '/auth': {
        target: 'http://backend:9999',
        changeOrigin: true,
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
