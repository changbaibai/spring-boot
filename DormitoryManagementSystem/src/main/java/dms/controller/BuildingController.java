package dms.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import dms.pojo.Building;

import dms.service.BuildingService;

@Controller
@RequestMapping("/superAdmin")
public class BuildingController {
	// 注入buildingService
	@Resource
	private BuildingService buildingService;
	//获取班级信息
	@RequestMapping("/getBuilding")
	@ResponseBody
	public List<Map<String, Object>> getBuilding() {
		return buildingService.getBuilding();
	}
	
    @RequestMapping("/addBuilding")
    public String add(Building building) {
    	buildingService.saveAll(building);
    	return "redirect:/superAdmin/showPageBuilding";
    }
	//批量删除
	@RequestMapping("/deleteBuilding")
	public String delete(@RequestParam("id") String id){
	    // 接收包含stuId的字符串，并将它分割成字符串数组
	    String[] idList = id.split(",");
	    // 将字符串数组转为List<Intger> 类型
	    List<Integer> LString = new ArrayList<Integer>();
	    for(String str : idList){
	        LString.add(new Integer(str));
	    }
	    buildingService.deleteBatch(LString);
	    return "redirect:/superAdmin/showPageBuilding";
	}
	//修改
	@RequestMapping("/editBuilding")
	public String edit(Building building) {
		buildingService.edit(building);
		return "redirect:/superAdmin/showPageBuilding";
	}
	
	// 分页查询
	@RequestMapping("/showPageBuilding")
	public String showPageBuilding(Model model, @RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "totalPage", defaultValue = "1") int totalPage,
			@RequestParam(value = "size", defaultValue = "2") int size) {
		if (page == -1) 
		{ page++; } 
		if (page == totalPage) 
		{ page--; }
		
		// 指定排序参数对象：根据id，进行降序查询
		Sort sort = new Sort(Sort.Direction.DESC, "buildingId");
		/**
		 * 封装分页实体 参数一：pageIndex表示当前查询的第几页(默认从0开始，0表示第一页) 参数二：表示每页展示多少数据，现在设置每页展示2条数据
		 * 参数三：封装排序对象，根据该对象的参数指定根据id降序查询
		 */
		System.out.println("BB啦啦啦啦" + totalPage);
		@SuppressWarnings("deprecation")
		Pageable pageable = new PageRequest(page, size, sort);
		Page<Building> articleDatas = buildingService.findAll(pageable);
		System.out.println("查询总页数:" + articleDatas.getTotalPages());
		System.out.println("查询总记录数:" + articleDatas.getTotalElements());
		System.out.println("查询当前第几页:" + articleDatas.getNumber());
		System.out.println("查询当前页面的记录数:" + articleDatas.getNumberOfElements());
		// 查询出的结果数据集合
		List<Building> articles = articleDatas.getContent();
		System.out.println("查询当前页面的集合:" + articles);
		model.addAttribute("list", articles);
		model.addAttribute("TotalPages", articleDatas.getTotalPages());
		model.addAttribute("TotalElements", articleDatas.getTotalElements());
		model.addAttribute("Numbe", articleDatas.getNumber() + 1);
		model.addAttribute("NumberOfElements", articleDatas.getNumberOfElements());
		model.addAttribute("temp", page);
		model.addAttribute("building", articles);
     	return "superAdmin/buildingInfo";
	}
	

}
