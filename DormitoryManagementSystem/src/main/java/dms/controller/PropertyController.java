package dms.controller;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import dms.pojo.FKRole;
import dms.pojo.FKUser;
import dms.service.RoleService;
import dms.service.UserService;

@Controller
@RequestMapping("/admin")
public class PropertyController {
        
	@Resource
	private UserService userService;
	@Resource
	private RoleService roleService;
	
    @RequestMapping("/userList")
    public String list(Model model) {
		List<Map<String, Object>> users =userService.findAllUser();
        model.addAttribute("userresults",users);
       
        return "admin/userList";
    }
    
    @RequestMapping("/saveRole")
    public String saveRole(Model model,String sid,String loginName,String username,String authority) {
    	System.out.println("AAAAAAAAAAAAAAAAAAA啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊"+sid + "BBBBBBBBBBBBBBBBBBB咩咩咩咩咩咩咩咩咩咩咩咩咩咩咩咩咩咩咩"+ authority);
    
    	Long id = Long.parseLong(sid);
    	FKUser user= new FKUser();
    	user.setId(id);
    	user.setLoginName(loginName);
    	user.setUsername(username);
		FKRole role=new FKRole(); 
		role.setAuthority(authority);
		user.getRoles().add(role);
		userService.save(user);
		roleService.save(role);
        return "redirect:/admin/userList";
    }

}
