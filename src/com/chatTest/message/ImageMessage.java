package com.chatTest.message;

import javax.swing.ImageIcon;

public class ImageMessage extends Message{
	private static final long serialVersionUID = 2172188117036551877L;
	private ImageIcon imageIcon;
	public ImageMessage(String srcUser, String dstUser, ImageIcon imageIcon) {
		super(srcUser, dstUser);
		this.imageIcon = imageIcon;
	}
	public ImageIcon getImageIcon() {
		return imageIcon;
	}
	public void setImageIcon(ImageIcon imageIcon) {
		this.imageIcon = imageIcon;
	}
	/*
	 * �ж��Ƿ�Ϊ������Ϣ
	 */
	public boolean isPublicMessage() {
		return getDstUser().equals("");
	}
}
