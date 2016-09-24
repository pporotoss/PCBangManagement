package teamproject.pc.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Timer;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import teamproject.pc.client.manager.ManagerClient;
import teamproject.pc.main.capture.ImgServer;
import teamproject.pc.main.dao.MainDAO;
import teamproject.pc.main.member.MemberList;
import teamproject.pc.main.stock.pay.StockPayForm;
import teamproject.pc.product.StockManagement;
import teamproject.pc.server.MainServer;

public class MainWindow extends JFrame {
	JPanel top_pan, center_pan, bottom_pan, inner_pan;
	String[] top_txt = { "상품계산", "재고관리", "회원관리","화면캡쳐"};
	String[] label_txt = {"아이디", "시작시간", "이용시간", "이용요금" };
	JButton[] top_btn = new JButton[top_txt.length];
	JButton bottom_btn = new JButton("계산");	
	JCheckBox[] center_chk = new JCheckBox[20];
	JPanel[] pc_pan = new JPanel[center_chk.length];
	JLabel[][] pc_label = new JLabel[center_chk.length][label_txt.length];
	Timer timer;
	MainDAO dao;
	MemberList member;
	String[] ipList = {"70.12.109.53","70.12.109.54","70.12.109.61"};
	ManagerClient manager;
	ArrayList<String> idList = new ArrayList<String>();
	ArrayList<String> payList = new ArrayList<String>();
	ArrayList<TimeThread> timeList = new ArrayList<TimeThread>();
	
	boolean onStock, onPay, onMember;
	MainServer mainServer;
		

	public MainWindow(MainServer mainServer) {
		this.mainServer = mainServer;
		
		manager = mainServer.getManager();
		
		dao = new MainDAO();		
		
		top_pan = new JPanel(new GridLayout(1, 4));
		
		center_pan = new JPanel(new GridLayout(5, 4));
		
		bottom_pan = new JPanel();
		

		// pc표시되는 패널
		add(center_pan);
		center_pan.setBorder(new LineBorder(Color.RED));
		
		for (int i = 0; i < center_chk.length; i++) {
			
			pc_pan[i] = new JPanel();			
			pc_pan[i].setBorder(new LineBorder(Color.RED));
			pc_pan[i].setLayout(new GridLayout(5, 1));
			center_chk[i]= new JCheckBox("체크");
			center_chk[i].setEnabled(false);
			center_pan.add(pc_pan[i]);
			for (int j = 0; j < label_txt.length; j++) {
				pc_label[i][j]=new JLabel(label_txt[j]);
				pc_pan[i].add(pc_label[i][j]);				
			}
			JPanel p_chk = new JPanel();
			p_chk.add(center_chk[i]);
			pc_pan[i].add(p_chk);
			
		}	

		// 상단 버튼
		for (int i = 0; i < top_btn.length; i++) {
			top_btn[i] = new JButton(top_txt[i]);
			top_btn[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JButton obj = (JButton) e.getSource();
					if(obj.equals(top_btn[0])){	// 상품계산
						System.out.println(onPay);
						if(!onPay){
							openPay();							
						}
					}else if(obj.equals(top_btn[1])){	// 재고관리
						if(!onStock){
							openStock();
						}
					}else if(obj.equals(top_btn[2])){	// 회원관리
						if(!onMember){
							openMember();							
						}
					}else if(obj.equals(top_btn[3])){	// 화면캡쳐						
						openCapture();
					}
				}
			});
			top_pan.add(top_btn[i]);
		}
		
		// 계산버튼 이벤트
		bottom_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectCount = 0;
				String select = "";
				int total = 0;
				for(int i = 0; i < center_chk.length; i++){
					if(center_chk[i].isSelected()){
						String tmp = pc_label[i][0].getText();						
						String selectedId = tmp.substring(tmp.lastIndexOf(" : ")+3);
						total += calc(selectedId);
						select += selectedId+" ";
						idList.add(selectedId);
						payList.add(ipList[i]);
						selectCount++;
					}
				}
				if(selectCount == 0){
					JOptionPane.showMessageDialog(getParent(), "자리를 선택해 주세요.");					
					return;
				}
				selectCount = 0;
				int sel = JOptionPane.showConfirmDialog(getParent(), select+"\n"+total, "계산",JOptionPane.YES_NO_OPTION);
				if(sel == JOptionPane.OK_OPTION){
					for(int i = 0; i < idList.size(); i++){
						String selId = idList.get(i); 
						String ip = null;
						mainServer.getManager().sendMsg("@@shutDown@@"+payList.get(i));
						for(int t = 0; t < timeList.size(); t++){
							TimeThread tt = timeList.get(t);
							if(tt.user_id.equals(selId)){
								tt.setOnTime(false);
								timeList.remove(tt);								
							}
						}
						idList.remove(i);
						payList.remove(i);
						
						for(int l = 0; l < pc_label.length; l++){
							String tmp = pc_label[l][0].getText();
							String labelId = tmp.substring(tmp.lastIndexOf(" : ")+3);
							if(selId.equals(labelId)){
								pc_label[l][0].setText("아이디");
								pc_label[l][1].setText("시작시간");
								pc_label[l][2].setText("이용시간");
								pc_label[l][3].setText("이용요금");
							}
						}
					}
					
				}
				
			}
		});

		bottom_pan.add(bottom_btn);		
				
		add(top_pan, BorderLayout.NORTH);
		add(center_pan, BorderLayout.CENTER);
		add(bottom_pan, BorderLayout.SOUTH);


		setTitle("Main");
		setSize(800, 800);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	
	public int calc(String id){
		int cal = dao.getPrice(id);
		return cal;
	}
	
	public String getStartTime(String user_id){
		String time = null;
		time = dao.getTime(user_id);
		/*Date date=new Date();
		SimpleDateFormat fm=new SimpleDateFormat("yyyy.MM.dd E hh:mm a");*/
		return time;
	}
	
	public void init(int num, String user_id){
		pc_label[num][0].setText("아이디 : "+user_id);
		center_chk[num].setEnabled(true);
		String time = getStartTime(user_id);
		pc_label[num][1].setText("시작 : "+time);
		
		TimeThread tt = new TimeThread(this, num, user_id);
		timeList.add(tt);
		tt.start();		
	}
	
	// 이용시간
	public void setUseTime(int num, int hour, int min, int sec){
		String h = Integer.toString(hour);
		String m = Integer.toString(min);
		String s = Integer.toString(sec);
		pc_label[num][2].setText("이용시간 : "+h+" : "+m+" : "+s);
	}
	
	public void setUsePrice(int num, String user_id){
		pc_label[num][3].setText("이용요금 : "+calc(user_id));
	}
	
	// 재고관리 열기
	public void openStock(){
		new StockManagement(this);
		onStock = true;
	}
	
	// 상품 구매 열기
	public void openPay(){
		int selectCount = 0;
		String id = null;
		String ip = null;
		int selNum = -1;
		for(int i = 0; i < center_chk.length; i++){
			if(center_chk[i].isSelected()){
				selectCount++;
				String tmp = pc_label[i][0].getText();
				
				id = tmp.substring(tmp.lastIndexOf(" : ")+3);
				ip = ipList[i];
				selNum = i;
			}
		}
		if(selectCount > 1){
			JOptionPane.showMessageDialog(getParent(), "한명만 선택해 주세요.");
			selectCount = 0;
			return;
		}else if(selectCount == 0){
			JOptionPane.showMessageDialog(getParent(), "자리를 선택해 주세요.");
			selectCount = 0;
			return;
		}		
		new  StockPayForm(this, id, ip, selNum);
		onPay = true;
	}
	
	// 회원관리
	public void openMember(){
		if(!onMember){
			member = new MemberList(this);
			onMember = true;
		}
	}
	
	// 화면캡쳐
	public void openCapture(){
		int selectCount = 0;
		String ip = null;
		for(int i = 0; i < center_chk.length; i++){
			if(center_chk[i].isSelected()){
				selectCount++;
				ip = ipList[i];
			}
		}
		if(selectCount > 1){
			JOptionPane.showMessageDialog(getParent(), "한명만 선택해 주세요.");
			selectCount = 0;
			return;
		}else if(selectCount == 0){
			JOptionPane.showMessageDialog(getParent(), "자리를 선택해 주세요.");
			selectCount = 0;
			return;
		}
		new ImgServer();
		mainServer.getManager().sendMsg("@@capture@@"+ip);
	}
	
	public MainServer getMainServer(){
		return mainServer;
	}
	
	// setter
	public void setOnStock(boolean onStock) {
		this.onStock = onStock;
	}


	public void setOnPay(boolean onPay) {
		this.onPay = onPay;
	}


	public void setOnMember(boolean onMember) {
		this.onMember = onMember;
	}
}
