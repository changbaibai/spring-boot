
package dms.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import dms.pojo.Student;
import dms.pojo.VisitInfo;
import dms.service.StudentService;
import dms.service.VisitInfoService;

@Controller
@RequestMapping("/superAdmin")
public class VisitInfoController {
	// 注入visService
	@Resource
	private VisitInfoService visService;
	
	@Resource
	private StudentService stuService;

	@RequestMapping("/deleteVisitInfo")
	public String delete(int id) {
		visService.delete(id);
		return "redirect:/superAdmin/showPage";
	}

	@RequestMapping("/editVisitInfo")
	public String edit(VisitInfo vis) {
		visService.edit(vis);
		return "redirect:/superAdmin/showPage";
	}
	@RequestMapping("/addVisitInfo")
	@ResponseBody
	public String add(VisitInfo vi,@RequestParam(value = "vis",required=false)String vis,@RequestParam(value = "mee",required=false)String mee
    		,@RequestParam(value = "dor",required=false)String dor,@RequestParam(value = "bui",required=false)String bui) {
	    System.out.println("啊啊啊"+vis +mee+dor+bui);
		Student student =stuService.getStuByName(mee);
		if (student==null) {
			return "0";
		}
		else {
			if(student.getDorm().getName().equals(dor)) {
				vi.setVisitor(vis);
				vi.setMeetPeople(mee);
				vi.setDormName(dor);
				vi.setBuildingName(bui);
		
				Date time=new Date();
				vi.setTime(time);
				VisitInfo visit=visService.saveAll(vi);
				if(visit!=null) {
					return "1";
				}else {
					return "2";
				}
		
			}
		}
		return "0";
	}


	
	@RequestMapping(value = "/selectExportVisitInfo")
	@ResponseBody
	public void selectExport(@RequestParam("Ids") String Ids,HttpServletResponse response)  throws IOException {
	    // 接收包含ids的字符串，并将它分割成字符串数组
	    String[] ids = Ids.split(",");
		List<VisitInfo> viss = visService.findVisitInfoByIds(ids);
		@SuppressWarnings("resource")
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("获取excel测试表格");

		HSSFRow row = null;

		row = sheet.createRow(0);// 创建第一个单元格
		row.setHeight((short) (26.25 * 20));
		row.createCell(0).setCellValue("来访信息列表");// 为第一行单元格设值

		/*
		 * 为标题设计空间 firstRow从第1行开始 lastRow从第0行结束
		 *
		 * 从第1个单元格开始 从第3个单元格结束
		 */
		CellRangeAddress rowRegion = new CellRangeAddress(0, 0, 0, 2);
		sheet.addMergedRegion(rowRegion);

		/*
		 * 动态获取数据库列 sql语句 select COLUMN_NAME from INFORMATION_SCHEMA.Columns where
		 * table_name='vis' and table_schema='test' 第一个table_name 表名字 第二个table_name 数据库名称
		 */
		row = sheet.createRow(1);
		row.setHeight((short) (22.50 * 20));// 设置行高
		row.createCell(0).setCellValue("Id");// 为第一个单元格设值
		row.createCell(1).setCellValue("来访者");// 为第二个单元格设值
		row.createCell(2).setCellValue("访问的人");// 为第三个单元格设值
		row.createCell(3).setCellValue("宿舍名称");// 为第四个单元格设值
		row.createCell(4).setCellValue("宿舍楼");// 为第四个单元格设值

		for (int i = 0; i < viss.size(); i++) {
			row = sheet.createRow(i + 2);
			VisitInfo vis = viss.get(i);
			row.createCell(0).setCellValue(vis.getId());
			row.createCell(1).setCellValue(vis.getVisitor());
			row.createCell(2).setCellValue(vis.getMeetPeople());
			row.createCell(3).setCellValue(vis.getDormName());
			row.createCell(4).setCellValue(vis.getBuildingName());
		}
		sheet.setDefaultRowHeight((short) (16.5 * 20));
		// 列宽自适应
		for (int i = 0; i <= 13; i++) {
			sheet.autoSizeColumn(i);
		}

		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		OutputStream os = response.getOutputStream();
		response.setHeader("Content-disposition", "attachment;filename=vis.xls");// 默认Excel名称
		wb.write(os);
		os.flush();
		os.close();

	}

	// 分页查询

//    @ResponseBody
	@RequestMapping("/showPageVisitInfo")
	public String showPageStudent(Model model, @RequestParam(value = "page", defaultValue = "1") int page,
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
		System.out.println("BB啦啦啦啦" + totalPage);
		@SuppressWarnings("deprecation")
		Pageable pageable = new PageRequest(page, size, sort);
		Page<VisitInfo> articleDatas = visService.findAll(pageable);

		System.out.println("查询总页数:" + articleDatas.getTotalPages());
		System.out.println("查询总记录数:" + articleDatas.getTotalElements());
		System.out.println("查询当前第几页:" + articleDatas.getNumber());
		System.out.println("查询当前页面的记录数:" + articleDatas.getNumberOfElements());

		// 查询出的结果数据集合
		List<VisitInfo> articles = articleDatas.getContent();
		System.out.println("查询当前页面的集合:" + articles);
		model.addAttribute("visitInfo", articles);
		model.addAttribute("TotalPages", articleDatas.getTotalPages());
		model.addAttribute("TotalElements", articleDatas.getTotalElements());
		model.addAttribute("Numbe", articleDatas.getNumber() + 1);
		model.addAttribute("NumberOfElements", articleDatas.getNumberOfElements());
		model.addAttribute("temp", page);


     	return "superAdmin/visitInfo";
	}
	
	@RequestMapping("/delsVisitInfo")
	public String batch_del_stu(@RequestParam("ids") String ids){
	    // 接收包含stuId的字符串，并将它分割成字符串数组
	    String[] list = ids.split(",");
	    // 将字符串数组转为List<Intger> 类型
	    List<Integer> LString = new ArrayList<Integer>();
	    for(String str : list){
	        LString.add(new Integer(str));
	    }
	    visService.deleteBatch(LString);
	    return "redirect:/superAdmin/showPageVisitInfo";
	}

}
