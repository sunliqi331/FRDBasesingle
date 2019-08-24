package com.its.common.service.impl.component;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.its.common.dao.component.ResourceDAO;
import com.its.common.entity.component.Resource;
import com.its.common.service.component.ResourceService;
import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;

@Service
@Transactional
public class ResourceServiceImpl implements ResourceService {
	
	@Autowired
	private ResourceDAO resourceDAO;

	/* (non-Javadoc)
	 * @see com.ketayao.ketacustom.service.component.ResourceService#get(java.lang.Long)
	 */
	@Override
	public Resource get(Long id) {
		return resourceDAO.findOne(id);
	}

	/* (non-Javadoc)
	 * @see com.ketayao.ketacustom.service.component.ResourceService#saveOrUpdate(com.ketayao.ketacustom.entity.component.Resource)
	 */
	@Override
	public void saveOrUpdate(Resource resource) {
		resourceDAO.save(resource);
	}

	/* (non-Javadoc)
	 * @see com.ketayao.ketacustom.service.component.ResourceService#delete(java.lang.Long)
	 */
	@Override
	public void delete(Long id) {
		resourceDAO.delete(id);
	}

	/* (non-Javadoc)
	 * @see com.ketayao.ketacustom.service.component.ResourceService#findAll(com.ketayao.ketacustom.util.dwz.Page)
	 */
	@Override
	public List<Resource> findAll(Page page) {
		org.springframework.data.domain.Page<Resource> springDataPage = resourceDAO.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	/* (non-Javadoc)
	 * @see com.ketayao.ketacustom.service.component.ResourceService#findByExample(org.springframework.data.jpa.domain.Specification, com.ketayao.ketacustom.util.dwz.Page)
	 */
	@Override
	public List<Resource> findByExample(Specification<Resource> specification,
			Page page) {
		org.springframework.data.domain.Page<Resource> springDataPage = resourceDAO.findAll(specification, PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	/* (non-Javadoc)
	 * @see com.ketayao.ketacustom.service.component.ResourceService#getByUuid(java.lang.String)
	 */
	@Override
	public Resource getByUuid(String uuid) {
		return resourceDAO.getByUuid(uuid);
	}
}
