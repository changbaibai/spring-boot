package dms.pojo;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
	private int code;
	private String name ;
	// 院系与专业是一对多的关联
	@OneToMany(
			   fetch=FetchType.LAZY,
			   targetEntity=Specialities.class,
			   mappedBy="college"
			)     
	private Set<Specialities> Specialitiess = new HashSet<>();
	public College() {	
	}
	public College(String name) {
		this.name = name;
	}

	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Set<Specialities> getSpecialitiess() {
		return Specialitiess;
	}
	public void setSpecialitiess(Set<Specialities> specialitiess) {
		Specialitiess = specialitiess;
	}


}
