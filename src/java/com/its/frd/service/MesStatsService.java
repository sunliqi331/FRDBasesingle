package com.its.frd.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.activemq.leveldb.util.TimeMetric;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.abel533.echarts.series.K;
import com.github.abel533.echarts.style.ItemStyle;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.its.common.util.dwz.Page;
import com.its.common.util.persistence.DynamicSpecifications;
import com.its.common.util.persistence.SearchFilter;
import com.its.common.util.persistence.SearchFilter.Operator;
import com.its.common.utils.SecurityUtils;
import com.its.frd.dao.CompanyinfoDao;
import com.its.frd.dao.MesDriverDao;
import com.its.frd.dao.MesDriverPointsDao;
import com.its.frd.dao.MesDriverStatsDao;
import com.its.frd.dao.MesDriverTypePropertyDao;
import com.its.frd.dao.MesEnergyDao;
import com.its.frd.dao.MesProductDao;
import com.its.frd.dao.MesProductionDao;
import com.its.frd.dao.MesProductlineDao;
import com.its.frd.echarts.entity.BarChart;
import com.its.frd.echarts.entity.BarChart.SeriesItem;
import com.its.frd.entity.Companyinfo;
import com.its.frd.entity.MesDataDriverStatus;
import com.its.frd.entity.MesDataProductProcedure;
import com.its.frd.entity.MesDataWeg;
import com.its.frd.entity.MesDriver;
import com.its.frd.entity.MesDriverStats;
import com.its.frd.entity.MesEnergy;
import com.its.frd.entity.MesProduct;
import com.its.frd.entity.MesProduction;
import com.its.frd.entity.MesProductline;
import com.its.frd.service.ProductAndEnergyAndDriverChartService.BarKeyType;
import com.its.frd.service.ProductAndEnergyAndDriverChartService.DateType;
import com.its.frd.service.ProductAndEnergyAndDriverChartService.PassType;
import com.its.frd.service.ProductAndEnergyAndDriverChartService.TypeScope;
import com.its.frd.util.DateUtils;
import com.its.frd.util.JSONUtils;
import com.its.frd.util.MesAnylsanysUtils;
import com.its.frd.util.MesDataRowkeyUtil;
import com.its.statistics.service.StatisticsService;

@Service
public class MesStatsService {

    @Resource
    private MesProductionDao mesProDao; // 产量
    @Resource
    private MesEnergyDao energyDao; // 能耗
    @Resource
    private MesDriverDao driverDao; // 设备
    @Resource
    private CompanyinfoDao cpDao; // 公司(工厂)
    @Resource
    private MesProductDao productDao; // 产品
    @Resource
    private MesProductlineDao productLineDao; // 产线
    @Resource
    private MesDriverPointsDao driverPointDao; // 设备测点
    @Resource
    private MesDriverStatsDao driverStatsDao;
    @Resource
    private MesDriverTypePropertyDao driverTypePropertyDao;
    @Resource
    private StatisticsService statisticsService;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private MesDataWegService mesDataWegService;
    @Resource
    private MesDriverService mesDriverService;
    @Resource
    private ProductAndEnergyAndDriverChartService productChartServ;
    @Autowired
    private MesDataDriverStatusService mesDataDriverStatusService;

    /**
     * 
     * @param startDateMarks
     * @param endDate
     * @param typeScope
     * @param modelnum
     * @param id
     * @param dateType
     * @returnbottom
     */
    @SuppressWarnings("unchecked")
    public Map<BarKeyType, Object> getProductionData(Timestamp startDate, Timestamp endDate, TypeScope typeScope,
            String modelnum, Long id, DateType dateType,
            String productBatchid) {
        if (id == null || dateType == null || dateType.equals(""))
            return null;
        // 当为自定义时间时,如果开始时间或者结束时间为空则返回null;
        if (dateType.equals(DateType.defineDate) && (startDate == null || endDate == null))
            return null;
        // 开始时间不能大于结束时间
        if (startDate.getTime() > endDate.getTime())
            return null;
        // 返回结果集
        Map<BarKeyType, Object> resultMap = new HashMap<>();
        List<String> xAxis_data = this.getDateListStr(dateType, startDate, endDate);
        List<MesProduction> productionList = Lists.newArrayList();
        List<String> producntNumList = this.getProductNum(productionList);
        List<String> legend_data = this.getProductName(producntNumList);
        resultMap.put(BarKeyType.legendData, legend_data); // legend中的data
        resultMap.put(BarKeyType.xAxisOfData, xAxis_data); // xAxis中的data
        resultMap.put(BarKeyType.seriesItemList, this.getSeries(startDate, endDate, dateType, modelnum, productBatchid)); // xAxis中的data
        Map<String, Object> percentMap = Maps.newHashMap();
        percentMap = this.getSeriesByTimePercent(startDate, endDate, dateType, modelnum, productBatchid);
        resultMap.put(BarKeyType.okPercentByEveryTime, percentMap.get("okPercentList")); // xAxis中的data

        // 产品合格率对比图用数据
        resultMap.put(BarKeyType.okPercentMaxList, percentMap.get("okPercentMaxList"));
        resultMap.put(BarKeyType.percentMaxList, percentMap.get("percentMaxList"));

        resultMap.put(BarKeyType.seriesPercentList, this.getPercentOfProduct((List<String>)resultMap.get(BarKeyType.seriesItemList))); // xAxis中的data
        Map<String, String> rsMap = getOKNGpercent(startDate, endDate, dateType, modelnum, productBatchid);
        if(null != rsMap) {
            resultMap.put(BarKeyType.okPercent, rsMap.get("okPercent"));
            resultMap.put(BarKeyType.ngPercent, rsMap.get("ngPercent"));

            resultMap.put(BarKeyType.okDisplay, rsMap.get("okDisplay"));
            resultMap.put(BarKeyType.totalDisplay,  rsMap.get("totalDisplay"));

            Map<String, Object> batchMap = this.generateProbatchids(Long.valueOf(modelnum), String.valueOf(startDate.getTime()).replace("000", ""),
                    String.valueOf(endDate.getTime()).replace("000", ""), rsMap.get("totalDisplay")); 
            if(null != batchMap && 0 < batchMap.size()) {
                resultMap.put(BarKeyType.batchIdXvalues, batchMap.get("rsList"));
                resultMap.put(BarKeyType.batchIdYvalues, batchMap.get("batchidsValueDisplay"));
                resultMap.put(BarKeyType.seriesDataComMaxForBatch, batchMap.get("seriesDataComMaxForBatch"));
                resultMap.put(BarKeyType.batchXvalueLis, batchMap.get("batchXvalueLis"));
            }
        }

        return resultMap;
    }
    
    /**
     * 根据分段时间获取不同时间段的产量数据
     * @param startDate
     * @param endDate
     * @param dateType
     * @param modelnum
     * @return
     */
    private List<String> getSeries(Timestamp startDate, Timestamp endDate,
            DateType dateType, String modelnum,
            String productBatchid) {
        // 拆分时间段
        List<Timestamp> times = this.getDateList(dateType, startDate, endDate);
        MesDataRowkeyUtil.setEntityManager(entityManager);

        List<String> rsCount = Lists.newArrayList();
        String startTi = "";
        String endTi = "";
        for(int i =0;i<times.size() - 1;i++) {
            startTi = String.valueOf(times.get(i).getTime()).substring(0, 10);
            endTi = String.valueOf(times.get(i+1).getTime()).substring(0, 10);
            List<String> totalCount = MesDataRowkeyUtil.getCountByPageSelect(
                    "",
                    "",
                    "",
                    modelnum,
                    "",
                    startTi,
                    endTi,
                    "",productBatchid);
            if(null == totalCount || 0 == totalCount.size()) {
                rsCount.add("0");
            } else {
                int tot = totalCount.size();
                rsCount.add(String.valueOf(totalCount.size()));
            }

        }
        return rsCount;

    }

    /**
     * 根据分段时间获取不同时间段的产量数据
     * @param startDate
     * @param endDate
     * @param dateType
     * @param modelnum
     * @return
     */
    private Map<String, String> getOKNGpercent(Timestamp startDate, Timestamp endDate, DateType dateType, String modelnum,
            String productBatchid) {
        String statrI = String.valueOf(startDate.getTime()).substring(0, 10);
        String endT = String.valueOf(endDate.getTime()).substring(0, 10);
        // 拆分时间段
        MesDataRowkeyUtil.setEntityManager(entityManager);
        List<String> totalCount =MesDataRowkeyUtil.getCountByPageSelect(
                "",
                "",
                "",
                modelnum,
                "",
                statrI,
                endT,
                "",
                productBatchid);
        if(null == totalCount || 0 == totalCount.size())
            return null;
        List<String> okCount =MesDataRowkeyUtil.getCountByPageSelect(
                "",
                "",
                "",
                modelnum,
                "",
                statrI,
                endT,
                "OK",
                productBatchid);
        List<String> ngCount =MesDataRowkeyUtil.getCountByPageSelect(
                "",
                "",
                "",
                modelnum,
                "",
                statrI,
                endT,
                "NG",
                productBatchid);
        Map<String, String> rsMap = Maps.newHashMap();
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        rsMap.put("ngPercent", numberFormat.format((float) ngCount.size() / (float) totalCount.size() * 100));
        rsMap.put("okPercent", numberFormat.format((float) okCount.size() / (float) totalCount.size() * 100));

        rsMap.put("okDisplay", String.valueOf(okCount.size()));
        rsMap.put("totalDisplay", String.valueOf(totalCount.size()));
        return rsMap;

    }

    /**
     * 获取百分比
     * @param countList
     * @return
     */
    private List<String> getPercentOfProduct(List<String> countList) {
        List<String> percetList = Lists.newArrayList();
        int total = 0;
        for(String count : countList) {
            total = Integer.valueOf(count) + total;
        }
        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        String result = "";
        for(String count : countList) {
            // 设置精确到小数点后2位
            numberFormat.setMaximumFractionDigits(2);
            if(0 != total) {
                result = numberFormat.format((float) Integer.valueOf(count) / (float) total * 100);
            } else {
                result = "0";
            }

            percetList.add(result);
        }
        return percetList;
    }

    public Map<BarKeyType, Object> getProductionData1(Timestamp startDate, Timestamp endDate, TypeScope typeScope,
            String modelnum, Long id, DateType dateType) {
        if (id == null || dateType == null || dateType.equals(""))
            return null;
        // 当为自定义时间时,如果开始时间或者结束时间为空则返回null;
        if (dateType.equals(DateType.defineDate) && (startDate == null || endDate == null))
            return null;
        // 开始时间不能大于结束时间
        if (startDate.getTime() > endDate.getTime())
            return null;
        // 返回结果集
        Map<BarKeyType, Object> resultMap = new HashMap<>();
        // xAxis中data的时间段间隔集合
        List<String> xAxis_data = this.getDateListStr(dateType, startDate, endDate);
        // series的list集合数据,以产品号种类来分
        List<SeriesItem> series = null;
        // 拆分时间段
        List<Timestamp> times = this.getDateList(dateType, startDate, endDate);
        // legend中的data集合list,即产品名称集合
        List<String> legend_data = null;// this.getDateListStr(dateType, startDate, endDate);
        // 根据时间查询出各种产品时间段的合格,不合格量
        List<MesProduction> proList = null;

        // TODO:以公司和产线为单位的统计计算先不做了，用的时候再做 -----start
        // if(times != null)
        // proList = this.getProductionList(modelnum, times.get(0),
        // times.get(times.size()-1));
        // TODO:以公司和产线为单位的统计计算先不做了，用的时候再做 -----end

        Long count = 0l;// 总产量
        Long passCount = 0l; // 合格数
        if (TypeScope.productline.equals(typeScope)) {
            // 当统计范围是产线时
            List<MesProductline> lines = new ArrayList<>();
            lines.add(new MesProductline(id));
            // 合格数,不合格数的map
            Map<PassType, Object> countMap = this.getCountMap(lines, proList);
            count = (Long) countMap.get(PassType.PASSCOUNT) + (Long) countMap.get(PassType.FAILCOUNT);
            passCount = (long) countMap.get(PassType.PASSCOUNT);
            // 属于该产线的product的集合
            List<MesProduction> productionList = (List<MesProduction>) countMap.get(PassType.matchProductList);
            // 查询出来的产量集合中有多少种产品号(productNum)
            List<String> producntNumList = this.getProductNum(productionList);
            legend_data = this.getProductName(producntNumList);
            // 计算series
            series = this.getSeriesForBarChart(producntNumList, times, legend_data, productionList);

        } else if (TypeScope.factory.equals(typeScope)) {
            // 当统计范围是工厂时
            // 查询出该工厂下所有的产线
            Companyinfo factorys = cpDao.findOne(id);
            List<MesProductline> lines = this.getProductlineOfFactory(factorys);
            // 合格数,不合格数的map;从而计算出总产量,及合格产量数
            Map<PassType, Object> countMap = this.getCountMap(lines, proList);
            count = (Long) countMap.get(PassType.PASSCOUNT) + (Long) countMap.get(PassType.FAILCOUNT);
            passCount = (long) countMap.get(PassType.PASSCOUNT);
            // 查询出和产线相关的所有产品集合
            List<MesProduction> productionList = (List<MesProduction>) countMap.get(PassType.matchProductList);
            // 查询出产量集合中有多少中产品号
            List<String> producntNumList = this.getProductNum(productionList);
            legend_data = this.getProductName(producntNumList);
            // 计算series
            series = this.getSeriesForBarChart(producntNumList, times, legend_data, productionList);

        } else if (TypeScope.company.equals(typeScope)) {
            // 当统计范围是公司时
            // 查询公司下一共多少工厂
            List<Companyinfo> factorys = cpDao.findByParentid(id);
            List<MesProductline> lines = this.getProductlineOfFactory(factorys);
            // List<Result> productList = Lists.newArrayList();
            // for(MesProductline mesline : lines) {
            // productList.addAll(new
            // HbaseUtil().getResultListByHbase(mesline.getCompanyinfo().getId(),
            // mesline.getId(), null, null, null, null, startDate, endDate, false));
            // }
            Map<String, Object> rsCountMap = statisticsService.getPassNoPassCountByMysql(id, null, null,
                    // Map<String, Object> rsCountMap =
                    // statisticsService.getPassNoPassCountByHbase(id, null, null,
                    (StringUtils.isBlank(modelnum) ? null : Long.valueOf(modelnum)), null, startDate, endDate);
            // Map<String, Object> totalCountMap = (Map<String, Object>)
            // rsCountMap.get("totalCount");
            count = (long) rsCountMap.get("totalCount");
            // passCount = Long.valueOf(String.valueOf(totalCountMap.size() -
            // (Long)rsCountMap.get("nopassCount")));
            passCount = (long) rsCountMap.get("nopassCount");
            List<MesProduction> productionList = Lists.newArrayList();// getMesProductionList(productList);
            MesProduction oneMoldel = new MesProduction();
            oneMoldel.setProductnum(modelnum);
            oneMoldel.setTotalCount(count);
            productionList.add(oneMoldel);
            /*
             * //当统计范围是公司时 //查询公司下一共多少工厂 List<Companyinfo> factorys =
             * cpDao.findByParentid(id); List<MesProductline> lines =
             * this.getProductlineOfFactory(factorys); //合格数,不合格数的map;从而计算出总产量,及合格产量数
             * Map<PassType,Object> countMap = this.getCountMap(lines, proList); count =
             * (Long)countMap.get(PassType.PASSCOUNT) +
             * (Long)countMap.get(PassType.FAILCOUNT); passCount =
             * (long)countMap.get(PassType.PASSCOUNT); //查询出和产线相关的所有产品集合 List<MesProduction>
             * productionList = (List<MesProduction>)
             * countMap.get(PassType.matchProductList);
             */
            // 查询出产量集合中有多少中产品号
            List<String> producntNumList = this.getProductNum(productionList);
            legend_data = this.getProductName(producntNumList);
            // 计算series
            series = this.getSeriesForBarChart(producntNumList, times, legend_data, productionList);

        }
        resultMap.put(BarKeyType.count, count);
        resultMap.put(BarKeyType.passCount, passCount);
        resultMap.put(BarKeyType.legendData, legend_data); // legend中的data
        resultMap.put(BarKeyType.xAxisOfData, xAxis_data); // xAxis中的data
        resultMap.put(BarKeyType.seriesItemList, series); // series
        return resultMap;
    }

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
    public List<String> getDateListStr(DateType dateType, Timestamp startDate, Timestamp endDate) {
        List<String> datesStr = null;
        if (DateType.day.equals(dateType)) {
            // 当天,分24小时段
            datesStr = this.get24HoursStr();
        } else if (DateType.week.equals(dateType)) {
            // 一星期,当天向前推7天,分7段时间
//            datesStr = this.get7DayStr();
            datesStr = this.get7DayStr2(startDate);
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
     * 把时间段拆分,返回字符串集合
     * 查处对应
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
    private List<String> get24HoursStr() {
        List<String> timeStrs = new ArrayList<>();
        List<Timestamp> times = this.getDay24Hours();
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
    private List<Timestamp> getDay24Hours() {
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
    private List<String> get7DayStr2(Timestamp startdate) {
        List<String> sivenDay = new ArrayList<>();
        List<Timestamp> times = this.get7Day2(startdate);
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
    private List<Timestamp> get7Day2(Timestamp startdate) {
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
        //Collections.reverse(times);
        return times;
    }

    /**
     * 获取当月天数时间日的字符串集合,例如: 12日,13日...
     * 
     * @return
     */
    private List<String> getMonthDayStr() {
        List<String> dayStr = new ArrayList<>();
        List<Timestamp> dayTimes = this.getMonthDay();
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
    private List<Timestamp> getMonthDay() {
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
    private List<String> getDefineTimeStr(Timestamp start, Timestamp end) {
        if ((end.getTime() - start.getTime()) <= 86400000L) {
            // 小于一天
            return this.getDefineTimeForHoursStr(start, end);
        } else if ((end.getTime() - start.getTime()) > 86400000L && (end.getTime() - start.getTime()) <= 2592000000L) {
            // 大于一天,小于等于31天
            return this.getDefineTimeForDayStr(start, end);
        } else if ((end.getTime() - start.getTime()) > 2592000000L) {
            // 大于31天
            return this.getDefineTimeForMonthStr(start, end);
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
    private List<String> getDefineTimeForHoursStr(Timestamp start, Timestamp end) {
        List<String> hoursStr = new ArrayList<>();
        List<Timestamp> times = this.getDefineTimeForHours(start, end);
        Calendar cd = Calendar.getInstance();
        for (Timestamp time : times) {
            cd.setTime(new Date(time.getTime()));
            hoursStr.add(cd.get(Calendar.DAY_OF_MONTH) + "日" + cd.get(Calendar.HOUR_OF_DAY) + "时");
        }
        return hoursStr;
    }

    /**
     * 以天为标准拆分,返回字符串集合
     * 
     * @param start
     * @param end
     * @return
     */
    private List<String> getDefineTimeForDayStr(Timestamp start, Timestamp end) {
        List<String> daysStr = new ArrayList<>();
        List<Timestamp> times = this.getDefineTimeForDay(start, end);
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
    private List<Timestamp> getDefineTimeForDay(Timestamp start, Timestamp end) {
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
     * 统计出有多少种产品号
     * 
     * @param productions
     *            被统计对象
     * @return
     */
    private List<String> getProductNum(List<MesProduction> productions) {
        List<String> distinctNameList = new ArrayList<>();
        if (productions == null || productions.size() <= 0)
            return distinctNameList;
        // 先把第一条添加进去
        distinctNameList.add(productions.get(0).getProductnum());
        Boolean isMatch;
        for (MesProduction production : productions) {
            isMatch = false;
            for (String productnum : distinctNameList) {
                if (productnum.equals(production.getProductnum())) {
                    isMatch = false;
                    break;
                } else {
                    isMatch = true;
                }
            }
            if (isMatch)
                distinctNameList.add(production.getProductnum());
        }
        return distinctNameList;
    }

    /**
     * 根据productNum(即与表mes_product中字段modelnum对应)来查询出 产品名称,参数需要加入公司限制;
     * 
     * @param productNums
     * @return
     */
    private List<String> getProductName(List<String> productNums) {
        if (productNums == null || productNums.size() <= 0)
            return null;
        List<String> productNames = new ArrayList<>();
        Long companyId = SecurityUtils.getShiroUser().getCompanyid();
        companyId = (null == companyId || 0L == companyId)? 511L : companyId; 
        for (String productNum : productNums) {
            MesProduct product = productDao.findByModelnumAndCompanyinfoId(productNum, companyId);
            if (product != null)
                productNames.add(product.getName());
        }
        return productNames;
    }

    /**
     * 循环产品号种类,统计该产品在不同时间段的产量; 目的是封装series中的数据;
     * 
     * @param producntNumList
     *            产品类型号集合
     * @param times
     *            时间段集合
     * @param legend_data
     *            产品类名名称集合
     * @param productionList
     * @return
     */
    private List<SeriesItem> getSeriesForBarChart(List<String> producntNumList, List<Timestamp> times,
            List<String> legend_data, List<MesProduction> productionList) {
        /*
         * 根据items的时间段来统计产量集合productionList不同时间段的产量有多少;
         */
        List<SeriesItem> series = new ArrayList<>();
        List<Double> series_data = null;
        BarChart.SeriesItem seriesItem = null;
        for (int i = 0; producntNumList != null && i < producntNumList.size(); i++) {
            String productNum = producntNumList.get(i);
            series_data = new ArrayList<>();
            // series集合中的一个实体元素
            seriesItem = new BarChart().new SeriesItem();
            // 产品名称
            seriesItem.setName(legend_data.get(i));
            // 时间循环
            for (int j = 0; times != null && j < times.size() - 1; j++) {
                // 一个时间段的统计总产量
                Double productNumCount = 0d;
                for (MesProduction production : productionList) {
                    // 是同一类产品
                    if (productNum.equals(production.getProductnum())) {
                        // 在该时间段内
                        productNumCount += production.getTotalCount();
                        // if(production.getUpdatetime().getTime() >= times.get(j).getTime()
                        // && production.getUpdatetime().getTime() <= times.get(j+1).getTime())
                        // productNumCount += production.getTotalCount();
                    }
                }
                // 一个时间段循环完成,需要把该时间段的统计数据添加到series中的data列表中
                series_data.add(productNumCount);
            }
            seriesItem.setData(series_data);
            // 把一类产品的数据元素放入到series集合中
            series.add(seriesItem);
        }
        return series;
    }

    /**
     * 获取一个工厂下的产线列表
     * 
     * @param factory
     * @return
     */
    public List<MesProductline> getProductlineOfFactory(Companyinfo factory) {
        List<Companyinfo> factorys = null;
        if (factory != null) {
            factorys = new ArrayList<>();
            factorys.add(factory);
        }
        return this.getProductlineOfFactory(factorys);
    }

    /**
     * 获取工厂列表下的产下列表
     * 
     * @return
     */
    public List<MesProductline> getProductlineOfFactory(List<Companyinfo> factorys) {
        List<MesProductline> lines = null;
        if (factorys != null && factorys.size() > 0) {
            lines = new ArrayList<>();
            for (Companyinfo factory : factorys) {
                lines.addAll(factory.getMesProductlines());
            }
        }
        return lines;
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
    public List<Timestamp> getDateList(DateType dateType, Timestamp startDate, Timestamp endDate) {
        List<Timestamp> times = null;
        if (DateType.day.equals(dateType)) {
            // 当天,分24小时段
            times = this.getDay24Hours();
        } else if (DateType.week.equals(dateType)) {
            // 一星期,当天向前推7天,分7段时间
            times = this.get7Day2(startDate);
            // times = this.get7Day();
        } else if (DateType.month.equals(dateType)) {
            // 当月,分当月的天数段
            times = this.getMonthDay();
        } else if (DateType.defineDate.equals(dateType)) {
            // 自定义时间:
            times = this.getDefineTime(startDate, endDate);
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
    public List<Timestamp> getDateListMake(DateType dateType, Timestamp startDate, Timestamp endDate) {
        List<Timestamp> times = null;
        if (DateType.day.equals(dateType)) {
            // 当天,分24小时段
            times = this.getDay24Hours();
        } else if (DateType.week.equals(dateType)) {
            // 一星期,当天向前推7天,分7段时间
            times = this.get7Day2(startDate);
            // times = this.get7Day();
        } else if (DateType.month.equals(dateType)) {
            // 当月,分当月的天数段
            times = this.getMonthDay();
        } else if (DateType.defineDate.equals(dateType)) {
            // 自定义时间:
            times = this.getDefineTime(startDate, endDate);
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
    @SuppressWarnings("unused")
    private List<Timestamp> getDefineTime(Timestamp start, Timestamp end) {
        /*
         * 1.当日期在一天以内的,以小时类分时间段; 当跨日时,需要注意日期; 2.当时间段差超过一天,且小于30天时,以开始时间与结束时间差的天数为分割时间段;
         * 3.当时间段差超过30天时,以月份段来划分,以跨度的月份来划分; 例如: 当跨度了3个月时,就以跨度的月份段来分;
         */
        if (start == null || end == null)
            return null;
        if ((end.getTime() - start.getTime()) <= 86400000L) {
            // 小于一天
            return this.getDefineTimeForHours(start, end);
        } else if ((end.getTime() - start.getTime()) > 86400000L && (end.getTime() - start.getTime()) <= 2592000000L) {
            // 大于一天,小于等于31天
            return this.getDefineTimeForDay(start, end);
        } else if ((end.getTime() - start.getTime()) > 2592000000L) {
            // 大于31天
            return this.getDefineTimeForMonth(start, end);
        }
        // 都不符合直接返回null
        return null;
    }

    /**
     * 根据开始,结束时间拆分为以小时的时间段
     * 
     * @param start
     * @param end
     * @return
     */
    private List<Timestamp> getDefineTimeForHours(Timestamp start, Timestamp end) {
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
     * 拆分月份段集合
     * 
     * @param start
     * @param end
     * @return
     */
    private List<Timestamp> getDefineTimeForMonth(Timestamp start, Timestamp end) {
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
     * 以月为时间段区分,返回字符串集合
     * 
     * @param start
     * @param end
     * @return
     */
    private List<String> getDefineTimeForMonthStr(Timestamp start, Timestamp end) {
        List<String> months = new ArrayList<>();
        List<Timestamp> times = this.getDefineTimeForMonth(start, end);
        Calendar cd = Calendar.getInstance();
        for (Timestamp time : times) {
            cd.setTime(new Date(time.getTime()));
            months.add((cd.get(Calendar.MONTH) + 1) + "月");
        }
        return months;
    }

    /**
     * 产量表中的driverId中的productline的id与产线的id对比, 如果相等则累加
     * 
     * @param lines
     * @param proList
     * @return
     */
    private Map<PassType, Object> getCountMap(List<MesProductline> lines, List<MesProduction> proList) {
        Map<PassType, Object> resultMap = new HashMap<>();
        List<MesProduction> matchProList = new ArrayList<>();
        // 合格数
        Long passCount = 0L;
        // 不合格数
        Long failCount = 0L;
        for (MesProduction production : proList) {
            for (MesProductline line : lines) {
                // 需要判断MesDriver与MesProductline是否为空,因为这个是大数据写入的数据,可能存在错误情况
                if (production.getMesDriver() != null && production.getMesDriver().getMesProductline() != null)
                    if (production.getMesDriver().getMesProductline().getId().compareTo(line.getId()) == 0) {
                        passCount += production.getPassCount();
                        failCount += production.getFailureCount();
                        matchProList.add(production);
                        break;
                    }
            }
        }
        resultMap.put(PassType.PASSCOUNT, passCount);
        resultMap.put(PassType.FAILCOUNT, failCount);
        resultMap.put(PassType.matchProductList, matchProList);
        return resultMap;
    }

    /**
     * 设备分析接口
     * @param energyType
     * @param typeScope
     * @param id
     * @param startDate
     * @param endDate
     * @param driverid
     * @param searchKind
     * @return
     */
    public Map<String, Object> getProductionDataForDriver(
            String energyType,
            TypeScope typeScope,
            Long id,
            Timestamp startDate,
            Timestamp endDate,
            Long driverid,
            int searchKind,
            DateType dateType,
            Long chooseFactoryId,
            Long chooseProductLineId,
            Timestamp anlysisDate) {
        // 返回值
        Map<String, Object> resultMap = Maps.newHashMap();
        // 单设备搜索
        if (0 == searchKind) {
            Map<String, Object> serviceData;
            if("runtime".equals(energyType)) {
                serviceData = this.getSeriesForDriverRuntimeStatus(energyType, startDate, endDate,
                        dateType, driverid, chooseFactoryId, chooseProductLineId, anlysisDate);

            } else {
                serviceData = this.getSeriesForDriver(energyType, startDate, endDate, dateType, driverid
                        , chooseFactoryId, chooseProductLineId);
            }

            System.out.println(serviceData);
            resultMap.put("serviceData", serviceData);
//            resultMap.put("xAxisData", this.getDateListStr(dateType, startDate, endDate));

            MesDriver mesDriver = mesDriverService.findById(driverid);
            List<String> driverNm = Lists.newArrayList();
            if (null != mesDriver) {
                driverNm.add(mesDriver.getName());
                resultMap.put("driverNm", driverNm);
                resultMap.put("driverSn", mesDriver.getSn());
            } else {
                resultMap.put("driverNm", "");
                resultMap.put("driverSn", "");
            }

        } else {
            List<MesDriver> driverList = new ArrayList<>();
            // 多设备分析
            if (TypeScope.productline.equals(typeScope)) {
                // 当统计范围是产线时
                List<MesProductline> lines = new ArrayList<>();
                lines.add(new MesProductline(id));
                driverList = productChartServ.getDriverListForProductLineList(lines);
            } else if (TypeScope.factory.equals(typeScope)) {
                // 当统计范围是工厂时
                // 查询出该工厂下所有的产线
                Companyinfo factorys = cpDao.findOne(id);
                List<MesProductline> lines = productChartServ.getProductlineOfFactory(factorys);
                driverList = productChartServ.getDriverListForProductLineList(lines);
            } else if (TypeScope.company.equals(typeScope)) {
                // 当统计范围是公司时
                // 查询公司下一共多少工厂
                id = SecurityUtils.getShiroUser().getCompanyid();
                List<Companyinfo> factorys = cpDao.findByParentid(id);
                List<MesProductline> lines = productChartServ.getProductlineOfFactory(factorys);
                driverList = productChartServ.getDriverListForProductLineList(lines);
            }
            if (driverList.size() < 1) {
                return null;
            }
            List<Long> driverIds = new ArrayList<Long>();
            for (MesDriver md : driverList) {
                driverIds.add(md.getId());
            }
            String searchEnergyType = "";
            if ("electric".equals(energyType)) {
                searchEnergyType = "ELECTRIC";
            } else if ("gas".equals(energyType)) {
                searchEnergyType = "GAS";
            } else if ("water".equals(energyType)) {
                searchEnergyType = "WATER";
            }
            List<String> driverMetatValList = MesDataRowkeyUtil.getDriverCount(driverIds, searchEnergyType,
                    String.valueOf(startDate.getTime()).replace("000", ""),
                    String.valueOf(endDate.getTime()).replace("000", ""));
//            resultMap.put("driverMetatValList", driverMetatValList);
            resultMap.put("serviceData", driverMetatValList);

            List<String> dirverNmList = Lists.newArrayList();
            MesDriver mesDriver = null;
            for (Long driverId : driverIds) {
                mesDriver = mesDriverService.findById(driverId);
                if (null != mesDriver)
                    dirverNmList.add(mesDriver.getName());
            }
            // 设备名称列表
//            resultMap.put("dirverNmList", dirverNmList);
            resultMap.put("xAxisData", dirverNmList);
            if(null == driverMetatValList || 0 == driverMetatValList.size()) {
                List<String> zeroList = Lists.newArrayList();
                for(String driverNm : dirverNmList) {
                    zeroList.add("0");
                }
                resultMap.put("serviceData", zeroList);
            }
        }

        return resultMap;
    }

    /**
     * 根据分段时间获取不同时间段的能耗数据
     * @param startDate
     * @param endDate
     * @param dateType
     * @param modelnum
     * @return
     */
    private Map<String, Object> getSeriesForDriver(String energyType,
            Timestamp startDate, Timestamp endDate, DateType dateType,
            Long driverid,
            Long chooseFactoryId,
            Long chooseProductLineId) {
        Map<String, Object> rsMap = Maps.newHashMap();

        // 拆分时间段
        List<Timestamp> times = this.getDateList(dateType, startDate, endDate);
//        List<Timestamp> times = DateUtils.getDateList(dateType, startDate, endDate);
        MesDataRowkeyUtil.setEntityManager(entityManager);

        List<Float> rsCountFloat = Lists.newArrayList();
        List<String> rsCount = Lists.newArrayList();
        String startTi = "";
        String endTi = "";
        String searchEnergyType = "";
        if ("electric".equals(energyType)) {
            searchEnergyType = "ELECTRIC";
        } else if ("gas".equals(energyType)) {
            searchEnergyType = "GAS";
        } else if ("water".equals(energyType)) {
            searchEnergyType = "WATER";
        }
        Map<String, Float> timeCountMap = Maps.newLinkedHashMap();
        List<SearchFilter> searchList = null;
        Float beforeSetVal = 0f;
        if(0f == beforeSetVal) {
            startTi = String.valueOf(DateUtils.getPreTime(times.get(0),  dateType).getTime()).substring(0, 10);
            endTi = String.valueOf(times.get(0).getTime()).substring(0, 10);
            searchList = Lists.newArrayList();
            searchList.add(new SearchFilter("mesDataMultiKey.factoryId", Operator.EQ, null == chooseFactoryId ? 0L :String.valueOf(chooseFactoryId)));
            searchList.add(new SearchFilter("mesDataMultiKey.productLineId", Operator.EQ, null == chooseProductLineId ? 0L :String.valueOf(chooseProductLineId)));
            searchList.add(new SearchFilter("mesDataMultiKey.driverId", Operator.EQ, null == chooseProductLineId ? 0L : String.valueOf(driverid)));
            searchList.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.GTE, startTi));
            searchList.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.LTE, endTi));
            searchList.add(new SearchFilter("type", Operator.EQ, searchEnergyType));
            Specification<MesDataWeg> specification = DynamicSpecifications.bySearchFilter(MesDataWeg.class,
                    searchList);
            Page page = new Page();
            page.setOrderField("mesDataMultiKey.insertTimestamp");
            page.setOrderDirection(Page.ORDER_DIRECTION_DESC);
            page.setNumPerPage(1);
            List<MesDataWeg> mesDataWegList = mesDataWegService.findPage(specification, page);
            if(null != mesDataWegList && 0< mesDataWegList.size()) {
                MesDataWeg outObj = mesDataWegList.get(0);
                beforeSetVal = StringUtils.isNotEmpty(outObj.getMetaValue()) ? Float.valueOf(outObj.getMetaValue()) : 0f;
            }
        }


        for (int i = 0; i < times.size() - 1; i++) {
            startTi = String.valueOf(times.get(i).getTime()).substring(0, 10);
            endTi = String.valueOf(times.get(i + 1).getTime()).substring(0, 10);

            searchList = Lists.newArrayList();
            searchList.add(new SearchFilter("mesDataMultiKey.factoryId", Operator.EQ, null == chooseFactoryId ? 0L :String.valueOf(chooseFactoryId)));
            searchList.add(new SearchFilter("mesDataMultiKey.productLineId", Operator.EQ, null == chooseProductLineId ? 0L :String.valueOf(chooseProductLineId)));
            searchList.add(new SearchFilter("mesDataMultiKey.driverId", Operator.EQ, null == chooseProductLineId ? 0L : String.valueOf(driverid)));
            searchList.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.GTE, startTi));
            searchList.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.LTE, endTi));
            searchList.add(new SearchFilter("type", Operator.EQ, searchEnergyType));
            Specification<MesDataWeg> specification = DynamicSpecifications.bySearchFilter(MesDataWeg.class,
                    searchList);
            Page page = new Page();
            page.setOrderField("mesDataMultiKey.insertTimestamp");
            page.setOrderDirection(Page.ORDER_DIRECTION_DESC);
            page.setNumPerPage(1);
            List<MesDataWeg> mesDataWegList = mesDataWegService.findPage(specification, page);
            MesDataWeg outObj = null;
            if (null != mesDataWegList && 0 < mesDataWegList.size()) {
                outObj = mesDataWegList.get(0);

                Float setVal = StringUtils.isNotEmpty(outObj.getMetaValue()) ? Float.valueOf(outObj.getMetaValue()) : 0f;
                if(0f == beforeSetVal) {
                    rsCountFloat.add(setVal);
                    timeCountMap.put(String.valueOf(times.get(i)), setVal);
                    rsCount.add(String.valueOf(setVal));
                    beforeSetVal = setVal;
                } else {
                    Float minusVal = MesAnylsanysUtils.getMinusVal(beforeSetVal, setVal);
                    rsCountFloat.add(minusVal);
                    timeCountMap.put(String.valueOf(times.get(i)), minusVal);
                    rsCount.add(String.valueOf(minusVal));
                    beforeSetVal = setVal;
                }

            } else {
                rsCount.add("0");
                rsCountFloat.add(0f);
                timeCountMap.put(String.valueOf(times.get(i)), 0f);
                beforeSetVal = 0f;
            }

        }
        // 各个时段的能耗
        rsMap.put("timeCount", rsCount);
        // 各个时段
        rsMap.put("time", times);
        // 各个时段的chinese
        rsMap.put("timeChinese", getDateListStr(dateType, startDate, endDate));

        // 能耗总量
        Float sumValue = 0f;
        for(Float val : rsCountFloat) {
            sumValue += val;
        }
        BigDecimal subValueBig = BigDecimal.valueOf(sumValue);
        rsMap.put("sumVaue", sumValue);
        // top
        Map<String, Object> topInfoMap = getTopEngryValueList(subValueBig, timeCountMap, 5, dateType);
        rsMap.put("topCount", topInfoMap.get("topCountList"));
        rsMap.put("barLegendVal", topInfoMap.get("barLegendVal"));

        return rsMap;

    }

    private static String beforeStausVal = "";
    private static String beforeTimeVal = "";
    /**
     * 根据画面所选日期，
     * @param startDate
     * @param endDate
     * @param dateType
     * @param modelnum
     * @return
     */
    @SuppressWarnings("unused")
    private Map<String, Object> getSeriesForDriverRuntimeStatus(String energyType,
            Timestamp startDate, Timestamp endDate, DateType dateType,
            Long driverid,
            Long chooseFactoryId,
            Long chooseProductLineId,
            Timestamp anlysisDate) {
        Map<String, Object> rsMap = Maps.newHashMap();

        List<SearchFilter> searchList = Lists.newArrayList();
        searchList.add(new SearchFilter("mesDataMultiKey.factoryId", Operator.EQ, null == chooseFactoryId ? 0L :String.valueOf(chooseFactoryId)));
        searchList.add(new SearchFilter("mesDataMultiKey.productLineId", Operator.EQ, null == chooseProductLineId ? 0L :String.valueOf(chooseProductLineId)));
        searchList.add(new SearchFilter("mesDataMultiKey.driverId", Operator.EQ, null == chooseProductLineId ? 0L : String.valueOf(driverid)));
        Map<String, Timestamp> timestampList = DateUtils.getEndTimestamp(anlysisDate);
        Timestamp startTime = timestampList.get("start");
        Timestamp endTime = timestampList.get("end");
        searchList.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.GTE, String.valueOf(startTime.getTime()).substring(0, 10)));
        searchList.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.LTE, String.valueOf(endTime.getTime()).substring(0, 10)));
        Specification<MesDataDriverStatus> specification = DynamicSpecifications.bySearchFilter(MesDataDriverStatus.class,
                searchList);
//        Page page = new Page();
//        page.setOrderField("mesDataMultiKey.insertTimestamp");
//        List<MesDataDriverStatus> driverStatusLs = mesDataDriverStatusService.findPage(specification, page);
        Sort sortParam = new Sort(Sort.Direction.ASC, "mesDataMultiKey.insertTimestamp");
        List<MesDataDriverStatus> driverStatusLs = mesDataDriverStatusService.findAll(specification, sortParam);
        // 数据格式样例
        // {name: '运行',value: [1, 1525835791000, 1525835791000 + 600000, 600000],itemStyle:{normal: {color: 'green'}}},
        List<Object> statusChangetimeList = Lists.newArrayList();
        if (null != driverStatusLs && 0 < driverStatusLs.size()) {
            driverStatusLs.forEach(obj -> {
                if (!"".equals(beforeStausVal)) {
                    String status = obj.getDriverStatus();
                    if (!beforeStausVal.equals(status)) {
                        Map<String, String> statusMap = Maps.newHashMap();
                        statusMap.put("status", beforeStausVal);
                        BigInteger beforeTime = BigInteger.valueOf(Long.valueOf(beforeTimeVal));
                        BigInteger nowTime = BigInteger
                                .valueOf(obj.getMesDataMultiKey().getInsertTimestamp().longValue());
                        statusMap.put("time", beforeTimeVal);
                        statusMap.put("endtime", String.valueOf(nowTime));
                        statusMap.put("minusTime", String.valueOf(nowTime.subtract(beforeTime)));
                        if("0".equals(statusMap.get("minusTime")))
                            statusMap.put("minusTime", "0");
                        if(Integer.valueOf(statusMap.get("minusTime")) < 0)
                            statusMap.put("minusTime", "0");
                        statusChangetimeList.add(statusMap);
                        beforeStausVal = status;
                        beforeTimeVal = String.valueOf(obj.getMesDataMultiKey().getInsertTimestamp());

                    }

                } else {
                    beforeStausVal = obj.getDriverStatus();
                    beforeTimeVal = String.valueOf(obj.getMesDataMultiKey().getInsertTimestamp());

                }

            });

        }
        List<BigInteger> stopByTotalList = Lists.newArrayList();
        List<BigInteger> standByTotalList = Lists.newArrayList();
        List<BigInteger> runByTotalList = Lists.newArrayList();
        // 数据格式样例
        // {name: '运行',value: [1, 1525835791000, 1525835791000 + 600000, 600000],itemStyle:{normal: {color: 'green'}}},
        List<Object> outList = Lists.newArrayList();
        if(null != statusChangetimeList && 0 < statusChangetimeList.size()) {
//            BigInteger standByTotalTime = BigInteger.valueOf(0L);
//            BigInteger standByTotalTime1 = BigInteger.valueOf(0L);
//            BigInteger stopTotalTime = BigInteger.valueOf(0L);
//            BigInteger runTotalTime = BigInteger.valueOf(0L);


            statusChangetimeList.forEach(obj->{
                Map<String, Object> makeMap = (Map)obj;
                Map<String, Object> paramMap = Maps.newHashMap();
                // String status = obj.getDriverStatus();
                String status = (String) makeMap.get("status");
                // 0 :运行， 1：停机 2：待机
                paramMap.put("name", MesAnylsanysUtils.getStatusValueByCode(status));

                List<Object> valels = Lists.newArrayList();
                valels.add(0);
                // 设备状态起始时间
                valels.add(BigInteger.valueOf(Long.valueOf(String.valueOf(makeMap.get("time")) + "000")));
                // 设备状态结束时间
                valels.add(BigInteger.valueOf(Long.valueOf(String.valueOf(makeMap.get("endtime")) + "000")));
                // 设备状态持续时间
                valels.add(BigInteger.valueOf(Long.valueOf(String.valueOf(makeMap.get("minusTime")) + "000")));
                paramMap.put("value", valels);

                ItemStyle itemStyle = new ItemStyle();
                itemStyle.normal().color(MesAnylsanysUtils.getStatusColorByCode(status));
                paramMap.put("itemStyle", itemStyle);
                if(!String.valueOf(makeMap.get("minusTime")).contains("-")) {
                    outList.add(paramMap);
                }

                // 各种时间的统计
                if("1".equals(status)) {
                    stopByTotalList.add(BigInteger.valueOf(Long.valueOf(String.valueOf(makeMap.get("minusTime")) + "000")));
                } else if("2".equals(status)) {
                    standByTotalList.add(BigInteger.valueOf(Long.valueOf(String.valueOf(makeMap.get("minusTime")) + "000")));
                } else {
                    runByTotalList.add(BigInteger.valueOf(Long.valueOf(String.valueOf(makeMap.get("minusTime")) + "000")));
                }
            });

        }
        MesAnylsanysUtils.getKindOfTime(rsMap, stopByTotalList, standByTotalList, runByTotalList);
        // page.setOrderDirection(Page.ORDER_DIRECTION_DESC);
        rsMap.put("driverRuntimeList", outList);

        // 设备非正常状态时间统计
        List<String> stopTimeEchartList = Lists.newArrayList();
        List<String> standbyTimeEchartList = Lists.newArrayList();

        List<Timestamp> times = this.getDateList(DateType.defineDate, startTime, endTime);
        String startTi = "";
        String endTi = "";
        for(int i =0;i<times.size() - 1;i++) {
            startTi = String.valueOf(times.get(i).getTime()).substring(0, 10);
            endTi = String.valueOf(times.get(i+1).getTime()).substring(0, 10);

            searchList = Lists.newArrayList();
            searchList.add(new SearchFilter("mesDataMultiKey.factoryId", Operator.EQ, null == chooseFactoryId ? 0L :String.valueOf(chooseFactoryId)));
            searchList.add(new SearchFilter("mesDataMultiKey.productLineId", Operator.EQ, null == chooseProductLineId ? 0L :String.valueOf(chooseProductLineId)));
            searchList.add(new SearchFilter("mesDataMultiKey.driverId", Operator.EQ, null == chooseProductLineId ? 0L : String.valueOf(driverid)));
            searchList.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.GTE, startTi));
            searchList.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.LTE, endTi));
            specification = DynamicSpecifications.bySearchFilter(MesDataDriverStatus.class, searchList);
            driverStatusLs = mesDataDriverStatusService.findAll(specification, sortParam);
            List<BigInteger> stopList = Lists.newArrayList();
            List<BigInteger> standbyList = Lists.newArrayList();
            driverStatusLs.forEach(obj -> {
                String driverStatus = obj.getDriverStatus();
                // 0 :运行  1：停机 2：待机
                if ("1".equals(driverStatus)) {
                    stopList.add(obj.getMesDataMultiKey().getInsertTimestamp());
                } else if ("2".equals(driverStatus)) {
                    standbyList.add(obj.getMesDataMultiKey().getInsertTimestamp());
                }
            });

            // 取得list的首位，进行该时间段的持续时间计算
            stopTimeEchartList.add(MesAnylsanysUtils.getListStartEndTime(stopList));
            standbyTimeEchartList.add(MesAnylsanysUtils.getListStartEndTime(standbyList));
        }
        rsMap.put("stopTimeEchartList", stopTimeEchartList);
        rsMap.put("standbyTimeEchartList", standbyTimeEchartList);
        return rsMap;

    }


    /**
     * 获取指定top的能耗数据的百分比计算
     * @return
     */
    private Map<String, Object> getTopEngryValueList(BigDecimal subValueBig,
            Map<String, Float> timeCountMap,
            int topNum,
            DateType dateType) {
        Map<String, Object> rsOutMap = Maps.newHashMap();

        Map<String, Float> chineseMap = changTimeKeyToChineseKey(timeCountMap, dateType);
        // 1.8使用lambda表达式
        // 排序
//        List<Map.Entry<String, Float>> listSorted = Lists.newArrayList();
//        listSorted.addAll(chineseMap.entrySet());
//        Collections.sort(
//                listSorted, 
//                (o1, o2) -> (int)(o1.getValue() - o2.getValue())
//                );
        List<Map.Entry<String, Float>> listSorted = new LinkedList<>(chineseMap.entrySet());
        listSorted.sort((o1, o2)-> o2.getValue().compareTo(o1.getValue()));
        Map<String, Float> result = new LinkedHashMap<>();
        listSorted.stream().forEach(entry -> result.put(entry.getKey(), entry.getValue()));

        Map<String, Float> timeCountMapChanged = Maps.newLinkedHashMap();
        for(int index=0;index< listSorted.size();index++) {
            Float setVal = listSorted.get(index).getValue();
            String setKey = listSorted.get(index).getKey();
            timeCountMapChanged.put(setKey, setVal);
            if(index == 4)
                break;
        }

        List<Object> rsLs = Lists.newArrayList();

       //获取格式化对象
        NumberFormat nt = NumberFormat.getPercentInstance();
        //设置百分数精确度2即保留两位小数
        nt.setMinimumFractionDigits(2);

        List<String> barLegendVal = Lists.newArrayList();
        int index = 0;
        timeCountMapChanged.forEach((key, val)->{
            if(index < topNum) {
                if(subValueBig.compareTo(BigDecimal.valueOf(0)) > 0) {
//                    rsMap.put(key, nt.format(BigDecimal.valueOf(val).divide(subValueBig, 4, RoundingMode.HALF_UP)));
                    Map<String, String> rsMap = Maps.newHashMap();
                    rsMap.put("name", key);
                    barLegendVal.add(key);
                    String setVal = nt.format(BigDecimal.valueOf(val).divide(subValueBig, 4, RoundingMode.HALF_UP)).replace("%", "");
                    System.out.println("setVal:" + setVal);
                    if(setVal.equals("0.00"))
                        setVal = "0";
                    if(setVal.equals("100.00"))
                        setVal = "100";
                    if(setVal.contains(".00"))
                        setVal = setVal.replaceAll(".00", "");
                    rsMap.put("value", setVal);
                    rsLs.add(rsMap);
                }
            }
        });
        rsOutMap.put("topCountList", rsLs);
        rsOutMap.put("barLegendVal", barLegendVal);

        return rsOutMap;
        
    }

    /**
     * 根据分段时间获取不同时间段的产量数据
     * @param startDate
     * @param endDate
     * @param dateType
     * @param modelnum
     * @return
     */
    private List<String> getSeriesForDriverArray(String energyType,
            Timestamp startDate, Timestamp endDate,
            List<Long> driverIds) {
        MesDataRowkeyUtil.setEntityManager(entityManager);
        List<String> rsCount = Lists.newArrayList();

        List<SearchFilter> searchList = Lists.newArrayList();
        searchList.add(new SearchFilter("mesDataMultiKey.driverId", Operator.IN, driverIds.toArray()));

        String time = String.valueOf(startDate.getTime());
        if(time.contains("000000"))
            time = time.replace("000000", "000");
        searchList.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.GTE,time));
        searchList.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.LTE, String.valueOf(endDate.getTime()).replace("000", "")));
        String searchEnergyType = "";
        if("electric".equals(energyType)) {
            searchEnergyType = "ELECTRIC";
        } else if("gas".equals(energyType)) {
            searchEnergyType = "GAS";
        } else if("water".equals(energyType)) {
            searchEnergyType = "WATER";
        }
        searchList.add(new SearchFilter("type", Operator.EQ, searchEnergyType));
        Specification<MesDataWeg> specification = DynamicSpecifications
                .bySearchFilter(MesDataWeg.class, searchList);
        List<MesDataWeg> mesDataWegList = mesDataWegService.findAll(specification);

        return rsCount;

    }

    /**
     * 获取补位最大数
     * @param param
     * @return
     */
    public String[] getPercent(List<String> param, List<String> xAxiaList) {

        List<Integer> comList = Lists.newArrayList();
        for(String pa : param) {
            if(StringUtils.isNotEmpty(pa))
            comList.add(pa.length());

        }
        Collections.sort(comList, Collections.reverseOrder());
        int maxVl = comList.get(0);
        String rs = "1";
        for(int i=1;i<maxVl + 1;i++) {
            rs = rs + "0";
        }
        String out[] = new String[xAxiaList.size()];
        for(int j = 0;j<xAxiaList.size();j++) {
            out[j] = rs;
        }

        return out;
    }

    /**
     * 根据分段时间获取不同时间段的产量数据的合格率
     * @param startDate
     * @param endDate
     * @param dateType
     * @param modelnum
     * @return
     */
    private Map<String, Object> getSeriesByTimePercent(Timestamp startDate, Timestamp endDate,
            DateType dateType, String modelnum,
            String productBatchid) {
        Map<String, Object> rsMap = Maps.newHashMap();
        // 拆分时间段
        List<Timestamp> times = this.getDateList(dateType, startDate, endDate);
        MesDataRowkeyUtil.setEntityManager(entityManager);

        List<String> okPercentMaxList = Lists.newArrayList();
        List<String> percentMaxList = Lists.newArrayList();
        List<String> okPercentList = Lists.newArrayList();
        String startTi = "";
        String endTi = "";
        for(int i =0;i<times.size() - 1;i++) {
            startTi = String.valueOf(times.get(i).getTime()).substring(0, 10);
            endTi = String.valueOf(times.get(i+1).getTime()).substring(0, 10);
            List<String> totalCount = MesDataRowkeyUtil.getCountByPageSelect(
                    "",
                    "",
                    "",
                    modelnum,
                    "",
                    startTi,
                    endTi,
                    "",
                    productBatchid);
            List<String> okCount =MesDataRowkeyUtil.getCountByPageSelect(
                    "",
                    "",
                    "",
                    modelnum,
                    "",
                    startTi,
                    endTi,
                    "OK", productBatchid);
            okPercentMaxList.add("100");
            if(null == totalCount || 0 == totalCount.size()) {
                okPercentList.add("0%");
                percentMaxList.add("0");
            } else {
                DecimalFormat df1 = new DecimalFormat("#0.00%");
                if(0 == okCount.size()) {
                    okPercentList.add("0%");
                    percentMaxList.add("0");
                } else {
                    String rs = df1.format((float)okCount.size()/totalCount.size());
                    if("100.00%".equals(rs)) {
                        okPercentList.add("100%");
                        percentMaxList.add("100");
                    } else {
                        String percentValue = df1.format((float)okCount.size()/totalCount.size());
                        okPercentList.add(percentValue);
                        percentMaxList.add(percentValue.substring(0, percentValue.length() - 1));
                    }
                }
            }

        }
        rsMap.put("okPercentList", okPercentList);
        rsMap.put("okPercentMaxList", okPercentMaxList);
        rsMap.put("percentMaxList", percentMaxList);
        return rsMap;

    }

    /**
     * 通过产品ID查找产品批次号
     * @param productId
     * @return
     */
    private Map<String, Object> generateProbatchids(long productId, String beginTime, String endTime
            ,String totalCount) {
        try {
            Map<String, Object> rsMap = Maps.newHashMap();
            MesDataRowkeyUtil.setEntityManager(entityManager);
            List<MesDataProductProcedure>  mesDataProductProcedureList = MesDataRowkeyUtil.getProductBatchids(String.valueOf(productId)
                    ,beginTime, endTime);
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
            StringBuffer batchids =new StringBuffer();
            if(null == mesDataProductProcedureList || 0 == mesDataProductProcedureList.size()) {
                return null;
            }
//            List<String> batchidsValueDisplay = Lists.newArrayList();
            String batchidsValueDisplay[] =  new String[mesDataProductProcedureList.size()];
            int i = 0;
            for(MesDataProductProcedure obj : mesDataProductProcedureList) {
                batchidsValueDisplay[i] = obj.getProductBatchid();
                i++;
//                batchidsValueDisplay.add("产品批次" + obj.getProductBatchid());
                batchids.append("'" + obj.getProductBatchid() + "'").append(",");
            }
            rsMap.put("batchidsValueDisplay", batchidsValueDisplay);
            String batchidsS = batchids.toString();
            if(StringUtils.isNotEmpty(batchidsS)) {
                batchidsS = batchidsS.substring(0, batchidsS.length() - 1);
            }
            Map<String, Object> multValMaps = MesDataRowkeyUtil.getProductCountByBatchids(String.valueOf(productId)
                    ,beginTime, endTime, batchidsS, totalCount);
            rsMap.put("rsList", multValMaps.get("percentValList"));
            rsMap.put("seriesDataComMaxForBatch", multValMaps.get("seriesDataComMaxForBatch"));
            rsMap.put("batchXvalueLis", multValMaps.get("batchXvalueLis"));

            return rsMap;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 时间KEY由数字转换为汉字KEY
     * @param timeCountMap
     * @return
     */
    private Map<String, Float> changTimeKeyToChineseKey(Map<String, Float> timeCountMap, DateType dateType) {
        Map<String, Float> rsMap = Maps.newHashMap();
        
        String msg1 = "", msg2 = "";
        if (DateType.day.equals(dateType)) {
            msg2 = "时";
        } else if (DateType.week.equals(dateType)) {
            msg1 = "周";
        } else if (DateType.month.equals(dateType)) {
            msg2 = "日";
        } else {
            msg2 = "月";
        }
        int index = 1;
        for(String dayIndex : timeCountMap.keySet()) {
            rsMap.put(msg1 + index + msg2, timeCountMap.get(dayIndex));
            index++;
        }
        return rsMap;
    }
}
