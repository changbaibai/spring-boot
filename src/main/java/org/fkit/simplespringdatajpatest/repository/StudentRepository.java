package org.fkit.simplespringdatajpatest.repository;

import java.util.List;

import org.fkit.simplespringdatajpatest.bean.Student;
import org.springframework.data.jpa.repository.JpaRepository;



/**
 * @Author dlei(徐磊)
 * @Email dlei0009@163.com
 * @Date 2017年11月4日上午9:03:06
 * @From http://www.fkjava.org 疯狂软件
 * @Version 1.0
 * 
 */
public interface StudentRepository extends JpaRepository<Student, Integer> {

	/**
	 * 通过学生姓名来查询学生对象
	 * 此方法相当于JPQL语句代码:select s from Student s where s.name = ?1
	 * @param name 参数
	 * @return Student对象
	 */
	Student findByName(String name);
	
	/**
	 * 通过名字和地址查询学生信息
	 * 此方法相当于JPQL语句代码:select s from Student s where s.name = ?1 and s.address=?2
	 * @param name
	 * @param address
	 * @return 包含Student对象的List集合
	 */
	List<Student> findByNameAndAddress(String name , String address);
	
	/**
	 * 通过学生姓名模糊查询学生信息 
	 * 此方法相当于JPQL语句代码:select s from Student s where s.name like ?1
	 * @param name 参数
	 * @return 包含Student对象的List集合
	 */
	List<Student> findByNameLike(String name);
	
	
	Student findById(int id);
	
}
