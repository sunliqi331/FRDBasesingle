package com.its.frd.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.its.common.service.RedisService;
import com.its.common.util.dwz.AjaxObject;
import com.its.common.util.dwz.Page;
import com.its.common.util.persistence.DynamicSpecifications;
import com.its.common.util.persistence.SearchFilter;
import com.its.common.util.persistence.SearchFilter.Operator;
import com.its.common.utils.SecurityUtils;
import com.its.frd.entity.Companyinfo;
import com.its.frd.entity.MesPointGateway;
import com.its.frd.entity.MesPoints;
import com.its.frd.entity.MesProcedureProperty;
import com.its.frd.entity.MesProduct;
import com.its.frd.entity.MesProductProcedure;
import com.its.frd.entity.MesProductline;
import com.its.frd.params.SendTemplate;
import com.its.frd.service.CompanyinfoService;
import com.its.frd.service.MesDriverService;
import com.its.frd.service.MesProductService;
import com.its.frd.service.ProductAndEnergyAndDriverChartService;
import com.its.monitor.service.MesPointsTemplateService;
import com.its.monitor.vo.MesPointsTemplate;

@Controller
@RequestMapping("/product")
public class MesProductController {
	private final String PRE_PAGE = "product/";

	@Resource
	private MesProductService productServ;
	@Resource
	private MesDriverService driverServ;
	@Resource
	private CompanyinfoService cpServ;
	@Resource
	private MesPointsTemplateService mesPointsTemplateService;
	@Resource
	private ProductAndEnergyAndDriverChartService productChartServ;
	@Resource
	private RedisService redisServ;

	@RequiresPermissions("Product:view")
	@RequestMapping("/productList")
	public String productList() {
		Long companyId = SecurityUtils.getShiroUser().getCompanyid();
		if(companyId == null)
			return "error/403";
		return PRE_PAGE + "productList";
	}
	
	@RequiresPermissions("Product:save")
	@RequestMapping("/addProduct")
	public String addProduct(HttpServletRequest request,Page page,Map<String,Object> map) {
		/*Specification<Companyinfo> specification = DynamicSpecifications.bySearchFilter(request, Companyinfo.class,
				new SearchFilter("companytype",Operator.EQ,"factory"),
				new SearchFilter("parentid",Operator.EQ,SecurityUtils.getShiroUser().getCompanyid()),
				new SearchFilter("companystatus", Operator.EQ,"1"));
		List<Companyinfo> company = cpServ.findPage(specification, page);
		map.put("company", company);*/
		return PRE_PAGE + "addProduct";
	}
	
	
	//update : 防止出现产品挂到工厂下面的情况出现，查询时添加公司子工厂
	/**
	 * 分页
	 * @param request
	 * @param page
	 * @return
	 * @throws JsonProcessingException 
	 */
	@RequestMapping("/productData")
	@ResponseBody
	public String data(HttpServletRequest request, Page page) throws JsonProcessingException {
		Map<String, Object> map = new HashMap<String, Object>();
		//		Long companyId = (long)110;
		Long companyId = SecurityUtils.getShiroUser().getCompanyid();
		//查询出当前公司下的所有子工厂 ，记录id
		List<Companyinfo> finfos = new ArrayList<>();
		List<Companyinfo> factorys = productChartServ.getFactoryList(companyId);
		List<Companyinfo> sonFactorys = null;
		for(Companyinfo factory : factorys){
			sonFactorys = productChartServ.getFactoryListByFactoryId(factory.getId());
			finfos.addAll(sonFactorys);
		}
		List<Long> mesProductCompanyIds = new ArrayList<Long>(); //该公司下所有工厂的id
		mesProductCompanyIds.add(companyId);
		for(Companyinfo ci : finfos){
			mesProductCompanyIds.add(ci.getId());
		}
		Specification<MesProduct> specification = DynamicSpecifications.bySearchFilter(request, MesProduct.class,
				new SearchFilter("companyinfo.id",Operator.IN,mesProductCompanyIds.toArray()));
		List<MesProduct> mp = productServ.findPage(specification, page);
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		map.put("page", page);
		map.put("mesProducts", mp);
		return mapper.writeValueAsString(map);
		//		return map;
	}
	/*
	 * 根据公司id获取产品
	 * */
	@RequestMapping("/getProductByCompanyid/{companyid}")
	@ResponseBody
	public String getProductByCompanyid(@PathVariable Long companyid,HttpServletRequest request,Page page) throws JsonProcessingException{
		Specification<MesProduct> specification = DynamicSpecifications.bySearchFilter(request, MesProduct.class,
				new SearchFilter("companyinfo.id",Operator.EQ,companyid)
				,new SearchFilter("companyinfo.companytype", Operator.EQ, "factory")
				);
		List<MesProduct> MesProduct =  productServ.findPage(specification, page);
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		return  mapper.writeValueAsString(MesProduct);
	}

	/**
	 * 添加数据
	 * @param MesProduct
	 * @return
	 */
	@RequiresPermissions(value={"Product:save","Product:edit"},logical=Logical.OR)
	@RequestMapping("/saveProduct")
	@ResponseBody
	@SendTemplate
	public String saveDriver(MesProduct mesProduct,HttpServletRequest request,Page page){
		String msg = "修改";
		if(mesProduct.getId() == null)
			msg = "添加";
		try {
			Long companyId = SecurityUtils.getShiroUser().getCompanyid();
			Specification<MesProduct> specification = DynamicSpecifications.bySearchFilter(request, MesProduct.class,
					new SearchFilter("companyinfo.id", Operator.EQ,companyId));
			List<MesProduct> mp = productServ.findPage(specification, page);
			for(MesProduct mps : mp) {
				if(mesProduct.getId() == null || mps.getId().compareTo(mesProduct.getId()) != 0) {
//					if(mps.getProductnum().equals(mesProduct.getProductnum()))
//						return AjaxObject.newError("该产品编号在该公司已存在！").setCallbackType("").toString();
					if(mps.getName().equals(mesProduct.getName()))
						return AjaxObject.newError("该产品名称在该公司已存在！").setCallbackType("").toString();
					if(mps.getModelnum().equals(mesProduct.getModelnum()))
						return AjaxObject.newError("该产品型号在该公司已存在！").setCallbackType("").toString();
				}
			}
			mesProduct.setCompanyinfo(cpServ.findById(companyId));
			productServ.saveOrUpdate(mesProduct);
			Set<String> macs = new HashSet<>();
			for(MesProductProcedure mesProductProcedure : mesProduct.getMesProductProcedures()){
				if(mesProductProcedure != null){
					continue;
				}
				for(MesProcedureProperty mesProcedureProperty : mesProductProcedure.getMesProcedureProperties()){
					if(mesProcedureProperty == null){
						continue;
					}
					MesPoints mesPoints = mesProcedureProperty.getMesPoints();
					if(null != mesPoints){
						MesPointGateway gateway = mesPoints.getMesPointGateway();
						macs.add(gateway.getMac());
						String result = mesPointsTemplateService.getTemplate(mesPoints);
						String key = MesPointsTemplate.class.getSimpleName()+"_"+gateway.getMac();
						Map<String, Object> hash = redisServ.getHash(key);
						if(hash == null){
							hash = new HashMap<String,Object>();
						}
						hash.put(mesPoints.getCodekey(), result);
						redisServ.setHash(key, hash);
					}
					
				}
			}
			for(String mac : macs){
				mesPointsTemplateService.sendTemplate(mac);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxObject.newError(msg + "失败!").setCallbackType("").toString();
		}
		return AjaxObject.newOk(msg + "成功!").toString();
	}


	@RequiresPermissions("Product:delete")
	@RequestMapping("/deleteProduct")
	@ResponseBody
	@SendTemplate
	public String deleteSubDriver(Long[] ids){
		try {
			Set<String> macs = new HashSet<>();
			for (int i = 0; i < ids.length; i++) {
				MesProduct mesProduct = productServ.findById(ids[i]);
				for(MesProductProcedure mesProductProcedure : mesProduct.getMesProductProcedures()){
					for(MesProcedureProperty mesProcedureProperty : mesProductProcedure.getMesProcedureProperties()){
						MesPoints mesPoints = mesProcedureProperty.getMesPoints();
						if(null != mesPoints){
							MesPointGateway gateway = mesPoints.getMesPointGateway();
							macs.add(gateway.getMac());
							String result = mesPointsTemplateService.getTemplate(mesPoints);
							String key = MesPointsTemplate.class.getSimpleName()+"_"+gateway.getMac();
							Map<String, Object> hash = redisServ.getHash(key);
							if(hash == null){
								hash = new HashMap<String,Object>();
							}
							hash.put(mesPoints.getCodekey(), result);
							redisServ.setHash(key, hash);
						}
						
					}
				}
				productServ.deleteById(ids[i]);
			}
			for(String mac : macs){
				mesPointsTemplateService.sendTemplate(mac);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxObject.newError("删除失败!").setCallbackType("").toString();
		}
		return AjaxObject.newOk("删除成功!").setCallbackType("").toString();
	}

	/**
	 * 根据id查找
	 * @param id
	 * @param model
	 * @return
	 */
	@RequiresPermissions("Product:edit")
	@RequestMapping("/findById/{id}")
	public String findByID(@PathVariable Long id,Model model,HttpServletRequest request,Page page,Map<String, Object> map){
		model.addAttribute("mesProduct",productServ.findById(id));
		/*Specification<Companyinfo> specification = DynamicSpecifications.bySearchFilter(request, Companyinfo.class,
				new SearchFilter("companytype",Operator.EQ,"factory"),
				new SearchFilter("parentid",Operator.EQ,SecurityUtils.getShiroUser().getCompanyid()),
				new SearchFilter("companystatus", Operator.EQ,"1"));
		List<Companyinfo> company = cpServ.findPage(specification, page);
		map.put("company", company);*/
		return PRE_PAGE + "editProduct";
	}
}
