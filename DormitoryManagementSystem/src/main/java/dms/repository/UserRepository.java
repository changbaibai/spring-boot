package dms.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import dms.pojo.FKUser;

public interface UserRepository extends JpaRepository<FKUser, Long>{

	// 根据登录名查询出用户
	FKUser findByLoginName(String loginName);


}
