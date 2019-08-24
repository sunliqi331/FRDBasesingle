package com.its.monitor.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MonitorPage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7846970100620413475L;

	private String monitorId;
	
	private String status;
	
	private List<DriverMonitor> driverMonitors = new ArrayList<>();
	
	private List<ProductMonitor> productMonitors = new ArrayList<>();
	
	private String driverMonitorsStr;
	
	private String productMonitorsStr;

	public String getMonitorId() {
		return monitorId;
	}

	public void setMonitorId(String monitorId) {
		this.monitorId = monitorId;
	}

	public List<DriverMonitor> getDriverMonitors() {
		return driverMonitors;
	}

	public void setDriverMonitors(List<DriverMonitor> driverMonitors) {
		this.driverMonitors = driverMonitors;
	}

	public List<ProductMonitor> getProductMonitors() {
		return productMonitors;
	}

	public void setProductMonitors(List<ProductMonitor> productMonitors) {
		this.productMonitors = productMonitors;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDriverMonitorsStr() {
		return driverMonitorsStr;
	}

	public void setDriverMonitorsStr(String driverMonitorsStr) {
		this.driverMonitorsStr = driverMonitorsStr;
	}

	public String getProductMonitorsStr() {
		return productMonitorsStr;
	}

	public void setProductMonitorsStr(String productMonitorsStr) {
		this.productMonitorsStr = productMonitorsStr;
	}

	
	
	
}
