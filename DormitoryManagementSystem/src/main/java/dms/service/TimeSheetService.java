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
import dms.pojo.TimeSheet;
import dms.repository.TimeSheetRepository;

@Service
public class TimeSheetService {

	// 注入数据访问层接口对象
	@Resource
	private TimeSheetRepository timeSheetRepository;



	public void saveAll(TimeSheet timeSheet) {
		timeSheetRepository.save(timeSheet);
	}


	public List<TimeSheet> getStusByNameLike(String name) {
		return timeSheetRepository.findByCheckerLike("%" + name + "%");
	}

	public void delete(int id) {
		timeSheetRepository.deleteById(id);

	}

	public void edit(TimeSheet timeSheet) {
		timeSheetRepository.save(timeSheet);
	}
	public Page<TimeSheet> findAll(Pageable page) {
		return timeSheetRepository.findAll(page);
	}
	
	public Iterable<TimeSheet> findAllSort(Sort sort) {
		return timeSheetRepository.findAll(sort);
	}
	public List<TimeSheet> getTimeSheetList() {
		return (List<TimeSheet>) timeSheetRepository.findAll();
	}
	@SuppressWarnings("serial")
	public List<Map<String, Object>> getStusByDynamic(TimeSheet timeSheet) {
		List<TimeSheet> TimeSheets = timeSheetRepository.findAll(new Specification<TimeSheet>() {
			@Override
			public Predicate toPredicate(Root<TimeSheet> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				// 本集合用于封装查询条件
				List<Predicate> predicates = new ArrayList<Predicate>();  
				if(timeSheet!=null){
					/** 是否传入了名称来查询  */
					if(!StringUtils.isEmpty(timeSheet.getChecker())){
						predicates.add(cb.like(root.<String> get("name"),"%" + timeSheet.getChecker() + "%"));
					}
					/** 是否传入了id来查询 */
					if(timeSheet.getId() != 0){
						predicates.add(cb.equal(root.<String> get("id"),timeSheet.getId()));
					}

				}
				return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
			}
		});
		List<Map<String, Object>>  results = new ArrayList<>(); 
		// 遍历查询出的学生对象，提取姓名，年龄，性别信息
		for(TimeSheet spe :TimeSheets){
			Map<String , Object> speMap = new HashMap<>(); 
			speMap.put("id", spe.getId());
			speMap.put("name", spe.getChecker());
//			speMap.put("timeSheetInfoCode", spe.getCollege().getCode());
//			speMap.put("timeSheetInfoCode", spe.getCollege().getName());
			results.add(speMap);
		}
		return results;
	}
	public List<TimeSheet> findTimeSheetByIds(String[] ids) {
		// TODO Auto-generated method stub
		List<TimeSheet> timeSheets=new ArrayList<TimeSheet>();
		for(String id:ids){
			int id2= Integer.parseInt(id);
			timeSheets.add(timeSheetRepository.findById(id2));
		}
		
		return timeSheets;
	}

	public void deleteBatch(List<Integer> idList) {
		timeSheetRepository.deleteBatch(idList);
		
	}
	public List<Map<String, Object>>  getTimeSheet() {
		Iterable<TimeSheet> timeSheets = timeSheetRepository.findAll();
		List<Map<String, Object>>  results = new ArrayList<>(); 
		// 遍历查询出的学生对象，提取姓名，年龄，性别信息
		for(TimeSheet stu :timeSheets){
			Map<String , Object> stuMap = new HashMap<>(); 
			stuMap.put("id", stu.getId());
			stuMap.put("name", stu.getChecker());
			results.add(stuMap);
		}
		return results;
	}


	public Page<TimeSheet> findByChecker(String checker, Pageable pageable) {
		// TODO Auto-generated method stub
		return timeSheetRepository.findByChecker(checker,pageable);
	}


	public Map<String, Object> queryOneStudentTimeSheetInfo(int page, int rows)
 {
		
    	String name =getUsername();  	

		Pageable pageable = PageRequest.of(page-1,rows,Sort.Direction.DESC,"id");
        Page<TimeSheet> allPage = timeSheetRepository.findByStudent_name(name,pageable);
		List<TimeSheet> tims =allPage.getContent();
		List<Map<String, Object>>  results = new ArrayList<>(); 
		// 遍历查询出的学生对象，提取姓名，年龄，性别信息
		for(TimeSheet tim :tims){
			Map<String , Object> map = new HashMap<>(); 
			map.put("id", tim.getId());
			map.put("time", tim.getTime());
			map.put("type", tim.getType());
			map.put("cause", tim.getCause());
			map.put("checker", tim.getChecker());

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
