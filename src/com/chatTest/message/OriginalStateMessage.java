package com.chatTest.message;

import com.chatTest.bean.UserBean;

/**
 * 判断用户初始状态消息
 * @author jpgong
 *
 */

public class OriginalStateMessage extends Message {
	private static final long serialVersionUID = -1468835587806440977L;
	private UserBean userBean;
	private boolean isSucceed;
	public boolean isRepeat() {
		return isRepeat;
	}

	public void setRepeat(boolean isRepeat) {
		this.isRepeat = isRepeat;
	}

	private boolean isRepeat;
	
	public OriginalStateMessage(String srcUser, String dstUser, UserBean userBean, boolean isSucceed) {
		super(srcUser, dstUser);
		this.userBean = userBean;
		this.isSucceed = isSucceed;
	}

	public boolean isSucceed() {
		return isSucceed;
	}

	public void setSucceed(boolean isSucceed) {
		this.isSucceed = isSucceed;
	}

	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	/*
	 * 判断是否为注册消息
	 */
	public boolean isRegistMessage() {
		return getDstUser().equals("Regist");
	}
	
	/*
	 * 判断是否为登录消息
	 */
	public boolean isLoginMessage() {
		return getDstUser().equals("Login");
	}
	
	/*
	 * 判断是否为修改消息
	 */
	public boolean isModifyMessage() {
		return getDstUser().equals("Modify");
	}
	/*
	 * 判断是否为获得用户信息
	 */
	public boolean isModifyPreMessage() {
		return getDstUser().equals("ModifyPrepare");
	}
	
}
