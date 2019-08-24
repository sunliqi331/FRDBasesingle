package com.its.frd.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
import com.its.frd.entity.MesPointGateway;
import com.its.frd.entity.MesPointType;
import com.its.frd.entity.MesPoints;
import com.its.frd.params.SendTemplate;
import com.its.frd.service.MesPointTypeService;
import com.its.monitor.service.MesPointsTemplateService;
import com.its.monitor.vo.MesPointsTemplate;

@Controller
@RequestMapping("/mesPointType")
public class MesPointTypeController {
	private final String PAGEPRE = "mesPointType/"; 

	@Resource
	private MesPointTypeService typeServ;
	@Resource
	private MesPointsTemplateService mesPointsTemplateService;
	
	@Resource
	private RedisService redisServ;
	
	@RequestMapping("/pointTypePage")
	public String pointListPage() {
		return PAGEPRE + "pointTypelist";
	}
	
	/**
	 * 分页
	 * @param request
	 * @param page
	 * @return
	 * @throws JsonProcessingException
	 */
	@RequestMapping("/data")
	@ResponseBody
	public String pointGatewayListData(HttpServletRequest request, Page page) throws JsonProcessingException{
		Map<String, Object> map = new HashMap<String, Object>();
        Specification<MesPointType> specification = DynamicSpecifications.bySearchFilter(request, MesPointType.class,
        				new SearchFilter("companyinfo.id",Operator.EQ,SecurityUtils.getShiroUser().getCompanyid()));      
        List<MesPointType> mesPoints = typeServ.findPage(specification, page);
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        map.put("page", page);
        map.put("meaPointType", mesPoints);
        return mapper.writeValueAsString(map);
	}
	
	
	/**
	 * 删
	 * @return
	 */
	@RequestMapping("/delById/{id}")
	@ResponseBody
	@SendTemplate
	public String delById(@PathVariable Long id){
		try {
			Set<String> macs = new HashSet<>();
			MesPointType mesPointType = typeServ.findById(id);
			typeServ.deleteById(id);
			for(MesPoints point : mesPointType.getMesPointses()){
				String template = mesPointsTemplateService.getTemplate(point);
				MesPointGateway gateway = point.getMesPointGateway();
				macs.add(gateway.getMac());
				String key = MesPointsTemplate.class.getSimpleName()+"_"+gateway.getMac();
				Map<String, Object> hash = redisServ.getHash(key);
				if(hash == null){
					hash = new HashMap<String,Object>();
				}
				hash.put(point.getCodekey(), template);
				redisServ.setHash(key, hash);
			}
			for (String mac : macs) {
				mesPointsTemplateService.sendTemplate(mac);
			}
		} catch (Exception e) {
			return AjaxObject.newError("删除测点类型失败！").toString();
		}
		return AjaxObject.newOk("删除测点类型成功！").toString();
	}
	
	/**
	 * 增或改
	 * @param pointGateway
	 * @return
	 */
	@RequestMapping("/addOrUpdate")
	@ResponseBody
	@SendTemplate
	public String addPointType(MesPointType type){
		try {
			if(type != null && type.getId() == null){
				//生产环境开启
				//type.setCompanyinfo(new Companyinfo(SecurityUtils.getShiroUser().getCompanyid()));
			}
			MesPointType pointType = typeServ.saveOrUpdate(type);
			Set<String> macs = new HashSet<>();
			for(MesPoints point : pointType.getMesPointses()){
				String template = mesPointsTemplateService.getTemplate(point);
				MesPointGateway gateway = point.getMesPointGateway();
				macs.add(gateway.getMac());
				String key = MesPointsTemplate.class.getSimpleName()+"_"+gateway.getMac();
				Map<String, Object> hash = redisServ.getHash(key);
				if(hash == null){
					hash = new HashMap<String,Object>();
				}
				hash.put(point.getCodekey(), template);
				redisServ.setHash(key, hash);
			}
			for (String mac : macs) {
				mesPointsTemplateService.sendTemplate(mac);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxObject.newError("添加测点类型失败！").toString();
		}
		return AjaxObject.newOk("添加测点类型成功！").toString();
	}
	
	@RequestMapping(value="/create", method=RequestMethod.GET)
	public String preCreate(Map<String, Object> map) {
		return PAGEPRE+"create";
	}
	
	
	/**
	 * 根据id查
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/showOne/{id}")
	public String findById(@PathVariable Long id,Model model){
		model.addAttribute("pointType",typeServ.findById(id));
		return PAGEPRE + "/showOne";
	}
	
	/**
	 * 根据当前公司id返回该公司下的所有测点
	 * @return
	 */
	/*@RequestMapping("/data_pointGateway")
	@ResponseBody
	public List<MesPointType> findByCompanyId(){
		try {
			//return typeServ.findByCompanyid(SecurityUtils.getShiroUser().getCompanyid());
		} catch (Exception e) {
			return null;
		}
	}*/
	
	@RequestMapping("/getAllType")
	@ResponseBody
	public List<MesPointType> findAllPointType(){
		return typeServ.findAll();
	}
}
