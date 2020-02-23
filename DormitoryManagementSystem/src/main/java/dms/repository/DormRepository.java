package dms.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import dms.pojo.Dorm;

public interface DormRepository extends JpaRepository<Dorm, Integer>{

	Dorm findByCode(int code);

	Dorm findByName(String name);

	
}

