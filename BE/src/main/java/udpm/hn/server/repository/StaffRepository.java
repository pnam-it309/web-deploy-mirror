package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import udpm.hn.server.entity.Staff;

import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, String> {

    Optional<Staff> findByEmail(String email);

    @Query("""
            select s.email from Staff s 
            where s.id = :idStaff
    """)
    String getEmailById(@Param("idStaff") String idStaff);
}