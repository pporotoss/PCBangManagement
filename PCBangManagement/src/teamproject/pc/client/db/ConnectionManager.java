package teamproject.pc.client.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectionManager {
	public static final String CON_ADDR = "localhost";
	
	private static ConnectionManager instance;

	String host = "jdbc:oracle:thin:@localhost:1521";
	String id = "pcbang";
	String pwd = "pcbang";

	public static ConnectionManager getInstane() {
		if (instance == null) {
			instance = new ConnectionManager();
		}
		return instance;
	}

	public Connection getConnection() {
		Connection con = null;
		try {
			con = DriverManager.getConnection(host, id, pwd);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return con;
	}

	public void CloseConnection(Connection con, PreparedStatement pstmt) {
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void CloseConnection(Connection con, PreparedStatement pstmt, ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
