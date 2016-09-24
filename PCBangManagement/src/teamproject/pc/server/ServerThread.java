package teamproject.pc.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerThread extends Thread{
	MainServer mainServer;
	BufferedReader buffr;
	BufferedWriter buffw;
	int threadCount;
	boolean isStop = false;
	String clientIp;
	
	public ServerThread(MainServer mainServer, Socket client, int threadCount) {
		this.mainServer = mainServer;
		this.threadCount = threadCount;
		try {
			buffr = new BufferedReader(new InputStreamReader(client.getInputStream()));
			buffw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		clientIp = client.getInetAddress().getHostAddress();
	}// constructor
	
	public void listen(){
		String msg = null;
		while(!isStop){
			try {
				msg = buffr.readLine();
				if(msg.startsWith("manager")){	// �Ŵ����� �Է��ϸ� ��ü���� �� �Ѹ�.
					for(ServerThread st : mainServer.clientList){
						st.sendMsg(msg);						
					}
				}else{
					for(ServerThread st : mainServer.clientList){	// �Ŵ����ϰ� �����׸� �޽��� �Ѹ�.
						if(st.getThreadCount() == 1 || st.getThreadCount() == this.threadCount){
							st.sendMsg(msg);
						}
					}
				}
			} catch (IOException e) {
				// ���� �����尡 �ð� �ִ� Ŭ���̾�Ʈ�� �����ϸ�,
				isStop = true;
								
				mainServer.area.append(clientIp+" : ����.\n");
				if(buffr != null){
					try {
						buffr.close();					
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				if(buffw != null){
					try {
						buffw.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				mainServer.clientList.remove(this);// ���� �����带 ����Ʈ���� ����.
			}
		}
	}
	
	public void sendMsg(String msg){
		try {
			buffw.write(msg);
			buffw.newLine();
			buffw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getThreadCount(){
		return threadCount;
	}
	
	public void run() {
		listen();
	}
}
