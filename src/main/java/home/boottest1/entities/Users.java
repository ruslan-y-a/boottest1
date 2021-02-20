package home.boottest1.entities;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import javax.persistence.*;
import javax.validation.constraints.Size;

@Slf4j
//@Data
@Getter
@Setter
//@RequiredArgsConstructor

@Entity
@Table
public class Users { 
	
   @Id
   @EqualsAndHashCode.Exclude
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

///////////////////////////////////////////////////////
    @Size(min = 3, max = 20)
    @NonNull
	private String login;
    @NonNull
	private String password;
    @NonNull
	private String email;
////////////////////////////////////////////////////////
@EqualsAndHashCode.Exclude
@ManyToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER) //@OneToMany//, orphanRemoval = true
@JoinColumn(name="users_id")
//@JsonIgnore
	private Set<Roles> roles= new HashSet<Roles>();

    public void clearRoles() { roles.forEach(x -> removeRoles(x));}
    public void addRoles(Roles role) {
        roles.add(role); role.addUser(this);
    }
    public void addRole(Roles role) {
        roles.add(role);
    }
    public void removeRoles(Roles role) {
        roles.remove(role); role.removeUser(this);
    }
    public void removeRole(Roles role) {
        roles.remove(role);
    }
    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "users", cascade = CascadeType.ALL) //, orphanRemoval = true
    private Set<Tasks> tasksset = new HashSet<Tasks>();

    public void addTasks(Tasks tasks) {
        tasksset.add(tasks); tasks.setUsers(this);
    }
    public void removeTasks(Tasks tasks) {
        tasksset.remove(tasks); tasks.setUsers(null);
    }
////////////////////////////////////////////////////////	
    public Users() {}
/////////////////////////////////////////////////////////
    public Users(String login, String password, String email) {
     	this.login = login;
    	this.password = password;
    	this.email = email;
    }
/////////////////////////////////////////////////////////	

    public String[] getRoleString() {
        String[] arr = new String[roles.size()];
        int i=-1;
        for(Roles ur: roles) {  //int i=0;i<roles.size();i++
            arr[++i]=ur.getRole().toString();
        }
        //return roles.stream().map(x -> x.getRoles()).map(x -> x.getRole().toString()).toArray(new String[roles.size()]);
        return arr;
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
    /*//////////////////////////////////////////////////////////////////////////*/
  /*//////////////////////////////////////////////////////////////////////////*/


	
}
