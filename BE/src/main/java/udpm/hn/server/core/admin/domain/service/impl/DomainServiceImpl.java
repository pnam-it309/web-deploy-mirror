package udpm.hn.server.core.admin.domain.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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

@Service
@RequiredArgsConstructor
public class DomainServiceImpl implements DomainService {

    private final DomainManageRepository domainRepository;
    private final AppManageRepository appRepository;
    private final ModelMapper modelMapper;
    private final Slugify slugify = Slugify.builder().build();

    @Override
    public List<DomainResponse> getAllDomains() {
        return domainRepository.findAll().stream()
                .filter(d -> d.getStatus() != udpm.hn.server.infrastructure.constant.EntityStatus.DELETED)
                .map(d -> modelMapper.map(d, DomainResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<DomainResponse> getDomains(Pageable pageable) {
        return domainRepository.findAll(pageable)
                .map(d -> modelMapper.map(d, DomainResponse.class));
    }

    @Override
    public DomainResponse getDomainById(String id) {
        Domain domain = domainRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Domain not found"));
        return modelMapper.map(domain, DomainResponse.class);
    }

    @Override
    public DomainResponse createDomain(DomainRequest request) {
        if (domainRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Tên lĩnh vực đã tồn tại");
        }
        Domain domain = modelMapper.map(request, Domain.class);
        domain.setSlug(slugify.slugify(request.getName()));

        return modelMapper.map(domainRepository.save(domain), DomainResponse.class);
    }

    @Override
    public DomainResponse updateDomain(String id, DomainRequest request) {
        Domain domain = domainRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Domain not found"));

        if (domainRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new IllegalArgumentException("Tên lĩnh vực đã tồn tại");
        }

        modelMapper.map(request, domain);
        domain.setSlug(slugify.slugify(request.getName()));

        return modelMapper.map(domainRepository.save(domain), DomainResponse.class);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void deleteDomain(String id) {
        try {
            Domain domain = domainRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Domain not found"));

            // Check logic kept for reference but not blocking soft delete
            if (appRepository.existsByDomainId(id)) {
                // Soft delete allows this
            }

            // Soft Delete
            domain.setStatus(udpm.hn.server.infrastructure.constant.EntityStatus.DELETED);
            domainRepository.save(domain);
            System.out.println("Soft deleted Domain: " + id);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void bulkUpdateOrder(
            java.util.List<udpm.hn.server.core.admin.domain.dto.request.DomainOrderRequest> requests) {
        for (udpm.hn.server.core.admin.domain.dto.request.DomainOrderRequest req : requests) {
            domainRepository.findById(req.getId()).ifPresent(domain -> {
                domain.setSortOrder(req.getSortOrder());
                domainRepository.save(domain);
            });
        }
    }
}
