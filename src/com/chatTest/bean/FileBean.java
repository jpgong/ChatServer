package com.chatTest.bean;

import java.io.Serializable;
import java.net.InetAddress;

/**
 * 文件消息类
 * @author jpgong
 *
 */
public class FileBean implements Serializable{
	private static final long serialVersionUID = 1797199300504822068L;
	private String fileName;
	private long fileSize;
	private String filePath;
	private InetAddress inetAddress;
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public InetAddress getAddress() {
		return inetAddress;
	}
	public void setAddress(InetAddress inetAddress) {
		this.inetAddress = inetAddress;
	}
}
