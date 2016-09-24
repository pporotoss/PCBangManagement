package teamproject.pc.client.client;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import teamproject.pc.client.dao.UserDAO;
import teamproject.pc.client.dto.UserDTO;

public class ClientRegistForm extends JFrame {
	JPanel p_center;
	JPanel p_south;
	JLabel la_header;
	JLabel[] laArray=new JLabel[5];
	String[] la_str={"���̵�","��й�ȣ","���Ȯ��","�̸�","����ó"};
	JTextField[] txtArray=new JTextField[5];
	JButton[] btArray=new JButton[2];
	String[] bt_str={"����","���"};
	int price=0;
	UserDAO dao;
	
	public ClientRegistForm(){
		la_header=new JLabel("ȸ������",JLabel.CENTER);
		p_center=new JPanel();
		p_south=new JPanel();
		la_header.setFont(new Font("", Font.BOLD, 40));
		p_center.setPreferredSize(new Dimension(300, 400));
		
		
		for(int i=0;i<laArray.length;i++){
			laArray[i]=new JLabel(la_str[i]);
			laArray[i].setPreferredSize(new Dimension(80, 20));
			if(i==1||i==2){
				txtArray[i]=new JPasswordField(15);
			}else{
				txtArray[i]=new JTextField(15);
			}
			p_center.add(laArray[i]);
			p_center.add(txtArray[i]);
		}
		
		for(int i=0;i<btArray.length;i++){
			btArray[i]=new JButton(bt_str[i]);
			p_south.add(btArray[i]);
		}
		//���Թ�ư
		btArray[0].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				regist();				
			}
		});
		
		//��ҹ�ư
		btArray[1].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);				
			}
		});
		
		setTitle("ȸ������");
		add(la_header,"North");
		add(p_center);
		add(p_south,"South");
		
		
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
	}
	
	public void regist(){
		String id=txtArray[0].getText();
		String pwd=txtArray[1].getText();
		String name=txtArray[3].getText();
		String phone=txtArray[4].getText();
		
		if(pwd.equals(txtArray[2].getText())){
			pwd=txtArray[1].getText();
		}else{
			JOptionPane.showMessageDialog(this, "��й�ȣ�� ��ġ���� �ʽ��ϴ�");
			return;
		}
		if(id.equals("")){
			JOptionPane.showMessageDialog(this, "���̵� �Է��ϼ���");
			return;
		}else if(pwd.equals("")){
			JOptionPane.showMessageDialog(this, "��й�ȣ�� �Է��ϼ���");
			return;
		}else if(name.equals("")){
			JOptionPane.showMessageDialog(this, "�̸��� �Է��ϼ���");
			return;
		}else if(phone.equals("")){
			JOptionPane.showMessageDialog(this, "����ó�� �Է��ϼ���");
			return;
		}
		
		UserDTO dto=new UserDTO();
		dto.setId(id);
		dto.setPwd(pwd);
		dto.setName(name);
		dto.setPhone(phone);
		dto.setPrice(price);
		
		dao=new UserDAO();
		//1�̸� ���� 0�̸� ����
		int result=dao.insert(dto);
		if(result==0){
			JOptionPane.showMessageDialog(this, "��� ����");
			return;
		}else if(result==1){
			JOptionPane.showMessageDialog(this, "��� ����");
			for(int i=0;i<txtArray.length;i++){
				txtArray[i].setText("");
			}
		}else if(result==2){
			JOptionPane.showMessageDialog(this, "���̵��ߺ��˴ϴ�");
			return;
		}
		
	}
	

}
