package com.chatTest.derby;

public class TestUserDatabase {
	public static void main(String[] args) {
		UserDatabase userDatabase = new UserDatabase();
		// ע���������û�
//		userDatabase.insertUser("ddd", "ddd","��","13212161302","756681240@qq.com");
//		userDatabase.insertUser("ggg", "ggg","��","13212161302","756681240@qq.com");
//		userDatabase.insertUser("bbb", "bbb");
//		userDatabase.insertUser("ccc", "ccc");
		// ��ʾ������ע���û���Ϣ
		userDatabase.showAllUsers();
		// �û���"ccc"������"ccc"�Ƿ��ܵ�¼
//		if (userDatabase.checkUserPassword("ggg", "ggg") == true) {
//			System.out.println("�û�ccc�����ÿ���ccc��¼");
//		}
////		 ɾ���û�"bbb"
//		if (userDatabase.deleteUser("ddd", "ddd")) {
//			System.out.println("ɾ���ɹ�");
//		}
		// ��ʾ������ע���û���Ϣ
//		userDatabase.showAllUsers();
		userDatabase.shutdownDatabase();
	}
}
