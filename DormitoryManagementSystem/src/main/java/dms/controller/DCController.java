
package dms.controller;

import java.io.IOException;
import java.io.OutputStream;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import dms.pojo.DC;
import dms.service.DCService;




@Controller
@RequestMapping("/admin")
public class DCController {
	// 注入StudentService
	@Resource
	private DCService dcService;
    @RequestMapping("toIndex")
    public String index(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("username", username);
        return "admin/index";
    }

    @RequestMapping("/index")
    public String list(Model model) {
        List<DC> dcs = dcService.getDCList();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("dcs", dcs);
        model.addAttribute("username", username);
        return "admin/list";
    }

	
	
	@RequestMapping("/nameAndAddress")
	public List<DC> getByNameAndAddress(String name,String address) {
		return dcService.getStusByElectricAndWater(name, address);
	}
	
	@RequestMapping("/nameLike")
	public List<DC> getByNameLile(String name) {
		return dcService.getStusByElectricLike(name);
	}
    @RequestMapping("/delete")
    public String delete(int id) {
        dcService.delete(id);
        return "redirect:/admin/index";
    }

    @RequestMapping("/toEdit")
    public String toEdit(Model model, int id) {
        DC dc = dcService.findStuById(id);
        model.addAttribute("dc", dc);
        return "admin/edit";
    }

    @RequestMapping("/edit")
    public String edit(DC dc) {
        dcService.edit(dc);
        return "redirect:/admin/index";
    }
    @RequestMapping("/toAdd")
    public String toAdd() {
        return "add";
    }

    @RequestMapping("/add")
    public String add(DC dc) {
    	dcService.saveAll(dc);
        return "redirect:/admin/index";
    }

	/*
	 * @RequestMapping("/index") public String showStudent(Model model) {
	 * List<Student> dcs = dcService.getStudentList();
	 * model.addAttribute("dc", dcs); return "index"; }
	 * 
	 */
	@RequestMapping(value = "/export")
	@ResponseBody
	public void export(HttpServletResponse response) throws IOException {
		List<DC> dcs = dcService.getDCList();

		@SuppressWarnings("resource")
		HSSFWorkbook wb = new HSSFWorkbook();

		HSSFSheet sheet = wb.createSheet("获取excel测试表格");

		HSSFRow row = null;

		row = sheet.createRow(0);//创建第一个单元格
		row.setHeight((short) (26.25 * 20));
		row.createCell(0).setCellValue("宿舍费用信息列表");//为第一行单元格设值

		/*为标题设计空间
		* firstRow从第1行开始
		* lastRow从第0行结束
		*
		*从第1个单元格开始
		* 从第3个单元格结束
		*/
		CellRangeAddress rowRegion = new CellRangeAddress(0, 0, 0, 2);
		sheet.addMergedRegion(rowRegion);



		/*
		* 动态获取数据库列 sql语句 select COLUMN_NAME from INFORMATION_SCHEMA.Columns where table_name='dc' and table_schema='test'
		* 第一个table_name 表名字
		* 第二个table_name 数据库名称
		* */
		row = sheet.createRow(1);
		row.setHeight((short) (22.50 * 20));//设置行高
		row.createCell(0).setCellValue("Id");//为第一个单元格设值
		row.createCell(1).setCellValue("电费");//为第二个单元格设值
		row.createCell(2).setCellValue("水费");//为第三个单元格设值
		row.createCell(3).setCellValue("住宿费");//为第四个单元格设值
		row.createCell(4).setCellValue("维修费");//为第五个单元格设值

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
		//列宽自适应
		for (int i = 0; i <= 13; i++) {
			sheet.autoSizeColumn(i);
		}

		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		OutputStream os = response.getOutputStream();
		response.setHeader("Content-disposition", "attachment;filename=dc.xls");//默认Excel名称
		wb.write(os);
		os.flush();
		os.close();


	}

	@RequestMapping(value = "/import")
	public String exImport(@RequestParam(value = "filename")MultipartFile file, HttpSession session) {

		boolean a = false;

		String fileName = file.getOriginalFilename();

		try {
			a = dcService.batchImport(fileName, file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/admin/index";
	}














	
}
