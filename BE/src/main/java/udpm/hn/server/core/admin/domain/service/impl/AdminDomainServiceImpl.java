package udpm.hn.server.core.admin.domain.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import udpm.hn.server.core.admin.domain.model.request.AdminCreateDomainRequest;
import udpm.hn.server.core.admin.domain.model.request.AdminUpdateDomainRequest;
import udpm.hn.server.core.admin.domain.model.response.AdminDomainResponse;
import udpm.hn.server.core.admin.domain.repository.AdminDomainRepository;
import udpm.hn.server.core.admin.domain.service.AdminDomainService;
import udpm.hn.server.entity.Domain;
import udpm.hn.server.infrastructure.constant.EntityStatus;
import udpm.hn.server.infrastructure.exception.RestApiException;
import udpm.hn.server.utils.SlugUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminDomainServiceImpl implements AdminDomainService {

    private final AdminDomainRepository adminDomainRepository;

    @Override
    public List<AdminDomainResponse> getAll() {
        return adminDomainRepository.findAll(Sort.by(Sort.Direction.ASC, "sortOrder"))
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AdminDomainResponse getDetail(String id) {
        return adminDomainRepository.findById(id)
                .map(this::toResponse)
                .orElse(null);
    }

    @Override
    public Boolean create(AdminCreateDomainRequest request) {
        if (adminDomainRepository.existsByName(request.getName())) {
            throw new RestApiException("Tên lĩnh vực đã tồn tại", HttpStatus.BAD_REQUEST);
        }

        String slug = StringUtils.hasText(request.getSlug())
                ? SlugUtils.toSlug(request.getSlug())
                : SlugUtils.toSlug(request.getName());

        if (adminDomainRepository.existsBySlug(slug)) {
            throw new RestApiException("Đường dẫn (slug) đã tồn tại", HttpStatus.BAD_REQUEST);
        }

        Domain domain = new Domain();
        domain.setName(request.getName());
        domain.setDescription(request.getDescription());
        domain.setIcon(request.getIcon());
        domain.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
        domain.setSlug(slug);

        if (StringUtils.hasText(request.getParentId())) {
            Optional<Domain> parent = adminDomainRepository.findById(request.getParentId());
            parent.ifPresent(domain::setParent);
        }

        domain.setStatus(EntityStatus.ACTIVE);
        adminDomainRepository.save(domain);
        return true;
    }

    @Override
    public Boolean update(String id, AdminUpdateDomainRequest request) {
        Optional<Domain> optionalDomain = adminDomainRepository.findById(id);
        if (optionalDomain.isEmpty()) {
            throw new RestApiException("Lĩnh vực không tồn tại", HttpStatus.NOT_FOUND);
        }
        Domain domain = optionalDomain.get();

        if (adminDomainRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new RestApiException("Tên lĩnh vực đã tồn tại", HttpStatus.BAD_REQUEST);
        }

        String slug = StringUtils.hasText(request.getSlug())
                ? SlugUtils.toSlug(request.getSlug())
                : SlugUtils.toSlug(request.getName());

        if (adminDomainRepository.existsBySlugAndIdNot(slug, id)) {
            throw new RestApiException("Đường dẫn (slug) đã tồn tại", HttpStatus.BAD_REQUEST);
        }

        domain.setName(request.getName());
        domain.setDescription(request.getDescription());
        domain.setIcon(request.getIcon());
        domain.setSortOrder(request.getSortOrder());
        domain.setSlug(slug);

        if (StringUtils.hasText(request.getParentId())) {
            if (!request.getParentId().equals(domain.getId())) { // Prevent self-parent
                Optional<Domain> parent = adminDomainRepository.findById(request.getParentId());
                parent.ifPresent(domain::setParent);
            }
        } else {
            domain.setParent(null);
        }

        adminDomainRepository.save(domain);
        return true;
    }

    @Override
    public Boolean delete(String id) {
        if (!adminDomainRepository.existsById(id)) {
            throw new RestApiException("Lĩnh vực không tồn tại", HttpStatus.NOT_FOUND);
        }

        Long appCount = adminDomainRepository.countAppsByDomainId(id);
        if (appCount > 0) {
            throw new RestApiException("Không thể xóa lĩnh vực đang chứa sản phẩm", HttpStatus.BAD_REQUEST);
        }

        adminDomainRepository.deleteById(id);
        return true;
    }

    @Override
    public Boolean toggleStatus(String id) {
        Optional<Domain> optionalDomain = adminDomainRepository.findById(id);
        if (optionalDomain.isPresent()) {
            Domain domain = optionalDomain.get();
            if (domain.getStatus() == EntityStatus.ACTIVE) {
                domain.setStatus(EntityStatus.INACTIVE);
            } else {
                domain.setStatus(EntityStatus.ACTIVE);
            }
            adminDomainRepository.save(domain);
            return true;
        }
        return false;
    }

    private AdminDomainResponse toResponse(Domain domain) {
        AdminDomainResponse response = new AdminDomainResponse();
        response.setId(domain.getId());
        response.setName(domain.getName());
        response.setSlug(domain.getSlug());
        response.setDescription(domain.getDescription());
        response.setIcon(domain.getIcon());
        response.setSortOrder(domain.getSortOrder());
        response.setStatus(domain.getStatus());
        if (domain.getParent() != null) {
            response.setParentId(domain.getParent().getId());
            response.setParentName(domain.getParent().getName());
        }
        response.setCreatedDate(domain.getCreatedDate());
        response.setLastModifiedDate(domain.getLastModifiedDate());
        return response;
    }
}
