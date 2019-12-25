package dms.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import dms.Exception.MyException;
import dms.pojo.DC;
import dms.repository.DCRepository;






@Service
public class DCService {
	
	// 注入数据访问层接口对象 
	@Resource
	private DCRepository dcRepository;
	
	@Transactional
    public List<DC> getStudentList() {
        return dcRepository.findAll();
    }
	public void saveAll(DC dc) {
		dcRepository.save(dc);
	}


    public DC findStuById(int id) {
        return dcRepository.findById(id);
    }
	
	public List<DC> getStusByElectricAndWater(String electric,String water) {
		return dcRepository.findByElectricAndWater(electric,water);
	}
	
	public List<DC> getStusByElectricLike(String electric) {
		return dcRepository.findByElectricLike("%"+electric+"%");
	}
	public void delete(int id) {
		dcRepository.deleteById(id);
		
	}
    public void edit(DC dc) {
        dcRepository.save(dc);
    }

    @SuppressWarnings("resource")
	public boolean batchImport(String fileElectric, MultipartFile file) throws Exception {
		boolean notNull = false;
		List<DC> dcList = new ArrayList<>();
		if (!fileElectric.matches("^.+\\.(?i)(xls)$") && !fileElectric.matches("^.+\\.(?i)(xlsx)$")) {
			throw new MyException("上传文件格式不正确");
		}
		boolean isExcel2003 = true;
		if (fileElectric.matches("^.+\\.(?i)(xlsx)$")) {
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
		if(sheet!=null){
			notNull = true;
		}
		DC dc;
		for (int r = 0; r <= sheet.getLastRowNum(); r++) {//r = 2 表示从第三行开始循环 如果你的第三行开始是数据
			Row row = sheet.getRow(r);//通过sheet表单对象得到 行对象
			if (row == null){
				continue;
			}

			//sheet.getLastRowNum() 的值是 10，所以Excel表中的数据至少是10条；不然报错 NullPointerException

			dc = new DC();

			String electric = row.getCell(0).getStringCellValue();//得到每一行第一个单元格的值

			if(electric == null || electric.isEmpty()){//判断是否为空
				throw new MyException("导入失败(第"+(r+1)+"行,用户名未填写)");
			}


           //得到每一行的 第二个单元格的值
			String water = row.getCell(1).getStringCellValue();


			if(water==null || water.isEmpty()){
				throw new MyException("导入失败(第"+(r+1)+"行,地址未填写)");
			}
			



           //得到每一行的 第三个单元格的值
			String hotelRates = row.getCell(2).getStringCellValue();


			if(hotelRates==null || hotelRates.isEmpty()){
				throw new MyException("导入失败(第"+(r+1)+"行,年龄未填写)");
			}
			

	        //得到每一行的 第四个单元格的值
			String upkeep = row.getCell(3).getStringCellValue();


			if(upkeep==null ||upkeep.isEmpty()){
				throw new MyException("导入失败(第"+(r+1)+"行,性别未填写)");
			}



			//完整的循环一次 就组成了一个对象
			dc.setElectric(electric);
			dc.setWater(water);
			dc.setHotelRates(hotelRates);
			dc.setUpkeep(upkeep);
			dcList.add(dc);
			System.out.println("啦啦啦啦啦啦啦啦啦啦啦"+dcList);
		}
		for (DC dcResord : dcList) {
			dcRepository.save(dcResord);
		}
		return notNull;
	}


}
