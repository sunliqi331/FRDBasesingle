package com.its.monitor.vo;

import java.io.Serializable;

public class EnergyMonitor extends ChartsBaseMonitor {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3412530653502050040L;

	private double value;

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	
	
}
