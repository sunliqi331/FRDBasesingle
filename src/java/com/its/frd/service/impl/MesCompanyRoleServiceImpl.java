package	com.its.frd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.frd.dao.MesCompanyRoleDao;
import com.its.frd.entity.MesCompanyRole;
import com.its.frd.service.MesCompanyRoleService;

@Service
@Transactional
public class MesCompanyRoleServiceImpl implements MesCompanyRoleService {
	
	//@Autowired
	@Resource
	private MesCompanyRoleDao dao;

	@Override
	public MesCompanyRole get(Long id) {
		return dao.findOne(id);
	}

	@Override
	public void saveOrUpdate(MesCompanyRole MesCompanyRole) {
		dao.save(MesCompanyRole);
	}

	@Override
	public void delete(Long id) {
		dao.delete(id);
	}
	
	@Override
	public List<MesCompanyRole> findAll(Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesCompanyRole> springDataPage = dao.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}
	
	@Override
	public List<MesCompanyRole> findByExample(
			Specification<MesCompanyRole> specification, Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesCompanyRole> springDataPage = dao.findAll(specification, PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public MesCompanyRole findById(Long id) {
		return dao.findOne(id);
	}

    @Override
    public List<MesCompanyRole> findByCompanyid(Long companyid) {
        return dao.findByCompanyid(companyid);
    }
}
