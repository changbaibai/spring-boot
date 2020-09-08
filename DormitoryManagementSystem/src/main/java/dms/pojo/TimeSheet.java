package dms.pojo;



import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="tb_timeSheet")
public class TimeSheet {
		
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String checker;  //检查人
	private Date time;
	private String type;
	private String cause;
	@ManyToOne(fetch=FetchType.LAZY,
			targetEntity=Student.class
			)
	@JoinColumn(name="studentId",referencedColumnName="id")
	private Student student;
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}


	
	public String getChecker() {
		return checker;
	}
	public void setChecker(String checker) {
		this.checker = checker;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCause() {
		return cause;
	}
	public void setCause(String cause) {
		this.cause = cause;
	}
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}


}
