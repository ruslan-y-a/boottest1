package home.boottest1.repos;

import home.boottest1.entities.Files;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;


@Component
public interface FilesRepository extends JpaRepository<Files, Long> {
  
	  @Query(value = "SELECT u FROM Files u WHERE u.url = :url")
	  Files findRolesByUrl(@Param("url") String url);
}