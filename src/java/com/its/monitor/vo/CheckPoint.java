package com.its.monitor.vo;

import java.io.Serializable;

public class CheckPoint implements Serializable {

	private static final long serialVersionUID = 1467799644191425L;
	
	private String mesPointKey;
	
	private String mesDriverPointsName;
	
	private String mesPointGateway;
	
	private String mesPointTypeKey;
	
	private String uploadTime;
	
	private String status;
	
	private String value;

	public String getMesPointKey() {
		return mesPointKey;
	}

	public void setMesPointKey(String mesPointKey) {
		this.mesPointKey = mesPointKey;
	}

	public String getMesDriverPointsName() {
		return mesDriverPointsName;
	}

	public void setMesDriverPointsName(String mesDriverPointsName) {
		this.mesDriverPointsName = mesDriverPointsName;
	}

	public String getMesPointGateway() {
		return mesPointGateway;
	}

	public void setMesPointGateway(String mesPointGateway) {
		this.mesPointGateway = mesPointGateway;
	}

	public String getMesPointTypeKey() {
		return mesPointTypeKey;
	}

	public void setMesPointTypeKey(String mesPointTypeKey) {
		this.mesPointTypeKey = mesPointTypeKey;
	}

	public String getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
	

}
