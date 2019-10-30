package org.fkit.simplespringdatajpatest.controller;


import java.util.List;

import javax.annotation.Resource;

import org.fkit.simplespringdatajpatest.bean.Student;
import org.fkit.simplespringdatajpatest.service.StudentService;
import org.fkit.simplespringdatajpatest.bean.Clazz;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;





@Controller
public class StudentController {
	
	// 注入StudentService
	@Resource
	private StudentService studentService;
    @RequestMapping("/")
    public String index() {
        return "redirect:/list";
    }

    @RequestMapping("/list")
    public String list(Model model) {
        List<Student> students = studentService.getStudentList();
        model.addAttribute("students", students);
        return "list";
    }

    @RequestMapping("/delete")
    public String delete(int id) {
        studentService.delete(id);
        return "redirect:/list";
    }

    @RequestMapping("/toEdit")
    public String toEdit(Model model, int id) {
        Student student = studentService.findStuById(id);
        model.addAttribute("student", student);
        return "edit";
    }

    @RequestMapping("/edit")
    public String edit(Student student) {
        studentService.edit(student);
        return "redirect:/list";
    }
    @RequestMapping("/toAdd")
    public String toAdd() {
        return "add";
    }

    @RequestMapping("/add")
    public String add(Student student) {
    	studentService.saveAll(student);
        return "redirect:/list";
    }
    

    @RequestMapping("/getClazzStus")
    public String list(Model model,String clazzName) {
    	List<Student> students = studentService.getStusByClazzName(clazzName);
        model.addAttribute("students", students);
        return "list";
    }
	@RequestMapping("/saveStudent")
	public String save(Student student) {
		
		Clazz clazz1 = new Clazz("疯狂java开发1班");
		student.setClazz(clazz1);
		studentService.saveStudentAll(student);
		return "保存学生对象成功";
	}
	
    @RequestMapping("/toAddClazz")
    public String toAddClazz() {
        return "addclazz";
    }

    @RequestMapping("/addClazz")
    public String add(Clazz clazz) {
    	studentService.saveClazzAll(clazz);
        return "redirect:/listClazz";
    }
    @RequestMapping("/listClazz")
    public String listClazz(Model model) {
        List<Clazz> clazz = studentService.getClazzList();
        model.addAttribute("clazz", clazz);
        return "listClazz";
    }
    @RequestMapping("/toEditClazz")
    public String toEditClazz(Model model, int id) {
        Clazz clazz = studentService.findClazzByCode(id);
        model.addAttribute("clazz",clazz);
        return "editClazz";
    }

    @RequestMapping("/editClazz")
    public String editClazz(Clazz clazz) {
        studentService.saveClazzAll(clazz);
        return "redirect:/listClazz";
    }
    @RequestMapping("/deleteClazz")
    public String deleteClazz(int id) {
        studentService.deleteClazz(id);
        return "redirect:/listClazz";
    }
	
}
