package com.its.frd.schedule;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.internal.SessionFactoryImpl;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.its.frd.entity.Companyinfo;
import com.its.frd.service.CompanyinfoService;

@Component
public class ProductionScheduler {
	private static final Logger log = LoggerFactory.getLogger(ProductionScheduler.class);

	@Autowired
	private CompanyinfoService companyinfoService;

	public static final String TABLE_PREFIX = "production_table_factory_";
	@Value("${productTableCreate}")
	private String productTableCreate;
	@Value("${productTableColumns}")
	private String productTableColumns;
	@PersistenceContext
	private EntityManager entityManager;

	private Connection conn = null;

	public ProductionScheduler() {
	}
//	@Scheduled(cron="0/10 * * * * ? ")
	//@Scheduled(cron = "0 0 */2 * * ?")
	public void callPartition(){

		List<Companyinfo> companys = getCompanyinfos();
		if(companys.size() == 0){
			return;
		}
		for(Companyinfo companyinfo : companys){
			String table = ProductionScheduler.TABLE_PREFIX+companyinfo.getId();
			CallableStatement cs = null;
			try {
				if(conn == null){
					conn = getConnection();
				}
				cs = conn.prepareCall("{call frd_base.create_partition_by_month('frd_base','"+table+"')}");
				cs.execute();
			}catch (SQLException e) {
				// TODO: handle exception
			}finally {
				if(cs != null){
					try {
						cs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}

	}
//	@Scheduled(cron="0 0/1 * * * ?")
	//@Scheduled(cron = "0 0 */2 * * ?")
	public void getProductionRecordTask(){
		List<Companyinfo> companys = getCompanyinfos();
		if(companys.size() == 0){
			return;
		}
		List<String> tableNamesList = tableNameListWithSQM(companys);
		String tables = StringUtils.join(tableNamesList, ",");
		removeExistTables(tableNamesList,tables);
		generateTables(tableNamesList);
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");    
		log.debug("获取记录时间："+sdf.format(DateTime.now().toDate()));
	}    

	private Connection getConnection() throws SQLException{
		Session session = (Session) entityManager.getDelegate();
		SessionFactoryImpl sessionFactory = (SessionFactoryImpl) session.getSessionFactory();
		if(null == conn){
			conn = sessionFactory.getConnectionProvider().getConnection();
		}
		return conn;
	}
	private List<Companyinfo> getCompanyinfos(){
		List<Companyinfo> companyinfoList = companyinfoService.findByCompanytypeAndCompanystatus(Companyinfo.FACTORY, Companyinfo.COPANYSTATUS_OK);
		return companyinfoList;
	}
	private List<String> tableNameListWithSQM(List<Companyinfo> companyinfos){
		List<String> tableNamesList = new ArrayList<>();
		for(int i = 0; i < companyinfos.size(); i++ ){
			tableNamesList.add("'"+ProductionScheduler.TABLE_PREFIX+companyinfos.get(i).getId()+"'");
		}
		return tableNamesList;
	}
	private boolean generateTables(List<String> tableNamesList){
		boolean flag = true;
		for(String tableName : tableNamesList){
			Statement s;
			try {
				if(conn == null){
					conn = getConnection();
				}
				s = conn.createStatement();
				boolean isCreated = s.execute(productTableCreate.replaceAll("TABLE_NAME", tableName.substring(1, tableName.length()-1)));
				if(isCreated){
					s.close();
				}else{
					flag = false;
					log.warn("有生产记录表未创建："+tableName);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.warn("有生产记录表未创建："+tableName);
			}
		}
		return flag;
	}
	private void removeExistTables(List<String> tableNamesList,String tables){
		Statement s1 = null;
		ResultSet existRs = null;
		try {
			if(conn == null){
				conn = getConnection();
			}
			s1 = conn.createStatement();
			existRs = s1.executeQuery("select `TABLE_NAME` from `INFORMATION_SCHEMA`.`TABLES` where `TABLE_SCHEMA`='frd_base_new' and `TABLE_NAME` in ("+tables+")");
			while(existRs.next()){
				tableNamesList.remove("'"+existRs.getString(1)+"'");
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}finally {
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
	}
}
