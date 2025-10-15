# Admin Dashboard - Product Template Download

Tính năng download template sản phẩm đã được tích hợp vào trang admin dashboard với đầy đủ các trường từ Product và ProductDetail entities.

## 📍 Vị trí

Nút "Download Product Template" được đặt ở góc phải phía trên của trang admin dashboard (`/admin`), ngay cạnh tiêu đề "Tổng quan".

## 📋 Nội dung Template

Template CSV bao gồm đầy đủ các trường từ cả 2 entities:

### **Product Fields:**
- `sku` - Mã sản phẩm (unique)
- `name` - Tên sản phẩm (bắt buộc)
- `slug` - Slug URL (unique, chỉ chữ cái, số và dấu gạch ngang)
- `shortDescription` - Mô tả ngắn
- `price` - Giá sản phẩm (số dương)
- `stockQuantity` - Số lượng tồn kho (số nguyên không âm)
- `brand` - Tên thương hiệu
- `category` - Tên danh mục
- `status` - Trạng thái (ACTIVE/INACTIVE)
- `unit` - Đơn vị (CAI, HOP, THUNG, etc.)

### **ProductDetail Fields:**
- `longDescription` - Mô tả dài chi tiết
- `specification` - Thông số kỹ thuật (JSON format)
- `packaging` - Đóng gói (ví dụ: "Hộp carton 10x15x20cm")
- `weight` - Trọng lượng (kg, có thể có số thập phân)
- `dimensions` - Kích thước (ví dụ: "10x15x20")
- `images` - Danh sách hình ảnh (ngăn cách bởi dấu phẩy)

## 🎯 Cách sử dụng

### **1. Truy cập Admin Dashboard:**
```
http://localhost:5173/admin
```

### **2. Tải Template:**
- Click nút **"Download Product Template"** ở góc phải màn hình
- File `product_template.csv` sẽ được tải về máy

### **3. Điền thông tin sản phẩm:**
```csv
sku,name,slug,shortDescription,price,stockQuantity,brand,category,status,unit,longDescription,specification,packaging,weight,dimensions,images
SP001,Sản phẩm mẫu 1,san-pham-mau-1,Mô tả ngắn sản phẩm mẫu 1,100000,50,Thương hiệu A,Danh mục A,ACTIVE,CAI,Mô tả dài chi tiết về sản phẩm mẫu 1 với đầy đủ thông tin về tính năng công dụng và hướng dẫn sử dụng.,"{""color"": ""Đỏ"", ""size"": ""Lớn"", ""material"": ""Nhựa""}",Hộp carton 10x15x20cm,1.5,10x15x20,image1.jpg,image2.jpg,image3.jpg
SP002,Sản phẩm mẫu 2,san-pham-mau-2,Mô tả ngắn sản phẩm mẫu 2,200000,30,Thương hiệu B,Danh mục B,ACTIVE,HOP,Mô tả dài chi tiết về sản phẩm mẫu 2 với đầy đủ thông tin về tính năng công dụng và hướng dẫn sử dụng.,"{""color"": ""Xanh"", ""size"": ""Trung bình"", ""material"": ""Kim loại""}",Thùng giấy 20x25x30cm,2.8,20x25x30,image4.jpg,image5.jpg
```

### **4. Upload và Import:**
- Chuẩn bị file CSV với định dạng đúng
- Upload thông qua chức năng import sản phẩm trong admin panel
- Hệ thống sẽ validate và xử lý dữ liệu

## ⚠️ Lưu ý quan trọng

### **Các trường bắt buộc:**
- `sku` - Phải unique trong hệ thống
- `name` - Không được để trống
- `slug` - Phải unique, chỉ chứa chữ cái, số và dấu gạch ngang
- `price` - Phải là số dương
- `status` - Chỉ nhận giá trị: ACTIVE, INACTIVE

### **Định dạng đặc biệt:**

#### **JSON Specification:**
```json
{
  "color": "Đỏ",
  "size": "Lớn",
  "material": "Nhựa",
  "warranty": "12 tháng"
}
```

#### **Images (nhiều hình):**
```
image1.jpg,image2.jpg,image3.jpg
```

#### **Dimensions:**
```
10x15x20 (dài x rộng x cao)
```

#### **Weight:**
```
1.5 (kg, có thể có số thập phân)
```

## 🔧 Technical Implementation

### **Component:** `AdminDashboard.vue`
- **Location:** `src/pages/admin/dashboard/AdminDashboard.vue`
- **Function:** `downloadProductTemplate()`
- **Format:** CSV với encoding UTF-8
- **Features:** Auto-download, proper escaping, sample data

### **Validation Rules:**
- SKU phải unique trong toàn bộ hệ thống
- Slug chỉ chứa chữ cái, số và dấu gạch ngang
- Price phải là số dương
- Stock quantity phải là số nguyên không âm
- Status chỉ nhận ACTIVE hoặc INACTIVE

## 📊 Sample Data

Template có kèm 2 mẫu sản phẩm với đầy đủ thông tin để làm tham khảo cách điền dữ liệu đúng định dạng.

## 🚀 Benefits

- ✅ **Tiết kiệm thời gian** - Không cần nhập tay từng sản phẩm
- ✅ **Độ chính xác cao** - Định dạng chuẩn, ít lỗi nhập liệu
- ✅ **Dễ sử dụng** - Giao diện trực quan, hướng dẫn rõ ràng
- ✅ **Linh hoạt** - Hỗ trợ đầy đủ các trường trong database
- ✅ **Tái sử dụng** - Có thể sử dụng lại template cho nhiều lần import

## 🎯 Use Cases

1. **Import sản phẩm hàng loạt** khi khởi tạo hệ thống
2. **Cập nhật thông tin** sản phẩm từ file Excel bên ngoài
3. **Sao lưu dữ liệu** sản phẩm dưới dạng file
4. **Chuẩn hóa quy trình** nhập liệu sản phẩm mới

## 🔄 Integration

Tính năng này được tích hợp sẵn vào admin dashboard và có thể được sử dụng ngay lập tức mà không cần cấu hình thêm.
