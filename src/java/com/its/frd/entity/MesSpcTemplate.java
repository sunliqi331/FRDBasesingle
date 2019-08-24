package com.its.frd.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mes_spc_template")
public class MesSpcTemplate {
	private Long id;
	private Double A2;
	private Double d2;
	private Double D3;
	private Double D4;
	private Double A3;
	private Double C4;
	private Double B3;
	private Double B4;
	private Integer COLUMN;
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Double getA2() {
		return A2;
	}
	public void setA2(Double a2) {
		A2 = a2;
	}
	public Double getd2() {
		return d2;
	}
	public void setd2(Double d2) {
		this.d2 = d2;
	}
	public Double getD3() {
		return D3;
	}
	public void setD3(Double d3) {
		D3 = d3;
	}
	public Double getD4() {
		return D4;
	}
	public void setD4(Double d4) {
		D4 = d4;
	}
	public Double getA3() {
		return A3;
	}
	public void setA3(Double a3) {
		A3 = a3;
	}
	public Double getC4() {
		return C4;
	}
	public void setC4(Double c4) {
		C4 = c4;
	}
	public Double getB3() {
		return B3;
	}
	public void setB3(Double b3) {
		B3 = b3;
	}
	public Double getB4() {
		return B4;
	}
	public void setB4(Double b4) {
		B4 = b4;
	}
	public Integer getCOLUMN() {
		return COLUMN;
	}
	public void setCOLUMN(Integer cOLUMN) {
		COLUMN = cOLUMN;
	}
	
	
	
	
	
}
