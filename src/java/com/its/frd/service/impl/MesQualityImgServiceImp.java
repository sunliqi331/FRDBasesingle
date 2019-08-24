package com.its.frd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.frd.dao.CompanyfileDao;
import com.its.frd.dao.MesQualityImgDao;
import com.its.frd.entity.Companyfile;
import com.its.frd.entity.MesQualityImg;
import com.its.frd.params.CompanyfileType;
import com.its.frd.service.MesQualityImgService;
@Service
public class MesQualityImgServiceImp implements MesQualityImgService {
    @Resource
    private MesQualityImgDao mdDao;
	@Resource
    private CompanyfileDao cfDao;
	
	
	@Override
	public MesQualityImg findById(Long id) {
		return mdDao.findOne(id);
	}

	@Override
	public void deleteById(Long id) {
	    mdDao.delete(id);
	    Companyfile cpDelFile = new Companyfile();
	    cpDelFile.setParentid(id);
	    cfDao.delete(cpDelFile);

	}

	@Override
	public List<MesQualityImg> findPage(Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesQualityImg> springDataPage = mdDao.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<MesQualityImg> findPage(Specification<MesQualityImg> specification, Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesQualityImg> springDataPage = mdDao.findAll(specification,PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public MesQualityImg saveOrUpdate(MesQualityImg t) {
		return mdDao.save(t);
	}
	

    @Override
    public void saveAndFile(MesQualityImg mesQualityImg, List<Companyfile> companyfilelist) {
        mdDao.save(mesQualityImg);
        for(Companyfile cpfile : companyfilelist){
            cpfile.setParentid(mesQualityImg.getId());
            cpfile.setParenttype(CompanyfileType.QUALITYFILE.toString());
            cfDao.save(cpfile);
        }
    }

    @Override
    public List<MesQualityImg> findByCompanyinfoidAndTypename(Long companyid, String typename) {
        return mdDao.findByCompanyinfoidAndTypename(companyid, typename);
    }

	@Override
	public List<MesQualityImg> findAll(Specification<MesQualityImg> specification) {
		return mdDao.findAll(specification);
	}

    @Override
    public void save(MesQualityImg mesQualityImg) {
        mdDao.save(mesQualityImg);
    }
	
}
