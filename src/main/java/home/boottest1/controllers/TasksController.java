package home.boottest1.controllers;

import home.boottest1.entities.TaskStatus;
import home.boottest1.entities.Tasks;
import home.boottest1.entities.Users;
import home.boottest1.repos.TasksRepository;
import home.boottest1.repos.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
@Controller
public class TasksController {
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    TasksRepository tasksRepository;

    @RequestMapping(value = { "/tasks", "/tasks/list" }, method = RequestMethod.GET)
    public String list(Model model) {
        List<Tasks> tasks = tasksRepository.findAll(); model.addAttribute("list", tasks);
        return "/tasks/list";
    }

    @RequestMapping(value = { "/tasks/edit" , "/tasks/edit/{uid}"}, method = RequestMethod.GET)
    public String edit(ModelMap model, @PathVariable(required = false, value = "uid") Long uid,
                       @RequestParam(required = false, value = "id") Long id) {
        if (uid!=null) {id=uid;}
        System.out.println("============================= edit tasks id " + id);
        Tasks tasks=null;
        if(id != null) {
            try {
                tasks = tasksRepository.findById(id).orElse(null);
                if (tasks==null) {throw new LogicException("no entity with this id");}
                System.out.println("]============================= edit tasks" + tasks);
            } catch (LogicException e) {e.printStackTrace();tasks = new Tasks();}
        }
        else {tasks = new Tasks();}
        model.addAttribute("tasks", tasks);
        List<Users> userslist = usersRepository.findAll();
        model.addAttribute("userslist", userslist);

        List<TaskStatus> statuslist = new LinkedList<TaskStatus>(); //TaskStatus
        statuslist.add(TaskStatus.DISCUSSED); statuslist.add(TaskStatus.OPEN); statuslist.add(TaskStatus.ESTIMATING);
        statuslist.add(TaskStatus.FIXING); statuslist.add(TaskStatus.SOLVED); statuslist.add(TaskStatus.CLOSED);
        model.addAttribute("statuslist", statuslist);

        return "/tasks/edit";
    }

    @RequestMapping(value = "/tasks/save", method = RequestMethod.POST)
    public String save(ModelMap model, @ModelAttribute("tasks") Tasks tasks) throws LogicException {
        if (tasks!=null) {System.out.println("============================= save tasks " + tasks);}
        Tasks utasks=null;
        try {utasks=tasksRepository.save(tasks);} catch (Exception e) {System.out.println("tasks Pgs not saved: "+ tasks);}
        return "redirect:/tasks/users?id=" + utasks.getId(); //return "redirect:/users/list"
    }

    @RequestMapping(value = { "/tasks/users" }, method = RequestMethod.GET)
    public String editparticipant(ModelMap model, @RequestParam(value = "id") Long id) throws LogicException {
        Tasks tasks=null;
        if(id != null) {tasks = tasksRepository.findById(id).orElse(null);}
        else {throw new LogicException("tasks do not exist with id " + id);}
        List<Users> userslist = usersRepository.findAll();
        model.addAttribute("tasks", tasks);
        model.addAttribute("userslist", userslist);
        return "/tasks/users";
    }
    @RequestMapping(value = { "/tasks/users" }, method = RequestMethod.POST)
    public String editparticipant(ModelMap model, @RequestParam(value = "taskid") Long taskid,
                                  @RequestParam(required = false, value = "userid") Long userid)  throws LogicException {
        Tasks tasks = tasksRepository.findById(taskid).orElse(null);
        System.out.println("============================= save tasks user " + tasks);
          if (userid!=null) {
              Users users = usersRepository.findById(userid).orElse(null);
              tasks.setUsers(users); users.addTasks(tasks);
              System.out.println("============================= save tasks user2 " + users);
          }

            if (tasks==null) { throw new LogicException("tasks do not exist with id " + taskid);}
            tasksRepository.save(tasks);
        return "redirect:/tasks/list";
    }
    @RequestMapping(value= {"/tasks/delete"  }, method = RequestMethod.POST)
    public String deleteUser(@RequestParam Long id) throws LogicException {
/*
        Tasks tasks = tasksRepository.findById(id).orElse(null);
        System.out.println("============================= del tasks  " + tasks);
        if (tasks==null) {return "error";}

        tasks.setUsers(null); */
      //  tasksRepository.save(tasks);
        tasksRepository.deleteById(id);
        return "redirect:/tasks/list";
    }

    @ExceptionHandler
    public String handleException(Exception e) throws UnsupportedEncodingException {
        return "redirect:/tasks/list?message=" + URLEncoder.encode(e.getMessage(), "UTF-8");
    }

}
