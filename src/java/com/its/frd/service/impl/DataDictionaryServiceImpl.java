package	com.its.frd.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.frd.dao.DataDictionaryDAO;
import com.its.frd.entity.DataDictionary;
import com.its.frd.entity.DataDictionary.DictionaryType;
import com.its.frd.service.DataDictionaryService;

@Service
@Transactional
public class DataDictionaryServiceImpl implements DataDictionaryService{
	
	@Autowired
	private DataDictionaryDAO dictionaryDAO;

	@Override
	public DataDictionary get(Long id) {
		return dictionaryDAO.findOne(id);
	}

	@Override
	public void saveOrUpdate(DataDictionary dictionary) {
		dictionaryDAO.save(dictionary);
	}

	@Override
	public void delete(Long id) {
		dictionaryDAO.delete(id);
	}

	@Override
	public List<DataDictionary> findAll(Page page) {
		org.springframework.data.domain.Page<DataDictionary> springDataPage = dictionaryDAO.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<DataDictionary> findByExample(
			Specification<DataDictionary> specification, Page page) {
		org.springframework.data.domain.Page<DataDictionary> springDataPage = dictionaryDAO.findAll(specification, PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<DataDictionary> findByThemeName(String themeName, Page page) {
		if (page == null) {
			return dictionaryDAO.findAllItems(themeName);
		}
		org.springframework.data.domain.Page<DataDictionary> springDataPage = dictionaryDAO.findByParentNameAndType(themeName, DictionaryType.ITEM, PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}
}
