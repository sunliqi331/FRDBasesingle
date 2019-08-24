package com.its.frd.util;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class PLCTcpUtil {
    
    private final static String PLC_LINE2_ID = ResourceUtil.getValueForDefaultProperties("plc.line2");
    
    private final static String PLC_LINE3_ID = ResourceUtil.getValueForDefaultProperties("plc.line3");
    
    private final static String PLC_LINE4_ID = ResourceUtil.getValueForDefaultProperties("plc.line4");
    
    private final static String PLC_LINE5_ID = ResourceUtil.getValueForDefaultProperties("plc.line5");
    
    private final static String PLC_LINE6_ID = ResourceUtil.getValueForDefaultProperties("plc.line6");

    private static void plcTcpip_client(String alertLineId){
        try {
            Socket sok = new Socket();
            String plcIp = ResourceUtil.getValueForDefaultProperties("plc.ip");
            int plcPort = Integer.valueOf(ResourceUtil.getValueForDefaultProperties("plc.port"));
            // sok.connect(new InetSocketAddress(InetAddress.getByName("192.168.217.45"), 2000)); // 连接服务器
            sok.connect(new InetSocketAddress(InetAddress.getByName(plcIp), plcPort)); // 连接服务器
            // 通路一建立，流就有了
            // 为了发送数据，要获取Socket流中的输出流
            OutputStream outS = sok.getOutputStream();
            byte lineIdSend = (byte)0x01;
            switch (alertLineId) {
                case "2":
                    lineIdSend = (byte)0x02;
                    break;

                case "3":
                    lineIdSend = (byte)0x02;
                    break;

                case "4":
                    lineIdSend = (byte)0x02;
                    break;

                case "5":
                    lineIdSend = (byte)0x02;
                    break;

                case "6":
                    lineIdSend = (byte)0x02;
                    break;

            }

            byte[] test=new byte[]{lineIdSend,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00};
            outS.write(test);
            sok.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    
    private static String getLineId(String alertLineId){
        String lineid = "1";
        switch(alertLineId) {
            case "1":
            lineid = "2";
            break;
        }
        return lineid;
    }
}
