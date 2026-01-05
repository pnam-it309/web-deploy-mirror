package udpm.hn.server.core.admin.technology.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import udpm.hn.server.core.admin.technology.dto.request.TechnologyRequest;
import udpm.hn.server.core.admin.technology.dto.response.TechnologyResponse;
import udpm.hn.server.core.admin.technology.repository.TechnologyManageRepository; // Tự tạo interface này kế thừa JpaRepository
import udpm.hn.server.core.admin.technology.service.TechnologyService;
import udpm.hn.server.entity.Technology;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TechnologyServiceImpl implements TechnologyService {

    private final TechnologyManageRepository technologyRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<TechnologyResponse> getAllTechnologies() {
        return technologyRepository.findAll().stream()
                .map(t -> modelMapper.map(t, TechnologyResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<TechnologyResponse> getTechnologies(Pageable pageable) {
        return technologyRepository.findAll(pageable)
                .map(t -> modelMapper.map(t, TechnologyResponse.class));
    }

    @Override
    public TechnologyResponse createTechnology(TechnologyRequest request) {
        // Logic check trùng tên tương tự Domain
        Technology tech = modelMapper.map(request, Technology.class);
        return modelMapper.map(technologyRepository.save(tech), TechnologyResponse.class);
    }

    @Override
    public TechnologyResponse updateTechnology(String id, TechnologyRequest request) {
        Technology tech = technologyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Technology not found"));
        modelMapper.map(request, tech);
        return modelMapper.map(technologyRepository.save(tech), TechnologyResponse.class);
    }

    @Override
    public void deleteTechnology(String id) {
        if (!technologyRepository.existsById(id)) {
            throw new EntityNotFoundException("Technology not found");
        }
        technologyRepository.deleteById(id);
    }
}