package com.its.frd.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class MesAnylsanysUtils {

    /**
     * 根据状态Code获取对应value
     * @param code
     * @return
     */
    public static String getStatusValueByCode(String code) {
        String rs = "运行";
//        switch (code) {
//            case "1":
//                rs = "停机";
//                break;
//            case "2":
//                rs = "待机";
//                break;
//            default:
//                break;
//        }
        if(code.contains("压机开机时间")||"压机开机时间".contains(code)) {
            rs = "开机";
        } else if(code.contains("压机关机时间")||"压机关机时间".contains(code)) {
            rs = "关机";
        } else if(code.contains("压机待机时间")||"压机待机时间".contains(code)) {
            rs = "待机";
        } else if(code.contains("压机运行时间")||"压机运行时间".contains(code)) {
            rs = "运行";
        }
        return rs;
    }
    
    /**
     * 根据状态Code获取对应颜色值
     * @param code
     * @return
     */
    public static String getStatusColorByCode(String code) {
        String rs = "green";
//        switch (code) {
//            case "1":
//                rs = "red";
//                break;
//            case "2":
//                rs = "yellow";
//                break;
//            default:
//                break;
//        }
        // 运行 绿色 停止红色 
        if(code.contains("压机开机时间")||"压机开机时间".contains(code)) {
            rs = "#489355"; // 绿色
        } else if(code.contains("压机关机时间")||"压机关机时间".contains(code)) {
            rs = "#932842"; // 红色
        } else if(code.contains("压机待机时间")||"压机待机时间".contains(code)) {
            rs = "#e9f05f"; // 黄色
        } else if(code.contains("压机运行时间")||"压机运行时间".contains(code)) {
            rs = "#489355"; // 绿色
        }
        return rs;
    }

    public static float getMinusVal(Float before, Float after) {
        float rs = 0f;
        rs = before - after;
        if(0 > rs) {
           rs = rs * -1; 
        }
        return rs;
   }

    /**
     * 取得list中首位时间戳，并计算区间时间，并换算成秒
     * @param list
     * @return
     */
    public static String getListStartEndTime(List<BigInteger> list) {
        String rs = "0";
        BigInteger startTime = BigInteger.ZERO;
        if(0 < list.size())
            if(null != list.get(0))
                startTime = list.get(0);

        BigInteger stopTime = BigInteger.ZERO;
        if(1 < list.size())
            if(null != list.get(list.size() - 1))
                stopTime = list.get(list.size() - 1);

        // 时间戳（转秒）
        BigInteger minusTime = stopTime.subtract(startTime).divide(BigInteger.valueOf(1000L));
        if(BigInteger.ZERO != minusTime) {
            rs = String.valueOf(minusTime);
        }
        return rs;
    }

    public static void getKindOfTime(Map<String, Object> rsMap, 
            List<BigInteger> stopByTotalList,
            List<BigInteger> standByTotalList,
            List<BigInteger> runByTotalList) {
        // 运行
        int sumRunByTotalList = runByTotalList.stream().mapToInt(BigInteger :: intValue).sum();
        if(0 == sumRunByTotalList) {
            rsMap.put("sumRunTimeFont", "0分0秒");
        } else {
            rsMap.put("sumRunTimeFont", getTimeStrByTimestamp(sumRunByTotalList));
        }
        // 待机
        int sumStandByTotalList = standByTotalList.stream().mapToInt(BigInteger :: intValue).sum();
        if(0 == sumStandByTotalList) {
            rsMap.put("sumStandByTimeFont", "0分0秒");
        } else {
            rsMap.put("sumStandByTimeFont", getTimeStrByTimestamp(sumRunByTotalList));
        }
        // 停机
        int sumStopByTotalList = stopByTotalList.stream().mapToInt(BigInteger :: intValue).sum();
        if(0 == sumStopByTotalList) {
            rsMap.put("sumStopTimeFont", "0分0秒");
        } else {
            rsMap.put("sumStopTimeFont", getTimeStrByTimestamp(sumRunByTotalList));
        }

        // 运行百分比
        if(0 == sumRunByTotalList) {
            rsMap.put("sumRunTimeFontPercent", "0");
        } else {
            rsMap.put("sumRunTimeFontPercent", getPercentByInputValue(String.valueOf(rsMap.get("sumRunTimeFont")),
                    String.valueOf(sumRunByTotalList + sumStandByTotalList + sumStopByTotalList)));
        }
        // 待机百分比
        if(0 == sumStandByTotalList) {
            rsMap.put("sumStandByTotalListPercent", "0");
        } else {
            rsMap.put("sumStandByTotalListPercent", getPercentByInputValue(String.valueOf(rsMap.get("sumStandByTimeFont")),
                    String.valueOf(sumRunByTotalList + sumStandByTotalList + sumStopByTotalList)));
        }
        // 停机百分比
        if(0 == sumStopByTotalList) {
            rsMap.put("sumStopByTotalListPercent", "0");
        } else {
            rsMap.put("sumStopByTotalListPercent", getPercentByInputValue(String.valueOf(rsMap.get("sumStopTimeFont")),
                    String.valueOf(sumRunByTotalList + sumStandByTotalList + sumStopByTotalList)));
        }

//        getPercentByInputValue
    }
    /**
     * 
     * @param time
     * @return
     */
    public static String getTimeStrByTimestamp(int time) {
        String rs = "0分0秒";
        int minute = time/1000;
        int minuteSecond = time%1000;
        int second = minuteSecond/1000*60;
        if(0 == minute) {
            rs = "0分";
        } else {
            rs = minute + "分";
        }

        if(0 == second) {
            rs += "0秒";
        } else {
            rs += second + "秒";
        }
        return rs;
    }

    /**
     * val1在val2的百分比
     * 
     * @param val1
     * @param val2
     * @return
     */
    public static String getPercentByInputValue(String val1, String val2) {
        String per = val1.substring(0, val1.lastIndexOf("分"));
//        String sum = val2.substring(0, val2.lastIndexOf("分"));

        DecimalFormat df1 = new DecimalFormat("#0.00%");
        if(StringUtils.isEmpty(val2) || "0".equals(val2))
            return "0";
//        String rs = df1.format(Integer.valueOf(per)/(Integer.valueOf(val2)/1000));
//        double f1 = new BigDecimal((float)Integer.valueOf(per)/(Integer.valueOf(val2)/1000)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        int f1 = new BigDecimal((float)Integer.valueOf(per)/(Integer.valueOf(val2)/1000)).multiply(BigDecimal.valueOf(100L)).intValue();
        String rs = String.valueOf(f1);
        return rs;
    }

}
