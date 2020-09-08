package dms.controller;



import java.util.ArrayList;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import dms.pojo.Student;
import dms.pojo.FKUser;
import dms.pojo.LeaveSchool;

import dms.service.LeaveSchoolService;
import dms.service.UserService;


@Controller
@RequestMapping("/superAdmin")
public class LeaveSchoolController {
	// 注入leaveSchoolService
	@Resource
	private LeaveSchoolService leaveSchoolService;
	
	@Resource
	private UserService userService;
    //离校签到
    @RequestMapping("/addLeaveSchool")
    @ResponseBody
    public String addLeaveSchool(Student student,LeaveSchool leaveSchool,@RequestParam(value = "des",required=false)String des,@RequestParam(value = "cau",required=false)String cau
    		,@RequestParam(value = "pn",required=false)String pn,@RequestParam(value = "ph",required=false)String ph) {
    	//获取登录名
    	String name =getUsername();  	
    	FKUser user=userService.findByName(name);
		int id =user.getStudents().get(0).getId();
		student.setId(id);
    	leaveSchool.setStudent(student);
    	leaveSchool.setDestination(des);
    	leaveSchool.setCause(cau);
    	leaveSchool.setParentsName(pn);
    	leaveSchool.setParentsPhone(ph);
    	Date time = new Date();
    	leaveSchool.setLeavingTime(time);
    	LeaveSchool lea=leaveSchoolService.saveAll(leaveSchool);
    	if (lea != null) {
    		return "1";
    	}
    	else {
    		return "0";
    	}
    }

    @RequestMapping("/queryUerLeaveSchoolInfo")
    @ResponseBody
    public Map<String , Object> queryUerLeaveSchoolInfo(@RequestParam(value = "page", defaultValue = "1") int page,@RequestParam(value = "rows", defaultValue = "2") int rows){
        return leaveSchoolService.queryUerLeaveSchoolInfo(page,rows);
    }

	// 分页查询
	@RequestMapping("/getShowPage")
	public Map<String, Object> getShowPage(@RequestParam(value = "pageNumber", defaultValue = "1") int page,
			@RequestParam(value = "totalPage", defaultValue = "1") int totalPage,
			@RequestParam(value = "pageSize", defaultValue = "5") int size){
		if (page == -1) 
		{ page++; } 
		if (page == totalPage) 
		{ page--; }
    	String name =getUsername();  	
    	FKUser user=userService.findByName(name);
    	int id =user.getStudents().get(0).getId();
    	
		// 指定排序参数对象：根据id，进行降序查询
		Sort sort = new Sort(Sort.Direction.DESC, "id");
		/**
		 * 封装分页实体 参数一：pageIndex表示当前查询的第几页(默认从0开始，0表示第一页) 参数二：表示每页展示多少数据，现在设置每页展示2条数据
		 * 参数三：封装排序对象，根据该对象的参数指定根据id降序查询
		 */


		/**
		 * 封装分页实体 参数一：pageIndex表示当前查询的第几页(默认从0开始，0表示第一页) 参数二：表示每页展示多少数据，现在设置每页展示2条数据
		 * 参数三：封装排序对象，根据该对象的参数指定根据id降序查询
		 */
		@SuppressWarnings("deprecation")
		Pageable pageable = new PageRequest(page, size, sort);
		Page<LeaveSchool> articleDatas = leaveSchoolService.findByStudentId(id,pageable);
		System.out.println("查询总页数:" + articleDatas.getTotalPages());
		System.out.println("查询总记录数:" + articleDatas.getTotalElements());
		System.out.println("查询当前第几页:" + articleDatas.getNumber());
		System.out.println("查询当前页面的记录数:" + articleDatas.getNumberOfElements());
		// 查询出的结果数据集合
		List<LeaveSchool> articles = articleDatas.getContent();
		System.out.println("查询当前页面的集合:" + articles);
	    Map<String, Object> map = new HashMap<String, Object>();
	    map.put("total", articleDatas.getTotalElements());//返回列表总条数
	    map.put("rows",articleDatas.getContent().toString());//数据
	    return map;
	}
 
    //返校签到
    @RequestMapping("/updateLeaveSchool")
    @ResponseBody
    public String updateLeaveSchool(LeaveSchool leaveSchool,@RequestParam(value = "id",required=false) int id) {
    	if(id!=0) {
        	leaveSchool = leaveSchoolService.findById(id);
        	System.out.println("返校时间"+leaveSchool.getReturnTime());
        	if (leaveSchool.getReturnTime()!=null)
        	{
        		return "2";
        		}
        	else {
        		Date time = new Date();
        		leaveSchool.setReturnTime(time);
        		LeaveSchool lea=leaveSchoolService.saveAll(leaveSchool);
        		if(lea!=null) {
        			return "1";
        			
        		}
        		else {
        			return "0";
        		}
        	}

    		
    	}
    	else {
    		return "0";
    	}
    }
	//批量删除
	@RequestMapping("/deleteLeaveSchool")
	public String delete(@RequestParam("id") String id){
	    // 接收包含stuId的字符串，并将它分割成字符串数组
	    String[] idList = id.split(",");
	    // 将字符串数组转为List<Intger> 类型
	    List<Integer> LString = new ArrayList<Integer>();
	    for(String str : idList){
	        LString.add(new Integer(str));
	    }
	    leaveSchoolService.deleteBatch(LString);
	    return null;
	}
	//修改
	@RequestMapping("/editLeaveSchool")
	public String edit(LeaveSchool leaveSchool) {
		leaveSchoolService.edit(leaveSchool);
		return "redirect:/superAdmin/showPageLeaveSchool";
	}
	// 分页查询
	@RequestMapping("/showPageLeaveSchool")
	public String showPageLeaveSchool(Model model, @RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "totalPage", defaultValue = "1") int totalPage,
			@RequestParam(value = "size", defaultValue = "4") int size) {
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
		System.out.println("BB啦啦啦啦" + totalPage);
		@SuppressWarnings("deprecation")
		Pageable pageable = new PageRequest(page, size, sort);
		Page<LeaveSchool> articleDatas = leaveSchoolService.findAll(pageable);
		System.out.println("查询总页数:" + articleDatas.getTotalPages());
		System.out.println("查询总记录数:" + articleDatas.getTotalElements());
		System.out.println("查询当前第几页:" + articleDatas.getNumber());
		System.out.println("查询当前页面的记录数:" + articleDatas.getNumberOfElements());
		// 查询出的结果数据集合
		List<LeaveSchool> articles = articleDatas.getContent();
		System.out.println("查询当前页面的集合:" + articles);
		model.addAttribute("TotalPages", articleDatas.getTotalPages());
		model.addAttribute("TotalElements", articleDatas.getTotalElements());
		model.addAttribute("Numbe", articleDatas.getNumber()+1);
		model.addAttribute("NumberOfElements", articleDatas.getNumberOfElements());
		model.addAttribute("leaveSchools", articles);
		model.addAttribute("temp", page);
     	return "superAdmin/leaveSchoolInfo";
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
