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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import dms.pojo.Clazz;
import dms.pojo.Dorm;
import dms.pojo.FKUser;
import dms.pojo.LeaveSchool;
import dms.pojo.Maintainer;
import dms.pojo.Repair;
import dms.pojo.Student;
import dms.service.MaintainerService;
import dms.service.RepairService;
import dms.service.UserService;

@Controller
@RequestMapping("/superAdmin")
public class RepairController {
	// 注入repairService
	@Resource
	private RepairService repairService;
	
	@Resource
	private UserService userService;
	// 注入maintainerService
	@Resource
	private MaintainerService maintainerService;

	@RequestMapping("/nameLikeRepair")
	public List<Repair> getByNameLile(String name) {
		return repairService.getStusByNameLike(name);
	}
    //维修申报
    @RequestMapping("/addRepair")
    @ResponseBody
    public String addLeaveSchool(@RequestParam(value = "re",required=false)String re) {
    	Dorm dorm =new Dorm();
    	Repair repair=new Repair();
    	//获取登录名
    	String name =getUsername();  	
    	FKUser user=userService.findByName(name);
		int id =user.getStudents().get(0).getDorm().getCode();
		dorm.setCode(id);
		repair.setDorm(dorm);
		repair.setContent(re);
		repair.setState("0");
		Repair rep =repairService.saveAll(repair);
    	if (rep != null) {
    		return "1";
    	}
    	else {
    		return "0";
    	} 
    }

    //维修申报
    @RequestMapping("/updateRepair")
    @ResponseBody
    public String updateLeaveSchool(@RequestParam(value = "repairId",required=false)String rid,@RequestParam(value = "mid",required=false)String mid,@RequestParam(value = "com",required=false)String com) {
    	Repair repair=new Repair();
    	Maintainer ma =new Maintainer();
    	int id =Integer.valueOf(rid).intValue();
    	int id2 =Integer.valueOf(mid).intValue();
    	repair =repairService.findById(id);	
    	if(com.equals("1")) {
		    ma.setId(id2);
			repair.setState("1");
			repair.setMaintainer(ma);
			Repair rep =repairService.saveAll(repair);
	    	if (rep != null) {
	    		return "1";
	    	}
	    	else {
	    		return "0";
	    	} 
    	}else {
    		String strNum = repair.getMaintainer().getNum();
    		if (strNum==null) {
    			strNum="0";
    		}
			repair.setState("2");
			ma.setId(repair.getMaintainer().getId());
			ma.setName(repair.getMaintainer().getName());
			ma.setPhone(repair.getMaintainer().getPhone());
	    	int num =Integer.valueOf(strNum).intValue();
			num++;
			String str = Integer.toString(num);
			ma.setNum(str);
			Maintainer mai=maintainerService.saveAll(ma);
			Repair rep =repairService.saveAll(repair);
	    	if (mai!= null && rep !=null) {
	    		return "1";
	    	}
	    	else {
	    		return "0";
	    	}
    	}

    }

	//批量删除
	@RequestMapping("/deleteRepair")
	public String delete(@RequestParam("id") String id){
	    // 接收包含stuId的字符串，并将它分割成字符串数组
	    String[] idList = id.split(",");
	    // 将字符串数组转为List<Intger> 类型
	    List<Integer> LString = new ArrayList<Integer>();
	    for(String str : idList){
	        LString.add(new Integer(str));
	    }
	    repairService.deleteBatch(LString);
	    return "redirect:/superAdmin/showPageRepair";
	}
	//修改
	@RequestMapping("/editRepair")
	public String edit(Repair repair) {
		repairService.edit(repair);
		return "redirect:/superAdmin/showPageRepair";
	}
	
	// 分页查询
	@RequestMapping("/showPageRepair")
	public String showPageRepair(Model model, @RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "totalPage", defaultValue = "1") int totalPage,
			@RequestParam(value = "size", defaultValue = "9") int size) {
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
		Pageable pageable = new PageRequest(page, size, sort);
		Page<Repair> articleDatas = repairService.findAll(pageable);
		System.out.println("查询总页数:" + articleDatas.getTotalPages());
		System.out.println("查询总记录数:" + articleDatas.getTotalElements());
		System.out.println("查询当前第几页:" + articleDatas.getNumber());
		System.out.println("查询当前页面的记录数:" + articleDatas.getNumberOfElements());

		// 查询出的结果数据集合
		List<Repair> articles = articleDatas.getContent();
		System.out.println("查询当前页面的集合:" + articles);
		model.addAttribute("list", articles);
		model.addAttribute("TotalPages", articleDatas.getTotalPages());
		model.addAttribute("TotalElements", articleDatas.getTotalElements());
		model.addAttribute("Numbe", articleDatas.getNumber() + 1);
		model.addAttribute("NumberOfElements", articleDatas.getNumberOfElements());
		model.addAttribute("temp", page);
		model.addAttribute("repair", articles);
     	return "superAdmin/repairInfo";
	}
	
    /**
     * 获得当前用户名称
     * */
     private String getUsername(){
 		String username = SecurityContextHolder.getContext().getAuthentication().getName();
 		return username;
 	}
     /**
      * 获得当前用户权限
      * */
     private String getAuthority(){
 		// 获得Authentication对象，表示用户认证信息。
 		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
 		List<String> roles = new ArrayList<String>();
 		// 将角色名称添加到List集合
 		for (GrantedAuthority a : authentication.getAuthorities()) {
 			roles.add(a.getAuthority());
 		}
 		return roles.toString();
 	}
     @RequestMapping("/queryUerRepairInfo")
     @ResponseBody
     public Map<String , Object> queryUerLeaveSchoolInfo(@RequestParam(value = "page", defaultValue = "1") int page,@RequestParam(value = "rows", defaultValue = "2") int rows){
         return repairService.queryUerMaintainerInfo(page,rows);
     }
}
