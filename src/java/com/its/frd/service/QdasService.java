package com.its.frd.service;

import java.util.List;

import com.its.frd.entity.MesQdas;

public interface QdasService extends BaseService<MesQdas>{
	public MesQdas findById(long id);
	
	public List<MesQdas> findAll();
}
