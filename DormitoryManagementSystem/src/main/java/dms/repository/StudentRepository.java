package dms.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import dms.pojo.Student;




public interface StudentRepository extends JpaRepository<Student,Integer>,JpaSpecificationExecutor<Student>{
	
}
