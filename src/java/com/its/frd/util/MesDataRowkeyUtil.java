package com.its.frd.util;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.text.NumberFormat;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ntp.TimeStamp;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.internal.SessionFactoryImpl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.its.frd.entity.MesDataMultiKey;
import com.its.frd.entity.MesDataProductProcedure;
import com.its.frd.service.ProductAndEnergyAndDriverChartService.DateType;
public class MesDataRowkeyUtil {
    private static EntityManager entityManager;
    
    private static Connection conn = null;
    public static void setConn(Connection conn) {
        MesDataRowkeyUtil.conn = conn;
    }

    /**
     * 组装MesData的主key
     * 
     * @param inputObj
     * @return
     */
    public static String getRowKey(MesDataProductProcedure inputObj) {
        StringBuffer outBur = new StringBuffer();
        MesDataMultiKey mesDataMultiKey = inputObj.getMesDataMultiKey();
        outBur.append(null != mesDataMultiKey.getFactoryId() ? mesDataMultiKey.getFactoryId() : "").append("_");
        outBur.append(null != mesDataMultiKey.getProductLineId() ? mesDataMultiKey.getProductLineId() : "").append("_");
        outBur.append(null != mesDataMultiKey.getDriverId() ? mesDataMultiKey.getDriverId() : "").append("_");
        outBur.append(null != mesDataMultiKey.getPointId() ? mesDataMultiKey.getPointId() : "").append("_");
        outBur.append(null != mesDataMultiKey.getInsertTimestamp() ? mesDataMultiKey.getInsertTimestamp() : "").append("_");
        outBur.append(null != inputObj.getProductProcedureId() ? inputObj.getProductProcedureId() : "");
        return outBur.toString();
    }

    /**
     * 根据ROWKEY拆分参数
     * @param rowKey
     * @return
     */
    public static Map<String, String> getValMapByProcedureDataRowKey(String rowKey) {
        Map<String, String> rsMap = Maps.newHashMap();
        if(StringUtils.isEmpty(rowKey))
            return rsMap;
        String inVal[] = rowKey.split("_");
        rsMap.put("factoryId", inVal[0]);
        rsMap.put("productLineId", inVal[1]);
        rsMap.put("driverId", inVal[2]);
        rsMap.put("pointId", inVal[3]);
        rsMap.put("insertTimestamp", inVal[4]);
        rsMap.put("productProcedureId", inVal[5]);
        return rsMap;
    }
    
    /**
     * 生产记录查询指定数的主key
     * @param factoryId
     * @param productLineId
     * @param driverId
     * @param begin
     * @param end
     * @return
     */
    public static List<MesDataProductProcedure> getMulKeyOfPage(String factoryId,
            String productLineId,
            String driverId,
            String productMode,
            String productProcedureId,
            String begin, String end,
            HBasePageModel hBasePageModel, String kind,
            String productBatchid,
            Integer meastype
            ) {
        List<MesDataProductProcedure> outList = Lists.newArrayList();
        Connection conn = null;
        Statement s1 = null;
        ResultSet existRs = null;
        try {
            Session session = (Session) entityManager.getDelegate();
            SessionFactoryImpl sessionFactory = (SessionFactoryImpl) session.getSessionFactory();
            conn = sessionFactory.getConnectionProvider().getConnection();
            s1 = conn.createStatement();
            StringBuilder sql = new StringBuilder();
            sql.append("select FACTORY_ID,"
                    + "PRODUCT_LINE_ID,"
                    + "DRIVER_ID,"
                    + "POINT_ID,"
                    + " INSERT_TIMESTAMP, "
                    + " PRODUCT_PROCEDURE_ID, "
                    + " product_bsn, "
                    + " product_batchid,"
                    + " MEASTYPE,"
                    + " QUALIFIED "
                    + "from data_product_procedure");
            sql.append(" where 1 = 1 ");
            if(StringUtils.isNotBlank( factoryId) && (!"null".equals( factoryId)))
            sql.append(" and FACTORY_ID = " + factoryId);
            if(StringUtils.isNotBlank(productLineId) && (!"0".equals(productLineId)))
            sql.append(" and PRODUCT_LINE_ID = " + productLineId);

            if(StringUtils.isNotBlank(begin))
            sql.append(" and INSERT_TIMESTAMP >= " + begin);

            if(StringUtils.isNotBlank(end))
            sql.append(" and INSERT_TIMESTAMP <= " + end);

            if(StringUtils.isNotBlank(driverId))
            sql.append(" and DRIVER_ID = " + driverId);
            if(StringUtils.isNotBlank(productMode))
            sql.append(" and PRODUCT_MODE = '" + productMode +"'");
            if(StringUtils.isNotBlank(productProcedureId))
            sql.append(" and PRODUCT_PROCEDURE_ID = " + productProcedureId);
            if(StringUtils.isNotEmpty(productBatchid) && !"0".equals(productBatchid))
                sql.append(" and PRODUCT_BATCHID = '" + productBatchid + "'");

            if(meastype!=null)
                sql.append(" and MEASTYPE = " + meastype);
            sql.append(" group by INSERT_TIMESTAMP");
            sql.append(" order by INSERT_TIMESTAMP desc");
            if("1".equals(kind))
            sql.append(" LIMIT " + (hBasePageModel.getPlainPageNum() - 1) * hBasePageModel.getNumPerPage() + " ," + hBasePageModel.getNumPerPage());
            existRs = s1.executeQuery(sql.toString());
            MesDataProductProcedure outObj = null;
            MesDataMultiKey mesDataMultiKey = null;
            while(existRs.next()){
                outObj = new MesDataProductProcedure();
                mesDataMultiKey = new MesDataMultiKey();
                mesDataMultiKey.setFactoryId(Integer.valueOf(existRs.getString(1)));
                mesDataMultiKey.setProductLineId(Integer.valueOf(existRs.getString(2)));
                mesDataMultiKey.setDriverId(Integer.valueOf(existRs.getString(3)));
                mesDataMultiKey.setPointId(Integer.valueOf(existRs.getString(4)));
                mesDataMultiKey.setInsertTimestamp(BigInteger.valueOf(Integer.valueOf(existRs.getString(5))));
                outObj.setMesDataMultiKey(mesDataMultiKey);
                outObj.setProductProcedureId(Integer.valueOf(existRs.getString(6)));
                outObj.setProductBsn(existRs.getString(7));
                outObj.setProductBatchid(existRs.getString(8));
                outObj.setMeastype(Integer.valueOf(existRs.getString(9)));
                outObj.setQualified(Integer.valueOf(existRs.getString(10)));
                outList.add(outObj);
            }
            if("1".equals(kind)) {
                if(null != outList && 0 < outList.size()) {
                    hBasePageModel.setTotalCount(outList.size());
                    hBasePageModel.setTotalPage(outList.size() / hBasePageModel.getNumPerPage());
                }
            }

        } catch (HibernateException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if(existRs != null){
                    existRs.close();
                }
                if(s1 != null){
                    s1.close();
                }
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return outList;
    }
    
    /**
     * 生产记录查询指定数的主key
     * @param factoryId
     * @param productLineId
     * @param driverId
     * @param begin
     * @param end
     * @return
     */
    public static List<String> getCountByPageSelect(String factoryId,
            String productLineId,
            String driverId,
            String productMode,
            String productProcedureId,
            String begin, String end, String checkValue,
            String productBatchid
            ) {
        List<String> timelist = Lists.newArrayList();
        Statement s1 = null;
        ResultSet existRs = null;
        try {
            Session session = (Session) entityManager.getDelegate();
            SessionFactoryImpl sessionFactory = (SessionFactoryImpl) session.getSessionFactory();
//            if(null == conn)
            conn = sessionFactory.getConnectionProvider().getConnection();
            s1 = conn.createStatement();
            StringBuilder sql = new StringBuilder();
            sql.append("select INSERT_TIMESTAMP "
                    + "from data_product_procedure");
            sql.append(" where 1 = 1");
            if(StringUtils.isNotEmpty(factoryId) && (!"null".equals(factoryId)))
            sql.append(" and FACTORY_ID = " + factoryId);
            if(StringUtils.isNotEmpty(begin))
            sql.append(" and INSERT_TIMESTAMP >= " + begin);

            if(StringUtils.isNotEmpty(end))
            sql.append(" and INSERT_TIMESTAMP <= " + end);

            if(StringUtils.isNotEmpty(productLineId) && (!"0".equals(productLineId)))
            sql.append(" and PRODUCT_LINE_ID = " + productLineId);

            if(StringUtils.isNotEmpty(driverId))
            sql.append(" and DRIVER_ID = " + driverId);

            if(StringUtils.isNotEmpty(productMode))
            sql.append(" and PRODUCT_MODE = '" + productMode +"'");
            
            if(StringUtils.isNotEmpty(productProcedureId))
            sql.append(" and PRODUCT_PROCEDURE_ID = " + productProcedureId);

            if(StringUtils.isNotEmpty(productBatchid) && !"0".equals(productBatchid))
            sql.append(" and PRODUCT_BATCHID = '" + productBatchid + "'");
            
            if(StringUtils.isNotEmpty(checkValue))
            sql.append(" and VALUE_STATUS = '" + checkValue + "'");

            sql.append(" group by INSERT_TIMESTAMP");

            existRs = s1.executeQuery(sql.toString());
            System.out.println("检测SQL：" + sql.toString());
            while(existRs.next()){
                timelist.add(existRs.getString(1));
            }

        } catch (HibernateException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if(existRs != null){
                    existRs.close();
                }
                if(s1 != null){
                    s1.close();
                }
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return timelist;
    }

    public static EntityManager getEntityManager() {
        return entityManager;
    }

    public static void setEntityManager(EntityManager entityManager) {
        MesDataRowkeyUtil.entityManager = entityManager;
    }
    
    /**
     * 设置HBASE搜索时间范围
     * @param scan
     * @param hBasePageModel
     */
    public static Map<String, Date> getScanTimeRangeByHBasePageModel(HBasePageModel hBasePageModel) {
        Map<String, Date> rs = Maps.newHashMap();
        Date begin = hBasePageModel.getAnalyzeSearch().getBegin();
        Date end = hBasePageModel.getAnalyzeSearch().getEnd();
        if (null != begin && null != end) {
            rs.put("begin", begin);
            rs.put("end", end);
        } else if (null == end && null != begin) {
            Date curretTime = new Date();
            rs.put("begin", begin);
            rs.put("end", curretTime);
        } else if (null == begin && null != end) {
            rs.put("begin", null);
            rs.put("end", end);
        }
        return rs;
    }
    
    /**
     * 生产记录查询指定数的主key
     * @param factoryId
     * @param productLineId
     * @param driverId
     * @param begin
     * @param end
     * @return
     */
    public static List<String> getDriverCount(
            List<Long> driveridList,
            String engryType,
            String begin,
            String end) {
        List<String> timelist = Lists.newArrayList();
        Statement s1 = null;
        ResultSet existRs = null;
        try {
            Session session = (Session) entityManager.getDelegate();
            SessionFactoryImpl sessionFactory = (SessionFactoryImpl) session.getSessionFactory();
            if(null == conn)
            conn = sessionFactory.getConnectionProvider().getConnection();
            s1 = conn.createStatement();
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT T1.*");
            sql.append(" FROM (");
            sql.append(" SELECT DRIVER_ID, META_VALUE "
                    + "FROM data_weg");
            sql.append(" WHERE 1 = 1");
            if(StringUtils.isNotEmpty(begin))
            sql.append(" AND INSERT_TIMESTAMP >= " + begin);
            if(StringUtils.isNotEmpty(end))
            sql.append(" AND INSERT_TIMESTAMP <= " + end);
            if(StringUtils.isNotEmpty(engryType))
            sql.append(" AND TYPE = '" + engryType + "'");
            if(null != driveridList)
            sql.append(" AND DRIVER_ID IN ( " + getDriverListStr(driveridList) + ")");
            sql.append(" ORDER BY INSERT_TIMESTAMP DESC");
            sql.append(" ) T1");
            sql.append(" GROUP BY T1.DRIVER_ID");

            existRs = s1.executeQuery(sql.toString());
//            System.out.println("检测SQL：" + sql.toString());
            while(existRs.next()){
                timelist.add(existRs.getString(1));
            }

        } catch (HibernateException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if(existRs != null){
                    existRs.close();
                }
                if(s1 != null){
                    s1.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return timelist;
    }

    /**
     * 设备sql用字符串
     * @param driverList
     * @return
     */
    private static String getDriverListStr(List<Long> driverList) {
        StringBuffer stringBuffer = new StringBuffer();
        for(Long num : driverList) {
            stringBuffer.append(String.valueOf(num));
            stringBuffer.append(",");
        }
        return stringBuffer.substring(0, stringBuffer.length()-1).toString();
    }
    
    /**
     * 根据产品ID获取对应的，已经生产的批次号
     * @param productMode 产品号。
     * @return
     */
    public static List<MesDataProductProcedure> getProductBatchids(String productMode
            ,String begin
            ,String end) {
        List<MesDataProductProcedure> outList = Lists.newArrayList();
        try {
            Session session = (Session) entityManager.getDelegate();
            SessionFactoryImpl sessionFactory = (SessionFactoryImpl) session.getSessionFactory();
            Connection conn = sessionFactory.getConnectionProvider().getConnection();
            Statement s1 = null;
            s1 = conn.createStatement();
            ResultSet existRs = null;
            StringBuilder sql = new StringBuilder();
            sql.append("select FACTORY_ID,"
                    + "PRODUCT_LINE_ID,"
                    + "DRIVER_ID,"
                    + "POINT_ID,"
                    + " INSERT_TIMESTAMP, "
                    + " PRODUCT_PROCEDURE_ID, "
                    + " product_bsn, "
                    + " product_batchid "
                    + "from data_product_procedure");
            sql.append(" where product_batchid is not NULL ");
            if(StringUtils.isNotEmpty(begin))
            sql.append(" AND INSERT_TIMESTAMP >= " + begin);
            if(StringUtils.isNotEmpty(end))
            sql.append(" AND INSERT_TIMESTAMP <= " + end);
            if(StringUtils.isNotBlank(productMode))
            sql.append(" and PRODUCT_MODE = " + productMode);
            sql.append(" group by product_batchid ");

            existRs = s1.executeQuery(sql.toString());
            System.out.println("[根据产品ID获取对应的，已经生产的批次号]:" + sql.toString());
            if(StringUtils.isNotEmpty(begin))
            System.out.println(DateUtils.getTimeStrByTimeStamp(new Timestamp(Long.valueOf(begin))));
            if(StringUtils.isNotEmpty(end))
            System.out.println(DateUtils.getTimeStrByTimeStamp(new Timestamp(Long.valueOf(end))));
            MesDataProductProcedure outObj = null;
            MesDataMultiKey mesDataMultiKey = null;
            while(existRs.next()){
                outObj = new MesDataProductProcedure();
                mesDataMultiKey = new MesDataMultiKey();
                mesDataMultiKey.setFactoryId(Integer.valueOf(existRs.getString(1)));
                mesDataMultiKey.setProductLineId(Integer.valueOf(existRs.getString(2)));
                mesDataMultiKey.setDriverId(Integer.valueOf(existRs.getString(3)));
                mesDataMultiKey.setPointId(Integer.valueOf(existRs.getString(4)));
                mesDataMultiKey.setInsertTimestamp(BigInteger.valueOf(Integer.valueOf(existRs.getString(5))));
                outObj.setMesDataMultiKey(mesDataMultiKey);
                outObj.setProductProcedureId(Integer.valueOf(existRs.getString(6)));
                outObj.setProductBsn(existRs.getString(7));
                outObj.setProductBatchid(existRs.getString(8));
                outList.add(outObj);
            }

        } catch (HibernateException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return outList;
    }

    /**
     * 根据产品ID获取对应的，已经生产的批次号
     * @param productMode 产品号。
     * @return
     */
    public static Map<String, Object> getProductCountByBatchids(String productMode
            ,String begin
            ,String end,
            String proBatchids,
            String totalCount) {
        Map<String, Object> resultMap = Maps.newHashMap();
        List<Map<String,String>> outList = Lists.newArrayList();
        try {
            Session session = (Session) entityManager.getDelegate();
            SessionFactoryImpl sessionFactory = (SessionFactoryImpl) session.getSessionFactory();
            Connection conn = sessionFactory.getConnectionProvider().getConnection();
            Statement s1 = null;
            s1 = conn.createStatement();
            ResultSet existRs = null;
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT");
            sql.append("    count(t1.PRODUCT_BATCHID) AS count, ");
            sql.append("    t1.PRODUCT_BATCHID AS batchid ");
            sql.append(" FROM");
            sql.append("    (");
            sql.append("        SELECT");
            sql.append("            INSERT_TIMESTAMP, ");
            sql.append("            PRODUCT_BATCHID ");
            sql.append("        FROM");
            sql.append("            data_product_procedure ");
            sql.append("        WHERE 1 = 1 ");
            if(StringUtils.isNotEmpty(proBatchids))
            sql.append("        AND product_batchid in ( "+proBatchids+" ) ");
            if(StringUtils.isNotEmpty(begin))
            sql.append("        AND INSERT_TIMESTAMP >= " + begin);
            if(StringUtils.isNotEmpty(end))
            sql.append("        AND INSERT_TIMESTAMP <= " + end);
            sql.append("            GROUP BY");
            sql.append("            INSERT_TIMESTAMP, ");
            sql.append("            PRODUCT_BATCHID ");
            sql.append("         ) t1");
            sql.append(" GROUP BY ");
            sql.append("    t1.PRODUCT_BATCHID ");
            sql.append(" ORDER BY ");
            sql.append("    t1.PRODUCT_BATCHID ");

//            sql.append("select count(PRODUCT_BATCHID) as count, PRODUCT_BATCHID as batchid from data_product_procedure ");
//            sql.append(" where 1 = 1 ");
//            if(StringUtils.isNotEmpty(proBatchids))
//            sql.append(" AND product_batchid in ( "+proBatchids+" ) ");
//            if(StringUtils.isNotEmpty(begin))
//            sql.append(" AND INSERT_TIMESTAMP >= " + begin);
//            if(StringUtils.isNotEmpty(end))
//            sql.append(" AND INSERT_TIMESTAMP <= " + end);
//            sql.append(" group by PRODUCT_BATCHID ");
            System.out.println("[根据产品ID获取对应的，已经生产的批次号2]:" + sql.toString());
            existRs = s1.executeQuery(sql.toString());
            String count = "";
            String proBatchid = "";

            List<String> xvalueLis = Lists.newArrayList();
            List<String> yvalueLis = Lists.newArrayList();
            Map<String, String> rsMap = Maps.newHashMap();
            NumberFormat numberFormat = NumberFormat.getInstance();
            while(existRs.next()){
                count = existRs.getString(1);
                proBatchid = existRs.getString(2);
                xvalueLis.add(count);
                yvalueLis.add(proBatchid);

                rsMap = Maps.newHashMap();
                rsMap.put("value", numberFormat.format((float) Integer.valueOf(existRs.getString(1)) / (float) Integer.valueOf(totalCount) * 100));
                rsMap.put("name", existRs.getString(2));
                outList.add(rsMap);
            }
            if(null != xvalueLis && 0 < xvalueLis.size())
                resultMap.put("batchXvalueLis", xvalueLis);
            if(null != outList && 0 < outList.size())
            resultMap.put("percentValList", outList);

            String out[] = getPercent(xvalueLis, yvalueLis);
            if(null !=  out)
            resultMap.put("seriesDataComMaxForBatch", out);

        } catch (HibernateException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultMap;
    }
    
    
    /**
     * 获取补位最大数
     * @param param
     * @return
     */
    private static String[] getPercent(List<String> param, List<String> xAxiaList) {

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
     * 生产记录查询指定数的主key
     * @param factoryId
     * @param productLineId
     * @param driverId
     * @param begin
     * @param end
     * @return
     */
    public static Map<String, String> getDriverCountByPageSelect(
            String factoryId,
            String productLineId,
            String driverId,
//            DateType dateType,
//            Timestamp startDate,
//            Timestamp endDate,
            List<Timestamp> times
            ) {
//        List<String> timelist = Lists.newArrayList();
        Statement s1 = null;
        ResultSet existRs = null;
        Map<String, String> rsMap = Maps.newHashMap();
        try {
            Session session = (Session) entityManager.getDelegate();
            SessionFactoryImpl sessionFactory = (SessionFactoryImpl) session.getSessionFactory();
            // if(null == conn)
            conn = sessionFactory.getConnectionProvider().getConnection();
            s1 = conn.createStatement();

//            List<Timestamp> times = TimeSplitUtil.getDateList(dateType, startDate, endDate);
            for(int i =0;i<times.size() - 1;i++) {
                StringBuilder sql = new StringBuilder();
                sql.append("select DRIVER_COUNT "
                        + "from data_driver_count");
                sql.append(" where 1 = 1");

                if(0L != times.get(i).getTime()) {
                    String searchTime = String.valueOf(times.get(i).getTime()).substring(0,  10);
                    sql.append(" and FROM_UNIXTIME(INSERT_TIMESTAMP,'%Y-%m-%d') = FROM_UNIXTIME(" + searchTime + ",'%Y-%m-%d') ");
                }

                if(StringUtils.isNotEmpty(factoryId) && (!"null".equals(factoryId)))
                sql.append(" and FACTORY_ID = " + factoryId);
                if(StringUtils.isNotEmpty(productLineId) && (!"0".equals(productLineId)))
                sql.append(" and PRODUCT_LINE_ID = " + productLineId);
                if(StringUtils.isNotEmpty(driverId))
                sql.append(" and DRIVER_ID = " + driverId);

                sql.append(" order by INSERT_TIMESTAMP desc limit 1");

                existRs = s1.executeQuery(sql.toString());
                System.out.println("检测SQL：" + sql.toString());
                if(existRs.next()){
//                    timelist.add(existRs.getString(1));
                    rsMap.put(String.valueOf(times.get(i).getTime()), existRs.getString(1));
                }
            }

        } catch (HibernateException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if(existRs != null){
                    existRs.close();
                }
                if(s1 != null){
                    s1.close();
                }
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return rsMap;
    }

}

