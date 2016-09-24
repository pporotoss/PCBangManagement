package teamproject.pc.client.capture;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import teamproject.pc.client.db.ConnectionManager;

public class ImgClient extends JFrame{
	JPanel p_north;
	JTextField t_port, t_input;
	JButton btn;
	JTextArea area;
	JScrollPane scroll;
	Socket client;
	ImgClientThread clientThread;
	boolean startCon = true;
	
	public ImgClient() {
		//setTitle(id);
		p_north = new JPanel();
		t_port = new JTextField("8888",15);
		btn = new JButton("접속");
		area = new JTextArea();
		scroll = new JScrollPane(area);
		t_input = new JTextField();
		
		// 이벤트 구현
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				connect();
			}
		});
		
		// 윈도우 이벤트
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				close();				
			}
		});
		
		
		p_north.add(t_port);
		p_north.add(btn);
		
		add(p_north, BorderLayout.NORTH);
		add(scroll);
		add(t_input, BorderLayout.SOUTH);
		
		setBounds(700,300,300,400);
		setVisible(false);
		connect();
	}
	
	public void connect(){
		if(startCon){
			int port = Integer.parseInt(t_port.getText());
			String ip = ConnectionManager.CON_ADDR;
			try {
				client = new Socket(ip, port);	// 서버의 서버소켓에 연결.
				// 접속과 동시에 청취 시작!!
				clientThread = new ImgClientThread(this);
				clientThread.start();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				clientThread.writing = false;
				this.dispose();			
				//e.printStackTrace();
			}
		}
	}
	
	public void close(){
		startCon = false;
		clientThread.writing = false;
		this.dispose();
	}
	
	public static void main(String[] args) {
		new ImgClient();
	}
}
