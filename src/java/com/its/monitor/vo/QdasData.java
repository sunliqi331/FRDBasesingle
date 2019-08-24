package com.its.monitor.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QdasData implements Serializable {
	private String mesProduct;

	private String mesProductModel;
	
	private List<QdasDataDetail> list = new ArrayList<>();

	public String getMesProduct() {
		return mesProduct;
	}

	public void setMesProduct(String mesProduct) {
		this.mesProduct = mesProduct;
	}

	public String getMesProductModel() {
		return mesProductModel;
	}

	public void setMesProductModel(String mesProductModel) {
		this.mesProductModel = mesProductModel;
	}

	public List<QdasDataDetail> getList() {
		return list;
	}

	public void setList(List<QdasDataDetail> list) {
		this.list = list;
	}
	
	
}
