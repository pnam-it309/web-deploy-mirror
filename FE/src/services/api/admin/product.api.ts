import { API_ADMIN_PRODUCT } from '@/constants/url'
import request from '@/services/request'
import {
  DefaultResponse,
  PaginationParams,
  PaginationResponse,
  ResponseList
} from '@/types/api.common'
import { AxiosResponse } from 'axios'

export interface ParamsGetProduct extends PaginationParams {
  skuOrName?: string | ''
  brandId: string | null
  categoryId: string | null
  status: string | null
}

export type ProductsResponse = ResponseList & {
  id: string
  sku: string
  name: string
  shortDescription: string;
  price: number;
  stockQuantity: number;
  brandName: string;
  categoryName: string;
  unit: string;
  status: string;
  productDetailId: string;
  url: string;
  image: string;
}


export const getAllProducts = async (params: ParamsGetProduct) => {
  const res = (await request({
    url: `${API_ADMIN_PRODUCT}/get-all-products`,
    method: 'GET',
    params: params
  })) as AxiosResponse<DefaultResponse<PaginationResponse<Array<ProductsResponse>>>>
  return res.data
}

