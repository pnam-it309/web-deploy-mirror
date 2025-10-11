package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Admin;

import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Admin, String> {

    Optional<Admin> findByEmail(String email);

    @Query("""
            select s.email from Admin s 
            where s.id = :idAdmin
    """)
    String getEmailById(@Param("idAdmin") String idStaff);
}