package com.its.frd.entity;

import java.math.BigInteger;

import javax.persistence.Embeddable;

/**
 * 复合主键key entiy
 * 
 * @author admin
 *
 */
@Embeddable
public class MesDataMultiKey implements java.io.Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 8112272885129263827L;

    // 工厂ID
    protected Integer factoryId;

    // 产线ID
    protected Integer productLineId;

    // 设备ID
    protected Integer driverId;

    // 测点ID
    protected Integer pointId;

    // 数据记录时间
    protected BigInteger insertTimestamp;

    public MesDataMultiKey() {

    }

    public MesDataMultiKey(Integer factoryId, Integer productLineId, Integer driverId, Integer pointId,
            BigInteger insertTimestamp) {
        this.factoryId = factoryId;
        this.productLineId = productLineId;
        this.driverId = driverId;
        this.pointId = pointId;
        this.insertTimestamp = insertTimestamp;
    }

    public Integer getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(Integer factoryId) {
        this.factoryId = factoryId;
    }

    public Integer getProductLineId() {
        return productLineId;
    }

    public void setProductLineId(Integer productLineId) {
        this.productLineId = productLineId;
    }

    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public Integer getPointId() {
        return pointId;
    }

    public void setPointId(Integer pointId) {
        this.pointId = pointId;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public BigInteger getInsertTimestamp() {
        return insertTimestamp;
    }

    public void setInsertTimestamp(BigInteger insertTimestamp) {
        this.insertTimestamp = insertTimestamp;
    }

}