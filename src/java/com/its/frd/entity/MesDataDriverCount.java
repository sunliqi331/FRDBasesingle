package com.its.frd.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 设备产量Entiy
 * 
 * @author admin
 *
 */
@Entity
@Table(name = "data_driver_count")
public class MesDataDriverCount implements java.io.Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    // 复合主键[工厂ID, 产线ID, 设备ID, 测点ID, 数据记录时间]
    @EmbeddedId
    private MesDataMultiKey mesDataMulrtiKey;

    // 设备产量元数据
    private String metaDriverCount;

    // 设备产量
    private Integer driverCount;

    public MesDataDriverCount() {
        super();
    }

    public String getMetaDriverCount() {
        return metaDriverCount;
    }

    public void setMetaDriverCount(String metaDriverCount) {
        this.metaDriverCount = metaDriverCount;
    }

    public Integer getDriverCount() {
        return driverCount;
    }

    public void setDriverCount(Integer driverCount) {
        this.driverCount = driverCount;
    }

    public MesDataMultiKey getMesDataMulrtiKey() {
        return mesDataMulrtiKey;
    }

    public void setMesDataMulrtiKey(MesDataMultiKey mesDataMulrtiKey) {
        this.mesDataMulrtiKey = mesDataMulrtiKey;
    }

}