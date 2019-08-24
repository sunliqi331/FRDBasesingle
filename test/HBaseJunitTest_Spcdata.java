import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.TableCallback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.its.frd.util.HbaseUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-test.xml",
        "classpath:applicationContext-shiro-cas.xml", "classpath:redis/applicationContext-redis.xml",
        "classpath:hadoop/applicationContext-hadoop.xml" })
@Transactional
public class HBaseJunitTest_Spcdata {

    @Autowired
    private HbaseTemplate hTemplate;

    @PersistenceContext
    private EntityManager entityManager;
    
    @Before
    public void before() {
    	System.setProperty("hadoop.home.dir", "F:\\hadoop-common-2.2.0-bin-master");
    }

    @Test
    public void test() {
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(new Date());
//        calendarStart.add(Calendar.HOUR, 0);

        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTime(new Date());
        calendarEnd.add(Calendar.MINUTE, 12);
        System.out.println((calendarEnd.getTimeInMillis() - calendarStart.getTimeInMillis())/ (60*1000));
    	
    }
    
    @Test
    public void testTmp() throws Exception {
    	Configuration c = new Configuration();
    	c.set("hbase.zookeeper.quorum", "192.168.217.35,192.168.217.44,192.168.217.45");
    	HConnection con = HConnectionManager.createConnection(c);
    	HTableInterface table = con.getTable("procedure".getBytes());
    	Scan scan = new Scan();
    	// starRow = 1515981132
    	// 选择的时间【1515980132】
    	// stopRow = 1515980132
//    	scan.setStartRow(String.valueOf(Long.MAX_VALUE - 1515673194L).getBytes());
//    	scan.setStopRow(String.valueOf(Long.MAX_VALUE - 1515673074L).getBytes());
		Calendar calendarTodayEnd = Calendar.getInstance();
		calendarTodayEnd.setTime(new Date());
		calendarTodayEnd.add(Calendar.MINUTE, -5);

    	scan.setStartRow(String.valueOf(Long.MAX_VALUE - 1515981132L).getBytes());
    	scan.setStopRow(String.valueOf(Long.MAX_VALUE - 1515980132L).getBytes());
    	scan.setMaxResultSize(1L);
    	ResultScanner scanner = table.getScanner(scan);
    	Iterator<Result> iterator = scanner.iterator();
        while(iterator.hasNext()) {
        	Result next = iterator.next();
        	for(Cell cell:next.listCells()) {
        		System.out.println(new String(cell.getRow()));
        	}
        }
        table.close();
        con.close();
    	
    }
    
    
    @Test
    public void testPageFilter20180416_Spc数据获取() {
    	
        List<String> totalResult = hTemplate.execute("procedure", new TableCallback<List<String>>() {
            List<String> list = new ArrayList<>();
            @Override
            public List<String> doInTable(HTableInterface table) throws Throwable {
                // 过滤器的添加
                Scan scan = new Scan();
                // 当前时间的前五分
        		Calendar calendarTodayEnd = Calendar.getInstance();
        		calendarTodayEnd.setTime(new Date());
        		calendarTodayEnd.add(Calendar.MINUTE, -5);
        		
        		// 选择的时间：1515673134 2018/1/11 20:18:54

                // StartRowKey = Long.Max - 页面上的endTime
        		Long startTime = Long.valueOf("1515673074");//[2018/1/11 20:17:54]
                // EndRowKey = Long.Max - 页面上的startTime
                Long stopTime = Long.valueOf("1515673194");//[2018/1/11 20:19:54]
                
        		// 第一种
        		//scan.setStartRow(Bytes.toBytes(Long.MAX_VALUE - stopTime));
        		//scan.setStartRow(Bytes.toBytes(Long.MAX_VALUE - 1515673194L));
                // scan.setStartRow(String.valueOf(Long.MAX_VALUE - 1515673194L).getBytes());
                // 9223372035339102661
                scan.setStartRow("516_242_358_9_235_1521467731_BN0003844".getBytes());
        		// 第一种
                // scan.setStopRow(Bytes.toBytes(Long.MAX_VALUE - startTime));
//                 scan.setStopRow(Bytes.toBytes(Long.MAX_VALUE - 1515673074L));
                // scan.setStopRow(String.valueOf(Long.MAX_VALUE - 1515673074L).getBytes());
                scan.setStopRow("516_242_358_9_235_1521467990_BN0003887".getBytes());
                // 限制条数
                scan.setMaxResultSize(2L);
                scan.setBatch(1);
                scan.setCaching(1);
                System.out.println("--------------scan.getBatch()-------------"  + scan.getBatch());
                System.out.println("-----------------scan.getCaching()----------------" + scan.getCaching());

//                System.out.println("当前" + new Date().getTime());
//                System.out.println("当前时间的前一分钟" + calendarTodayEnd.getTimeInMillis());
                System.out.println("-----startRowkey--" + String.valueOf(Long.MAX_VALUE - startTime));
                System.out.println("-----stopRowkey--" + String.valueOf(Long.MAX_VALUE - stopTime));

//                System.out.println("当前时间" + new Date().getTime());
//                System.out.println("当前时间的前一分钟" + calendarTodayEnd.getTimeInMillis());
//                System.out.println("-----startRowkey--" + String.valueOf(Long.MAX_VALUE - new Date().getTime()));
//                System.out.println("-----stopRowkey--" + String.valueOf(Long.MAX_VALUE - calendarTodayEnd.getTimeInMillis()));
        		
                // 时间区间
                ResultScanner rscanner = table.getScanner(scan);
                Iterator<Result> iterator = rscanner.iterator();
                while(iterator.hasNext()) {
                	Result next = iterator.next();
                	for(Cell cell:next.listCells()) {
                		System.out.println(new String(cell.getRow()));
                	}
                }
                Map<String, String> rsMap = null;
                for (Result result : rscanner) {
                    rsMap = getRowByResult(result);
                    // System.out.println(rsMap.get("rowkey").split("_")[5]);
                    list.add(rsMap.get("rowkey"));
                }
                
                System.out.println("------件数------" + list.size());

                return list;
            }
        });
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
        Map<String, String> cellMap = new HashMap<String, String>();
        int index = 0;
        for (Cell cell : result.listCells()) {
        	if(0 == index) {
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
//    			System.out.println("----------------value-----------" + value);
//    			cellMap.put(CpConstants.HBASE_TABLE_PROP_ROWKEY, rowkey);
//    			cellMap.put(CpConstants.HBASE_TABLE_PROP_COLUMNFAMILY, cf);
    			cellMap.put("rowkey", rowkey);
    			cellMap.put("qf", value);
        	}

        }
        return cellMap;
    }

}
