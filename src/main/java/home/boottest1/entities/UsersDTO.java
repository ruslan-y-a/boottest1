package home.boottest1.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Getter
@Setter
public class UsersDTO {
    private Long id;
    private String login;
    private String password;
    private String email;
    private Set<String> roles= new HashSet<>();
    private Set<String> tasksset = new HashSet<>();

    private UsersDTO() {}

    private static Set<String> setUsersRoles(Set<Roles> uroles){
        Set<String> lroles= new HashSet<>();
        if (uroles!=null) {uroles.forEach(x -> lroles.add(x.getRole().toString()));}
        return lroles;
    }
    private static Set<String> setUsersTasks(Set<Tasks> uroles){
        Set<String> ltasksset = new HashSet<>();
        if (uroles!=null) {uroles.forEach(x -> ltasksset.add(x.getName()));}
        return ltasksset;
    }
    public static UsersDTO importUser(Users users){
        UsersDTO usersDTO= new UsersDTO();
        usersDTO.setEmail(users.getEmail()); usersDTO.setId(users.getId());
        usersDTO.setPassword(users.getPassword()); usersDTO.setLogin(users.getLogin());
        usersDTO.setRoles(setUsersRoles(users.getRoles())); usersDTO.setTasksset(setUsersTasks(users.getTasksset()));
        return usersDTO;
    }
}
