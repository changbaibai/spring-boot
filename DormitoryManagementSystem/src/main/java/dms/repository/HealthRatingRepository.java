package dms.repository;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import dms.pojo.HealthRating;



public interface HealthRatingRepository extends PagingAndSortingRepository<HealthRating, Integer>{
	HealthRating findById(int idInt);
//	List<HealthRating> findByNameLike(String string);
	// 注意更新和删除是需要加事务的， 并且要加上 @Modify的注解
    @Modifying
    @Transactional
    @Query("delete from HealthRating s where s.id in (?1)")
    void deleteBatch(List<Integer> ids);
	List<HealthRating> findByGradeLike(String str);
	List<HealthRating> findAll(Specification<HealthRating> specification);
}

