package com.its.frd.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.its.common.service.DictionaryService;
import com.its.common.util.dwz.AjaxObject;
import com.its.common.util.dwz.Page;
import com.its.common.util.persistence.DynamicSpecifications;
import com.its.common.util.persistence.SearchFilter;
import com.its.common.utils.SecurityUtils;
import com.its.frd.entity.*;
import com.its.frd.params.SendTemplate;
import com.its.frd.service.*;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by zq on 2019-05-09.
 */

@Controller
@RequestMapping("/measuringToolAlarm")
public class MeasuringToolAlarmController {
    private final String PRE_PAGE = "measuringToolAlarm/";

    @Resource
    private MeasuringToolAlarmService measuringToolAlarmService;
    @Resource
    private MesProductService mesProductService;
    @Resource
    private MesProductProcedureService mesProductProcedureService;
    @Resource
    private CompanyinfoService cpService;
    @Resource
    private MesProductlineService mesProductlineService;
    @Resource
    private DictionaryService dicServ;

    @RequestMapping("/list")
    public String productList(HttpServletRequest request,Map<String, Object> map) {
        //工序
        List<MesProduct> mesProductList = this.mesProductService.findByCompanyinfo(SecurityUtils.getShiroUser().getCompanyid());
        List<Long> mesProductListIds = new ArrayList<>();
        for(MesProduct mesProduct:mesProductList){
            mesProductListIds.add(mesProduct.getId());
        }
        List<MesProductProcedure> mesProductProcedureList = mesProductProcedureService.findByMesProductIdIn(mesProductListIds);
        map.put("mesProductProcedureList",mesProductProcedureList);

        //产线
        List<Companyinfo> companyinfos = cpService.getTreeFactory();
        List<Long> companyinfoIds = new ArrayList<>();
        for(Companyinfo companyinfo:companyinfos){
            companyinfoIds.add(companyinfo.getId());
        }
        List<MesProductline> mesProductlineList = this.mesProductlineService.findByCompanyinfoIdIn(companyinfoIds);
        map.put("mesProductlineList",mesProductlineList);

        //量具类型
        Specification<com.its.common.entity.main.Dictionary> specification1 = DynamicSpecifications.bySearchFilter(request, com.its.common.entity.main.Dictionary.class,
                new SearchFilter("parent.id", SearchFilter.Operator.EQ,135));
        List<com.its.common.entity.main.Dictionary> Dictionary1 = dicServ.findAll(specification1);

        map.put("Dictionary1",Dictionary1);

        return PRE_PAGE + "list";
    }

    /**
     * 量具分页
     * @param request
     * @param page
     * @return
     * @throws JsonProcessingException
     */
    @RequestMapping("/measuringToolAlarmData")
    @ResponseBody
    public String data(HttpServletRequest request, Page page) throws JsonProcessingException {
        Map<String, Object> map = new HashMap<String, Object>();

        Specification<MeasuringToolAlarm> specification = DynamicSpecifications.bySearchFilter(request, MeasuringToolAlarm.class);
        List<MeasuringToolAlarm> list = this.measuringToolAlarmService.findPage(specification,page);

        for(MeasuringToolAlarm measuringToolAlarm : list){

            if(measuringToolAlarm.getMeasuringTool().getIsenabled()==0){
                measuringToolAlarm.getMeasuringTool().setIsenabledshow("使用");
            }else if(measuringToolAlarm.getMeasuringTool().getIsenabled()==1){
                measuringToolAlarm.getMeasuringTool().setIsenabledshow("停用");
            }else{
                measuringToolAlarm.getMeasuringTool().setIsenabledshow("检修");
            }

            if(measuringToolAlarm.getStatus()==1){
                measuringToolAlarm.setStatusbutton("<button style=\"height:18px;width:60px; background:orange\" class=\"btn\"></button>");
           }else if(measuringToolAlarm.getStatus()==2){
                measuringToolAlarm.setStatusbutton("<button style=\"height:18px;width:60px; background:red\" class=\"btn\"></button>");
           }

            if(measuringToolAlarm.getMeasuringTool().getSpcsite()!=null&&measuringToolAlarm.getMeasuringTool().getSpcsite()==1){
                measuringToolAlarm.getMeasuringTool().setSpcsiteName("SPC工作站1");
            }else if(measuringToolAlarm.getMeasuringTool().getSpcsite()!=null&&measuringToolAlarm.getMeasuringTool().getSpcsite()==2){
                measuringToolAlarm.getMeasuringTool().setSpcsiteName("SPC工作站2");
            }

//            measuringToolAlarm.getMeasuringTool().setProductAddprocedureName(measuringToolAlarm.getMeasuringTool().getMesProductProcedure().getMesProduct().getName()+"--"+measuringToolAlarm.getMeasuringTool().getMesProductProcedure().getProcedurename());
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        map.put("page", page);
        map.put("MeasuringToolAlarmList", list);

        return mapper.writeValueAsString(map);
    }



    @RequestMapping("/getDataForMeasuringToolAlarm")
    @ResponseBody
    public String getDataForMeasuringToolAlarm(String scope,String id,Long line) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

//        List<Long> mesProductProcedureIds = new ArrayList<>();
//        if("procedure".equals(scope)){
//            if(!"0".equals(id)){
//                MesProductProcedure mesProductProcedure = mesProductProcedureService.findById(Long.valueOf(id));
//                mesProductProcedureIds.add(mesProductProcedure.getId());
//            }else{
//                List<MesProductProcedure> mesProductProcedureList =  this.mesProductProcedureService.findAll();
//                for(MesProductProcedure mesProductProcedure : mesProductProcedureList){
//                    mesProductProcedureIds.add(mesProductProcedure.getId());
//                }
//            }
//        }
//        List<MeasuringToolAlarm> measuringToolAlarmList = this.measuringToolAlarmService.getDataWithMesProductProcedureIdsAndLine(mesProductProcedureIds,line);

        List<MeasuringToolAlarm> measuringToolAlarmList = this.measuringToolAlarmService.getDataWithByLine(line);
//        if(measuringToolAlarmList.size()<1){
//            return null;
//        }
        return mapper.writeValueAsString(measuringToolAlarmList);
    }






























}
