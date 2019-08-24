package com.its.frd.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 水电气Entiy
 * 
 * @author admin
 *
 */
@Entity
@Table(name = "data_weg")
public class MesDataWeg implements java.io.Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    // 复合主键[工厂ID, 产线ID, 设备ID, 测点ID, 数据记录时间]
    @EmbeddedId
    private MesDataMultiKey mesDataMultiKey;

    // 类型 WATER | GAS | ELECTRIC
    private String type;

    // 水电气元数据
    private String metaValue;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMetaValue() {
        return metaValue;
    }

    public void setMetaValue(String metaValue) {
        this.metaValue = metaValue;
    }

    public MesDataMultiKey getMesDataMultiKey() {
        return mesDataMultiKey;
    }

    public void setMesDataMultiKey(MesDataMultiKey mesDataMultiKey) {
        this.mesDataMultiKey = mesDataMultiKey;
    }
}