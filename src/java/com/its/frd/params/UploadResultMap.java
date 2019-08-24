package com.its.frd.params;

import java.util.List;

import com.its.frd.entity.Companyfile;

public class UploadResultMap {
    public static final String ErrorCode = "300";
    public static final String OkCode = "200";
    
    private List<Companyfile> files;
    private String status;
    
    public List<Companyfile> getFiles() {
        return files;
    }
    public void setFiles(List<Companyfile> files) {
        this.files = files;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public UploadResultMap(List<Companyfile> files, String status) {
        super();
        this.files = files;
        this.status = status;
    }
    public UploadResultMap() {
        super();
    }
    
}
