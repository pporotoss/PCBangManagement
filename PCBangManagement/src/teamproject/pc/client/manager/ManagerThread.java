package teamproject.pc.client.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import teamproject.pc.main.MainWindow;

/*
	�� �޽��� �ǽð� û�븦 ���� ������ ����!!!
 */

public class ManagerThread extends Thread{
	ManagerClient managerClient;
	BufferedReader buffr;
	boolean flag_listen = true;
	ArrayList<ManagerChat> chatList = new ArrayList<ManagerChat>();
	
	
	public ManagerThread(ManagerClient managerClient) {
		this.managerClient = managerClient;
		try {
			buffr = new BufferedReader(new InputStreamReader(managerClient.client.getInputStream()));
			
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
				managerClient.area.append(msg+"\n");
				
				if(!msg.startsWith("manager")){	// �Է¹��� �޽����� manager�� �Է��Ѱ� �ƴϸ�,
					String[] client = msg.split(" : ");
					
					if(client[1].startsWith("@@chatStart@@")){	// �Է¹��� �޽����� @@chatStart@@�̸�,
						
						// �Ŵ��� ä��â ����.
						if(chatList.size() != 0){
							for(int i = 0; i < chatList.size(); i++){
								ManagerChat oldChat = chatList.get(i);
								String oldId = oldChat.getClientId();
								if(!client[0].equals(oldId)){
									ManagerChat managerChat = new ManagerChat(managerClient, client[0]);
									chatList.add(managerChat);
								}							
							}// for
						}else{							
							ManagerChat managerChat = new ManagerChat(managerClient, client[0]);
							chatList.add(managerChat);
						}
					}else if(client[1].startsWith("@@chat@@")){// �Է¹����� ä�ø޽����̸�,
						for(int i = 0; i < chatList.size(); i++){
							ManagerChat managerChat = chatList.get(i);
							if(client[0].equals(managerChat.getClientId())){
								String appendMsg = client[1].substring(client[1].indexOf("@@chat@@")+8);
								managerChat.appendChat(appendMsg);
							}
						}
					}else if(client[1].startsWith("@@chatEnd@@")){	// ä������ �޽���
						for(int i = 0; i < chatList.size(); i++){
							ManagerChat managerChat = chatList.get(i);
							if(client[0].equals(managerChat.getClientId())){								
								managerChat.appendChat("���� ä���� �����ϼ̽��ϴ�.");
							}
						}
					}else if(client[1].startsWith("@@login@@")){// �Է¹����� �α��θ޽����̸�,
						String user_id = client[1].substring(client[1].indexOf("@@login@@")+9);
						
						for(int i = 0; i < MainWindow.ipList.length; i++){
							
							if(client[0].equals(MainWindow.ipList[i])){
								managerClient.mainServer.getMainWindow().init(i, user_id);	// �α����� �ڸ� ����.
							}
							
						}
					}
					
					
					
				}
			} catch (IOException e) {
				flag_listen = false;
				try {
					managerClient.client.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	public void run() {
		listen();
	}
}
