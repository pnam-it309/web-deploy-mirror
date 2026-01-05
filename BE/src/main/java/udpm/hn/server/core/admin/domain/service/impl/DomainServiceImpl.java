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
import udpm.hn.server.infrastructure.constant.EntityProperties;
import udpm.hn.server.utils.Helper; // Giả sử bạn có Util tạo slug, nếu chưa có thì dùng thư viện hoặc tự viết

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DomainServiceImpl implements DomainService {

    private final DomainManageRepository domainRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<DomainResponse> getAllDomains() {
        return domainRepository.findAll().stream()
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
        // Tạo slug đơn giản (bạn có thể thay bằng thư viện slugify)
        domain.setSlug(request.getName().toLowerCase().replace(" ", "-"));

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
        domain.setSlug(request.getName().toLowerCase().replace(" ", "-"));

        return modelMapper.map(domainRepository.save(domain), DomainResponse.class);
    }

    @Override
    public void deleteDomain(String id) {
        if (!domainRepository.existsById(id)) {
            throw new EntityNotFoundException("Domain not found");
        }
        // Logic check ràng buộc: Nếu Domain đang có App thì không cho xoá (Tuỳ chọn)
        domainRepository.deleteById(id);
    }
}