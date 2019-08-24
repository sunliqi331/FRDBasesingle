package com.its.frd.entity;

import java.sql.Timestamp;
import java.util.Date;
import javax.annotation.Resource;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.its.frd.service.CompanyinfoService;

@Entity
@Table(name = "mes_product_alarm")
public class MesProductAlarm implements java.io.Serializable {

	// Fields

	private Long id;
	private MesProcedureProperty mesProcedureProperty;
	private String productmodelnum;
	private String productsn;
	private Date updatetime;
	private String statecode;
	private String tstatecode;
	private Double errorchangevalue;
	private Long pointid;
	private MesProduct mesProduct;
	

	private String companyname;
	private String productline;
	  @Resource
	private CompanyinfoService cpServ;

	// Constructors

	/** default constructor */
	public MesProductAlarm() {
	}

	/** minimal constructor */
	public MesProductAlarm(Timestamp updatetime) {
		this.updatetime = updatetime;
	}

	/** full constructor */
	public MesProductAlarm(MesProcedureProperty mesProcedureProperty,
			String productmodelnum, String productsn, Timestamp updatetime,
			String statecode, String tstatecode, Double errorchangevalue, Long pointid) {
		this.mesProcedureProperty = mesProcedureProperty;
		this.productmodelnum = productmodelnum;
		this.productsn = productsn;
		this.updatetime = updatetime;
		this.statecode = statecode;
		this.tstatecode = tstatecode;
		this.errorchangevalue = errorchangevalue;
		this.pointid = pointid;
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
	@JoinColumn(name = "procedurepropertyid")
	public MesProcedureProperty getMesProcedureProperty() {
		return this.mesProcedureProperty;
	}

	public void setMesProcedureProperty(
			MesProcedureProperty mesProcedureProperty) {
		this.mesProcedureProperty = mesProcedureProperty;
	}

	@Transient
	public String getProductmodelnum() {
		return this.mesProcedureProperty.getMesProductProcedure().getMesProduct().getModelnum();
	}

	public void setProductmodelnum(String productmodelnum) {
		this.productmodelnum = productmodelnum;
	}

	@Column(name = "productsn", length = 100)
	public String getProductsn() {
		return this.productsn;
	}

	public void setProductsn(String productsn) {
		this.productsn = productsn;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updatetime", nullable = false, length = 19)
	public Date getUpdatetime() {
		return this.updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	@Column(name = "statecode", length = 5)
	public String getStatecode() {
		return this.statecode;
	}

	public void setStatecode(String statecode) {
		this.statecode = statecode;
	}

	@Transient
	public String getTstatecode() {
        return tstatecode;
    }

    public void setTstatecode(String tstatecode) {
        this.tstatecode = tstatecode;
    }

    @Column(name = "errorchangevalue", precision = 22, scale = 0)
	public Double getErrorchangevalue() {
		return this.errorchangevalue;
	}

	public void setErrorchangevalue(Double errorchangevalue) {
		this.errorchangevalue = errorchangevalue;
	}

	@Column(name = "pointid")
	public Long getPointid() {
		return this.pointid;
	}

	public void setPointid(Long pointid) {
		this.pointid = pointid;
	}

	@Transient
	public String getCompanyname() {
		/*return this.mesProcedureProperty.getMesProductProcedure().getMesProduct().getCompanyinfo().getCompanyname();*/
		//return this.mesProcedureProperty.getMesPoints().getCurrentMesProductline().getCompanyinfo().getCompanyname();
		/*return this.mesProcedureProperty.getMesPoints().getMesPointGateway().getCompanyinfo().getCompanyname();*/
		return this.mesProcedureProperty.getMesPoints() !=null ? 
				(this.mesProcedureProperty.getMesPoints().getCurrentMesProductline() !=null ?  
						(this.mesProcedureProperty.getMesPoints().getCurrentMesProductline().getCompanyinfo() !=null ?
								this.mesProcedureProperty.getMesPoints().getCurrentMesProductline().getCompanyinfo().getCompanyname() : "") : "") : "";

	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}
	@Transient
	public String getProductline() {
		return this.mesProcedureProperty.getMesPoints().getCurrentMesProductline() !=null ? this.mesProcedureProperty.getMesPoints().getCurrentMesProductline().getLinename() : this.productline;
		/*return this.mesProcedureProperty.getMesPoints().getMesPointGateway().getMesDriver().getMesProductline().getLinename();*/
	}

	public void setProductline(String productline) {
		this.productline = productline;
	}
	@Transient
	public MesProduct getMesProduct() {
		return this.mesProcedureProperty.getMesProductProcedure().getMesProduct();
	}

	public void setMesProduct(MesProduct mesProduct) {
		this.mesProduct = mesProduct;
	}

}