package com.its.frd.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.sql.Timestamp;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by zq on 2019-05-05.
 */


@Entity
@Table(name = "measuringtoolalarm")
public class MeasuringToolAlarm {

    //量具报警ID
    private Long id;
    //量具
    private MeasuringTool measuringTool;
    //预警时间
    private Timestamp alarmtime;
    //记录时间
    private Timestamp recordtime;
    //报警时间
    private Timestamp endtime;
    //超出报警时长
    private Long hours;
    //报警状态 1黄色表示预警期，2红色表示报警期
    private Integer status;
    private String statusbutton;


    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "measuringToolId", nullable = false)
    public MeasuringTool getMeasuringTool() {
        return measuringTool;
    }
    public void setMeasuringTool(MeasuringTool measuringTool) {
        this.measuringTool = measuringTool;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @Column(name = "alarmtime", nullable = false, length = 19)
    public Timestamp getAlarmtime() {
        return alarmtime;
    }
    public void setAlarmtime(Timestamp alarmtime) {
        this.alarmtime = alarmtime;
    }


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @Column(name = "recordtime", nullable = false, length = 19)
    public Timestamp getRecordtime() {
        return recordtime;
    }
    public void setRecordtime(Timestamp recordtime) {
        this.recordtime = recordtime;
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
}
