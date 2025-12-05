export interface CustomerOrderResponse {
  id: string
  orderCode: string
  createdDate: number
  totalAmount: number
  status: string
  orderStatus: string
  paymentStatus: string
  estimatedDeliveryDate: string
  itemsCount: number
}

export interface CustomerOrderItemResponse {
  id: string
  productName: string
  productImage: string | null
  quantity: number
  unitPrice: number
  totalPrice: number
}

export interface CustomerOrderDetailResponse {
  id: string
  orderCode: string
  createdDate: number
  totalAmount: number
  status: string
  orderStatus: string
  paymentStatus: string
  paymentMethod: string
  estimatedDeliveryDate: string
  actualDeliveryDate: string | null
  cancellationReason: string | null
  notes: string | null
  customerName: string
  customerPhone: string
  customerAddress: string
  items: CustomerOrderItemResponse[]
}

export interface OrderFilterParams {
  page?: number
  size?: number
  search?: string
  status?: string
}
