package com.its.frd.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.its.common.util.dwz.Page;
import com.its.frd.entity.Subsysteminfo;

public interface SubsysteminfoService {
	public List<Subsysteminfo> findPage(Specification<Subsysteminfo> specification,Page page); 
	public void saveOrUpdateInfo(Subsysteminfo info);
	public void deleteInfoById(Long id);
	public Subsysteminfo findOneById(Long id);
}
