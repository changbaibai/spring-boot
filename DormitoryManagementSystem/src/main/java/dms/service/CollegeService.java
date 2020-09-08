package dms.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import dms.pojo.College;
import dms.repository.CollegeRepository;

@Service
public class CollegeService {

	// 注入数据访问层接口对象
	@Resource
	private CollegeRepository collegeRepository;



	public void saveAll(College college) {
		collegeRepository.save(college);
	}


	public List<College> getStusByNameLike(String name) {
		return collegeRepository.findByNameLike("%" + name + "%");
	}


	public void edit(College college) {
		collegeRepository.save(college);
	}
	public Page<College> findAll(Pageable page) {
		return collegeRepository.findAll(page);
	}
	
	public Iterable<College> findAllSort(Sort sort) {
		return collegeRepository.findAll(sort);
	}
	public List<College> getCollegeList() {
		return (List<College>) collegeRepository.findAll();
	}
	public List<College> findCollegeByIds(String[] ids) {
		// TODO Auto-generated method stub
		List<College> colleges=new ArrayList<College>();
		for(String id:ids){
			int id2= Integer.parseInt(id);
			colleges.add(collegeRepository.findByCode(id2));
		}
		
		return colleges;
	}

	public void deleteBatch(List<Integer> idList) {
		collegeRepository.deleteBatch(idList);
		
	}

	public List<Map<String, Object>>  getCollege() {
		Iterable<College> colleges = collegeRepository.findAll();
		List<Map<String, Object>>  results = new ArrayList<>(); 
		// 遍历查询出的学生对象，提取姓名，年龄，性别信息
		for(College stu :colleges){
			Map<String , Object> stuMap = new HashMap<>(); 
			stuMap.put("id", stu.getCode());
			stuMap.put("name", stu.getName());
			results.add(stuMap);
		}
		return results;
	}

	
}
