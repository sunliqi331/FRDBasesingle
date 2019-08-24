package com.its.frd.util.echarts;

import com.github.abel533.echarts.Legend;
import com.github.abel533.echarts.Option;
import com.github.abel533.echarts.Title;
import com.github.abel533.echarts.code.X;
import com.github.abel533.echarts.json.GsonOption;
import com.google.gson.Gson;

public abstract class EchartsOption <T> {
	protected GsonOption option = new GsonOption();
	private Gson gson = new Gson();
	protected Legend legend = new Legend().x(X.right).right(10);
	@SuppressWarnings("unchecked")
	public T title(String title){
		Title t = new Title();
		option.title(t.text(title).x(X.center));
		return (T)this;
	}
	
	public String beData(){
		return gson.toJson(option);
	}
	
	public abstract GsonOption data(Object map) throws Exception;
}
