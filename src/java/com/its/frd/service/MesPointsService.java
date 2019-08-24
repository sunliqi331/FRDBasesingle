package com.its.frd.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.its.common.util.dwz.Page;
import com.its.frd.entity.MesPoints;

public interface MesPointsService {
	public List<MesPoints> findPage(Page page);
	public List<MesPoints> findPage(Specification<MesPoints> specification,Page page);
	public MesPoints findById(Long id);
	public void batchSaveOrUpdate(List<MesPoints> pointList);
	public MesPoints saveOrUpdate(MesPoints point);
	public void deleteById(Long id);
	public List<MesPoints> findPointsByPointsId(List<Long> driverIds);
	public List<MesPoints> checkCodekeyNew(Long id,String codekey);
}
