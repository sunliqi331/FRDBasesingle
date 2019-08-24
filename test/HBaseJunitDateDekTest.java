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

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.KeyValue;
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
@ContextConfiguration(locations = { "classpath:applicationContext-test.xml",
        "classpath:applicationContext-shiro-cas.xml", "classpath:redis/applicationContext-redis.xml",
        "classpath:hadoop/applicationContext-hadoop.xml" })
@Transactional
public class HBaseJunitDateDekTest {
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

    @Resource(name = "redisService")
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

    /**
     * 根据双环要求删除数据
     */
    @Test
    public void testHbaseDataDel_20180322() {
        try {
            String rowKey = "516_242_358_9_235_1521468916_BN0004041";
            Map<String, Object> map = hTemplate.get("procedure", rowKey, new RowMapper<Map<String, Object>>() {
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
                hTemplate.delete("procedure", rowKey, familynm);
                System.out.println("数据删除结束。");
            } else {
                System.out.println("数据不存在，取消删除");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
