package com.its.frd.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.client.Scan;

import com.its.common.util.dwz.Page;
import com.its.frd.entity.MesProcedureProperty;
import com.its.frd.entity.MesProduct;
import com.its.frd.entity.MesProductProcedure;
import com.its.frd.entity.MesProductline;
import com.its.statistics.vo.AnalyzeSearch;
public class HBasePageModel extends Page implements Serializable {
    private static final long serialVersionUID = 330410716100946538L;
    private String pageStartRowKey = null;
    private String rowKey = null;
    private String pageEndRowKey = null;
    private String pagePreStartRowkey = null;
    private Scan scan;
    private boolean isPagable = true;
    private AnalyzeSearch analyzeSearch;
    private Map<Long ,MesProductProcedure> procedureMap = new HashMap<>();
	private Map<Long ,MesProcedureProperty> procedurePropertyMap = new HashMap<>();
	private Map<Long ,MesProductline> productLineMap = new HashMap<>();
	private Map<String ,MesProduct> productMap = new HashMap<>();
	Map<String,Long> goodMap = new HashMap<>();
	Map<String,Long> badMap = new HashMap<>();
	/**
     * 获取每页起始行键
     * @return
     */
    public String getPageStartRowKey() {
        return pageStartRowKey;
    }
    /**
     * 设置每页起始行键
     * @param pageStartRowKey
     */
    public void setPageStartRowKey(String pageStartRowKey) {
        this.pageStartRowKey = pageStartRowKey;
    }
    /**
     * 获取每页结束行键
     * @return
     */
    public String getPageEndRowKey() {
        return pageEndRowKey;
    }
    /**
     * 设置每页结束行键
     * @param pageStartRowKey
     */
    public void setPageEndRowKey(String pageEndRowKey) {
        this.pageEndRowKey = pageEndRowKey;
    }
	public String getPagePreStartRowkey() {
		return pagePreStartRowkey;
	}
	public void setPagePreStartRowkey(String pagePreStartRowkey) {
		this.pagePreStartRowkey = pagePreStartRowkey;
	}
	public Scan getScan() {
		return scan;
	}
	public void setScan(Scan scan) {
		this.scan = scan;
	}
	public AnalyzeSearch getAnalyzeSearch() {
		return analyzeSearch;
	}
	public void setAnalyzeSearch(AnalyzeSearch analyzeSearch) {
		this.analyzeSearch = analyzeSearch;
	}
	public Map<Long, MesProductProcedure> getProcedureMap() {
		return procedureMap;
	}
	public void setProcedureMap(Map<Long, MesProductProcedure> procedureMap) {
		this.procedureMap = procedureMap;
	}
	public Map<Long, MesProcedureProperty> getProcedurePropertyMap() {
		return procedurePropertyMap;
	}
	public void setProcedurePropertyMap(Map<Long, MesProcedureProperty> procedurePropertyMap) {
		this.procedurePropertyMap = procedurePropertyMap;
	}
	public Map<Long, MesProductline> getProductLineMap() {
		return productLineMap;
	}
	public void setProductLineMap(Map<Long, MesProductline> productLineMap) {
		this.productLineMap = productLineMap;
	}
	public Map<String, MesProduct> getProductMap() {
		return productMap;
	}
	public void setProductMap(Map<String, MesProduct> productMap) {
		this.productMap = productMap;
	}
	public Map<String, Long> getGoodMap() {
		return goodMap;
	}
	public void setGoodMap(Map<String, Long> goodMap) {
		this.goodMap = goodMap;
	}
	public Map<String, Long> getBadMap() {
		return badMap;
	}
	public void setBadMap(Map<String, Long> badMap) {
		this.badMap = badMap;
	}
	public String getRowKey() {
		return rowKey;
	}
	public void setRowKey(String rowKey) {
		this.rowKey = rowKey;
	}
	public boolean isPagable() {
		return isPagable;
	}
	public void setPagable(boolean isPagable) {
		this.isPagable = isPagable;
	}
	
}