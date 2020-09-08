package dms.service;

import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import dms.pojo.HealthRating;
import dms.pojo.Notice;
import dms.repository.HealthRatingRepository;

@Service
public class HealthRatingService {

	// 注入数据访问层接口对象
	@Resource
	private HealthRatingRepository healthRatingRepository;



	public void saveAll(HealthRating healthRating) {
	  healthRatingRepository.save(healthRating);
	}


	public List<HealthRating> getStusByNameLike(String name) {
		return healthRatingRepository.findByGradeLike("%" + name + "%");
	}

	public void delete(int id) {
		healthRatingRepository.deleteById(id);

	}

	public void edit(HealthRating healthRating) {
		healthRatingRepository.save(healthRating);
	}
	public Page<HealthRating> findAll(Pageable page) {
		return healthRatingRepository.findAll(page);
	}
	
	public Iterable<HealthRating> findAllSort(Sort sort) {
		return healthRatingRepository.findAll(sort);
	}
	public List<HealthRating> getHealthRatingList() {
		return (List<HealthRating>) healthRatingRepository.findAll();
	}
	@SuppressWarnings("serial")
	public List<Map<String, Object>> getStusByDynamic(HealthRating healthRating) {
		List<HealthRating> HealthRatings = healthRatingRepository.findAll(new Specification<HealthRating>() {
			@Override
			public Predicate toPredicate(Root<HealthRating> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				// 本集合用于封装查询条件
				List<Predicate> predicates = new ArrayList<Predicate>();  
				if(healthRating!=null){
					/** 是否传入了名称来查询  */
					if(!StringUtils.isEmpty(healthRating.getGrade())){
						predicates.add(cb.like(root.<String> get("name"),"%" + healthRating.getGrade() + "%"));
					}
					/** 是否传入了id来查询 */
					if(healthRating.getId() != 0){
						predicates.add(cb.equal(root.<String> get("id"),healthRating.getId()));
					}

				}
				return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
			}
		});
		List<Map<String, Object>>  results = new ArrayList<>(); 
		// 遍历查询出的学生对象，提取姓名，年龄，性别信息
		for(HealthRating spe :HealthRatings){
			Map<String , Object> speMap = new HashMap<>(); 
			speMap.put("id", spe.getId());
			speMap.put("name", spe.getGrade());
//			speMap.put("healthRatingInfoCode", spe.getCollege().getCode());
//			speMap.put("healthRatingInfoCode", spe.getCollege().getName());
			results.add(speMap);
		}
		return results;
	}
	public List<HealthRating> findHealthRatingByIds(String[] ids) {
		// TODO Auto-generated method stub
		List<HealthRating> healthRatings=new ArrayList<HealthRating>();
		for(String id:ids){
			int id2= Integer.parseInt(id);
			healthRatings.add(healthRatingRepository.findById(id2));
		}
		
		return healthRatings;
	}

	public void deleteBatch(List<Integer> idList) {
		healthRatingRepository.deleteBatch(idList);
		
	}
	public List<Map<String, Object>>  getHealthRating() {
		Iterable<HealthRating> healthRatings = healthRatingRepository.findAll();
		List<Map<String, Object>>  results = new ArrayList<>(); 
		// 遍历查询出的学生对象，提取姓名，年龄，性别信息
		for(HealthRating stu :healthRatings){
			Map<String , Object> stuMap = new HashMap<>(); 
			stuMap.put("id", stu.getId());
			stuMap.put("name", stu.getGrade());
			results.add(stuMap);
		}
		return results;
	}


	public Map<String, Object> findAllInfo(int page, int rows) {
		Pageable pageable = PageRequest.of(page-1,rows,Sort.Direction.DESC,"id");
		Page<HealthRating> healthRatingDatas = healthRatingRepository.findAll(pageable);
		List<HealthRating> leas =healthRatingDatas.getContent();
		System.out.println("啊啊啊啊啊啊啊啊"+healthRatingDatas);
		List<Map<String, Object>>  results = new ArrayList<>(); 
		// 遍历查询出的学生对象，提取姓名，年龄，性别信息
		for(HealthRating lea :leas){
			Map<String , Object> map = new HashMap<>(); 
			map.put("id", lea.getId());
			map.put("grade", lea.getGrade());
			map.put("content", lea.getContent());
			map.put("dormName", lea.getDorm().getName());
			map.put("buildName", lea.getDorm().getBuilding().getName());
			map.put("time", lea.getTime());
			results.add(map);
		}
	
        long TotalPages = healthRatingDatas.getTotalElements();
       
        long Total = healthRatingDatas.getTotalPages();

        Map<String, Object> map = new HashMap<>();
        map.put("total", TotalPages);
        map.put("totalNotFiltered",Total);
        map.put("rows",results);
        return map;
	}


	
}
