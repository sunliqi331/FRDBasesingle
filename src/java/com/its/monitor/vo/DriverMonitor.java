package com.its.monitor.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DriverMonitor implements Serializable {
	private static final long serialVersionUID = -3936864415826776161L;
	
	private String mesDriverId;
	
	private List<DriverCheckPoint> driverCheckPoints = new ArrayList<>();
	

	public String getMesDriverId() {
		return mesDriverId;
	}

	public void setMesDriverId(String mesDriverId) {
		this.mesDriverId = mesDriverId;
	}

	public List<DriverCheckPoint> getDriverCheckPoints() {
		return driverCheckPoints;
	}

	public void setDriverCheckPoints(List<DriverCheckPoint> driverCheckPoints) {
		this.driverCheckPoints = driverCheckPoints;
	}

	

	
}
