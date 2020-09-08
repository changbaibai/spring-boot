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

import dms.pojo.Building;
import dms.pojo.Dorm;
import dms.repository.BuildingRepository;

@Service
public class BuildingService {

	// 注入数据访问层接口对象
	@Resource
	private BuildingRepository BuildingRepository;



	public void saveAll(Building building) {
		BuildingRepository.save(building);
	}


	public void delete(int id) {
		BuildingRepository.deleteById(id);

	}

	public void edit(Building building) {
		BuildingRepository.save(building);
	}
	public Page<Building> findAll(Pageable page) {
		return BuildingRepository.findAll(page);
	}
	
	public Iterable<Building> findAllSort(Sort sort) {
		return BuildingRepository.findAll(sort);
	}
	public List<Building> getBuildingList() {
		return (List<Building>) BuildingRepository.findAll();
	}
	public List<Map<String, Object>> getStusByDynamic(Building building) {
		Iterable<dms.pojo.Building> buildings = BuildingRepository.findAll();
		List<Map<String, Object>>  results = new ArrayList<>(); 
		// 遍历查询出的学生对象，提取姓名，年龄，性别信息
		for(Building spe :buildings){
			Map<String , Object> speMap = new HashMap<>(); 
			speMap.put("id", spe.getBuildingId());
			speMap.put("name", spe.getName());

			results.add(speMap);
		}
		return results;
	}
	public List<Building> findBuildingByIds(String[] ids) {
		// TODO Auto-generated method stub
		List<Building> Buildings=new ArrayList<Building>();
		for(String id:ids){
			int id2= Integer.parseInt(id);
			Buildings.add(BuildingRepository.findById(id2));
		}
		
		return Buildings;
	}

	public void deleteBatch(List<Integer> idList) {
		BuildingRepository.deleteBatch(idList);
		
	}
	public List<Map<String, Object>>  getBuilding() {
		Iterable<Building> Buildings = BuildingRepository.findAll();
		List<Map<String, Object>>  results = new ArrayList<>(); 
		// 遍历查询出的学生对象，提取姓名，年龄，性别信息
		for(Building stu :Buildings){
			Map<String , Object> stuMap = new HashMap<>(); 
			stuMap.put("id", stu.getBuildingId());
			stuMap.put("name", stu.getName());
			results.add(stuMap);
		}
		return results;
	}





	
}
