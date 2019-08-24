package com.its.frd.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.its.common.entity.main.User;
import com.its.common.service.UserService;
import com.its.common.util.dwz.AjaxObject;
import com.its.common.util.dwz.Page;
import com.its.common.util.persistence.DynamicSpecifications;
import com.its.common.util.persistence.SearchFilter;
import com.its.common.util.persistence.SearchFilter.Operator;
import com.its.common.utils.SecurityUtils;
import com.its.frd.entity.MesAlarmSendRelation;
import com.its.frd.entity.MesDriver;
import com.its.frd.entity.MesPoints;
import com.its.frd.entity.MesProcedureProperty;
import com.its.frd.entity.MesProcedurePropertyPointLog;
import com.its.frd.entity.MesProduct;
import com.its.frd.entity.MesProductProcedure;
import com.its.frd.entity.Usercompanys;
import com.its.frd.params.AlarmInfo;
import com.its.frd.params.AlarmInfo.InfoType;
import com.its.frd.params.DriverAlarm;
import com.its.frd.params.Info;
import com.its.frd.params.Info.ChangeValueType;
import com.its.frd.params.ProductAlarm;
import com.its.frd.service.MesAlarmSendRelationService;
import com.its.frd.service.MesDriverService;
import com.its.frd.service.MesPointsService;
import com.its.frd.service.MesProcedurePropertyPointLogService;
import com.its.frd.service.MesProcedurePropertyService;
import com.its.frd.service.MesProductService;
import com.its.frd.service.UsercompanysService;
import com.its.frd.util.ResourceUtil;

/**
 * 功能: 为发送告警邮件信息服务
 */
@Controller
@RequestMapping("/sendAlarm")
public class SendAlarmInfoController {
	@Resource
	private JavaMailSenderImpl mailSender;
	@Resource
	private MesDriverService driverServ;
	@Resource
	private MesPointsService pointServ;
	@Resource
	private MesProductService productServ;
	@Resource
	private MesProcedurePropertyService mesProcedurePropertyService;
	@Resource
	private MesAlarmSendRelationService alarmRelationServ;
	@Autowired
	private MesProcedurePropertyPointLogService mesProcedurePropertyPointLogService;
	@Resource
	private UserService userService;
	@Resource
	private UsercompanysService usercompanysService;

    @RequiresPermissions("sendAlarm:view")
    @RequestMapping("/sendAlarmList")
    public String sendAlarmList() {
    	Long companyId = SecurityUtils.getShiroUser().getCompanyid();
		if(companyId == null)
			return "error/403";
        return "Alarm/sendAlarmList";
    }
    
    /**
     * 分页
     * @param request
     * @param page
     * @return
     * @throws JsonProcessingException 
     */
    @RequestMapping("/Data")
    @ResponseBody
    public String data(HttpServletRequest request, Page page) throws JsonProcessingException {
        Map<String, Object> map = new HashMap<String, Object>();
        page.setOrderField("creattime");
        page.setOrderDirection(Page.ORDER_DIRECTION_DESC);
        Specification<MesAlarmSendRelation> specification = DynamicSpecifications.bySearchFilter(request, MesAlarmSendRelation.class
//                ,new SearchFilter("user.id",Operator.EQ,SecurityUtils.getShiroUser().getUser().getId())
                ,new SearchFilter("companyid",Operator.EQ,SecurityUtils.getShiroUser().getCompanyid())
                );
        List<MesAlarmSendRelation> mesAlarmSendRelations = alarmRelationServ.findPage(specification, page);
        for(MesAlarmSendRelation mesAlarmSendRelation : mesAlarmSendRelations){
        	String talarttype = "";
            if(mesAlarmSendRelation.getAlarttype().contains("driver")){
            		talarttype += "设备";
            }
            if(mesAlarmSendRelation.getAlarttype().contains("product")){
            	if(!talarttype.equals("")){
            		talarttype += ",";
            	}
            	talarttype += "产品";
            }
            mesAlarmSendRelation.setTalarttype(talarttype);
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        map.put("page", page);
        map.put("mesAlarmSendRelations", mesAlarmSendRelations);
        return mapper.writeValueAsString(map);
    }
    
    /**
     * 删
     * @param id
     * @return
     */
    @RequiresPermissions("sendAlarm:delete")
    @RequestMapping("/deleteMany")
    @ResponseBody
    public String deleteMany(Long[] ids){
        try {
            for (int i = 0; i < ids.length; i++) {
                alarmRelationServ.deleteById(ids[i]);
            }
        } catch (Exception e) {
            return AjaxObject.newError("删除失败！").toString();
        }
        return AjaxObject.newOk("删除成功！").toString();
    }
    

    @RequiresPermissions("sendAlarm:save")
    @RequestMapping("/addSendAlarm")
    public String addSendAlarm(Map<String, Object> map,Page page) {
        List<User> users = userService.findAll(page);
        map.put("users", users);
        return "Alarm/addSendAlarm";
    }
    /*
     * 添加或修改
     */
    @RequiresPermissions(value={"sendAlarm:save","sendAlarm:edit"},logical=Logical.OR)
    @RequestMapping("/saveOrUpdate")
    @ResponseBody
    public String saveOrUpdate(HttpServletRequest request,Page page) {
        try {
            String userids = request.getParameter("userids");
            String alarttypes[] = request.getParameterValues("alarttype");
            User user = new User();
            if (userids.length() > 0) {
                String[] arr = userids.split(",");
                for(int i=0;i<arr.length;i++){
                		MesAlarmSendRelation mesAlarmSendRelation = new MesAlarmSendRelation();
                		user = userService.findById(Long.valueOf(arr[i]));
                		mesAlarmSendRelation.setUser(user);
                		mesAlarmSendRelation.setAlarttype(StringUtils.join(alarttypes, ","));
                		mesAlarmSendRelation.setCompanyid(SecurityUtils.getShiroUser().getCompanyid());
                		mesAlarmSendRelation.setCreattime(new Date());
                		alarmRelationServ.saveOrUpdate(mesAlarmSendRelation);
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
            return AjaxObject.newError("添加失败！").setCallbackType("").toString();
        }
        return AjaxObject.newOk("添加成功！").toString();
    }

    @RequestMapping("/data2")
    @ResponseBody
    public String data2(HttpServletRequest request,User user,Page page) throws JsonProcessingException {
        Map<String,Object> map = new HashMap<String,Object>();
        List<Long> ids = new ArrayList<Long>();
        List<User> users = new ArrayList<User>();
        List<MesAlarmSendRelation> mesAlarmSendRelations = alarmRelationServ.findPage(page);
        List<Usercompanys> usercompanys = usercompanysService.findByCompanyid(SecurityUtils.getShiroUser().getCompanyid());
        for(Usercompanys usercompany : usercompanys){
            ids.add(usercompany.getUserid());
        }
        for(MesAlarmSendRelation mesAlarmSendRelation : mesAlarmSendRelations){
            ids.remove(mesAlarmSendRelation.getUser().getId());
        }
        ids.remove(SecurityUtils.getShiroUser().getUser().getId());
        List<SearchFilter> searchFilters = new ArrayList<>();
        SearchFilter searchFilter1 = new SearchFilter("registerstate",  Operator.EQ, "1");
        SearchFilter searchFilter2 = new SearchFilter("id",  Operator.IN, ids.toArray());
    	searchFilters.add(searchFilter1);
    	searchFilters.add(searchFilter2);
        if(StringUtils.isNotEmpty(user.getRealname())){
        	SearchFilter searchFilter = new SearchFilter("realname",  Operator.LIKE, user.getRealname());
        	searchFilters.add(searchFilter);
        }
        if(StringUtils.isNotEmpty(user.getPhone())){
        	SearchFilter searchFilter = new SearchFilter("phone",  Operator.LIKE, user.getRealname());
        	searchFilters.add(searchFilter);
        }
        if(ids.size() > 0){
            Specification<User> specification = DynamicSpecifications.bySearchFilter(request, User.class
                    ,searchFilters);
            users = userService.findByExample(specification, page);
        }
//        for(User user : users){
//            ids.add(user.getId());
//        }
//        for(MesAlarmSendRelation mesAlarmSendRelation : mesAlarmSendRelations){
//            ids.remove(mesAlarmSendRelation.getUser().getId());
//        }
//        for(Long id : ids){
//            users2.add(userService.findById(id));
//        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        map.put("page", page);
        map.put("user", users);
        return mapper.writeValueAsString(map);
    }
    
    /**
	 * 发送告警信息给关联的用户.
	 */
	@RequestMapping("/sendInfos")
	@ResponseBody
	public boolean sendForAlarmInfos(HttpServletRequest request){
	    
	//public boolean sendForAlarmInfos(AlarmInfo alarmInfo){
		try {
		    String productLineid = request.getParameter("productLineid");
			//this.sendEmailForAlarm(alarmInfo);
		    System.out.println("发送告警信息给关联的用户:" + productLineid);
		    System.out.println("发送告警信息给关联的用户:" + productLineid);
		    System.out.println("发送告警信息给关联的用户:" + productLineid);
		    System.out.println("发送告警信息给关联的用户:" + productLineid);
		    System.out.println("发送告警信息给关联的用户:" + productLineid);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	@RequestMapping("/sendInfos_json")
	@ResponseBody
	public boolean sendForAlarmInfos_json(@RequestBody String alarmInfo){
		try {
			//System.out.println(alarmInfo);
			//this.sendEmailForAlarm(alarmInfo);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 把告警信息以邮件的形式发送出去.
	 */
	private void sendEmailForAlarm(String alarmInfo){
		Long companyId = this.getCompanyId(alarmInfo);
		if(companyId == null)
			return;
		List<String> mailAdrs = this.getEmailAddressList(companyId,alarmInfo);
		try {
			this.sendMail(mailAdrs, alarmInfo);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取公司id
	 * @param alarmInfo
	 * @return
	 */
	private Long getCompanyId(String alarmInfo){
		//JSONObject jsonObject = new JSONObject(alarmInfo);
		
		
		
		if(alarmInfo == null)
			return null;
		net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(alarmInfo);
		//if(InfoType.DRIVER.equals(alarmInfo.getInfoType())){
		if(InfoType.DRIVER.toString().equals(jsonObject.getString("infoType"))){
			
			DriverAlarm driverAlarm = this.getDriverAlarm(jsonObject);
			if(driverAlarm == null || driverAlarm.getDriverid() == null)
				return null;
			return this.getCompanyIdForDriverId(driverAlarm.getDriverid());
		}else if(InfoType.PRODUCT.toString().equals(jsonObject.getString("infoType"))){
			ProductAlarm productAlarm = this.getProductAlarm(jsonObject);
			if(productAlarm == null || productAlarm.getProductId() == null)
				return null;
			return this.getComapnyIdForProductId(productAlarm.getProductId());
		}
		return null;
	}
	
	/**
	 * 通过公司id获取发送邮件地址列表
	 * @param companyid
	 * @return
	 */
	private List<String> getEmailAddressList(Long companyid,String alarmInfo){
		List<String> emailAddrs = new ArrayList<>();
		net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(alarmInfo);
		if(alarmInfo == null || jsonObject.getJSONObject("alarm").isEmpty())
			return emailAddrs;
		List<MesAlarmSendRelation> alarmRs = alarmRelationServ.findByCompanyid(companyid);
		String string = jsonObject.getString("infoType");
		for(MesAlarmSendRelation alarmR : alarmRs){
			User user = alarmR.getUser();
			if(user != null){
				if(user.getEmail() != null && !user.getEmail().trim().equals("")){
					if(alarmR.getAlarttype().contains(string.toLowerCase())){
						emailAddrs.add(user.getEmail());
					}
					
				}
			}
				
		}
		return emailAddrs;
	}
	
	/**
	 * 通过设备id获取公司id
	 * @param driverId
	 * @return
	 */
	private Long getCompanyIdForDriverId(Long driverId){
		MesDriver driver = driverServ.findById(driverId);
		return driver.getCompanyinfo().getId();
	}
	
	/**
	 * 通过产品id获取公司id
	 * @param productId
	 * @return
	 */
	private Long getComapnyIdForProductId(Long productId){
		MesProduct product = productServ.findById(productId);
		if(null != product){
			return product.getCompanyinfo().getId();
		}
		return null;
	}
	
	/**
	 * 获取设备告警对象.
	 * @param alarmInfo
	 * @return
	 */
	private DriverAlarm getDriverAlarm(net.sf.json.JSONObject alarmInfo){
		if(alarmInfo == null || (null != alarmInfo && alarmInfo.getJSONObject("alarm").isEmpty()))
			return null;
		DriverAlarm driverAlarm = (DriverAlarm) net.sf.json.JSONObject.toBean(alarmInfo.getJSONObject("alarm"), DriverAlarm.class);
		/*if(InfoType.DRIVER.equals(alarmInfo.getInfoType())){
			return (DriverAlarm)alarmInfo.getAlarm();
		}*/
		return driverAlarm;
	}
	
	/**
	 * 获取产品告警对象
	 * @param alarmInfo
	 * @return
	 */
	private ProductAlarm getProductAlarm(net.sf.json.JSONObject alarmInfo){
		if(alarmInfo == null || (null != alarmInfo && alarmInfo.getJSONObject("alarm").isEmpty()))
			return null;
		ProductAlarm productAlarm = (ProductAlarm) net.sf.json.JSONObject.toBean(alarmInfo.getJSONObject("alarm"), ProductAlarm.class);
		/*if(InfoType.PRODUCT.equals(alarmInfo.getInfoType())){
			return (ProductAlarm)alarmInfo.getAlarm();
		}*/
		return productAlarm;
	}
	
	/**
	 * 把告警信息发送出去
	 * @param mailAdrs
	 * @param alarmMsg
	 * @throws MessagingException 
	 */
	private void sendMail(List<String> mailAdrs,String alarmInfo) throws MessagingException{
		net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(alarmInfo);
		if(alarmInfo == null || jsonObject.getJSONObject("alarm").isEmpty())
			return;
		 SimpleMailMessage mailMessage = new SimpleMailMessage(); 
		 //邮件标题
		 String subject = "";
		 //邮件内容
		 String text = "";
		 Info info = null;
		 try {
			 if(InfoType.DRIVER.toString().equals(jsonObject.getString("infoType"))){
				 DriverAlarm driverAlarm = this.getDriverAlarm(jsonObject);
				 info = driverAlarm.getInfo();
				 MesDriver driverName = this.getDriverName(driverAlarm.getDriverid());
				 MesPoints pointName = this.getPointName(info.getPointId());
				 Double changeValue = info.getChangeValue();
				 String valueType = info.getValueType().toString().equals(ChangeValueType.UP.toString()) ? "升高":"降低";
				 if(driverName == null || pointName == null)
					 return;
				 subject = driverName.getName() + "检测异常";
				 text = "测点： " + pointName.getName()+"较标准值"+valueType+":"+changeValue+"<br>设备:"+driverName.getName()+"<br>产线:"+driverName.getMesProductline().getLinename();
				
			 }else if(InfoType.PRODUCT.toString().equals(jsonObject.getString("infoType"))){
				 ProductAlarm productAlarm = this.getProductAlarm(jsonObject);
				 info = productAlarm.getInfo();
				 MesProduct productName = this.getProductName(productAlarm.getProductId());
				 MesPoints pointName = this.getPointName(info.getPointId());
				 Double changeValue = info.getChangeValue();
				 String valueType = info.getValueType().toString().equals(ChangeValueType.UP.toString()) ? "升高":"降低";
				 MesProcedurePropertyPointLog propertyPointLog = mesProcedurePropertyPointLogService.findLastedLogByPointId(pointName.getId());
				 MesProcedureProperty procedureProperty = mesProcedurePropertyService.findById(null != propertyPointLog ? propertyPointLog.getPropertyid() : 0);
				 
				 if(productName == null)
					 return;
				 subject = productName.getName() + "检测异常";
				 text = "测点: " + pointName.getName()+",较标准值"+valueType+":"+changeValue;
				 if(null != procedureProperty){
					 text += "<br>工序属性:"+procedureProperty.getPropertyname();
					 MesProductProcedure mesProductProcedure = procedureProperty.getMesProductProcedure();
					 if(mesProductProcedure != null){
						 text += "<br>工序:"+mesProductProcedure.getProcedurename();
						 MesProduct mesProduct = mesProductProcedure.getMesProduct();
						 if(mesProduct != null){
							 text += "<br>工件:"+mesProduct.getName();
						 }
					 }
				 }
			 }
			 if(info == null)
				 return;
			 if(Info.ChangeValueType.UP.equals(info.getChangeValue())){
				 text += ", 超出上限"+info.getChangeValue();
			 }else if(Info.ChangeValueType.DOWN.equals(info.getChangeValue())){
				 text += ", 低于下限"+info.getChangeValue();
			 }
		} catch (Exception e) {
			return;
		}
		 if(mailAdrs == null || mailAdrs.size() <= 0)
			 return;
		 String[] mailAddresses = new String[mailAdrs.size()];
		 mailAdrs.toArray(mailAddresses);
		 MimeMessage mimeMessage = mailSender.createMimeMessage();
		 MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
		 // 来自
		 helper.setFrom(mailSender.getUsername());
		 // 标题
		 helper.setSubject(subject);
		 // 邮件内容
		 helper.setText(text,true);
		 // 发送对象
		 helper.setTo(InternetAddress.parse(StringUtils.join(mailAddresses,","))); 
		/* try {
			mimeMessage.setSubject(subject);
			mimeMessage.setText(text);
			mimeMessage.setRecipients(Message.RecipientType.TO,InternetAddress.parse(StringUtils.join(mailAddresses))); 
		} catch (MessagingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		/* mailMessage.setSubject(subject);  
		 mailMessage.setText(text);
		 mailMessage.setFrom(ResourceUtil.getValueForDefaultProperties("mail.username"));
		
		 mailMessage.setTo(mailAdrs.toArray(mailAddresses));*/
		 //发送邮件
		 try {
			 mailSender.send(mimeMessage);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("邮件发送异常!");
		}
	}
	
	/**
	 * 邮件测试
	 * @return
	 * @throws MessagingException 
	 */
	@RequestMapping("/sm")
	@ResponseBody
	public Boolean sendMailTest() throws MessagingException{
		 MimeMessage mimeMessage = mailSender.createMimeMessage();
		 MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
		 String[] mailAddresses = {"connor-tan@outlook.com","jialiang.tan@pactera.com"};
		 try {
			 helper.setFrom(mailSender.getUsername());
			 helper.setSubject("asdasdasd");
			 helper.setText("asdasd<br>asdasdasdasdasd<br>",true);
			 helper.setTo(InternetAddress.parse(StringUtils.join(mailAddresses,","))); 
		} catch (MessagingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		SimpleMailMessage msg = new SimpleMailMessage(); 
		msg.setText("测试邮件");
		msg.setTo("15601745617@163.com");
		msg.setSubject("内容");
		msg.setFrom(ResourceUtil.getValueForDefaultProperties("mail.username"));
		try {
			mailSender.send(mimeMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 获取设备名称
	 * @param driverId
	 * @return
	 */
	private MesDriver getDriverName(Long driverId){
		MesDriver driver = driverServ.findById(driverId);
		return driver;
	}
	/**
	 * 获取测点名称
	 * @param pointId
	 * @return
	 */
	private MesPoints getPointName(Long pointId){
		MesPoints point = pointServ.findById(pointId);
		return point;
	}
	/**
	 * 获取产品名称
	 * @param productId
	 * @return
	 */
	private MesProduct getProductName(Long productId){
		MesProduct product = productServ.findById(productId);
		return product;
	}
	
	
}
