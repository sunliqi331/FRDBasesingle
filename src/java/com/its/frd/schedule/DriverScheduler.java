package com.its.frd.schedule;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.Date;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.its.frd.entity.MeasuringTool;
import com.its.frd.entity.MeasuringToolAlarm;
import com.its.frd.service.MeasuringToolAlarmService;
import com.its.frd.service.MeasuringToolService;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.internal.SessionFactoryImpl;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.its.common.service.RedisService;
import com.its.frd.entity.Companyinfo;
import com.its.frd.service.CompanyinfoService;

import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;

@Component
public class DriverScheduler {
	private static final Logger log = LoggerFactory.getLogger(DriverScheduler.class);

	@Resource
	private MeasuringToolService measuringToolService;//量具Service
	@Resource
	private MeasuringToolAlarmService measuringToolAlarmService;//量具报警Service

	@Autowired
	private CompanyinfoService companyinfoService;
    @Resource(name = "redisService")
    private RedisService redisService;
//    @Resource(name="redisTemplate")
//    private RedisTemplate<String,Object> redisTemplate;


	public static final String TABLE_PREFIX = "driver_table_factory_";
	@Value("${driverTableCreate}")
	private String driverTableCreate;
	@Value("${driverTableColumns}")
	private String driverTableColumns;
	@PersistenceContext
	private EntityManager entityManager;

	private Connection conn = null;

	public DriverScheduler() {
	}
	
	/**
	 * 定期处理monitoryHistory的数据
	 * 每隔30分钟
	 *
	 */
//    @Scheduled(cron="0 0/2 * * * ?")
    public void monitoryHistoryDel() {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.debug("monitoryHistoryDel start：" + sdf.format(DateTime.now().toDate()));

        Statement s1 = null;
        ResultSet existRs = null;
        try {
            if(conn == null){
                conn = getConnection();
            }
            s1 = conn.createStatement();
            String excuteSql = " delete from monitor_history where id not in(select c.id "
                    + " from(select a.id from monitor_history a where 20 > (select count(*) "
                    + " from monitor_history where page = a.page and id > a.id ) order by "
                    + " a.id desc) as c)";
            System.out.println(new Date() + "------------执行SQL：" + excuteSql);
            int rsCount = s1.executeUpdate(excuteSql);
            System.out.println("执行数据结果Count:" + rsCount);

        } catch (SQLException e1) {
             e1.printStackTrace();
        }finally {
            try {
                if(existRs != null){
                    existRs.close();
                }
                if(s1 != null){
                    s1.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        log.debug("monitoryHistoryDel end：" + sdf.format(DateTime.now().toDate()));
    }
	
	// 秒 （0~59） 分钟（0~59） 小时（0~23）天（月）（0~31，但是你需要考虑你月的天数） 
	// 月（0~11）  天（星期）（1~7 1=SUN 或 SUN，MON，TUE，WED，THU，FRI，SAT） 年份（1970－2099）
	@Scheduled(cron="0 0/2 * * * ?")
    public void macstatusCheck() {
	    try {
	        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        log.debug("macstatusCheck start：" + sdf.format(DateTime.now().toDate()));

//	        Jedis jedis = jedisPool.getResource();
//	        Map<String, String> macHeartInfo = jedis.hgetAll("mac_status_data");
	        Map<String, Object> macHeartInfo = redisService.getHash("mac_status_data");
	        if (macHeartInfo.isEmpty())
	            return;
	        long currentTime = Instant.now().toEpochMilli();
	        Map<String, Object> macStatusInfo = new HashMap<>();
	        JSONObject jo = null;

	        log.equals(macHeartInfo.toString());
	        System.err.println(macHeartInfo.toString());

	        for (Map.Entry<String, Object> entry : macHeartInfo.entrySet()) {
	            jo = JSONObject.fromObject(entry.getValue());
	            if (120000 < currentTime - Long.valueOf(jo.getString("time")).longValue())
	                macStatusInfo.put(entry.getKey(), "1"); // NG
	            else
	                macStatusInfo.put(entry.getKey(), "0"); // OK
	        }
	        if (!macStatusInfo.isEmpty())
	            redisService.setHash("mac_status_result", macStatusInfo);

	            // redisTemplate.opsForHash().put("mac_status_result", "");
//	            jedis.hmset("mac_status_result", macStatusInfo);
//	        jedisPool.returnResource(jedis);
	        log.debug("macstatusCheck end：" + sdf.format(DateTime.now().toDate()));
	    } catch(Exception ex) {
	        log.debug(ex.getMessage());
	        System.out.println("网关定时任务---异常信息：" + ex.getMessage());
	    }

    }

	// @Scheduled(cron="0/10 * * * * ? ")
	//@Scheduled(cron = "0 0 */2 * * ?")
	public void callPartition(){

		List<Companyinfo> companys = getCompanyinfos();
		if(companys.size() == 0){
			return;
		}
		for(Companyinfo companyinfo : companys){
			String table = DriverScheduler.TABLE_PREFIX+companyinfo.getId();
			CallableStatement cs = null;
			try {
				if(conn == null){
					conn = getConnection();
				}
				cs = conn.prepareCall("{call frd_base.create_partition_by_month('frd_base','"+table+"')}");
				cs.execute();
			}catch (SQLException e) {
				// TODO: handle exception
			}finally {
				if(cs != null){
					try {
						cs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}

	}
	//@Scheduled(cron="0 0/1 * * * ?")
	public void getProductionRecordTask(){
		List<Companyinfo> companys = getCompanyinfos();
		if(companys.size() == 0){
			return;
		}
		List<String> tableNamesList = tableNameListWithSQM(companys);
		String tables = StringUtils.join(tableNamesList, ",");
		removeExistTables(tableNamesList,tables);
		generateTables(tableNamesList);
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");    
		log.debug("获取记录时间："+sdf.format(DateTime.now().toDate()));
	}    

	private Connection getConnection() throws SQLException{
		Session session = (Session) entityManager.getDelegate();
		SessionFactoryImpl sessionFactory = (SessionFactoryImpl) session.getSessionFactory();
		if(null == conn){
			conn = sessionFactory.getConnectionProvider().getConnection();
		}
		return conn;
	}
	private List<Companyinfo> getCompanyinfos(){
		List<Companyinfo> companyinfoList = companyinfoService.findByCompanytypeAndCompanystatus(Companyinfo.FACTORY, Companyinfo.COPANYSTATUS_OK);
		return companyinfoList;
	}
	private List<String> tableNameListWithSQM(List<Companyinfo> companyinfos){
		List<String> tableNamesList = new ArrayList<>();
		for(int i = 0; i < companyinfos.size(); i++ ){
			tableNamesList.add("'"+DriverScheduler.TABLE_PREFIX+companyinfos.get(i).getId()+"'");
		}
		return tableNamesList;
	}
	private boolean generateTables(List<String> tableNamesList){
		boolean flag = true;
		for(String tableName : tableNamesList){
			Statement s;
			try {
				if(conn == null){
					conn = getConnection();
				}
				s = conn.createStatement();
				boolean isCreated = s.execute(driverTableCreate.replaceAll("TABLE_NAME", tableName.substring(1, tableName.length()-1)));
				if(isCreated){
					s.close();
				}else{
					flag = false;
					log.warn("有设备记录表未创建："+tableName);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.warn("有设备记录表未创建："+tableName);
			}
		}
		return flag;
	}
	private void removeExistTables(List<String> tableNamesList,String tables){
		Statement s1 = null;
		ResultSet existRs = null;
		try {
			if(conn == null){
				conn = getConnection();
			}
			s1 = conn.createStatement();
			System.out.println(new Date() + "------------执行SQL：" + "select `TABLE_NAME` from `INFORMATION_SCHEMA`.`TABLES` where `TABLE_SCHEMA`='frd_base_new' and `TABLE_NAME` in ("+tables+")");
			existRs = s1.executeQuery("select `TABLE_NAME` from `INFORMATION_SCHEMA`.`TABLES` where `TABLE_SCHEMA`='frd_base_new' and `TABLE_NAME` in ("+tables+")");
			while(existRs.next()){
				tableNamesList.remove("'"+existRs.getString(1)+"'");
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}finally {
			try {
				if(existRs != null){
					existRs.close();
				}
				if(s1 != null){
					s1.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}


	// 秒 （0~59） 分钟（0~59） 小时（0~23）天（月）（0~31，但是你需要考虑你月的天数）
	// 月（0~11）  天（星期）（1~7 1=SUN 或 SUN，MON，TUE，WED，THU，FRI，SAT） 年份（1970－2099）
	//量具定时任务 算出使用时长 和状态
//	@Scheduled(cron="0 */1 * * * ?")
	@Scheduled(cron="0 0 */1 * * ?")
	public void measuringToolHours() {
		Integer isdelete = 0;
		List<MeasuringTool> list = this.measuringToolService.findAllByIsdelete(isdelete);
		this.measuringToolAlarmService.deleteAll();
		for(MeasuringTool measuringTool:list ){
			long nd = 1000 * 24 * 60 * 60;
			long nh = 1000 * 60 * 60;
			long nm = 1000 * 60;
			Date date = new Date();
			Timestamp starttime = measuringTool.getStarttime();
			Timestamp endtime = measuringTool.getEndtime();
			Long days = measuringTool.getDays();

			//设置使用时长
			long diff = date.getTime() - starttime.getTime();
			Long day = diff / nd;
			measuringTool.setHours(day);

			Calendar calendar = Calendar.getInstance(); //得到日历
			calendar.setTime(endtime);//把当前时间赋给日历
			calendar.set(Calendar.HOUR_OF_DAY, (int) (calendar.get(Calendar.HOUR_OF_DAY) - days));
//			calendar.add(Calendar.DAY_OF_MONTH, (int) -days);
			Date warningtime = calendar.getTime();//预警时间

			//设置量具报警状态
			if(date.getTime() < warningtime.getTime()){
				measuringTool.setStatus(0);
			}else if(warningtime.getTime() < date.getTime() && date.getTime() < endtime.getTime()){
				measuringTool.setStatus(1);

				MeasuringToolAlarm measuringToolAlarm = new MeasuringToolAlarm();
				measuringToolAlarm.setMeasuringTool(measuringTool);
				measuringToolAlarm.setStatus(1);
				measuringToolAlarm.setAlarmtime(new Timestamp(warningtime.getTime()));
				measuringToolAlarm.setEndtime(measuringTool.getEndtime());
				measuringToolAlarm.setRecordtime(new Timestamp(date.getTime()));
				measuringToolAlarm.setHours((long) 0);
				this.measuringToolAlarmService.saveOrUpdate(measuringToolAlarm);
			}else if(date.getTime()>endtime.getTime()){
				measuringTool.setStatus(2);

				MeasuringToolAlarm measuringToolAlarm = new MeasuringToolAlarm();
				measuringToolAlarm.setMeasuringTool(measuringTool);
				measuringToolAlarm.setStatus(2);
				measuringToolAlarm.setAlarmtime(new Timestamp(warningtime.getTime()));
				measuringToolAlarm.setEndtime(measuringTool.getEndtime());
				measuringToolAlarm.setRecordtime(new Timestamp(date.getTime()));
				measuringToolAlarm.setHours((date.getTime() - endtime.getTime()) / nh);
				this.measuringToolAlarmService.saveOrUpdate(measuringToolAlarm);
			}else{
				measuringTool.setStatus(0);
			}
			this.measuringToolService.saveOrUpdate(measuringTool);
		}
	}

}
