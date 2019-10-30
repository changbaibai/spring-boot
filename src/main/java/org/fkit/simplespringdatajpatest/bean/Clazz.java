package org.fkit.simplespringdatajpatest.bean;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="tb_clazz")
public class Clazz implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int code ;
	private String name ;
	// 班级与学生是一对多的关联
	@OneToMany(
			   fetch=FetchType.LAZY,
			   targetEntity=Student.class,
			   mappedBy="clazz"
			)     
	private Set<Student> students = new HashSet<>();
	
	public Clazz() {
		
	}
	// 班级对象
	public Clazz(String name) {
		this.name = name;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Set<Student> getStudents() {
		return students;
	}
	public void setStudents(Set<Student> students) {
		this.students = students;
	}
}
