package com.its.common.controller.component;

import java.beans.PropertyEditorSupport;

import com.its.common.entity.component.StoreType;


public class StoreTypeEditor extends PropertyEditorSupport {

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		for (StoreType storeType : StoreType.values()) {
			// 忽略大小写
			if (storeType.value().equalsIgnoreCase(text)) {
				this.setValue(storeType);
				return;
			}
		}
		
		throw new IllegalArgumentException("没有找到" + text + "对应的枚举值。");
	}

}
