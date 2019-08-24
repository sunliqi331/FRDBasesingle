import java.net.InetAddress;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;

public class ping {

    private static final byte[] DEFAULT_KEY = new byte[]{-97,88,-94,9,70,-76,126,25,0,3,-20,113,108,28,69,125}; 
    
    public static void main(String[] args) throws Exception {
        
//        int  timeOut =  3000 ;  //超时应该在3钞以上        
//        boolean status = InetAddress.getByName("139.196.142.55").isReachable(timeOut);     // 当返回值是true时，说明host是可用的，false则不可。
//        System.out.println(status);
        
//        long time = 1526888660000L;
//        //time = 1526889067415L;
//        System.out.println(new Timestamp(time));
//        System.out.println(DateUtils.unixTimestampToDate(time));
//        System.out.println(new Date().getTime());
//        System.out.println(DateUtils.unixTimestampToDate(new Date().getTime()));
        
//        int num1 = 7;
//        int num2 = 9;
//        // 创建一个数值格式化对象
//        NumberFormat numberFormat = NumberFormat.getInstance();
//        // 设置精确到小数点后2位
//        numberFormat.setMaximumFractionDigits(2);
//        String result = numberFormat.format((float) num1 / (float) num2 * 100);
////        System.out.println("num1和num2的百分比为:" + result + "%");
//            String url = "192.168.217.34";
//            //url = "gw.api.taobao.com/router/rest";
//            System.out.println(ping(url));
//        NumberFormat nf   =   NumberFormat.getPercentInstance();
//        System.out.println(nf.format((float)"0.47"));
//        DecimalFormat df1 = new DecimalFormat("##.00%");    //##.00%   百分比格式，后面不足2位的用0补齐
//        System.out.println(df1.format((float)1/3));
//           List<Integer> testList = Lists.newArrayList();
//           testList.add(10);
//           testList.add(3);
//           testList.add(2);
//           testList.add(4);
//           Collections.sort(testList, Collections.reverseOrder());
//           for(int i : testList) {
//               System.out.println(i);
//           }
//           System.out.println("------------------");
//           System.out.println(testList.get(0));
        
//        String date = timeStamp2Date("1532506107", "yyyy-MM-dd HH:mm:ss");  
//        System.out.println(date);
//        
//        String param = "123456789";
//        System.out.println(param.substring(param.length() -2, param.length() -1));
        
        String percentVal = "12.35%";
        System.out.println(percentVal.substring(0, percentVal.length() - 1));

    }
    
    public static String timeStamp2Date(String seconds,String format) {  
        if(seconds == null || seconds.isEmpty() || seconds.equals("null")){  
            return "";  
        }  
        if(format == null || format.isEmpty()){
            format = "yyyy-MM-dd HH:mm:ss";
        }   
        SimpleDateFormat sdf = new SimpleDateFormat(format);  
        return sdf.format(new Date(Long.valueOf(seconds+"000")));  
    } 
    
    public static boolean ping(String ipAddress) throws Exception {
        int  timeOut =  3000 ;  //超时应该在3钞以上        
        boolean status = InetAddress.getByName(ipAddress).isReachable(timeOut);
        // 当返回值是true时，说明host是可用的，false则不可。
        return status;
    }

}
