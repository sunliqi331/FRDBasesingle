package com.its.monitor.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductMonitor implements Serializable {
	private static final long serialVersionUID = -503660864082767281L;
	
	private String productId;
	
	private String procedureId;
	
	private List<ProductCheckPoint> productCheckPoints = new ArrayList<>();

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public List<ProductCheckPoint> getProductCheckPoints() {
		return productCheckPoints;
	}

	public void setProductCheckPoints(List<ProductCheckPoint> productCheckPoints) {
		this.productCheckPoints = productCheckPoints;
	}

	public String getProcedureId() {
		return procedureId;
	}

	public void setProcedureId(String procedureId) {
		this.procedureId = procedureId;
	}
	
	
}
