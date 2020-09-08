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
import dms.pojo.VisitInfo;
import dms.pojo.Dorm;
import dms.repository.VisitInfoRepository;

@Service
public class VisitInfoService {

	// 注入数据访问层接口对象
	@Resource
	private VisitInfoRepository visRepository;



	public VisitInfo saveAll(VisitInfo vis) {
		return visRepository.save(vis);
	}


	public void delete(int id) {
		visRepository.deleteById(id);

	}

	public void edit(VisitInfo vis) {
		visRepository.save(vis);
	}

	public Page<VisitInfo> findAll(Pageable page) {
		return visRepository.findAll(page);
	}
	
	public Iterable<VisitInfo> findAllSort(Sort sort) {
		return visRepository.findAll(sort);
	}
	public List<VisitInfo> getVisitInfoList() {
		return (List<VisitInfo>) visRepository.findAll();
	}
	public List<VisitInfo> findVisitInfoByIds(String[] ids) {
		// TODO Auto-generated method stub
		List<VisitInfo> viss=new ArrayList<VisitInfo>();
		for(String id:ids){
			int id2 = Integer.parseInt(id);
			viss.add(visRepository.findById(id2));
		}
		
		return viss;
	}



	public void deleteBatch(List<Integer> stuList) {
		visRepository.deleteBatch(stuList);
		
	}
	

	
}
