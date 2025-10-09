import { API_ADMIN_CATEGORY} from '@/constants/url'
import request from '@/services/request'
import {
  DefaultResponse,
  PaginationParams,
  PaginationResponse,
  ResponseList
} from '@/types/api.common'
import { AxiosResponse } from 'axios'


export type CategoryResponse = ResponseList & {



    
id: string;
     slug :string;
     name :string;
     description:string;
     parentId:string;
children:Array<CategoryResponse>;
     createdAt:string;
     updatedAt:string;
 
}
export const getAllCategory= async ( ) => {
  const res = (await request({
    url: `${API_ADMIN_CATEGORY}/get-all-categories`,
    method: 'GET',
  })) as AxiosResponse<DefaultResponse<PaginationResponse<Array<CategoryResponse>>>>
  return res.data
}