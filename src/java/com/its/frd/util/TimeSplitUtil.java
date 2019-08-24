package com.its.frd.util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.its.frd.service.ProductAndEnergyAndDriverChartService.DateType;

public class TimeSplitUtil {

    /**
     * 把时间段拆分,返回字符串集合
     * 
     * @param dateType
     *            按什么类型来区分时间段
     * @param startDate
     *            开始时间
     * @param endDate
     *            结束时间
     * @return 时间分段字符串集合
     */
    public static List<String> getDateListStr(DateType dateType, Timestamp startDate, Timestamp endDate) {
        List<String> datesStr = null;
        if (DateType.day.equals(dateType)) {
            // 当天,分24小时段
            datesStr = get24HoursStr();
        } else if (DateType.week.equals(dateType)) {
            // 一星期,当天向前推7天,分7段时间
            // datesStr = this.get7DayStr();
            datesStr = get7DayStr2(startDate);
        } else if (DateType.month.equals(dateType)) {
            // 当月,分当月的天数段
            datesStr = getMonthDayStr();
        } else if (DateType.defineDate.equals(dateType)) {
            // 自定义时间:
            datesStr = getDefineTimeStr(startDate, endDate);
        }
        return datesStr;
    }

    /**
     * 把时间段拆分,返回字符串集合 查处对应
     * 
     * @param dateType
     *            按什么类型来区分时间段
     * @param startDate
     *            开始时间
     * @param endDate
     *            结束时间
     * @return 时间分段字符串集合
     */
    public List<String> getBatchIdsByDateListStr(DateType dateType, Timestamp startDate, Timestamp endDate) {
        List<String> datesStr = null;
        if (DateType.day.equals(dateType)) {
            // 当天,分24小时段
            datesStr = this.get24HoursStr();
        } else if (DateType.week.equals(dateType)) {
            // 一星期,当天向前推7天,分7段时间
            datesStr = this.get7DayStr();
        } else if (DateType.month.equals(dateType)) {
            // 当月,分当月的天数段
            datesStr = this.getMonthDayStr();
        } else if (DateType.defineDate.equals(dateType)) {
            // 自定义时间:
            datesStr = this.getDefineTimeStr(startDate, endDate);
        }
        return datesStr;
    }

    /**
     * 获得字符串时间整数,例如 2016-12-13 02:00:00 --> 字符串2
     * 
     * @param times
     * @return
     */
    private static List<String> get24HoursStr() {
        List<String> timeStrs = new ArrayList<>();
        List<Timestamp> times = getDay24Hours();
        for (Timestamp time : times) {
            timeStrs.add(time.getHours() + "时");
        }
        return timeStrs;
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
     * 获取7天
     * 
     * @return
     */
    private List<String> get7DayStr() {
        List<String> sivenDay = new ArrayList<>();
        List<Timestamp> times = this.get7Day();
        Calendar cd = Calendar.getInstance();
        for (Timestamp time : times) {
            cd.setTime(new Date(time.getTime()));
            sivenDay.add(cd.get(Calendar.MONTH) + 1 + "月" + cd.get(Calendar.DAY_OF_MONTH) + "日");
        }
        return sivenDay;
    }

    /**
     * 获取当天向前推6天,包含当天的7天时间段的集合
     * 
     * @return
     */
    private List<Timestamp> get7Day() {
        List<Timestamp> times = new ArrayList<>();
        Calendar cd = Calendar.getInstance();
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
        times.add(new Timestamp(cd.getTime().getTime()));
        for (int i = 0; i > -6; i--) {
            cd.add(Calendar.DAY_OF_MONTH, -1);
            times.add(new Timestamp(cd.getTime().getTime()));
        }
        Collections.reverse(times);
        return times;
    }

    /**
     * 获取7天
     * 
     * @return
     */
    private static List<String> get7DayStr2(Timestamp startdate) {
        List<String> sivenDay = new ArrayList<>();
        List<Timestamp> times = get7Day2(startdate);
        Calendar cd = Calendar.getInstance();
        for (Timestamp time : times) {
            cd.setTime(new Date(time.getTime()));
            sivenDay.add(cd.get(Calendar.MONTH) + 1 + "月" + cd.get(Calendar.DAY_OF_MONTH) + "日");
        }
        return sivenDay;
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

    /**
     * 获取当月天数时间日的字符串集合,例如: 12日,13日...
     * 
     * @return
     */
    private static List<String> getMonthDayStr() {
        List<String> dayStr = new ArrayList<>();
        List<Timestamp> dayTimes = getMonthDay();
        Calendar cd = Calendar.getInstance();
        for (Timestamp time : dayTimes) {
            cd.setTime(new Date(time.getTime()));
            dayStr.add(cd.get(Calendar.DAY_OF_MONTH) + "日");
        }
        return dayStr;
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
     * 拆分自定义时间段,返回字符串集合
     * 
     * @return
     */
    private static List<String> getDefineTimeStr(Timestamp start, Timestamp end) {
        if ((end.getTime() - start.getTime()) <= 86400000L) {
            // 小于一天
            return getDefineTimeForHoursStr(start, end);
        } else if ((end.getTime() - start.getTime()) > 86400000L && (end.getTime() - start.getTime()) <= 2592000000L) {
            // 大于一天,小于等于31天
            return getDefineTimeForDayStr(start, end);
        } else if ((end.getTime() - start.getTime()) > 2592000000L) {
            // 大于31天
            return getDefineTimeForMonthStr(start, end);
        }
        // 不符合返回null
        return null;
    }

    /**
     * 获取自定义时间段小时段字符串,例如 13日1时
     * 
     * @param start
     * @param end
     * @return
     */
    private static List<String> getDefineTimeForHoursStr(Timestamp start, Timestamp end) {
        List<String> hoursStr = new ArrayList<>();
        List<Timestamp> times = getDefineTimeForHours(start, end);
        Calendar cd = Calendar.getInstance();
        for (Timestamp time : times) {
            cd.setTime(new Date(time.getTime()));
            hoursStr.add(cd.get(Calendar.DAY_OF_MONTH) + "日" + cd.get(Calendar.HOUR_OF_DAY) + "时");
        }
        return hoursStr;
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
     * 以天为标准拆分,返回字符串集合
     * 
     * @param start
     * @param end
     * @return
     */
    private static List<String> getDefineTimeForDayStr(Timestamp start, Timestamp end) {
        List<String> daysStr = new ArrayList<>();
        List<Timestamp> times = getDefineTimeForDay(start, end);
        Calendar cd = Calendar.getInstance();
        for (Timestamp time : times) {
            cd.setTime(new Date(time.getTime()));
            daysStr.add(cd.get(Calendar.MONTH) + 1 + "月" + cd.get(Calendar.DAY_OF_MONTH) + "日");
        }
        return daysStr;
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
     * 以月为时间段区分,返回字符串集合
     * 
     * @param start
     * @param end
     * @return
     */
    private static List<String> getDefineTimeForMonthStr(Timestamp start, Timestamp end) {
        List<String> months = new ArrayList<>();
        List<Timestamp> times = getDefineTimeForMonth(start, end);
        Calendar cd = Calendar.getInstance();
        for (Timestamp time : times) {
            cd.setTime(new Date(time.getTime()));
            months.add((cd.get(Calendar.MONTH) + 1) + "月");
        }
        return months;
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
}
