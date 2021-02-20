package home.boottest1.entities;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.ManyToAny;
import org.hibernate.annotations.NaturalId;

import java.util.HashSet;
import java.util.Set;

@Slf4j
//@Data
@Getter
@Setter
@RequiredArgsConstructor

@Entity
//@EqualsAndHashCode(exclude = {"id"}) //@EqualsAndHashCode(exclude = {"id", "role"})
@Table
public class Roles {
	@Id
	//@EqualsAndHashCode.Exclude
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@NaturalId
	private Role role;
/*
	public Roles(@NonNull Role role) {
		this.role = role;
	}

	public Roles(Long id) {}*/

	//@EqualsAndHashCode.Exclude
	@ManyToMany(fetch = FetchType.EAGER,  cascade = CascadeType.ALL) //mappedBy = "roles_id", , orphanRemoval = true
	@JoinColumn(name="roles_id")
	//@JsonIgnore
	private Set<Users> users = new HashSet<Users>();

	public void addUsers(Users user) {
		users.add(user); user.addRole(this);
	}
	public void addUser(Users user) {
		users.add(user);
	}
	public void removeUsers(Users user) {
		users.remove(user); user.removeRole(this);
	}
	public void removeUser(Users user) {
		users.remove(user);
	}

	@Override
	public String toString() {
		return "Roles{" +
				"id=" + id +
				", role=" + role +
				'}';
	}
}
