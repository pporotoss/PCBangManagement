package teamproject.pc.main.capture;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ImgServer extends JFrame{
	JPanel p_north;
	JTextField t_port;
	JButton btn;
	JTextArea area;
	JScrollPane scroll;
	ServerSocket server;
	ArrayList<ImgServerThread> clientList = new ArrayList<ImgServerThread>();
	boolean startCon = true;
	boolean frameOn = false;
	TestFrame test;
		
	public ImgServer() {
		setTitle("ChatServer");
		p_north = new JPanel();
		t_port = new JTextField("8888",15);
		btn = new JButton("서버시작!!");
		area = new JTextArea();
		scroll = new JScrollPane(area);
		area.setEditable(false);
		
		startServer();
		p_north.add(t_port);
		p_north.add(btn);
		
		add(p_north, BorderLayout.NORTH);
		add(scroll);
		
		setBounds(800,300,300,400);
		setVisible(false);
	}
	
	public void startConn(){
		int port = Integer.parseInt(t_port.getText());		
		try{
			if(server == null){
				server = new ServerSocket(port);
				area.append("서버시작!!\n");
			}
			if(!frameOn){
				test = new TestFrame(this);
				frameOn = true;
			}
			while(startCon){
				Socket client = null;
				try {
					client = server.accept();
				} catch (Exception e) {
					return;					
				}
				area.append("ip : "+client.getInetAddress().getHostAddress()+" 접속\n");
				ImgServerThread st = new ImgServerThread(this, client, test);
				st.start();
				//clientList.add(st);
				area.append("총 누적 접속자 수 : "+clientList.size()+"명\n");
			}//while			
		} catch (IOException e) {
			closeServer();
			e.printStackTrace();
		}
	}
	
	public void startServer(){
		Thread th_conn = new Thread(new Runnable(){
			public void run() {
				startConn();
			}
		});
		th_conn.start();
	}
	
	public void closeServer(){
		startCon = false;
		try {
			server.close();			
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.dispose();
	}
	
	public static void main(String[] args) {
		new ImgServer();
	}
}
