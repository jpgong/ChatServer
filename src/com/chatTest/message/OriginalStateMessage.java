package com.chatTest.message;

import com.chatTest.bean.UserBean;

/**
 * �ж��û���ʼ״̬��Ϣ
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
	 * �ж��Ƿ�Ϊע����Ϣ
	 */
	public boolean isRegistMessage() {
		return getDstUser().equals("Regist");
	}
	
	/*
	 * �ж��Ƿ�Ϊ��¼��Ϣ
	 */
	public boolean isLoginMessage() {
		return getDstUser().equals("Login");
	}
	
	/*
	 * �ж��Ƿ�Ϊ�޸���Ϣ
	 */
	public boolean isModifyMessage() {
		return getDstUser().equals("Modify");
	}
	/*
	 * �ж��Ƿ�Ϊ����û���Ϣ
	 */
	public boolean isModifyPreMessage() {
		return getDstUser().equals("ModifyPrepare");
	}
	
}
