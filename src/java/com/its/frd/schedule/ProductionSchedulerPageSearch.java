package com.its.frd.schedule;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.TableCallback;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.its.common.utils.SecurityUtils;
import com.its.frd.dao.MesProductDao;
import com.its.frd.dao.MesProductlineDao;
import com.its.frd.entity.Companyinfo;
import com.its.frd.entity.MesDataProductProcedure;
import com.its.frd.entity.MesProduct;
import com.its.frd.entity.MesProductline;
import com.its.frd.service.CompanyinfoService;
import com.its.frd.service.MesProductlineService;
import com.its.frd.util.HBasePageModel;
import com.its.frd.util.HBaseUtilNew;
import com.its.frd.util.HbaseUtil;
import com.its.frd.util.MesDataRowkeyUtil;
import com.its.statistics.vo.AnalyzeSearch;
import com.its.statistics.vo.ProductionRecord;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.util.Bytes;

@Component
public class ProductionSchedulerPageSearch {
	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	private MesProductlineService mesProductlineService;
	@Autowired
	private CompanyinfoService companyinfoService;
	@Autowired
	private MesProductDao mesProductDao;
	//@Autowired
	//private HbaseTemplate hTemplate;
	@Autowired
	private MesProductlineDao mesProductlineDao;

	@SuppressWarnings("unchecked")
	public List<ProductionRecord> getProductionRecordPagable(HBasePageModel page){
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
		Query query = session.createSQLQuery(sql.toString()).setResultTransformer(Transformers.aliasToBean(ProductionRecord.class));
		if(page.isPagable()){
			query.setFirstResult((currentPageNumber-1) * page.getNumPerPage());
			query.setMaxResults(page.getNumPerPage());
		}
		return query.list();
	}
	public int getProductionRecordAmount(HBasePageModel page){
		List<String> tableNames = generateTableName(page);
		Session session = (Session) entityManager.getDelegate();
		StringBuilder sql = new StringBuilder();
		for(String tableName : tableNames){
			boolean tableExist = checkTableExist(session, tableName);
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
		if(analyzeSearch.getProductionSn() != null && !analyzeSearch.getProductionSn().equals("")){
			sql.append( returnWhereOrAnd(sql) + " productSn like '%" + analyzeSearch.getProductionSn() + "%'");
		}
		if(analyzeSearch.getProductProcedureId() != 0){
			sql.append( returnWhereOrAnd(sql) + " procedureId = " + analyzeSearch.getProductProcedureId());
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
			String tableName = ProductionScheduler.TABLE_PREFIX+factoryId;
			tableList.add(tableName);
		}else{
			long companyId = SecurityUtils.getShiroUser().getCompanyid();
			List<Companyinfo> factoryList = companyinfoService.findByParentidAndCompanytype(companyId, Companyinfo.FACTORY);
			for(Companyinfo factory : factoryList){
				if(!factory.getCompanystatus().equals(Companyinfo.COPANYSTATUS_OK)){
					continue;
				}
				String tableName = ProductionScheduler.TABLE_PREFIX+factory.getId();
				tableList.add(tableName);
			}
		}
		return tableList;
	}
	
   /**
    * 非分布式，单体版监控服务
     * 用Mysql继续数据查询
     * @param page
     * @return
     */
    public List<ProductionRecord> getProductionRecordPagableByMql(HBasePageModel page){
        AnalyzeSearch analyzeSearch = page.getAnalyzeSearch();
        // 工厂ID
        Long factoryId = null;
        // 产线ID
        Long productlineId = analyzeSearch.getProductLineId();
        // 产品ID
        MesProduct mesProduct = mesProductDao.findOne(analyzeSearch.getProductId());
        Long productId = Long.valueOf(mesProduct.getModelnum());
        productId = productId != null && productId!= 0L ? productId : null;
        // 工序ID
        Long produceId = analyzeSearch.getProductProcedureId();
        // 产线名
        String productlineNm = "";
        // 产品名
        String productNm = mesProduct.getName();
        for(MesProductline mesProductline : getProductionLineByCurrentCompanyId(analyzeSearch.getProductLineId())){
            if(productlineId.equals(productlineId)) {
                if(null != mesProductline.getCompanyinfo()) {
                    factoryId = mesProductline.getCompanyinfo().getId();
                    productlineNm = mesProductline.getLinename();
                }
                break;
            }
        }
        Map<String, Date> timeForScan = MesDataRowkeyUtil.getScanTimeRangeByHBasePageModel(page);
//        List<Result> productionRecordResultList = new HbaseUtil().getResultListByHbase(factoryId, productlineId, null,
//                productId, produceId, null, timeForScan.get("begin"), timeForScan.get("end"), true);

        MesDataRowkeyUtil.setEntityManager(entityManager);
        String beginTimeS = null == timeForScan.get("begin") ? "" : String.valueOf(timeForScan.get("begin").getTime()).replace("000", "");
        String endTimeS = null == timeForScan.get("end") ? "" : String.valueOf(timeForScan.get("end").getTime()).replace("000", "");
        List<MesDataProductProcedure> mesDataProductProcedureList =MesDataRowkeyUtil.getMulKeyOfPage(String.valueOf(factoryId), String.valueOf(productlineId), "", String.valueOf(productId),
                String.valueOf(produceId),
                beginTimeS,endTimeS, page, "", analyzeSearch.getProductBatchid(),analyzeSearch.getMeastype());
        List<String> statusTimeList = Lists.newArrayList();
        if(null != mesDataProductProcedureList && 0 <mesDataProductProcedureList.size()) {
            String beTime = String.valueOf(mesDataProductProcedureList.get(mesDataProductProcedureList.size() - 1).getMesDataMultiKey().getInsertTimestamp());
            String enTime = String.valueOf(mesDataProductProcedureList.get(0).getMesDataMultiKey().getInsertTimestamp());
            statusTimeList =MesDataRowkeyUtil.getCountByPageSelect(String.valueOf(factoryId), String.valueOf(productlineId), "", String.valueOf(productId),
                    String.valueOf(produceId),
                    beTime,enTime, "NG", analyzeSearch.getProductBatchid());
        }
        
        if(StringUtils.isEmpty(productlineNm)) {
            if(null != mesDataProductProcedureList && 0 < mesDataProductProcedureList.size()) {
                Integer productLineId = mesDataProductProcedureList.get(0).getMesDataMultiKey().getProductLineId();
                MesProductline  mesproductline = mesProductlineDao.findOne(Long.valueOf(String.valueOf(productLineId)));
                productlineNm = mesproductline.getLinename();
            }
        }
        
        List<ProductionRecord> listOut = Lists.newArrayList();
        ProductionRecord productionRecordOut = null;
        for(MesDataProductProcedure obj : mesDataProductProcedureList) {
            productionRecordOut = new ProductionRecord();
            productionRecordOut.setRowkey(MesDataRowkeyUtil.getRowKey(obj));
            // 批次号
            productionRecordOut.setProductBatchid(obj.getProductBatchid());
            // 工件号
            productionRecordOut.setProductSn(obj.getProductBsn());
            // 生产线
            productionRecordOut.setProductlineName(productlineNm);
            // 产品名称
            productionRecordOut.setProductName(productNm);
            // 日期
            productionRecordOut.setDatetime(obj.getMesDataMultiKey().getInsertTimestamp());
            // 状态
            productionRecordOut.setStatus(statusTimeList.contains(String.valueOf(obj.getMesDataMultiKey().getInsertTimestamp())) ? "NG" : "OK");
            // 工序属性ID
            productionRecordOut.setPropertyId(String.valueOf(obj.getProcedurePropertyId()));
            listOut.add(productionRecordOut);
        }

//        ProductionRecord productionRecord = null;
//        List<String> timeSort = Lists.newArrayList();
//        Map<String, ProductionRecord> mapSort = Maps.newHashMap();
//        for (Result result : productionRecordResultList) {
//            Map<String, String> outResult = getRowByResultForOutPut(result);
//            productionRecord = new ProductionRecord();
//            productionRecord.setRowkey(outResult.get("rowkey"));
//            // 工件号
//            productionRecord.setProductSn(outResult.get("productSn"));
//            // 生产线
//            productionRecord.setProductlineName(productlineNm);
//            // 产品名称
//            productionRecord.setProductName(productNm);
//            // 日期
//            productionRecord
//                    .setDatetime(BigInteger.valueOf(Long.valueOf(outResult.get("timestamp"))));
//            // 状态
//            productionRecord.setStatus(outResult.get("status"));
//            // 工序属性ID
//            productionRecord.setPropertyId(outResult.get("propertyValue"));
//            mapSort.put(productionRecord.getDatetime(), productionRecord);
//            timeSort.add(productionRecord.getDatetime());
//
//        }
//        Collections.reverse(timeSort);
//        List<ProductionRecord> list = Lists.newArrayList();
//        for (String timeS : timeSort) {
//            productionRecord = mapSort.get(timeS);
//            list.add(productionRecord);
//        }
        return listOut;
    }
	
	
	/**
	 * 用HBASE
	 * @param page
	 * @return
	 */
//	public List<ProductionRecord> getProductionRecordPagableByHbase(HBasePageModel page){
//		AnalyzeSearch analyzeSearch = page.getAnalyzeSearch();
//		// 工厂ID
//		Long factoryId = null;
//		// 产线ID
//		Long productlineId = analyzeSearch.getProductLineId();
//		// 产品ID
//		MesProduct mesProduct = mesProductDao.findOne(analyzeSearch.getProductId());
//		Long productId = Long.valueOf(mesProduct.getModelnum());
//		productId = productId != null && productId!= 0L ? productId : null;
//		// 工序ID
//		Long produceId = analyzeSearch.getProductProcedureId();
//		// 产线名
//		String productlineNm = "";
//		// 产品名
//		String productNm = mesProduct.getName();
//		for(MesProductline mesProductline : getProductionLineByCurrentCompanyId(analyzeSearch.getProductLineId())){
//			if(productlineId.equals(productlineId)) {
//				if(null != mesProductline.getCompanyinfo()) {
//					factoryId = mesProductline.getCompanyinfo().getId();
//					productlineNm = mesProductline.getLinename();
//				}
//				break;
//			}
//		}
//		Map<String, Date> timeForScan = MesDataRowkeyUtil.getScanTimeRangeByHBasePageModel(page);
//		List<Result> productionRecordResultList = new HbaseUtil().getResultListByHbase(factoryId, productlineId, null,
//				productId, produceId, null, timeForScan.get("begin"), timeForScan.get("end"), true);
//        ProductionRecord productionRecord = null;
//        List<String> timeSort = Lists.newArrayList();
//        Map<String, ProductionRecord> mapSort = Maps.newHashMap();
//        for (Result result : productionRecordResultList) {
//            Map<String, String> outResult = getRowByResultForOutPut(result);
//            productionRecord = new ProductionRecord();
//            productionRecord.setRowkey(outResult.get("rowkey"));
//            // 工件号
//            productionRecord.setProductSn(outResult.get("productSn"));
//            // 生产线
//            productionRecord.setProductlineName(productlineNm);
//            // 产品名称
//            productionRecord.setProductName(productNm);
//            // 日期
//            productionRecord
//                    .setDatetime(BigInteger.valueOf(Long.valueOf(outResult.get("timestamp"))));
//            // 状态
//            productionRecord.setStatus(outResult.get("status"));
//            // 工序属性ID
//            productionRecord.setPropertyId(outResult.get("propertyValue"));
//            mapSort.put(productionRecord.getDatetime(), productionRecord);
//            timeSort.add(productionRecord.getDatetime());
//
//        }
//        Collections.reverse(timeSort);
//        List<ProductionRecord> list = Lists.newArrayList();
//        for (String timeS : timeSort) {
//            productionRecord = mapSort.get(timeS);
//            list.add(productionRecord);
//        }
//        return list;
//	}
	
	/**
	 * 获取当前公司下的所有产线
	 * @return
	 */
	public List<MesProductline> getProductionLineByCurrentCompanyId(){
		return mesProductlineDao.findAll(new Specification<MesProductline>() {
			@Override
			public Predicate toPredicate(Root<MesProductline> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				Long companyId = SecurityUtils.getShiroUser().getCompanyid();
				Predicate p1 = builder.equal(root.get("companyinfo").get("parentid").as(Long.class),companyId);
				return query.where(p1).getRestriction();
			}
		});
	}
	/**
	 * 获取当前产线的所在工厂
	 * @return
	 */
	public List<MesProductline> getProductionLineByCurrentCompanyId(Long productLine){
		return mesProductlineDao.findAll(new Specification<MesProductline>() {
			@Override
			public Predicate toPredicate(Root<MesProductline> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				//Long companyId = SecurityUtils.getShiroUser().getCompanyid();
				Predicate p1 = builder.equal(root.get("id").as(Long.class),productLine);
				return query.where(p1).getRestriction();
			}
		});
	}
    /**
     * 设置HBASE搜索时间范围
     * @param scan
     * @param hBasePageModel
     */
    private void setScanTimeRange(Scan scan, HBasePageModel hBasePageModel, int times) {
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
     * 获取同一个rowkey下的记录集合
     * 
     * @param result
     *            结果集
     * @return
     */
    private Map<String, String> getRowByResultForOutPut(Result result) {
        if (result == null) {
            return null;
        }
        Map<String, String> cellMap = new HashMap<String, String>();
        int index = 0;
        String status = "OK";
        for (Cell cell : result.listCells()) {
            String rowkey = Bytes.toString(cell.getRowArray(), cell.getRowOffset(), cell.getRowLength());
            String qf = Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength());
            if(0 == index) {
                cellMap.put("rowkey", rowkey);
                cellMap.put("productSn", rowkey.split("_")[6]);
                cellMap.put("timestamp", String.valueOf(cell.getTimestamp()).substring(0, 10));
    			cellMap.put("pointId", qf.split("_")[1]);
    			cellMap.put("propertyValue", qf.split("_")[2]);
            }
            if("NG".equals(qf.split("_")[4])) {
            	status = "NG";
            	break;
            }
            index++;
        }
		cellMap.put("status", status);
        return cellMap;
    }

}
