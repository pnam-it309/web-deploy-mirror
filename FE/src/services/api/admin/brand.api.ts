import { API_ADMIN_BRAND } from '@/constants/url'
import request from '@/services/request'
import {
  DefaultResponse,
  PaginationParams,
  PaginationResponse,
  ResponseList
} from '@/types/api.common'
import { AxiosResponse } from 'axios'


export type BrandResponse = ResponseList & {

id: string;
     code :string;
     name :string;
     description:string;
     logoUrl:string;
    status:string;
     createdAt:string;
     updatedAt:string;
     createdBy:string;
     updatedBy:string;
}
export const getAllBrand = async ( ) => {
  const res = (await request({
    url: `${API_ADMIN_BRAND}/get-all-brands`,
    method: 'GET',
  })) as AxiosResponse<DefaultResponse<PaginationResponse<Array<BrandResponse>>>>
  return res.data
}