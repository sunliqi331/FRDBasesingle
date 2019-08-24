package com.its.frd.service;

import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import com.its.common.util.dwz.Page;
import com.its.frd.entity.MesAlarmShow;

public interface MesAlarmShowService {
	
	/**
	 * 查询最新的两千条数据
	 * @return
	 */
	public List<MesAlarmShow> findNewTwoThousand();
	
	/**
	 * 分页查找设备告警展示列表
	 * @param specification
	 * @param page
	 * @return
	 */
	public List<MesAlarmShow> findPage(Specification<MesAlarmShow> specification, Page page);

	/**
	 * 更改设备报警记录的确认状态
	 * @param id
	 */
	public void updateStatusChange(Long id);
	
	/** 
	 * 为监控页面展示最新的设备报警数据
	 * @param driverIds 设备id集合
	 * @param line 展示的行数
	 * @return
	 */
	public List<MesAlarmShow> getDataWithDriverIdsAndLine(List<Long> driverIds, Long line);
	
}
