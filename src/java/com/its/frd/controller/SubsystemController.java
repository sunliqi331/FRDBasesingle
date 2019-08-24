package com.its.frd.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.its.common.util.dwz.AjaxObject;
import com.its.common.util.dwz.Page;
import com.its.common.util.persistence.DynamicSpecifications;
import com.its.frd.entity.Subsysteminfo;
import com.its.frd.service.SubsysteminfoService;

@Controller
@RequestMapping("/subsys")
public class SubsystemController {
	private final String PAGEPRE = "subSystem/";
	
	@Resource
	private SubsysteminfoService subsysServ;
	
	@RequestMapping(method=RequestMethod.GET)
	public String showPage(){
		return PAGEPRE + "list";
	}
	
	@RequestMapping("/data")
	@ResponseBody
	public Map<String,Object> data(ServletRequest request, Page page){
		Map<String, Object> map = new HashMap<String, Object>();
		Specification<Subsysteminfo> specification = DynamicSpecifications.bySearchFilter(request, Subsysteminfo.class);
		List<Subsysteminfo> subsysteminfos = subsysServ.findPage(specification, page);

		map.put("page", page);
		map.put("subsysteminfos", subsysteminfos);
		return map;
	}
	
	/**
	 * 图标显示使用   http://10.12.65.30:8080/FRDBase/ + subsysImg/picname
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/showOne/{id}") //-1
	public String findOne(@PathVariable Long id,Model model){
		Subsysteminfo subsysteminfo = subsysServ.findOneById(id);
		model.addAttribute("subsysteminfo", subsysteminfo);
		return PAGEPRE + "update";
	}

    @RequestMapping(value="/create",method = RequestMethod.GET)
    public String createSubSys() {
        return PAGEPRE + "create";
    }

	@RequestMapping("/saveOrUpdate")
	@ResponseBody
	public String saveOrUpdate(HttpServletRequest request,MultipartFile file,Subsysteminfo subsysteminfo){
		String msg = "修改";
		if(subsysteminfo.getId() == null)
			msg = "添加";
		try {
			if(file != null&& !file.isEmpty()){
				String fileBaseName = file.getOriginalFilename();
				String filePath = request.getSession().getServletContext().getRealPath("/") + "subsysImg";
				File uploadFile = new java.io.File(filePath + File.separator +fileBaseName);
				if (!uploadFile.exists()) {
					uploadFile.mkdirs();
				}
				file.transferTo(uploadFile);
				subsysteminfo.setPicname(fileBaseName);
			}
			
			subsysServ.saveOrUpdateInfo(subsysteminfo);
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxObject.newError(msg + "失败!").setCallbackType("").toString();
		}
		return AjaxObject.newOk(msg + "成功！").toString();
	}
	
	@RequestMapping("/delete/{id}")
	@ResponseBody
	public String deleteById(@PathVariable Long id){
		try {
			subsysServ.deleteInfoById(id);
		} catch (Exception e) {
			return AjaxObject.newError("删除失败!").setCallbackType("").toString();
		}
		return AjaxObject.newOk("删除成功！").toString();
	}
	
}
