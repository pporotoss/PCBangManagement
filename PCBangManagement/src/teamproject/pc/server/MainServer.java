package teamproject.pc.server;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import teamproject.pc.client.manager.ManagerClient;
import teamproject.pc.main.MainWindow;

public class MainServer extends JFrame{
	JPanel p_north;
	JTextField t_port;
	JButton bt_conn;
	JTextArea area;
	JScrollPane scroll;
	ServerSocket server;
	MainWindow mainWindow;
	ManagerClient manager;
	ArrayList<ServerThread> clientList = new ArrayList<ServerThread>();
	int port;
	int threadCount = 0;
	
	
	public MainServer() {
		setTitle("MainServer");		
		
		p_north = new JPanel();
		t_port = new JTextField("7777",10);
		bt_conn = new JButton("서버가동");
		area = new JTextArea();
		scroll = new JScrollPane(area);
		
		area.setEditable(false);		
		// 버튼 이벤트
		bt_conn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {	// 숫자만 입력 여부 검사.
					port = Integer.parseInt(t_port.getText());
				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(getParent(), "포트번호는 숫자만 입력해주세요.");
					t_port.setText("");
					return;
				}
				threadStart();
				bt_conn.setEnabled(false);
				openMainWindow();
			}
		});
		
		p_north.add(t_port);
		p_north.add(bt_conn);
		
		add(p_north, BorderLayout.NORTH);
		add(scroll);
		
		setSize(300,400);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void startServer(){
		try {
			server =  new ServerSocket(port);
			String serverIp = InetAddress.getLocalHost().getHostAddress();
			area.append("IP : "+serverIp+" / PORT : "+port+" 서버가동 시작!!\n");
			manager = new ManagerClient("manager", this);
			while(true){
				Socket client = server.accept();
				String clientIp = client.getInetAddress().getHostAddress();
				area.append(clientIp+" : 접속\n");
				threadCount++;
				ServerThread st = new ServerThread(this, client, threadCount);
				clientList.add(st);
				st.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void openMainWindow(){
		mainWindow = new MainWindow(this);
	}
	
	public MainWindow getMainWindow(){
		return mainWindow;
	}
	
	public ManagerClient getManager(){
		
		return manager;
	}
	
	public void threadStart(){
		Thread thread = new Thread(){
			public void run() {
				startServer();
			}
		};
		thread.start();
	}	
}
