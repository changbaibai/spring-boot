package org.fkit.springbootmybatistest.controller;
import java.util.List;

import javax.annotation.Resource;


import org.fkit.springbootmybatistest.bean.User;
import org.fkit.springbootmybatistest.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
@Controller
public class UserController {
	
	// 注入UserService
	@Resource
	private UserService userService;
	
	@RequestMapping("/")
	public String index() {
		return "add";
	}

	@RequestMapping("/insertUser")
	public String insertUser(User user){
//		return "插入数据["+userService.insertUser(user)+"]条";
		userService.insertUser(user);
		return "redirect:/list";
	}
	
	@RequestMapping("/insertGetKey")
	public User insertGetKey(User user) {
		userService.insertGetKey(user);
		return user ;
	}
	
	@RequestMapping("/selectByUsername")
	public User selectByUsername(String username){
		System.out.println("test");
		return userService.selectByUsername(username);
	}
	
	@RequestMapping("/findAll")
	public List<User> findAll(){
		return userService.findAll();
	}


	    @RequestMapping("/list")
	    public String list(Model model) {
	        List<User> users = userService.findAll();
	        model.addAttribute("users", users);
	        return "list";
	    }
	
	@RequestMapping("/update")
	public String update(User user) {
		userService.update(user);
		return "redirect:list";
	}
	
	@RequestMapping("/delete")
	public String delete(Integer id) {
		userService.delete(id);
		return "redirect:list";
	}
	@RequestMapping("/toEdit")
	public String findUserById(Model model,Integer id) {
		   User user = userService.findUserById(id);
	        model.addAttribute("user", user);
	        return "edit";
		
	}
}
