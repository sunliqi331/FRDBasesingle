package com.its.frd.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.its.common.util.dwz.Page;
import com.its.frd.entity.Subsysrole;

public interface SubsysroleService {
	public List<Subsysrole> findPage(Specification<Subsysrole> specification,Page page); 
	public void saveOrUpdateInfo(Subsysrole info);
	public void deleteInfoById(Long id);
	public Subsysrole findOneById(Long id);
}
