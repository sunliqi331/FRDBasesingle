package com.its.frd.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
@Table(name = "mes_points")
public class MesPoints implements java.io.Serializable {

	// Fields
	
	private Long id;
	private MesPointType mesPointType;
	private MesPointGateway mesPointGateway;
	private String name;
	private String codekey;
	private String datatype;
	private String units;
	private Long unitsId;
	private String status;
	private MesDriver currentMesDriver = new MesDriver();
	private MesProductline currentMesProductline = new MesProductline();
	//private MesProcedureProperty currentMesProcedureProperty = new MesProcedureProperty();
	private MesDrivertypeProperty currentMesDrivertypeProperty;
	private String currentMesProcedurePropertyName;
	private Long currentMesProcedurePropertyId;
	private MesProductProcedure currentMesProductProcedure = new MesProductProcedure();
	private MesProduct currentMesProduct = new MesProduct();
	private String factoryName;
	public static final String BINDING = "1";
    public static final String UNBINDING = "0";
    public static final String BINDING_PROCE = "2";
   

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	
	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}
	
	public Long getUnitsId() {
		return unitsId;
	}

	public void setUnitsId(Long unitsId) {
		this.unitsId = unitsId;
	}

	@Column(name = "status", length = 20)
	public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private List<MesDriverPoints> mesDriverPointses = new ArrayList<MesDriverPoints>();
	private List<MesProcedureProperty> mesProcedureProperties = new ArrayList<MesProcedureProperty>();

	// Constructors

	/** default constructor */
	public MesPoints() {
	}

	/** full constructor */
	public MesPoints(MesPointType mesPointType,
			MesPointGateway mesPointGateway, String codekey,
			List<MesDriverPoints> mesDriverPointses,String status,MesProductline currentMesProductline) {
		this.mesPointType = mesPointType;
		this.mesPointGateway = mesPointGateway;
		this.codekey = codekey;
		this.mesDriverPointses = mesDriverPointses;
		this.status = status;
		this.currentMesProductline=currentMesProductline;
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
	@JoinColumn(name = "pointtypeid")
	public MesPointType getMesPointType() {
		return this.mesPointType;
	}

	public void setMesPointType(MesPointType mesPointType) {
		this.mesPointType = mesPointType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pointgatewayid")
	public MesPointGateway getMesPointGateway() {
		return this.mesPointGateway;
	}

	public void setMesPointGateway(MesPointGateway mesPointGateway) {
		this.mesPointGateway = mesPointGateway;
	}

	@Column(name = "codekey", length = 20)
	public String getCodekey() {
		return this.codekey;
	}

	public void setCodekey(String codekey) {
		this.codekey = codekey;
	}
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mesPoints")
	public List<MesDriverPoints> getMesDriverPointses() {
		return this.mesDriverPointses;
	}

	public void setMesDriverPointses(List<MesDriverPoints> mesDriverPointses) {
		this.mesDriverPointses = mesDriverPointses;
	}
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mesPoints")
	public List<MesProcedureProperty> getMesProcedureProperties() {
		return this.mesProcedureProperties;
	}

	public void setMesProcedureProperties(
			List<MesProcedureProperty> mesProcedureProperties) {
		this.mesProcedureProperties = mesProcedureProperties;
	}

	@Transient
	public MesDriver getCurrentMesDriver() {
		return this.mesDriverPointses.size() != 0 ? this.mesDriverPointses.get(0).getMesDriver() : this.currentMesDriver;
	}

	public void setCurrentMesDriver(MesDriver currentMesDriver) {
		this.currentMesDriver = currentMesDriver;
	}
	/*@Transient
	public MesProcedureProperty getCurrentMesProcedureProperty() {
		return this.mesProcedureProperties.size() != 0 ? this.mesProcedureProperties.get(0) : this.currentMesProcedureProperty;
	}

	public void setCurrentMesProcedureProperty(MesProcedureProperty currentMesProcedureProperty) {
		this.currentMesProcedureProperty = currentMesProcedureProperty;
	}*/
	@Transient
	public String getCurrentMesProcedurePropertyName() {
		return this.mesProcedureProperties.size() != 0 ? this.mesProcedureProperties.get(0).getPropertyname() : "";
	}

	public void setCurrentMesProcedurePropertyName(String currentMesProcedurePropertyName) {
		this.currentMesProcedurePropertyName = currentMesProcedurePropertyName;
	}
	@Transient
	public Long getCurrentMesProcedurePropertyId() {
		return this.mesProcedureProperties.size() != 0 ? this.mesProcedureProperties.get(0).getId() : 0;
	}

	public void setCurrentMesProcedurePropertyId(Long currentMesProcedurePropertyId) {
		this.currentMesProcedurePropertyId = currentMesProcedurePropertyId;
	}

	
	
	
	@Transient
	public MesProductProcedure getCurrentMesProductProcedure() {
		return this.mesProcedureProperties.size() != 0 ? this.mesProcedureProperties.get(0).getMesProductProcedure() : this.currentMesProductProcedure;
	}

	public void setCurrentMesProductProcedure(MesProductProcedure currentMesProductProcedure) {
		this.currentMesProductProcedure = currentMesProductProcedure;
	}
	@Transient
	public MesProduct getCurrentMesProduct() {
		return this.mesProcedureProperties.size() != 0 ? this.mesProcedureProperties.get(0).getMesProductProcedure().getMesProduct() : this.currentMesProduct; 
	}

	public void setCurrentMesProduct(MesProduct currentMesProduct) {
		this.currentMesProduct = currentMesProduct;
	}
	@Transient
	public MesDrivertypeProperty getCurrentMesDrivertypeProperty() {
		return mesDriverPointses.size() != 0 ? mesDriverPointses.get(0).getMesDrivertypeProperty() : this.currentMesDrivertypeProperty;
	}

	public void setCurrentMesDrivertypeProperty(MesDrivertypeProperty currentMesDrivertypeProperty) {
		this.currentMesDrivertypeProperty = currentMesDrivertypeProperty;
	}
	@Transient
	public MesProductline getCurrentMesProductline() {
		return this.mesDriverPointses.size() != 0 && this.mesDriverPointses.get(0).getMesDriver() != null ? this.mesDriverPointses.get(0).getMesDriver().getMesProductline() : this.currentMesProductline;
	}

	public void setCurrentMesProductline(MesProductline currentMesProductline) {
		this.currentMesProductline = currentMesProductline;
	}
	
	@Transient  
	public String getFactoryName() {
		return factoryName;
	}

	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}
	
	
	
}