package home.boottest1.repos;

import home.boottest1.entities.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public interface TasksRepository extends JpaRepository<Tasks, Long> {
/*
	  @Query(value = "SELECT u FROM Tasks u WHERE u.users_id = :users_id")
	  List<Tasks> findTasksByUserId(@Param("users_id") Long users_id);
*/
}
