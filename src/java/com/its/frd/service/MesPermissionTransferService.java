package com.its.frd.service;

import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import com.its.common.util.dwz.Page;
import com.its.frd.entity.MesPermissionTransfer;

public interface MesPermissionTransferService extends BaseService<MesPermissionTransfer> {
	
	public List<MesPermissionTransfer> findByPage(Specification<MesPermissionTransfer> specification, Page page);
	
}
