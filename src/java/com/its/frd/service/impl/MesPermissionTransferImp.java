package com.its.frd.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.frd.dao.MesPermissionTransferDao;
import com.its.frd.entity.Companyinfo;
import com.its.frd.entity.MesDriver;
import com.its.frd.entity.MesPermissionTransfer;
import com.its.frd.service.MesPermissionTransferService;
@Service
public class MesPermissionTransferImp implements MesPermissionTransferService {
	@Resource
	private MesPermissionTransferDao dao;
	
	@Override
	public List<MesPermissionTransfer> findPage(Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesPermissionTransfer> springDataPage = dao.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}
	@Override
	public List<MesPermissionTransfer> findPage(Specification<MesPermissionTransfer> specification, Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesPermissionTransfer> springDataPage = dao.findAll(specification,PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public MesPermissionTransfer saveOrUpdate(MesPermissionTransfer t) {
		return dao.save(t);
	}
	
	@Override
	public List<MesPermissionTransfer> findByPage(Specification<MesPermissionTransfer> specification, Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesPermissionTransfer> springDataPage = dao.findAll(specification,PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

}
