package dms.pojo;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tb_DC")
public class DC {


	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String electric;
	private String water;
	private String hotelRates; 
	private String upkeep;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getElectric() {
		return electric;
	}

	public void setElectric(String electric) {
		this.electric = electric;
	}

	public String getWater() {
		return water;
	}

	public void setWater(String water) {
		this.water = water;
	}

	public String getHotelRates() {
		return hotelRates;
	}

	public void setHotelRates(String hotelRates) {
		this.hotelRates = hotelRates;
	}

	public String getUpkeep() {
		return upkeep;
	}

	public void setUpkeep(String upkeep) {
		this.upkeep = upkeep;
	}

	
}
