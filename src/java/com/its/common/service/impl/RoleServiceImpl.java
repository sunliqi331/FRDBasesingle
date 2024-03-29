package	com.its.common.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.its.common.dao.RoleDAO;
import com.its.common.entity.main.Role;
import com.its.common.service.RoleService;
import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
	
	@Autowired
	private RoleDAO roleDAO;

	/*
	 * (non-Javadoc)
	 * @see com.ketayao.ketacustom.service.RoleService#get(java.lang.Long)  
	 */ 
	@Override
	public Role get(Long id) {
		return roleDAO.findOne(id);
	}

	/*
	 * (non-Javadoc) 
	 * @see com.ketayao.ketacustom.service.RoleService#saveOrUpdate(com.ketayao.ketacustom.entity.main.Role)  
	 */
	@Override
	public void saveOrUpdate(Role role) {
		roleDAO.save(role);
	}

	/*
	 * (non-Javadoc)
	 * @see com.ketayao.ketacustom.service.RoleService#delete(java.lang.Long)  
	 */
	@Override
	public void delete(Long id) {
		roleDAO.delete(id);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.ketayao.ketacustom.service.RoleService#findAll(com.ketayao.ketacustom.util.dwz.Page)  
	 */
	@Override
	public List<Role> findAll(Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<Role> springDataPage = roleDAO.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.ketayao.ketacustom.service.RoleService#findByExample(org.springframework.data.jpa.domain.Specification, com.ketayao.ketacustom.util.dwz.Page)	
	 */
	@Override
	public List<Role> findByExample(
			Specification<Role> specification, Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<Role> springDataPage = roleDAO.findAll(specification, PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}
}
