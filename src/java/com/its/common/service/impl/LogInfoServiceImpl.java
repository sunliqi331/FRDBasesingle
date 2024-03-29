package	com.its.common.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.its.common.dao.LogInfoDAO;
import com.its.common.entity.main.LogInfo;
import com.its.common.service.LogInfoService;
import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;

@Service
@Transactional
public class LogInfoServiceImpl implements LogInfoService {
	
	@Autowired
	private LogInfoDAO logInfoDAO;

	/*
	 * (non-Javadoc)
	 * @see com.ketayao.ketacustom.service.LogInfoService#get(java.lang.Long)  
	 */ 
	@Override
	public LogInfo get(Long id) {
		return logInfoDAO.findOne(id);
	}

	/*
	 * (non-Javadoc) 
	 * @see com.ketayao.ketacustom.service.LogInfoService#saveOrUpdate(com.ketayao.ketacustom.entity.main.LogInfo)  
	 */
	@Override
	public void saveOrUpdate(LogInfo logInfo) {
		logInfoDAO.save(logInfo);
	}

	/*
	 * (non-Javadoc)
	 * @see com.ketayao.ketacustom.service.LogInfoService#delete(java.lang.Long)  
	 */
	@Override
	public void delete(Long id) {
		logInfoDAO.delete(id);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.ketayao.ketacustom.service.LogInfoService#findAll(com.ketayao.ketacustom.util.dwz.Page)  
	 */
	@Override
	public List<LogInfo> findAll(Page page) {
		org.springframework.data.domain.Page<LogInfo> springDataPage = logInfoDAO.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.ketayao.ketacustom.service.LogInfoService#findByExample(org.springframework.data.jpa.domain.Specification, com.ketayao.ketacustom.util.dwz.Page)	
	 */
	@Override
	public List<LogInfo> findByExample(
			Specification<LogInfo> specification, Page page) {
		org.springframework.data.domain.Page<LogInfo> springDataPage = logInfoDAO.findAll(specification, PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}
}
