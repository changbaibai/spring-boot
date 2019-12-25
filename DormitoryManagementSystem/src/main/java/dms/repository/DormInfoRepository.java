package dms.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import dms.pojo.DormInfo;

public interface DormInfoRepository extends JpaRepository<DormInfo, Integer>{

	DormInfo findByCode(int code);

	DormInfo findByName(String name);

	
}

