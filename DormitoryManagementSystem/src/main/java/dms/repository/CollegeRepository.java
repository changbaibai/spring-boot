package dms.repository;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import dms.pojo.College;



public interface CollegeRepository extends PagingAndSortingRepository<College, Integer>{
	List<College> findByNameLike(String string);
	College findByCode(int id2);
	// 注意更新和删除是需要加事务的， 并且要加上 @Modify的注解
    @Modifying
    @Transactional
    @Query("delete from College s where s.code in (?1)")
    void deleteBatch(List<Integer> ids);
	
}

