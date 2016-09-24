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
	String[] la_str={"ID","���۽ð�","�̿�ð�","�̿���"};
	JButton bt_chat;
	UserDTO dto;
	UserDAO dao;
	Thread timeThread;
	int countMinute=0; //exit������ �ʱ�ȭ�������
	int countSecond=0;
	int priceCount=100;
	
	int usePrice;
	int defaultPrice=1000; //�⺻���
	
	ChatClient chatClient;	
	String id;
	
	boolean timeOn = true;
	
	public ClientSubForm(UserDTO dto, ChatClient chatClient){
		
		this.chatClient = chatClient;
				
		this.dto=dto;
		p_center=new JPanel();
		bt_chat=new JButton("ī���� ȣ��");
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
		
		// ī���� ȣ��
		bt_chat.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				chatClient.sendMsg("@@chatStart@@");	// ī���� ä�� ����
				chatClient.sendMsg("@@myChatStart@@");	// �� ä�� ����
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
		setContent(dto);// ȸ��â �ʱ�ȭ		
		
		setTitle(id+" : �α���");
	}
	
	public void setTime(){
		countSecond++;
		if(countSecond==60){//60��
			countSecond=0;
			countMinute++;
		}
		txt[2].setText(countMinute+"��"+countSecond+"��");
		
		if(countSecond%10==0){
			//System.out.println(countSecond);
			
			addPriceUpdate(id, usePrice+priceCount); //10�ʸ��� db����
			
			txt[3].setText(dao.getPrice(id)+"��");
			// ��������
			priceCount+=100;			 
		}
	}
	
	public void setContent(UserDTO dto){
		dao=new UserDAO();
		
		ArrayList<String> clientContentList=dao.getSubContent(dto);
		id=clientContentList.get(0); // �α��� ���̵� ���.
		String start_time=clientContentList.get(1);
		
		
		String price=clientContentList.get(2);
		usePrice=(Integer.parseInt(price));		
		
		txt[0].setText(id);
		txt[1].setText(start_time);
		
		//defaultPrice=(Integer.parseInt(price));
		txt[3].setText(usePrice+"��");
		timeThread.start();	// ������ ����
	}
	
	public void addPriceUpdate(String id, int usePrice){
		dao.priceUpdate(id,usePrice);
	}
	
	public void updateContent(UserDTO dto){
		usePrice += dto.getPrice();
		txt[3].setText(usePrice+"��");
	}
	
	public String getId(){
		return id;
	}
	
	public void setTimeOn(boolean timeOn){
		this.timeOn = timeOn;
	}
}
