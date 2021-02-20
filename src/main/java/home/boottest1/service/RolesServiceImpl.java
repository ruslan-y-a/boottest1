package home.boottest1.service;

import home.boottest1.entities.Role;
import home.boottest1.entities.Roles;
import home.boottest1.repos.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class RolesServiceImpl implements RolesService {
	@Autowired
	RolesRepository repo;

	public void setRepo(RolesRepository repo) {
		this.repo = repo;
	}

	public List<Roles> findAll(){return repo.findAll();};
		public Roles findById(Long id){return repo.findById(id).orElse(null);};
		public Roles save(Roles roles){return repo.save(roles);};
		public void deleteById(Long id){repo.deleteById(id);};
	    public Roles findRolesByRole(Role role){return repo.findRolesByRole(role);};

}