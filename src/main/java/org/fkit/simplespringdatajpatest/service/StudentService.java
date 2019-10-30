package org.fkit.simplespringdatajpatest.service;

import java.util.List;


import javax.annotation.Resource;

import org.fkit.simplespringdatajpatest.bean.Student;
import org.fkit.simplespringdatajpatest.repository.StudentRepository;
import org.fkit.simplespringdatajpatest.repository.ClazzRepository;
import org.fkit.simplespringdatajpatest.bean.Clazz;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
public class StudentService {
	
	// 注入数据访问层接口对象 
	@Resource
	private StudentRepository studentRepository;
	@Resource
	private ClazzRepository clazzRepository;
	
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
    
	public List<Student> getStusByClazzName(String clazzName) {
		return studentRepository.findStudentsByClazzName(clazzName);
	}
	public void saveClazzAll(Clazz clazz) {
		clazzRepository.save(clazz);
	}
    public List<Clazz> getClazzList() {
        return clazzRepository.findAll();
    }
	public void saveStudentAll(Student student) {
		studentRepository.save(student);
	}
    public Clazz findClazzByCode(int id) {
        return clazzRepository.findById(id);
    }
	public void deleteClazz(int id) {
		clazzRepository.deleteById(id);
		// TODO Auto-generated method stub
		
	}

}
