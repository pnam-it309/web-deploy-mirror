package udpm.hn.server.core.admin.technology.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import udpm.hn.server.core.admin.technology.dto.request.TechnologyRequest;
import udpm.hn.server.core.admin.technology.dto.response.TechnologyResponse;
import udpm.hn.server.core.admin.technology.repository.TechnologyManageRepository; // Tự tạo interface này kế thừa JpaRepository
import udpm.hn.server.core.admin.technology.service.TechnologyService;
import udpm.hn.server.entity.Technology;
import udpm.hn.server.core.admin.app.repository.AppManageRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TechnologyServiceImpl implements TechnologyService {

    private final TechnologyManageRepository technologyRepository;
    private final AppManageRepository appRepository;
    private final ModelMapper modelMapper;

    @Override
    @Cacheable(value = "technologies", key = "'all'")
    public List<TechnologyResponse> getAllTechnologies() {
        log.debug("Cache MISS: Fetching all technologies from database");
        return technologyRepository.findAll().stream()
                .filter(t -> t.getStatus() != udpm.hn.server.infrastructure.constant.EntityStatus.DELETED
                        && t.getStatus() != null)
                .sorted((a, b) -> a.getName().compareToIgnoreCase(b.getName()))
                .map(t -> modelMapper.map(t, TechnologyResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<TechnologyResponse> getTechnologies(Pageable pageable) {
        return technologyRepository.findAll(pageable)
                .map(t -> modelMapper.map(t, TechnologyResponse.class));
    }

    @Override
    @CacheEvict(value = "technologies", allEntries = true)
    public TechnologyResponse createTechnology(TechnologyRequest request) {
        log.info("Creating technology: name={}", request.getName());

        // Kiểm tra tên trùng (chỉ với records chưa bị xóa mềm)
        if (technologyRepository.existsByNameAndStatusNot(request.getName(), udpm.hn.server.infrastructure.constant.EntityStatus.DELETED)) {
            throw new IllegalArgumentException("Tên công nghệ '" + request.getName() + "' đã tồn tại");
        }

        Technology tech = modelMapper.map(request, Technology.class);
        tech.setStatus(udpm.hn.server.infrastructure.constant.EntityStatus.ACTIVE);

        // Auto generate slug if empty
        if (tech.getSlug() == null || tech.getSlug().isEmpty()) {
            tech.setSlug(udpm.hn.server.utils.SlugUtils.toSlug(tech.getName()));
        }

        TechnologyResponse response = modelMapper.map(technologyRepository.save(tech), TechnologyResponse.class);
        log.info("Technology created successfully: id={}, evicting cache", response.getId());
        return response;
    }

    @Override
    @CacheEvict(value = "technologies", allEntries = true)
    public TechnologyResponse updateTechnology(String id, TechnologyRequest request) {
        log.info("Updating technology: id={}, name={}", id, request.getName());
        Technology tech = technologyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Technology not found"));

        // Kiểm tra tên trùng (chỉ với records chưa bị xóa mềm, loại trừ chính nó)
        if (technologyRepository.existsByNameAndIdNotAndStatusNot(request.getName(), id, udpm.hn.server.infrastructure.constant.EntityStatus.DELETED)) {
            throw new IllegalArgumentException("Tên công nghệ '" + request.getName() + "' đã tồn tại");
        }

        modelMapper.map(request, tech);
        // Regenerate slug
        tech.setSlug(udpm.hn.server.utils.SlugUtils.toSlug(tech.getName()));

        TechnologyResponse response = modelMapper.map(technologyRepository.save(tech), TechnologyResponse.class);
        log.info("Technology updated successfully, evicting cache");
        return response;
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    @CacheEvict(value = "technologies", allEntries = true)
    public void deleteTechnology(String id) {
        log.info("Soft deleting technology: id={}", id);
        try {
            Technology tech = technologyRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Technology not found"));

            if (appRepository.existsByTechnologiesId(id)) {
                // Soft Delete bypasses FK constraints because the record remains in DB
            }

            // Soft Delete
            tech.setStatus(udpm.hn.server.infrastructure.constant.EntityStatus.DELETED);
            technologyRepository.save(tech);
            log.info("Technology soft deleted successfully, evicting cache");
        } catch (Exception e) {
            log.error("Failed to delete technology: id={}, error={}", id, e.getMessage(), e);
            throw e;
        }
    }
}