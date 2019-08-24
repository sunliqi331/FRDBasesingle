package com.its.frd.entity;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.its.common.entity.main.User;

@Entity
@Table(name = "mes_alarm_send_relation")
public class MesAlarmSendRelation implements java.io.Serializable {

	// Fields

	private Long id;
	private User user;
	private Long companyid;
	private String alarttype;
	private String talarttype;
	private Date creattime;

	// Constructors

	/** default constructor */
	public MesAlarmSendRelation() {
	}

	/** minimal constructor */
	public MesAlarmSendRelation(Date creattime) {
		this.creattime = creattime;
	}

	/** full constructor */
	public MesAlarmSendRelation(User user, Long companyid,
	        String alarttype, String talarttype, Timestamp creattime) {
		this.user = user;
		this.companyid = companyid;
		this.alarttype = alarttype;
		this.talarttype = talarttype;
		this.creattime = creattime;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userid")
	public User getUser() {
		return this.user;
	}

	public void setUser(User ketaUser) {
		this.user = ketaUser;
	}

	@Column(name = "companyid")
	public Long getCompanyid() {
		return this.companyid;
	}

	public void setCompanyid(Long companyid) {
		this.companyid = companyid;
	}

	@Column(name = "alarttype", length = 30)
	public String getAlarttype() {
		return this.alarttype;
	}

	public void setAlarttype(String alarttype) {
		this.alarttype = alarttype;
	}

	@Transient
	public String getTalarttype() {
        return talarttype;
    }

    public void setTalarttype(String talarttype) {
        this.talarttype = talarttype;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	@Column(name = "creattime", nullable = false, length = 19)
	public Date getCreattime() {
		return this.creattime;
	}

	public void setCreattime(Date creattime) {
		this.creattime = creattime;
	}

}