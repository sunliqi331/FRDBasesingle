package com.its.frd.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.its.frd.entity.Companyfile;
import com.its.frd.params.UploadResultMap;
import com.its.frd.util.DateUtils;

@Controller
@RequestMapping("/frdUpload")
public class UploadController {
    @RequestMapping(value = "/upload")
    public String preUpload() {
        System.out.print("hhhhhhhhhhhhhhhh");
        return "companyManage/upload";
    }
    
    @RequestMapping("/uploadfile")
    @ResponseBody
    public UploadResultMap upload(@RequestParam("files") MultipartFile[] files){
    	System.out.println(files!=null?files.length:null);
        List<Companyfile> filelist = null;
        if (files != null && files.length > 0) {
           filelist = new ArrayList<Companyfile>();
            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];
                if (!file.isEmpty()) {
                    try {
                        String fileBaseName = file.getOriginalFilename();
                        String newFileName = UUID.randomUUID().toString()
                                + fileBaseName.substring(fileBaseName.lastIndexOf("."));
                        String filePath = com.its.frd.util.ResourceUtil.getUploadDirectory();
                        System.out.println("this is a test:"+this.getClass().getClassLoader().getResource("/").getPath());
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
                        filelist.add(new Companyfile());
                    } catch (Exception e) {
                        e.printStackTrace();
                        //return new ResponseEntity<String>("上传文件错误。", HttpStatus.INTERNAL_SERVER_ERROR);
                        return new UploadResultMap(null,UploadResultMap.ErrorCode);
                    }
                }
            }
        }
        return new UploadResultMap(filelist,UploadResultMap.OkCode);
    }
}
