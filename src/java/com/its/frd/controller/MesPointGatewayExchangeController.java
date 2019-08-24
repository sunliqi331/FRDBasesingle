package com.its.frd.controller;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.directory.shared.kerberos.codec.apRep.actions.CheckMsgType;
import org.apache.directory.shared.kerberos.codec.checksum.ChecksumGrammar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.its.common.controller.BaseController;
import com.its.common.util.dwz.AjaxObject;
import com.its.common.utils.SecurityUtils;
import com.its.frd.entity.MesDriver;
import com.its.frd.entity.MesPointGateway;
import com.its.frd.entity.MesPointGatewayExchange;
import com.its.frd.service.CompanyinfoService;
import com.its.frd.service.MesPointGatewayExchangeService;
import com.its.frd.service.MesPointGatewayService;

@Controller
@RequestMapping("/mesPointGatewayExchange")
public class MesPointGatewayExchangeController extends BaseController {
	private static final String ERROR_500 = "error/500";
	
	@Autowired
	private MesPointGatewayService mesPointGatewayService;
	@Autowired
	private MesPointGatewayExchangeService mesPointGatewayExchangeService;

	/**
	 * 前往网关替换页面
	 * @return
	 */
	@RequestMapping("/exchangePage/{id}")
	public ModelAndView toExchangePage(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response){
		ModelAndView modelAndView = new ModelAndView("mesPointGateway/exchangePage");
		if(id == 0){
			return new ModelAndView(ERROR_500);
		}
		MesPointGateway mesPointGateway = mesPointGatewayService.findById(id);
		if(null != mesPointGateway){
			modelAndView.addObject("mesPointGateway", mesPointGateway);
		}
	
		List<MesPointGateway> list = mesPointGatewayService.findAll(new Specification<MesPointGateway>() {
			@Override
			public Predicate toPredicate(Root<MesPointGateway> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Predicate predicate = builder.equal(root.get("isActive").as(String.class), MesPointGateway.ACTIVE_NO);
				return query.where(predicate).getRestriction();
			}
		});
		if(list.size() != 0){
			modelAndView.addObject("mesPointGatewayList", list);
		}
		
		return modelAndView;
	}
	@RequestMapping("/doExchangePage")
	public @ResponseBody String doExchangePage(HttpServletRequest request, HttpServletResponse response,MesPointGatewayExchange mesPointGatewayExchange){
		
		int result = mesPointGatewayExchangeService.saveExchangeGateway(mesPointGatewayExchange);
		/**begin
		 * 查询新替换的网关是否存在，并且验证网关的准确性
		 */
		if(result == MesPointGatewayExchangeService.EXCHANGE_ERROR_NO_MAC_CODE){
			return AjaxObject.newError("您输入的编码不存在！").setCallbackType("").toString();
		}
		if(result == MesPointGatewayExchangeService.EXCHANGE_ERROR_BEEN_VERIFIED){
			return AjaxObject.newError("该网关已验证过！").setCallbackType("").toString();
		}
		if(result == MesPointGatewayExchangeService.EXCHANGE_ERROR_NO_COMPANYINFO){
			return AjaxObject.newError("公司不存在！").setCallbackType("").toString();
		}
		/**
		 * end
		 */
		
		return AjaxObject.newOk("替换成功").toString();
	}
	
	
}
