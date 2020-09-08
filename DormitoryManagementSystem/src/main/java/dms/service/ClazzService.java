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

import dms.pojo.Clazz;
import dms.repository.ClazzRepository;

@Service
public class ClazzService {

	// 注入数据访问层接口对象
	@Resource
	private ClazzRepository clazzRepository;



	public void saveAll(Clazz clazz) {
		clazzRepository.save(clazz);
	}


	public List<Clazz> getStusByNameLike(String name) {
		return clazzRepository.findByNameLike("%" + name + "%");
	}

	public void delete(int id) {
		clazzRepository.deleteById(id);

	}

	public void edit(Clazz clazz) {
		clazzRepository.save(clazz);
	}
	public Page<Clazz> findAll(Pageable page) {
		return clazzRepository.findAll(page);
	}
	
	public Iterable<Clazz> findAllSort(Sort sort) {
		return clazzRepository.findAll(sort);
	}
	public List<Clazz> getClazzList() {
		return (List<Clazz>) clazzRepository.findAll();
	}
	@SuppressWarnings("serial")
	public List<Map<String, Object>> getStusByDynamic(Clazz clazz) {
		List<Clazz> Clazzs = clazzRepository.findAll(new Specification<Clazz>() {
			@Override
			public Predicate toPredicate(Root<Clazz> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				// 本集合用于封装查询条件
				List<Predicate> predicates = new ArrayList<Predicate>();  
				if(clazz!=null){
					/** 是否传入了名称来查询  */
					if(!StringUtils.isEmpty(clazz.getName())){
						predicates.add(cb.like(root.<String> get("name"),"%" + clazz.getName() + "%"));
					}
					/** 是否传入了id来查询 */
					if(clazz.getClazzId() != 0){
						predicates.add(cb.equal(root.<String> get("id"),clazz.getClazzId()));
					}

				}
				return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
			}
		});
		List<Map<String, Object>>  results = new ArrayList<>(); 
		// 遍历查询出的学生对象，提取姓名，年龄，性别信息
		for(Clazz spe :Clazzs){
			Map<String , Object> speMap = new HashMap<>(); 
			speMap.put("id", spe.getClazzId());
			speMap.put("name", spe.getName());
//			speMap.put("clazzInfoCode", spe.getCollege().getCode());
//			speMap.put("clazzInfoCode", spe.getCollege().getName());
			results.add(speMap);
		}
		return results;
	}
	public List<Clazz> findClazzByIds(String[] ids) {
		// TODO Auto-generated method stub
		List<Clazz> clazzs=new ArrayList<Clazz>();
		for(String id:ids){
			int id2= Integer.parseInt(id);
			clazzs.add(clazzRepository.findById(id2));
		}
		
		return clazzs;
	}

	public void deleteBatch(List<Integer> idList) {
		clazzRepository.deleteBatch(idList);
		
	}
	public List<Map<String, Object>>  getClazz() {
		Iterable<Clazz> clazzs = clazzRepository.findAll();
		List<Map<String, Object>>  results = new ArrayList<>(); 
		// 遍历查询出的学生对象，提取姓名，年龄，性别信息
		for(Clazz stu :clazzs){
			Map<String , Object> stuMap = new HashMap<>(); 
			stuMap.put("id", stu.getClazzId());
			stuMap.put("name", stu.getName());
			results.add(stuMap);
		}
		return results;
	}


	
}
