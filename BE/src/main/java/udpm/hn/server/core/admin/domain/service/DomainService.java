package udpm.hn.server.core.admin.domain.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import udpm.hn.server.core.admin.domain.dto.request.DomainRequest;
import udpm.hn.server.core.admin.domain.dto.response.DomainResponse;

import java.util.List;

public interface DomainService {

    // Lấy tất cả (dùng cho dropdown list khi tạo App)
    List<DomainResponse> getAllDomains();

    // Lấy danh sách có phân trang (dùng cho bảng quản lý Domain)
    Page<DomainResponse> getDomains(Pageable pageable);

    // Lấy chi tiết
    DomainResponse getDomainById(String id);

    // Tạo mới
    DomainResponse createDomain(DomainRequest request);

    // Cập nhật
    DomainResponse updateDomain(String id, DomainRequest request);

    // Xoá
    void deleteDomain(String id);
}