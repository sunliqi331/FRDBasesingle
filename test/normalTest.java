import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.its.frd.util.DateUtils;
import com.its.frd.util.JSONUtils;
import com.its.monitor.vo.MesPointsTemplate;

public class normalTest {

    public static void main(String[] args) {

        // String value = "555_272_438_58813116411_288_1520488453_sn1013-1";
        // String valueArray[] = value.split("_");
        // String lineId = valueArray[1];
        // String driverId = valueArray[2];
        // String productId = valueArray[3];
        // String produrceId = valueArray[4];
        // System.out.println("产线ID：" + lineId);
        // System.out.println("设备ID：" + driverId);
        // System.out.println("产品ID：" + productId);
        // System.out.println("工序ID：" + produrceId);
        //
        // String pointIdArray = "1001,1001,";
        // System.out.println(pointIdArray.lastIndexOf("22"));
        // System.out.println(pointIdArray.substring(0, pointIdArray.lastIndexOf(",")));
//        String time = "1529337600000";
//        Date start = DateUtils.parse(time);
//        System.out.println(start.getTime());
//        String time = "2018-06-19 00:00:00";
//        Timestamp startTime = Timestamp.valueOf(time);
//        System.out.println(startTime.getTime());
//
//        String time1 = "2018-06-19 23:59:59";
//        Timestamp endTime = Timestamp.valueOf(time1);
//        System.out.println(endTime.getTime());
//        Date date1 = null;
//        try {
//            date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(time);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println("1529427600000".substring(0, 10));
//        List<Long> list = Lists.newArrayList();
//        list.add(1L);
//        list.add(2L);
//        list.add(3L);
//        StringBuffer stringBuffer = new StringBuffer();
//        for(Long num : list) {
//            System.out.println(String.valueOf(num));
//            stringBuffer.append(String.valueOf(num));
//            stringBuffer.append(",");
//        }
//        System.out.println(stringBuffer.substring(0, stringBuffer.length()-1));
//        System.out.println(JSONUtils.beanToJson(list));
//        String str = JSONUtils.beanToJson(list);
//        JSONArray jsonArray = JSONUtils.strToJSONArray(str);
//        for(Object obj : jsonArray) {
//            System.out.println((String)obj);
//        }
//        System.out.println(str[0]);
        System.out.println(MesPointsTemplate.class.getSimpleName());
    }

}
