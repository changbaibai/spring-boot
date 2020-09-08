package dms.controller;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

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
import org.springframework.web.multipart.MultipartFile;

import dms.pojo.Clazz;
import dms.pojo.Dorm;
import dms.pojo.FKRole;
import dms.pojo.FKUser;
import dms.pojo.LeaveSchool;
import dms.pojo.Student;
import dms.service.StudentService;
import dms.service.UserService;

@Controller
@RequestMapping("/superAdmin")
public class StudentController {
	// 注入studentService
	@Resource
	private StudentService studentService;
	@Resource
	private UserService userService;

    @RequestMapping("/addStudent")
    public String add(Student student,Dorm dorm,Clazz clazz) {
    	student.setDorm(dorm);
    	student.setClazz(clazz);
    	studentService.saveAll(student);
    	return "redirect:/superAdmin/showPageStudent";
    }
	//批量删除
	@RequestMapping("/deleteStudent")
	public String delete(@RequestParam("id") String id){
	    // 接收包含stuId的字符串，并将它分割成字符串数组
	    String[] idList = id.split(",");
	    // 将字符串数组转为List<Intger> 类型
	    List<Integer> LString = new ArrayList<Integer>();
	    for(String str : idList){
	        LString.add(new Integer(str));
	    }
	    studentService.deleteBatch(LString);
	    return "redirect:/superAdmin/showPageStudent";
	}
	//修改
	@RequestMapping("/editStudent")
	public String edit(Student student) {
		studentService.edit(student);
		return "redirect:/superAdmin/showPageStudent";
	}
	
	// 分页查询
	@RequestMapping("/showPageStudent")
	public String showPageStudent(Model model, @RequestParam(value = "page", defaultValue = "1") int page,
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
		@SuppressWarnings("deprecation")
		Pageable pageable = new PageRequest(page, size, sort);
		Page<Student> articleDatas = studentService.findAll(pageable);
		System.out.println("查询总页数:" + articleDatas.getTotalPages());
		System.out.println("查询总记录数:" + articleDatas.getTotalElements());
		System.out.println("查询当前第几页:" + articleDatas.getNumber());
		System.out.println("查询当前页面的记录数:" + articleDatas.getNumberOfElements());
		// 查询出的结果数据集合
		List<Student> articles = articleDatas.getContent();
		System.out.println("查询当前页面的集合:" + articles);
		model.addAttribute("list", articles);
		model.addAttribute("TotalPages", articleDatas.getTotalPages());
		model.addAttribute("TotalElements", articleDatas.getTotalElements());
		model.addAttribute("Numbe", articleDatas.getNumber() + 1);
		model.addAttribute("NumberOfElements", articleDatas.getNumberOfElements());
		model.addAttribute("temp", page);
		
		List<Map<String, Object>>  results = new ArrayList<>(); 
		// 遍历查询出的学生对象，提取姓名，年龄，性别信息
		for(Student stu :articles){
			Map<String , Object> stuMap = new HashMap<>(); 
			stuMap.put("studentId", stu.getId());
			stuMap.put("studentSno", stu.getSno());
			stuMap.put("studentName", stu.getName());
			stuMap.put("studentAddress", stu.getAddress());
			stuMap.put("studentSno", stu.getSno());
			stuMap.put("studentSex", stu.getSex());
			stuMap.put("studentPhone", stu.getPhone());
			if(stu.getDorm()!=null) {
				stuMap.put("dormCode", stu.getDorm().getCode());
				stuMap.put("dormName", stu.getDorm().getName());
				stuMap.put("dormType", stu.getDorm().getType());
				if(stu.getDorm().getBuilding()!=null) {
					stuMap.put("buildingName", stu.getDorm().getBuilding().getName());
					stuMap.put("buildingBuildings", stu.getDorm().getBuilding().getBuildings());
					stuMap.put("buildingType", stu.getDorm().getBuilding().getBuildingType());
				}
			}else {
				stuMap.put("dormCode","暂无");
				stuMap.put("dormName","暂无");
				stuMap.put("dormType", "暂无");
				stuMap.put("buildingName", "暂无");
				stuMap.put("buildingBuildings", "暂无");
				stuMap.put("buildingType", "暂无");
				
			}
			if(stu.getClazz()!=null) {
				stuMap.put("clazzId", stu.getClazz().getClazzId());
				stuMap.put("clazzName", stu.getClazz().getName());
				if(stu.getClazz().getSpecialities()!=null) {
					stuMap.put("specialitiesName", stu.getClazz().getSpecialities().getName());
					if(stu.getClazz().getSpecialities().getCollege()!=null) {
						stuMap.put("collegeName", stu.getClazz().getSpecialities().getCollege().getName());
					}
				}
				
			}else {
				stuMap.put("clazzId","暂无");
				stuMap.put("clazzName", "暂无");
				stuMap.put("collegeName", "暂无");
				stuMap.put("specialitiesName","暂无");
			}
			results.add(stuMap);
		}
		System.out.println("啊啊啊啊啊啊啊啊啊啊啊" +results);
		
		model.addAttribute("students", results);
     	return "superAdmin/studentInfo";
	}
	

	@RequestMapping(value = "/autoImportStudent")
	public String exImport(@RequestParam(value = "filename") MultipartFile file, HttpSession session,@RequestParam(value = "bid",required=false)String bid) {

		@SuppressWarnings("unused")
		boolean a = false;

		String fileName = file.getOriginalFilename();

		try {
			a = studentService.batchImport(fileName, file ,bid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/superAdmin/showPageStudent";
	}
	
	
    //检查用户是否完成学生信息小问卷调查
    @RequestMapping("/checkerQuestionnaire")
    @ResponseBody
    public String checkerQuestionnaire() {
    

    	//获取登录名
    	String name =getUsername();  	
    	String aut =getAuthority();
    	FKUser user=userService.findByName(name);
        //未绑定信息且为学生
    	if(user.getStudents().size()!=0 && aut.equals("[ROLE_STUDENT]")) {
    		if(user.getStudents().get(0).getType()==null) {
    			return "0";
    		}
    	}
    return "1";
		
    
    }
    
    //检查用户是否完成学生信息小问卷调查
    @RequestMapping("/addQuestionnaire")
    @ResponseBody
    public String addcheckerQuestionnaire(@RequestParam(value = "tid",required=false)String tid) {
    	//获取登录名
    	String name =getUsername();  	
    	String aut =getAuthority();
    	FKUser user=userService.findByName(name);
        //未绑定信息且为学生
    	if(user.getStudents().size()!=0 && aut.equals("[ROLE_STUDENT]")) {
    		if(user.getStudents().get(0).getId()!=0) {
    			
    			int id =user.getStudents().get(0).getId();
    			Student stu=studentService.findStudentByIds(id);
    			int studentId=stu.getId();
    			int clazzId=stu.getClazz().getClazzId();
    			String s="";
    			String sex=stu.getSex();
    			if (sex.equals("男")) {
    				s="1";
    			}else {
    				s="0";
    			}
    			String sid= Integer.toString(studentId);
    			String cid= Integer.toString(clazzId);
    			String type=sid+","+cid+","+s+","+tid;      //学生编号，班级编号，性别，爱好
    			stu.setType(type);
    			Student student=studentService.saveAll(stu);
    			if(student !=null) {
    				return "1";
    			}
    			
    		}
    	}
    return "0";
		
    
    }
    
	
    //自动分配
    @RequestMapping("/autoAssign")
    @ResponseBody
    public String autoAssign() {
    	studentService.autoAssign();
         return "1";	
    
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
