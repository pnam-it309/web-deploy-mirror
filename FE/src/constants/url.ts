import { ROLES } from './roles'
import { ROUTES_CONSTANTS } from './path'

// DOMAIN

const getBackendUrl = () => {
  const hostname = window.location.hostname
  if (hostname.includes('vercel.app') || hostname.includes('onrender.com')) {
    return 'https://web-deploy-mirror.onrender.com'
  }
  return 'http://localhost:9999'
}

export const DOMAIN_BACKEND = getBackendUrl()

// Role-based identifiers
export const SCREEN_ROLE_ADMIN = ROLES.ADMIN
export const SCREEN_ROLE_CUSTOMER = ROLES.CUSTOMER

// // ✅ URL login Google cho ADMIN
// export const URL_OAUTH2_GOOGLE_ADMIN = (): string => {
//   const redirectUri = encodeURIComponent(
//     `${window.location.origin}${ROUTES_CONSTANTS.SELECTION.path}`
//   )
//   return `${getBackendUrl()}/oauth2/authorization/google?state=${SCREEN_ROLE_ADMIN}&redirect_uri=${redirectUri}`
// }

// // ✅ URL login Google cho CUSTOMER
// export const URL_OAUTH2_GOOGLE_CUSTOMER = (): string => {
//   const redirectUri = encodeURIComponent(
//     `${window.location.origin}${ROUTES_CONSTANTS.SELECTION.path}`
//   )
//   return `${getBackendUrl()}/oauth2/authorization/google?state=${SCREEN_ROLE_CUSTOMER}&redirect_uri=${redirectUri}`
// }

// Use proxy in development, direct URL in production
// Khi dùng axios instance đã có baseURL='/api/v1', ta không cần prefix '/api/v1' ở đây nữa
export const API_URL = import.meta.env.VITE_API_BASE_URL || ''

// AUTH API
export const PREFIX_API_AUTH = `/auth` as string

// ADMIN API
export const PREFIX_API_ADMIN = `/admin` as string
export const API_ADMIN_DOMAIN = `${PREFIX_API_ADMIN}/domains` as string
export const API_ADMIN_APP = `${PREFIX_API_ADMIN}/apps` as string
export const API_ADMIN_FEATURE = `${PREFIX_API_ADMIN}/features` as string
export const API_ADMIN_TECHNOLOGY = `${PREFIX_API_ADMIN}/technologies` as string
export const API_ADMIN_DASHBOARD = `${PREFIX_API_ADMIN}/dashboard` as string

// CUSTOMER API
export const PREFIX_API_CUSTOMER = `/customer` as string
export const API_CUSTOMER_DOMAIN = `${PREFIX_API_CUSTOMER}/domains` as string
export const API_CUSTOMER_APP = `${PREFIX_API_CUSTOMER}/apps` as string
export const API_CUSTOMER_TECHNOLOGY = `${PREFIX_API_CUSTOMER}/technologies` as string
export const API_CUSTOMER_DASHBOARD = `${PREFIX_API_CUSTOMER}/dashboard` as string
