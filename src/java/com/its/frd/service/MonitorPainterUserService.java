package com.its.frd.service;


import java.util.List;
import com.its.frd.entity.MonitorPainterUser;

public interface MonitorPainterUserService {
	public void saveMonitorPainterUser(MonitorPainterUser monitorPainterUser);
	
	public void deleteMonitorPainterUserByPainterIdAndUserId(long painterId, long userId);

	public void saveMonitorPainterUsers(List<MonitorPainterUser> monitorPainterUsers);
	
	public void deleteMonitorPaintUserByPainterId(Long painterId);
}
