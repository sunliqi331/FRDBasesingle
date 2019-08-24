import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.transform.Transformers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.its.common.service.RedisService;
import com.its.common.util.dwz.Page;
import com.its.frd.dao.MesPointsDao;
import com.its.frd.dao.MesProcedurePropertyPointLogDao;
import com.its.frd.entity.MesPointGateway;
import com.its.frd.entity.MesPointType;
import com.its.frd.entity.MesPoints;
import com.its.frd.entity.MesProcedureProperty;
import com.its.frd.schedule.ProductionScheduler;
import com.its.frd.service.CompanyinfoService;
import com.its.frd.service.MesPointGatewayService;
import com.its.frd.service.MesSpcMonitorService;
import com.its.frd.util.HBaseUtilNew;
import com.its.frd.util.HbUtils;
import com.its.monitor.service.MesPointsTemplateService;
import com.its.statistics.service.StatisticsService;
import com.its.statistics.vo.CgAnalyzeData;
import com.its.statistics.vo.CgAnalyzeSearch;
import com.its.statistics.vo.GrrAnalyzeSearch;
import com.its.statistics.vo.ProductionRecord;
import com.its.statistics.vo.SpcAnalyzeSearch;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-test.xml","classpath:applicationContext-shiro-cas.xml","classpath:redis/applicationContext-redis.xml","classpath:hadoop/applicationContext-hadoop.xml"})
@Transactional
public class JUnitTest { 
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
	@Test
	public void testGrr() throws IOException{
		GrrAnalyzeSearch grrAnalyzeSearch = new GrrAnalyzeSearch();
		grrAnalyzeSearch.setPersonNum(3);
		grrAnalyzeSearch.setWorkpieceNum(10);
		grrAnalyzeSearch.setCheckNum(3);
		grrAnalyzeSearch.setScale(4);
		grrAnalyzeSearch.setAnalyseGrrType(0);
		grrAnalyzeSearch.setProcedurePropertyId(89);
		List<String> dataList = FileUtils.readLines(new File("D:\\FRD_upload_FILE\\GrrData.txt"), "UTF-8");
		List<CgAnalyzeData> analyzeDatas = new ArrayList<>();
		for(String data : dataList){
			CgAnalyzeData analyzeData = new CgAnalyzeData();
			analyzeData.setValue(data);
			analyzeDatas.add(analyzeData);
		}
		grrAnalyzeSearch.setDataList(analyzeDatas);
		System.out.println(statisticsService.analyseGrrData(grrAnalyzeSearch));
		//System.out.println(statisticsService.analyseSpcData(grrAnalyzeSearch));
	}
	@Test
	public void testSpc() throws IOException{
		SpcAnalyzeSearch spcAnalyzeData = new SpcAnalyzeSearch();
		spcAnalyzeData.setSubNum(5);
		spcAnalyzeData.setSubRange(25);
		spcAnalyzeData.setSubSeq(0);
		spcAnalyzeData.setScale(4);
		spcAnalyzeData.setProcedurePropertyId(88);
		List<Double> dataLists = new ArrayList<Double>();
		List<String> dataList = FileUtils.readLines(new File("D:\\FRD_upload_FILE\\SpcData.txt"), "UTF-8");
		for(String data : dataList){
			dataLists.add(Double.parseDouble(data));
		}
		spcAnalyzeData.setValues(dataLists);
		System.out.println(statisticsService.analyseSpcData(spcAnalyzeData));
	}
	@Test
	public void testCg() throws IOException{
		CgAnalyzeSearch cgAnalyzeData = new CgAnalyzeSearch();
		cgAnalyzeData.setAnalysisMethod("cg");
		cgAnalyzeData.setActualValue(0.488);
		cgAnalyzeData.setMaxValue(0.5000);
		cgAnalyzeData.setMinValue(-0.6000);
		cgAnalyzeData.setStandardValue(0.4880);
		cgAnalyzeData.setScale(4);
		cgAnalyzeData.setStatisticalStandard(4);
		List<Double> dataLists = new ArrayList<Double>();
		List<String> dataList = FileUtils.readLines(new File("D:\\FRD_upload_FILE\\CgData.txt"), "UTF-8");
		for(String data : dataList){
			dataLists.add(Double.parseDouble(data));
		}
		cgAnalyzeData.setValues(dataLists);
		statisticsService.analyseCgData(cgAnalyzeData);
	}
	@Test
	@Transactional
	@Rollback(false)
	public void test() throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException{
		MesPoints mesPoints = mesPointsDao.findOne(196L);
		MesPointType mesPointType = mesPoints.getMesPointType();
		if(HbUtils.isProxy(mesPointType)){
			Class claz = HbUtils.getClassWithoutInitializingProxy(mesPointType);
			MesPointType mesPointType2 = HbUtils.deproxy(mesPointType);
			System.out.println(claz.getMethod("getId").invoke(mesPointType2));
		}
	}
	
	@Test
	@Transactional
	@Rollback(false)
	public void test1() throws IOException {
		//String template = mesPointsTemplateService.sendTemplate(mesPointsDao.findAll());
		//FileUtils.writeStringToFile(new File("d:\\mesTemplete.txt"), template);
	}
	@Test
	public void test2() throws SQLException {
		Session session = (Session) entityManager.getDelegate();
		
		SessionFactoryImpl sessionFactory = (SessionFactoryImpl) session.getSessionFactory();
		Connection connection = sessionFactory.getConnectionProvider().getConnection();
		String ddl = "";
		//conn.prepareCall("{call frd_base.create_partition_by_month('frd_base','"+table+"')}");
		CallableStatement cs = connection.prepareCall("{call frd_base.create_partition_by_month('frd_base','TABLE_NAME')}");
		cs.execute();
	}
	@Test
	public void test3() throws SQLException {
		//productionScheduler.getProductionRecordTask();
		/*Map<String, Object> properties = entityManager.getEntityManagerFactory().getProperties();
		for(String key : properties.keySet()){
			System.out.println("key:"+key+"------value:"+properties.get(key));
		}*/
		Session session = (Session) entityManager.getDelegate();
		Page page = new Page();
		page.setPageNum(1);
		page.setNumPerPage(5);
		Query query = session.createSQLQuery("select * from production_table_factory_524").setResultTransformer(Transformers.aliasToBean(ProductionRecord.class));
		//query.setFirstResult((page.getPageNum()-1) * page.getNumPerPage());
		//query.setMaxResults(page.getNumPerPage());
		List<ProductionRecord> list = query.list();
		List<String> rowKeys = new ArrayList<>();
		for(ProductionRecord productionRecord : list){
			rowKeys.add(productionRecord.getRowkey());
		}
		List<Result> listResult = HBaseUtilNew.getDatasFromHbase("frd", "cf", rowKeys, null, true, true);
		System.out.println(listResult.size());
		for(Result result : listResult){
			for(Cell _cell:result.listCells()){ 
				String _value = Bytes.toString( _cell.getValueArray(), _cell.getValueOffset(), _cell.getValueLength());  
				String quali = Bytes.toString( _cell.getQualifierArray(),_cell.getQualifierOffset(),_cell.getQualifierLength());
				if(quali.split(":")[3].equals(MesPointType.TYPE_PRODUCT_PROCEDURE) && !quali.split(":")[4].equals("0")){
					//productionStatus = false;
				}
				//String row = Bytes.toString( cell.getRowArray(), cell.getRowOffset(), cell.getRowLength());
				String checkValue = _value.split(":")[3];
				System.out.println(checkValue);
				String propertyId = quali.split(":")[1];
				//MesProcedureProperty mesProcedureProperty = hBasePageModel.getProcedurePropertyMap().get(Long.parseLong(propertyId));
				//mesProcedureProperty.setCheckValue(checkValue);
			}
		}
		
		
		/*for(ProductionRecord mesQdas : list){
			System.out.println(mesQdas.getCompanyId());
		}*/
		//System.out.println(list.get(0));
		//DbUtils.loadDriver(driverClassName)
	}
	
	@Test
	public void testRedis() {
		List<MesPointGateway> findAll = mesPointGatewayService.findAll(null);
		System.out.println(findAll.size());
	}
}
