package com.its.frd.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.its.common.controller.BaseController;
import com.its.common.util.dwz.AjaxObject;
import com.its.common.util.dwz.Page;
import com.its.common.util.persistence.DynamicSpecifications;
import com.its.common.util.persistence.SearchFilter;
import com.its.common.util.persistence.SearchFilter.Operator;
import com.its.frd.entity.MesQdas;
import com.its.frd.entity.MesQdasCategory;
import com.its.frd.service.QdasCategoryService;
import com.its.frd.service.QdasService;
import com.its.statistics.service.StatisticsService;
import com.its.statistics.vo.AnalyzeSearch;

@Controller
@RequestMapping("/qdas")
public class QdasController extends BaseController{
	@Autowired
	private QdasService qdasService;
	@Autowired
	private QdasCategoryService qdasCategoryService;
	@Autowired
	private StatisticsService statisticsService;
	@RequestMapping("/qdas")
	public ModelAndView qdas(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("Qdas/qdas");
		List<SearchFilter> searchFilters = new ArrayList<>();
		searchFilters.add(new SearchFilter("isexport", Operator.EQ, 0));
		List<MesQdasCategory> list = qdasCategoryService.findAll(DynamicSpecifications.bySearchFilter(MesQdasCategory.class, searchFilters));
		modelAndView.addObject("qdasCategoryList", list);
		return modelAndView;
	}
	@RequestMapping("/toAddPage/{id}")
	public ModelAndView toAddPage(HttpServletRequest request,@PathVariable long id,MesQdas mesQdas) {
		ModelAndView modelAndView = new ModelAndView("Qdas/qdasAdd");
		//modelAndView.addObject("parameters", qdasService.findByIsexport(0));
		List<SearchFilter> searchFilters = new ArrayList<>();
		searchFilters.add(new SearchFilter("isexport", Operator.EQ, 0));
		List<MesQdasCategory> list = qdasCategoryService.findAll(DynamicSpecifications.bySearchFilter(MesQdasCategory.class, searchFilters));
		modelAndView.addObject("qdasCategoryList", list);
		if(id != 0){
			mesQdas = this.qdasService.findById(id);
			MesQdasCategory mesQdasCategory = mesQdas.getMesqdascategory();
			modelAndView.addObject("id", id);
			try {
				modelAndView.addObject("mesQdasCategory", new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS).writeValueAsString(mesQdasCategory));
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return modelAndView;
	}
	@RequestMapping("/generateQdasSelect")
	public @ResponseBody String generateQdasSelect(HttpServletRequest request,MesQdasCategory mesQdasCategory){
		MesQdasCategory existMesQdasCategory = this.qdasCategoryService.findById(mesQdasCategory.getId());
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		try {
			return mapper.writeValueAsString(existMesQdasCategory.getMesQdasList());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}
	@RequestMapping("/data")
	@ResponseBody
	public String data(HttpServletRequest request, Page page) throws JsonProcessingException {
		List<MesQdas> list = new ArrayList<MesQdas>();
		Map<String, Object> map = new HashMap<String, Object>();
		Specification<MesQdas> specification = DynamicSpecifications.bySearchFilter(request, MesQdas.class
		        ,new SearchFilter("mesqdascategory.isexport",Operator.EQ,"0")
		        ,new SearchFilter("keyfield",Operator.NOTEQ,"")
		        );
		list = qdasService.findPage(specification, page);
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		map.put("page", page);
		map.put("qdas", list);

		return mapper.writeValueAsString(map);
	}
	@RequestMapping("/addOrUpdate")
	@ResponseBody
	public String addOrUpdate(HttpServletRequest request,MesQdas mesQdas) throws JsonProcessingException {
		MesQdas qdas = qdasService.findById(mesQdas.getId());
		qdas.setKeyfield(mesQdas.getKeyfield());
		MesQdas returnQdas = qdasService.saveOrUpdate(qdas);
		if(null != returnQdas){
			return  AjaxObject.newOk( "新增/修改成功!").toString();
		}else{
			return AjaxObject.newError("新增/修改失败").toString();
		}
	}
	@RequestMapping("/exportQdasPage")
	@ResponseBody
	public ModelAndView exportQdasPage(HttpServletRequest request,AnalyzeSearch analyzeSearch) throws JsonProcessingException {
		ModelAndView modelAndView = new ModelAndView("Qdas/exportQdas");
		modelAndView.addObject("products", statisticsService.getProductByCurrentCompanyId());
		return modelAndView;
	}
	
	/*@RequestMapping("/exportQdas")
	public String exportQdas(AnalyzeSearch analyzeSearch , HttpServletRequest request, HttpServletResponse response) throws IOException, ClassNotFoundException{
		String result = statisticsService.ExportQdasData(analyzeSearch,null);
		String path = DynamicSpecifications.getRequest().getSession().getServletContext().getRealPath("/");
		File file = new File(path+"\\qdas."+analyzeSearch.getSuffix());
		OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
		org.apache.commons.io.FileUtils.writeStringToFile(file, result);
		IOUtils.closeQuietly(out);
		response.setContentType("application/force-download");// 设置强制下载不打开
		response.addHeader("Content-Disposition","attachment;fileName=qdas." + analyzeSearch.getSuffix());// 设置文件名
		byte[] buffer = new byte[1024];
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		try {
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);
			OutputStream os = response.getOutputStream();
			int i = bis.read(buffer);
			while (i != -1) {
				os.write(buffer, 0, i);
				i = bis.read(buffer);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (fis != null) {
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}*/
	
	/**
	 * 判断在增加或修改操作中是否存在重复的K值
	 * @param keyValue
	 * @param editId
	 * @return
	 */
	@RequestMapping("/checkValueQualified")
	@ResponseBody
	public String checkValueQualified(String keyValue,Long editId){
		//查找所有的已存在的Qdas的K值
		List<String> kValueList = new ArrayList<>();
		List<MesQdas> qdasList = qdasService.findAll();
		for(MesQdas md : qdasList){
			if(StringUtils.isNotEmpty(md.getKeyfield())){
				kValueList.add(md.getKeyfield());
			}
		}
		boolean flag = true;
		//修改操作
		if(null != editId && 0!=editId){
			MesQdas qdas = qdasService.findById(editId);
			if(keyValue.equals(qdas.getKeyfield())){
				return "ok";
			}
		}
		//增加操作
		for(String st : kValueList){
			if(st.equals(keyValue)){
				flag = false;
				break;
			}
		}	
		if(flag){
			return "ok";
		}else{
			return "error";
		}
	}


}
