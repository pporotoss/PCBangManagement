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
		la_id = new JLabel("아이디", JLabel.CENTER);
		la_pwd = new JLabel("비밀번호", JLabel.CENTER);
		la_nonId = new JLabel("비회원 번호", JLabel.CENTER);

		t_id = new JTextField(15);
		t_pwd = new JPasswordField(15);
		t_nonId = new JTextField(15);

		bt_login = new JButton("로그인");
		bt_regist = new JButton("회원가입");
		bt_nonLogin = new JButton("비회원 로그인");

		la_id.setPreferredSize(new Dimension(80, 20));
		la_pwd.setPreferredSize(new Dimension(80, 20));
		la_nonId.setPreferredSize(new Dimension(80, 20));
		// 회원 로그인 버튼
		bt_login.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});

		// 회원가입 버튼
		bt_regist.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				registOpen();
			}
		});
		// 비회원 로그인 버튼
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

		setTitle("로그인");
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setVisible(true);
	}
	
	// 비회원 로그인
	public void nonLogin() {
		UserDTO dto = new UserDTO();
		dto.setId(t_nonId.getText());
		dto.setPwd("project");
		dto.setStartTime(openTime());
		UserDAO dao = new UserDAO();
		int result = dao.checkLogin(dto);
		if (result == 0) {
			JOptionPane.showMessageDialog(this, "없는 비회원번호입니다");
			return;
		} else {
			JOptionPane.showMessageDialog(this, "로그인 성공");
			t_nonId.setText("");
		}
	}
	
	// 회원로그인
	public void login() {
		UserDTO dto = new UserDTO();
		dto.setId(t_id.getText());
		dto.setPwd(t_pwd.getText());
		dto.setStartTime(openTime());
		dto.setPrice(1000);// 기본요금 입력.

		UserDAO dao = new UserDAO();
		int result = dao.checkLogin(dto);
		if (result == 0) {
			JOptionPane.showMessageDialog(this, "아이디 비밀번호 틀렸습니다");
			return;
		} else {
			//로그인 중복접속 체크
			String connection=dao.getConnection(t_id.getText().trim());
			if(connection.equals("true")){
				JOptionPane.showMessageDialog(getParent(), "이미 로그인 되어있습니다");
				return;
			}
			
			JOptionPane.showMessageDialog(this, "로그인 성공");
			
			dao.priceUpdate(dto.getId(),1000,dto.getStartTime(),"true"); //로그인시 기본요금 추가 db연동
			ChatClient chatClient =  new ChatClient(t_id.getText(), this);	// 클라이언트용 서버.
			subForm = new ClientSubForm(dto, chatClient); // 클라이언트 서브 폼 생성
			
			this.dispose();
		}

	}
	//로그인시 시작시간, 로그인되어있는지 true false 넣기
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
