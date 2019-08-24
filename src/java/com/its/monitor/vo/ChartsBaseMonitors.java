package com.its.monitor.vo;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class ChartsBaseMonitors implements Serializable {
	List<ChartsBaseMonitor> monitors = new ArrayList<>();

	public List<ChartsBaseMonitor> getMonitors() {
		return monitors;
	}

	public void setMonitors(List<ChartsBaseMonitor> monitors) {
		this.monitors = monitors;
	}
	
	
}
