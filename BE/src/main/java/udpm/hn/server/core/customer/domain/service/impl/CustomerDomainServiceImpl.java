package udpm.hn.server.core.customer.domain.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import udpm.hn.server.core.customer.domain.model.response.CustomerDomainResponse;
import udpm.hn.server.core.customer.domain.repository.CustomerDomainRepository;
import udpm.hn.server.core.customer.domain.service.CustomerDomainService;
import udpm.hn.server.infrastructure.constant.EntityStatus;
import udpm.hn.server.infrastructure.constant.ApprovalStatus;
import udpm.hn.server.core.customer.app.repository.CustomerAppRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerDomainServiceImpl implements CustomerDomainService {

    private final CustomerDomainRepository customerDomainRepository;
    private final CustomerAppRepository customerAppRepository;

    @Override
    public List<CustomerDomainResponse> getAllActiveInfo() {
        return customerDomainRepository.findAll().stream()
                .filter(d -> d.getStatus() == EntityStatus.ACTIVE)
                .sorted((d1, d2) -> {
                    if (d1.getSortOrder() == null)
                        return 1;
                    if (d2.getSortOrder() == null)
                        return -1;
                    return d1.getSortOrder().compareTo(d2.getSortOrder());
                })
                .map(d -> {
                    long productCount = customerAppRepository.countByDomain_IdAndApprovalStatus(d.getId(), ApprovalStatus.APPROVED);
                    return new CustomerDomainResponse(d.getId(), d.getName(), d.getSlug(), d.getDescription(),
                            d.getIcon(), productCount);
                })
                .collect(Collectors.toList());
    }
}
