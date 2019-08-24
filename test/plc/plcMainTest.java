package plc;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import com.its.frd.util.ResourceUtil;

public class plcMainTest {

    public static void main(String[] args) {
        try {
            Socket sok = new Socket();
            String plcIp = ResourceUtil.getValueForDefaultProperties("plc.ip");
            int plcPort = Integer.valueOf(ResourceUtil.getValueForDefaultProperties("plc.port"));
            // sok.connect(new InetSocketAddress(InetAddress.getByName("192.168.217.45"), 2000)); // 连接服务器
            sok.connect(new InetSocketAddress(InetAddress.getByName(plcIp), plcPort)); // 连接服务器
            // 通路一建立，流就有了
            // 为了发送数据，要获取Socket流中的输出流
            OutputStream outS = sok.getOutputStream();
            byte[] test=new byte[]{(byte)0x05,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00};
            outS.write(test);
            sok.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    /*
     * 字节数组转16进制字符串
     */
    public static String bytes2HexString(byte[] b) {
        String r = "";
        
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            r += hex.toUpperCase();
        }
        
        return r;
    }

}
