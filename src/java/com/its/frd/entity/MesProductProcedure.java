package com.its.frd.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "mes_product_procedure")
public class MesProductProcedure implements java.io.Serializable {

	// Fields

	private Long id;
	private MesProduct mesProduct;
	private String procedurenum;
	private String procedurename;
	private String ops;
	private String customername;
	private String partname;
	private String customerpicturenum;
	private Date versiondate;
	private String swpicturenum;
	private String drivermodel;
	private String drivernum;
	private String jipname;
	private String jippicturenum;
	private String operationnum;
	private String operationversion;
	private String materialnum;
	private String materialstandards;
	private String weightsmaterials;
	private String nextprocedurename;
	private String blankshapesize;
	private String makecase;
	private String reservation;
	private String workshop;
	private String remarks;
	private List<MesProcedureProperty> mesProcedureProperties = new ArrayList<MesProcedureProperty>();
	private String rowKey;
	private String sn;
	private String processControlBtn;
	private String processCharacteristicBtn;
	/**
	 * 设备集合
	 */
	private String driveridarray;
	

	// Constructors

	/** default constructor */
	public MesProductProcedure() {
	}

	/** full constructor */
	public MesProductProcedure(MesProduct mesProduct, String procedurenum,
			String procedurename, String ops, String customername,
			String partname, String customerpicturenum, Date versiondate,
			String swpicturenum, String drivermodel, String drivernum,
			String jipname, String jippicturenum, String operationnum,
			String operationversion, String materialnum,
			String materialstandards, String weightsmaterials,
			String nextprocedurename, String blankshapesize, String makecase,
			String reservation, String workshop, String remarks,
			List<MesProcedureProperty> mesProcedureProperties) {
		this.mesProduct = mesProduct;
		this.procedurenum = procedurenum;
		this.procedurename = procedurename;
		this.ops = ops;
		this.customername = customername;
		this.partname = partname;
		this.customerpicturenum = customerpicturenum;
		this.versiondate = versiondate;
		this.swpicturenum = swpicturenum;
		this.drivermodel = drivermodel;
		this.drivernum = drivernum;
		this.jipname = jipname;
		this.jippicturenum = jippicturenum;
		this.operationnum = operationnum;
		this.operationversion = operationversion;
		this.materialnum = materialnum;
		this.materialstandards = materialstandards;
		this.weightsmaterials = weightsmaterials;
		this.nextprocedurename = nextprocedurename;
		this.blankshapesize = blankshapesize;
		this.makecase = makecase;
		this.reservation = reservation;
		this.workshop = workshop;
		this.remarks = remarks;
		this.mesProcedureProperties = mesProcedureProperties;
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
	@JoinColumn(name = "productid")
	public MesProduct getMesProduct() {
		return this.mesProduct;
	}

	public void setMesProduct(MesProduct mesProduct) {
		this.mesProduct = mesProduct;
	}

	@Column(name = "procedurenum", length = 30)
	public String getProcedurenum() {
		return this.procedurenum;
	}

	public void setProcedurenum(String procedurenum) {
		this.procedurenum = procedurenum;
	}

	@Column(name = "procedurename", length = 30)
	public String getProcedurename() {
		return this.procedurename;
	}

	public void setProcedurename(String procedurename) {
		this.procedurename = procedurename;
	}

	@Column(name = "ops", length = 5)
	public String getOps() {
		return this.ops;
	}

	public void setOps(String ops) {
		this.ops = ops;
	}

	@Column(name = "customername", length = 50)
	public String getCustomername() {
		return this.customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	@Column(name = "partname", length = 50)
	public String getPartname() {
		return this.partname;
	}

	public void setPartname(String partname) {
		this.partname = partname;
	}

	@Column(name = "customerpicturenum", length = 50)
	public String getCustomerpicturenum() {
		return this.customerpicturenum;
	}

	public void setCustomerpicturenum(String customerpicturenum) {
		this.customerpicturenum = customerpicturenum;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "versiondate", length = 10)
	public Date getVersiondate() {
		return this.versiondate;
	}

	public void setVersiondate(Date versiondate) {
		this.versiondate = versiondate;
	}

	@Column(name = "swpicturenum", length = 30)
	public String getSwpicturenum() {
		return this.swpicturenum;
	}

	public void setSwpicturenum(String swpicturenum) {
		this.swpicturenum = swpicturenum;
	}

	@Column(name = "drivermodel", length = 30)
	public String getDrivermodel() {
		return this.drivermodel;
	}

	public void setDrivermodel(String drivermodel) {
		this.drivermodel = drivermodel;
	}

	@Column(name = "drivernum", length = 30)
	public String getDrivernum() {
		return this.drivernum;
	}

	public void setDrivernum(String drivernum) {
		this.drivernum = drivernum;
	}

	@Column(name = "jipname", length = 30)
	public String getJipname() {
		return this.jipname;
	}

	public void setJipname(String jipname) {
		this.jipname = jipname;
	}

	@Column(name = "jippicturenum", length = 30)
	public String getJippicturenum() {
		return this.jippicturenum;
	}

	public void setJippicturenum(String jippicturenum) {
		this.jippicturenum = jippicturenum;
	}

	@Column(name = "operationnum", length = 50)
	public String getOperationnum() {
		return this.operationnum;
	}

	public void setOperationnum(String operationnum) {
		this.operationnum = operationnum;
	}

	@Column(name = "operationversion", length = 20)
	public String getOperationversion() {
		return this.operationversion;
	}

	public void setOperationversion(String operationversion) {
		this.operationversion = operationversion;
	}

	@Column(name = "materialnum", length = 30)
	public String getMaterialnum() {
		return this.materialnum;
	}

	public void setMaterialnum(String materialnum) {
		this.materialnum = materialnum;
	}

	@Column(name = "materialstandards", length = 30)
	public String getMaterialstandards() {
		return this.materialstandards;
	}

	public void setMaterialstandards(String materialstandards) {
		this.materialstandards = materialstandards;
	}

	@Column(name = "weightsmaterials", length = 30)
	public String getWeightsmaterials() {
		return this.weightsmaterials;
	}

	public void setWeightsmaterials(String weightsmaterials) {
		this.weightsmaterials = weightsmaterials;
	}

	@Column(name = "nextprocedurename", length = 50)
	public String getNextprocedurename() {
		return this.nextprocedurename;
	}

	public void setNextprocedurename(String nextprocedurename) {
		this.nextprocedurename = nextprocedurename;
	}

	@Column(name = "blankshapesize", length = 50)
	public String getBlankshapesize() {
		return this.blankshapesize;
	}

	public void setBlankshapesize(String blankshapesize) {
		this.blankshapesize = blankshapesize;
	}

	@Column(name = "makecase", length = 30)
	public String getMakecase() {
		return this.makecase;
	}

	public void setMakecase(String makecase) {
		this.makecase = makecase;
	}

	@Column(name = "reservation", length = 30)
	public String getReservation() {
		return this.reservation;
	}

	public void setReservation(String reservation) {
		this.reservation = reservation;
	}

	@Column(name = "workshop", length = 30)
	public String getWorkshop() {
		return this.workshop;
	}

	public void setWorkshop(String workshop) {
		this.workshop = workshop;
	}

	@Column(name = "remarks")
	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mesProductProcedure")
	public List<MesProcedureProperty> getMesProcedureProperties() {
		return this.mesProcedureProperties;
	}

	public void setMesProcedureProperties(
			List<MesProcedureProperty> mesProcedureProperties) {
		this.mesProcedureProperties = mesProcedureProperties;
	}

	@Transient
	public String getRowKey() {
		return rowKey;
	}

	public void setRowKey(String rowKey) {
		this.rowKey = rowKey;
	}
	@Transient
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}
	@Transient
	public String getProcessControlBtn() {
		return processControlBtn;
	}

	public void setProcessControlBtn(String processControlBtn) {
		this.processControlBtn = processControlBtn;
	}
	@Transient
	public String getProcessCharacteristicBtn() {
		return processCharacteristicBtn;
	}

	public void setProcessCharacteristicBtn(String processCharacteristicBtn) {
		this.processCharacteristicBtn = processCharacteristicBtn;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MesProductProcedure other = (MesProductProcedure) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getDriveridarray() {
		return driveridarray;
	}

	public void setDriveridarray(String driveridarray) {
		this.driveridarray = driveridarray;
	}
	
	
}