package teamproject.pc.client.client;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import teamproject.pc.client.dao.UserDAO;
import teamproject.pc.client.dto.UserDTO;

public class ClientSubForm extends JFrame{
	JPanel p_center;
	JLabel la[] = new JLabel[4];
	JTextField txt[]=new JTextField[4];
	String[] la_str={"ID","시작시간","이용시간","이용요금"};
	JButton bt_chat;
	UserDTO dto;
	UserDAO dao;
	Thread timeThread;
	int countMinute=0; //exit누르면 초기화해줘야함
	int countSecond=0;
	int priceCount=100;
	
	int usePrice;
	int defaultPrice=1000; //기본요금
	
	ChatClient chatClient;	
	String id;
	
	boolean timeOn = true;
	
	public ClientSubForm(UserDTO dto, ChatClient chatClient){
		
		this.chatClient = chatClient;
				
		this.dto=dto;
		p_center=new JPanel();
		bt_chat=new JButton("카운터 호출");
		p_center.setPreferredSize(new Dimension(300, 250));
		
		for(int i=0;i<txt.length;i++){
			la[i]=new JLabel(la_str[i],JLabel.CENTER);
			la[i].setPreferredSize(new Dimension(80, 20) );
			p_center.add(la[i]);
			txt[i]=new JTextField(18);
			txt[i].setEditable(false);
			p_center.add(txt[i]);
		}
		p_center.add(bt_chat);
		add(p_center);
		
		// 카운터 호출
		bt_chat.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				chatClient.sendMsg("@@chatStart@@");	// 카운터 채팅 시작
				chatClient.sendMsg("@@myChatStart@@");	// 내 채팅 시작
			}
		});
		
		timeThread=new Thread(){
			@Override
			public void run() {
				while(timeOn){
					try {
						Thread.sleep(1000);
						setTime();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		
		pack();
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(screen.width-300, 0);
		
		setResizable(false);
		setVisible(true);		
		setContent(dto);// 회원창 초기화		
		
		setTitle(id+" : 로그인");
	}
	
	public void setTime(){
		countSecond++;
		if(countSecond==60){//60초
			countSecond=0;
			countMinute++;
		}
		txt[2].setText(countMinute+"분"+countSecond+"초");
		
		if(countSecond%10==0){
			//System.out.println(countSecond);
			
			addPriceUpdate(id, usePrice+priceCount); //10초마다 db저장
			
			txt[3].setText(dao.getPrice(id)+"원");
			// 가격증가
			priceCount+=100;			 
		}
	}
	
	public void setContent(UserDTO dto){
		dao=new UserDAO();
		
		ArrayList<String> clientContentList=dao.getSubContent(dto);
		id=clientContentList.get(0); // 로그인 아이디 얻기.
		String start_time=clientContentList.get(1);
		
		
		String price=clientContentList.get(2);
		usePrice=(Integer.parseInt(price));		
		
		txt[0].setText(id);
		txt[1].setText(start_time);
		
		//defaultPrice=(Integer.parseInt(price));
		txt[3].setText(usePrice+"원");
		timeThread.start();	// 스레드 시작
	}
	
	public void addPriceUpdate(String id, int usePrice){
		dao.priceUpdate(id,usePrice);
	}
	
	public void updateContent(UserDTO dto){
		usePrice += dto.getPrice();
		txt[3].setText(usePrice+"원");
	}
	
	public String getId(){
		return id;
	}
	
	public void setTimeOn(boolean timeOn){
		this.timeOn = timeOn;
	}
}
