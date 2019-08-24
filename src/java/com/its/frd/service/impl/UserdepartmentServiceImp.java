package com.its.frd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.frd.dao.UserdepartmentDao;
import com.its.frd.entity.Userdepartment;
import com.its.frd.service.UserdepartmentService;
@Service
public class UserdepartmentServiceImp implements UserdepartmentService {
	@Resource
	private UserdepartmentDao dao;
	
	
	@Override
	public Userdepartment findById(Long id) {
		return dao.findOne(id);
	}

	@Override
	public void deleteById(Long id) {
		dao.delete(id);
	}

	@Override
	public List<Userdepartment> findPage(Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<Userdepartment> springDataPage = dao.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<Userdepartment> findPage(Specification<Userdepartment> specification, Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<Userdepartment> springDataPage = dao.findAll(specification,PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public Userdepartment saveOrUpdate(Userdepartment t) {
		return dao.save(t);
	}

    @Override
    public List<Userdepartment> findByUserid(Long userid) {
        return dao.findByUserid(userid);
    }

	@Override
	public List<Userdepartment> findByDepartmentid(Long departmentid) {
		return dao.findByDepartmentid(departmentid);
	}

    @Override
    public Userdepartment findByUseridAndDepartmentid(Long userid, Long departmentid) {
        return dao.findByUseridAndDepartmentid(userid, departmentid);
    }


}
