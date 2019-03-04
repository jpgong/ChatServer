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
	private final DefaultTableModel onLineUsersDtm = new DefaultTableModel(new Object[] { "�û���", "IP", "�˿�", "��¼ʱ��" },
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
					// ����ServerSocket�򿪶˿�9999�����ͻ�������,����������
					SSLContext context = SSLUtil.createServerSSLContext();
					SSLServerSocketFactory factory = context.getServerSocketFactory();
					serverSocket = (SSLServerSocket) factory.createServerSocket(PORT);
					// �ڡ���Ϣ��¼���ı������ú�ɫ��ʾ�������������ɹ���������ʱ����Ϣ
					String msg = dateFormat.format(new Date()) + " �����������ɹ���" + "\r\n";
					FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.RED, 12, false, false);
					userDatabase = new UserDatabase();
					// ����һ�������߳����������������߳�
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
		btnStart.setFont(new Font("����", Font.BOLD, 15));
		panelNorth.add(btnStart);

		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		panelNorth.add(horizontalStrut_1);

		JButton btnManageUser = new JButton("\u7BA1\u7406\u7528\u6237");
		btnManageUser.addActionListener(e -> {
			ManagerUsers managerUsers = new ManagerUsers(this);
			// ʹ���ڿɼ�
			managerUsers.setVisible(true);
			managerUsers.setSize(new Dimension(551, 369));
			this.setVisible(false);
			
			managerUsers.showUsersMessage();
			
		});
		btnManageUser.setFont(new Font("����", Font.BOLD, 15));
		panelNorth.add(btnManageUser);
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int i = JOptionPane.showConfirmDialog(ServerFrame.this, "ȷ��Ҫ�˳���ϵͳ��","�˳�ϵͳ",JOptionPane.YES_NO_OPTION);
				if (i == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});
	}

	// ʵ���û������߳�
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
						// �����û�״̬��Ϣ
						processUserStateMessage((UserStateMessage) message);

					} else if (message instanceof ChatMessage) {
						// ����������Ϣ
						processChatMessage((ChatMessage) message);

					} else if (message instanceof OriginalStateMessage) {
						// �����¼��ע����Ϣ
						processOriginalMessage((OriginalStateMessage) message);
					} else if (message instanceof FileStateMessage) {
						//�����ļ����ͺͽ���������Ϣ
						processFileStateMessage((FileStateMessage)message);
					}else if (message instanceof ImageMessage) {  //����ͼƬ��Ϣ
						processImageMessage((ImageMessage) message);
					}else {
						System.out.println("��Ϣ��ʽ����");
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
		 * ����ͼƬ��Ϣ
		 * @param message
		 */
		private void processImageMessage(ImageMessage message) {
			String srcName = message.getSrcUser();
			String dstName = message.getDstUser();
			ImageIcon imageIcon = message.getImageIcon();
			if (userManager.hasUser(srcName)) { // ������û����û��б���
				if (message.isPublicMessage()) {
					// ��������Ϣ���͸����������û�
					String msg = dateFormat.format(new Date()) + " " + srcName + "���ҷ���Ƭ��\r\n";
					FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.black, 12, false, false);
					StyledDocument doc = textPaneMsgRecord.getStyledDocument();
					textPaneMsgRecord.setCaretPosition(doc.getLength()); // ���ò���λ��
					textPaneMsgRecord.insertIcon(imageIcon);
					try {
						doc.insertString(doc.getLength(), "\r\n", null);
					} catch (BadLocationException e1) {
						e1.printStackTrace();
					}
					tranferMsgToOthers(message);
				} else {
					// ��˽����Ƭ��Ϣ���͸��û�
					String msg = dateFormat.format(new Date()) + " " + srcName + "��" + dstName + "����Ƭ��\r\n";
					FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.black, 12, false, false);
					StyledDocument doc = textPaneMsgRecord.getStyledDocument();
					textPaneMsgRecord.setCaretPosition(doc.getLength()); // ���ò���λ��
					textPaneMsgRecord.insertIcon(imageIcon);
					try {
						doc.insertString(doc.getLength(), "\r\n", null);
					} catch (BadLocationException e1) {
						e1.printStackTrace();
					}
					// ������Ϣ���͸�Ŀ���û�
					tranferMsgToDst(message);
				}
			} else {
				// ���������Ӧ���û�δ����������Ϣ��ֱ�ӷ�����������Ϣ��Ӧ�÷���Ϣ��ʾ�ͻ���
				System.err.println("����δ����������Ϣ��ֱ�ӷ�����������Ϣ");
				return;
			}
		}

		/**
		 * �����ļ�����������Ϣ���ļ�����������Ϣ
		 * 
		 * @param message
		 */
		private void processFileStateMessage(FileStateMessage message) {
			String srcName = message.getSrcUser();
			String dstName = message.getDstUser();
			FileBean fileBean = message.getFileBean();
			String fileName = fileBean.getFileName();
			if (message.isRequest()) { // ���ļ�����������Ϣ
				String msg = dateFormat.format(new Date()) + " " + srcName + "������" + dstName + "�����ļ���" + fileName
						+ "\r\n";
				FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.black, 12, false, false);
				tranferMsgToDst(message);
			} else if (message.isResponse()) { // ���ļ�������Ӧ��Ϣ
				if (message.isState()) { // ������ͬ����ո��ļ�
					String msg = dateFormat.format(new Date()) + " " + dstName + "������" + srcName + "�����ļ���" + fileName + "������"
							+ "\r\n";
					FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.black, 12, false, false);
					fileBean.setAddress(userManager.getSocket(dstName).getInetAddress());
					// ������Ϣ���͸������ߣ�������ʼ���ļ�
					tranferFileMsgToSrc(message);

				} else { // �����߾ܾ����ո��ļ�
					String msg = dateFormat.format(new Date()) + " " + dstName + "�ܾ�" + srcName + "�����ļ���" + fileName + "������"
							+ "\r\n";
					FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.black, 12, false, false);
					//�ѽ����߾ܾ������ļ�����Ϣ���͸�������
					tranferFileMsgToSrc(message);
				}
			} else {
				System.out.println("����Ϣ�����ļ���Ϣ����");
				return;
			}
		}

		/**
		 * ���ļ���Ӧ��Ϣ���͸�������
		 * 
		 * @param message
		 */
		private void tranferFileMsgToSrc(FileStateMessage message) {
			String srcUsers = message.getSrcUser();
			// ������ڸ�Ŀ���û���������Ϣ������ͼ�����
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
				System.out.println("�����ˣ������ڸ��û���");
			}
		}

		/**
		 * �����û�ע��͵�½��Ϣ
		 * 
		 * @param message
		 */
		private void processOriginalMessage(OriginalStateMessage message) {
			String srcName = message.getSrcUser();
			UserBean userBean = message.getUserBean();
		
			// ��ø��û���ע����ߵ�¼��Ϣ
			String userName = userBean.getUserName();
			String userPwd = userBean.getPassword();
			String sex = userBean.getSex();
			String telPhone = userBean.getTelPhone();
			String mail = userBean.getMailBox();

			if (message.isRegistMessage()) {
				String msg = dateFormat.format(new Date()) + " �յ���" + srcName + "�������û�ע����Ϣ\r\n";
				FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.black, 12, false, false);

				// ���ע��ɹ���������Ϣ��־�м��ز���������ظ��ͻ���
				if (userDatabase.insertUser(userName, userPwd, sex, telPhone, mail)) {
					String msg1 = dateFormat.format(new Date()) + " �û�" + srcName + "ע��ɹ�\r\n";
					FontStyle.addMsgRecord(textPaneMsgRecord, msg1, Color.black, 12, false, false);
					// ע��ɹ������ɹ���־λ����Ϊtrue
					message.setSucceed(true);
					message.setRepeat(false);
					// ������Ϣ���͸�ע���ˣ�����ע��ɹ�
					tranferMsgToSrc(message);
				} else {
					// ����־�ļ�����˵���û�ע��ʧ��
					String msg1 = dateFormat.format(new Date()) + " ע��ʧ��\r\n";
					FontStyle.addMsgRecord(textPaneMsgRecord, msg1, Color.black, 12, false, false);
					// ���ע��ʧ�ܣ��򽫱�־λ����Ϊfalse
					message.setSucceed(false);
					message.setRepeat(false);
					// ������Ϣ���͸�ע���ˣ�����ע��ʧ��
					tranferMsgToSrc(message);
				}
			} else if (message.isLoginMessage()) { // �����¼��Ϣ
				if (userManager.hasUser(srcName)) { // ����û��ظ���¼
					String msg = dateFormat.format(new Date()) + " " + srcName + " �ظ���¼��" + "\r\n";
					FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.GREEN, 12, false, false);
					
					message.setSucceed(false);
					message.setRepeat(true);
					// ������Ϣ���͸���¼�ˣ�������¼ʧ��
					tranferMsgToSrc(message);
				} else if (userDatabase.checkUserPassword(userName, userPwd)) { // ���ݿ��и��û�����������ƥ��
					String msg = dateFormat.format(new Date()) + " " + srcName + " ��¼�ˣ�" + "\r\n";
					FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.GREEN, 12, false, false);
					message.setSucceed(true);
					message.setRepeat(false);
					tranferMsgToSrc(message);

				} else {
					String msg = dateFormat.format(new Date()) + " " + srcName + " ��¼ʧ�ܣ�" + "\r\n";
					FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.GREEN, 12, false, false);
					// ���ע��ʧ�ܣ��򽫱�־λ����Ϊfalse
					message.setSucceed(false);
					message.setRepeat(false);
					// ������Ϣ���͸�ע���ˣ�����ע��ʧ��
					tranferMsgToSrc(message);
				}
			} else if (message.isModifyPreMessage()) {  //�����û���Ϣ
				String msg = dateFormat.format(new Date()) + " " + srcName + " Ҫ���޸���Ϣ��" + "\r\n";
				FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.GREEN, 12, false, false);
				userBean = userDatabase.getUserMesssage(userBean.getUserName());
				
				if (userBean != null) {    //��������ݿ��������򽫸����ݷ��ظ��ͻ���
					message.setUserBean(userBean);
					message.setSucceed(true);
					message.setRepeat(false);
					tranferMsgToSrc(message);
				}else {   //�����ɹ���Ϣ
					message.setSucceed(false);    
					message.setRepeat(false);
					tranferMsgToSrc(message);
					System.out.println("�����޸���Ϣ���󱻻ؾ�!");
				}
				
			} else if (message.isModifyMessage()) {  //�����û�������Ϣ��Ϣ
				if(userDatabase.UpdateUser(userBean,srcName)) {   //�����ݿ��и��¸��û���Ϣ
					message.setSucceed(true);
					message.setRepeat(false);
					tranferMsgToSrc(message);
					String msg = dateFormat.format(new Date()) + " " + srcName + " �޸���Ϣ�ɹ���" + "\r\n";
					FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.GREEN, 12, false, false);
				}else {
					message.setSucceed(false);
					message.setRepeat(false);
					tranferMsgToSrc(message);
					String msg = dateFormat.format(new Date()) + " " + srcName + " �޸���Ϣʧ�ܣ�" + "\r\n";
					FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.GREEN, 12, false, false);
				}
			}else {
				System.out.println("�����ڸ���Ϣ��ʽ��");
				return;
			}
		}

		/**
		 * ���û�ע����ߵ�½�Ƿ�ɹ�����Ϣ���͸�ע����
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
		 * �����û�������Ϣ
		 * 
		 * @param message
		 */
		private void processChatMessage(ChatMessage message) {
			String srcName = message.getSrcUser();
			String dstName = message.getDstUser();
			String msgContent = message.getMsgContent();
			if (userManager.hasUser(srcName)) { // ������û����û��б���
				if (message.isPublicMessage()) {
					// ��������Ϣ���͸����������û�
					String msg = dateFormat.format(new Date()) + " " + srcName + "����˵��" + msgContent + "\r\n";
					FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.black, 12, false, false);
					tranferMsgToOthers(message);
				} else {
					// ��˽����Ϣ���͸��û�
					String msg = dateFormat.format(new Date()) + " " + srcName + "��" + dstName + "����Ϣ��" + msgContent
							+ "\r\n";
					FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.black, 12, false, false);
					// ������Ϣ���͸�Ŀ���û�
					tranferMsgToDst(message);
				}
			} else {
				// ���������Ӧ���û�δ����������Ϣ��ֱ�ӷ�����������Ϣ��Ӧ�÷���Ϣ��ʾ�ͻ���
				System.err.println("����δ����������Ϣ��ֱ�ӷ�����������Ϣ");
				return;
			}
		}

		/**
		 * ����˽����Ϣ���͸�Ŀ���û�
		 * 
		 * @param message
		 */
		private void tranferMsgToDst(Message message) {
			String dstUser = message.getDstUser();
			// ������ڸ�Ŀ���û���������Ϣ������ͼ�����
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
				System.out.println("�����ˣ������ڸ��û���");
			}
		}

		/**
		 * �����û�״̬��Ϣ
		 * 
		 * @param message
		 */
		public void processUserStateMessage(UserStateMessage message) {
			String srcUser = message.getSrcUser();
			if (message.isUserOffline()) { // �û�������
				if (userManager.hasUser(srcUser)) {
					// ����ɫ���ڡ���Ϣ��¼���ı�������ʾ�û�������Ϣ������ʱ��
					String msg = dateFormat.format(new Date()) + " " + srcUser + " �˳���" + "\r\n";
					FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.GREEN, 12, false, false);
					// System.out.println(srcUser + "������");
					userManager.removeUser(srcUser);
					for (int i = 0; i < onLineUsersDtm.getRowCount(); i++) {
						if (onLineUsersDtm.getValueAt(i, 0).equals(srcUser)) {
							// �ڡ������û��б���ɾ�������û�
							onLineUsersDtm.removeRow(i);
						}
					}
					tranferMsgToOthers(message);
				} else {
					System.out.println("�յ���������Ϣ��");
				}
			} else if (message.isUserOnline()) { // �����û�������Ϣ
				if (userManager.hasUser(srcUser)) {
					String msg = dateFormat.format(new Date()) + " " + srcUser + " �ظ���¼����" + "\r\n";
					FontStyle.addMsgRecord(textPaneMsgRecord, msg, Color.GREEN, 12, false, false);
					System.out.println(srcUser + "�ظ���¼��");
					return;
				}

				System.out.println("�����û�����");
				// �������ߵ��û�ת�������û�����Ϣ
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

				// �������û�ת�����û�����Ϣ
				tranferMsgToOthers(message);
				userManager.addUser(srcUser, currentSocket, ois, oos);
				// �������ߵ��û���Ϣ���뵽�����û��б���
				onLineUsersDtm.addRow(new Object[] { srcUser, currentSocket.getInetAddress().getHostAddress(),
						currentSocket.getPort(), dateFormat.format(new Date()) });
			}
		}

		/**
		 * ���û�������Ϣת�����������������û�
		 */
		private void tranferMsgToOthers(Message msg) {
			String[] users = userManager.getAllUser();
			for (String user : users) {
				// �������û�������˷�����Ϣ
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

// �����û���Ϣ�Ĺ�����
class UserManager {
	// ��������û����û�����User��������
	private Map<String, User> onLineUser = new HashMap<>();

	// �ж�һ���û��Ƿ�����
	public boolean hasUser(String userName) {
		return onLineUser.containsKey(userName);
	}

	// �ж��û��б��Ƿ�Ϊ��
	public boolean isEmpty() {
		return onLineUser.isEmpty();
	}

	// ��ȡ�����û��Ķ��������
	public ObjectOutputStream getUseroos(String userName) {
		if (hasUser(userName)) {
			return onLineUser.get(userName).getOos();
		}
		return null;
	}

	// ��ȡ�����û��Ķ���������
	public ObjectInputStream getUserois(String userName) {
		if (hasUser(userName)) {
			return onLineUser.get(userName).getOis();
		}
		return null;
	}

	// ��ȡ�����û���Socket����
	public Socket getSocket(String userName) {
		if (hasUser(userName)) {
			return onLineUser.get(userName).getSocket();
		}
		return null;
	}

	// ��ӡ�ɾ������Ѱ������
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

	// ��ȡ��ǰ�����û��б�
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

	// ���ص��������û�����
	public int getOnlineUser() {
		return onLineUser.size();
	}
}

// һ��user�������������ϵ�һ�������û�
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
