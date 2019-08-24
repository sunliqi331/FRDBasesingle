package com.its.frd.util;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

public class JSONUtils {

    /**
     * Bean对象转JSON
     * 
     * @param object
     * @return
     */
    public static String beanToJson(Object object) {
        if (object != null) {
            return JSON.toJSONString(object);
        } else {
            return null;
        }
    }

    /**
     * Bean对象转JSON
     * 
     * @param object
     * @return
     */
    public static JSONArray strToJSONArray(String str) {
        if (StringUtils.isNotBlank(str)) {
            return JSON.parseArray(str);
        } else {
            return null;
        }
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
