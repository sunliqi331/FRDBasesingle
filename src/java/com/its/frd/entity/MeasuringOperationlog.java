package com.its.frd.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by zq on 2019-06-03.
 */

@Entity
@Table(name = "measuringoperationlog")
public class MeasuringOperationlog {

    //量具操作日志ID
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

//    //量具
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "measuringToolId", nullable = false)
//    private MeasuringTool measuringTool;

    //编号
    @Column(length=32)
    private String sn;

    //名称
    @Column(length=32)
    private String name;

    //操作人
    @Column(length=32)
    private String username;

    //信息
    @Column(length=256)
    private String message;

    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable=false)
    private Date createTime;

    //是否删除 0表示正常，1表示删除
    @Column(name = "isdelete")
    private Integer isdelete;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public MeasuringTool getMeasuringTool() {
//        return measuringTool;
//    }
//
//    public void setMeasuringTool(MeasuringTool measuringTool) {
//        this.measuringTool = measuringTool;
//    }


    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(Integer isdelete) {
        this.isdelete = isdelete;
    }
}
