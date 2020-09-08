package dms.controller;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import dms.pojo.Student;
import dms.pojo.Dorm;
import dms.pojo.HealthRating;

import dms.service.HealthRatingService;

@Controller
@RequestMapping("/superAdmin")
public class HealthRatingController {
	// 注入healthRatingService
	@Resource
	private HealthRatingService healthRatingService;
    //添加卫生评比信息
    @RequestMapping("/addHealthRating")
    public String add(HealthRating healthRating,Dorm dorm) {
    	healthRating.setDorm(dorm);
        Date time= new Date();
        healthRating.setTime(time);
    	healthRatingService.saveAll(healthRating);
    	return "redirect:/superAdmin/showPageHealthRating";
    }
	//批量删除
	@RequestMapping("/deleteHealthRating")
	public String delete(@RequestParam("id") String id){
	    // 接收包含stuId的字符串，并将它分割成字符串数组
	    String[] idList = id.split(",");
	    // 将字符串数组转为List<Intger> 类型
	    List<Integer> LString = new ArrayList<Integer>();
	    for(String str : idList){
	        LString.add(new Integer(str));
	    }
	    healthRatingService.deleteBatch(LString);
	    return "redirect:/superAdmin/showPageHealthRating";
	}
	//修改
	@RequestMapping("/editHealthRating")
	public String edit(HealthRating healthRating,Dorm dorm,@RequestParam(value = "date",required=false)String date) throws ParseException {
		healthRating.setDorm(dorm);
        String dateStr = date;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date time = sdf.parse(dateStr);
            healthRating.setTime(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
		healthRatingService.edit(healthRating);
		return "redirect:/superAdmin/showPageHealthRating";
	}
	
	// 分页查询
	@RequestMapping("/showPageHealthRating")
	public String showPageHealthRating(Model model, @RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "totalPage", defaultValue = "1") int totalPage,
			@RequestParam(value = "size", defaultValue = "5") int size) {
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
		System.out.println("BB啦啦啦啦" + totalPage);
		@SuppressWarnings("deprecation")
		Pageable pageable = new PageRequest(page, size, sort);
		Page<HealthRating> articleDatas = healthRatingService.findAll(pageable);
		System.out.println("查询总页数:" + articleDatas.getTotalPages());
		System.out.println("查询总记录数:" + articleDatas.getTotalElements());
		System.out.println("查询当前第几页:" + articleDatas.getNumber());
		System.out.println("查询当前页面的记录数:" + articleDatas.getNumberOfElements());

		// 查询出的结果数据集合
		List<HealthRating> articles = articleDatas.getContent();
		System.out.println("查询当前页面的集合:" + articles);
		model.addAttribute("list", articles);
		model.addAttribute("TotalPages", articleDatas.getTotalPages());
		model.addAttribute("TotalElements", articleDatas.getTotalElements());
		model.addAttribute("Numbe", articleDatas.getNumber() + 1);
		model.addAttribute("NumberOfElements", articleDatas.getNumberOfElements());
		model.addAttribute("temp", page);
		model.addAttribute("healthRating", articles);
     	return "superAdmin/healthRatingInfo";
	}
	
    @RequestMapping("/queryHealthRatingInfo")
    @ResponseBody
    public Map<String , Object> queryHealthRatingInfo(@RequestParam(value = "page", defaultValue = "1") int page,@RequestParam(value = "rows", defaultValue = "2") int rows){
        return healthRatingService.findAllInfo(page,rows);
    }
	

}
