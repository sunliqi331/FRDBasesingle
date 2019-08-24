package com.its.frd.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.sql.Timestamp;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by zq on 2019-05-05.
 */


@Entity
@Table(name = "measuringtool")
public class MeasuringTool {

    //量具ID
    private Long id;
    //编号
    private String sn;
    //名称
    private String name;
    //类型
    private String type;
    //规格
    private String Specifications;

//    //工序
//    private MesProductProcedure mesProductProcedure;
//    private String productAddprocedureName;

    //spc站点
    private Integer spcsite;
    private String spcsiteName;



    //产线
    private MesProductline mesProductline;
    //检测时间
    private Timestamp testingtime;
    //开始使用时间
    private Timestamp starttime;
    //结束使用时间
    private Timestamp endtime;
    //使用时长(单位天)
    private Long hours;
    //预警天数 改成小时，字段不变
    private Long days;


    //使用状态 0表示启用，1表示停用，2表示检修
    private Integer isenabled;
    private String isenabledshow;

    //状态描述
    private String description;

    //报警显示 0绿色表示是正常，1黄色表示报警周，2红色表示超出报警
    private Integer status;
    private String statusbutton;

    //是否删除 0表示正常，1表示删除
    private Integer isdelete;


    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "sn", length = 60)
    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    @Column(name = "name", length = 60)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "type", length = 60)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "specifications", length = 60)
    public String getSpecifications() {
        return Specifications;
    }

    public void setSpecifications(String specifications) {
        Specifications = specifications;
    }

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "mesProductProcedureId", nullable = false)
//    public MesProductProcedure getMesProductProcedure() {
//        return mesProductProcedure;
//    }
//
//    public void setMesProductProcedure(MesProductProcedure mesProductProcedure) {
//        this.mesProductProcedure = mesProductProcedure;
//    }
//    @Transient
//    public String getProductAddprocedureName() {
//        return productAddprocedureName;
//    }
//
//    public void setProductAddprocedureName(String productAddprocedureName) {
//        this.productAddprocedureName = productAddprocedureName;
//    }

    @Column(name = "spcsite")
    public Integer getSpcsite() {
        return spcsite;
    }
    public void setSpcsite(Integer spcsite) {
        this.spcsite = spcsite;
    }

    @Transient
    public String getSpcsiteName() {
        return spcsiteName;
    }
    public void setSpcsiteName(String spcsiteName) {
        this.spcsiteName = spcsiteName;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productlineid")
    public MesProductline getMesProductline() {
        return this.mesProductline;
    }

    public void setMesProductline(MesProductline mesProductline) {
        this.mesProductline = mesProductline;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @Column(name = "testingtime", nullable = false, length = 19)
    public Timestamp getTestingtime() {
        return testingtime;
    }

    public void setTestingtime(Timestamp testingtime) {
        this.testingtime = testingtime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @Column(name = "starttime", nullable = false, length = 19)
    public Timestamp getStarttime() {
        return starttime;
    }

    public void setStarttime(Timestamp starttime) {
        this.starttime = starttime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @Column(name = "endtime", nullable = false, length = 19)
    public Timestamp getEndtime() {
        return endtime;
    }

    public void setEndtime(Timestamp endtime) {
        this.endtime = endtime;
    }

    @Column(name = "hours")
    public Long getHours() {
        return hours;
    }

    public void setHours(Long hours) {
        this.hours = hours;
    }

    @Column(name = "days")
    public Long getDays() {
        return days;
    }

    public void setDays(Long days) {
        this.days = days;
    }

    @Column(name = "isenabled")
    public Integer getIsenabled() {
        return isenabled;
    }

    public void setIsenabled(Integer isenabled) {
        this.isenabled = isenabled;
    }

    @Transient
    public String getIsenabledshow() {
        return isenabledshow;
    }

    public void setIsenabledshow(String isenabledshow) {
        this.isenabledshow = isenabledshow;
    }


    @Column(name = "description", length = 60)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Transient
    public String getStatusbutton() {
        return statusbutton;
    }

    public void setStatusbutton(String statusbutton) {
        this.statusbutton = statusbutton;
    }

    @Column(name = "isdelete")
    public Integer getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(Integer isdelete) {
        this.isdelete = isdelete;
    }


}
