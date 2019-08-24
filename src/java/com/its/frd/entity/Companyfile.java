package com.its.frd.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Companyfile entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "companyfile")
public class Companyfile implements java.io.Serializable {

	// Fields
	public static Map<String,String> picTypeMap = new HashMap<String,String>();
	
	static{
		picTypeMap.put(".jpg", ".jpg");
		picTypeMap.put(".png", ".png");
		picTypeMap.put(".jpeg", ".jpeg");
	}
	
	private Long id;
	private String filebasename;
	private Long filelength;
	private String filenewname;
	private String filepath;
	private Long parentid;
	private String parenttype;
	

	// Constructors

	/** default constructor */
	public Companyfile() {
	}

	/** full constructor */
	public Companyfile(String filebasename, Long filelength,
			String filenewname, String filepath, Long companyid,String parenttype) {
		this.filebasename = filebasename;
		this.filelength = filelength;
		this.filenewname = filenewname;
		this.filepath = filepath;
		this.parenttype = parenttype;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "filebasename", length = 30)
	public String getfilebasename() {
		return this.filebasename;
	}

	public void setfilebasename(String filebasename) {
		this.filebasename = filebasename;
	}

	@Column(name = "filelength")
	public Long getfilelength() {
		return this.filelength;
	}

	public void setfilelength(Long filelength) {
		this.filelength = filelength;
	}

	@Column(name = "filenewname", length = 50)
	public String getfilenewname() {
		return this.filenewname;
	}

	public void setfilenewname(String filenewname) {
		this.filenewname = filenewname;
	}

	@Column(name = "filepath", length = 30)
	public String getfilepath() {
		return this.filepath;
	}

	public void setfilepath(String filepath) {
		this.filepath = filepath;
	}

	@Column(name = "parentid")
	public Long getParentid() {
		return parentid;
	}

	public void setParentid(Long parentid) {
		this.parentid = parentid;
	}
	
	@Column(name = "parenttype", length = 20)
	public String getParenttype() {
		return this.parenttype;
	}

	public void setParenttype(String parenttype) {
		this.parenttype = parenttype;
	}

}