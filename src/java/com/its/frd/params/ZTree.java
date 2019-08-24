package com.its.frd.params;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ZTree implements Serializable {
	private static final long serialVersionUID = -8194444146977315563L;

	private String name;
	
	private List<ZTree> children = new ArrayList<>();
	

	private List<CustomAttr> customAttrList = new ArrayList<>();
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ZTree> getChildren() {
		return children;
	}

	public void setChildren(List<ZTree> children) {
		this.children = children;
	}
	
	
	public List<CustomAttr> getCustomAttrList() {
		return customAttrList;
	}

	public void setCustomAttrList(List<CustomAttr> customAttrList) {
		this.customAttrList = customAttrList;
	}


	public class CustomAttr implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = -2260542030481719259L;
		private String name;
		private Object value;
		public CustomAttr(String name, Object value){
			this.name = name;
			this.value = value;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Object getValue() {
			return value;
		}
		public void setValue(Object value) {
			this.value = value;
		}
		
		
	}
	
}
