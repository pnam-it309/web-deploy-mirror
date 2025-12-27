package udpm.hn.server.core.customer.domain.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import udpm.hn.server.core.customer.domain.model.response.CustomerDomainResponse;
import udpm.hn.server.core.customer.domain.repository.CustomerDomainRepository;
import udpm.hn.server.core.customer.domain.service.CustomerDomainService;
import udpm.hn.server.infrastructure.constant.EntityStatus;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerDomainServiceImpl implements CustomerDomainService {

    private final CustomerDomainRepository customerDomainRepository;

    @Override
    public List<CustomerDomainResponse> getAllActiveInfo() {
        // Fetch only active, sort by sortOrder
        // Assuming repository can do this or filter here.
        // Using stream sort for simplicity or we should defined a method in repo.
        // For now, simple filter.
        return customerDomainRepository.findAll().stream()
                .filter(d -> d.getStatus() == EntityStatus.ACTIVE)
                .sorted((d1, d2) -> {
                    if (d1.getSortOrder() == null)
                        return 1;
                    if (d2.getSortOrder() == null)
                        return -1;
                    return d1.getSortOrder().compareTo(d2.getSortOrder());
                })
                .map(d -> new CustomerDomainResponse(d.getId(), d.getName(), d.getSlug(), d.getDescription(),
                        d.getIcon()))
                .collect(Collectors.toList());
    }
}
