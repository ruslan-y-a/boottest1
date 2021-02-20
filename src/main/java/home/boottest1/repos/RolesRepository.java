package home.boottest1.repos;

import home.boottest1.entities.Role;
import home.boottest1.entities.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;


@Component
public interface RolesRepository extends JpaRepository<Roles, Long> {
  
	  @Query(value = "SELECT u FROM Roles u WHERE u.role = :role")
	  Roles findRolesByRole(@Param("role") Role role);
}