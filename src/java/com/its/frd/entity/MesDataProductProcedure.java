package com.its.frd.entity;

import java.math.BigInteger;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 工序Entiy
 * 
 * @author admin
 *
 */
@Entity
@Table(name = "data_product_procedure")
public class MesDataProductProcedure implements java.io.Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    // 复合主键[工厂ID, 产线ID, 设备ID, 测点ID, 数据记录时间]
    @EmbeddedId
    private MesDataMultiKey mesDataMultiKey;

    // 产品型号 (来自数据包)
    private String productMode;

    // 产品工序数据ID
    private Integer productProcedureId;
    
    private Integer procedurePropertyId;

    // 工件号
    private String productBsn;

    // 批次号
    private String productBatchid;

    // 标准值
    private String standardValue;

    // 最大值
    private String maxValue;

    // 最小值
    private String minValue;

    // 工件元数据信息
    private String metaValue;

    // 工序结果 OK / NG
    private String valueStatus;

    // 网关地址
    private String macAddr;

    //测量类型
    private Integer meastype;

    //合格率
    private Integer qualified;

    public Integer getMeastype() {
        return meastype;
    }

    public void setMeastype(Integer meastype) {
        this.meastype = meastype;
    }

    public Integer getQualified() {
        return qualified;
    }

    public void setQualified(Integer qualified) {
        this.qualified = qualified;
    }

    public String getProductMode() {
        return productMode;
    }

    public void setProductMode(String productMode) {
        this.productMode = productMode;
    }

    public Integer getProductProcedureId() {
        return productProcedureId;
    }

    public void setProductProcedureId(Integer productProcedureId) {
        this.productProcedureId = productProcedureId;
    }

    public String getProductBsn() {
        return productBsn;
    }

    public void setProductBsn(String productBsn) {
        this.productBsn = productBsn;
    }

    public String getStandardValue() {
        return standardValue;
    }

    public void setStandardValue(String standardValue) {
        this.standardValue = standardValue;
    }

    public String getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(String maxValue) {
        this.maxValue = maxValue;
    }

    public String getMinValue() {
        return minValue;
    }

    public void setMinValue(String minValue) {
        this.minValue = minValue;
    }

    public String getMetaValue() {
        return metaValue;
    }

    public void setMetaValue(String metaValue) {
        this.metaValue = metaValue;
    }

    public String getValueStatus() {
        return valueStatus;
    }

    public void setValueStatus(String valueStatus) {
        this.valueStatus = valueStatus;
    }

    public MesDataMultiKey getMesDataMultiKey() {
        return mesDataMultiKey;
    }

    public void setMesDataMultiKey(MesDataMultiKey mesDataMultiKey) {
        this.mesDataMultiKey = mesDataMultiKey;
    }

    public Integer getProcedurePropertyId() {
        return procedurePropertyId;
    }

    public void setProcedurePropertyId(Integer procedurePropertyId) {
        this.procedurePropertyId = procedurePropertyId;
    }

    public String getMacAddr() {
        return macAddr;
    }

    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr;
    }

    public String getProductBatchid() {
        return productBatchid;
    }

    public void setProductBatchid(String productBatchid) {
        this.productBatchid = productBatchid;
    }

}