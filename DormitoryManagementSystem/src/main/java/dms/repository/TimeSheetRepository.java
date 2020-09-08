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
import dms.pojo.TimeSheet;



public interface TimeSheetRepository extends PagingAndSortingRepository<TimeSheet, Integer>{
	TimeSheet findById(int idInt);
//	List<TimeSheet> findByNameLike(String string);
	// 注意更新和删除是需要加事务的， 并且要加上 @Modify的注解
    @Modifying
    @Transactional
    @Query("delete from TimeSheet s where s.id in (?1)")
    void deleteBatch(List<Integer> ids);
	List<TimeSheet> findByCheckerLike(String string);
	List<TimeSheet> findAll(Specification<TimeSheet> specification);
	Page<TimeSheet> findByChecker(String checker, Pageable pageable);
	Page<TimeSheet> findByStudent_name(String name, Pageable pageable);

	
}

