package home.boottest1.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Getter
@Setter
public class RolesDTO {
    private Long id;
    private Role role;
    private Set<String> users = new HashSet<>();
    private RolesDTO() {}

    private static Set<String> setRolesUsers(Set<Users> uusers){
        Set<String> lusers= new HashSet<>();
        if (uusers!=null) {uusers.forEach(x -> lusers.add(x.getLogin()));}
        return lusers;
    }
    public static RolesDTO importRoles(Roles roles){
        RolesDTO rolesDTO= new RolesDTO();
        rolesDTO.setId(roles.getId()); rolesDTO.setRole(roles.getRole());
        rolesDTO.setUsers(setRolesUsers(roles.getUsers()));
        return rolesDTO;
    }
}
