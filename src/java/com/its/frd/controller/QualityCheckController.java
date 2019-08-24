package com.its.frd.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.its.common.util.dwz.AjaxObject;
import com.its.common.util.dwz.Page;
import com.its.common.util.persistence.DynamicSpecifications;
import com.its.common.util.persistence.SearchFilter;
import com.its.common.util.persistence.SearchFilter.Operator;
import com.its.common.utils.SecurityUtils;
import com.its.frd.entity.Companyfile;
import com.its.frd.entity.MesQualityImg;
import com.its.frd.params.CompanyfileType;
import com.its.frd.params.SendTemplate;
import com.its.frd.service.CompanyfileService;
import com.its.frd.service.MesQualityImgService;
import com.its.frd.util.DateUtils;

/**
 * 质检图片Controller
 * @author admin
 *
 */
@Controller
@RequestMapping("/qualityCheck")
public class QualityCheckController {
    @Resource
    private MesQualityImgService mesQualityImgService;

    @Resource
    private CompanyfileService cfServ;

    // PRE_PAGE
    private final String PRE_PAGE = "qualityManage/";

    // 质检图片列表胡米娜
    @RequestMapping("/list")
    public String list() {
        return PRE_PAGE + "list";
    }
    
    /**
     * 质检图片分页
     * @param request
     * @param page
     * @return
     */
    @RequestMapping("/qualityCheckImgsData")
    @ResponseBody
    public Map<String, Object> data(HttpServletRequest request, Page page) {
        Map<String, Object> map = new HashMap<String, Object>();
        Specification<MesQualityImg> specification = DynamicSpecifications.bySearchFilter(request, MesQualityImg.class
//                , new SearchFilter("factoryid",Operator.EQ,SecurityUtils.getShiroUser().getCompanyid())
                , new SearchFilter("factoryid",Operator.EQ, SecurityUtils.getShiroUser().getCompanyid())
                );
        List<MesQualityImg> mds = mesQualityImgService.findPage(specification, page);

        for(MesQualityImg type : mds){
            List<Companyfile> cf= cfServ.findByParentidAndType(type.getId(),CompanyfileType.QUALITYFILE.toString());
            for(Companyfile cfs : cf) {
                String picSource = request.getServletContext().getContextPath()+"/company/showPic/" +cfs.getId();
                String picDetail = request.getServletContext().getContextPath()+"/qualityCheck/showPicDetail/"+cfs.getId();
                type.setShowPic("<img data-url='"+ picDetail +"' style='height:30px; width:40px' src="+ picSource +">");
            }
        }
        map.put("page", page);
        map.put("qualityCheckImgs", mds);
        return map;
    }

    /**
     * 添加质检图片数据
     * MesDrivertypeProperty为属性数据模型,添加数据为MesDrivertypeProperty[index].属性
     * @param mesDrivertype
     * @return
     */
    // @RequiresPermissions(value={"driverType:save","driverType:edit"},logical=Logical.OR)
    @RequestMapping("/saveQualityCheckImg")
    @ResponseBody
    @SendTemplate
    public String saveQualityCheckImg(@RequestParam("files") MultipartFile[] files,MesQualityImg mesQualityImg,HttpServletRequest request,Page page){
//        String msg = "修改";
//        if(mesDrivertype.getId() == null)
//            msg = "添加";
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
                        return AjaxObject.newError( "添加" +"失败:请检查上传文件是否正确!").setCallbackType("").toString();
                    }
                }
            }
        }

        try {
            // 所属公司
            mesQualityImg.setFactoryid(SecurityUtils.getShiroUser().getCompanyid());
            // 实时图片id
            mesQualityImg.setQualityimageid(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
            // 图片上传时间
            mesQualityImg.setCreatedate(new Date());
            mesQualityImg.setUpdatedate(new Date());
            mesQualityImgService.saveAndFile(mesQualityImg, companyfilelist);

        } catch (Exception e) {
            e.printStackTrace();
            return AjaxObject.newError("添加" + "质检图片失败!").setCallbackType("").toString();
        }
        return AjaxObject.newOk("添加" + "质检图片成功!").toString();
    }
    
    /**
     * 用来检查用户是否能创建公司
     * 
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

    /**
     * 根据id删除
     * @param id
     * @return
     */
    @RequestMapping(value = "/deleteQualityImg", method = RequestMethod.POST)
    public @ResponseBody String deleteQualityImg(Long[] ids) {
        try {
            for (Long i :ids) {
                mesQualityImgService.deleteById(Long.valueOf(i));
            }
            // 删除实体文件
            this.fileDel(ids);
        } catch (Exception e) {
            return AjaxObject.newError("删除质检图片失败!").setCallbackType("").toString();
        }
        return AjaxObject.newOk("删除质检图片成功！").setCallbackType("").toString();
    }

    /**
     * 用来检查用户是否能创建公司
     * 
     * @return  
     */
    private void fileDel(Long[] ids) throws IOException {

        for (Long fileId : ids) {
            List<Companyfile> cfList = cfServ.findByParentidAndType(fileId, CompanyfileType.QUALITYFILE.toString());
            if(null != cfList && 0 < cfList.size() && null != cfList.get(0)) {
                Companyfile cf = cfList.get(0);
                File delFile = new java.io.File(cf.getfilepath() + File.separator + cf.getfilenewname());
                if(delFile.exists()) {
                    delFile.delete();
                }
            }
        }
    }

    @RequestMapping("/showPicDetail/{id}")
    public String showPicDetail (@PathVariable Long id,Map<String,Object> map) {
        map.put("id", id);
        return PRE_PAGE + "showPicDetail";
    }

    @RequestMapping("/addQualityCheckImage")
    public String addQualityCheckImage () {
        return PRE_PAGE + "addQualityCheckImage";
    }

    /**
     * 批量添加质检图片
     * @return
     */
    @RequestMapping("/addMuitiQualityCheckImage")
    public String addMuitiQualityCheckImage () {
        return PRE_PAGE + "addMuitiQualityCheckImage";
    }
    
    /**
     * 批量添加质检图片数据
     * @return
     */
    @RequestMapping("/saveMultiQualityCheckImage")
    @ResponseBody
    public String saveMultiDriverType(@RequestParam("files") MultipartFile[] files,HttpServletRequest request,Page page){
        String msg = "批量添加";
        /*if(!this.checkPictureType(files))
            return AjaxObject.newError("不支持该文件格式!").setCallbackType("").toString();*/
        List<Companyfile> companyfilelist = null;
        MesQualityImg mesQualityImg = null;
        if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];

                if (!file.isEmpty()) {
                    try {
                        companyfilelist = Lists.newArrayList();
                        fileSave(companyfilelist, file);
                        try {
                            mesQualityImg = new MesQualityImg();
                            // 所属公司
                            mesQualityImg.setFactoryid(SecurityUtils.getShiroUser().getCompanyid());
                            mesQualityImg.setQualitynm("批量上传的质检图片");
                            // 实时图片id
                            mesQualityImg.setQualityimageid(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
                            // 图片上传时间
                            mesQualityImg.setCreatedate(new Date());
                            mesQualityImg.setUpdatedate(new Date());
                            mesQualityImgService.saveAndFile(mesQualityImg, companyfilelist);

                        } catch (Exception e) {
                            e.printStackTrace();
                            return AjaxObject.newError("添加" + "质检图片失败!").setCallbackType("").toString();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return AjaxObject.newError( msg +"失败:请检查上传文件是否正确!").setCallbackType("").toString();
                    }
                }
            }
        }

        return AjaxObject.newOk(msg + "质检图片成功!").toString();
    }

}
