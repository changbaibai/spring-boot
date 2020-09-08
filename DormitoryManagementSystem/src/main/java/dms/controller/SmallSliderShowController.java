package dms.controller;
 
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
 
import javax.servlet.http.HttpServletRequest;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import dms.pojo.SliderShow;
import dms.pojo.SmallSliderShow;
import dms.repository.SliderShowRepository;
import dms.repository.SmallSliderShowRepository;
 

 
@Controller
@RequestMapping("/superAdmin")
public class SmallSliderShowController {
 
	
	@Autowired
	private SmallSliderShowRepository smallSliderShowRepository;
	
//	
//	@RequestMapping("/listSmallSliderInfo")
//	public String list(Model model) {
//		List<SmallSliderShow> lists = smallSliderShowRepository.findAll();
//		System.out.println("啊啊啊啊啊啊"+lists);
//		model.addAttribute("lists", lists);
//		return "superAdmin/smallSliderInfo";
//	}
	@RequestMapping("/listSmallSliderInfoAll")
	@ResponseBody
	public List<SmallSliderShow> listAll() {
		List<SmallSliderShow> lists = smallSliderShowRepository.findAll();
		System.out.println("啊啊啊啊啊啊"+lists);
		return lists;
	}
	
	@RequestMapping("/addSmallSliderInfo")
	public String add(SmallSliderShow smallSliderShow,@RequestParam(value = "file", required = false) MultipartFile file,
			HttpServletRequest request) throws IllegalStateException, IOException {
		

		if (!file.isEmpty()) {

	        String filePath = "E://temp-rainy//"; // 上传后的路径
	       
            String fileName = file.getOriginalFilename();
          //重命名
			fileName = UUID.randomUUID().toString().replace("-", "")
					+ fileName.substring(fileName.lastIndexOf("."));
            //File saveFile = new File(request.getSession().getServletContext().getRealPath("/upload/") + fileName); 
            File saveFile = new File(filePath + fileName);
            if (!saveFile.getParentFile().exists()) {  
                saveFile.getParentFile().mkdirs();  
            }  
              
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(saveFile));  
            out.write(file.getBytes());  
            out.flush();  
            out.close();
    
            String filename = "/temp-rainy/" + fileName;
            smallSliderShow.setFilepath(filename);
            smallSliderShowRepository.save(smallSliderShow);
			return "redirect:/superAdmin/listSmallSliderInfo";
            
        } else {
        	smallSliderShowRepository.save(smallSliderShow);
			return "redirect:/superAdmin/listSmallSliderInfo";
        }  
		
	}
	@RequestMapping("/toEditSmallSliderInfo")
	public String toEdit(int id ,Model model) {
		System.out.println("lllllll啦啦啦啦啦"+id);
		SmallSliderShow smallSiderShow=smallSliderShowRepository.findById(id);
		model.addAttribute("siderShow", smallSiderShow);
		return "superAdmin/smallSliderInfoEdit";
	}
	@RequestMapping("/delSmallSliderInfo")
	public String del(int id,Model model) {
		SmallSliderShow smallSliderShow=smallSliderShowRepository.findById(id);
		smallSliderShowRepository.delete(smallSliderShow);

		return "redirect:/superAdmin/listSmallSliderInfo";
	}
	// 分页查询
	@RequestMapping("/listSmallSliderInfo")
	public String showPageRepair(Model model, @RequestParam(value = "page", defaultValue = "1") int page,
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
		@SuppressWarnings("deprecation")
		Pageable pageable = new PageRequest(page, size, sort);
		Page<SmallSliderShow> articleDatas = smallSliderShowRepository.findAll(pageable);
		System.out.println("查询总页数:" + articleDatas.getTotalPages());
		System.out.println("查询总记录数:" + articleDatas.getTotalElements());
		System.out.println("查询当前第几页:" + articleDatas.getNumber());
		System.out.println("查询当前页面的记录数:" + articleDatas.getNumberOfElements());

		// 查询出的结果数据集合
		List<SmallSliderShow> articles = articleDatas.getContent();
		System.out.println("查询当前页面的集合:" + articles);
		model.addAttribute("lists", articles);
		model.addAttribute("TotalPages", articleDatas.getTotalPages());
		model.addAttribute("TotalElements", articleDatas.getTotalElements());
		model.addAttribute("Numbe", articleDatas.getNumber() + 1);
		model.addAttribute("NumberOfElements", articleDatas.getNumberOfElements());
		model.addAttribute("temp", page);
     	return "superAdmin/smallSliderInfo";
	}
}