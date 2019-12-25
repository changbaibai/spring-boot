package dms.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import dms.pojo.FKUser;
import dms.service.UserService;

@Controller
public class AppController {
	@Resource
	private UserService userService;

	/*
	 * @RequestMapping("/") public String index() { return "index"; }
	 */

	 @RequestMapping(value = "/login")
    public String login() {
        return "login";
    }
	 @RequestMapping(value = "/toRegist")
	    public String toRegist() {
	        return "regist";
	    }
	 

	    @RequestMapping("/regist")
	    public String add(FKUser fkuser) {
	    	userService.save(fkuser);
	        return "login";
	    }
	    


	@RequestMapping("/home")
    public String homePage(Model model) {
    	model.addAttribute("user", getUsername());
    	model.addAttribute("role", getAuthority());
        return "home";
    }
    
    @RequestMapping(value = "/admin")
    public String adminPage(Model model) {
    	model.addAttribute("user", getUsername());
    	model.addAttribute("role", getAuthority());
        return "admin";
    }
    
    @RequestMapping(value = "/dba")
    public String dbaPage(Model model) {
    	model.addAttribute("user", getUsername());
    	model.addAttribute("role", getAuthority());
		return "dba";
    }
    
    @RequestMapping(value = "/accessDenied")
	public String accessDeniedPage(Model model) {
		model.addAttribute("user", getUsername());
		model.addAttribute("role", getAuthority());
		return "accessDenied";
	}
   
    
   @RequestMapping(value="/logout")
	public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
	   // Authentication是一个接口，表示用户认证信息
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		// 如果用户认知信息不为空，注销
		if (auth != null){    
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		// 重定向到login页面
//		return "redirect:/login?logout";esz
		return "login";
	}
    
   /**
    * 获得当前用户名称
    * */
    private String getUsername(){
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		System.out.println("username = " + username);
		UserDetails user=(UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		System.out.println(user);
		
		return username;
	}
    
    /**
     * 获得当前用户权限
     * */
    private String getAuthority(){
		// 获得Authentication对象，表示用户认证信息。
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		List<String> roles = new ArrayList<String>();
		// 将角色名称添加到List集合
		for (GrantedAuthority a : authentication.getAuthorities()) {
			roles.add(a.getAuthority());
		}
		System.out.println("role = " + roles);
		return roles.toString();
	}
    
}
