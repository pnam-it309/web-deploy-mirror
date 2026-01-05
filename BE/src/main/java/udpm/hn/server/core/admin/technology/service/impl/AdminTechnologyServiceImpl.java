package udpm.hn.server.core.admin.technology.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import udpm.hn.server.core.admin.technology.model.request.AdminCreateTechnologyRequest;
import udpm.hn.server.core.admin.technology.model.request.AdminUpdateTechnologyRequest;
import udpm.hn.server.core.admin.technology.model.response.AdminTechnologyResponse;
import udpm.hn.server.core.admin.technology.repository.AdminTechnologyRepository;
import udpm.hn.server.core.admin.technology.service.AdminTechnologyService;
import udpm.hn.server.entity.Technology;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminTechnologyServiceImpl implements AdminTechnologyService {

    private final AdminTechnologyRepository technologyRepository;

    @Override
    public List<AdminTechnologyResponse> getAll() {
        return technologyRepository.findAll().stream()
                .map(AdminTechnologyResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public AdminTechnologyResponse getDetail(String id) {
        return technologyRepository.findById(id).map(AdminTechnologyResponse::new).orElse(null);
    }

    @Override
    public Boolean create(AdminCreateTechnologyRequest request) {
        Technology technology = new Technology();
        technology.setName(request.getName());
        technology.setIcon(request.getIcon());
        technologyRepository.save(technology);
        return true;
    }

    @Override
    public Boolean update(String id, AdminUpdateTechnologyRequest request) {
        Optional<Technology> technologyOptional = technologyRepository.findById(id);
        if (technologyOptional.isPresent()) {
            Technology technology = technologyOptional.get();
            technology.setName(request.getName());
            technology.setIcon(request.getIcon());
            technologyRepository.save(technology);
            return true;
        }
        return false;
    }

    @Override
    public Boolean delete(String id) {
        if (technologyRepository.existsById(id)) {
            technologyRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
