package com.its.frd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.its.common.entity.main.User;
import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.frd.dao.UsercompanysDao;
import com.its.frd.entity.Companyinfo;
import com.its.frd.entity.Usercompanys;
import com.its.frd.service.UsercompanysService;
@Service
public class UsercompanysServiceImp implements UsercompanysService {
	@Resource
	private UsercompanysDao ucDao;
	
	@Override
	public List<Usercompanys> findPage(Specification<Usercompanys> specification, Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<Usercompanys> pageData = ucDao.findAll(specification, PageUtils.createPageable(page));
		page.setTotalCount(pageData.getTotalElements());
		return pageData.getContent();
	}

	@Override
	public void saveOrUpdateInfo(Usercompanys info) {
		ucDao.save(info);
	}

	@Override
	public void deleteinfoByUserIdAndCompanyId(Long userid,Long companyid) {
		List<Usercompanys> list = ucDao.findByUseridAndCompanyinfoId(userid,companyid);
		ucDao.delete(list);
	}

    @Override
    public Usercompanys findById(Long id) {
            return ucDao.findOne(id);
        }

	@Override
	public List<Usercompanys> findPage(Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<Usercompanys> pageData = ucDao.findAll(PageUtils.createPageable(page));
		page.setTotalCount(pageData.getTotalElements());
		return pageData.getContent();
	}
    
    @Override
    public List<Usercompanys> findByUserid(Long userid) {
        return ucDao.findByUserid(userid);
    }

    @Override
    public List<Usercompanys> findByCompanyid(Long companyid) {
        return ucDao.findByCompanyinfoId(companyid);
    }

	@Override
	public void deleteCompanyInfoByUserId(Long userId) {
		ucDao.deleteByUserid(userId);
	}

}
