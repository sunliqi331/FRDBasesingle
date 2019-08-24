package com.its.frd.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 网关数据上传类
 * @author Administrator
 *
 */
@Entity
@Table(name = "mes_driver_checkData")
public class DriverCheckData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8563384966722408934L;
	
	private long id;
	
	private String propertyKey;
	
	private String propertyName;
	
	private String value;
	
	private long uploadTime;
	
	private String productId;
	
	private String mesDriverId;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getPropertyKey() {
		return propertyKey;
	}

	public void setPropertyKey(String propertyKey) {
		this.propertyKey = propertyKey;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public long getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(long uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getMesDriverId() {
		return mesDriverId;
	}

	public void setMesDriverId(String mesDriverId) {
		this.mesDriverId = mesDriverId;
	}

	
	
}
