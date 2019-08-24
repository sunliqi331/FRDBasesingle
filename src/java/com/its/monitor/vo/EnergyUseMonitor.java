package com.its.monitor.vo;

import java.io.Serializable;

public class EnergyUseMonitor implements Serializable {
	private String productionLineId;
	
	private String energyUse;
	
	private String uploadTime;

	public String getProductionLineId() {
		return productionLineId;
	}

	public void setProductionLineId(String productionLineId) {
		this.productionLineId = productionLineId;
	}

	public String getEnergyUse() {
		return energyUse;
	}

	public void setEnergyUse(String energyUse) {
		this.energyUse = energyUse;
	}

	public String getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}
	
	
}
