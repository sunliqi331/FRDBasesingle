package com.its.statistics.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PropertyTrendSearch extends AnalyzeSearch {
	private List<Long> procedurePropertyIds = new ArrayList<>();
	
	public List<Long> getProcedurePropertyIds() {
		return procedurePropertyIds;
	}

	public void setProcedurePropertyIds(List<Long> procedurePropertyIds) {
		this.procedurePropertyIds = procedurePropertyIds;
	}

}
