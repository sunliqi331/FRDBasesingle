
package com.its.frd.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.its.common.entity.main.Dictionary;
import com.its.common.service.DictionaryService;
import com.its.common.service.RedisService;
import com.its.common.util.dwz.AjaxObject;
import com.its.common.util.dwz.Page;
import com.its.common.util.persistence.DynamicSpecifications;
import com.its.common.util.persistence.SearchFilter;
import com.its.common.util.persistence.SearchFilter.Operator;
import com.its.common.utils.SecurityUtils;
import com.its.frd.dao.MesPointTypeDao;
import com.its.frd.entity.*;
import com.its.frd.params.SendTemplate;
import com.its.frd.service.*;
import com.its.frd.util.ExcelUtils;
import com.its.monitor.service.MesPointsTemplateService;
import com.its.monitor.vo.MesPointsTemplate;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.persistence.criteria.*;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/mesPoints")
public class MesPointsController {
	private final String PAGEPRE = "mesPoints/";
	@Resource
	private MesPointsService pointServ;

	@Resource
	private MesPointTypeService mptServ;

	@Resource
	private MesPointGatewayService mpgServ;

	@Resource
	private MesDriverService driverServ;

	@Resource
	private DictionaryService dictionaryService;

	@Resource
	private MesDriverAlarmService mesDriverAlarmService;

	@Resource
	private CompanyinfoService companyinfoServ;

	// @Autowired
//	private HbaseTemplate hTemplate;

	private HConnection connection = null;

	@Resource
	private MesPointsTemplateService mesPointsTemplateService;

	@Resource
	private RedisService redisServ;

	@Resource
	private MesPointTypeDao mesPointTypeDao;

	@Resource
	private MesProductAlarmService mesproductalarm;

	@Resource
	private MesDriverPointService mesDriverPointService;

	@Autowired
	protected SimpMessagingTemplate simTemplate;

    @Resource
    private MesPointGatewayService mesPointGatewayService;

	@RequiresPermissions("point:view")
	@RequestMapping("/pointList")
	public String pointListPage(ServletRequest request, Page page, Map<String, Object> map)  {
		Long companyId = SecurityUtils.getShiroUser().getCompanyid();
		Specification<MesPointGateway> specification1 = DynamicSpecifications.bySearchFilter(request, MesPointGateway.class,
				new SearchFilter("companyinfo.id",Operator.EQ,SecurityUtils.getShiroUser().getCompanyid()));
		List<MesPointGateway> mesPointGateways = mpgServ.findAll(specification1);
		List<MesPointType> mesPointTypes = mptServ.findAllOrderByName();
		map.put("mesPointGateway", mesPointGateways);
		map.put("mesPointTypes", mesPointTypes);
		if(companyId == null)
			return "error/403";
		return PAGEPRE + "pointList";
	}

	/**
	 * 分页
	 * @param request
	 * @param page
	 * @return
	 * @throws JsonProcessingException
	 */
	@RequestMapping("/data")
	@ResponseBody
	public String pointListData(HttpServletRequest request, Page page) throws JsonProcessingException{
		Map<String, Object> map = new HashMap<String, Object>();
		Specification<MesPoints> specification =null;
		String mesPointGateway= request.getParameter("mesPointGateway");
		String mesPointType = request.getParameter("mesPointType");
		SearchFilter companyFilter = new SearchFilter("mesPointGateway.companyinfo.id",Operator.EQ,
				SecurityUtils.getShiroUser().getCompanyid());
		if(StringUtils.isNotBlank(mesPointGateway)) {
		    mesPointGateway = mesPointGateway.equals("全部") ? "" : mesPointGateway;
//		    request.setAttribute("mesPointGateway", "");
		    request.removeAttribute("mesPointGateway");
		} else {
		    request.removeAttribute("mesPointGateway");
		}
		boolean key1 = StringUtils.isNotEmpty(mesPointGateway);
		boolean key2 = StringUtils.isNotEmpty(mesPointType);
		SearchFilter getWayFilter = null;
		if(key1){
			getWayFilter = new SearchFilter("mesPointGateway.id",Operator.EQ,
					Long.parseLong(mesPointGateway));
		}
		SearchFilter typeFilter = new SearchFilter("mesPointType.id",Operator.EQ,mesPointType);
		if(key1&&key2){
			specification = DynamicSpecifications.bySearchFilter(request,
					MesPoints.class, companyFilter,getWayFilter,typeFilter);
		}else if((!key1)&&(key2)){
			specification = DynamicSpecifications.bySearchFilter(request,
					MesPoints.class, companyFilter,typeFilter);
		}else if((key1)&&(!key2)){
			specification = DynamicSpecifications.bySearchFilter(request,
					MesPoints.class, companyFilter,getWayFilter);
		}else{
			specification = DynamicSpecifications.bySearchFilter(request,MesPoints.class, companyFilter);
		}
		List<MesPoints> mesPoints = pointServ.findPage(specification, page);
		List<Dictionary> unitList = dictionaryService.findAll(new Specification<Dictionary>() {
			@Override
			public Predicate toPredicate(Root<Dictionary> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				// TODO Auto-generated method stub
				Expression<Long> expression = root.get("parent").get("id").as(Long.class);
				Predicate predicate = builder.equal(expression, 90);
				return query.where(predicate).getRestriction();
			}
		});
		Map<Long,String> unitsMap = new HashMap<>();
		for (Dictionary d : unitList){
			unitsMap.put(d.getId(), d.getName());
		}
		for (MesPoints mesPoint : mesPoints) {
			//根据当前测点所绑定的产线所绑定的工厂ID，找到工厂名称
			//但如果测点还没有对应的产线，或者设备还没有对应的工厂，则不显示
			//Dictionary dictionary = dictionaryService.findById(mesPoint.getUnitsId());
			String unitsName = unitsMap.get(mesPoint.getUnitsId());
			mesPoint.setUnits(unitsName);
			if (mesPoint.getCurrentMesProductline()!=null) {
				Companyinfo companyinfo = mesPoint.getCurrentMesProductline().getCompanyinfo();
				if (companyinfo!=null) {
					mesPoint.setFactoryName(companyinfo.getCompanyname());
				}
			}
		}
		//pointServ.batchSaveOrUpdate(mesPoints);
		/*Specification<MesPointGateway> specification1 = DynamicSpecifications.bySearchFilter(request, MesPointGateway.class,
				new SearchFilter("companyinfo.id",Operator.EQ,SecurityUtils.getShiroUser().getCompanyid()));
		List<MesPointGateway> mesPointGateways = mpgServ.findAll(specification1);*/

		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		map.put("page", page);
		map.put("points", mesPoints);
		//map.put("mesPointGateway", mesPointGateways);
		return mapper.writeValueAsString(map);
	}

    /**
     * 分页
     * @param request
     * @param page
     * @return
     * @throws JsonProcessingException
     */
    @RequestMapping("/gateWayDate")
    @ResponseBody
    public String gateWayDate(HttpServletRequest request, Page page) throws JsonProcessingException{
//        List<MesPointGateway> mesGateways = Lists.newArrayList();;
//        MesPointGateway allSelect = new MesPointGateway();
//        allSelect.setId(Long.valueOf("0"));
//        allSelect.setName("全部");
//        mesGateways.add(allSelect);
//        Long companyId = SecurityUtils.getShiroUser().getCompanyid();
//        mesGateways.addAll(mesPointGatewayService.findByCompanyid(companyId));

        Specification<MesPointGateway> specificationGateWay = DynamicSpecifications.bySearchFilter(request, MesPointGateway.class,
                new SearchFilter("companyinfo.id",Operator.EQ,SecurityUtils.getShiroUser().getCompanyid()));
        List<MesPointGateway> mesPointGateways = mesPointGatewayService.findPage(specificationGateWay, page);

        Map<String, Object> map = new HashMap<String, Object>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        map.put("page", page);
        map.put("mesgateways", mesPointGateways);
        return mapper.writeValueAsString(map);
    }

	/**
	 * 删
	 * @param
	 * @return
	 */
	@SendTemplate
	@RequiresPermissions("point:delete")
	@RequestMapping("/deleteById")
	@ResponseBody
	public String delById(Long[] ids){
		try {
			Set<String> macs = new HashSet<>();
			String pointCodes = "";
			for (int i = 0; i < ids.length; i++) {
				MesPoints mesPoints = pointServ.findById(ids[i]);
				pointServ.deleteById(ids[i]);
				String template = mesPointsTemplateService.getTemplate(mesPoints);
				MesPointGateway gateway = mesPoints.getMesPointGateway();
				macs.add(gateway.getMac());
				String key = MesPointsTemplate.class.getSimpleName()+"_"+gateway.getMac();
				Map<String, Object> hash = redisServ.getHash(key);
				if(hash == null){
					hash = new HashMap<String,Object>();
				}
				hash.put(mesPoints.getCodekey(), template);
				redisServ.setHash(key, hash);

				pointCodes += (mesPoints.getCodekey() + ",");
			}
			pointCodes = pointCodes.substring(0, pointCodes.lastIndexOf(","));
			JSONObject obj = null;
			for(String mac : macs){
				obj = new JSONObject();
				obj.put("type", "2");
				obj.put("mac", mac);
				obj.put("pointId", pointCodes);
				simTemplate.convertAndSend( "/showMonitor/advise/template", obj);
				// mesPointsTemplateService.sendTemplate(mac);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxObject.newError("删除测点失败！").toString();
		}
		return AjaxObject.newOk("删除测点成功！").toString();
	}

	@RequestMapping("/checkPointIsdeletable")
	@ResponseBody
	public String checkPointIsdeletable(Long[] ids){
		/*s*/
		for (int i = 0; i < ids.length; i++) {
			List<MesDriverAlarm> mesDriverAlarms = mesDriverAlarmService.findByMesPointsId(ids[i]);
			 List<MesProductAlarm> mesproduct  = mesproductalarm.findByMesPointsId(ids[i]);
			 MesDriverPoints mesDriverpoints = mesDriverPointService.findByMesPointsId(ids[i]);
			 if(mesDriverpoints!=null){
				 return AjaxObject.newOk("error").toString();
			 }
			 else if(mesDriverAlarms.size()>0){
		        return AjaxObject.newOk("error").toString();
		    }
		    else if(mesproduct.size()>0){
		    	return AjaxObject.newOk("error").toString();
		    }
		    }
		return AjaxObject.newOk("success").toString();
	}
	
		   
		  /*  List<Filter> filterList = new ArrayList<>();
		    MesPoints mesPoints = pointServ.findById(ids[i]);
		    Filter filter = new ColumnPrefixFilter(Bytes.toBytes(mesPoints.getCodekey()));
		    filterList.add(filter);
		    Filter filter2 = new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator("^\\w+"+mesPoints.getMesPointGateway().getMac() + ":((?!:).)+:\\w+:((?!:).)+$"));
		    filterList.add(filter2);
		    FilterList filterL = new FilterList(FilterList.Operator.MUST_PASS_ALL, filterList);
		    filters.add(filterL);
		}
		FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ONE, filters);
		HbaseUtil.connectHbase();
		 Result resultRow = HbaseUtil.selectFirstResultRow(filterList);
		if(null == resultRow){
			return AjaxObject.newOk("success").toString();
		}
		return AjaxObject.newOk("error").toString();
	}*/

	/**
	 * 增或改
	 * @param point
	 * @return
	 */
	@SendTemplate
	@RequiresPermissions(value={"point:save","point:edit"},logical=Logical.OR)
	@RequestMapping("/addOrUpdate")
	@ResponseBody
	public String addMesPoints(MesPoints point,HttpServletRequest request){
		Page page = new Page();
		String msg = "修改";
		if(point.getId() == null){
			msg = "添加";
			point.setStatus(MesPoints.UNBINDING);
		}
		try {
			Specification<MesPoints> specification = DynamicSpecifications.bySearchFilter(request, MesPoints.class
					,new SearchFilter("mesPointGateway.id", Operator.EQ,point.getMesPointGateway().getId())
					,new SearchFilter("codekey", Operator.EQ, point.getCodekey())
//					,new SearchFilter("name", Operator.EQ, point.getName())
					);
			List<MesPoints> mesPoints = pointServ.findPage(specification, page);
//			MesPointGateway mesPointGateway = mpgServ.findById(point.getMesPointGateway().getId());
//            String pageMac = mesPointGateway.getMac();
			if(mesPoints != null && 0< mesPoints.size()) {
			    // 如果当前为添加模式
			    if(null == point.getId()) {
	                return AjaxObject.newError("该网关下已有同样的测点了，请重新输入网关/测点ID。").setCallbackType("").toString();
			    }
			}
//			for(MesPoints mp : mesPoints){
//				if(mp.getName().equals(point.getName())){
////				    String dbMac = mp.getMesPointGateway().getMac();
//                    if (point.getId() == null || point.getId().compareTo(mp.getId()) != 0) {
////                        if (pageMac.equals(dbMac)) {
//                            return AjaxObject.newError("该测点名称已存在！").setCallbackType("").toString();
////                        }
//                    }
//				}
//			}
			MesPoints points = pointServ.saveOrUpdate(point);
			String template = mesPointsTemplateService.getTemplate(points);
			MesPointGateway gateway = points.getMesPointGateway();
			if(null != gateway){
				MesPointGateway mac =mpgServ.findById(gateway.getId());
				System.out.println("mac1:----------"+gateway.getId());
				System.out.println("mac2:----------"+gateway.getMac());
				String key = MesPointsTemplate.class.getSimpleName()+"_"+mac.getMac();
				Map<String, Object> hash = redisServ.getHash(key);
				if(hash == null){
					hash = new HashMap<String,Object>();
				}
				hash.put(point.getCodekey(), template);
				redisServ.setHash(key, hash);
				if("修改".equals(msg)) {
					JSONObject obj = new JSONObject();
					obj.put("type", "2");
					obj.put("mac", mac.getMac());
					obj.put("pointId", point.getCodekey());
					simTemplate.convertAndSend("/showMonitor/advise/template", obj);
					// mesPointsTemplateService.sendTemplate(mac.getMac());
				}

			}else{
				pointServ.deleteById(points.getId());
				return AjaxObject.newError(msg + "测点失败！").toString();
			}

		} catch (Exception e) {
			e.printStackTrace();
			return AjaxObject.newError(msg + "测点失败！").toString();
		}
		return AjaxObject.newOk(msg + "测点成功！").toString();
	}
	@RequiresPermissions("point:save")
	@RequestMapping(value="/create", method=RequestMethod.GET)
	public String preCreate(Map<String, Object> map,Page page,HttpServletRequest request) {
		page.setPageNum(1);
		page.setNumPerPage(Integer.MAX_VALUE);
		Specification<MesPointGateway> specification = DynamicSpecifications.bySearchFilter(request, MesPointGateway.class,
				new SearchFilter("companyinfo.id",Operator.EQ,SecurityUtils.getShiroUser().getCompanyid()));
		List<MesPointGateway> mesPointGateways = mpgServ.findAll(specification);

		List<MesPointType> mesPointTypes = mptServ.findAllOrderByName();
		//数据类型
        Specification<Dictionary> specification1 = DynamicSpecifications.bySearchFilter(request, Dictionary.class,
                new SearchFilter("parent.id", Operator.EQ,89));
        List<Dictionary> datatype = dictionaryService.findByExample(specification1, page);
        //单位
        Specification<Dictionary> specification2 = DynamicSpecifications.bySearchFilter(request, Dictionary.class,
                new SearchFilter("parent.id", Operator.EQ,90));
        List<Dictionary> units = dictionaryService.findByExample(specification2, page);

        map.put("datatype", datatype);
        map.put("units", units);
		map.put("mesPointType", mesPointTypes);
		map.put("mesPointGateway", mesPointGateways);
		return PAGEPRE+"create";
	}

	@RequiresPermissions("point:edit")
	@RequestMapping("/findById/{id}")
	public String findById(@PathVariable Long id,Model model,Map<String, Object> map,Page page,HttpServletRequest request){
		page.setPageNum(1);
		page.setNumPerPage(10000000);
		//	public String preCreate(Map<String, Object> map,Page page,HttpServletRequest request) {
		 MesPoints point = pointServ.findById(id);
		Specification<MesPointGateway> specification = DynamicSpecifications.bySearchFilter(request, MesPointGateway.class
				,new SearchFilter("companyinfo.id",Operator.EQ,SecurityUtils.getShiroUser().getCompanyid())
				,new SearchFilter("id",Operator.NOTEQ,point.getMesPointGateway().getId())
				);
		List<MesPointGateway> mesPointGateways = mpgServ.findPage(specification, page);
		//测点类型
		List<MesPointType> mesPointTypes = mptServ.findAllOrderByName();
		for(int i=0;i<mesPointTypes.size();i++){
			if(mesPointTypes.get(i).getId()==point.getMesPointType().getId()){
				mesPointTypes.remove(i);
			}
		}

		//数据类型
        Specification<Dictionary> specification1 = DynamicSpecifications.bySearchFilter(request, Dictionary.class,
                new SearchFilter("parent.id", Operator.EQ,89));
        List<Dictionary> datatype = dictionaryService.findByExample(specification1, page);
        //单位
        Specification<Dictionary> specification2 = DynamicSpecifications.bySearchFilter(request, Dictionary.class,
                new SearchFilter("parent.id", Operator.EQ,90));
        List<Dictionary> units = dictionaryService.findByExample(specification2, page);

        map.put("datatype", datatype);
        map.put("units", units);
		map.put("mesPointType", mesPointTypes);
		map.put("mesPointGateway", mesPointGateways);
		model.addAttribute("point", point);
		//		model.addAttribute("pointGateway",pointGatewayServ.findById(id));
		return PAGEPRE + "update";
	}

	@RequestMapping("/checkname/{name}")
	@ResponseBody
	public String checkUsername(@PathVariable String name,HttpServletRequest request) throws JsonProcessingException {
		Page page = new Page();
		Map<String, Object> map = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		try {
			Long companyId = SecurityUtils.getShiroUser().getCompanyid();
			List<MesPoints> mesPoint = new ArrayList<MesPoints>();
			if(companyId != null){
				Specification<MesPoints> specification = DynamicSpecifications.bySearchFilter(request, MesPoints.class
						,new SearchFilter("mesPointGateway.companyinfo", Operator.EQ, companyId)
						,new SearchFilter("name", Operator.EQ, name)
						);
				mesPoint = pointServ.findPage(specification, page);
			}
			if(mesPoint.size() > 0){
				map.put("0", 0);
			}else {
				map.put("1", 1);
			}
		} catch (Exception e) {

			return null;
		}
		return mapper.writeValueAsString(map);
	}

	@RequestMapping("/checkNameGateway/{name}")
	@ResponseBody
	public String checkNameGateway(@PathVariable String name,HttpServletRequest request) throws JsonProcessingException {
		Page page = new Page();
		Map<String, Object> map = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		String[] temp =name.split(",");
		try {
			Long companyId = SecurityUtils.getShiroUser().getCompanyid();
			List<MesPoints> mesPoint = new ArrayList<MesPoints>();
			if(companyId != null){
				Specification<MesPoints> specification = DynamicSpecifications.bySearchFilter(request, MesPoints.class
						,new SearchFilter("mesPointGateway.companyinfo", Operator.EQ, companyId)
						,new SearchFilter("name", Operator.EQ, temp[1]),new SearchFilter("mesPointGateway.id", Operator.EQ, temp[0])
						);
				mesPoint = pointServ.findPage(specification, page);
			}
			if(mesPoint.size() > 0){
				map.put("0", 0);
			}else {
				map.put("1", 1);
			}
		} catch (Exception e) {

			return null;
		}
		return mapper.writeValueAsString(map);
	}



	@RequestMapping("/checkCodekey/{codekey}/{macId}")
	@ResponseBody
	public String checkPhone(@PathVariable String codekey,@PathVariable String macId,HttpServletRequest request) throws JsonProcessingException {
		Map<String, Object> map = new HashMap<String, Object>();
		Page page = new Page();
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		try {
			Long companyId = SecurityUtils.getShiroUser().getCompanyid();
			List<MesPoints> mesPoint = new ArrayList<MesPoints>();
			if(companyId != null){
				Specification<MesPoints> specification = DynamicSpecifications.bySearchFilter(request, MesPoints.class
						,new SearchFilter("mesPointGateway.companyinfo", Operator.EQ, companyId)
						,new SearchFilter("codekey", Operator.EQ, codekey)
						);
				mesPoint = pointServ.findPage(specification, page);
			}
			if(mesPoint.size() > 0){
			    MesPoints mespoint = mesPoint.get(0);
			    String dbMacId = String.valueOf(mespoint.getMesPointGateway().getId());
			    if(dbMacId.equals(macId)) {
	                map.put("0", 0);
			    } else {
			        map.put("1", 1);
			    }

			}else {
				map.put("1", 1);
			}
		} catch (Exception e) {

			return null;
		}
		return mapper.writeValueAsString(map);
	}
	/*不同的网管中测点的ID可以相同*/
	@RequestMapping("/checkCodekeyNew")
	@ResponseBody
	public String checkCodekeyNew(HttpServletRequest request) throws JsonProcessingException {
		String mesPointGateway = request.getParameter("mesPointGateway");
		String codekey = request.getParameter("codekey");
		Map<String, Object> map = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		try {
			Long companyId = SecurityUtils.getShiroUser().getCompanyid();
			List<MesPoints> mesPoint = new ArrayList<MesPoints>();
			if(companyId != null){
				mesPoint = pointServ.checkCodekeyNew(new Long(mesPointGateway), codekey);
			}
			if(mesPoint.size() > 0){
				map.put("0", 0);
			}else {
				map.put("1", 1);
			}
		} catch (Exception e) {

			return null;
		}
		return mapper.writeValueAsString(map);
	}

	@RequestMapping("/readexcel")
	@ResponseBody
	public String readExcel(MultipartFile alarmRelationFile) throws JsonProcessingException {
		Map<String, Object> map_result = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper();
		Workbook wb = null;
		Sheet sheet = null;
		Row row = null;
		List<Map<String, String>> list = null;
		String cellData = null;
		String columns[] = {"测点ID", "描述", "网关ID", "测点类型", "数据类型", "单位"};
		wb = ExcelUtils.readExcel(alarmRelationFile);
		try {
		if (wb != null) {
			//用来存放表中数据
			list = new ArrayList<Map<String, String>>();
			//获取第一个sheet
			sheet = wb.getSheetAt(0);
			//获取最大行数
			int rownum = sheet.getPhysicalNumberOfRows();
			//获取第一行
			row = sheet.getRow(0);
			//获取最大列数
			int colnum = row.getPhysicalNumberOfCells();
			for (int i = 1; i < rownum; i++) {
				Map<String, String> map = new LinkedHashMap<String, String>();
				row = sheet.getRow(i);
				if (row != null) {
					for (int j = 0; j < colnum; j++) {
						cellData = (String) ExcelUtils.getCellFormatValue(row.getCell(j));
						map.put(columns[j], cellData);
					}
				} else {
					break;
				}
				list.add(map);
			}
		}
		//遍历解析出来的list
		List<MesPoints> mesPointsList = new ArrayList<MesPoints>();
		for (Map<String, String> map : list) {
			MesPoints point = new MesPoints();
			point.setStatus(MesPoints.UNBINDING);
			for (Map.Entry<String, String> entry : map.entrySet()) {
//                System.out.print(entry.getKey()+":"+entry.getValue()+",");
				if (entry.getKey().equals("测点ID")) {
					point.setCodekey(entry.getValue().replace(".0", ""));
				} else if (entry.getKey().equals("描述")) {
					point.setName(entry.getValue());
				} else if (entry.getKey().equals("网关ID")) {
					MesPointGateway mesPointGateway = new MesPointGateway();
					mesPointGateway.setId(Long.valueOf(entry.getValue().replaceAll(".0", "")));
					point.setMesPointGateway(mesPointGateway);
				} else if (entry.getKey().equals("测点类型")) {
					MesPointType mesPointType = new MesPointType();
					mesPointType.setId(Long.valueOf(entry.getValue().replace(".0", "")));
					point.setMesPointType(mesPointType);
				} else if (entry.getKey().equals("数据类型")) {
					point.setDatatype(entry.getValue());
				} else if (entry.getKey().equals("单位")) {
					point.setUnitsId(Long.valueOf(entry.getValue().replace(".0", "")));
				}
			}
			MesPointGateway mac = mpgServ.findById(point.getMesPointGateway().getId());
			String key = MesPointsTemplate.class.getSimpleName() + "_" + mac.getMac();
			MesPoints points = pointServ.saveOrUpdate(point);
			String template = mesPointsTemplateService.getTemplate(points);
			Map<String, Object> hash = redisServ.getHash(key);
			if (hash == null) {
				hash = new HashMap<String, Object>();
			}
			hash.put(point.getCodekey(), template);
			redisServ.setHash(key, hash);
		}
			map_result.put("result", "success");
			return mapper.writeValueAsString(map_result);
		} catch (Exception e) {
			map_result.put("result", "fail");
			return mapper.writeValueAsString(map_result);
		}
	}
}
