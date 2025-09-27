export type UnitType = 'xe' | 'kien' | 'vien' | 'cay'

export interface WarehouseInfo {
  name: string
  code: string
}

export interface PackagingInfo {
  unit: UnitType
  unitName: string
  quantityPerUnit: number
  description: string
}

export interface StockInfo {
  status: 'in_stock' | 'out_of_stock' | 'contact'
  quantityUnits?: number
  warehouse?: WarehouseInfo
}

export interface ProductItem {
  sku: string
  name: string
  shortDescription: string
  description?: string
  brand?: string
  category?: string
  thumbnail?: string
  images?: string[]
  packaging: PackagingInfo
  stock: StockInfo
  badges?: string[]
}

export interface QuoteFormInput {
  companyName: string
  contactPerson: string
  phone: string
  email: string
  sku: string
  productName: string
  quantity: number
  preferredDate?: string
  warehouse?: string
  notes?: string
}
