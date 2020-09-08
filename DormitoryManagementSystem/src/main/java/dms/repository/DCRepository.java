package dms.repository;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import dms.pojo.DC;



public interface DCRepository extends PagingAndSortingRepository<DC, Integer>{
	DC findById(int id);
	List<DC> findByElectricAndWater(String electric, String water);
	List<DC> findByElectricLike(String string);
	// 注意更新和删除是需要加事务的， 并且要加上 @Modify的注解
    @Modifying
    @Transactional
    @Query("delete from DC s where s.id in (?1)")
    void deleteBatch(List<Integer> ids);
	

}
