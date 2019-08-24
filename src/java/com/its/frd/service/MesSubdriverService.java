package com.its.frd.service;

import com.its.frd.entity.MesDriver;
import com.its.frd.entity.MesSubdriver;

public interface MesSubdriverService extends BaseService<MesSubdriver> {
	
	public MesSubdriver findById(Long id);
	
	public void deleteById(Long id);
}
