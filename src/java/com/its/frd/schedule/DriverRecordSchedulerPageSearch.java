package com.its.frd.schedule;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.its.common.util.persistence.DynamicSpecifications;
import com.its.common.util.persistence.SearchFilter;
import com.its.common.util.persistence.SearchFilter.Operator;
import com.its.common.utils.SecurityUtils;
import com.its.frd.entity.Companyinfo;
import com.its.frd.entity.MesDataDriverStatus;
import com.its.frd.entity.MesDriver;
import com.its.frd.service.CompanyinfoService;
import com.its.frd.service.MesDataDriverStatusService;
import com.its.frd.service.MesDriverService;
import com.its.frd.service.MesProductService;
import com.its.frd.service.MesProductlineService;
import com.its.frd.util.HBasePageModel;
import com.its.statistics.vo.AnalyzeSearch;
import com.its.statistics.vo.DriverRecord;

@Component
public class DriverRecordSchedulerPageSearch {
	
	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	private MesProductlineService mesProductlineService;
	@Autowired
	private MesDriverService mds;
	@Autowired
	private MesProductService mps;
	@Autowired
	private CompanyinfoService companyinfoService;
	@Autowired
	private MesDataDriverStatusService mesDriverStatusSer;
	
	@SuppressWarnings("unchecked")
	public List<DriverRecord> getDriverRecordPagable(HBasePageModel page){
		List<String> tableNames = generateTableName(page);
		Session session = (Session) entityManager.getDelegate();
		StringBuilder sql = new StringBuilder();
		for(String tableName : tableNames){
			boolean tableExist = checkTableExist(session, tableName);
			if(!tableExist){
				continue;
			}
			sql.append( "select * from " + tableName);
			generateSqlCondition(sql,page);
			sql.append(" UNION ALL ");
		}
		sql.delete(sql.lastIndexOf("UNION ALL"), sql.length());
		sql.append(" ORDER BY datetime desc");
		//sql = sql.substring(0, sql.lastIndexOf("UNION ALL"));
		if(sql.equals("")){
			return new ArrayList<>();
		}
		int currentPageNumber = page.getPlainPageNum();
		Query query = session.createSQLQuery(sql.toString()).setResultTransformer(Transformers.aliasToBean(DriverRecord.class));
		if(page.isPagable()){
			query.setFirstResult((currentPageNumber-1) * page.getNumPerPage());
			query.setMaxResults(page.getNumPerPage());
		}
		return query.list();
	}
	public int getDriverRecordAmount(HBasePageModel page){
		List<String> tableNames = generateTableName(page);
		Session session = (Session) entityManager.getDelegate();
		StringBuilder sql = new StringBuilder();
		System.out.println(new Date() + "----tableNames-----" + tableNames.size());
		System.out.println(new Date() + "----tableNames-----" + tableNames.get(0));
		for(String tableName : tableNames){
			boolean tableExist = checkTableExist(session, tableName);
			System.out.println(new Date() + "----tableExist-----" + tableExist);
			if(!tableExist){
				continue;
			}
			sql.append( "select count(*) from " + tableName);
			generateSqlCondition(sql,page);
			sql.append(" UNION ALL ");
		}
		sql.delete(sql.lastIndexOf("UNION ALL"), sql.length());
		int amount = 0;
		if(sql.equals("")){
			return amount;
		}
		BigInteger bigInteger = new BigInteger("0");
		SQLQuery query = session.createSQLQuery(sql.toString());
		List<BigInteger> list = query.list();
		for(BigInteger num : list){
			bigInteger = bigInteger.add(num);
		}
		return bigInteger.intValue();
	}
	
	private String returnWhereOrAnd(StringBuilder sql){
		if(sql.indexOf("UNION ALL") != -1 ? sql.substring(sql.lastIndexOf("UNION ALL")).toString().toLowerCase().indexOf("where") != -1 : sql.toString().toLowerCase().indexOf("where") != -1){
			return " and ";
		}else{
			return " where ";
		}
	}


	private StringBuilder generateSqlCondition(StringBuilder sql , HBasePageModel page){
		AnalyzeSearch analyzeSearch = page.getAnalyzeSearch();
		if(analyzeSearch.getBegin() != null){
			sql.append( returnWhereOrAnd(sql) + " datetime >= " + Long.parseLong(String.valueOf(analyzeSearch.getBegin().getTime()).substring(0, 10)));
		}
		if(analyzeSearch.getEnd() != null){
			sql.append( returnWhereOrAnd(sql) + " datetime <= " + Long.parseLong(String.valueOf(analyzeSearch.getEnd().getTime()).substring(0, 10)));
		}
		if(analyzeSearch.getProductLineId() != 0){
			sql.append( returnWhereOrAnd(sql) + " productlineId = " + analyzeSearch.getProductLineId());
		}
		if(analyzeSearch.getMesDriverId() != 0){
			sql.append( returnWhereOrAnd(sql) + " mesDriverId = " + analyzeSearch.getMesDriverId());
		}
		if(analyzeSearch.getProductId() != 0){
			sql.append( returnWhereOrAnd(sql) + " productId = " + analyzeSearch.getProductId());
		}
		if(StringUtils.isNotEmpty(analyzeSearch.getDriver_count())){
			sql.append( returnWhereOrAnd(sql) + " driver_count = " + analyzeSearch.getDriver_count());
		}
		return sql;

	}

	private boolean checkTableExist(Session session ,String tableName){
		SQLQuery query1 = session.createSQLQuery("select `TABLE_NAME` from `INFORMATION_SCHEMA`.`TABLES` where `TABLE_SCHEMA`='frd_base_new' and `TABLE_NAME` = '"+tableName+"'");
		List<Object> tableList = query1.list();
		if(tableList.size() == 0){
			return false;
		}
		return true;
	}

	private List<String> generateTableName(HBasePageModel page){
		List<String> tableList = new ArrayList<>();
		long lineId = page.getAnalyzeSearch().getProductLineId();
		if(lineId != 0){
			long factoryId = 0;
			factoryId = mesProductlineService.findById(lineId).getCompanyinfo().getId();
			String tableName = "driver_table_factory_"+factoryId;
			tableList.add(tableName);
		}else{
			long companyId = SecurityUtils.getShiroUser().getCompanyid();
			List<Companyinfo> factoryList = companyinfoService.findByParentidAndCompanytype(companyId, Companyinfo.FACTORY);
			for(Companyinfo factory : factoryList){
				if(!factory.getCompanystatus().equals(Companyinfo.COPANYSTATUS_OK)){
					continue;
				}
				String tableName = "driver_table_factory_"+factory.getId();
				tableList.add(tableName);
			}
		}
		return tableList;
	}
	
	/**
	 * 根据设备Id、时间段、产品型号获取driverRecord集合
	 * @param begin
	 * @param end
	 * @param driverId
	 * @param modelNum 产品型号
	 * @return
	 */
//	@SuppressWarnings("unchecked")
//	public List<DriverRecord> getByDriverIdAndTimeIfModelnum(Date begin,Date end,Long driverId,String modelNum){
//		List<DriverRecord> list = new ArrayList<DriverRecord>();
//		Session session = (Session) entityManager.getDelegate();
//		//获取此设备所属工厂，拼接表名，获取查询参数
//		MesDriver driver = mds.findById(driverId);
//		Long factoryId = driver.getMesProductline().getCompanyinfo().getId();
//		Long productId = null;
//		if(StringUtils.isNotEmpty(modelNum)){
//			productId = mps.findByCompanyIdAndModelnum(driver.getCompanyinfo().getId(), modelNum).getId();
//		}else{	
//			return list;
//		}
//		String startTime = (begin.getTime()+"").substring(0, 10);//10位的时间戳
//		String endTime = (end.getTime()+"").substring(0, 10);
//		String driverTableName = "driver_table_factory_"+factoryId;
//		String productionTableName = "production_table_factory_"+factoryId;
//		//检查表是否存在
//		if(!this.checkTableExist(session, driverTableName) || !this.checkTableExist(session, productionTableName)){
//			return list;
//		}
//		//拼接sql语句
//		String sql = "select distinct t1.datetime,t1.driver_status from "+driverTableName+" t1,"
//				+"(select t2.* from "+productionTableName+" t2 where t2.datetime > "+startTime
//				+" and t2.datetime < "+endTime+" and t2.productId = "+productId+") t3 "
//				+" where t1.datetime = t3.datetime and t1.mesDriverId = t3.mesDriverId";
//		Query query = session.createSQLQuery(sql).setResultTransformer(Transformers.aliasToBean(DriverRecord.class));
//		list = query.list();
//		return list;
//	}
	
	
	   /**
     * 根据设备Id、时间段、产品型号获取driverRecord集合
     * @param begin
     * @param end
     * @param driverId
     * @param modelNum 产品型号
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<DriverRecord> getByDriverIdAndTimeIfModelnum(Date begin,Date end,Long driverId,String modelNum, Long chooseFactory2,Long chooseProductLine2){
        
        List<SearchFilter> searchList = Lists.newArrayList();
        searchList.add(new SearchFilter("mesDataMultiKey.factoryId", Operator.EQ, chooseFactory2));
        searchList.add(new SearchFilter("mesDataMultiKey.productLineId", Operator.EQ, chooseProductLine2));
        searchList.add(new SearchFilter("mesDataMultiKey.driverId", Operator.EQ, driverId));
        searchList.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.GTE, String.valueOf(begin.getTime()).substring(0,  10)));
        searchList.add(new SearchFilter("mesDataMultiKey.insertTimestamp", Operator.LTE, String.valueOf(end.getTime()).substring(0,  10)));

        Specification<MesDataDriverStatus> specification = DynamicSpecifications
                .bySearchFilter(MesDataDriverStatus.class, searchList);
        
        List<DriverRecord> list = new ArrayList<DriverRecord>();

        List<MesDataDriverStatus> driverStatusLs = mesDriverStatusSer.findAll(specification);
        if(null == driverStatusLs || 0 == driverStatusLs.size())
            return list;

        driverStatusLs.forEach(obj->{
            DriverRecord driverRecord = new DriverRecord();
            driverRecord.setDatetime(obj.getMesDataMultiKey().getInsertTimestamp());
            driverRecord.setDriver_status(obj.getDriverStatus());
            list.add(driverRecord);
        });

//        Session session = (Session) entityManager.getDelegate();
//        //获取此设备所属工厂，拼接表名，获取查询参数
//        MesDriver driver = mds.findById(driverId);
//        Long factoryId = driver.getMesProductline().getCompanyinfo().getId();
//        Long productId = null;
//        if(StringUtils.isNotEmpty(modelNum)){
//            productId = mps.findByCompanyIdAndModelnum(driver.getCompanyinfo().getId(), modelNum).getId();
//        }else{  
//            return list;
//        }
//        String startTime = (begin.getTime()+"").substring(0, 10);//10位的时间戳
//        String endTime = (end.getTime()+"").substring(0, 10);
//        String driverTableName = "driver_table_factory_"+factoryId;
//        String productionTableName = "production_table_factory_"+factoryId;
//        //检查表是否存在
//        if(!this.checkTableExist(session, driverTableName) || !this.checkTableExist(session, productionTableName)){
//            return list;
//        }
//        //拼接sql语句
//        String sql = "select distinct t1.datetime,t1.driver_status from "+driverTableName+" t1,"
//                +"(select t2.* from "+productionTableName+" t2 where t2.datetime > "+startTime
//                +" and t2.datetime < "+endTime+" and t2.productId = "+productId+") t3 "
//                +" where t1.datetime = t3.datetime and t1.mesDriverId = t3.mesDriverId";
//        Query query = session.createSQLQuery(sql).setResultTransformer(Transformers.aliasToBean(DriverRecord.class));
//        list = query.list();
        return list;
    }
	
}
