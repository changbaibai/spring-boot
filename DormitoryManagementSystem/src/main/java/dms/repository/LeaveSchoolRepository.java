package dms.repository;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import dms.pojo.LeaveSchool;



public interface LeaveSchoolRepository extends PagingAndSortingRepository<LeaveSchool, Integer>{
	LeaveSchool findById(int idInt);
//	List<LeaveSchool> findByNameLike(String string);
	// 注意更新和删除是需要加事务的， 并且要加上 @Modify的注解
    @Modifying
    @Transactional
    @Query("delete from LeaveSchool s where s.id in (?1)")
    void deleteBatch(List<Integer> ids);
	List<LeaveSchool> findByDestinationLike(String string);
	List<LeaveSchool> findAll(Specification<LeaveSchool> specification);
	Page<LeaveSchool> findByStudentId(int id, Pageable pageable);
}

