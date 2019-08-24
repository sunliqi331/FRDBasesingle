package com.its.frd.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.its.frd.dao.MesProductionDao;
import com.its.frd.entity.MesProduction;
import com.its.frd.service.MesProductionService;

@Service
public class MesProductionServiceImp implements MesProductionService{
	
	@Autowired
	private MesProductionDao mpd;
	
	@Override
	public List<MesProduction> findByTimeSpan(Timestamp startDate, Timestamp endDate, Long driverId,
			String productnum) {
		List<MesProduction> list = new ArrayList<>();
		if(startDate!=null && endDate!=null && driverId!=null && productnum!=null){
			list = mpd.findByTimeSpan(startDate,endDate,driverId,productnum);
		}
		return list;
	}

}
