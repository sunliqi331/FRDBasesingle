package com.its.frd.service.impl;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.its.common.util.dwz.Page;
import com.its.common.util.dwz.PageUtils;
import com.its.common.utils.SecurityUtils;
import com.its.frd.dao.MesAlarmShowDao;
import com.its.frd.entity.MesAlarmShow;
import com.its.frd.entity.MesDriver;
import com.its.frd.service.MesAlarmShowService;
import com.its.frd.service.ProductAndEnergyAndDriverChartService;

@Service
public class MesAlarmShowServiceImp implements MesAlarmShowService{
	
	@Resource
	private MesAlarmShowDao mesAlarmShowDao;
	
	@Resource
	private ProductAndEnergyAndDriverChartService productChartServ;

	@Override
	public List<MesAlarmShow> findNewTwoThousand() {
		List<MesAlarmShow> alarmShowList = new ArrayList<MesAlarmShow>();
		//获取登录公司下边所有的设备
		List<MesDriver> driverList = productChartServ.getDriverListByCompanyId(SecurityUtils.getShiroUser().getCompanyid());
		List<Long> driverIds = new ArrayList<>();
		for(MesDriver md : driverList){
			driverIds.add(md.getId());
		}
		alarmShowList = mesAlarmShowDao.findNewTwoThousand(driverIds);
		return alarmShowList;
	}
	
	@Override
	public List<MesAlarmShow> findPage(Specification<MesAlarmShow> specification, Page page) {
		page.setOrderField("id");
		org.springframework.data.domain.Page<MesAlarmShow> springDataPage = mesAlarmShowDao.findAll(specification,PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public void updateStatusChange(Long id) {
		MesAlarmShow mesAlarmShow = mesAlarmShowDao.findOne(id);
		if("未确认".equals(mesAlarmShow.getStatus())){
			mesAlarmShow.setStatus("已确认");
			mesAlarmShowDao.save(mesAlarmShow);
		}
	}

	@Override
	public List<MesAlarmShow> getDataWithDriverIdsAndLine(List<Long> driverIds, Long line) {
		List<MesAlarmShow> alarmShowList = new ArrayList<>();
		alarmShowList = mesAlarmShowDao.findNewData(driverIds,line);
		return alarmShowList;
	}
	
}
