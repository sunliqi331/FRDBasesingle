package com.its.frd.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.its.common.util.dwz.AjaxObject;
import com.its.common.util.dwz.Page;
import com.its.common.util.persistence.DynamicSpecifications;
import com.its.common.util.persistence.SearchFilter;
import com.its.common.util.persistence.SearchFilter.Operator;
import com.its.common.utils.SecurityUtils;
import com.its.frd.entity.MesCompanyPosition;
import com.its.frd.service.MesCompanyPositionService;

@Controller
@RequestMapping("/position")
public class PositionController {

	private final String PAGEPRE = "position/";
	@Resource
	private MesCompanyPositionService positionServ;

	@RequiresPermissions("position:view")
	@RequestMapping("/positionList")
	public String positionList() {
		return PAGEPRE + "positionList";
	}

	@RequiresPermissions("position:save")
	@RequestMapping("/addPosition")
	public String addPosition(Map<String, Object> map) {
		Long companyId = SecurityUtils.getShiroUser().getCompanyid();
		map.put("companyId", companyId);
		return PAGEPRE + "addPosition";
	}

	/*
	 * 职位data
	 */
	@RequestMapping("/data")
	@ResponseBody
	public String data(HttpServletRequest request, Page page) throws JsonProcessingException {
		Map<String, Object> map = new HashMap<String, Object>();
		Specification<MesCompanyPosition> specification = DynamicSpecifications.bySearchFilter(request, MesCompanyPosition.class
				,new SearchFilter("companyinfo.id", Operator.EQ, SecurityUtils.getShiroUser().getCompanyid()));
		List<MesCompanyPosition> position = positionServ.findPage(specification, page);
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		map.put("page", page);
		map.put("position", position);
		return mapper.writeValueAsString(map);
	}

	/*
	 * 添加或修改
	 */
	@RequiresPermissions(value={"position:save","position:edit"},logical=Logical.OR)
	@RequestMapping("/saveOrUpdate")
	@ResponseBody
	public String saveOrUpdate(MesCompanyPosition mesCompanyPosition,HttpServletRequest request,Page page) {
		String msg = "修改";
		if(mesCompanyPosition.getId() == null)
			msg = "添加";
		try {
			/*Specification<MesCompanyPosition> specification = DynamicSpecifications.bySearchFilter(request, MesCompanyPosition.class
					,new SearchFilter("companyinfo.id", Operator.EQ, SecurityUtils.getShiroUser().getCompanyid()));*/
			List<MesCompanyPosition> position = positionServ.findByCompanyid(SecurityUtils.getShiroUser().getCompanyid());
			for(MesCompanyPosition positions : position) {
				if(positions.getId() != mesCompanyPosition.getId()) {
					if(mesCompanyPosition.getPositionname().equals(positions.getPositionname()))
						return AjaxObject.newError("该职位已存在！").setCallbackType("").toString();
				}
			}
			positionServ.saveOrUpdate(mesCompanyPosition);
		}catch(Exception e) {
			e.printStackTrace();
			return AjaxObject.newError(msg + "职位失败！").setCallbackType("").toString();
		}
		return AjaxObject.newOk(msg + "职位成功！").toString();
	}

	@RequiresPermissions("position:edit")
	@RequestMapping("/findById/{id}")
	public String findById(@PathVariable Long id,Model model) {
		MesCompanyPosition mesCompanyPosition = positionServ.findById(id);
		model.addAttribute("position", mesCompanyPosition);
		return PAGEPRE + "editPosition";
	}

	/*
	 * 删除职位
	 */
	@RequiresPermissions("position:delete")
	@RequestMapping("/deleteById")
	@ResponseBody
	public String deleteById(Long[] ids) {
		try{
			for (int i = 0; i < ids.length; i++) {
				positionServ.deleteById(ids[i]);
			}
		}catch(Exception e){
			e.printStackTrace();
			return AjaxObject.newError("删除职位失败！").setCallbackType("").toString();
		}
		return AjaxObject.newOk("删除职位成功!").toString();
	}
}
