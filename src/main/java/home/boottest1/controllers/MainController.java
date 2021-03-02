package home.boottest1.controllers;

import home.boottest1.entities.Files;
import home.boottest1.entities.Role;
import home.boottest1.entities.Roles;
import home.boottest1.entities.RolesDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Controller
public class MainController {

    @RequestMapping(value = { "/" }, method = RequestMethod.GET)
    public String index(Model model) {
     //   ModelAndView modelAndView = new ModelAndView();
       // modelAndView.setViewName("index");
        return "index";
    }

    @RequestMapping(value = {"/rolelist" }, method = RequestMethod.GET)
    public String rolelist(Model model) {
        List<Role> rolelist= Arrays.asList(Role.values());
        model.addAttribute("rolelist", rolelist);
        return "rolelist";
    }

/*
    @RequestMapping(value = { "/index" })
    public String index0(Model model) {
        return "redirect:/";
    }


    @RequestMapping(value = { "/login" }, method = RequestMethod.GET)
    public String login(Model model) {
        return "login";
    }
*/

}
