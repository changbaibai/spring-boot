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
@Table(name="tb_student")
public class Student {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String sno ; 
	private String name ;
	private String address ;
	private String sex;
	private String phone;
	private String type;
	// 学生与宿舍是多对一的关系，这里配置的是双向关联
	@ManyToOne(fetch=FetchType.LAZY,
			targetEntity=Dorm.class
			)
	@JoinColumn(name="dormId",referencedColumnName="code")
	private Dorm dorm;
	
	@ManyToOne(fetch=FetchType.LAZY,
			targetEntity=Clazz.class
			)
	@JoinColumn(name="clazzId",referencedColumnName="clazzId")
	private Clazz clazz;
	
	@OneToMany(
			   fetch=FetchType.LAZY,
			   targetEntity=LeaveSchool.class,
			   mappedBy="student"
			)     
	private Set<LeaveSchool> LeaveSchools = new HashSet<>();
	@OneToMany(
			   fetch=FetchType.LAZY,
			   targetEntity=TimeSheet.class,
			   mappedBy="student"
			)     
	private Set<TimeSheet> timeSheets = new HashSet<>();
	public Student() {
		super();
}

	public Student(String name, String address,String sex, String sno, String phone,
			Dorm dorm,Clazz clazz) {
		super();
		this.sno = sno;
		this.name = name;
		this.address = address;
		this.sex = sex;
		this.phone = phone;
		this.type = type;
		this.dorm= dorm;
		this.clazz = clazz;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSno() {
		return sno;
	}

	public void setSno(String sno) {
		this.sno = sno;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Dorm getDorm() {
		return dorm;
	}

	public void setDorm(Dorm dorm) {
		this.dorm = dorm;
	}

	public Clazz getClazz() {
		return clazz;
	}

	public void setClazz(Clazz clazz) {
		this.clazz = clazz;
	}

	public Set<LeaveSchool> getLeaveSchools() {
		return LeaveSchools;
	}

	public void setLeaveSchools(Set<LeaveSchool> leaveSchools) {
		LeaveSchools = leaveSchools;
	}

	public Set<TimeSheet> getTimeSheets() {
		return timeSheets;
	}

	public void setTimeSheets(Set<TimeSheet> timeSheets) {
		this.timeSheets = timeSheets;
	}

	@Override
	public String toString() {
		return "Student [id=" + id + ", sno=" + sno + ", name=" + name + ", address=" + address + ", sex=" + sex
				+ ", phone=" + phone + ", type=" + type + ", dorm=" + dorm + ", clazz=" + clazz + ", LeaveSchools="
				+ LeaveSchools + ", timeSheets=" + timeSheets + "]";
	}







}

