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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zq on 2019-05-09.
 */

@Controller
@RequestMapping("/measuringOperationlog")
public class MeasuringOperationlogController {
    private final String PRE_PAGE = "measuringoperationlog/";

    @Resource
    private MeasuringOperationlogService measuringOperationlogService;//量具操作日志Service

    @RequestMapping("/list")
    public String productList(Map<String, Object> map) {
        return PRE_PAGE + "list";
    }


    /**
     * 量具分页
     * @param request
     * @param page
     * @return
     * @throws JsonProcessingException
     */
    @RequestMapping("/data")
    @ResponseBody
    public String data(HttpServletRequest request, Page page) throws JsonProcessingException {
        Map<String, Object> map = new HashMap<String, Object>();

        Specification<MeasuringOperationlog> specification = DynamicSpecifications.bySearchFilter(request, MeasuringOperationlog.class,
               new SearchFilter("isdelete", SearchFilter.Operator.EQ,"0")
        );
        List<MeasuringOperationlog> list = this.measuringOperationlogService.findPage(specification,page);

//        for(MeasuringOperationlog measuringOperationlog : list){
//           if(measuringTool.getIsenabled()==0){
//               measuringTool.setIsenabledshow("启用");
//           }else if(measuringTool.getIsenabled()==1){
//               measuringTool.setIsenabledshow("停用");
//           }else{
//               measuringTool.setIsenabledshow("检修");
//           }
//
//           if(measuringTool.getStatus()==0){
//               measuringTool.setStatusbutton("<button style=\"height:18px;width:60px; background:green\" class=\"btn\"></button>");
//           }else if(measuringTool.getStatus()==1){
//               measuringTool.setStatusbutton("<button style=\"height:18px;width:60px; background:orange\" class=\"btn\"></button>");
//           }else if(measuringTool.getStatus()==2){
//               measuringTool.setStatusbutton("<button style=\"height:18px;width:60px; background:red\" class=\"btn\"></button>");
//           }
//        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        map.put("page", page);
        map.put("measuringToolList", list);

        return mapper.writeValueAsString(map);
    }

}
