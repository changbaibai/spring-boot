package dms.repository;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import dms.pojo.VisitInfo;



public interface VisitInfoRepository extends PagingAndSortingRepository<VisitInfo, Integer>{
	VisitInfo findById(int id);

    @Modifying
    @Transactional
    @Query("delete from VisitInfo s where s.id in (?1)")
    void deleteBatch(List<Integer> ids);
	

}
