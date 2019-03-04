package com.chatTest.Jframe;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.chatTest.bean.RegistBean;

import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JButton;

public class ManagerUsers extends JFrame {
	
	private static final long serialVersionUID = -1568605338655431353L;
	private JPanel contentPane;
	public static DefaultTableModel tm = new DefaultTableModel(new String[] {"用户名","性别","口令哈希值","盐值","注册时间"}, 0);
	private JPanel panel;
	private JLabel labelTitle;
	private JScrollPane scrollPane;
	private JPanel panel_1;
	private JButton btnBack;
	private JTable tableUserMsg;
	private ServerFrame serverFrame;

	/**
	 * Create the frame.
	 */
	@SuppressWarnings("serial")
	public ManagerUsers(ServerFrame serverFrame) {
		this.serverFrame = serverFrame;
		setTitle("\u670D\u52A1\u5668");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 521, 322);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		
		labelTitle = new JLabel("\u7528\u6237\u4FE1\u606F\u7BA1\u7406");
		labelTitle.setFont(new Font("楷体", Font.BOLD, 18));
		labelTitle.setForeground(Color.RED);
		panel.add(labelTitle);
		
		scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		tableUserMsg = new JTable(tm) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		scrollPane.setViewportView(tableUserMsg);
		
		tableUserMsg.addMouseListener(new RightClick());
		
		
		panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.SOUTH);
		
		btnBack = new JButton("\u8FD4\u56DE");
		btnBack.addActionListener( e ->{
			serverFrame.setVisible(true);
			this.setVisible(false);
			
		});
		btnBack.setFont(new Font("楷体", Font.BOLD, 15));
		panel_1.add(btnBack);
		
	}
	
	/**
	 * 定义鼠标右击监听器
	 * 用来处理对用户信息的删除和修改
	 * @author jpgong
	 *
	 */
	class RightClick extends MouseAdapter{
		@Override
		public void mousePressed(MouseEvent e) {
			if (SwingUtilities.isRightMouseButton(e)) {   //如果是右击时间
				int row = tableUserMsg.rowAtPoint(e.getPoint());
				if (row != -1) {  //存在选中行

					final JPopupMenu menu = new JPopupMenu();
					JMenuItem deleteItem = new JMenuItem("删除");
					//定义删除操作的响应事件
					deleteItem.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							//从数据库中删除该用户
							String userName = (String) tm.getValueAt(row, 0);
							tm.removeRow(row);;  //删除行
							if (serverFrame.userDatabase.deleteUser(userName)) {
								JOptionPane.showMessageDialog(ManagerUsers.this, "用户" + userName + "已被删除！");
							}else {
								JOptionPane.showMessageDialog(ManagerUsers.this, "该操作失败，请稍后再试！");
							}
						}
					});
					menu.add(deleteItem);
					menu.add(new JSeparator());
					menu.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		}
	}
	
	public void showUsersMessage() {
		tm.setRowCount(0);   //重新加载Jtable中的数据之前，需要先清空以前的数据
		List<RegistBean> beans = serverFrame.userDatabase.getAllUsers();
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				for (RegistBean registBean : beans) {
					// 如果不是更新消息，则直接将消息显示在图形界面即可
					ManagerUsers.tm.addRow(new String[] { registBean.getUserName(),registBean.getSex(),registBean.getPassword(),
							registBean.getSalt(),registBean.getCreateDate()});
				}
			}
		});
	}

}
