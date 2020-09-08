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

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import dms.pojo.Specialities;
import dms.repository.SpecialitiesRepository;

@Service
public class SpecialitiesService {

	// 注入数据访问层接口对象
	@Resource
	private SpecialitiesRepository specialitiesRepository;



	public void saveAll(Specialities specialities) {
		specialitiesRepository.save(specialities);
	}


	public List<Specialities> getStusByNameLike(String name) {
		return specialitiesRepository.findByNameLike("%" + name + "%");
	}

	public void delete(int id) {
		specialitiesRepository.deleteById(id);

	}

	public void edit(Specialities specialities) {
		specialitiesRepository.save(specialities);
	}
	public Page<Specialities> findAll(Pageable page) {
		return specialitiesRepository.findAll(page);
	}
	
	public Iterable<Specialities> findAllSort(Sort sort) {
		return specialitiesRepository.findAll(sort);
	}
	public List<Specialities> getSpecialitiesList() {
		return (List<Specialities>) specialitiesRepository.findAll();
	}
	@SuppressWarnings("serial")
	public List<Map<String, Object>> getStusByDynamic(Specialities Specialities) {
		List<Specialities> Specialitiess = specialitiesRepository.findAll(new Specification<Specialities>() {
			@Override
			public Predicate toPredicate(Root<Specialities> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				// 本集合用于封装查询条件
				List<Predicate> predicates = new ArrayList<Predicate>();  
				if(Specialities!=null){
					/** 是否传入了名称来查询  */
					if(!StringUtils.isEmpty(Specialities.getName())){
						predicates.add(cb.like(root.<String> get("name"),"%" + Specialities.getName() + "%"));
					}
					/** 是否传入了id来查询 */
					if(Specialities.getId() != 0){
						predicates.add(cb.equal(root.<String> get("id"),Specialities.getId()));
					}

				}
				return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
			}
		});
		List<Map<String, Object>>  results = new ArrayList<>(); 
		// 遍历查询出的学生对象，提取姓名，年龄，性别信息
		for(Specialities spe :Specialitiess){
			Map<String , Object> speMap = new HashMap<>(); 
			speMap.put("id", spe.getId());
			speMap.put("name", spe.getName());
			speMap.put("dormInfoCode", spe.getCollege().getCode());
			speMap.put("dormInfoCode", spe.getCollege().getName());
			results.add(speMap);
		}
		return results;
	}
	public List<Specialities> findSpecialitiesByIds(String[] ids) {
		// TODO Auto-generated method stub
		List<Specialities> specialitiess=new ArrayList<Specialities>();
		for(String id:ids){
			int id2= Integer.parseInt(id);
			specialitiess.add(specialitiesRepository.findById(id2));
		}
		
		return specialitiess;
	}

	public void deleteBatch(List<Integer> idList) {
		specialitiesRepository.deleteBatch(idList);
		
	}


	public List<Map<String, Object>> getSpecialities() {
		Iterable<Specialities> Specialities = specialitiesRepository.findAll();
		List<Map<String, Object>>  results = new ArrayList<>(); 
		// 遍历查询出的学生对象，提取姓名，年龄，性别信息
		for(Specialities stu :Specialities){
			Map<String , Object> stuMap = new HashMap<>(); 
			stuMap.put("id", stu.getId());
			stuMap.put("name", stu.getName());
			results.add(stuMap);
		}
		return results;
	}

	
}
