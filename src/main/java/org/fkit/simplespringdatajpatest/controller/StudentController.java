package org.fkit.simplespringdatajpatest.controller;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;

import org.fkit.simplespringdatajpatest.bean.Student;
import org.fkit.simplespringdatajpatest.service.StudentService;
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

	
	@RequestMapping("/name")
	public Student getByName(String name) {
		return studentService.getStuByName(name);
	}
	
	@RequestMapping("/nameAndAddress")
	public List<Student> getByNameAndAddress(String name,String address) {
		return studentService.getStusByNameAndAddress(name, address);
	}
	
	@RequestMapping("/nameLike")
	public List<Student> getByNameLile(String name) {
		return studentService.getStusByNameLike(name);
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
	
}
