package dms.pojo;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="tb_college")
public class College {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id ;
	private String name ;
	// 院系与专业是一对多的关联
	@OneToMany(mappedBy="college")     
	private Set<Specialities> Specialities = new HashSet<>();
	public College() {
		
	}
	public College(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Set<Specialities> getSpecialities() {
		return Specialities;
	}
	public void setSpecialitiesInfo(Set<Specialities> Specialities) {
		this.Specialities = Specialities;
	}
}
