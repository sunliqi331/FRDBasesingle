package com.its.frd.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.common.utils.SecurityUtils;
import com.its.frd.dao.CompanyfileDao;
import com.its.frd.dao.CompanyinfoDao;
import com.its.frd.dao.UsercompanysDao;
import com.its.frd.entity.Companyfile;
import com.its.frd.entity.Companyinfo;
import com.its.frd.params.CompanyfileType;
import com.its.frd.service.CompanyinfoService;

@Service
public class CompanyinfoServiceImp implements CompanyinfoService {
	@Resource
	private CompanyinfoDao cpDao;
	@Resource
	private CompanyfileDao cfDao;
	@Resource
	private UsercompanysDao ucDao;
	
	@Override
	public List<Companyinfo> findPage(Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<Companyinfo> springDataPage = cpDao.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<Companyinfo> findPage(Specification<Companyinfo> specification, Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<Companyinfo> springDataPage = cpDao.findAll(specification,PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
		//return null;
	}

	@Override
	public void updateStatus(Long id,String status) {
		Companyinfo info = cpDao.findOne(id);
		info.setCompanystatus(status);
		cpDao.save(info);
	}

	@Override
	public Companyinfo findById(Long id) {
		return cpDao.findOne(id);
	}

	@Override
	public Companyinfo saveAndFile(Companyinfo companyinfo, List<Companyfile> companyfilelist) {
		Companyinfo companyinfo_ = cpDao.save(companyinfo);
		for(Companyfile cpfile : companyfilelist){
			cpfile.setParentid(companyinfo.getId());
			cpfile.setParenttype(CompanyfileType.COMPANYINFOFILE.toString());
			cfDao.save(cpfile);
		}
		return companyinfo_;
	}

	@Override
	public Companyinfo saveOrUpdate(Companyinfo info) {
		return cpDao.save(info);
	}

	@Override
	public List<Companyinfo> findByUserid(Long id) {
		return cpDao.findByUserid(id);
	}

	@Override
	public List<Companyinfo> findPage2() {
		
		return null;
	}

    @Override
    public Companyinfo findByCompanyname(String companyname) {
        return cpDao.findByCompanyname(companyname);
    }

	@Override
	public List<Companyinfo> findByParentidAndCompanytype(Long parentid, String companytype) {
		return cpDao.findByParentidAndCompanytype(parentid, companytype);
	}

	@Override
	public List<Companyinfo> findByCompanytypeAndCompanystatus(String companytype, String companystatus) {
		
		return cpDao.findByCompanytypeAndCompanystatus(companytype,companystatus);
	}
	
	@Override
	public List<Companyinfo> getTreeFactory(){
	    List<Companyinfo> treeList = new ArrayList<Companyinfo>();
//	    Companyinfo companyinfo = cpDao.findOne(SecurityUtils.getShiroUser().getCompanyid());
	    List<Companyinfo> factoryList = cpDao.findByParentId(SecurityUtils.getShiroUser().getCompanyid(),"3","factory"); //公司下面的工厂
//	    treeList.add(companyinfo); //根节点
	    //循环factoryListByCompanyId
	    for(Companyinfo companyinfo3 : factoryList)
	        this.makeSonFactory(treeList, companyinfo3);
	    return treeList;
	}
	
	private void makeSonFactory(List<Companyinfo> treeList,Companyinfo companyinfo){
	    List<Companyinfo> sonList = cpDao.findByParentId(companyinfo.getId(),"3","factory");//为companyinfo的子工厂
	    treeList.add(companyinfo);
	    if(sonList == null){
	        return;
	    }else{
	        for(Companyinfo companyinfo2 : sonList)
	            this.makeSonFactory(treeList, companyinfo2);
	    }
	    
	}
	
	@Override
	public Companyinfo getCompanyTree(){
	    Companyinfo companyinfo = cpDao.findOne(SecurityUtils.getShiroUser().getCompanyid());
	    List<Companyinfo> sonList = cpDao.findByParentId(companyinfo.getId(),"3","factory");//公司下的第二级目录
	    companyinfo.setSonCompanyinfo(sonList);
	    for(Companyinfo companyinfo2 : sonList){
	        this.getSonFactory(companyinfo2);
	    }
	    return companyinfo;
	}
	
	private void getSonFactory(Companyinfo cp){
	    List<Companyinfo> list = cpDao.findByParentId(cp.getId(),"3","factory");
	    cp.setSonCompanyinfo(list);
	    for(Companyinfo companyinfo2 : list)
	        this.getSonFactory(companyinfo2);
	}

    @Override
    public List<Companyinfo> findByParentId(Long parentId, String companystatus, String companytype) {
        return cpDao.findByParentId(parentId, companystatus, companytype);
    }

	@Override
	public void saveOrUpdateForfile(Companyinfo info, List<Companyfile> files, String[] pictureids) {
		cpDao.save(info);
		List<Companyfile> cpfiles = cfDao.findByParentidAndParenttypeOrderByIdAsc(info.getId(),CompanyfileType.COMPANYINFOFILE.toString());
		for(Companyfile file : cpfiles){
			boolean isDel = false;
			for(String str : pictureids){
				if(file.getId().toString().equals(str)){
					isDel = false;
					break;
				}
				isDel = true;
			}
			if(isDel)
				cfDao.delete(file.getId());
		}
		
		for(Companyfile file : files){
			file.setParentid(info.getId());
			file.setParenttype(CompanyfileType.COMPANYINFOFILE.toString());
			cfDao.save(file);
		}
	}

	@Override
	public void deleteById(Long id) {
			cpDao.delete(id);
	}

	@Override
	public List<Companyinfo> findByIdInAndCompanystatusAndCompanytype(List<Long> ids, String status, String type) {
		if(ids == null || status == null || type == null)
			return null;
		return cpDao.findByIdInAndCompanystatusAndCompanytype(ids, status, type);
	}

	@Override
	public List<Companyinfo> findAll() {
		// TODO Auto-generated method stub
		return cpDao.findAll();
	}

	
}
