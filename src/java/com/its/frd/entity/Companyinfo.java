package com.its.frd.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.its.common.service.UserService;

@Entity
@Table(name = "companyinfo")
public class Companyinfo implements java.io.Serializable {
	
	public static final String COPANYSTATUS_OK = "1";
	
	public static final String FACTORY = "factory";
	public static final String COMPANY = "company";
	
    // Fields

    private Long id;
    private String companyname;
    private String address;
    private String legalperson;
    private Timestamp createtime;
    private String companystatus;
    private Long userid;
    private String username;
    private String registernum;
    private String registerorgan;
    private BigDecimal registercapital;
    private String businesstype;
    private String registerstatus;
    private String infotype;
    private String startdate;
    private Long parentid;
    private String parentname;
    private String companytype;
    private String companyemail;
    private String companyphone;
    private List<MesProductline> mesProductlines = new ArrayList<MesProductline>();
    private List<MesDriver> mesDrivers = new ArrayList<MesDriver>();
    private List<MesPointGateway> mesPointGatewaies = new ArrayList<MesPointGateway>();
    /*private List<MesPointType> mesPointTypes = new ArrayList<MesPointType>();*/
    private List<Usercompanys> usercompanyses = new ArrayList<Usercompanys>(0);
    private List<MesProduct> mesProducts = new ArrayList<MesProduct>(0);
    private List<MesDrivertype> mesDrivertypes = new ArrayList<MesDrivertype>(0);
    private List<MesCompanyPosition> mesCompanyPositions = new ArrayList<MesCompanyPosition>();
    private List<MesPointGatewayExchange> gatewayExchanges = new ArrayList<>();
   
    private List<Companyinfo> sonCompanyinfo = new ArrayList<Companyinfo>(); 
    private UserService userSer;
    // Constructors

    /** default constructor */
    public Companyinfo() {
    }

    public Companyinfo(Long id) {
		super();
		this.id = id;
	}

	/** minimal constructor */
    public Companyinfo(Timestamp createTime) {
        this.createtime = createTime;
    }

    /** full constructor */
    public Companyinfo(String companyname, String address, String legalperson,
            Long userid, Timestamp createTime, String status, String username,
            String registernum, String registerorgan, BigDecimal registercapital,
            String businesstype, String registerstatus, String type,
            String startdate, Long parentid, String companytype, String companyphone,
            List<MesProductline> mesProductlines, List<MesDriver> mesDrivers) {
        this.companyname = companyname;
        this.address = address;
        this.legalperson = legalperson;
        this.createtime = createTime;
        this.companystatus = status;
        this.registernum = registernum;
        this.registerorgan = registerorgan;
        this.registercapital = registercapital;
        this.businesstype = businesstype;
        this.registerstatus = registerstatus;
        this.userid=userid;
        this.username=username;
        this.infotype = type;
        this.startdate = startdate;
        this.parentid = parentid;
        this.companytype = companytype;
        this.companyphone = companyphone;
        this.mesProductlines = mesProductlines;
        this.mesDrivers = mesDrivers;
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

    @Column(name = "companyname", length = 60)
    public String getCompanyname() {
        return this.companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    @Column(name = "address", length = 60)
    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "legalperson", length = 60)
    public String getLegalperson() {
        return this.legalperson;
    }

    public void setLegalperson(String legalperson) {
        this.legalperson = legalperson;
    }

    @Column(name = "userid")
    public Long getUserid() {
        return this.userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    @Column(name = "createtime", nullable = false, length = 19)
    public Timestamp getCreatetime() {
        return this.createtime;
    }

    public void setCreatetime(Timestamp createTime) {
        this.createtime = createTime;
    }

    @Column(name = "companystatus", length = 10)
    public String getCompanystatus() {
        return this.companystatus;
    }

    public void setCompanystatus(String status) {
        this.companystatus = status;
    }

    @Column(name = "registernum", length = 60)
    public String getRegisternum() {
        return this.registernum;
    }

    public void setRegisternum(String registernum) {
        this.registernum = registernum;
    }

    @Column(name = "registerorgan", length = 60)
    public String getRegisterorgan() {
        return this.registerorgan;
    }

    public void setRegisterorgan(String registerorgan) {
        this.registerorgan = registerorgan;
    }
    @Range(min=1, max=999999,message="注册资金范围0-999999（万）之间")
    @Column(name = "registercapital", precision = 12, scale = 0)
    public BigDecimal getRegistercapital() {
        return this.registercapital;
    }

    public void setRegistercapital(BigDecimal registercapital) {
        this.registercapital = registercapital;
    }

    @Column(name = "businesstype", length = 10)
    public String getBusinesstype() {
        return this.businesstype;
    }

    public void setBusinesstype(String businesstype) {
        this.businesstype = businesstype;
    }

    @Column(name = "companyphone", length = 15)
    public String getCompanyphone() {
        return companyphone;
    }

    public void setCompanyphone(String companyphone) {
        this.companyphone = companyphone;
    }

    @Column(name = "registerstatus", length = 10)
    public String getRegisterstatus() {
        return this.registerstatus;
    }

    public void setRegisterstatus(String registerstatus) {
        this.registerstatus = registerstatus;
    }

    @Column(name = "infotype", length = 20)
    public String getInfotype() {
        return this.infotype;
    }

    public void setInfotype(String type) {
        this.infotype = type;
    }

    @Column(name = "startdate", length = 20)
    public String getStartdate() {
        return this.startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }
    @Email
    @Column(name = "companyemail", length = 100)
    public String getCompanyemail() {
		return companyemail;
	}

	public void setCompanyemail(String companyemail) {
		this.companyemail = companyemail;
	}

	@Column(name = "parentid")
    public Long getParentid() {
        return this.parentid;
    }

    public void setParentid(Long parentid) {
        this.parentid = parentid;
    }

    @Column(name = "companytype", length = 20)
    public String getCompanytype() {
        return this.companytype;
    }

    public void setCompanytype(String companytype) {
        this.companytype = companytype;
    }
    
    @JsonIgnore
    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "companyinfo")
    public List<MesProductline> getMesProductlines() {
        return this.mesProductlines;
    }

    public void setMesProductlines(List<MesProductline> mesProductlines) {
        this.mesProductlines = mesProductlines;
    }
    
    @JsonIgnore
    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "companyinfo")
    public List<MesDriver> getMesDrivers() {
        return this.mesDrivers;
    }

    public void setMesDrivers(List<MesDriver> mesDrivers) {
        this.mesDrivers = mesDrivers;
    }
    
    @Transient
    public String getParentname() {
        return parentname;
    }

    public void setParentname(String parentname) {
        this.parentname = parentname;
    }
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "companyinfo")
	public List<MesPointGateway> getMesPointGatewaies() {
		return this.mesPointGatewaies;
	}

	public void setMesPointGatewaies(List<MesPointGateway> mesPointGatewaies) {
		this.mesPointGatewaies = mesPointGatewaies;
	}
	/*@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "companyinfo")
	public List<MesPointType> getMesPointTypes() {
		return this.mesPointTypes;
	}

	public void setMesPointTypes(List<MesPointType> mesPointTypes) {
		this.mesPointTypes = mesPointTypes;
	}*/
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "companyinfo")
	public List<Usercompanys> getUsercompanyses() {
		return this.usercompanyses;
	}

	public void setUsercompanyses(List<Usercompanys> usercompanyses) {
		this.usercompanyses = usercompanyses;
	}
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "companyinfo")
	public List<MesProduct> getMesProducts() {
		return this.mesProducts;
	}

	public void setMesProducts(List<MesProduct> mesProducts) {
		this.mesProducts = mesProducts;
	}
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "companyinfo")
	public List<MesDrivertype> getMesDrivertypes() {
		return this.mesDrivertypes;
	}

	public void setMesDrivertypes(List<MesDrivertype> mesDrivertypes) {
		this.mesDrivertypes = mesDrivertypes;
	}
	
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "companyinfo")
	public List<MesCompanyPosition> getMesCompanyPositions() {
		return this.mesCompanyPositions;
	}

	public void setMesCompanyPositions(
			List<MesCompanyPosition> mesCompanyPositions) {
		this.mesCompanyPositions = mesCompanyPositions;
	}
	 @Transient
    public List<Companyinfo> getSonCompanyinfo() {
        return sonCompanyinfo;
    }

    public void setSonCompanyinfo(List<Companyinfo> sonCompanyinfo) {
        this.sonCompanyinfo = sonCompanyinfo;
    }
    @JsonIgnore
    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "companyinfo")
	public List<MesPointGatewayExchange> getGatewayExchanges() {
		return gatewayExchanges;
	}

	public void setGatewayExchanges(List<MesPointGatewayExchange> gatewayExchanges) {
		this.gatewayExchanges = gatewayExchanges;
	}
	@Transient
	public String getUsername() {
		
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
}