package com.its.frd.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.data.jpa.domain.Specification;
import com.its.common.util.dwz.Page;
import com.its.frd.entity.MesDriver;
import com.its.frd.entity.MesDriverOEE;
import com.its.frd.entity.MesDriverStats;
import com.its.frd.entity.MesEnergy;
import com.its.frd.util.HBasePageModel;
import com.its.statistics.vo.DriverRecord;
import com.its.frd.service.ProductAndEnergyAndDriverChartService.DateType;
public interface MesDriverService extends BaseService<MesDriver> {
	
	public MesDriver findById(Long id);
	
	public MesDriver findByMesPointGatewayid(Long mesPointGatewayid);
	
	public void deleteById(Long id);
	
	public List<MesDriver> findByCompanyidAndDifferencetype(Long comapnyid,String differencetype);
	
	public List<MesDriver> findByMesProductline(Long companyid);
	
	public List<MesDriver> findByMesProductlineid(Long mesProductlineid);
	
	public List<MesDriver> findByCompanyinfoidAndSnAndDif(Long companyid,String sn,String dif);
	
	public List<MesDriver> findByMesDriverTypeId(Long mesDriverTypeId);
	
	public MesDriver findBySn(String sn);
	
	public List<MesDriverOEE> findOEEPage(Specification<MesDriverOEE> specification,Page page);

	public void saveHandOEEResult(MesDriverOEE mesDriverOEE);

	public List<MesEnergy> findenergyPage(Specification<MesEnergy> specification,Page page);
	
	/**
	 * 检索设备能耗记录，用于导出excel表格
	 * @param energyType
	 * @param startDate
	 * @param endDate
	 * @param driverId
	 * @return
	 */
	public List<MesEnergy> findenergy(String energyType,Timestamp startDate,Timestamp endDate,Long driverId);
	
	/**
	 * 检索设备能耗记录，用于导出excel表格
	 * @param energyType
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<MesEnergy> findenergy(String energyType,Timestamp startDate,Timestamp endDate);
	
	/**
	 * 获取设备属性的rowkey集合
	 * @param start 开始时间
	 * @param end 结束时间
	 * @param driverId 设备id
	 * @param page 分页
	 * @return
	 */
	public List<String> findDriverPropertyRowKeys(Timestamp start, Timestamp end, Long driverId,HBasePageModel page);
	
	/**
	 * 获取设备生产记录，driverRecord集合，支持分页
	 * @param start
	 * @param end
	 * @param driverId
	 * @param page
	 * @return
	 */
	public List<DriverRecord> getDriverOutputByOne(Timestamp start, Timestamp end,
			Long driverId,HBasePageModel page);
	
	/**
	 * 从hbase中取设备属性状态数据，支持分页
	 * @param rowkeys 检索条件rowkeys集合
	 * @param driverPropertyId 设备类型属性id集合
	 * @param driverId 设备id
	 * @return
	 */
//	public List<Map<String, String>> findDriverPropertyByHbase(List<String> rowkeys, 
//			List<Long> driverPropertyIds, Long driverId);
	
	/**
	 * 从hbase中取设备属性状态数据，支持分页
	 * @param rowkeys 检索条件rowkeys集合
	 * @param driverPropertyId 设备类型属性id集合
	 * @param driverId 设备id
	 * @return
	 * @throws IOException 
	 */
	public List<Map<String, String>> findDriverPropertyByHbase(Timestamp start,Timestamp end,String factoryId, String productLineId, 
			List<Long> driverPropertyIds, Long driverId, String rowKey, HBasePageModel page) throws IOException;
	
	/**
	 * 获取指定设备的用于redis的key查询的数量测点mac地址+codekey
	 * @param driver
	 * @return
	 */
	public String findDriverRedisMac(MesDriver driver);
	
	/**
	 * 根据时间段、设备id查询设备产量，并按id降序排列
	 * @param startDate
	 * @param endDate
	 * @param driverId
	 * @return
	 */
	public List<MesDriverStats> findDriverOutput(Timestamp startDate, Timestamp endDate, Long driverId);
	
	/**
	 * 计算设备实际运行时间，根据传的参数DriverRecord集合，筛选计算实际运算时间
	 * @param driverRecordList
	 * @param driverId
	 * @return
	 */
	public int getDriverRunTime(List<DriverRecord> driverRecordList,Long driverId);

	/**
	 * 根据设备的运行时间段、产品型号查询数据库，得到 hbase检索条件，driverRecord集合
	 * @param map  设备的运行时间段
	 * @param chooseDriver2  设备id
	 * @param modelNum 产品型号
	 * @return
	 */
	public List<DriverRecord> getDriverRecordRowkeys(Map<String, String> map, Long chooseDriver2, String modelNum, Long chooseFactory2,Long chooseProductLine2) throws ParseException;
	
	/**
	 * 计算设备OEE
	 * @param runTime  设备运行时间
     * @param haltTime  计划停机时间
     * @param adjustTime  设备调整时间 
     * @param hitchTime  故障停机时间
     * @param circle  理论加工周期
     * @param output  总产量
     * @param waste 废品件
     * @param chooseExact 数值精确度
	 * @param autoTrueTime 自动OEE中的参数，设备实际运行时间，手动OEE填null
	 * @return
	 */
	public MesDriverOEE calculate(Integer runTime, Integer haltTime, Integer adjustTime, Integer hitchTime,
			Double circle, Integer output, Integer waste, Integer chooseExact, Integer autoTrueTime);
	
	/**
	 * 计算设备在  起始日期到目前时间之间，所属班次时间段之内的    运行时间段
	 * 数据格式  {2017-06-13 08:12:35=2017-06-13 16:12:35,……}
	 * @param autotime1 班次开始时间
	 * @param autotime2 班次结束时间
	 * @param autotime3 起始时间
	 * @return
	 * @throws ParseException 
	 */
	public Map<String, String> getTotalTime(String autotime1, String autotime2, String autotime3) throws ParseException;
	
	/**
	 * 获取起始日期到 指定日期 或者 现在 的所有日期，格式 ：2017-06-10,2017-06-11
	 * @param autotime3 起始日期
	 * @param closeTime 结束日期，默认为当前时间，可以为null
	 * @return
	 * @throws ParseException
	 */
	public List<String> getAllDays(String autotime3,String closeTime) throws ParseException;
	
	/**
	 * 根据id删除一条oee记录
	 * @param id
	 */
	public void deleteOEEById(Long id);
	
	/**
	 * 根据时间,设备id,班次来检索oee数据,班次为null代表检索所有班次
	 * @param start
	 * @param end
	 * @param driverId
	 * @param classes
	 * @return
	 */
	public List<MesDriverOEE> findByTimeAndClasses(Date start,Date end,Long driverId,String classes);
	
	/**
	 * 计算平均oee
	 * @param list oee数据集合
	 * @return
	 */
	public Double averageOEE(List<MesDriverOEE> list);
	
	/**
	 * 查询production_table_factory表获取设备生产'某一道工序'的产量和不合格数
	 * @param chooseProductLine2 产线
	 * @param chooseModel 产品型号
	 * @param chooseDriver2 设备ID
	 * @param autotime3 开始时间--现在
	 * @return
	 */
	public Map<String, Long> getYieldAndFailure(Long chooseFactory2, Long chooseProductLine2, String chooseModel, Long chooseDriver2,
			String autotime3);

//	List<Map<String, String>> findDriverPropertyByHbase2(Timestamp start, Timestamp end, String factoryId,
//			String productLineId, List<Long> driverPropertyIds, Long driverId) throws IOException;

	public List<DriverRecord> getDriverOutputByHbase(Timestamp startDate, Timestamp endDate, String chooseFactory,
			String chooseProductLine, Long driverId, HBasePageModel page, DateType dateType);

	
}
