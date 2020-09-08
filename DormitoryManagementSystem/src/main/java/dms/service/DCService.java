package dms.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import dms.Exception.MyException;
import dms.pojo.DC;
import dms.pojo.Dorm;
import dms.repository.DCRepository;

@Service
public class DCService {

	// 注入数据访问层接口对象
	@Resource
	private DCRepository dcRepository;



	public void saveAll(DC dc) {
		dcRepository.save(dc);
	}

//	public DC findStuById(int id) {
//		return dcRepository.findById(id);
//	}

	public List<DC> getStusByElectricAndWater(String electric, String water) {
		return dcRepository.findByElectricAndWater(electric, water);
	}

	public List<DC> getStusByElectricLike(String electric) {
		return dcRepository.findByElectricLike("%" + electric + "%");
	}

	public void delete(int id) {
		dcRepository.deleteById(id);

	}

	public void edit(DC dc) {
		dcRepository.save(dc);
	}

	@SuppressWarnings({ "deprecation", "resource" })
	public boolean batchImport(String fileName, MultipartFile file) throws Exception {
		boolean notNull = false;
		List<DC> dcList = new ArrayList<>();
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
		DC dc;
		for (int r = 2; r <= sheet.getLastRowNum(); r++) {// r = 2 表示从第三行开始循环 如果你的第三行开始是数据
			Row row = sheet.getRow(r);// 通过sheet表单对象得到 行对象
			if (row == null) {
				continue;
			}

			// sheet.getLastRowNum() 的值是 10，所以Excel表中的数据至少是10条；不然报错 NullPointerException

			dc = new DC();
			/*
			 * if( row.getCell(0).getCellType() !=1){//循环时，得到每一行的单元格进行判断 throw new
			 * MyException("导入失败(第"+(r+1)+"行,用户名请设为文本格式)"); }
			 */

	

			row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
			  String electric = row.getCell(0).getStringCellValue();//得到每一行第一个单元格的值

			 if(electric == null || electric.isEmpty()){//判断是否为空
				throw new MyException("导入失败(第"+(r+1)+"行,用户名未填写)"); }

			 //得到每一行的 第二个单元格的值 
			 row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
			 String water = row.getCell(1).getStringCellValue();

			  if(water==null || water.isEmpty()){ 
				  throw new  MyException("导入失败(第"+(r+1)+"行,地址未填写)"); }

			 //得到每一行的 第三个单元格的值
			  row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
			  String hotelRates = row.getCell(2).getStringCellValue();

			  if(hotelRates==null || hotelRates.isEmpty()){ throw new
			  MyException("导入失败(第"+(r+1)+"行,年龄未填写)"); }

			 //得到每一行的 第四个单元格的值 
			  row.getCell(3).setCellType(Cell.CELL_TYPE_STRING);
			  String upkeep = row.getCell(3).getStringCellValue();

			 if(upkeep==null ||upkeep.isEmpty()){ throw new
			 MyException("导入失败(第"+(r+1)+"行,性别未填写)"); }
			 

			// 完整的循环一次 就组成了一个对象
			dc.setElectric(electric);
			dc.setWater(water);
			dc.setHotelRates(hotelRates);
			dc.setUpkeep(upkeep);
			dcList.add(dc);
		}
		for (DC dcResord : dcList) {
			dcRepository.save(dcResord);
		}
		return notNull;
	}
	public Page<DC> findAll(Pageable page) {
		return dcRepository.findAll(page);
	}
	
	public Iterable<DC> findAllSort(Sort sort) {
		return dcRepository.findAll(sort);
	}
	public List<DC> getDCList() {
		return (List<DC>) dcRepository.findAll();
	}
	public List<DC> findDCByIds(String[] ids) {
		// TODO Auto-generated method stub
		List<DC> dcs=new ArrayList<DC>();
		for(String id:ids){
			int id2 = Integer.parseInt(id);
			dcs.add(dcRepository.findById(id2));
		}
		
		return dcs;
	}



	public void deleteBatch(List<Integer> stuList) {
		dcRepository.deleteBatch(stuList);
		
	}
	

	
}
