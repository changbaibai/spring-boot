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
import dms.pojo.LeaveSchool;
import dms.pojo.Repair;
import dms.repository.RepairRepository;

@Service
public class RepairService {

	// 注入数据访问层接口对象
	@Resource
	private RepairRepository repairRepository;

	@Resource
	private UserService userService;



	public Repair saveAll(Repair repair) {
		return repairRepository.save(repair);
	}


	public List<Repair> getStusByNameLike(String name) {
		return repairRepository.findByContentLike("%" + name + "%");
	}

	public void delete(int id) {
		repairRepository.deleteById(id);

	}

	public void edit(Repair repair) {
		repairRepository.save(repair);
	}
	public Page<Repair> findAll(Pageable page) {
		return repairRepository.findAll(page);
	}
	
	public Iterable<Repair> findAllSort(Sort sort) {
		return repairRepository.findAll(sort);
	}
	public List<Repair> getRepairList() {
		return (List<Repair>) repairRepository.findAll();
	}
	@SuppressWarnings("serial")
	public List<Map<String, Object>> getStusByDynamic(Repair repair) {
		List<Repair> Repairs = repairRepository.findAll(new Specification<Repair>() {
			@Override
			public Predicate toPredicate(Root<Repair> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				// 本集合用于封装查询条件
				List<Predicate> predicates = new ArrayList<Predicate>();  
				if(repair!=null){
					/** 是否传入了名称来查询  */
					if(!StringUtils.isEmpty(repair.getContent())){
						predicates.add(cb.like(root.<String> get("name"),"%" + repair.getContent() + "%"));
					}
					/** 是否传入了id来查询 */
					if(repair.getId() != 0){
						predicates.add(cb.equal(root.<String> get("id"),repair.getId()));
					}

				}
				return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
			}
		});
		List<Map<String, Object>>  results = new ArrayList<>(); 
		// 遍历查询出的学生对象，提取姓名，年龄，性别信息
		for(Repair spe :Repairs){
			Map<String , Object> speMap = new HashMap<>(); 
			speMap.put("id", spe.getId());
			speMap.put("name", spe.getContent());
//			speMap.put("dormInfoCode", spe.getCollege().getCode());
//			speMap.put("dormInfoCode", spe.getCollege().getName());
			results.add(speMap);
		}
		return results;
	}
	public List<Repair> findRepairByIds(String[] ids) {
		// TODO Auto-generated method stub
		List<Repair> repairs=new ArrayList<Repair>();
		for(String id:ids){
			int id2= Integer.parseInt(id);
			repairs.add(repairRepository.findById(id2));
		}		
		return repairs;
	}
	
	public Repair findById(int id) {

		Repair re =	repairRepository.findById(id);
		return re;

	}
	public void deleteBatch(List<Integer> idList) {
		repairRepository.deleteBatch(idList);		
	}


	public Map<String, Object> findAllInfo(int page, int rows) {
		// TODO Auto-generated method stub
		return null;
	}


	public Map<String, Object> queryUerMaintainerInfo(int page, int rows) {
    	String name =getUsername();  	
    	FKUser user=userService.findByName(name);
    	int code=user.getStudents().get(0).getDorm().getCode();
		Pageable pageable = PageRequest.of(page-1,rows,Sort.Direction.DESC,"id");
        Page<Repair> allPage = repairRepository.findByDormCode(code,pageable);
		List<Repair> leas =allPage.getContent();
		List<Map<String, Object>>  results = new ArrayList<>(); 
		// 遍历查询出的学生对象，提取姓名，年龄，性别信息
		for(Repair lea :leas){
			Map<String , Object> map = new HashMap<>(); 
			map.put("id", lea.getId());
			map.put("co", lea.getContent());
			map.put("st", lea.getState());
			if (lea.getMaintainer()!=null) {
				map.put("name", lea.getMaintainer().getName());
			}
			

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
