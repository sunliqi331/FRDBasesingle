package com.its.statistics.vo;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.its.frd.entity.MesProductProcedure;
import com.its.frd.util.DateUtils;

public class ProductionRecord implements Serializable{

	private BigInteger id;

	/**
	 * 产线名
	 */
	private BigInteger productId;

	/**
	 * 产品名
	 */
	private String productName;
	
	/**
	 * 工厂ID
	 */
	private BigInteger factoryId;
	
	/**
	 * 工厂名
	 */
	private String factoryName;
	
	/**
	 * 公司ID
	 */
	private BigInteger companyId;
	
	/**
	 * 公司名
	 */
	private String companyName;
	
	/**
	 * 公司ID
	 */
	private BigInteger productlineId;
	
	/**
	 * 产线名
	 */
	private String productlineName;
	
	/**
	 * 工件
	 */
	private String productSn;
	
/**
     * 批次号
     */
    private String productBatchid;
	
	private BigInteger datetime;
	
	/**
	 * 设备ID
	 */
	private BigInteger mesDriverId;
	
	/**
	 * 设备名
	 */
	private String mesDriverName;
	
	private String codeKey;
	
	/**
	 * 行键
	 */
	private String rowkey;
	
	/**
	 * 网管
	 */
	private String gateway;
	
	/**
	 * 状态
	 */
	private String status = "NG";
	
	/**
	 * 工序ID
	 */
	private BigInteger procedureId;
	
	/**
	 * 工序名
	 */
	private String procedureName;
	
	/**
	 * 属性ID
	 */
	private String propertyId = "";
	
	/**
	 * 属性名
	 */
	private String propertyNm = "";


	//测量类型
	private String meastype;

	//合格率
	private String qualified;
	
	//private List<MesProductProcedure> list = new ArrayList<>();

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public BigInteger getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(BigInteger factoryId) {
		this.factoryId = factoryId;
	}

	public String getFactoryName() {
		return factoryName;
	}

	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}

	public BigInteger getCompanyId() {
		return companyId;
	}

	public void setCompanyId(BigInteger companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public BigInteger getProductlineId() {
		return productlineId;
	}

	public void setProductlineId(BigInteger productlineId) {
		this.productlineId = productlineId;
	}

	public String getProductlineName() {
		return productlineName;
	}

	public void setProductlineName(String productlineName) {
		this.productlineName = productlineName;
	}

	public BigInteger getProductId() {
		return productId;
	}

	public void setProductId(BigInteger productId) {
		this.productId = productId;
	}


	public String getProductSn() {
		return productSn;
	}

	public void setProductSn(String productSn) {
		this.productSn = productSn;
	}

	public String getDatetime() {
		String date = DateUtils.unixTimestampToDate(Long.parseLong(String.valueOf(datetime)+"000"));
		return date;
	}

	public void setDatetime(BigInteger datetime) {
		this.datetime = datetime;
	}

	public BigInteger getMesDriverId() {
		return mesDriverId;
	}

	public void setMesDriverId(BigInteger mesDriverId) {
		this.mesDriverId = mesDriverId;
	}

	public String getMesDriverName() {
		return mesDriverName;
	}

	public void setMesDriverName(String mesDriverName) {
		this.mesDriverName = mesDriverName;
	}

	public String getCodeKey() {
		return codeKey;
	}

	public void setCodeKey(String codeKey) {
		this.codeKey = codeKey;
	}

	public String getRowkey() {
		return rowkey;
	}

	public void setRowkey(String rowkey) {
		this.rowkey = rowkey;
	}

	public String getGateway() {
		return gateway;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	/*public List<MesProductProcedure> getList() {
		return list;
	}

	public void setList(List<MesProductProcedure> list) {
		this.list = list;
	}*/

	public BigInteger getProcedureId() {
		return procedureId;
	}

	public void setProcedureId(BigInteger procedureId) {
		this.procedureId = procedureId;
	}

	public String getProcedureName() {
		return procedureName;
	}

	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}

	public String getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	public String getPropertyNm() {
		return propertyNm;
	}

	public void setPropertyNm(String propertyNm) {
		this.propertyNm = propertyNm;
	}

    public String getProductBatchid() {
        return productBatchid;
    }

    public void setProductBatchid(String productBatchid) {
        this.productBatchid = productBatchid;
    }

	public String getMeastype() {
		return meastype;
	}

	public void setMeastype(String meastype) {
		this.meastype = meastype;
	}

	public String getQualified() {
		return qualified;
	}

	public void setQualified(String qualified) {
		this.qualified = qualified;
	}
}


