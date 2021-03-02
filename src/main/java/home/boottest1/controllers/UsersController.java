package home.boottest1.controllers;

import home.boottest1.entities.Role;
import home.boottest1.entities.Roles;
import home.boottest1.entities.Users;
import home.boottest1.entities.UsersDTO;
import home.boottest1.repos.RolesRepository;
import home.boottest1.service.RolesService;
import home.boottest1.service.UsersService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import home.boottest1.repos.UsersRepository;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
@Controller
public class UsersController {
    @Autowired
    UsersService repo;
    @Autowired
    RolesService rolerepo;
    @Autowired public BCryptPasswordEncoder passwordEncoder;

    public void setRolesRepository(RolesService rolesRepository) {this.rolerepo = rolesRepository;}
    public void setUsersRepository(UsersService usersRepository) {this.repo = usersRepository;}

    @RequestMapping(value = { "/users", "/users/list" }, method = RequestMethod.GET)
    public String userorder(Model model) {

        List<Users> users= repo.findAll(); model.addAttribute("list", users);
        return "users/list";
    }

    @RequestMapping(value = {"/users/jlist" }, method = RequestMethod.GET)
    @ResponseBody
    public List<UsersDTO> jlist() throws LogicException {
        List<Users> users= repo.findAll(); List<UsersDTO> jusers= new LinkedList<>();
        users.forEach(x -> jusers.add(UsersDTO.importUser(x)));
        return jusers;
    }

    @RequestMapping(value = "/users/list/{id}", method = RequestMethod.GET)
    @ResponseBody
    public UsersDTO findById(Model model, @PathVariable("id") Long id) throws Exception {
        Users users = repo.findById(id); //  model.addAttribute("list", users);
        UsersDTO usersDTO = UsersDTO.importUser(users);
        return usersDTO;
    }

    @RequestMapping(value = { "/users/edit", "/users/edit/{uid}" }, method = RequestMethod.GET)
    public String edit(ModelMap model, @PathVariable(required = false, value = "uid") Long uid,
                       @RequestParam(required = false, value = "id") Long id) {
        if (uid!=null) {id=uid;}
        System.out.println("============================= edit users id " + id);
        Users user=null;
        if(id != null) {
            try {
                user = repo.findById(id);
                if (user==null) {throw new LogicException("no entity with this id");}
                System.out.println("]============================= edit user" + user);
            } catch (LogicException e) {e.printStackTrace();user = new Users();}
        }
        else {user = new Users();}
        model.addAttribute("users", user);
        return "/users/edit";
    }

    @RequestMapping(value = "/users/save", method = RequestMethod.POST)
    public String saveUser(ModelMap model, @ModelAttribute("users") Users users) throws LogicException {

        if (users!=null) {System.out.println("============================= save users " + users);}
        users=encodeUserPass(users);
        Users usersaved=repo.save(users);
        return "redirect:/users/role?id=" + usersaved.getId(); //return "redirect:/users/list"
    }

    @RequestMapping(value = "/users/jmsave", method = RequestMethod.POST)
    @ResponseBody
    public Users jsaveUser(ModelMap model, @RequestParam(required = false, value = "id") Long id,
                           @ModelAttribute("users") Users users,
             @RequestParam(required = false, value = "login") String login,  @RequestParam(required = false, value = "password") String password,
             @RequestParam(required = false, value = "email") String email, @RequestParam(required = false, value = "roleid") Long roleid) throws LogicException {

        if (users==null) {users = new Users(); users.setLogin(login); users.setEmail(email); users=encodeUserPass(users); users.setPassword(password);}
        if (roleid!=null) {
        Roles roles = rolerepo.findById(roleid);
        users.addRole(roles);
        }
        System.out.println("============================= jmsave users " );

        users=encodeUserPass(users);
        Users usersaved=repo.save(users);
        model.addAttribute("users", usersaved);
        return usersaved; //return "redirect:/users/list"
    }
    @RequestMapping(value = "/users/jsave", method = RequestMethod.POST)
    @ResponseBody
    public Users jmsaveUser(ModelMap model, @ModelAttribute("users") Users users) throws LogicException {
        users=encodeUserPass(users);
        Users usersaved=repo.save(users);
        model.addAttribute("users", usersaved);
        return usersaved; //return "redirect:/users/list"
    }

    @RequestMapping(value = { "/users/role" }, method = RequestMethod.GET)
    public String editUsersRole(ModelMap model, @RequestParam(value = "id") Long id) throws LogicException {
        Users user=null; List<Roles> roles = rolerepo.findAll();
        if(id != null) {user = repo.findById(id);}
        else {throw new LogicException("User do not exist with id " + id);}
        model.addAttribute("users", user);
        model.addAttribute("roles", roles);
        System.out.print("============================LOADED ROLES ");
        roles.forEach(x -> System.out.print(x + " "));
        return "/users/role";
    }

    @RequestMapping(value = "/users/role", method = RequestMethod.POST)
    public String saveUserRole(ModelMap model, @RequestParam(required = false, value = "sroles") List<String> sroles,
         @RequestParam(value = "usersid") String usersid, @RequestParam(required = false, value = "newrole") String newrole
    ) throws LogicException { //, @ModelAttribute Users user
        System.out.println("============================= /users/role POST " );
        Users users= repo.findById(Long.valueOf(usersid));
        if (sroles!=null && sroles.size()>0) {
            users.clearRoles();
            for(String x: sroles) {
                Roles newroles = rolerepo.findById(Long.valueOf(x));
                /*
                if (newroles==null) {
                    newroles = new Roles(); newroles.setRole(  Role.valueOf(x));
                    Roles nrole=rolerepo.save(newroles);
                    System.out.println("=============================  Roles newroles ID: "  + nrole.getId() + " " + x);
                } */
               if (newroles!=null) {
                  // newroles.addUsers(users);
                   users.addRoles(newroles);
                   System.out.println("=============================  UserRole ur: " + newroles);
               }
            }
        }
        if (newrole!=null && !newrole.isBlank()){
            System.out.println("=============================  newrole : " + newrole);
            Roles newroles =  new Roles();
            newroles.setRole(Role.valueOf(newrole));  newroles.addUsers(users);
            newroles = rolerepo.save(newroles);
            users.addRoles(newroles);
        }
        repo.save(users);
        System.out.println("============================= save users " + users + "\n roles: ");
       if (sroles!=null) {sroles.forEach(x -> System.out.print(x + " "));}

        return "redirect:/users/list";
    }

    @RequestMapping(value= {"/users/delete"  }, method = RequestMethod.POST)
    public String deleteUser(@RequestParam(required = false) Long id) throws LogicException {

        if (id==null) {return "error";}
       Users user = repo.findById(id);
        if (user==null) {return "error";}
        user.clearRoles();
        repo.deleteById(id);
        return "redirect:/users/list";
    }

    private Users encodeUserPass(Users user) {
        if (user==null) {return null;}
        if (user.getPassword()==null || user.getPassword().isBlank()) {return user;}
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return user;
    }

}
