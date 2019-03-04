package com.chatTest.derby;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import com.chatTest.bean.RegistBean;
import com.chatTest.bean.UserBean;
import com.chatTest.util.DateUtil;
import com.chatTest.util.DigestUtil;
import com.chatTest.util.StringUtil;

public class UserDatabase {
	// ## DEFINE VARIABLES SECTION ##
	// define the driver to use
	String driver = "org.apache.derby.jdbc.EmbeddedDriver";
	// the database name
	String dbName = "USERDB";
	// define the Derby connection URL to use
	String connectionURL = "jdbc:derby:" + dbName + ";create=true";
	Connection conn;

	public UserDatabase() {
		// ## LOAD DRIVER SECTION ##
		try {
			/*
			 * * Load the Derby driver.* When the embedded Driver is used this
			 * action start the Derby engine.* Catch an error and suggest a
			 * CLASSPATH problem
			 */
			Class.forName(driver);     //����jdbc������
			System.out.println(driver + " loaded. ");
		} catch (java.lang.ClassNotFoundException e) {
			System.err.print("ClassNotFoundException: ");
			System.err.println(e.getMessage());
			System.out.println("\n    >>> Please check your CLASSPATH variable   <<<\n");
		}
		String createString = "create table USERTABLE " // ����
				+ "(USERNAME varchar(20) primary key not null, " // �û���
				+ "HASHEDPWD char(20) for bit data, " // �����HASHֵ
				+ "SALT varchar(6),"   //��ֵ
				+ "SEX varchar(10),"   //�û��Ա�
				+ "TELPHONE varchar(20),"    //�û��ֻ���
				+ "MAIL varchar(20),"    //�û�����
				+ "REGISTERTIME timestamp default CURRENT_TIMESTAMP)"; // ע��ʱ��

		try {
			//�������ݿ��ļ�����־
			DriverManager.setLogWriter(new PrintWriter(new File("���ݿ���־�ļ�.txt")));
			// Create (if needed) and connect to the database
			conn = DriverManager.getConnection(connectionURL);
			// Create a statement to issue simple commands.
			Statement s = conn.createStatement();
			// Call utility method to check if table exists.
			// Create the table if needed
			if (!checkTable(conn)) {
				System.out.println(" . . . . creating table USERTABLE");
				s.execute(createString);
			}
			s.close();
			System.out.println("Database openned normally");
		} catch (SQLException e) {
			errorPrint(e);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	// Insert a new user into the USERTABLE table
	/**
	 * ���USERTABLE�в���һ���µ��û�
	 * 
	 * @param userName
	 *            �û���
	 * @param userPwd
	 *            δ���м��ܵĿ���ֵ
	 * @param sex
	 *            �û��Ա�
	 * @param telPhone
	 *            �û��ֻ�����
	 * @param smail
	 *            �û�����
	 * @return
	 */
	public boolean insertUser(String userName, String userPwd, String sex, String telPhone, String mail) {
		try {
			// �����ӵ���ڲ������м�飬����û���������ֻ��ţ������Ƿ�Ϊ�գ��ظ�
			if (!userName.isEmpty() && !userPwd.isEmpty() && !sex.isEmpty() && !telPhone.isEmpty()
					&& !mail.isEmpty()) {
				// ����ִ��Ԥ����SQL���
				PreparedStatement psTest = conn.prepareStatement("select * from USERTABLE where USERNAME=? and TELPHONE=? and MAIL=?",
						ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				psTest.setString(1, userName);
				psTest.setString(2, telPhone);
				psTest.setString(3, mail);
				ResultSet rs = psTest.executeQuery();
				rs.last();
				int n = rs.getRow();
				psTest.close();
				if (n == 0) {
					PreparedStatement psInsert = conn.prepareStatement("insert into USERTABLE values (?,?,?,?,?,?,?)");
					String salt = StringUtil.getRandomString(6);
					byte[] hashedPwd = DigestUtil.getSHA1Digest(userPwd + salt);
					psInsert.setString(1, userName);
					psInsert.setBytes(2, hashedPwd);
					psInsert.setString(3, salt);
					psInsert.setString(4, sex);
					psInsert.setString(5, telPhone);
					psInsert.setString(6, mail);
					psInsert.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
					psInsert.executeUpdate();
					psInsert.close();
					System.out.println("�ɹ�ע�����û�" + userName);
					return true;
				}else {
					System.out.println("�����Ѵ��ڸ��û���Ϣ");
				}
			} else {
				System.out.println("��ڲ�������������");
			}
		}catch (SQLException e) {
			errorPrint(e);
		}
		return false;
	}

	/**
	 * ɾ�����û���Ϣ
	 * @param userName �û���
	 * @param userPwd  δ�����ܵĿ���ֵ
	 * @return ɾ���ɹ�������true,ɾ��ʧ�ܷ���false
	 */
	public boolean deleteUser(String userName) {
		boolean flag = false;
		try {
			PreparedStatement psDelete = conn
					.prepareStatement("delete from USERTABLE where USERNAME=?");
			psDelete.setString(1, userName);
			int n = psDelete.executeUpdate();
			psDelete.close();
			if (n > 0) {
				System.out.println("�ɹ�ɾ���û�" + userName);
				flag = true;
			} else {
				System.out.println("ɾ���û�" + userName + "ʧ��,���ݿ���û�и��û�");
				flag = false;
			}
		} catch (SQLException e) {
			errorPrint(e);
		}
		return flag;
	}

	// check if userName with password userPwd can logon
	/**
	 * �����û����Ϳ���ֵ�Ƿ�һ��
	 * @param userName
	 * @param userPwd
	 * @return ��� ���и��û����ͺͿ���ֵһ�£��򷵻�true,���򷵻�false
	 */
	public boolean checkUserPassword(String userName, String userPwd) {
		boolean flag = false;
		try {
			if (!userName.isEmpty() && !userPwd.isEmpty()) {
				
				PreparedStatement psTest = conn.prepareStatement(
						"select * from USERTABLE where USERNAME=?",
						ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				psTest.setString(1, userName);
				ResultSet rs = psTest.executeQuery();
				if (rs.next()) {  
					String salt = rs.getString("SALT");
					byte[] psswordDb = rs.getBytes("HASHEDPWD");
					
					byte[] hashedPwd = DigestUtil.getSHA1Digest(userPwd + salt);
					//������ݿ��еĿ���ֵ�ʹ������Ŀ���ֵһ��ʱ����˵��ƥ�䣬����true
					if (new HexBinaryAdapter().marshal(psswordDb).equals(new HexBinaryAdapter().marshal(hashedPwd))) {
						flag = true;
					}
				}
			}
		} catch (SQLException e) {
			errorPrint(e);
		}
		System.out.println(flag);
		return flag;
	}

	// show the information of all users in table USERTABLE, should be called
	// before the program exited
	/**
	 * ��ʾ��ǰ���е������û���Ϣ
	 */
	public void showAllUsers() {
		String printLine = "  ______________��ǰ����ע���û�______________";
		try {
			Statement s = conn.createStatement();
			// Select all records in the USERTABLE table
			ResultSet users = s
					.executeQuery("select USERNAME, HASHEDPWD, SALT, SEX, TELPHONE, MAIL, REGISTERTIME from USERTABLE order by REGISTERTIME");

			// Loop through the ResultSet and print the data
			System.out.println(printLine);
			while (users.next()) {
				System.out.println("User-Name: " + users.getString("USERNAME")
						+ " Hashed-Pasword: "
						+ new HexBinaryAdapter().marshal(users.getBytes("HASHEDPWD"))
						+ " TelPhone:" + users.getString("TELPHONE")
						+ " Salt:" + users.getString("SALT")
						+ " Sex:" + users.getString("SEX")
						+ " Regiester-Time:" + DateUtil.getDateFormat(users.getTimestamp("REGISTERTIME")));
			}
			System.out.println(printLine);
			// Close the resultSet
			s.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<RegistBean> getAllUsers() {
		RegistBean registBean = null;
		List<RegistBean> beans = new ArrayList<>();
		try {
			Statement s = conn.createStatement();
			// Select all records in the USERTABLE table
			ResultSet users = s
					.executeQuery("select USERNAME, HASHEDPWD, SALT, SEX, REGISTERTIME from USERTABLE order by REGISTERTIME");

			// Loop through the ResultSet and print the data
			while (users.next()) {
				registBean = new RegistBean();
				registBean.setUserName(users.getString("USERNAME"));
				registBean.setSex(users.getString("SEX"));
				registBean.setPassword(new HexBinaryAdapter().marshal(users.getBytes("HASHEDPWD")));
				registBean.setSalt(users.getString("SALT"));
				registBean.setCreateDate(DateUtil.getDateFormat(users.getTimestamp("REGISTERTIME")));
				beans.add(registBean);
			}
			// Close the resultSet
			s.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return beans;
	}
	// �ر����ݿ�
	public void shutdownDatabase() {
		/***
		 * In embedded mode, an application should shut down Derby. Shutdown
		 * throws the XJ015 exception to confirm success.
		 ***/
		if (driver.equals("org.apache.derby.jdbc.EmbeddedDriver")) {
			boolean gotSQLExc = false;
			try {
				conn.close();
				DriverManager.getConnection("jdbc:derby:;shutdown=true");
			} catch (SQLException se) {
				if (se.getSQLState().equals("XJ015")) {
					gotSQLExc = true;
				}
			}
			if (!gotSQLExc) {
				System.out.println("Database did not shut down normally");
			} else {
				System.out.println("Database shut down normally");
			}
		}
	}

	/*** Check for USER table ****/
	public boolean checkTable(Connection conTst) throws SQLException {
		try {
			Statement s = conTst.createStatement();
			s.execute("update USERTABLE set USERNAME= 'TEST', REGISTERTIME = CURRENT_TIMESTAMP where 1=3");
		} catch (SQLException sqle) {
			String theError = (sqle).getSQLState();
			// System.out.println("  Utils GOT:  " + theError);
			/** If table exists will get - WARNING 02000: No row was found **/
			if (theError.equals("42X05")) // Table does not exist
			{
				return false;
			} else if (theError.equals("42X14") || theError.equals("42821")) {
				System.out
						.println("checkTable: Incorrect table definition. Drop table USERTABLE and rerun this program");
				throw sqle;
			} else {
				System.out.println("checkTable: Unhandled SQLException");
				throw sqle;
			}
		}
		return true;
	}

	// Exception reporting methods with special handling of SQLExceptions
	static void errorPrint(Throwable e) {
		if (e instanceof SQLException) {
			SQLExceptionPrint((SQLException) e);
		} else {
			System.out.println("A non SQL error occured.");
			e.printStackTrace();
		}
	}

	// Iterates through a stack of SQLExceptions
	static void SQLExceptionPrint(SQLException sqle) {
		while (sqle != null) {
			System.out.println("\n---SQLException Caught---\n");
			System.out.println("SQLState:   " + (sqle).getSQLState());
			System.out.println("Severity: " + (sqle).getErrorCode());
			System.out.println("Message:  " + (sqle).getMessage());
			sqle.printStackTrace();
			sqle = sqle.getNextException();
		}
	}
	
	/**
	 * �������ݿ��и��û���Ϣ
	 * @param userName
	 * @return
	 */

	public UserBean getUserMesssage(String userName) {
		UserBean userBean = null;
		try {
			if (!userName.isEmpty()) {
				PreparedStatement psfind = conn.prepareStatement(
						"select * from USERTABLE where USERNAME=?",
						ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				psfind.setString(1, userName);
				ResultSet rs = psfind.executeQuery();
				if (rs.next()) {
					userBean = new UserBean();
					userBean.setUserName(userName);
					userBean.setPassword(new HexBinaryAdapter().marshal(rs.getBytes("HASHEDPWD")));
					userBean.setSex(rs.getString("SEX"));
					userBean.setTelPhone(rs.getString("TELPHONE"));
					userBean.setMailBox(rs.getString("MAIL"));
				}
			}else {
				System.out.println("�û���ϢΪ�ա�");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userBean;
	}
	
	/**
	 * �����û���Ϣ�޸Ĺ���
	 * @param userBean
	 * username �����ݿ�����Ҫ�޸ĵ��û���
	 * @return ����޸ĳɹ����򷵻�true;����޸�ʧ��ʧ�ܣ��򷵻�false
	 */

	public boolean UpdateUser(UserBean userBean,String username) {
		boolean flag = false;
		try {
			if (userBean != null) {
				PreparedStatement psTest = conn.prepareStatement(
						"select * from USERTABLE where USERNAME=?",
						ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				psTest.setString(1, username);
				ResultSet rs = psTest.executeQuery();
				if (rs.next()) {  
					//�����ֵ
					String salt = rs.getString("SALT");
					
					psTest.close();
					PreparedStatement psUpdate = conn
					.prepareStatement("update USERTABLE set USERNAME=?, HASHEDPWD=?, SEX=?, TELPHONE=?, MAIL=? where USERNAME=?");
					byte[] password = DigestUtil.getSHA1Digest(userBean.getPassword()+salt);
					
//					System.out.println(username);
//					System.out.println(userBean.getSex());
//					System.out.println(userBean.getTelPhone());
//					System.out.println(userBean.getMailBox());
//					System.out.println(userBean.getUserName());
					
					psUpdate.setString(1, userBean.getUserName());
					psUpdate.setBytes(2, password);
					psUpdate.setString(3, userBean.getSex());
					psUpdate.setString(4, userBean.getTelPhone());
					psUpdate.setString(5, userBean.getMailBox());
					psUpdate.setString(6, username);
					int n = psUpdate.executeUpdate();
					if (n > 0) {
						System.out.println(userBean.getUserName() + "�û���Ϣ�޸ĳɹ�");
						flag = true;
					} else {
						System.out.println(userBean.getUserName() + "�û���Ϣ�޸�ʧ��");
						flag = false;
					}
					psUpdate.close();
				}
			}else {
				System.out.println("�û���ϢΪ��");
			}
		} catch (SQLException e) {
			errorPrint(e);
		}
		System.out.println(flag);
		return flag;
	}
}
