package com.its.statistics.service;

import com.its.frd.dao.MonitorPainterDao;
import com.its.frd.dao.MonitorSpcDao;
import com.its.frd.entity.MonitorPainter;
import com.its.frd.entity.MonitorSpc;
import com.its.statistics.vo.CgAnalyzeData;
import com.its.statistics.vo.SpcAnalyzeSearch;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service("spcAnalyseService")
public class SpcAnalyseService extends StatisticsService {

    @Autowired
    private MonitorPainterDao monitorPainterDao;

    @Autowired
    private MonitorSpcDao monitorSpcDao;

    @Resource
    private StatisticsService statisticsService;

    public String saveAnalyseSPCData(SpcAnalyzeSearch spcAnalyzeData, String datas){
//        Thread t=new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(100);
//                    List<MonitorPainter> monitorPainterList= monitorPainterDao.findBySpcAnalysisDataNotNull();
//                    for(MonitorPainter m : monitorPainterList) {//其内部实质上还是调用了迭代器遍历方式，这种循环方式还有其他限制，不建议使用。
//                        long id = m.getId();
//                        String spc_analysis_Data = m.getSpcAnalysisData();
//                        saveSpcAnalysis(id, spc_analysis_Data);
//                        System.out.println(id);
//                    }
//
////                    System.out.println("数据库操作");
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        t.start();


        try {
            List<MonitorPainter> monitorPainterList= monitorPainterDao.findBySpcAnalysisDataNotNull();
            for(MonitorPainter m : monitorPainterList) {//其内部实质上还是调用了迭代器遍历方式，这种循环方式还有其他限制，不建议使用。
                long id = m.getId();
                String spc_analysis_Data = m.getSpcAnalysisData();
                saveSpcAnalysis(id, spc_analysis_Data, datas);
            }
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

    public void saveSpcAnalysis(long id, String spc_analysis_Data, String datas) {
        JSONArray jsonArray_spc_upload = JSONArray.fromObject(datas);
        JSONArray jsonArray = JSONArray.fromObject(spc_analysis_Data);
        List<MonitorSpc> monitorSpcList = new ArrayList<MonitorSpc>();
        String procedurePropertyId_str = "";
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String chartId = (String) jsonObject.get("chartId");
            JSONArray jsonArray_data = (JSONArray) jsonObject.get("data");
            SpcAnalyzeSearch spcAnalyzeSearch = new SpcAnalyzeSearch();
            long mesDriverId = 0;
            long productId = 0;
            long productProcedureId = 0;
            long procedurePropertyId = 0;
            int subRange = 0;
            int subSeq = 0;
            int subNum = 0;
            int scale = 4;
            int hasProperty = 0; //spc监控选择的属性,从spc传递过来是否包含
            String productionSn = null;
            String queryname = null;
            String upper = null;
            String lower = null;
            for (int j = 0; j < jsonArray_data.size(); j++) {
                JSONObject jsonObject_data = jsonArray_data.getJSONObject(j);
                String name = (String) jsonObject_data.get("name");
                if (name.equals("mesDriverId")) {
                    mesDriverId = Long.valueOf((String) jsonObject_data.get("value"));
                } else if (name.equals("productId")){
                    productId = Long.valueOf((String)  jsonObject_data.get("value"));
                } else if (name.equals("productProcedureId")){
                    productProcedureId = Long.valueOf((String) jsonObject_data.get("value"));
                } else if (name.equals("procedurePropertyId")){
                    procedurePropertyId = Long.valueOf((String) jsonObject_data.get("value"));
                } else if (name.equals("subRange")){
                    subRange = Integer.parseInt((String)jsonObject_data.get("value"));
                } else if (name.equals("subSeq")){
                    subSeq = Integer.parseInt((String)jsonObject_data.get("value"));
                } else if (name.equals("subNum")){
                    subNum =Integer.parseInt((String)jsonObject_data.get("value"));
                } else if (name.equals("productionSn")){
                    productionSn = (String) jsonObject_data.get("value");
                } else if (name.equals("queryname")){
                    queryname = (String) jsonObject_data.get("value");
                } else if (name.equals("upper")){
                    upper = (String) jsonObject_data.get("value");
                } else if (name.equals("lower")){
                    lower = (String) jsonObject_data.get("value");
                }
            }
            /**start 一个监控中相同属性不多次重复计算spc4个值**/
//            if (procedurePropertyId_str.indexOf(String.valueOf(procedurePropertyId)) !=-1){
//                continue;
//            }
            /**end**/
            procedurePropertyId_str += procedurePropertyId+";";
            /**start 判断spc监控选择的属性,从spc传递过来是否包含**/
            for (int k=0; k<jsonArray_spc_upload.size(); k++){
                JSONObject jsonObject_spc = jsonArray_spc_upload.getJSONObject(k);
                if (procedurePropertyId == Long.valueOf(String.valueOf(jsonObject_spc.get("procedurePropertyId")))){
                    hasProperty = 1;
                }
            }
            if (hasProperty == 0){
                continue;
            }
            /**end **/
            spcAnalyzeSearch.setMesDriverId(mesDriverId);
            spcAnalyzeSearch.setProductId(productId);
            spcAnalyzeSearch.setProductProcedureId(productProcedureId);
            spcAnalyzeSearch.setProcedurePropertyId(procedurePropertyId);
            spcAnalyzeSearch.setSubRange(subRange);
            spcAnalyzeSearch.setSubSeq(subSeq);
            spcAnalyzeSearch.setSubNum(subNum);
            spcAnalyzeSearch.setProductionSn(productionSn);
            spcAnalyzeSearch.setQueryname(queryname);
            spcAnalyzeSearch.setUpper(upper);
            spcAnalyzeSearch.setLower(lower);
            spcAnalyzeSearch.setScale(scale);
            Map<String, Object> map = statisticsService.spcAnalyzeDataSearch(spcAnalyzeSearch);
            List<CgAnalyzeData> list = (List<CgAnalyzeData>) map.get("cgAnalyzeData");
//            Collections.reverse(list);
            List<Double> doubleList = new ArrayList<>();
            String values = "";
            for (CgAnalyzeData c:list){
                doubleList.add(Double.valueOf(c.getValue()));
//                values += c.getValue()+",";
            }
            //spc分析没有值则跳出循环
            if (doubleList.size() == 0){
                continue;
            }
//            values = values.substring(0,values.length() - 1);
            spcAnalyzeSearch.setValues(doubleList);
//            for (int j=0; j<list.size(); j++){
//
//            }
//            String result = statisticsService.saveAnalyseSPCData(spcAnalyzeSearch);
            String result = statisticsService.saveAnalyseSPCData(spcAnalyzeSearch);
            JSONObject jSONObject = JSONObject.fromObject(result);
            Double Cp;
            Double Cpk;
            Double Pp;
            Double Ppk;
            //除数为0特殊处理
            if (String.valueOf(jSONObject.get("Cp")).equals("0")){
                Cp = 0.0;
            } else {
                Cp = (Double) jSONObject.get("Cp");
            }
            if (String.valueOf(jSONObject.get("Cpk")).equals("0")){
                Cpk = 0.0;
            } else {
                Cpk = (Double) jSONObject.get("Cpk");
            }
            if (String.valueOf(jSONObject.get("Pp")).equals("0")){
                Pp = 0.0;
            } else {
                Pp = (Double) jSONObject.get("Pp");
            }
            if (String.valueOf(jSONObject.get("Ppk")).equals("0")){
                Ppk = 0.0;
            } else {
                Ppk = (Double) jSONObject.get("Ppk");
            }
            MonitorSpc monitorSpc = new MonitorSpc();
            monitorSpc.setCp(Cp);
            monitorSpc.setCpk(Cpk);
            monitorSpc.setPp(Pp);
            monitorSpc.setPpk(Ppk);
            monitorSpc.setChartId(chartId);
            monitorSpc.setMonitorPainterId(id);
            monitorSpc.setDriverPropertyId(procedurePropertyId);
            monitorSpc.setMesDriverId(mesDriverId);
            monitorSpc.setProductId(productId);
            monitorSpc.setProductProcedureId(productProcedureId);
            monitorSpc.setSubnum((long) subNum);
            monitorSpc.setSubrange((long) subRange);
            monitorSpcList.add(monitorSpc);
        }
        monitorSpcDao.save(monitorSpcList);
    }
}
