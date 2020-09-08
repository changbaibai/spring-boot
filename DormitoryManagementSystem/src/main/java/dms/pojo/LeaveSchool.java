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

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name="tb_leaveSchool")
public class LeaveSchool {
		
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String destination;
	private String cause;
	private Date leavingTime;
	private Date returnTime;
	private String parentsName;
	private String parentsPhone;
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
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getCause() {
		return cause;
	}
	public void setCause(String cause) {
		this.cause = cause;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getLeavingTime() {
		return leavingTime;
	}
	public void setLeavingTime(Date leavingTime) {
		this.leavingTime = leavingTime;
	}
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getReturnTime() {
		return returnTime;
	}
	public void setReturnTime(Date returnTime) {
		this.returnTime = returnTime;
	}
	public String getParentsName() {
		return parentsName;
	}
	public void setParentsName(String parentsName) {
		this.parentsName = parentsName;
	}
	public String getParentsPhone() {
		return parentsPhone;
	}
	public void setParentsPhone(String parentsPhone) {
		this.parentsPhone = parentsPhone;
	}
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}


	

	}



