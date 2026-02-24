package udpm.hn.server.core.admin.domain.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import udpm.hn.server.core.admin.domain.dto.request.DomainRequest;
import udpm.hn.server.core.admin.domain.dto.response.DomainResponse;
import udpm.hn.server.core.admin.domain.repository.DomainManageRepository;
import udpm.hn.server.core.admin.domain.service.DomainService;
import udpm.hn.server.entity.Domain;
import udpm.hn.server.core.admin.app.repository.AppManageRepository;
import com.github.slugify.Slugify;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DomainServiceImpl implements DomainService {

    private final DomainManageRepository domainRepository;
    private final AppManageRepository appRepository;
    private final ModelMapper modelMapper;
    private final Slugify slugify = Slugify.builder().build();

    @Override
    @Cacheable(value = "domains", key = "'all'")
    public List<DomainResponse> getAllDomains() {
        log.debug("Cache MISS: Fetching all domains from database");
        return domainRepository.findAll().stream()
                .filter(d -> d.getStatus() != udpm.hn.server.infrastructure.constant.EntityStatus.DELETED
                        && d.getStatus() != null)
                .sorted((a, b) -> {
                    int orderA = a.getSortOrder() != null ? a.getSortOrder() : Integer.MAX_VALUE;
                    int orderB = b.getSortOrder() != null ? b.getSortOrder() : Integer.MAX_VALUE;
                    return Integer.compare(orderA, orderB);
                })
                .map(d -> modelMapper.map(d, DomainResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<DomainResponse> getDomains(Pageable pageable) {
        return domainRepository.findAll(pageable)
                .map(d -> modelMapper.map(d, DomainResponse.class));
    }

    @Override
    @Cacheable(value = "domains", key = "#id")
    public DomainResponse getDomainById(String id) {
        log.debug("Cache MISS: Fetching domain by id={}", id);
        Domain domain = domainRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Domain not found"));
        return modelMapper.map(domain, DomainResponse.class);
    }

    @Override
    @CacheEvict(value = "domains", allEntries = true)
    public DomainResponse createDomain(DomainRequest request) {
        log.info("Creating domain: name={}", request.getName());
        // Chỉ kiểm tra tên với các bản ghi chưa bị xóa mềm
        if (domainRepository.existsByNameAndStatusNot(request.getName(), udpm.hn.server.infrastructure.constant.EntityStatus.DELETED)) {
            throw new IllegalArgumentException("Tên lĩnh vực đã tồn tại");
        }
        Domain domain = modelMapper.map(request, Domain.class);
        domain.setSlug(slugify.slugify(request.getName()));
        domain.setStatus(udpm.hn.server.infrastructure.constant.EntityStatus.ACTIVE);

        DomainResponse response = modelMapper.map(domainRepository.save(domain), DomainResponse.class);
        log.info("Domain created successfully: id={}, evicting cache", response.getId());
        return response;
    }

    @Override
    @CacheEvict(value = "domains", allEntries = true)
    public DomainResponse updateDomain(String id, DomainRequest request) {
        log.info("Updating domain: id={}, name={}", id, request.getName());
        Domain domain = domainRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Domain not found"));

        // Chỉ kiểm tra tên với các bản ghi chưa bị xóa mềm (loại trừ chính nó)
        if (domainRepository.existsByNameAndIdNotAndStatusNot(request.getName(), id, udpm.hn.server.infrastructure.constant.EntityStatus.DELETED)) {
            throw new IllegalArgumentException("Tên lĩnh vực đã tồn tại");
        }

        modelMapper.map(request, domain);
        domain.setSlug(slugify.slugify(request.getName()));

        DomainResponse response = modelMapper.map(domainRepository.save(domain), DomainResponse.class);
        log.info("Domain updated successfully, evicting cache");
        return response;
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    @CacheEvict(value = "domains", allEntries = true)
    public void deleteDomain(String id) {
        log.info("Soft deleting domain: id={}", id);
        Domain domain = domainRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Domain not found: id={}", id);
                    return new EntityNotFoundException("Không tìm thấy lĩnh vực với id=" + id);
                });

        // Soft Delete – set status to DELETED
        domain.setStatus(udpm.hn.server.infrastructure.constant.EntityStatus.DELETED);
        domainRepository.save(domain);
        log.info("Domain soft deleted successfully: id={}, evicting cache", id);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    @CacheEvict(value = "domains", allEntries = true)
    public void bulkUpdateOrder(
            List<udpm.hn.server.core.admin.domain.dto.request.DomainOrderRequest> requests) {
        if (requests == null) {
            log.warn("Bulk update order called with null list");
            return;
        }
        log.info("Bulk updating domain order: count={}", requests.size());
        for (udpm.hn.server.core.admin.domain.dto.request.DomainOrderRequest req : requests) {
            if (req.getId() == null) {
                log.warn("Skipping domain update with null ID");
                continue;
            }
            domainRepository.findById(req.getId()).ifPresent(domain -> {
                domain.setSortOrder(req.getSortOrder());
                domainRepository.save(domain);
            });
        }
        log.info("Domain order updated successfully, evicting cache");
    }
}
