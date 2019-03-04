package com.chatTest.Jframe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JSplitPane;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JTextPane;
import javax.swing.JTable;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.awt.Component;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.swing.Box;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import com.chatTest.bean.FileBean;
import com.chatTest.bean.UserBean;
import com.chatTest.derby.UserDatabase;
import com.chatTest.message.ChatMessage;
import com.chatTest.message.FileStateMessage;
import com.chatTest.message.ImageMessage;
import com.chatTest.message.Message;
import com.chatTest.message.OriginalStateMessage;
import com.chatTest.message.UserStateMessage;
import com.chatTest.util.FontStyle;
import com.chatTest.util.SSLUtil;

import java.awt.Dimension;

public class ServerFrame extends JFrame {

	private static final long serialVersionUID = -5509496527069806201L;
	private JPanel contentPane;
	private final DefaultTableModel onLineUsersDtm = new DefaultTableModel(new Object[] { "用户名", "IP", "端口", "登录时间" },
			0);
	private final int PORT = 9999;
	private UserManager userManager = new UserManager();
	private SSLServerSocket serverSocket;
	private JTable tableOnlineUsers;
	private JTextPane textPaneMsgRecord;
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	public UserDatabase userDatabase;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerFrame frame = new ServerFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ServerFrame() {
		setTitle("\u670D\u52A1\u5668");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 551, 369);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panelSouth = new JPanel();
		contentPane.add(panelSouth, BorderLayout.CENTER);
		panelSouth.setLayout(new BoxLayout(panelSouth, BoxLayout.X_AXIS));

		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(1.0);
		panelSouth.add(splitPane);

		textPaneMsgRecord = new JTextPane();
		JScrollPane scrollPaneMsgRecord = new JScrollPane(textPaneMsgRecord);
		scrollPaneMsgRecord.setBorder(
				new TitledBorder(null, "\u6D88\u606F\u8BB0\u5F55", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		splitPane.setLeftComponent(scrollPaneMsgRecord);

		tableOnlineUsers = new JTable(onLineUsersDtm);
		JScrollPane scrollPaneOnlineUser = new JScrollPane(tableOnlineUsers);
		scrollPaneOnlineUser.setPreferredSize(new Dimension(275, 402));
		scrollPaneOnlineUser.setBorder(
				new TitledBorder(null, "\u7528\u6237\u5217\u8868", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		splitPane.setRightComponent(scrollPaneOnlineUser);

		JPanel panelNorth = new JPanel();
		contentPane.add(panelNorth, BorderLayout.SOUTH);

		Component horizontalStrut = Box.createHorizontalStrut(20);
		panelNorth.add(horizontalStrut);

		JButton btnStart = new JButton("\u542F\u52A8");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					// 创建ServerSocket打开端口9999监听客户端连接,创建服务器
					SSLContext context = SSLUtil.createServerSSLContext();
					SSLServerSocketFactory factory = context.getServerSocketFactory();
					serverSocket = (SSLServerSocket) factory.createServerSocket(PORT);
					// 在“消息记录”文本框中用红色显示“服务器启动成功”和启动时间信息
					String msg = dateFormat.format(new Date()) + " 服务器启动成功！" + "\r\n";
					FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.RED, 12, false, false);
					userDatabase = new UserDatabase();
					// 建立一个匿名线程类用来创建启动线程
					new Thread() {
						public void run() {
							while (true) {
								try {
									SSLSocket socket = (SSLSocket) serverSocket.accept();
									UserHandle userHandle = new UserHandle(socket);
									new Thread(userHandle).start();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					}.start();
					btnStart.setEnabled(false);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnStart.setFont(new Font("楷体", Font.BOLD, 15));
		panelNorth.add(btnStart);

		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		panelNorth.add(horizontalStrut_1);

		JButton btnManageUser = new JButton("\u7BA1\u7406\u7528\u6237");
		btnManageUser.addActionListener(e -> {
			ManagerUsers managerUsers = new ManagerUsers(this);
			// 使窗口可见
			managerUsers.setVisible(true);
			managerUsers.setSize(new Dimension(551, 369));
			this.setVisible(false);
			
			managerUsers.showUsersMessage();
			
		});
		btnManageUser.setFont(new Font("楷体", Font.BOLD, 15));
		panelNorth.add(btnManageUser);
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int i = JOptionPane.showConfirmDialog(ServerFrame.this, "确定要退出该系统？","退出系统",JOptionPane.YES_NO_OPTION);
				if (i == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});
	}

	// 实现用户服务线程
	class UserHandle implements Runnable {
		private SSLSocket currentSocket;
		private ObjectInputStream ois;
		private ObjectOutputStream oos;

		public UserHandle(SSLSocket socket) {
			currentSocket = socket;
			try {
				ois = new ObjectInputStream(socket.getInputStream());
				oos = new ObjectOutputStream(socket.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void run() {
			try {
				while (true) {
					Message message;
					synchronized (ois) {
						message = (Message) ois.readObject();
					}
					if (message instanceof UserStateMessage) {
						// 处理用户状态消息
						processUserStateMessage((UserStateMessage) message);

					} else if (message instanceof ChatMessage) {
						// 处理聊天消息
						processChatMessage((ChatMessage) message);

					} else if (message instanceof OriginalStateMessage) {
						// 处理登录和注册消息
						processOriginalMessage((OriginalStateMessage) message);
					} else if (message instanceof FileStateMessage) {
						//处理文件发送和接收请求消息
						processFileStateMessage((FileStateMessage)message);
					}else if (message instanceof ImageMessage) {  //处理图片消息
						processImageMessage((ImageMessage) message);
					}else {
						System.out.println("消息格式错误！");
					}
				}
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			} finally {
				if (currentSocket != null) {
					try {
						currentSocket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		/**
		 * 处理图片消息
		 * @param message
		 */
		private void processImageMessage(ImageMessage message) {
			String srcName = message.getSrcUser();
			String dstName = message.getDstUser();
			ImageIcon imageIcon = message.getImageIcon();
			if (userManager.hasUser(srcName)) { // 如果该用户在用户列表中
				if (message.isPublicMessage()) {
					// 将公聊消息发送给在线所有用户
					String msg = dateFormat.format(new Date()) + " " + srcName + "向大家发照片：\r\n";
					FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.black, 12, false, false);
					StyledDocument doc = textPaneMsgRecord.getStyledDocument();
					textPaneMsgRecord.setCaretPosition(doc.getLength()); // 设置插入位置
					textPaneMsgRecord.insertIcon(imageIcon);
					try {
						doc.insertString(doc.getLength(), "\r\n", null);
					} catch (BadLocationException e1) {
						e1.printStackTrace();
					}
					tranferMsgToOthers(message);
				} else {
					// 将私聊照片消息发送给用户
					String msg = dateFormat.format(new Date()) + " " + srcName + "向" + dstName + "发照片：\r\n";
					FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.black, 12, false, false);
					StyledDocument doc = textPaneMsgRecord.getStyledDocument();
					textPaneMsgRecord.setCaretPosition(doc.getLength()); // 设置插入位置
					textPaneMsgRecord.insertIcon(imageIcon);
					try {
						doc.insertString(doc.getLength(), "\r\n", null);
					} catch (BadLocationException e1) {
						e1.printStackTrace();
					}
					// 将该消息发送给目标用户
					tranferMsgToDst(message);
				}
			} else {
				// 这种情况对应着用户未发送上线消息就直接发送了聊天消息，应该发消息提示客户端
				System.err.println("用启未发送上线消息就直接发送了聊天消息");
				return;
			}
		}

		/**
		 * 处理文件发送请求消息和文件接收请求消息
		 * 
		 * @param message
		 */
		private void processFileStateMessage(FileStateMessage message) {
			String srcName = message.getSrcUser();
			String dstName = message.getDstUser();
			FileBean fileBean = message.getFileBean();
			String fileName = fileBean.getFileName();
			if (message.isRequest()) { // 是文件发送请求消息
				String msg = dateFormat.format(new Date()) + " " + srcName + "请求向" + dstName + "发送文件：" + fileName
						+ "\r\n";
				FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.black, 12, false, false);
				tranferMsgToDst(message);
			} else if (message.isResponse()) { // 是文件发送响应消息
				if (message.isState()) { // 接收者同意接收该文件
					String msg = dateFormat.format(new Date()) + " " + dstName + "接收了" + srcName + "发送文件：" + fileName + "的请求。"
							+ "\r\n";
					FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.black, 12, false, false);
					fileBean.setAddress(userManager.getSocket(dstName).getInetAddress());
					// 将该消息发送给发送者，让他开始发文件
					tranferFileMsgToSrc(message);

				} else { // 接收者拒绝接收该文件
					String msg = dateFormat.format(new Date()) + " " + dstName + "拒绝" + srcName + "发送文件：" + fileName + "的请求。"
							+ "\r\n";
					FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.black, 12, false, false);
					//把接收者拒绝接收文件的消息发送给发送者
					tranferFileMsgToSrc(message);
				}
			} else {
				System.out.println("该消息不是文件消息类型");
				return;
			}
		}

		/**
		 * 把文件响应消息发送给发送人
		 * 
		 * @param message
		 */
		private void tranferFileMsgToSrc(FileStateMessage message) {
			String srcUsers = message.getSrcUser();
			// 如果存在该目标用户，则发送消息，否则就见鬼了
			if (userManager.hasUser(srcUsers)) {
				ObjectOutputStream oos = userManager.getUseroos(srcUsers);
				synchronized (oos) {
					try {
						oos.writeObject(message);
						oos.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} else {
				System.out.println("见鬼了，不存在该用户！");
			}
		}

		/**
		 * 处理用户注册和登陆消息
		 * 
		 * @param message
		 */
		private void processOriginalMessage(OriginalStateMessage message) {
			String srcName = message.getSrcUser();
			UserBean userBean = message.getUserBean();
		
			// 获得该用户的注册或者登录消息
			String userName = userBean.getUserName();
			String userPwd = userBean.getPassword();
			String sex = userBean.getSex();
			String telPhone = userBean.getTelPhone();
			String mail = userBean.getMailBox();

			if (message.isRegistMessage()) {
				String msg = dateFormat.format(new Date()) + " 收到了" + srcName + "发来的用户注册消息\r\n";
				FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.black, 12, false, false);

				// 如果注册成功，则在消息日志中记载并将结果返回给客户端
				if (userDatabase.insertUser(userName, userPwd, sex, telPhone, mail)) {
					String msg1 = dateFormat.format(new Date()) + " 用户" + srcName + "注册成功\r\n";
					FontStyle.addMsgRecord(textPaneMsgRecord, msg1, Color.black, 12, false, false);
					// 注册成功，将成功标志位设置为true
					message.setSucceed(true);
					message.setRepeat(false);
					// 将该消息发送给注册人，表明注册成功
					tranferMsgToSrc(message);
				} else {
					// 在日志文件里面说明用户注册失败
					String msg1 = dateFormat.format(new Date()) + " 注册失败\r\n";
					FontStyle.addMsgRecord(textPaneMsgRecord, msg1, Color.black, 12, false, false);
					// 如果注册失败，则将标志位设置为false
					message.setSucceed(false);
					message.setRepeat(false);
					// 将该消息发送给注册人，表明注册失败
					tranferMsgToSrc(message);
				}
			} else if (message.isLoginMessage()) { // 处理登录消息
				if (userManager.hasUser(srcName)) { // 如果用户重复登录
					String msg = dateFormat.format(new Date()) + " " + srcName + " 重复登录！" + "\r\n";
					FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.GREEN, 12, false, false);
					
					message.setSucceed(false);
					message.setRepeat(true);
					// 将该消息发送给登录人，表明登录失败
					tranferMsgToSrc(message);
				} else if (userDatabase.checkUserPassword(userName, userPwd)) { // 数据库中该用户名和密码相匹配
					String msg = dateFormat.format(new Date()) + " " + srcName + " 登录了！" + "\r\n";
					FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.GREEN, 12, false, false);
					message.setSucceed(true);
					message.setRepeat(false);
					tranferMsgToSrc(message);

				} else {
					String msg = dateFormat.format(new Date()) + " " + srcName + " 登录失败！" + "\r\n";
					FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.GREEN, 12, false, false);
					// 如果注册失败，则将标志位设置为false
					message.setSucceed(false);
					message.setRepeat(false);
					// 将该消息发送给注册人，表明注册失败
					tranferMsgToSrc(message);
				}
			} else if (message.isModifyPreMessage()) {  //返回用户消息
				String msg = dateFormat.format(new Date()) + " " + srcName + " 要求修改信息！" + "\r\n";
				FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.GREEN, 12, false, false);
				userBean = userDatabase.getUserMesssage(userBean.getUserName());
				
				if (userBean != null) {    //如果从数据库获得数据则将该数据返回给客户端
					message.setUserBean(userBean);
					message.setSucceed(true);
					message.setRepeat(false);
					tranferMsgToSrc(message);
				}else {   //处理不成功消息
					message.setSucceed(false);    
					message.setRepeat(false);
					tranferMsgToSrc(message);
					System.out.println("请求修改消息请求被回拒!");
				}
				
			} else if (message.isModifyMessage()) {  //处理用户更改信息消息
				if(userDatabase.UpdateUser(userBean,srcName)) {   //在数据库中更新该用户信息
					message.setSucceed(true);
					message.setRepeat(false);
					tranferMsgToSrc(message);
					String msg = dateFormat.format(new Date()) + " " + srcName + " 修改信息成功！" + "\r\n";
					FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.GREEN, 12, false, false);
				}else {
					message.setSucceed(false);
					message.setRepeat(false);
					tranferMsgToSrc(message);
					String msg = dateFormat.format(new Date()) + " " + srcName + " 修改信息失败！" + "\r\n";
					FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.GREEN, 12, false, false);
				}
			}else {
				System.out.println("不存在该消息格式。");
				return;
			}
		}

		/**
		 * 将用户注册或者登陆是否成功的消息发送给注册人
		 * 
		 * @param message
		 */
		private void tranferMsgToSrc(OriginalStateMessage message) {
			synchronized (oos) {
				try {
					oos.writeObject(message);
					oos.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		/**
		 * 处理用户聊天消息
		 * 
		 * @param message
		 */
		private void processChatMessage(ChatMessage message) {
			String srcName = message.getSrcUser();
			String dstName = message.getDstUser();
			String msgContent = message.getMsgContent();
			if (userManager.hasUser(srcName)) { // 如果该用户在用户列表中
				if (message.isPublicMessage()) {
					// 将公聊消息发送给在线所有用户
					String msg = dateFormat.format(new Date()) + " " + srcName + "向大家说：" + msgContent + "\r\n";
					FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.black, 12, false, false);
					tranferMsgToOthers(message);
				} else {
					// 将私聊消息发送给用户
					String msg = dateFormat.format(new Date()) + " " + srcName + "向" + dstName + "发消息：" + msgContent
							+ "\r\n";
					FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.black, 12, false, false);
					// 将该消息发送给目标用户
					tranferMsgToDst(message);
				}
			} else {
				// 这种情况对应着用户未发送上线消息就直接发送了聊天消息，应该发消息提示客户端
				System.err.println("用启未发送上线消息就直接发送了聊天消息");
				return;
			}
		}

		/**
		 * 将该私聊消息发送给目标用户
		 * 
		 * @param message
		 */
		private void tranferMsgToDst(Message message) {
			String dstUser = message.getDstUser();
			// 如果存在该目标用户，则发送消息，否则就见鬼了
			if (userManager.hasUser(dstUser)) {
				ObjectOutputStream oos = userManager.getUseroos(dstUser);
				synchronized (oos) {
					try {
						oos.writeObject(message);
						oos.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} else {
				System.out.println("见鬼了，不存在该用户！");
			}
		}

		/**
		 * 处理用户状态消息
		 * 
		 * @param message
		 */
		public void processUserStateMessage(UserStateMessage message) {
			String srcUser = message.getSrcUser();
			if (message.isUserOffline()) { // 用户已下线
				if (userManager.hasUser(srcUser)) {
					// 用绿色字在“消息记录”文本框中显示用户下线消息及下线时间
					String msg = dateFormat.format(new Date()) + " " + srcUser + " 退出了" + "\r\n";
					FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.GREEN, 12, false, false);
					// System.out.println(srcUser + "下线了");
					userManager.removeUser(srcUser);
					for (int i = 0; i < onLineUsersDtm.getRowCount(); i++) {
						if (onLineUsersDtm.getValueAt(i, 0).equals(srcUser)) {
							// 在“在线用户列表”中删除下线用户
							onLineUsersDtm.removeRow(i);
						}
					}
					tranferMsgToOthers(message);
				} else {
					System.out.println("收到了幽灵消息。");
				}
			} else if (message.isUserOnline()) { // 处理用户上线消息
				if (userManager.hasUser(srcUser)) {
					String msg = dateFormat.format(new Date()) + " " + srcUser + " 重复登录了了" + "\r\n";
					FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.GREEN, 12, false, false);
					System.out.println(srcUser + "重复登录了");
					return;
				}

				System.out.println("有新用户上线");
				// 向新上线的用户转发其他用户的消息
				String[] users = userManager.getAllUser();
				for (String user : users) {
					UserStateMessage userStateMessage = new UserStateMessage(user, srcUser, true);
					synchronized (oos) {
						try {
							oos.writeObject(userStateMessage);
							oos.flush();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

				// 向其他用户转发新用户的消息
				tranferMsgToOthers(message);
				userManager.addUser(srcUser, currentSocket, ois, oos);
				// 将新上线的用户信息加入到在线用户列表中
				onLineUsersDtm.addRow(new Object[] { srcUser, currentSocket.getInetAddress().getHostAddress(),
						currentSocket.getPort(), dateFormat.format(new Date()) });
			}
		}

		/**
		 * 将用户下线消息转发给所有其它在线用户
		 */
		private void tranferMsgToOthers(Message msg) {
			String[] users = userManager.getAllUser();
			for (String user : users) {
				// 给出该用户以外的人发送消息
				if (!user.equals(msg.getSrcUser())) {
					ObjectOutputStream oos = userManager.getUseroos(user);
					try {
						synchronized (oos) {
							oos.writeObject(msg);
							oos.flush();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}

// 管理用户信息的工具类
class UserManager {
	// 用来存放用户的用户名和User关联起来
	private Map<String, User> onLineUser = new HashMap<>();

	// 判断一个用户是否在线
	public boolean hasUser(String userName) {
		return onLineUser.containsKey(userName);
	}

	// 判断用户列表是否为空
	public boolean isEmpty() {
		return onLineUser.isEmpty();
	}

	// 获取在线用户的对象输出流
	public ObjectOutputStream getUseroos(String userName) {
		if (hasUser(userName)) {
			return onLineUser.get(userName).getOos();
		}
		return null;
	}

	// 获取在线用户的对象输入流
	public ObjectInputStream getUserois(String userName) {
		if (hasUser(userName)) {
			return onLineUser.get(userName).getOis();
		}
		return null;
	}

	// 获取在线用户的Socket对象
	public Socket getSocket(String userName) {
		if (hasUser(userName)) {
			return onLineUser.get(userName).getSocket();
		}
		return null;
	}

	// 添加、删除、遍寻、计数
	public boolean addUser(String userName, Socket socket) {
		if (userName != null && socket != null) {
			onLineUser.put(userName, new User(socket));
			return true;
		}
		return false;
	}

	public boolean addUser(String userName, Socket socket, ObjectInputStream ois, ObjectOutputStream oos) {
		if (userName != null && socket != null && ois != null && oos != null) {
			onLineUser.put(userName, new User(socket, ois, oos));
			return true;
		}
		return false;
	}

	public boolean removeUser(String userName) {
		if (hasUser(userName)) {
			onLineUser.remove(userName);
			return true;
		}
		return false;
	}

	// 获取当前在线用户列表
	public String[] getAllUser() {
		String[] users = new String[onLineUser.size()];
		if (users.length > 0) {
			int i = 0;
			for (Map.Entry<String, User> entry : onLineUser.entrySet()) {
				users[i++] = entry.getKey();
			}
		}
		return users;
	}

	// 返回当其在线用户个数
	public int getOnlineUser() {
		return onLineUser.size();
	}
}

// 一个user对象代表服务器上的一个在线用户
class User {
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private Date date;

	public User(Socket socket) {
		this.socket = socket;
		try {
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		date = new Date();
	}

	public User(Socket socket, ObjectInputStream ois, ObjectOutputStream oos) {
		this.socket = socket;
		this.ois = ois;
		this.oos = oos;
		date = new Date();
	}

	public User(Socket socket, ObjectInputStream ois, ObjectOutputStream oos, Date date) {
		this.socket = socket;
		this.ois = ois;
		this.oos = oos;
		this.date = date;
	}

	public Socket getSocket() {
		return socket;
	}

	public ObjectInputStream getOis() {
		return ois;
	}

	public ObjectOutputStream getOos() {
		return oos;
	}

	public Date getDateTime() {
		return date;
	}
}
