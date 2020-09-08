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
import dms.repository.LeaveSchoolRepository;

@Service
public class LeaveSchoolService {

	// 注入数据访问层接口对象
	@Resource
	private LeaveSchoolRepository leaveSchoolRepository;
	@Resource
	private UserService userService;



	public LeaveSchool saveAll(LeaveSchool leaveSchool) {
		return leaveSchoolRepository.save(leaveSchool);
	}


	public List<LeaveSchool> getStusByNameLike(String name) {
		return leaveSchoolRepository.findByDestinationLike("%" + name + "%");
	}

	public void delete(int id) {
		leaveSchoolRepository.deleteById(id);

	}

	public void edit(LeaveSchool leaveSchool) {
		leaveSchoolRepository.save(leaveSchool);
	}
	public Page<LeaveSchool> findAll(Pageable page) {
		return leaveSchoolRepository.findAll(page);
	}
	
	public Iterable<LeaveSchool> findAllSort(Sort sort) {
		return leaveSchoolRepository.findAll(sort);
	}
//	public List<LeaveSchool> getLeaveSchoolList() {
//		return (List<LeaveSchool>) leaveSchoolRepository.findAll();
//	}
//	public List<LeaveSchool> getLeaveSchoolList(int id) {
//		return (List<LeaveSchool>) leaveSchoolRepository.findByStudentId(id);
//	}
	@SuppressWarnings("serial")
	public List<Map<String, Object>> getStusByDynamic(LeaveSchool leaveSchool) {
		List<LeaveSchool> LeaveSchools = leaveSchoolRepository.findAll(new Specification<LeaveSchool>() {
			@Override
			public Predicate toPredicate(Root<LeaveSchool> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				// 本集合用于封装查询条件
				List<Predicate> predicates = new ArrayList<Predicate>();  
				if(leaveSchool!=null){
					/** 是否传入了名称来查询  */
					if(!StringUtils.isEmpty(leaveSchool.getDestination())){
						predicates.add(cb.like(root.<String> get("name"),"%" + leaveSchool.getDestination() + "%"));
					}
					/** 是否传入了id来查询 */
					if(leaveSchool.getId() != 0){
						predicates.add(cb.equal(root.<String> get("id"),leaveSchool.getId()));
					}

				}
				return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
			}
		});
		List<Map<String, Object>>  results = new ArrayList<>(); 
		// 遍历查询出的学生对象，提取姓名，年龄，性别信息
		for(LeaveSchool spe :LeaveSchools){
			Map<String , Object> speMap = new HashMap<>(); 
			speMap.put("id", spe.getId());
			speMap.put("name", spe.getDestination());
			results.add(speMap);
		}
		return results;
	}
	public List<LeaveSchool> findLeaveSchoolByIds(String[] ids) {
		// TODO Auto-generated method stub
		List<LeaveSchool> leaveSchools=new ArrayList<LeaveSchool>();
		for(String id:ids){
			int id2= Integer.parseInt(id);
			leaveSchools.add(leaveSchoolRepository.findById(id2));
		}
		
		return leaveSchools;
	}

	public void deleteBatch(List<Integer> idList) {
		leaveSchoolRepository.deleteBatch(idList);
		
	}
	public List<Map<String, Object>>  getLeaveSchool() {
		Iterable<LeaveSchool> leaveSchools = leaveSchoolRepository.findAll();
		List<Map<String, Object>>  results = new ArrayList<>(); 
		// 遍历查询出的学生对象，提取姓名，年龄，性别信息
		for(LeaveSchool stu :leaveSchools){
			Map<String , Object> stuMap = new HashMap<>(); 
			stuMap.put("id", stu.getId());
			stuMap.put("name", stu.getDestination());
			results.add(stuMap);
		}
		return results;
	}


	public LeaveSchool findById(int id) {
		// TODO Auto-generated method stub
		return leaveSchoolRepository.findById(id);
	}


	public Page<LeaveSchool> findByStudentId(int id,Pageable pageable) {
		return leaveSchoolRepository.findByStudentId(id,pageable);

	}


	public Map<String, Object> queryUerLeaveSchoolInfo(Integer page, Integer rows) {
		
    	String name =getUsername();  	
    	FKUser user=userService.findByName(name);
    	int id =user.getStudents().get(0).getId();
		Pageable pageable = PageRequest.of(page-1,rows,Sort.Direction.DESC,"id");
        Page<LeaveSchool> allPage = leaveSchoolRepository.findByStudentId(id,pageable);
		List<LeaveSchool> leas =allPage.getContent();
		List<Map<String, Object>>  results = new ArrayList<>(); 
		// 遍历查询出的学生对象，提取姓名，年龄，性别信息
		for(LeaveSchool lea :leas){
			System.out.println("啊啊啊啊啊啊"+lea.getReturnTime());
			Map<String , Object> map = new HashMap<>(); 
			map.put("id", lea.getId());
			map.put("des", lea.getDestination());
			map.put("cau", lea.getCause());
			map.put("lt", lea.getLeavingTime());
			map.put("rt", lea.getReturnTime());
			map.put("pn", lea.getParentsName());
			map.put("ph", lea.getParentsPhone());
			map.put("stuId", lea.getStudent().getId());
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
