package udpm.hn.server.core.admin.app.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import udpm.hn.server.core.admin.app.dto.request.AppCreateRequest;
import udpm.hn.server.core.admin.app.dto.request.AppDetailUpdateRequest;
import udpm.hn.server.core.admin.app.dto.request.AppFilterRequest;
import udpm.hn.server.core.admin.app.dto.request.AppUpdateRequest;
import udpm.hn.server.core.admin.app.dto.response.AppDetailResponse;
import udpm.hn.server.core.admin.app.dto.response.AppResponse;

public interface AppService {
    Page<AppResponse> getAllApps(AppFilterRequest request, Pageable pageable);
    AppResponse getAppById(String id);
    AppResponse createApp(AppCreateRequest request);
    AppResponse updateApp(String id, AppUpdateRequest request);
    void deleteApp(String id);
    AppDetailResponse getAppDetail(String appId);
    AppDetailResponse updateAppDetail(String appId, AppDetailUpdateRequest request);
}