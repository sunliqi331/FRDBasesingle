package com.its.frd.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 设备属性Entiy
 * 
 * @author admin
 *
 */
@Entity
@Table(name = "data_driver_property")
public class MesDataDriverProperty implements java.io.Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 7709461920667710734L;

    // 复合主键[工厂ID, 产线ID, 设备ID, 测点ID, 数据记录时间]
    @EmbeddedId
    private MesDataMultiKey mesDataMultiKey;

    // 设备属性ID
    private Integer driverPropertyId;

    // 设备属性元数据
    private String metaPropertyValue;

    // 设备属性值
    private String driverPropertyValue;

    public Integer getDriverPropertyId() {
        return driverPropertyId;
    }

    public void setDriverPropertyId(Integer driverPropertyId) {
        this.driverPropertyId = driverPropertyId;
    }

    public String getMetaPropertyValue() {
        return metaPropertyValue;
    }

    public void setMetaPropertyValue(String metaPropertyValue) {
        this.metaPropertyValue = metaPropertyValue;
    }

    public String getDriverPropertyValue() {
        return driverPropertyValue;
    }

    public void setDriverPropertyValue(String driverPropertyValue) {
        this.driverPropertyValue = driverPropertyValue;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public MesDataMultiKey getMesDataMultiKey() {
        return mesDataMultiKey;
    }

    public void setMesDataMultiKey(MesDataMultiKey mesDataMultiKey) {
        this.mesDataMultiKey = mesDataMultiKey;
    }
}