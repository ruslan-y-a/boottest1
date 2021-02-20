package home.boottest1.repos;

import home.boottest1.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;


@Component
public interface UsersRepository extends JpaRepository<Users, Long> {
	 @Query(value = "SELECT u FROM Users u WHERE u.login = :login AND u.password = :password")
	 Users login(@Param("login") String login, @Param("password") String password);
  
	  @Query(value = "SELECT u FROM Users u WHERE u.login = :login")
	  Users findUserByLogin(@Param("login") String login);
}