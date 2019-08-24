package com.its.frd.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.its.frd.dao.MonitorPainterUserDao;
import com.its.frd.entity.MonitorPainterUser;
import com.its.frd.service.MonitorPainterUserService;

@Service("monitorPainterUserService")
public class MonitorPainterUserServiceImpl implements MonitorPainterUserService {

	@Autowired
	private MonitorPainterUserDao monitorPainterUserDao;
	@Override
	public void deleteMonitorPainterUserByPainterIdAndUserId(long painterId, long userId) {
		monitorPainterUserDao.deleteMonitorPainterByPainterIdAndUserId(painterId, userId);
	}
	@Override
	public void saveMonitorPainterUser(MonitorPainterUser monitorPainterUser) {
		monitorPainterUserDao.save(monitorPainterUser);
		
	}
	@Override
	public void saveMonitorPainterUsers(List<MonitorPainterUser> monitorPainterUsers) {
		monitorPainterUserDao.save(monitorPainterUsers);
		
	}
	@Override
	public void deleteMonitorPaintUserByPainterId(Long painterId) {
		monitorPainterUserDao.deleteByMonitorPainterId(painterId);
	}

}
