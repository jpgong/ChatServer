package com.chatTest.message;

import com.chatTest.bean.FileBean;

/**
 * 文件的状态消息（是否接收）
 * @author jpgong
 *
 */
public class FileStateMessage extends Message {
	private static final long serialVersionUID = 1L;
	private FileBean fileBean;
	private boolean state;
	private String type;
	
	public FileStateMessage(String srcUser, String dstUser, FileBean fileBean, boolean state, String type) {
		super(srcUser, dstUser);
		this.fileBean = fileBean;
		this.state = state;
		this.type = type;
	}

	public FileBean getFileBean() {
		return fileBean;
	}

	public void setFileBean(FileBean fileBean) {
		this.fileBean = fileBean;
	}

	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean isRequest() {
		return type.equals("请求");
	}
	public boolean isResponse() {
		return type.equals("响应");
	}
}
