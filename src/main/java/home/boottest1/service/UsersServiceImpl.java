package home.boottest1.service;

import home.boottest1.entities.Users;
import home.boottest1.repos.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class UsersServiceImpl implements UsersService {
	@Autowired
	UsersRepository repo;

	public void setRepo(UsersRepository repo) {
		this.repo = repo;
	}

	public List<Users> findAll(){return repo.findAll();};
	public Users findById(Long id){return repo.findById(id).orElse(null);};
	public Users save(Users roles){return repo.save(roles);};
	public void deleteById(Long id){repo.deleteById(id);}

}