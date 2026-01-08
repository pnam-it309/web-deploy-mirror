package udpm.hn.server.core.customer.technology.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import udpm.hn.server.core.customer.technology.model.CustomerTechnologyResponse;
import udpm.hn.server.core.customer.technology.repository.CustomerTechnologyRepository;
import udpm.hn.server.core.customer.technology.service.CustomerTechnologyService;
import udpm.hn.server.infrastructure.constant.EntityStatus;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerTechnologyServiceImpl implements CustomerTechnologyService {

    private final CustomerTechnologyRepository technologyRepository;

    @Override
    public List<CustomerTechnologyResponse> getAllActiveInfo() {
        return technologyRepository.findAllActive(EntityStatus.ACTIVE).stream()
                .map(t -> new CustomerTechnologyResponse(t.getId(), t.getName(), t.getIcon()))
                .collect(Collectors.toList());
    }
}
