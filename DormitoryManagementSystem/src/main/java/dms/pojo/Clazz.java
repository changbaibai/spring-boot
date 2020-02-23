package dms.pojo;


import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="tb_clazz")
public class Clazz {
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String name;

	// 班级与学生是一对多的关联
	@OneToMany(mappedBy="clazz")     
	private Set<Student> students = new HashSet<>();
	// 班级与专业是多对一的关联
	@ManyToOne
	private Specialities specialities;
	public Clazz() {
		
	}
	public Clazz(String name) {
		this.name = name;
	}
	public int getCode() {
		return id;
	}
	public void setCode(int code) {
		this.id = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Set<Student> getStudents() {
		return students;
	}
	public void setStudents(Set<Student> students) {
		this.students = students;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Specialities getSpecialities() {
		return specialities;
	}
	public void setSpecialities(Specialities specialities) {
		this.specialities = specialities;
	}
	
}
