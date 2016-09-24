package teamproject.pc.client.client;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import teamproject.pc.client.db.ConnectionManager;

public class ChatClient extends JFrame{
	JTextArea area;
	JScrollPane scroll;
	JTextField t_input;
		
	// ��Ʈ��ũ ���� ���� 
	Socket client;
	String ip = ConnectionManager.CON_ADDR;	// ������ ���� IP
	int port = 7777;	// ������ ���� ��Ʈ
	BufferedWriter buffw;
	ClientThread clientThread;
	ClientLoginForm loginForm;
	String id;
	String user_id;
		
	public ChatClient(String user_id, ClientLoginForm loginForm) {
		this.loginForm = loginForm;
		this.user_id = user_id;
		try {
			id = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		setTitle(id);
		
		area = new JTextArea();
		scroll = new JScrollPane(area);
		t_input = new JTextField(20);
		
		
		// �̺�Ʈ ����
		t_input.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					String msg = t_input.getText();	// Ű���� ���� ġ��,
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
		
		//ä�ü����� ����!!
		connect();
		
	}
	
	public void connect(){
		try {
			client = new Socket(ip, port);	// �ش� ip�� port�� ���ӽõ�!!
			// ���ӵǸ� ��� ����!!
			buffw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			
			// �����κ��� û������ ������ ����.
			clientThread = new ClientThread(this);
			clientThread.start();
			
			// ���� �����ϸ� �α��� �޽��� ����
			sendMsg("@@login@@"+user_id);
			
		} catch (UnknownHostException e) {			
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	public void sendMsg(String msg){
		
		try {
			buffw.write(id+" : "+msg);
			buffw.newLine();
			buffw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void close(){
		dispose();
	}
	
	public String getId(){		
		return id;
	}
}
