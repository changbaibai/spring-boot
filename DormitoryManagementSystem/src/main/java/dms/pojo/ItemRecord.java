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
//物品出入信息表
@Entity
@Table(name="tb_itemRecord")
public class ItemRecord {
		
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String goods;
	private Date leavingTime;
	private Date returnTime;
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


	public String getGoods() {
		return goods;
	}
	public void setGoods(String goods) {
		this.goods = goods;
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
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}

	}



