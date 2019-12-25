package dms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dms.pojo.FKRole;

public interface RoleRepository extends JpaRepository<FKRole, Integer> {

}
