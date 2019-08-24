package com.its.frd.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 设备状态Entiy
 * 
 * @author admin
 *
 */
@Entity
@Table(name = "data_driver_status")
public class MesDataDriverStatus implements java.io.Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    // 复合主键[工厂ID, 产线ID, 设备ID, 测点ID, 数据记录时间]
    @EmbeddedId
    private MesDataMultiKey mesDataMultiKey;

    // 设备状态
    private String driverStatus;

    // 设备状态元数据
    private String metaDriverStatus;

    // 设备属性id
    private Integer driverPropertyId;

    public Integer getDriverPropertyId() {
        return driverPropertyId;
    }

    public void setDriverPropertyId(Integer driverPropertyId) {
        this.driverPropertyId = driverPropertyId;
    }

    public String getDriverStatus() {
        return driverStatus;
    }

    public void setDriverStatus(String driverStatus) {
        this.driverStatus = driverStatus;
    }

    public String getMetaDriverStatus() {
        return metaDriverStatus;
    }

    public void setMetaDriverStatus(String metaDriverStatus) {
        this.metaDriverStatus = metaDriverStatus;
    }

    public MesDataMultiKey getMesDataMultiKey() {
        return mesDataMultiKey;
    }

    public void setMesDataMultiKey(MesDataMultiKey mesDataMultiKey) {
        this.mesDataMultiKey = mesDataMultiKey;
    }
    
}