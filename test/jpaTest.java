import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;import org.apache.http.impl.io.SocketOutputBuffer;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.transform.Transformers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.Lists;
import com.its.common.dao.RoleDAO;
import com.its.common.entity.main.Role;
import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.common.util.persistence.DynamicSpecifications;
import com.its.common.util.persistence.SearchFilter;
import com.its.common.util.persistence.SearchFilter.Operator;
import com.its.common.utils.SecurityUtils;
import com.its.frd.dao.MesDataDriverPropertyDao;
import com.its.frd.dao.MesDataProductProcedureDao;
import com.its.frd.dao.MesProcedurePropertyDao;
import com.its.frd.dao.MesProductDao;
import com.its.frd.dao.MesQualityImgDao;
import com.its.frd.entity.MesDataDriverCount;
import com.its.frd.entity.MesDataDriverProperty;
import com.its.frd.entity.MesDataMultiKey;
import com.its.frd.entity.MesDataProductProcedure;
import com.its.frd.entity.MesProcedureProperty;
import com.its.frd.entity.MesProduct;
import com.its.frd.entity.MesProductProcedure;
import com.its.frd.entity.MesQualityImg;
import com.its.frd.service.MesDataDriverCountService;
import com.its.frd.service.MesDataDriverPropertyService;
import com.its.frd.service.MesDataProductProcedureService;
import com.its.frd.util.DateUtils;
import com.its.frd.util.HbaseUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-test.xml",
        "classpath:applicationContext-shiro-cas.xml", "classpath:redis/applicationContext-redis.xml" })
public class jpaTest {

    @Resource
    private MesDataDriverCountService mesDataDriverCountService;

    @Resource
    private MesDataDriverPropertyService mesDataDriverPropertyService;

    @PersistenceContext
    EntityManager entityManager;

    @Resource
    private MesDataDriverPropertyDao mdDao;

    @Autowired
    private RoleDAO roleDAO;

    @Resource
    private MesQualityImgDao mesQualityImgDao;
    
    @Autowired
    private MesDataProductProcedureService mesDataProductProcedureService;

    @Resource
    private MesProcedurePropertyDao mesProcedurePropertyDao;

    @Resource
    private MesProductDao mesProductDao;
    @Resource
    private MesDataProductProcedureDao mesDataProductProcedureDao;

    /**
     * 设备产量计数
     */
    @Test
    public void mesDriverTypeService() {
        try {
            Date begin = DateUtils.parse("2018-05-09 11:09:15", DateUtils.DATE_FULL_STR);
            List<SearchFilter> list = Lists.newArrayList();
            list.add(new SearchFilter("mesDataMulrtiKey.insertTimestamp", Operator.GTE, new Timestamp(begin.getTime())));
            Specification<MesDataDriverCount> specification1 = DynamicSpecifications
                    .bySearchFilter(MesDataDriverCount.class, list);

            List<MesDataDriverCount> outList = mesDataDriverCountService.findAll(specification1);
            System.out.println(outList.size());

            // List<MesDataDriverCount> outL =
            // mesDataDriverCountService.findAllMesDataDriverCount(511, 101, 202, 1001,
            // new Timestamp(begin.getTime()), new Timestamp(new Date().getTime()));
            // System.out.println(outL.size());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 设备属性
     */
    public void data_driver_property() {
        try {
            List<SearchFilter> list = Lists.newArrayList();
            List<Long> driverPropertyIds = Lists.newArrayList();
            driverPropertyIds.add(252L);
            list.add(new SearchFilter("driverPropertyId", Operator.IN, driverPropertyIds.toArray()));
            Specification<MesDataDriverProperty> specification = DynamicSpecifications
                    .bySearchFilter(MesDataDriverProperty.class, list);
            List<String> sortProperties = Lists.newArrayList();
            sortProperties.add("insertTimestamp");
            Sort sort = new Sort(Sort.Direction.DESC, sortProperties);
            List<MesDataDriverProperty> outList = mesDataDriverPropertyService.findAll(specification, sort);
            System.out.println(outList.size());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 设备属性 分页
     */
    @Test
    public void data_driver_count() {
        try {
            List<Long> driverPropertyIds = Lists.newArrayList();
            driverPropertyIds.add(252L);
            List<SearchFilter> list = Lists.newArrayList();
            list.add(new SearchFilter("mesDataMultiKey.productLineId", Operator.EQ, "101"));
            Specification<MesDataDriverProperty> specification = DynamicSpecifications
                    .bySearchFilter(MesDataDriverProperty.class, list);
            Page page = new Page();
            org.springframework.data.domain.Page<MesDataDriverProperty> springDataPage = mdDao
                    .findAll(specification, PageUtils.createPageable(page));
            page.setTotalCount(springDataPage.getTotalElements());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    /**
     * 设备属性 分页
     */
    @Test
    public void data_driver_property_for_page() {
        try {
            Date begin = DateUtils.parse("2018-05-01 11:09:15", DateUtils.DATE_FULL_STR);
            Date end = DateUtils.parse("2018-05-15 11:09:15", DateUtils.DATE_FULL_STR);
            List<SearchFilter> list = Lists.newArrayList();
            list.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.GTE, new Timestamp(begin.getTime())));
            list.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.LTE, new Timestamp(end.getTime())));
            list.add(new SearchFilter("mesDataMultiKey.productLineId", Operator.EQ, "242"));

            Specification<MesDataDriverProperty> specification = DynamicSpecifications
                    .bySearchFilter(MesDataDriverProperty.class, list);
            
            List<String> sortProperties = Lists.newArrayList();
            sortProperties.add("mesDataMultiKey.insertTimestamp");
            Sort sort = new Sort(Sort.Direction.DESC, sortProperties);
            List<MesDataDriverProperty> rs = mesDataDriverPropertyService.findAll(specification, sort);
            System.out.println(rs.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 设备属性 分页
     */
    @Test
    public void data_product_procedure_pageTest() {
        
        HbaseUtil hbaseU = new HbaseUtil();
        List<SearchFilter> searchList = Lists.newArrayList();
        searchList.add(new SearchFilter("mesDataMultiKey.factoryId", Operator.EQ, "516"));
        searchList.add(new SearchFilter("mesDataMultiKey.productLineId", Operator.EQ, "262"));
        Date timeSear = DateUtils.parse("2018-05-15 22:27:32", DateUtils.DATE_FULL_STR);
        searchList.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.GTE, 1526511642L));
        searchList.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.LTE, 1526521011L));
        searchList.add(new SearchFilter("productMode", Operator.EQ, "8"));
        Specification<MesDataProductProcedure> specification = DynamicSpecifications
                .bySearchFilter(MesDataProductProcedure.class, searchList);
        Page outPage = new Page();
        outPage.setOrderField("mesDataMultiKey.insertTimestamp");
        outPage.setOrderDirection(Page.ORDER_DIRECTION_DESC);
        outPage.setPageNum(0);
        outPage.setNumPerPage(10);
        List<MesDataProductProcedure> mesDataProductProcedureList= mesDataProductProcedureService.findPage(specification, outPage);
        System.out.println(mesDataProductProcedureList.size());
        
        List<SearchFilter> searchList1 = Lists.newArrayList();
        searchList.add(new SearchFilter("mesProductProcedure.id", Operator.EQ, 234L));
        Specification<MesProcedureProperty> specification1 = DynamicSpecifications
                .bySearchFilter(MesProcedureProperty.class, searchList1);
        MesProductProcedure mesProductProcedure = new MesProductProcedure();
        mesProductProcedure.setId(234L);
        List<MesProcedureProperty> out = mesProcedurePropertyDao.findByMesProductProcedure(mesProductProcedure);
        System.out.println(out.size());

    }
// Predicate p1 =  builder.equal(root.get("companyinfo").get("id").as(Long.class),SecurityUtils.getShiroUser().getCompanyid());    
    
    /**
     * 直接写sql文，读取主键，并区去重复。
     * @throws SQLException 
     */
    @SuppressWarnings("unchecked")
    @Test
    public void daba_zhjieduqu() throws SQLException {
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
                    + " INSERT_TIMESTAMP from data_product_procedure");
            sql.append(" where FACTORY_ID = 516");
            sql.append(" and PRODUCT_LINE_ID = 242");
            sql.append(" and DRIVER_ID = 358");
            sql.append(" and PRODUCT_MODE = 9");
            sql.append(" and INSERT_TIMESTAMP > 1526545854");
            sql.append(" group by INSERT_TIMESTAMP");
            sql.append(" order by INSERT_TIMESTAMP");
            sql.append(" LIMIT 10");
            existRs = s1.executeQuery(sql.toString());
            int count = 0;
            while(existRs.next()){
                count++;
//                System.out.println(existRs.getString(0));
                System.out.println(existRs.getString(1));
            }
            System.out.println(count);
//            sql.append(" LIMIT 10");
            // Query query = session.createSQLQuery(sql.toString()).setResultTransformer(Transformers.aliasToBean(MesDataMultiKey.class));
//            Query query = (Query) entityManager.createQuery(sql.toString());
//            Query query = (Query) entityManager.createNamedQuery(sql.toString());
//            session.getTransaction().commit();  
//            List<MesDataMultiKey> list = query.list();
//            System.out.println(list.size());
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void ttt() {
//        try {
//            int rs = mesDataProductProcedureDao.deleteByRowKey(516, 242, 358, 5202, BigInteger.valueOf(1526955014));
//            System.out.println(rs);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        
        Date date=new Date();//取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
//        calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动\n" +
        calendar.add(calendar.MINUTE, 1);
        date=calendar.getTime(); //这个时间就是日期往后推一天的结果\n" +
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = formatter.format(date);
        System.out.println(dateString.substring(0,4));
        System.out.println(dateString.substring(4,6));
        System.out.println(dateString.substring(6,8));
        System.out.println(dateString.substring(8,10));
        System.out.println(dateString.substring(10,12));
        System.out.println(dateString.substring(12));
        
        String val = "1527004813000";
        System.out.println(DateUtils.unixTimestampToDate(Long.valueOf(val)));
    }
    public static void main(String[] args) {
        String val = "1527004813000";
        System.out.println(DateUtils.unixTimestampToDate(Long.valueOf(val)));
    }

}
