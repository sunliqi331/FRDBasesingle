package com.its.frd.util;


import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.filter.QualifierFilter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.ValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.RowMapper;
import org.springframework.data.hadoop.hbase.TableCallback;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.its.common.utils.SecurityUtils;
import com.its.frd.dao.MesProductDao;
import com.its.frd.dao.MesProductlineDao;
import com.its.frd.entity.MesProduct;
import com.its.frd.entity.MesProductline;
import com.its.statistics.vo.AnalyzeSearch;
import com.its.statistics.vo.PropertyTrendSearch;

public class HbaseUtil {

	private final static Log log = LogFactory.getLog(HbaseUtil.class);
	private static HbaseTemplate htemplate = null;//SpringBeanUtils.getBean("htemplate",HbaseTemplate.class);
	

	public static final String TABLE_NAME = "frd2";
	public static final String FAMILY_NAME = "cf";

	private static final String TABLE_COLUMN_FAMILY_NAME = "test1";
	// 声明静态配置
	private static Configuration conf = null;//htemplate.getConfiguration();
	private static HConnection connection = null;
	private static HTableInterface attachmentsTable = null;

	// HBSE:生产记录表
	public static final String HBASE_TABLE_DRIVECOUNT = "driveCount";

	// HBSE:设备属性表
	public static final String HBASE_TABLE_DRIVERPROPERTYANDSTATUS = "driverPropertyAndStatus";

	// HBSE:生产记录表
	public static final String HBASE_TABLE_PROCEDURE = "procedure";
	
//    @Autowired
//    private HbaseTemplate hTemplate;
	
	public static Configuration getConfiguration(){
		return conf;
	}

//	public static void connectHbase() {
//		try {
//			if (connection == null) {
//				connection = HConnectionManager.createConnection(conf);
//			}
//			if (attachmentsTable == null) {
//				attachmentsTable = connection.getTable(TABLE_NAME);
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			String errMsg = "Error retrievinging connection and access to dangerousEventsTable";
//			log.error(errMsg, e);
//			throw new Runtim	eException(errMsg, e);
//		}
//	}

	/*
	 * 创建表
	 * 
	 * @tableName 表名
	 * 
	 * @family 列族列表
	 */
	public static void creatTable(String tableName, String columnfamily) {
		try {
			HBaseAdmin admin = new HBaseAdmin(connection);
			HTableDescriptor desc = new HTableDescriptor(tableName);
			desc.addFamily(new HColumnDescriptor(columnfamily));
			if (admin.tableExists(tableName)) {
				log.info("table:" + tableName + " Exists!");
				// System.exit(0);
			} else {
				admin.createTable(desc);
				log.info("create table:" + tableName + " Success!");
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.info("create table:" + tableName + " Error!", e);
		}

	}

	/*
	 * 为表添加数据（适合知道有多少列族的固定表）
	 * 
	 * @rowKey rowKey
	 * 
	 * @column1 第一个列族列表
	 * 
	 * @value1 第一个列的值的列表
	 * 
	 */
	public static Put getPut(String rowKey, String column, String value) {
		System.out.println("Record with key[" + rowKey + "] going to be inserted...");
		Put put = new Put(rowKey.getBytes());// 设置rowkey
		put.add(TABLE_COLUMN_FAMILY_NAME.getBytes(), column.getBytes(), value.getBytes());
		return put;
	}

//	public static void addData(List<Put> puts) {
//		try {
//			attachmentsTable.put(puts);
//			log.info("Success inserting " + puts.size() + " into HBase table[" + TABLE_NAME + "]");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			log.error("	Error inserting event into HBase table[" + TABLE_NAME + "]", e);
//		}
//	}

	/*
	 * 根据rwokey查询
	 * 
	 * @rowKey rowKey
	 * 
	 * @tableName 表名
	 */
	public static Result getResult(String tableName, String rowKey) throws IOException {
		Get get = new Get(Bytes.toBytes(rowKey));
		HTable table = new HTable(conf, Bytes.toBytes(tableName));// 获取表
		Result result = table.get(get);
		for (KeyValue kv : result.list()) {
			System.out.println("family:" + Bytes.toString(kv.getFamily()));
			System.out.println("qualifier:" + Bytes.toString(kv.getQualifier()));
			System.out.println("value:" + Bytes.toString(kv.getValue()));
			System.out.println("Timestamp:" + kv.getTimestamp());
			System.out.println("-------------------------------------------");
		}
		return result;
	}

	/*
	 * 遍历查询hbase表
	 * 
	 * @tableName 表名
	 */
//	public static void getResultScann(String tableName) throws IOException {
//		Scan scan = new Scan();
//		ResultScanner rs = null;
//		try {
//			rs = attachmentsTable.getScanner(scan);
//			for (Result r : rs) {
//				for (KeyValue kv : r.list()) {
//					System.out.println("row:" + Bytes.toString(kv.getRow()));
//					System.out.println("family:" + Bytes.toString(kv.getFamily()));
//					System.out.println("qualifier:" + Bytes.toString(kv.getQualifier()));
//					System.out.println("value:" + Bytes.toString(kv.getValue()));
//					System.out.println("timestamp:" + kv.getTimestamp());
//					System.out.println("-------------------------------------------");
//				}
//			}
//		} finally {
//			rs.close();
//			//table.close();
//		}
//	}
	
	public static void getResultScann(String tableName,Scan scan) throws IOException {
		ResultScanner rs = null;
		try {
			rs = attachmentsTable.getScanner(scan);
			for (Result r : rs) {
				for (KeyValue kv : r.list()) {
					System.out.println("row:" + Bytes.toString(kv.getRow()));
					System.out.println("family:" + Bytes.toString(kv.getFamily()));
					System.out.println("qualifier:" + Bytes.toString(kv.getQualifier()));
					System.out.println("value:" + Bytes.toString(kv.getValue()));
					System.out.println("timestamp:" + kv.getTimestamp());
					System.out.println("-------------------------------------------");
				}
			}
		} finally {
			rs.close();
			//table.close();
		}
	}

	/*
	 * 遍历查询hbase表
	 * 
	 * @tableName 表名
	 */
	public static void getResultScann(String tableName, String start_rowkey, String stop_rowkey) throws IOException {
		Scan scan = new Scan();
		scan.setStartRow(Bytes.toBytes(start_rowkey));
		scan.setStopRow(Bytes.toBytes(stop_rowkey));
		ResultScanner rs = null;
		HTable table = new HTable(conf, Bytes.toBytes(tableName));
		try {
			rs = table.getScanner(scan);
			// for (Result r : rs) {
			// for (KeyValue kv : r.list()) {
			// System.out.println("row:" + Bytes.toString(kv.getRow()));
			// System.out.println("family:"
			// + Bytes.toString(kv.getFamily()));
			// System.out.println("qualifier:"
			// + Bytes.toString(kv.getQualifier()));
			// System.out
			// .println("value:" + Bytes.toString(kv.getValue()));
			// System.out.println("timestamp:" + kv.getTimestamp());
			// System.out
			// .println("-------------------------------------------");
			// }
			// }
		} finally {
			rs.close();
		}
	}

	/*
	 * 查询表中的某一列
	 * 
	 * @tableName 表名
	 * 
	 * @rowKey rowKey
	 */
	public static Result getResultByColumn(String tableName, String rowKey, String familyName, String columnName) {
		Get get = new Get(Bytes.toBytes(rowKey));
		get.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(columnName)); // 获取指定列族和列修饰符对应的列
		Result result = null;
		try {
			HTableInterface attachmentsTable2 = connection.getTable(tableName);
			result = attachmentsTable2.get(get);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.info("error selectbycolumn", e);
		}
		return result;
	}

	/*
	 * 更新表中的某一列
	 * 
	 * @tableName 表名
	 * 
	 * @rowKey rowKey
	 * 
	 * @familyName 列族名
	 * 
	 * @columnName 列名
	 * 
	 * @value 更新后的值
	 */
	public static void updateTable(String tableName, String rowKey, String familyName, String columnName, String value)
			throws IOException {
		Put put = new Put(Bytes.toBytes(rowKey));
		put.add(Bytes.toBytes(familyName), Bytes.toBytes(columnName), Bytes.toBytes(value));
		attachmentsTable.put(put);
		System.out.println("update table Success!");
	}

	/*
	 * 查询某列数据的多个版本
	 * 
	 * @tableName 表名
	 * 
	 * @rowKey rowKey
	 * 
	 * @familyName 列族名
	 * 
	 * @columnName 列名
	 */
	public static void getResultByVersion(String tableName, String rowKey, String familyName, String columnName)
			throws IOException {
		HTable table = new HTable(conf, Bytes.toBytes(tableName));
		Get get = new Get(Bytes.toBytes(rowKey));
		get.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(columnName));
		get.setMaxVersions(5);
		Result result = table.get(get);
		for (KeyValue kv : result.list()) {
			System.out.println("family:" + Bytes.toString(kv.getFamily()));
			System.out.println("qualifier:" + Bytes.toString(kv.getQualifier()));
			System.out.println("value:" + Bytes.toString(kv.getValue()));
			System.out.println("Timestamp:" + kv.getTimestamp());
			System.out.println("-------------------------------------------");
		}
		/*
		 * List<?> results = table.get(get).list(); Iterator<?> it =
		 * results.iterator(); while (it.hasNext()) {
		 * System.out.println(it.next().toString()); }
		 */
	}

	/*
	 * 删除指定的行
	 * 
	 * @tableName 表名
	 * 
	 * @rowKey rowKey
	 * 
	 * @familyName 列族名
	 * 
	 * @columnName 列名
	 */
	public static void deleteRowByRowkey(String tableName, String rowKey)
			throws IOException {
        try {
            Map<String, Object> map = htemplate.get(tableName, rowKey, new RowMapper<Map<String, Object>>() {
                @SuppressWarnings("deprecation")
                public Map<String, Object> mapRow(Result result, int rowNum) throws Exception {
                    List<Cell> ceList = result.listCells();
                    Map<String, Object> map = new HashMap<String, Object>();
                    if (ceList != null && ceList.size() > 0) {
                        for (Cell cell : ceList) {
                            map.put("faliyNm", Bytes.toString(cell.getFamily()));
                        }
                    }
                    return map;
                }
            });
            String familynm = "";
            for(String nm : map.keySet()) {
                System.out.println("------------------------------");
                System.out.println("familynm:" + map.get(nm));
                familynm = String.valueOf(map.get(nm));
                System.out.println("------------------------------");
                StringUtils.isNotEmpty(familynm);
                break;
            }
            if(null != map && 0 < map.size() && StringUtils.isNotEmpty(familynm)) {
                System.out.println("数据存在，准备删除");
                htemplate.delete("procedure", rowKey, familynm);
                System.out.println("数据删除结束。");
            } else {
                System.out.println("数据不存在，取消删除");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

	/*
     * 删除指定的列
     * 
     * @tableName 表名
     * 
     * @rowKey rowKey
     * 
     * @familyName 列族名
     * 
     * @columnName 列名
     */
    public static void deleteRow(String tableName, String rowKey, String falilyName, String columnName)
            throws IOException {
        HTable table = new HTable(conf, Bytes.toBytes(tableName));
        Delete deleteColumn = new Delete(Bytes.toBytes(rowKey));
        deleteColumn.deleteColumns(Bytes.toBytes(falilyName), Bytes.toBytes(columnName));
        table.delete(deleteColumn);
        System.out.println(falilyName + ":" + columnName + "is deleted!");
    }

	/*
	 * 删除指定的列
	 * 
	 * @tableName 表名
	 * 
	 * @rowKey rowKey
	 */
	public static void deleteAllColumn(String tableName, String rowKey) throws IOException {
		HTable table = new HTable(conf, Bytes.toBytes(tableName));
		Delete deleteAll = new Delete(Bytes.toBytes(rowKey));
		table.delete(deleteAll);
		System.out.println("all columns are deleted!");
	}

	/*
	 * 删除表
	 * 
	 * @tableName 表名
	 */
	public static void deleteTable(String tableName) throws IOException {
		HBaseAdmin admin = new HBaseAdmin(conf);
		admin.disableTable(tableName);
		admin.deleteTable(tableName);
		System.out.println(tableName + "is deleted!");
	}

	public static void release() {
		try {
			if (attachmentsTable != null) {
				attachmentsTable.close();
			}
			if (connection != null) {
				connection.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("Error closing connections", e);
		}
	}

	public static HBasePageModel getScanByColoumsPageable(String start_rowkey, String stop_rowkey,String rowParams,
			String columnParams,String valueParames,HBasePageModel pageModel,boolean isCount){
		Scan scan = new Scan();
		List<Filter> filterLists = new ArrayList<Filter>();
		if(null != rowParams && !"".equals(rowParams)){
			List<Filter> filterList = new ArrayList<Filter>();
			for (String string : rowParams.split(",")) {
				if(string.equals("")){
					continue;
				}
				filterList.add(new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator(string)));
			}

			FilterList fl = new FilterList(FilterList.Operator.MUST_PASS_ONE, filterList);
			filterLists.add(fl);
		}
		if(null != columnParams && !columnParams.equals("")){
			List<Filter> filters = new ArrayList<Filter>();
			QualifierFilter ff = null;
			for (String string : columnParams.split(",")) {
				ff = new QualifierFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator(string));
				filters.add(ff);
			}
			FilterList fl = new FilterList(FilterList.Operator.MUST_PASS_ONE, filters);
			filterLists.add(fl);
		}
		if(null != valueParames && !valueParames.equals("")){
			List<Filter> filters = new ArrayList<Filter>();
			ValueFilter vf = null;
			for (String string : valueParames.split(",")) {
				vf = new ValueFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator(string));
				filters.add(vf);
			}
			FilterList fl = new FilterList(FilterList.Operator.MUST_PASS_ALL, filters);
			filterLists.add(fl);
		}
		if(isCount){
			PageFilter pageFilter = new PageFilter(pageModel.getNumPerPage() + 1);
			scan.setCaching(pageModel.getNumPerPage()+1);
			scan.setMaxResultSize(pageModel.getNumPerPage()+1);
			filterLists.add(pageFilter);
		}
		if(pageModel.getPageStartRowKey() == null || "".equals(pageModel.getPageStartRowKey())) {
			if(null != start_rowkey && !"".equals(start_rowkey)){
				pageModel.setPageStartRowKey(start_rowkey);
			}
		} else {
			if(!isCount){
				pageModel.setPageStartRowKey(start_rowkey);
			}
			if(pageModel.getPageEndRowKey() != null) {
				pageModel.setPageStartRowKey(pageModel.getPageEndRowKey());
			}
		}
		FilterList fl = new FilterList(FilterList.Operator.MUST_PASS_ALL, filterLists);
		if(null != pageModel.getPageStartRowKey() && !pageModel.getPageStartRowKey().equals("")){
			scan.setStartRow(Bytes.toBytes(pageModel.getPageStartRowKey()));
		}
		if(null != stop_rowkey && !"".equals(stop_rowkey)){
			scan.setStopRow(Bytes.toBytes(stop_rowkey));
		}
		if(fl.getFilters().size() != 0){
			scan.setFilter(fl);
		}
		pageModel.setScan(scan);
		return pageModel;
	}
	public static Scan getResultScanner(String start_rowkey, String stop_rowkey,
			String rowCondition, String columnCondition,String params,int rowNum) {
		Scan scan = new Scan();
		List<Filter> filters = new ArrayList<Filter>();
		if(rowNum != 0){
			PageFilter pageFilter = new PageFilter(rowNum);
			filters.add(pageFilter);
		}
		if(null != rowCondition && !"".equals(rowCondition)){
			List<Filter> filterList = new ArrayList<Filter>();
			for (String string : rowCondition.split(",")) {
				if(string.equals("")){
					continue;
				}
				filterList.add(new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator(string)));
			}
			FilterList fl = new FilterList(FilterList.Operator.MUST_PASS_ONE, filterList);
			filters.add(fl);
		}
		if(!start_rowkey.equals("")){
			scan.setStartRow(Bytes.toBytes(start_rowkey));
		}if(!stop_rowkey.equals("")){
			scan.setStopRow(Bytes.toBytes(stop_rowkey));
		}
		if(StringUtils.isNotEmpty(params)){
			ValueFilter vf = new ValueFilter(CompareOp.EQUAL,new RegexStringComparator (params));
			filters.add(vf);
		}

		if(null != columnCondition){
			List<Filter> filterList = new ArrayList<Filter>();
			if(columnCondition.contains(",")){
				String[] param = columnCondition.split(",");
				for (String string : param) {
					filterList.add(new QualifierFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator(string)));
				}
			}else{
				filterList.add(new QualifierFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator(columnCondition)));
			}
			FilterList fl = new FilterList(FilterList.Operator.MUST_PASS_ONE, filterList);
			filters.add(fl);
		}

		/*QualifierFilter ff = new QualifierFilter(CompareFilter.CompareOp.EQUAL,
				new SubstringComparator(params));*/
		//filters.add(ff);
		FilterList fl = new FilterList(FilterList.Operator.MUST_PASS_ALL, filters);
		scan.setFilter(fl);
		/*if (attachmentsTable == null) {
			attachmentsTable = connection.getTable(TABLE_NAME);
		}
		ResultScanner rs = attachmentsTable.getScanner(scan);
		return rs;*/
		return scan;
	}

	/**
	 * 检索指定表的第一行记录。<br>
	 * （如果在创建表时为此表指定了非默认的命名空间，则需拼写上命名空间名称，格式为【namespace:tablename】）。
	 * @param tableName 表名称(*)。
	 * @param filterList 过滤器集合，可以为null。
	 * @return
	 */
//	public static Result selectFirstResultRow(FilterList filterList) {
//		HTable table = null;
//		try {
//			table = new HTable(conf, Bytes.toBytes(TABLE_NAME));// 获取表
//			Scan scan = new Scan();
//			if(filterList != null) {
//				scan.setFilter(filterList);
//			}
//			ResultScanner scanner = table.getScanner(scan);
//			Iterator<Result> iterator = scanner.iterator();
//			int index = 0;
//			while(iterator.hasNext()) {
//				Result rs = iterator.next();
//				if(index == 0) {
//					scanner.close();
//					return rs;
//				}
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				table.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		return null;
//	}
	
	/**
	 * 根据工厂/产线/设备，获取HBSE的数据
	 * 
	 */
	public Double getDataCountByHbase(Long factoryId,
			Long productLineId, Long driverId, Long productId,
			Long produceId, Date startTime, Date stopTime) {
		Double rsCount = 0D;
        String factoryIdInput = String.valueOf(factoryId == null? "" : factoryId);
        String productLineIdInput = String.valueOf(productLineId == null? "" : productLineId);
        String driverIdInput = String.valueOf(driverId == null? "" : driverId);
        String productIdInput = String.valueOf(productId == null? "" : productId);
        String produceIdInput = String.valueOf(produceId == null? "" : produceId);
        List<String> totalResult = htemplate.execute(HBASE_TABLE_PROCEDURE, new TableCallback<List<String>>() {
            List<String> list = new ArrayList<>();
            @Override
            public List<String> doInTable(HTableInterface table) throws Throwable {
                // 过滤器的添加
                Scan scan = new Scan();
                // 时间区间确定
                if(null != startTime && null != stopTime) {
                    scan.setTimeRange(startTime.getTime(), stopTime.getTime());
                }
                List<Filter> filters = new ArrayList<>();
                // rowKey前匹配
                StringBuffer regexStrBur = new StringBuffer();
                regexStrBur.append("^");
                regexStrBur.append(StringUtils.isNotBlank(factoryIdInput)? factoryIdInput + "_" : ".*_");
                regexStrBur.append(StringUtils.isNotBlank(productLineIdInput)? productLineIdInput + "_" : ".*_");
                regexStrBur.append(StringUtils.isNotBlank(driverIdInput)? driverIdInput + "_" : ".*_");
                regexStrBur.append(StringUtils.isNotBlank(productIdInput)? productIdInput + "_" : ".*_");
                regexStrBur.append(StringUtils.isNotBlank(produceIdInput)? produceIdInput + "_" : ".*_");
                regexStrBur.append(".*");
                Filter filter2 = new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator(regexStrBur.toString()));
                filters.add(filter2);
                FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL, filters);
                scan.setFilter(filterList);
                ResultScanner rscanner = table.getScanner(scan);
                Map<String, String> rsMap = null;
                for (Result result : rscanner) {
                    rsMap = getRowByResult(result);
                    list.add(rsMap.get("rowkey"));
                }

                return list;
            }
        });
        if(null != totalResult && 0 != totalResult.size())
        rsCount = Double.valueOf(totalResult.size());
        return rsCount;
	}
	/**
	 * 根据工厂/产线/设备，获取HBSE的数据
	 * @param factoryId 工厂ID
	 * @param productLineId 产线ID
	 * @param driverId 设备ID
	 * @param productId 产品ID
	 * @param produceId 工序ID
	 * @param snIdInput 工件ID
	 * @param startTime 起始时间
	 * @param stopTime 终止时间
	 * @param timeLimitFlg 时间控制flg
	 * @return
	 */
	public List<Result> getResultListByHbase(Long factoryId,
			Long productLineId, Long driverId, Long productId,
			Long produceId, String snIdInput, Date startTime, Date stopTime, Boolean timeLimitFlg) {
        String factoryIdInput = String.valueOf(factoryId == null? "" : factoryId);
        String productLineIdInput = String.valueOf(productLineId == null? "" : productLineId);
        String driverIdInput = String.valueOf(driverId == null? "" : driverId);
        String productIdInput = String.valueOf(productId == null? "" : productId);
        String produceIdInput = String.valueOf(produceId == null? "" : produceId);
        List<Result> totalResult = htemplate.execute(HBASE_TABLE_PROCEDURE, new TableCallback<List<Result>>() {
            List<Result> listIn = new ArrayList<>();
            @Override
            public List<Result> doInTable(HTableInterface table) throws Throwable {
                int day = 0;
                while(0 >= listIn.size()) {
                	if(3 == day) {
                        break;
                	}
                    // 过滤器的添加
                    Scan scan = new Scan();
                    // scan的时间区间设置
                    setScanRange(scan, startTime, stopTime, day, timeLimitFlg);
                    List<Filter> filters = new ArrayList<>();
                    // rowKey前匹配
                    StringBuffer regexStrBur = new StringBuffer();
                    regexStrBur.append("^");
                    regexStrBur.append(StringUtils.isNotBlank(factoryIdInput)? factoryIdInput + "_" : ".*_");
                    regexStrBur.append(StringUtils.isNotBlank(productLineIdInput)? productLineIdInput + "_" : ".*_");
                    regexStrBur.append(StringUtils.isNotBlank(driverIdInput)? driverIdInput + "_" : ".*_");
                    regexStrBur.append(StringUtils.isNotBlank(productIdInput)? productIdInput + "_" : ".*_");
                    regexStrBur.append(StringUtils.isNotBlank(produceIdInput)? produceIdInput + "_" : ".*_");
                    regexStrBur.append(".*_");
                    regexStrBur.append(StringUtils.isNotBlank(snIdInput)? snIdInput : ".*");
                    Filter filter2 = new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator(regexStrBur.toString()));
                    System.out.println("执行条件：" + regexStrBur.toString());
                    filters.add(filter2);
                    FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL, filters);
                    scan.setFilter(filterList);
                    scan.setMaxResultSize(Long.valueOf("2000"));
                    ResultScanner rscanner = table.getScanner(scan);
                    for (Result result : rscanner) {
                    	listIn.add(result);
                    }
                    if(null != listIn) {
                        System.out.println("搜索件数：" + listIn.size());
                    }

                    if(timeLimitFlg) {
                        day++;
                    } else {
                    	break;
                    }

            	}
                return listIn;
            }
        });

        return totalResult;
	}
	
	/**
	 * 设置scan的时间区间
	 * @param scan
	 * @param startTime
	 * @param stopTime
	 * @param times
	 * @throws IOException
	 */
	private void setScanRange(Scan scan, Date startTime, Date stopTime, int times, Boolean timeLimitFlg) throws IOException {
        // 时间区间确定
        if(null != startTime && null != stopTime) {
              // 在选择时间的情况下，默认8小时以内进行检索。
//            int passHour = (int)(stopTime.getTime() - startTime.getTime())/ (60*60*1000);
//            if(8 < passHour) {
//                Calendar calendarStart = Calendar.getInstance();
//                calendarStart.setTime(startTime);
//                calendarStart.add(Calendar.HOUR, times == 0? 0: times * 1);
//                Calendar calendarEnd = Calendar.getInstance();
//                calendarEnd.setTime(startTime);
//                calendarEnd.add(Calendar.HOUR, 8 * (times + 1));
//                if(0 == times) {
//                    scan.setTimeRange(startTime.getTime(), calendarEnd.getTimeInMillis());
//                } else {
//                    int passMinite = (int)(stopTime.getTime() - calendarEnd.getTimeInMillis())/ (60*1000);
//                    if(0 > passMinite) {
//                        int passMinite2 = (int)(calendarStart.getTimeInMillis() - stopTime.getTime())/ (60*1000);
//                        if(0 < passMinite2) {
//                            scan.setTimeRange(calendarStart.getTimeInMillis(), stopTime.getTime());
//                        }
//                    } else {
//                        scan.setTimeRange(calendarStart.getTimeInMillis(), calendarEnd.getTimeInMillis());
//                    }
//                }
//
//            } else {
                scan.setTimeRange(startTime.getTime(), stopTime.getTime());
//            }

        } else if(null == startTime && null == stopTime) {
        	if(timeLimitFlg) {
                Calendar calendarStart = Calendar.getInstance();
                calendarStart.setTime(new Date());
                calendarStart.add(Calendar.HOUR, -8 * (times + 1));

                Calendar calendarEnd = Calendar.getInstance();
                calendarEnd.setTime(new Date());
                calendarEnd.add(Calendar.HOUR, times == 0? 0: times * 1);
                scan.setTimeRange(calendarStart.getTimeInMillis(), calendarEnd.getTimeInMillis());
        	}

        }
	}
	
	/**
	 * 根据工厂/产线/设备，获取HBSE的数据
	 * 
	 */
	public List<Result> getResultListByHbaseOfRowKey(String rowKey, Long timestamps) {
//		Double rsCount = 0D;
        List<Result> totalResult = htemplate.execute(HBASE_TABLE_PROCEDURE, new TableCallback<List<Result>>() {
            List<Result> listIn = new ArrayList<>();
            @Override
            public List<Result> doInTable(HTableInterface table) throws Throwable {
                // 过滤器的添加
                Scan scan = new Scan();
                scan.setTimeStamp(timestamps);
                ResultScanner rscanner = table.getScanner(scan);
                for (Result result : rscanner) {
                	listIn.add(result);
                }

                return listIn;
            }
        });

        return totalResult;
	}
	/**
	 * 获取同一个rowkey下的记录集合
	 * 
	 * @param result
	 *            结果集
	 * @return
	 */
	public static Map<String, String> getRowByResult(Result result) {
		if (result == null) {
			return null;
		}
//		System.out.println("---------------start---------------------------------------------------------------------------------------------------------------");
		Map<String, String> cellMap = new HashMap<String, String>();
		for (Cell cell : result.listCells()) {
			String rowkey = Bytes.toString(cell.getRowArray(),
					cell.getRowOffset(), cell.getRowLength());
			String cf = Bytes.toString(cell.getFamilyArray(),
					cell.getFamilyOffset(), cell.getFamilyLength());
			String qf = Bytes.toString(cell.getQualifierArray(),
					cell.getQualifierOffset(), cell.getQualifierLength());
			String value = Bytes.toString(cell.getValueArray(),
					cell.getValueOffset(), cell.getValueLength());
//			System.out.println("-------------rowkey--------------" + rowkey);
//			System.out.println("----------------cf-----------" + cf);
//			System.out.println("-----------------qf----------" + qf);
//			System.out.println("----------------value-----------" + value);
//			cellMap.put(CpConstants.HBASE_TABLE_PROP_ROWKEY, rowkey);
//			cellMap.put(CpConstants.HBASE_TABLE_PROP_COLUMNFAMILY, cf);
			cellMap.put("rowkey", rowkey);
			cellMap.put(qf, value);
		}
//		System.out.println("---------------end---------------------------------------------------------------------------------------------------------------");
		return cellMap;
	}
	
    /**
     * 设置HBASE搜索时间范围
     * @param scan
     * @param hBasePageModel
     */
    public Map<String, Date> getScanTimeRangeByHBasePageModel(HBasePageModel hBasePageModel) {
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
     * 生产记录画面
     *  对HBASE的SCAN工具进行时间设置。
     * @param scan HBAE搜索工具
     * @param hBasePageModel 画面中的分页元素和搜索条件元素
     * @param times 当画面不选时间的时候，进行多次的，以8小时为单位的重复数据抽取
     */
    public static void setScanTimeRange(Scan scan, HBasePageModel hBasePageModel, int times) {
    	Date begin = hBasePageModel.getAnalyzeSearch().getBegin();
    	Date end = hBasePageModel.getAnalyzeSearch().getEnd();
		try {
			if (null != begin && null != end) {
				scan.setTimeRange(begin.getTime(), end.getTime());
			} else if (null == end && null != begin) {
				Date curretTime = new Date();
				scan.setTimeRange(begin.getTime(), curretTime.getTime());
			} else if (null == begin && null != end) {
				scan.setTimeRange(Long.valueOf("0"), end.getTime());
			} else if(null == begin && null == end) {
	            Calendar calendarStart = Calendar.getInstance();
	            calendarStart.setTime(new Date());
	            calendarStart.add(Calendar.HOUR, -8 * (times + 1));

	            Calendar calendarEnd = Calendar.getInstance();
	            calendarEnd.setTime(new Date());
	            calendarEnd.add(Calendar.HOUR, times == 0? 0: times * 1);
	            scan.setTimeRange(calendarStart.getTimeInMillis(), calendarEnd.getTimeInMillis());
	        }
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	

    /**
     * 根据工厂/产线/设备，获取HBSE的数据
     * @param factoryId 工厂ID
     * @param productLineId 产线ID
     * @param driverId 设备ID
     * @param productId 产品ID
     * @param produceId 工序ID
     * @param snIdInput 工件ID
     * @param startTime 起始时间
     * @param stopTime 终止时间
     * @param timeLimitFlg 时间控制flg
     * @return
     */
    public List<Result> getSpcDateByHbaseStopRow(Long factoryId,
            Long productLineId, Long driverId, Long productId,
            Long produceId, String snIdInput, Date startTime, Date stopTime, Boolean timeLimitFlg
            ,String startRowkey
            ,String stopRowykey) {
        String factoryIdInput = String.valueOf(factoryId == null? "" : factoryId);
        String productLineIdInput = String.valueOf(productLineId == null? "" : productLineId);
        String driverIdInput = String.valueOf(driverId == null? "" : driverId);
        String productIdInput = String.valueOf(productId == null? "" : productId);
        String produceIdInput = String.valueOf(produceId == null? "" : produceId);
        String startK = startRowkey;
        String stopK = stopRowykey;
        List<Result> totalResult = htemplate.execute(HBASE_TABLE_PROCEDURE, new TableCallback<List<Result>>() {
            List<Result> listIn = new ArrayList<>();
            @Override
            public List<Result> doInTable(HTableInterface table) throws Throwable {
                int day = 0;
                while(0 >= listIn.size()) {
                    if(3 == day) {
                        break;
                    }
                    // 过滤器的添加
                    Scan scan = new Scan();
                    // scan的时间区间设置
                    setScanRange(scan, startTime, stopTime, day, timeLimitFlg);
                    List<Filter> filters = new ArrayList<>();
                    // rowKey前匹配
                    StringBuffer regexStrBur = new StringBuffer();
                    regexStrBur.append("^");
                    regexStrBur.append(StringUtils.isNotBlank(factoryIdInput)? factoryIdInput + "_" : ".*_");
                    regexStrBur.append(StringUtils.isNotBlank(productLineIdInput)? productLineIdInput + "_" : ".*_");
                    regexStrBur.append(StringUtils.isNotBlank(driverIdInput)? driverIdInput + "_" : ".*_");
                    regexStrBur.append(StringUtils.isNotBlank(productIdInput)? productIdInput + "_" : ".*_");
                    regexStrBur.append(StringUtils.isNotBlank(produceIdInput)? produceIdInput + "_" : ".*_");
                    regexStrBur.append(".*_");
                    regexStrBur.append(StringUtils.isNotBlank(snIdInput)? snIdInput : ".*");
                    Filter filter2 = new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator(regexStrBur.toString()));
                    System.out.println("执行条件：" + regexStrBur.toString());
                    filters.add(filter2);
                    FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL, filters);
                    scan.setFilter(filterList);
                    scan.setMaxResultSize(Long.valueOf("2000"));
                    if(StringUtils.isNotBlank(startRowkey))
                    scan.setStartRow(startK.getBytes());
                    if(StringUtils.isNotBlank(stopRowykey))
                    scan.setStopRow(stopK.getBytes());
                    ResultScanner rscanner = table.getScanner(scan);
                    for (Result result : rscanner) {
                        listIn.add(result);
                    }
                    if(null != listIn) {
                        System.out.println("搜索件数：" + listIn.size());
                    }

                    if(timeLimitFlg) {
                        day++;
                    } else {
                        break;
                    }

                }
                return listIn;
            }
        });

        return totalResult;
    }
	
	public static void main(String[] args) throws IOException {
		//HbaseUtil.connectHbase();
//		getResultScann(TABLE_NAME);
		/**
		 * row:14821299700200-14-97-11-7D-11
family:cf
qualifier:5303:75:73
value:9:BN0000113:5303:45.99:96:1482129970
timestamp:1482573164412
		 */
		/*HBasePageModel pageModel = new HBasePageModel(10);
		pageModel = getScanByColoumsPageable("1482021170", "1482129979", "5303", "", pageModel);
		if(pageModel.getResultList().size() == 0) {
		    //本页没有数据，说明已经是最后一页了。
		    return;
		}*/
	}
}