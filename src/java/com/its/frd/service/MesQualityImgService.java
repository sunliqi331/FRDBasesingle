package com.its.frd.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.its.common.util.dwz.Page;
import com.its.frd.entity.Companyfile;
import com.its.frd.entity.MesQualityImg;

public interface MesQualityImgService extends BaseService<MesQualityImg> {
    
    public void save(MesQualityImg mesQualityImg);
	
	public MesQualityImg findById(Long id);
	
	public void deleteById(Long id);
	
	public List<MesQualityImg> findPage(Specification<MesQualityImg> specification,Page page);
	
	public List<MesQualityImg> findAll(Specification<MesQualityImg> specification);
 	
	public void saveAndFile(MesQualityImg mesQualityImg, List<Companyfile> companyfilelist);
	
	public List<MesQualityImg> findByCompanyinfoidAndTypename(Long companyid,String typename);
	
}
