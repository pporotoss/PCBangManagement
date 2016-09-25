package teamproject.pc.main;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import teamproject.pc.server.MainServer;

public class AdminLogin extends JFrame {
	JPanel p_top, p_bottom;
	JLabel la_id, la_pwd;
	JTextField txt_id, txt_pwd;
	JButton loginBT;
	String id = "pcbang";
	String pwd = "pcbang";
	String host = "jdbc:oracle:thin:@localhost:1521";
	LoginWindow win;	
	
	public AdminLogin() {		
		
		p_top = new JPanel();
		p_bottom = new JPanel();

		la_id = new JLabel("ID");
		la_pwd = new JLabel("PWD");

		txt_id = new JTextField(21);
		txt_pwd = new JTextField(20);

		loginBT = new JButton("관리시작");

		pack();
		p_top.add(la_id);
		p_top.add(txt_id);
		p_top.add(la_pwd);
		p_top.add(txt_pwd);

		p_bottom.add(loginBT);

		add(p_top, BorderLayout.CENTER);
		add(p_bottom, BorderLayout.SOUTH);

		setTitle("Admin Login");
		setSize(280, 140);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//win = new LoginWindow();

		setVisible(true);

		loginBT.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				login();
			}
		});
	}

	public void login() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		AdminDTO dto = null;
		String sql = "select * from member";
		MainServer server = null;
		// 로그인 창 시작할때 db는 가동
		// db에서 id, pwd가 일치하는 사용자가 있을때 서버접속
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(host, id, pwd);
			if (con != null) {

				System.out.println("DB접속성공");
			}
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			/*
			 * id= txt_id.getText(); pwd= txt_pwd.getText();
			 * dto.setUsername(rs.getString("username"));
			 * dto.setUser_id(rs.getString("user_id"));
			 */
			while (rs.next()) {
				dto = new AdminDTO();
				// 레코드에서 id와 pwd정보 set
				dto.setId(rs.getString("id"));
				dto.setPwd(rs.getString("pwd"));
				// dto에 set된 레코드 데이터로 id 유효성 check
				if (txt_id.getText() == dto.getId()) {
					JOptionPane.showMessageDialog(this, "id 가올바르지않습니다.");
					// pwd 유효성 check
					if (txt_pwd.getText() == dto.getPwd()) {
						JOptionPane.showMessageDialog(this, "pwd 가올바르지않습니다.");
					}
				}
			}			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				pstmt.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		if (con != null) {
			//MainWindow main = new MainWindow();
			new MainServer();	// 메인서버 가동.
			this.dispose();
			/*win.dispose();
			win = null;*/
		}

		/*
		 * if (!txt_id.getText().equals(id)) {
		 * JOptionPane.showMessageDialog(this, "ID가 올바르지않습니다."); } else if
		 * (!txt_pwd.getText().equals(pwd)) {
		 * JOptionPane.showMessageDialog(this, "PWD가 올바르지않습니다."); } else { new
		 * MainWindow();
		 * 
		 * }
		 */
	}

	public static void main(String[] args) {
		new AdminLogin();
	}

}

/*
 * * Connection -> Server Run -> Client가 사용을 시작할때부터 ->실시간으로 시간 체크 기본 ui 완성후 메인
 * ->윈도우 호출 임시 db 작성완료 admin 계정 접속 유효성검사 완료후 메인 관리 윈도우 호출 ->서버 가동후 클라이언트 접속 까지
 * 확인됨(ip정보로 확인)
 */