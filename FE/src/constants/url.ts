import { ROLES } from './roles'

export const { VITE_BASE_URL_SERVER } = import.meta.env || {}
export const { VITE_BASE_URL_CLIENT } = import.meta.env || {}

// DOMAIN
const isLocal = window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1'
export const DOMAIN_BACKEND = isLocal ? (VITE_BASE_URL_SERVER || 'http://localhost:9999') : (VITE_BASE_URL_SERVER || 'https://web-deploy-mirror.onrender.com')
export const DOMAIN_FRONTEND = `${VITE_BASE_URL_CLIENT}` as string
export const URL_FRONTEND = `${DOMAIN_FRONTEND}/redirect`

// Role-based identifiers
export const SCREEN_ROLE_ADMIN = ROLES.ADMIN
export const SCREEN_ROLE_CUSTOMER = ROLES.CUSTOMER

// ✅ URL login Google cho ADMIN
export const URL_OAUTH2_GOOGLE_ADMIN = (): string => {
  const redirectUri = encodeURIComponent(`${window.location.origin}/selection`)
  return `${DOMAIN_BACKEND}/oauth2/authorization/google?state=${SCREEN_ROLE_ADMIN}&redirect_uri=${redirectUri}`
}

// ✅ URL login Google cho CUSTOMER
export const URL_OAUTH2_GOOGLE_CUSTOMER = (): string => {
  const redirectUri = encodeURIComponent(`${window.location.origin}/selection`)
  return `${DOMAIN_BACKEND}/oauth2/authorization/google?state=${SCREEN_ROLE_CUSTOMER}&redirect_uri=${redirectUri}`
}

// Use proxy in development, direct URL in production
// Use proxy in development, direct URL in production
// Khi dùng axios instance đã có baseURL='/api/v1', ta không cần prefix '/api/v1' ở đây nữa
export const API_URL = import.meta.env.VITE_API_BASE_URL || '' 

// AUTH API
export const PREFIX_API_AUTH = `/auth` as string

// ADMIN API
export const PREFIX_API_ADMIN = `/admin` as string

// Product Management
export const API_ADMIN_PRODUCT = `${PREFIX_API_ADMIN}/products` as string
export const API_ADMIN_PRODUCT_UPLOAD = `${API_ADMIN_PRODUCT}/import_data` as string
export const API_ADMIN_PRODUCT_STATS = `${API_ADMIN_PRODUCT}/status` as string
export const API_ADMIN_CATEGORY = `${PREFIX_API_ADMIN}/categories` as string
export const API_ADMIN_BRAND = `${PREFIX_API_ADMIN}/brand` as string
// Order Management
export const API_ADMIN_ORDER = `${PREFIX_API_ADMIN}/orders` as string
export const API_ADMIN_ORDER_STATS = `${API_ADMIN_ORDER}/stats` as string

// Customer Management
export const API_ADMIN_CUSTOMER = `${PREFIX_API_ADMIN}/customers` as string

// Staff Management
export const PREFIX_API_STAFF = `${PREFIX_API_ADMIN}/staff` as string
export const API_ADMIN_STAFF_INVITE = `${PREFIX_API_STAFF}/invite` as string

// Reports & Analytics
export const API_ADMIN_REPORT = `${PREFIX_API_ADMIN}/reports` as string
export const API_ADMIN_ANALYTICS = `${PREFIX_API_ADMIN}/analytics` as string

// CUSTOMER API
export const PREFIX_API_CUSTOMER = '/api/v1/customer' as string
export const API_CUSTOMER_PROFILE = `${PREFIX_API_CUSTOMER}/profile` as string
export const API_CUSTOMER_ORDERS = `${PREFIX_API_CUSTOMER}/order` as string
export const API_CUSTOMER_CART = `${PREFIX_API_CUSTOMER}/cart` as string
export const API_CUSTOMER_ADDRESS = `${PREFIX_API_CUSTOMER}/addresses` as string
export const API_CUSTOMER_REGISTER = `${PREFIX_API_CUSTOMER}/register` as string
export const API_CUSTOMER_PRODUCTS = `${PREFIX_API_CUSTOMER}/view_products` as string
export const API_CUSTOMER_WISHLIST = `${PREFIX_API_CUSTOMER}/wishlist` as string

// PUBLIC API
export const PREFIX_API_PUBLIC = '/api/v1/public' as string
export const API_PUBLIC_PRODUCTS = `${PREFIX_API_PUBLIC}/view_products` as string
export const API_PUBLIC_CATEGORIES = `${PREFIX_API_PUBLIC}/categories` as string
