package com.its.frd.service;

import java.util.List;
import java.util.Set;
import com.its.frd.entity.MesProduct;

public interface MesProductService extends BaseService<MesProduct> {
	
	public MesProduct findById(Long id);
	
	public void deleteById(Long id);
	
	public List<MesProduct> findByCompanyinfo(Long companyinfoId);

	public List<MesProduct> findByCompanyinfo(Long companyinfoId, String productName);
	
	/**
	 * 查询出所有的产品型号
	 * @return
	 */
	public Set<String> findAllModelLnum();
	
	/**
	 * 根据公司id查找该公司下的所有产品型号
	 * @param companyId
	 * @return
	 */
	public Set<String> findByCompanyId(Long companyId);
	
	/**
	 * 根据公司id和产品型号查找产品
	 * @param companyId
	 * @param modelnum
	 * @return
	 */
	public MesProduct findByCompanyIdAndModelnum(Long companyId,String modelnum);
}
