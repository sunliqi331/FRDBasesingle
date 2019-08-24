package com.its.frd.util;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.google.common.collect.Maps;
import com.its.frd.service.ProductAndEnergyAndDriverChartService.DateType;

public class DateUtils {
    /** 定义常量 **/
    public static final String DATE_JFP_STR = "yyyyMM";
    public static final String DATE_FULL_STR = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_SMALL_STR = "yyyy-MM-dd";
    public static final String DATE_SMALL_STRHH = "yyyy-MM-dd HH";
    public static final String DATE_SMALL_STRHH2 = "yyyyMMddHH";
    public static final String DATE_SSMALL_STR = "yyyy-MM";
    public static final String DATE_KEY_STR = "yyMMddHHmmss";
    public static final String DATE_FULL = "yyyyMMddHHmmss";
    public static final String DATE_SHORT_HH = "HH";

    /**
     * 使用预设格式提取字符串日期
     * 
     * @param strDate
     *            日期字符串
     * @return
     */
    public static Date parse(String strDate) {
        return parse(strDate, DATE_FULL_STR);
    }

    /**
     * 使用用户格式提取字符串日期
     * 
     * @param strDate
     *            日期字符串
     * @param pattern
     *            日期格式
     * @return
     */
    public static Date parse(String strDate, String pattern) {
        try {
            SimpleDateFormat df1 = new SimpleDateFormat(pattern);
            return df1.parse(strDate);
        } catch (ParseException e) {
            SimpleDateFormat df2 = new SimpleDateFormat(DATE_SMALL_STR);
            try {
                return df2.parse(strDate);
            } catch (ParseException e1) {
                SimpleDateFormat df3 = new SimpleDateFormat(DATE_FULL);
                try {
                    return df3.parse(strDate);
                } catch (ParseException e2) {
                    return null;
                }
            }
        }
    }

    /**
     * 两个时间比较
     * 
     * @param date
     * @return
     */
    public static int compareDateWithNow(Date date1) {
        Date date2 = new Date();
        int rnum = date1.compareTo(date2);
        return rnum;
    }

    /**
     * 两个时间比较(时间戳比较)
     * 
     * @param date
     * @return
     */
    public static int compareDateWithNow(long date1) {
        long date2 = dateToUnixTimestamp();
        if (date1 > date2) {
            return 1;
        } else if (date1 < date2) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * 获取系统当前时间
     * 
     * @return
     */
    public static String getNowTime() {
        SimpleDateFormat df = new SimpleDateFormat(DATE_FULL_STR);
        return df.format(new Date());
    }

    /**
     * 获取系统当前时间
     * 
     * @return
     */
    public static String getNowTime(String type) {
        SimpleDateFormat df = new SimpleDateFormat(type);
        return df.format(new Date());
    }

    /**
     * 获取系统当前计费期
     * 
     * @return
     */
    public static String getJFPTime() {
        SimpleDateFormat df = new SimpleDateFormat(DATE_JFP_STR);
        return df.format(new Date());
    }

    /**
     * 将指定的日期转换成Unix时间戳
     * 
     * @param String
     *            date 需要转换的日期 yyyy-MM-dd HH:mm:ss
     * @return long 时间戳
     */
    public static long dateToUnixTimestamp(String date) {
        long timestamp = 0;
        try {
            timestamp = new SimpleDateFormat(DATE_FULL_STR).parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timestamp;
    }

    /**
     * 将指定的日期转换成Unix时间戳
     * 
     * @param String
     *            date 需要转换的日期 yyyy-MM-dd
     * @return long 时间戳
     */
    public static long dateToUnixTimestamp(String date, String dateFormat) {
        long timestamp = 0;
        try {
            timestamp = new SimpleDateFormat(dateFormat).parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timestamp;
    }

    /**
     * 将当前日期转换成Unix时间戳
     * 
     * @return long 时间戳
     */
    public static long dateToUnixTimestamp() {
        long timestamp = new Date().getTime();
        return timestamp;
    }

    /**
     * 将Unix时间戳转换成日期
     * 
     * @param long
     *            timestamp 时间戳
     * @return String 日期字符串
     */
    public static String unixTimestampToDate(long timestamp) {
        SimpleDateFormat sd = new SimpleDateFormat(DATE_FULL_STR);
        // sd.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return sd.format(new Date(timestamp));
    }

    /**
     * @Title: getyyyyMMddHH
     * @Description: 返回日期字符串格式：getyyyyMMddHH
     * @param date
     *            当前日期
     * @return String 日期字符串
     */
    public static String getyyyyMMddHH(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(DATE_SMALL_STRHH);
        return df.format(date);
    }

    public static String getyyyyMMddHH2(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(DATE_SMALL_STRHH2);
        return df.format(date);
    }

    /**
     * @Title: getyyyyMMddHHmmss
     * @Description: 返回日期字符串格式：yyyyMMddHHmmss
     * @param date
     *            当前日期
     * @return String 日期字符串
     */
    public static String getyyyyMMddHHmmss(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(DATE_FULL);
        return df.format(date);
    }

    /**
     * 获取日期字符串，格式：yyyy-MM-dd
     * 
     * @Title: getYYYYMMDDDayStr
     */
    public static String getYYYYMMDDDayStr(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(DATE_SMALL_STR);
        return df.format(date);
    }

    /**
     * 获取日期字符串，格式：yyyy-MM-dd HH:mm:ss
     * 
     * @Title: getYYYYMMDDHHMMSSDayStr
     */
    public static String getYYYYMMDDHHMMSSDayStr(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(DATE_FULL_STR);
        return df.format(date);
    }

    /**
     * 获取日期字符串，格式：yyyy-MM
     * 
     * @Title: getYYYYMMDayStr
     */
    public static String getYYYYMMDayStr(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(DATE_SSMALL_STR);
        return df.format(new Date());
    }

    /**
     * 获取日期字符串，格式：yyyy-MM
     * 
     * @Title: getYYYYMMDayStr
     */
    public static String changeDateToStringHaveFormat(Date date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(new Date());
    }

    /**
     * 获得前一天的日期
     * 
     * @Title: getPreDay
     * @param date
     * @return Date 日期
     */
    public static Date getPreDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return calendar.getTime();
    }

    /**
     * 时间  前一小时
     * @param date
     * @return
     */
    public static Timestamp getPreHour(Timestamp date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, -1);//时间的 前一小时
        return new Timestamp(calendar.getTimeInMillis());
    }
    

    /**
     * 时间  前
     * 选择本日:0时前一小时
     * 选择本月:周一前一天
     * 选择本年:一月前一个月
     * @param date
     * @return
     */
    public static Timestamp getPreTime(Timestamp date, DateType dateType) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if(DateType.day.equals(dateType)) {
            calendar.add(Calendar.HOUR, -1);//时间的 前一小时
        } else if(DateType.week.equals(dateType) || DateType.month.equals(dateType)) {
            calendar.add(Calendar.DAY_OF_MONTH, -1);//时间的 前一天
        } else {
            calendar.add(Calendar.MONTH, -1);//时间的 前一个月
        }
        return new Timestamp(calendar.getTimeInMillis());
    }

    public static String getPreDateByStr(String value) {
        if (null != value && !value.equals("")) {
            return getYYYYMMDDDayStr(getPreDay(getDateByStr(value)));
        } else {
            return "";
        }
    }

    /**
     * 获取当前年
     * 
     * @Title: getCurrentYear
     * @return int
     */
    public static int getCurrentYear() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        return year;
    }

    public static Date getDateByStr(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 得到UTC时间，类型为字符串，格式为"yyyy-MM-dd HH:mm:ss"<br />
     * 如果获取失败，返回null
     * 
     * @return
     */
    public static Long getUTCTimeStr(String indate) {
        Date date = null;

        try {
            DateFormat format = new SimpleDateFormat(DATE_FULL_STR);
            date = format.parse(indate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance(TimeZone.getDefault());// TimeZone.getDefault()获取主机的默认

        cal.setTime(date);

        return date.getTime() + cal.getTimeZone().getRawOffset();
    }

    public void DateToUTC() {
        SimpleDateFormat foo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar gc = GregorianCalendar.getInstance();
        System.out.println("foo:" + foo.format(new Date()) + "    毫秒：" + gc.getTimeInMillis());

        // 1、取得本地时间：
        java.util.Calendar cal = java.util.Calendar.getInstance();

        // 2、取得时间偏移量：
        int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);

        // 3、取得夏令时差：
        int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);

        // 4、从本地时间里扣除这些差量，即可以取得UTC时间：
        cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));

        // 之后调用cal.get(int x)或cal.getTimeInMillis()方法所取得的时间即是UTC标准时间。
        System.out.println(
                "UTC:" + foo.format(new Date(cal.getTimeInMillis())) + "---------------" + cal.getTimeInMillis());

    }

    /**
     * 获得指定日期的前n小时
     * 
     * @param specifiedDay
     * @return
     * @throws Exception
     */
    public static String getSpecifiedHourBefore(String specifiedDay, int d) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        DateFormat format = new SimpleDateFormat(DATE_FULL_STR);
        try {
            date = format.parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        c.add(Calendar.HOUR_OF_DAY, -d);// before 8 hour
        return format.format(c.getTime());
    }

    /**
     * 取当前时间的前N个小时
     * 
     * @return
     */
    public static String beforeOneHourToNowDate(Integer hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - hour);
        SimpleDateFormat df = new SimpleDateFormat(DATE_FULL_STR);
        // System.out.println("一个小时前的时间："+ df.format(calendar.getTime()));
        // System.out.println("当前的时间："+ df.format(new Date()));
        return df.format(calendar.getTime());
    }

    public static String beforeOneHourToNowDate(Integer hour, String datefotmet) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - hour);
        SimpleDateFormat df = new SimpleDateFormat(datefotmet);
        // System.out.println("一个小时前的时间："+ df.format(calendar.getTime()));
        // System.out.println("当前的时间："+ df.format(new Date()));
        return df.format(calendar.getTime());
    }

    public static String getTimeFromLong(String d) {
        Date dat = new Date(Long.parseLong(d));
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dat);
        DateFormat format = new SimpleDateFormat(DATE_FULL_STR);
        return format.format(gc.getTime());
    }

    /**
     * 计算两个时间相差的小时数
     * 
     * @param startTime
     *            开始时间字符串 yyyy-MM-dd HH:mm:ss
     * @param endTime
     *            结束时间字符串 yyyy-MM-dd HH:mm:ss
     * @return 小时数
     */
    public static long getTwoTimeHour(String startTime, String endTime) {
        long hour = 0;
        if (DATE_FULL_STR != null && DATE_FULL_STR != null) {
            long start = dateToUnixTimestamp(startTime, DATE_FULL_STR);
            long end = dateToUnixTimestamp(endTime, DATE_FULL_STR);
            long time = end - start;
            hour = time / 3600000;
        }
        return hour;
    }

    public static String getTimeStrByTimeStamp(Timestamp timestamp) {
        String rsTime = "";
        DateFormat sdf = new SimpleDateFormat(DATE_FULL_STR);
        try {
            // 方法一
            rsTime = sdf.format(timestamp);
            System.out.println(rsTime);
            // //方法二
            // tsStr = ts.toString();
            // System.out.println(tsStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rsTime;
    }

    /**
     * 获取当天的24小时段集合; 例如: 2016-12-13 00:00:00 ,2016-12-13 01:00:00
     * 
     * @return
     */
    private static List<Timestamp> getDay24Hours() {
        List<Timestamp> items = new ArrayList<>();
        Calendar cd = Calendar.getInstance();
        // 分钟设置成00
        cd.set(Calendar.MINUTE, 0);
        // 秒钟设置成00
        cd.set(Calendar.SECOND, 0);
        // 毫秒设置成0
        cd.set(Calendar.MILLISECOND, 0);
        for (int i = 0; i < 24; i++) {
            cd.set(Calendar.HOUR_OF_DAY, i);
            items.add(new Timestamp(cd.getTime().getTime()));
        }
        return items;
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

        Calendar cdEnd = Calendar.getInstance();
        cdEnd.setTime(new Date(startdate.getTime()));
        // 小时设置成00
        cdEnd.set(Calendar.HOUR_OF_DAY, 23);
        // 分钟设置成00
        cdEnd.set(Calendar.MINUTE, 59);
        // 秒钟设置成00
        cdEnd.set(Calendar.SECOND, 59);
        // 毫秒设置成0
        cdEnd.set(Calendar.MILLISECOND, 0);
        /*
         * Calendar cd2 = Calendar.getInstance();//把今天的结束时间置为23:59:59
         * cd2.set(Calendar.HOUR_OF_DAY, 23); cd2.set(Calendar.MINUTE, 59);
         * cd2.set(Calendar.SECOND, 59); cd2.set(Calendar.MILLISECOND, 0);
         */
        times.add(startdate);
        // times.add(new Timestamp(cd.getTime().getTime()));
        for (int i = 0; i < 6; i++) {
            cd.add(Calendar.DAY_OF_MONTH, 1);
            cdEnd.add(Calendar.DAY_OF_MONTH, 1);
            times.add(new Timestamp(cd.getTime().getTime()));
            times.add(new Timestamp(cdEnd.getTime().getTime()));
        }
        Collections.reverse(times);
        return times;
    }

    /**
     * 获取当月天数时间段
     * 
     * @return
     */
    private static List<Timestamp> getMonthDay() {
        List<Timestamp> days = new ArrayList<>();
        Calendar cd = Calendar.getInstance();
        // 小时设置成00
        cd.set(Calendar.HOUR_OF_DAY, 0);
        // 分钟设置成00
        cd.set(Calendar.MINUTE, 0);
        // 秒钟设置成00
        cd.set(Calendar.SECOND, 0);
        // 毫秒设置成0
        cd.set(Calendar.MILLISECOND, 0);
        for (int i = 1; i <= cd.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            /*
             * if(i==30){ //把月末的结束时间置为23:59:59 cd.set(Calendar.HOUR_OF_DAY, 23);
             * cd.set(Calendar.MINUTE, 59); cd.set(Calendar.SECOND, 59); }
             */
            cd.set(Calendar.DAY_OF_MONTH, i);
            days.add(new Timestamp(cd.getTime().getTime()));
        }
        return days;
    }

    /**
     * 根据开始,结束时间拆分为以小时的时间段
     * 
     * @param start
     * @param end
     * @return
     */
    private static List<Timestamp> getDefineTimeForHours(Timestamp start, Timestamp end) {
        List<Timestamp> hours = new ArrayList<>();
        Calendar startCd = Calendar.getInstance();
        startCd.setTime(new Date(start.getTime()));
        // 分钟设置成00
        startCd.set(Calendar.MINUTE, 0);
        // 秒钟设置成00
        startCd.set(Calendar.SECOND, 0);
        // 毫秒设置成0
        startCd.set(Calendar.MILLISECOND, 0);

        Calendar endCd = Calendar.getInstance();
        endCd.setTime(new Date(end.getTime()));
        // 分钟设置成00
        startCd.set(Calendar.MINUTE, 0);
        // 秒钟设置成00
        startCd.set(Calendar.SECOND, 0);
        // 毫秒设置成0
        startCd.set(Calendar.MILLISECOND, 0);

        while (startCd.getTime().getTime() <= endCd.getTime().getTime()) {
            System.out.println(startCd.getTime() + "-" + endCd.getTime());
            hours.add(new Timestamp(startCd.getTime().getTime()));
            startCd.add(Calendar.HOUR_OF_DAY, 1);
        }
        return hours;
    }

    /**
     * 拆分时间段为天数间隔
     * 
     * @param start
     * @param end
     * @return
     */
    private static List<Timestamp> getDefineTimeForDay(Timestamp start, Timestamp end) {
        List<Timestamp> days = new ArrayList<>();
        Calendar startCd = Calendar.getInstance();
        startCd.setTime(new Date(start.getTime()));
        // 分钟设置成00
        startCd.set(Calendar.MINUTE, 0);
        // 秒钟设置成00
        startCd.set(Calendar.SECOND, 0);
        // 毫秒设置成0
        startCd.set(Calendar.MILLISECOND, 0);

        Calendar endCd = Calendar.getInstance();
        endCd.setTime(new Date(end.getTime()));
        // 分钟设置成00
        endCd.set(Calendar.MINUTE, 0);
        // 秒钟设置成00
        endCd.set(Calendar.SECOND, 0);
        // 毫秒设置成0
        endCd.set(Calendar.MILLISECOND, 0);
        while (startCd.getTime().getTime() <= endCd.getTime().getTime()) {
            days.add(new Timestamp(startCd.getTime().getTime()));
            startCd.add(Calendar.DAY_OF_MONTH, 1);
        }
        return days;
    }

    /**
     * 拆分月份段集合
     * 
     * @param start
     * @param end
     * @return
     */
    private static List<Timestamp> getDefineTimeForMonth(Timestamp start, Timestamp end) {
        List<Timestamp> months = new ArrayList<>();
        Calendar startCd = Calendar.getInstance();
        startCd.setTime(new Date(start.getTime()));
        // 日期设置为1号
        startCd.set(Calendar.DAY_OF_MONTH, 1);
        // 小时设置成00
        startCd.set(Calendar.HOUR_OF_DAY, 0);
        // 分钟设置成00
        startCd.set(Calendar.MINUTE, 0);
        // 秒钟设置成00
        startCd.set(Calendar.SECOND, 0);
        // 毫秒设置成0
        startCd.set(Calendar.MILLISECOND, 0);

        Calendar endCd = Calendar.getInstance();
        endCd.setTime(new Date(end.getTime()));

        while (startCd.getTime().getTime() <= endCd.getTime().getTime()) {
            months.add(new Timestamp(startCd.getTime().getTime()));
            startCd.add(Calendar.MONTH, 1);
        }
        return months;
    }

    /**
     * 拆分自定义时间段
     * 
     * @return
     */
    private static List<Timestamp> getDefineTime(Timestamp start, Timestamp end) {
        /*
         * 1.当日期在一天以内的,以小时类分时间段; 当跨日时,需要注意日期; 2.当时间段差超过一天,且小于30天时,以开始时间与结束时间差的天数为分割时间段;
         * 3.当时间段差超过30天时,以月份段来划分,以跨度的月份来划分; 例如: 当跨度了3个月时,就以跨度的月份段来分;
         */
        if (start == null || end == null)
            return null;
        if ((end.getTime() - start.getTime()) <= 86400000L) {
            // 小于一天
            return getDefineTimeForHours(start, end);
        } else if ((end.getTime() - start.getTime()) > 86400000L && (end.getTime() - start.getTime()) <= 2592000000L) {
            // 大于一天,小于等于31天
            return getDefineTimeForDay(start, end);
        } else if ((end.getTime() - start.getTime()) > 2592000000L) {
            // 大于31天
            return getDefineTimeForMonth(start, end);
        }
        // 都不符合直接返回null
        return null;
    }

    /**
     * 把时间段拆分
     * 
     * @param dateType
     *            按什么类型来区分时间段
     * @param startDate
     *            开始时间
     * @param endDate
     *            结束时间
     * @return 时间分段集合
     */
    public static List<Timestamp> getDateList(DateType dateType, Timestamp startDate, Timestamp endDate) {
        List<Timestamp> times = null;
        if (DateType.day.equals(dateType)) {
            // 当天,分24小时段
            times = getDay24Hours();
        } else if (DateType.week.equals(dateType)) {
            // 一星期,当天向前推7天,分7段时间
            times = get7Day2(startDate);
            return times;
            // times = this.get7Day();
        } else if (DateType.month.equals(dateType)) {
            // 当月,分当月的天数段
            times = getMonthDay();
        } else if (DateType.defineDate.equals(dateType)) {
            // 自定义时间:
            times = getDefineTime(startDate, endDate);
        }
        // 填充timestamp中最后一个数据
        if ((endDate.getTime() - startDate.getTime()) > 2592000000L) { // 如果时间间隔大于一个月
            Timestamp timestamp = times.get(times.size() - 1);
            Calendar startCd = Calendar.getInstance();
            startCd.setTime(new Date(timestamp.getTime()));
            // 设置当月最后一天
            String last = timestamp.toString().split("-")[0] + "-" + timestamp.toString().split("-")[1] + "-"
                    + startCd.getActualMaximum(Calendar.DAY_OF_MONTH) + " 23:59:59";
            Timestamp lastValue = Timestamp.valueOf(last);
            times.add(lastValue);
        } else {
            Timestamp timestamp = times.get(times.size() - 1);
            String last = timestamp.toString().split(" ")[0] + " 23:59:59";
            Timestamp lastValue = Timestamp.valueOf(last);
            times.add(lastValue);
        }
        return times;
    }

    public static Map<String, Timestamp> getEndTimestamp(Timestamp startDate) {
        Map<String, Timestamp> rsMap = Maps.newHashMap();
        Timestamp endDate;
        Calendar cd = Calendar.getInstance();
        cd.setTime(new Date(startDate.getTime()));
        // 小时设置成00
        cd.set(Calendar.HOUR_OF_DAY, 23);
        // 分钟设置成00
        cd.set(Calendar.MINUTE, 59);
        // 秒钟设置成00
        cd.set(Calendar.SECOND, 59);
        // 毫秒设置成0
        cd.set(Calendar.MILLISECOND, 0);
        endDate = new Timestamp(cd.getTime().getTime());
        rsMap.put("end", endDate);

        Timestamp startTime;
        cd = Calendar.getInstance();
        cd.setTime(new Date(startDate.getTime()));
        // 小时设置成00
        cd.set(Calendar.HOUR_OF_DAY, 0);
        // 分钟设置成00
        cd.set(Calendar.MINUTE, 0);
        // 秒钟设置成00
        cd.set(Calendar.SECOND, 0);
        // 毫秒设置成0
        cd.set(Calendar.MILLISECOND, 0);
        startTime = new Timestamp(cd.getTime().getTime());
        rsMap.put("start", startTime);
        return rsMap;
    }
    

    public static void main(String[] args) {
        System.out.println(DateUtils.beforeOneHourToNowDate(24));
    }

    /** 
     * 取得当前时间戳（精确到秒） 
     * @return 
     */  
    public static String timeStamp(){  
        long time = System.currentTimeMillis();
        String t = String.valueOf(time/1000);  
        return t;  
    }

    /** 
     * 时间戳转换成日期格式字符串 
     * @param seconds 精确到秒的字符串 
     * @param formatStr 
     * @return 
     */  
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

    /** 
     * 日期格式字符串转换成时间戳 
     * @param date 字符串日期 
     * @param format 如：yyyy-MM-dd HH:mm:ss 
     * @return 
     */  
    public static String date2TimeStamp(String date_str,String format){  
        try {  
            SimpleDateFormat sdf = new SimpleDateFormat(format);  
            return String.valueOf(sdf.parse(date_str).getTime()/1000);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return "";  
    }
    
    public static Map<String, Timestamp> getEndTimestampForOneDay() {
        Map<String, Timestamp> rsMap = Maps.newHashMap();
        Timestamp endDate;
        Calendar cd = Calendar.getInstance();
        cd.setTime(new Date());
        // 小时设置成00
        cd.set(Calendar.HOUR_OF_DAY, 23);
        // 分钟设置成00
        cd.set(Calendar.MINUTE, 59);
        // 秒钟设置成00
        cd.set(Calendar.SECOND, 59);
        // 毫秒设置成0
        cd.set(Calendar.MILLISECOND, 0);
        endDate = new Timestamp(cd.getTime().getTime());
        rsMap.put("end", endDate);

        Timestamp startTime;
        cd = Calendar.getInstance();
        cd.setTime(new Date());
        // 小时设置成00
        cd.set(Calendar.HOUR_OF_DAY, 0);
        // 分钟设置成00
        cd.set(Calendar.MINUTE, 0);
        // 秒钟设置成00
        cd.set(Calendar.SECOND, 0);
        // 毫秒设置成0
        cd.set(Calendar.MILLISECOND, 0);
        startTime = new Timestamp(cd.getTime().getTime());
        rsMap.put("start", startTime);
        return rsMap;
    }
}