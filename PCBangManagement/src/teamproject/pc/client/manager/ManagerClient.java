package teamproject.pc.client.manager;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import teamproject.pc.client.db.ConnectionManager;
import teamproject.pc.server.MainServer;

public class ManagerClient extends JFrame{
	JTextArea area;
	JScrollPane scroll;
	JTextField t_input;
		
	// 네트워크 관련 정보 
	Socket client;
	String ip = ConnectionManager.CON_ADDR;	// 접속할 서버 IP
	int port = 7777;	// 접속할 서버 포트
	BufferedWriter buffw;
	ManagerThread clientThread;
	MainServer mainServer;
	String id;
	
	
	
	public ManagerClient(String id, MainServer mainServer) {
		super("ID : "+id);	// 현재 창 타이틀 세팅.
		this.id = id;	// 현재 채팅하는 ID 
		this.mainServer = mainServer;
		
		area = new JTextArea();
		scroll = new JScrollPane(area);
		t_input = new JTextField(20);
		
		
		// 이벤트 구현
		t_input.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					String msg = t_input.getText();	// 키보드 엔터 치면,
					sendMsg(msg);
					t_input.setText("");
				}
			}
		});
		
		add(scroll);
		add(t_input, BorderLayout.SOUTH);
		setBounds(450, 150, 300, 400);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
		//채팅서버에 접속!!
		connect();
	}
	
	public void connect(){
		try {
			client = new Socket(ip, port);	// 해당 ip와 port로 접속시도!!
			// 접속되면 통로 연결!!
			buffw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			
			// 서버로부터 청취전용 스레드 생성.
			clientThread = new ManagerThread(this);
			clientThread.start();
		} catch (UnknownHostException e) {			
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}	 
	
	public void sendMsg(String msg){
		
		try {
			// manager : 메시지
			buffw.write(id+" : "+msg);
			buffw.newLine();
			buffw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
}// class
