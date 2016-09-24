package teamproject.pc.client.client;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

public class ChatWindow extends JFrame{
	JScrollPane scroll;
	JTextArea area;
	JTextField t_chat;
	String clientId;
	ChatClient chatClient;
	String myId;
	
	
	public ChatWindow(ChatClient chatClient, String clientId) {
		setTitle("채팅창");
		
		this.chatClient = chatClient;
		this.clientId = clientId;
		myId = chatClient.getId();		
				
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
					area.append(myId+" : "+msg+"\n");
					chatClient.sendMsg("@@chat@@"+msg);
					t_chat.setText("");
				}
			}
		});
		
		// 윈도우이벤트
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int sel = JOptionPane.showConfirmDialog(getParent(), "채팅을 종료하시겠습니까?", "종료", JOptionPane.OK_CANCEL_OPTION);
				if(sel == JOptionPane.OK_OPTION){
					closeWin();
				}
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
		chatClient.sendMsg("@@chatEnd@@");
		chatClient.clientThread.setChatWinON(false);
		this.dispose();
	}
}
