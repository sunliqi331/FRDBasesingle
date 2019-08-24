package com.its.frd.util;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.github.abel533.echarts.style.ItemStyle;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class MonitorUtil {

    /**
     * 获取监控设备数据list
     * @param
     * @return
     */
    public static List<Object> getMonitorList(List<Object> inputList, Long startTime, Long endTime) {
        List<Object> rsList = Lists.newArrayList();
        if(0 < inputList.size()) {
            Map<String, Object> makeFristMap = (Map) inputList.get(0);
            List<String> ftimes = (List)makeFristMap.get("value");
//            Long fristTime = Long.valueOf(String.valueOf(ftimes.get(1)).replace("000", ""));
//            Long startTimeCompare = Long.valueOf(String.valueOf(startTime).replace("000", ""));
            Long fristTime = Long.valueOf(String.valueOf(ftimes.get(1)));
            Long startTimeCompare = Long.valueOf(String.valueOf(startTime));
            if(fristTime > startTimeCompare) {
                Map<String, Object> paramMap = Maps.newHashMap();
                paramMap.put("name", "关机");
                List<Object> valels = Lists.newArrayList();
                valels.add(0);
                // 设备状态起始时间
                valels.add(BigInteger.valueOf(startTime));
                // 设备状态结束时间
//                valels.add(BigInteger.valueOf(Long.valueOf(String.valueOf(fristTime.toString()) + "000")));
                valels.add(BigInteger.valueOf(fristTime));
                // 设备状态持续时间
//                Long minusVal = fristTime - Long.valueOf(startTime.toString().replace("000", ""));
//                valels.add(BigInteger.valueOf(Long.valueOf(String.valueOf(minusVal) + "000")));
                Long minusVal = fristTime - startTimeCompare;
                valels.add(BigInteger.valueOf(Long.valueOf(String.valueOf(minusVal))));
                paramMap.put("value", valels);
                ItemStyle itemStyle = new ItemStyle();
                itemStyle.normal().color("#932842");
                paramMap.put("itemStyle", itemStyle);
                rsList.add(paramMap);
            }
            rsList.addAll(inputList);

            Map<String, Object> makeEndMap = (Map) inputList.get(inputList.size() - 1);
            List<String> etimes = (List)makeEndMap.get("value");
//            Long lastTime = Long.valueOf(String.valueOf(etimes.get(2)).replace("000", ""));
//            Long endTimeCompare = Long.valueOf(String.valueOf(endTime).replace("000", ""));
            Long lastTime = Long.valueOf(String.valueOf(etimes.get(2)));
            Long endTimeCompare = Long.valueOf(String.valueOf(endTime));
            if(endTimeCompare > lastTime) {
                Map<String, Object> paramMap = Maps.newHashMap();
                paramMap.put("name", "nodata");
                List<Object> valels = Lists.newArrayList();
                valels.add(0);
                // 设备状态起始时间
//                valels.add(BigInteger.valueOf(Long.valueOf(String.valueOf(lastTime.toString()) + "000")));
                valels.add(BigInteger.valueOf(lastTime));
                // 设备状态结束时间
                valels.add(BigInteger.valueOf(endTime));
                // 设备状态持续时间
//                Long minusVal = Long.valueOf(endTime.toString().replace("000", "")) - lastTime;
//                valels.add(BigInteger.valueOf(Long.valueOf(String.valueOf(minusVal) + "000")));
                Long minusVal = endTimeCompare - lastTime;
                valels.add(BigInteger.valueOf(Long.valueOf(String.valueOf(minusVal))));
                paramMap.put("value", valels);
                ItemStyle itemStyle = new ItemStyle();
                itemStyle.normal().color("#0e1b52");
                paramMap.put("itemStyle", itemStyle);
                rsList.add(paramMap);
            }

        } else {
            Map<String, Object> paramMap = Maps.newHashMap();
            paramMap.put("name", "nodata");
            List<Object> valels = Lists.newArrayList();
            valels.add(0);
            // 设备状态起始时间
            // valels.add(BigInteger.valueOf(Long.valueOf(String.valueOf(startTime.toString()) + "000")));
            valels.add(BigInteger.valueOf(Long.valueOf(startTime)));
            // 设备状态结束时间
            // valels.add(BigInteger.valueOf(Long.valueOf(String.valueOf(endTime.toString()) + "000")));
            valels.add(BigInteger.valueOf(Long.valueOf(endTime)));
            // 设备状态持续时间
            Long minusVal = endTime - startTime;
//            valels.add(BigInteger.valueOf(Long.valueOf(String.valueOf(minusVal) + "000")));
            valels.add(BigInteger.valueOf(Long.valueOf(minusVal)));
            paramMap.put("value", valels);
            ItemStyle itemStyle = new ItemStyle();
            itemStyle.normal().color("#0e1b52");
            paramMap.put("itemStyle", itemStyle);
            rsList.add(paramMap);
        }

        return rsList;
    }

    public static void main(String[] args) {

    }

}
