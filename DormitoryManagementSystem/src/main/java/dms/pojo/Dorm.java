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
@Table(name="tb_dorm")
public class Dorm {	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int code ;
	private String name ;
	private String type;//房间可住人数类型
	private String num;//已住人数
	// 学生与宿舍是多对一的关系，这里配置的是双向关联
	@ManyToOne(fetch=FetchType.LAZY,
			targetEntity=Building.class
			)
	@JoinColumn(name="BuildingId",referencedColumnName="BuildingId")
	private Building building;
	// 宿舍与学生是一对多的关联
	@OneToMany(
			   fetch=FetchType.LAZY,
			   targetEntity=Student.class,
			   mappedBy="dorm"
			)     
	private Set<Student> students = new HashSet<>();
	// 宿舍与维修申请是一对多的关联
	@OneToMany(
			   fetch=FetchType.LAZY,
			   targetEntity=Repair.class,
			   mappedBy="dorm"
			)     
	private Set<Repair> repairs = new HashSet<>();
	@OneToMany(
			   fetch=FetchType.LAZY,
			   targetEntity=HealthRating.class,
			   mappedBy="dorm"
			)     
	private Set<HealthRating> healthRatings = new HashSet<>();
	public Dorm() {
		
	}
	public Dorm(String name) {
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public Building getBuilding() {
		return building;
	}
	public void setBuilding(Building building) {
		this.building = building;
	}
	public Set<Student> getStudents() {
		return students;
	}
	public void setStudents(Set<Student> students) {
		this.students = students;
	}
	public Set<Repair> getRepairs() {
		return repairs;
	}
	public void setRepairs(Set<Repair> repairs) {
		this.repairs = repairs;
	}
	public Set<HealthRating> getHealthRatings() {
		return healthRatings;
	}
	public void setHealthRatings(Set<HealthRating> healthRatings) {
		this.healthRatings = healthRatings;
	}

	
}
