package udpm.hn.server.core.admin.technology.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import udpm.hn.server.core.admin.technology.dto.request.TechnologyRequest;
import udpm.hn.server.core.admin.technology.dto.response.TechnologyResponse;

import java.util.List;

public interface TechnologyService {

    // Lấy tất cả (dùng cho việc select nhiều công nghệ khi tạo App)
    List<TechnologyResponse> getAllTechnologies();

    // Lấy danh sách có phân trang (dùng cho bảng quản lý Technology)
    Page<TechnologyResponse> getTechnologies(Pageable pageable);

    // Tạo mới
    TechnologyResponse createTechnology(TechnologyRequest request);

    // Cập nhật
    TechnologyResponse updateTechnology(String id, TechnologyRequest request);

    // Xoá
    void deleteTechnology(String id);
}