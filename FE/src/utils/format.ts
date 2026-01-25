import { format } from 'date-fns'

/**
 * Format date to string: dd/MM/yyyy HH:mm
 */
export const formatDateTime = (date: any): string => {
  if (!date) return ''
  try {
    return format(new Date(date), 'dd/MM/yyyy HH:mm')
  } catch (error) {
    console.error('Invalid date for formatDateTime:', date)
    return ''
  }
}

/**
 * Format date to string (custom name usage): dd/MM/yyyy HH:mm
 * "NVV" likely refers to a specific business context, but we default to standard datetime.
 */
export const formatDateNVV = (date: any): string => {
  if (!date) return ''
  try {
    return format(new Date(date), 'dd/MM/yyyy HH:mm')
  } catch (error) {
    console.error('Invalid date for formatDateNVV:', date)
    return ''
  }
}

/**
 * Format number to VND currency
 */
export const formatCurrency = (value: number | undefined | null): string => {
  if (value === undefined || value === null) return '0 ₫'
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value)
}
