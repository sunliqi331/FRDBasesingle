package com.its.frd.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.its.common.util.dwz.Page;
import com.its.frd.entity.MesDataMultiKey;
import com.its.frd.entity.MonitorSpc;
import com.its.frd.entity.MonitorSpc;
import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.internal.SessionFactoryImpl;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.sql.*;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class SpcDataRowkeyUtil {
    private static EntityManager entityManager;

    private static Connection conn = null;
    public static void setConn(Connection conn) {
        SpcDataRowkeyUtil.conn = conn;
    }

    /**
     * spc统计查询指定数的主key
     * @param begin
     * @param end
     * @return
     */
    public static List<MonitorSpc> getMulKeyOfPage(
            String mesDriverId,
            String productId,
            String productProcedureId,
            String driverPropertyId,
            String subrange,
            String subnum,
            String begin,
            String end,
            String kind,
            HBasePageModel hBasePageModel
            ) {
        List<MonitorSpc> outList = Lists.newArrayList();
        Connection conn = null;
        Statement s1 = null;
        ResultSet existRs = null;
        try {
            Session session = (Session) entityManager.getDelegate();
            SessionFactoryImpl sessionFactory = (SessionFactoryImpl) session.getSessionFactory();
            conn = sessionFactory.getConnectionProvider().getConnection();
            s1 = conn.createStatement();
            StringBuilder sql = new StringBuilder();
            sql.append("select * "
//                    "id,"
//                    + "monitor_painter_id,"
//                    + "driver_property_id,"
//                    + "chart_id,"
//                    + " cp, "
//                    + " cpk, "
//                    + " pp, "
//                    + " product_batchid,"
//                    + " MEASTYPE,"
//                    + " QUALIFIED "
                    + "from monitor_spc");
            sql.append(" where 1 = 1 ");
            if(StringUtils.isNotBlank( mesDriverId) && (!"null".equals( mesDriverId)))
            sql.append(" and mes_driver_id = " + mesDriverId);

            if(StringUtils.isNotBlank(productId) && (!"0".equals(productId)))
            sql.append(" and product_id = " + productId);

            if(StringUtils.isNotBlank(begin))
            sql.append(" and createtime >= '" + begin + "'");

            if(StringUtils.isNotBlank(end))
            sql.append(" and createtime <= '" + end + "'");

            if(StringUtils.isNotBlank(driverPropertyId))
            sql.append(" and driver_property_id = " + driverPropertyId);

            if(StringUtils.isNotBlank(subrange))
            sql.append(" and subrange = " + subrange);

            if(StringUtils.isNotBlank(subnum))
            sql.append(" and subnum = " + subnum);

            sql.append(" group by createtime");
            sql.append(" order by createtime desc");
            if("1".equals(kind))
            sql.append(" LIMIT " + (hBasePageModel.getPlainPageNum() - 1) * hBasePageModel.getNumPerPage() + " ," + hBasePageModel.getNumPerPage());
            existRs = s1.executeQuery(sql.toString());
            MonitorSpc outObj = null;
            MesDataMultiKey mesDataMultiKey = null;
            while(existRs.next()){
                outObj = new MonitorSpc();
//                mesDataMultiKey = new MesDataMultiKey();
//                mesDataMultiKey.setFactoryId(Integer.valueOf(existRs.getString(1)));
//                mesDataMultiKey.setProductLineId(Integer.valueOf(existRs.getString(2)));
//                mesDataMultiKey.setDriverId(Integer.valueOf(existRs.getString(3)));
//                mesDataMultiKey.setPointId(Integer.valueOf(existRs.getString(4)));
//                mesDataMultiKey.setInsertTimestamp(BigInteger.valueOf(Integer.valueOf(existRs.getString(5))));
//                outObj.setMesDataMultiKey(mesDataMultiKey);
//                outObj.setProductProcedureId(Integer.valueOf(existRs.getString(6)));
//                outObj.setProductBsn(existRs.getString(7));
//                outObj.setProductBatchid(existRs.getString(8));
//                outObj.setMeastype(Integer.valueOf(existRs.getString(9)));
//                outObj.setQualified(Integer.valueOf(existRs.getString(10)));
                outObj.setId(Integer.parseInt(existRs.getString("id")));
                outObj.setMonitorPainterId(Long.valueOf(existRs.getString("monitor_painter_id")));
                outObj.setDriverPropertyId(Long.valueOf(existRs.getString("driver_property_id")));
                outObj.setChartId(existRs.getString("chart_id"));
                outObj.setCp(Double.valueOf(existRs.getString("cp")));
                outObj.setCpk(Double.valueOf(existRs.getString("cpk")));
                outObj.setPp(Double.valueOf(existRs.getString("pp")));
                outObj.setPpk(Double.valueOf(existRs.getString("ppk")));
                outObj.setCreatetime(Timestamp.valueOf(existRs.getString("createtime")));
                outObj.setProductId(Long.valueOf(existRs.getString("product_id")));
                outObj.setProductProcedureId(Long.valueOf(existRs.getString("product_procedure_id")));
                outObj.setMesDriverId(Long.valueOf(existRs.getString("mes_driver_id")));
                outObj.setSubrange(Long.valueOf(existRs.getString("subrange")));
                outObj.setSubnum(Long.valueOf(existRs.getString("subnum")));
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

    public static List<String> getCountByPageSelect(
            String mesDriverId,
            String productId,
            String productProcedureId,
            String driverPropertyId,
            String subrange,
            String subnum,
            String begin,
            String end
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
            sql.append("select createtime "
                    + "from monitor_spc");
            sql.append(" where 1 = 1 ");
            if(StringUtils.isNotBlank( mesDriverId) && (!"null".equals( mesDriverId)))
                sql.append(" and mes_driver_id = " + mesDriverId);

            if(StringUtils.isNotBlank(productId) && (!"0".equals(productId)))
                sql.append(" and product_id = " + productId);

            if(StringUtils.isNotBlank(begin))
            sql.append(" and createtime >= '" + begin +"'");

            if(StringUtils.isNotBlank(end))
            sql.append(" and createtime <= '" + end +"'");

            if(StringUtils.isNotBlank(driverPropertyId))
                sql.append(" and driver_property_id = " + driverPropertyId);

            if(StringUtils.isNotBlank(subrange))
                sql.append(" and subrange = " + subrange);

            if(StringUtils.isNotBlank(subnum))
                sql.append(" and subnum = " + subnum);

            sql.append(" group by createtime");

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
        SpcDataRowkeyUtil.entityManager = entityManager;
    }
}

