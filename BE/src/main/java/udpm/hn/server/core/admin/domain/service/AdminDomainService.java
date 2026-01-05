package udpm.hn.server.core.admin.domain.service;

import udpm.hn.server.core.admin.domain.model.request.AdminCreateDomainRequest;
import udpm.hn.server.core.admin.domain.model.request.AdminUpdateDomainRequest;
import udpm.hn.server.core.admin.domain.model.response.AdminDomainResponse;

import java.util.List;

public interface AdminDomainService {
    List<AdminDomainResponse> getAll();

    AdminDomainResponse getDetail(String id);

    Boolean create(AdminCreateDomainRequest request);

    Boolean update(String id, AdminUpdateDomainRequest request);

    Boolean delete(String id);

    Boolean toggleStatus(String id);
}
