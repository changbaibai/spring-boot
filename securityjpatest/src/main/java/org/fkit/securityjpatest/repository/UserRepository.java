package org.fkit.securityjpatest.repository;


import org.fkit.securityjpatest.pojo.FKUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<FKUser, Long>{

	// 根据登录名查询出用户
	FKUser findByLoginName(String loginName);

}
