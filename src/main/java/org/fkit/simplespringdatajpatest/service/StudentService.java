package org.fkit.simplespringdatajpatest.service;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.fkit.simplespringdatajpatest.bean.Student;
import org.fkit.simplespringdatajpatest.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
public class StudentService {
	
	// 注入数据访问层接口对象 
	@Resource
	private StudentRepository studentRepository;
	
	@Transactional
    public List<Student> getStudentList() {
        return studentRepository.findAll();
    }
	public void saveAll(Student student) {
		studentRepository.save(student);
	}

	public Student getStuByName(String name) {
		return studentRepository.findByName(name);
	}
    public Student findStuById(int id) {
        return studentRepository.findById(id);
    }
	
	public List<Student> getStusByNameAndAddress(String name,String address) {
		return studentRepository.findByNameAndAddress(name,address);
	}
	
	public List<Student> getStusByNameLike(String name) {
		return studentRepository.findByNameLike("%"+name+"%");
	}
	public void delete(int id) {
		studentRepository.deleteById(id);
		
	}
    public void edit(Student student) {
        studentRepository.save(student);
    }


}
