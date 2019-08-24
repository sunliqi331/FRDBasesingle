package com.its.frd.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 图片上传管理Entiy
 * 
 * @author admin
 *
 */
@Entity
@Table(name = "quality_img_manage")
public class MesQualityImg implements java.io.Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -7208261957422513548L;

    // Fields
    private Long id;

    // 工厂ID
    private Long factoryid;

    // 质检图片ID
    private String qualityimageid;

    // 质检图片名
    private String qualitynm;

    // 质检图片上传日期
    private Date createdate;

    // 质检图片修改日期
    private Date updatedate;

    // 质检图片状态
    private String status;

    private String showPic;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "status", length = 255)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getFactoryid() {
        return factoryid;
    }

    public void setFactoryid(Long factoryid) {
        this.factoryid = factoryid;
    }

    public String getQualitynm() {
        return qualitynm;
    }

    public void setQualitynm(String qualitynm) {
        this.qualitynm = qualitynm;
    }

    public String getQualityimageid() {
        return qualityimageid;
    }

    public void setQualityimageid(String qualityimageid) {
        this.qualityimageid = qualityimageid;
    }

    @Transient
    public String getShowPic() {
        return showPic;
    }

    public void setShowPic(String showPic) {
        this.showPic = showPic;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    public Date getUpdatedate() {
        return updatedate;
    }

    public void setUpdatedate(Date updatedate) {
        this.updatedate = updatedate;
    }
}