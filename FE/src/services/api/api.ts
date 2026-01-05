
import axios from 'axios';
import { localStorageAction } from '@/utils/storage';
import { ACCESS_TOKEN_STORAGE_KEY } from '@/constants/storagekey';
const apiClient = axios.create({
  baseURL: '/api/v1', 
  headers: {
    'Content-Type': 'application/json',
  },
});

// 3. TẠO "NGƯỜI VẬN CHUYỂN" INTERCEPTOR
// (Nó sẽ lấy "Thẻ VIP" (JWT) từ ĐÚNG nơi bạn đã "CẤT")
apiClient.interceptors.request.use(
  (config) => {
    
    // 3a. Lấy token từ ĐÚNG "TÚI" VÀ ĐÚNG "CHÌA KHÓA"
    // (localStorageAction.get sẽ tự động JSON.parse)
    const token = localStorageAction.get(ACCESS_TOKEN_STORAGE_KEY); 
    
    if (token && typeof token === 'string') { // Kiểm tra token có thật không
      // 3b. Đính token vào header
      config.headers.Authorization = `Bearer ${token}`;
    }
    
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 4. Xuất ra để các service khác dùng
export default apiClient;