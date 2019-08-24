package com.its.frd.websocket;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class SocketSessionUtil {


    private static Map<String, String> clients = new ConcurrentHashMap<>();

    /**
     * 保存一个连接
     * @param inquiryId
     * @param empNo
     * @param session
     */
    public static void add(String sessionId, String session){
        clients.put(sessionId, session);
    }

    /**
     * 获取一个连接
     * @param inquiryId
     * @param empNo
     * @return
     */
    public static String get(String sessionId){
        return clients.get(sessionId);
    }

    /**
     * 移除一个连接
     * @param inquiryId
     * @param empNo
     */
    public static void remove(String sessionId) {
        clients.remove(sessionId);
    }


    /**
     * 判断是否有效连接
     * 判断是否存在
     * 判断连接是否开启
     * 无效的进行清除
     * @param inquiryId
     * @param empNo
     * @return
     */
    public static boolean hasConnection(String sessionId) {
        if (clients.containsKey(sessionId)) {
            return true;
        }

        return false;
    }

    /**
     * 获取连接数的数量
     * @return
     */
    public static int getSize() {
        return clients.size();
    }


}