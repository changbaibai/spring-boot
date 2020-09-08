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

import dms.pojo.Maintainer;
import dms.pojo.FKUser;
import dms.repository.MaintainerRepository;
import dms.repository.UserRepository;

@Service
public class MaintainerService {
	// 注入持久层接口UserRepository
	@Autowired
    UserRepository userRepository;

	// 注入数据访问层接口对象
	@Resource
	private MaintainerRepository maintainerRepository;
	public Maintainer saveAll(Maintainer maintainer) {
		return maintainerRepository.save(maintainer);
	}
	public void delete(int id) {
		maintainerRepository.deleteById(id);

	}
	public void edit(Maintainer maintainer) {
		maintainerRepository.save(maintainer);
	}
	public Page<Maintainer> findAll(Pageable page) {
		return maintainerRepository.findAll(page);
	}
	
	public Iterable<Maintainer> findAllSort(Sort sort) {
		return maintainerRepository.findAll(sort);
	}
	public List<Maintainer> getMaintainerList() {
		return (List<Maintainer>) maintainerRepository.findAll();
	}
	public List<Map<String, Object>> getStusByDynamic(Maintainer Maintainer) {
		Iterable<dms.pojo.Maintainer> Maintainers = maintainerRepository.findAll();
		List<Map<String, Object>>  results = new ArrayList<>(); 
		// 遍历查询出的维修对象，提取信息
		for(Maintainer ma :Maintainers){
			Map<String , Object> maMap = new HashMap<>(); 
			maMap.put("id", ma.getId());
			maMap.put("name",ma.getName() );
			maMap.put("phone",ma.getPhone() );

			results.add(maMap);
		}
		return results;
	}
	public List<Maintainer> findMaintainerByIds(String[] ids) {
		// TODO Auto-generated method stub
		List<Maintainer> Maintainers=new ArrayList<Maintainer>();
		for(String id:ids){
			int id2= Integer.parseInt(id);
			Maintainers.add(maintainerRepository.findById(id2));
		}
		
		return Maintainers;
	}
	public Maintainer findById(int id) {
		return maintainerRepository.findById(id);
	}
	public void deleteBatch(List<Integer> idList) {
		maintainerRepository.deleteBatch(idList);
		
	}
	public List<Map<String, Object>>  getMaintainerInfo() {
		List<Map<String, Object>>  results = new ArrayList<>(); 

		List<Maintainer> maintainers=(List<Maintainer>) maintainerRepository.findAll();	

		// 遍历查询出的维修人员对象，提取id,姓名，等信息
		for(Maintainer ma :maintainers){
			Map<String , Object> maMap = new HashMap<>(); 
			maMap.put("id", ma.getId());
			maMap.put("name","维修人员名字："+ma.getName() +"  手机号："+ma.getPhone()+"  维修次数："+ma.getNum());
			results.add(maMap);
		}
		return results;
	}

	   /**
	    * 获得当前用户名称
	    * */
	    private String getUsername(){
			String username = SecurityContextHolder.getContext().getAuthentication().getName();			
			return username;
		}



	
}
