package dms.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import dms.pojo.FKRole;
import dms.repository.RoleRepository;


@Service
public class RoleService {
	@Resource
	private RoleRepository roleRepository;
    
	public void save(FKRole fKRole) {
		roleRepository.save(fKRole);
	}

}
