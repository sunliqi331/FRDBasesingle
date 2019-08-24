package com.its.frd.entity;

import static javax.persistence.GenerationType.IDENTITY;
import java.sql.Timestamp;
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

/**
 * @author ldc
 *
 */
@Entity
@Table(name = "procedure_update_data_manage")
public class ProcedureUpdateDataManage implements java.io.Serializable {

	/**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    private Long id;
	private MesProductline mesProductline;
	private MesDrivertype mesDrivertype;
	private Companyinfo companyinfo;
	private String sn;
	private String name;
	private String modelnumber;
	private String description;
	private String remarks;
	private String brand;
	private Timestamp createtime;
	private String differencetype;
	private Date leavefactorydate;
	private String driverStatus; //设备状态
	private String button; //实时数据按钮
	private String configPoint; //实时数据按钮
	private List<MesDriverrepair> mesDriverrepairs = new ArrayList<MesDriverrepair>();
	//private List<MesSubdriver> mesSubdrivers = new ArrayList<MesSubdriver>();
	private List<ProductionRecord> productionRecords = new ArrayList<ProductionRecord>();
	//private List<MesProductline> mesProductlines = new ArrayList<MesProductline>();
	private List<MesDriverPoints> mesDriverPointses = new ArrayList<MesDriverPoints>();
	private List<MesEnergy> mesEnergies = new ArrayList<MesEnergy>(0);
	private List<MesDriverFaultTime> mesDriverFaultTimes = new ArrayList<MesDriverFaultTime>();
	// Constructors

	/** default constructor */
	public ProcedureUpdateDataManage() {
	}

	/** minimal constructor */
	public ProcedureUpdateDataManage(Companyinfo companyinfo, String sn) {
		this.companyinfo = companyinfo;
		this.sn = sn;
	}

	/** full constructor */
	public ProcedureUpdateDataManage(MesProductline mesProductline,
			MesDrivertype mesDrivertype, 
			Companyinfo companyinfo, String sn,
			String name, String modelnumber,
			String description, String remarks, String brand,
			Timestamp createtime,String button, String configPoint,
			List<MesDriverrepair> mesDriverrepairs//,
			//List<MesDriverProperty> mesDriverProperties
			//,List<MesSubdriver> mesSubdrivers
			) {
		this.mesProductline = mesProductline;
		//this.mesDrivertype = mesDrivertype;
		this.companyinfo = companyinfo;
		this.sn = sn;
		this.name = name;
		this.modelnumber = modelnumber;
		this.description = description;
		this.remarks = remarks;
		this.brand = brand;
		this.createtime = createtime;
		this.button = button;
		this.configPoint = configPoint;
		this.mesDriverrepairs = mesDriverrepairs;
		//this.mesDriverProperties = mesDriverProperties;
		//this.mesSubdrivers = mesSubdrivers;
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
	@JoinColumn(name = "productlineid")
	//@JsonIgnore
	public MesProductline getMesProductline() {
		return this.mesProductline;
	}

	public void setMesProductline(MesProductline mesProductline) {
		this.mesProductline = mesProductline;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "drivertypeid")
	//@JsonIgnore
	public MesDrivertype getMesDrivertype() {
		return this.mesDrivertype;
	}

	public void setMesDrivertype(MesDrivertype mesDrivertype) {
		this.mesDrivertype = mesDrivertype;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "companyid", nullable = false)
	//@JsonIgnore
	public Companyinfo getCompanyinfo() {
		return this.companyinfo;
	}

	public void setCompanyinfo(Companyinfo companyinfo) {
		this.companyinfo = companyinfo;
	}

	@Column(name = "sn", nullable = false, length = 30)
	public String getSn() {
		return this.sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	@Column(name = "name", length = 30)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Transient
    public String getButton() {
        return button;
    }

    public void setButton(String button) {
        this.button = button;
    }

    @Transient
    public String getConfigPoint() {
        return configPoint;
    }

    public void setConfigPoint(String configPoint) {
        this.configPoint = configPoint;
    }

    @Column(name = "modelnumber", length = 30)
	public String getModelnumber() {
		return this.modelnumber;
	}

	public void setModelnumber(String modelnumber) {
		this.modelnumber = modelnumber;
	}

	@Column(name = "description", length = 100)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "remarks", length = 30)
	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Column(name = "brand", length = 30)
	public String getBrand() {
		return this.brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

    @Column(name = "createtime", nullable = false, length = 19)
    public Timestamp getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Timestamp createtime) {
        this.createtime = createtime;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mesDriver")
	@JsonIgnore
	public List<MesDriverrepair> getMesDriverrepairs() {
		return this.mesDriverrepairs;
	}

	public void setMesDriverrepairs(List<MesDriverrepair> mesDriverrepairs) {
		this.mesDriverrepairs = mesDriverrepairs;
	}

	/*@OneToMany(cascade={CascadeType.PERSIST, CascadeType.REMOVE},mappedBy = "mesDriver",orphanRemoval=true)
	@JsonIgnore
	public List<MesDriverProperty> getMesDriverProperties() {
		return this.mesDriverProperties;
	}

	public void setMesDriverProperties(
			List<MesDriverProperty> mesDriverProperties) {
		this.mesDriverProperties = mesDriverProperties;
	}*/

	/*@JsonIgnore
	@OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "mesDriver")
	public List<MesSubdriver> getMesSubdrivers() {
		return this.mesSubdrivers;
	}

	public void setMesSubdrivers(List<MesSubdriver> mesSubdrivers) {
		this.mesSubdrivers = mesSubdrivers;
	}*/
	/*@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mesDriver")
	public List<MesProductline> getMesProductlines() {
		return this.mesProductlines;
	}

	public void setMesProductlines(List<MesProductline> mesProductlines) {
		this.mesProductlines = mesProductlines;
	}*/
	@OneToMany(cascade=CascadeType.MERGE,fetch=FetchType.LAZY,mappedBy="mesDriver",targetEntity=ProductionRecord.class)
	@JsonIgnore
	public List<ProductionRecord> getProductionRecords() {
		return productionRecords;
	}

	public void setProductionRecords(List<ProductionRecord> productionRecords) {
		this.productionRecords = productionRecords;
	}
	
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mesDriver")
	public List<MesDriverPoints> getMesDriverPointses() {
		return this.mesDriverPointses;
	}

	public void setMesDriverPointses(List<MesDriverPoints> mesDriverPointses) {
		this.mesDriverPointses = mesDriverPointses;
	}
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mesDriver")
	public List<MesEnergy> getMesEnergies() {
		return this.mesEnergies;
	}

	public void setMesEnergies(List<MesEnergy> mesEnergies) {
		this.mesEnergies = mesEnergies;
	}
	@Column(name = "differencetype", length = 10)
	public String getDifferencetype() {
		return differencetype;
	}

	public void setDifferencetype(String differencetype) {
		this.differencetype = differencetype;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "leavefactorydate", length = 10)
	public Date getLeavefactorydate() {
		return this.leavefactorydate;
	}

	public void setLeavefactorydate(Date leavefactorydate) {
		this.leavefactorydate = leavefactorydate;
	}
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mesDriver")
	public List<MesDriverFaultTime> getMesDriverFaultTimes() {
		return this.mesDriverFaultTimes;
	}

	public void setMesDriverFaultTimes(
			List<MesDriverFaultTime> mesDriverFaultTimes) {
		this.mesDriverFaultTimes = mesDriverFaultTimes;
	}
	@Transient
	public String getDriverStatus() {
		return driverStatus;
	}

	public void setDriverStatus(String driverStatus) {
		this.driverStatus = driverStatus;
	}
	
}