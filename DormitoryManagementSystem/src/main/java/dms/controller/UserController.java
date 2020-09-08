package dms.controller;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import dms.pojo.Building;
import dms.pojo.FKRole;
import dms.pojo.FKUser;
import dms.pojo.LeaveSchool;
import dms.pojo.Specialities;
import dms.pojo.Student;
import dms.service.RoleService;
import dms.service.StudentService;
import dms.service.UserService;

@Controller
@RequestMapping("/superAdmin")
public class UserController {
        
	@Resource
	private UserService userService;
	@Resource
	private RoleService roleService;
	@Resource
	private StudentService studentService;
    @RequestMapping("/addUser")
    public String add(FKUser fkuser,@RequestParam("Data") int id) {
    	FKRole role =new FKRole();
    	role.setId(id);
    	String str = ""; 
    
    	if(id==1) {
    		str="ROLE_STUDENT";
    	}    	
    	if(id==2) {
    		str="ROLE_SUPERADMIN";
    	}    	
    	if(id==3) {
    		str="ROLE_ADMIN";
    	} 
    	FKUser user=new FKUser();
    	user =userService.findByUsername(fkuser.getUsername());
    	if (user==null) {
        	role.setAuthority(str);
        	List<FKRole> roles =Arrays.asList(role);
        	fkuser.setRoles(roles);
        	FKUser u =userService.save(fkuser);
        	if(u==null) {
        		System.out.println("添加成功！");
        	}
    	}else {
    		return "0";
    	}

    	return "redirect:/superAdmin/showPageUser";
    }

	

    
    @RequestMapping("/saveRole")
    public String saveRole(Model model,String sid,String loginName,String username,String authority) {
    	int id=Integer.parseInt(String.valueOf(sid));
    	FKUser user= new FKUser();
    	user.setId(id);
    	user.setUsername(username);
		FKRole role=new FKRole(); 
		role.setAuthority(authority);
		user.getRoles().add(role);
		userService.save(user);
		roleService.save(role);
        return "redirect:/superAdmin/showPageUser";
    }
    
    @RequestMapping("/editRole")
    public String add(String uid,String cid) {
    	int userId=Integer.parseInt(String.valueOf(uid));
    	FKUser user1 = userService.getUserById(userId);
    	FKUser user =new FKUser();
    	FKRole role =new FKRole();
    	int id=Integer.parseInt(String.valueOf(cid));
    	role.setId(id);
    	user.setId(user1.getId());
    	user.setPassword(user1.getPassword());
    	user.setUsername(user1.getUsername());
    	List<FKRole> roles =Arrays.asList(role);
    	user.setRoles(roles);
    	userService.saveAll(user);
    	return "redirect:/superAdmin/showPageUser";
    }
	
	// 分页查询
	@RequestMapping("/showPageUser")
	public String showPageUser(Model model, @RequestParam(value = "page", defaultValue = "1") String pa,
			@RequestParam(value = "totalPage", defaultValue = "1") int totalPage,
			@RequestParam(value = "size", defaultValue = "4") int size) {
		System.out.println("AAAAAAAAAAAA page="+pa);
		int page=Integer.parseInt(String.valueOf(pa));
		if (page == -1) 
		{ page++; } 
		if (page == totalPage) 
		{ page--; }
		// 指定排序参数对象：根据id，进行降序查询
		Sort sort = new Sort(Sort.Direction.DESC, "id");
		/**
		 * 封装分页实体 参数一：pageIndex表示当前查询的第几页(默认从0开始，0表示第一页) 参数二：表示每页展示多少数据，现在设置每页展示2条数据
		 * 参数三：封装排序对象，根据该对象的参数指定根据id降序查询
		 */
		@SuppressWarnings("deprecation")
		Pageable pageable = new PageRequest(page, size, sort);
		Page<FKUser> articleDatas = userService.findAll(pageable);
		System.out.println("查询总页数:" + articleDatas.getTotalPages());
		System.out.println("查询总记录数:" + articleDatas.getTotalElements());
		System.out.println("查询当前第几页:" + articleDatas.getNumber());
		System.out.println("查询当前页面的记录数:" + articleDatas.getNumberOfElements());

		// 查询出的结果数据集合
		List<FKUser> articles = articleDatas.getContent();
		System.out.println("查询当前页面的集合:" + articles);
		model.addAttribute("list", articles);
		model.addAttribute("TotalPages", articleDatas.getTotalPages());
		model.addAttribute("TotalElements", articleDatas.getTotalElements());
		model.addAttribute("Numbe", articleDatas.getNumber() + 1);
		model.addAttribute("NumberOfElements", articleDatas.getNumberOfElements());
		model.addAttribute("temp", page);
		List<FKUser> fKUser =  articles;
		List<Map<String, Object>>  re= new ArrayList<>(); 
		// 遍历查询出的学生对象，提取姓名，年龄，性别信息
		for(FKUser stu :fKUser){
			String authority=new String();
			Map<String , Object> stuMap = new HashMap<>(); 
			stuMap.put("id", stu.getId());
			stuMap.put("username", stu.getUsername());
			if(stu.getRoles()!= null && stu.getRoles().size()!=0)
			{
			   authority=stu.getRoles().get(0).getAuthority();
			   }
	        stuMap.put("authority",authority);
			re.add(stuMap);
		}
	
		model.addAttribute("users", re);
     	return "superAdmin/userInfo";
	}
	
	
	// 分页查询
	@RequestMapping("/showPageAdminUser")
	public String showPageAdminUser(Model model, @RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "totalPage", defaultValue = "1") int totalPage,
			@RequestParam(value = "size", defaultValue = "9") int size) {
		if (page == -1) 
		{ page++; } 
		if (page == totalPage) 
		{ page--; }
		
		// 指定排序参数对象：根据id，进行降序查询
		Sort sort = new Sort(Sort.Direction.DESC, "id");
		/**
		 * 封装分页实体 参数一：pageIndex表示当前查询的第几页(默认从0开始，0表示第一页) 参数二：表示每页展示多少数据，现在设置每页展示2条数据
		 * 参数三：封装排序对象，根据该对象的参数指定根据id降序查询
		 */
		@SuppressWarnings("deprecation")
		Pageable pageable = new PageRequest(page, size, sort);
		Page<FKUser> articleDatas = userService.findAll(pageable);
		System.out.println("查询总页数:" + articleDatas.getTotalPages());
		System.out.println("查询总记录数:" + articleDatas.getTotalElements());
		System.out.println("查询当前第几页:" + articleDatas.getNumber());
		System.out.println("查询当前页面的记录数:" + articleDatas.getNumberOfElements());

		// 查询出的结果数据集合
		List<FKUser> articles = articleDatas.getContent();
		System.out.println("查询当前页面的集合:" + articles);
		model.addAttribute("list", articles);
		model.addAttribute("TotalPages", articleDatas.getTotalPages());
		model.addAttribute("TotalElements", articleDatas.getTotalElements());
		model.addAttribute("Numbe", articleDatas.getNumber() + 1);
		model.addAttribute("NumberOfElements", articleDatas.getNumberOfElements());
		model.addAttribute("temp", page);
		List<FKUser> fKUser =  articles;
		List<Map<String, Object>>  re= new ArrayList<>(); 
		// 遍历查询出的学生对象，提取姓名，年龄，性别信息
		for(FKUser stu :fKUser){
			String authority=new String();
			Map<String , Object> stuMap = new HashMap<>(); 
			stuMap.put("id", stu.getId());
			stuMap.put("username", stu.getUsername());
			if(stu.getRoles()!= null && stu.getRoles().size()!=0)
			{
			   authority=stu.getRoles().get(0).getAuthority();
			   }
	        stuMap.put("authority",authority);
			re.add(stuMap);
		}
	
		model.addAttribute("users", re);
     	return "superAdmin/userInfo";
	}
	//批量删除
	@RequestMapping("/deleteUser")
	public String delete(@RequestParam("id") String id){
	    // 接收包含stuId的字符串，并将它分割成字符串数组
	    String[] idList = id.split(",");
	    // 将字符串数组转为List<Intger> 类型
	    List<Integer> LString = new ArrayList<Integer>();
	    for(String str : idList){
	        LString.add(new Integer(str));
	    }
	    userService.deleteBatch(LString);
	    return "redirect:/superAdmin/showPageUser";
	}
	
	//绑定学生信息
	@RequestMapping("/addStudentId")
	@ResponseBody
	public String addStudentId(@RequestParam("sno") String sno, @RequestParam("name") String name,FKUser fkUser) {
		Student stu=studentService.getStudnetIdSnoName(sno);
		if(name.equals(stu.getName())) {		
		if (stu!=null) {
			int id=stu.getId();
	    	Student st =new Student();
	    	st.setId(id);
	    	String str =getUsername();  	
	    	fkUser=userService.findByName(str);
	    	List<Student> stus =Arrays.asList(st);
	    	int id1=1;
	    	FKUser user =new FKUser();
	    	FKRole role =new FKRole();
	    	role.setId(id1);
	    	user.setId(fkUser.getId());
	    	user.setPassword(fkUser.getPassword());
	    	user.setUsername(fkUser.getUsername());
	    	user.setAnswer(fkUser.getAnswer());
	    	List<FKRole> roles =Arrays.asList(role);
	    	user.setStudents(stus);
	    	user.setRoles(roles);
	    	if(userService.saveAll(user)!=null) {
	    		return "1";
	    	}
	    	else{
	    		return "2";
	    		}
			
			}
			
		}
			return "0";
	}
    @RequestMapping("/AdminInfo")

    public String toAdminInfo(){
        return "superAdmin/adminInfo";
    }
	
    @RequestMapping("/queryAdminInfo")
    @ResponseBody
    public Map<String , Object> queryAdminInfo(@RequestParam(value = "page", defaultValue = "1") int page,@RequestParam(value = "rows", defaultValue = "2") int rows){
        return userService.queryAdminInfo(page,rows);
    }
	
	//绑定宿舍楼信息
	@RequestMapping("/addBuildingId")
	public String addBuildingId(@RequestParam("bid") String id,@RequestParam("uid") String id2) {
    	int bid=Integer.parseInt(String.valueOf(id));
    	int uid=Integer.parseInt(String.valueOf(id2));
    	FKUser user1 = userService.getUserById(uid); 
    	FKUser user =new FKUser();
    	Building bu =new Building();
    	bu.setBuildingId(bid);;

    	user.setId(user1.getId());
    	user.setPassword(user1.getPassword());
    	user.setUsername(user1.getUsername());
    	List<Building> buildings =Arrays.asList(bu);
    	FKRole role =new FKRole();
    	int rid=3;
    	role.setId(rid);
    	List<FKRole> roles =Arrays.asList(role);
    	user.setRoles(roles);
    	user.setBuildings(buildings);
    	userService.saveAll(user);
    	return "redirect:/superAdmin/AdminInfo";
	
	}
    //检查用户是否绑定学生信息且为学生权限
    @RequestMapping("/checkerUser")
    @ResponseBody
    public String checkerUser() {
    

    	//获取登录名
    	String name =getUsername();  	
    	String aut =getAuthority();
    	FKUser user=userService.findByName(name);
        //未绑定信息且为学生
    	if(user.getStudents().size()!=0 && aut.equals("[ROLE_STUDENT]")) {
    		return "1";
    	}
    	else if(aut.equals("[ROLE_ADMIN]")) {
    		return "2";
    	}    	
    	else if(aut.equals("[ROLE_SUPERADMIN]")) {
    		return "2";
    	}else {
    		return"0";
    	}
    	
    }

	   /**
	    * 获得当前用户名称
	    * */
	    private String getUsername(){
			String username = SecurityContextHolder.getContext().getAuthentication().getName();			
			return username;
		}
	     /**
	      * 获得当前用户权限
	      * */
	     private String getAuthority(){
	 		// 获得Authentication对象，表示用户认证信息。
	 		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	 		List<String> roles = new ArrayList<String>();
	 		// 将角色名称添加到List集合
	 		for (GrantedAuthority a : authentication.getAuthorities()) {
	 			roles.add(a.getAuthority());
	 		}
	 		return roles.toString();
	 	}
}
