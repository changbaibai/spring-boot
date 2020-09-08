package dms.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import dms.pojo.FKRole;
import dms.pojo.FKUser;
import dms.pojo.LeaveSchool;
import dms.pojo.Specialities;
import dms.repository.UserRepository;



/**
 * 需要实现UserDetailsService接口
 * 因为在Spring Security中配置的相关参数需要是UserDetailsService类型的数据
 * */
@Service
public class UserService implements UserDetailsService{

	// 注入持久层接口UserRepository
	@Autowired
    UserRepository userRepository;
	
	/*
	 *  重写UserDetailsService接口中的loadUserByUsername方法，通过该方法查询到对应的用户(non-Javadoc)
	 *  返回对象UserDetails是Spring Security中一个核心的接口。
	 *  其中定义了一些可以获取用户名、密码、权限等与认证相关的信息的方法。
	 */
    public FKUser save(FKUser fkUser) {
    	String s =fkUser.getPassword();
    	String password	=new BCryptPasswordEncoder().encode(s);
		fkUser.setPassword(password);
        return userRepository.save(fkUser);
    }
    public FKUser saveAll(FKUser fkUser) {
        return userRepository.save(fkUser);
    }

	
	public FKUser getUserById(int id) {
		return userRepository.findById(id);
	}


	//登录验证
  
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// 调用持久层接口findByLoginName方法查找用户，此处的传进来的参数实际是loginName
		FKUser fkUser = userRepository.findByUsername(username);
		System.out.println(fkUser);
        if (fkUser == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        // 创建List集合，用来保存用户权限，GrantedAuthority对象代表赋予给当前用户的权限
        List<GrantedAuthority> authorities = new ArrayList<>();
        // 获得当前用户权限集合
        List<FKRole> roles = fkUser.getRoles();
        for (FKRole role : roles) {
        	// 将关联对象Role的authority属性保存为用户的认证权限
        	authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
        }
       
        System.out.println("new BCryptPasswordEncoder().encode(fkUser.getPassword().trim()):"+new BCryptPasswordEncoder().encode(fkUser.getPassword().trim()));
        // 此处返回的是org.springframework.security.core.userdetails.User类，该类是Spring Security内部的实现
        return new User(fkUser.getUsername(), fkUser.getPassword().trim(), authorities);
	}
	public Page<FKUser> findAll(Pageable page) {
		return userRepository.findAll(page);
	}
	public void deleteBatch(List<Integer> idList) {
		userRepository.deleteBatch(idList);
		
	}
	public FKUser findByName(String str) {	
		FKUser fkUser =userRepository.findByUsername(str);
		return fkUser;
	}
	public Map<String, Object> queryAdminInfo(int page, int rows) {
    	int id =3;
		Pageable pageable = PageRequest.of(page-1,rows,Sort.Direction.DESC,"id");
        Page<FKUser> allPage = userRepository.findByroles_Id(id,pageable);
		List<FKUser>  userList=allPage.getContent();
		
		List<Map<String, Object>>  results = new ArrayList<>(); 
		// 遍历查询出的学生对象，提取姓名，年龄，性别信息
		for(FKUser user :userList){
			Map<String , Object> map = new HashMap<>(); 
			map.put("id", user.getId());
			map.put("userName", user.getUsername());
			if(user.getBuildings().isEmpty())
			
			{   
				System.out.println("bbb");
			 
				}
			else {
				map.put("buildName", user.getBuildings().get(0).getName());
				System.out.println("ccc");
				
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
	public FKUser findByUsername(String username) {
		// TODO Auto-generated method stub
		return userRepository.findByUsername(username);
	}








}
