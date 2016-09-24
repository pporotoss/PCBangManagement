package teamproject.pc.client.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/*
	※ 메시지 실시간 청취를 위한 스레드 정의!!!
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
	
	
	// 서버에서 듣기.
	public void listen(){
		String msg = null;
		while(flag_listen){
			try {
				msg = buffr.readLine();
				// 서버에서 받은 내용 TextArea에 출력.
				managerClient.area.append(msg+"\n");
				
				if(!msg.startsWith("manager")){	// 입력받은 메시지가 manager가 입력한게 아니면,
					String[] client = msg.split(" : ");
					
					if(client[1].startsWith("@@chatStart@@")){	// 입력받은 메시지가 @@chatStart@@이면,
						
						// 매니저 채팅창 띄우기.
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
					}else if(client[1].startsWith("@@chat@@")){// 입력받은게 채팅메시지이면,
						for(int i = 0; i < chatList.size(); i++){
							ManagerChat managerChat = chatList.get(i);
							if(client[0].equals(managerChat.getClientId())){
								String appendMsg = client[1].substring(client[1].indexOf("@@chat@@")+8);
								managerChat.appendChat(appendMsg);
							}
						}
					}else if(client[1].startsWith("@@chatEnd@@")){	// 채팅종료 메시지
						for(int i = 0; i < chatList.size(); i++){
							ManagerChat managerChat = chatList.get(i);
							if(client[0].equals(managerChat.getClientId())){								
								managerChat.appendChat("님이 채팅을 종료하셨습니다.");
							}
						}
					}else if(client[1].startsWith("@@login@@")){// 입력받은게 로그인메시지이면,
						String user_id = client[1].substring(client[1].indexOf("@@login@@")+9);
						if(client[0].equals("70.12.109.53")){// 첫번째 자리
							
							managerClient.mainServer.getMainWindow().init(0,user_id);
							
						}else if(client[0].equals("70.12.109.54")){	// 두번째 자리							
							
							managerClient.mainServer.getMainWindow().init(1,user_id);
							
						}else if(client[0].equals("70.12.109.61")){	// 세번째 자리
							
							managerClient.mainServer.getMainWindow().init(2,user_id);
							
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
