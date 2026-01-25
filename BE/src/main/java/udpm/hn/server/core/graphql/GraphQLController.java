package udpm.hn.server.core.graphql;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import udpm.hn.server.entity.App;
import udpm.hn.server.entity.Domain;
import udpm.hn.server.entity.Technology;
import udpm.hn.server.repository.AppRepository;
import udpm.hn.server.repository.DomainRepository;
import udpm.hn.server.repository.TechnologyRepository;

import java.util.List;

/**
 * GraphQL API Controller
 * Simplified version using repositories only
 */
@Controller
@RequiredArgsConstructor
public class GraphQLController {

    private final AppRepository appRepository;
    private final DomainRepository domainRepository;
    private final TechnologyRepository technologyRepository;

    /**
     * Query all apps (simplified - no filtering)
     */
    @QueryMapping
    public List<App> apps(
            @Argument Integer page,
            @Argument Integer size) {
        int pageNum = page != null ? page - 1 : 0;
        int pageSize = size != null ? size : 10;
        return appRepository.findAll(PageRequest.of(pageNum, pageSize)).getContent();
    }

    /**
     * Get single app by ID
     */
    @QueryMapping
    public App app(@Argument String id) {
        return appRepository.findById(id).orElse(null);
    }

    /**
     * Get all domains
     */
    @QueryMapping
    public List<Domain> domains() {
        return domainRepository.findAll();
    }

    /**
     * Get single domain by ID
     */
    @QueryMapping
    public Domain domain(@Argument String id) {
        return domainRepository.findById(id).orElse(null);
    }

    /**
     * Get all technologies
     */
    @QueryMapping
    public List<Technology> technologies() {
        return technologyRepository.findAll();
    }

    /**
     * Get single technology by ID
     */
    @QueryMapping
    public Technology technology(@Argument String id) {
        return technologyRepository.findById(id).orElse(null);
    }
}
