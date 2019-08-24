package com.its.frd.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Friends entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "friends")
public class Friends implements java.io.Serializable {

	// Fields

	private Long id;
	private Long userid;
	private Long friendid;
	private String phone;
	private String username;
	private String friendname;
	private Long remainingNum;
	private String email;
	// Constructors

	/** default constructor */
	public Friends() {
	}

	/** minimal constructor */
	public Friends(Long userid, Long friendid) {
		this.userid = userid;
		this.friendid = friendid;
	}

	/** full constructor */
	public Friends(Long userid, Long friendid,String phone,String username,  String friendname,String email) {
		this.userid = userid;
		this.friendid = friendid;
		this.phone =phone;
		this.username =username;
		this.friendname = friendname;
		this.email = email;
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

	@Column(name = "userid", nullable = false)
	public Long getUserid() {
		return this.userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	@Column(name = "friendid", nullable = false)
	public Long getFriendid() {
		return this.friendid;
	}

	public void setFriendid(Long friendid) {
		this.friendid = friendid;
	}

	@Column(name = "friendname")
	public String getFriendname() {
		return this.friendname;
	}

	public void setFriendname(String friendname) {
		this.friendname = friendname;
	}

	@Transient
	public Long getRemainingNum() {
		return remainingNum;
	}

	public void setRemainingNum(Long remainingNum) {
		this.remainingNum = remainingNum;
	}
	
	@Transient
	//@Column(name = "email")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	@Transient
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	@Transient
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
	
}