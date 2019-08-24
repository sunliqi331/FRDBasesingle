package com.its.frd.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.its.frd.entity.Userdepartment;

public interface UserdepartmentDao extends JpaRepository<Userdepartment, Long>,JpaSpecificationExecutor<Userdepartment>{
	
	 @Query("FROM Userdepartment M WHERE M.department.id=?1")
	public List<Userdepartment> findByDepartmentid(Long departmentid);
	 
	 @Query("FROM Userdepartment M WHERE M.userid=?1")
	 public List<Userdepartment> findByUserid(Long userid);
	 
	 @Query("FROM Userdepartment M WHERE M.userid=?1 AND M.department.id=?2")
	 public Userdepartment findByUseridAndDepartmentid(Long userid,Long departmentid);
}
