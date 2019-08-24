package com.its.statistics.vo;

import java.util.List;

public class GrrAnalyzeSearch extends AnalyzeSearch {
	private int personNum;
	
	private int checkNum;
	
	private int workpieceNum;
	
	private int analyseGrrType;//0:件次人，1：件人次
	
	private List<CgAnalyzeData> dataList;

	public int getPersonNum() {
		return personNum;
	}

	public void setPersonNum(int personNum) {
		this.personNum = personNum;
	}

	public int getCheckNum() {
		return checkNum;
	}

	public void setCheckNum(int checkNum) {
		this.checkNum = checkNum;
	}

	public int getWorkpieceNum() {
		return workpieceNum;
	}

	public void setWorkpieceNum(int workpieceNum) {
		this.workpieceNum = workpieceNum;
	}

	public int getAnalyseGrrType() {
		return analyseGrrType;
	}

	public void setAnalyseGrrType(int analyseGrrType) {
		this.analyseGrrType = analyseGrrType;
	}

	public List<CgAnalyzeData> getDataList() {
		return dataList;
	}

	public void setDataList(List<CgAnalyzeData> dataList) {
		this.dataList = dataList;
	}
	
	
}
