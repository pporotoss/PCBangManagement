package teamproject.pc.client.manager;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

public class ManagerChat extends JFrame{
	JScrollPane scroll;
	JTextArea area;
	JTextField t_chat;
	String clientId;
	ManagerClient managerClient;
	
	public ManagerChat(ManagerClient managerClient, String clientId) {
		this.clientId = clientId;
		this.managerClient = managerClient;
		
		setTitle("매니저 채팅창 : "+clientId);
		area = new JTextArea();
		DefaultCaret caret = (DefaultCaret)area.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		scroll = new JScrollPane(area);
		t_chat =  new JTextField();
		
		// 이벤트구현
		t_chat.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					String msg = t_chat.getText();
					area.append("manager : "+msg+"\n");
					managerClient.sendMsg("@@chat@@"+clientId+msg);
					t_chat.setText("");
				}
			}
		});
		//윈도우 이벤트
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				closeWin();
			}
		});
		
		add(scroll);
		add(t_chat, BorderLayout.SOUTH);
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setSize(300,500);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void appendChat(String msg){
		area.append(clientId+" : "+msg+"\n");
	}
	
	public String getClientId(){
		return clientId;
	}
	
	public void closeWin(){
		managerClient.clientThread.chatList.remove(this);
		this.dispose();		
	}
}
