package com.its.frd.service.impl;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.its.frd.dao.MesDriverDao;
import com.its.frd.dao.MesDriverFaultTimeDao;
import com.its.frd.dao.MesProductDao;
import com.its.frd.dao.MesProductionDao;
import com.its.frd.entity.MesDriverFaultTime;
import com.its.frd.entity.MesProduction;
import com.its.frd.service.OEEService;

@Service
public class OEEServiceImp implements OEEService {

    @Resource
    private MesDriverDao driverDao;  //设备
    
    @Resource
    private MesDriverFaultTimeDao mesDriverFaultTimeDao;
    
    @Resource
    private MesProductionDao mesProductionDao;
    
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
    
    @Override
    public List<String> getOEEData(Double CalendarWorkingTime, Double ScheduledDownTime,
            Double DeviceAdjustInitializationTime, Double TheoreticalProcessingCycle,
            Double ActualCycleTime, Long id) {
        //负荷时间
        double DurationOfLoadApplication;
        //开动时间
        double CrankupTime;
        //故障时间
        double DownTime = 0;
        //时间开动率
        double TimeStartRate;
        //速度开动率
        double RateOfSpeed;
        //净开动率
        double NetOperatingRate;
        //加工数量
        double TotalCount = 0;
        //性能开动率
        double PerformanceEfficiency;
        //合格品数
        double PassCount = 0;
        //合格品率
        double RateOfQualifiedProducts;
        //OEE
        double OEE;
        List<MesDriverFaultTime> mesDriverFaultTimes = mesDriverFaultTimeDao.findByMesDriverId(id);
        if(mesDriverFaultTimes.size() <= 0){
            return null;
        }
        for(MesDriverFaultTime mesDriverFaultTime : mesDriverFaultTimes){
            double Changetime = mesDriverFaultTime.getChangetime();
            DownTime += Changetime / 60;
        }
        DurationOfLoadApplication = 60 * CalendarWorkingTime - ScheduledDownTime;
        CrankupTime = DurationOfLoadApplication - DeviceAdjustInitializationTime - DownTime;
        TimeStartRate = CrankupTime / DurationOfLoadApplication;
        RateOfSpeed = TheoreticalProcessingCycle / ActualCycleTime;
        List<MesProduction> mesProductions = mesProductionDao.findByMesDriverId(id);
        if(mesProductions.size() <= 0){
            return null;
        }
        for(MesProduction mesProduction : mesProductions){
            TotalCount += mesProduction.getTotalCount();
            PassCount += mesProduction.getPassCount();
        }
        NetOperatingRate = TotalCount * ActualCycleTime/CrankupTime;
        PerformanceEfficiency = NetOperatingRate*RateOfSpeed;
        RateOfQualifiedProducts = (double) (PassCount / TotalCount);
        OEE = TimeStartRate * PerformanceEfficiency * RateOfQualifiedProducts;
        List<String> countOee = new ArrayList<String>();
        NumberFormat nFromat = NumberFormat.getPercentInstance();
        countOee.add(nFromat.format(TimeStartRate));
        countOee.add(nFromat.format(PerformanceEfficiency));
        countOee.add(nFromat.format(RateOfQualifiedProducts));
        countOee.add(nFromat.format(OEE));
        return countOee;
    }
	

}
