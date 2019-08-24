package	com.its.frd.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.frd.dao.MesCompanyRolePermissionDao;
import com.its.frd.entity.MesCompanyRolePermission;
import com.its.frd.service.MesCompanyRolePermissionService;

@Service
@Transactional
public class MesCompanyRolePermissionServiceImp implements MesCompanyRolePermissionService {
	
	@Autowired
	private MesCompanyRolePermissionDao dao;

	@Override
	public MesCompanyRolePermission get(Long id) {
		return dao.findOne(id);
	}

	@Override
	public void saveOrUpdate(MesCompanyRolePermission mesCompanyRolePermission) {
		dao.save(mesCompanyRolePermission);
	}
	@Override
	public void saveOrUpdates(List<MesCompanyRolePermission> rolePermissions) {
		dao.save(rolePermissions);
	}
	@Override
	public void delete(Long id) {
		dao.delete(id);
	}
	
	@Override
	public List<MesCompanyRolePermission> findAll(Page page) {
		org.springframework.data.domain.Page<MesCompanyRolePermission> springDataPage = dao.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}
	
	@Override
	public List<MesCompanyRolePermission> findByExample(
			Specification<MesCompanyRolePermission> specification, Page page) {
		org.springframework.data.domain.Page<MesCompanyRolePermission> springDataPage = dao.findAll(specification, PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<MesCompanyRolePermission> findByCompanyRoleId(Long id) {
		return dao.findByMesCompanyRoleId(id);
	}

	@Override
	public void save(List<MesCompanyRolePermission> newRList) {
		dao.save(newRList);
	}

	@Override
	public void delete(List<MesCompanyRolePermission> delRList) {
		dao.delete(delRList);
	}

}
