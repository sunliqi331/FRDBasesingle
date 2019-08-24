package com.its.frd.service;

import java.util.List;

import com.its.frd.entity.Companyinfo;
import com.its.frd.entity.MesProductline;

public interface MesProductlineService extends BaseService<MesProductline> {
	
	public MesProductline findById(Long id);
	
	public void deleteById(Long id);
	
	public List<MesProductline> findByCompanyid(Long comapnyid);
	
	public MesProductline findByCompanyinfoidAndLinen(Long companyid,String linesn);
	
	public MesProductline findByLinename(String linename);

	List<MesProductline> findByCompanyinfoIdIn(List<Long> companyinfoIds);
}
