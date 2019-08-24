package com.its.frd.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;

import com.its.common.entity.main.Module;
import com.its.common.util.dwz.Page;
import com.its.frd.entity.Department;
import com.its.frd.entity.MesCompanyPosition;

public interface DepartmentService {
    
	public List<Department> findPage(Specification<Department> specification,Page page);
	
	public Department findById(Long id);
	
	public Department findByDepartmentIdAndCompanyid(Long companyid);
	
	public void saveOrUpdate(Department info);
	
	public void deleteById(Long id);
	
	public List<Department> findByCompanyid(Long comapnyid);
	
	Department getTree();
	
	public List<Department> findByCompanyinfoidAndSn(Long companyid,String sn);
	
	public List<Department> findByCompanyinfoidAndName(Long companyid,String name);
		
	public List<Department> findByParentid(Long parentId);

	public List<Department> findForCompany(Long id);

	public List<Department> findForFactory(Long id);
	
	public String getTree(Long comapnyid);
	
	
	

	public boolean verfyDept(Long id);
	
	

}
