package udpm.hn.server.core.customer.technology.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import udpm.hn.server.core.customer.technology.service.CustomerTechnologyService;
import udpm.hn.server.entity.Technology;
import udpm.hn.server.repository.TechnologyRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerTechnologyServiceImpl implements CustomerTechnologyService {

    private final TechnologyRepository technologyRepository;

    @Override
    public List<Technology> getAllTechnologies() {
        return technologyRepository.findAll();
    }
}
