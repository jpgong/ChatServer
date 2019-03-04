package com.chatTest.message;

import java.io.Serializable;

public class Message implements Serializable{
	private static final long serialVersionUID = 7782423484748978989L;
	private String srcUser;
	 private String dstUser;
	 
	public Message(String srcUser, String dstUser) {
		super();
		this.srcUser = srcUser;
		this.dstUser = dstUser;
	}
	public String getSrcUser() {
		return srcUser;
	}
	public void setSrcUser(String srcUser) {
		this.srcUser = srcUser;
	}
	public String getDstUser() {
		return dstUser;
	}
	public void setDstUser(String dstUser) {
		this.dstUser = dstUser;
	}
	 
	

}
