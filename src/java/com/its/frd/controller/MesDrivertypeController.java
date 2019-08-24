package com.its.frd.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

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
import com.its.frd.entity.Companyfile;
import com.its.frd.entity.Companyinfo;
import com.its.frd.entity.MesDriver;
import com.its.frd.entity.MesDriverAlarm;
import com.its.frd.entity.MesDriverPoints;
import com.its.frd.entity.MesDrivertype;
import com.its.frd.entity.MesDrivertypeProperty;
import com.its.frd.entity.MesPointGateway;
import com.its.frd.entity.MesPoints;
import com.its.frd.params.CompanyfileType;
import com.its.frd.params.SendTemplate;
import com.its.frd.service.CompanyfileService;
import com.its.frd.service.MesDriverAlarmService;
import com.its.frd.service.MesDriverPointService;
import com.its.frd.service.MesDriverService;
import com.its.frd.service.MesDriverTypePropertyService;
import com.its.frd.service.MesDriverTypeService;
import com.its.frd.util.DateUtils;
import com.its.monitor.service.MesPointsTemplateService;
import com.its.monitor.vo.MesPointsTemplate;

@Controller
@RequestMapping("/drivertype")
public class MesDrivertypeController {
	private final String PRE_PAGE = "driver/";

	@Resource
	private MesDriverTypeService mdtServ;
	@Resource
	private MesDriverTypePropertyService mdtpServ;
	@Resource
	private CompanyfileService cfServ;
	@Resource
	private MesDriverPointService mdpServ;
	@Resource
	private MesDriverAlarmService mesDriverAlarmService;
	@Resource
	private MesDriverService mesDriverService;
	@Resource
	private DictionaryService dictionaryService;
	@Resource
	private MesPointsTemplateService mesPointsTemplateService;
	@Resource
	private MesDriverPointService mesDriverPointService;
	@Resource
	private RedisService redisServ;
	@RequiresPermissions("driverType:view")
	@RequestMapping("/driverTypeList")
	public String driverTypeList () {
		Long companyId = SecurityUtils.getShiroUser().getCompanyid();
		if(companyId == null)
			return "error/403";
		return PRE_PAGE + "driverTypeList";
	}
	
	@RequiresPermissions("driverType:viewProperty")
	@RequestMapping("/driverAttributeList/{id}")
	public String driverAttributeList (@PathVariable Long id,Map<String,Object> map) {
		MesDrivertype mesDrivertype = mdtServ.findById(id);
		map.put("mesDrivertype", mesDrivertype);
		map.put("mesDrivertypeId", id);
		return PRE_PAGE + "driverAttributeList";
	}
	
	@RequiresPermissions("driverType:save")
	@RequestMapping("/addDriverType")
	public String addDriverType () {
		return PRE_PAGE + "addDriverType";
	}
	
	@RequiresPermissions("driverType:save")
	@RequestMapping("/addMuitiDriverType")
	public String addMuitiDriverType () {
		return PRE_PAGE + "addMultiDriverType";
	}

	@RequiresPermissions("driverType:saveProperty")
	@RequestMapping("/addDriverAttribute/{id}")
	public String addDriverAttribute (@PathVariable Long id,Map<String,Object> map,HttpServletRequest request) {
		map.put("mesDrivertypeId", id);
		Specification<Dictionary> specification1 = DynamicSpecifications.bySearchFilter(request, Dictionary.class,
                new SearchFilter("parent.id", Operator.EQ,89));
        List<Dictionary> datatype = dictionaryService.findAll(specification1);
        //单位
        Specification<Dictionary> specification2 = DynamicSpecifications.bySearchFilter(request, Dictionary.class,
                new SearchFilter("parent.id", Operator.EQ,90));
        List<Dictionary> units = dictionaryService.findAll(specification2);
        map.put("datatype", datatype);
        map.put("units", units);
		return PRE_PAGE + "addDriverAttribute";
	}

	/**
	 * 设备类型分页
	 * @param request
	 * @param page
	 * @return
	 */
	@RequestMapping("/driverTypeData")
	@ResponseBody
	public Map<String, Object> data(HttpServletRequest request, Page page) {
		Map<String, Object> map = new HashMap<String, Object>();
		Specification<MesDrivertype> specification = DynamicSpecifications.bySearchFilter(request, MesDrivertype.class
				, new SearchFilter("companyinfo.id",Operator.EQ,SecurityUtils.getShiroUser().getCompanyid())
				);
		List<MesDrivertype> mds = mdtServ.findPage(specification, page);
		for(MesDrivertype type : mds){
			List<Companyfile> cf= cfServ.findByParentidAndType(type.getId(),CompanyfileType.DRIVERTYPEFILE.toString());
			for(Companyfile cfs : cf) {
				String picSource = request.getServletContext().getContextPath()+"/company/showPic/" +cfs.getId();
				String picDetail = request.getServletContext().getContextPath()+"/drivertype/showPicDetail/"+cfs.getId();
				type.setShowPic("<img data-url='"+ picDetail +"' style='height:30px; width:40px' src="+ picSource +">");
			}
		}
		map.put("page", page);
		map.put("mesDrivertypes", mds);
		return map;
	}
	
	@RequestMapping("/showPicDetail/{id}")
	public String showPicDetail (@PathVariable Long id,Map<String,Object> map) {
		map.put("id", id);
		return PRE_PAGE + "showPicDetail";
	}
	/**
	 * 设备类型属性分页
	 * @param request
	 * @param page
	 * @return
	 * @throws JsonProcessingException 
	 */
	@RequestMapping("/driverTypePropertyData/{id}")
	@ResponseBody
	public String dataPro(@PathVariable Long id, HttpServletRequest request, Page page) throws JsonProcessingException {
		Map<String, Object> map = new HashMap<String, Object>();
		Specification<MesDrivertypeProperty> specification = DynamicSpecifications.bySearchFilter(request, MesDrivertypeProperty.class,new SearchFilter("mesDrivertype.id",Operator.EQ,id));
		List<MesDrivertypeProperty> mesDrivertypePropertys = mdtpServ.findPage(specification, page);
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		map.put("page", page);
		map.put("mesDrivertypePropertys", mesDrivertypePropertys);
		return mapper.writeValueAsString(map);
	}
	/**
	 * 添加设备类型数据
	 * MesDrivertypeProperty为属性数据模型,添加数据为MesDrivertypeProperty[index].属性
	 * @param mesDrivertype
	 * @return
	 */
	@RequiresPermissions(value={"driverType:save","driverType:edit"},logical=Logical.OR)
	@RequestMapping("/saveDriverType")
	@ResponseBody
	@SendTemplate
	public String saveDriverType(@RequestParam("files") MultipartFile[] files,MesDrivertype mesDrivertype,HttpServletRequest request,Page page){
		String msg = "修改";
		if(mesDrivertype.getId() == null)
			msg = "添加";
		/*if(!this.checkPictureType(files))
			return AjaxObject.newError("不支持该文件格式!").setCallbackType("").toString();*/
		List<Companyfile> companyfilelist = new ArrayList<Companyfile>();
		if (files != null && files.length > 0) {
			companyfilelist = new ArrayList<Companyfile>();
			for (int i = 0; i < files.length; i++) {
				MultipartFile file = files[i];
				if (!file.isEmpty()) {
					try {
						fileSave(companyfilelist, file);
					} catch (Exception e) {
						e.printStackTrace();
						return AjaxObject.newError( msg +"失败:请检查上传文件是否正确!").setCallbackType("").toString();
					}
				}
			}
		}
		try {
			if(mesDrivertype.getId() == null ) {
				mesDrivertype.setCompanyinfo(new Companyinfo(SecurityUtils.getShiroUser().getCompanyid()));
				mdtServ.saveAndFile(mesDrivertype, companyfilelist);
				//				mdtServ.saveOrUpdate(mesDrivertype);
				/*for(MesDrivertypeProperty mesDrivertypeProperty : mesDrivertype.getMesDrivertypeProperties()){
			    if(mesDrivertypeProperty.getPropertyname()!=null){
			        mesDrivertypeProperty.setMesDrivertype(mesDrivertype);
			        mdtpServ.saveOrUpdate(mesDrivertypeProperty);
			    }
			}*/
			}else {
				if(companyfilelist != null && companyfilelist.size() > 0)
					cfServ.deleteCompanyfileByParentidAndType(mesDrivertype.getId(),CompanyfileType.DRIVERTYPEFILE.toString());
				mesDrivertype.setCompanyinfo(new Companyinfo(SecurityUtils.getShiroUser().getCompanyid()));
				mdtServ.saveAndFile(mesDrivertype, companyfilelist);
				Set<String> macs = new HashSet<>();
				for(MesDriver mesDriver : mesDrivertype.getMesDrivers()){
					List<MesDriverPoints> mesDriverPointses = mesDriver.getMesDriverPointses();
					for (MesDriverPoints mesDriverPoints : mesDriverPointses) {
						MesDriverPoints driverPoints = mesDriverPointService.findById(mesDriverPoints.getId());
						MesPoints mesPoints = driverPoints.getMesPoints();
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
						
					}
					
				}
				for(String mac : macs){
					mesPointsTemplateService.sendTemplate(mac);
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxObject.newError(msg + "设备类型失败!").setCallbackType("").toString();
		}
		return AjaxObject.newOk(msg + "设备类型成功!").toString();
	}
	
	/**
	 * 批量添加设备类型数据
	 * @return
	 */
	@RequiresPermissions(value={"driverType:save","driverType:edit"},logical=Logical.OR)
	@RequestMapping("/saveMultiDriverType")
	@ResponseBody
	public String saveMultiDriverType(@RequestParam("files") MultipartFile[] files,HttpServletRequest request,Page page){
		String msg = "批量添加";
		/*if(!this.checkPictureType(files))
			return AjaxObject.newError("不支持该文件格式!").setCallbackType("").toString();*/
		if (files != null && files.length > 0) {
			for (int i = 0; i < files.length; i++) {
				MultipartFile file = files[i];

				if (!file.isEmpty()) {
					try {
						batchFileSave(file);
					} catch (Exception e) {
						e.printStackTrace();
						return AjaxObject.newError( msg +"失败:请检查上传文件是否正确!").setCallbackType("").toString();
					}
				}
			}
		}
		return AjaxObject.newOk(msg + "设备类型成功!").toString();
	}


	/**
	 * 用来检查用户是否能创建公司
	 * 当user表中的companyid不为null时,则不能创建公司
	 * @return  
	 */
	private void fileSave(List<Companyfile> companyfilelist, MultipartFile file) throws IOException {
		Companyfile cpfile;
		String fileBaseName = file.getOriginalFilename();
		String newFileName = UUID.randomUUID().toString()
				+ fileBaseName.substring(fileBaseName.lastIndexOf("."));
		String filePath = com.its.frd.util.ResourceUtil.getUploadDirectory();
		String classPath = this.getClass().getClassLoader().getResource("/").getPath();
		int position = classPath.toLowerCase().indexOf("web-inf");
		position = -1;
		if(position != -1){
			filePath = classPath.substring(0, position)+"FRD_upload_FILE/";
		}
		filePath += DateUtils.getyyyyMMddHH2(new Date()); // 精确到小时
		File uploadFile = new java.io.File(filePath + File.separator + newFileName);
		if (!uploadFile.exists()) {
			uploadFile.mkdirs();
		}
		file.transferTo(uploadFile);
		
		cpfile = new Companyfile();
		cpfile.setfilebasename(fileBaseName);
		cpfile.setfilelength(file.getSize());
		cpfile.setfilenewname(newFileName);
		cpfile.setfilepath(filePath);
		companyfilelist.add(cpfile);
	}

	private void batchFileSave(MultipartFile file) throws IOException {
		Companyfile cpfile;
		String fileBaseName = file.getOriginalFilename();
		String newFileName = UUID.randomUUID().toString()
				+ fileBaseName.substring(fileBaseName.lastIndexOf("."));
		String filePath = com.its.frd.util.ResourceUtil.getUploadDirectory();
		String classPath = this.getClass().getClassLoader().getResource("/").getPath();
		int position = classPath.toLowerCase().indexOf("web-inf");
		position = -1;
		if(position != -1){
			filePath = classPath.substring(0, position)+"FRD_upload_FILE/";
		}
		filePath += DateUtils.getyyyyMMddHH2(new Date()); // 精确到小时
		File uploadFile = new java.io.File(filePath + File.separator + newFileName);
		if (!uploadFile.exists()) {
			uploadFile.mkdirs();
		}
		file.transferTo(uploadFile);
		
		MesDrivertype mesDrivertype = new MesDrivertype();
		mesDrivertype.setCompanyinfo(new Companyinfo(SecurityUtils.getShiroUser().getCompanyid()));
		mesDrivertype.setTypename(fileBaseName.substring(0, fileBaseName.lastIndexOf(".")));//去除文件扩展名部分
		MesDrivertype drivertype = mdtServ.saveOrUpdate(mesDrivertype);
		
		cpfile = new Companyfile();
		cpfile.setfilebasename(fileBaseName);
		cpfile.setfilelength(file.getSize());
		cpfile.setfilenewname(newFileName);
		cpfile.setfilepath(filePath);
		cpfile.setParentid(drivertype.getId());
		cpfile.setParenttype(CompanyfileType.DRIVERTYPEFILE.toString());
		cfServ.saveOrUpdate(cpfile);
	}

	private boolean checkPictureType(MultipartFile[] files){
		if (files != null && files.length > 0) {
			for (int i = 0; i < files.length; i++) {
				MultipartFile file = files[i];
				if (!file.isEmpty()) {
					try {
						String fileBaseName = file.getOriginalFilename();
						if(Companyfile.picTypeMap.get(fileBaseName.substring(fileBaseName.lastIndexOf(".")).toLowerCase())==null)
								return false;
					} catch (Exception e) {
						e.printStackTrace();
						return false;
					}
				}
			}
		}
		return true;
	}
	/**
	 * 给driverTypeId对应的设备类型添加属性列表
	 * @param driverTypeId 设备类型主键id 
	 * @param props 添加的属性列表
	 * @return
	 */
	@RequiresPermissions(value={"driverType:saveProperty","driverType:editProperty"},logical=Logical.OR)
	@RequestMapping("/saveDriverTypeProperty/{driverTypeId}")
	@ResponseBody
	public String saveDriverTypeProperty(@PathVariable Long driverTypeId,MesDrivertypeProperty mesDrivertypeProperty,HttpServletRequest request,Page page){
		String msg = "修改";
		if(mesDrivertypeProperty.getId() == null)
			msg = "添加";
		MesDrivertype mesDrivertype = mdtServ.findById(driverTypeId);
		if(mesDrivertype == null)
			return AjaxObject.newError("无设备类型!").setCallbackType("").toString();
		try {
			mesDrivertypeProperty.setMesDrivertype(mesDrivertype);
			mdtpServ.saveOrUpdate(mesDrivertypeProperty);
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxObject.newError(msg + "设备类型属性失败!").setCallbackType("").toString();
		}
		return AjaxObject.newOk(msg + "设备类型属性成功!").toString();
	}

	/**
	 * 根据id查找
	 * @param id
	 * @param model
	 * @return
	 */
	@RequiresPermissions("driverType:edit")
	@RequestMapping("/findById/{id}")
	public String findByID2Page(@PathVariable Long id,Model model,Map<String,Object> map){
		List<Companyfile> fileList = cfServ.findByParentidAndType(id, CompanyfileType.DRIVERTYPEFILE.toString());
		boolean bool = false;
		if(fileList.size() > 0){
			bool = true;
		}
		model.addAttribute("mdt",mdtServ.findById(id));
		model.addAttribute("mdtp",mdtpServ.findByMesDrivertypeId(id));
		map.put("typeFile", fileList);
		map.put("bool", bool);
		return PRE_PAGE + "editDriverType";
	}

	/**
	 * 根据id查找
	 * @param id
	 * @param model
	 * @return
	 */
	@RequiresPermissions("driverType:editProperty")
	@RequestMapping("/findTypePropertyById/{id}")
	public String findTypePropertyById(@PathVariable Long id,Model model,Map<String, Object> map,HttpServletRequest request){
		MesDrivertypeProperty mdtp = mdtpServ.findById(id);
		Specification<Dictionary> specification1 = DynamicSpecifications.bySearchFilter(request, Dictionary.class,
                new SearchFilter("parent.id", Operator.EQ,89));
        List<Dictionary> datatype = dictionaryService.findAll(specification1);
        //单位
        Specification<Dictionary> specification2 = DynamicSpecifications.bySearchFilter(request, Dictionary.class,
                new SearchFilter("parent.id", Operator.EQ,90));
        List<Dictionary> units = dictionaryService.findAll(specification2);
        map.put("datatype", datatype);
        map.put("units", units);
		Long mdtId = mdtp.getMesDrivertype().getId();
		model.addAttribute("mdtp",mdtp);
		map.put("mesDrivertypeId", mdtId);
		return PRE_PAGE + "editDriverAttribute";
	}

	/**
	 * 根据id删除
	 * @param id
	 * @return
	 */
	@RequiresPermissions("driverType:delete")
	@RequestMapping(value = "/deleteDriverType", method = RequestMethod.POST)
	public @ResponseBody String deleteManyDriverType(Long[] ids) {
		try {
			for (int i = 0; i < ids.length; i++) {
				MesDrivertype mesDrivertype = mdtServ.findById(ids[i]);
				List<MesDriver> mesDrivers = mesDriverService.findByMesDriverTypeId(mesDrivertype.getId());
				if(mesDrivers.size() > 0){
				    for(MesDriver mesDriver : mesDrivers){
				        List<MesDriverAlarm> mesDriverAlarms = mesDriverAlarmService.findByMesDriverId(mesDriver.getId());
				        if(mesDriverAlarms.size() > 0){
				            return AjaxObject.newError("此设备类型存在告警信息,不可删除!").setCallbackType("").toString();
				        }
				    }
				}
				mdtServ.deleteById(mesDrivertype.getId());
			}
		} catch (Exception e) {
			return AjaxObject.newError("删除设备类型失败!").setCallbackType("").toString();
		}
		return AjaxObject.newOk("删除设备类型成功！").setCallbackType("").toString();
	}

	
	@RequiresPermissions("driverType:deleteProperty")
	@RequestMapping(value = "/deleteDriverTypeProperty", method = RequestMethod.POST)
	public @ResponseBody String deleteManyDriverTypeProperty(Long[] ids,HttpServletRequest request) {
		Page page = new Page();
		try {
			for (int i = 0; i < ids.length; i++) {
				Specification<MesDriverPoints> specification = DynamicSpecifications.bySearchFilter(request, MesDriverPoints.class
						,new SearchFilter("mesDrivertypeProperty.id", Operator.EQ,ids[i])
						);
				List<MesDriverPoints> mdp = mdpServ.findPage(specification, page);
				if(mdp.size() > 0){
					String name = mdtpServ.findById(ids[i]).getPropertyname();
					return AjaxObject.newError(name + "属性绑定设备测点，无法删除！").setCallbackType("").toString();
				}
				mdtpServ.deleteById(ids[i]);
			}
		} catch (Exception e) {
			return AjaxObject.newError("删除设备类型属性失败!").toString();
		}
		return AjaxObject.newOk("删除设备类型属性成功！").setCallbackType("").toString();
	}

    @RequestMapping("/checkTypename/{typename}")
    @ResponseBody
    public String checkTypename(@PathVariable String typename) throws JsonProcessingException{
        Map<String, Object> map = new HashMap<String, Object>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        try {
                if(mdtServ.findByCompanyinfoidAndTypename(SecurityUtils.getShiroUser().getCompanyid(), typename).size()<1){
                map.put("1", 1);
            }else {
                map.put("0", 0);
            }
        } catch (Exception e) {
            return null;
        }
        return mapper.writeValueAsString(map);
    }
    
    @RequestMapping("/checkProname/{name}/{typeId}")
    @ResponseBody
    public String checkProname(@PathVariable String name,@PathVariable Long typeId,HttpServletRequest request) throws JsonProcessingException{
    	Map<String, Object> map = new HashMap<String, Object>();
    	ObjectMapper mapper = new ObjectMapper();
    	mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    	try {
    		Page page = new Page();
    		page.setNumPerPage(Integer.MAX_VALUE);
    		Specification<MesDrivertypeProperty> specification = DynamicSpecifications.bySearchFilter(request, MesDrivertypeProperty.class
    				,new SearchFilter("propertyname", Operator.EQ, name)
    				,new SearchFilter("mesDrivertype.id", Operator.EQ, typeId)
    				);
    		List<MesDrivertypeProperty> mdtp = mdtpServ.findPage(specification, page);
    		if(mdtp.size() > 0){
    			map.put("0", 0);
    		}else {
    			map.put("1", 1);
    		}
    	} catch (Exception e) {
    		return null;
    	}
    	return mapper.writeValueAsString(map);
    }
    
    @RequestMapping("/checkProid/{id}/{typeId}")
    @ResponseBody
    public String checkProid(@PathVariable String id,@PathVariable Long typeId,HttpServletRequest request) throws JsonProcessingException{
    	Map<String, Object> map = new HashMap<String, Object>();
    	ObjectMapper mapper = new ObjectMapper();
    	mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    	try {
    		Page page = new Page();
    		page.setNumPerPage(Integer.MAX_VALUE);
    		Specification<MesDrivertypeProperty> specification = DynamicSpecifications.bySearchFilter(request, MesDrivertypeProperty.class
    				,new SearchFilter("propertykeyid", Operator.EQ, id)
    				,new SearchFilter("mesDrivertype.id", Operator.EQ, typeId)
    				);
    		List<MesDrivertypeProperty> mdtp = mdtpServ.findPage(specification, page);
    		if(mdtp.size() > 0){
    			map.put("0", 0);
    		}else {
    			map.put("1", 1);
    		}
    	} catch (Exception e) {
    		return null;
    	}
    	return mapper.writeValueAsString(map);
    }
}
