import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.Cell;
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
public class HBaseJunitTest_Monitor {

    @Autowired
    private HbaseTemplate hTemplate;

    @PersistenceContext
    private EntityManager entityManager;
    
    @Before
    public void before() {
    	System.setProperty("hadoop.home.dir", "F:\\hadoop-common-2.2.0-bin-master");
    }
    @Test
    public void test1() {
    	String value = "549_268_16_284_00-14-97-14-3A-2F";
    	System.out.println(value.split("_")[4]);
    }
    

    @Test
    public void test() {
        System.out.println("---------------");
//        String factoryId = "511";
//        String productLineId = "242";
//        String driverId = "";
//        String productId = "9";
//        String produceId = "235";
        
        String factoryId = "516";
        String productLineId = "242";
        String driverId = 	"358";
        String productId = "109";
        String produceId = "235";
        StringBuffer regexStrBur = new StringBuffer();
        regexStrBur.append("^");
        regexStrBur.append(StringUtils.isNotBlank(factoryId)? factoryId + "_" : ".*_");
        regexStrBur.append(StringUtils.isNotBlank(productLineId)? productLineId + "_" : ".*_");
        regexStrBur.append(StringUtils.isNotBlank(driverId)? driverId + "_" : ".*_");
        regexStrBur.append(StringUtils.isNotBlank(productId)? productId + "_" : ".*_");
        regexStrBur.append(StringUtils.isNotBlank(produceId)? produceId + "_" : ".*_");
        regexStrBur.append(".*");
        System.out.println(regexStrBur.toString());
    }

    @Test
    public void testPageFilter20180101_01_监控产量() {
        List<String> totalResult = hTemplate.execute("procedure", new TableCallback<List<String>>() {
            List<String> list = new ArrayList<>();
            @Override
            public List<String> doInTable(HTableInterface table) throws Throwable {
                // 过滤器的添加
                Scan scan = new Scan();
                // 时间区间确定
                // scan.setTimeRange(Long.valueOf("1514822400000"), Long.valueOf("1514908800000"));
                List<Filter> filters = new ArrayList<>();
                // rowKey前匹配
//                String factoryId = "516";
//                String productLineId = "242";
//                String driverId = "";
//                String productId = "9";
//                String produceId = "235";
                String factoryId = "516";
                String productLineId = "242";
                String driverId = 	"358";
                String productId = "109";
                String produceId = "235";
                StringBuffer regexStrBur = new StringBuffer();
                regexStrBur.append("^");
                regexStrBur.append(StringUtils.isNotBlank(factoryId)? factoryId + "_" : ".*_");
                regexStrBur.append(StringUtils.isNotBlank(productLineId)? productLineId + "_" : ".*_");
                regexStrBur.append(StringUtils.isNotBlank(driverId)? driverId + "_" : ".*_");
                regexStrBur.append(StringUtils.isNotBlank(productId)? productId + "_" : ".*_");
                regexStrBur.append(StringUtils.isNotBlank(produceId)? produceId + "_" : ".*_");
                regexStrBur.append(".*");
                Filter filter2 = new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator(regexStrBur.toString()));
                filters.add(filter2);
                FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL, filters);
                scan.setFilter(filterList);
                ResultScanner rscanner = table.getScanner(scan);
                Map<String, String> rsMap = null;
                for (Result result : rscanner) {
                	rsMap = HbaseUtil.getRowByResult(result);
//                    rsMap = getRowByResult(result);
                    list.add(rsMap.get("rowkey"));
                }

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
			System.out.println("-----------------qf----------" + qf.split("_")[2]);
			System.out.println("-----------------pointId----------" + qf);
//			System.out.println("----------------value-----------" + value);
//			cellMap.put(CpConstants.HBASE_TABLE_PROP_ROWKEY, rowkey);
//			cellMap.put(CpConstants.HBASE_TABLE_PROP_COLUMNFAMILY, cf);
			cellMap.put("rowkey", rowkey);
			cellMap.put("qf", value);
        }
        return cellMap;
    }

}
