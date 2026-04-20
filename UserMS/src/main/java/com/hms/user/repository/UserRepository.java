package com.hms.user.repository;

import com.hms.user.dto.MonthlyRoleCountDTO;
import com.hms.user.dto.Roles;
import com.hms.user.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("""
    SELECT new com.hms.user.dto.MonthlyRoleCountDTO(
        CAST(FUNCTION('TO_CHAR', a.createdAt, 'FMMonth') AS string),
        COUNT(a)
    )
    FROM User a
    WHERE a.role = ?1
    AND EXTRACT(YEAR FROM a.createdAt) = EXTRACT(YEAR FROM CURRENT_DATE)
    GROUP BY 
        EXTRACT(MONTH FROM a.createdAt),
        CAST(FUNCTION('TO_CHAR', a.createdAt, 'FMMonth') AS string)
    ORDER BY EXTRACT(MONTH FROM a.createdAt)
""")
    List<MonthlyRoleCountDTO> countRegistrationsByRoleGroupedByMonth(Roles role);
}
