package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.IpWhitelist;

import java.util.List;

@Repository
public interface IpWhitelistRepository extends JpaRepository<IpWhitelist, String> {

    List<IpWhitelist> findByIsActiveTrue();

    @Query("SELECT i FROM IpWhitelist i WHERE i.isActive = true AND (i.expiresAt IS NULL OR i.expiresAt > :now)")
    List<IpWhitelist> findActiveNonExpired(long now);

    boolean existsByIpAddress(String ipAddress);
}
