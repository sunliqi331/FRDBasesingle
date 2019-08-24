package	com.its.frd.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.frd.dao.UserCompanyroleDao;
import com.its.frd.entity.UserCompanyrole;
import com.its.frd.service.UserCompanyroleService;

@Service
@Transactional
public class UserCompanyroleServiceImpl implements UserCompanyroleService {
	
	@Autowired
	private UserCompanyroleDao UserCompanyroleDAO;

	@Override
	public UserCompanyrole get(Long id) {
		return UserCompanyroleDAO.findOne(id);
	}

	@Override
	public void saveOrUpdate(UserCompanyrole UserCompanyrole) {
		UserCompanyroleDAO.save(UserCompanyrole);
	}

	@Override
	public void delete(Long id) {
		UserCompanyroleDAO.delete(id);
	}
	
	@Override
	public List<UserCompanyrole> findAll(Page page) {
		org.springframework.data.domain.Page<UserCompanyrole> springDataPage = UserCompanyroleDAO.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}
	
	@Override
	public List<UserCompanyrole> findByExample(
			Specification<UserCompanyrole> specification, Page page) {
		org.springframework.data.domain.Page<UserCompanyrole> springDataPage = UserCompanyroleDAO.findAll(specification, PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<UserCompanyrole> findByUserId(Long userId) {
		return UserCompanyroleDAO.findByUserId(userId);
	}

	@Override
	public void deleteByUserId(Long userId) {
		UserCompanyroleDAO.deleteByUserId(userId);
	}

	@Override
	public List<UserCompanyrole> findByUserIdAndMesCompanyRoleCompanyid(Long id, Long companyid) {
		// TODO Auto-generated method stub
		return UserCompanyroleDAO.findByUserIdAndMesCompanyRoleCompanyid(id, companyid);
	}
	
}
