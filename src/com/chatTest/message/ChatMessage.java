package com.chatTest.message;

public class ChatMessage extends Message{
	private static final long serialVersionUID = 4576423169309630291L;
	private String msgContent;
	public ChatMessage(String srcUser, String dstUser, String msgContent) {
		super(srcUser, dstUser);
		this.msgContent = msgContent;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}
	
	/*
	 * �ж��Ƿ�Ϊ������Ϣ
	 */
	public boolean isPublicMessage() {
		return getDstUser().equals("");
	}
	
}