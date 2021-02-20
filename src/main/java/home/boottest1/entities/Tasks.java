package home.boottest1.entities;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.HashSet;


import javax.persistence.*;
//import javax.persistence.Column;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
@Slf4j
@Data
@Entity
@Table
public class Tasks  implements Comparable<Tasks> {
	@Id
	@EqualsAndHashCode.Exclude
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@EqualsAndHashCode.Exclude
	private TaskStatus taskstatus;
	private String name;
	private String text;

	@Temporal(TemporalType.DATE)
	@EqualsAndHashCode.Exclude
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date datestart;
	@Temporal(TemporalType.DATE)
	@EqualsAndHashCode.Exclude
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date datelimit;

	//@Column(name = "users_id")
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "users_id")
	private Users users;

	@Override
	public int compareTo(Tasks o) {
		return 0;
	}
//	public Long getDocuments() {return documents;}
//	public void setDocuments(Long documents) {this.documents = documents;}

	 	
}
