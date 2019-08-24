package com.its.common.entity.main;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.its.common.entity.Idable;
import com.its.frd.entity.Companyinfo;
import com.its.frd.entity.MesUserPosition;
import com.its.frd.entity.UserCompanyrole;
import com.its.frd.entity.Usercompanys;

@Entity
@Table(name="keta_user")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region="com.its.common.entity.main.User")
public class User implements Idable<Long> {
	
	public static final String STATUS_DISABLED = "disabled";
	public static final String STATUS_ENABLED = "enabled";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Length(max=32)
	@Column(length=32, nullable=false)
	private String realname;

	@NotBlank
	@Length(max=32)
	@Column(length=32, nullable=false, unique=true, updatable=false)
	private String username;
	
	@Column(length=64, nullable=false)
	private String password;
	
	@Transient
	private String plainPassword;
	
	@Transient
	private String position;
	
	@Column(length=32, nullable=false)
	private String salt;
	
	@Length(max=32)
	@Column(length=32)
	private String phone;
	
	@Email
	@Length(max=128)
	@Column(length=128)
	private String email;
	
	/**
	 * 使用状态disabled，enabled
	 */
	@NotBlank
	@Length(max=16)
	@Column(length=16, nullable=false)
	private String status = STATUS_ENABLED;
	
	/**
	 * 帐号创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable=false)
	private Date createTime;
	
	@JsonIgnore
	@OneToMany(fetch=FetchType.LAZY,mappedBy="user", cascade={CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval=true)
	@OrderBy("priority ASC")
	private List<UserRole> userRoles = new ArrayList<UserRole>();
	
	@Transient
	private List<UserCompanyrole> userComrole =new ArrayList<UserCompanyrole>(); 
	
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="organizationId")
	private Organization organization;
	
	private String province;
	
	private String city;
	
	private String sex;
	
	private String companyinfo;
	
	private String registerstate;
	
	@Transient
	private String usersRole;
	
	@Transient
	private String companyRole;
	


	@Transient
	private String department;
	
	
	@Transient
	public String getCompanyRole() {
		return companyRole;
	}
	
	public void setCompanyRole(String companyRole) {
		this.companyRole = companyRole;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public User() {
		super();
	}

	public User(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	/**  
	 * 返回 realname 的值   
	 * @return realname  
	 */
	public String getRealname() {
		return realname;
	}

	/**  
	 * 设置 realname 的值  
	 * @param realname
	 */
	public void setRealname(String realname) {
		this.realname = realname;
	}

	/**  
	 * 返回 username 的值   
	 * @return username  
	 */
	public String getUsername() {
		return username;
	}

	/**  
	 * 设置 username 的值  
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**  
	 * 返回 password 的值   
	 * @return password  
	 */
	public String getPassword() {
		return password;
	}

	/**  
	 * 设置 password 的值  
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**  
	 * 返回 createTime 的值   
	 * @return createTime  
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**  
	 * 设置 createTime 的值  
	 * @param createTime
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	/**  
	 * 返回 status 的值   
	 * @return status  
	 */
	public String getStatus() {
		return status;
	}

	/**  
	 * 设置 status 的值  
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**  
	 * 返回 plainPassword 的值   
	 * @return plainPassword  
	 */
	public String getPlainPassword() {
		return plainPassword;
	}

	/**  
	 * 设置 plainPassword 的值  
	 * @param plainPassword
	 */
	public void setPlainPassword(String plainPassword) {
		this.plainPassword = plainPassword;
	}

	/**  
	 * 返回 salt 的值   
	 * @return salt  
	 */
	public String getSalt() {
		return salt;
	}

	/**  
	 * 设置 salt 的值  
	 * @param salt
	 */
	public void setSalt(String salt) {
		this.salt = salt;
	}
	
	/**  
	 * 返回 email 的值   
	 * @return email  
	 */
	public String getEmail() {
		return email;
	}

	/**  
	 * 设置 email 的值  
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**  
	 * 返回 userRoles 的值   
	 * @return userRoles  
	 */
	public List<UserRole> getUserRoles() {
		return userRoles;
	}

	/**  
	 * 设置 userRoles 的值  
	 * @param userRoles
	 */
	public void setUserRoles(List<UserRole> userRoles) {
		this.userRoles = userRoles;
	}
	
	/**  
	 * 返回 phone 的值   
	 * @return phone  
	 */
	public String getPhone() {
		return phone;
	}

	/**  
	 * 设置 phone 的值  
	 * @param phone
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	/**
	 * 
	 * @return province
	 */
	public String getProvince() {
		return province;
	}

	/**
	 * 
	 * @param province
	 */
	public void setProvince(String province) {
		this.province = province;
	}

	/**
	 * 
	 * @return city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * 
	 * @param city
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * 
	 * @return sex
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * 
	 * @param sex
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    /**
	 * 
	 * @return
	 */
    @Transient
	public String getCompanyinfo() {
		return companyinfo;
	}

	/**
	 * 
	 * @param companyname
	 */
	public void setCompanyinfo(String companyinfo) {
		this.companyinfo = companyinfo;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	@Column(name = "registerstate", length = 5)
	public String getRegisterstate() {
		return registerstate;
	}

	public void setRegisterstate(String registerstate) {
		this.registerstate = registerstate;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	@Transient
	public String getUsersRole() {
		/*String roles = "";
		for(UserRole userRole : this.userRoles){
			String roleName = userRole.getRole().getName();
			if("".equals(roles)){
				roles += roleName;
			}else{
				roles +=","+roleName;
			}
		}
		return roles;
	}*/
		return usersRole;
	}

	public void setUsersRole(String usersRole) {
		this.usersRole = usersRole;
	}


	public List<UserCompanyrole> getUserComrole() {
		return userComrole;
	}


	public void setUserComrole(List<UserCompanyrole> userComrole) {
		this.userComrole = userComrole;
	}

	
	
}
