package dms.repository;



import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;


import dms.pojo.SliderShow;

public interface SliderShowRepository extends JpaRepository<SliderShow, Long>{
 
	SliderShow findById(int i);
	SliderShow findByText(String email);
	List<SliderShow> findByTitle(String name);
	void deleteById(int id);
	
 

}

