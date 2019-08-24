package com.its.frd.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.frd.dao.MesProductDao;
import com.its.frd.entity.Companyinfo;
import com.its.frd.entity.MesProduct;
import com.its.frd.service.CompanyinfoService;
import com.its.frd.service.MesProductService;

@Service
public class MesProductImp implements MesProductService {
	@Resource
	private MesProductDao dao;
	
	@Resource
    private CompanyinfoService cfServ;
	
	@Override
	public MesProduct findById(Long id) {
		return dao.findOne(id);
	}

	@Override
	public void deleteById(Long id) {
		dao.delete(id);
	}

	@Override
	public List<MesProduct> findPage(Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesProduct> springDataPage = dao.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<MesProduct> findPage(Specification<MesProduct> specification, Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesProduct> springDataPage = dao.findAll(specification,PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public MesProduct saveOrUpdate(MesProduct t) {
		return dao.save(t);
	}

    @Override
    public List<MesProduct> findByCompanyinfo(Long companyinfoId) {
        Companyinfo companyinfo = cfServ.findById(companyinfoId);
        return dao.findByCompanyinfo(companyinfo);
    }
    
    @Override
    public List<MesProduct> findByCompanyinfo(Long companyinfoId, String productName) {
    	Companyinfo companyinfo = cfServ.findById(companyinfoId);
    	return dao.findByCompanyinfoAndName(companyinfo, productName);
    }


	@Override
	public Set<String> findAllModelLnum() {
		Set<String> list = new HashSet<String>();
		List<MesProduct> findAll = dao.findAll();
		for(MesProduct product : findAll){
			list.add(product.getModelnum());
		}
		return list;
	}
	
	@Override
	public Set<String> findByCompanyId(Long companyId) {
		Set<String> list = new HashSet<String>();
		List<MesProduct> findAll = dao.findByCompanyinfo(cfServ.findById(companyId));
		for(MesProduct product : findAll){
			list.add(product.getModelnum());
		}
		return list;
	}

	@Override
	public MesProduct findByCompanyIdAndModelnum(Long companyId, String modelnum) {
		return dao.findBycompanyinfoIdAndModelnum(companyId,modelnum);
	}

	
}
