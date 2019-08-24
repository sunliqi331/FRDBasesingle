package com.its.frd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.frd.dao.MesDriverPropertyDao;
import com.its.frd.entity.MesDriverProperty;
import com.its.frd.service.MesDriverPropertyService;
@Service
public class MesDriverPropertyImp implements MesDriverPropertyService {
	@Resource
	private MesDriverPropertyDao dao;
	
	
	@Override
	public MesDriverProperty findById(Long id) {
		return dao.findOne(id);
	}

	@Override
	public void deleteById(Long id) {
		dao.delete(id);
	}

	@Override
	public List<MesDriverProperty> findPage(Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesDriverProperty> springDataPage = dao.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<MesDriverProperty> findPage(Specification<MesDriverProperty> specification, Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesDriverProperty> springDataPage = dao.findAll(specification,PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public MesDriverProperty saveOrUpdate(MesDriverProperty t) {
		return dao.save(t);
	}

    @Override
    public MesDriverProperty findByPropertyname(String propertyname) {
        return dao.findByPropertyname(propertyname);
    }

    @Override
    public List<MesDriverProperty> findByMesDriverid(Long mesDriverid) {
        return dao.findByMesDriverid(mesDriverid);
    }

    @Override
    public MesDriverProperty findByMesDriverAndPropertyname(String mesDriverName, String propertyname) {
        return dao.findByMesDriverAndPropertyname(mesDriverName, propertyname);
    }

}
