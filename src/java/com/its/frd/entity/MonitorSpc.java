package com.its.frd.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.camel.util.Time;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "monitor_spc")
public class MonitorSpc {
    private int id;
    private Long monitorPainterId;
    private Long driverPropertyId;
    private String chartId;
    private Double cp;
    private Double cpk;
    private Double pp;
    private Double ppk;
    private Timestamp createtime;
    private Long productId;
    private Long productProcedureId;
    private Long mesDriverId;
    private Long subrange;
    private Long subnum;



    @Id
    @Column(name = "id", insertable=false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "monitor_painter_id")
    public Long getMonitorPainterId() {
        return monitorPainterId;
    }

    public void setMonitorPainterId(Long monitorPainterId) {
        this.monitorPainterId = monitorPainterId;
    }

    @Basic
    @Column(name = "driver_property_id")
    public Long getDriverPropertyId() {
        return driverPropertyId;
    }

    public void setDriverPropertyId(Long driverPropertyId) {
        this.driverPropertyId = driverPropertyId;
    }

    @Basic
    @Column(name = "chart_id")
    public String getChartId() {
        return chartId;
    }

    public void setChartId(String chartId) {
        this.chartId = chartId;
    }

    @Basic
    @Column(name = "cp")
    public Double getCp() {
        return cp;
    }

    public void setCp(Double cp) {
        this.cp = cp;
    }

    @Basic
    @Column(name = "cpk")
    public Double getCpk() {
        return cpk;
    }

    public void setCpk(Double cpk) {
        this.cpk = cpk;
    }

    @Basic
    @Column(name = "pp")
    public Double getPp() {
        return pp;
    }

    public void setPp(Double pp) {
        this.pp = pp;
    }

    @Basic
    @Column(name = "ppk")
    public Double getPpk() {
        return ppk;
    }

    public void setPpk(Double ppk) {
        this.ppk = ppk;
    }

    @Basic
    @Column(name = "createtime", insertable=false)
//    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    public Timestamp getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Timestamp createtime) {
        this.createtime = createtime;
    }

    @Basic
    @Column(name = "product_id")
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Basic
    @Column(name = "product_procedure_id")
    public Long getProductProcedureId() {
        return productProcedureId;
    }

    public void setProductProcedureId(Long productProcedureId) {
        this.productProcedureId = productProcedureId;
    }

    @Basic
    @Column(name = "mes_driver_id")
    public Long getMesDriverId() {
        return mesDriverId;
    }

    public void setMesDriverId(Long mesDriverId) {
        this.mesDriverId = mesDriverId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MonitorSpc that = (MonitorSpc) o;
        return id == that.id &&
                Objects.equals(monitorPainterId, that.monitorPainterId) &&
                Objects.equals(driverPropertyId, that.driverPropertyId) &&
                Objects.equals(chartId, that.chartId) &&
                Objects.equals(cp, that.cp) &&
                Objects.equals(cpk, that.cpk) &&
                Objects.equals(pp, that.pp) &&
                Objects.equals(ppk, that.ppk) &&
                Objects.equals(createtime, that.createtime) &&
                Objects.equals(productId, that.productId) &&
                Objects.equals(productProcedureId, that.productProcedureId) &&
                Objects.equals(mesDriverId, that.mesDriverId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, monitorPainterId, driverPropertyId, chartId, cp, cpk, pp, ppk, createtime, productId, productProcedureId, mesDriverId);
    }

    @Basic
    @Column(name = "subrange")
    public Long getSubrange() {
        return subrange;
    }

    public void setSubrange(Long subrange) {
        this.subrange = subrange;
    }

    @Basic
    @Column(name = "subnum")
    public Long getSubnum() {
        return subnum;
    }

    public void setSubnum(Long subnum) {
        this.subnum = subnum;
    }
}
