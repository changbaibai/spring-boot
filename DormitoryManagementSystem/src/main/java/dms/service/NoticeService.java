package dms.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import dms.pojo.FKUser;
import dms.pojo.LeaveSchool;
import dms.pojo.Notice;
import dms.repository.NoticeRepository;

@Service
public class NoticeService {

	// 注入数据访问层接口对象
	@Resource
	private NoticeRepository noticeRepository;



	public void saveAll(Notice notice) {
	      Date now = new Date();
	      notice.setTime(now);
	      System.out.println(now);
		noticeRepository.save(notice);
	}

	public Notice findStuById(int id) {
		return noticeRepository.findById(id);
	}

	public void delete(int id) {
		noticeRepository.deleteById(id);

	}

	public void edit(Notice notice) {
		noticeRepository.save(notice);
	}

	public Page<Notice> findAll(Pageable page) {
		return noticeRepository.findAll(page);
	}
	
	public Iterable<Notice> findAllSort(Sort sort) {
		return noticeRepository.findAll(sort);
	}
	public List<Notice> getNoticeList() {
		return (List<Notice>) noticeRepository.findAll();
	}
	public List<Notice> findNoticeByIds(String[] ids) {
		// TODO Auto-generated method stub
		List<Notice> notices=new ArrayList<Notice>();
		for(String id:ids){
			int id2 = Integer.parseInt(id);
			notices.add(noticeRepository.findById(id2));
		}
		
		return notices;
	}



	public void deleteBatch(List<Integer> stuList) {
		noticeRepository.deleteBatch(stuList);
		
	}

	public Map<String, Object> findAllInfo(int page, int rows) {
			Pageable pageable = PageRequest.of(page-1,rows,Sort.Direction.DESC,"id");
			Page<Notice> noticeDatas = noticeRepository.findAll(pageable);
			List<Notice> leas =noticeDatas.getContent();
			System.out.println("啊啊啊啊啊啊啊啊"+noticeDatas);
			List<Map<String, Object>>  results = new ArrayList<>(); 
			// 遍历查询出的学生对象，提取姓名，年龄，性别信息
			for(Notice lea :leas){
				Map<String , Object> map = new HashMap<>(); 
				map.put("id", lea.getId());
				map.put("text", lea.getText());
				map.put("time", lea.getTime());
				results.add(map);
			}
		
	        long TotalPages = noticeDatas.getTotalElements();
	       
	        long Total = noticeDatas.getTotalPages();

	        Map<String, Object> map = new HashMap<>();
	        map.put("total", TotalPages);
	        map.put("totalNotFiltered",Total);
	        map.put("rows",results);
	        return map;
		

	}

	
}
