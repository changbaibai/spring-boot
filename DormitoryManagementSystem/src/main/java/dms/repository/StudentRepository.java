package dms.repository;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import dms.pojo.Dorm;
import dms.pojo.Student;



public interface StudentRepository extends PagingAndSortingRepository<Student, Integer>{
	Student findById(int idInt);
	List<Student> findByNameLike(String string);
	// 注意更新和删除是需要加事务的， 并且要加上 @Modify的注解
    @Modifying
    @Transactional
    @Query("delete from Student s where s.id in (?1)")
    void deleteBatch(List<Integer> ids);
	List<Student> findAll(Specification<Student> specification);
	Student findBySno(String sno);
	List<Student> findByDorm_code(int id);
	Student findByName(String name);
}

