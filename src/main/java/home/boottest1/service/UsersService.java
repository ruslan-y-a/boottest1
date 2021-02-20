package home.boottest1.service;

import home.boottest1.entities.Role;
import home.boottest1.entities.Roles;
import home.boottest1.entities.Users;
import home.boottest1.repos.UsersRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public interface UsersService  {
	public void setRepo(UsersRepository repo);
	public List<Users> findAll();
	public Users findById(Long id);
	public Users save(Users roles);
	public void deleteById(Long id);

}