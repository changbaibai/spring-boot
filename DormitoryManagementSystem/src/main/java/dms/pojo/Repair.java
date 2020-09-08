package dms.pojo;



import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="tb_repair")
public class Repair{
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String content;
	private String state;
//  修理申请对维修人员
  //映射多对一的关联关系
	@ManyToOne(fetch=FetchType.LAZY,
			targetEntity=Maintainer.class
			)
	@JoinColumn(name="maintainerId",referencedColumnName="id")
	private Maintainer maintainer;

//  修理申请对宿舍
  //映射多对一的关联关系
	@ManyToOne(fetch=FetchType.LAZY,
			targetEntity=Dorm.class
			)
	@JoinColumn(name="dormId",referencedColumnName="code")
	private Dorm dorm;

	public Repair() {

	}
//	public Repair(String content, Dorm dorm ) {
//		super();
//		this.content = content;
//		this.dorm = dorm;
//	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	public Maintainer getMaintainer() {
		return maintainer;
	}

	public void setMaintainer(Maintainer maintainer) {
		this.maintainer = maintainer;
	}

	public Dorm getDorm() {
		return dorm;
	}
	public void setDorm(Dorm dorm) {
		this.dorm = dorm;
	}






}


