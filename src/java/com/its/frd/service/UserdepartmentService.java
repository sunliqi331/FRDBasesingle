package com.its.frd.service;

import java.util.List;

import com.its.frd.entity.Userdepartment;

public interface UserdepartmentService extends BaseService<Userdepartment> {
	
	public Userdepartment findById(Long id);
	
	public void deleteById(Long id);
	
	public List<Userdepartment> findByUserid(Long userid);
	
	public List<Userdepartment> findByDepartmentid(Long departmentid);
	
	public Userdepartment findByUseridAndDepartmentid(Long userid,Long departmentid);
	
}
