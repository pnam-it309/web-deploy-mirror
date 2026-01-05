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
        // Vàng kim (Gold)
        primary: {
          DEFAULT: '#D4AF37', // Vàng kim cổ điển
          hover: '#C5A028',   // Đậm hơn khi hover
          light: '#F3E5AB',   // Vàng nhạt (dùng cho background nhẹ)
        },
        // Đen (Dùng màu than chì đậm thay vì đen tuyền #000 sẽ sang hơn)
        dark: {
          DEFAULT: '#1A1A1A',
          light: '#2D2D2D',
        }
      }
    }
  },
  plugins: [],
}