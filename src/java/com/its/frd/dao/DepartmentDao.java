package com.its.frd.dao;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import com.its.common.entity.main.Module;
import com.its.frd.entity.Department;
import com.its.frd.entity.MesDriver;

public interface DepartmentDao extends JpaRepository<Department, Long>, JpaSpecificationExecutor<Department> {

    
    @QueryHints(value={
            @QueryHint(name="org.hibernate.cacheable",value="true"),
            @QueryHint(name="org.hibernate.cacheRegion",value="com.its.frd.entity.Department")
        }
    )
    @Query("from Department m order by m.id ASC")
    List<Department> findAllWithCache();
    
    @Query("FROM Department M WHERE M.companyid=?1")
    public List<Department> findByCompanyid(Long companyid);
    
    @Query("FROM Department M WHERE M.companyid=?1 And M.department.id is Null")
    public Department findByDepartmentIdAndCompanyid(Long companyid);
    
    @Query("FROM Department M WHERE M.companyid=?1 AND M.sn=?2")
    public List<Department> findByCompanyinfoidAndSn(Long companyid,String sn);
    
    @Query("FROM Department M WHERE M.companyid=?1 AND M.name=?2")
    public List<Department> findByCompanyinfoidAndName(Long companyid,String name);

	public List<Department> findByDepartment(Department parent);
	
	@Query("FROM Department M WHERE M.companyid=?1 AND M.department.id is NULL AND M.factoryid is NULL")
	public List<Department> findByCompanyidWithoutParentIdAndFactoryId(Long companyid);
	
	@Query("FROM Department M WHERE M.id=?1 AND M.department.id is NULL")
	public Department verfyDept(Long id);

	public List<Department> findByFactoryid(Long factoryId);
	
}
