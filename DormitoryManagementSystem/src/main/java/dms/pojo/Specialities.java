package dms.pojo;



import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name="tb_specialities")
public class Specialities{
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name ;
	// 专业与院系是多对一的关系，这里配置的是双向关联
	@ManyToOne(fetch=FetchType.LAZY,
			targetEntity=College.class
			)
	@JoinColumn(name="collegeId",referencedColumnName="code")
	private College college;
	// 专业与班级是一对多的关系，这里配置的是双向关联
	@OneToMany(mappedBy="specialities") 
	private Set<Clazz> clazz= new HashSet<>();
	public Specialities() {
	}
	public Specialities(String name, College college ) {
		super();
		this.name = name;
		this.college = college;
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

	public College getCollege() {
		return college;
	}
	public void setCollege(College college) {
		this.college= college;
	}
	public Set<Clazz> getClazz() {
		return clazz;
	}
	public void setClazz(Set<Clazz> clazz) {
		this.clazz = clazz;
	}

}

