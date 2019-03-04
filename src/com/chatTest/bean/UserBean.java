package com.chatTest.bean;

import java.io.Serializable;

/**
 * 用户管理基本信息
 * 
 * @author jpgong
 *
 */
public class UserBean implements Serializable{
	private static final long serialVersionUID = 3093005814688306948L;
	private String userName;
	private String password;
	private String sex;
	private String telPhone;
	private String mailBox;
	private String createDate;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getTelPhone() {
		return telPhone;
	}

	public void setTelPhone(String telPhone) {
		this.telPhone = telPhone;
	}

	public String getMailBox() {
		return mailBox;
	}

	public void setMailBox(String mailBox) {
		this.mailBox = mailBox;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
}
