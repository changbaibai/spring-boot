package dms.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import dms.pojo.Dorm;
import dms.pojo.Student;
import dms.repository.DormRepository;
import dms.repository.StudentRepository;



@Service
public class StudentService {
	
	// 注入数据访问层接口对象 
	@Resource
	private StudentRepository studentRepository;
	@Resource
	private DormRepository dormInfoRepository;
	
	@Transactional
    public List<Student> getStudentList() {
        return studentRepository.findAll();
    }

	public void saveAll(Student student) {
		studentRepository.save(student);
	}

	public void saveDormInfoAll(Dorm dormInfo) {
		dormInfoRepository.save(dormInfo);
	}
    public List<Dorm> getDormInfoList() {
        return dormInfoRepository.findAll();
    }
	public void saveStudentAll(Student student) {
		studentRepository.save(student);
	}
	public void deleteDormInfo(int id) {
		dormInfoRepository.deleteById(id);
		// TODO Auto-generated method stub
		
	}
	public void deleteStudent(int id) {
		studentRepository.deleteById(id);
		// TODO Auto-generated method stub
		
	}
	/**
	 * 动态查询学生信息 ：可以根据学生对象的姓名(模糊匹配)， 地址查询(模糊匹配),性别,班级查询学生信息 
	 *               如果没有传输参数,默认查询所有的学生信息
	 * @param 
	 * @return
	 */
	@SuppressWarnings("serial")
	public List<Map<String, Object>> getStusByDynamic(Student student) {
		List<Student> students = studentRepository.findAll(new Specification<Student>() {
			@Override
			public Predicate toPredicate(Root<Student> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				// 本集合用于封装查询条件
				List<Predicate> predicates = new ArrayList<Predicate>();  
				if(student!=null){
					/** 是否传入了姓名来查询  */
					if(!StringUtils.isEmpty(student.getName())){
						predicates.add(cb.like(root.<String> get("name"),"%" + student.getName() + "%"));
					}
					/** 是否传入了地址来查询  */
					if(!StringUtils.isEmpty(student.getAddress())){
						predicates.add(cb.like(root.<String> get("address"),"%" + student.getAddress() + "%"));
					}
					/** 是否传入了学号来查询 */
					if(student.getSno() != null){
						predicates.add(cb.equal(root.<String> get("sno"),student.getSno()));
					}
					/** 判断是否传入了宿舍名来查询 */
					if(student.getDorm()!=null && !StringUtils.isEmpty(student.getDorm().getName())){
						root.join("DormInfo", JoinType.INNER);
						Path<String> dormInfoName = root.get("DormInfo").get("name");
						predicates.add(cb.equal(dormInfoName, student.getDorm().getName()));
					}
				}
				return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
			}
		});
		List<Map<String, Object>>  results = new ArrayList<>(); 
		// 遍历查询出的学生对象，提取姓名，年龄，性别信息
		for(Student stu :students){
			Map<String , Object> stuMap = new HashMap<>(); 
			stuMap.put("id", stu.getId());
			stuMap.put("name", stu.getName());
			stuMap.put("address", stu.getAddress());
			stuMap.put("sno", stu.getSno());
			stuMap.put("phone", stu.getPhone());
			stuMap.put("dormInfoCode", stu.getDorm().getCode());
			stuMap.put("dormInfoName", stu.getDorm().getName());
			stuMap.put("dormInfoType", stu.getDorm().getType());
			results.add(stuMap);
		}
		return results;
	}
    public Dorm  findDormInfoByCode(int code) {
        return dormInfoRepository.findByCode(code);
    }

	public Dorm findDormInfoByCode(String name) {
		// TODO Auto-generated method stub
		return dormInfoRepository.findByName(name);
	}

}
