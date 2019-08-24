package com.its.statistics.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

public class SpcAnalyzeSearch extends AnalyzeSearch {
    private Double cp;
    private Double cpk;
    private Double pp;
    private Double ppk;
    private Timestamp createtime;
    private String mesDriverName;

    public Double getCp() {
        return cp;
    }

    public void setCp(Double cp) {
        this.cp = cp;
    }

    public Double getCpk() {
        return cpk;
    }

    public void setCpk(Double cpk) {
        this.cpk = cpk;
    }

    public Double getPp() {
        return pp;
    }

    public void setPp(Double pp) {
        this.pp = pp;
    }

    public Double getPpk() {
        return ppk;
    }

    public void setPpk(Double ppk) {
        this.ppk = ppk;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    public Timestamp getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Timestamp createtime) {
        this.createtime = createtime;
    }

    public String getMesDriverName() {
        return mesDriverName;
    }

    public void setMesDriverName(String mesDriverName) {
        this.mesDriverName = mesDriverName;
    }
}
