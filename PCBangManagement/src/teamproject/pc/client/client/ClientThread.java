package teamproject.pc.client.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

import teamproject.pc.client.capture.ImgClient;
import teamproject.pc.client.dao.UserDAO;
import teamproject.pc.client.dto.UserDTO;

/*
	�� �޽��� �ǽð� û�븦 ���� ������ ����!!!
 */

public class ClientThread extends Thread{
	ChatClient chatClient;
	BufferedReader buffr;
	boolean flag_listen = true;
	ChatWindow chatWindow;
	boolean chatWinOn = false;
	String myIp;
	
	
	public ClientThread(ChatClient chatClient) {
		this.chatClient = chatClient;
		try {
			myIp = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		
		try {
			buffr = new BufferedReader(new InputStreamReader(chatClient.client.getInputStream()));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	// �������� ���.
	public void listen(){
		String msg = null;
		while(flag_listen){
			try {
				msg = buffr.readLine();
				// �������� ���� ���� TextArea�� ���.
				chatClient.area.append(msg+"\n");
				String[] manager = msg.split(" : ");
				if(manager[1].startsWith("@@myChatStart@@")){	// ä��â �ѱ�
					if(!chatWinOn){	// �����ִ� ä��â�� ������,
						chatWindow = new ChatWindow(chatClient, "manager");
						chatWinOn = true;
					}
				}else if(manager[0].equals("manager")){	// ���� �޽����� �Ŵ����� ���� �޽�����,
					if(manager[1].startsWith("@@chat@@"+myIp)){	// ä�� �޽����̸�,
						String appendMsg = manager[1].substring(manager[1].indexOf("@@chat@@")+8+chatClient.getId().length());						
						chatWindow.appendChat(appendMsg);
					}else if(manager[1].startsWith("@@shutDown@@"+myIp)){		//���� �޽����̸�, 
						//������ ����!
						UserDAO dao = new UserDAO();
						String id = chatClient.loginForm.getSubForm().getId();
						chatClient.loginForm.getSubForm().setTimeOn(false);
						dao.priceUpdate(id, 0, "","false");// ���� �ʱ�ȭ.
						
						/*try {
							Process p = Runtime.getRuntime().exec("shutdown -s -t 2");
						} catch (IOException e1) {
							e1.printStackTrace();
						}*/
					}else if(manager[1].startsWith("@@capture@@"+myIp)){	// ĸ�� �޽����̸�,
						new ImgClient();
					}else if(manager[1].startsWith("@@update@@"+myIp)){
						UserDAO dao = new UserDAO();
						String id = chatClient.loginForm.getSubForm().getId();
						int price = dao.getPrice(id);
						System.out.println(price);
						UserDTO dto = new UserDTO();						
						dto.setId(id);
						dto.setPrice(price);
						chatClient.loginForm.getSubForm().updateContent(dto);
					}				
				}
			} catch (IOException e) {
				flag_listen = false;
				try {
					chatClient.client.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	public void setChatWinON(boolean chatWinOn){
		this.chatWinOn = chatWinOn;
	}
	
	public void run() {
		listen();
	}
}
