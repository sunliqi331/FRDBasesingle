package com.its.monitor.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gs.collections.impl.map.mutable.ConcurrentHashMap;
import com.its.common.entity.main.Dictionary;
import com.its.common.service.DictionaryService;
import com.its.common.utils.SecurityUtils;
import com.its.frd.dao.MesDriverDao;
import com.its.frd.dao.MesProcedurePropertyPointDao;
import com.its.frd.entity.Companyinfo;
import com.its.frd.entity.MesDriver;
import com.its.frd.entity.MesDriverPoints;
import com.its.frd.entity.MesPointGateway;
import com.its.frd.entity.MesPointType;
import com.its.frd.entity.MesPoints;
import com.its.frd.entity.MesProcedureProperty;
import com.its.frd.entity.MesProcedurePropertyPoint;
import com.its.frd.entity.MesProduct;
import com.its.frd.entity.MesProductProcedure;
import com.its.frd.entity.MesProductline;
import com.its.monitor.vo.MesPointsTemplate;

import net.sf.json.JSONObject;

@Service("mesPointsTemplateService")
public class MesPointsTemplateService{
	@Autowired
	protected SimpMessagingTemplate template;

	@Autowired
	private MesProcedurePropertyPointDao mesProcedurePropertyPointDao;

	@Autowired
	private DictionaryService dictionaryService;

	@Autowired
	private MesDriverDao mesDriverDao;

	public String sendTemplate(String mac){
		/*List<Dictionary> unitList = dictionaryService.findAll(new Specification<Dictionary>() {
			@Override
			public Predicate toPredicate(Root<Dictionary> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				// TODO Auto-generated method stub
				Expression<Long> expression = root.get("parent").get("id").as(Long.class);
				Predicate predicate = builder.equal(expression, 90);
				return query.where(predicate).getRestriction();
			}


		});
		Map<Long,String> unitsMap = new HashMap<>();
		for (Dictionary d : unitList){
			unitsMap.put(d.getId(), d.getName());
		}
		List<MesProcedurePropertyPoint> mesProcedurePropertyPointslist = mesProcedurePropertyPointDao.findAll();
		Map<Long,MesProcedurePropertyPoint> map = new ConcurrentHashMap<>();
		for(MesProcedurePropertyPoint procedurePropertyPoint : mesProcedurePropertyPointslist){
			if(null != procedurePropertyPoint && null != procedurePropertyPoint.getMesPoints()){
				map.put(procedurePropertyPoint.getMesPoints().getId(), procedurePropertyPoint);
			}
		}
		List<MesPointsTemplate> list = new ArrayList<MesPointsTemplate>();

		List<MesDriver> gatewayList = mesDriverDao.findAll(new Specification<MesDriver>() {
			@Override
			public Predicate toPredicate(Root<MesDriver> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Predicate predicate2 = builder.equal(root.get("differencetype").as(String.class), "0");
				return query.where(predicate2).getRestriction();
			}
		});
		for(MesDriver mesDriver : gatewayList){
			MesPointsTemplate mesPointsTemplate = new MesPointsTemplate();
			mesPointsTemplate.setMesDriverId(mesDriver.getId());
			mesPointsTemplate.setMesPointGateway(mesDriver.getSn());
			mesPointsTemplate.setMesPointKey("0");
			list.add(mesPointsTemplate);
		}
		for (MesPoints mesPoints : mesPointsList) {
			if(null == mesPoints || null == mesPoints.getCodekey() || mesPoints.getCodekey().equals("")
					|| (null != mesPoints.getMesPointType() && null != mesPoints.getMesPointType().getPointtypekey() && !mesPoints.getMesPointType().getPointtypekey().equals(MesPointType.TYPE_PRODUCT_PROCEDURE) && mesPoints.getMesDriverPointses().size() == 0)){
				continue;
			}
			if(null == mesPoints){
				continue;
			}
			MesPointType mesPointType = mesPoints.getMesPointType();
			MesPointGateway mesPointGateway = mesPoints.getMesPointGateway();
			MesDriverPoints mesDriverPoints = mesPoints.getMesDriverPointses().size() != 0 ? mesPoints.getMesDriverPointses().get(0) : new MesDriverPoints();
			//			MesDriver mesDriver = mesDriverPoints.getMesDriver();
			MesDriver mesDriver = mesPoints.getCurrentMesDriver();
			MesPointsTemplate mesPointsTemplate = new MesPointsTemplate();
			mesPointsTemplate.setMesDriverPointsName(mesPoints.getName());
			mesPointsTemplate.setUnit(unitsMap.get(mesPoints.getUnitsId()));
			mesPointsTemplate.setMesPointId(null != mesPoints ? mesPoints.getId() : 0L);
			mesPointsTemplate.setMesPointKey(null != mesPoints ? mesPoints.getCodekey() : "");


			if(null != mesDriver && mesDriver.getId() != null){
				MesProductline mesProductline = mesDriver.getMesProductline();
				mesPointsTemplate.setMesDriverId(mesDriver.getId());
				mesPointsTemplate.setMesDriverName(mesDriver.getName());
				if(null != mesProductline){
					mesPointsTemplate.setMesProductLineId(mesProductline.getId());
					mesPointsTemplate.setProductlineName(mesProductline.getLinename());
					Companyinfo companyinfo = mesProductline.getCompanyinfo();
					if(null != companyinfo){
						mesPointsTemplate.setFactoryId(companyinfo.getId());
						mesPointsTemplate.setFactoryName(companyinfo.getCompanyname());
					}
				}
			}
			mesPointsTemplate.setMesPointCheckDataList(mesDriverPoints.getMesPointCheckDatas());
			MesProcedurePropertyPoint mesProcedurePropertyPoint = map.get(mesPoints.getId());

			if(null != mesProcedurePropertyPoint){
				MesProcedureProperty mesProcedureProperty = map.get(mesPoints.getId()).getMesProcedureProperty();
				if(null != mesProcedureProperty){
					mesPointsTemplate.setProcedurePropertyId(mesProcedureProperty.getId());
					mesPointsTemplate.setMaxValue(null != mesDriverPoints.getMaxValue() ? mesDriverPoints.getMaxValue() : mesProcedureProperty.getUppervalues());
					mesPointsTemplate.setMinValue(null != mesDriverPoints.getMinValue() ? mesDriverPoints.getMinValue() : mesProcedureProperty.getLowervalues());
					mesPointsTemplate.setStandardValue(null != mesDriverPoints.getStandardValue() ? mesDriverPoints.getStandardValue() : mesProcedureProperty.getStandardvalues());
					MesProductProcedure mesProductProcedure = mesProcedureProperty.getMesProductProcedure();
					if(null != mesProductProcedure){
						mesPointsTemplate.setMesDriverProcedureId(mesProductProcedure.getId());
						mesPointsTemplate.setMesProcedureName(mesProductProcedure.getProcedurename());
						MesProduct mesProduct = mesProductProcedure.getMesProduct();
						if(null != mesProduct){
							mesPointsTemplate.setProductId(mesProduct.getId());
							mesPointsTemplate.setMesProduct(mesProduct.getName());
							mesPointsTemplate.setMesProductModel(mesProduct.getModelnum());
							Companyinfo companyinfo = mesProduct.getCompanyinfo();
							if(null != companyinfo){
								mesPointsTemplate.setMesProductCompanyinfo(companyinfo.getCompanyname());
								mesPointsTemplate.setCompanyId(companyinfo.getId());
							}
						}
					}

				}
			}
			mesPointsTemplate.setMesPointGateway(null != mesPointGateway ? mesPointGateway.getMac() : "");
			mesPointsTemplate.setMesPointTypeKey(null != mesPointType ? mesPointType.getPointtypekey() : "");

				mesPointsTemplate.setMaxValue(mesPoints.getMesProcedureProperties().size() != 0?mesPoints.getMesProcedureProperties().get(0).getUppervalues():"");
			mesPointsTemplate.setMinValue(mesPoints.getMesProcedureProperties().size() != 0?mesPoints.getMesProcedureProperties().get(0).getLowervalues():"");
			mesPointsTemplate.setStandardValue(mesPoints.getMesProcedureProperties().size() != 0?mesPoints.getMesProcedureProperties().get(0).getStandardvalues():"");
			list.add(mesPointsTemplate);
		}
		//JSONArray jsonArray = new JSONArray(list);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(Include.NON_NULL); 
		try {
			//	String templateStr = jsonArray.toString();
			try {
				String result = objectMapper.writeValueAsString(list);
				System.out.println(result);
				template.convertAndSend( "/topic/showMonitor/advise/template", objectMapper.writeValueAsString(list));
				return result;
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "";
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return null;*/
		// template.convertAndSend( "/queues/showMonitor/advise/template", mac);
//      pointGateway.setCreateTime(new Timestamp(new Date().getTime()));

        JSONObject obj = new JSONObject();
         obj.put("type", "1");
         obj.put("mac", mac);
         obj.put("pointId", "");
         // 网关发生变化的地方，要通知大数据
         // template.convertAndSend( "/showMonitor/advise/template", mac);
         template.convertAndSend( "/showMonitor/advise/template", obj);
		return mac;
	}
	public String getTemplate(MesPoints mesPoints){
		if(null == mesPoints){
			return null;
		}
		List<Dictionary> unitList = dictionaryService.findAll(new Specification<Dictionary>() {
			@Override
			public Predicate toPredicate(Root<Dictionary> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				// TODO Auto-generated method stub
				Expression<Long> expression = root.get("parent").get("id").as(Long.class);
				Predicate predicate = builder.equal(expression, 90);
				return query.where(predicate).getRestriction();
			}


		});
		Map<Long,String> unitsMap = new HashMap<>();
		for (Dictionary d : unitList){
			unitsMap.put(d.getId(), d.getName());
		}
		List<MesProcedurePropertyPoint> mesProcedurePropertyPointslist = mesProcedurePropertyPointDao.findAll();
		Map<Long,MesProcedurePropertyPoint> map = new ConcurrentHashMap<>();
		for(MesProcedurePropertyPoint procedurePropertyPoint : mesProcedurePropertyPointslist){
			if(null != procedurePropertyPoint && null != procedurePropertyPoint.getMesPoints()){
				map.put(procedurePropertyPoint.getMesPoints().getId(), procedurePropertyPoint);
			}
		}

		/*if(null == mesPoints || null == mesPoints.getCodekey() || mesPoints.getCodekey().equals("")
					|| (null != mesPoints.getMesPointType() && null != mesPoints.getMesPointType().getPointtypekey() && !mesPoints.getMesPointType().getPointtypekey().equals(MesPointType.TYPE_PRODUCT_PROCEDURE) && mesPoints.getMesDriverPointses().size() == 0)){
				continue;
			}*/
		
		MesPointType mesPointType = mesPoints.getMesPointType();
		MesPointGateway mesPointGateway = mesPoints.getMesPointGateway();
		MesDriverPoints mesDriverPoints = mesPoints.getMesDriverPointses().size() != 0 ? mesPoints.getMesDriverPointses().get(0) : new MesDriverPoints();
		MesDriver mesDriver = mesPoints.getCurrentMesDriver();
		MesPointsTemplate mesPointsTemplate = new MesPointsTemplate();
		mesPointsTemplate.setMesDriverPointsName(mesPoints.getName());
		mesPointsTemplate.setUnit(unitsMap.get(mesPoints.getUnitsId()));
		mesPointsTemplate.setMesPointId(null != mesPoints ? mesPoints.getId() : 0L);
		mesPointsTemplate.setMesPointKey(null != mesPoints ? mesPoints.getCodekey() : "");


		if(null != mesDriver && mesDriver.getId() != null){
			MesProductline mesProductline = mesDriver.getMesProductline();
			mesPointsTemplate.setMesDriverId(mesDriver.getId());
			mesPointsTemplate.setMesDriverName(mesDriver.getName());
			if(null != mesProductline){
				mesPointsTemplate.setMesProductLineId(mesProductline.getId());
				mesPointsTemplate.setProductlineName(mesProductline.getLinename());
				Companyinfo companyinfo = mesProductline.getCompanyinfo();
				if(null != companyinfo){
					mesPointsTemplate.setFactoryId(companyinfo.getId());
					mesPointsTemplate.setCompanyId(companyinfo.getParentid());
					mesPointsTemplate.setFactoryName(companyinfo.getCompanyname());
				}
			}
		}
		mesPointsTemplate.setMesPointCheckDataList(mesDriverPoints.getMesPointCheckDatas());
		MesProcedurePropertyPoint mesProcedurePropertyPoint = map.get(mesPoints.getId());
		mesPointsTemplate.setMaxValue(null != mesDriverPoints.getMaxValue() ? mesDriverPoints.getMaxValue() : "");
		mesPointsTemplate.setMinValue(null != mesDriverPoints.getMinValue() ? mesDriverPoints.getMinValue() : "");
		mesPointsTemplate.setStandardValue(null != mesDriverPoints.getStandardValue() ? mesDriverPoints.getStandardValue() : "");

		if(null != mesProcedurePropertyPoint){
			MesProcedureProperty mesProcedureProperty = map.get(mesPoints.getId()).getMesProcedureProperty();
			if(null != mesProcedureProperty){
				mesPointsTemplate.setProcedurePropertyId(mesProcedureProperty.getId());
				mesPointsTemplate.setMaxValue(null != mesProcedureProperty.getUppervalues() ? mesProcedureProperty.getUppervalues() : "");
				mesPointsTemplate.setProcedurePropertyName(mesProcedureProperty.getPropertyname());
				mesPointsTemplate.setMinValue(null != mesProcedureProperty.getLowervalues() ? mesProcedureProperty.getLowervalues() : "");
				mesPointsTemplate.setStandardValue(null != mesProcedureProperty.getStandardvalues() ? mesProcedureProperty.getStandardvalues() : "");
				MesProductProcedure mesProductProcedure = mesProcedureProperty.getMesProductProcedure();
				if(null != mesProductProcedure){
					mesPointsTemplate.setMesDriverProcedureId(mesProductProcedure.getId());
					mesPointsTemplate.setMesProcedureName(mesProductProcedure.getProcedurename());
					MesProduct mesProduct = mesProductProcedure.getMesProduct();
					if(null != mesProduct){
						mesPointsTemplate.setProductId(mesProduct.getId());
						mesPointsTemplate.setMesProduct(mesProduct.getName());
						mesPointsTemplate.setMesProductModel(mesProduct.getModelnum());
						Companyinfo companyinfo = mesProduct.getCompanyinfo();
						if(null != companyinfo){
							mesPointsTemplate.setMesProductCompanyinfo(companyinfo.getCompanyname());
						}
					}
				}

			}
		}
		mesPointsTemplate.setMesPointGateway(null != mesPointGateway ? mesPointGateway.getMac() : "");
		mesPointsTemplate.setMesPointTypeKey(null != mesPointType ? mesPointType.getPointtypekey() : "");

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(Include.NON_NULL); 
		try {
			try {
				String result = objectMapper.writeValueAsString(mesPointsTemplate);
				return result;
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			return "";
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return null;
	}
}
