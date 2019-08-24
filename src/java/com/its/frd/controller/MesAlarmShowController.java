package com.its.frd.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JavaType;
import com.its.frd.entity.*;
import com.its.frd.service.*;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.Lists;
import com.its.common.entity.main.Dictionary;
import com.its.common.service.DictionaryService;
import com.its.common.service.RedisService;
import com.its.common.util.dwz.AjaxObject;
import com.its.common.util.dwz.Page;
import com.its.common.util.persistence.DynamicSpecifications;
import com.its.common.util.persistence.SearchFilter;
import com.its.common.util.persistence.SearchFilter.Operator;
import com.its.common.utils.SecurityUtils;
import com.its.frd.util.DateUtils;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/MesAlarmShow")
public class MesAlarmShowController {

	private final String PAGEPRE = "Alarm/";

	@Resource
	private CompanyinfoService cpServ;

    @Resource
    private DictionaryService dictionaryService;

    @Autowired
    private MesAlarmShowService mesAlarmShowService;

    @Resource
	private ProductAndEnergyAndDriverChartService productChartServ;

    @Resource
    private MesDriverPointService mesDriverPointService;

    @Autowired
    private MesAlarmRelationService marServ;

    @Autowired
    private MesPointsService mps;

    @Resource
	private CompanyfileService companyfileService;

    @Resource
	private CompanyfileService cfServ;

    @Resource
    private CompanyinfoService companyinfoService;

    @Resource
    private MesProductlineService mesProductlineService;

	@Resource
	private MesProductService mesProductService;

	@Resource
	private MesDataProductProcedureService mesDataProductProcedureService;


    @Resource(name="redisService")
    private RedisService redisService;

	 /**
     * 进入设备报警信息显示屏页面
     * @return
     */
    @RequiresPermissions("alarmShow:show")
    @RequestMapping("/alarmShow")
    public ModelAndView alarmShow() {
    	ModelAndView modelAndView = new ModelAndView(PAGEPRE + "alarmShowList");
    	Long companyId = SecurityUtils.getShiroUser().getCompanyid();
    	List<Dictionary> itemList = dictionaryService.findByThemeName("设备告警类型", null);
    	modelAndView.addObject("companyinfos", cpServ.getTreeFactory());
    	modelAndView.addObject("itemList", itemList);
		if(companyId == null){
			return new ModelAndView("error/403");
		}
        return modelAndView;
    }

    /**
     * 初始化显示两千条设备告警数据
     * @param page
     * @return
     * @throws JsonProcessingException
     */
    @RequestMapping("/initTable")
    @ResponseBody
    public String initTbale(Page page) throws JsonProcessingException {
    	Map<String, Object> map = new HashMap<String, Object>();
    	ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		List<MesAlarmShow> alarmShowList =  mesAlarmShowService.findNewTwoThousand();
		if(alarmShowList.size()<1){
			return null;
		}
		//手动分页
		page.setTotalCount(alarmShowList.size());
		int perPage = page.getNumPerPage(); //每页显示数量
		int nowPage = page.getPageNum();//当前页码
		int startIndex = 0; //开始索引
		int endIndex = 0; //结束索引
		if(perPage<alarmShowList.size()){
			startIndex = (nowPage-1)*perPage;
			endIndex = nowPage*perPage;
			if(endIndex>alarmShowList.size()){
				endIndex = alarmShowList.size();
			}
			List<MesAlarmShow> subList = alarmShowList.subList(startIndex, endIndex);
			map.put("alarmShowList", subList);
		}else{
			map.put("alarmShowList", alarmShowList);
		}
		map.put("page", page);
    	return mapper.writeValueAsString(map);
    }

    /**
     * 条件检索设备告警记录
     * @param request
     * @param page
     * @return
     * @throws JsonProcessingException
     */
    @RequiresPermissions("alarmShow:show")
	@RequestMapping("/tableSearch")
	@ResponseBody
	public String tableSearch(HttpServletRequest request, Page page) throws JsonProcessingException{
		Map<String, Object> map = new HashMap<String, Object>();
    	ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		List<MesAlarmShow> alarmShowList = new ArrayList<>();
		List<MesDriver> driverList = productChartServ.getDriverListByCompanyId(SecurityUtils.getShiroUser().getCompanyid());
		List<Long> driverIds = new ArrayList<>();
		for(MesDriver md : driverList){
			driverIds.add(md.getId());
		}
		if(driverIds.size()>0){
			Specification<MesAlarmShow> specification = DynamicSpecifications.bySearchFilter(request, MesAlarmShow.class
                    ,new SearchFilter("mesDriver.id",Operator.IN,driverIds.toArray()));
			alarmShowList = mesAlarmShowService.findPage(specification, page);
		}
		map.put("alarmShowList", alarmShowList);
		map.put("page", page);
    	return mapper.writeValueAsString(map);
	}

	/**
	 * 修改告警信息确认状态
	 * @param ids
	 * @return
	 */
	@RequiresPermissions("alarmShow:edit")
	@RequestMapping(value="/statusChange", method = RequestMethod.POST)
	@ResponseBody
	public String statusChange(Long[] ids){
		try {
			if(ids!=null && ids.length>0){
				for(int i = 0; i < ids.length; i++){
					mesAlarmShowService.updateStatusChange(ids[i]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxObject.newError("状态确认失败!").setCallbackType("").toString();
		}
    	return AjaxObject.newOk("状态确认成功!").setCallbackType("").toString();
	}


	/**
	 * 进入告警测点关系配置界面
	 * @param id
	 * @return
	 */
	@RequiresPermissions("alarmShow:edit")
	@RequestMapping("/editAlarmRelation/{id}")
	public String addDriverPoint(@PathVariable Long id,Map<String,Object> map) {
		MesDriverPoints driverPiont = mesDriverPointService.findById(id);
		MesPoints point = driverPiont.getMesPoints();
		List<Dictionary> itemList = dictionaryService.findByThemeName("设备告警类型", null);
		map.put("point", point);
		map.put("itemList", itemList);
		return PAGEPRE + "editAlarmRelation";
	}

	/**
	 * 展示测点的报警信息关联表
	 */
	@RequiresPermissions("alarmShow:show")
	@RequestMapping("/initRelationTable/{id}")
	@ResponseBody
	public String listRelationById(@PathVariable Long id,HttpServletRequest request,Page page) throws JsonProcessingException{
		Map<String, Object> map = new HashMap<String, Object>();
    	ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		Specification<MesAlarmRelation> specification = DynamicSpecifications.bySearchFilter(request, MesAlarmRelation.class
                ,new SearchFilter("mesPoints.id",Operator.EQ,id));
		List<MesAlarmRelation> relationList = marServ.findPage(specification, page);
		map.put("page", page);
		map.put("relationList", relationList);
		return mapper.writeValueAsString(map);
	}

	/**
	 * 删除报警测点关联关系
	 * @param ids
	 * @return
	 */
	@RequiresPermissions("alarmShow:edit")
	@RequestMapping(value="/deleteAlarmRelation",method=RequestMethod.POST)
	@ResponseBody
	public String deleteAlarmRelation(Long[] ids){
		try {
			if(ids!=null && ids.length>0){
				for(int i = 0; i < ids.length; i++){
					marServ.deleteById(ids[i]);
				}
			}
		} catch (Exception e) {
			return AjaxObject.newError("删除失败!").setCallbackType("").toString();
		}
		return AjaxObject.newOk("删除成功!").setCallbackType("").toString();
	}

	/**
	 * 添加测点关联关系
	 * @param
	 * @return
	 */
	@RequiresPermissions("alarmShow:edit")
	@RequestMapping(value="/addAlarmRelation",method=RequestMethod.POST)
	@ResponseBody
	public String saveAlarmRelation(String alarmType,String alarmCode,String alarmInfo,Long pointId){
		try {
			MesPoints point = mps.findById(pointId);
			if(null == point){
				return "添加失败,测点不存在!";
			}
			if("SSS".equals(alarmCode)){
				return "非法字符!";
			}
			List<MesAlarmRelation> alarmRelations = marServ.findByPointId(pointId);
			for(MesAlarmRelation ma : alarmRelations){
				if(alarmCode.equals(ma.getAlarmCode())){
					return "报警ID重复!";
				}
			}
			MesAlarmRelation mesAlarmRelation = new MesAlarmRelation();
			mesAlarmRelation.setMesPoints(point);
			mesAlarmRelation.setAlarmType(alarmType);
			mesAlarmRelation.setAlarmCode(alarmCode);
			mesAlarmRelation.setInfo(alarmInfo);
			marServ.saveOrUpdate(mesAlarmRelation);
		} catch (Exception e) {
			return "添加失败!";
		}
		return "ok";
	}

	/**
	 * 导入模板
	 * 数据类型：csv文本
	 * 数据格式：A0001 报警 预检站，工件方向反正放置错误！
	 * @return 状态值
	 */
	@RequestMapping(value="/importAlarmRelation",method=RequestMethod.POST)
	@ResponseBody
	public String importAlarmRelation(MultipartFile alarmRelationFile,Long pointId){
		MesPoints point = mps.findById(pointId);
		if(null == point){
			return "error";
		}
		//报警类型的字典集合
		List<Dictionary> itemList = dictionaryService.findByThemeName("设备告警类型", null);
		String statusCode = marServ.saveModelRelation(itemList,alarmRelationFile,point);
		return statusCode;
	}

	/**
	 * 导出模板
	 * @param pointId
	 * @return
	 */
	@RequestMapping("/exportAlarmRelation")
	@ResponseBody
	public String exportAlarmRelation(Long pointId){
		if(pointId==null){
			return "error";
		}
		//创建本地文件
		String newFileName = UUID.randomUUID().toString()+".csv";
		String filePath = com.its.frd.util.ResourceUtil.getUploadDirectory();
		String classPath = this.getClass().getClassLoader().getResource("/").getPath();
		int position = classPath.toLowerCase().indexOf("web-inf");
		position = -1;
		if(position != -1){
			filePath = classPath.substring(0, position)+"FRD_upload_FILE/";
		}
		filePath += DateUtils.getyyyyMMddHH2(new Date()); // 精确到小时
		File uploadFile = new File(filePath + File.separator);
		if (!uploadFile.exists()) {
			uploadFile.mkdirs();
		}
		//向本地文件中写入数据
		Long fileLength = null;
		try {
			File relationFile = new File(filePath + File.separator + newFileName);
			if(!relationFile.exists()){
				relationFile.createNewFile();
			}
			String fileContent = "";
			List<MesAlarmRelation> relationList = marServ.findByPointId(pointId);
			for(MesAlarmRelation ma : relationList){
				fileContent += "\t"+ma.getAlarmCode()+","+ma.getAlarmType()+","+ma.getInfo()+"\n";
			}
			PrintWriter pw = new PrintWriter(relationFile, "gbk");
			Integer length = fileContent.length();
			fileLength = length.longValue();
			pw.println(fileContent);
			pw.close();
		} catch (IOException e) {
			return "error";
		}
		Companyfile cpfile = new Companyfile();
		cpfile.setfilebasename("设备报警测点配置模板_"+pointId);
		cpfile.setfilelength(fileLength);
		cpfile.setfilenewname(newFileName);
		cpfile.setfilepath(filePath + File.separator + newFileName);
		companyfileService.saveOrUpdate(cpfile);
		//回传文件id，下载文件
		return cpfile.getId()+"";
	}

	@RequestMapping("/downloadExport/{id}")
	public void downloadExport(@PathVariable Long id,HttpServletRequest request,HttpServletResponse response) throws IOException{
		Companyfile companyfile = cfServ.findById(id);
		if(null == companyfile || (null != companyfile && !new File(companyfile.getfilepath()).exists())){
			String errorMessage = "Sorry. The file you are downloading does not exist";
			System.out.println(errorMessage);
			OutputStream outputStream = response.getOutputStream();
			outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
			outputStream.close();
			return;
		}
		File file = new File(companyfile.getfilepath());
		response.setContentType("application/octet-stream");

		response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() +"\""));

		response.setContentLength((int)file.length());

		InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
		FileCopyUtils.copy(inputStream, response.getOutputStream());
	}

	/**
	 * 监控页面中展示最新的设备告警数据
	 * @param scope 检索范围 ：工厂，产线，设备
	 * @param id
	 * @param line 展示的行数
	 * @return
	 * @throws JsonProcessingException
	 */
	@RequestMapping("/getDataForMonitor")
	@ResponseBody
	public String getDataForMonitor(String scope,Long id,Long line) throws JsonProcessingException{
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		List<MesDriver> driverList = new ArrayList<>();
		//所选择范围下的设备Id集合
		List<Long> driverIds = new ArrayList<>();
		List<MesAlarmShow> alarmShowList = new ArrayList<>();
		if("company".equals(scope)){
			Companyinfo company = companyinfoService.findById(id);
			if(null != company){
				List<MesProductline> procutionLines = productChartServ.getProductlineOfFactory(company);
				driverList = productChartServ.getDriverListForProductLineList(procutionLines);
			}
		}else if("productline".equals(scope)){
			MesProductline productline = mesProductlineService.findById(id);
			if(null != productline){
				List<MesProductline> procutionLines = new ArrayList<>();
				procutionLines.add(productline);
				driverList = productChartServ.getDriverListForProductLineList(procutionLines);
			}
		}else if("driver".equals(scope)){
			driverIds.add(id);
		}
		for(MesDriver md : driverList){
			driverIds.add(md.getId());
		}
		alarmShowList = mesAlarmShowService.getDataWithDriverIdsAndLine(driverIds,line);
		for(MesAlarmShow obj : alarmShowList) {
			if("0".equals(obj.getStatus())) {
				obj.setStatus("未确定");
			} else {
				obj.setStatus("确定");
			}
		}
		if(alarmShowList.size()<1){
			return null;
		}
		return mapper.writeValueAsString(alarmShowList);
	}

    /**
     * 监控页面中展示最新的网关告警数据
     * @param macString 网关MAC地址加类型加
     * @return
     * @throws JsonProcessingException
     */
    @RequestMapping("/getGateWayConnectionMonitor")
    @ResponseBody
    public String getGateWayConnectionMonitor(@RequestParam("list")String macString) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
		//使用jackson将json转为List<>
		JavaType jt = mapper.getTypeFactory().constructParametricType(ArrayList.class, Pojo.class);
		try {
			List<Pojo> list =  (List<Pojo>)mapper.readValue(macString, jt);
			mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
			List<String> macNowConStatus = Lists.newArrayList();
			List<Object> macNowStats = Lists.newArrayList();
			for(Pojo pojo:list){
				JSONObject outObj = new JSONObject();
				if(pojo.getDataType().equals("ArcReal")){
					outObj.put("mac", pojo.getMac());

					String conStats = String.valueOf(redisService.getHash("mac_status_data", pojo.getMac()));
					if(StringUtils.isNotBlank(conStats)&&!conStats.equals("null")) {
						macNowConStatus.add(StringUtils.isNotEmpty(conStats) ? conStats : "1");
						outObj.put("macStatsu", StringUtils.isNotEmpty(conStats) ? conStats : "1");
					} else {
						macNowConStatus.add("1");
						outObj.put("macStatsu", "1");
					}
				}else{//Arc
					String conStats = String.valueOf(redisService.getHash("drive_status", pojo.getDriverid()));
					outObj.put("driverid", pojo.getDriverid());
					if(StringUtils.isNotBlank(conStats)&&!conStats.equals("null")) {
						macNowConStatus.add(StringUtils.isNotEmpty(conStats) ? conStats : "2");
						outObj.put("macStatsu", StringUtils.isNotEmpty(conStats) ? conStats : "2");
					} else {
						macNowConStatus.add("2");
						outObj.put("macStatsu", "2");
					}
				}
				macNowStats.add(outObj);
			}
			return mapper.writeValueAsString(macNowStats);

		} catch (IOException e) {
			e.printStackTrace();
			return mapper.writeValueAsString("");
		}

//		try {
//            // 实验data
//            // macString = "00-14-97-14-3A-2F, F4-8E-38-A6-E8-C3";
//            mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
//			List<String> macNowConStatus = Lists.newArrayList();
//			List<Object> macNowStats = Lists.newArrayList();
//            if(dataType.equals("ArcReal")){
//				if(null != macString && 0 < macString.length) {
//					JSONObject macConStatus = null;
//					for(String mac : macString) {
//						mac = mac.contains("[") ? mac.replace("[", "") : mac;
//						mac = mac.contains("]") ? mac.replace("]", "") : mac;
//						mac = mac.replaceAll("\"","");
//						System.out.println(mac);
//						JSONObject outObj = new JSONObject();
//						outObj.put("mac", mac);
//
//						String conStats = String.valueOf(redisService.getHash("mac_status_data", mac));
//						if(StringUtils.isNotBlank(conStats)) {
//							macNowConStatus.add(StringUtils.isNotEmpty(conStats) ? conStats : "1");
//							outObj.put("macStatsu", StringUtils.isNotEmpty(conStats) ? conStats : "1");
//						} else {
//							macNowConStatus.add("1");
//							outObj.put("macStatsu", "1");
//						}
//
////                    macConStatus = JSONObject.fromObject(redisService.getHash("mac_status_data", mac));
////                    if (null != macConStatus && 0 < macConStatus.size()) {
////                        // 最后check的时间点，当前作为保留字段
////                        // String lastCheckTime = String.valueOf(macConStatus.get("time"));
////                        String conStats = String.valueOf(macConStatus.get("status"));
////                        macNowConStatus.add(StringUtils.isNotEmpty(conStats) ? conStats : "1");
////                        outObj.put("macStatsu", StringUtils.isNotEmpty(conStats) ? conStats : "1");
////                    } else {
////                        macNowConStatus.add("1");
////                        outObj.put("macStatsu", "1");
////                    }
//						macNowStats.add(outObj);
//					}
//					return mapper.writeValueAsString(macNowStats);
//				}
//
//			}else{//Arc
//				JSONObject outObj = new JSONObject();
//				String conStats = String.valueOf(redisService.getHash("drive_status", driverid));
//				if(StringUtils.isNotBlank(conStats)) {
//					macNowConStatus.add(StringUtils.isNotEmpty(conStats) ? conStats : "2");
//					outObj.put("macStatsu", StringUtils.isNotEmpty(conStats) ? conStats : "2");
//				} else {
//					macNowConStatus.add("2");
//					outObj.put("macStatsu", "2");
//				}
//				macNowStats.add(outObj);
//				return mapper.writeValueAsString(macNowStats);
//			}
//
//            return mapper.writeValueAsString("");
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return mapper.writeValueAsString("");
//        }
    }

	/**
	 * 监控页面中展示产品信息，产品合格率
	 * @param productInfoString
	 * @return
	 * @throws JsonProcessingException
	 */
	@RequestMapping("/getProductInfo")
	@ResponseBody
	public String getProductInfo(@RequestParam("list")String productInfoString) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		//使用jackson将json转为List<>
		JavaType jt = mapper.getTypeFactory().constructParametricType(ArrayList.class, Pojo.class);
		try {
			List<Pojo> list =  (List<Pojo>)mapper.readValue(productInfoString, jt);
			mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
			List<String> macNowConStatus = Lists.newArrayList();
			List<Object> macNowStats = Lists.newArrayList();
			for(Pojo pojo:list){
				JSONObject outObj = new JSONObject();
				if(pojo.getDataType().equals("Qualified")){
					outObj.put("productid", pojo.getProductid());
					MesProduct mesProduct = this.mesProductService.findById(Long.valueOf(pojo.getProductid()));
					List<Object[]> Object  =  mesDataProductProcedureService.findDataProductByPRODUCT_MODE(mesProduct.getModelnum());
					if(Object!=null && Object.size()>0){
						String time = Object.get(0)[0].toString();
						String qualified = Object.get(0)[1].toString();
						outObj.put("time", time);
						outObj.put("qualified", qualified);
					}
				}else{//ArcVirtual
                    outObj.put("productid", pojo.getProductid());

                }
				macNowStats.add(outObj);
			}
			return mapper.writeValueAsString(macNowStats);

		} catch (IOException e) {
			e.printStackTrace();
			return mapper.writeValueAsString("");
		}
	}



}
