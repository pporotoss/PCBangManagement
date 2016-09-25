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

		loginBT = new JButton("��������");

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
		// �α��� â �����Ҷ� db�� ����
		// db���� id, pwd�� ��ġ�ϴ� ����ڰ� ������ ��������
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(host, id, pwd);
			if (con != null) {

				System.out.println("DB���Ӽ���");
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
				// ���ڵ忡�� id�� pwd���� set
				dto.setId(rs.getString("id"));
				dto.setPwd(rs.getString("pwd"));
				// dto�� set�� ���ڵ� �����ͷ� id ��ȿ�� check
				if (txt_id.getText() == dto.getId()) {
					JOptionPane.showMessageDialog(this, "id ���ùٸ����ʽ��ϴ�.");
					// pwd ��ȿ�� check
					if (txt_pwd.getText() == dto.getPwd()) {
						JOptionPane.showMessageDialog(this, "pwd ���ùٸ����ʽ��ϴ�.");
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
			new MainServer();	// ���μ��� ����.
			this.dispose();
			/*win.dispose();
			win = null;*/
		}

		/*
		 * if (!txt_id.getText().equals(id)) {
		 * JOptionPane.showMessageDialog(this, "ID�� �ùٸ����ʽ��ϴ�."); } else if
		 * (!txt_pwd.getText().equals(pwd)) {
		 * JOptionPane.showMessageDialog(this, "PWD�� �ùٸ����ʽ��ϴ�."); } else { new
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
 * * Connection -> Server Run -> Client�� ����� �����Ҷ����� ->�ǽð����� �ð� üũ �⺻ ui �ϼ��� ����
 * ->������ ȣ�� �ӽ� db �ۼ��Ϸ� admin ���� ���� ��ȿ���˻� �Ϸ��� ���� ���� ������ ȣ�� ->���� ������ Ŭ���̾�Ʈ ���� ����
 * Ȯ�ε�(ip������ Ȯ��)
 */