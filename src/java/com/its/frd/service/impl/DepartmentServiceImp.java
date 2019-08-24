package com.its.frd.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.common.utils.SecurityUtils;
import com.its.frd.dao.DepartmentDao;
import com.its.frd.entity.Companyinfo;
import com.its.frd.entity.Department;
import com.its.frd.params.ZTree;
import com.its.frd.service.CompanyinfoService;
import com.its.frd.service.DepartmentService;
@Service
public class DepartmentServiceImp implements DepartmentService {

	@Resource
	private DepartmentDao dtDao;
	
	@Autowired
	private CompanyinfoService companyinfoService;

	@Override
	public List<Department> findPage(Specification<Department> specification, Page page) {
		page.setOrderField("id");
		
		org.springframework.data.domain.Page<Department> springDataPage = dtDao.findAll(specification,PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public Department findById(Long id) {
		return dtDao.findOne(id);
	}

	@Override
	public void saveOrUpdate(Department info) {
		dtDao.save(info);
	}

	@Override
	public void deleteById(Long id) {
		dtDao.delete(id);
	}

	@Override
	public Department getTree() {
		//        List<Department> list = dtDao.findAllWithCache();
		Department department = new Department();
		List<Department> list = dtDao.findByCompanyid(SecurityUtils.getShiroUser().getCompanyid());

		List<Department> rootList = makeTree(list);
		if(rootList.size()<=0){
			return department;
		}
		return rootList.get(0);
	}

	private List<Department> makeTree(List<Department> list) {
		List<Department> parent = new ArrayList<Department>();
		// get parentId = null;
		for (Department e : list) {
			//如果父节点为空
			if (e.getDepartment() == null) {
				e.setDepartments(new ArrayList<Department>(0));
				//将父节点为空的节点存入parentList里
				parent.add(e);
			}
		}
		// 删除parentId = null;
		list.removeAll(parent);

		makeChildren(parent, list);

		return parent;
	}

	private void makeChildren(List<Department> parent, List<Department> children) {
		//判断子节点是否为空
		if (children.isEmpty()) {
			return ;
		}

		List<Department> tmp = new ArrayList<Department>();
		for (Department c1 : parent) {
			for (Department c2 : children) {
				c2.setDepartments(new ArrayList<Department>(0));
				if (c1.getId().equals(c2.getDepartment().getId())) {
					c1.getDepartments().add(c2);
					tmp.add(c2);
				}
			}
		}

		children.removeAll(tmp);

		makeChildren(tmp, children);
	}

	@Override
	public Department findByDepartmentIdAndCompanyid(Long companyid) {
		return dtDao.findByDepartmentIdAndCompanyid(companyid);
	}

	@Override
	public List<Department> findByCompanyid(Long comapnyid) {
		return dtDao.findByCompanyid(comapnyid);
	}

    @Override
    public List<Department> findByCompanyinfoidAndSn(Long companyid, String sn) {
        return dtDao.findByCompanyinfoidAndSn(companyid, sn);
    }

    @Override
    public List<Department> findByCompanyinfoidAndName(Long companyid, String name) {
        return dtDao.findByCompanyinfoidAndName(companyid, name);
    }

	@Override
	public List<Department> findByParentid(Long parentId) {
		return dtDao.findByDepartment(dtDao.findOne(parentId));
	}


	@Override
	public boolean verfyDept(Long id) {
		boolean key = false;
		Department dep = dtDao.verfyDept(id);
		if(dep!=null){
			key = true;
		}
		return key;
	}
	@Override
	public String getTree(Long comapnyid) {
		// TODO Auto-generated method stub
		Companyinfo companyinfo = companyinfoService.findById(comapnyid);
		ZTree zTreeRoot = new ZTree();
		zTreeRoot.setName(companyinfo.getCompanyname());
		ZTree.CustomAttr attr = zTreeRoot.new CustomAttr(Department.class.getSimpleName(),companyinfo);
		zTreeRoot.getCustomAttrList().add(attr);
		List<Department> departments = dtDao.findByCompanyid(comapnyid);
		generateDepartmentTree(departments, zTreeRoot);
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		try {
			return mapper.writeValueAsString(zTreeRoot);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return new JSONObject().toString();
	}

	private void generateDepartmentTree(List<Department> departments,ZTree ztree){
		for(Department department : departments){
			ZTree zTree = new ZTree();
			zTree.setName(department.getName());
			ZTree.CustomAttr attr = zTree.new CustomAttr(Department.class.getSimpleName(),department);
			zTree.getCustomAttrList().add(attr);
			List<Department> departmentsList = department.getDepartments();
			if(departmentsList.size() != 0){
				generateDepartmentTree(departmentsList,zTree);
			}
			ztree.getChildren().add(zTree);
			//list.add(zTree);
		}
	}
	
	@Override
	public List<Department> findForCompany(Long companyid) {
		return dtDao.findByCompanyidWithoutParentIdAndFactoryId(companyid);
	}
	
	@Override
	public List<Department> findForFactory(Long factoryId) {
		return dtDao.findByFactoryid(factoryId);
	}
	
}
