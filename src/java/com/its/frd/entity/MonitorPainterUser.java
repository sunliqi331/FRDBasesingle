package com.its.frd.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="monitor_painter_user")
public class MonitorPainterUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5291762446643872681L;
	
	private long id;
	
	private long monitorPainterId;
	
	private long userId;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getMonitorPainterId() {
		return monitorPainterId;
	}

	public void setMonitorPainterId(long monitorPainterId) {
		this.monitorPainterId = monitorPainterId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	

}
