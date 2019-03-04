package com.chatTest.message;

/**
 * 表示用户在线状态
 * 
 * @author jpgong
 *
 */
public class UserStateMessage extends Message {
	private static final long serialVersionUID = -3781187687414753171L;
	private boolean userOnline;

	public UserStateMessage(String srcUser, String dstUser, boolean userOnline) {
		super(srcUser, dstUser);
		this.userOnline = userOnline;
	}

	public boolean isUserOnline() {
		return userOnline;
	}

	public boolean isUserOffline() {
		return !userOnline;
	}

	public void setUserOnline(boolean userOnline) {
		this.userOnline = userOnline;
	}

	public boolean isPubUserStateMessage() {
		return getDstUser().equals("");
	}

}
