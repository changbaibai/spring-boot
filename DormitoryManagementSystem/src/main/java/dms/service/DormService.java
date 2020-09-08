package dms.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import dms.pojo.Dorm;
import dms.pojo.FKUser;
import dms.repository.DormRepository;
import dms.repository.UserRepository;

@Service
public class DormService {
	// 注入持久层接口UserRepository
	@Autowired
    UserRepository userRepository;

	// 注入数据访问层接口对象
	@Resource
	private DormRepository dormRepository;
	public void saveAll(Dorm Dorm) {
		dormRepository.save(Dorm);
	}
	public void delete(int id) {
		dormRepository.deleteById(id);

	}
	public void edit(Dorm Dorm) {
		dormRepository.save(Dorm);
	}
	public Page<Dorm> findAll(Pageable page) {
		return dormRepository.findAll(page);
	}
	
	public Iterable<Dorm> findAllSort(Sort sort) {
		return dormRepository.findAll(sort);
	}
	public List<Dorm> getDormList() {
		return (List<Dorm>) dormRepository.findAll();
	}
	public List<Map<String, Object>> getStusByDynamic(Dorm Dorm) {
		Iterable<dms.pojo.Dorm> Dorms = dormRepository.findAll();
		List<Map<String, Object>>  results = new ArrayList<>(); 
		// 遍历查询出的学生对象，提取姓名，年龄，性别信息
		for(Dorm spe :Dorms){
			Map<String , Object> speMap = new HashMap<>(); 
			speMap.put("id", spe.getCode());
			speMap.put("name",spe.getName() );
			speMap.put("name", spe.getBuilding().getName());
//			speMap.put("dormInfoCode", spe.getCollege().getCode());
//			speMap.put("dormInfoCode", spe.getCollege().getName());
			results.add(speMap);
		}
		return results;
	}
	public List<Dorm> findDormByIds(String[] ids) {
		// TODO Auto-generated method stub
		List<Dorm> Dorms=new ArrayList<Dorm>();
		for(String id:ids){
			int id2= Integer.parseInt(id);
			Dorms.add(dormRepository.findById(id2));
		}
		
		return Dorms;
	}

	public void deleteBatch(List<Integer> idList) {
		dormRepository.deleteBatch(idList);
		
	}
	public List<Map<String, Object>>  getDorm() {
		Iterable<Dorm> Dorms = dormRepository.findAll();
		List<Map<String, Object>>  results = new ArrayList<>(); 
		// 遍历查询出的学生对象，提取姓名，年龄，性别信息
		for(Dorm stu :Dorms){
			System.out.println("啦啦啦啦啦"+stu);
			Map<String , Object> stuMap = new HashMap<>(); 
			stuMap.put("id", stu.getCode());
			stuMap.put("name", stu.getName() + stu.getBuilding().getName());
			results.add(stuMap);
		}
		return results;
	}
	
	public List<Map<String, Object>> getDormInfo() {
		List<Map<String, Object>>  results = new ArrayList<>(); 
		String userName = getUsername();
		FKUser user=userRepository.findByUsername(userName);
		if (user.getBuildings().isEmpty()) {
			return results;
		}else {
			List<Dorm> dorms=dormRepository.findByBuilding_buildingId(user.getBuildings().get(0).getBuildingId());		
			// 遍历查询出的宿舍对象
			for(Dorm dorm:dorms){
				Map<String , Object> stuMap = new HashMap<>(); 			
				stuMap.put("id",dorm.getCode());
				stuMap.put("name",dorm.getName());		
				results.add(stuMap);
			}
			return results;
		}

	}
	   /**
	    * 获得当前用户名称
	    * */
	    private String getUsername(){
			String username = SecurityContextHolder.getContext().getAuthentication().getName();			
			return username;
		}



	
}
