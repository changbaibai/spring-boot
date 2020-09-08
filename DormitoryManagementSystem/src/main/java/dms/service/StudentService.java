package dms.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import dms.Exception.MyException;
import dms.pojo.Clazz;
import dms.pojo.DC;
import dms.pojo.Dorm;
import dms.pojo.FKRole;
import dms.pojo.FKUser;
import dms.pojo.Student;
import dms.pojo.TimeSheet;
import dms.repository.DormRepository;
import dms.repository.StudentRepository;
import dms.repository.UserRepository;

@Service
public class StudentService {

	// 注入数据访问层接口对象
	@Resource
	private StudentRepository studentRepository;

	// 注入持久层接口UserRepository
	@Autowired
    UserRepository userRepository;
	
	@Resource
	private DormRepository dormRepository;
	
	

	public Student saveAll(Student student) {
		return studentRepository.save(student);
	}


	public List<Student> getStusByNameLike(String name) {
		return studentRepository.findByNameLike("%" + name + "%");
	}
	public Student getStuByName(String name) {
		return studentRepository.findByName(name);
	}
	public void delete(int id) {
		studentRepository.deleteById(id);

	}

	public void edit(Student student) {
		studentRepository.save(student);
	}
	public Page<Student> findAll(Pageable page) {
		return studentRepository.findAll(page);
	}
	
	public Iterable<Student> findAllSort(Sort sort) {
		return studentRepository.findAll(sort);
	}
	public List<Student> getStudentList() {
		return (List<Student>) studentRepository.findAll();
	}
	@SuppressWarnings("serial")
	public List<Map<String, Object>> getStusByDynamic(Student student) {
		List<Student> Students = studentRepository.findAll(new Specification<Student>() {
			@Override
			public Predicate toPredicate(Root<Student> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				// 本集合用于封装查询条件
				List<Predicate> predicates = new ArrayList<Predicate>();  
				if(student!=null){
					/** 是否传入了名称来查询  */
					if(!StringUtils.isEmpty(student.getName())){
						predicates.add(cb.like(root.<String> get("name"),"%" + student.getName() + "%"));
					}
					/** 是否传入了id来查询 */
					if(student.getId() != 0){
						predicates.add(cb.equal(root.<String> get("id"),student.getId()));
					}

				}
				return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
			}
		});
		List<Map<String, Object>>  results = new ArrayList<>(); 
		// 遍历查询出的学生对象，提取姓名，年龄，性别信息
		for(Student spe :Students){
			Map<String , Object> speMap = new HashMap<>(); 
			speMap.put("id", spe.getId());
			speMap.put("name", spe.getName());
			results.add(speMap);
		}
		return results;
	}
	public List<Student> findStudentByIds(String[] ids) {
		// TODO Auto-generated method stub
		List<Student> students=new ArrayList<Student>();
		for(String id:ids){
			int id2= Integer.parseInt(id);
			students.add(studentRepository.findById(id2));
		}		
		return students;
	}
	public void deleteBatch(List<Integer> idList) {
		studentRepository.deleteBatch(idList);		
	}


	public Student getStudnetIdSnoName(String sno) {
		Student stu = studentRepository.findBySno(sno);
		return stu;
		

	}


	public List<Map<String, Object>> getOneDormStudentInfo(int did) {
		List<Student> stus = studentRepository.findByDorm_code(did);
		List<Map<String, Object>>  results = new ArrayList<>(); 
		// 遍历查询出的学生对象，提取姓名，年龄，性别信息
		for(Student stu :stus){
			Map<String , Object> speMap = new HashMap<>(); 
			speMap.put("id", stu.getId());
			speMap.put("name", stu.getName());
			results.add(speMap);
		}
		return results;
	
	}



	@SuppressWarnings({ "deprecation", "resource" })
	public boolean batchImport(String fileName, MultipartFile file,String bid) throws Exception {
		

		boolean notNull = false;
		if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
			throw new MyException("上传文件格式不正确");
		}
		boolean isExcel2003 = true;
		if (fileName.matches("^.+\\.(?i)(xlsx)$")) {
			isExcel2003 = false;
		}
		InputStream is = file.getInputStream();
		Workbook wb = null;
		if (isExcel2003) {
			wb = new HSSFWorkbook(is);
		} else {
			wb = new XSSFWorkbook(is);
		}
		Sheet sheet = wb.getSheetAt(0);
		if (sheet != null) {
			notNull = true;
		}

		for (int r = 0; r <= sheet.getLastRowNum(); r++) {// r = 2 表示从第三行开始循环 如果你的第三行开始是数据
			Row row = sheet.getRow(r);// 通过sheet表单对象得到 行对象
			if (row == null) {
				continue;
			}

			// sheet.getLastRowNum() 的值是 1g0，所以Excel表中的数据至少是10条；不然报错 NullPointerException

	
			/*
			 * if( row.getCell(0).getCellType() !=1){//循环时，得到每一行的单元格进行判断 throw new
			 * MyException("导入失败(第"+(r+1)+"行,用户名请设为文本格式)"); }
			 */

	

			row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
			  String sno = row.getCell(0).getStringCellValue();//得到每一行第一个单元格的值

			 if(sno.isEmpty()){//判断是否为空
				throw new MyException("导入失败(第"+(r+1)+"行,学号未填写)"); }

			 //得到每一行的 第二个单元格的值 
			 row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
			 String name = row.getCell(1).getStringCellValue();

			  if(name.isEmpty()){ 
				  throw new  MyException("导入失败(第"+(r+1)+"行,学生姓名未填写)"); }

			 //得到每一行的 第三个单元格的值
			  row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
			  String clazzId = row.getCell(2).getStringCellValue();

			  if(clazzId.isEmpty()){ throw new
			  MyException("导入失败(第"+(r+1)+"行,班级编号未填写)"); }
				 
			  //得到每一行的 第四个单元格的值
			  row.getCell(3).setCellType(Cell.CELL_TYPE_STRING);
			  String sex = row.getCell(3).getStringCellValue();

			  if(clazzId.isEmpty()){ throw new
			  MyException("导入失败(第"+(r+1)+"行,性别未填写)"); }
			  
			  
			Student stu=new Student();
			Clazz cla=new Clazz();  
			// 完整的循环一次 就组成了一个对象
			stu.setSno(sno);
			stu.setName(name);
			stu.setSex(sex);
			int cid=Integer.parseInt(String.valueOf(clazzId));
			cla.setClazzId(cid);
			stu.setClazz(cla);
			Student student=studentRepository.save(stu);
			
	    	FKRole role =new FKRole();
	    	FKUser user =new FKUser();
         	String s ="123456";// 初始密码
         	String password	=new BCryptPasswordEncoder().encode(s);//加密
	    	int rid=1;
	    	String str = "ROLE_STUDENT"; 
	    	role.setId(rid);
	    	role.setAuthority(str);
	    	List<FKRole> roles =Arrays.asList(role);
	    	List<Student> students =Arrays.asList(student);
	    	user.setRoles(roles);
	    	user.setUsername(sno);
	    	user.setPassword(password);
	    	user.setStudents(students);
	    	userRepository.save(user);

	
		}

		return notNull;
	}


	public Student findStudentByIds(int id) {
		// TODO Auto-generated method stub
		return studentRepository.findById(id);
	}




	public void autoAssign() {
		List<Student> stus = (List<Student>) studentRepository.findAll();
		List<Student> student=new ArrayList<>();
		List<String> studentList=new ArrayList<>();  //学号列表
		List<String> typeList=new ArrayList<>();   //类型列表
		for(Student stu :stus){
			if(stu.getDorm()==null) {			
				student.add(stu);
			}
		}
		System.out.println("列表："+student);
		for(Student stu2: student) {
			String type=stu2.getType();	
			String[]  strs=type.split(",");	
			String  type2=strs[0].toString();
			studentList.add(type2);
		
		}
		for(Student stu3: student) {
			String type=stu3.getType();
			String[]  strs=type.split(",");		
			String  type2=strs[1].toString()+","+strs[2].toString()+","+strs[3].toString();
			System.out.println("type="+type2);
			typeList.add(type2);
		
		}
		System.out.println("学生列表："+studentList);
		//查出类型
		//利用map.containsKey()
		Map<String, Integer> map = new HashMap<>(); 
		List<String> repeatList = new ArrayList<>();
		for(String s : typeList){
			//1:map.containsKey()   检测key是否重复
			if(map.containsKey(s)){
				repeatList.add(s);//获取重复的
				Integer num = map.get(s);
				map.put(s, num+1);
			}else{
				map.put(s, 1);//不重复的
			}
				
		}
		  Map<String, String> classify = new HashMap<>(); 
		  Iterator<Entry<String, Integer>> iter = map.entrySet().iterator();
		  while(iter.hasNext()){
		   Entry<String, Integer> entry = iter.next();
		   String key = entry.getKey();
		   List<String> Index=getIndex(typeList,key);//调用方法
		   String strIndex="";
		   for(int i=0;i<Index.size();i++) {
			   strIndex+=","+Index.get(i);
		   }
		   classify.put(key, strIndex);
		   System.out.println(key+"位置"+Index);
		   Integer value = entry.getValue();
//		   System.out.println(key+" "+value);
		   System.out.println("类别以及对应位置"+" "+classify );
		  }
		  
		  Iterator<Entry<String, String>> iterClassify = classify.entrySet().iterator();
		  while(iterClassify.hasNext()){
		   Entry<String, String> entry = iterClassify.next();
		   String key2 = entry.getKey();
		   String value2 = entry.getValue();
		   System.out.println("待存数据"+key2+" "+value2);
		   System.out.println("待存数据位置"+ value2);
		    // 接收包含stuId的字符串，并将它分割成字符串数组
		    String[] idList = value2.split(",");
		    // 将字符串数组转为List<Intger> 类型
		    List<Integer> LString = new ArrayList<Integer>();
		    for(String str : idList){
		    	

		    	   if (str.equals("")) {
		    		   System.out.println("空值");
		    	   }else {
		    		   System.out.println("啦啦啦啦"+str);
					   List<Dorm> dorm=(List<Dorm>) dormRepository.findAll();
					   Dorm dorm2 = new Dorm();
					   for(Dorm dor : dorm) {
						   int num =Integer.valueOf(dor.getNum()).intValue();
						   int type =Integer.valueOf(dor.getType()).intValue();  
						   if (num < type) {
							   System.out.println("有剩余床位");
							   dorm2=dor;
							   break;
						   }
					   }
					   int Lindex=Integer.valueOf(str).intValue();
					   String stuId=studentList.get(Lindex);
					   System.out.println("学号："+stuId);
					   String strNum=dorm2.getNum();
					   int dormNum=Integer.valueOf(strNum).intValue();
					   int studentId=Integer.valueOf(stuId).intValue();
					   
					   Student student1 = studentRepository.findById(studentId);
					   student1.setDorm(dorm2);
					   Student s =studentRepository.save(student1);
					   if (s!=null) {
						   
						   System.out.println("分配成功！");
						   dormNum++;
						   String num =Integer.toString(dormNum);
						   dorm2.setNum(num);
						   dormRepository.save(dorm2);
					   }
					   
		    	   }


		    }

		
		  }


		
		
	}
//	获取索引位置
	   public static List<String> getIndex(List<String> typeList, String key) {
		   List<String> list = new ArrayList<String>();
	        for (int i = 0; i < typeList.size(); i++) {
	            if (typeList.get(i).equals(key)) {
	            	
	            	String stri=Integer.toString(i);
	            	list.add(stri);
	                
	            }
	        }
			return list;
	       
	    }

	
}
