package dms.repository;



import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import dms.pojo.Building;
import dms.pojo.Dorm;
public interface BuildingRepository extends PagingAndSortingRepository<Building, Integer>{
	Building findById(int idInt);
	List<Building> findByNameLike(String string);
	// 注意更新和删除是需要加事务的， 并且要加上 @Modify的注解
    @Modifying
    @Transactional
    @Query("delete from Building s where s.buildingId in (?1)")
    void deleteBatch(List<Integer> ids);
	List<Building> findAll(Specification<Building> specification);
}

