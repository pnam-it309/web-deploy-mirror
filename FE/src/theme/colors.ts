/**
 * Color Palette
 * Centralized color management for the entire application
 */

// Brand Colors
export const brand = {
  primary: '#224D66', // Navy blue (from themeConfig)
  primaryHover: '#1B3F53', // Darker navy for hover states
  primaryLight: '#D1E2EC', // Light blue for backgrounds
  secondary: '#F4F7F9', // Very light gray
} as const

// Semantic Colors
export const semantic = {
  success: '#52c41a',
  warning: '#faad14',
  error: '#ff4d4f',
  info: '#1890ff',
  processing: '#1890ff',
} as const

// Grayscale
export const gray = {
  50: '#fafafa',
  100: '#f5f5f5',
  200: '#e5e5e5',
  300: '#d4d4d4',
  400: '#a3a3a3',
  500: '#737373',
  600: '#525252',
  700: '#404040',
  800: '#262626',
  900: '#171717',
} as const

// CSS Custom Properties (for Tailwind compatibility)
export const cssVariables = {
  // Light theme
  light: {
    background: '0 0% 100%',
    foreground: '222.2 84% 4.9%',
    card: '0 0% 100%',
    cardForeground: '222.2 84% 4.9%',
    popover: '0 0% 100%',
    popoverForeground: '222.2 84% 4.9%',
    primary: '222.2 47.4% 11.2%',
    primaryForeground: '210 40% 98%',
    secondary: '210 40% 96.1%',
    secondaryForeground: '222.2 47.4% 11.2%',
    muted: '210 40% 96.1%',
    mutedForeground: '215.4 16.3% 46.9%',
    accent: '210 40% 96.1%',
    accentForeground: '222.2 47.4% 11.2%',
    destructive: '0 84.2% 60.2%',
    destructiveForeground: '210 40% 98%',
    border: '214.3 31.8% 91.4%',
    input: '214.3 31.8% 91.4%',
    ring: '222.2 84% 4.9%',
  },

  // Dark theme
  dark: {
    background: '222.2 84% 4.9%',
    foreground: '210 40% 98%',
    card: '222.2 84% 4.9%',
    cardForeground: '210 40% 98%',
    popover: '222.2 84% 4.9%',
    popoverForeground: '210 40% 98%',
    primary: '210 40% 98%',
    primaryForeground: '222.2 47.4% 11.2%',
    secondary: '217.2 32.6% 17.5%',
    secondaryForeground: '210 40% 98%',
    muted: '217.2 32.6% 17.5%',
    mutedForeground: '215 20.2% 65.1%',
    accent: '217.2 32.6% 17.5%',
    accentForeground: '210 40% 98%',
    destructive: '0 62.8% 30.6%',
    destructiveForeground: '210 40% 98%',
    border: '217.2 32.6% 17.5%',
    input: '217.2 32.6% 17.5%',
    ring: '212.7 26.8% 83.9%',
  },
} as const

// Theme-aware color getter
export const getThemeColors = (isDark: boolean) => ({
  background: isDark ? gray[900] : '#ffffff',
  foreground: isDark ? gray[50] : gray[900],
  border: isDark ? gray[700] : gray[200],
  muted: isDark ? gray[800] : gray[100],
})

// Type exports
export type BrandColors = typeof brand
export type SemanticColors = typeof semantic
export type GrayScale = typeof gray
