package dms.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import dms.pojo.FKUser;
import dms.pojo.ItemRecord;
import dms.repository.ItemRecordRepository;

@Service
public class ItemRecordService {

	// 注入数据访问层接口对象
	@Resource
	private ItemRecordRepository iteRepository;
	@Resource
	private UserService userService;



	public ItemRecord saveAll(ItemRecord leaveSchool) {
		return iteRepository.save(leaveSchool);
	}



	public void delete(int id) {
		iteRepository.deleteById(id);

	}

	public void edit(ItemRecord leaveSchool) {
		iteRepository.save(leaveSchool);
	}
	public Page<ItemRecord> findAll(Pageable page) {
		return iteRepository.findAll(page);
	}
	
	public Iterable<ItemRecord> findAllSort(Sort sort) {
		return iteRepository.findAll(sort);
	}

	public List<ItemRecord> findItemRecordByIds(String[] ids) {
		// TODO Auto-generated method stub
		List<ItemRecord> leaveSchools=new ArrayList<ItemRecord>();
		for(String id:ids){
			int id2= Integer.parseInt(id);
			leaveSchools.add(iteRepository.findById(id2));
		}
		
		return leaveSchools;
	}

	public void deleteBatch(List<Integer> idList) {
		iteRepository.deleteBatch(idList);
		
	}


	public ItemRecord findById(int id) {
		// TODO Auto-generated method stub
		return iteRepository.findById(id);
	}


	public Page<ItemRecord> findByStudentId(int id,Pageable pageable) {
		return iteRepository.findByStudentId(id,pageable);

	}


	public Map<String, Object> queryUerItemRecordInfo(Integer page, Integer rows) {		
    	String name =getUsername();  	
    	FKUser user=userService.findByName(name);
    	int id =user.getStudents().get(0).getId();
		Pageable pageable = PageRequest.of(page-1,rows,Sort.Direction.DESC,"id");
        Page<ItemRecord> allPage = iteRepository.findByStudentId(id,pageable);
		List<ItemRecord> leas =allPage.getContent();
		List<Map<String, Object>>  results = new ArrayList<>(); 
		// 遍历查询出的学生对象，提取姓名，年龄，性别信息
		for(ItemRecord lea :leas){
			Map<String , Object> map = new HashMap<>(); 
			map.put("id", lea.getId());
			map.put("gs", lea.getGoods());
			map.put("lt", lea.getLeavingTime());
			map.put("rt", lea.getReturnTime());
			results.add(map);
		}
		
        long TotalPages = allPage.getTotalElements();
       
        long Total = allPage.getTotalPages();

        Map<String, Object> map = new HashMap<>();
        map.put("total", TotalPages);
        map.put("totalNotFiltered",Total);
        map.put("rows",results);
        return map;
	}

    /**
     * 获得当前用户名称
     * */
     private String getUsername(){
 		String username = SecurityContextHolder.getContext().getAuthentication().getName();
 		return username;
 	}


	
}
