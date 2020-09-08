package dms.pojo;


import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="tb_building")
public class Building {	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int buildingId;
	private String name ;
	private String buildingType;
	private String buildings;
	// 宿舍与学生是一对多的关联
	@OneToMany(
			   fetch=FetchType.LAZY,
			   targetEntity=Dorm.class,
			   mappedBy="building"
			)     
	private Set<Dorm> dorms = new HashSet<>();
	// 宿舍与管理人员是一对多的关联

	public Building() {	
	}
	public Building(String name) {
		this.name = name;
	}
	public int getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(int buildingId) {
		this.buildingId = buildingId;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBuildingType() {
		return buildingType;
	}
	public void setBuildingType(String buildingType) {
		this.buildingType = buildingType;
	}
	public String getBuildings() {
		return buildings;
	}
	public void setBuildings(String buildings) {
		this.buildings = buildings;
	}
	public Set<Dorm> getDorms() {
		return dorms;
	}
	public void setDorms(Set<Dorm> dorms) {
		this.dorms = dorms;
	}



	
}
