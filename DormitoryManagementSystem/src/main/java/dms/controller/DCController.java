
package dms.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
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

import dms.pojo.DC;
import dms.service.DCService;

@Controller
@RequestMapping("/superAdmin")
public class DCController {
	// 注入dcService
	@Resource
	private DCService dcService;
	@RequestMapping("/nameAndAddress")
	public List<DC> getByNameAndAddress(String name, String address) {
		return dcService.getStusByElectricAndWater(name, address);
	}
	@RequestMapping("/nameLike")
	public List<DC> getByNameLile(String name) {
		return dcService.getStusByElectricLike(name);
	}

	@RequestMapping("/delete")
	public String delete(int id) {
		dcService.delete(id);
		return "redirect:/superAdmin/showPage";
	}

	@RequestMapping("/edit")
	public String edit(DC dc) {
		dcService.edit(dc);
		return "redirect:/superAdmin/showPage";
	}
	@RequestMapping("/add")
	public String add(DC dc) {
		dcService.saveAll(dc);
		return "redirect:/superAdmin/showPage";
	}

	@RequestMapping(value = "/export")
	@ResponseBody
	public void export(HttpServletResponse response) throws IOException {
		List<DC> dcs = dcService.getDCList();
		@SuppressWarnings("resource")
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("获取excel测试表格");
		HSSFRow row = null;
		row = sheet.createRow(0);// 创建第一个单元格
		row.setHeight((short) (26.25 * 20));
		row.createCell(0).setCellValue("宿舍费用信息列表");// 为第一行单元格设值
		/*
		 * 为标题设计空间 firstRow从第1行开始 lastRow从第0行结束
		 *
		 * 从第1个单元格开始 从第3个单元格结束
		 */
		CellRangeAddress rowRegion = new CellRangeAddress(0, 0, 0, 2);
		sheet.addMergedRegion(rowRegion);

		/*
		 * 动态获取数据库列 sql语句 select COLUMN_NAME from INFORMATION_SCHEMA.Columns where
		 * table_name='dc' and table_schema='test' 第一个table_name 表名字 第二个table_name 数据库名称
		 */
		row = sheet.createRow(1);
		row.setHeight((short) (22.50 * 20));// 设置行高
		row.createCell(0).setCellValue("Id");// 为第一个单元格设值
		row.createCell(1).setCellValue("电费");// 为第二个单元格设值
		row.createCell(2).setCellValue("水费");// 为第三个单元格设值
		row.createCell(3).setCellValue("住宿费");// 为第四个单元格设值
		row.createCell(4).setCellValue("维修费");// 为第五个单元格设值

		for (int i = 0; i < dcs.size(); i++) {
			row = sheet.createRow(i + 2);
			DC dc = dcs.get(i);
			row.createCell(0).setCellValue(dc.getId());
			row.createCell(1).setCellValue(dc.getElectric());
			row.createCell(2).setCellValue(dc.getWater());
			row.createCell(3).setCellValue(dc.getHotelRates());
			row.createCell(4).setCellValue(dc.getUpkeep());
		}
		sheet.setDefaultRowHeight((short) (16.5 * 20));
		// 列宽自适应
		for (int i = 0; i <= 13; i++) {
			sheet.autoSizeColumn(i);
		}

		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		OutputStream os = response.getOutputStream();
		response.setHeader("Content-disposition", "attachment;filename=dc.xls");// 默认Excel名称
		wb.write(os);
		os.flush();
		os.close();

	}
	
	@RequestMapping(value = "/selectExport")
	@ResponseBody
	public void selectExport(@RequestParam("Ids") String Ids,HttpServletResponse response)  throws IOException {
	    // 接收包含ids的字符串，并将它分割成字符串数组
	    String[] ids = Ids.split(",");
		List<DC> dcs = dcService.findDCByIds(ids);
		@SuppressWarnings("resource")
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("获取excel测试表格");

		HSSFRow row = null;

		row = sheet.createRow(0);// 创建第一个单元格
		row.setHeight((short) (26.25 * 20));
		row.createCell(0).setCellValue("宿舍费用信息列表");// 为第一行单元格设值

		/*
		 * 为标题设计空间 firstRow从第1行开始 lastRow从第0行结束
		 *
		 * 从第1个单元格开始 从第3个单元格结束
		 */
		CellRangeAddress rowRegion = new CellRangeAddress(0, 0, 0, 2);
		sheet.addMergedRegion(rowRegion);

		/*
		 * 动态获取数据库列 sql语句 select COLUMN_NAME from INFORMATION_SCHEMA.Columns where
		 * table_name='dc' and table_schema='test' 第一个table_name 表名字 第二个table_name 数据库名称
		 */
		row = sheet.createRow(1);
		row.setHeight((short) (22.50 * 20));// 设置行高
		row.createCell(0).setCellValue("Id");// 为第一个单元格设值
		row.createCell(1).setCellValue("电费");// 为第二个单元格设值
		row.createCell(2).setCellValue("水费");// 为第三个单元格设值
		row.createCell(3).setCellValue("住宿费");// 为第四个单元格设值
		row.createCell(4).setCellValue("维修费");// 为第五个单元格设值

		for (int i = 0; i < dcs.size(); i++) {
			row = sheet.createRow(i + 2);
			DC dc = dcs.get(i);
			row.createCell(0).setCellValue(dc.getId());
			row.createCell(1).setCellValue(dc.getElectric());
			row.createCell(2).setCellValue(dc.getWater());
			row.createCell(3).setCellValue(dc.getHotelRates());
			row.createCell(4).setCellValue(dc.getUpkeep());
		}
		sheet.setDefaultRowHeight((short) (16.5 * 20));
		// 列宽自适应
		for (int i = 0; i <= 13; i++) {
			sheet.autoSizeColumn(i);
		}

		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		OutputStream os = response.getOutputStream();
		response.setHeader("Content-disposition", "attachment;filename=dc.xls");// 默认Excel名称
		wb.write(os);
		os.flush();
		os.close();

	}

	@RequestMapping(value = "/import")
	public String exImport(@RequestParam(value = "filename") MultipartFile file, HttpSession session) {

		@SuppressWarnings("unused")
		boolean a = false;

		String fileName = file.getOriginalFilename();

		try {
			a = dcService.batchImport(fileName, file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/superAdmin/showPage";
	}

	// 分页查询

//    @ResponseBody
	@RequestMapping("/showPage")
	public String showPageStudent(Model model, @RequestParam(value = "page", defaultValue = "1") int page,
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
		System.out.println("BB啦啦啦啦" + totalPage);
		@SuppressWarnings("deprecation")
		Pageable pageable = new PageRequest(page, size, sort);
		Page<DC> articleDatas = dcService.findAll(pageable);

		System.out.println("查询总页数:" + articleDatas.getTotalPages());
		System.out.println("查询总记录数:" + articleDatas.getTotalElements());
		System.out.println("查询当前第几页:" + articleDatas.getNumber());
		System.out.println("查询当前页面的记录数:" + articleDatas.getNumberOfElements());

		// 查询出的结果数据集合
		List<DC> articles = articleDatas.getContent();
		System.out.println("查询当前页面的集合:" + articles);
		model.addAttribute("list", articles);
		model.addAttribute("TotalPages", articleDatas.getTotalPages());
		model.addAttribute("TotalElements", articleDatas.getTotalElements());
		model.addAttribute("Numbe", articleDatas.getNumber() + 1);
		model.addAttribute("NumberOfElements", articleDatas.getNumberOfElements());
		model.addAttribute("temp", page);
		model.addAttribute("dcs", articles);

     	return "superAdmin/list";
	}
	
	@RequestMapping("/del_some")
	public String batch_del_stu(@RequestParam("stu_id") String stu_id){
	    // 接收包含stuId的字符串，并将它分割成字符串数组
	    String[] stuList = stu_id.split(",");
	    // 将字符串数组转为List<Intger> 类型
	    List<Integer> LString = new ArrayList<Integer>();
	    for(String str : stuList){
	        LString.add(new Integer(str));
	    }
	    dcService.deleteBatch(LString);
	    return "redirect:/superAdmin/showPage";
	}

}
