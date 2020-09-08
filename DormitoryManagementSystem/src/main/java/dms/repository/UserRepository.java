package dms.repository;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import dms.pojo.FKUser;
import dms.pojo.LeaveSchool;


public interface UserRepository extends PagingAndSortingRepository<FKUser,Integer>{

	// 注意更新和删除是需要加事务的， 并且要加上 @Modify的注解
    @Modifying
    @Transactional
    @Query("delete from FKUser s where s.id in (?1)")
    
    void deleteBatch(List<Integer> ids);

 // 根据用户名名查询出用户
    FKUser findByUsername(String name);
    
    FKUser findById(int id);
	
   
	Page<FKUser> findByroles_Id(int id, Pageable pageable);



}
