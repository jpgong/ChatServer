package com.chatTest.derby;

public class TestUserDatabase {
	public static void main(String[] args) {
		UserDatabase userDatabase = new UserDatabase();
		// 注册三个新用户
//		userDatabase.insertUser("ddd", "ddd","男","13212161302","756681240@qq.com");
//		userDatabase.insertUser("ggg", "ggg","男","13212161302","756681240@qq.com");
//		userDatabase.insertUser("bbb", "bbb");
//		userDatabase.insertUser("ccc", "ccc");
		// 显示所有已注册用户信息
		userDatabase.showAllUsers();
		// 用户名"ccc"，口令"ccc"是否能登录
//		if (userDatabase.checkUserPassword("ggg", "ggg") == true) {
//			System.out.println("用户ccc可以用口令ccc登录");
//		}
////		 删除用户"bbb"
//		if (userDatabase.deleteUser("ddd", "ddd")) {
//			System.out.println("删除成功");
//		}
		// 显示所有已注册用户信息
//		userDatabase.showAllUsers();
		userDatabase.shutdownDatabase();
	}
}
