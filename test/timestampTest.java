import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.google.common.util.concurrent.TimeLimiter;
import com.its.frd.util.DateUtils;

public class timestampTest {

	public static void main(String[] args) {
	    Timestamp startdate = new Timestamp(1534089600000L);//Timestamp.valueOf("1534089600000");
	    Timestamp enddate = new Timestamp(1534694399000L); //Timestamp.valueOf("1534694399000");
	    List<Timestamp> timesList = get7Day2(startdate);
	    for(Timestamp time : timesList) {
	        System.out.println(DateUtils.getyyyyMMddHHmmss(new Date(time.getTime())));
	    }
	}
	
	
    /**
     * 获取制定时间的所属的周的日期
     * 
     * @return
     */
    private static List<Timestamp> get7Day2(Timestamp startdate) {
        List<Timestamp> times = new ArrayList<>();
        Calendar cd = Calendar.getInstance();
        cd.setTime(new Date(startdate.getTime()));
        // 小时设置成00
        cd.set(Calendar.HOUR_OF_DAY, 0);
        // 分钟设置成00
        cd.set(Calendar.MINUTE, 0);
        // 秒钟设置成00
        cd.set(Calendar.SECOND, 0);
        // 毫秒设置成0
        cd.set(Calendar.MILLISECOND, 0);
        /*
         * Calendar cd2 = Calendar.getInstance();//把今天的结束时间置为23:59:59
         * cd2.set(Calendar.HOUR_OF_DAY, 23); cd2.set(Calendar.MINUTE, 59);
         * cd2.set(Calendar.SECOND, 59); cd2.set(Calendar.MILLISECOND, 0);
         */
        times.add(startdate);
        // times.add(new Timestamp(cd.getTime().getTime()));
        for (int i = 0; i < 6; i++) {
            cd.add(Calendar.DAY_OF_MONTH, 1);
            times.add(new Timestamp(cd.getTime().getTime()));
        }
        Collections.reverse(times);
        return times;
    }

}
