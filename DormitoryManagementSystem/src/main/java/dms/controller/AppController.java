package dms.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import dms.pojo.FKRole;
import dms.pojo.FKUser;
import dms.pojo.SliderShow;
import dms.repository.SliderShowRepository;
import dms.service.UserService;

@Controller
public class AppController {
	@Resource
	private UserService userService;
	@Autowired
	private SliderShowRepository sliderShowRepository;


	@RequestMapping(value = "/login")
    public String login() {
        return "login";
    }
	@RequestMapping(value = "/toRegist")
	    public String toRegist() {
	        return "regist";
	    }
	@RequestMapping(value = "/toIndex")
	    public String toIndex(Model model) {
		model.addAttribute("role", getAuthority());
    	return "student/index";
	    }
    @RequestMapping("/regist")
    @ResponseBody
    public String regist(FKUser fkuser,@RequestParam(value = "name",required=false)String name,@RequestParam(value = "username",required=false)String username
    		,@RequestParam(value = "password",required=false)String password,@RequestParam(value = "answer",required=false)String answer) {
    	FKRole role =new FKRole();
    	fkuser.setName(name);
    	fkuser.setUsername(username);
    	fkuser.setPassword(password);
    	fkuser.setAnswer(answer);
    	int a=1;
    	role.setId(a);
    	role.setAuthority("ROLE_STUDENT");
    	List<FKRole> roles =Arrays.asList(role);
    	FKUser user =userService.findByUsername(fkuser.getUsername());
    	if (user==null) {
	    	fkuser.setRoles(roles);
	    	FKUser u =userService.save(fkuser);
	    	if(u!=null) {
	    		return "1";
	    	}else {
	    		return "2";
	    	}
    	}else {
    		return "0";
    	}

    }
	@RequestMapping("/student")
    public String homePage(Model model) {
    	model.addAttribute("user", getUsername());
    	model.addAttribute("role", getAuthority());
        return "/student/index";
    }
    
    @RequestMapping(value = "/superAdmin")
    public String superAdminPage(Model model) {
    	model.addAttribute("user", getUsername());
    	model.addAttribute("role", getAuthority());
        return "/superAdmin/index";
    }
    
    @RequestMapping(value = "/admin")
    public String dbaPage(Model model) {
    	model.addAttribute("user", getUsername());
    	model.addAttribute("role", getAuthority());
		return "/admin/index";
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
		return "login";
	}
    
   /**
    * 获得当前用户名称
    * */
    private String getUsername(){
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
	
		UserDetails user=(UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
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
		return roles.toString();
	}  
}
