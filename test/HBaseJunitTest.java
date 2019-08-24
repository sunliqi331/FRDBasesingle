import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.BinaryPrefixComparator;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.FamilyFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.MultipleColumnPrefixFilter;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.RowMapper;
import org.springframework.data.hadoop.hbase.TableCallback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.its.common.service.RedisService;
import com.its.frd.dao.MesPointsDao;
import com.its.frd.dao.MesProcedurePropertyPointLogDao;
import com.its.frd.schedule.ProductionScheduler;
import com.its.frd.service.CompanyinfoService;
import com.its.frd.service.MesPointGatewayService;
import com.its.frd.service.MesSpcMonitorService;
import com.its.frd.util.HBaseUtilNew;
import com.its.monitor.service.MesPointsTemplateService;
import com.its.statistics.service.StatisticsService;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = 
{"classpath:applicationContext-test.xml",
        "classpath:applicationContext-shiro-cas.xml",
        "classpath:redis/applicationContext-redis.xml",
        "classpath:hadoop/applicationContext-hadoop.xml"})
@Transactional
public class HBaseJunitTest { 
    @Autowired
    private StatisticsService statisticsService;
    @Autowired
    private MesPointsDao mesPointsDao;
    @Autowired
    private MesProcedurePropertyPointLogDao mesProcedurePropertyPointLogDao;
    @Autowired
    private MesSpcMonitorService mesSpcMonitorService;
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    @Autowired
    private MesPointsTemplateService mesPointsTemplateService;
    @Autowired
    private HbaseTemplate hTemplate;
    
    @Resource(name="redisService")
    private RedisService redisService;
    @Autowired
    private CompanyinfoService companyinfoService;
    @Autowired
    private ProductionScheduler productionScheduler;
    @Autowired
    private MesPointGatewayService mesPointGatewayService;
    @PersistenceContext
    private EntityManager entityManager;
    
    @Before
    public void before() {
    	System.setProperty("hadoop.home.dir", "F:\\hadoop-common-2.2.0-bin-master");
    }
    
    @Test
    public void testDriverPropertyValues() {
        try {
            Scan scan = new Scan();
            scan.setTimeRange(Long.valueOf("1513699200000"), Long.valueOf("1513785599000"));
            //            scan.setTimeRange(Long.valueOf("1513759615"), Long.valueOf("1513785599000"));
            scan.setBatch(10);
            List<Filter> filters = new ArrayList<>();
            String factoryId = "516";
            String produceId = "242";
            String driverId = "358";
            Filter filterPreFixFilter = new PrefixFilter(Bytes.toBytes(factoryId + "_" + produceId + "_" + driverId));
            filters.add(filterPreFixFilter);
            // 表中存在以property打头的列族，过滤结果为该列族所有行
            FamilyFilter familyFilter = new FamilyFilter(CompareFilter.CompareOp.EQUAL,
                    new BinaryPrefixComparator(Bytes.toBytes("property")));
            filters.add(familyFilter);
            byte[][] prefixes = new byte[][] { Bytes.toBytes("262") };
            MultipleColumnPrefixFilter multipleColumnPrefixFilter = new MultipleColumnPrefixFilter(prefixes);
            filters.add(multipleColumnPrefixFilter);
            Filter pageFilter = new PageFilter(10); 
            filters.add(pageFilter);
            FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL, filters);
            scan.setFilter(filterList);
            // ColumnPrefixFilter filter = new ColumnPrefixFilter(Bytes.toBytes("263"));
            // filters.add(filter);
            // FamilyFilter familyFilter = new FamilyFilter(CompareOp.EQUAL , new
            // BinaryComparator(Bytes.toBytes("property")));
            // filters.add(familyFilter);

            // QualifierFilter filter1 = new QualifierFilter(CompareOp.EQUAL, new
            // BinaryComparator(Bytes.toBytes("timestamp")));
            // filters.add(filter1);
            // QualifierFilter ff = new QualifierFilter(
            // CompareFilter.CompareOp.EQUAL ,
            // new BinaryComparator(Bytes.toBytes("belong")) //表中不存在belong列，过滤结果为空
            // );
            // filters.add(ff);
            // ColumnPrefixFilter filterColumnPrefixFilter = new
            // ColumnPrefixFilter(Bytes.toBytes("property"));
            // filters.add(filterPreFixFilter);
            // ColumnRangeFilter filter = new ColumnRangeFilter(Bytes.toBytes("minColumn"),
            // true, Bytes.toBytes("maxColumn"), true);
            // filters.add(filterColumnPrefixFilter);
            // SingleColumnValueFilter filter = new SingleColumnValueFilter(
            // Bytes.toBytes("user"), Bytes.toBytes("name"), CompareOp.EQUAL, new
            // SubstringComparator(factoryId + "_" + produceId + "_" + driverId));
            // filter.setFilterIfMissing(false);// 默认情况下为false

            // Filter filter1 = new ValueFilter(CompareOp.EQUAL, new
            // BinaryPrefixComparator(Bytes.toBytes(9)));
            // filters.add(filter1);
            // String columnCondition =
            // "^((?!:).)+:((?!0).)+:((?!0).)+:((?!:).)+:((?!:).)+$";
            // Filter filter2 = new QualifierFilter(CompareOp.EQUAL, new
            // RegexStringComparator(columnCondition));
            // filters.add(filter2);
            // FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL,
            // filters);
            // FilterList filterList = new FilterList();
            // filterList.addFilter(filterPreFixFilter);
            // filterList.addFilter(familyFilter);
            // filterList.addFilter(multipleColumnPrefixFilter);
            // scan.setFilter(filterList);

            // List<Filter> filtersProperty = new ArrayList<>();
            // ColumnPrefixFilter filter = new ColumnPrefixFilter(Bytes.toBytes("263"));
            // filtersProperty.add(filter);
            // ColumnPrefixFilter filter1 = new ColumnPrefixFilter(Bytes.toBytes("267"));
            // filtersProperty.add(filter1);
            // ColumnPrefixFilter filter2 = new ColumnPrefixFilter(Bytes.toBytes("268"));
            // filtersProperty.add(filter2);
            // FilterList filterListProperty = new
            // FilterList(FilterList.Operator.MUST_PASS_ONE, filtersProperty);

            // scan.setFilter(filterList);
            List<Map<String, String>> list = hTemplate.find("driverPropertyAndStatus", scan,
                    new RowMapper<Map<String, String>>() {
                        @Override
                        public Map<String, String> mapRow(Result result, int arg1) throws Exception {
                            List<Cell> ceList = result.listCells();
                            Map<String, String> map = new HashMap<>();
                            if (ceList != null && ceList.size() > 0) {
                                for (Cell cell : ceList) {
                                    // System.out.println(Bytes.toString(cell.getRowArray()));
                                    String row = Bytes.toString(cell.getRowArray(), cell.getRowOffset(),
                                            cell.getRowLength());
                                    System.out.println("-------row-pre------" + row);
                                    String value = Bytes.toString(cell.getValueArray(), cell.getValueOffset(),
                                            cell.getValueLength());
                                    System.out.println("-------value------" + value);
                                    String quali = Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(),
                                            cell.getQualifierLength());
                                    System.out.println("-------quali------" + quali);

                                    System.out.println("------------cell.getFamilyArray()-------------"
                                            + Bytes.toString(cell.getFamilyArray()));
                                    // String gateway = row.split(":")[0].substring(12);
                                    // String productLineId = row.split(":")[2];
                                    // String pointKey = quali.split(":")[0];
                                    // String propertyId = quali.split(":")[1];
                                    // map.put(gateway+":"+pointKey+"-"+propertyId+"-"+productLineId,
                                    // value.split(":")[3]+"-"+value.substring(value.lastIndexOf(":")+1));
                                }
                            }
                            return map;
                        }
                    });
            System.out.println(list.size());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    @Test
    public void testValueSubString() {
    	byte[][] prefixes = new byte[][] { Bytes.toBytes("263"), Bytes.toBytes("265"), Bytes.toBytes("267") };
    	byte[][] prefixesTest = new byte[2][];
    	prefixesTest[0] = Bytes.toBytes(263);
    	List<Long> list = Lists.newArrayList();
    	list.add(Long.valueOf(263));
    	list.add(Long.valueOf(265));
    	list.add(Long.valueOf(267));
    	byte[][] test = new byte[list.size()][];
    	for(int i=0;i<list.size();i++) {
    		test[i] = Bytes.toBytes(list.get(i).toString());
    		System.out.println(list.get(i));
    	}
    	String value = "38.01_OK+9:BN0019986:5304:38.01:97:1513663496";
    	System.out.println(value.substring(0, value.indexOf("_")));
    	System.out.println(value.substring(value.indexOf("_") + 1, value.indexOf("+")));
    	System.out.println(value.substring(value.indexOf("+") - 1).split(":")[5]);
    	
    	String quali = "263_1513663452";
    	System.out.println(quali.substring(0, quali.indexOf("_")));
    	

    }

    @Test
    public void testHBaseUtilNew() {
       Date current = new Date();
       System.out.println(current.getTime());
       
    }

    @Test
    public void testPageFilter() {
    	System.out.println("--------------testPageFilter----------------start-------");
    	List<String> rowkeys = Lists.newArrayList();
    	rowkeys.add("516_242_358_1513827165");
    	List<Result> res = HBaseUtilNew.getDatasFromHbase("driverPropertyAndStatus", "property", rowkeys, null, false, false);
    	for(Result rs : res) {
    		getRowByResult(rs);
    	}
    	System.out.println("--------------testPageFilter-----------------end------");
    }
    
	/**
	 * 获取同一个rowkey下的记录集合
	 * 
	 * @param result
	 *            结果集
	 * @return
	 */
	private Map<String, String> getRowByResult(Result result) {
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
			System.out.println("----------------cf-----------" + cf);
			System.out.println("-----------------qf----------" + qf);
//			System.out.println("----------------value-----------" + value);
//			cellMap.put(CpConstants.HBASE_TABLE_PROP_ROWKEY, rowkey);
//			cellMap.put(CpConstants.HBASE_TABLE_PROP_COLUMNFAMILY, cf);
			cellMap.put("rowkey", rowkey);
			cellMap.put(qf, value);
		}
//		System.out.println("---------------end---------------------------------------------------------------------------------------------------------------");
		return cellMap;
	}
	
	
    @Test
    public void testPageFilter20171225() {
    	System.setProperty("hadoop.home.dir", "F:\\hadoop-common-2.2.0-bin-master");
    	List<Result> outList = hTemplate.execute("driverPropertyAndStatus", new TableCallback<List<Result>>() {
            List<Result> list = new ArrayList<>();
            @Override
            public List<Result> doInTable(HTableInterface table) throws Throwable {
                // 过滤器的添加
                Scan scan = new Scan();
                scan.setTimeRange(Long.valueOf("516_242_358_1514131200"), Long.valueOf("1514217599000"));
                //scan.setStartRow(Bytes.toBytes("516_242_358_1514217599000"));
                //scan.setStopRow(Bytes.toBytes("516_242_358_1514131200"));

                List<Filter> filters = new ArrayList<>();
//                String lastrowKey = "516_242_358_1514217599000";
//                Filter rowFilterMax = new RowFilter(CompareFilter.CompareOp.LESS,
//                        new BinaryComparator(Bytes.toBytes(lastrowKey)));  
//                filters.add(rowFilterMax);

//                String startRowKey = "516_242_358_1514131200";
//                Filter rowFilterMin = new RowFilter(CompareFilter.CompareOp.GREATER,
//                        new BinaryComparator(Bytes.toBytes(startRowKey)));  
//                filters.add(rowFilterMin);

                // 表中存在以property打头的列族，过滤结果为该列族所有行
                FamilyFilter familyFilter = new FamilyFilter(CompareFilter.CompareOp.EQUAL,
                        new BinaryPrefixComparator(Bytes.toBytes("property")));
                filters.add(familyFilter);

                List<Long> driverPropertyIds = Lists.newArrayList();
                driverPropertyIds.add(Long.valueOf("262"));
                byte[][] prefixes = new byte[driverPropertyIds.size()][];
                for(int i = 0;i<driverPropertyIds.size();i++) {
                    prefixes[i] = Bytes.toBytes(String.valueOf(driverPropertyIds.get(i)));
                }
                MultipleColumnPrefixFilter multipleColumnPrefixFilter = new MultipleColumnPrefixFilter(prefixes);
                filters.add(multipleColumnPrefixFilter);

//                Filter pageFilter = new PageFilter(10); 
//                filters.add(pageFilter);

                FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL, filters);
                scan.setFilter(filterList);
                ResultScanner rscanner = table.getScanner(scan);
                for(Result result : rscanner){
                	getRowByResult(result);
                    list.add(result);
                }
                return list;
            }
        });
    	
    }

    /**
     * 获取时间范围的
     */
    @Test
    public void testPageFilter20171225_01_设备属性历史记录() {
    	System.setProperty("hadoop.home.dir", "F:\\hadoop-common-2.2.0-bin-master");
    	List<String> outList = hTemplate.execute("driverPropertyAndStatus", new TableCallback<List<String>>() {
            List<Result> list = new ArrayList<>();
            @Override
            public List<String> doInTable(HTableInterface table) throws Throwable {
                // 过滤器的添加
                Scan scan = new Scan();
                scan.setTimeRange(Long.valueOf("1514131200"), Long.valueOf("1514217599000"));
                //scan.setStartRow(Bytes.toBytes("516_242_358_1514217599000"));
                //scan.setStopRow(Bytes.toBytes("516_242_358_1514131200"));
                String factoryId = "516";
                String produceId = "242";
                String driverId = "358";
                List<Filter> filters = new ArrayList<>();
                Filter filterPreFixFilter = new PrefixFilter(Bytes.toBytes(factoryId + "_" + produceId + "_" + driverId));
                filters.add(filterPreFixFilter);
//                String lastrowKey = "516_242_358_1514217599000";
//                Filter rowFilterMax = new RowFilter(CompareFilter.CompareOp.LESS,
//                        new BinaryComparator(Bytes.toBytes(lastrowKey)));  
//                filters.add(rowFilterMax);

//                String startRowKey = "516_242_358_1514131200";
//                Filter rowFilterMin = new RowFilter(CompareFilter.CompareOp.GREATER,
//                        new BinaryComparator(Bytes.toBytes(startRowKey)));  
//                filters.add(rowFilterMin);

                // 表中存在以property打头的列族，过滤结果为该列族所有行
                FamilyFilter familyFilter = new FamilyFilter(CompareFilter.CompareOp.EQUAL,
                        new BinaryPrefixComparator(Bytes.toBytes("property")));
                filters.add(familyFilter);

                List<Long> driverPropertyIds = Lists.newArrayList();
                driverPropertyIds.add(Long.valueOf("262"));
                byte[][] prefixes = new byte[driverPropertyIds.size()][];
                for(int i = 0;i<driverPropertyIds.size();i++) {
                    prefixes[i] = Bytes.toBytes(String.valueOf(driverPropertyIds.get(i)));
                }
                MultipleColumnPrefixFilter multipleColumnPrefixFilter = new MultipleColumnPrefixFilter(prefixes);
                filters.add(multipleColumnPrefixFilter);

//                Filter pageFilter = new PageFilter(10); 
//                filters.add(pageFilter);

                FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL, filters);
                scan.setFilter(filterList);
                ResultScanner rscanner = table.getScanner(scan);
                List<String> outListRs = Lists.newArrayList();
                for(Result result : rscanner){
                    list.add(result);
                	Map<String, String> rsMap = getRowByResult(result);
                	outListRs.add(rsMap.get("rowkey"));
                }
                return outListRs;
            }
        });
        List<String> partRowKeys = outList.subList(outList.size() - 10, outList.size());
        Collections.reverse(partRowKeys);
        
    	List<Result> res = HBaseUtilNew.getDatasFromHbase("driverPropertyAndStatus", "property", partRowKeys, null, false, false);

    	for(Result resa : res) {
    		getRowByResult20171225_01(resa);
    	}
    }

    /**
     * 获取时间范围的
     */
    @Test
	//    public void testPageFilter20171225_01_设备属性产量() {
	//    	System.setProperty("hadoop.home.dir", "F:\\hadoop-common-2.2.0-bin-master");
	//    	List<String> outList = hTemplate.execute("driverPropertyAndStatus", new TableCallback<List<String>>() {
	//            List<Result> list = new ArrayList<>();
	//            @Override
	//            public List<String> doInTable(HTableInterface table) throws Throwable {
	//                // 过滤器的添加
	//                Scan scan = new Scan();
	//                // 时间区间确定
	//                scan.setTimeRange(start.getTime(), end.getTime());
	//                List<Filter> filters = new ArrayList<>();
	//                // rowKey前匹配
	//                Filter filterPreFixFilter = new PrefixFilter(Bytes.toBytes(factoryId + "_" + productLineId + "_" + driverId + "_"));
	//                filters.add(filterPreFixFilter);
	//
	//                // HBase's familiy : 设备属性关键字
	//                FamilyFilter familyFilter = new FamilyFilter(CompareFilter.CompareOp.EQUAL,
	//                        new BinaryPrefixComparator(Bytes.toBytes("count")));
	//                filters.add(familyFilter);
	//
	//                FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL, filters);
	//                scan.setFilter(filterList);
	//                ResultScanner rscanner = table.getScanner(scan);
	//                Map<String, String> rsMap = null;
	//                for(Result result : rscanner){
	//                	rsMap = getRowByResult(result);
	//                	list.add(rsMap.get("rowkey"));
	//                }
	//                return list;
	//            }
	//        });
	//        List<String> partRowKeys = outList.subList(outList.size() - 10, outList.size());
	//        Collections.reverse(partRowKeys);
	//        
	//    	List<Result> res = HBaseUtilNew.getDatasFromHbase("driverPropertyAndStatus", "property", partRowKeys, null, false, false);
	//
	//    	for(Result resa : res) {
	//    		getRowByResult20171225_01(resa);
	//    	}
	//    }

	/**
	 * 获取同一个rowkey下的记录集合
	 * 
	 * @param result
	 *            结果集
	 * @return
	 */
	private Map<String, String> getRowByResult20171225_01(Result result) {
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
			System.out.println("-------------rowkey--------------" + rowkey);
			System.out.println("----------------cf-----------" + cf);
			System.out.println("-----------------qf----------" + qf);
			System.out.println("----------------value-----------" + value);
//			cellMap.put(CpConstants.HBASE_TABLE_PROP_ROWKEY, rowkey);
//			cellMap.put(CpConstants.HBASE_TABLE_PROP_COLUMNFAMILY, cf);
			cellMap.put("rowkey", rowkey);
			cellMap.put(qf, value);
		}
//		System.out.println("---------------end---------------------------------------------------------------------------------------------------------------");
		return cellMap;
	}
	
    /**
     * 获取时间范围的
     */
    @Test
    public void testSubString() {
//    "516_242_358_1514181725                          column=count:5316, timestamp=1514181725000, value=1"
    String testSt = "516_242_358_1514181725";
    System.out.println(testSt.split("_")[3]);
    }
    
  /**
   * HBASE共同方法
   */
//	@Override
//	public List<DriverRecord> getDriverOutputByHbase(Timestamp start, Timestamp end,
//			String factoryId, String productLineId,Long driverId,HBasePageModel page) {
//		List<String> totalResult = hTemplate.execute("driveCount", new TableCallback<List<String>>() {
//            List<String> list = new ArrayList<>();
//            @Override
//            public List<String> doInTable(HTableInterface table) throws Throwable {
//                // 过滤器的添加
//                Scan scan = new Scan();
//                // 时间区间确定
//                scan.setTimeRange(start.getTime(), end.getTime());
//                List<Filter> filters = new ArrayList<>();
//                // rowKey前匹配
//                Filter filterPreFixFilter = new PrefixFilter(Bytes.toBytes(factoryId + "_" + productLineId + "_" + driverId + "_"));
//                filters.add(filterPreFixFilter);
//
//                // HBase's familiy : 设备属性关键字
//                FamilyFilter familyFilter = new FamilyFilter(CompareFilter.CompareOp.EQUAL,
//                        new BinaryPrefixComparator(Bytes.toBytes("count")));
//                filters.add(familyFilter);
//
//                FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL, filters);
//                scan.setFilter(filterList);
//                ResultScanner rscanner = table.getScanner(scan);
//                Map<String, String> rsMap = null;
//                for(Result result : rscanner){
//                	rsMap = getRowByResult(result);
//                	list.add(rsMap.get("rowkey"));
//                }
//                return list;
//            }
//        });
//		List<DriverRecord> driverRecordList = Lists.newArrayList();
//		if(0 == totalResult.size()) {
//			return driverRecordList;
//		}
//        // 抽取队列的最后10条
//        int pageSelectedCount = totalResult.size() - page.getPageNum() * 10;
//        List<String> partRowKeys = totalResult.subList(pageSelectedCount - 10, pageSelectedCount);
//        Collections.reverse(partRowKeys);
//    	List<Result> res = HBaseUtilNew.getDatasFromHbase("driveCount", "count", partRowKeys, null, false, false);
//
//    	page.setTotalCount(totalResult.size());
//		int totalPage = Integer.valueOf(String.valueOf(page.getTotalCount()))/10;
//		page.setTotalPage(totalPage);
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        MesDriver driver = dao.findOne(driverId);
//        List<Map<String, String>> listOut = Lists.newArrayList();
//
//        DriverRecord driverRecord = null;
//        for (Result result : res) {
//            List<Cell> ceList = result.listCells();
//            driverRecord = new DriverRecord();
//            driverRecord.setMesDriverName(driver.getName());
//            if (ceList != null && ceList.size() > 0) {
//                for (Cell cell : ceList) {
//                    String row = Bytes.toString(cell.getRowArray(), cell.getRowOffset(),
//                            cell.getRowLength());
//                    String value = Bytes.toString(cell.getValueArray(), cell.getValueOffset(),
//                            cell.getValueLength());
//                    driverRecord.setDriver_count(value);
//                    driverRecord.setDatetime(BigInteger.valueOf(Long.valueOf(row.split("_")[3])));
//                }
//            }
//            driverRecordList.add(driverRecord);
//        }
//		return driverRecordList;
//	}
    
    /**
     * 
     */
    @Test
	public void testPageFilter20171225_01_生产记录() {
		List<String> totalResult = hTemplate.execute("procedure", new TableCallback<List<String>>() {
            List<String> list = new ArrayList<>();
            @Override
            public List<String> doInTable(HTableInterface table) throws Throwable {
                // 过滤器的添加
                Scan scan = new Scan();
                // 时间区间确定
                //scan.setTimeRange(start.getTime(), end.getTime());
                //scan.setTimeRange(Long.valueOf("1514131200"), Long.valueOf("1514217599000"));
                List<Filter> filters = new ArrayList<>();
                // rowKey前匹配
                String factoryId = "549";
                String productLineId = "268";
                // String driverId = "358";
                Filter filterPreFixFilter = new PrefixFilter(Bytes.toBytes(factoryId + "_" + productLineId + "_"));
                filters.add(filterPreFixFilter);
                String productId = "16";
                String produceId = "284";

                Filter filter2 = new RowFilter(CompareFilter.CompareOp.EQUAL, 
                		new RegexStringComparator("^" +factoryId + "_" + productLineId + "_.*_" + productId + "_"  + produceId + "_.*" ));
                filters.add(filter2);

                // HBase's familiy : 设备属性关键字
//                FamilyFilter familyFilter = new FamilyFilter(CompareFilter.CompareOp.EQUAL,
//                        new BinaryPrefixComparator(Bytes.toBytes("cf")));
//                filters.add(familyFilter);
                
//                List<Long> driverPropertyIds = Lists.newArrayList();
//                driverPropertyIds.add(Long.valueOf("262"));
//                byte[][] prefixes = new byte[driverPropertyIds.size()][];
//                for(int i = 0;i<driverPropertyIds.size();i++) {
//                    prefixes[i] = Bytes.toBytes(String.valueOf(driverPropertyIds.get(i)));
//                }
//                MultipleColumnPrefixFilter multipleColumnPrefixFilter = new MultipleColumnPrefixFilter(prefixes);
//                filters.add(multipleColumnPrefixFilter);

                FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL, filters);
                scan.setFilter(filterList);
                ResultScanner rscanner = table.getScanner(scan);
                Map<String, String> rsMap = null;
                for(Result result : rscanner){
                	rsMap = getRowByResult(result);
                	list.add(rsMap.get("rowkey"));
                }
                return list;
            }
        });

    }
}
