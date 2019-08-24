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
@RequestMapping("/measuringTool")
public class MeasuringToolController {
    private final String PRE_PAGE = "measuringTool/";

    @Resource
    private MeasuringToolService measuringToolService;//量具Service
    @Resource
    private MeasuringToolAlarmService measuringToolAlarmService;//量具报警Service
    @Resource
    private MeasuringOperationlogService measuringOperationlogService;//量具操作日志Service
    @Resource
    private MesProductService mesProductService;
    @Resource
    private MesProductProcedureService mesProductProcedureService;
    @Resource
    private MesProductlineService mesProductlineService;
    @Resource
    private DictionaryService dicServ;
    @Resource
    private CompanyinfoService cpService;



    @RequestMapping("/list")
    public String productList(Map<String, Object> map) {

        List<MesProduct> mesProductList = this.mesProductService.findByCompanyinfo(SecurityUtils.getShiroUser().getCompanyid());
        List<Long> mesProductListIds = new ArrayList<>();
        for(MesProduct mesProduct:mesProductList){
            mesProductListIds.add(mesProduct.getId());
        }
        List<MesProductProcedure> mesProductProcedureList = mesProductProcedureService.findByMesProductIdIn(mesProductListIds);

        map.put("mesProductProcedureList",mesProductProcedureList);
        map.put("companyinfos", cpService.getTreeFactory());

        return PRE_PAGE + "list";
    }


    @RequiresPermissions("Measuring:save")
    @RequestMapping("/addMeasuring")
    public String addMeasuring (HttpServletRequest request,Map<String, Object> map) {

        //量具类型
        Specification<com.its.common.entity.main.Dictionary> specification1 = DynamicSpecifications.bySearchFilter(request, com.its.common.entity.main.Dictionary.class,
                new SearchFilter("parent.id", SearchFilter.Operator.EQ,135));
        List<com.its.common.entity.main.Dictionary> Dictionary1 = dicServ.findAll(specification1);

        map.put("Dictionary1",Dictionary1);


        List<MesProduct> mesProductList = this.mesProductService.findByCompanyinfo(SecurityUtils.getShiroUser().getCompanyid());
        List<Long> mesProductListIds = new ArrayList<>();
        for(MesProduct mesProduct:mesProductList){
            mesProductListIds.add(mesProduct.getId());
        }
        List<MesProductProcedure> mesProductProcedureList = mesProductProcedureService.findByMesProductIdIn(mesProductListIds);
        map.put("mesProductProcedureList",mesProductProcedureList);

        List<Companyinfo> companyinfos = cpService.getTreeFactory();
        List<Long> companyinfoIds = new ArrayList<>();
        for(Companyinfo companyinfo:companyinfos){
            companyinfoIds.add(companyinfo.getId());
        }
        List<MesProductline> mesProductlineList = this.mesProductlineService.findByCompanyinfoIdIn(companyinfoIds);
        map.put("mesProductlineList",mesProductlineList);

        return PRE_PAGE + "addMeasuring";
    }

    @RequiresPermissions("Measuring:edit")
    @RequestMapping("/findById/{id}")
    public String findByID(@PathVariable Long id, Model model, Map<String,Object> map, Page page, HttpServletRequest request){
        MeasuringTool measuringTool = this.measuringToolService.findById(id);
        model.addAttribute("measuringTool",measuringTool);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String testingtime = df.format(measuringTool.getTestingtime());
        String starttime = df.format(measuringTool.getStarttime());
        String endtime = df.format(measuringTool.getEndtime());
        map.put("testingtime",testingtime);
        map.put("starttime",starttime);
        map.put("endtime",endtime);


        //量具类型
        Specification<com.its.common.entity.main.Dictionary> specification1 = DynamicSpecifications.bySearchFilter(request, com.its.common.entity.main.Dictionary.class,
                new SearchFilter("parent.id", SearchFilter.Operator.EQ,135));
        List<com.its.common.entity.main.Dictionary> Dictionary1 = dicServ.findAll(specification1);

        map.put("Dictionary1",Dictionary1);


        List<MesProduct> mesProductList = this.mesProductService.findByCompanyinfo(SecurityUtils.getShiroUser().getCompanyid());
        List<Long> mesProductListIds = new ArrayList<>();
        for(MesProduct mesProduct:mesProductList){
            mesProductListIds.add(mesProduct.getId());
        }
        List<MesProductProcedure> mesProductProcedureList = mesProductProcedureService.findByMesProductIdIn(mesProductListIds);
        map.put("mesProductProcedureList",mesProductProcedureList);

        List<Companyinfo> companyinfos = cpService.getTreeFactory();
        List<Long> companyinfoIds = new ArrayList<>();
        for(Companyinfo companyinfo:companyinfos){
            companyinfoIds.add(companyinfo.getId());
        }
        List<MesProductline> mesProductlineList = this.mesProductlineService.findByCompanyinfoIdIn(companyinfoIds);
        map.put("mesProductlineList",mesProductlineList);

        return PRE_PAGE + "editMeasuring";
    }


    @SendTemplate
    @RequiresPermissions(value={"Measuring:save","Measuring:edit"},logical= Logical.OR)
    @RequestMapping("/saveMeasuring")
    @ResponseBody
    public String saveMeasuring(@Valid MeasuringTool measuringTool, HttpServletRequest request, Page page){
        String msg = "修改";
        if(measuringTool.getId() == null){
            msg = "添加";
        }
        measuringTool.setIsdelete(0);

        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;

        Timestamp endtime = measuringTool.getEndtime();
        Long days = measuringTool.getDays();
        Date date = new Date();

        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(endtime);//把当前时间赋给日历
        calendar.set(Calendar.HOUR_OF_DAY, (int) (calendar.get(Calendar.HOUR_OF_DAY) - days));
//      calendar.add(Calendar.DAY_OF_MONTH, (int) -days);
        Date warningtime = calendar.getTime();

        if(date.getTime() < warningtime.getTime()){
            measuringTool.setStatus(0);
            this.measuringToolService.saveOrUpdate(measuringTool);
            //查询是否有报警，如果有就删除
            if(measuringTool.getId() != null){
                List<MeasuringToolAlarm> MeasuringToolAlarmList = this.measuringToolAlarmService.findByMeasuringToolId(measuringTool.getId());
                if(MeasuringToolAlarmList!=null && MeasuringToolAlarmList.size()>0){
                    this.measuringToolAlarmService.deleteByMeasuringToolId(measuringTool.getId());
                }
            }
        }else if( warningtime.getTime() < date.getTime() && date.getTime() < endtime.getTime()){
            measuringTool.setStatus(1);
            this.measuringToolService.saveOrUpdate(measuringTool);

            //查询是否有报警，如果有就删除，重新生成报警
            if(measuringTool.getId() != null){
                List<MeasuringToolAlarm> MeasuringToolAlarmList = this.measuringToolAlarmService.findByMeasuringToolId(measuringTool.getId());
                if(MeasuringToolAlarmList!=null && MeasuringToolAlarmList.size()>0){
                    this.measuringToolAlarmService.deleteByMeasuringToolId(measuringTool.getId());
                }
            }

            MeasuringToolAlarm measuringToolAlarm = new MeasuringToolAlarm();
            measuringToolAlarm.setMeasuringTool(measuringTool);
            measuringToolAlarm.setStatus(1);
            measuringToolAlarm.setAlarmtime(new Timestamp(warningtime.getTime()));
            measuringToolAlarm.setEndtime(measuringTool.getEndtime());
            measuringToolAlarm.setRecordtime(new Timestamp(date.getTime()));
            measuringToolAlarm.setHours((long) 0);
            this.measuringToolAlarmService.saveOrUpdate(measuringToolAlarm);

        }else if(date.getTime()>endtime.getTime()){
            measuringTool.setStatus(2);
            this.measuringToolService.saveOrUpdate(measuringTool);

            //查询是否有报警，如果有就删除，重新生成报警
            if(measuringTool.getId() != null){
                List<MeasuringToolAlarm> MeasuringToolAlarmList = this.measuringToolAlarmService.findByMeasuringToolId(measuringTool.getId());
                if(MeasuringToolAlarmList!=null && MeasuringToolAlarmList.size()>0){
                    this.measuringToolAlarmService.deleteByMeasuringToolId(measuringTool.getId());
                }
            }

            MeasuringToolAlarm measuringToolAlarm = new MeasuringToolAlarm();
            measuringToolAlarm.setMeasuringTool(measuringTool);
            measuringToolAlarm.setStatus(2);
            measuringToolAlarm.setAlarmtime(new Timestamp(warningtime.getTime()));
            measuringToolAlarm.setEndtime(measuringTool.getEndtime());
            measuringToolAlarm.setRecordtime(new Timestamp(date.getTime()));
            measuringToolAlarm.setHours((date.getTime() - endtime.getTime()) / nh);
            this.measuringToolAlarmService.saveOrUpdate(measuringToolAlarm);

        }else{
            measuringTool.setStatus(0);
            this.measuringToolService.saveOrUpdate(measuringTool);
        }
        MeasuringOperationlog measuringOperationlog = new MeasuringOperationlog();
        measuringOperationlog.setIsdelete(0);
        measuringOperationlog.setUsername(SecurityUtils.getShiroUser().getLoginName());
        measuringOperationlog.setName(measuringTool.getName());
        measuringOperationlog.setSn(measuringTool.getSn());
        String str = "";
        if(measuringTool.getIsenabled()==0){
            str="使用";
        }else if (measuringTool.getIsenabled()==1){
            str="停用";
        }else if (measuringTool.getIsenabled()==2){
            str="检修";
        }
        measuringOperationlog.setMessage(msg+"量具，状态为："+str);
        measuringOperationlog.setCreateTime(new Date());
        measuringOperationlogService.saveOrUpdate(measuringOperationlog);


        return AjaxObject.newOk(msg + "成功!").toString();
    }




    /**
     * 量具分页
     * @param request
     * @param page
     * @return
     * @throws JsonProcessingException
     */
    @RequestMapping("/measuringData")
    @ResponseBody
    public String data(HttpServletRequest request, Page page) throws JsonProcessingException {
        Map<String, Object> map = new HashMap<String, Object>();

        Specification<MeasuringTool> specification = DynamicSpecifications.bySearchFilter(request, MeasuringTool.class,
               new SearchFilter("isdelete", SearchFilter.Operator.EQ,"0")
        );
        List<MeasuringTool> list = this.measuringToolService.findPage(specification,page);

        for(MeasuringTool measuringTool : list){
           if(measuringTool.getIsenabled()==0){
               measuringTool.setIsenabledshow("使用");
           }else if(measuringTool.getIsenabled()==1){
               measuringTool.setIsenabledshow("停用");
           }else{
               measuringTool.setIsenabledshow("检修");
           }

//           if(measuringTool.getStatus()==0){
//               measuringTool.setStatusbutton("<button style=\"height:18px;width:60px; background:green\" class=\"btn\"></button>");
//           }else if(measuringTool.getStatus()==1){
//               measuringTool.setStatusbutton("<button style=\"height:18px;width:60px; background:orange\" class=\"btn\"></button>");
//           }else if(measuringTool.getStatus()==2){
//               measuringTool.setStatusbutton("<button style=\"height:18px;width:60px; background:red\" class=\"btn\"></button>");
//           }

            if(measuringTool.getSpcsite()!=null&&measuringTool.getSpcsite()==1){
                measuringTool.setSpcsiteName("SPC工作站1");
            }else if(measuringTool.getSpcsite()!=null&&measuringTool.getSpcsite()==2){
                measuringTool.setSpcsiteName("SPC工作站2");
            }

//            measuringTool.setProductAddprocedureName(measuringTool.getMesProductProcedure().getMesProduct().getName()+"--"+measuringTool.getMesProductProcedure().getProcedurename());
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        map.put("page", page);
        map.put("measuringToolList", list);

        return mapper.writeValueAsString(map);
    }



    @RequestMapping("/checkSn/{sn}")
    @ResponseBody
    public String checkSn(@PathVariable String sn) throws JsonProcessingException{
        Map<String, Object> map = new HashMap<String, Object>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        List<MeasuringTool> list = this.measuringToolService.findBySnAndIsdelete(sn,0);
        try {
            if(list.size()>0){
                map.put("0", 0);
            }else {
                map.put("1", 1);
            }
        } catch (Exception e) {
            return null;
        }
        return mapper.writeValueAsString(map);

    }



    @RequiresPermissions("Measuring:delete")
    @RequestMapping("/deleteMeasuring")
    @ResponseBody
    @SendTemplate
    public String deleteSubDriver(Long[] ids){
        try {
            for (int i = 0; i < ids.length; i++) {
                MeasuringTool measuringTool = this.measuringToolService.findById(ids[i]);
                measuringTool.setIsdelete(1);
                List<MeasuringToolAlarm>  MeasuringToolAlarmList = this.measuringToolAlarmService.findByMeasuringToolId(ids[i]);
                if(MeasuringToolAlarmList!=null && MeasuringToolAlarmList.size()>0){
                    this.measuringToolAlarmService.deleteByMeasuringToolId(ids[i]);
                }
                this.measuringToolService.saveOrUpdate(measuringTool);

                MeasuringOperationlog measuringOperationlog = new MeasuringOperationlog();
                measuringOperationlog.setIsdelete(0);
                measuringOperationlog.setUsername(SecurityUtils.getShiroUser().getLoginName());
                measuringOperationlog.setName(measuringTool.getName());
                measuringOperationlog.setSn(measuringTool.getSn());
                String str = "";
                if(measuringTool.getIsenabled()==0){
                    str="使用";
                }else if (measuringTool.getIsenabled()==1){
                    str="停用";
                }else if (measuringTool.getIsenabled()==2){
                    str="检修";
                }
                measuringOperationlog.setMessage("删除量具");
                measuringOperationlog.setCreateTime(new Date());
                measuringOperationlogService.saveOrUpdate(measuringOperationlog);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxObject.newError("删除失败!").setCallbackType("").toString();
        }
        return AjaxObject.newOk("删除成功!").setCallbackType("").toString();
    }




    @RequestMapping("/saveSpcMeasuring")
    public void saveSpcMeasuring(String param){
        String[] packageData = param.split("\n");
        String programmeridArr = packageData[5];
        String programmerid = programmeridArr.split(":")[1];

        Integer spcsite = Integer.valueOf(packageData[7].split(":")[4]);
        //先删除某站点量具的报警，再删除量具
        this.measuringToolAlarmService.deleteAllBySpcsite(spcsite);
        this.measuringToolService.deleteAllBySpcsite(spcsite);

        for (int index = 7; index < packageData.length; index++) {
            MeasuringTool measuringTool = new MeasuringTool();
            if("8".equals(programmerid)){
                MesProductline productline = this.mesProductlineService.findById((long) 1);
                if(productline!=null){
                    measuringTool.setMesProductline(productline);
                }
            }else if("9".equals(programmerid)){
                MesProductline productline = this.mesProductlineService.findById((long) 2);
                if(productline!=null){
                    measuringTool.setMesProductline(productline);
                }
            }

            measuringTool.setSn(packageData[index].split(":")[1]);
            measuringTool.setName(packageData[index].split(":")[2]);
            switch (packageData[index].split(":")[3]) {
                case "1":
                    measuringTool.setType("马尔");
                    break;
                case "2":
                    measuringTool.setType("三丰");
                    break;
                case "3":
                    measuringTool.setType("广陆");
                    break;
                case "4":
                    measuringTool.setType("富瑞德旧");
                    break;
                case "5":
                    measuringTool.setType("ENT1");
                    break;
                case "6":
                    measuringTool.setType("富瑞德新");
                    break;
                case "7":
                    measuringTool.setType("其他");
                    break;
                default:
                    break;
            }
//            measuringTool.setMesProductProcedure(this.mesProductProcedureService.findById(Long.valueOf(packageData[index].split(":")[4])));
            measuringTool.setSpcsite(Integer.valueOf(packageData[index].split(":")[4]));
            measuringTool.setSpecifications(packageData[index].split(":")[5]);
            measuringTool.setTestingtime(new Timestamp(new Long(packageData[index].split(":")[6])*1000));
            measuringTool.setStarttime(new Timestamp(new Long(packageData[index].split(":")[7])*1000));
            measuringTool.setEndtime(new Timestamp(new Long(packageData[index].split(":")[8])*1000));
            measuringTool.setDays(new Long(packageData[index].split(":")[9]));
            measuringTool.setIsenabled(Integer.valueOf(packageData[index].split(":")[10]));
            measuringTool.setIsdelete(0);


            long nd = 1000 * 24 * 60 * 60;
            long nh = 1000 * 60 * 60;
            long nm = 1000 * 60;

            Timestamp endtime = new Timestamp(new Long(packageData[index].split(":")[8])*1000);
            Long days = measuringTool.getDays();
            Date date = new Date();

            Calendar calendar = Calendar.getInstance(); //得到日历
            calendar.setTime(endtime);//把当前时间赋给日历
            calendar.set(Calendar.HOUR_OF_DAY, (int) (calendar.get(Calendar.HOUR_OF_DAY) - days));
//            calendar.add(Calendar.DAY_OF_MONTH, (int) -days);
            Date warningtime = calendar.getTime();

            if(date.getTime() < warningtime.getTime()){
                measuringTool.setStatus(0);
                this.measuringToolService.saveOrUpdate(measuringTool);
            }else if( warningtime.getTime() < date.getTime() && date.getTime() < endtime.getTime()){
                measuringTool.setStatus(1);
                this.measuringToolService.saveOrUpdate(measuringTool);

                MeasuringToolAlarm measuringToolAlarm = new MeasuringToolAlarm();
                measuringToolAlarm.setMeasuringTool(measuringTool);
                measuringToolAlarm.setStatus(1);
                measuringToolAlarm.setAlarmtime(new Timestamp(warningtime.getTime()));
                measuringToolAlarm.setEndtime(measuringTool.getEndtime());
                measuringToolAlarm.setRecordtime(new Timestamp(date.getTime()));
                measuringToolAlarm.setHours((long) 0);
                this.measuringToolAlarmService.saveOrUpdate(measuringToolAlarm);

            }else if(date.getTime()>endtime.getTime()){
                measuringTool.setStatus(2);
                this.measuringToolService.saveOrUpdate(measuringTool);

                MeasuringToolAlarm measuringToolAlarm = new MeasuringToolAlarm();
                measuringToolAlarm.setMeasuringTool(measuringTool);
                measuringToolAlarm.setStatus(2);
                measuringToolAlarm.setAlarmtime(new Timestamp(warningtime.getTime()));
                measuringToolAlarm.setEndtime(measuringTool.getEndtime());
                measuringToolAlarm.setRecordtime(new Timestamp(date.getTime()));
                measuringToolAlarm.setHours((date.getTime() - endtime.getTime()) / nh);
                this.measuringToolAlarmService.saveOrUpdate(measuringToolAlarm);

            }else{
                measuringTool.setStatus(0);
                this.measuringToolService.saveOrUpdate(measuringTool);
            }

            MeasuringOperationlog measuringOperationlog = new MeasuringOperationlog();
            measuringOperationlog.setIsdelete(0);
            measuringOperationlog.setUsername("SPC数据同步");
            measuringOperationlog.setName(measuringTool.getName());
            measuringOperationlog.setSn(measuringTool.getSn());
            String str = "";
            if(measuringTool.getIsenabled()==0){
                str="使用";
            }else if (measuringTool.getIsenabled()==1){
                str="停用";
            }else if (measuringTool.getIsenabled()==2){
                str="检修";
            }
            measuringOperationlog.setMessage("同步量具，状态为："+str);
            measuringOperationlog.setCreateTime(new Date());
            measuringOperationlogService.saveOrUpdate(measuringOperationlog);
        }
    }
}
