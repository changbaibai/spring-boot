package dms.controller;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import dms.pojo.LeaveSchool;
import dms.pojo.Student;
import dms.pojo.TimeSheet;
import dms.service.DormService;
import dms.service.StudentService;
import dms.service.TimeSheetService;

@Controller
@RequestMapping("/superAdmin")
public class TimeSheetController {
	// 注入timeSheetService
	@Resource
	private TimeSheetService timeSheetService;
	@Resource
	private DormService dormService;
	@Resource
	private StudentService studentService;

	//获晚归信息
	@RequestMapping("/getTimeSheet")
	@ResponseBody
	public List<Map<String, Object>> getTimeSheet() {
		return timeSheetService.getTimeSheet();
	}
	
	@RequestMapping("/getAdminDormInfo")
	@ResponseBody
	public List<Map<String, Object>> getBuildingById() {
		return dormService.getDormInfo();
	}
	@RequestMapping("/getOneDormStudentInfo")
	@ResponseBody
	public List<Map<String, Object>> getOneDormStudentInfo( @RequestParam(value = "did") int did) {
	    System.out.println("啊啊啊啊啊啊啊啊啊啊啊啊did="+did);
	
		return studentService.getOneDormStudentInfo(did);
	}
    @RequestMapping("/addTimeSheet")
    public String add(TimeSheet timeSheet, @RequestParam(value = "sid") int sid) {
    	System.out.println("啊啊啊啊啊啊啊啊啊啊啊啊啊啊阿"+sid);
    	String name =getUsername();
    	Student student=new Student();
    	Date time = new Date();
    	student.setId(sid);
    	timeSheet.setStudent(student);
    	timeSheet.setChecker(name);
    	timeSheet.setTime(time);
    	timeSheetService.saveAll(timeSheet);
    	return "redirect:/superAdmin/showPageTimeSheet";
    }
	//批量删除
	@RequestMapping("/deleteTimeSheet")
	public String delete(@RequestParam("ids") String id){
	    // 接收包含stuId的字符串，并将它分割成字符串数组
	    String[] idList = id.split(",");
	    // 将字符串数组转为List<Intger> 类型
	    List<Integer> LString = new ArrayList<Integer>();
	    for(String str : idList){
	        LString.add(new Integer(str));
	    }
	    timeSheetService.deleteBatch(LString);
	    return "redirect:/superAdmin/showPageTimeSheet";
	}
	//修改
	@RequestMapping("/editTimeSheet")
	public String edit(TimeSheet timeSheet) {
		timeSheetService.edit(timeSheet);
		return "redirect:/superAdmin/showPageTimeSheet";
	}
	
	// 分页查询
	@RequestMapping("/showPageTimeSheet")
	public String showPageTimeSheet(Model model, @RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "totalPage", defaultValue = "1") int totalPage,
			@RequestParam(value = "size", defaultValue = "2") int size) {
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
		String checker = getUsername();
		Pageable pageable = new PageRequest(page, size, sort);
		Page<TimeSheet> articleDatas = timeSheetService.findByChecker(checker,pageable);
		System.out.println("查询总页数:" + articleDatas.getTotalPages());
		System.out.println("查询总记录数:" + articleDatas.getTotalElements());
		System.out.println("查询当前第几页:" + articleDatas.getNumber());
		System.out.println("查询当前页面的记录数:" + articleDatas.getNumberOfElements());

		// 查询出的结果数据集合
		List<TimeSheet> articles = articleDatas.getContent();
		System.out.println("查询当前页面的集合:" + articles);
		model.addAttribute("list", articles);
		model.addAttribute("TotalPages", articleDatas.getTotalPages());
		model.addAttribute("TotalElements", articleDatas.getTotalElements());
		model.addAttribute("Numbe", articleDatas.getNumber() + 1);
		model.addAttribute("NumberOfElements", articleDatas.getNumberOfElements());
		model.addAttribute("temp", page);
		model.addAttribute("timeSheets", articles);
     	return "superAdmin/timeSheetInfo";
	}
    @RequestMapping("/queryOneStudentTimeSheetInfo")
    @ResponseBody
    public Map<String , Object> queryOneStudentTimeSheetInfo(@RequestParam(value = "page", defaultValue = "1") int page,@RequestParam(value = "rows", defaultValue = "2") int rows){
        return timeSheetService.queryOneStudentTimeSheetInfo(page,rows);
    }
	   /**
	    * 获得当前用户名称
	    * */
	    private String getUsername(){
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
			UserDetails user=(UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			return username;
		}

}
