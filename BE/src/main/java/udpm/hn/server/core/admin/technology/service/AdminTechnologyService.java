package udpm.hn.server.core.admin.technology.service;

import udpm.hn.server.core.admin.technology.model.request.AdminCreateTechnologyRequest;
import udpm.hn.server.core.admin.technology.model.request.AdminUpdateTechnologyRequest;
import udpm.hn.server.core.admin.technology.model.response.AdminTechnologyResponse;

import java.util.List;

public interface AdminTechnologyService {
    List<AdminTechnologyResponse> getAll();

    AdminTechnologyResponse getDetail(String id);

    Boolean create(AdminCreateTechnologyRequest request);

    Boolean update(String id, AdminUpdateTechnologyRequest request);

    Boolean delete(String id);
}
