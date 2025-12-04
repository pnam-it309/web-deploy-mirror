/** @type {import('tailwindcss').Config} */
export default {
  darkMode: 'class',
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        brand: {
          // --- LIGHT MODE COLORS ---
          cream: '#f0ead2',
          sage: '#dde5b6',
          olive: '#adc178',
          coffee: '#a98467',
          mocha: '#6c584c',
          
          // --- DARK MODE COLORS (High Contrast) ---
          // Sử dụng tông nâu rất đậm (gần như đen) để làm nền
          dark: {
            50:  '#5e504a', // Border / Separator (Sáng nhất trong dark)
            100: '#3e332e', // Card Background (Nổi lên trên nền)
            200: '#241c18', // Main Page Background (Nền chính)
            300: '#120d0b', // Sidebar / Header / Deepest (Tối nhất)
            text: '#f0ead2' // Cream sáng dùng cho text chính
          }
        }
      }
    },
  },
  plugins: [],
}