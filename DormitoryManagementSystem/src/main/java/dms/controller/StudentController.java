package dms.controller;



import java.util.List;
import java.util.Map;

import javax.annotation.Resource;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import dms.pojo.Dorm;
import dms.pojo.Student;
import dms.service.StudentService;





@Controller
@RequestMapping("/admin")
public class StudentController {
	
	// 注入StudentService
	@Resource
	private StudentService studentService;
    @RequestMapping("/")
    public String index() {
        return "redirect:/admin/studentList";
    }

    @RequestMapping("/listStudent")
    public String list(Model model) {
        List<Student> student = studentService.getStudentList();   
        model.addAttribute("students", student);
        return "admin/studentList";
    }

	@RequestMapping("/deleteStudent")
	public String delete(int id) {
	
		studentService.deleteStudent(id);
	    return "redirect:/admin/getStusByDynamic";
		}
	 
    @RequestMapping("/toAddStudent")
    public String toAdd() {
        return "add";
    }
	  @RequestMapping("/toEditStudent") public String toEdit(String sno,Model model) 
	  {
		  Student student = new Student();
		  student.setSno(sno);
	      List<Map<String, Object>> list = studentService.getStusByDynamic(student);
	        for( @SuppressWarnings("rawtypes") Map map1: list){
	        	model.addAttribute("id", map1.get("id"));
	        	model.addAttribute("name", map1.get("name"));
	        	model.addAttribute("address", map1.get("address"));
	        	model.addAttribute("sno", map1.get("sno"));
	        	model.addAttribute("phone", map1.get("phone"));
	        	model.addAttribute("code", map1.get("dormInfoCode"));
	            
	        }

		  return "admin/editStudent"; 
	  }

    @RequestMapping("/addStudent")
    public String add(Student student,Dorm dorm) {
    	
    	student.setDorm(dorm);
    	studentService.saveAll(student);
        return "redirect:/admin/getStusByDynamic";
    }



    @RequestMapping("/addDormInfo")
    public String add(Dorm dormInfo) {
    	studentService.saveDormInfoAll(dormInfo);
        return "redirect:/admin/listDormInfo";
    }
    @RequestMapping("/listDormInfo")
    public String listDormInfo(Model model) {
        List<Dorm> dormInfo = studentService.getDormInfoList();
        model.addAttribute("dormInfo", dormInfo);
        return "admin/dormInfoList";
    }


    @RequestMapping("/toEditDormInfo")
	    public String toEditClazz(Model model, int id) {
	    	Dorm dormInfo= studentService.findDormInfoByCode(id);
	        model.addAttribute("dormInfo",dormInfo);
	        return "admin/editDormInfo"; 
	    }
    @RequestMapping("/getDorm")
    public String getByName(Model model,Dorm dormInfo) {
    	String name =dormInfo.getName();
    	Dorm dormInfo1= studentService.findDormInfoByCode(name);
        model.addAttribute("dormInfo",dormInfo1);
        return "admin/dormInfoList"; 
    }
	 
    @RequestMapping("/editDormInfo")
    public String editDormInfo(Dorm dormInfo) {
        studentService.saveDormInfoAll(dormInfo);
        return "redirect:/admin/listDormInfo";
    }
    @RequestMapping("/deleteDormInfo")
    public String deleteDormInfo(int id) {
        studentService.deleteDormInfo(id);
        return "redirect:/admin/listDormInfo";
    }
    

	// 动态的查询学生信息
	// 可以根据学生对象的姓名(模糊匹配)， 地址查询(模糊匹配),性别,班级查询学生信息 
	@RequestMapping("/getStusByDynamic")
	public String getStusByDynamic(Student student,Model model) {
		
        List<Map<String, Object>> students = studentService.getStusByDynamic(student);
		model.addAttribute("students", students);
        return "admin/studentList";
	} 
	
}
