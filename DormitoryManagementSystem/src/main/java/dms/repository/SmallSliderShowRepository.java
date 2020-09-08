package dms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dms.pojo.SmallSliderShow;

public interface SmallSliderShowRepository extends JpaRepository<SmallSliderShow, Long>{
 
	SmallSliderShow findById(int i);
	
 

}

