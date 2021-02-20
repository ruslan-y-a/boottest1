package home.boottest1.service;

import home.boottest1.entities.Role;
import home.boottest1.entities.Roles;
import home.boottest1.repos.RolesRepository;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public interface RolesService  {
	public void setRepo(RolesRepository repo);
		public List<Roles> findAll();
		public Roles findById(Long id);
		public Roles save(Roles roles);
		public void deleteById(Long id);
	    public Roles findRolesByRole(Role role);

}