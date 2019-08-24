package plc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class tcpdemo {
    public static void main(String[] args) throws Exception { 
        
        /**ServerSocket服务=======================
         * */
        //1、建立服务
         ServerSocket svSok = new ServerSocket(2000);
         
         //2、阻塞监听，并负责接受连接-------------返回值：Socket
         Socket sok = svSok.accept();   
         
         //3、已连接，获取对方ip及端口，获取通道的流管道读取数据，发送数据
         // 3.1 得到客户端的流，使用输入流进行读取  ------> new BufferedReader(new InputStreamReader(sok.getInputStream())).readline()
         //         读取数据
         // 3.2 将读取到的数据原文发送回去
         System.out.println("【Receive from "+sok.getInetAddress().getHostAddress()+": "+sok.getPort()+"】");
          
         BufferedReader brReader = new BufferedReader(new InputStreamReader(sok.getInputStream()));
         String recvStrs = brReader.readLine(); //阻塞读--|-->
         System.out.println(recvStrs);
          
         //BufferedWriter brWriter = new BufferedWriter(new OutputStreamWriter(sok.getOutputStream()));
         //brWriter.write("received");
         OutputStream outS = sok.getOutputStream();
         outS.write(recvStrs.getBytes());
    } 
}
