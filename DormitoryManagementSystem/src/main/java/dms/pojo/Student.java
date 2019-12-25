package dms.pojo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="tb_student")
public class Student implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name ;
	private String address ;
	private String sno ; 
	private String phone;
	// 学生与宿舍是多对一的关系，这里配置的是双向关联
	@ManyToOne(fetch=FetchType.LAZY,
			targetEntity=DormInfo.class
			)
	@JoinColumn(name="dorminfoId",referencedColumnName="code")
	private DormInfo dorminfo;
	public Student() {

	}
	public Student(String name, String address, String sno, String phone,
			DormInfo dorminfo) {
		super();
		this.name = name;
		this.address = address;
		this.sno = sno;
		this.phone = phone;
		this.dorminfo = dorminfo;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getSno() {
		return sno;
	}
	public void setSno(String sno) {
		this.sno = sno;
	}
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public DormInfo getDorminfo() {
		return dorminfo;
	}
	public void setDorminfo(DormInfo dorminfo) {
		this.dorminfo = dorminfo;
	}


}

