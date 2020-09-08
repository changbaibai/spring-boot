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
@Table(name="tb_healthRating")
public class HealthRating {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String grade; 
	private String content;
	private Date time;
	// 学生与宿舍是多对一的关系，这里配置的是双向关联
	@ManyToOne(fetch=FetchType.LAZY,
			targetEntity=Dorm.class
			)
	@JoinColumn(name="dormId",referencedColumnName="code")
	private Dorm dorm;
	
	public HealthRating() {

	}
	public HealthRating(String grade, String content,Date time,
			Dorm dorm) {
		super();
		this.grade=grade;
		this.content=content;
		this.time=time;
		this.dorm= dorm;

	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}



	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public Dorm getDorm() {
		return dorm;
	}
	public void setDorm(Dorm dorm) {
		this.dorm = dorm;
	}


}

