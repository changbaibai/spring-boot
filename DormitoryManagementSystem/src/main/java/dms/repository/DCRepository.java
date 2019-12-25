package dms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dms.pojo.DC;



public interface DCRepository extends JpaRepository<DC, Integer> {
	
	
	DC findById(int id);

	List<DC> findByElectricAndWater(String electric, String water);

	List<DC> findByElectricLike(String string);


}
