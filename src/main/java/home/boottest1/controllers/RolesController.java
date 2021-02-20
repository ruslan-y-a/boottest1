package home.boottest1.controllers;

import home.boottest1.entities.Role;
import home.boottest1.entities.Roles;
import home.boottest1.entities.RolesDTO;
import home.boottest1.entities.UsersDTO;
import home.boottest1.repos.RolesRepository;
import home.boottest1.service.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping("/roles")
public class RolesController
{
    @Autowired
    RolesService rolesRepository;
    public void setRolesRepository(RolesService rolesRepository) {this.rolesRepository = rolesRepository;}

    @RequestMapping(value = { "/", "/list" }, method = RequestMethod.GET)
    public String rolesorder(Model model) {

        List<Roles> roles=null;
        try {roles = rolesRepository.findAll(); } catch (Exception e) {e.printStackTrace();}
        model.addAttribute("list", roles);
        return "/roles/list";
    }

    @RequestMapping(value = {"/jlist" }, method = RequestMethod.GET)
    @ResponseBody
    public List<RolesDTO> jlist() {
        List<Roles> roles= rolesRepository.findAll();  List<RolesDTO> rolesDTO= new LinkedList<>();
        roles.forEach(x -> rolesDTO.add(RolesDTO.importRoles(x)));
        return rolesDTO;
    }

    @RequestMapping(value = { "/{id}", "/list/{id}" }, method = RequestMethod.GET)
    @ResponseBody
    public RolesDTO findById(Model model, @PathVariable("id") Long id) throws Exception {
        Roles roles = rolesRepository.findById(id); //  model.addAttribute("list", users);
        RolesDTO usersDTO = RolesDTO.importRoles(roles);
        return usersDTO;
    }

    @RequestMapping(value = { "/edit" , "/edit/{uid}"}, method = RequestMethod.GET)
    public String edit(Model model, @PathVariable(required = false, value = "uid") Long uid,
                       @RequestParam(required = false, value = "id") Long id,
                       @RequestParam(required = false, value = "role") String role) {
        if (uid!=null) {id=uid;}
        System.out.println("]============================= edit id " + id);
        Roles roles=null;
        if(id != null) {roles = rolesRepository.findById(id);
            System.out.println("]============================= edit roles" + roles);
            role = roles.getRole().toString();
        }
        else {
          if (role!=null && role.isBlank()) {
              roles = rolesRepository.findRolesByRole(Role.valueOf(role));
             } else{roles = new Roles();}
         }
        model.addAttribute("roles", roles);
        return "/roles/edit";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveUser(@RequestParam(required = false, value = "id") Long id, @RequestParam(value = "role") String role,
                           ModelMap model)  {	//@ModelAttribute("roles") Roles roles, BindingResult result,
        Roles roles = new Roles(); roles.setRole(Role.USER);
        if (id!=null) {roles.setId(id);} roles.setRole(Role.valueOf(role));
        System.out.println("==============================" + roles);
        //   if (result.hasErrors()) {  return "error";}
        try {rolesRepository.save(roles);} catch (Exception e) {System.out.println("Roles not saved: "+ roles);}
        return "redirect:/roles/list";
    }
    @RequestMapping(value = "/jsave", method = RequestMethod.POST)
    @ResponseBody
    public Roles jsaveUser(@RequestParam(required = false, value = "id") Long id, @RequestParam(value = "role") String role,
                           ModelMap model)  {	//@ModelAttribute("roles") Roles roles, BindingResult result,
        Roles roles = new Roles(); roles.setRole(Role.USER);
        if (id!=null) {roles.setId(id);} roles.setRole(Role.valueOf(role));
        System.out.println("==============================" + roles);
        //   if (result.hasErrors()) {  return "error";}
        try {roles=rolesRepository.save(roles);} catch (Exception e) {System.out.println("Roles not saved: "+ roles);}
        return roles;
    }
    @RequestMapping(value= {"/delete" , "/delete/{uid}" }, method = RequestMethod.POST)
    public String deleteUser(@RequestParam(required = false) Long id, @PathVariable(required = false) Long uid) throws LogicException {
        if (uid!=null) {id = uid;}
        if (id==null) {return "error";}
        rolesRepository.deleteById(id);
        return "redirect:/roles/list";
    }
}
