package org.fkit.simplespringdatajpatest.repository;
import org.fkit.simplespringdatajpatest.bean.Clazz;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * @Author dlei(徐磊)
 * @Email dlei0009@163.com
 */
public interface ClazzRepository extends JpaRepository<Clazz, Integer> {

	Clazz findById(int id);

//	void deleteByCode(int id);
	
}
