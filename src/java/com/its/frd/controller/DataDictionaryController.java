package com.its.frd.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.its.common.SecurityConstants;
import com.its.common.exception.ServiceException;
import com.its.common.log.Log;
import com.its.common.shiro.ShiroUser;
import com.its.common.util.dwz.AjaxObject;
import com.its.common.util.dwz.Page;
import com.its.common.util.persistence.DynamicSpecifications;
import com.its.common.util.persistence.SearchFilter;
import com.its.common.util.persistence.SearchFilter.Operator;
import com.its.common.utils.SecurityUtils;
import com.its.frd.entity.DataDictionary;
import com.its.frd.service.DataDictionaryService;
import com.its.frd.entity.DataDictionary.DictionaryType;

@Controller
@RequestMapping("DataDictionary")
public class DataDictionaryController {

	@Autowired
	private DataDictionaryService DataDictionaryService;

	private static final String CREATE = "DataDictionary/create";
	private static final String UPDATE = "DataDictionary/update";
	private static final String LIST = "DataDictionary/list";
	private static final String VIEW = "DataDictionary/view";

	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public String list(Long id, ServletRequest request, Page page, Map<String, Object> map) {
		Specification<DataDictionary> specification = null;
		if (id != null) {
			specification = DynamicSpecifications.bySearchFilter(request, DataDictionary.class,
					new SearchFilter("parent.id", Operator.EQ, id));
			map.put("dictionaryType", DictionaryType.ITEM.name());
			map.put("pDictionary", DataDictionaryService.get(id));
		} else {
			specification = DynamicSpecifications.bySearchFilter(request, DataDictionary.class,
					new SearchFilter("type", Operator.EQ, DictionaryType.THEME.name()));
			map.put("dictionaryType", DictionaryType.THEME.name());
		}
		List<DataDictionary> DataDictionary = DataDictionaryService.findByExample(specification, page);
		ShiroUser shiroUser = SecurityUtils.getShiroUser();
		map.put(SecurityConstants.LOGIN_USER, shiroUser.getUser());
		map.put("page", page);
		map.put("DataDictionary", DataDictionary);
		return LIST;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String preCreate(Map<String, Object> map) {
		return CREATE;
	}

	@Log(message = "添加了id={0}数据字典。")
	@RequestMapping(value = "/createDictionary", method = RequestMethod.POST)
	public @ResponseBody String create(@Valid DataDictionary DataDictionary) {
		DataDictionaryService.saveOrUpdate(DataDictionary);
		return AjaxObject.newOk("数据字典添加成功！").toString();
	}

	@ModelAttribute("preloadDictionary")
	public DataDictionary preload(@RequestParam(value = "id", required = false) Long id) {
		if (id != null) {
			DataDictionary DataDictionary = DataDictionaryService.get(id);
			return DataDictionary;
		}
		return null;
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public String preUpdate(@PathVariable Long id, Map<String, Object> map) {
		DataDictionary DataDictionary = DataDictionaryService.get(id);
		map.put("DataDictionary", DataDictionary);
		return UPDATE;
	}

	@Log(message = "修改了id={0}数据字典的信息。")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody String update(@Valid @ModelAttribute("preloadDictionary") DataDictionary DataDictionary) {
		DataDictionaryService.saveOrUpdate(DataDictionary);
		return AjaxObject.newOk("数据字典修改成功！").toString();
	}

	@Log(message = "删除了{0}数据字典。")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	public @ResponseBody String delete(@PathVariable Long id) {
		DataDictionary datadictionary = null;
		try {
			datadictionary = DataDictionaryService.get(id);
			DataDictionaryService.delete(datadictionary.getId());
		} catch (ServiceException e) {
			return AjaxObject.newError("删除字典失败!").setCallbackType("").toString();
		}
		return AjaxObject.newOk("删除字典成功！").setCallbackType("").toString();
	}

	@Log(message = "批量删除了id={0}数据字典。")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody String deleteMany(Long[] ids) {
		for (int i = 0; i < ids.length; i++) {
			DataDictionary datadictionary = DataDictionaryService.get(ids[i]);
			DataDictionaryService.delete(datadictionary.getId());
		}
		return AjaxObject.newOk("数据字典删除成功！").setCallbackType("").toString();
	}

	@RequestMapping(value = "/view/{id}", method = { RequestMethod.GET })
	public String view(@PathVariable Long id, Map<String, Object> map) {
		map.put("pDictionary", DataDictionaryService.get(id));
		return VIEW;
	}

	@RequestMapping(value = "/data/{id}")
	@ResponseBody
	public Map<String, Object> dataList(@PathVariable Long id, ServletRequest request, Page page) {
		Map<String, Object> map = new HashMap<String, Object>();
		Specification<DataDictionary> specification = null;
		if (id != 0) {
			specification = DynamicSpecifications.bySearchFilter(request, DataDictionary.class,
					new SearchFilter("parent.id", Operator.EQ, id));
			map.put("dictionaryType", DictionaryType.ITEM.name());
			map.put("pDictionary", DataDictionaryService.get(id));
		} else {
			specification = DynamicSpecifications.bySearchFilter(request, DataDictionary.class,
					new SearchFilter("type", Operator.EQ, DictionaryType.THEME.name()));
			map.put("dictionaryType", DictionaryType.THEME.name());
		}
		List<DataDictionary> DataDictionary = DataDictionaryService.findByExample(specification, page);
		map.put("page", page);
		map.put("DataDictionary", DataDictionary);
		return map;
	}
    @RequestMapping("/checkName/{name}")
    @ResponseBody
    public String checkName(@PathVariable String name,HttpServletRequest request) throws JsonProcessingException{
    	Map<String, Object> map = new HashMap<String, Object>();
    	ObjectMapper mapper = new ObjectMapper();
    	mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    	try {
    		Page page = new Page();
    		page.setNumPerPage(Integer.MAX_VALUE);
    		Specification<DataDictionary> specification = DynamicSpecifications.bySearchFilter(request, DataDictionary.class
    				,new SearchFilter("name", Operator.EQ, name)
    				);
    		List<DataDictionary> dictionary = DataDictionaryService.findByExample(specification, page);
    		if(dictionary.size() > 0){
    			map.put("0", 0);
    		}else {
    			map.put("1", 1);
    		}
    	} catch (Exception e) {
    		return null;
    	}
    	return mapper.writeValueAsString(map);
    }
}