package teamproject.pc.client.client;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JWindow;

import teamproject.pc.client.dao.UserDAO;
import teamproject.pc.client.dto.UserDTO;

public class ClientLoginForm extends JFrame {
	JPanel p_center;
	JLabel la_id, la_pwd, la_nonId;
	JTextField t_id, t_pwd, t_nonId;
	JButton bt_login, bt_regist, bt_nonLogin;
	ClientSubForm subForm;

	public ClientLoginForm() {
		p_center = new JPanel();
		la_id = new JLabel("���̵�", JLabel.CENTER);
		la_pwd = new JLabel("��й�ȣ", JLabel.CENTER);
		la_nonId = new JLabel("��ȸ�� ��ȣ", JLabel.CENTER);

		t_id = new JTextField(15);
		t_pwd = new JPasswordField(15);
		t_nonId = new JTextField(15);

		bt_login = new JButton("�α���");
		bt_regist = new JButton("ȸ������");
		bt_nonLogin = new JButton("��ȸ�� �α���");

		la_id.setPreferredSize(new Dimension(80, 20));
		la_pwd.setPreferredSize(new Dimension(80, 20));
		la_nonId.setPreferredSize(new Dimension(80, 20));
		// ȸ�� �α��� ��ư
		bt_login.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});

		// ȸ������ ��ư
		bt_regist.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				registOpen();
			}
		});
		// ��ȸ�� �α��� ��ư
		bt_nonLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				nonLogin();

			}
		});

		p_center.setPreferredSize(new Dimension(200, 300));
		p_center.add(la_id);
		p_center.add(t_id);
		p_center.add(la_pwd);
		p_center.add(t_pwd);
		p_center.add(bt_login);
		p_center.add(bt_regist);
		p_center.add(la_nonId);
		p_center.add(t_nonId);
		p_center.add(bt_nonLogin);

		add(p_center);

		setTitle("�α���");
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setVisible(true);
	}
	
	// ��ȸ�� �α���
	public void nonLogin() {
		UserDTO dto = new UserDTO();
		dto.setId(t_nonId.getText());
		dto.setPwd("project");
		dto.setStartTime(openTime());
		UserDAO dao = new UserDAO();
		int result = dao.checkLogin(dto);
		if (result == 0) {
			JOptionPane.showMessageDialog(this, "���� ��ȸ����ȣ�Դϴ�");
			return;
		} else {
			JOptionPane.showMessageDialog(this, "�α��� ����");
			t_nonId.setText("");
		}
	}
	
	// ȸ���α���
	public void login() {
		UserDTO dto = new UserDTO();
		dto.setId(t_id.getText());
		dto.setPwd(t_pwd.getText());
		dto.setStartTime(openTime());
		dto.setPrice(1000);// �⺻��� �Է�.

		UserDAO dao = new UserDAO();
		int result = dao.checkLogin(dto);
		if (result == 0) {
			JOptionPane.showMessageDialog(this, "���̵� ��й�ȣ Ʋ�Ƚ��ϴ�");
			return;
		} else {
			//�α��� �ߺ����� üũ
			String connection=dao.getConnection(t_id.getText().trim());
			if(connection.equals("true")){
				JOptionPane.showMessageDialog(getParent(), "�̹� �α��� �Ǿ��ֽ��ϴ�");
				return;
			}
			
			JOptionPane.showMessageDialog(this, "�α��� ����");
			
			dao.priceUpdate(dto.getId(),1000,dto.getStartTime(),"true"); //�α��ν� �⺻��� �߰� db����
			ChatClient chatClient =  new ChatClient(t_id.getText(), this);	// Ŭ���̾�Ʈ�� ����.
			subForm = new ClientSubForm(dto, chatClient); // Ŭ���̾�Ʈ ���� �� ����
			
			this.dispose();
		}

	}
	//�α��ν� ���۽ð�, �α��εǾ��ִ��� true false �ֱ�
	public String openTime(){ 
		Date date=new Date();
		SimpleDateFormat fm=new SimpleDateFormat("yyyy.MM.dd E hh:mm a");
		return fm.format(date);		
	}

	public void registOpen() {
		new ClientRegistForm();
	}
	
	public ClientSubForm getSubForm(){
		
		return subForm;
	}

	public static void main(String[] args) {
		new ClientLoginForm();
	}
}
