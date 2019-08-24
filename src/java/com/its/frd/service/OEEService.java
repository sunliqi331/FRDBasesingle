package com.its.frd.service;

import java.util.List;

public interface OEEService {
	
    /**
     * 设备OEE
     * @param CalendarWorkingTime 日历工作时间
     * @param ScheduledDownTime 计划停机时间
     * @param DownTime 故障停机时间
     * @param DeviceAdjustInitializationTime 设备调整初始化时间
     * @param ActualCycleTime 实际加工周期
    * @param CrankupTime 开动时间
     * @param TheoreticalProcessingCycle 理论加工周期
     * @param id 设备id
     * @return
     */
	public List<String> getOEEData(Double CalendarWorkingTime, Double ScheduledDownTime,
            Double DeviceAdjustInitializationTime, Double TheoreticalProcessingCycle,
            Double ActualCycleTime, Long id);
}
